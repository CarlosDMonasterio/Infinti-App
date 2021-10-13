package org.ih.dao.hibernate;

import org.ih.dao.model.SurveyModel;

import java.util.List;

public class SurveyDAO extends HibernateRepository<SurveyModel> {

    @Override
    public SurveyModel get(long id) {
        return super.retrieve(SurveyModel.class, id);
    }

    public List<SurveyModel> list(String sort, boolean asc, int start, int limit) {
        return super.list(SurveyModel.class, sort, asc, start, limit);
    }

    public long listCount() {
        return super.listCount(SurveyModel.class);
    }


}
