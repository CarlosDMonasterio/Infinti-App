package org.ih.dao.hibernate;

import org.ih.dao.model.TestModel;

public class TestDAO extends HibernateRepository<TestModel> {

    @Override
    public TestModel get(long id) {
        return super.retrieve(TestModel.class, id);
    }
}
