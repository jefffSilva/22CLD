package br.com.iwe.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;


import br.com.iwe.model.Trips;

public class TripsRepository {

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();
	
	public Trips save(final Trips trips) {
		mapper.save(trips);
		return trips;
	}

	public List<Trips> findByCity(final String country, final String city) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(country));
		eav.put(":val2", new AttributeValue().withS(city));

		final DynamoDBQueryExpression<Trips> queryExpression = new DynamoDBQueryExpression<Trips>()
				.withIndexName("cityIndex").withConsistentRead(false)
				.withKeyConditionExpression("country = :val1 and city=:val2").withExpressionAttributeValues(eav);

		final List<Trips> viagens = mapper.query(Trips.class, queryExpression);

		return viagens;
	}
	
	public List<Trips> findByCountry(final String country) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(country));
		

		final DynamoDBQueryExpression<Trips> queryExpression = new DynamoDBQueryExpression<Trips>()
				.withIndexName("cityIndex").withConsistentRead(false)
				.withKeyConditionExpression("country = :val1").withExpressionAttributeValues(eav);

		final List<Trips> viagens = mapper.query(Trips.class, queryExpression);

		return viagens;
	}
	
}