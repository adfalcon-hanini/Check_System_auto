package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetTotalRegistryTransferDepositData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetTotalRegistryTransferDepositData - Testing 7 fetch methods
 */
public class GetTotalRegistryTransferDepositDataTest {

    private static final Logger logger = Logger.getLogger(GetTotalRegistryTransferDepositDataTest.class);
    private OracleDBConnection dbConnection;
    private GetTotalRegistryTransferDepositData stockTransferData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_SYMBOL = "QNBK";
    private static final String TARGET_STATUS = "Success";
    private static final String START_DATE = "01-Jan-2025";
    private static final String END_DATE = "25-Nov-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for stock transfer deposit data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            stockTransferData = new GetTotalRegistryTransferDepositData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchStockTransferByNinSymbolStatusAndDateRange method")
    public void testFetchStockTransferByNinSymbolStatusAndDateRange() {
        logger.info("=== Test 1: fetchStockTransferByNinSymbolStatusAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Stock Transfer By NIN, Symbol, Status and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);
        System.out.println("  - Status: " + TARGET_STATUS);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Volume: > 0");

        boolean success = stockTransferData.fetchStockTransferByNinSymbolStatusAndDateRange(TARGET_NIN, TARGET_SYMBOL, TARGET_STATUS, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByNinSymbolStatusAndDateRange executed successfully");
    }

    @Test(priority = 2, description = "Test fetchStockTransferByNinAndSymbol method")
    public void testFetchStockTransferByNinAndSymbol() {
        logger.info("=== Test 2: fetchStockTransferByNinAndSymbol ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Stock Transfer By NIN and Symbol (All Statuses and Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData allData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = allData.fetchStockTransferByNinAndSymbol(TARGET_NIN, TARGET_SYMBOL);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByNinAndSymbol executed successfully");
    }

    @Test(priority = 3, description = "Test fetchSuccessfulTransfersByNinSymbolAndDateRange method")
    public void testFetchSuccessfulTransfersByNinSymbolAndDateRange() {
        logger.info("=== Test 3: fetchSuccessfulTransfersByNinSymbolAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Successful Stock Transfers By NIN, Symbol and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData successData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = successData.fetchSuccessfulTransfersByNinSymbolAndDateRange(TARGET_NIN, TARGET_SYMBOL, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Successful stock transfers found");
        } else {
            System.out.println("Status: No successful stock transfers found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchSuccessfulTransfersByNinSymbolAndDateRange executed successfully");
    }

    @Test(priority = 4, description = "Test fetchStockTransferByNin method")
    public void testFetchStockTransferByNin() {
        logger.info("=== Test 4: fetchStockTransferByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Stock Transfer By NIN (All Symbols, Statuses, and Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData ninData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = ninData.fetchStockTransferByNin(TARGET_NIN);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByNin executed successfully");
    }

    @Test(priority = 5, description = "Test fetchStockTransferByStatus method")
    public void testFetchStockTransferByStatus() {
        logger.info("=== Test 5: fetchStockTransferByStatus ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Stock Transfer By Status (All NIPs and Symbols)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Status: " + TARGET_STATUS);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData statusData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = statusData.fetchStockTransferByStatus(TARGET_STATUS);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByStatus executed successfully");
    }

    @Test(priority = 6, description = "Test fetchStockTransferByNinAndStatus method")
    public void testFetchStockTransferByNinAndStatus() {
        logger.info("=== Test 6: fetchStockTransferByNinAndStatus ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Get Stock Transfer By NIN and Status");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Status: " + TARGET_STATUS);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData ninStatusData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = ninStatusData.fetchStockTransferByNinAndStatus(TARGET_NIN, TARGET_STATUS);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByNinAndStatus executed successfully");
    }

    @Test(priority = 7, description = "Test fetchStockTransferByNinAndDateRange method")
    public void testFetchStockTransferByNinAndDateRange() {
        logger.info("=== Test 7: fetchStockTransferByNinAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 7: Get Stock Transfer By NIN and Date Range (All Symbols and Statuses)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Volume: > 0");

        GetTotalRegistryTransferDepositData dateData = new GetTotalRegistryTransferDepositData(dbConnection);
        boolean success = dateData.fetchStockTransferByNinAndDateRange(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Stock transfer data found");
        } else {
            System.out.println("Status: No stock transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchStockTransferByNinAndDateRange executed successfully");
    }

    @AfterClass(alwaysRun = true)
    public void teardownDatabase() {
        try {
            if (dbConnection != null) {
                logger.info("Closing database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection: " + e.getMessage(), e);
        }
    }
}
