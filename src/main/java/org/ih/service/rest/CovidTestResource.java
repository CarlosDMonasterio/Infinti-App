package org.ih.service.rest;

import org.ih.common.logging.Logger;
import org.ih.dto.LabTest;
import org.ih.hygiene.HygieneType;
import org.ih.labtest.LabTests;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tests")
public class CovidTestResource extends RestResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(LabTest labTest) {
        String userId = requireUserId();
        Logger.info(userId + ": creating new test result");
        LabTests tests = new LabTests(userId);
        return super.respond(tests.create(labTest));
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
        String userId = requireUserId();
        LabTests tests = new LabTests(userId);
        return super.respond(tests.list(start, limit, asc, sort));
    }
}
