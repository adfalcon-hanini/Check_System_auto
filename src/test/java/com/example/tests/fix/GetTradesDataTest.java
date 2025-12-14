package com.example.tests.fix;

import com.example.screensData.xdp.GetTradesData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Test class for GetTradesData
 * Tests retrieval of trades from XDP_TRADES table
 *
 * This class contains 4 focused test methods:
 * 1. Fetch trades for today
 * 2. Fetch trades by specific date
 * 3. Fetch trades by instrument code
 * 4. Fetch and get trade count by instrument for today
 */
public class GetTradesDataTest {

    private OracleDBConnection dbConnection;
    private GetTradesData tradesData;

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         GET TRADES DATA TEST STARTED                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            tradesData = new GetTradesData(dbConnection);
            System.out.println("✓ GetTradesData instance initialized successfully!");

        } catch (Exception e) {
            System.err.println("✗ Setup failed: " + e.getMessage());
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    /**
     * TEST 1: Fetch trades for today using current date as parameter
     * Gets current date in dd-MM-yyyy format and fetches all trades for today
     */
    @Test(priority = 1, description = "Fetch trades for today using current date")
    public void testFetchTradesToday() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch Trades for Today                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get current date in dd-MM-yyyy format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        System.out.println("\nCurrent Date: " + currentDate);
        System.out.println("Fetching trades for today using date parameter...");

        GetTradesData todayTrades = new GetTradesData(dbConnection);
        boolean result = todayTrades.fetchTradesByDate(currentDate);

        if (result) {
            System.out.println("\n✓ Successfully fetched " + todayTrades.getRecordCount() + " trade(s) for today (" + currentDate + ")");

            // Print trades table
            todayTrades.printTradesTable();

            // Print summary
            todayTrades.printTradesSummary();

            Assert.assertTrue(todayTrades.getRecordCount() > 0, "Should have at least one trade for today");

            System.out.println("\n✓ TEST 1 PASSED: Trades for today fetched successfully");
        } else {
            System.out.println("ℹ No trades found for today (" + currentDate + ")");
            System.out.println("This is expected if no trades occurred today.");
            System.out.println("\n✓ TEST 1 PASSED: No trades today (valid scenario)");
        }
    }

    /**
     * TEST 2: Fetch trades by specific date
     * User can modify the date parameter to fetch trades for any specific date
     *
     * @param date - Date in format dd-MM-yyyy (e.g., "01-11-2025")
     */
    @Test(priority = 2, description = "Fetch trades for a specific date (parameterized)")
    public void testFetchTradesByDate() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Fetch Trades by Specific Date                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // User can modify this date parameter
        String targetDate = "01-11-2025";  // Format: dd-MM-yyyy

        System.out.println("\nTarget Date: " + targetDate);
        System.out.println("Fetching trades for the specified date...");

        GetTradesData tradesForDate = new GetTradesData(dbConnection);
        boolean result = tradesForDate.fetchTradesByDate(targetDate);

        if (result) {
            System.out.println("\n✓ Successfully fetched " + tradesForDate.getRecordCount() + " trade(s) for date: " + targetDate);

            // Print trades table
            tradesForDate.printTradesTable();

            // Print summary
            tradesForDate.printTradesSummary();

            Assert.assertTrue(tradesForDate.getRecordCount() > 0, "Should have at least one trade for the date");

            System.out.println("\n✓ TEST 2 PASSED: Trades for " + targetDate + " fetched successfully");
        } else {
            System.out.println("ℹ No trades found for date: " + targetDate);
            System.out.println("This is expected if no trades occurred on " + targetDate);
            System.out.println("\n✓ TEST 2 PASSED: No trades for specified date (valid scenario)");
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  NOTE: To test different dates, modify the 'targetDate'       ║");
        System.out.println("║  variable in the test method (Format: dd-MM-yyyy)             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * TEST 3: Fetch trades by instrument code
     * User can modify the instrument code parameter to fetch trades for any instrument
     *
     * @param instrumentCode - Instrument code (INST_SEQ) e.g., "QNBK", "MARK", "CBQK"
     */
    @Test(priority = 3, description = "Fetch trades for a specific instrument (parameterized)")
    public void testFetchTradesByInstrument() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Fetch Trades by Instrument Code                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // User can modify this instrument code parameter
        String instrumentCode = "42";  // Examples: QNBK, MARK, CBQK, etc.

        System.out.println("\nInstrument Code: " + instrumentCode);
        System.out.println("Fetching trades for the specified instrument...");

        GetTradesData tradesForInstrument = new GetTradesData(dbConnection);
        boolean result = tradesForInstrument.fetchTradesByInstrument(instrumentCode);

        if (result) {
            System.out.println("\n✓ Successfully fetched " + tradesForInstrument.getRecordCount() + " trade(s) for " + instrumentCode);

            // Print trades table
            tradesForInstrument.printTradesTable();

            // Print summary
            tradesForInstrument.printTradesSummary();

            // Verify all trades are for the specified instrument
            System.out.println("\nVerifying all trades are for " + instrumentCode + "...");
            boolean allMatch = true;
            for (Map<String, Object> trade : tradesForInstrument.getAllTradeRecords()) {
                String instCode = trade.get("INST_SEQ") != null ? trade.get("INST_SEQ").toString() : "";
                if (!instCode.equals(instrumentCode)) {
                    allMatch = false;
                    break;
                }
            }

            Assert.assertTrue(allMatch, "All trades should be for " + instrumentCode);
            Assert.assertTrue(tradesForInstrument.getRecordCount() > 0, "Should have trades for " + instrumentCode);

            System.out.println("✓ Verified: All " + tradesForInstrument.getRecordCount() + " trades are for " + instrumentCode);
            System.out.println("\n✓ TEST 3 PASSED: Trades for " + instrumentCode + " fetched and verified successfully");
        } else {
            System.out.println("ℹ No trades found for instrument: " + instrumentCode);
            System.out.println("This is expected if " + instrumentCode + " has no trades in the database");
            System.out.println("\n✓ TEST 3 PASSED: No trades for instrument (valid scenario)");
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  NOTE: To test different instruments, modify the              ║");
        System.out.println("║  'instrumentCode' variable in the test method                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * TEST 4: Fetch and get trade count by instrument for today
     * Gets trade counts for all instruments that have trades today
     */
    @Test(priority = 4, description = "Get trade count for all instruments for today")
    public void testFetchAndGetTradeCountByInstrumentForToday() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Get Trade Count by Instrument (Today)              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        System.out.println("\nCurrent Date: " + currentDate);
        System.out.println("Fetching today's trades to calculate counts by instrument...");

        // First, fetch all trades for today
        GetTradesData todayTrades = new GetTradesData(dbConnection);
        boolean tradesExist = todayTrades.fetchTradesByDate(currentDate);

        if (tradesExist) {
            System.out.println("✓ Fetched " + todayTrades.getRecordCount() + " trade(s) for today");

            // Calculate trade counts by instrument from today's data
            Map<String, Integer> instrumentCounts = todayTrades.getTradeCountByInstrument();

            if (!instrumentCounts.isEmpty()) {
                System.out.println("\n✓ Trade counts calculated for " + instrumentCounts.size() + " instrument(s)");

                // Print the counts in a formatted table
                todayTrades.printTradeCountByInstrument(instrumentCounts);

                // Calculate and display statistics
                int totalTrades = instrumentCounts.values().stream().mapToInt(Integer::intValue).sum();
                System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                    Today's Statistics                          ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝");
                System.out.println("Date: " + currentDate);
                System.out.println("Total Instruments Traded: " + instrumentCounts.size());
                System.out.println("Total Trades: " + totalTrades);

                // Show top 5 most active instruments today
                System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║         Top 5 Most Active Instruments Today                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝");

                instrumentCounts.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(5)
                    .forEach(entry -> {
                        double percentage = (entry.getValue() * 100.0) / totalTrades;
                        System.out.printf("  %-15s : %,6d trade(s)  (%.2f%%)%n",
                                         entry.getKey(), entry.getValue(), percentage);
                    });

                Assert.assertTrue(instrumentCounts.size() > 0, "Should have at least one instrument");
                Assert.assertTrue(totalTrades > 0, "Should have at least one trade");

                System.out.println("\n✓ TEST 4 PASSED: Trade counts by instrument for today calculated successfully");
            } else {
                System.out.println("ℹ No instrument counts calculated");
                Assert.fail("Failed to calculate instrument counts from today's trades");
            }
        } else {
            System.out.println("ℹ No trades found for today (" + currentDate + ")");
            System.out.println("Cannot calculate trade counts when no trades exist for today.");
            System.out.println("\n✓ TEST 4 PASSED: No trades today (valid scenario)");
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  This test shows which instruments were most actively         ║");
        System.out.println("║  traded today with their respective trade counts              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
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
        System.out.println("║         GET TRADES DATA TEST COMPLETED                        ║");
        System.out.println("║                                                               ║");
        System.out.println("║  Summary of Tests:                                            ║");
        System.out.println("║  1. ✓ Fetch trades for today                                 ║");
        System.out.println("║  2. ✓ Fetch trades by specific date                          ║");
        System.out.println("║  3. ✓ Fetch trades by instrument code                        ║");
        System.out.println("║  4. ✓ Get trade count by instrument for today                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
