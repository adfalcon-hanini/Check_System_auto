package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Quotes data from XDP_QUOTES table
 */
public class GetQuotesData {

    private static final Logger logger = Logger.getLogger(GetQuotesData.class);
    private OracleDBConnection dbConnection;

    // XDP_QUOTES column fields
    private String seqid;  // NUMBER
    private String instSeq;  // VARCHAR2
    private String quoteDate;  // DATE
    private String askprice;  // NUMBER
    private String asksize;  // NUMBER
    private String bidprice;  // NUMBER
    private String bidsize;  // NUMBER
    private String marketType;  // CHAR
    private String numberaskorders;  // NUMBER
    private String numberbidorders;  // NUMBER
    private String typeofaskprice;  // NUMBER
    private String typeofbidprice;  // CHAR
    private String quotecondition;  // CHAR
    private String quotenumber;  // NUMBER

    private List<Map<String, Object>> allQuotesData;

    public GetQuotesData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allQuotesData = new ArrayList<>();
    }

    public boolean fetchAllQuotes() {
        try {
            logger.info("Fetching all quotes data from XDP_QUOTES");
            String query = "SELECT * FROM XDP_QUOTES";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allQuotesData = results;
                logger.info("Quotes data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No quotes data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching quotes data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchQuotesByCondition(String whereClause) {
        try {
            logger.info("Fetching quotes data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_QUOTES WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allQuotesData = results;
                logger.info("Quotes data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No quotes data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching quotes data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllQuotesRecords() {
        return allQuotesData;
    }

    public Map<String, Object> getQuotesRecordByIndex(int index) {
        if (index >= 0 && index < allQuotesData.size()) {
            return allQuotesData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allQuotesData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.seqid = row.get("SEQID") != null ? row.get("SEQID").toString() : "";
        this.instSeq = row.get("INST_SEQ") != null ? row.get("INST_SEQ").toString() : "";
        this.quoteDate = row.get("QUOTE_DATE") != null ? row.get("QUOTE_DATE").toString() : "";
        this.askprice = row.get("ASKPRICE") != null ? row.get("ASKPRICE").toString() : "";
        this.asksize = row.get("ASKSIZE") != null ? row.get("ASKSIZE").toString() : "";
        this.bidprice = row.get("BIDPRICE") != null ? row.get("BIDPRICE").toString() : "";
        this.bidsize = row.get("BIDSIZE") != null ? row.get("BIDSIZE").toString() : "";
        this.marketType = row.get("MARKET_TYPE") != null ? row.get("MARKET_TYPE").toString() : "";
        this.numberaskorders = row.get("NUMBERASKORDERS") != null ? row.get("NUMBERASKORDERS").toString() : "";
        this.numberbidorders = row.get("NUMBERBIDORDERS") != null ? row.get("NUMBERBIDORDERS").toString() : "";
        this.typeofaskprice = row.get("TYPEOFASKPRICE") != null ? row.get("TYPEOFASKPRICE").toString() : "";
        this.typeofbidprice = row.get("TYPEOFBIDPRICE") != null ? row.get("TYPEOFBIDPRICE").toString() : "";
        this.quotecondition = row.get("QUOTECONDITION") != null ? row.get("QUOTECONDITION").toString() : "";
        this.quotenumber = row.get("QUOTENUMBER") != null ? row.get("QUOTENUMBER").toString() : "";
    }

    // Getters and Setters
    public String getSeqid() { return seqid; }
    public String getInstSeq() { return instSeq; }
    public String getQuoteDate() { return quoteDate; }
    public String getAskprice() { return askprice; }
    public String getAsksize() { return asksize; }
    public String getBidprice() { return bidprice; }
    public String getBidsize() { return bidsize; }
    public String getMarketType() { return marketType; }
    public String getNumberaskorders() { return numberaskorders; }
    public String getNumberbidorders() { return numberbidorders; }
    public String getTypeofaskprice() { return typeofaskprice; }
    public String getTypeofbidprice() { return typeofbidprice; }
    public String getQuotecondition() { return quotecondition; }
    public String getQuotenumber() { return quotenumber; }

    public void printAllQuotesData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              All Quotes Records (" + allQuotesData.size() + " total)                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allQuotesData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allQuotesData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
