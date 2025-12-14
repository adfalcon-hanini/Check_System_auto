package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store FIX Send Query Tags data from SEC_FIX_SEND_QUEY_TAGS joined with SEC_ORDERS
 */
public class GetFixSendQueryTagsData {

    private static final Logger logger = Logger.getLogger(GetFixSendQueryTagsData.class);
    private OracleDBConnection dbConnection;

    // FIX query tags data fields
    private String slNo;
    private String msgDate;
    private String tag52SendingTime;
    private String clientId;
    private String tag55Symbol;
    private String tag54Side;
    private String tag38OrderQty;
    private String tag432ExpireDate;
    private String status;

    private List<Map<String, Object>> allFixSendQueryTagsData;

    public GetFixSendQueryTagsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allFixSendQueryTagsData = new ArrayList<>();
    }

    public boolean fetchFixSendQueryTagsByClientId(String clientId) {
        try {
            logger.info("Fetching FIX send query tags by client ID: " + clientId);
            String query = "SELECT SF.SL_NO, " +
                          "       SF.MSG_DATE, " +
                          "       SF.TAG_52_SENDING_TIME, " +
                          "       SO.CL_ID, " +
                          "       SF.TAG_55_SYMBOL, " +
                          "       SF.TAG_54_SIDE, " +
                          "       SF.TAG_38_ORDER_QTY, " +
                          "       SF.TAG_432_EXPIREDATE, " +
                          "       SF.STATUS " +
                          "  FROM SEC_FIX_SEND_QUEY_TAGS SF " +
                          "INNER JOIN SEC_ORDERS SO " +
                          "    ON SF.SL_NO = SO.SL_NO " +
                          "WHERE SO.CL_ID = ? " +
                          "   AND TRUNC(SF.TIME_STAMP) = TRUNC(SYSDATE) " +
                          "ORDER BY SF.SL_NO DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allFixSendQueryTagsData = results;
                parseFixSendQueryTagsData(results.get(0));
                logger.info("FIX send query tags data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No FIX send query tags found for client ID: " + clientId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching FIX send query tags data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchFixSendQueryTagsByClientIdAndDate(String clientId, String date) {
        try {
            logger.info("Fetching FIX send query tags by client ID: " + clientId + " and date: " + date);
            String query = "SELECT SF.SL_NO, " +
                          "       SF.MSG_DATE, " +
                          "       SF.TAG_52_SENDING_TIME, " +
                          "       SO.CL_ID, " +
                          "       SF.TAG_55_SYMBOL, " +
                          "       SF.TAG_54_SIDE, " +
                          "       SF.TAG_38_ORDER_QTY, " +
                          "       SF.TAG_432_EXPIREDATE, " +
                          "       SF.STATUS " +
                          "  FROM SEC_FIX_SEND_QUEY_TAGS SF " +
                          "INNER JOIN SEC_ORDERS SO " +
                          "    ON SF.SL_NO = SO.SL_NO " +
                          "WHERE SO.CL_ID = ? " +
                          "   AND TRUNC(SF.TIME_STAMP) = TO_DATE(?, 'DD-MON-YY') " +
                          "ORDER BY SF.SL_NO DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId, date);

            if (!results.isEmpty()) {
                allFixSendQueryTagsData = results;
                parseFixSendQueryTagsData(results.get(0));
                logger.info("FIX send query tags data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No FIX send query tags found for client ID: " + clientId + " on date: " + date);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching FIX send query tags data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchFixSendQueryTagsBySlNo(String slNo) {
        try {
            logger.info("Fetching FIX send query tags by SL_NO: " + slNo);
            String query = "SELECT SF.SL_NO, " +
                          "       SF.MSG_DATE, " +
                          "       SF.TAG_52_SENDING_TIME, " +
                          "       SO.CL_ID, " +
                          "       SF.TAG_55_SYMBOL, " +
                          "       SF.TAG_54_SIDE, " +
                          "       SF.TAG_38_ORDER_QTY, " +
                          "       SF.TAG_432_EXPIREDATE, " +
                          "       SF.STATUS " +
                          "  FROM SEC_FIX_SEND_QUEY_TAGS SF " +
                          "INNER JOIN SEC_ORDERS SO " +
                          "    ON SF.SL_NO = SO.SL_NO " +
                          "WHERE SF.SL_NO = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, slNo);

            if (!results.isEmpty()) {
                allFixSendQueryTagsData = results;
                parseFixSendQueryTagsData(results.get(0));
                logger.info("FIX send query tags data fetched successfully");
                return true;
            } else {
                logger.warn("No FIX send query tags found for SL_NO: " + slNo);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching FIX send query tags data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchFixSendQueryTagsBySymbol(String clientId, String symbol) {
        try {
            logger.info("Fetching FIX send query tags by client ID: " + clientId + " and symbol: " + symbol);
            String query = "SELECT SF.SL_NO, " +
                          "       SF.MSG_DATE, " +
                          "       SF.TAG_52_SENDING_TIME, " +
                          "       SO.CL_ID, " +
                          "       SF.TAG_55_SYMBOL, " +
                          "       SF.TAG_54_SIDE, " +
                          "       SF.TAG_38_ORDER_QTY, " +
                          "       SF.TAG_432_EXPIREDATE, " +
                          "       SF.STATUS " +
                          "  FROM SEC_FIX_SEND_QUEY_TAGS SF " +
                          "INNER JOIN SEC_ORDERS SO " +
                          "    ON SF.SL_NO = SO.SL_NO " +
                          "WHERE SO.CL_ID = ? " +
                          "   AND SF.TAG_55_SYMBOL = ? " +
                          "   AND TRUNC(SF.TIME_STAMP) = TRUNC(SYSDATE) " +
                          "ORDER BY SF.SL_NO DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId, symbol);

            if (!results.isEmpty()) {
                allFixSendQueryTagsData = results;
                parseFixSendQueryTagsData(results.get(0));
                logger.info("FIX send query tags data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No FIX send query tags found for client ID: " + clientId + " and symbol: " + symbol);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching FIX send query tags data: " + e.getMessage(), e);
            return false;
        }
    }

    private void parseFixSendQueryTagsData(Map<String, Object> row) {
        this.slNo = row.get("SL_NO") != null ? row.get("SL_NO").toString() : "";
        this.msgDate = row.get("MSG_DATE") != null ? row.get("MSG_DATE").toString() : "";
        this.tag52SendingTime = row.get("TAG_52_SENDING_TIME") != null ? row.get("TAG_52_SENDING_TIME").toString() : "";
        this.clientId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
        this.tag55Symbol = row.get("TAG_55_SYMBOL") != null ? row.get("TAG_55_SYMBOL").toString() : "";
        this.tag54Side = row.get("TAG_54_SIDE") != null ? row.get("TAG_54_SIDE").toString() : "";
        this.tag38OrderQty = row.get("TAG_38_ORDER_QTY") != null ? row.get("TAG_38_ORDER_QTY").toString() : "";
        this.tag432ExpireDate = row.get("TAG_432_EXPIREDATE") != null ? row.get("TAG_432_EXPIREDATE").toString() : "";
        this.status = row.get("STATUS") != null ? row.get("STATUS").toString() : "";
    }

    public void printFixSendQueryTagsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                        FIX Send Query Tags Table                                                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-10s ║ %-12s ║ %-20s ║ %-10s ║ %-12s ║ %-8s ║ %-10s ║ %-15s ║ %-10s ║%n",
                          "SL_NO", "MSG_DATE", "SENDING_TIME", "CL_ID", "SYMBOL", "SIDE", "ORDER_QTY", "EXPIRE_DATE", "STATUS");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allFixSendQueryTagsData) {
            String slNo = record.get("SL_NO") != null ? record.get("SL_NO").toString() : "";
            String msgDate = record.get("MSG_DATE") != null ? record.get("MSG_DATE").toString() : "";
            String sendingTime = record.get("TAG_52_SENDING_TIME") != null ? record.get("TAG_52_SENDING_TIME").toString() : "";
            String clId = record.get("CL_ID") != null ? record.get("CL_ID").toString() : "";
            String symbol = record.get("TAG_55_SYMBOL") != null ? record.get("TAG_55_SYMBOL").toString() : "";
            String side = record.get("TAG_54_SIDE") != null ? record.get("TAG_54_SIDE").toString() : "";
            String orderQty = record.get("TAG_38_ORDER_QTY") != null ? record.get("TAG_38_ORDER_QTY").toString() : "";
            String expireDate = record.get("TAG_432_EXPIREDATE") != null ? record.get("TAG_432_EXPIREDATE").toString() : "";
            String status = record.get("STATUS") != null ? record.get("STATUS").toString() : "";

            if (msgDate.length() > 12) msgDate = msgDate.substring(0, 9) + "...";
            if (sendingTime.length() > 20) sendingTime = sendingTime.substring(0, 17) + "...";
            if (expireDate.length() > 15) expireDate = expireDate.substring(0, 12) + "...";

            System.out.printf("║ %-10s ║ %-12s ║ %-20s ║ %-10s ║ %-12s ║ %-8s ║ %-10s ║ %-15s ║ %-10s ║%n",
                            slNo, msgDate, sendingTime, clId, symbol, side, orderQty, expireDate, status);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allFixSendQueryTagsData.size());
    }

    public void printFixSendQueryTagsSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              FIX Send Query Tags Summary                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allFixSendQueryTagsData.isEmpty()) {
            System.out.println("No FIX send query tags found.");
            return;
        }

        int buyOrders = 0;
        int sellOrders = 0;
        int totalQuantity = 0;

        for (Map<String, Object> record : allFixSendQueryTagsData) {
            String side = record.get("TAG_54_SIDE") != null ? record.get("TAG_54_SIDE").toString() : "";
            if ("1".equals(side) || "BUY".equalsIgnoreCase(side)) {
                buyOrders++;
            } else if ("2".equals(side) || "SELL".equalsIgnoreCase(side)) {
                sellOrders++;
            }

            String qty = record.get("TAG_38_ORDER_QTY") != null ? record.get("TAG_38_ORDER_QTY").toString() : "0";
            try {
                totalQuantity += Integer.parseInt(qty);
            } catch (NumberFormatException e) {
                // Skip invalid quantities
            }
        }

        System.out.println("\nTotal FIX Messages: " + allFixSendQueryTagsData.size());
        System.out.println("Buy Orders: " + buyOrders);
        System.out.println("Sell Orders: " + sellOrders);
        System.out.println("Total Quantity: " + totalQuantity);
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    public void printDetailedFixSendQueryTags() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║           Detailed FIX Send Query Tags Information             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        for (Map<String, Object> record : allFixSendQueryTagsData) {
            System.out.println("\n┌────────────────────────────────────────────────────────────────┐");
            System.out.printf("│ Serial Number (SL_NO): %-39s │%n", record.get("SL_NO"));
            System.out.printf("│ Client ID: %-51s │%n", record.get("CL_ID"));
            System.out.printf("│ Symbol: %-54s │%n", record.get("TAG_55_SYMBOL"));
            System.out.printf("│ Side: %-56s │%n", record.get("TAG_54_SIDE"));
            System.out.printf("│ Order Quantity: %-46s │%n", record.get("TAG_38_ORDER_QTY"));
            System.out.printf("│ Message Date: %-48s │%n", record.get("MSG_DATE"));
            System.out.printf("│ Sending Time: %-48s │%n", record.get("TAG_52_SENDING_TIME"));
            System.out.printf("│ Expire Date: %-49s │%n", record.get("TAG_432_EXPIREDATE"));
            System.out.printf("│ Status: %-54s │%n", record.get("STATUS"));
            System.out.println("└────────────────────────────────────────────────────────────────┘");
        }

        System.out.println("\nTotal Records: " + allFixSendQueryTagsData.size());
    }

    // Getters
    public List<Map<String, Object>> getAllFixSendQueryTagsRecords() { return allFixSendQueryTagsData; }
    public int getRecordCount() { return allFixSendQueryTagsData.size(); }
    public String getSlNo() { return slNo; }
    public String getMsgDate() { return msgDate; }
    public String getTag52SendingTime() { return tag52SendingTime; }
    public String getClientId() { return clientId; }
    public String getTag55Symbol() { return tag55Symbol; }
    public String getTag54Side() { return tag54Side; }
    public String getTag38OrderQty() { return tag38OrderQty; }
    public String getTag432ExpireDate() { return tag432ExpireDate; }
    public String getStatus() { return status; }
}
