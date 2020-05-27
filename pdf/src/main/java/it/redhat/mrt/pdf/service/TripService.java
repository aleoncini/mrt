package it.redhat.mrt.pdf.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import it.redhat.mrt.pdf.model.Trip;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/trips")
@RegisterRestClient
public interface TripService {
    
    @GET
    @Path("/{rhid}/{year}/{month}")
    @Produces("application/json")
    Set<Trip> get(@PathParam String rhid, @PathParam int year, @PathParam int month);

}