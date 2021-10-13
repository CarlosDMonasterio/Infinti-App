package org.ih.service.rest;

import org.ih.common.logging.Logger;
import org.ih.dto.IncidentReport;
import org.ih.hygiene.HygieneType;
import org.ih.incident.Incidents;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/incidents")
public class IncidentResource extends RestResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(IncidentReport report) {
        String userId = getUserId();
        Logger.info(userId + ": adding incident report");
        Incidents incidents = new Incidents(userId);
        return super.respond(incidents.createReport(report));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listReports(@PathParam("type") HygieneType type,
                                @DefaultValue("15") @QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("start") int start,
                                @DefaultValue("false") @QueryParam("asc") boolean asc,
                                @DefaultValue("id") @QueryParam("sort") String sort,
                                @QueryParam("filterText") String filter) {
        String userId = getUserId();
        Incidents incidents = new Incidents(userId);
        return super.respond(incidents.list(start, limit, asc, sort));
    }
}
