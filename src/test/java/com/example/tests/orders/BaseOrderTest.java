package com.example.tests.orders;

import com.example.enums.OrderType;
import com.example.utils.BaseOrderManager;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base test class for order testing
 * Implements Template Method pattern to eliminate test code duplication
 * Provides common setup, teardown, and assertion methods
 */
public abstract class BaseOrderTest {

    protected static final Logger logger = Logger.getLogger(BaseOrderTest.class);
    protected OracleDBConnection dbConnection;
    protected BaseOrderManager orderManager;

    // Database configuration constants
    private static final String DB_URL = "DB01M:1523/GRPUAT";
    private static final String DB_USER = "sec1";
    private static final String DB_PASSWORD = "sec12345";

    // Display formatting constants
    private static final String SEPARATOR = "=".repeat(100);
    private static final String SECTION_SEPARATOR = "=".repeat(40);

    /**
     * Template method to be implemented by subclasses
     * @return Configured order manager instance
     */
    protected abstract BaseOrderManager createOrderManager(OracleDBConnection connection);

    /**
     * Get order type for logging purposes
     * @return Order type
     */
    protected abstract OrderType getOrderType();

    @BeforeClass
    public void setupDatabase() {
        try {
            logInfo(String.format("Setting up database connection for %s order tests", getOrderType().getDisplayName()));

            dbConnection = new OracleDBConnection(DB_URL, DB_USER, DB_PASSWORD);
            dbConnection.connect();

            orderManager = createOrderManager(dbConnection);

            logSuccess("Database connection established successfully");
            logInfo("Default NIN: " + orderManager.getDefaultNin());
        } catch (Exception e) {
            logError("Failed to establish database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Step("Get {orderType} orders for NIN: {nin}")
    protected void testGetOrders(String testName, String nin) {
        logTestHeader(testName, nin);

        boolean success = orderManager.getOrders(nin);

        logTestResult(success);

        // Test passes if method executes successfully (no exceptions)
        // The actual presence of orders is data-dependent and not a test failure
        if (!success) {
            logInfo("No orders found - this may be expected based on database state");
        }
    }

    /**
     * Assert that orders exist for a specific NIN
     * Use this when you know orders should exist
     * @param nin Client NIN
     */
    protected void assertOrdersExist(String nin) {
        boolean success = orderManager.getOrders(nin);
        Assert.assertTrue(success,
            String.format("Expected to find %s orders for NIN: %s", getOrderType().getDisplayName(), nin));
    }

    /**
     * Get order count and verify it matches expected value
     * @param nin Client NIN
     * @param expectedCount Expected number of orders
     */
    protected void assertOrderCount(String nin, int expectedCount) {
        int actualCount = orderManager.getOrderCount(nin);
        Assert.assertEquals(actualCount, expectedCount,
            String.format("Order count mismatch for NIN: %s", nin));
    }

    // Logging helper methods
    protected void logTestHeader(String testName, String nin) {
        logger.info(String.format("=== %s ===", testName));
        System.out.println("\n" + SECTION_SEPARATOR);
        System.out.println(testName);
        System.out.println(SECTION_SEPARATOR);
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + nin);
        System.out.println("  - Order Type: " + getOrderType().getDisplayName());
    }

    protected void logTestResult(boolean success) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("RESULT");
        System.out.println(SEPARATOR);

        if (success) {
            System.out.println(String.format("Status: SUCCESS - %s orders found", getOrderType().getDisplayName()));
        } else {
            System.out.println(String.format("Status: No %s orders found", getOrderType().getDisplayName()));
        }

        System.out.println(SEPARATOR);
        System.out.println(SECTION_SEPARATOR + "\n");
    }

    protected void logInfo(String message) {
        logger.info(message);
    }

    protected void logSuccess(String message) {
        logger.info("✓ " + message);
    }

    protected void logError(String message, Exception e) {
        logger.error(message + ": " + e.getMessage(), e);
    }

    @AfterClass(alwaysRun = true)
    public void teardownDatabase() {
        try {
            if (dbConnection != null) {
                logInfo("Closing database connection");
                dbConnection.closeConnection();
                logSuccess("Database connection closed successfully");
            }
        } catch (Exception e) {
            logError("Error closing database connection", e);
        }
    }
}
