package org.ih.hygiene;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.HygieneDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.HygieneModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.Hygiene;

import java.util.Date;
import java.util.List;

public class HygieneReports {

    private final String userId;
    private final HygieneDAO dao;

    public HygieneReports(String userId) {
        this.userId = userId;
        this.dao = DAOFactory.getHygieneDAO();
    }

    public Hygiene create(Hygiene hygiene) {
        HygieneModel model = new HygieneModel();
        model.setDate(new Date());

        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(this.userId);
        model.setReporter(accountModel);

        // get district
        DistrictModel districtModel = DAOFactory.getDistrictDAO().get(hygiene.getDistrict().getId());
        model.setDistrict(districtModel);

        // get school (todo : school must be in the same district)
        SchoolModel schoolModel = DAOFactory.getSchoolDAO().get(hygiene.getSchool().getId());
        model.setSchool(schoolModel);

        model.setType(hygiene.getType());
        model.setRole(hygiene.getRole());
        model.setCompliant(hygiene.isCompliant());
        model = this.dao.create(model);
        return model.toDataObject();
    }

    public Results<Hygiene> list(HygieneType type, int limit, int start, boolean asc, String sort, String filter) {
        Results<Hygiene> results = new Results<>();
        long listCount = this.dao.listCount(type);
        results.setAvailable(listCount);

        List<HygieneModel> modelList = this.dao.list(type, sort, asc, start, limit);
        for (HygieneModel model : modelList) {
            results.getRequested().add(model.toDataObject());
        }
        return results;
    }
}
