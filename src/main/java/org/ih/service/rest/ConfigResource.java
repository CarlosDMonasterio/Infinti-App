package org.ih.service.rest;

import org.ih.config.Settings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/config")
public class ConfigResource extends RestResource {

    @GET
    @Path("/init")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInitialValues() {
        return super.respond(new Settings().getInitialValues());
    }

    @GET
    @Path("/site")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSiteSettings() {
        Settings settings = new Settings();
        if (!settings.hasDataDirectory()) {
            return super.respond(Response.Status.SERVICE_UNAVAILABLE);
        }
        return super.respond(settings.getSiteSettings());
    }
}
