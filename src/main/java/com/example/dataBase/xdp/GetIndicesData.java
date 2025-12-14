package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Indices data from XDP_INDICES table
 */
public class GetIndicesData {

    private static final Logger logger = Logger.getLogger(GetIndicesData.class);
    private OracleDBConnection dbConnection;

    // XDP_INDICES column fields (33 columns)
    private String instCode;  // VARCHAR2
    private String indexlevel;  // FLOAT
    private String sessionhigh;  // FLOAT
    private String sessionlow;  // FLOAT
    private String percentageofcapitalization;  // FLOAT
    private String variationfrompreviousday;  // FLOAT
    private String indexlevelcode;  // VARCHAR2
    private String rebroadcastindicator;  // VARCHAR2
    private String preliminaryopeninglevel;  // FLOAT
    private String preliminaryopeningtime;  // VARCHAR2
    private String firstlevel;  // FLOAT
    private String firsttime;  // VARCHAR2
    private String openingreferencelevel;  // FLOAT
    private String openingreferencetime;  // VARCHAR2
    private String closingreferencelevel;  // FLOAT
    private String closingreferencetime;  // VARCHAR2
    private String percentvariationprevclos;  // FLOAT
    private String highlevel;  // FLOAT
    private String hightime;  // VARCHAR2
    private String lowlevel;  // FLOAT
    private String lowtime;  // VARCHAR2
    private String clearinglevel;  // FLOAT
    private String clearingtime;  // VARCHAR2
    private String liquidationlevel;  // FLOAT
    private String liquidationtime;  // VARCHAR2
    private String typeoflevel;  // NUMBER
    private String marketId;  // NUMBER
    private String numofsecuritiesquoted;  // NUMBER
    private String updatedbymsgno;  // VARCHAR2
    private String dateoflastupdate;  // DATE
    private String closingindexlevel;  // FLOAT
    private String netchange;  // NUMBER
    private String dateoflastupdatenano;  // TIMESTAMP(8)

    private List<Map<String, Object>> allIndicesData;

    public GetIndicesData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allIndicesData = new ArrayList<>();
    }

    public boolean fetchAllIndices() {
        try {
            logger.info("Fetching all indices data from XDP_INDICES");
            String query = "SELECT * FROM XDP_INDICES";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allIndicesData = results;
                logger.info("Indices data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No indices data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching indices data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchIndicesByCondition(String whereClause) {
        try {
            logger.info("Fetching indices data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_INDICES WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allIndicesData = results;
                logger.info("Indices data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No indices data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching indices data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllIndicesRecords() {
        return allIndicesData;
    }

    public Map<String, Object> getIndicesRecordByIndex(int index) {
        if (index >= 0 && index < allIndicesData.size()) {
            return allIndicesData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allIndicesData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.instCode = row.get("INST_CODE") != null ? row.get("INST_CODE").toString() : "";
        this.indexlevel = row.get("INDEXLEVEL") != null ? row.get("INDEXLEVEL").toString() : "";
        this.sessionhigh = row.get("SESSIONHIGH") != null ? row.get("SESSIONHIGH").toString() : "";
        this.sessionlow = row.get("SESSIONLOW") != null ? row.get("SESSIONLOW").toString() : "";
        this.percentageofcapitalization = row.get("PERCENTAGEOFCAPITALIZATION") != null ? row.get("PERCENTAGEOFCAPITALIZATION").toString() : "";
        this.variationfrompreviousday = row.get("VARIATIONFROMPREVIOUSDAY") != null ? row.get("VARIATIONFROMPREVIOUSDAY").toString() : "";
        this.indexlevelcode = row.get("INDEXLEVELCODE") != null ? row.get("INDEXLEVELCODE").toString() : "";
        this.rebroadcastindicator = row.get("REBROADCASTINDICATOR") != null ? row.get("REBROADCASTINDICATOR").toString() : "";
        this.preliminaryopeninglevel = row.get("PRELIMINARYOPENINGLEVEL") != null ? row.get("PRELIMINARYOPENINGLEVEL").toString() : "";
        this.preliminaryopeningtime = row.get("PRELIMINARYOPENINGTIME") != null ? row.get("PRELIMINARYOPENINGTIME").toString() : "";
        this.firstlevel = row.get("FIRSTLEVEL") != null ? row.get("FIRSTLEVEL").toString() : "";
        this.firsttime = row.get("FIRSTTIME") != null ? row.get("FIRSTTIME").toString() : "";
        this.openingreferencelevel = row.get("OPENINGREFERENCELEVEL") != null ? row.get("OPENINGREFERENCELEVEL").toString() : "";
        this.openingreferencetime = row.get("OPENINGREFERENCETIME") != null ? row.get("OPENINGREFERENCETIME").toString() : "";
        this.closingreferencelevel = row.get("CLOSINGREFERENCELEVEL") != null ? row.get("CLOSINGREFERENCELEVEL").toString() : "";
        this.closingreferencetime = row.get("CLOSINGREFERENCETIME") != null ? row.get("CLOSINGREFERENCETIME").toString() : "";
        this.percentvariationprevclos = row.get("PERCENTVARIATIONPREVCLOS") != null ? row.get("PERCENTVARIATIONPREVCLOS").toString() : "";
        this.highlevel = row.get("HIGHLEVEL") != null ? row.get("HIGHLEVEL").toString() : "";
        this.hightime = row.get("HIGHTIME") != null ? row.get("HIGHTIME").toString() : "";
        this.lowlevel = row.get("LOWLEVEL") != null ? row.get("LOWLEVEL").toString() : "";
        this.lowtime = row.get("LOWTIME") != null ? row.get("LOWTIME").toString() : "";
        this.clearinglevel = row.get("CLEARINGLEVEL") != null ? row.get("CLEARINGLEVEL").toString() : "";
        this.clearingtime = row.get("CLEARINGTIME") != null ? row.get("CLEARINGTIME").toString() : "";
        this.liquidationlevel = row.get("LIQUIDATIONLEVEL") != null ? row.get("LIQUIDATIONLEVEL").toString() : "";
        this.liquidationtime = row.get("LIQUIDATIONTIME") != null ? row.get("LIQUIDATIONTIME").toString() : "";
        this.typeoflevel = row.get("TYPEOFLEVEL") != null ? row.get("TYPEOFLEVEL").toString() : "";
        this.marketId = row.get("MARKET_ID") != null ? row.get("MARKET_ID").toString() : "";
        this.numofsecuritiesquoted = row.get("NUMOFSECURITIESQUOTED") != null ? row.get("NUMOFSECURITIESQUOTED").toString() : "";
        this.updatedbymsgno = row.get("UPDATEDBYMSGNO") != null ? row.get("UPDATEDBYMSGNO").toString() : "";
        this.dateoflastupdate = row.get("DATEOFLASTUPDATE") != null ? row.get("DATEOFLASTUPDATE").toString() : "";
        this.closingindexlevel = row.get("CLOSINGINDEXLEVEL") != null ? row.get("CLOSINGINDEXLEVEL").toString() : "";
        this.netchange = row.get("NETCHANGE") != null ? row.get("NETCHANGE").toString() : "";
        this.dateoflastupdatenano = row.get("DATEOFLASTUPDATENANO") != null ? row.get("DATEOFLASTUPDATENANO").toString() : "";
    }

    // Getters and Setters (compact format)
    public String getInstCode() { return instCode; }
    public String getIndexlevel() { return indexlevel; }
    public String getSessionhigh() { return sessionhigh; }
    public String getSessionlow() { return sessionlow; }
    public String getPercentageofcapitalization() { return percentageofcapitalization; }
    public String getVariationfrompreviousday() { return variationfrompreviousday; }
    public String getIndexlevelcode() { return indexlevelcode; }
    public String getRebroadcastindicator() { return rebroadcastindicator; }
    public String getPreliminaryopeninglevel() { return preliminaryopeninglevel; }
    public String getPreliminaryopeningtime() { return preliminaryopeningtime; }
    public String getFirstlevel() { return firstlevel; }
    public String getFirsttime() { return firsttime; }
    public String getOpeningreferencelevel() { return openingreferencelevel; }
    public String getOpeningreferencetime() { return openingreferencetime; }
    public String getClosingreferencelevel() { return closingreferencelevel; }
    public String getClosingreferencetime() { return closingreferencetime; }
    public String getPercentvariationprevclos() { return percentvariationprevclos; }
    public String getHighlevel() { return highlevel; }
    public String getHightime() { return hightime; }
    public String getLowlevel() { return lowlevel; }
    public String getLowtime() { return lowtime; }
    public String getClearinglevel() { return clearinglevel; }
    public String getClearingtime() { return clearingtime; }
    public String getLiquidationlevel() { return liquidationlevel; }
    public String getLiquidationtime() { return liquidationtime; }
    public String getTypeoflevel() { return typeoflevel; }
    public String getMarketId() { return marketId; }
    public String getNumofsecuritiesquoted() { return numofsecuritiesquoted; }
    public String getUpdatedbymsgno() { return updatedbymsgno; }
    public String getDateoflastupdate() { return dateoflastupdate; }
    public String getClosingindexlevel() { return closingindexlevel; }
    public String getNetchange() { return netchange; }
    public String getDateoflastupdatenano() { return dateoflastupdatenano; }

    public void printAllIndicesData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║             All Indices Records (" + allIndicesData.size() + " total)                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allIndicesData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allIndicesData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
