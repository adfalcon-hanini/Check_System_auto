package utils;

import com.example.screensData.clients.GetClientsData;
import com.example.screensData.mcalc.GetCalculatorStudyData;
import com.example.screensData.mcalc.GetVirtualTradeData;
import com.example.screensData.portfolio.GetDailyPortfolioData;
import com.example.screensData.portfolio.GetEqSharesData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive integration test to fetch data from all tables for each NIN
 * Tables: SEC_CLIENTS, sec_portfolio_avg_price, MYCALCULATOR_STUDY,
 *         sec_virtual_trade, Sec_Eq_Shares, SEC_EQU_DAILY_PORTFOLIO
 */
public class AllTablesIntegrationTest {

    private OracleDBConnection dbConnection;
    private GetClientsData clientsData;
    private GetCalculatorStudyData calculatorStudyData;
    private GetVirtualTradeData virtualTradeData;
    private GetEqSharesData eqSharesData;
    private GetDailyPortfolioData dailyPortfolioData;

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║      ALL TABLES INTEGRATION TEST STARTED                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            // Initialize all data classes
            clientsData = new GetClientsData(dbConnection);
            calculatorStudyData = new GetCalculatorStudyData(dbConnection);
            virtualTradeData = new GetVirtualTradeData(dbConnection);
            eqSharesData = new GetEqSharesData(dbConnection);
            dailyPortfolioData = new GetDailyPortfolioData(dbConnection);

            System.out.println("✓ All data classes initialized successfully!");

        } catch (SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed to establish database connection");
        }
    }

    @Test(priority = 1)
    public void testFetchAllDataForSingleNIN() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch All Data for First Client NIN                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get first client ID dynamically
        List<String> clientIds = clientsData.fetchAllClientIds(1);
        if (clientIds.isEmpty()) {
            System.out.println("✗ No clients found in database");
            return;
        }

        String testNIN = clientIds.get(0);
        System.out.println("\nTesting with first client NIN: " + testNIN);

        // Fetch from all tables
        System.out.println("\n--- Fetching from SEC_CLIENTS ---");
        boolean hasClient = clientsData.fetchClientData(testNIN);
        System.out.println(hasClient ? "✓ Client data found" : "✗ No client data");

        // Portfolio data check removed (GetPortfolioData class no longer exists)

        System.out.println("\n--- Fetching from MYCALCULATOR_STUDY ---");
        boolean hasCalculator = calculatorStudyData.fetchStudyData(testNIN);
        System.out.println(hasCalculator ? "✓ Calculator study data found (" + calculatorStudyData.getRecordCount() + " records)" : "✗ No calculator study data");

        System.out.println("\n--- Fetching from sec_virtual_trade ---");
        boolean hasVirtualTrade = virtualTradeData.fetchTradeData(testNIN);
        System.out.println(hasVirtualTrade ? "✓ Virtual trade data found (" + virtualTradeData.getRecordCount() + " records)" : "✗ No virtual trade data");

        System.out.println("\n--- Fetching from Sec_Eq_Shares ---");
        boolean hasEqShares = eqSharesData.fetchEqSharesByNin(testNIN);
        System.out.println(hasEqShares ? "✓ Equity shares data found" : "✗ No equity shares data");

        System.out.println("\n--- Fetching from SEC_EQU_DAILY_PORTFOLIO ---");
        boolean hasDailyPortfolio = dailyPortfolioData.fetchDailyPortfolioData(testNIN);
        System.out.println(hasDailyPortfolio ? "✓ Daily portfolio data found (" + dailyPortfolioData.getRecordCount() + " records)" : "✗ No daily portfolio data");

        // Summary
        System.out.println("\n========== DATA AVAILABILITY SUMMARY ==========");
        System.out.println("SEC_CLIENTS: " + (hasClient ? "YES" : "NO"));
        System.out.println("MYCALCULATOR_STUDY: " + (hasCalculator ? "YES (" + calculatorStudyData.getRecordCount() + ")" : "NO"));
        System.out.println("sec_virtual_trade: " + (hasVirtualTrade ? "YES (" + virtualTradeData.getRecordCount() + ")" : "NO"));
        System.out.println("Sec_Eq_Shares: " + (hasEqShares ? "YES" : "NO"));
        System.out.println("SEC_EQU_DAILY_PORTFOLIO: " + (hasDailyPortfolio ? "YES (" + dailyPortfolioData.getRecordCount() + ")" : "NO"));
        System.out.println("===============================================");
    }

    @Test(priority = 2)
    public void testFetchAllDataForMultipleNINs() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Fetch All Data for Multiple NIINs                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get first 5 client IDs
        List<String> nins = clientsData.fetchAllClientIds(5);
        System.out.println("\nTesting with " + nins.size() + " NIINs: " + nins);

        System.out.println("\n--- Fetching all tables data for multiple NIINs ---");

        calculatorStudyData.fetchStudyDataForMultipleNins(nins);
        virtualTradeData.fetchTradeDataForMultipleNins(nins);

        // Fetch equity shares data for each NIN individually (no batch method available)
        int eqSharesCount = 0;
        for (String nin : nins) {
            if (eqSharesData.fetchEqSharesByNin(nin)) {
                eqSharesCount++;
            }
        }

        dailyPortfolioData.fetchDailyPortfolioDataForMultipleNins(nins);

        System.out.println("\n========== RESULTS ==========");
        System.out.println("Calculator study records: " + calculatorStudyData.getRecordCount());
        System.out.println("Virtual trade records: " + virtualTradeData.getRecordCount());
        System.out.println("Equity shares NIINs with data: " + eqSharesCount);
        System.out.println("Daily portfolio records: " + dailyPortfolioData.getRecordCount());
        System.out.println("============================");
    }

    @Test(priority = 3)
    public void testGenerateComprehensiveReport() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Generate Comprehensive Report for All NIINs        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        List<String> allNIINs = clientsData.fetchAllClientIds(10);
        System.out.println("\nGenerating report for " + allNIINs.size() + " clients...\n");

        for (String nin : allNIINs) {
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│ NIN: " + nin);
            System.out.println("├────────────────────────────────────────────────────────────┤");

            // Fetch client info
            if (clientsData.fetchClientData(nin)) {
                System.out.println("│ Name (EN): " + clientsData.getNameEnglish());
                System.out.println("│ Name (AR): " + clientsData.getNameArabic());
            }

            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│ DATA AVAILABILITY:");

            // Check each table
            boolean hasCalculator = calculatorStudyData.fetchStudyData(nin);
            boolean hasVirtualTrade = virtualTradeData.fetchTradeData(nin);
            boolean hasEqShares = eqSharesData.fetchEqSharesByNin(nin);
            boolean hasDailyPortfolio = dailyPortfolioData.fetchDailyPortfolioData(nin);

            System.out.println("│   Calculator Study: " + (hasCalculator ? "✓ (" + calculatorStudyData.getRecordCount() + ")" : "✗"));
            System.out.println("│   Virtual Trade: " + (hasVirtualTrade ? "✓ (" + virtualTradeData.getRecordCount() + ")" : "✗"));
            System.out.println("│   Equity Shares: " + (hasEqShares ? "✓" : "✗"));
            System.out.println("│   Daily Portfolio: " + (hasDailyPortfolio ? "✓ (" + dailyPortfolioData.getRecordCount() + ")" : "✗"));
            System.out.println("└────────────────────────────────────────────────────────────┘");
        }
    }

    @Test(priority = 4)
    public void testDetailedDataForSpecificNIN() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Detailed Data Display for First Client             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get first client ID dynamically
        List<String> clientIds = clientsData.fetchAllClientIds(1);
        if (clientIds.isEmpty()) {
            System.out.println("✗ No clients found in database");
            return;
        }

        String nin = clientIds.get(0);
        System.out.println("\nShowing detailed data for NIN: " + nin);

        // Fetch all data
        clientsData.fetchClientData(nin);
        calculatorStudyData.fetchStudyData(nin);
        virtualTradeData.fetchTradeData(nin);
        boolean hasEqShares = eqSharesData.fetchEqSharesByNin(nin);
        dailyPortfolioData.fetchDailyPortfolioData(nin);

        // Print detailed data
        clientsData.printClientData();

        if (calculatorStudyData.getRecordCount() > 0) {
            calculatorStudyData.printAllData();
        }

        if (virtualTradeData.getRecordCount() > 0) {
            virtualTradeData.printAllData();
        }

        // Note: EqShares data print methods not available in minimal implementation
        System.out.println("\n--- Equity Shares Data ---");
        System.out.println(hasEqShares ? "✓ Equity shares data available" : "✗ No equity shares data");

        if (dailyPortfolioData.getRecordCount() > 0) {
            dailyPortfolioData.printAllData();
        }
    }

    @Test(priority = 5)
    public void testDataStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 5: Data Statistics Across All Tables                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        List<String> allNIINs = clientsData.fetchAllClientIds();
        System.out.println("\nTotal Clients: " + allNIINs.size());

        int clientsWithCalculator = 0;
        int clientsWithVirtualTrade = 0;
        int clientsWithEqShares = 0;
        int clientsWithDailyPortfolio = 0;

        System.out.println("\nAnalyzing data availability...\n");

        for (String nin : allNIINs) {
            if (calculatorStudyData.fetchStudyData(nin)) clientsWithCalculator++;
            if (virtualTradeData.fetchTradeData(nin)) clientsWithVirtualTrade++;
            if (eqSharesData.fetchEqSharesByNin(nin)) clientsWithEqShares++;
            if (dailyPortfolioData.fetchDailyPortfolioData(nin)) clientsWithDailyPortfolio++;
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              DATA AVAILABILITY STATISTICS                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Total Clients: " + String.format("%-42d", allNIINs.size()) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Calculator Study: " + String.format("%-40d", clientsWithCalculator) + "║");
        System.out.println("║ Virtual Trade: " + String.format("%-43d", clientsWithVirtualTrade) + "║");
        System.out.println("║ Equity Shares: " + String.format("%-43d", clientsWithEqShares) + "║");
        System.out.println("║ Daily Portfolio: " + String.format("%-41d", clientsWithDailyPortfolio) + "║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Test(priority = 6)
    public void testFetchAllDataForAllClients() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 6: Fetch Data from All Tables for ALL Clients         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Get ALL client IDs
        List<String> allClientIds = clientsData.fetchAllClientIds();
        System.out.println("\nTotal clients to process: " + allClientIds.size());

        // Statistics counters
        int totalProcessed = 0;
        int clientsWithData = 0;

        // Data availability counters per table
        int calculatorCount = 0;
        int virtualTradeCount = 0;
        int eqSharesCount = 0;
        int dailyPortfolioCount = 0;

        System.out.println("\nProcessing all clients...\n");
        System.out.println("Progress: [" + "=".repeat(50) + "]");

        // Process each client
        for (int i = 0; i < allClientIds.size(); i++) {
            String nin = allClientIds.get(i);
            totalProcessed++;

            boolean hasAnyData = false;

            // Check each table
            if (calculatorStudyData.fetchStudyData(nin)) {
                calculatorCount++;
                hasAnyData = true;
            }

            if (virtualTradeData.fetchTradeData(nin)) {
                virtualTradeCount++;
                hasAnyData = true;
            }

            if (eqSharesData.fetchEqSharesByNin(nin)) {
                eqSharesCount++;
                hasAnyData = true;
            }

            if (dailyPortfolioData.fetchDailyPortfolioData(nin)) {
                dailyPortfolioCount++;
                hasAnyData = true;
            }

            if (hasAnyData) {
                clientsWithData++;
            }

            // Show progress every 10%
            if ((i + 1) % Math.max(1, allClientIds.size() / 10) == 0) {
                int percent = ((i + 1) * 100) / allClientIds.size();
                System.out.println("Progress: " + percent + "% - Processed " + (i + 1) + "/" + allClientIds.size() + " clients");
            }
        }

        // Display comprehensive results
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          COMPREHENSIVE DATA FETCH RESULTS - ALL CLIENTS        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Total Clients Processed: " + String.format("%-37d", totalProcessed) + "║");
        System.out.println("║ Clients with Any Data: " + String.format("%-39d", clientsWithData) + "║");
        System.out.println("║ Clients with No Data: " + String.format("%-40d", (totalProcessed - clientsWithData)) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ DATA AVAILABILITY BY TABLE:                                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Calculator Study:        " + String.format("%-35d", calculatorCount) + "║");
        System.out.println("║ Virtual Trade:           " + String.format("%-35d", virtualTradeCount) + "║");
        System.out.println("║ Equity Shares:           " + String.format("%-35d", eqSharesCount) + "║");
        System.out.println("║ Daily Portfolio:         " + String.format("%-35d", dailyPortfolioCount) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ DATA COVERAGE PERCENTAGES:                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Calculator Study:        " + String.format("%.2f%%", (calculatorCount * 100.0 / totalProcessed)) + String.format("%" + (35 - String.format("%.2f%%", (calculatorCount * 100.0 / totalProcessed)).length()) + "s", "") + "║");
        System.out.println("║ Virtual Trade:           " + String.format("%.2f%%", (virtualTradeCount * 100.0 / totalProcessed)) + String.format("%" + (35 - String.format("%.2f%%", (virtualTradeCount * 100.0 / totalProcessed)).length()) + "s", "") + "║");
        System.out.println("║ Equity Shares:           " + String.format("%.2f%%", (eqSharesCount * 100.0 / totalProcessed)) + String.format("%" + (35 - String.format("%.2f%%", (eqSharesCount * 100.0 / totalProcessed)).length()) + "s", "") + "║");
        System.out.println("║ Daily Portfolio:         " + String.format("%.2f%%", (dailyPortfolioCount * 100.0 / totalProcessed)) + String.format("%" + (35 - String.format("%.2f%%", (dailyPortfolioCount * 100.0 / totalProcessed)).length()) + "s", "") + "║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        Assert.assertTrue(totalProcessed > 0, "Should process at least one client");
    }

    @Test(priority = 7)
    public void testExportAllClientDataToList() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 7: Export All Client Data with Table Availability     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        List<String> allClientIds = clientsData.fetchAllClientIds();
        System.out.println("\nExporting data for " + allClientIds.size() + " clients...\n");

        // Create comprehensive data structure
        List<Map<String, Object>> exportData = new ArrayList<>();

        for (String nin : allClientIds) {
            Map<String, Object> clientDataMap = new HashMap<>();

            // Client info
            if (clientsData.fetchClientData(nin)) {
                clientDataMap.put("NIN", nin);
                clientDataMap.put("Name_EN", clientsData.getNameEnglish());
                clientDataMap.put("Name_AR", clientsData.getNameArabic());
                clientDataMap.put("Mobile", clientsData.getMobileNo());
                clientDataMap.put("Email", clientsData.getEmail());
            }

            // Table availability flags
            clientDataMap.put("Has_Calculator", calculatorStudyData.fetchStudyData(nin));
            clientDataMap.put("Has_VirtualTrade", virtualTradeData.fetchTradeData(nin));
            clientDataMap.put("Has_EqShares", eqSharesData.fetchEqSharesByNin(nin));
            clientDataMap.put("Has_DailyPortfolio", dailyPortfolioData.fetchDailyPortfolioData(nin));

            // Record counts
            clientDataMap.put("Calculator_Count", calculatorStudyData.getRecordCount());
            clientDataMap.put("VirtualTrade_Count", virtualTradeData.getRecordCount());
            clientDataMap.put("EqShares_Count", "N/A");  // No record count available
            clientDataMap.put("DailyPortfolio_Count", dailyPortfolioData.getRecordCount());

            exportData.add(clientDataMap);
        }

        // Display clients with any data
        System.out.println("Export Data (Clients with Data):\n");
        int clientCounter = 0;
        for (int i = 0; i < exportData.size(); i++) {
            Map<String, Object> client = exportData.get(i);

            // Only print clients that have any records
            boolean hasAnyData = Boolean.TRUE.equals(client.get("Has_Calculator")) ||
                                 Boolean.TRUE.equals(client.get("Has_VirtualTrade")) ||
                                 Boolean.TRUE.equals(client.get("Has_EqShares")) ||
                                 Boolean.TRUE.equals(client.get("Has_DailyPortfolio"));

            if (hasAnyData) {
                clientCounter++;
                System.out.println("Client " + clientCounter + ":");
                System.out.println("  NIN: " + client.get("NIN"));
                System.out.println("  Name: " + client.get("Name_EN"));
                System.out.println("  Calculator: " + (Boolean.TRUE.equals(client.get("Has_Calculator")) ? "✓ (" + client.get("Calculator_Count") + ")" : "✗"));
                System.out.println("  Virtual Trade: " + (Boolean.TRUE.equals(client.get("Has_VirtualTrade")) ? "✓ (" + client.get("VirtualTrade_Count") + ")" : "✗"));
                System.out.println("  Eq Shares: " + (Boolean.TRUE.equals(client.get("Has_EqShares")) ? "✓ (" + client.get("EqShares_Count") + ")" : "✗"));
                System.out.println("  Daily Portfolio: " + (Boolean.TRUE.equals(client.get("Has_DailyPortfolio")) ? "✓ (" + client.get("DailyPortfolio_Count") + ")" : "✗"));
                System.out.println();
            }
        }
        System.out.println("Total clients with data: " + clientCounter + " out of " + exportData.size() + " clients");

        System.out.println("✓ Successfully exported data for " + exportData.size() + " clients");
        Assert.assertEquals(exportData.size(), allClientIds.size(), "Export should contain all clients");
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
        System.out.println("║      ALL TABLES INTEGRATION TEST COMPLETED                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
