package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Markets data from XDP_MARKETS table
 */
public class GetMarketsData {

    private static final Logger logger = Logger.getLogger(GetMarketsData.class);
    private OracleDBConnection dbConnection;

    // XDP_MARKETS column fields
    private String marketseq;  // NUMBER
    private String marketname;  // VARCHAR2
    private String marketplace;  // VARCHAR2
    private String marketcode;  // VARCHAR2
    private String marketProtocol;  // VARCHAR2
    private String stockExchangeCode;  // NUMBER
    private String mic;  // VARCHAR2
    private String marketIndicator;  // NUMBER
    private String sourceid;  // NUMBER

    private List<Map<String, Object>> allMarketsData;

    public GetMarketsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allMarketsData = new ArrayList<>();
    }

    public boolean fetchAllMarkets() {
        try {
            logger.info("Fetching all markets data from XDP_MARKETS");
            String query = "SELECT * FROM XDP_MARKETS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMarketsData = results;
                logger.info("Markets data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No markets data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching markets data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchMarketsByCondition(String whereClause) {
        try {
            logger.info("Fetching markets data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_MARKETS WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMarketsData = results;
                logger.info("Markets data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No markets data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching markets data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllMarketsRecords() {
        return allMarketsData;
    }

    public Map<String, Object> getMarketsRecordByIndex(int index) {
        if (index >= 0 && index < allMarketsData.size()) {
            return allMarketsData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allMarketsData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.marketseq = row.get("MARKETSEQ") != null ? row.get("MARKETSEQ").toString() : "";
        this.marketname = row.get("MARKETNAME") != null ? row.get("MARKETNAME").toString() : "";
        this.marketplace = row.get("MARKETPLACE") != null ? row.get("MARKETPLACE").toString() : "";
        this.marketcode = row.get("MARKETCODE") != null ? row.get("MARKETCODE").toString() : "";
        this.marketProtocol = row.get("MARKET_PROTOCOL") != null ? row.get("MARKET_PROTOCOL").toString() : "";
        this.stockExchangeCode = row.get("STOCK_EXCHANGE_CODE") != null ? row.get("STOCK_EXCHANGE_CODE").toString() : "";
        this.mic = row.get("MIC") != null ? row.get("MIC").toString() : "";
        this.marketIndicator = row.get("MARKET_INDICATOR") != null ? row.get("MARKET_INDICATOR").toString() : "";
        this.sourceid = row.get("SOURCEID") != null ? row.get("SOURCEID").toString() : "";
    }

    // Getters and Setters
    public String getMarketseq() {
        return marketseq;
    }
    public String getMarketname() {
        return marketname;
    }
    public String getMarketplace() {
        return marketplace;
    }
    public String getMarketcode() {
        return marketcode;
    }
    public String getMarketProtocol() {
        return marketProtocol;
    }
    public String getStockExchangeCode() {
        return stockExchangeCode;
    }
    public String getMic() {
        return mic;
    }
    public String getMarketIndicator() {
        return marketIndicator;
    }
    public String getSourceid() {
        return sourceid;
    }
    public void printAllMarketsData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              All Markets Records (" + allMarketsData.size() + " total)                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allMarketsData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allMarketsData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
