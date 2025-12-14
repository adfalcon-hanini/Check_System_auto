package com.example.utils;

import com.example.enums.OrderType;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for order management operations
 * Implements Template Method pattern to eliminate code duplication
 * between Buy and Sell order operations
 */
public abstract class BaseOrderManager {

    private static final Logger logger = Logger.getLogger(BaseOrderManager.class);
    protected final OracleDBConnection dbConnection;
    private final OrderType orderType;
    private final String defaultNin;

    private static final String ORDER_QUERY =
        "SELECT SL_NO, NIN_NUMBER, COMPANY_CODE, VOLUMN_OF_SHARES, ACTUAL_PRICE, ORDER_STATUS, ORDER_DATE " +
        "FROM SEC_ORDERS WHERE NIN_NUMBER = ? AND ORDER_TYPE = ? ORDER BY ORDER_DATE DESC";

    /**
     * Constructor
     * @param dbConnection Database connection instance
     * @param orderType Type of order (BUY/SELL)
     * @param defaultNin Default NIN for this order type
     */
    protected BaseOrderManager(OracleDBConnection dbConnection, OrderType orderType, String defaultNin) {
        this.dbConnection = dbConnection;
        this.orderType = orderType;
        this.defaultNin = defaultNin;
    }

    /**
     * Get orders for the default NIN
     * @return true if orders found, false otherwise
     */
    public boolean getOrders() {
        return getOrders(defaultNin);
    }

    /**
     * Get orders for a specific NIN
     * @param nin Client NIN
     * @return true if orders found, false otherwise
     */
    public boolean getOrders(String nin) {
        try {
            logger.info(String.format("Fetching %s orders for NIN: %s", orderType.getDisplayName(), nin));

            List<Map<String, Object>> results = executeOrderQuery(nin);

            if (results.isEmpty()) {
                logger.warn(String.format("No %s orders found for NIN: %s", orderType.getDisplayName(), nin));
                return false;
            }

            logOrderResults(results);
            return true;

        } catch (SQLException e) {
            logger.error(String.format("Error fetching %s orders: %s", orderType.getDisplayName(), e.getMessage()), e);
            return false;
        }
    }

    /**
     * Execute order query with parameters
     * @param nin Client NIN
     * @return List of order records
     * @throws SQLException if query execution fails
     */
    private List<Map<String, Object>> executeOrderQuery(String nin) throws SQLException {
        return dbConnection.executeQueryWithParams(ORDER_QUERY, nin, orderType.getCode());
    }

    /**
     * Log order results
     * @param results List of order records
     */
    private void logOrderResults(List<Map<String, Object>> results) {
        logger.info(String.format("%s orders fetched successfully. Found %d order(s)",
            orderType.getDisplayName(), results.size()));

        results.forEach(this::logOrderDetails);
    }

    /**
     * Log individual order details
     * @param row Order record
     */
    private void logOrderDetails(Map<String, Object> row) {
        logger.info(String.format("SL_NO: %s, Symbol: %s, Shares: %s, Price: %s, Status: %s, Date: %s",
            row.get("SL_NO"),
            row.get("COMPANY_CODE"),
            row.get("VOLUMN_OF_SHARES"),
            row.get("ACTUAL_PRICE"),
            row.get("ORDER_STATUS"),
            row.get("ORDER_DATE")));
    }

    /**
     * Get all orders with details
     * @param nin Client NIN
     * @return List of orders, empty list if none found
     */
    public List<Map<String, Object>> getOrderDetails(String nin) {
        try {
            return executeOrderQuery(nin);
        } catch (SQLException e) {
            logger.error(String.format("Error fetching %s order details: %s", orderType.getDisplayName(), e.getMessage()), e);
            return Collections.emptyList();
        }
    }

    /**
     * Get order count for a NIN
     * @param nin Client NIN
     * @return Number of orders
     */
    public int getOrderCount(String nin) {
        return getOrderDetails(nin).size();
    }

    // Getters
    public String getDefaultNin() {
        return defaultNin;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
