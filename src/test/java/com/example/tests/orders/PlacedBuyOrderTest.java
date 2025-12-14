package com.example.tests.orders;

import com.example.enums.OrderType;
import com.example.utils.BaseOrderManager;
import com.example.utils.OracleDBConnection;
import com.example.utils.PlacedBuyOrder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

/**
 * Test class for PlacedBuyOrder utility
 * Tests buy order retrieval for NIN: 12240
 *
 * Refactored to extend BaseOrderTest following DRY principle
 */
@Epic("Order Management")
@Feature("Buy Orders")
public class PlacedBuyOrderTest extends BaseOrderTest {

    @Override
    protected BaseOrderManager createOrderManager(OracleDBConnection connection) {
        return new PlacedBuyOrder(connection);
    }

    @Override
    protected OrderType getOrderType() {
        return OrderType.BUY;
    }

    @Test(priority = 1, description = "Test getting buy orders for default NIN")
    @Story("Retrieve Buy Orders")
    @Description("Verify buy orders can be retrieved for the default NIN (12240)")
    public void testGetBuyOrders() {
        testGetOrders("Get Buy Orders", orderManager.getDefaultNin());
        logSuccess("testGetBuyOrders executed successfully");
    }

    /**
     * NOTE: Tests for direct database INSERT/UPDATE operations have been removed.
     * Use UI-based tests (OrdersPageTest with Selenium) for:
     * - Placing buy orders
     * - Cancelling buy orders
     *
     * This ensures proper business logic, validation, and authorization checks.
     */
}
