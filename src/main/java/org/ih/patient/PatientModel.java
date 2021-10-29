package org.ih.patient;

import org.ih.dao.DatabaseModel;

import javax.persistence.*;

@Entity
@Table(name = "Patient")
public class PatientModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "patient_id")
    @SequenceGenerator(name = "patient_id", sequenceName = "patient_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "identifier", unique = true)     // some sort of human-readable or local specific identifier
    private String identifier;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "phone", length = 320)
    private String phone;

    @Column(name = "birthDate", length = 12)
    private String birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "uuid", unique = true)
    private String uuid;

    public long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public Patient toDataObject() {
        Patient patient = new Patient();
        patient.setIdentifier(this.identifier);
        patient.setId(this.id);
        patient.setLastName(this.lastName);
        patient.setFirstName(this.firstName);
        patient.setPhone(this.phone);
        patient.setBirthDate(this.birthDate);
        patient.setUuid(this.uuid);
        patient.setGender(this.gender);
        return patient;
    }
}
