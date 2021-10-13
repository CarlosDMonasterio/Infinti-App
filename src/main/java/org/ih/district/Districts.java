package org.ih.district;

import com.opencsv.CSVReader;
import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.DistrictDAO;
import org.ih.dao.hibernate.SchoolDAO;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.District;
import org.ih.dto.School;
import org.ih.school.Schools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Districts {

    private final DistrictDAO dao;
    private final SchoolDAO schoolDAO;

    public Districts() {
        this.dao = DAOFactory.getDistrictDAO();
        this.schoolDAO = DAOFactory.getSchoolDAO();
    }

    public District create(District district) {
        DistrictModel model = new DistrictModel();
        model.setDescription(district.getDescription());
        model.setLabel(district.getLabel());
        return this.dao.create(model).toDataObject();
    }

    public Results<District> list(int offset, int limit, boolean asc, String sort, String filter) {
        Results<District> resultData = new Results<>();
        List<DistrictModel> results = this.dao.list(sort, asc, offset, limit);
        for (DistrictModel model : results) {
            resultData.getRequested().add(model.toDataObject());
        }

        resultData.setAvailable(this.dao.listCount());
        return resultData;
    }

    public List<School> filterSchools(long districtId, String filter, int limit) {
        List<SchoolModel> results = this.schoolDAO.filterSchoolsByDistrict(districtId, filter, limit);
        List<School> resultData = new ArrayList<>();
        for (SchoolModel model : results)
            resultData.add(model.toDataObject());
        return resultData;
    }

    public Results<School> schools(long districtId, int offset, int limit, boolean asc, String sort) {
        Results<School> results = new Results<>();
        List<SchoolModel> models = this.schoolDAO.pageDistrictSchools(districtId, offset, limit, sort, asc, null);
        for (SchoolModel model : models) {
            results.getRequested().add(model.toDataObject());
        }
        long available = this.schoolDAO.districtSchoolsCount(districtId, null);
        results.setAvailable(available);
        return results;
    }

    public void importSchools(long districtId, InputStream inputStream) {

        DistrictModel districtModel = this.dao.get(districtId);
        if (districtModel == null)
            throw new IllegalArgumentException("Cannot find district for id " + districtId);

        //expected file format
        // School Name, Address, Phone, Grades, School Type
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        Schools schools = new Schools();

        for (String[] next : reader) {
            if (next.length != 5) {
                throw new IllegalArgumentException("Cannot import file. Improper format");
            }

            String name = next[0];
            String address = next[1];
            String phone = next[2];
            String grades = next[3];
            String type = next[4];

            School school = new School();
            school.setDistrict(districtModel.toDataObject());
            school.setName(name);
            school.setAddress(address);
            school.setPhone(phone);
            school.setGrades(grades);
            school.setDescription(type);

            schools.create(school);
        }
    }
}
