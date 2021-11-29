package org.ih.service.rest;

import org.ih.common.logging.Logger;
import org.ih.dto.Survey;
import org.ih.survey.SurveyType;
import org.ih.survey.Surveys;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource for managing site surveys
 *
 * @author Hector Plahar
 */
@Path("/surveys")
public class SurveyResource extends RestResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSurvey(Survey survey) {
        String userId = requireUserId();
        Surveys surveys = new Surveys(userId);
        Logger.info(userId + ": creating new survey of type " + survey.getType());
        return super.respond(surveys.create(survey));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list(@DefaultValue("15") @QueryParam("limit") int limit,
                         @DefaultValue("0") @QueryParam("start") int start,
                         @DefaultValue("false") @QueryParam("asc") boolean asc,
                         @DefaultValue("id") @QueryParam("sort") String sort,
                         @DefaultValue("AUDIT") @QueryParam("type") SurveyType type) {
        String userId = requireUserId();
        Logger.info(userId + ": paging surveys " + type.name());
        Surveys surveys = new Surveys(userId);
        return super.respond(surveys.list(start, limit, asc, sort, type));
    }
}
