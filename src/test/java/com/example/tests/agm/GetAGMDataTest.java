package com.example.tests.agm;

import com.example.screensData.agm.GetAGMData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetAGMData - Testing AGM data fetch methods
 */
public class GetAGMDataTest {

    private static final Logger logger = Logger.getLogger(GetAGMDataTest.class);
    private OracleDBConnection dbConnection;
    private GetAGMData agmData;

    private static final String TARGET_COMPANY_CODE = "QNBK";
    private static final String TARGET_NEXT_WORK_DAY = "03-11-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for AGM data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            agmData = new GetAGMData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchAGMDataByCompanyCode method")
    public void testFetchAGMDataByCompanyCode() {
        logger.info("=== Test 1: fetchAGMDataByCompanyCode ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch AGM Data By Company Code");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Company Code: " + TARGET_COMPANY_CODE);

        boolean success = agmData.fetchAGMDataByCompanyCode(TARGET_COMPANY_CODE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - AGM data found for Company Code: " + TARGET_COMPANY_CODE);

            // Print detailed data table
            try {
                String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                              "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                              "FROM Fund_Agm_Dates AGM " +
                              "WHERE AGM.COMPANY_CODE = ? " +
                              "ORDER BY AGM.TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_COMPANY_CODE);

                System.out.println("\n" + "=".repeat(140));
                System.out.println("AGM DATA FOR COMPANY CODE: " + TARGET_COMPANY_CODE);
                System.out.println("=".repeat(140));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                        "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
                    System.out.println("-".repeat(140));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                            row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "",
                            row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "",
                            row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "",
                            row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "",
                            row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "",
                            row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "",
                            row.get("APPROVED") != null ? row.get("APPROVED").toString() : "");
                    }

                    System.out.println("=".repeat(140));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No AGM data found for Company Code: " + TARGET_COMPANY_CODE);
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch AGM data for company code: " + TARGET_COMPANY_CODE);
        logger.info("✓ fetchAGMDataByCompanyCode executed successfully");
    }

    @Test(priority = 2, description = "Test fetchAllAGMData method")
    public void testFetchAllAGMData() {
        logger.info("=== Test 2: fetchAllAGMData ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch All AGM Data");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - None (fetching all company codes)");

        GetAGMData allAGMData = new GetAGMData(dbConnection);
        boolean success = allAGMData.fetchAllAGMData();

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - AGM data found");

            // Print detailed data table (first 20 records)
            try {
                String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                              "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                              "FROM Fund_Agm_Dates AGM " +
                              "ORDER BY AGM.TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                System.out.println("\n" + "=".repeat(140));
                System.out.println("ALL AGM DATA (Showing first 20 records)");
                System.out.println("=".repeat(140));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                        "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
                    System.out.println("-".repeat(140));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                            row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "",
                            row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "",
                            row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "",
                            row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "",
                            row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "",
                            row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "",
                            row.get("APPROVED") != null ? row.get("APPROVED").toString() : "");
                    }

                    System.out.println("=".repeat(140));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " total records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No AGM data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch all AGM data");
        logger.info("✓ fetchAllAGMData executed successfully");
    }

    @Test(priority = 3, description = "Test fetchAGMDataByNextWorkDay method")
    public void testFetchAGMDataByNextWorkDay() {
        logger.info("=== Test 3: fetchAGMDataByNextWorkDay ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch AGM Data By Next Work Day");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Next Work Day: " + TARGET_NEXT_WORK_DAY + " (format: dd-mm-yyyy)");

        GetAGMData nextWorkDayData = new GetAGMData(dbConnection);
        boolean success = nextWorkDayData.fetchAGMDataByNextWorkDay(TARGET_NEXT_WORK_DAY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - AGM data found for Next Work Day: " + TARGET_NEXT_WORK_DAY);

            // Print detailed data table
            try {
                String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                              "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                              "FROM Fund_Agm_Dates AGM " +
                              "WHERE TRUNC(AGM.NEXT_WORK_DAY) = TO_DATE(?, 'DD-MM-YYYY') " +
                              "ORDER BY AGM.TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, TARGET_NEXT_WORK_DAY);

                System.out.println("\n" + "=".repeat(140));
                System.out.println("AGM DATA FOR NEXT WORK DAY: " + TARGET_NEXT_WORK_DAY);
                System.out.println("=".repeat(140));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                        "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
                    System.out.println("-".repeat(140));

                    // Print rows
                    for (Map<String, Object> row : results) {
                        System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                            row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "",
                            row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "",
                            row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "",
                            row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "",
                            row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "",
                            row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "",
                            row.get("APPROVED") != null ? row.get("APPROVED").toString() : "");
                    }

                    System.out.println("=".repeat(140));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No AGM data found for Next Work Day: " + TARGET_NEXT_WORK_DAY);
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch AGM data for next work day: " + TARGET_NEXT_WORK_DAY);
        logger.info("✓ fetchAGMDataByNextWorkDay executed successfully");
    }

    @Test(priority = 4, description = "Test fetchAGMDataWithCashDistribution method")
    public void testFetchAGMDataWithCashDistribution() {
        logger.info("=== Test 4: fetchAGMDataWithCashDistribution ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Fetch AGM Data With Cash Distribution");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Filter: Records with non-zero cash distribution");

        GetAGMData cashDistData = new GetAGMData(dbConnection);
        boolean success = cashDistData.fetchAGMDataWithCashDistribution();

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - AGM data with cash distribution found");

            // Print detailed data table (first 20 records)
            try {
                String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                              "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                              "FROM Fund_Agm_Dates AGM " +
                              "WHERE AGM.CASH_DISTRIBUTION IS NOT NULL AND AGM.CASH_DISTRIBUTION > 0 " +
                              "ORDER BY AGM.TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                System.out.println("\n" + "=".repeat(140));
                System.out.println("AGM DATA WITH CASH DISTRIBUTION (Showing first 20 records)");
                System.out.println("=".repeat(140));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                        "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
                    System.out.println("-".repeat(140));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                            row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "",
                            row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "",
                            row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "",
                            row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "",
                            row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "",
                            row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "",
                            row.get("APPROVED") != null ? row.get("APPROVED").toString() : "");
                    }

                    System.out.println("=".repeat(140));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " total records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No AGM data with cash distribution found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch AGM data with cash distribution");
        logger.info("✓ fetchAGMDataWithCashDistribution executed successfully");
    }

    @Test(priority = 5, description = "Test fetchAGMDataWithSharesDistribution method")
    public void testFetchAGMDataWithSharesDistribution() {
        logger.info("=== Test 5: fetchAGMDataWithSharesDistribution ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Fetch AGM Data With Shares Distribution");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Filter: Records with non-zero shares distribution");

        GetAGMData sharesDistData = new GetAGMData(dbConnection);
        boolean success = sharesDistData.fetchAGMDataWithSharesDistribution();

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - AGM data with shares distribution found");

            // Print detailed data table (first 20 records)
            try {
                String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                              "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                              "FROM Fund_Agm_Dates AGM " +
                              "WHERE AGM.SHARE_DISTRIBUTION IS NOT NULL AND AGM.SHARE_DISTRIBUTION > 0 " +
                              "ORDER BY AGM.TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                System.out.println("\n" + "=".repeat(140));
                System.out.println("AGM DATA WITH SHARES DISTRIBUTION (Showing first 20 records)");
                System.out.println("=".repeat(140));

                if (!results.isEmpty()) {
                    // Print header
                    System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                        "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
                    System.out.println("-".repeat(140));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        System.out.printf("%-15s | %-25s | %-18s | %-18s | %-23s | %-18s | %-10s%n",
                            row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "",
                            row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "",
                            row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "",
                            row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "",
                            row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "",
                            row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "",
                            row.get("APPROVED") != null ? row.get("APPROVED").toString() : "");
                    }

                    System.out.println("=".repeat(140));
                    if (results.size() > 20) {
                        System.out.println("Showing first 20 of " + results.size() + " total records");
                    } else {
                        System.out.println("Total Records: " + results.size());
                    }
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No AGM data with shares distribution found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch AGM data with shares distribution");
        logger.info("✓ fetchAGMDataWithSharesDistribution executed successfully");
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
