package br.com.iwe.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.iwe.dao.TripsRepository;
import br.com.iwe.model.HandlerRequest;
import br.com.iwe.model.HandlerResponse;
import br.com.iwe.model.Trips;

public class GetTripsRecordsByCity implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripsRepository repository = new TripsRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String country = request.getPathParameters().get("country");
		final String city = request.getQueryStringParameters().get("city");

		context.getLogger().log("Searching for registered cities for " + country + " and city equals " + city);

		final List<Trips> viagens = this.repository.findByCity(country, city);

		if (viagens == null || viagens.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}

		return HandlerResponse.builder().setStatusCode(200).setObjectBody(viagens).build();
	}
}
