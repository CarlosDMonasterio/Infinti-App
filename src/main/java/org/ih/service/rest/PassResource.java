package org.ih.service.rest;

import org.ih.pass.Passes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST resource for daily passes
 *
 * @author Hector Plahar
 */
@Path("passes")
public class PassResource extends RestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPass(@QueryParam("userId") String id) {
        String userId = requireUserId();
        Passes passes = new Passes(userId);
        return super.respond(passes.get(id));
    }
}
