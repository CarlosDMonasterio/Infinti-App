package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.patient.PatientModel;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class PatientDAO extends HibernateRepository<PatientModel> {

    @Override
    public PatientModel get(long id) {
        return super.retrieve(PatientModel.class, id);
    }

    public Optional<PatientModel> getByIdentifier(String identifier) {
        try {
            CriteriaQuery<PatientModel> query = getBuilder().createQuery(PatientModel.class);
            Root<PatientModel> from = query.from(PatientModel.class);
            query.where(getBuilder().equal(from.get("identifier"), identifier));
            return currentSession().createQuery(query).uniqueResultOptional();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public Optional<PatientModel> getByUUID(String uuid) {
        try {
            CriteriaQuery<PatientModel> query = getBuilder().createQuery(PatientModel.class);
            Root<PatientModel> from = query.from(PatientModel.class);
            query.where(getBuilder().equal(from.get("uuid"), uuid));
            return currentSession().createQuery(query).uniqueResultOptional();
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }

    public List<PatientModel> list(String sort, boolean asc, int start, int limit) {
        return super.list(PatientModel.class, sort, asc, start, limit);
    }

    public long listCount() {
        return super.listCount(PatientModel.class);
    }
}
