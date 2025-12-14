package com.example.screensData.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Alert Status data from ALERT_STATUS table
 */
public class GetAlertStatusData {

    private static final Logger logger = Logger.getLogger(GetAlertStatusData.class);
    private OracleDBConnection dbConnection;

    // Alert status data fields
    private String statusId;
    private String statusName;
    private String statusDescription;
    private String isActive;
    private String createdDate;
    private String modifiedDate;

    // Store all rows
    private List<Map<String, Object>> allAlertStatusData;

    public GetAlertStatusData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAlertStatusData = new ArrayList<>();
    }

    public boolean fetchAllAlertStatus() {
        try {
            logger.info("Fetching all alert status data");
            String query = "SELECT * FROM ALERT_STATUS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertStatusData = results;
                parseAlertStatusData(results.get(0));
                logger.info("Alert status data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert status data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertStatusById(String statusId) {
        try {
            logger.info("Fetching alert status by ID: " + statusId);
            String query = "SELECT * FROM ALERT_STATUS WHERE STATUS_ID = '" + statusId + "'";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertStatusData = results;
                parseAlertStatusData(results.get(0));
                logger.info("Alert status data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert status data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseAlertStatusData(Map<String, Object> row) {
        this.statusId = row.get("STATUS_ID") != null ? row.get("STATUS_ID").toString() : "";
        this.statusName = row.get("STATUS_NAME") != null ? row.get("STATUS_NAME").toString() : "";
        this.statusDescription = row.get("STATUS_DESCRIPTION") != null ? row.get("STATUS_DESCRIPTION").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
        this.modifiedDate = row.get("MODIFIED_DATE") != null ? row.get("MODIFIED_DATE").toString() : "";
    }

    public void printAlertStatusTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         Alert Status Table                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-20s ║ %-30s ║ %-10s ║%n", "STATUS_ID", "STATUS_NAME", "DESCRIPTION", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allAlertStatusData) {
            String id = record.get("STATUS_ID") != null ? record.get("STATUS_ID").toString() : "";
            String name = record.get("STATUS_NAME") != null ? record.get("STATUS_NAME").toString() : "";
            String desc = record.get("STATUS_DESCRIPTION") != null ? record.get("STATUS_DESCRIPTION").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            if (desc.length() > 30) desc = desc.substring(0, 27) + "...";

            System.out.printf("║ %-12s ║ %-20s ║ %-30s ║ %-10s ║%n", id, name, desc, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allAlertStatusData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllAlertStatusRecords() { return allAlertStatusData; }
    public int getRecordCount() { return allAlertStatusData.size(); }
    public String getStatusId() { return statusId; }
    public String getStatusName() { return statusName; }
    public String getStatusDescription() { return statusDescription; }
    public String getIsActive() { return isActive; }
    public String getCreatedDate() { return createdDate; }
    public String getModifiedDate() { return modifiedDate; }
}
