package org.ih.incident;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.IncidentReportDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.IncidentReportModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.IncidentReport;
import org.ih.util.StringUtil;

import java.util.Date;
import java.util.List;

public class Incidents {

    private final String userId;
    private final IncidentReportDAO dao;

    public Incidents(String userId) {
        this.userId = userId;
        this.dao = DAOFactory.getIncidentReportDAO();
    }

    public IncidentReport createReport(IncidentReport report) {
        if (StringUtil.isEmpty(report.getDetails()))
            throw new IllegalArgumentException("Cannot create report without details");

        IncidentReportModel model = new IncidentReportModel();
        model.setDate(new Date());
        model.setDetails(report.getDetails());

        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(userId);
        model.setReporter(accountModel);
        model.setDepartment(report.getDepartment());
        model.setLocation(report.getLocation());
        model.setDate(new Date(report.getDateTime()));
        model.setSupervisor(report.getSupervisor());
        model.setAdditionalDetails(report.getAdditionalDetails());

        // get district
        if (report.getDistrict() != null) {
            DistrictModel districtModel = DAOFactory.getDistrictDAO().get(report.getDistrict().getId());
            if (districtModel == null)
                throw new IllegalArgumentException("Cannot create report without district");
            model.setDistrict(districtModel);

            // get school
            if (report.getSchool() != null) {
                SchoolModel schoolModel = DAOFactory.getSchoolDAO().get(report.getSchool().getId());
                if (schoolModel == null)
                    throw new IllegalArgumentException("Cannot create report without school");
                model.setSchool(schoolModel);
            }
        }

        model = this.dao.create(model);
        return model.toDataObject();
    }

    public Results<IncidentReport> list(int offset, int limit, boolean asc, String sort) {
        List<IncidentReportModel> models = dao.list(sort, asc, offset, limit);
        long count = dao.listCount();

        Results<IncidentReport> results = new Results<>();
        results.setAvailable(count);
        for (IncidentReportModel model : models) {
            results.getRequested().add(model.toDataObject());
        }

        return results;
    }
}
