package com.example.factory;

import com.example.screensData.clients.GetCashData;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Parameterized test class for Cash Data testing
 * Each instance tests different NIN and date combinations
 */
@Epic("Database Testing")
@Feature("Cash Data Retrieval")
public class ParameterizedCashTest {

    private static final Logger logger = Logger.getLogger(ParameterizedCashTest.class);
    private OracleDBConnection dbConnection;
    private GetCashData cashData;
    private final TestDataProvider testData;

    public ParameterizedCashTest(TestDataProvider testData) {
        this.testData = testData;
    }

    @BeforeClass
    public void setup() {
        logger.info("Setting up test: " + testData.getTestName());
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Test Instance: " + testData.getTestName());
        System.out.println("NIN: " + testData.getNin());

        if (testData.getDate() != null) {
            System.out.println("Date: " + testData.getDate());
        }

        if (testData.getStartDate() != null && testData.getEndDate() != null) {
            System.out.println("Date Range: " + testData.getStartDate() + " to " + testData.getEndDate());
        }

        System.out.println("=".repeat(80));

        try {
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            cashData = new GetCashData(dbConnection);
            logger.info("Database connection established");
        } catch (Exception e) {
            logger.error("Failed to establish database connection", e);
            Assert.fail("Database connection failed");
        }
    }

    @Test(priority = 1)
    @Story("Fetch Cash Data by NIN and Date")
    @Description("Verify cash data can be fetched for NIN and specific date")
    public void testFetchCashDataByNinAndDate() {
        if (testData.getDate() == null) {
            System.out.println("⚠ Skipping - No date specified for this test");
            return;
        }

        logger.info("Testing fetchClientBalanceByNinAndDate");
        System.out.println("\n--- Test: Fetch Cash Data by NIN and Date ---");
        System.out.println("Parameters: NIN=" + testData.getNin() + ", Date=" + testData.getDate());

        boolean success = cashData.fetchClientBalanceByNinAndDate(
            testData.getNin(),
            testData.getDate()
        );

        System.out.println("Result: " + (success ? "SUCCESS" : "NO DATA FOUND"));

        if (testData.getExpectedResult().equals("success")) {
            Assert.assertTrue(success, "Expected to find cash data");
            System.out.println("✓ Cash data retrieved successfully");
        }
    }

    @Test(priority = 2)
    @Story("Fetch Cash Data by NIN and Date Range")
    @Description("Verify cash data can be fetched for NIN within date range")
    public void testFetchCashDataByNinAndDateRange() {
        if (testData.getStartDate() == null || testData.getEndDate() == null) {
            System.out.println("⚠ Skipping - No date range specified for this test");
            return;
        }

        logger.info("Testing fetchClientBalanceByNinAndDateRange");
        System.out.println("\n--- Test: Fetch Cash Data by NIN and Date Range ---");
        System.out.println("Parameters: NIN=" + testData.getNin() +
                         ", Range=" + testData.getStartDate() + " to " + testData.getEndDate());

        GetCashData rangeData = new GetCashData(dbConnection);
        boolean success = rangeData.fetchClientBalanceByNinAndDateRange(
            testData.getNin(),
            testData.getStartDate(),
            testData.getEndDate()
        );

        System.out.println("Result: " + (success ? "SUCCESS" : "NO DATA FOUND"));

        if (testData.getExpectedResult().equals("success")) {
            Assert.assertTrue(success, "Expected to find cash data in date range");
            System.out.println("✓ Cash data retrieved successfully for date range");
        }
    }

    @Test(priority = 3)
    @Story("Fetch Latest Cash Data")
    @Description("Verify latest cash data can be fetched for NIN")
    public void testFetchLatestCashData() {
        logger.info("Testing fetchLatestClientBalanceByNin");
        System.out.println("\n--- Test: Fetch Latest Cash Data ---");
        System.out.println("Parameters: NIN=" + testData.getNin());

        GetCashData latestData = new GetCashData(dbConnection);
        boolean success = latestData.fetchLatestClientBalanceByNin(testData.getNin());

        System.out.println("Result: " + (success ? "SUCCESS" : "NO DATA FOUND"));

        if (success) {
            System.out.println("✓ Latest cash data retrieved successfully");
        } else {
            System.out.println("⚠ No latest cash data found");
        }
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down test: " + testData.getTestName());
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Completing test: " + testData.getTestName());

        if (dbConnection != null) {
            dbConnection.closeConnection();
            logger.info("Database connection closed");
        }

        System.out.println("=".repeat(80) + "\n");
    }
}
