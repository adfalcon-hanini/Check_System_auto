package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetTotalRegistryTransferWithdrawData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetTotalRegistryTransferWithdrawData - Testing 7 fetch methods
 */
public class GetTotalRegistryTransferWithdrawDataTest {

    private static final Logger logger = Logger.getLogger(GetTotalRegistryTransferWithdrawDataTest.class);
    private OracleDBConnection dbConnection;
    private GetTotalRegistryTransferWithdrawData registryTransferData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_SYMBOL = "QNBK";
    private static final String TARGET_STATUS = "Success";
    private static final String START_DATE = "01-Jan-2025";
    private static final String END_DATE = "25-Nov-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for registry transfer withdraw data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            registryTransferData = new GetTotalRegistryTransferWithdrawData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchRegistryTransferByNinSymbolStatusAndDateRange method")
    public void testFetchRegistryTransferByNinSymbolStatusAndDateRange() {
        logger.info("=== Test 1: fetchRegistryTransferByNinSymbolStatusAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Registry Transfer By NIN, Symbol, Status and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);
        System.out.println("  - Status: " + TARGET_STATUS);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        boolean success = registryTransferData.fetchRegistryTransferByNinSymbolStatusAndDateRange(TARGET_NIN, TARGET_SYMBOL, TARGET_STATUS, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByNinSymbolStatusAndDateRange executed successfully");
    }

    @Test(priority = 2, description = "Test fetchRegistryTransferByNinAndSymbol method")
    public void testFetchRegistryTransferByNinAndSymbol() {
        logger.info("=== Test 2: fetchRegistryTransferByNinAndSymbol ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Registry Transfer By NIN and Symbol (All Statuses and Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);

        GetTotalRegistryTransferWithdrawData allData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = allData.fetchRegistryTransferByNinAndSymbol(TARGET_NIN, TARGET_SYMBOL);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByNinAndSymbol executed successfully");
    }

    @Test(priority = 3, description = "Test fetchSuccessfulTransfersByNinSymbolAndDateRange method")
    public void testFetchSuccessfulTransfersByNinSymbolAndDateRange() {
        logger.info("=== Test 3: fetchSuccessfulTransfersByNinSymbolAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Successful Registry Transfers By NIN, Symbol and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Symbol: " + TARGET_SYMBOL);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalRegistryTransferWithdrawData successData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = successData.fetchSuccessfulTransfersByNinSymbolAndDateRange(TARGET_NIN, TARGET_SYMBOL, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Successful registry transfers found");
        } else {
            System.out.println("Status: No successful registry transfers found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchSuccessfulTransfersByNinSymbolAndDateRange executed successfully");
    }

    @Test(priority = 4, description = "Test fetchRegistryTransferByNin method")
    public void testFetchRegistryTransferByNin() {
        logger.info("=== Test 4: fetchRegistryTransferByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Registry Transfer By NIN (All Symbols, Statuses, and Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        GetTotalRegistryTransferWithdrawData ninData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = ninData.fetchRegistryTransferByNin(TARGET_NIN);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByNin executed successfully");
    }

    @Test(priority = 5, description = "Test fetchRegistryTransferByStatus method")
    public void testFetchRegistryTransferByStatus() {
        logger.info("=== Test 5: fetchRegistryTransferByStatus ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Registry Transfer By Status (All NIPs and Symbols)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Status: " + TARGET_STATUS);

        GetTotalRegistryTransferWithdrawData statusData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = statusData.fetchRegistryTransferByStatus(TARGET_STATUS);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByStatus executed successfully");
    }

    @Test(priority = 6, description = "Test fetchRegistryTransferByNinAndStatus method")
    public void testFetchRegistryTransferByNinAndStatus() {
        logger.info("=== Test 6: fetchRegistryTransferByNinAndStatus ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Get Registry Transfer By NIN and Status");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Status: " + TARGET_STATUS);

        GetTotalRegistryTransferWithdrawData ninStatusData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = ninStatusData.fetchRegistryTransferByNinAndStatus(TARGET_NIN, TARGET_STATUS);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByNinAndStatus executed successfully");
    }

    @Test(priority = 7, description = "Test fetchRegistryTransferByNinAndDateRange method")
    public void testFetchRegistryTransferByNinAndDateRange() {
        logger.info("=== Test 7: fetchRegistryTransferByNinAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 7: Get Registry Transfer By NIN and Date Range (All Symbols and Statuses)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalRegistryTransferWithdrawData dateData = new GetTotalRegistryTransferWithdrawData(dbConnection);
        boolean success = dateData.fetchRegistryTransferByNinAndDateRange(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Registry transfer data found");
        } else {
            System.out.println("Status: No registry transfer data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchRegistryTransferByNinAndDateRange executed successfully");
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
