package org.ih.service.rest;

import org.ih.common.logging.Logger;
import org.ih.dto.Test;
import org.ih.patient.Patient;
import org.ih.patient.Patients;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/patients")
public class PatientResource extends RestResource {

    @GET
    @Path("/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPatient(@PathParam("identifier") String identifier) {
        String userId = requireUserId();
        Logger.info(userId + ": retrieving patient by identifier " + identifier);
        Patients patients = new Patients(userId);
        return super.respond(patients.get(identifier));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePatient(Patient patient) {
        String userId = requireUserId();
        Logger.info(userId + ": creating new patient record");
        Patients patients = new Patients(userId);
        return super.respond(patients.create(patient));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list(@DefaultValue("15") @QueryParam("limit") int limit,
                         @DefaultValue("0") @QueryParam("start") int start,
                         @DefaultValue("false") @QueryParam("asc") boolean asc,
                         @DefaultValue("id") @QueryParam("sort") String sort,
                         @QueryParam("filterText") String filter) {
        String userId = requireUserId();
        Logger.info(userId + ": paging patient list");
        Patients patients = new Patients(userId);
        return super.respond(patients.list(start, limit, asc, sort));
    }

    @POST
    @Path("/{uuid}/tests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTestForPatient(@PathParam("uuid") String uuid, Test test) {
        String userId = requireUserId();
        Logger.info(userId + ": creating test for user " + uuid);
        Patients patients = new Patients(userId);
        return super.respond(patients.createPatientTest(uuid, test));
    }

    @GET
    @Path("/{uuid}/tests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listPatientTests(@PathParam("uuid") String uuid,
                                     @DefaultValue("15") @QueryParam("limit") int limit,
                                     @DefaultValue("0") @QueryParam("start") int start,
                                     @DefaultValue("false") @QueryParam("asc") boolean asc,
                                     @DefaultValue("id") @QueryParam("sort") String sort,
                                     @QueryParam("filterText") String filter) {
        String userId = requireUserId();
        Logger.info(userId + ": paging patient tests");
        Patients patients = new Patients(userId);
        return super.respond(patients.listPatientTests(uuid, start, limit, asc, sort));
    }
}
