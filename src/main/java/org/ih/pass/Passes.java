package org.ih.pass;

import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.SurveyDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.QuestionModel;
import org.ih.dao.model.SurveyModel;
import org.ih.dto.Pass;
import org.ih.survey.SurveyType;

import java.util.UUID;

/**
 * Daily Passes for workers
 *
 * @author Hector Plahar
 */
public class Passes {

    private final SurveyDAO surveyDAO;
    private final String userId;

    public Passes(String userId) {
        this.userId = userId;
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
            return null;
        for (QuestionModel questionModel : model.getQuestions()) {
            if (questionModel.isAnswer())
                return pass;
        }

        // temporary solution
        pass.setUuid(UUID.randomUUID().toString());
        return pass;
    }
}
