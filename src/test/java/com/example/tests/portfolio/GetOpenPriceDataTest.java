package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetOpenPriceData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetOpenPriceData - Testing 6 fetch methods
 */
public class GetOpenPriceDataTest {

    private static final Logger logger = Logger.getLogger(GetOpenPriceDataTest.class);
    private OracleDBConnection dbConnection;
    private GetOpenPriceData openPriceData;

    private static final String TARGET_COMPANY = "BRES";
    private static final String TARGET_DATE = "10-DEC-2025";
    private static final String START_DATE = "01-Nov-2025";
    private static final String END_DATE = "10-DEC-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for open price data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            openPriceData = new GetOpenPriceData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchOpenPriceByCompanyAndDate method")
    public void testFetchOpenPriceByCompanyAndDate() {
        logger.info("=== Test 1: fetchOpenPriceByCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch Open Price By Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Trade Date (before): " + TARGET_DATE);

        boolean success = openPriceData.fetchOpenPriceByCompanyAndDate(TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Open Price Data for Company: " + TARGET_COMPANY + " before Date: " + TARGET_DATE);
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch open price data for company: " + TARGET_COMPANY + " before date: " + TARGET_DATE);
        logger.info("✓ fetchOpenPriceByCompanyAndDate executed successfully");
    }

    @Test(priority = 2, description = "Test fetchOpenPriceByCompanyCode method")
    public void testFetchOpenPriceByCompanyCode() {
        logger.info("=== Test 2: fetchOpenPriceByCompanyCode ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Open Price By Company Code");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetOpenPriceData companyData = new GetOpenPriceData(dbConnection);
        boolean success = companyData.fetchOpenPriceByCompanyCode(TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Open Price Data for Company: " + TARGET_COMPANY + " (Showing first 20)");
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch open price data for company: " + TARGET_COMPANY);
        logger.info("✓ fetchOpenPriceByCompanyCode executed successfully");
    }

    @Test(priority = 3, description = "Test fetchOpenPriceBeforeDate method")
    public void testFetchOpenPriceBeforeDate() {
        logger.info("=== Test 3: fetchOpenPriceBeforeDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Open Price Before Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Trade Date (before): " + TARGET_DATE);

        GetOpenPriceData dateData = new GetOpenPriceData(dbConnection);
        boolean success = dateData.fetchOpenPriceBeforeDate(TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Open Price Data before Date: " + TARGET_DATE + " (Showing first 20)");
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch open price data before date: " + TARGET_DATE);
        logger.info("✓ fetchOpenPriceBeforeDate executed successfully");
    }

    @Test(priority = 4, description = "Test fetchAllOpenPrice method")
    public void testFetchAllOpenPrice() {
        logger.info("=== Test 4: fetchAllOpenPrice ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch All Open Price Data");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - None (fetching all records)");

        GetOpenPriceData allData = new GetOpenPriceData(dbConnection);
        boolean success = allData.fetchAllOpenPrice();

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("All Open Price Data (Showing first 20)");
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch all open price data");
        logger.info("✓ fetchAllOpenPrice executed successfully");
    }

    @Test(priority = 5, description = "Test fetchOpenPriceByCompanyAndDateRange method")
    public void testFetchOpenPriceByCompanyAndDateRange() {
        logger.info("=== Test 5: fetchOpenPriceByCompanyAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Fetch Open Price By Company and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetOpenPriceData rangeData = new GetOpenPriceData(dbConnection);
        boolean success = rangeData.fetchOpenPriceByCompanyAndDateRange(TARGET_COMPANY, START_DATE, END_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND TRADE_DATE >= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, START_DATE, END_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Open Price Data for Company: " + TARGET_COMPANY + " between " + START_DATE + " and " + END_DATE);
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch open price data for company: " + TARGET_COMPANY + " in date range");
        logger.info("✓ fetchOpenPriceByCompanyAndDateRange executed successfully");
    }

    @Test(priority = 6, description = "Test fetchLatestOpenPriceByCompanyBeforeDate method")
    public void testFetchLatestOpenPriceByCompanyBeforeDate() {
        logger.info("=== Test 6: fetchLatestOpenPriceByCompanyBeforeDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Fetch Latest Open Price By Company Before Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Trade Date (before): " + TARGET_DATE);

        GetOpenPriceData latestData = new GetOpenPriceData(dbConnection);
        boolean success = latestData.fetchLatestOpenPriceByCompanyBeforeDate(TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                              "AND TRADE_DATE = (SELECT MAX(TRADE_DATE) FROM MDF_TIME_SERIES " +
                              "                   WHERE COMPANY_CODE = ? AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY'))";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, TARGET_DATE, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Latest Open Price Data for Company: " + TARGET_COMPANY + " before Date: " + TARGET_DATE);
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-25s | %-20s%n",
                            row.get("COMPANY_CODE"),
                            row.get("TRADE_DATE"),
                            row.get("CLOSE"));
                    }

                    System.out.println("=".repeat(100));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch latest open price data for company: " + TARGET_COMPANY + " before date: " + TARGET_DATE);
        logger.info("✓ fetchLatestOpenPriceByCompanyBeforeDate executed successfully");
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
