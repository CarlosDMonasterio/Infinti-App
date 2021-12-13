package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.LabTest;
import org.ih.labtest.LabTestResult;
import org.ih.labtest.LabTestType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LabTest")
public class LabTestModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lab_test_id")
    @SequenceGenerator(name = "lab_test_id", sequenceName = "lab_test_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private LabTestType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountModel account;

    @ManyToOne()
    @JoinColumn(name = "district_id")
    private DistrictModel district;

    @OneToOne()
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "test_date")
    private Date testDate;

    @Column(name = "location")
    private String location;

    @Column(name = "department")
    private String department;

    @Column(name = "result")
    @Enumerated(value = EnumType.STRING)
    private LabTestResult result;

    public long getId() {
        return id;
    }

    public LabTestType getType() {
        return type;
    }

    public void setType(LabTestType type) {
        this.type = type;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    public DistrictModel getDistrict() {
        return district;
    }

    public void setDistrict(DistrictModel district) {
        this.district = district;
    }

    public SchoolModel getSchool() {
        return school;
    }

    public void setSchool(SchoolModel school) {
        this.school = school;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LabTestResult getResult() {
        return result;
    }

    public void setResult(LabTestResult result) {
        this.result = result;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    @Override
    public LabTest toDataObject() {
        LabTest labTest = new LabTest();
        labTest.setDepartment(this.department);
        labTest.setLocation(this.location);
        labTest.setDateTime(this.testDate.getTime());
        if (this.district != null) {
            labTest.setDistrict(this.district.toDataObject());

            if (this.school != null)
                labTest.setSchool(this.school.toDataObject());
        }
        return labTest;
    }
}
