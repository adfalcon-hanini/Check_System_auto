package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Trades data
 *
 * TABLE SOURCES:
 * - Methods 1-4, 8: Query XDP_TRADES (market-wide exchange data feed without client info)
 * - Methods 5-7, 9: Query SEC_VIRTUAL_TRADE (client trades with NIN identification)
 *
 * Contains 9 essential methods for fetching trades based on various criteria
 */
public class GetTradesData {

    private static final Logger logger = Logger.getLogger(GetTradesData.class);
    private OracleDBConnection dbConnection;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allTradesData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetTradesData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allTradesData = new ArrayList<>();
    }

    /**
     * Fetch all trades from XDP_TRADES
     * @return true if data found, false otherwise
     */
    public boolean fetchAllTrades() {
        try {
            logger.info("Fetching all trades from XDP_TRADES");

            String query = "SELECT * FROM xdp_trades";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by instrument code
     * @param instrumentCode Instrument code to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByInstrument(String instrumentCode) {
        try {
            logger.info("Fetching trades for instrument: " + instrumentCode);

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE INST_SEQ = ? " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, instrumentCode);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for instrument: " + instrumentCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades for today
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesToday() {
        try {
            logger.info("Fetching trades for today");

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE TRUNC(TRADE_DATE) = TRUNC(SYSDATE) " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for today");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades for a specific date
     * @param date Date in format dd-MM-yyyy (e.g., "01-11-2025")
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByDate(String date) {
        try {
            logger.info("Fetching trades for date: " + date);

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, date);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s) for date: " + date);
                return true;
            } else {
                logger.warn("No trades found for date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data for date: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by client NIN (National Identification Number)
     * NOTE: Uses sec_virtual_trade table which has NIN column
     * XDP_TRADES is a market feed without client identification
     * @param nin Client NIN to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByNIN(String nin) {
        try {
            logger.info("Fetching trades for NIN: " + nin);

            String query = "SELECT TRADE_DATE, NIN as CLIENT_NIN, COMPANY_CODE, TRNX_TYPE, " +
                          "VOLUME, PRICE, REASON, SEQ, TIME_STAMP " +
                          "FROM sec_virtual_trade " +
                          "WHERE NIN = ? " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s) for NIN: " + nin);
                return true;
            } else {
                logger.warn("No trades found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data for NIN: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by NIN and date
     * NOTE: Uses sec_virtual_trade table which has NIN column
     * @param nin Client NIN to filter
     * @param date Date in format dd-MM-yyyy
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByNINAndDate(String nin, String date) {
        try {
            logger.info("Fetching trades for NIN: " + nin + " and Date: " + date);

            String query = "SELECT TRADE_DATE, NIN as CLIENT_NIN, COMPANY_CODE, TRNX_TYPE, " +
                          "VOLUME, PRICE, REASON, SEQ, TIME_STAMP " +
                          "FROM sec_virtual_trade " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, date);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for NIN: " + nin + " and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by NIN and instrument
     * NOTE: Uses sec_virtual_trade table which has NIN and COMPANY_CODE columns
     * @param nin Client NIN to filter
     * @param instrumentCode Instrument code (company code) to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByNINAndInstrument(String nin, String instrumentCode) {
        try {
            logger.info("Fetching trades for NIN: " + nin + " and Instrument: " + instrumentCode);

            String query = "SELECT TRADE_DATE, NIN as CLIENT_NIN, COMPANY_CODE, TRNX_TYPE, " +
                          "VOLUME, PRICE, REASON, SEQ, TIME_STAMP " +
                          "FROM sec_virtual_trade " +
                          "WHERE NIN = ? " +
                          "AND COMPANY_CODE = ? " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, instrumentCode);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for NIN: " + nin + " and Instrument: " + instrumentCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by date and instrument
     * @param date Date in format dd-MM-yyyy
     * @param instrumentCode Instrument code to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByDateAndInstrument(String date, String instrumentCode) {
        try {
            logger.info("Fetching trades for Date: " + date + " and Instrument: " + instrumentCode);

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "AND INST_SEQ = ? " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, date, instrumentCode);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for Date: " + date + " and Instrument: " + instrumentCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades by NIN, date, and instrument (all three parameters)
     * NOTE: Uses sec_virtual_trade table which has NIN and COMPANY_CODE columns
     * @param nin Client NIN to filter
     * @param date Date in format dd-MM-yyyy
     * @param instrumentCode Instrument code (company code) to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByNINDateAndInstrument(String nin, String date, String instrumentCode) {
        try {
            logger.info("Fetching trades for NIN: " + nin + ", Date: " + date + ", Instrument: " + instrumentCode);

            String query = "SELECT TRADE_DATE, NIN as CLIENT_NIN, COMPANY_CODE, TRNX_TYPE, " +
                          "VOLUME, PRICE, REASON, SEQ, TIME_STAMP " +
                          "FROM sec_virtual_trade " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "AND COMPANY_CODE = ? " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, date, instrumentCode);

            if (!results.isEmpty()) {
                allTradesData = results;
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for the specified parameters");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get all trade records
     * @return List of all trade data rows
     */
    public List<Map<String, Object>> getAllTradeRecords() {
        return allTradesData;
    }

    /**
     * Get trade record by index
     * @param index Index of the record
     * @return Map containing trade data for that index
     */
    public Map<String, Object> getTradeRecordByIndex(int index) {
        if (index >= 0 && index < allTradesData.size()) {
            return allTradesData.get(index);
        }
        return null;
    }

    /**
     * Get total number of trade records
     * @return Number of records
     */
    public int getRecordCount() {
        return allTradesData.size();
    }

    /**
     * Print trades in a formatted table
     */
    public void printTradesTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        Trades Table                                                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-12s ║ %-30s ║ %-10s ║ %-10s ║%n",
                          "SEQ", "INST_CODE", "TRADE_DATE", "PRICE", "VOLUME");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allTradesData) {
            String trdId = record.get("SEQ") != null ? record.get("SEQ").toString() : "";
            String inst = record.get("INST_SEQ") != null ? record.get("INST_SEQ").toString() : "";
            String date = record.get("TRADE_DATE") != null ? record.get("TRADE_DATE").toString() : "";
            String prc = record.get("PRICE") != null ? record.get("PRICE").toString() : "";
            String qty = record.get("VOLUME") != null ? record.get("VOLUME").toString() : "";


            // Truncate long values
            if (trdId.length() > 12) trdId = trdId.substring(0, 9) + "...";
            if (inst.length() > 12) inst = inst.substring(0, 9) + "...";
            if (date.length() > 12) date = date.substring(0, 9) + "...";
          //  if (val.length() > 15) val = val.substring(0, 12) + "...";

            System.out.printf("║ %-12s ║ %-12s ║ %-30s ║ %-10s ║ %-10s ║%n",
                            trdId, inst, date, prc, qty);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Trades: " + allTradesData.size());
    }

    /**
     * Print trades summary
     */
    public void printTradesSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     Trades Summary                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allTradesData.isEmpty()) {
            System.out.println("No trades found.");
            return;
        }

        // Calculate statistics
        double totalValue = 0;
        double totalQuantity = 0;
        Map<String, Integer> instrumentCount = new HashMap<>();

        for (Map<String, Object> record : allTradesData) {
            // Sum values
            try {
                if (record.get("VALUE") != null) {
                    totalValue += Double.parseDouble(record.get("VALUE").toString());
                }
                if (record.get("QUANTITY") != null) {
                    totalQuantity += Double.parseDouble(record.get("QUANTITY").toString());
                }
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }

            // Count by instrument
            String inst = record.get("INST_CODE") != null ? record.get("INST_CODE").toString() : "UNKNOWN";
            instrumentCount.put(inst, instrumentCount.getOrDefault(inst, 0) + 1);
        }

        System.out.println("\nTotal Trades: " + allTradesData.size());
        System.out.println("Total Quantity: " + String.format("%.0f", totalQuantity));
        System.out.println("Total Value: " + String.format("%.2f", totalValue));
        if (allTradesData.size() > 0) {
            System.out.println("Average Trade Value: " + String.format("%.2f", totalValue / allTradesData.size()));
        }

        System.out.println("\nTrades by Instrument:");
        for (Map.Entry<String, Integer> entry : instrumentCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " trade(s)");
        }
        System.out.println("════════════════════════════════════════════════════════════════");
    }
}
