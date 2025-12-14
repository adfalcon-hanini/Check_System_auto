package com.example.screensData.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Corporate Event data from XDP_LKUP_CORP_EVENT table
 */
public class GetLkupCorpEventData {

    private static final Logger logger = Logger.getLogger(GetLkupCorpEventData.class);
    private OracleDBConnection dbConnection;

    // Lookup Corporate Event data fields - All 2 columns from XDP_LKUP_CORP_EVENT table
    private String eventtypeCode;  // VARCHAR2
    private String eventtypeDesc;  // VARCHAR2

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupCorpEventData;

    public GetLkupCorpEventData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupCorpEventData = new ArrayList<>();
    }

    public boolean fetchAllLkupCorpEvent() {
        try {
            logger.info("Fetching all lookup corporate event data from XDP_LKUP_CORP_EVENT");
            String query = "SELECT * FROM XDP_LKUP_CORP_EVENT";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCorpEventData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup corporate event data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup corporate event data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup corporate event data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupCorpEventByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup corporate event data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_CORP_EVENT WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCorpEventData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup corporate event data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup corporate event data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup corporate event data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup corporate event data from database row - All 2 XDP_LKUP_CORP_EVENT columns
     * @param row Database row containing lookup corporate event data
     */
    private void parseData(Map<String, Object> row) {
        this.eventtypeCode = row.get("EVENTTYPE_CODE") != null ? row.get("EVENTTYPE_CODE").toString() : "";
        this.eventtypeDesc = row.get("EVENTTYPE_DESC") != null ? row.get("EVENTTYPE_DESC").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupCorpEventRecords() {
        return allLkupCorpEventData;
    }

    public Map<String, Object> getLkupCorpEventRecordByIndex(int index) {
        if (index >= 0 && index < allLkupCorpEventData.size()) {
            return allLkupCorpEventData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupCorpEventData.size();
    }

    public void printAllLkupCorpEventData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║      All Lkup Corp Event Records (" + allLkupCorpEventData.size() + " total)            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupCorpEventData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupCorpEventData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 2 XDP_LKUP_CORP_EVENT columns
    public String getEventtypeCode() { return eventtypeCode; }
    public String getEventtypeDesc() { return eventtypeDesc; }

}
