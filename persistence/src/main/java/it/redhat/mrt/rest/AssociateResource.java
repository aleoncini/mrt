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
import it.redhat.mrt.model.Associate;

@Path("/associates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AssociateResource {

    @GET
    public List<PanacheMongoEntityBase> list() {
        return Associate.listAll();
    }

    @GET
    @Path("/{id}")
    public Associate get(@PathParam("id") String id) {
        return Associate.findById(new ObjectId(id));
    }

    @GET
    @Path("/rhid/{rhid}")
    public Associate getByRhid(@PathParam("rhid") String rhid) {
        return Associate.findByRhid(rhid);
    }

    @POST
    public Associate create(Associate associate) {
        associate.persist();
        return associate;
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, Associate associate) {
        associate.update();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        Associate associate = Associate.findById(new ObjectId(id));
        if(associate == null) {
            throw new NotFoundException();
        }
        associate.delete();
    }

    @GET
    @Path("/count")
    public Long count() {
        return Associate.count();
    }    

}
