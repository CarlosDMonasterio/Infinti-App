package org.ih.service.rest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.ih.common.file.Files;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/files")
public class FileResource extends RestResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition contentDisposition) {
        String userId = requireUserId();
        Files files = new Files(userId);
        return super.respond(files.add(contentDisposition.getName(), fileInputStream));
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{identifier}")
    public Response getFile(@QueryParam("sid") String sessionId, @PathParam("identifier") String identifier) {
        return null;

//        FileStreamingOutput fileStreamingOutput = new FileStreamingOutput(file);
//        return Response.ok(fileStreamingOutput)
//                .header("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"")
//                .build();
    }
}
