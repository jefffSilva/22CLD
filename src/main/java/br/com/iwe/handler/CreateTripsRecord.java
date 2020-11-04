package br.com.iwe.handler;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.iwe.dao.TripsRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Trips;


public class CreateTripsRecord implements RequestHandler<HandlerRequest, HandlerResponse> {
	
	private final TripsRepository repository = new TripsRepository();

	@Override
	public HandlerResponse handleRequest(final HandlerRequest request, final Context context) {

		Trips trips = null;
		try {
			trips = new ObjectMapper().readValue(request.getBody(), Trips.class);
		} catch (IOException e) {
			return HandlerResponse.builder().setStatusCode(400).setRawBody("There is a error in your trip!").build();
		}
		context.getLogger().log("Creating a new trip record  " + trips.getCountry());
		final Trips tripsRecorded = repository.save(trips);
		return HandlerResponse.builder().setStatusCode(201).setObjectBody(tripsRecorded).build();
	}
}
