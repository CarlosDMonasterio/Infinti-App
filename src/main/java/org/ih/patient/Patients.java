package org.ih.patient;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.PatientDAO;
import org.ih.dao.model.TestModel;
import org.ih.dto.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// todo : check roles
public class Patients {

    private final String userId;
    private final PatientDAO dao;

    public Patients(String userId) {
        this.userId = userId;
        this.dao = DAOFactory.getPatientDAO();
    }

    public Patient get(String identifier) {
        Optional<PatientModel> optional = this.dao.getByUUID(identifier);
        return optional.map(PatientModel::toDataObject).orElse(null);
    }

    public Patient create(Patient patient) {
        PatientModel model = new PatientModel();
        model.setEmail(patient.getEmail());
        model.setFirstName(patient.getFirstName());
        model.setLastName(patient.getLastName());
        model.setIdentifier(patient.getIdentifier());
        model.setBirthDate(patient.getBirthDate());
        model.setPhone(patient.getPhone());
        model.setUuid(UUID.randomUUID().toString());
        return this.dao.create(model).toDataObject();
    }

    public Results<Patient> list(int offset, int limit, boolean asc, String sort) {
        Results<Patient> results = new Results<>();
        results.setAvailable(this.dao.listCount());
        List<PatientModel> models = this.dao.list(sort, asc, offset, limit);
        for (PatientModel model : models) {
            results.getRequested().add(model.toDataObject());
        }
        return results;
    }

    public Test createPatientTest(String uuid, Test test) {
        Optional<PatientModel> optional = this.dao.getByUUID(uuid);
        if (optional.isEmpty())
            return null;

        TestModel testModel = new TestModel();
        testModel.setPatient(optional.get());
        testModel.setDate(new Date());
        testModel.setNotified(false);
        testModel.setSpecimen(test.getSpecimen());
        testModel = DAOFactory.getTestDAO().create(testModel);

        return testModel.toDataObject();
    }

    public Results<Test> listPatientTests(String uuid, int offset, int limit, boolean asc, String sort) {
        Results<Test> results = new Results<>();

        return results;
    }
}
