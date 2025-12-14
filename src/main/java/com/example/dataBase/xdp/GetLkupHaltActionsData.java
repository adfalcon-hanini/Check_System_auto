package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Halt Actions data from XDP_LKUP_HALT_ACTIONS table
 */
public class GetLkupHaltActionsData {

    private static final Logger logger = Logger.getLogger(GetLkupHaltActionsData.class);
    private OracleDBConnection dbConnection;

    // Lookup Halt Actions data fields - All 2 columns from XDP_LKUP_HALT_ACTIONS table
    private String haltActionCode;  // VARCHAR2
    private String haltActionDesc;  // VARCHAR2

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupHaltActionsData;

    public GetLkupHaltActionsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupHaltActionsData = new ArrayList<>();
    }

    public boolean fetchAllLkupHaltActions() {
        try {
            logger.info("Fetching all lookup halt actions data from XDP_LKUP_HALT_ACTIONS");
            String query = "SELECT * FROM XDP_LKUP_HALT_ACTIONS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupHaltActionsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup halt actions data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup halt actions data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup halt actions data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupHaltActionsByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup halt actions data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_HALT_ACTIONS WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupHaltActionsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup halt actions data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup halt actions data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup halt actions data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup halt actions data from database row - All 2 XDP_LKUP_HALT_ACTIONS columns
     * @param row Database row containing lookup halt actions data
     */
    private void parseData(Map<String, Object> row) {
        this.haltActionCode = row.get("HALT_ACTION_CODE") != null ? row.get("HALT_ACTION_CODE").toString() : "";
        this.haltActionDesc = row.get("HALT_ACTION_DESC") != null ? row.get("HALT_ACTION_DESC").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupHaltActionsRecords() {
        return allLkupHaltActionsData;
    }

    public Map<String, Object> getLkupHaltActionsRecordByIndex(int index) {
        if (index >= 0 && index < allLkupHaltActionsData.size()) {
            return allLkupHaltActionsData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupHaltActionsData.size();
    }

    public void printAllLkupHaltActionsData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     All Lkup Halt Actions Records (" + allLkupHaltActionsData.size() + " total)         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupHaltActionsData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupHaltActionsData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 2 XDP_LKUP_HALT_ACTIONS columns
    public String getHaltActionCode() { return haltActionCode; }
    public String getHaltActionDesc() { return haltActionDesc; }

}
