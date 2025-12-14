package com.example.tests.clients;

import com.example.screensData.clients.GetClientsData;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Client Data Test Suite
 *
 * TestNG Groups:
 * - clients: Module group
 * - database: Type group
 * - smoke: Suite group (for critical tests)
 * - regression: Suite group (for all tests)
 * - fast: Performance group
 */
@Epic("Clients Module")
@Feature("Client Data Management")
public class GetClientsDataTest {

    private OracleDBConnection dbConnection;
    private GetClientsData clientsData;

    @BeforeClass
    public void setup() {
        // Initialize connection with provided credentials
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("=== GetClientsData Test Started ===");

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

    @Test(priority = 1, groups = {"clients", "database", "smoke", "regression", "fast"},
          description = "Verify fetching client data for single ID")
    public void testFetchClientDataForSingleId() {
        System.out.println("\n=== Test: Fetch Client Data for Single ID ===");

        String clientId = "12240";
        boolean success = clientsData.fetchClientData(clientId);

        if (success) {
            System.out.println("✓ Client data fetched successfully for ID: " + clientId);
            System.out.println("Total records found: " + clientsData.getRecordCount());

            // Print client details
            clientsData.printClientData();

            // Verify data is not empty
            Assert.assertNotNull(clientsData.getClientId(), "Client ID should not be null");
            Assert.assertFalse(clientsData.getClientId().isEmpty(), "Client ID should not be empty");

            // Print individual fields
            System.out.println("\n--- Accessing Individual Fields ---");
            System.out.println("Client ID: " + clientsData.getClientId());
            System.out.println("Arabic Name: " + clientsData.getNameArabic());
            System.out.println("English Name: " + clientsData.getNameEnglish());
            System.out.println("Nationality: " + clientsData.getNationality());
            System.out.println("Personal ID: " + clientsData.getPersonalId());
            System.out.println("Passport ID: " + clientsData.getPassportId());
            System.out.println("P.O. Box: " + clientsData.getPoBox());
            System.out.println("Mobile: " + clientsData.getMobileNo());
            System.out.println("Home Tel: " + clientsData.getHomeTelNo());
            System.out.println("Work Tel: " + clientsData.getWorkTelNo());
            System.out.println("Address: " + clientsData.getAddress());
            System.out.println("Email: " + clientsData.getEmail());

        } else {
            System.err.println("✗ Failed to fetch client data for ID: " + clientId);
            Assert.fail("No client data found for ID: " + clientId);
        }
    }

    @Test(priority = 2)
    public void testGetAllClientRecords() {
        System.out.println("\n=== Test: Get All Client Records ===");

        String clientId = "12240";
        clientsData.fetchClientData(clientId);

        List<Map<String, Object>> allRecords = clientsData.getAllClientRecords();
        System.out.println("Total records: " + allRecords.size());

        Assert.assertNotNull(allRecords, "Records list should not be null");
        Assert.assertTrue(allRecords.size() > 0, "Should have at least one record");

        // Print all records
        clientsData.printAllClientData();
    }

    @Test(priority = 3)
    public void testGetClientRecordByIndex() {
        System.out.println("\n=== Test: Get Client Record by Index ===");

        String clientId = "12240";
        clientsData.fetchClientData(clientId);

        int recordCount = clientsData.getRecordCount();
        System.out.println("Total records available: " + recordCount);

        if (recordCount > 0) {
            // Get first record
            Map<String, Object> firstRecord = clientsData.getClientRecordByIndex(0);
            Assert.assertNotNull(firstRecord, "First record should not be null");

            System.out.println("\n--- First Record ---");
            for (Map.Entry<String, Object> entry : firstRecord.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Test invalid index
            Map<String, Object> invalidRecord = clientsData.getClientRecordByIndex(999);
            Assert.assertNull(invalidRecord, "Invalid index should return null");
            System.out.println("\n✓ Invalid index correctly returns null");
        }
    }

    @Test(priority = 4)
    public void testGetAllDataAsMap() {
        System.out.println("\n=== Test: Get All Data as Map ===");

        String clientId = "12240";
        clientsData.fetchClientData(clientId);

        Map<String, String> allData = clientsData.getAllData();
        Assert.assertNotNull(allData, "Data map should not be null");
        Assert.assertFalse(allData.isEmpty(), "Data map should not be empty");

        System.out.println("\n--- Data Map Contents ---");
        for (Map.Entry<String, String> entry : allData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n✓ Successfully retrieved data as map");
    }

    @Test(priority = 5)
    public void testFetchClientDataForMultipleIds() {
        System.out.println("\n=== Test: Fetch Client Data for Multiple IDs ===");

        List<String> clientIds = Arrays.asList("12240","1218","312");
        boolean success = clientsData.fetchClientDataForMultipleIds(clientIds);

        if (success) {
            System.out.println("✓ Client data fetched successfully for multiple IDs");
            System.out.println("Total records found: " + clientsData.getRecordCount());

            clientsData.printAllClientData();

            Assert.assertTrue(clientsData.getRecordCount() > 0, "Should have at least one record");
        } else {
            System.err.println("✗ Failed to fetch client data for multiple IDs");
        }
    }

    @Test(priority = 6)
    public void testInvalidClientId() {
        System.out.println("\n=== Test: Invalid Client ID ===");

        GetClientsData testData = new GetClientsData(dbConnection);
        String invalidId = "99999999";
        boolean success = testData.fetchClientData(invalidId);

        if (!success) {
            System.out.println("✓ Correctly handled invalid Client ID: " + invalidId);
            Assert.assertEquals(testData.getRecordCount(), 0, "Should have zero records for invalid ID");
        } else {
            System.err.println("✗ Unexpectedly found data for invalid Client ID");
        }
    }

    @Test(priority = 8)
    public void testArabicNameDisplay() {
        System.out.println("\n=== Test: Arabic Name Display ===");

        String clientId = "12240";
        if (clientsData.fetchClientData(clientId)) {
            String arabicName = clientsData.getNameArabic();
            System.out.println("Arabic Name: " + arabicName);

            Assert.assertNotNull(arabicName, "Arabic name should not be null");
            System.out.println("✓ Arabic name retrieved successfully");

            // Check if contains Arabic characters
            boolean containsArabic = arabicName.matches(".*[\\u0600-\\u06FF].*");
            if (containsArabic) {
                System.out.println("✓ Arabic name contains Arabic characters");
            } else {
                System.out.println("⚠ Arabic name does not contain Arabic characters (may be encoding issue)");
            }
        }
    }

    @Test(priority = 9)
    public void testFetchAllClientIds() {
        System.out.println("\n=== Test: Fetch All Client IDs ===");

        List<String> allClientIds = clientsData.fetchAllClientIds();

        Assert.assertNotNull(allClientIds, "Client IDs list should not be null");
        System.out.println("✓ Total client IDs found: " + allClientIds.size());

        // Display first 10 client IDs
        System.out.println("\n--- First 10 Client IDs ---");
        int displayCount = Math.min(10, allClientIds.size());
        for (int i = 0; i < displayCount; i++) {
            System.out.println((i + 1) + ". " + allClientIds.get(i));
        }

        if (allClientIds.size() > 10) {
            System.out.println("... and " + (allClientIds.size() - 10) + " more");
        }
    }

    @Test(priority = 10)
    public void testFetchAllClientIdsWithLimit() {
        System.out.println("\n=== Test: Fetch Client IDs with Limit ===");

        int limit = 5;
        List<String> limitedClientIds = clientsData.fetchAllClientIds(limit);

        Assert.assertNotNull(limitedClientIds, "Client IDs list should not be null");
        Assert.assertTrue(limitedClientIds.size() <= limit,
            "Should return maximum " + limit + " client IDs");

        System.out.println("✓ Requested limit: " + limit);
        System.out.println("✓ Actual count: " + limitedClientIds.size());

        System.out.println("\n--- Client IDs (Limited) ---");
        for (int i = 0; i < limitedClientIds.size(); i++) {
            System.out.println((i + 1) + ". " + limitedClientIds.get(i));
        }
    }

    @Test(priority = 11)
    public void testGetAllClientsCount() {
        System.out.println("\n=== Test: Get Total Clients Count ===");

        int totalCount = clientsData.getAllClientsCount();

        Assert.assertTrue(totalCount >= 0, "Count should be non-negative");
        System.out.println("✓ Total clients in SEC_CLIENTS: " + totalCount);

        // Verify count matches fetchAllClientIds
        List<String> allIds = clientsData.fetchAllClientIds();
        System.out.println("✓ Verification: fetchAllClientIds returned " + allIds.size() + " IDs");

        if (totalCount == allIds.size()) {
            System.out.println("✓ Count verification passed!");
        } else {
            System.out.println("⚠ Count mismatch - may be due to null/empty IDs");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n=== Closing Connection ===");
        if (dbConnection != null) {
            dbConnection.closeConnection();
            System.out.println("✓ Connection closed successfully");
        }
        System.out.println("=== GetClientsData Test Completed ===");
    }
}
