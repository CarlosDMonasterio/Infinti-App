package org.ih.school;

import com.opencsv.CSVReader;
import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.SchoolDAO;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.School;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Schools {

    private final SchoolDAO dao;

    public Schools() {
        this.dao = DAOFactory.getSchoolDAO();
    }

    public School create(School school) {
        // get the district this school will be in
        DistrictModel districtModel = null;
        if (school.getDistrict() != null) {
            districtModel = DAOFactory.getDistrictDAO().get(school.getDistrict().getId());
        }

        SchoolModel model = new SchoolModel();
        model.setAddress(school.getAddress());
        model.setDescription(school.getDescription());
        model.setName(school.getName());
        model.setPhone(school.getPhone());
        model.setGrades(school.getGrades());
        model.setDistrict(districtModel);
        model = this.dao.create(model);
        return model.toDataObject();
    }

    public Results<School> list(int offset, int limit, boolean asc, String sort, String filter) {
        Results<School> resultData = new Results<>();
        List<SchoolModel> results = this.dao.list(sort, asc, offset, limit);
        for (SchoolModel model : results) {
            resultData.getRequested().add(model.toDataObject());
        }

        resultData.setAvailable(this.dao.listCount());
        return resultData;
    }

    public void createDemoSchool() {

    }

    public void parse(InputStream inputStream) {
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
//        reader.readNext()
    }
}
