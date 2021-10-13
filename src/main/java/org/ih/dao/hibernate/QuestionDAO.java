package org.ih.dao.hibernate;

import org.ih.dao.model.QuestionModel;

import java.util.List;

public class QuestionDAO extends HibernateRepository<QuestionModel> {

    @Override
    public QuestionModel get(long id) {
        return super.retrieve(QuestionModel.class, id);
    }

    public List<QuestionModel> list(String sort, boolean asc, int start, int limit) {
        return super.list(QuestionModel.class, sort, asc, start, limit);
    }

    public long listCount() {
        return super.listCount(QuestionModel.class);
    }
}
