package com.example.screensData.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Alert Actions data from ALERT_ACTIONS table
 */
public class GetAlertActionsData {

    private static final Logger logger = Logger.getLogger(GetAlertActionsData.class);
    private OracleDBConnection dbConnection;

    // Alert actions data fields
    private String actionId;
    private String actionName;
    private String actionType;
    private String actionDescription;
    private String isActive;
    private String createdDate;

    private List<Map<String, Object>> allAlertActionsData;

    public GetAlertActionsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAlertActionsData = new ArrayList<>();
    }

    public boolean fetchAllAlertActions() {
        try {
            logger.info("Fetching all alert actions data");
            String query = "SELECT * FROM ALERT_ACTIONS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertActionsData = results;
                parseAlertActionsData(results.get(0));
                logger.info("Alert actions data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert actions data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertActionById(String actionId) {
        try {
            logger.info("Fetching alert action by ID: " + actionId);
            String query = "SELECT * FROM ALERT_ACTIONS WHERE ACTION_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, actionId);

            if (!results.isEmpty()) {
                allAlertActionsData = results;
                parseAlertActionsData(results.get(0));
                logger.info("Alert action data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert action data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseAlertActionsData(Map<String, Object> row) {
        this.actionId = row.get("ACTION_ID") != null ? row.get("ACTION_ID").toString() : "";
        this.actionName = row.get("ACTION_NAME") != null ? row.get("ACTION_NAME").toString() : "";
        this.actionType = row.get("ACTION_TYPE") != null ? row.get("ACTION_TYPE").toString() : "";
        this.actionDescription = row.get("ACTION_DESCRIPTION") != null ? row.get("ACTION_DESCRIPTION").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
    }

    public void printAlertActionsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         Alert Actions Table                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-20s ║ %-15s ║ %-10s ║%n", "ACTION_ID", "ACTION_NAME", "ACTION_TYPE", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allAlertActionsData) {
            String id = record.get("ACTION_ID") != null ? record.get("ACTION_ID").toString() : "";
            String name = record.get("ACTION_NAME") != null ? record.get("ACTION_NAME").toString() : "";
            String type = record.get("ACTION_TYPE") != null ? record.get("ACTION_TYPE").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            System.out.printf("║ %-12s ║ %-20s ║ %-15s ║ %-10s ║%n", id, name, type, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allAlertActionsData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllAlertActionsRecords() { return allAlertActionsData; }
    public int getRecordCount() { return allAlertActionsData.size(); }
    public String getActionId() { return actionId; }
    public String getActionName() { return actionName; }
    public String getActionType() { return actionType; }
    public String getActionDescription() { return actionDescription; }
    public String getIsActive() { return isActive; }
    public String getCreatedDate() { return createdDate; }
}
