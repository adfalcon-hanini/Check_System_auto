package com.example.screensData.alerts;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Alert Symbols data from ALERT_SYMBOLS table
 */
public class GetAlertSymbolsData {

    private static final Logger logger = Logger.getLogger(GetAlertSymbolsData.class);
    private OracleDBConnection dbConnection;

    // Alert symbols data fields
    private String symbolId;
    private String symbolCode;
    private String symbolName;
    private String market;
    private String sector;
    private String isActive;
    private String createdDate;

    private List<Map<String, Object>> allAlertSymbolsData;

    public GetAlertSymbolsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAlertSymbolsData = new ArrayList<>();
    }

    public boolean fetchAllAlertSymbols() {
        try {
            logger.info("Fetching all alert symbols data");
            String query = "SELECT * FROM ALERT_SYMBOLS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAlertSymbolsData = results;
                parseAlertSymbolsData(results.get(0));
                logger.info("Alert symbols data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert symbols data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertSymbolById(String symbolId) {
        try {
            logger.info("Fetching alert symbol by ID: " + symbolId);
            String query = "SELECT * FROM ALERT_SYMBOLS WHERE SYMBOL_ID = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, symbolId);

            if (!results.isEmpty()) {
                allAlertSymbolsData = results;
                parseAlertSymbolsData(results.get(0));
                logger.info("Alert symbol data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert symbol data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAlertSymbolByCode(String symbolCode) {
        try {
            logger.info("Fetching alert symbol by code: " + symbolCode);
            String query = "SELECT * FROM ALERT_SYMBOLS WHERE SYMBOL_CODE = ?";
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, symbolCode);

            if (!results.isEmpty()) {
                allAlertSymbolsData = results;
                parseAlertSymbolsData(results.get(0));
                logger.info("Alert symbol data fetched successfully");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error fetching alert symbol data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseAlertSymbolsData(Map<String, Object> row) {
        this.symbolId = row.get("SYMBOL_ID") != null ? row.get("SYMBOL_ID").toString() : "";
        this.symbolCode = row.get("SYMBOL_CODE") != null ? row.get("SYMBOL_CODE").toString() : "";
        this.symbolName = row.get("SYMBOL_NAME") != null ? row.get("SYMBOL_NAME").toString() : "";
        this.market = row.get("MARKET") != null ? row.get("MARKET").toString() : "";
        this.sector = row.get("SECTOR") != null ? row.get("SECTOR").toString() : "";
        this.isActive = row.get("IS_ACTIVE") != null ? row.get("IS_ACTIVE").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
    }

    public void printAlertSymbolsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           Alert Symbols Table                                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-15s ║ %-25s ║ %-12s ║ %-10s ║%n", "SYMBOL_ID", "SYMBOL_CODE", "SYMBOL_NAME", "MARKET", "IS_ACTIVE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allAlertSymbolsData) {
            String id = record.get("SYMBOL_ID") != null ? record.get("SYMBOL_ID").toString() : "";
            String code = record.get("SYMBOL_CODE") != null ? record.get("SYMBOL_CODE").toString() : "";
            String name = record.get("SYMBOL_NAME") != null ? record.get("SYMBOL_NAME").toString() : "";
            String market = record.get("MARKET") != null ? record.get("MARKET").toString() : "";
            String active = record.get("IS_ACTIVE") != null ? record.get("IS_ACTIVE").toString() : "";

            if (name.length() > 25) name = name.substring(0, 22) + "...";

            System.out.printf("║ %-12s ║ %-15s ║ %-25s ║ %-12s ║ %-10s ║%n", id, code, name, market, active);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allAlertSymbolsData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllAlertSymbolsRecords() { return allAlertSymbolsData; }
    public int getRecordCount() { return allAlertSymbolsData.size(); }
    public String getSymbolId() { return symbolId; }
    public String getSymbolCode() { return symbolCode; }
    public String getSymbolName() { return symbolName; }
    public String getMarket() { return market; }
    public String getSector() { return sector; }
    public String getIsActive() { return isActive; }
    public String getCreatedDate() { return createdDate; }
}
