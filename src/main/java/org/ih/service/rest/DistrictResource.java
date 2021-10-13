package org.ih.service.rest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.ih.common.logging.Logger;
import org.ih.district.Districts;
import org.ih.dto.District;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/districts")
public class DistrictResource extends RestResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(District district) {
        String userId = getUserId();
        Logger.info(userId + ": creating user id");
        Districts districts = new Districts();
        return super.respond(districts.create(district));
    }

    @GET
    @Path("{id}/schools/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterSchoolsForDistrict(@PathParam("id") long id,
                                             @QueryParam("filter") String filter,
                                             @DefaultValue("7") @QueryParam("limit") int limit) {
        String userId = getUserId();
        Logger.info(userId + ": filtering schools for district");
        Districts districts = new Districts();
        return super.respond(districts.filterSchools(id, filter, limit));
    }

    @GET
    @Path("{id}/schools")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchoolsForDistrict(@PathParam("id") long id,
                                          @DefaultValue("0") @QueryParam("start") int start,
                                          @DefaultValue("false") @QueryParam("asc") boolean asc,
                                          @DefaultValue("id") @QueryParam("sort") String sort,
                                          @QueryParam("filter") String filter,
                                          @DefaultValue("7") @QueryParam("limit") int limit) {
        String userId = getUserId();
        Logger.info(userId + ": retrieving schools for district with id: " + id);
        Districts districts = new Districts();
        return super.respond(districts.schools(id, start, limit, asc, sort));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getList(@DefaultValue("15") @QueryParam("limit") int limit,
                            @DefaultValue("0") @QueryParam("start") int start,
                            @DefaultValue("false") @QueryParam("asc") boolean asc,
                            @DefaultValue("id") @QueryParam("sort") String sort,
                            @QueryParam("filterText") String filter) {
        String userId = getUserId();
        Logger.info(userId + ": retrieving all districts");
        Districts districts = new Districts();
        return super.respond(districts.list(start, limit, asc, sort, filter));
    }

    @POST
    @Path("{id}/schools/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importUsers(@PathParam("id") long id,
                                @FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition contentDisposition) {
        String userId = getUserId();
        Districts districts = new Districts();
        districts.importSchools(id, fileInputStream);
        return super.respond(true);
    }
}
