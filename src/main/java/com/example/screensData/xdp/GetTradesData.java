package com.example.screensData.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to retrieve and store Trades data from XDP_TRADES table
 */
public class GetTradesData {

    private static final Logger logger = Logger.getLogger(GetTradesData.class);
    private OracleDBConnection dbConnection;

    // XDP_TRADES column fields (all 31 columns from database)
    private String seq;  // NUMBER
    private String tradeid;  // NUMBER
    private String quotelinkid;  // VARCHAR2
    private String instSeq;  // VARCHAR2
    private String tradeDate;  // DATE
    private String price;  // FLOAT
    private String volume;  // NUMBER
    private String smalltradeindicator;  // CHAR
    private String tradcond2;  // CHAR
    private String tradcond3;  // CHAR
    private String tradeorigin;  // CHAR
    private String openingtradeindicator;  // VARCHAR2
    private String tradeState;  // CHAR
    private String iscancelled;  // NUMBER
    private String cancelleddate;  // DATE
    private String cumulativequantity;  // NUMBER
    private String highestprice;  // NUMBER
    private String lowestprice;  // NUMBER
    private String tickdirection;  // VARCHAR2
    private String dateoflastupdate;  // TIMESTAMP(6)
    private String updatedbymsgno;  // VARCHAR2
    private String createdBy;  // VARCHAR2
    private String creationDate;  // TIMESTAMP(6)
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String variationlastprice;  // NUMBER
    private String createdByIpAddress;  // VARCHAR2
    private String createdByPcName;  // VARCHAR2
    private String userIdIpAddress;  // VARCHAR2
    private String userIdPcName;  // VARCHAR2
    private String tradedirection;  // VARCHAR2

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
     * Fetch last 10 trades from XDP_TRADES
     * @return true if data found, false otherwise
     */
    public boolean fetchLast10Trades() {
        try {
            logger.info("Fetching last 10 trades from XDP_TRADES");

            String query = "SELECT * FROM xdp_trades " +
                          "ORDER BY ROWNUM DESC " +
                          "FETCH FIRST 10 ROWS ONLY";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
     * Fetch last N trades from XDP_TRADES
     * @param limit Number of trades to fetch
     * @return true if data found, false otherwise
     */
    public boolean fetchLastNTrades(int limit) {
        try {
            logger.info("Fetching last " + limit + " trades from XDP_TRADES");

            String query = "SELECT * FROM xdp_trades " +
                          "ORDER BY ROWNUM DESC " +
                          "FETCH FIRST " + limit + " ROWS ONLY";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
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
     * Fetch trades by INST_SEQ
     * @param instSeq Instrument sequence to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByInstSeq(String instSeq) {
        try {
            logger.info("Fetching trades for INST_SEQ: " + instSeq);

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE INST_SEQ = ? " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, instSeq);

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for INST_SEQ: " + instSeq);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades for multiple INST_SEQ values
     * @param instSeqList List of instrument sequences
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByInstSeqList(List<String> instSeqList) {
        try {
            logger.info("Fetching trades for " + instSeqList.size() + " instrument sequences");

            if (instSeqList == null || instSeqList.isEmpty()) {
                logger.warn("Empty INST_SEQ list provided");
                return false;
            }

            // Build IN clause with placeholders
            String placeholders = instSeqList.stream()
                    .map(seq -> "?")
                    .collect(Collectors.joining(", "));

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE INST_SEQ IN (" + placeholders + ") " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, instSeqList.toArray());

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for provided INST_SEQ list");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades based on instruments from GetInstrumentsData
     * @param instrumentsData GetInstrumentsData instance with loaded instruments
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByInstruments(GetInstrumentsData instrumentsData) {
        try {
            logger.info("Fetching trades based on instruments from GetInstrumentsData");

            List<Map<String, Object>> instruments = instrumentsData.getAllInstrumentRecords();

            if (instruments == null || instruments.isEmpty()) {
                logger.warn("No instruments found in GetInstrumentsData");
                return false;
            }

            // Extract INST_SEQ or INST_CODE from instruments
            List<String> instCodes = new ArrayList<>();
            for (Map<String, Object> instrument : instruments) {
                String instCode = instrument.get("INST_CODE") != null ?
                                 instrument.get("INST_CODE").toString() : null;
                if (instCode != null && !instCode.isEmpty()) {
                    instCodes.add(instCode);
                }
            }

            if (instCodes.isEmpty()) {
                logger.warn("No valid INST_CODE found in instruments");
                return false;
            }

            logger.info("Fetching trades for " + instCodes.size() + " instruments");

            // Build IN clause with placeholders
            String placeholders = instCodes.stream()
                    .map(code -> "?")
                    .collect(Collectors.joining(", "));

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE INST_CODE IN (" + placeholders + ") " +
                          "ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, instCodes.toArray());

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for provided instruments");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch trades based on instruments from GetInstrumentsData with limit
     * @param instrumentsData GetInstrumentsData instance with loaded instruments
     * @param limit Maximum number of trades to fetch
     * @return true if data found, false otherwise
     */
    public boolean fetchTradesByInstrumentsWithLimit(GetInstrumentsData instrumentsData, int limit) {
        try {
            logger.info("Fetching trades based on instruments with limit: " + limit);

            List<Map<String, Object>> instruments = instrumentsData.getAllInstrumentRecords();

            if (instruments == null || instruments.isEmpty()) {
                logger.warn("No instruments found in GetInstrumentsData");
                return false;
            }

            // Extract INST_CODE from instruments
            List<String> instCodes = new ArrayList<>();
            for (Map<String, Object> instrument : instruments) {
                String instCode = instrument.get("INST_CODE") != null ?
                                 instrument.get("INST_CODE").toString() : null;
                if (instCode != null && !instCode.isEmpty()) {
                    instCodes.add(instCode);
                }
            }

            if (instCodes.isEmpty()) {
                logger.warn("No valid INST_CODE found in instruments");
                return false;
            }

            logger.info("Fetching trades for " + instCodes.size() + " instruments");

            // Build IN clause with placeholders
            String placeholders = instCodes.stream()
                    .map(code -> "?")
                    .collect(Collectors.joining(", "));

            String query = "SELECT * FROM xdp_trades " +
                          "WHERE INST_CODE IN (" + placeholders + ") " +
                          "ORDER BY ROWNUM DESC " +
                          "FETCH FIRST " + limit + " ROWS ONLY";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, instCodes.toArray());

            if (!results.isEmpty()) {
                allTradesData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseTradeData(row);
                logger.info("Trades data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No trades found for provided instruments");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching trades data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch and display trades by instruments from database
     * This is a convenience method that fetches instruments and then trades
     * @param instSeqList List of INST_SEQ values to fetch
     * @return true if data found, false otherwise
     */
    public boolean fetchAndDisplayTradesByInstSeqList(List<String> instSeqList) {
        boolean result = fetchTradesByInstSeqList(instSeqList);

        if (result) {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║         Trades for Specified Instruments                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            printTradesTable();
            printTradesSummary();
        }

        return result;
    }

    /**
     * Parse trade data from database row
     * @param row Database row containing trade data
     */
    private void parseTradeData(Map<String, Object> row) {
        this.seq = row.get("SEQ") != null ? row.get("SEQ").toString() : "";
        this.tradeid = row.get("TRADEID") != null ? row.get("TRADEID").toString() : "";
        this.quotelinkid = row.get("QUOTELINKID") != null ? row.get("QUOTELINKID").toString() : "";
        this.instSeq = row.get("INST_SEQ") != null ? row.get("INST_SEQ").toString() : "";
        this.tradeDate = row.get("TRADE_DATE") != null ? row.get("TRADE_DATE").toString() : "";
        this.price = row.get("PRICE") != null ? row.get("PRICE").toString() : "";
        this.volume = row.get("VOLUME") != null ? row.get("VOLUME").toString() : "";
        this.smalltradeindicator = row.get("SMALLTRADEINDICATOR") != null ? row.get("SMALLTRADEINDICATOR").toString() : "";
        this.tradcond2 = row.get("TRADCOND2") != null ? row.get("TRADCOND2").toString() : "";
        this.tradcond3 = row.get("TRADCOND3") != null ? row.get("TRADCOND3").toString() : "";
        this.tradeorigin = row.get("TRADEORIGIN") != null ? row.get("TRADEORIGIN").toString() : "";
        this.openingtradeindicator = row.get("OPENINGTRADEINDICATOR") != null ? row.get("OPENINGTRADEINDICATOR").toString() : "";
        this.tradeState = row.get("TRADE_STATE") != null ? row.get("TRADE_STATE").toString() : "";
        this.iscancelled = row.get("ISCANCELLED") != null ? row.get("ISCANCELLED").toString() : "";
        this.cancelleddate = row.get("CANCELLEDDATE") != null ? row.get("CANCELLEDDATE").toString() : "";
        this.cumulativequantity = row.get("CUMULATIVEQUANTITY") != null ? row.get("CUMULATIVEQUANTITY").toString() : "";
        this.highestprice = row.get("HIGHESTPRICE") != null ? row.get("HIGHESTPRICE").toString() : "";
        this.lowestprice = row.get("LOWESTPRICE") != null ? row.get("LOWESTPRICE").toString() : "";
        this.tickdirection = row.get("TICKDIRECTION") != null ? row.get("TICKDIRECTION").toString() : "";
        this.dateoflastupdate = row.get("DATEOFLASTUPDATE") != null ? row.get("DATEOFLASTUPDATE").toString() : "";
        this.updatedbymsgno = row.get("UPDATEDBYMSGNO") != null ? row.get("UPDATEDBYMSGNO").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.variationlastprice = row.get("VARIATIONLASTPRICE") != null ? row.get("VARIATIONLASTPRICE").toString() : "";
        this.createdByIpAddress = row.get("CREATED_BY_IP_ADDRESS") != null ? row.get("CREATED_BY_IP_ADDRESS").toString() : "";
        this.createdByPcName = row.get("CREATED_BY_PC_NAME") != null ? row.get("CREATED_BY_PC_NAME").toString() : "";
        this.userIdIpAddress = row.get("USER_ID_IP_ADDRESS") != null ? row.get("USER_ID_IP_ADDRESS").toString() : "";
        this.userIdPcName = row.get("USER_ID_PC_NAME") != null ? row.get("USER_ID_PC_NAME").toString() : "";
        this.tradedirection = row.get("TRADEDIRECTION") != null ? row.get("TRADEDIRECTION").toString() : "";
    }

    /**
     * Get all trade data as a map (first row)
     * @return Map containing all trade fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("seq", this.seq);
        data.put("tradeid", this.tradeid);
        data.put("quotelinkid", this.quotelinkid);
        data.put("instSeq", this.instSeq);
        data.put("tradeDate", this.tradeDate);
        data.put("price", this.price);
        data.put("volume", this.volume);
        data.put("smalltradeindicator", this.smalltradeindicator);
        data.put("tradcond2", this.tradcond2);
        data.put("tradcond3", this.tradcond3);
        data.put("tradeorigin", this.tradeorigin);
        data.put("openingtradeindicator", this.openingtradeindicator);
        data.put("tradeState", this.tradeState);
        data.put("iscancelled", this.iscancelled);
        data.put("cancelleddate", this.cancelleddate);
        data.put("cumulativequantity", this.cumulativequantity);
        data.put("highestprice", this.highestprice);
        data.put("lowestprice", this.lowestprice);
        data.put("tickdirection", this.tickdirection);
        data.put("dateoflastupdate", this.dateoflastupdate);
        data.put("updatedbymsgno", this.updatedbymsgno);
        data.put("createdBy", this.createdBy);
        data.put("creationDate", this.creationDate);
        data.put("userId", this.userId);
        data.put("timeStamp", this.timeStamp);
        data.put("variationlastprice", this.variationlastprice);
        data.put("createdByIpAddress", this.createdByIpAddress);
        data.put("createdByPcName", this.createdByPcName);
        data.put("userIdIpAddress", this.userIdIpAddress);
        data.put("userIdPcName", this.userIdPcName);
        data.put("tradedirection", this.tradedirection);
        return data;
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
     * Get trade count grouped by instrument
     * @return Map of instrument code to trade count
     */
    public Map<String, Integer> getTradeCountByInstrument() {
        Map<String, Integer> instrumentCount = new HashMap<>();

        for (Map<String, Object> record : allTradesData) {
            String instCode = record.get("INST_CODE") != null ?
                             record.get("INST_CODE").toString() : "UNKNOWN";
            instrumentCount.put(instCode, instrumentCount.getOrDefault(instCode, 0) + 1);
        }

        return instrumentCount;
    }

    /**
     * Fetch all trades and return trade count by instrument
     * This is a convenience method that fetches all trades and returns counts
     * @return Map of instrument code to trade count
     */
    public Map<String, Integer> fetchAndGetTradeCountByInstrument() {
        try {
            logger.info("Fetching all trades to calculate count by instrument");

            String query = "SELECT INST_CODE, COUNT(*) as TRADE_COUNT " +
                          "FROM xdp_trades " +
                          "GROUP BY INST_CODE " +
                          "ORDER BY TRADE_COUNT DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            Map<String, Integer> instrumentCount = new HashMap<>();

            for (Map<String, Object> row : results) {
                String instCode = row.get("INST_CODE") != null ?
                                 row.get("INST_CODE").toString() : "UNKNOWN";
                int count = row.get("TRADE_COUNT") != null ?
                           Integer.parseInt(row.get("TRADE_COUNT").toString()) : 0;
                instrumentCount.put(instCode, count);
            }

            logger.info("Successfully calculated trade counts for " + instrumentCount.size() + " instrument(s)");
            return instrumentCount;

        } catch (SQLException e) {
            logger.error("Error fetching trade counts: " + e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Print trade count by instrument
     * @param instrumentCount Map of instrument code to trade count
     */
    public void printTradeCountByInstrument(Map<String, Integer> instrumentCount) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Trade Count by Instrument                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (instrumentCount.isEmpty()) {
            System.out.println("No trade counts available.");
            return;
        }

        int totalTrades = instrumentCount.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("\nTotal Instruments: " + instrumentCount.size());
        System.out.println("Total Trades: " + totalTrades);
        System.out.println("\nBreakdown by Instrument:");
        System.out.println("─────────────────────────────────────────────────────────────────");

        // Sort by count descending
        instrumentCount.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                String percentage = String.format("%.2f%%", (entry.getValue() * 100.0) / totalTrades);
                System.out.printf("  %-15s : %,6d trade(s)  (%s)%n",
                                 entry.getKey(), entry.getValue(), percentage);
            });

        System.out.println("════════════════════════════════════════════════════════════════");
    }

    /**
     * Print trade data to console (first record)
     */
    public void printTradeData() {
        System.out.println("\n========== Trade Data (First Record) ==========");
        System.out.println("Seq: " + seq);
        System.out.println("Trade ID: " + tradeid);
        System.out.println("Quote Link ID: " + quotelinkid);
        System.out.println("Instrument Seq: " + instSeq);
        System.out.println("Trade Date: " + tradeDate);
        System.out.println("Price: " + price);
        System.out.println("Volume: " + volume);
        System.out.println("Small Trade Indicator: " + smalltradeindicator);
        System.out.println("Trade State: " + tradeState);
        System.out.println("Is Cancelled: " + iscancelled);
        System.out.println("Highest Price: " + highestprice);
        System.out.println("Lowest Price: " + lowestprice);
        System.out.println("Tick Direction: " + tickdirection);
        System.out.println("================================================");
    }

    /**
     * Print all trade records
     */
    public void printAllTradeData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              All Trade Records (" + allTradesData.size() + " total)                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allTradesData.size(); i++) {
            System.out.println("─────────────────── Trade " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allTradesData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-20s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print trades in a formatted table
     */
    public void printTradesTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        Trades Table                                                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-12s ║ %-12s ║ %-10s ║ %-10s ║ %-12s ║ %-12s ║ %-15s ║%n",
                          "TRADE_ID", "INST_CODE", "TRADE_DATE", "PRICE", "QUANTITY", "BUY_CL_ID", "SELL_CL_ID", "VALUE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allTradesData) {
            String trdId = record.get("TRADE_ID") != null ? record.get("TRADE_ID").toString() : "";
            String inst = record.get("INST_SEQ") != null ? record.get("INST_SEQ").toString() : "";
            String date = record.get("TRADE_DATE") != null ? record.get("TRADE_DATE").toString() : "";
            String prc = record.get("PRICE") != null ? record.get("PRICE").toString() : "";
            String qty = record.get("QUANTITY") != null ? record.get("QUANTITY").toString() : "";
            String buyClId = record.get("BUY_CL_ID") != null ? record.get("BUY_CL_ID").toString() : "";
            String sellClId = record.get("SELL_CL_ID") != null ? record.get("SELL_CL_ID").toString() : "";
            String val = record.get("VALUE") != null ? record.get("VALUE").toString() : "";

            // Truncate long values
            if (trdId.length() > 12) trdId = trdId.substring(0, 9) + "...";
            if (inst.length() > 12) inst = inst.substring(0, 9) + "...";
            if (date.length() > 12) date = date.substring(0, 9) + "...";
            if (val.length() > 15) val = val.substring(0, 12) + "...";

            System.out.printf("║ %-12s ║ %-12s ║ %-12s ║ %-10s ║ %-10s ║ %-12s ║ %-12s ║ %-15s ║%n",
                            trdId, inst, date, prc, qty, buyClId, sellClId, val);
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

    /**
     * Print last 10 trades in a simple format
     */
    public void printLast10Trades() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Last 10 Trades                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        int count = Math.min(10, allTradesData.size());

        for (int i = 0; i < count; i++) {
            Map<String, Object> trade = allTradesData.get(i);
            System.out.println("Trade " + (i + 1) + ":");
            System.out.println("  Trade ID: " + (trade.get("TRADE_ID") != null ? trade.get("TRADE_ID") : "N/A"));
            System.out.println("  Instrument: " + (trade.get("INST_CODE") != null ? trade.get("INST_CODE") : "N/A"));
            System.out.println("  Price: " + (trade.get("PRICE") != null ? trade.get("PRICE") : "N/A"));
            System.out.println("  Quantity: " + (trade.get("QUANTITY") != null ? trade.get("QUANTITY") : "N/A"));
            System.out.println("  Value: " + (trade.get("VALUE") != null ? trade.get("VALUE") : "N/A"));
            System.out.println("  Date: " + (trade.get("TRADE_DATE") != null ? trade.get("TRADE_DATE") : "N/A"));
            System.out.println();
        }

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Displayed " + count + " trade(s)");
    }

    // Getters and Setters for all 31 XDP_TRADES columns
    public String getSeq() { return seq; }
    public String getTradeid() { return tradeid; }
    public String getQuotelinkid() { return quotelinkid; }
    public String getInstSeq() { return instSeq; }
    public String getTradeDate() { return tradeDate; }
    public String getPrice() { return price; }
    public String getVolume() { return volume; }
    public String getSmalltradeindicator() { return smalltradeindicator; }
    public String getTradcond2() { return tradcond2; }
    public String getTradcond3() { return tradcond3; }
    public String getTradeorigin() { return tradeorigin; }
    public String getOpeningtradeindicator() { return openingtradeindicator; }
    public String getTradeState() { return tradeState; }
    public String getIscancelled() { return iscancelled; }
    public String getCancelleddate() { return cancelleddate; }
    public String getCumulativequantity() { return cumulativequantity; }
    public String getHighestprice() { return highestprice; }
    public String getLowestprice() { return lowestprice; }
    public String getTickdirection() { return tickdirection; }
    public String getDateoflastupdate() { return dateoflastupdate; }
    public String getUpdatedbymsgno() { return updatedbymsgno; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getVariationlastprice() { return variationlastprice; }
    public String getCreatedByIpAddress() { return createdByIpAddress; }
    public String getCreatedByPcName() { return createdByPcName; }
    public String getUserIdIpAddress() { return userIdIpAddress; }
    public String getUserIdPcName() { return userIdPcName; }
    public String getTradedirection() { return tradedirection; }
}
