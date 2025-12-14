package com.example.utils;

import com.example.enums.OrderType;

/**
 * Utility class for managing SELL orders
 * Uses NIN: 12240
 *
 * Refactored to extend BaseOrderManager following DRY principle
 */
public class PlacedSellOrder extends BaseOrderManager {

    private static final String DEFAULT_SELL_NIN = "12240";

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public PlacedSellOrder(OracleDBConnection dbConnection) {
        super(dbConnection, OrderType.SELL, DEFAULT_SELL_NIN);
    }

    /**
     * Get sell orders for the default NIN
     * @return true if orders found, false otherwise
     */
    public boolean getSellOrders() {
        return getOrders();
    }

    /**
     * Get sell orders for a specific NIN
     * @param nin Client NIN
     * @return true if orders found, false otherwise
     */
    public boolean getSellOrders(String nin) {
        return getOrders(nin);
    }

    /**
     * NOTE: Direct order placement via database INSERT has been removed.
     * Use UI-based tests (Selenium) to place orders through the application interface.
     * This ensures proper business logic, validation, and authorization checks.
     */
}
