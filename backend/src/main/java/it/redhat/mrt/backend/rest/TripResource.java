package it.redhat.mrt.backend.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.redhat.mrt.backend.model.Trip;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/trips")
public class TripResource {
    @Inject TripService service;

    @GET
    public List<Trip> list() {
        return service.list();
    }

    @POST
    public List<Trip> add(Trip trip) {
        service.add(trip);
        return list();
    }

}