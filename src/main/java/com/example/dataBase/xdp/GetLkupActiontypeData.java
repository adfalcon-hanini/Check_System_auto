package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Action Type data from XDP_LKUP_ACTIONTYPE table
 */
public class GetLkupActiontypeData {

    private static final Logger logger = Logger.getLogger(GetLkupActiontypeData.class);
    private OracleDBConnection dbConnection;

    // Lookup Action Type data fields - All 2 columns from XDP_LKUP_ACTIONTYPE table
    private String actiontypeCode;  // VARCHAR2
    private String actiontypeDesc;  // VARCHAR2

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupActiontypeData;

    public GetLkupActiontypeData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupActiontypeData = new ArrayList<>();
    }

    public boolean fetchAllLkupActiontype() {
        try {
            logger.info("Fetching all lookup action type data from XDP_LKUP_ACTIONTYPE");
            String query = "SELECT * FROM XDP_LKUP_ACTIONTYPE";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupActiontypeData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup action type data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup action type data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup action type data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupActiontypeByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup action type data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_ACTIONTYPE WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupActiontypeData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup action type data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup action type data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup action type data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup action type data from database row - All 2 XDP_LKUP_ACTIONTYPE columns
     * @param row Database row containing lookup action type data
     */
    private void parseData(Map<String, Object> row) {
        this.actiontypeCode = row.get("ACTIONTYPE_CODE") != null ? row.get("ACTIONTYPE_CODE").toString() : "";
        this.actiontypeDesc = row.get("ACTIONTYPE_DESC") != null ? row.get("ACTIONTYPE_DESC").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupActiontypeRecords() {
        return allLkupActiontypeData;
    }

    public Map<String, Object> getLkupActiontypeRecordByIndex(int index) {
        if (index >= 0 && index < allLkupActiontypeData.size()) {
            return allLkupActiontypeData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupActiontypeData.size();
    }

    public void printAllLkupActiontypeData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║       All Lkup Actiontype Records (" + allLkupActiontypeData.size() + " total)           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupActiontypeData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupActiontypeData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 2 XDP_LKUP_ACTIONTYPE columns
    public String getActiontypeCode() { return actiontypeCode; }
    public String getActiontypeDesc() { return actiontypeDesc; }

}
