package org.ih.dao.hibernate;

import org.ih.common.logging.Logger;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.SchoolModel;
import org.ih.util.StringUtil;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class SchoolDAO extends HibernateRepository<SchoolModel> {

    @Override
    public SchoolModel get(long id) {
        return retrieve(SchoolModel.class, id);
    }

    public List<SchoolModel> list(String sort, boolean asc, int start, int limit) {
        return super.list(SchoolModel.class, sort, asc, start, limit);
    }

    public long listCount() {
        return super.listCount(SchoolModel.class);
    }

    public List<SchoolModel> filterSchoolsByDistrict(long districtId, String filter, int limit) {
        CriteriaQuery<SchoolModel> query = getBuilder().createQuery(SchoolModel.class);
        Root<SchoolModel> from = query.from(SchoolModel.class);
        Join<DistrictModel, SchoolModel> district = from.join("district");
        if (StringUtil.isEmpty(filter))
            query.where(getBuilder().equal(district.get("id"), districtId));
        else {
            query.where(getBuilder().equal(district.get("id"), districtId),
                    getBuilder().like(getBuilder().lower(from.get("name")), "%" + filter.toLowerCase() + "%"));
        }
        return currentSession().createQuery(query).setMaxResults(limit).getResultList();
    }

    public List<SchoolModel> pageDistrictSchools(long districtId, int offset, int limit, String sort, boolean asc,
                                                 String filter) {
        CriteriaQuery<SchoolModel> query = getBuilder().createQuery(SchoolModel.class);
        Root<SchoolModel> from = query.from(SchoolModel.class);
        Join<DistrictModel, SchoolModel> district = from.join("district");
        if (StringUtil.isEmpty(filter))
            query.where(getBuilder().equal(district.get("id"), districtId));
        else {
            query.where(getBuilder().equal(district.get("id"), districtId),
                    getBuilder().like(getBuilder().lower(from.get("name")), "%" + filter.toLowerCase() + "%"));
        }
        query.orderBy(asc ? getBuilder().asc(from.get(sort)) : getBuilder().desc(from.get(sort)));
        return currentSession().createQuery(query).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public long districtSchoolsCount(long districtId, String filter) {
        try {
            CriteriaQuery<Long> query = getBuilder().createQuery(Long.class);
            Root<SchoolModel> from = query.from(SchoolModel.class);
            Join<DistrictModel, SchoolModel> district = from.join("district");
            if (StringUtil.isEmpty(filter))
                query.where(getBuilder().equal(district.get("id"), districtId));
            else {
                query.where(getBuilder().equal(district.get("id"), districtId),
                        getBuilder().like(getBuilder().lower(from.get("name")), "%" + filter.toLowerCase() + "%"));
            }
            query.select(getBuilder().countDistinct(from.get("id")));
            return currentSession().createQuery(query).uniqueResult();
        } catch (Exception e) {
            Logger.error(e);
            throw new DataAccessException(e);
        }
    }
}
