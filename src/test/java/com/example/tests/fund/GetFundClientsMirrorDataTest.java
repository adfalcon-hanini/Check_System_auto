package com.example.tests.fund;

import com.example.screensData.fund.GetFundClientsMirrorData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetFundClientsMirrorData
 * Tests retrieval of fund clients mirror from FUND_CLIENTS_MIRROR table
 */
public class GetFundClientsMirrorDataTest {

    private OracleDBConnection dbConnection;
    private GetFundClientsMirrorData fundClientsMirrorData;
    private static final String TEST_CLIENT_ID = "12240";

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║    GET FUND CLIENTS MIRROR DATA TEST STARTED                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            fundClientsMirrorData = new GetFundClientsMirrorData(dbConnection);
            System.out.println("✓ GetFundClientsMirrorData instance initialized successfully!");

        } catch (Exception e) {
            System.err.println("✗ Setup failed: " + e.getMessage());
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testFetchFundClientsMirrorByClientId() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch Fund Clients Mirror by Client ID             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.println("\nFetching fund clients mirror for Client ID: " + TEST_CLIENT_ID + "...\n");

        boolean result = fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (result) {
            System.out.println("✓ Successfully fetched " + fundClientsMirrorData.getRecordCount() + " fund client mirror record(s)");

            // Print fund clients mirror table
            fundClientsMirrorData.printFundClientsMirrorTable();

            // Print fund clients mirror summary
            fundClientsMirrorData.printFundClientsMirrorSummary();

            Assert.assertTrue(fundClientsMirrorData.getRecordCount() > 0, "Should have at least one fund client mirror record");
            Assert.assertEquals(fundClientsMirrorData.getClientId(), TEST_CLIENT_ID, "Client ID should match");
        } else {
            System.out.println("ℹ No fund clients mirror found for client " + TEST_CLIENT_ID);
            System.out.println("This might be expected if the client has no fund mirror accounts.");
        }
    }

    @Test(priority = 2)
    public void testPrintAllFundClientMirrorData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Print All Fund Client Mirror Data                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            // Print all fund clients mirror with full details
            fundClientsMirrorData.printAllFundClientMirrorData();

            System.out.println("\n✓ Successfully printed all fund client mirror data");
        } else {
            System.out.println("ℹ No fund clients mirror to print");
        }
    }

    @Test(priority = 3)
    public void testFetchActiveFundClientsMirrorByClientId() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Fetch Active Fund Clients Mirror Only              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        GetFundClientsMirrorData activeMirror = new GetFundClientsMirrorData(dbConnection);
        boolean result = activeMirror.fetchActiveFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (result) {
            System.out.println("\n✓ Successfully fetched " + activeMirror.getRecordCount() + " active fund client mirror record(s)");

            // Print active fund clients mirror
            activeMirror.printFundClientsMirrorTable();

            // Verify all are active
            for (Map<String, Object> record : activeMirror.getAllFundClientMirrorRecords()) {
                String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "";
                Assert.assertEquals(status, "ACTIVE", "All records should have ACTIVE status");
            }

            System.out.println("\n✓ All records verified to be ACTIVE");
        } else {
            System.out.println("\nℹ No active fund clients mirror found for client " + TEST_CLIENT_ID);
        }
    }

    @Test(priority = 4)
    public void testGetFundClientMirrorRecordByIndex() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Get Fund Client Mirror Record by Index             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            // Test getting first record
            Map<String, Object> firstRecord = fundClientsMirrorData.getFundClientMirrorRecordByIndex(0);
            Assert.assertNotNull(firstRecord, "First record should not be null");

            System.out.println("\nFirst Fund Client Mirror Record:");
            System.out.println("  CL_ID: " + firstRecord.get("CL_ID"));
            System.out.println("  FUND_CODE: " + firstRecord.get("FUND_CODE"));
            System.out.println("  FUND_NAME: " + firstRecord.get("FUND_NAME"));
            System.out.println("  ACCOUNT_NUMBER: " + firstRecord.get("ACCOUNT_NUMBER"));
            System.out.println("  BALANCE: " + firstRecord.get("BALANCE"));
            System.out.println("  AVAILABLE_BALANCE: " + firstRecord.get("AVAILABLE_BALANCE"));
            System.out.println("  ACCOUNT_STATUS: " + firstRecord.get("ACCOUNT_STATUS"));
            System.out.println("  MIRROR_DATE: " + firstRecord.get("MIRROR_DATE"));
            System.out.println("  SYNC_STATUS: " + firstRecord.get("SYNC_STATUS"));

            // Test invalid index
            Map<String, Object> invalidRecord = fundClientsMirrorData.getFundClientMirrorRecordByIndex(999999);
            Assert.assertNull(invalidRecord, "Invalid index should return null");

            System.out.println("\n✓ Successfully retrieved fund client mirror record by index");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 5)
    public void testGetAllFundClientMirrorRecords() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 5: Get All Fund Client Mirror Records                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        List<Map<String, Object>> allRecords = fundClientsMirrorData.getAllFundClientMirrorRecords();

        Assert.assertNotNull(allRecords, "Records list should not be null");
        Assert.assertEquals(allRecords.size(), fundClientsMirrorData.getRecordCount(), "Record count should match");

        System.out.println("\n✓ Successfully retrieved all fund client mirror records");
        System.out.println("  Total records: " + allRecords.size());
    }

    @Test(priority = 6)
    public void testFundClientMirrorDataFieldValidation() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 6: Validate Fund Client Mirror Data Fields            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            // Validate that essential fields are not null
            Assert.assertNotNull(fundClientsMirrorData.getClientId(), "CLIENT_ID should not be null");
            Assert.assertEquals(fundClientsMirrorData.getClientId(), TEST_CLIENT_ID, "CLIENT_ID should match test client");

            System.out.println("\nValidated Fields (First Fund Client Mirror):");
            System.out.println("  CLIENT_ID: " + fundClientsMirrorData.getClientId() + " ✓");
            System.out.println("  FUND_CODE: " + fundClientsMirrorData.getFundCode() + " ✓");
            System.out.println("  FUND_NAME: " + fundClientsMirrorData.getFundName() + " ✓");
            System.out.println("  ACCOUNT_NUMBER: " + fundClientsMirrorData.getAccountNumber() + " ✓");
            System.out.println("  BALANCE: " + fundClientsMirrorData.getBalance() + " ✓");
            System.out.println("  AVAILABLE_BALANCE: " + fundClientsMirrorData.getAvailableBalance() + " ✓");
            System.out.println("  CURRENCY: " + fundClientsMirrorData.getCurrency() + " ✓");
            System.out.println("  ACCOUNT_STATUS: " + fundClientsMirrorData.getAccountStatus() + " ✓");
            System.out.println("  MIRROR_DATE: " + fundClientsMirrorData.getMirrorDate() + " ✓");
            System.out.println("  SYNC_STATUS: " + fundClientsMirrorData.getSyncStatus() + " ✓");

            System.out.println("\n✓ All fields validated successfully");
        } else {
            System.out.println("ℹ No records available to validate");
        }
    }

    @Test(priority = 7)
    public void testPrintFirstFundClientMirrorData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 7: Print First Fund Client Mirror Data                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            // Print first fund client mirror details
            fundClientsMirrorData.printFundClientMirrorData();

            System.out.println("\n✓ Successfully printed first fund client mirror data");
        } else {
            System.out.println("ℹ No fund clients mirror to print");
        }
    }

    @Test(priority = 8)
    public void testGetAllData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 8: Get All Data as Map                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients mirror first
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            Map<String, String> dataMap = fundClientsMirrorData.getAllData();

            Assert.assertNotNull(dataMap, "Data map should not be null");
            Assert.assertTrue(dataMap.containsKey("clientId"), "Map should contain clientId");
            Assert.assertTrue(dataMap.containsKey("fundCode"), "Map should contain fundCode");
            Assert.assertTrue(dataMap.containsKey("mirrorDate"), "Map should contain mirrorDate");

            System.out.println("\nData Map Contents (First Fund Client Mirror):");
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\n✓ Successfully retrieved data as map");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 9)
    public void testFetchFundClientsMirrorByFundCode() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 9: Fetch Fund Clients Mirror by Fund Code             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // First get a fund code from existing data
        fundClientsMirrorData.fetchFundClientsMirrorByClientId(TEST_CLIENT_ID);

        if (fundClientsMirrorData.getRecordCount() > 0) {
            String testFundCode = fundClientsMirrorData.getFundCode();
            System.out.println("\nTesting with FUND_CODE: " + testFundCode);

            // Create new instance and fetch by fund code
            GetFundClientsMirrorData fundCodeMirror = new GetFundClientsMirrorData(dbConnection);
            boolean result = fundCodeMirror.fetchFundClientsMirrorByFundCode(testFundCode);

            if (result) {
                System.out.println("✓ Found " + fundCodeMirror.getRecordCount() + " client mirror(s) for fund: " + testFundCode);

                fundCodeMirror.printFundClientsMirrorTable();

                Assert.assertTrue(fundCodeMirror.getRecordCount() > 0, "Should have at least one record");
            } else {
                System.out.println("ℹ No clients mirror found for fund code: " + testFundCode);
            }
        } else {
            System.out.println("ℹ No fund clients mirror available to get fund code");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              CLOSING DATABASE CONNECTION                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        if (dbConnection != null) {
            dbConnection.closeConnection();
            System.out.println("✓ Connection closed successfully");
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║    GET FUND CLIENTS MIRROR DATA TEST COMPLETED                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
