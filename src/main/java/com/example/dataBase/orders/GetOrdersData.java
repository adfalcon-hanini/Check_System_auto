package com.example.dataBase.orders;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Orders data from SEC_ORDERS table
 */
public class GetOrdersData {

    private static final Logger logger = Logger.getLogger(GetOrdersData.class);
    private OracleDBConnection dbConnection;

    // Order data fields - Main columns from SEC_ORDERS table
    private String orderId;  // ORDER_ID_37
    private String clientId;  // CL_ID
    private String orderStatus;  // ORDER_STATUS
    private String companyCode;  // COMPANY_CODE (instrument)
    private String volumeOfShares;  // VOLUMN_OF_SHARES
    private String actualPrice;  // ACTUAL_PRICE
    private String orderType;  // ORDER_TYPE
    private String orderDate;  // ORDER_DATE
    private String timeStamp;  // TIME_STAMP
    private String createdBy;  // CREATED_BY
    private String userId;  // USER_ID
    private String executedShares;  // EXECUTED_SHARES
    private String remainingShares;  // REMAINING_SHARES
    private String orderValue;  // ORDER_VALUE
    private String slNo;  // SL_NO
    private String dealingAcc;  // DEALING_ACC
    private String ninNumber;  // NIN_NUMBER
    private String orderSource;  // ORDER_SOURCE
    private String fixStatus;  // FIX_STATUS
    private String orderTime;  // ORDER_TIME
    private String entryDate;  // ENTRY_DATE
    private String mktOrderNo;  // MKT_ORDER_NO
    private String orderIdMitch;  // ORDER_ID_MITCH_278
    private String fixOrderType;  // FIX_ORD_TYPE_40
    private String tradingFees;  // TRADING_FEES

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allOrdersData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetOrdersData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allOrdersData = new ArrayList<>();
    }

    /**
     * Fetch orders by client ID for today's date
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchOrdersByClientIdToday(String clientId) {
        try {
            logger.info("Fetching orders for client ID: " + clientId + " for today");

            String query = "SELECT * FROM sec_orders o " +
                          "WHERE o.cl_id = ? " +
                          "AND order_date = TRUNC(SYSDATE) " +
                          "ORDER BY o.time_stamp DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Orders data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found for client ID: " + clientId + " for today");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching orders data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch orders by client ID for a specific date
     * @param clientId Client ID to query
     * @param orderDate Order date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchOrdersByClientIdAndDate(String clientId, String orderDate) {
        try {
            logger.info("Fetching orders for client ID: " + clientId + " for date: " + orderDate);

            String query = "SELECT * FROM sec_orders o " +
                          "WHERE o.cl_id = ? " +
                          "AND TRUNC(order_date) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "ORDER BY o.time_stamp DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId, orderDate);

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Orders data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found for client ID: " + clientId + " for date: " + orderDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching orders data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all orders by client ID (no date filter)
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchAllOrdersByClientId(String clientId) {
        try {
            logger.info("Fetching all orders for client ID: " + clientId);

            String query = "SELECT * FROM sec_orders o " +
                          "WHERE o.cl_id = ? " +
                          "ORDER BY o.time_stamp DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Orders data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found for client ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching orders data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch ALL orders from XDP_ORDERS table without any filter
     * @return true if data found, false otherwise
     */
    public boolean fetchAllOrdersFromXDP() {
        try {
            logger.info("Fetching ALL orders from XDP_ORDERS table");

            String query = "SELECT * FROM XDP_ORDERS ORDER BY ROWNUM DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Orders data fetched successfully from XDP_ORDERS. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found in XDP_ORDERS table");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching orders data from XDP_ORDERS: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch limited number of orders from XDP_ORDERS table
     * @param limit Maximum number of records to fetch
     * @return true if data found, false otherwise
     */
    public boolean fetchAllOrdersFromXDPWithLimit(int limit) {
        try {
            logger.info("Fetching last " + limit + " orders from XDP_ORDERS table");

            String query = "SELECT * FROM XDP_ORDERS ORDER BY ROWNUM DESC FETCH FIRST " + limit + " ROWS ONLY";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Orders data fetched successfully from XDP_ORDERS. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found in XDP_ORDERS table");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching orders data from XDP_ORDERS: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Dynamic method to fetch orders from SEC_ORDERS table with flexible parameters
     * Accepts any number of column-value pairs as parameters and builds a safe query
     *
     * @param parameters Map of column names and their values to filter by
     * @return true if data found, false otherwise
     *
     * Example usage:
     * Map<String, Object> params = new HashMap<>();
     * params.put("CL_ID", "12240");
     * params.put("ORDER_STATUS", "WAP");
     * params.put("COMPANY_CODE", "DOHI");
     * boolean result = ordersData.fetchOrdersWithDynamicParams(params);
     */
    public boolean fetchOrdersWithDynamicParams(Map<String, Object> parameters) {
        try {
            // Validate parameters
            if (parameters == null || parameters.isEmpty()) {
                logger.warn("No parameters provided for dynamic query");
                return false;
            }

            // Filter out null and empty values, and validate column names
            Map<String, Object> validParams = new HashMap<>();
            List<Object> paramValues = new ArrayList<>();
            StringBuilder whereClause = new StringBuilder();

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();

                // Skip null or empty values
                if (value == null) {
                    logger.debug("Skipping null value for column: " + columnName);
                    continue;
                }

                // Skip empty strings
                if (value instanceof String && ((String) value).trim().isEmpty()) {
                    logger.debug("Skipping empty value for column: " + columnName);
                    continue;
                }

                // Validate column name to prevent SQL injection (only allow alphanumeric and underscore)
                if (!isValidColumnName(columnName)) {
                    logger.warn("Invalid column name detected: " + columnName + ". Skipping.");
                    continue;
                }

                validParams.put(columnName, value);
            }

            // Check if we have any valid parameters after filtering
            if (validParams.isEmpty()) {
                logger.warn("No valid parameters after filtering null/empty values");
                return false;
            }

            // Build WHERE clause with parameterized query
            boolean isFirst = true;
            for (String columnName : validParams.keySet()) {
                if (!isFirst) {
                    whereClause.append(" AND ");
                }
                whereClause.append(columnName).append(" = ?");
                paramValues.add(validParams.get(columnName));
                isFirst = false;
            }

            // Build the complete query
            String query = "SELECT * FROM sec_orders WHERE " + whereClause.toString() + " ORDER BY TIME_STAMP DESC";

            logger.info("Executing dynamic query with " + validParams.size() + " parameter(s)");
            logger.debug("Query: " + query);
            logger.debug("Parameters: " + validParams);

            // Execute the query using parameterized statement
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, paramValues.toArray());

            if (!results.isEmpty()) {
                allOrdersData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Dynamic query executed successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found for the given parameters: " + validParams);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error executing dynamic query: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error in dynamic query: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Validates column name to prevent SQL injection
     * Only allows alphanumeric characters, underscores, and common database naming patterns
     *
     * @param columnName The column name to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidColumnName(String columnName) {
        if (columnName == null || columnName.trim().isEmpty()) {
            return false;
        }

        // Allow only letters, numbers, underscores, and hyphens
        // Column names must start with a letter or underscore
        String pattern = "^[A-Za-z_][A-Za-z0-9_]{0,63}$";
        return columnName.matches(pattern);
    }

    /**
     * Dynamic method to fetch orders with date range support
     * Extends the basic dynamic query to support date ranges
     *
     * @param parameters Map of column names and their values
     * @param dateColumn The date column to filter on (e.g., "ORDER_DATE")
     * @param startDate Start date (format: YYYY-MM-DD)
     * @param endDate End date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchOrdersWithDateRange(Map<String, Object> parameters, String dateColumn, String startDate, String endDate) {
        try {
            // Validate date column name
            if (!isValidColumnName(dateColumn)) {
                logger.error("Invalid date column name: " + dateColumn);
                return false;
            }

            // Initialize parameters map if null
            Map<String, Object> allParams = parameters != null ? new HashMap<>(parameters) : new HashMap<>();

            // Filter out null and empty values
            Map<String, Object> validParams = new HashMap<>();
            List<Object> paramValues = new ArrayList<>();
            StringBuilder whereClause = new StringBuilder();

            // Add regular parameters
            if (!allParams.isEmpty()) {
                for (Map.Entry<String, Object> entry : allParams.entrySet()) {
                    String columnName = entry.getKey();
                    Object value = entry.getValue();

                    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                        continue;
                    }

                    if (!isValidColumnName(columnName)) {
                        logger.warn("Invalid column name: " + columnName);
                        continue;
                    }

                    if (whereClause.length() > 0) {
                        whereClause.append(" AND ");
                    }
                    whereClause.append(columnName).append(" = ?");
                    paramValues.add(value);
                    validParams.put(columnName, value);
                }
            }

            // Add date range conditions
            if (startDate != null && !startDate.trim().isEmpty()) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("TRUNC(").append(dateColumn).append(") >= TO_DATE(?, 'YYYY-MM-DD')");
                paramValues.add(startDate);
            }

            if (endDate != null && !endDate.trim().isEmpty()) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append("TRUNC(").append(dateColumn).append(") <= TO_DATE(?, 'YYYY-MM-DD')");
                paramValues.add(endDate);
            }

            // Check if we have any conditions
            if (whereClause.length() == 0) {
                logger.warn("No valid parameters or date range provided");
                return false;
            }

            // Build the complete query
            String query = "SELECT * FROM sec_orders WHERE " + whereClause.toString() + " ORDER BY TIME_STAMP DESC";

            logger.info("Executing dynamic query with date range");
            logger.debug("Query: " + query);
            logger.debug("Parameters: " + validParams + ", startDate: " + startDate + ", endDate: " + endDate);

            // Execute the query
            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, paramValues.toArray());

            if (!results.isEmpty()) {
                allOrdersData = results;
                Map<String, Object> row = results.get(0);
                parseOrderData(row);
                logger.info("Dynamic date range query executed successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No orders found for the given parameters and date range");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error executing dynamic date range query: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error in dynamic date range query: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse order data from database row - Main columns from SEC_ORDERS table
     * @param row Database row containing order data
     */
    private void parseOrderData(Map<String, Object> row) {
        this.orderId = row.get("ORDER_ID_37") != null ? row.get("ORDER_ID_37").toString() : "";
        this.clientId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
        this.orderStatus = row.get("ORDER_STATUS") != null ? row.get("ORDER_STATUS").toString() : "";
        this.companyCode = row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "";
        this.volumeOfShares = row.get("VOLUMN_OF_SHARES") != null ? row.get("VOLUMN_OF_SHARES").toString() : "";
        this.actualPrice = row.get("ACTUAL_PRICE") != null ? row.get("ACTUAL_PRICE").toString() : "";
        this.orderType = row.get("ORDER_TYPE") != null ? row.get("ORDER_TYPE").toString() : "";
        this.orderDate = row.get("ORDER_DATE") != null ? row.get("ORDER_DATE").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.executedShares = row.get("EXECUTED_SHARES") != null ? row.get("EXECUTED_SHARES").toString() : "";
        this.remainingShares = row.get("REMAINING_SHARES") != null ? row.get("REMAINING_SHARES").toString() : "";
        this.orderValue = row.get("ORDER_VALUE") != null ? row.get("ORDER_VALUE").toString() : "";
        this.slNo = row.get("SL_NO") != null ? row.get("SL_NO").toString() : "";
        this.dealingAcc = row.get("DEALING_ACC") != null ? row.get("DEALING_ACC").toString() : "";
        this.ninNumber = row.get("NIN_NUMBER") != null ? row.get("NIN_NUMBER").toString() : "";
        this.orderSource = row.get("ORDER_SOURCE") != null ? row.get("ORDER_SOURCE").toString() : "";
        this.fixStatus = row.get("FIX_STATUS") != null ? row.get("FIX_STATUS").toString() : "";
        this.orderTime = row.get("ORDER_TIME") != null ? row.get("ORDER_TIME").toString() : "";
        this.entryDate = row.get("ENTRY_DATE") != null ? row.get("ENTRY_DATE").toString() : "";
        this.mktOrderNo = row.get("MKT_ORDER_NO") != null ? row.get("MKT_ORDER_NO").toString() : "";
        this.orderIdMitch = row.get("ORDER_ID_MITCH_278") != null ? row.get("ORDER_ID_MITCH_278").toString() : "";
        this.fixOrderType = row.get("FIX_ORD_TYPE_40") != null ? row.get("FIX_ORD_TYPE_40").toString() : "";
        this.tradingFees = row.get("TRADING_FEES") != null ? row.get("TRADING_FEES").toString() : "";
    }

    /**
     * Get all order data as a map (first row)
     * @return Map containing main SEC_ORDERS fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("orderId", this.orderId);
        data.put("clientId", this.clientId);
        data.put("orderStatus", this.orderStatus);
        data.put("companyCode", this.companyCode);
        data.put("volumeOfShares", this.volumeOfShares);
        data.put("actualPrice", this.actualPrice);
        data.put("orderType", this.orderType);
        data.put("orderDate", this.orderDate);
        data.put("timeStamp", this.timeStamp);
        data.put("createdBy", this.createdBy);
        data.put("userId", this.userId);
        data.put("executedShares", this.executedShares);
        data.put("remainingShares", this.remainingShares);
        data.put("orderValue", this.orderValue);
        data.put("slNo", this.slNo);
        data.put("dealingAcc", this.dealingAcc);
        data.put("ninNumber", this.ninNumber);
        data.put("orderSource", this.orderSource);
        data.put("fixStatus", this.fixStatus);
        data.put("orderTime", this.orderTime);
        data.put("entryDate", this.entryDate);
        data.put("mktOrderNo", this.mktOrderNo);
        data.put("orderIdMitch", this.orderIdMitch);
        data.put("fixOrderType", this.fixOrderType);
        data.put("tradingFees", this.tradingFees);
        return data;
    }

    /**
     * Get all order records
     * @return List of all order data rows
     */
    public List<Map<String, Object>> getAllOrderRecords() {
        return allOrdersData;
    }

    /**
     * Get order record by index
     * @param index Index of the record
     * @return Map containing order data for that index
     */
    public Map<String, Object> getOrderRecordByIndex(int index) {
        if (index >= 0 && index < allOrdersData.size()) {
            return allOrdersData.get(index);
        }
        return null;
    }

    /**
     * Get total number of order records
     * @return Number of records
     */
    public int getRecordCount() {
        return allOrdersData.size();
    }

    /**
     * Print order data to console (first record) - Main SEC_ORDERS fields
     */
    public void printOrderData() {
        System.out.println("\n========== Order Data (First Record) - SEC_ORDERS ==========");
        System.out.println("Order ID: " + orderId);
        System.out.println("Client ID: " + clientId);
        System.out.println("Order Status: " + orderStatus);
        System.out.println("Company Code (Instrument): " + companyCode);
        System.out.println("Volume of Shares: " + volumeOfShares);
        System.out.println("Actual Price: " + actualPrice);
        System.out.println("Order Type: " + orderType);
        System.out.println("Order Date: " + orderDate);
        System.out.println("Time Stamp: " + timeStamp);
        System.out.println("Created By: " + createdBy);
        System.out.println("User ID: " + userId);
        System.out.println("Executed Shares: " + executedShares);
        System.out.println("Remaining Shares: " + remainingShares);
        System.out.println("Order Value: " + orderValue);
        System.out.println("SL No: " + slNo);
        System.out.println("Dealing Account: " + dealingAcc);
        System.out.println("NIN Number: " + ninNumber);
        System.out.println("Order Source: " + orderSource);
        System.out.println("FIX Status: " + fixStatus);
        System.out.println("Order Time: " + orderTime);
        System.out.println("Entry Date: " + entryDate);
        System.out.println("Market Order No: " + mktOrderNo);
        System.out.println("Order ID Mitch: " + orderIdMitch);
        System.out.println("FIX Order Type: " + fixOrderType);
        System.out.println("Trading Fees: " + tradingFees);
        System.out.println("=============================================================");
    }

    /**
     * Print all order records
     */
    public void printAllOrderData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              All Order Records (" + allOrdersData.size() + " total)                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allOrdersData.size(); i++) {
            System.out.println("─────────────────── Order " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allOrdersData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-20s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print orders in a formatted table
     */
    public void printOrdersTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                   Orders Table                                                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-12s ║ %-10s ║ %-12s ║ %-10s ║ %-10s ║ %-10s ║ %-10s ║ %-15s ║%n",
                          "ORDER_ID", "CL_ID", "INST_CODE", "SIDE", "QUANTITY", "PRICE", "STATUS", "TIME_STAMP");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allOrdersData) {
            String ordId = record.get("ORDER_ID") != null ? record.get("ORDER_ID").toString() : "";
            String clId = record.get("CL_ID") != null ? record.get("CL_ID").toString() : "";
            String inst = record.get("INST_CODE") != null ? record.get("INST_CODE").toString() : "";
            String side = record.get("ORDER_SIDE") != null ? record.get("ORDER_SIDE").toString() : "";
            String qty = record.get("QUANTITY") != null ? record.get("QUANTITY").toString() : "";
            String prc = record.get("PRICE") != null ? record.get("PRICE").toString() : "";
            String status = record.get("ORDER_STATUS") != null ? record.get("ORDER_STATUS").toString() : "";
            String time = record.get("TIME_STAMP") != null ? record.get("TIME_STAMP").toString() : "";

            // Truncate long values
            if (ordId.length() > 12) ordId = ordId.substring(0, 9) + "...";
            if (inst.length() > 12) inst = inst.substring(0, 9) + "...";
            if (side.length() > 10) side = side.substring(0, 7) + "...";
            if (status.length() > 10) status = status.substring(0, 7) + "...";
            if (time.length() > 15) time = time.substring(0, 12) + "...";

            System.out.printf("║ %-12s ║ %-10s ║ %-12s ║ %-10s ║ %-10s ║ %-10s ║ %-10s ║ %-15s ║%n",
                            ordId, clId, inst, side, qty, prc, status, time);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Orders: " + allOrdersData.size());
    }

    /**
     * Print orders summary
     */
    public void printOrdersSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     Orders Summary                             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allOrdersData.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        // Count by order side
        int buyOrders = 0;
        int sellOrders = 0;
        int otherOrders = 0;

        // Count by status
        Map<String, Integer> statusCount = new HashMap<>();

        for (Map<String, Object> record : allOrdersData) {
            // Count by side
            String side = record.get("ORDER_SIDE") != null ? record.get("ORDER_SIDE").toString().toUpperCase() : "";
            if (side.contains("BUY") || side.equals("B")) {
                buyOrders++;
            } else if (side.contains("SELL") || side.equals("S")) {
                sellOrders++;
            } else {
                otherOrders++;
            }

            // Count by status
            String status = record.get("ORDER_STATUS") != null ? record.get("ORDER_STATUS").toString() : "UNKNOWN";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }

        System.out.println("\nClient ID: " + (!allOrdersData.isEmpty() && allOrdersData.get(0).get("CL_ID") != null ?
                          allOrdersData.get(0).get("CL_ID").toString() : "N/A"));
        System.out.println("Total Orders: " + allOrdersData.size());
        System.out.println("\nBy Order Side:");
        System.out.println("  BUY Orders: " + buyOrders);
        System.out.println("  SELL Orders: " + sellOrders);
        if (otherOrders > 0) {
            System.out.println("  Other Orders: " + otherOrders);
        }

        System.out.println("\nBy Order Status:");
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    // Getters for main SEC_ORDERS columns
    public String getOrderId() { return orderId; }
    public String getClientId() { return clientId; }
    public String getOrderStatus() { return orderStatus; }
    public String getCompanyCode() { return companyCode; }
    public String getVolumeOfShares() { return volumeOfShares; }
    public String getActualPrice() { return actualPrice; }
    public String getOrderType() { return orderType; }
    public String getOrderDate() { return orderDate; }
    public String getTimeStamp() { return timeStamp; }
    public String getCreatedBy() { return createdBy; }
    public String getUserId() { return userId; }
    public String getExecutedShares() { return executedShares; }
    public String getRemainingShares() { return remainingShares; }
    public String getOrderValue() { return orderValue; }
    public String getSlNo() { return slNo; }
    public String getDealingAcc() { return dealingAcc; }
    public String getNinNumber() { return ninNumber; }
    public String getOrderSource() { return orderSource; }
    public String getFixStatus() { return fixStatus; }
    public String getOrderTime() { return orderTime; }
    public String getEntryDate() { return entryDate; }
    public String getMktOrderNo() { return mktOrderNo; }
    public String getOrderIdMitch() { return orderIdMitch; }
    public String getFixOrderType() { return fixOrderType; }
    public String getTradingFees() { return tradingFees; }

}
