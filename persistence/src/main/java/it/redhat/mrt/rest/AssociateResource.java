package it.redhat.mrt.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;

import it.redhat.mrt.model.Associate;

@Path("/associate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AssociateResource {

    @Inject
    JsonWebToken token;
 
    @GET
    @Path("/all")
    public List<Associate> list() {
        return Associate.listAll();
    }

    @GET
    public Associate get() {
        return Associate.findByUserid(token.getClaim("sub"));
    }

    @GET
    @Path("/{userid}")
    public Associate get(@PathParam("userid") String userid) {
        return Associate.findByUserid(userid);
    }

    @POST
    public Associate create(Associate associate) {
        associate.persist();
        return associate;
    }

    @PUT
    public void update(Associate associate) {
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
