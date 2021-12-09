package org.ih.survey;

import org.apache.commons.lang3.StringUtils;
import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.QuestionDAO;
import org.ih.dao.hibernate.SurveyDAO;
import org.ih.dao.model.*;
import org.ih.dto.Question;
import org.ih.dto.Survey;
import org.ih.notification.NotificationTask;
import org.ih.task.TaskRunner;

import java.util.ArrayList;
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
        if (survey.getDistrict() != null) {
            districtModel = DAOFactory.getDistrictDAO().get(survey.getDistrict().getId());
            if (districtModel == null)
                throw new IllegalArgumentException("Cannot create survey for null district");
        }

        // get school (this is optional in some cases)
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
        model.setType(survey.getType() == null ? SurveyType.AUDIT : survey.getType());
        model.setStatus(SurveyStatus.IN_PROGRESS);
        model.setCreated(new Date());
        model = this.dao.create(model);

        // for daily health screens, keep track of positive answers
        List<Question> positiveAnswers = new ArrayList<>();
        QuestionDAO dao = DAOFactory.getQuestionDAO();

        // create questions
        if (survey.getQuestions() != null && !survey.getQuestions().isEmpty()) {
            for (Question question : survey.getQuestions()) {
                QuestionModel questionModel = new QuestionModel();
                questionModel.setAnswer(question.isAnswer());
                questionModel.setComments(question.getComments());
                questionModel.setLabel(question.getLabel());
                questionModel.setSurvey(model);
                questionModel = dao.create(questionModel);
                if (model.getType() == SurveyType.DAILY_HEALTH && question.isAnswer()) {
                    positiveAnswers.add(questionModel.toDataObject());
                }
            }
        }

        survey = model.toDataObject();
        checkSendEmailNotificationForDailyHealthScreen(survey, positiveAnswers);
        return model.toDataObject();
    }

    /**
     * Checks if we have a list of affirmative questions for daily health survey and send email notification
     */
    private void checkSendEmailNotificationForDailyHealthScreen(Survey survey, List<Question> positiveAnswers) {
        if (positiveAnswers == null || positiveAnswers.isEmpty() || survey.getType() != SurveyType.DAILY_HEALTH)
            return;

        String subject = "Infiniti Health Daily Health Screening Alert";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello, ");
        stringBuilder.append("\n\nAn alert has been triggered by answers to the following questions in the daily health screening.");

        for (Question question : positiveAnswers) {
            stringBuilder.append("\n\n\t").append(question.getLabel());
            stringBuilder.append("\n\t").append("Yes");
        }

        stringBuilder.append("\n\n").append("These answers were submitted by ")
                .append(survey.getAccount().getEmail()).append(" at ")
                .append(new Date(survey.getCreationTime()));

        stringBuilder.append("\n\n").append("Thank you!\n\n");

        NotificationTask notificationTask = new NotificationTask();
        notificationTask.addInformation("infectionprevention@infinitihealth.org", subject, stringBuilder.toString());
        TaskRunner.getInstance().runTask(notificationTask);
    }

    public Results<Survey> list(int offset, int limit, boolean asc, String sort, SurveyType type) {
        if (StringUtils.isBlank(sort))
            sort = "id";

        Results<Survey> results = new Results<>();
        results.setAvailable(this.dao.listCount(type));
        List<SurveyModel> questions = this.dao.list(sort, asc, offset, limit, type);
        for (SurveyModel model : questions) {
            Survey survey = model.toDataObject();

            // get the questions
            for (QuestionModel questionModel : model.getQuestions()) {
                survey.getQuestions().add(questionModel.toDataObject());
            }
            results.getRequested().add(survey);

//            List<QuestionModel> list = questionDAO.list(model.getId(), "id", true, 0, 100);
//            System.out.println(list.size());
        }

        return results;
    }
}
