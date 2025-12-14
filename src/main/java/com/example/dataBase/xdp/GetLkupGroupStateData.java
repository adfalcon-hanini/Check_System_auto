package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Group State data from XDP_LKUP_GROUP_STATE table
 */
public class GetLkupGroupStateData {

    private static final Logger logger = Logger.getLogger(GetLkupGroupStateData.class);
    private OracleDBConnection dbConnection;

    // Lookup Group State data fields - All 2 columns from XDP_LKUP_GROUP_STATE table
    private String stateCode;  // VARCHAR2
    private String stateDesc;  // VARCHAR2

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupGroupStateData;

    public GetLkupGroupStateData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupGroupStateData = new ArrayList<>();
    }

    public boolean fetchAllLkupGroupState() {
        try {
            logger.info("Fetching all lookup group state data from XDP_LKUP_GROUP_STATE");
            String query = "SELECT * FROM XDP_LKUP_GROUP_STATE";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupGroupStateData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup group state data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup group state data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup group state data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupGroupStateByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup group state data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_GROUP_STATE WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupGroupStateData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup group state data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup group state data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup group state data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup group state data from database row - All 2 XDP_LKUP_GROUP_STATE columns
     * @param row Database row containing lookup group state data
     */
    private void parseData(Map<String, Object> row) {
        this.stateCode = row.get("STATE_CODE") != null ? row.get("STATE_CODE").toString() : "";
        this.stateDesc = row.get("STATE_DESC") != null ? row.get("STATE_DESC").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupGroupStateRecords() {
        return allLkupGroupStateData;
    }

    public Map<String, Object> getLkupGroupStateRecordByIndex(int index) {
        if (index >= 0 && index < allLkupGroupStateData.size()) {
            return allLkupGroupStateData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupGroupStateData.size();
    }

    public void printAllLkupGroupStateData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║      All Lkup Group State Records (" + allLkupGroupStateData.size() + " total)          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupGroupStateData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupGroupStateData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 2 XDP_LKUP_GROUP_STATE columns
    public String getStateCode() { return stateCode; }
    public String getStateDesc() { return stateDesc; }

}
