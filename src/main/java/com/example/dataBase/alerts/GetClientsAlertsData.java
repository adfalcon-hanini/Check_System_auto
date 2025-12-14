package com.example.dataBase.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Clients Alerts data from CLIENTSALERTS table
 */
public class GetClientsAlertsData {

    private static final Logger logger = Logger.getLogger(GetClientsAlertsData.class);
    private OracleDBConnection dbConnection;

    // Clients alerts data fields
    private String alertId;
    private String clientId;
    private String alertType;
    private String symbolId;
    private String criteriaId;
    private String actionId;
    private String statusId;
    private String triggerDate;
    private String createdDate;
    private String modifiedDate;
    private String isActive;

    private List<Map<String, Object>> allClientsAlertsData;

    public GetClientsAlertsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allClientsAlertsData = new ArrayList<>();
    }

    public boolean fetchAllClientsAlerts() {
        try {
            logger.info("Fetching all clients alerts data");
            String query = "SELECT * FROM CLIENTSALERTS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Clients alerts data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching clients alerts data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchClientsAlertsByClientId(String clientId) {
        try {
            logger.info("Fetching clients alerts by client ID: " + clientId);
            String query = "SELECT * FROM CLIENTSALERTS WHERE CL_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Clients alerts data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No clients alerts found for client ID: " + clientId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching clients alerts data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchClientsAlertById(String alertId) {
        try {
            logger.info("Fetching client alert by ID: " + alertId);
            String query = "SELECT * FROM CLIENTSALERTS WHERE ALERT_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, alertId);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Client alert data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching client alert data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchActiveClientsAlertsByClientId(String clientId) {
        try {
            logger.info("Fetching active clients alerts by client ID: " + clientId);
            String query = "SELECT * FROM CLIENTSALERTS WHERE CL_ID = ? AND IS_ACTIVE = 'Y'";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Active clients alerts data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No active clients alerts found for client ID: " + clientId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching active clients alerts data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchClientsAlertsByStatus(String statusId) {
        try {
            logger.info("Fetching clients alerts by status: " + statusId);
            String query = "SELECT * FROM CLIENTSALERTS WHERE STATUS_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, statusId);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Clients alerts data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching clients alerts data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch active alerts by company code and date
     * @param companyCode Company code (SYMBOLID)
     * @param date Date in format dd-MM-yyyy
     * @return true if data found, false otherwise
     */
    public boolean fetchActiveAlertsByCompanyAndDate(String companyCode, String date) {
        try {
            logger.info("Fetching active alerts for Company: " + companyCode + " and Date: " + date);

            String query = "SELECT * FROM CLIENTSALERTS " +
                          "WHERE SYMBOLID = ? " +
                          "AND TRUNC(CREATION_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "AND STATUSID = 1 " +
                          "ORDER BY CREATION_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, date);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Active alerts fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No active alerts found for Company: " + companyCode + " and Date: " + date);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching active alerts: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch alerts by company code, date, and status (flexible status)
     * @param companyCode Company code (SYMBOLID)
     * @param date Date in format dd-MM-yyyy
     * @param statusId Status ID (1 for active, other values for different statuses)
     * @return true if data found, false otherwise
     */
    public boolean fetchAlertsByCompanyDateAndStatus(String companyCode, String date, String statusId) {
        try {
            logger.info("Fetching alerts for Company: " + companyCode +
                       ", Date: " + date + ", StatusID: " + statusId);

            String query = "SELECT * FROM CLIENTSALERTS " +
                          "WHERE SYMBOLID = ? " +
                          "AND TRUNC(CREATION_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "AND STATUSID = ? " +
                          "ORDER BY CREATION_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, date, statusId);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Alerts fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No alerts found for the specified parameters");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching alerts: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all active alerts by company code (no date filter)
     * @param companyCode Company code (SYMBOLID)
     * @return true if data found, false otherwise
     */
    public boolean fetchActiveAlertsByCompany(String companyCode) {
        try {
            logger.info("Fetching all active alerts for Company: " + companyCode);

            String query = "SELECT * FROM CLIENTSALERTS " +
                          "WHERE SYMBOLID = ? " +
                          "AND STATUSID = 1 " +
                          "ORDER BY CREATION_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode);

            if (!results.isEmpty()) {
                allClientsAlertsData = results;
                parseClientsAlertsData(results.get(0));
                logger.info("Active alerts fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No active alerts found for Company: " + companyCode);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching active alerts: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseClientsAlertsData(Map<String, Object> row) {
        this.alertId = row.get("ALERT_ID") != null ? row.get("ALERT_ID").toString() : "";
        this.clientId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
        this.alertType = row.get("ALERT_TYPE") != null ? row.get("ALERT_TYPE").toString() : "";
        this.symbolId = row.get("SYMBOL_ID") != null ? row.get("SYMBOL_ID").toString() : "";
        this.criteriaId = row.get("CRITERIA_ID") != null ? row.get("CRITERIA_ID").toString() : "";
        this.actionId = row.get("ACTION_ID") != null ? row.get("ACTION_ID").toString() : "";
        this.statusId = row.get("STATUS_ID") != null ? row.get("STATUS_ID").toString() : "";
        this.triggerDate = row.get("TRIGGER_DATE") != null ? row.get("TRIGGER_DATE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
        this.modifiedDate = row.get("MODIFIED_DATE") != null ? row.get("MODIFIED_DATE").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
    }

    public void printClientsAlertsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    Clients Alerts Table                                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-10s ║ %-15s ║ %-12s ║ %-12s ║ %-12s ║ %-12s ║ %-10s ║%n",
                          "ALERT_ID", "CL_ID", "ALERT_TYPE", "SYMBOL_ID", "CRITERIA_ID", "ACTION_ID", "STATUS_ID", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allClientsAlertsData) {
            String alertId = record.get("ALERT_ID") != null ? record.get("ALERT_ID").toString() : "";
            String clId = record.get("CL_ID") != null ? record.get("CL_ID").toString() : "";
            String type = record.get("ALERT_TYPE") != null ? record.get("ALERT_TYPE").toString() : "";
            String symbolId = record.get("SYMBOL_ID") != null ? record.get("SYMBOL_ID").toString() : "";
            String criteriaId = record.get("CRITERIA_ID") != null ? record.get("CRITERIA_ID").toString() : "";
            String actionId = record.get("ACTION_ID") != null ? record.get("ACTION_ID").toString() : "";
            String statusId = record.get("STATUS_ID") != null ? record.get("STATUS_ID").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            System.out.printf("║ %-12s ║ %-10s ║ %-15s ║ %-12s ║ %-12s ║ %-12s ║ %-12s ║ %-10s ║%n",
                            alertId, clId, type, symbolId, criteriaId, actionId, statusId, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allClientsAlertsData.size());
    }

    public void printClientsAlertsSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Clients Alerts Summary                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allClientsAlertsData.isEmpty()) {
            System.out.println("No clients alerts found.");
            return;
        }

        int activeAlerts = 0;
        int triggeredAlerts = 0;

        for (Map<String, Object> record : allClientsAlertsData) {
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";
            if ("Y".equalsIgnoreCase(active)) {
                activeAlerts++;
            }

            String triggerDate = record.get("TRIGGER_DATE") != null ? record.get("TRIGGER_DATE").toString() : "";
            if (!triggerDate.isEmpty()) {
                triggeredAlerts++;
            }
        }

        System.out.println("\nTotal Alerts: " + allClientsAlertsData.size());
        System.out.println("Active Alerts: " + activeAlerts);
        System.out.println("Triggered Alerts: " + triggeredAlerts);
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    // Getters
    public List<Map<String, Object>> getAllClientsAlertsRecords() { return allClientsAlertsData; }
    public int getRecordCount() { return allClientsAlertsData.size(); }
    public String getAlertId() { return alertId; }
    public String getClientId() { return clientId; }
    public String getAlertType() { return alertType; }
    public String getSymbolId() { return symbolId; }
    public String getCriteriaId() { return criteriaId; }
    public String getActionId() { return actionId; }
    public String getStatusId() { return statusId; }
    public String getTriggerDate() { return triggerDate; }
    public String getCreatedDate() { return createdDate; }
    public String getModifiedDate() { return modifiedDate; }
    public String getIsActive() { return isActive; }
}
