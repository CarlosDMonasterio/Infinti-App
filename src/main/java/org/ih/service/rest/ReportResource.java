package org.ih.service.rest;

import org.ih.dto.Hygiene;
import org.ih.hygiene.HygieneReports;
import org.ih.hygiene.HygieneType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/reports")
public class ReportResource extends RestResource {

    @POST
    @Path("/hygiene")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReport(Hygiene hygiene) {
        String userId = getUserId();
        HygieneReports reports = new HygieneReports(userId);
        return super.respond(reports.create(hygiene));
    }

    @GET
    @Path("/hygiene/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listReports(@PathParam("type") HygieneType type,
                                @DefaultValue("15") @QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("start") int start,
                                @DefaultValue("false") @QueryParam("asc") boolean asc,
                                @DefaultValue("id") @QueryParam("sort") String sort,
                                @QueryParam("filterText") String filter) {
        String userId = getUserId();
        HygieneReports reports = new HygieneReports(userId);
        return super.respond(reports.list(type, limit, start, asc, sort, filter));
    }
}
