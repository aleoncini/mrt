package it.redhat.mrt.backend.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.redhat.mrt.backend.model.Location;
import it.redhat.mrt.backend.model.LocationService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/locations")
public class LocationResource {
    @Inject LocationService service;

    @GET
    public List<Location> list() {
        return service.list();
    }

    @POST
    public List<Location> add(Location location) {
        service.add(location);
        return list();
    }

}