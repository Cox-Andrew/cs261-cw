package com.moodlysis.moodbe.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/v0/events/{eventID}")
public class EventSingleEndpoint {
	
	@GET
	@Produces("application/json")
	public String getEventRequest(@PathParam("eventID") String eventIDString) {
		
		
		
		return null;
	}
}
