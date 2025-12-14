package com.example.screensData.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Instruments data from XDP_INSTRUMENTS table
 */
public class GetInstrumentsData {

    private static final Logger logger = Logger.getLogger(GetInstrumentsData.class);
    private OracleDBConnection dbConnection;

    // XDP_INSTRUMENTS column fields (73 columns)
    private String instCode, showOrder, groupCode, name, marketseq, startHaltDate, haltReason, haltActionCode;
    private String openTime, orderEntryState, state, tradingState, startRefDate, endRefDate, fixPriceTick, instType;
    private String countryCode, tradingCurrency, instCategory, repoiCode, repoiExpDate, frstSettleDate, typeOfDerivat;
    private String bicDeposit, icb, cfi, quantNotation, marketFeedCode, lotSize, mnemo, tradeCode, smallTradeAmount;
    private String lastadjprice, prevvolumetraded, eventdate, periodindicator, typeofmarketadmission, dateoflasttrade;
    private String underlyingwisincode, depositarylist, maindepositary, typeofcorporateevent, financialmarketcode;
    private String prevdaycapitaltraded, nommktprice, numberinstrumentcirc, repoindicator, typeofunitexp;
    private String marketindicator, taxcode, strikeprice, strikecurrency, currencycoef, tradingcurrencyindicator;
    private String strikecurrencyindicator, marketsegment, algo, localname, smalltrade, dynhighcollar, dynlowcollar;
    private String statichighcollar, staticlowcollar, collartype, appearInDailyBook, updatedbymsgno, dateoflastupdate;
    private String mic, prevClose, sharesout, subbook, marketDataGroup, newEndTime;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allInstrumentsData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetInstrumentsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allInstrumentsData = new ArrayList<>();
    }

    /**
     * Fetch all instruments data based on specified criteria
     * @return true if data found, false otherwise
     */
    public boolean fetchInstrumentsData() {
        try {
            logger.info("Fetching instruments data from XDP_INSTRUMENTS");

            String query = "SELECT XD.INST_CODE, XD.GROUP_CODE, XD.MNEMO, XD.NAME, XD.SHOW_ORDER, XD.TRADE_CODE " +
                          "FROM XDP_INSTRUMENTS XD " +
                          "WHERE XD.GROUP_CODE IN ('RM', 'BM', 'IP', 'RR', 'OE', 'VM') " +
                          "AND XD.INST_TYPE IN (041, 273) " +
                          "AND XD.NAME NOT LIKE '%Bond%' " +
                          "AND XD.NAME NOT LIKE '%Tbill%' " +
                          "AND XD.APPEAR_IN_DAILY_BOOK = 'Y' " +
                          "ORDER BY XD.SHOW_ORDER ASC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allInstrumentsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseInstrumentData(row);
                logger.info("Instruments data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No instruments data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching instruments data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch instrument data by INST_CODE
     * @param instCode Instrument code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchInstrumentByCode(String instCode) {
        try {
            logger.info("Fetching instrument data for INST_CODE: " + instCode);

            String query = "SELECT XD.INST_CODE, XD.GROUP_CODE, XD.MNEMO, XD.NAME, XD.SHOW_ORDER, XD.TRADE_CODE " +
                          "FROM XDP_INSTRUMENTS XD " +
                          "WHERE XD.INST_CODE = '" + instCode + "' " +
                          "AND XD.GROUP_CODE IN ('RM', 'BM', 'IP', 'RR', 'OE', 'VM') " +
                          "AND XD.INST_TYPE IN (041, 273) " +
                          "AND XD.NAME NOT LIKE '%Bond%' " +
                          "AND XD.NAME NOT LIKE '%Tbill%' " +
                          "AND XD.APPEAR_IN_DAILY_BOOK = 'Y'";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allInstrumentsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseInstrumentData(row);
                logger.info("Instrument data fetched successfully for INST_CODE: " + instCode);
                return true;
            } else {
                logger.warn("No instrument data found for INST_CODE: " + instCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching instrument data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch instruments by GROUP_CODE
     * @param groupCode Group code to filter by
     * @return true if data found, false otherwise
     */
    public boolean fetchInstrumentsByGroupCode(String groupCode) {
        try {
            logger.info("Fetching instruments data for GROUP_CODE: " + groupCode);

            String query = "SELECT XD.INST_CODE, XD.GROUP_CODE, XD.MNEMO, XD.NAME, XD.SHOW_ORDER, XD.TRADE_CODE " +
                          "FROM XDP_INSTRUMENTS XD " +
                          "WHERE XD.GROUP_CODE = '" + groupCode + "' " +
                          "AND XD.INST_TYPE IN (041, 273) " +
                          "AND XD.NAME NOT LIKE '%Bond%' " +
                          "AND XD.NAME NOT LIKE '%Tbill%' " +
                          "AND XD.APPEAR_IN_DAILY_BOOK = 'Y' " +
                          "ORDER BY XD.SHOW_ORDER ASC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allInstrumentsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseInstrumentData(row);
                logger.info("Instruments data fetched successfully. Found " + results.size() + " record(s) for GROUP_CODE: " + groupCode);
                return true;
            } else {
                logger.warn("No instruments data found for GROUP_CODE: " + groupCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching instruments data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse instrument data from database row
     * @param row Database row containing instrument data
     */
    private void parseInstrumentData(Map<String, Object> row) {
        this.instCode = row.get("INST_CODE") != null ? row.get("INST_CODE").toString() : "";
        this.groupCode = row.get("GROUP_CODE") != null ? row.get("GROUP_CODE").toString() : "";
        this.mnemo = row.get("MNEMO") != null ? row.get("MNEMO").toString() : "";
        this.name = row.get("NAME") != null ? row.get("NAME").toString() : "";
        this.showOrder = row.get("SHOW_ORDER") != null ? row.get("SHOW_ORDER").toString() : "";
        this.tradeCode = row.get("TRADE_CODE") != null ? row.get("TRADE_CODE").toString() : "";
    }

    /**
     * Get all instrument data as a map (first row)
     * @return Map containing all instrument fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("instCode", this.instCode);
        data.put("groupCode", this.groupCode);
        data.put("mnemo", this.mnemo);
        data.put("name", this.name);
        data.put("showOrder", this.showOrder);
        data.put("tradeCode", this.tradeCode);
        return data;
    }

    /**
     * Get all instrument records
     * @return List of all instrument data rows
     */
    public List<Map<String, Object>> getAllInstrumentRecords() {
        return allInstrumentsData;
    }

    /**
     * Get instrument record by index
     * @param index Index of the record
     * @return Map containing instrument data for that index
     */
    public Map<String, Object> getInstrumentRecordByIndex(int index) {
        if (index >= 0 && index < allInstrumentsData.size()) {
            return allInstrumentsData.get(index);
        }
        return null;
    }

    /**
     * Get total number of instrument records
     * @return Number of records
     */
    public int getRecordCount() {
        return allInstrumentsData.size();
    }

    /**
     * Print instrument data to console (first record)
     */
    public void printInstrumentData() {
        System.out.println("\n========== Instrument Data ==========");
        System.out.println("Instrument Code: " + instCode);
        System.out.println("Group Code: " + groupCode);
        System.out.println("MNEMO: " + mnemo);
        System.out.println("Name: " + name);
        System.out.println("Show Order: " + showOrder);
        System.out.println("Trade Code: " + tradeCode);
        System.out.println("====================================");
    }

    /**
     * Print all instrument records
     */
    public void printAllInstrumentData() {
        System.out.println("\n========== All Instrument Records (" + allInstrumentsData.size() + ") ==========");
        for (int i = 0; i < allInstrumentsData.size(); i++) {
            System.out.println("\n--- Record " + (i + 1) + " ---");
            Map<String, Object> record = allInstrumentsData.get(i);
            System.out.println("INST_CODE: " + record.get("INST_CODE"));
            System.out.println("GROUP_CODE: " + record.get("GROUP_CODE"));
            System.out.println("MNEMO: " + record.get("MNEMO"));
            System.out.println("NAME: " + record.get("NAME"));
            System.out.println("SHOW_ORDER: " + record.get("SHOW_ORDER"));
            System.out.println("TRADE_CODE: " + record.get("TRADE_CODE"));
        }
        System.out.println("=================================================================");
    }

    /**
     * Print instruments summary table
     */
    public void printInstrumentsSummary() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          Instruments Summary                                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-10s ║ %-15s ║ %-25s ║%n", "INST_CODE", "GROUP_CODE", "MNEMO", "NAME");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allInstrumentsData) {
            String inst = record.get("INST_CODE") != null ? record.get("INST_CODE").toString() : "";
            String group = record.get("GROUP_CODE") != null ? record.get("GROUP_CODE").toString() : "";
            String mnemoStr = record.get("MNEMO") != null ? record.get("MNEMO").toString() : "";
            String nameStr = record.get("NAME") != null ? record.get("NAME").toString() : "";

            // Truncate long names
            if (nameStr.length() > 25) {
                nameStr = nameStr.substring(0, 22) + "...";
            }

            System.out.printf("║ %-12s ║ %-10s ║ %-15s ║ %-25s ║%n", inst, group, mnemoStr, nameStr);
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Instruments: " + allInstrumentsData.size());
    }

    // Getters
    public String getInstCode() {
        return instCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getMnemo() {
        return mnemo;
    }

    public String getName() {
        return name;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public String getTradeCode() {
        return tradeCode;
    }

}
