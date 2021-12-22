package org.ih.dao;

import org.ih.dao.hibernate.*;

/**
 * Instantiates various data access objects as needed. To be replaced by DI
 *
 * @author Hector Plahar
 */
public class DAOFactory {

    private static AccountDAO accountDAO;
    private static AuditDAO auditDAO;
    private static GroupDAO groupDAO;
    private static ConfigurationDAO configurationDAO;
    private static DistrictDAO districtDAO;
    private static SchoolDAO schoolDAO;
    private static IncidentReportDAO incidentReportDAO;
    private static HygieneDAO hygieneDAO;
    private static SurveyDAO surveyDAO;
    private static QuestionDAO questionDAO;
    private static PatientDAO patientDAO;
    private static TestDAO testDAO;
    private static FileStorageDAO fileStorageDAO;
    private static LabTestDAO labTestDAO;

    public static AccountDAO getAccountDAO() {
        if (accountDAO == null)
            accountDAO = new AccountDAO();
        return accountDAO;
    }

    public static AuditDAO getAuditDAO() {
        if (auditDAO == null)
            auditDAO = new AuditDAO();
        return auditDAO;
    }

    public static GroupDAO getGroupDAO() {
        if (groupDAO == null)
            groupDAO = new GroupDAO();
        return groupDAO;
    }

    public static ConfigurationDAO getConfigurationDAO() {
        if (configurationDAO == null)
            configurationDAO = new ConfigurationDAO();
        return configurationDAO;
    }

    public static DistrictDAO getDistrictDAO() {
        if (districtDAO == null)
            districtDAO = new DistrictDAO();
        return districtDAO;
    }

    public static IncidentReportDAO getIncidentReportDAO() {
        if (incidentReportDAO == null)
            incidentReportDAO = new IncidentReportDAO();
        return incidentReportDAO;
    }

    public static SchoolDAO getSchoolDAO() {
        if (schoolDAO == null)
            schoolDAO = new SchoolDAO();
        return schoolDAO;
    }

    public static HygieneDAO getHygieneDAO() {
        if (hygieneDAO == null)
            hygieneDAO = new HygieneDAO();
        return hygieneDAO;
    }

    public static SurveyDAO getSurveyDAO() {
        if (surveyDAO == null)
            surveyDAO = new SurveyDAO();
        return surveyDAO;
    }

    public static QuestionDAO getQuestionDAO() {
        if (questionDAO == null)
            questionDAO = new QuestionDAO();
        return questionDAO;
    }

    public static PatientDAO getPatientDAO() {
        if (patientDAO == null)
            patientDAO = new PatientDAO();
        return patientDAO;
    }

    public static TestDAO getTestDAO() {
        if (testDAO == null)
            testDAO = new TestDAO();
        return testDAO;
    }

    public static FileStorageDAO getFileStorageDAO() {
        if (fileStorageDAO == null)
            fileStorageDAO = new FileStorageDAO();
        return fileStorageDAO;
    }

    public static LabTestDAO getLabTestDAO() {
        if (labTestDAO == null)
            labTestDAO = new LabTestDAO();
        return labTestDAO;
    }
}
