package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Instrument Country data from XDP_LKUP_INST_CNTRY table
 */
public class GetLkupInstCntryData {

    private static final Logger logger = Logger.getLogger(GetLkupInstCntryData.class);
    private OracleDBConnection dbConnection;

    // Lookup Instrument Country data fields - All 4 columns from XDP_LKUP_INST_CNTRY table
    private String countryCode;  // VARCHAR2
    private String countryName;  // VARCHAR2
    private String countryMic;  // VARCHAR2
    private String seq;  // NUMBER

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupInstCntryData;

    public GetLkupInstCntryData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupInstCntryData = new ArrayList<>();
    }

    public boolean fetchAllLkupInstCntry() {
        try {
            logger.info("Fetching all lookup instrument country data from XDP_LKUP_INST_CNTRY");
            String query = "SELECT * FROM XDP_LKUP_INST_CNTRY";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupInstCntryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup instrument country data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup instrument country data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup instrument country data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupInstCntryByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup instrument country data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_INST_CNTRY WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupInstCntryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup instrument country data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup instrument country data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup instrument country data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup instrument country data from database row - All 4 XDP_LKUP_INST_CNTRY columns
     * @param row Database row containing lookup instrument country data
     */
    private void parseData(Map<String, Object> row) {
        this.countryCode = row.get("COUNTRY_CODE") != null ? row.get("COUNTRY_CODE").toString() : "";
        this.countryName = row.get("COUNTRY_NAME") != null ? row.get("COUNTRY_NAME").toString() : "";
        this.countryMic = row.get("COUNTRY_MIC") != null ? row.get("COUNTRY_MIC").toString() : "";
        this.seq = row.get("SEQ") != null ? row.get("SEQ").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupInstCntryRecords() {
        return allLkupInstCntryData;
    }

    public Map<String, Object> getLkupInstCntryRecordByIndex(int index) {
        if (index >= 0 && index < allLkupInstCntryData.size()) {
            return allLkupInstCntryData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupInstCntryData.size();
    }

    public void printAllLkupInstCntryData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     All Lkup Inst Cntry Records (" + allLkupInstCntryData.size() + " total)            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupInstCntryData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupInstCntryData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 4 XDP_LKUP_INST_CNTRY columns
    public String getCountryCode() { return countryCode; }
    public String getCountryName() { return countryName; }
    public String getCountryMic() { return countryMic; }
    public String getSeq() { return seq; }

}
