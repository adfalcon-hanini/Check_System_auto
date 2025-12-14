package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetDeptData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetDeptData - Testing 5 fetch methods
 */
public class GetDeptDataTest {

    private static final Logger logger = Logger.getLogger(GetDeptDataTest.class);
    private OracleDBConnection dbConnection;
    private GetDeptData deptData;

    private static final String TARGET_CLIENT_ID = "12240";
    private static final String TARGET_DATE = "01-DEC-2025";
    private static final String START_DATE = "01-Nov-2025";
    private static final String END_DATE = "20-DEC-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for dept data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            deptData = new GetDeptData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchFundSummaryByClientIdAndDate method")
    public void testFetchFundSummaryByClientIdAndDate() {
        logger.info("=== Test 1: fetchFundSummaryByClientIdAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Fund Daily Summary By Client ID and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Client ID: " + TARGET_CLIENT_ID);
        System.out.println("  - Summary Date: " + TARGET_DATE);

        boolean success = deptData.fetchFundSummaryByClientIdAndDate(TARGET_CLIENT_ID, TARGET_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Fund daily summary data found");
        } else {
            System.out.println("Status: No fund daily summary data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchFundSummaryByClientIdAndDate executed successfully");
    }

    @Test(priority = 2, description = "Test fetchFundSummaryByClientId method")
    public void testFetchFundSummaryByClientId() {
        logger.info("=== Test 2: fetchFundSummaryByClientId ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Fund Daily Summary By Client ID (All Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Client ID: " + TARGET_CLIENT_ID);

        GetDeptData allDatesData = new GetDeptData(dbConnection);
        boolean success = allDatesData.fetchFundSummaryByClientId(TARGET_CLIENT_ID);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Fund daily summary data found");
        } else {
            System.out.println("Status: No fund daily summary data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchFundSummaryByClientId executed successfully");
    }

    @Test(priority = 3, description = "Test fetchFundSummaryByClientIdAndDateRange method")
    public void testFetchFundSummaryByClientIdAndDateRange() {
        logger.info("=== Test 3: fetchFundSummaryByClientIdAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Fund Daily Summary By Client ID and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Client ID: " + TARGET_CLIENT_ID);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetDeptData rangeData = new GetDeptData(dbConnection);
        boolean success = rangeData.fetchFundSummaryByClientIdAndDateRange(TARGET_CLIENT_ID, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Fund daily summary data found");
        } else {
            System.out.println("Status: No fund daily summary data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchFundSummaryByClientIdAndDateRange executed successfully");
    }

    @Test(priority = 4, description = "Test fetchLatestFundSummaryByClientId method")
    public void testFetchLatestFundSummaryByClientId() {
        logger.info("=== Test 4: fetchLatestFundSummaryByClientId ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Latest Fund Daily Summary By Client ID");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Client ID: " + TARGET_CLIENT_ID);

        GetDeptData latestData = new GetDeptData(dbConnection);
        boolean success = latestData.fetchLatestFundSummaryByClientId(TARGET_CLIENT_ID);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Latest fund daily summary data found");
        } else {
            System.out.println("Status: No latest fund daily summary data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchLatestFundSummaryByClientId executed successfully");
    }

    @Test(priority = 5, description = "Test fetchFundSummaryByDate method")
    public void testFetchFundSummaryByDate() {
        logger.info("=== Test 5: fetchFundSummaryByDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Fund Daily Summary By Date (All Clients)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Summary Date: " + TARGET_DATE);

        GetDeptData dateData = new GetDeptData(dbConnection);
        boolean success = dateData.fetchFundSummaryByDate(TARGET_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Fund daily summary data found");
        } else {
            System.out.println("Status: No fund daily summary data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchFundSummaryByDate executed successfully");
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
