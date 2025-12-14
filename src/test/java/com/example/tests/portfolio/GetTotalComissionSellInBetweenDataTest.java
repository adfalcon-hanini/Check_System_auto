package com.example.tests.portfolio;

import com.example.screensData.mcalc.GetTotalComissionSellInBetweenData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test class for GetTotalComissionSellInBetweenData - Testing 5 calculation methods
 */
public class GetTotalComissionSellInBetweenDataTest {

    private static final Logger logger = Logger.getLogger(GetTotalComissionSellInBetweenDataTest.class);
    private OracleDBConnection dbConnection;
    private GetTotalComissionSellInBetweenData totalSellData;

    private static final String TARGET_NIN = "12240";
    private static final String TARGET_COMPANY = "QNBK";
    private static final String START_DATE = "01-Jan-2025";
    private static final String END_DATE = "25-Nov-2025";
    private static final String TRNX_TYPE_SELL = "SELL";
    private static final String TRNX_TYPE_BUY = "BUY";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for total sell data tests");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            totalSellData = new GetTotalComissionSellInBetweenData(dbConnection);
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Test getTotalSellByNinAndCompanyBetweenDates method")
    public void testGetTotalSellByNinAndCompanyBetweenDates() {
        logger.info("=== Test 1: getTotalSellByNinAndCompanyBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 1: Get Total SELL Amount By NIN, Company and Date Range");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        Double totalSell = totalSellData.getTotalSellByNinAndCompanyBetweenDates(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalSell != null) {
            System.out.println("Total SELL Amount: " + totalSell);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total SELL Amount: 0 (No SELL transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalSellByNinAndCompanyBetweenDates executed successfully");
    }

    @Test(priority = 2, description = "Test getTotalSellByNinAndCompany method")
    public void testGetTotalSellByNinAndCompany() {
        logger.info("=== Test 2: getTotalSellByNinAndCompany ===");
        System.out.println("\n========================================");
        System.out.println("Test 2: Get Total SELL Amount By NIN and Company (All Dates)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);

        GetTotalComissionSellInBetweenData allDatesData = new GetTotalComissionSellInBetweenData(dbConnection);
        Double totalSell = allDatesData.getTotalSellByNinAndCompany(TARGET_NIN, TARGET_COMPANY);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalSell != null) {
            System.out.println("Total SELL Amount (All Dates): " + totalSell);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total SELL Amount: 0 (No SELL transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalSellByNinAndCompany executed successfully");
    }

    @Test(priority = 3, description = "Test getTotalAmountByType method")
    public void testGetTotalAmountByType() {
        logger.info("=== Test 3: getTotalAmountByType ===");
        System.out.println("\n========================================");
        System.out.println("Test 3: Get Total Amount By Transaction Type");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Transaction Type: " + TRNX_TYPE_SELL);

        GetTotalComissionSellInBetweenData typeData = new GetTotalComissionSellInBetweenData(dbConnection);
        Double totalAmount = typeData.getTotalAmountByType(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY, TRNX_TYPE_SELL);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalAmount != null) {
            System.out.println("Total " + TRNX_TYPE_SELL + " Amount: " + totalAmount);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total Amount: 0 (No " + TRNX_TYPE_SELL + " transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalAmountByType executed successfully");
    }

    @Test(priority = 4, description = "Test getTotalSellByNinBetweenDates method")
    public void testGetTotalSellByNinBetweenDates() {
        logger.info("=== Test 4: getTotalSellByNinBetweenDates ===");
        System.out.println("\n========================================");
        System.out.println("Test 4: Get Total SELL Amount By NIN (All Companies)");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);

        GetTotalComissionSellInBetweenData ninData = new GetTotalComissionSellInBetweenData(dbConnection);
        Double totalSell = ninData.getTotalSellByNinBetweenDates(TARGET_NIN, START_DATE, END_DATE);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (totalSell != null) {
            System.out.println("Total SELL Amount (All Companies): " + totalSell);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Total SELL Amount: 0 (No SELL transactions found)");
            System.out.println("Status: No data found");
        }
        System.out.println("=".repeat(100));
        System.out.println("========================================\n");

        logger.info("✓ getTotalSellByNinBetweenDates executed successfully");
    }

    @Test(priority = 5, description = "Test getTransactionCount method")
    public void testGetTransactionCount() {
        logger.info("=== Test 5: getTransactionCount ===");
        System.out.println("\n========================================");
        System.out.println("Test 5: Get Transaction Count By Type");
        System.out.println("========================================");
        System.out.println("Parameters:");
        System.out.println("  - NIN: " + TARGET_NIN);
        System.out.println("  - Company Code: " + TARGET_COMPANY);
        System.out.println("  - Start Date: " + START_DATE);
        System.out.println("  - End Date: " + END_DATE);
        System.out.println("  - Transaction Type: " + TRNX_TYPE_SELL);

        GetTotalComissionSellInBetweenData countData = new GetTotalComissionSellInBetweenData(dbConnection);
        Integer count = countData.getTransactionCount(TARGET_NIN, START_DATE, END_DATE, TARGET_COMPANY, TRNX_TYPE_SELL);

        System.out.println("\n" + "=".repeat(100));
        System.out.println("RESULT");
        System.out.println("=".repeat(100));
        if (count != null) {
            System.out.println("Number of " + TRNX_TYPE_SELL + " Transactions: " + count);
            System.out.println("Status: SUCCESS - Data found");
        } else {
            System.out.println("Transaction Count: 0 (No " + TRNX_TYPE_SELL + " transactions found)");
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
