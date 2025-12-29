package com.example.tests.portfolio;

import com.example.screensData.clients.GetCashData;
import com.example.screensData.mcalc.GetCashTodayData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetCashData - Testing 5 fetch methods
 * Also includes GetCashTodayData test - Testing 1 fetch method
 */
public class GetCashDataTest {

    private static final Logger logger = Logger.getLogger(GetCashDataTest.class);
    private OracleDBConnection dbConnection;
    private GetCashData cashData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_DATE = "21-DEC-2025";
    private static final String START_DATE = "01-DEC-2025";
    private static final String END_DATE = "21-DEC-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for cash data tests");

            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            cashData = new GetCashData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchClientBalanceByNinAndDate method")
    public void testFetchClientBalanceByNinAndDate() {
        logger.info("=== Test 1: fetchClientBalanceByNinAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Client Daily Balance By NIN and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Balance Date: " + TARGET_DATE);

        boolean success = cashData.fetchClientBalanceByNinAndDate(TARGET_NIN, TARGET_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Client daily balance data found");
        } else {
            System.out.println("Status: No client daily balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchClientBalanceByNinAndDate executed successfully");
    }

    @Test(priority = 2, description = "Test fetchClientBalanceByNin method")
    public void testFetchClientBalanceByNin() {
        logger.info("=== Test 2: fetchClientBalanceByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Client Daily Balance By NIN (All Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        GetCashData allDatesData = new GetCashData(dbConnection);
        boolean success = allDatesData.fetchClientBalanceByNin(TARGET_NIN);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Client daily balance data found");
        } else {
            System.out.println("Status: No client daily balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchClientBalanceByNin executed successfully");
    }

    @Test(priority = 3, description = "Test fetchClientBalanceByNinAndDateRange method")
    public void testFetchClientBalanceByNinAndDateRange() {
        logger.info("=== Test 3: fetchClientBalanceByNinAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Client Daily Balance By NIN and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetCashData rangeData = new GetCashData(dbConnection);
        boolean success = rangeData.fetchClientBalanceByNinAndDateRange(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Client daily balance data found");
        } else {
            System.out.println("Status: No client daily balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchClientBalanceByNinAndDateRange executed successfully");
    }

    @Test(priority = 4, description = "Test fetchLatestClientBalanceByNin method")
    public void testFetchLatestClientBalanceByNin() {
        logger.info("=== Test 4: fetchLatestClientBalanceByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Latest Client Daily Balance By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        GetCashData latestData = new GetCashData(dbConnection);
        boolean success = latestData.fetchLatestClientBalanceByNin(TARGET_NIN);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Latest client daily balance data found");
        } else {
            System.out.println("Status: No latest client daily balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchLatestClientBalanceByNin executed successfully");
    }

    @Test(priority = 5, description = "Test fetchClientBalanceByDate method")
    public void testFetchClientBalanceByDate() {
        logger.info("=== Test 5: fetchClientBalanceByDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Client Daily Balance By Date (All Clients)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Balance Date: " + TARGET_DATE);

        GetCashData dateData = new GetCashData(dbConnection);
        boolean success = dateData.fetchClientBalanceByDate(TARGET_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Client daily balance data found");
        } else {
            System.out.println("Status: No client daily balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchClientBalanceByDate executed successfully");
    }

    @Test(priority = 6, description = "Test fetchClientBalancesByNin from GetCashTodayData")
    public void testFetchCashTodayData() {
        logger.info("=== Test 6: GetCashTodayData - fetchClientBalancesByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Get Client Current Balance (Cash Today)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        GetCashTodayData cashTodayData = new GetCashTodayData(dbConnection);
        boolean success = cashTodayData.fetchClientBalancesByNin(TARGET_NIN);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Current balance data found");

            // Print data as table
            try {
                String query = "SELECT NIN, CUR_BAL FROM Sec_Clients_Balances WHERE nin = ?";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("CLIENT CURRENT BALANCE DATA");
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s %-25s%n", "NIN", "CURRENT BALANCE");
                    System.out.println("-".repeat(100));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s %-25s%n",
                            row.get("NIN"),
                            row.get("CUR_BAL"));
                    }

                    System.out.println("=".repeat(100));
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No current balance data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ fetchClientBalancesByNin from GetCashTodayData executed successfully");
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
