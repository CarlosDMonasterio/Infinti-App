package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.Account;
import org.ih.dto.Hygiene;
import org.ih.hygiene.HygieneType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Hygiene")
public class HygieneModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hygiene_id")
    @SequenceGenerator(name = "hygiene_id", sequenceName = "hygiene_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountModel reporter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "district_id")
    private DistrictModel district;

    @ManyToOne(optional = false)
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @Column(name = "role")
    private String role;

    @Column(name = "compliance")
    private boolean compliant;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private HygieneType type;

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

    public SchoolModel getSchool() {
        return school;
    }

    public void setSchool(SchoolModel school) {
        this.school = school;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getCompliant() {
        return compliant;
    }

    public void setCompliant(Boolean compliant) {
        this.compliant = compliant;
    }

    public HygieneType getType() {
        return type;
    }

    public void setType(HygieneType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Hygiene toDataObject() {
        Hygiene hygiene = new Hygiene();
        hygiene.setId(this.id);
        hygiene.setCreationTime(this.date.getTime());

        Account account = new Account();
        account.setId(this.reporter.getId());
        account.setEmail(this.reporter.getEmail());
        account.setFirstName(this.reporter.getFirstName());
        account.setLastName(this.reporter.getLastName());
        hygiene.setReporter(account);

        hygiene.setRole(this.role);
        hygiene.setCompliant(this.compliant);
        hygiene.setDistrict(this.district.toDataObject());
        hygiene.setSchool(this.school.toDataObject());
        hygiene.setType(this.type);
        hygiene.setCompliant(this.compliant);
        return hygiene;
    }
}
