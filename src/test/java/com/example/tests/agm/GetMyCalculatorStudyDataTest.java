package com.example.tests.agm;

import com.example.screensData.agm.GetMyCalculatorStudyData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test class for GetMyCalculatorStudyData - Testing Mycalculator_Study data fetch methods
 */
public class GetMyCalculatorStudyDataTest {

    private static final Logger logger = Logger.getLogger(GetMyCalculatorStudyDataTest.class);
    private OracleDBConnection dbConnection;
    private GetMyCalculatorStudyData calculatorData;

    // Column list matching the main class
    private static final String COLUMN_LIST = "NIN, TRNX_DATE, SHARES_VALUE, CASH, DIVIDEND, ASSETS, " +
                                               "WITHDRAW, DEPOSIT, PROFIT_LOSS, FUND_AMOUNT, FEES_AMOUNT, BONUS";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for Mycalculator_Study tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            calculatorData = new GetMyCalculatorStudyData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test fetchAllData method")
    public void testFetchAllData() {
        logger.info("=== Test 1: fetchAllData ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Fetch All Data from Mycalculator_Study");
        System.out.println("========================================");
        System.out.println("Query: SELECT * FROM Mycalculator_Study ORDER BY TIME_STAMP DESC");

        boolean success = calculatorData.fetchAllData();

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Mycalculator_Study data found");
            System.out.println("Total Records: " + calculatorData.getRecordCount());

            // Print detailed data table
            try {
                String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study ORDER BY TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                if (!results.isEmpty()) {
                    System.out.println("\n" + "=".repeat(150));
                    System.out.println("MYCALCULATOR_STUDY DATA (Showing first 20 records)");
                    System.out.println("=".repeat(150));

                    // Get column names from first record
                    Map<String, Object> firstRecord = results.get(0);
                    List<String> columnNames = new ArrayList<>(firstRecord.keySet());

                    // Print header
                    for (String columnName : columnNames) {
                        System.out.printf("%-25s | ", columnName);
                    }
                    System.out.println();
                    System.out.println("-".repeat(150));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        for (String columnName : columnNames) {
                            Object value = row.get(columnName);
                            String displayValue = value != null ? value.toString() : "";
                            // Truncate long values
                            if (displayValue.length() > 23) {
                                displayValue = displayValue.substring(0, 20) + "...";
                            }
                            System.out.printf("%-25s | ", displayValue);
                        }
                        System.out.println();
                    }

                    System.out.println("=".repeat(150));
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
            System.out.println("Status: No data found in Mycalculator_Study table");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data from Mycalculator_Study table");
        logger.info("✓ fetchAllData executed successfully");
    }

    @Test(priority = 2, description = "Test fetchTopNRecords method")
    public void testFetchTopNRecords() {
        logger.info("=== Test 2: fetchTopNRecords ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Fetch Top 10 Records from Mycalculator_Study");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - Limit: 10 records");

        GetMyCalculatorStudyData topData = new GetMyCalculatorStudyData(dbConnection);
        boolean success = topData.fetchTopNRecords(10);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Top 10 records fetched");
            System.out.println("Total Records: " + topData.getRecordCount());

            // Print detailed data table
            try {
                String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study WHERE ROWNUM <= 10 ORDER BY TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQuery(query);

                if (!results.isEmpty()) {
                    System.out.println("\n" + "=".repeat(150));
                    System.out.println("TOP 10 MYCALCULATOR_STUDY RECORDS");
                    System.out.println("=".repeat(150));

                    // Get column names from first record
                    Map<String, Object> firstRecord = results.get(0);
                    List<String> columnNames = new ArrayList<>(firstRecord.keySet());

                    // Print header
                    for (String columnName : columnNames) {
                        System.out.printf("%-25s | ", columnName);
                    }
                    System.out.println();
                    System.out.println("-".repeat(150));

                    // Print all rows
                    for (Map<String, Object> row : results) {
                        for (String columnName : columnNames) {
                            Object value = row.get(columnName);
                            String displayValue = value != null ? value.toString() : "";
                            // Truncate long values
                            if (displayValue.length() > 23) {
                                displayValue = displayValue.substring(0, 20) + "...";
                            }
                            System.out.printf("%-25s | ", displayValue);
                        }
                        System.out.println();
                    }

                    System.out.println("=".repeat(150));
                    System.out.println("Total Records: " + results.size());
                }
            } catch (Exception e) {
                logger.error("Error printing table data: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Status: No data found in Mycalculator_Study table");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch top 10 records from Mycalculator_Study table");
        logger.info("✓ fetchTopNRecords executed successfully");
    }

    @Test(priority = 3, description = "Test fetchDataByNIN method with NIN 12240")
    public void testFetchDataByNIN() {
        logger.info("=== Test 3: fetchDataByNIN ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Fetch Data By NIN");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: 12240");

        GetMyCalculatorStudyData ninData = new GetMyCalculatorStudyData(dbConnection);
        boolean success = ninData.fetchDataByNIN("12240");

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (success) {
            System.out.println("Status: SUCCESS - Data found for NIN 12240");
            System.out.println("Total Records: " + ninData.getRecordCount());

            // Print detailed data table
            try {
                String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study WHERE NIN = ? ORDER BY TIME_STAMP DESC";
                List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, "12240");

                if (!results.isEmpty()) {
                    System.out.println("\n" + "=".repeat(150));
                    System.out.println("MYCALCULATOR_STUDY DATA FOR NIN 12240 (Showing first 20 records)");
                    System.out.println("=".repeat(150));

                    // Get column names from first record
                    Map<String, Object> firstRecord = results.get(0);
                    List<String> columnNames = new ArrayList<>(firstRecord.keySet());

                    // Print header
                    for (String columnName : columnNames) {
                        System.out.printf("%-25s | ", columnName);
                    }
                    System.out.println();
                    System.out.println("-".repeat(150));

                    // Print rows (limit to first 20)
                    int displayLimit = Math.min(results.size(), 20);
                    for (int i = 0; i < displayLimit; i++) {
                        Map<String, Object> row = results.get(i);
                        for (String columnName : columnNames) {
                            Object value = row.get(columnName);
                            String displayValue = value != null ? value.toString() : "";
                            // Truncate long values
                            if (displayValue.length() > 23) {
                                displayValue = displayValue.substring(0, 20) + "...";
                            }
                            System.out.printf("%-25s | ", displayValue);
                        }
                        System.out.println();
                    }

                    System.out.println("=".repeat(150));
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
            System.out.println("Status: No data found for NIN 12240");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(success, "Should fetch data for NIN 12240");
        logger.info("✓ fetchDataByNIN executed successfully");
    }

    @Test(priority = 4, description = "Test getRecordCount method")
    public void testGetRecordCount() {
        logger.info("=== Test 4: getRecordCount ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Record Count");
        System.out.println("========================================");

        GetMyCalculatorStudyData countData = new GetMyCalculatorStudyData(dbConnection);
        countData.fetchAllData();
        int count = countData.getRecordCount();

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        System.out.println("Total Records in Mycalculator_Study: " + count);
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        Assert.assertTrue(count >= 0, "Record count should be non-negative");
        logger.info("✓ getRecordCount executed successfully. Count: " + count);
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
