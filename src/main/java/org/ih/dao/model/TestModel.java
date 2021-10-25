package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.Test;
import org.ih.patient.PatientModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Test")
public class TestModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "test_id")
    @SequenceGenerator(name = "test_id", sequenceName = "test_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "patient")
    @ManyToOne(optional = false)
    private PatientModel patient;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "notified")
    private boolean notified;

    @Column(name = "specimen")
    private String specimen;

    @Column(name = "specimen_id")
    private String specimenId;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "resulted")
    private Date resulted;

    public long getId() {
        return id;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Date getResulted() {
        return resulted;
    }

    public void setResulted(Date resulted) {
        this.resulted = resulted;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(String specimenId) {
        this.specimenId = specimenId;
    }

    @Override
    public Test toDataObject() {
        Test test = new Test();
        test.setId(this.id);
        return test;
    }
}
