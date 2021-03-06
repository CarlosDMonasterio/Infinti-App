package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.IncidentReport;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IncidentReport")
public class IncidentReportModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "incident_report_id")
    @SequenceGenerator(name = "incident_report", sequenceName = "incident_report_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel reporter;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictModel district;

    @OneToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @Column(name = "details")
    private String details;

    @Column(name = "additional_details")
    private String additionalDetails;

    @Column(name = "department")
    private String department;

    @Column(name = "location")
    private String location;

    @Column(name = "supervisor")
    private String supervisor;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    public long getId() {
        return id;
    }

    public AccountModel getReporter() {
        return reporter;
    }

    public void setReporter(AccountModel reporter) {
        this.reporter = reporter;
    }

    public DistrictModel getDistrict() {
        return district;
    }

    public void setDistrict(DistrictModel district) {
        this.district = district;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SchoolModel getSchool() {
        return school;
    }

    public void setSchool(SchoolModel school) {
        this.school = school;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public IncidentReport toDataObject() {
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setId(this.id);
        incidentReport.setDateTime(this.date.getTime());
        if (this.district != null)
            incidentReport.setDistrict(this.district.toDataObject());
        if (this.school != null)
            incidentReport.setSchool(this.school.toDataObject());
        incidentReport.setDetails(this.details);
        incidentReport.setUser(this.reporter.toDataObject());
        incidentReport.setDepartment(this.department);
        incidentReport.setLocation(this.location);
        incidentReport.setAdditionalDetails(this.additionalDetails);
        incidentReport.setSupervisor(this.supervisor);
        return incidentReport;
    }
}
