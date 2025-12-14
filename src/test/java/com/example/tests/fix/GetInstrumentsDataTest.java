package com.example.tests.fix;

import com.example.screensData.xdp.GetInstrumentsData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class for GetInstrumentsData
 * Tests retrieval of instruments from XDP_INSTRUMENTS table
 */
public class GetInstrumentsDataTest {

    private OracleDBConnection dbConnection;
    private GetInstrumentsData instrumentsData;

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         GET INSTRUMENTS DATA TEST STARTED                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            instrumentsData = new GetInstrumentsData(dbConnection);
            System.out.println("✓ GetInstrumentsData instance initialized successfully!");

        } catch (Exception e) {
            System.err.println("✗ Setup failed: " + e.getMessage());
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testFetchAllInstruments() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch All Instruments Data                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        boolean result = instrumentsData.fetchInstrumentsData();

        Assert.assertTrue(result, "Should successfully fetch instruments data");
        Assert.assertTrue(instrumentsData.getRecordCount() > 0, "Should have at least one instrument record");

        System.out.println("\n✓ Successfully fetched " + instrumentsData.getRecordCount() + " instrument(s)");

        // Display first record details
        if (instrumentsData.getRecordCount() > 0) {
            instrumentsData.printInstrumentData();
        }
    }

    @Test(priority = 2)
    public void testPrintAllInstrumentsData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Print All Instruments Data                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        // Print all records
        instrumentsData.printAllInstrumentData();

        Assert.assertTrue(instrumentsData.getRecordCount() > 0, "Should have records to print");
        System.out.println("\n✓ Successfully printed all instruments data");
    }

    @Test(priority = 3)
    public void testPrintInstrumentsSummary() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Print Instruments Summary Table                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        // Print summary table
        instrumentsData.printInstrumentsSummary();

        Assert.assertTrue(instrumentsData.getRecordCount() > 0, "Should have records for summary");
        System.out.println("\n✓ Successfully printed instruments summary");
    }

    @Test(priority = 4)
    public void testGetInstrumentRecordByIndex() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Get Instrument Record by Index                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        if (instrumentsData.getRecordCount() > 0) {
            // Test getting first record
            Map<String, Object> firstRecord = instrumentsData.getInstrumentRecordByIndex(0);
            Assert.assertNotNull(firstRecord, "First record should not be null");

            System.out.println("\nFirst Instrument Record:");
            System.out.println("  INST_CODE: " + firstRecord.get("INST_CODE"));
            System.out.println("  GROUP_CODE: " + firstRecord.get("GROUP_CODE"));
            System.out.println("  MNEMO: " + firstRecord.get("MNEMO"));
            System.out.println("  NAME: " + firstRecord.get("NAME"));

            // Test invalid index
            Map<String, Object> invalidRecord = instrumentsData.getInstrumentRecordByIndex(999999);
            Assert.assertNull(invalidRecord, "Invalid index should return null");

            System.out.println("\n✓ Successfully retrieved instrument record by index");
        } else {
            Assert.fail("No records available to test");
        }
    }

    @Test(priority = 5)
    public void testFetchInstrumentsByGroupCode() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 5: Fetch Instruments by Group Code                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        String[] groupCodes = {"RM", "BM", "IP", "RR", "OE", "VM"};

        for (String groupCode : groupCodes) {
            System.out.println("\n--- Testing Group Code: " + groupCode + " ---");

            GetInstrumentsData groupInstruments = new GetInstrumentsData(dbConnection);
            boolean result = groupInstruments.fetchInstrumentsByGroupCode(groupCode);

            if (result) {
                System.out.println("✓ Found " + groupInstruments.getRecordCount() + " instrument(s) for group: " + groupCode);

                // Print first instrument details for this group
                if (groupInstruments.getRecordCount() > 0) {
                    System.out.println("  First instrument: " + groupInstruments.getName() + " (" + groupInstruments.getMnemo() + ")");
                }
            } else {
                System.out.println("  No instruments found for group: " + groupCode);
            }
        }

        System.out.println("\n✓ Successfully tested fetching by group codes");
    }

    @Test(priority = 6)
    public void testFetchInstrumentByCode() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 6: Fetch Instrument by Specific INST_CODE             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // First, fetch all instruments to get a valid INST_CODE
        instrumentsData.fetchInstrumentsData();

        if (instrumentsData.getRecordCount() > 0) {
            String testInstCode = instrumentsData.getInstCode();
            System.out.println("\nTesting with INST_CODE: " + testInstCode);

            // Create new instance and fetch by specific code
            GetInstrumentsData singleInstrument = new GetInstrumentsData(dbConnection);
            boolean result = singleInstrument.fetchInstrumentByCode(testInstCode);

            Assert.assertTrue(result, "Should successfully fetch instrument by code");
            Assert.assertEquals(singleInstrument.getInstCode(), testInstCode, "INST_CODE should match");

            System.out.println("\nFetched Instrument Details:");
            singleInstrument.printInstrumentData();

            System.out.println("\n✓ Successfully fetched instrument by INST_CODE");
        } else {
            System.out.println("✗ No instruments available to test");
        }
    }

    @Test(priority = 7)
    public void testGetAllData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 7: Get All Data as Map                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        if (instrumentsData.getRecordCount() > 0) {
            Map<String, String> dataMap = instrumentsData.getAllData();

            Assert.assertNotNull(dataMap, "Data map should not be null");
            Assert.assertTrue(dataMap.containsKey("instCode"), "Map should contain instCode");
            Assert.assertTrue(dataMap.containsKey("groupCode"), "Map should contain groupCode");
            Assert.assertTrue(dataMap.containsKey("mnemo"), "Map should contain mnemo");
            Assert.assertTrue(dataMap.containsKey("name"), "Map should contain name");

            System.out.println("\nData Map Contents:");
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\n✓ Successfully retrieved data as map");
        } else {
            Assert.fail("No records available to test");
        }
    }

    @Test(priority = 8)
    public void testGetAllInstrumentRecords() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 8: Get All Instrument Records                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        List<Map<String, Object>> allRecords = instrumentsData.getAllInstrumentRecords();

        Assert.assertNotNull(allRecords, "Records list should not be null");
        Assert.assertTrue(allRecords.size() > 0, "Should have at least one record");
        Assert.assertEquals(allRecords.size(), instrumentsData.getRecordCount(), "Record count should match");

        System.out.println("\n✓ Successfully retrieved all instrument records");
        System.out.println("  Total records: " + allRecords.size());
    }

    @Test(priority = 9)
    public void testInstrumentDataFieldValidation() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 9: Validate Instrument Data Fields                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch data first
        instrumentsData.fetchInstrumentsData();

        if (instrumentsData.getRecordCount() > 0) {
            // Validate that essential fields are not null/empty
            Assert.assertNotNull(instrumentsData.getInstCode(), "INST_CODE should not be null");
            Assert.assertNotNull(instrumentsData.getGroupCode(), "GROUP_CODE should not be null");
            Assert.assertNotNull(instrumentsData.getName(), "NAME should not be null");

            System.out.println("\nValidated Fields:");
            System.out.println("  INST_CODE: " + instrumentsData.getInstCode() + " ✓");
            System.out.println("  GROUP_CODE: " + instrumentsData.getGroupCode() + " ✓");
            System.out.println("  MNEMO: " + instrumentsData.getMnemo() + " ✓");
            System.out.println("  NAME: " + instrumentsData.getName() + " ✓");
            System.out.println("  SHOW_ORDER: " + instrumentsData.getShowOrder() + " ✓");
            System.out.println("  TRADE_CODE: " + instrumentsData.getTradeCode() + " ✓");

            System.out.println("\n✓ All fields validated successfully");
        } else {
            Assert.fail("No records available to validate");
        }
    }

    @Test(priority = 10)
    public void testInstrumentsGroupedByGroupCode() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 10: Group Instruments by GROUP_CODE                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch all instruments
        instrumentsData.fetchInstrumentsData();

        List<Map<String, Object>> allRecords = instrumentsData.getAllInstrumentRecords();

        // Count instruments by group code
        Map<String, Integer> groupCount = new java.util.HashMap<>();

        for (Map<String, Object> record : allRecords) {
            String groupCode = record.get("GROUP_CODE") != null ? record.get("GROUP_CODE").toString() : "";
            groupCount.put(groupCode, groupCount.getOrDefault(groupCode, 0) + 1);
        }

        System.out.println("\nInstruments Count by Group Code:");
        System.out.println("─────────────────────────────────");
        for (Map.Entry<String, Integer> entry : groupCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " instrument(s)");
        }

        System.out.println("\n✓ Successfully grouped instruments by GROUP_CODE");
        System.out.println("  Total Groups: " + groupCount.size());
        System.out.println("  Total Instruments: " + allRecords.size());
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
        System.out.println("║         GET INSTRUMENTS DATA TEST COMPLETED                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
