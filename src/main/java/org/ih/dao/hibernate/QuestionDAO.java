package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.QuestionModel;
import org.ih.dao.model.SurveyModel;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class QuestionDAO extends HibernateRepository<QuestionModel> {

    @Override
    public QuestionModel get(long id) {
        return super.retrieve(QuestionModel.class, id);
    }

    public List<QuestionModel> list(long surveyId, String sort, boolean asc, int start, int limit) {
        try {
            CriteriaQuery<QuestionModel> query = getBuilder().createQuery(QuestionModel.class);
            Root<QuestionModel> from = query.from(QuestionModel.class);
            Join<QuestionModel, SurveyModel> join = from.join("survey");
            query.where(getBuilder().equal(join.get("id"), surveyId));
            query.orderBy(asc ? getBuilder().asc(from.get(sort)) : getBuilder().desc(from.get(sort)));
            return currentSession().createQuery(query).setMaxResults(limit).setFirstResult(start).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public long listCount() {
        return super.listCount(QuestionModel.class);
    }
}
