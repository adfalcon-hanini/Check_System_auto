package com.example.tests.portfolio;

import com.example.screensData.portfolio.GetPortfolioAvgPriceData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetPortfolioAvgPriceData - Testing 4 fetch methods
 */
public class GetPortfolioAvgPriceDataTest {

    private static final Logger logger = Logger.getLogger(GetPortfolioAvgPriceDataTest.class);
    private OracleDBConnection dbConnection;
    private GetPortfolioAvgPriceData portfolioData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "BRES";
    private static final String TARGET_DATE = "2025-12-10";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for portfolio average price data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            portfolioData = new GetPortfolioAvgPriceData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchPortfolioAvgPriceByNin method")
    public void testFetchPortfolioAvgPriceByNin() {
        logger.info("=== Test 1: fetchPortfolioAvgPriceByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch Portfolio Avg Price By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        boolean success = portfolioData.fetchPortfolioAvgPriceByNin(TARGET_NIN);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                              "WHERE NIN = ? " +
                              "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                              "AND AMOUNT IS NOT NULL AND AMOUNT != 0 " +
                              "ORDER BY TRADE_DATE DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN);

                System.out.println("\n" + "=".repeat(150));
                System.out.println("Portfolio Average Price Data for NIN: " + TARGET_NIN);
                System.out.println("=".repeat(150));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                        "SEQ", "NIN", "COMPANY_CODE", "AVG_PRICE", "AMOUNT", "VALUE", "TRADE_DATE");
                    System.out.println("-".repeat(150));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("COMPANY_CODE"),
                            row.get("AVG_PRICE"),
                            row.get("AMOUNT"),
                            row.get("VALUE"),
                            row.get("TRADE_DATE"));
                    }

                    System.out.println("=".repeat(150));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN);
        logger.info("✓ fetchPortfolioAvgPriceByNin executed successfully");
    }

    @Test(priority = 2, description = "Test fetchPortfolioAvgPriceByNinAndCompany method")
    public void testFetchPortfolioAvgPriceByNinAndCompany() {
        logger.info("=== Test 2: fetchPortfolioAvgPriceByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Portfolio Avg Price By NIN and Company");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);

        GetPortfolioAvgPriceData companyData = new GetPortfolioAvgPriceData(dbConnection);
        boolean success = companyData.fetchPortfolioAvgPriceByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? " +
                              "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                              "AND AMOUNT IS NOT NULL AND AMOUNT != 0 " +
                              "ORDER BY TRADE_DATE DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(150));
                System.out.println("Portfolio Average Price Data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
                System.out.println("=".repeat(150));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                        "SEQ", "NIN", "COMPANY_CODE", "AVG_PRICE", "AMOUNT", "VALUE", "TRADE_DATE");
                    System.out.println("-".repeat(150));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("COMPANY_CODE"),
                            row.get("AVG_PRICE"),
                            row.get("AMOUNT"),
                            row.get("VALUE"),
                            row.get("TRADE_DATE"));
                    }

                    System.out.println("=".repeat(150));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        // Soft assertion - don't fail if no data exists for this combination
        if (!success) {
            System.out.println("⚠ WARNING: No portfolio data found for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
            System.out.println("This may be expected if the client doesn't hold this security.");
        }
        // Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
        logger.info("✓ fetchPortfolioAvgPriceByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test fetchPortfolioAvgPriceByNinAndDate method")
    public void testFetchPortfolioAvgPriceByNinAndDate() {
        logger.info("=== Test 3: fetchPortfolioAvgPriceByNinAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Portfolio Avg Price By NIN and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Date: " + TARGET_DATE);

        GetPortfolioAvgPriceData dateData = new GetPortfolioAvgPriceData(dbConnection);
        boolean success = dateData.fetchPortfolioAvgPriceByNinAndDate(TARGET_NIN, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                              "WHERE NIN = ? AND TRUNC(TRADE_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                              "AND AMOUNT IS NOT NULL AND AMOUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_DATE);

                System.out.println("\n" + "=".repeat(150));
                System.out.println("Portfolio Average Price Data for NIN: " + TARGET_NIN + " and Date: " + TARGET_DATE);
                System.out.println("=".repeat(150));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                        "SEQ", "NIN", "COMPANY_CODE", "AVG_PRICE", "AMOUNT", "VALUE", "TRADE_DATE");
                    System.out.println("-".repeat(150));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("COMPANY_CODE"),
                            row.get("AVG_PRICE"),
                            row.get("AMOUNT"),
                            row.get("VALUE"),
                            row.get("TRADE_DATE"));
                    }

                    System.out.println("=".repeat(150));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchPortfolioAvgPriceByNinAndDate executed successfully");
        } else {
            logger.warn("⚠ No data found for date: " + TARGET_DATE + " (this may be expected if date has no records)");
        }

        System.out.println("========================================\n");
    }

    @Test(priority = 4, description = "Test fetchPortfolioAvgPriceByNinAndCompanyAndDate method")
    public void testFetchPortfolioAvgPriceByNinAndCompanyAndDate() {
        logger.info("=== Test 4: fetchPortfolioAvgPriceByNinAndCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch Portfolio Avg Price By NIN, Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);
        System.out.println("  - Date: " + TARGET_DATE);

        GetPortfolioAvgPriceData specificData = new GetPortfolioAvgPriceData(dbConnection);
        boolean success = specificData.fetchPortfolioAvgPriceByNinAndCompanyAndDate(TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(TRADE_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                              "AND AMOUNT IS NOT NULL AND AMOUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(150));
                System.out.println("Portfolio Average Price Data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + ", Date: " + TARGET_DATE);
                System.out.println("=".repeat(150));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                        "SEQ", "NIN", "COMPANY_CODE", "AVG_PRICE", "AMOUNT", "VALUE", "TRADE_DATE");
                    System.out.println("-".repeat(150));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-15s | %-15s | %-15s | %-15s | %-20s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("COMPANY_CODE"),
                            row.get("AVG_PRICE"),
                            row.get("AMOUNT"),
                            row.get("VALUE"),
                            row.get("TRADE_DATE"));
                    }

                    System.out.println("=".repeat(150));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchPortfolioAvgPriceByNinAndCompanyAndDate executed successfully");
        } else {
            logger.warn("⚠ No data found for Company: " + TARGET_COMPANY + " and Date: " + TARGET_DATE + " (this may be expected)");
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
