package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetPortfolioVolumData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetPortfolioVolumData - Testing 7 fetch methods
 */
public class GetPortfolioVolumDataTest {

    private static final Logger logger = Logger.getLogger(GetPortfolioVolumDataTest.class);
    private OracleDBConnection dbConnection;
    private GetPortfolioVolumData portfolioVolumData;

    private static final String TARGET_NIN = "843";
    private static final String TARGET_COMPANY = "QNBK";
    private static final String TARGET_DATE = "25-Nov-2025";
    private static final String START_DATE = "01-Nov-2025";
    private static final String END_DATE = "25-Nov-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for portfolio volume data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            portfolioVolumData = new GetPortfolioVolumData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchPortfolioVolumByNinAndCompanyAndDate method")
    public void testFetchPortfolioVolumByNinAndCompanyAndDate() {
        logger.info("=== Test 1: fetchPortfolioVolumByNinAndCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch Portfolio Volume By NIN, Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Portfolio Date (up to): " + TARGET_DATE);

        boolean success = portfolioVolumData.fetchPortfolioVolumByNinAndCompanyAndDate(TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND COMPANY_CODE = ? " +
                              "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY PORTFOLIO_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + ", up to Date: " + TARGET_DATE);
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch portfolio volume data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY);
        logger.info("✓ fetchPortfolioVolumByNinAndCompanyAndDate executed successfully");
    }

    @Test(priority = 2, description = "Test fetchPortfolioVolumByNinAndCompany method")
    public void testFetchPortfolioVolumByNinAndCompany() {
        logger.info("=== Test 2: fetchPortfolioVolumByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Portfolio Volume By NIN and Company");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetPortfolioVolumData companyData = new GetPortfolioVolumData(dbConnection);
        boolean success = companyData.fetchPortfolioVolumByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND COMPANY_CODE = ? " +
                              "ORDER BY PORTFOLIO_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY + " (Showing first 20)");
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
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

        Assert.assertTrue(success, "Should fetch portfolio volume data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
        logger.info("✓ fetchPortfolioVolumByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test fetchPortfolioVolumByNin method")
    public void testFetchPortfolioVolumByNin() {
        logger.info("=== Test 3: fetchPortfolioVolumByNin ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Portfolio Volume By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);

        GetPortfolioVolumData ninData = new GetPortfolioVolumData(dbConnection);
        boolean success = ninData.fetchPortfolioVolumByNin(TARGET_NIN);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "ORDER BY PORTFOLIO_DATE DESC, COMPANY_CODE";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for NIN: " + TARGET_NIN + " (Showing first 20)");
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
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

        Assert.assertTrue(success, "Should fetch portfolio volume data for NIN: " + TARGET_NIN);
        logger.info("✓ fetchPortfolioVolumByNin executed successfully");
    }

    @Test(priority = 4, description = "Test fetchPortfolioVolumByNinAndDate method")
    public void testFetchPortfolioVolumByNinAndDate() {
        logger.info("=== Test 4: fetchPortfolioVolumByNinAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch Portfolio Volume By NIN and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Portfolio Date (up to): " + TARGET_DATE);

        GetPortfolioVolumData dateData = new GetPortfolioVolumData(dbConnection);
        boolean success = dateData.fetchPortfolioVolumByNinAndDate(TARGET_NIN, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY PORTFOLIO_DATE DESC, COMPANY_CODE";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_DATE);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for NIN: " + TARGET_NIN + " up to Date: " + TARGET_DATE + " (Showing first 20)");
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
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

        Assert.assertTrue(success, "Should fetch portfolio volume data for NIN: " + TARGET_NIN + " up to Date: " + TARGET_DATE);
        logger.info("✓ fetchPortfolioVolumByNinAndDate executed successfully");
    }

    @Test(priority = 5, description = "Test fetchPortfolioVolumByCompanyAndDate method")
    public void testFetchPortfolioVolumByCompanyAndDate() {
        logger.info("=== Test 5: fetchPortfolioVolumByCompanyAndDate ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Fetch Portfolio Volume By Company and Date");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Portfolio Date (up to): " + TARGET_DATE);

        GetPortfolioVolumData companyDateData = new GetPortfolioVolumData(dbConnection);
        boolean success = companyDateData.fetchPortfolioVolumByCompanyAndDate(TARGET_COMPANY, TARGET_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE COMPANY_CODE = ? " +
                              "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY PORTFOLIO_DATE DESC, NIN";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY, TARGET_DATE);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for Company: " + TARGET_COMPANY + " up to Date: " + TARGET_DATE + " (Showing first 20)");
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
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

        Assert.assertTrue(success, "Should fetch portfolio volume data for Company: " + TARGET_COMPANY + " up to Date: " + TARGET_DATE);
        logger.info("✓ fetchPortfolioVolumByCompanyAndDate executed successfully");
    }

    @Test(priority = 6, description = "Test fetchLatestPortfolioVolumByNinAndCompany method")
    public void testFetchLatestPortfolioVolumByNinAndCompany() {
        logger.info("=== Test 6: fetchLatestPortfolioVolumByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 6: Fetch Latest Portfolio Volume By NIN and Company");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetPortfolioVolumData latestData = new GetPortfolioVolumData(dbConnection);
        boolean success = latestData.fetchLatestPortfolioVolumByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND COMPANY_CODE = ? " +
                              "AND PORTFOLIO_DATE = (SELECT MAX(PORTFOLIO_DATE) FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "                      WHERE NIN = ? AND COMPANY_CODE = ?)";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, TARGET_NIN, TARGET_COMPANY);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Latest Portfolio Volume Data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch latest portfolio volume data for NIN: " + TARGET_NIN + " and Company: " + TARGET_COMPANY);
        logger.info("✓ fetchLatestPortfolioVolumByNinAndCompany executed successfully");
    }

    @Test(priority = 7, description = "Test fetchPortfolioVolumByNinAndCompanyAndDateRange method")
    public void testFetchPortfolioVolumByNinAndCompanyAndDateRange() {
        logger.info("=== Test 7: fetchPortfolioVolumByNinAndCompanyAndDateRange ===");
        System.out.println("\n========================================");
        System.out.println("Test 7: Fetch Portfolio Volume By NIN, Company and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetPortfolioVolumData rangeData = new GetPortfolioVolumData(dbConnection);
        boolean success = rangeData.fetchPortfolioVolumByNinAndCompanyAndDateRange(TARGET_NIN, TARGET_COMPANY, START_DATE, END_DATE);

        System.out.println("\nResult: " + (success ? "SUCCESS - Data found" : "FAILED - No data found"));

        // Print data as table
        if (success) {
            try {
                String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                              "WHERE NIN = ? " +
                              "AND COMPANY_CODE = ? " +
                              "AND PORTFOLIO_DATE >= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                              "ORDER BY PORTFOLIO_DATE DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NIN, TARGET_COMPANY, START_DATE, END_DATE);

                System.out.println("\n" + "=".repeat(120));
                System.out.println("Portfolio Volume Data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + " between " + START_DATE + " and " + END_DATE);
                System.out.println("=".repeat(120));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                        "NIN", "PORTFOLIO_DATE", "SHARES_COUNT", "COMPANY_CODE");
                    System.out.println("-".repeat(120));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-10s | %-25s | %-20s | %-15s%n",
                            row.get("NIN"),
                            row.get("PORTFOLIO_DATE"),
                            row.get("SHARES_COUNT"),
                            row.get("COMPANY_CODE"));
                    }

                    System.out.println("=".repeat(120));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        }

        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch portfolio volume data for NIN: " + TARGET_NIN + ", Company: " + TARGET_COMPANY + " in date range");
        logger.info("✓ fetchPortfolioVolumByNinAndCompanyAndDateRange executed successfully");
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
