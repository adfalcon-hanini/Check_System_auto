package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetTotalBuyInBetweenData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetTotalBuyInBetweenData - Testing 5 calculation methods
 */
public class GetTotalBuyInBetweenDataTest {

    private static final Logger logger = Logger.getLogger(GetTotalBuyInBetweenDataTest.class);
    private OracleDBConnection dbConnection;
    private GetTotalBuyInBetweenData totalBuyData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "MARK";
    private static final String START_DATE = "01-Nov-2025";
    private static final String END_DATE = "26-Nov-2025";
    private static final String TRNX_TYPE_BUY = "BUY";
    private static final String TRNX_TYPE_SELL = "SELL";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for total buy data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            totalBuyData = new GetTotalBuyInBetweenData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test getTotalBuyByNinAndCompanyBetweenDates method")
    public void testGetTotalBuyByNinAndCompanyBetweenDates() {
        logger.info("=== Test 1: getTotalBuyByNinAndCompanyBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Total BUY Amount By NIN, Company and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        Double totalBuy = totalBuyData.getTotalBuyByNinAndCompanyBetweenDates(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalBuy != null) {
            System.out.println("Total BUY Amount: " + totalBuy);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total BUY Amount: 0 (No BUY transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalBuyByNinAndCompanyBetweenDates executed successfully");
    }


    @Test(priority = 2, description = "Test getTotalBuyByNinAndCompany method")
    public void testGetTotalBuyByNinAndCompany() {
        logger.info("=== Test 3: getTotalBuyByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Total BUY Amount By NIN and Company (All Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetTotalBuyInBetweenData allDatesData = new GetTotalBuyInBetweenData(dbConnection);
        Double totalBuy = allDatesData.getTotalBuyByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalBuy != null) {
            System.out.println("Total BUY Amount (All Dates): " + totalBuy);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total BUY Amount: 0 (No BUY transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalBuyByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test getTotalAmountByType method")
    public void testGetTotalAmountByType() {
        logger.info("=== Test 4: getTotalAmountByType ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Total Amount By Transaction Type");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Transaction Type: " + TRNX_TYPE_BUY);

        GetTotalBuyInBetweenData typeData = new GetTotalBuyInBetweenData(dbConnection);
        Double totalAmount = typeData.getTotalAmountByType(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY, TRNX_TYPE_BUY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalAmount != null) {
            System.out.println("Total " + TRNX_TYPE_BUY + " Amount: " + totalAmount);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total Amount: 0 (No " + TRNX_TYPE_BUY + " transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalAmountByType executed successfully");
    }

    @Test(priority = 4, description = "Test getTotalBuyByNinBetweenDates method")
    public void testGetTotalBuyByNinBetweenDates() {
        logger.info("=== Test 5: getTotalBuyByNinBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Total BUY Amount By NIN (All Companies)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalBuyInBetweenData ninData = new GetTotalBuyInBetweenData(dbConnection);
        Double totalBuy = ninData.getTotalBuyByNinBetweenDates(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalBuy != null) {
            System.out.println("Total BUY Amount (All Companies): " + totalBuy);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total BUY Amount: 0 (No BUY transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalBuyByNinBetweenDates executed successfully");
    }

    @Test(priority = 5, description = "Test getTransactionCount method")
    public void testGetTransactionCount() {
        logger.info("=== Test 7: getTransactionCount ===");
        System.out.println("\n========================================");
        System.out.println("Test 7: Get Transaction Count By Type");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Transaction Type: " + TRNX_TYPE_BUY);

        GetTotalBuyInBetweenData countData = new GetTotalBuyInBetweenData(dbConnection);
        Integer count = countData.getTransactionCount(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY, TRNX_TYPE_BUY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (count != null) {
            System.out.println("Number of " + TRNX_TYPE_BUY + " Transactions: " + count);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Transaction Count: 0 (No " + TRNX_TYPE_BUY + " transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTransactionCount executed successfully");
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
