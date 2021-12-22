package org.ih.dao.hibernate;

import org.ih.dao.model.LabTestModel;

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
}
