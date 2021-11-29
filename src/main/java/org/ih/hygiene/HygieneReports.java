package org.ih.hygiene;

import org.ih.common.Results;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.HygieneDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.DistrictModel;
import org.ih.dao.model.HygieneModel;
import org.ih.dao.model.SchoolModel;
import org.ih.dto.Hygiene;
import org.ih.dto.HygieneGraphData;
import org.ih.dto.Pair;

import java.time.ZonedDateTime;
import java.util.*;

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

    public List<HygieneGraphData> getGraphData(HygieneType type, int numberOfDays) {
        final ZonedDateTime startOfLastWeek = ZonedDateTime.now().minusDays(numberOfDays);
        List<HygieneModel> modelList = this.dao.recordsAfter(Date.from(startOfLastWeek.toInstant()).getTime(), type);
        HashMap<String, List<Pair>> map = new LinkedHashMap<>();

        // add the seven days
        for (int i = 1; i <= numberOfDays; i += 1) {
            Date date = Date.from(startOfLastWeek.plusDays(i).toInstant());
            List<Pair> graphData = new ArrayList<>(2);
            graphData.add(new Pair("Non-Compliance", 0));
            graphData.add(new Pair("Compliance", 0));
            map.put(getDateWithoutTime(date).toString().substring(0, 10), graphData);
        }

        // iterate model list
        for (HygieneModel model : modelList) {
            String dateString = getDateWithoutTime(model.getDate()).toString().substring(0, 10);
            List<Pair> graphData = map.get(dateString);
            if (graphData == null) {
                graphData = new ArrayList<>(2);
                graphData.add(new Pair("Non-Compliance", 0));
                graphData.add(new Pair("Compliance", 0));
            }

            // add data to graph data
            int index = model.getCompliant() ? 1 : 0;
            int value = graphData.get(index).getValue() + 1;
            graphData.get(index).setValue(value);
        }

        List<HygieneGraphData> data = new ArrayList<>();
        for (Map.Entry<String, List<Pair>> entry : map.entrySet()) {
            HygieneGraphData graphData = new HygieneGraphData();
            graphData.setName(entry.getKey());
            graphData.getSeries().add(entry.getValue().get(0));
            graphData.getSeries().add(entry.getValue().get(1));
            data.add(graphData);
        }

        return data;
    }

    public static Date getDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
