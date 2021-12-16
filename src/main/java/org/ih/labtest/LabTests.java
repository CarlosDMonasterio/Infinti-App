package org.ih.labtest;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.LabTestDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.LabTestModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.LabTest;
import org.ih.notification.NotificationTask;
import org.ih.task.TaskRunner;

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

        // send email if positive test
        if (test.getResult() == LabTestResult.POSITIVE || test.getResult() == LabTestResult.INCONCLUSIVE)
            sendEmailNotification();

        return model.toDataObject();
    }

    private void sendEmailNotification() {
        String subject = "Infiniti Health COVID-19 Test result alert";
        String stringBuilder = """
                Hello,\s

                A user has reported a positive or inconclusive COVID-19 test result.
                Test result submissions are located at:

                https://infinitihealth.tech/reports/surveys

                Thank you!

                """;

        NotificationTask notificationTask = new NotificationTask();
        notificationTask.addInformation("infectionprevention@infinitihealth.org", subject, stringBuilder);
        TaskRunner.getInstance().runTask(notificationTask);
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
