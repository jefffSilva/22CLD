## Team 

22CLD - 2020<br>
Course: Cloud Computing<br>

## People

Renan Miranda<br>
Jeferson Mendes<br>
Thiago Cardoso<br>
Hedipo Silva<br>
Vinicius Sanfilippo<br>

## Project 
**Microservices and Severless** <br><br>


## AWS SAM Application for Managing Trips Data Lake


This project was created by group of course Cloud Computing<br>
The example projet used was https://github.com/iworks-education/study-datalake<br>


This is a sample application to demonstrate how to build an application on AWS Serverless Envinronment using the
AWS SAM, Amazon API Gateway, AWS Lambda and Amazon DynamoDB.<br>

It also uses the DynamoDBMapper ORM structure to map trip items in a DynamoDB table to a RESTful API for managing trips.<br>


## Requirements

* AWS CLI already configured with at least PowerUser permission
* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker installed](https://www.docker.com/community-edition)
* [Maven](https://maven.apache.org/install.html)
* [SAM CLI](https://github.com/awslabs/aws-sam-cli)
* [Python 3](https://docs.python.org/3/)


## Setup process

### Installing dependencies

We use `maven` to install our dependencies and package our application into a JAR file:


```bash
mvn install
```

### Local development

**Invoking function locally through local API Gateway**

This step will create a docker container that emulate a DynamoDB Local

**Pay Attention if you will have a problem with access table, you can use docker ip in a src/test/resources/test_environment_linux.json**<br>

1. Start DynamoDB Local in a Docker container. `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`


**You'll make a new table from dynamodb for recive new data from API REST** <br>

```bash
aws dynamodb create-table --table-name trips --attribute-definitions AttributeName=country,AttributeType=S AttributeName=city,AttributeType=S  --key-schema AttributeName=country,KeyType=HASH AttributeName=city,KeyType=RANGE --local-secondary-indexes 'IndexName=cityIndex,KeySchema=[{AttributeName=country,KeyType=HASH},{AttributeName=city,KeyType=RANGE}],Projection={ProjectionType=ALL}' --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000 --profile fiap
```



If the table already exist, you can delete: `aws dynamodb delete-table --table-name trips --endpoint-url http://localhost:8000`

3. Start the SAM local API.
 - On a Mac: `sam local start-api --env-vars src/test/resources/test_environment_mac.json`
 - On Windows: `sam local start-api --env-vars src/test/resources/test_environment_windows.json`
 - On Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json`
 

OBS:  If you already have the container locally (in your case the java8), then you can use --skip-pull-image to remove the download


If the previous command ran successfully you should now be able to hit the following local endpoint to
invoke the functions rooted at `http://localhost:3000/trips/`.

you can use postman and curl client to send data for `http://localhost:3000/trips/` for it. 

`curl -H "content-Type: application/json" -X POST -d \
{ \
    "country": "Mexico", \
    "city": "Cancon"  \
    "date": "2020-11-01 \
    "reason": "work" \
} \ 
http://localhost:3000/trips/`

**After you can use GET method for bring you trip by country**

`curl http://localhost:3000/trips/Mexico` 



It shoud return 404. Now you can explore all endpoints, use the src/test/resources/trips DataLake.postman_collection.json to import a API Rest Collection into Postman.


**SAM CLI** is used to emulate both Lambda and API Gateway locally and uses our `template.yaml` to
understand how to bootstrap this environment (runtime, where the source code is, etc.) - The
following excerpt is what the CLI will read in order to initialize an API and its routes:


## Packaging and deployment


AWS Lambda Java runtime accepts either a zip file or a standalone JAR file - We use the latter in
this example. SAM will use `CodeUri` property to know where to look up for both application and
dependencies:


Firstly, we need a `S3 bucket` where we can upload our Lambda functions packaged as ZIP before we
deploy anything - If you don't have a S3 bucket to store code artifacts then this is a good time to
create one:


```bash
export BUCKET_NAME=my_cool_new_bucket
aws s3 mb s3://$BUCKET_NAME
```

Next, run the following command to package our Lambda function to S3:


```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $BUCKET_NAME
    --profile fiap
```


Next, the following command will create a Cloudformation Stack and deploy your SAM resources.


```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name TRIP \
    --capabilities CAPABILITY_IAM \
    --profile fiap
```

> **See [Serverless Application Model (SAM) HOWTO Guide](https://github.com/awslabs/serverless-application-model/blob/master/HOWTO.md) for more details in how to get started.**

After deployment is complete you can run the following command to retrieve the API Gateway Endpoint URL:


```bash
aws cloudformation describe-stacks \
    --stack-name sam-orderHandler \
    --query 'Stacks[].Outputs'
```

# Appendix

## AWS CLI commands

AWS CLI commands to package, deploy and describe outputs defined within the cloudformation stack:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket REPLACE_THIS_WITH_YOUR_S3_BUCKET_NAME

sam deploy \
    --template-file packaged.yaml \
    --stack-name sam-orderHandler \
    --capabilities CAPABILITY_IAM \
    --parameter-overrides MyParameterSample=MySampleValue

aws cloudformation describe-stacks \
    --stack-name sam-orderHandler --query 'Stacks[].Outputs'
```


