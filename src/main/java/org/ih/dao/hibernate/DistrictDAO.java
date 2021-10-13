package org.ih.dao.hibernate;

import org.ih.dao.model.DistrictModel;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class DistrictDAO extends HibernateRepository<DistrictModel> {

    @Override
    public DistrictModel get(long id) {
        return super.retrieve(DistrictModel.class, id);
    }

    public List<DistrictModel> list(String sort, boolean asc, int start, int limit) {
        return super.list(DistrictModel.class, sort, asc, start, limit);
    }

    public Optional<DistrictModel> getByName(String name) {
        CriteriaQuery<DistrictModel> query = getBuilder().createQuery(DistrictModel.class);
        Root<DistrictModel> from = query.from(DistrictModel.class);
        query.where(getBuilder().equal(from.get("label"), name));
        return currentSession().createQuery(query).uniqueResultOptional();
    }

    public long listCount() {
        return super.listCount(DistrictModel.class);
    }
}
