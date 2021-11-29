package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.SurveyModel;
import org.ih.survey.SurveyType;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SurveyDAO extends HibernateRepository<SurveyModel> {

    @Override
    public SurveyModel get(long id) {
        return super.retrieve(SurveyModel.class, id);
    }

    public List<SurveyModel> list(String sort, boolean asc, int start, int limit, SurveyType type) {
        try {
            CriteriaQuery<SurveyModel> query = getBuilder().createQuery(SurveyModel.class);
            Root<SurveyModel> from = query.from(SurveyModel.class);
            query.where(getBuilder().equal(from.get("type"), type));
            query.orderBy(asc ? getBuilder().asc(from.get(sort)) : getBuilder().desc(from.get(sort)));
            return currentSession().createQuery(query).setMaxResults(limit).setFirstResult(start).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public long listCount(SurveyType type) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<SurveyModel> from = query.from(SurveyModel.class);
            query.where(getBuilder().equal(from.get("type"), type));
            query.select(getBuilder().countDistinct(from.get("id")));
            return currentSession().createQuery(query).uniqueResult();
        } catch (Exception e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }
}
