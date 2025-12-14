package com.example.tests.orders;

import com.example.enums.OrderType;
import com.example.utils.BaseOrderManager;
import com.example.utils.OracleDBConnection;
import com.example.utils.PlacedSellOrder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

/**
 * Test class for PlacedSellOrder utility
 * Tests sell order retrieval for NIN: 12240
 *
 * Refactored to extend BaseOrderTest following DRY principle
 */
@Epic("Order Management")
@Feature("Sell Orders")
public class PlacedSellOrderTest extends BaseOrderTest {

    @Override
    protected BaseOrderManager createOrderManager(OracleDBConnection connection) {
        return new PlacedSellOrder(connection);
    }

    @Override
    protected OrderType getOrderType() {
        return OrderType.SELL;
    }

    @Test(priority = 1, description = "Test getting sell orders for default NIN")
    @Story("Retrieve Sell Orders")
    @Description("Verify sell orders can be retrieved for the default NIN (12240)")
    public void testGetSellOrders() {
        testGetOrders("Get Sell Orders", orderManager.getDefaultNin());
        logSuccess("testGetSellOrders executed successfully");
    }

    /**
     * NOTE: Tests for direct database INSERT/UPDATE operations have been removed.
     * Use UI-based tests (OrdersPageTest with Selenium) for:
     * - Placing sell orders
     * - Cancelling sell orders
     *
     * This ensures proper business logic, validation, and authorization checks.
     */
}
