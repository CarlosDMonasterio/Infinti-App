package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.HygieneModel;
import org.ih.hygiene.HygieneType;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HygieneDAO extends HibernateRepository<HygieneModel> {

    @Override
    public HygieneModel get(long id) {
        return super.retrieve(HygieneModel.class, id);
    }

    public List<HygieneModel> list(HygieneType type, String sort, boolean asc, int start, int limit) {
        try {
            CriteriaQuery<HygieneModel> query = getBuilder().createQuery(HygieneModel.class);
            Root<HygieneModel> from = query.from(HygieneModel.class);
            query.where(getBuilder().equal(from.get("type"), type));
            query.orderBy(asc ? getBuilder().asc(from.get(sort)) : getBuilder().desc(from.get(sort)));
            return currentSession().createQuery(query).setMaxResults(limit).setFirstResult(start).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public long listCount(HygieneType type) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<HygieneModel> from = query.from(HygieneModel.class);
            query.where(getBuilder().equal(from.get("type"), type));
            query.select(getBuilder().countDistinct(from.get("id")));
            return currentSession().createQuery(query).uniqueResult();
        } catch (Exception e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public List<HygieneModel> recordsAfter(long time, HygieneType type) {
        try {
            CriteriaQuery<HygieneModel> query = getBuilder().createQuery(HygieneModel.class);
            Root<HygieneModel> from = query.from(HygieneModel.class);
            Date fromDate = getDateWithoutTime(new Date(time));
            query.where(getBuilder().greaterThanOrEqualTo(from.get("date"), fromDate), getBuilder().equal(from.get("type"), type));
            return currentSession().createQuery(query).list();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public static Date getDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
