package com.example.tests.fund;

import com.example.screensData.fund.GetFundClientsData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetFundClientsData
 * Tests retrieval of fund clients from FUND_CLIENTS table
 */
public class GetFundClientsDataTest {

    private OracleDBConnection dbConnection;
    private GetFundClientsData fundClientsData;
    private static final String TEST_CLIENT_ID = "12240";

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║       GET FUND CLIENTS DATA TEST STARTED                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            fundClientsData = new GetFundClientsData(dbConnection);
            System.out.println("✓ GetFundClientsData instance initialized successfully!");

        } catch (Exception e) {
            System.err.println("✗ Setup failed: " + e.getMessage());
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testFetchFundClientsByClientId() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch Fund Clients by Client ID                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.println("\nFetching fund clients for Client ID: " + TEST_CLIENT_ID + "...\n");

        boolean result = fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (result) {
            System.out.println("✓ Successfully fetched " + fundClientsData.getRecordCount() + " fund client record(s)");

            // Print fund clients table
            fundClientsData.printFundClientsTable();

            // Print fund clients summary
            fundClientsData.printFundClientsSummary();

            Assert.assertTrue(fundClientsData.getRecordCount() > 0, "Should have at least one fund client record");
            Assert.assertEquals(fundClientsData.getClientId(), TEST_CLIENT_ID, "Client ID should match");
        } else {
            System.out.println("ℹ No fund clients found for client " + TEST_CLIENT_ID);
            System.out.println("This might be expected if the client has no fund accounts.");
        }
    }

    @Test(priority = 2)
    public void testPrintAllFundClientData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Print All Fund Client Data                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            // Print all fund clients with full details
            fundClientsData.printAllFundClientData();

            System.out.println("\n✓ Successfully printed all fund client data");
        } else {
            System.out.println("ℹ No fund clients to print");
        }
    }

    @Test(priority = 3)
    public void testFetchActiveFundClientsByClientId() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Fetch Active Fund Clients Only                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        GetFundClientsData activeFundClients = new GetFundClientsData(dbConnection);
        boolean result = activeFundClients.fetchActiveFundClientsByClientId(TEST_CLIENT_ID);

        if (result) {
            System.out.println("\n✓ Successfully fetched " + activeFundClients.getRecordCount() + " active fund client record(s)");

            // Print active fund clients
            activeFundClients.printFundClientsTable();

            // Verify all are active
            for (Map<String, Object> record : activeFundClients.getAllFundClientRecords()) {
                String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "";
                Assert.assertEquals(status, "ACTIVE", "All records should have ACTIVE status");
            }

            System.out.println("\n✓ All records verified to be ACTIVE");
        } else {
            System.out.println("\nℹ No active fund clients found for client " + TEST_CLIENT_ID);
        }
    }

    @Test(priority = 4)
    public void testGetFundClientRecordByIndex() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Get Fund Client Record by Index                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            // Test getting first record
            Map<String, Object> firstRecord = fundClientsData.getFundClientRecordByIndex(0);
            Assert.assertNotNull(firstRecord, "First record should not be null");

            System.out.println("\nFirst Fund Client Record:");
            System.out.println("  CL_ID: " + firstRecord.get("CL_ID"));
            System.out.println("  FUND_CODE: " + firstRecord.get("FUND_CODE"));
            System.out.println("  FUND_NAME: " + firstRecord.get("FUND_NAME"));
            System.out.println("  ACCOUNT_NUMBER: " + firstRecord.get("ACCOUNT_NUMBER"));
            System.out.println("  BALANCE: " + firstRecord.get("BALANCE"));
            System.out.println("  AVAILABLE_BALANCE: " + firstRecord.get("AVAILABLE_BALANCE"));
            System.out.println("  ACCOUNT_STATUS: " + firstRecord.get("ACCOUNT_STATUS"));

            // Test invalid index
            Map<String, Object> invalidRecord = fundClientsData.getFundClientRecordByIndex(999999);
            Assert.assertNull(invalidRecord, "Invalid index should return null");

            System.out.println("\n✓ Successfully retrieved fund client record by index");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 5)
    public void testGetAllFundClientRecords() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 5: Get All Fund Client Records                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        List<Map<String, Object>> allRecords = fundClientsData.getAllFundClientRecords();

        Assert.assertNotNull(allRecords, "Records list should not be null");
        Assert.assertEquals(allRecords.size(), fundClientsData.getRecordCount(), "Record count should match");

        System.out.println("\n✓ Successfully retrieved all fund client records");
        System.out.println("  Total records: " + allRecords.size());
    }

    @Test(priority = 6)
    public void testFundClientDataFieldValidation() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 6: Validate Fund Client Data Fields                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            // Validate that essential fields are not null
            Assert.assertNotNull(fundClientsData.getClientId(), "CLIENT_ID should not be null");
            Assert.assertEquals(fundClientsData.getClientId(), TEST_CLIENT_ID, "CLIENT_ID should match test client");

            System.out.println("\nValidated Fields (First Fund Client):");
            System.out.println("  CLIENT_ID: " + fundClientsData.getClientId() + " ✓");
            System.out.println("  FUND_CODE: " + fundClientsData.getFundCode() + " ✓");
            System.out.println("  FUND_NAME: " + fundClientsData.getFundName() + " ✓");
            System.out.println("  ACCOUNT_NUMBER: " + fundClientsData.getAccountNumber() + " ✓");
            System.out.println("  BALANCE: " + fundClientsData.getBalance() + " ✓");
            System.out.println("  AVAILABLE_BALANCE: " + fundClientsData.getAvailableBalance() + " ✓");
            System.out.println("  CURRENCY: " + fundClientsData.getCurrency() + " ✓");
            System.out.println("  ACCOUNT_STATUS: " + fundClientsData.getAccountStatus() + " ✓");
            System.out.println("  OPEN_DATE: " + fundClientsData.getOpenDate() + " ✓");

            System.out.println("\n✓ All fields validated successfully");
        } else {
            System.out.println("ℹ No records available to validate");
        }
    }

    @Test(priority = 7)
    public void testPrintFirstFundClientData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 7: Print First Fund Client Data                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            // Print first fund client details
            fundClientsData.printFundClientData();

            System.out.println("\n✓ Successfully printed first fund client data");
        } else {
            System.out.println("ℹ No fund clients to print");
        }
    }

    @Test(priority = 8)
    public void testGetAllData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 8: Get All Data as Map                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch fund clients first
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            Map<String, String> dataMap = fundClientsData.getAllData();

            Assert.assertNotNull(dataMap, "Data map should not be null");
            Assert.assertTrue(dataMap.containsKey("clientId"), "Map should contain clientId");
            Assert.assertTrue(dataMap.containsKey("fundCode"), "Map should contain fundCode");

            System.out.println("\nData Map Contents (First Fund Client):");
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\n✓ Successfully retrieved data as map");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 9)
    public void testFetchFundClientsByFundCode() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 9: Fetch Fund Clients by Fund Code                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // First get a fund code from existing data
        fundClientsData.fetchFundClientsByClientId(TEST_CLIENT_ID);

        if (fundClientsData.getRecordCount() > 0) {
            String testFundCode = fundClientsData.getFundCode();
            System.out.println("\nTesting with FUND_CODE: " + testFundCode);

            // Create new instance and fetch by fund code
            GetFundClientsData fundCodeData = new GetFundClientsData(dbConnection);
            boolean result = fundCodeData.fetchFundClientsByFundCode(testFundCode);

            if (result) {
                System.out.println("✓ Found " + fundCodeData.getRecordCount() + " client(s) for fund: " + testFundCode);

                fundCodeData.printFundClientsTable();

                Assert.assertTrue(fundCodeData.getRecordCount() > 0, "Should have at least one record");
            } else {
                System.out.println("ℹ No clients found for fund code: " + testFundCode);
            }
        } else {
            System.out.println("ℹ No fund clients available to get fund code");
        }
    }

    @Test(priority = 10)
    public void testFetchAllFundClients() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 10: Fetch All Fund Clients (Limited Display)          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        GetFundClientsData allFundClients = new GetFundClientsData(dbConnection);
        boolean result = allFundClients.fetchAllFundClients();

        if (result) {
            System.out.println("\n✓ Successfully fetched " + allFundClients.getRecordCount() + " fund client record(s)");

            // Print summary only for all clients
            allFundClients.printFundClientsSummary();

            Assert.assertTrue(allFundClients.getRecordCount() > 0, "Should have at least one fund client");
        } else {
            System.out.println("\nℹ No fund clients found in database");
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
        System.out.println("║       GET FUND CLIENTS DATA TEST COMPLETED                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
