package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Header data from XDP_LKUP_HEADER table
 */
public class GetLkupHeaderData {

    private static final Logger logger = Logger.getLogger(GetLkupHeaderData.class);
    private OracleDBConnection dbConnection;

    // Lookup Header data fields - All 4 columns from XDP_LKUP_HEADER table
    private String msgCode;  // VARCHAR2
    private String msgType;  // VARCHAR2
    private String msgName;  // VARCHAR2
    private String msgDir;  // CHAR

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupHeaderData;

    public GetLkupHeaderData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupHeaderData = new ArrayList<>();
    }

    public boolean fetchAllLkupHeader() {
        try {
            logger.info("Fetching all lookup header data from XDP_LKUP_HEADER");
            String query = "SELECT * FROM XDP_LKUP_HEADER";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupHeaderData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup header data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup header data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup header data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupHeaderByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup header data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_HEADER WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupHeaderData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup header data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup header data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup header data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup header data from database row - All 4 XDP_LKUP_HEADER columns
     * @param row Database row containing lookup header data
     */
    private void parseData(Map<String, Object> row) {
        this.msgCode = row.get("MSG_CODE") != null ? row.get("MSG_CODE").toString() : "";
        this.msgType = row.get("MSG_TYPE") != null ? row.get("MSG_TYPE").toString() : "";
        this.msgName = row.get("MSG_NAME") != null ? row.get("MSG_NAME").toString() : "";
        this.msgDir = row.get("MSG_DIR") != null ? row.get("MSG_DIR").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupHeaderRecords() {
        return allLkupHeaderData;
    }

    public Map<String, Object> getLkupHeaderRecordByIndex(int index) {
        if (index >= 0 && index < allLkupHeaderData.size()) {
            return allLkupHeaderData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupHeaderData.size();
    }

    public void printAllLkupHeaderData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║        All Lkup Header Records (" + allLkupHeaderData.size() + " total)                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupHeaderData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupHeaderData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 4 XDP_LKUP_HEADER columns
    public String getMsgCode() { return msgCode; }
    public String getMsgType() { return msgType; }
    public String getMsgName() { return msgName; }
    public String getMsgDir() { return msgDir; }

}
