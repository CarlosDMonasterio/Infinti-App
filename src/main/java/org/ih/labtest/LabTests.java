package org.ih.labtest;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.LabTestDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.LabTestModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.LabTest;

import java.util.Date;
import java.util.List;

public class LabTests {

    private final LabTestDAO dao;
    private final String userId;

    public LabTests(String userId) {
        this.dao = DAOFactory.getLabTestDAO();
        this.userId = userId;
    }

    public LabTest create(LabTest test) {
        LabTestModel model = new LabTestModel();
        model.setCreated(new Date());

        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(userId);
        model.setAccount(accountModel);

        // get district if available
        if (test.getDistrict() != null) {
            DistrictModel districtModel = DAOFactory.getDistrictDAO().get(test.getDistrict().getId());
            model.setDistrict(districtModel);
        }

        // get school (todo : school must be in the same district)
        if (test.getSchool() != null) {
            SchoolModel schoolModel = DAOFactory.getSchoolDAO().get(test.getSchool().getId());
            model.setSchool(schoolModel);
        }

        model.setDepartment(test.getDepartment());
        model.setLocation(test.getLocation());
        model.setResult(test.getResult());
        model.setTestDate(new Date(test.getDateTime()));
        model.setType(test.getType());
        model.setFileId(test.getFileId());
        model = this.dao.create(model);

        return model.toDataObject();
    }

    public Results<LabTest> list(int offset, int limit, boolean asc, String sort) {
        List<LabTestModel> models = dao.list(sort, asc, offset, limit);
        long count = dao.listCount();

        Results<LabTest> results = new Results<>();
        results.setAvailable(count);
        for (LabTestModel model : models) {
            results.getRequested().add(model.toDataObject());
        }

        return results;
    }
}
