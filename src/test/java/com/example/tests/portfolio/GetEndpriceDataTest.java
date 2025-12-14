package com.example.tests.portfolio;

import com.example.screensData.portfolio.GetEndpriceData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetEndpriceData - Testing 6 fetch methods
 */
public class GetEndpriceDataTest {

    private static final Logger logger = Logger.getLogger(GetEndpriceDataTest.class);
    private OracleDBConnection dbConnection;
    private GetEndpriceData endpriceData;

    private static final String TARGET_DATE = "20-Nov-2025";
    private static final String TARGET_COMPANY = "BRES";
    private static final String EXACT_DATE = "20-DEC-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for end price data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            endpriceData = new GetEndpriceData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchAllEndprice method")
    public void testFetchAllEndprice() {
        logger.info("=== Test 1: fetchAllEndprice ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch All End Price Data");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - None (fetching all records)");

        boolean success = endpriceData.fetchAllEndprice();

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("End Price Data (All Records - Showing first 20)");
                System.out.println("=".repeat(100));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-20s%n",
                        "COMPANY_CODE", "TRADE_DATE", "CLOSE");
                    System.out.println("-".repeat(100));

                    // Print rows (limit to first 20 for readability)
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

        Assert.assertTrue(success, "Should fetch all end price data");
        logger.info("✓ fetchAllEndprice executed successfully");
    }

    @Test(priority = 2, description = "Test fetchEndpriceByDate method")
    public void testFetchEndpriceByDate() {
        logger.info("=== Test 2: fetchEndpriceByDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch End Price By Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Trade Date: " + TARGET_DATE);

        GetEndpriceData dateData = new GetEndpriceData(dbConnection);
        boolean success = dateData.fetchEndpriceByDate(TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE TRADE_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("End Price Data up to Date: " + TARGET_DATE + " (Showing first 20)");
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

        Assert.assertTrue(success, "Should fetch end price data up to date: " + TARGET_DATE);
        logger.info("✓ fetchEndpriceByDate executed successfully");
    }

    @Test(priority = 3, description = "Test fetchEndpriceByCompanyCode method")
    public void testFetchEndpriceByCompanyCode() {
        logger.info("=== Test 3: fetchEndpriceByCompanyCode ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch End Price By Company Code");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetEndpriceData companyData = new GetEndpriceData(dbConnection);
        boolean success = companyData.fetchEndpriceByCompanyCode(TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("End Price Data for Company: " + TARGET_COMPANY);
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

        Assert.assertTrue(success, "Should fetch end price data for company: " + TARGET_COMPANY);
        logger.info("✓ fetchEndpriceByCompanyCode executed successfully");
    }

    @Test(priority = 4, description = "Test fetchEndpriceByCompanyAndDate method")
    public void testFetchEndpriceByCompanyAndDate() {
        logger.info("=== Test 4: fetchEndpriceByCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch End Price By Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Trade Date: " + TARGET_DATE);

        GetEndpriceData companyDateData = new GetEndpriceData(dbConnection);
        boolean success = companyDateData.fetchEndpriceByCompanyAndDate(TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND TRADE_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY TRADE_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("End Price Data for Company: " + TARGET_COMPANY + " up to Date: " + TARGET_DATE);
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

        Assert.assertTrue(success, "Should fetch end price data for company: " + TARGET_COMPANY + " up to date: " + TARGET_DATE);
        logger.info("✓ fetchEndpriceByCompanyAndDate executed successfully");
    }

    @Test(priority = 5, description = "Test fetchEndpriceByExactDate method")
    public void testFetchEndpriceByExactDate() {
        logger.info("=== Test 5: fetchEndpriceByExactDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Fetch End Price By Exact Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Exact Trade Date: " + EXACT_DATE);

        GetEndpriceData exactDateData = new GetEndpriceData(dbConnection);
        boolean success = exactDateData.fetchEndpriceByExactDate(EXACT_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY COMPANY_CODE";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, EXACT_DATE);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("End Price Data for Exact Date: " + EXACT_DATE);
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
            logger.info("✓ fetchEndpriceByExactDate executed successfully");
        } else {
            logger.warn("⚠ No data found for exact date: " + EXACT_DATE + " (this may be expected if date has no records)");
        }

        System.out.println("========================================\n");
    }

    @Test(priority = 6, description = "Test fetchLatestEndpriceByCompanyCode method")
    public void testFetchLatestEndpriceByCompanyCode() {
        logger.info("=== Test 6: fetchLatestEndpriceByCompanyCode ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Fetch Latest End Price By Company Code");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetEndpriceData latestData = new GetEndpriceData(dbConnection);
        boolean success = latestData.fetchLatestEndpriceByCompanyCode(TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND TRADE_DATE = (SELECT MAX(TRADE_DATE) FROM MDF_TIME_SERIES WHERE COMPANY_CODE = ?)";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(100));
                System.out.println("Latest End Price Data for Company: " + TARGET_COMPANY);
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

        Assert.assertTrue(success, "Should fetch latest end price data for company: " + TARGET_COMPANY);
        logger.info("✓ fetchLatestEndpriceByCompanyCode executed successfully");
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
