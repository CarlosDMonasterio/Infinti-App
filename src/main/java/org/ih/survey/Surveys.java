package org.ih.survey;

import org.apache.commons.lang3.StringUtils;
import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.SurveyDAO;
import org.ih.dao.model.*;
import org.ih.dto.Question;
import org.ih.dto.Survey;

import java.util.Date;
import java.util.List;

public class Surveys {

    private final String userId;
    private final SurveyDAO dao;

    public Surveys(String userId) {
        this.userId = userId;
        this.dao = DAOFactory.getSurveyDAO();
    }

    public Survey create(Survey survey) {

        // get school district
        DistrictModel districtModel = null;
        if (survey.getDistrict() != null) {    // district is required for quality audit but not for dhs
            districtModel = DAOFactory.getDistrictDAO().get(survey.getDistrict().getId());
            if (districtModel == null)
                throw new IllegalArgumentException("Cannot create survey for null district");
        }

        // get school
        SchoolModel schoolModel = null;
        if (survey.getSchool() != null) {
            schoolModel = DAOFactory.getSchoolDAO().get(survey.getSchool().getId());
            if (schoolModel == null)
                throw new IllegalArgumentException("Cannot create survey for null school");
        }

        if (schoolModel != null && districtModel != null) {
            if (schoolModel.getDistrict() == null || schoolModel.getDistrict().getId() != survey.getDistrict().getId())
                throw new IllegalArgumentException("Incompatible school and district");
        }

        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(this.userId);
        if (accountModel == null)
            throw new IllegalArgumentException("Invalid account for survey");

        SurveyModel model = new SurveyModel();
        model.setDistrict(districtModel);
        model.setSchool(schoolModel);
        model.setAccount(accountModel);
        model.setStatus(SurveyStatus.IN_PROGRESS);
        model.setCreated(new Date());
        model = this.dao.create(model);

        // create questions
        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            for (Question question : survey.getQuestions()) {
                QuestionModel questionModel = new QuestionModel();
                questionModel.setAnswer(question.isAnswer());
                questionModel.setLabel(question.getLabel());
                questionModel.setSurvey(model);
                DAOFactory.getQuestionDAO().create(questionModel);
            }
        }

        return model.toDataObject();
    }

    public Results<Survey> list(int offset, int limit, boolean asc, String sort) {
        if (StringUtils.isBlank(sort))
            sort = "id";

//        QuestionDAO questionDAO = DAOFactory.getQuestionDAO();
        Results<Survey> results = new Results<>();
        results.setAvailable(this.dao.listCount());

        List<SurveyModel> questions = this.dao.list(sort, asc, offset, limit);
        for (SurveyModel model : questions)
            results.getRequested().add(model.toDataObject());

        return results;
    }
}
