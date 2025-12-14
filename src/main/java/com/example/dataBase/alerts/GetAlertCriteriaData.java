package com.example.dataBase.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Alert Criteria data from ALERT_CRITERIA table
 */
public class GetAlertCriteriaData {

    private static final Logger logger = Logger.getLogger(GetAlertCriteriaData.class);
    private OracleDBConnection dbConnection;

    // Alert criteria data fields
    private String criteriaId;
    private String criteriaName;
    private String criteriaType;
    private String criteriaDescription;
    private String condition;
    private String threshold;
    private String isActive;
    private String createdDate;

    private List<Map<String, Object>> allAlertCriteriaData;

    public GetAlertCriteriaData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAlertCriteriaData = new ArrayList<>();
    }

    public boolean fetchAllAlertCriteria() {
        try {
            logger.info("Fetching all alert criteria data");
            String query = "SELECT * FROM ALERT_CRITERIA";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertCriteriaData = results;
                parseAlertCriteriaData(results.get(0));
                logger.info("Alert criteria data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert criteria data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertCriteriaById(String criteriaId) {
        try {
            logger.info("Fetching alert criteria by ID: " + criteriaId);
            String query = "SELECT * FROM ALERT_CRITERIA WHERE CRITERIA_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, criteriaId);

            if (!results.isEmpty()) {
                allAlertCriteriaData = results;
                parseAlertCriteriaData(results.get(0));
                logger.info("Alert criteria data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert criteria data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertCriteriaByType(String criteriaType) {
        try {
            logger.info("Fetching alert criteria by type: " + criteriaType);
            String query = "SELECT * FROM ALERT_CRITERIA WHERE CRITERIA_TYPE = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, criteriaType);

            if (!results.isEmpty()) {
                allAlertCriteriaData = results;
                parseAlertCriteriaData(results.get(0));
                logger.info("Alert criteria data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert criteria data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseAlertCriteriaData(Map<String, Object> row) {
        this.criteriaId = row.get("CRITERIA_ID") != null ? row.get("CRITERIA_ID").toString() : "";
        this.criteriaName = row.get("CRITERIA_NAME") != null ? row.get("CRITERIA_NAME").toString() : "";
        this.criteriaType = row.get("CRITERIA_TYPE") != null ? row.get("CRITERIA_TYPE").toString() : "";
        this.criteriaDescription = row.get("CRITERIA_DESCRIPTION") != null ? row.get("CRITERIA_DESCRIPTION").toString() : "";
        this.condition = row.get("CONDITION") != null ? row.get("CONDITION").toString() : "";
        this.threshold = row.get("THRESHOLD") != null ? row.get("THRESHOLD").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
    }

    public void printAlertCriteriaTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              Alert Criteria Table                                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-20s ║ %-15s ║ %-15s ║ %-12s ║ %-10s ║%n",
                          "CRITERIA_ID", "CRITERIA_NAME", "TYPE", "CONDITION", "THRESHOLD", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allAlertCriteriaData) {
            String id = record.get("CRITERIA_ID") != null ? record.get("CRITERIA_ID").toString() : "";
            String name = record.get("CRITERIA_NAME") != null ? record.get("CRITERIA_NAME").toString() : "";
            String type = record.get("CRITERIA_TYPE") != null ? record.get("CRITERIA_TYPE").toString() : "";
            String cond = record.get("CONDITION") != null ? record.get("CONDITION").toString() : "";
            String thresh = record.get("THRESHOLD") != null ? record.get("THRESHOLD").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            if (name.length() > 20) name = name.substring(0, 17) + "...";

            System.out.printf("║ %-12s ║ %-20s ║ %-15s ║ %-15s ║ %-12s ║ %-10s ║%n",
                            id, name, type, cond, thresh, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allAlertCriteriaData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllAlertCriteriaRecords() { return allAlertCriteriaData; }
    public int getRecordCount() { return allAlertCriteriaData.size(); }
    public String getCriteriaId() { return criteriaId; }
    public String getCriteriaName() { return criteriaName; }
    public String getCriteriaType() { return criteriaType; }
    public String getCriteriaDescription() { return criteriaDescription; }
    public String getCondition() { return condition; }
    public String getThreshold() { return threshold; }
    public String getIsActive() { return isActive; }
    public String getCreatedDate() { return createdDate; }
}
