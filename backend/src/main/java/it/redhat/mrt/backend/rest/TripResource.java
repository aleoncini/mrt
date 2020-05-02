package it.redhat.mrt.backend.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.redhat.mrt.backend.model.Trip;
import it.redhat.mrt.backend.model.TripService;

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
    public Trip add(Trip trip) {
        service.add(trip);
        return trip;
    }

    @GET
    @Path("{rhid}/{year}/{month}")
    public List<Trip> get(@PathParam("rhid") String rhid, @PathParam("year") int year, @PathParam("month") int month) {
        return service.monthList(rhid, year, month);
    }

}