package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Lookup Currency data from XDP_LKUP_CURRENCY table
 */
public class GetLkupCurrencyData {

    private static final Logger logger = Logger.getLogger(GetLkupCurrencyData.class);
    private OracleDBConnection dbConnection;

    // Lookup Currency data fields - All 4 columns from XDP_LKUP_CURRENCY table
    private String currencyCode;  // VARCHAR2
    private String decimalConvRule;  // NUMBER
    private String currencyDesc;  // VARCHAR2
    private String decimalSize;  // NUMBER

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allLkupCurrencyData;

    public GetLkupCurrencyData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allLkupCurrencyData = new ArrayList<>();
    }

    public boolean fetchAllLkupCurrency() {
        try {
            logger.info("Fetching all lookup currency data from XDP_LKUP_CURRENCY");
            String query = "SELECT * FROM XDP_LKUP_CURRENCY";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCurrencyData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup currency data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup currency data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup currency data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchLkupCurrencyByCondition(String whereClause) {
        try {
            logger.info("Fetching lookup currency data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_LKUP_CURRENCY WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allLkupCurrencyData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Lookup currency data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No lookup currency data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching lookup currency data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse lookup currency data from database row - All 4 XDP_LKUP_CURRENCY columns
     * @param row Database row containing lookup currency data
     */
    private void parseData(Map<String, Object> row) {
        this.currencyCode = row.get("CURRENCY_CODE") != null ? row.get("CURRENCY_CODE").toString() : "";
        this.decimalConvRule = row.get("DECIMAL_CONV_RULE") != null ? row.get("DECIMAL_CONV_RULE").toString() : "";
        this.currencyDesc = row.get("CURRENCY_DESC") != null ? row.get("CURRENCY_DESC").toString() : "";
        this.decimalSize = row.get("DECIMAL_SIZE") != null ? row.get("DECIMAL_SIZE").toString() : "";
    }

    public List<Map<String, Object>> getAllLkupCurrencyRecords() {
        return allLkupCurrencyData;
    }

    public Map<String, Object> getLkupCurrencyRecordByIndex(int index) {
        if (index >= 0 && index < allLkupCurrencyData.size()) {
            return allLkupCurrencyData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allLkupCurrencyData.size();
    }

    public void printAllLkupCurrencyData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║       All Lkup Currency Records (" + allLkupCurrencyData.size() + " total)              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allLkupCurrencyData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allLkupCurrencyData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    // Getters for all 4 XDP_LKUP_CURRENCY columns
    public String getCurrencyCode() { return currencyCode; }
    public String getDecimalConvRule() { return decimalConvRule; }
    public String getCurrencyDesc() { return currencyDesc; }
    public String getDecimalSize() { return decimalSize; }

}
