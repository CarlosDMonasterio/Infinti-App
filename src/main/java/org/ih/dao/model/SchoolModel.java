package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.School;

import javax.persistence.*;

@Entity
@Table(name = "School")
public class SchoolModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "school_id")
    @SequenceGenerator(name = "school_id", sequenceName = "school_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "grades")
    private String grades;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictModel district;

    public long getId() {
        return id;
    }

    public DistrictModel getDistrict() {
        return district;
    }

    public void setDistrict(DistrictModel district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGrades() {
        return grades;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    @Override
    public School toDataObject() {
        School school = new School();
        school.setId(this.id);
        school.setDescription(this.description);
        school.setAddress(this.address);
        school.setGrades(this.grades);
        school.setPhone(this.phone);
        school.setName(this.name);
        school.setUrl(url);
        school.setDistrict(this.district.toDataObject());
        return school;
    }
}
