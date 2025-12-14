package com.example.screensData.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Category data from XDP_LKUP_CATEGORY table
 */
public class GetLkupCategoryData {

    private static final Logger logger = Logger.getLogger(GetLkupCategoryData.class);
    private OracleDBConnection dbConnection;

    // Lookup Category data fields - All 2 columns from XDP_LKUP_CATEGORY table
    private String categoryCode;  // VARCHAR2
    private String categoryDesc;  // VARCHAR2

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupCategoryData;

    public GetLkupCategoryData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupCategoryData = new ArrayList<>();
    }

    public boolean fetchAllLkupCategory() {
        try {
            logger.info("Fetching all lookup category data from XDP_LKUP_CATEGORY");
            String query = "SELECT * FROM XDP_LKUP_CATEGORY";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCategoryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup category data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup category data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup category data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupCategoryByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup category data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_CATEGORY WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCategoryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup category data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup category data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup category data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup category data from database row - All 2 XDP_LKUP_CATEGORY columns
     * @param row Database row containing lookup category data
     */
    private void parseData(Map<String, Object> row) {
        this.categoryCode = row.get("CATEGORY_CODE") != null ? row.get("CATEGORY_CODE").toString() : "";
        this.categoryDesc = row.get("CATEGORY_DESC") != null ? row.get("CATEGORY_DESC").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupCategoryRecords() {
        return allLkupCategoryData;
    }

    public Map<String, Object> getLkupCategoryRecordByIndex(int index) {
        if (index >= 0 && index < allLkupCategoryData.size()) {
            return allLkupCategoryData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupCategoryData.size();
    }

    public void printAllLkupCategoryData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║        All Lkup Category Records (" + allLkupCategoryData.size() + " total)             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupCategoryData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupCategoryData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 2 XDP_LKUP_CATEGORY columns
    public String getCategoryCode() { return categoryCode; }
    public String getCategoryDesc() { return categoryDesc; }

}
