package it.redhat.mrt.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.NotFoundException;

import org.bson.types.ObjectId;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import it.redhat.mrt.model.Location;

@Path("/locations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocationResource {

    @GET
    public List<PanacheMongoEntityBase> list() {
        return Location.listAll();
    }

    @GET
    @Path("/{id}")
    public Location get(@PathParam("id") String id) {
        return Location.findById(new ObjectId(id));
    }

    @POST
    public Location create(Location location) {
        location.persist();
        return location;
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, Location location) {
        location.update();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        Location location = Location.findById(new ObjectId(id));
        if(location == null) {
            throw new NotFoundException();
        }
        location.delete();
    }

    @GET
    @Path("/search/{name}")
    public Location search(@PathParam("name") String name) {
        return Location.findByName(name);
    }

    @GET
    @Path("/range/{distance}")
    public List<PanacheMongoEntityBase> searchByRange(@PathParam("distance") int distance) {
        return Location.find("distance <= ?1", distance).list();
    }

    @GET
    @Path("/count")
    public Long count() {
        return Location.count();
    }    
}
