package com.example.screensData.orders;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Orders History data from XDP_ORDERS_HIST table
 */
public class GetOrdersHistData {

    private static final Logger logger = Logger.getLogger(GetOrdersHistData.class);
    private OracleDBConnection dbConnection;

    // XDP_ORDERS_HIST column fields (same as XDP_ORDERS)
    private String instSeq;  // VARCHAR2
    private String price;  // FLOAT
    private String aggregatedvolume;  // NUMBER
    private String volume;  // NUMBER
    private String linkid;  // NUMBER
    private String marketSeq;  // NUMBER
    private String numberorders;  // CHAR
    private String side;  // CHAR
    private String ordertype;  // VARCHAR2
    private String actiontype;  // VARCHAR2
    private String orderdate;  // DATE
    private String orderprioritydate;  // TIMESTAMP(6)
    private String orderorigin;  // NUMBER
    private String orderprioritymicrosec;  // NUMBER
    private String orderpriority;  // NUMBER
    private String isdeleted;  // NUMBER
    private String createdBy;  // VARCHAR2
    private String creationDate;  // DATE
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String marketDataGroup;  // NUMBER
    private String subbook;  // NUMBER
    private String orderid;  // VARCHAR2
    private String mitchOrderid;  // VARCHAR2

    private List<Map<String, Object>> allOrdersHistData;

    public GetOrdersHistData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allOrdersHistData = new ArrayList<>();
    }

    public boolean fetchAllOrdersHist() {
        try {
            logger.info("Fetching all orders history data from XDP_ORDERS_HIST");
            String query = "SELECT * FROM XDP_ORDERS_HIST";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allOrdersHistData = results;
                logger.info("Orders history data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders history data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching orders history data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchOrdersHistByCondition(String whereClause) {
        try {
            logger.info("Fetching orders history data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_ORDERS_HIST WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allOrdersHistData = results;
                logger.info("Orders history data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders history data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching orders history data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchOrdersHistByLimit(int limit) {
        try {
            logger.info("Fetching last " + limit + " orders history records");
            String query = "SELECT * FROM XDP_ORDERS_HIST ORDER BY ROWNUM DESC FETCH FIRST " + limit + " ROWS ONLY";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allOrdersHistData = results;
                logger.info("Orders history data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders history data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching orders history data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllOrdersHistRecords() {
        return allOrdersHistData;
    }

    public Map<String, Object> getOrdersHistRecordByIndex(int index) {
        if (index >= 0 && index < allOrdersHistData.size()) {
            return allOrdersHistData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allOrdersHistData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.instSeq = row.get("INST_SEQ") != null ? row.get("INST_SEQ").toString() : "";
        this.price = row.get("PRICE") != null ? row.get("PRICE").toString() : "";
        this.aggregatedvolume = row.get("AGGREGATEDVOLUME") != null ? row.get("AGGREGATEDVOLUME").toString() : "";
        this.volume = row.get("VOLUME") != null ? row.get("VOLUME").toString() : "";
        this.linkid = row.get("LINKID") != null ? row.get("LINKID").toString() : "";
        this.marketSeq = row.get("MARKET_SEQ") != null ? row.get("MARKET_SEQ").toString() : "";
        this.numberorders = row.get("NUMBERORDERS") != null ? row.get("NUMBERORDERS").toString() : "";
        this.side = row.get("SIDE") != null ? row.get("SIDE").toString() : "";
        this.ordertype = row.get("ORDERTYPE") != null ? row.get("ORDERTYPE").toString() : "";
        this.actiontype = row.get("ACTIONTYPE") != null ? row.get("ACTIONTYPE").toString() : "";
        this.orderdate = row.get("ORDERDATE") != null ? row.get("ORDERDATE").toString() : "";
        this.orderprioritydate = row.get("ORDERPRIORITYDATE") != null ? row.get("ORDERPRIORITYDATE").toString() : "";
        this.orderorigin = row.get("ORDERORIGIN") != null ? row.get("ORDERORIGIN").toString() : "";
        this.orderprioritymicrosec = row.get("ORDERPRIORITYMICROSEC") != null ? row.get("ORDERPRIORITYMICROSEC").toString() : "";
        this.orderpriority = row.get("ORDERPRIORITY") != null ? row.get("ORDERPRIORITY").toString() : "";
        this.isdeleted = row.get("ISDELETED") != null ? row.get("ISDELETED").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.marketDataGroup = row.get("MARKET_DATA_GROUP") != null ? row.get("MARKET_DATA_GROUP").toString() : "";
        this.subbook = row.get("SUBBOOK") != null ? row.get("SUBBOOK").toString() : "";
        this.orderid = row.get("ORDERID") != null ? row.get("ORDERID").toString() : "";
        this.mitchOrderid = row.get("MITCH_ORDERID") != null ? row.get("MITCH_ORDERID").toString() : "";
    }

    // Getters and Setters
    public String getInstSeq() { return instSeq; }
    public String getPrice() { return price; }
    public String getAggregatedvolume() { return aggregatedvolume; }
    public String getVolume() { return volume; }
    public String getLinkid() { return linkid; }
    public String getMarketSeq() { return marketSeq; }
    public String getNumberorders() { return numberorders; }
    public String getSide() { return side; }
    public String getOrdertype() { return ordertype; }
    public String getActiontype() { return actiontype; }
    public String getOrderdate() { return orderdate; }
    public String getOrderprioritydate() { return orderprioritydate; }
    public String getOrderorigin() { return orderorigin; }
    public String getOrderprioritymicrosec() { return orderprioritymicrosec; }
    public String getOrderpriority() { return orderpriority; }
    public String getIsdeleted() { return isdeleted; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getMarketDataGroup() { return marketDataGroup; }
    public String getSubbook() { return subbook; }
    public String getOrderid() { return orderid; }
    public String getMitchOrderid() { return mitchOrderid; }

    public void printAllOrdersHistData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║         All Orders Hist Records (" + allOrdersHistData.size() + " total)              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allOrdersHistData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allOrdersHistData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    public void printOrdersHistTable() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   Orders History Table                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        if (allOrdersHistData.isEmpty()) {
            System.out.println("║  No data available                                            ║");
        } else {
            // Get column names from first record
            Map<String, Object> firstRecord = allOrdersHistData.get(0);
            List<String> columnNames = new ArrayList<>(firstRecord.keySet());

            // Print header
            System.out.print("║ ");
            for (String col : columnNames) {
                System.out.printf("%-20s │ ", col);
            }
            System.out.println();
            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            // Print data rows
            for (Map<String, Object> record : allOrdersHistData) {
                System.out.print("║ ");
                for (String col : columnNames) {
                    String value = record.get(col) != null ? record.get(col).toString() : "";
                    if (value.length() > 20) value = value.substring(0, 17) + "...";
                    System.out.printf("%-20s │ ", value);
                }
                System.out.println();
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allOrdersHistData.size());
    }
}
