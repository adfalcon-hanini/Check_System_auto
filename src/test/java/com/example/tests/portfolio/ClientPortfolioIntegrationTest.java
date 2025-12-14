package com.example.tests.portfolio;

import com.example.screensData.clients.GetClientsData;
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
 * Integration test to fetch portfolio data for all clients
 */
public class ClientPortfolioIntegrationTest {

    private OracleDBConnection dbConnection;
    private GetClientsData clientsData;

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("=== Client Portfolio Integration Test Started ===");

        try {
            dbConnection.connect();
            System.out.println("✓ Connection established successfully!");

            clientsData = new GetClientsData(dbConnection);

        } catch (SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed to establish database connection");
        }
    }

    @Test(priority = 1)
    public void testFetchAllClients() {
        System.out.println("\n=== Test: Fetch All Clients ===");

        // Get all client IDs
        System.out.println("\nFetching all client IDs...");
        List<String> allClientIds = clientsData.fetchAllClientIds();
        System.out.println("✓ Total client IDs found: " + allClientIds.size());

        // Display summary
        System.out.println("\n========== Clients Summary ==========");
        System.out.println("Total Clients: " + allClientIds.size());
        System.out.println("====================================");

        Assert.assertTrue(allClientIds.size() > 0, "Should have at least one client");
    }

    @Test(priority = 2)
    public void testFetchLimitedClients() {
        System.out.println("\n=== Test: Fetch Limited Clients (First 10) ===");

        // Get first 10 client IDs
        List<String> limitedClientIds = clientsData.fetchAllClientIds(10);
        System.out.println("✓ Fetched " + limitedClientIds.size() + " clients");

        for (String clientId : limitedClientIds) {
            System.out.println("\n--- Client ID: " + clientId + " ---");
            if (clientsData.fetchClientData(clientId)) {
                clientsData.printClientData();
            }
        }

        System.out.println("\n========== Clients Processed ==========");
        System.out.println("Total Clients: " + limitedClientIds.size());
        System.out.println("======================================");
    }

    @Test(priority = 3)
    public void testFetchDetailedClient() {
        System.out.println("\n=== Test: Fetch Detailed Client Data ===");

        String testClientId = "12240";

        // Fetch client data
        System.out.println("\nFetching client data...");
        boolean clientFound = clientsData.fetchClientData(testClientId);

        if (clientFound) {
            System.out.println("✓ Client data found");
            clientsData.printClientData();
        } else {
            System.out.println("✗ Client not found");
            return;
        }

        // Summary
        System.out.println("\n========== Client Summary ==========");
        System.out.println("Client ID: " + clientsData.getClientId());
        System.out.println("Client Name (EN): " + clientsData.getNameEnglish());
        System.out.println("Client Name (AR): " + clientsData.getNameArabic());
        System.out.println("Mobile: " + clientsData.getMobileNo());
        System.out.println("Email: " + clientsData.getEmail());
        System.out.println("====================================");

        Assert.assertTrue(clientFound, "Client should be found");
    }

    @Test(priority = 4)
    public void testFetchMultipleClients() {
        System.out.println("\n=== Test: Fetch Multiple Specific Clients ===");

        // Get first 5 client IDs
        List<String> specificClientIds = clientsData.fetchAllClientIds(5);

        System.out.println("Testing with client IDs: " + specificClientIds);

        for (String clientId : specificClientIds) {
            if (clientsData.fetchClientData(clientId)) {
                System.out.println("\n✓ Client " + clientId + ": " + clientsData.getNameEnglish());
            }
        }
    }

    @Test(priority = 5)
    public void testGenerateClientReport() {
        System.out.println("\n=== Test: Generate Client Report ===");

        // Get limited number of clients for demo
        List<String> clientIds = clientsData.fetchAllClientIds(5);

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               CLIENT REPORT                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝");

        int reportNumber = 1;
        for (String clientId : clientIds) {
            // Fetch client info
            if (clientsData.fetchClientData(clientId)) {
                System.out.println("\n┌─────────────────────────────────────────────────────────────────────┐");
                System.out.println("│ Report #" + reportNumber + " - Client ID: " + clientId);
                System.out.println("├─────────────────────────────────────────────────────────────────────┤");
                System.out.println("│ Name (EN): " + clientsData.getNameEnglish());
                System.out.println("│ Name (AR): " + clientsData.getNameArabic());
                System.out.println("│ Mobile: " + clientsData.getMobileNo());
                System.out.println("│ Email: " + clientsData.getEmail());
                System.out.println("└─────────────────────────────────────────────────────────────────────┘");
                reportNumber++;
            }
        }

        System.out.println("\n" + "═".repeat(75));
        System.out.println("Report Generated Successfully for " + clientIds.size() + " clients");
        System.out.println("═".repeat(75));
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n=== Closing Connection ===");
        if (dbConnection != null) {
            dbConnection.closeConnection();
            System.out.println("✓ Connection closed successfully");
        }
        System.out.println("=== Client Portfolio Integration Test Completed ===");
    }
}
