package org.ih.dto;

import org.ih.patient.Patient;

public class Test implements DataObject {

    private long id;
    private Patient patient;
    private long testDate;
    private boolean notified;
    private String specimen;
    private String specimenId;
    private long resulted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public long getTestDate() {
        return testDate;
    }

    public void setTestDate(long testDate) {
        this.testDate = testDate;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public long getResulted() {
        return resulted;
    }

    public void setResulted(long resulted) {
        this.resulted = resulted;
    }

    public String getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(String specimenId) {
        this.specimenId = specimenId;
    }
}
