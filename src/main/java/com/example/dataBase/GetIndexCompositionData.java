package com.example.dataBase;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Index Composition data from XDP_INDEX_COMPOSITION table
 */
public class GetIndexCompositionData {

    private static final Logger logger = Logger.getLogger(GetIndexCompositionData.class);
    private OracleDBConnection dbConnection;

    // XDP_INDEX_COMPOSITION column fields
    private String instCode;  // VARCHAR2
    private String instrumentweight;  // FLOAT
    private String instrumentfactor;  // FLOAT
    private String previousclose;  // FLOAT
    private String numberofconponents;  // NUMBER
    private String instrumentidofcomponent;  // VARCHAR2
    private String mnemo;  // VARCHAR2
    private String indexfrequency;  // VARCHAR2
    private String seq;  // NUMBER

    private List<Map<String, Object>> allIndexCompositionData;

    public GetIndexCompositionData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allIndexCompositionData = new ArrayList<>();
    }

    public boolean fetchAllIndexComposition() {
        try {
            logger.info("Fetching all index composition data from XDP_INDEX_COMPOSITION");
            String query = "SELECT * FROM XDP_INDEX_COMPOSITION";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allIndexCompositionData = results;
                logger.info("Index composition data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No index composition data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching index composition data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchIndexCompositionByCondition(String whereClause) {
        try {
            logger.info("Fetching index composition data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_INDEX_COMPOSITION WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allIndexCompositionData = results;
                logger.info("Index composition data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No index composition data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching index composition data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllIndexCompositionRecords() {
        return allIndexCompositionData;
    }

    public Map<String, Object> getIndexCompositionRecordByIndex(int index) {
        if (index >= 0 && index < allIndexCompositionData.size()) {
            return allIndexCompositionData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allIndexCompositionData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.instCode = row.get("INST_CODE") != null ? row.get("INST_CODE").toString() : "";
        this.instrumentweight = row.get("INSTRUMENTWEIGHT") != null ? row.get("INSTRUMENTWEIGHT").toString() : "";
        this.instrumentfactor = row.get("INSTRUMENTFACTOR") != null ? row.get("INSTRUMENTFACTOR").toString() : "";
        this.previousclose = row.get("PREVIOUSCLOSE") != null ? row.get("PREVIOUSCLOSE").toString() : "";
        this.numberofconponents = row.get("NUMBEROFCONPONENTS") != null ? row.get("NUMBEROFCONPONENTS").toString() : "";
        this.instrumentidofcomponent = row.get("INSTRUMENTIDOFCOMPONENT") != null ? row.get("INSTRUMENTIDOFCOMPONENT").toString() : "";
        this.mnemo = row.get("MNEMO") != null ? row.get("MNEMO").toString() : "";
        this.indexfrequency = row.get("INDEXFREQUENCY") != null ? row.get("INDEXFREQUENCY").toString() : "";
        this.seq = row.get("SEQ") != null ? row.get("SEQ").toString() : "";
    }

    // Getters and Setters
    public String getInstCode() {
        return instCode;
    }
    public String getInstrumentweight() {
        return instrumentweight;
    }
    public String getInstrumentfactor() {
        return instrumentfactor;
    }
    public String getPreviousclose() {
        return previousclose;
    }
    public String getNumberofconponents() {
        return numberofconponents;
    }
    public String getInstrumentidofcomponent() {
        return instrumentidofcomponent;
    }
    public String getMnemo() {
        return mnemo;
    }
    public String getIndexfrequency() {
        return indexfrequency;
    }
    public String getSeq() {
        return seq;
    }
    public void printAllIndexCompositionData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║        All Index Composition Records (" + allIndexCompositionData.size() + " total)       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allIndexCompositionData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allIndexCompositionData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
