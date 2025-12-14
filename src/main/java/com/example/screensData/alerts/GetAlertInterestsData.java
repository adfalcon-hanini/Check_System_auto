package com.example.screensData.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Alert Interests data from ALERT_INTERESTS table
 */
public class GetAlertInterestsData {

    private static final Logger logger = Logger.getLogger(GetAlertInterestsData.class);
    private OracleDBConnection dbConnection;

    // Alert interests data fields
    private String interestId;
    private String interestName;
    private String interestDescription;
    private String category;
    private String isActive;
    private String createdDate;

    private List<Map<String, Object>> allAlertInterestsData;

    public GetAlertInterestsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAlertInterestsData = new ArrayList<>();
    }

    public boolean fetchAllAlertInterests() {
        try {
            logger.info("Fetching all alert interests data");
            String query = "SELECT * FROM ALERT_INTERESTS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertInterestsData = results;
                parseAlertInterestsData(results.get(0));
                logger.info("Alert interests data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert interests data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertInterestById(String interestId) {
        try {
            logger.info("Fetching alert interest by ID: " + interestId);
            String query = "SELECT * FROM ALERT_INTERESTS WHERE INTEREST_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, interestId);

            if (!results.isEmpty()) {
                allAlertInterestsData = results;
                parseAlertInterestsData(results.get(0));
                logger.info("Alert interest data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert interest data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseAlertInterestsData(Map<String, Object> row) {
        this.interestId = row.get("INTEREST_ID") != null ? row.get("INTEREST_ID").toString() : "";
        this.interestName = row.get("INTEREST_NAME") != null ? row.get("INTEREST_NAME").toString() : "";
        this.interestDescription = row.get("INTEREST_DESCRIPTION") != null ? row.get("INTEREST_DESCRIPTION").toString() : "";
        this.category = row.get("CATEGORY") != null ? row.get("CATEGORY").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
    }

    public void printAlertInterestsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       Alert Interests Table                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-25s ║ %-15s ║ %-10s ║%n", "INTEREST_ID", "INTEREST_NAME", "CATEGORY", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allAlertInterestsData) {
            String id = record.get("INTEREST_ID") != null ? record.get("INTEREST_ID").toString() : "";
            String name = record.get("INTEREST_NAME") != null ? record.get("INTEREST_NAME").toString() : "";
            String cat = record.get("CATEGORY") != null ? record.get("CATEGORY").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            if (name.length() > 25) name = name.substring(0, 22) + "...";

            System.out.printf("║ %-12s ║ %-25s ║ %-15s ║ %-10s ║%n", id, name, cat, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allAlertInterestsData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllAlertInterestsRecords() { return allAlertInterestsData; }
    public int getRecordCount() { return allAlertInterestsData.size(); }
    public String getInterestId() { return interestId; }
    public String getInterestName() { return interestName; }
    public String getInterestDescription() { return interestDescription; }
    public String getCategory() { return category; }
    public String getIsActive() { return isActive; }
    public String getCreatedDate() { return createdDate; }
}
