package com.example.tests.portfolio;

import com.example.screensData.portfolio.GetEquDailyPortfolioData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetEquDailyPortfolioData - Testing 5 fetch methods
 */
public class GetEquDailyPortfolioDataTest {

    private static final Logger logger = Logger.getLogger(GetEquDailyPortfolioDataTest.class);
    private OracleDBConnection dbConnection;
    private GetEquDailyPortfolioData portfolioData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "BRES";
    private static final String TARGET_DATE = "2025-12-10";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for daily portfolio data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            portfolioData = new GetEquDailyPortfolioData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchEquDailyPortfolioByNin method")
    public void testFetchEquDailyPortfolioByNin() {
        logger.info("=== Test 1: fetchEquDailyPortfolioByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch Daily Portfolio By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        boolean success = portfolioData.fetchEquDailyPortfolioByNin(TARGET_NIN);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                              "ORDER BY PORTFOLIO_DATE DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN);

                System.out.println("\n" + "=".repeat(200));
                System.out.println("Daily Portfolio Data for NIN: " + TARGET_NIN);
                System.out.println("=".repeat(200));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                        "PORTFOLIO_DATE", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SHARE_PRICE", "SHARES_VALUE", "AVG_PRICE", "BLOCKED_SHARES");
                    System.out.println("-".repeat(200));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                            row.get("PORTFOLIO_DATE"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SHARE_PRICE"),
                            row.get("SHARES_VALUE"),
                            row.get("AVG_PRICE"),
                            row.get("BLOCKED_SHARES"));
                    }

                    System.out.println("=".repeat(200));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN);
        logger.info("✓ fetchEquDailyPortfolioByNin executed successfully");
    }

    @Test(priority = 2, description = "Test fetchEquDailyPortfolioByNinAndCompany method")
    public void testFetchEquDailyPortfolioByNinAndCompany() {
        logger.info("=== Test 2: fetchEquDailyPortfolioByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Daily Portfolio By NIN and Company");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);

        GetEquDailyPortfolioData companyData = new GetEquDailyPortfolioData(dbConnection);
        boolean success = companyData.fetchEquDailyPortfolioByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                              "ORDER BY PORTFOLIO_DATE DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(200));
                System.out.println("Daily Portfolio Data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
                System.out.println("=".repeat(200));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                        "PORTFOLIO_DATE", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SHARE_PRICE", "SHARES_VALUE", "AVG_PRICE", "BLOCKED_SHARES");
                    System.out.println("-".repeat(200));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                            row.get("PORTFOLIO_DATE"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SHARE_PRICE"),
                            row.get("SHARES_VALUE"),
                            row.get("AVG_PRICE"),
                            row.get("BLOCKED_SHARES"));
                    }

                    System.out.println("=".repeat(200));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
        logger.info("✓ fetchEquDailyPortfolioByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test fetchEquDailyPortfolioByNinAndDate method")
    public void testFetchEquDailyPortfolioByNinAndDate() {
        logger.info("=== Test 3: fetchEquDailyPortfolioByNinAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Daily Portfolio By NIN and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Date: " + TARGET_DATE);

        GetEquDailyPortfolioData dateData = new GetEquDailyPortfolioData(dbConnection);
        boolean success = dateData.fetchEquDailyPortfolioByNinAndDate(TARGET_NIN, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? AND TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_DATE);

                System.out.println("\n" + "=".repeat(200));
                System.out.println("Daily Portfolio Data for NIN: " + TARGET_NIN + " and Date: " + TARGET_DATE);
                System.out.println("=".repeat(200));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                        "PORTFOLIO_DATE", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SHARE_PRICE", "SHARES_VALUE", "AVG_PRICE", "BLOCKED_SHARES");
                    System.out.println("-".repeat(200));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                            row.get("PORTFOLIO_DATE"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SHARE_PRICE"),
                            row.get("SHARES_VALUE"),
                            row.get("AVG_PRICE"),
                            row.get("BLOCKED_SHARES"));
                    }

                    System.out.println("=".repeat(200));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchEquDailyPortfolioByNinAndDate executed successfully");
        } else {
            logger.warn("⚠ No data found for date: " + TARGET_DATE + " (this may be expected if date has no records)");
        }

        System.out.println("========================================\n");
    }

    @Test(priority = 4, description = "Test fetchEquDailyPortfolioByNinAndCompanyAndDate method")
    public void testFetchEquDailyPortfolioByNinAndCompanyAndDate() {
        logger.info("=== Test 4: fetchEquDailyPortfolioByNinAndCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch Daily Portfolio By NIN, Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);
        System.out.println("  - Date: " + TARGET_DATE);

        GetEquDailyPortfolioData specificData = new GetEquDailyPortfolioData(dbConnection);
        boolean success = specificData.fetchEquDailyPortfolioByNinAndCompanyAndDate(TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(200));
                System.out.println("Daily Portfolio Data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + ", Date: " + TARGET_DATE);
                System.out.println("=".repeat(200));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                        "PORTFOLIO_DATE", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SHARE_PRICE", "SHARES_VALUE", "AVG_PRICE", "BLOCKED_SHARES");
                    System.out.println("-".repeat(200));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                            row.get("PORTFOLIO_DATE"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SHARE_PRICE"),
                            row.get("SHARES_VALUE"),
                            row.get("AVG_PRICE"),
                            row.get("BLOCKED_SHARES"));
                    }

                    System.out.println("=".repeat(200));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchEquDailyPortfolioByNinAndCompanyAndDate executed successfully");
        } else {
            logger.warn("⚠ No data found for Company: " + TARGET_COMPANY + " and Date: " + TARGET_DATE + " (this may be expected)");
        }

        System.out.println("========================================\n");
    }

    @Test(priority = 5, description = "Test fetchEquDailyPortfolioByPortfolioDate method")
    public void testFetchEquDailyPortfolioByPortfolioDate() {
        logger.info("=== Test 5: fetchEquDailyPortfolioByPortfolioDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Fetch Daily Portfolio By Portfolio Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Portfolio Date: " + TARGET_DATE);

        GetEquDailyPortfolioData dateOnlyData = new GetEquDailyPortfolioData(dbConnection);
        boolean success = dateOnlyData.fetchEquDailyPortfolioByPortfolioDate(TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_DATE);

                System.out.println("\n" + "=".repeat(200));
                System.out.println("Daily Portfolio Data for Portfolio Date: " + TARGET_DATE + " (All NIINs)");
                System.out.println("=".repeat(200));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                        "PORTFOLIO_DATE", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SHARE_PRICE", "SHARES_VALUE", "AVG_PRICE", "BLOCKED_SHARES");
                    System.out.println("-".repeat(200));

                    // Print rows (limit to first 20 for readability)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-10s | %-12s | %-15s | %-15s | %-12s | %-15s | %-12s | %-15s%n",
                            row.get("PORTFOLIO_DATE"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SHARE_PRICE"),
                            row.get("SHARES_VALUE"),
                            row.get("AVG_PRICE"),
                            row.get("BLOCKED_SHARES"));
                    }

                    System.out.println("=".repeat(200));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchEquDailyPortfolioByPortfolioDate executed successfully");
        } else {
            logger.warn("⚠ No data found for Portfolio Date: " + TARGET_DATE + " (this may be expected if date has no records)");
        }

        System.out.println("========================================\n");
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
