package org.ih.dao.hibernate;

import org.ih.dao.model.IncidentReportModel;

import java.util.List;

public class IncidentReportDAO extends HibernateRepository<IncidentReportModel> {

    @Override
    public IncidentReportModel get(long id) {
        return super.retrieve(IncidentReportModel.class, id);
    }

    public List<IncidentReportModel> list(String id, boolean asc, int offset, int limit) {
        return super.list(IncidentReportModel.class, id, asc, offset, limit);
    }

    public long listCount() {
        return super.listCount(IncidentReportModel.class);
    }
}
