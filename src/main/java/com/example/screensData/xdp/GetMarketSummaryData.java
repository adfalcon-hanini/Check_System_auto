package com.example.screensData.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Market Summary data from XDP_MARKET_SUMMARY table
 */
public class GetMarketSummaryData {

    private static final Logger logger = Logger.getLogger(GetMarketSummaryData.class);
    private OracleDBConnection dbConnection;

    // Market Summary data fields - All 5 columns from XDP_MARKET_SUMMARY table
    private String marketSeq;  // NUMBER
    private String currentTrades;  // NUMBER
    private String currentVolume;  // NUMBER
    private String currentValue;  // FLOAT
    private String dateoflastupdate;  // DATE

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allMarketSummaryData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetMarketSummaryData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allMarketSummaryData = new ArrayList<>();
    }

    /**
     * Fetch all market summary data
     * @return true if data found, false otherwise
     */
    public boolean fetchAllMarketSummary() {
        try {
            logger.info("Fetching all market summary data from XDP_MARKET_SUMMARY");

            String query = "SELECT * FROM XDP_MARKET_SUMMARY";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMarketSummaryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Market summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No market summary data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching market summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch market summary by specific criteria
     * @param whereClause WHERE clause for filtering (e.g., "GROUP_CODE = 'RM'")
     * @return true if data found, false otherwise
     */
    public boolean fetchMarketSummaryByCondition(String whereClause) {
        try {
            logger.info("Fetching market summary data with condition: " + whereClause);

            String query = "SELECT * FROM XDP_MARKET_SUMMARY WHERE " + whereClause;

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMarketSummaryData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Market summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No market summary data found for condition: " + whereClause);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching market summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse market summary data from database row - All 5 XDP_MARKET_SUMMARY columns
     * @param row Database row containing market summary data
     */
    private void parseData(Map<String, Object> row) {
        this.marketSeq = row.get("MARKET_SEQ") != null ? row.get("MARKET_SEQ").toString() : "";
        this.currentTrades = row.get("CURRENT_TRADES") != null ? row.get("CURRENT_TRADES").toString() : "";
        this.currentVolume = row.get("CURRENT_VOLUME") != null ? row.get("CURRENT_VOLUME").toString() : "";
        this.currentValue = row.get("CURRENT_VALUE") != null ? row.get("CURRENT_VALUE").toString() : "";
        this.dateoflastupdate = row.get("DATEOFLASTUPDATE") != null ? row.get("DATEOFLASTUPDATE").toString() : "";
    }

    /**
     * Get all market summary records
     * @return List of all market summary data rows
     */
    public List<Map<String, Object>> getAllMarketSummaryRecords() {
        return allMarketSummaryData;
    }

    /**
     * Get market summary record by index
     * @param index Index of the record
     * @return Map containing market summary data for that index
     */
    public Map<String, Object> getMarketSummaryRecordByIndex(int index) {
        if (index >= 0 && index < allMarketSummaryData.size()) {
            return allMarketSummaryData.get(index);
        }
        return null;
    }

    /**
     * Get total number of market summary records
     * @return Number of records
     */
    public int getRecordCount() {
        return allMarketSummaryData.size();
    }

    /**
     * Print all market summary records
     */
    public void printAllMarketSummaryData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║         All Market Summary Records (" + allMarketSummaryData.size() + " total)           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allMarketSummaryData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allMarketSummaryData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print market summary in a formatted table
     */
    public void printMarketSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   Market Summary Table                        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");

        if (allMarketSummaryData.isEmpty()) {
            System.out.println("║  No data available                                            ║");
        } else {
            // Get column names from first record
            Map<String, Object> firstRecord = allMarketSummaryData.get(0);
            List<String> columnNames = new ArrayList<>(firstRecord.keySet());

            // Print header
            System.out.print("║ ");
            for (String col : columnNames) {
                System.out.printf("%-20s │ ", col);
            }
            System.out.println();
            System.out.println("╠═══════════════════════════════════════════════════════════════╣");

            // Print data rows
            for (Map<String, Object> record : allMarketSummaryData) {
                System.out.print("║ ");
                for (String col : columnNames) {
                    String value = record.get(col) != null ? record.get(col).toString() : "";
                    if (value.length() > 20) value = value.substring(0, 17) + "...";
                    System.out.printf("%-20s │ ", value);
                }
                System.out.println();
            }
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allMarketSummaryData.size());
    }

    // Getters for all 5 XDP_MARKET_SUMMARY columns
    public String getMarketSeq() { return marketSeq; }
    public String getCurrentTrades() { return currentTrades; }
    public String getCurrentVolume() { return currentVolume; }
    public String getCurrentValue() { return currentValue; }
    public String getDateoflastupdate() { return dateoflastupdate; }

}
