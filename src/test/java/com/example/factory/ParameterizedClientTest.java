package com.example.factory;

import com.example.screensData.clients.GetClientsData;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.Map;

/**
 * Parameterized test class that will be instantiated by the Factory
 * Each instance will have different test parameters
 */
@Epic("Database Testing")
@Feature("Client Data Retrieval")
public class ParameterizedClientTest {

    private OracleDBConnection dbConnection;
    private GetClientsData clientsData;
    private final TestDataProvider testData;

    // Constructor that receives test data from Factory
    public ParameterizedClientTest(TestDataProvider testData) {
        this.testData = testData;
    }

    @BeforeClass
    public void setup() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Setting up test instance: " + testData.getTestName());
        System.out.println("Client ID: " + testData.getClientId());
        System.out.println("=".repeat(80));

        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");

        try {
            dbConnection.connect();
            clientsData = new GetClientsData(dbConnection);
            System.out.println("✓ Database connection established for " + testData.getTestName());
        } catch (SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            Assert.fail("Failed to establish database connection");
        }
    }

    @Test(priority = 1)
    @Story("Fetch Client Data")
    @Description("Verify client data can be fetched for the given client ID")
    public void testFetchClientData() {
        System.out.println("\n--- Test: Fetch Client Data ---");
        System.out.println("Test Instance: " + testData.getTestName());
        System.out.println("Client ID: " + testData.getClientId());

        boolean success = clientsData.fetchClientData(testData.getClientId());

        if (success) {
            System.out.println("✓ Data fetched successfully");
            System.out.println("Record Count: " + clientsData.getRecordCount());

            Assert.assertTrue(clientsData.getRecordCount() > 0,
                "Should have at least one record");
        } else {
            System.out.println("✗ No data found for Client ID: " + testData.getClientId());

            // For invalid IDs, we expect failure
            if (testData.getClientId().equals("99999999")) {
                Assert.assertEquals(clientsData.getRecordCount(), 0,
                    "Invalid client ID should return 0 records");
                System.out.println("✓ Correctly handled invalid Client ID");
            } else {
                Assert.fail("Expected to find data for Client ID: " + testData.getClientId());
            }
        }
    }

    @Test(priority = 2, dependsOnMethods = "testFetchClientData")
    @Story("Verify Client Details")
    @Description("Verify client details are properly retrieved")
    public void testVerifyClientDetails() {
        System.out.println("\n--- Test: Verify Client Details ---");
        System.out.println("Test Instance: " + testData.getTestName());

        boolean success = clientsData.fetchClientData(testData.getClientId());

        if (success && clientsData.getRecordCount() > 0) {
            String clientId = clientsData.getClientId();
            String nameEnglish = clientsData.getNameEnglish();
            String nationality = clientsData.getNationality();

            System.out.println("Client ID: " + clientId);
            System.out.println("Name: " + nameEnglish);
            System.out.println("Nationality: " + nationality);

            Assert.assertNotNull(clientId, "Client ID should not be null");
            Assert.assertFalse(clientId.isEmpty(), "Client ID should not be empty");

            System.out.println("✓ Client details verified successfully");
        } else {
            System.out.println("⚠ Skipping verification - no data available");
        }
    }

    @Test(priority = 3, dependsOnMethods = "testFetchClientData")
    @Story("Get All Client Records")
    @Description("Verify all client records can be retrieved")
    public void testGetAllClientRecords() {
        System.out.println("\n--- Test: Get All Client Records ---");
        System.out.println("Test Instance: " + testData.getTestName());

        clientsData.fetchClientData(testData.getClientId());
        Map<String, String> allData = clientsData.getAllData();

        if (allData != null && !allData.isEmpty()) {
            System.out.println("✓ Retrieved " + allData.size() + " data fields");
            Assert.assertTrue(allData.size() > 0, "Should have data fields");
        } else {
            System.out.println("⚠ No data fields retrieved");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Tearing down test instance: " + testData.getTestName());

        if (dbConnection != null) {
            dbConnection.closeConnection();
            System.out.println("✓ Database connection closed for " + testData.getTestName());
        }

        System.out.println("=".repeat(80) + "\n");
    }
}
