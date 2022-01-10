package org.ih.pass;

import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.SurveyDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.LabTestModel;
import org.ih.dao.model.QuestionModel;
import org.ih.dao.model.SurveyModel;
import org.ih.dto.Pass;
import org.ih.labtest.LabTestResult;
import org.ih.survey.SurveyType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * Daily Passes for workers
 *
 * @author Hector Plahar
 */
public class Passes {

    private final SurveyDAO surveyDAO;
    private final String requester;

    public Passes(String userId) {
        this.requester = userId;
        this.surveyDAO = DAOFactory.getSurveyDAO();
    }

    /**
     * Get pass for user with specified user id. To be eligible for a pass, the referenced user id must have
     * a valid daily health screening questionnaire and a negative covid test within the past week
     *
     * @param userId unique identifier for user the pass is being retrieved for
     * @return unique identifier for pass
     */
    public Pass get(String userId) {
        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(userId);
        if (accountModel == null)
            return null;

        Pass pass = new Pass();
        pass.setAccount(accountModel.toDataObject());

        // check for daily health screen for today
        SurveyModel model = this.surveyDAO.getUserSurveyFromToday(SurveyType.DAILY_HEALTH, userId);
        if (model == null)
            return pass;

        for (QuestionModel questionModel : model.getQuestions()) {
            if (questionModel.isAnswer())
                return pass;
        }

        // check for a covid test in the past week (get most recent covid test)
        LabTestModel labTestModel = DAOFactory.getLabTestDAO().getMostRecentForUser(userId);
        if (labTestModel == null)
            return pass;

        // if test is not within the past week or is negative then do not generate a pass
        if (!this.isWithinOneWeek(labTestModel.getTestDate()) || labTestModel.getResult() == LabTestResult.POSITIVE)
            return pass;

        // todo: temporary solution. this should be valid when scanned
        pass.setUuid(UUID.randomUUID().toString());
        return pass;
    }

    /**
     * Checks if passed date is within one week of today
     * @param testDate date to check
     * @return true, if referenced date occurs less than one week ago; false otherwise
     */
    private boolean isWithinOneWeek(Date testDate) {
        return (testDate.toInstant().isAfter(Instant.now().minus(7, ChronoUnit.DAYS)));
    }
}
