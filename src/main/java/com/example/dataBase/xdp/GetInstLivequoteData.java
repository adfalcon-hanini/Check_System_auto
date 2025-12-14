package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Instrument Live Quote data from XDP_INST_LIVEQUOTE table
 */
public class GetInstLivequoteData {

    private static final Logger logger = Logger.getLogger(GetInstLivequoteData.class);
    private OracleDBConnection dbConnection;

    // XDP_INST_LIVEQUOTE column fields (46 columns)
    private String instCode, marketType, updateDate, firstprice, highestprice, lowestprice, lastprice, closeprice;
    private String indicativPrice, variation, volume, direction, valuatuationPrice, groupCode, typeofprice;
    private String cumulativequantity, typeoflastprice, tickdirection, sendingindicator, closingpricerule;
    private String adjustedclosingprice, nbtrades, qtyshares, amounttraded, imprice, imvariation, imvolume;
    private String instrumentvaluationprice, weeksHigh52, weeksLow52, updatedbymsgno, dateoflastupdate;
    private String tradeCancellationPrice, askPrice, bidPrice, imbalanceAskPrice, imbalanceBidPrice;
    private String changeOfFirstPrice, newPreviousClosingPrice, warrantOpeningIndicative, warrantClosingIndicative;
    private String avgPrice, changeStatus, auctionType, imbalanceDirection, pairedQuantity;

    private List<Map<String, Object>> allInstLivequoteData;

    public GetInstLivequoteData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allInstLivequoteData = new ArrayList<>();
    }

    public boolean fetchAllInstLivequote() {
        try {
            logger.info("Fetching all instrument live quote data from XDP_INST_LIVEQUOTE");
            String query = "SELECT * FROM XDP_INST_LIVEQUOTE";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allInstLivequoteData = results;
                logger.info("Instrument live quote data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No instrument live quote data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching instrument live quote data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchInstLivequoteByCondition(String whereClause) {
        try {
            logger.info("Fetching instrument live quote data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_INST_LIVEQUOTE WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allInstLivequoteData = results;
                logger.info("Instrument live quote data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No instrument live quote data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching instrument live quote data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllInstLivequoteRecords() {
        return allInstLivequoteData;
    }

    public Map<String, Object> getInstLivequoteRecordByIndex(int index) {
        if (index >= 0 && index < allInstLivequoteData.size()) {
            return allInstLivequoteData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allInstLivequoteData.size();
    }

    public void printAllInstLivequoteData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║         All Inst Livequote Records (" + allInstLivequoteData.size() + " total)          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allInstLivequoteData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allInstLivequoteData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
