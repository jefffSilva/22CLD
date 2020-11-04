package br.com.iwe.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.iwe.dao.TripsRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Trips;

public class GetTripsRecordsByCountry implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripsRepository repository = new TripsRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String country = request.getPathParameters().get("country");
	

		context.getLogger().log("Searching for registered Country for " + country );

		final List<Trips> viagens = this.repository.findByCountry(country);

		if (viagens == null || viagens.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}

		return HandlerResponse.builder().setStatusCode(200).setObjectBody(viagens).build();
	}
}
