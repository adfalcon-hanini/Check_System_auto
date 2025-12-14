package com.example.tests.portfolio;

import com.example.screensData.portfolio.GetEqSharesData;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Equity Shares Test Suite
 *
 * TestNG Groups:
 * - portfolio: Module group
 * - database: Type group
 * - smoke: Critical tests
 * - regression: All tests
 * - fast: Quick execution
 */
@Epic("Portfolio Module")
@Feature("Equity Shares Management")
public class GetEqSharesDataTest {

    private static final Logger logger = Logger.getLogger(GetEqSharesDataTest.class);
    private OracleDBConnection dbConnection;
    private GetEqSharesData eqSharesData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "BRES";
    private static final String TARGET_DATE = "2025-12-10";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for equity shares data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            eqSharesData = new GetEqSharesData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, groups = {"portfolio", "database", "smoke", "regression", "fast"},
          description = "Test fetchEqSharesByNin method")
    public void testFetchEqSharesByNin() {
        logger.info("=== Test 1: fetchEqSharesByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch Equity Shares By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        boolean success = eqSharesData.fetchEqSharesByNin(TARGET_NIN);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQ_SHARES " +
                              "WHERE NIN = ? " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                              "ORDER BY TIME_STAMP DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN);

                System.out.println("\n" + "=".repeat(180));
                System.out.println("Equity Shares Data for NIN: " + TARGET_NIN);
                System.out.println("=".repeat(180));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                        "SEQ", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SEQ_2", "STREAM_SEQ", "DAILY_BATCH", "BLOCKED_SHARES", "CL_ID", "TIME_STAMP");
                    System.out.println("-".repeat(180));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SEQ"),
                            row.get("STREAM_SEQ"),
                            row.get("IS_DAILY_BATCH"),
                            row.get("BLOCKED_SHARES"),
                            row.get("CL_ID"),
                            row.get("TIME_STAMP"));
                    }

                    System.out.println("=".repeat(180));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN);
        logger.info("✓ fetchEqSharesByNin executed successfully");
    }

    @Test(priority = 2, description = "Test fetchEqSharesByNinAndCompany method")
    public void testFetchEqSharesByNinAndCompany() {
        logger.info("=== Test 2: fetchEqSharesByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Equity Shares By NIN and Company");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);

        GetEqSharesData companyData = new GetEqSharesData(dbConnection);
        boolean success = companyData.fetchEqSharesByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQ_SHARES " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                              "ORDER BY TIME_STAMP DESC";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(180));
                System.out.println("Equity Shares Data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
                System.out.println("=".repeat(180));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                        "SEQ", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SEQ_2", "STREAM_SEQ", "DAILY_BATCH", "BLOCKED_SHARES", "CL_ID", "TIME_STAMP");
                    System.out.println("-".repeat(180));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SEQ"),
                            row.get("STREAM_SEQ"),
                            row.get("IS_DAILY_BATCH"),
                            row.get("BLOCKED_SHARES"),
                            row.get("CL_ID"),
                            row.get("TIME_STAMP"));
                    }

                    System.out.println("=".repeat(180));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
        logger.info("✓ fetchEqSharesByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test fetchEqSharesByNinAndDate method")
    public void testFetchEqSharesByNinAndDate() {
        logger.info("=== Test 3: fetchEqSharesByNinAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Equity Shares By NIN and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Date: " + TARGET_DATE);

        GetEqSharesData dateData = new GetEqSharesData(dbConnection);
        boolean success = dateData.fetchEqSharesByNinAndDate(TARGET_NIN, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQ_SHARES " +
                              "WHERE NIN = ? AND TRUNC(TIME_STAMP) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_DATE);

                System.out.println("\n" + "=".repeat(180));
                System.out.println("Equity Shares Data for NIN: " + TARGET_NIN + " and Date: " + TARGET_DATE);
                System.out.println("=".repeat(180));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                        "SEQ", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SEQ_2", "STREAM_SEQ", "DAILY_BATCH", "BLOCKED_SHARES", "CL_ID", "TIME_STAMP");
                    System.out.println("-".repeat(180));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SEQ"),
                            row.get("STREAM_SEQ"),
                            row.get("IS_DAILY_BATCH"),
                            row.get("BLOCKED_SHARES"),
                            row.get("CL_ID"),
                            row.get("TIME_STAMP"));
                    }

                    System.out.println("=".repeat(180));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchEqSharesByNinAndDate executed successfully");
        } else {
            logger.warn("⚠ No data found for date: " + TARGET_DATE + " (this may be expected if date has no records)");
        }

        System.out.println("========================================\n");
    }

    @Test(priority = 4, description = "Test fetchEqSharesByNinAndCompanyAndDate method")
    public void testFetchEqSharesByNinAndCompanyAndDate() {
        logger.info("=== Test 4: fetchEqSharesByNinAndCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch Equity Shares By NIN, Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company: " + TARGET_COMPANY);
        System.out.println("  - Date: " + TARGET_DATE);

        GetEqSharesData specificData = new GetEqSharesData(dbConnection);
        boolean success = specificData.fetchEqSharesByNinAndCompanyAndDate(TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT * FROM SEC_EQ_SHARES " +
                              "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(TIME_STAMP) = TO_DATE(?, 'YYYY-MM-DD') " +
                              "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(180));
                System.out.println("Equity Shares Data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + ", Date: " + TARGET_DATE);
                System.out.println("=".repeat(180));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                        "SEQ", "NIN", "C_ACCOUNT", "COMPANY_CODE", "SHARES_COUNT", "SEQ_2", "STREAM_SEQ", "DAILY_BATCH", "BLOCKED_SHARES", "CL_ID", "TIME_STAMP");
                    System.out.println("-".repeat(180));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-8s | %-10s | %-12s | %-15s | %-15s | %-10s | %-12s | %-12s | %-15s | %-15s | %-10s%n",
                            row.get("SEQ"),
                            row.get("NIN"),
                            row.get("C_ACCOUNT"),
                            row.get("COMPANY_CODE"),
                            row.get("SHARES_COUNT"),
                            row.get("SEQ"),
                            row.get("STREAM_SEQ"),
                            row.get("IS_DAILY_BATCH"),
                            row.get("BLOCKED_SHARES"),
                            row.get("CL_ID"),
                            row.get("TIME_STAMP"));
                    }

                    System.out.println("=".repeat(180));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
            logger.info("✓ fetchEqSharesByNinAndCompanyAndDate executed successfully");
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
