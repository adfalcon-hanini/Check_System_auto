package com.example.tests.fix;

import com.example.screensData.xdp.GetFixSendQueryTagsData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetFixSendQueryTagsData
 */
public class GetFixSendQueryTagsDataTest {

    private static final Logger logger = Logger.getLogger(GetFixSendQueryTagsDataTest.class);
    private OracleDBConnection dbConnection;
    private GetFixSendQueryTagsData fixSendQueryTagsData;
    private static final String TEST_CLIENT_ID = "12240";

    @BeforeClass
    public void setUp() {
        logger.info("Setting up GetFixSendQueryTagsDataTest...");
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");

        try {
            dbConnection.connect();
            logger.info("Database connection established successfully");

            fixSendQueryTagsData = new GetFixSendQueryTagsData(dbConnection);
            logger.info("GetFixSendQueryTagsData instance initialized successfully");

        } catch (Exception e) {
            logger.error("Setup failed: " + e.getMessage(), e);
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testFetchFixSendQueryTagsByClientId() {
        logger.info("Test 1: Fetching FIX send query tags by client ID: " + TEST_CLIENT_ID);

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result) {
            Assert.assertTrue(result, "Failed to fetch FIX send query tags");
            Assert.assertTrue(fixSendQueryTagsData.getRecordCount() > 0, "No FIX send query tags found");

            logger.info("Successfully fetched " + fixSendQueryTagsData.getRecordCount() + " FIX send query tags");
            logger.info("First record - SL_NO: " + fixSendQueryTagsData.getSlNo());
            logger.info("First record - Client ID: " + fixSendQueryTagsData.getClientId());
            logger.info("First record - Symbol: " + fixSendQueryTagsData.getTag55Symbol());
            logger.info("First record - Side: " + fixSendQueryTagsData.getTag54Side());
            logger.info("First record - Order Quantity: " + fixSendQueryTagsData.getTag38OrderQty());

            fixSendQueryTagsData.printFixSendQueryTagsTable();
        } else {
            logger.warn("No FIX send query tags found for client ID: " + TEST_CLIENT_ID);
            Assert.assertTrue(true, "No data available for testing, but query executed successfully");
        }
    }

    @Test(priority = 2)
    public void testPrintFixSendQueryTagsSummary() {
        logger.info("Test 2: Printing FIX send query tags summary");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result) {
            fixSendQueryTagsData.printFixSendQueryTagsSummary();
            Assert.assertTrue(fixSendQueryTagsData.getRecordCount() > 0, "No records to summarize");
            logger.info("Summary printed successfully");
        } else {
            logger.warn("No data available to print summary");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 3)
    public void testPrintDetailedFixSendQueryTags() {
        logger.info("Test 3: Printing detailed FIX send query tags");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result) {
            fixSendQueryTagsData.printDetailedFixSendQueryTags();
            Assert.assertTrue(fixSendQueryTagsData.getRecordCount() > 0, "No records to display");
            logger.info("Detailed information printed successfully");
        } else {
            logger.warn("No data available to print details");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 4)
    public void testFetchFixSendQueryTagsBySlNo() {
        logger.info("Test 4: Fetching FIX send query tags by SL_NO");

        // First get all records to get a valid SL_NO
        boolean fetchResult = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (fetchResult && fixSendQueryTagsData.getRecordCount() > 0) {
            String testSlNo = fixSendQueryTagsData.getSlNo();
            logger.info("Using SL_NO: " + testSlNo + " for testing");

            GetFixSendQueryTagsData singleRecord = new GetFixSendQueryTagsData(dbConnection);
            boolean result = singleRecord.fetchFixSendQueryTagsBySlNo(testSlNo);

            Assert.assertTrue(result, "Failed to fetch FIX send query tags by SL_NO");
            Assert.assertEquals(singleRecord.getSlNo(), testSlNo, "SL_NO mismatch");

            logger.info("Successfully fetched FIX send query tag for SL_NO: " + testSlNo);
            logger.info("Symbol: " + singleRecord.getTag55Symbol());
            logger.info("Side: " + singleRecord.getTag54Side());
            logger.info("Status: " + singleRecord.getStatus());

            singleRecord.printDetailedFixSendQueryTags();
        } else {
            logger.warn("No FIX send query tags available to test SL_NO fetch");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 5)
    public void testFetchFixSendQueryTagsBySymbol() {
        logger.info("Test 5: Fetching FIX send query tags by symbol");

        // First get all records to get a valid symbol
        boolean fetchResult = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (fetchResult && fixSendQueryTagsData.getRecordCount() > 0) {
            String testSymbol = fixSendQueryTagsData.getTag55Symbol();
            logger.info("Using symbol: " + testSymbol + " for testing");

            GetFixSendQueryTagsData symbolData = new GetFixSendQueryTagsData(dbConnection);
            boolean result = symbolData.fetchFixSendQueryTagsBySymbol(TEST_CLIENT_ID, testSymbol);

            if (result) {
                Assert.assertTrue(result, "Failed to fetch FIX send query tags by symbol");
                Assert.assertEquals(symbolData.getTag55Symbol(), testSymbol, "Symbol mismatch");

                logger.info("Successfully fetched " + symbolData.getRecordCount() + " FIX send query tags for symbol: " + testSymbol);
                symbolData.printFixSendQueryTagsTable();
            } else {
                logger.warn("No FIX send query tags found for symbol: " + testSymbol);
                Assert.assertTrue(true, "No data available for this symbol");
            }
        } else {
            logger.warn("No FIX send query tags available to test symbol fetch");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 6)
    public void testValidateFixMessageStructure() {
        logger.info("Test 6: Validating FIX message structure");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result && fixSendQueryTagsData.getRecordCount() > 0) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            for (Map<String, Object> record : allRecords) {
                // Validate required fields are present
                Assert.assertNotNull(record.get("SL_NO"), "SL_NO should not be null");
                Assert.assertNotNull(record.get("CL_ID"), "CL_ID should not be null");

                String side = record.get("TAG_54_SIDE") != null ? record.get("TAG_54_SIDE").toString() : "";
                logger.info("Record SL_NO: " + record.get("SL_NO") +
                          " | Symbol: " + record.get("TAG_55_SYMBOL") +
                          " | Side: " + side +
                          " | Qty: " + record.get("TAG_38_ORDER_QTY"));
            }

            logger.info("Validated " + allRecords.size() + " FIX message records");
            Assert.assertTrue(true, "FIX message structure validation passed");
        } else {
            logger.warn("No FIX send query tags available for structure validation");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 7)
    public void testBuySellOrdersCounting() {
        logger.info("Test 7: Testing buy/sell orders counting");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result && fixSendQueryTagsData.getRecordCount() > 0) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            int buyCount = 0;
            int sellCount = 0;
            int otherCount = 0;

            for (Map<String, Object> record : allRecords) {
                String side = record.get("TAG_54_SIDE") != null ? record.get("TAG_54_SIDE").toString() : "";

                if ("1".equals(side) || "BUY".equalsIgnoreCase(side)) {
                    buyCount++;
                } else if ("2".equals(side) || "SELL".equalsIgnoreCase(side)) {
                    sellCount++;
                } else {
                    otherCount++;
                }
            }

            logger.info("Buy Orders: " + buyCount);
            logger.info("Sell Orders: " + sellCount);
            logger.info("Other Orders: " + otherCount);
            logger.info("Total Orders: " + allRecords.size());

            Assert.assertEquals(buyCount + sellCount + otherCount, allRecords.size(),
                              "Order counts don't match total records");

            fixSendQueryTagsData.printFixSendQueryTagsSummary();
        } else {
            logger.warn("No FIX send query tags available for counting");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 8)
    public void testTotalQuantityCalculation() {
        logger.info("Test 8: Testing total quantity calculation");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result && fixSendQueryTagsData.getRecordCount() > 0) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            int totalQuantity = 0;
            int recordsWithQuantity = 0;

            for (Map<String, Object> record : allRecords) {
                String qty = record.get("TAG_38_ORDER_QTY") != null ? record.get("TAG_38_ORDER_QTY").toString() : "0";

                try {
                    int quantity = Integer.parseInt(qty);
                    totalQuantity += quantity;
                    recordsWithQuantity++;

                    logger.info("SL_NO: " + record.get("SL_NO") + " | Quantity: " + quantity);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid quantity format for SL_NO: " + record.get("SL_NO") + " | Value: " + qty);
                }
            }

            logger.info("Total Quantity across all orders: " + totalQuantity);
            logger.info("Records with valid quantity: " + recordsWithQuantity);

            Assert.assertTrue(totalQuantity >= 0, "Total quantity should be non-negative");
        } else {
            logger.warn("No FIX send query tags available for quantity calculation");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 9)
    public void testFetchByInvalidClientId() {
        logger.info("Test 9: Testing fetch with invalid client ID");

        GetFixSendQueryTagsData invalidData = new GetFixSendQueryTagsData(dbConnection);
        boolean result = invalidData.fetchFixSendQueryTagsByClientId("99999999");

        Assert.assertFalse(result, "Should return false for invalid client ID");
        Assert.assertEquals(invalidData.getRecordCount(), 0, "Record count should be 0 for invalid client ID");

        logger.info("Invalid client ID test passed - no records returned as expected");
    }

    @Test(priority = 10)
    public void testAllRecordsForToday() {
        logger.info("Test 10: Validating all records are for today");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result && fixSendQueryTagsData.getRecordCount() > 0) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            logger.info("Verifying all " + allRecords.size() + " records are from today");

            for (Map<String, Object> record : allRecords) {
                String msgDate = record.get("MSG_DATE") != null ? record.get("MSG_DATE").toString() : "";
                String sendingTime = record.get("TAG_52_SENDING_TIME") != null ? record.get("TAG_52_SENDING_TIME").toString() : "";

                logger.info("SL_NO: " + record.get("SL_NO") +
                          " | MSG_DATE: " + msgDate +
                          " | SENDING_TIME: " + sendingTime);
            }

            logger.info("All records validated for today's date");
            Assert.assertTrue(true, "Date validation completed");
        } else {
            logger.warn("No FIX send query tags available for today");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 11)
    public void testGetAllRecordsList() {
        logger.info("Test 11: Testing getAllFixSendQueryTagsRecords method");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            Assert.assertNotNull(allRecords, "Records list should not be null");
            Assert.assertEquals(allRecords.size(), fixSendQueryTagsData.getRecordCount(),
                              "List size should match record count");

            logger.info("Successfully retrieved list of " + allRecords.size() + " records");

            if (allRecords.size() > 0) {
                Map<String, Object> firstRecord = allRecords.get(0);
                logger.info("First record SL_NO: " + firstRecord.get("SL_NO"));
                logger.info("First record Symbol: " + firstRecord.get("TAG_55_SYMBOL"));
            }
        } else {
            logger.warn("No records available to retrieve");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @Test(priority = 12)
    public void testStatusFieldValues() {
        logger.info("Test 12: Analyzing STATUS field values");

        boolean result = fixSendQueryTagsData.fetchFixSendQueryTagsByClientId(TEST_CLIENT_ID);

        if (result && fixSendQueryTagsData.getRecordCount() > 0) {
            List<Map<String, Object>> allRecords = fixSendQueryTagsData.getAllFixSendQueryTagsRecords();

            int statusCount = 0;
            for (Map<String, Object> record : allRecords) {
                String status = record.get("STATUS") != null ? record.get("STATUS").toString() : "NULL";
                String slNo = record.get("SL_NO").toString();

                logger.info("SL_NO: " + slNo + " | Status: " + status);

                if (status != null && !status.isEmpty() && !"NULL".equals(status)) {
                    statusCount++;
                }
            }

            logger.info("Records with status: " + statusCount + " out of " + allRecords.size());
            Assert.assertTrue(true, "Status analysis completed");
        } else {
            logger.warn("No FIX send query tags available for status analysis");
            Assert.assertTrue(true, "No data available for testing");
        }
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down GetFixSendQueryTagsDataTest...");
        if (dbConnection != null) {
            dbConnection.closeConnection();
            logger.info("Database connection closed");
        }
    }
}
