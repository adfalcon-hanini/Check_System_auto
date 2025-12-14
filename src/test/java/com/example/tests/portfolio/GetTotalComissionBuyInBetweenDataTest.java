package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetTotalComissionBuyInBetweenData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetTotalComissionBuyInBetweenData - Testing 4 calculation methods
 */
public class GetTotalComissionBuyInBetweenDataTest {

    private static final Logger logger = Logger.getLogger(GetTotalComissionBuyInBetweenDataTest.class);
    private OracleDBConnection dbConnection;
    private GetTotalComissionBuyInBetweenData totalCommissionBuyData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "MARK";
    private static final String START_DATE = "01-Nov-2025";
    private static final String END_DATE = "26-Nov-2025";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for total commission buy data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            totalCommissionBuyData = new GetTotalComissionBuyInBetweenData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test getTotalCommissionBuyByNinAndCompanyBetweenDates method")
    public void testGetTotalCommissionBuyByNinAndCompanyBetweenDates() {
        logger.info("=== Test 1: getTotalCommissionBuyByNinAndCompanyBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Total Commission for BUY By NIN, Company and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        Double totalCommission = totalCommissionBuyData.getTotalCommissionBuyByNinAndCompanyBetweenDates(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalCommission != null) {
            System.out.println("Total Commission for BUY: " + totalCommission);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total Commission: 0 (No commission data found for BUY transactions)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalCommissionBuyByNinAndCompanyBetweenDates executed successfully");
    }

    @Test(priority = 2, description = "Test getTotalCommissionBuyByNinAndCompany method")
    public void testGetTotalCommissionBuyByNinAndCompany() {
        logger.info("=== Test 2: getTotalCommissionBuyByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Total Commission for BUY By NIN and Company (All Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetTotalComissionBuyInBetweenData allDatesData = new GetTotalComissionBuyInBetweenData(dbConnection);
        Double totalCommission = allDatesData.getTotalCommissionBuyByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalCommission != null) {
            System.out.println("Total Commission for BUY (All Dates): " + totalCommission);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total Commission: 0 (No commission data found for BUY transactions)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalCommissionBuyByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test getTotalCommissionBuyByNinBetweenDates method")
    public void testGetTotalCommissionBuyByNinBetweenDates() {
        logger.info("=== Test 3: getTotalCommissionBuyByNinBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Total Commission for BUY By NIN (All Companies)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalComissionBuyInBetweenData ninData = new GetTotalComissionBuyInBetweenData(dbConnection);
        Double totalCommission = ninData.getTotalCommissionBuyByNinBetweenDates(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalCommission != null) {
            System.out.println("Total Commission for BUY (All Companies): " + totalCommission);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total Commission: 0 (No commission data found for BUY transactions)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalCommissionBuyByNinBetweenDates executed successfully");
    }

    @Test(priority = 4, description = "Test getCommissionBuyCount method")
    public void testGetCommissionBuyCount() {
        logger.info("=== Test 4: getCommissionBuyCount ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Commission Transaction Count for BUY");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalComissionBuyInBetweenData countData = new GetTotalComissionBuyInBetweenData(dbConnection);
        Integer count = countData.getCommissionBuyCount(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (count != null) {
            System.out.println("Number of Commission Transactions for BUY: " + count);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Transaction Count: 0 (No commission transactions found for BUY)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getCommissionBuyCount executed successfully");
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
