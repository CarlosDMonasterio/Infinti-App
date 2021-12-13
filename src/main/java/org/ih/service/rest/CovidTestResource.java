package org.ih.service.rest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.ih.common.logging.Logger;
import org.ih.dto.LabTest;
import org.ih.labtest.LabTests;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/tests")
public class CovidTestResource extends RestResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(LabTest labTest) {
        String userId = requireUserId();
        Logger.info(userId + ": creating new test result");
        LabTests tests = new LabTests();
        return super.respond(tests.create(userId, labTest));
    }

    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importUsers(@FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition contentDisposition) {
        String userId = requireUserId();
        LabTests labTests = new LabTests();
        return super.respond(labTests.importFile(userId, fileInputStream));
    }
}
