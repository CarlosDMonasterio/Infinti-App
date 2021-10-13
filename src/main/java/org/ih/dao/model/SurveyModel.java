package org.ih.dao.model;

import org.ih.dao.DatabaseModel;
import org.ih.dto.Survey;
import org.ih.survey.SurveyStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Survey")
public class SurveyModel implements DatabaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "survey_id")
    @SequenceGenerator(name = "survey_id", sequenceName = "survey_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private SurveyStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountModel account;

    @ManyToOne(optional = false)
    @JoinColumn(name = "district_id")
    private DistrictModel district;

    @OneToOne(optional = false)
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "survey")
    private final Set<QuestionModel> questions = new HashSet<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    public long getId() {
        return id;
    }

    public SurveyStatus getStatus() {
        return status;
    }

    public void setStatus(SurveyStatus status) {
        this.status = status;
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

    public Set<QuestionModel> getQuestions() {
        return questions;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Survey toDataObject() {
        Survey survey = new Survey();
        survey.setId(this.id);
        survey.setAccount(this.account.toDataObject());
        survey.setDistrict(this.district.toDataObject());
        survey.setSchool(this.school.toDataObject());
        if (this.created != null)
            survey.setCreationTime(this.created.getTime());
        return survey;
    }
}
