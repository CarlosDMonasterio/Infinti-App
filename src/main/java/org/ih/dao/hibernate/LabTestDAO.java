package org.ih.dao.hibernate;

import org.hibernate.HibernateException;
import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.LabTestModel;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class LabTestDAO extends HibernateRepository<LabTestModel> {

    @Override
    public LabTestModel get(long id) {
        return super.retrieve(LabTestModel.class, id);
    }

    public List<LabTestModel> list(String id, boolean asc, int offset, int limit) {
        return super.list(LabTestModel.class, id, asc, offset, limit);
    }

    public long listCount() {
        return super.listCount(LabTestModel.class);
    }

    public LabTestModel getMostRecentForUser(String userId) {
        try {
            CriteriaQuery<LabTestModel> query = getBuilder().createQuery(LabTestModel.class);
            Root<LabTestModel> from = query.from(LabTestModel.class);
            Join<LabTestModel, AccountModel> join = from.join("account");
            query.where(getBuilder().equal(join.get("email"), userId));
            query.orderBy(getBuilder().desc(from.get("id")));
            List<LabTestModel> results = currentSession().createQuery(query).setMaxResults(1).list();
            if (results == null || results.isEmpty())
                return null;
            return results.get(0);
        } catch (HibernateException e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }
}
