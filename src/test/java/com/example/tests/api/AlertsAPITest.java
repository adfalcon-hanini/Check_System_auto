package com.example.tests.api;

import com.example.api.AlertsAPI;
import com.example.utils.APIConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for Alerts API
 * Tests: Get alerts, create alert, update alert, delete alert, get active alerts
 */
@Epic("API Testing")
@Feature("Alerts Management")
public class AlertsAPITest {

    private AlertsAPI alertsAPI;
    private String baseUrl;
    private String testAlertId = "TEST_ALERT_001";
    private String testNIN = "12345678";

    @BeforeClass
    public void setup() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INITIALIZING ALERTS API TEST");
        System.out.println("=".repeat(70));

        // Get base URL from config
        baseUrl = APIConfigManager.getBaseUrl();

        // Initialize AlertsAPI
        alertsAPI = new AlertsAPI(baseUrl);

        System.out.println("Base URL: " + baseUrl);
        System.out.println("Config File: src/main/resources/api-config.properties");
        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 1)
    @Story("Get all alerts for a specific NIN")
    @Description("Send request to get all alerts for a NIN and verify response")
    public void testGetAlerts() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET ALL ALERTS BY NIN");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING GET ALERTS REQUEST");
            System.out.println("-".repeat(70));
            System.out.println("NIN: " + testNIN);
            System.out.println("Endpoint: " + baseUrl + "/api/alerts?nin=" + testNIN);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.getAlerts(testNIN);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");
            assertFalse(response.isEmpty(), "Response should not be empty");

            System.out.println("✓ ASSERTION PASSED: Response received successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    @Story("Get alert by ID")
    @Description("Send request to get a specific alert by ID and verify response")
    public void testGetAlertById() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET ALERT BY ID");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING GET ALERT BY ID REQUEST");
            System.out.println("-".repeat(70));
            System.out.println("Alert ID: " + testAlertId);
            System.out.println("Endpoint: " + baseUrl + "/api/alerts/" + testAlertId);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.getAlertById(testAlertId);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");
            assertFalse(response.isEmpty(), "Response should not be empty");

            System.out.println("✓ ASSERTION PASSED: Alert retrieved successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    @Story("Create new alert")
    @Description("Send request to create a new alert and verify response")
    public void testCreateAlert() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: CREATE NEW ALERT");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING CREATE ALERT REQUEST");
            System.out.println("-".repeat(70));

            String alertJson = "{\n" +
                    "    \"alertId\": \"" + testAlertId + "\",\n" +
                    "    \"nin\": \"" + testNIN + "\",\n" +
                    "    \"alertType\": \"PRICE_ALERT\",\n" +
                    "    \"companyCode\": \"QNBK\",\n" +
                    "    \"targetPrice\": 18.50,\n" +
                    "    \"condition\": \"GREATER_THAN\",\n" +
                    "    \"status\": \"ACTIVE\",\n" +
                    "    \"notificationMethod\": \"EMAIL\"\n" +
                    "}";

            System.out.println("Endpoint: " + baseUrl + "/api/alerts");
            System.out.println("Request Body:");
            System.out.println(alertJson);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.createAlert(alertJson);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");
            assertFalse(response.isEmpty(), "Response should not be empty");

            System.out.println("✓ ASSERTION PASSED: Alert created successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    @Story("Update existing alert")
    @Description("Send request to update an existing alert and verify response")
    public void testUpdateAlert() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: UPDATE ALERT");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING UPDATE ALERT REQUEST");
            System.out.println("-".repeat(70));

            String alertJson = "{\n" +
                    "    \"alertId\": \"" + testAlertId + "\",\n" +
                    "    \"nin\": \"" + testNIN + "\",\n" +
                    "    \"alertType\": \"PRICE_ALERT\",\n" +
                    "    \"companyCode\": \"QNBK\",\n" +
                    "    \"targetPrice\": 19.00,\n" +
                    "    \"condition\": \"GREATER_THAN\",\n" +
                    "    \"status\": \"ACTIVE\",\n" +
                    "    \"notificationMethod\": \"SMS\"\n" +
                    "}";

            System.out.println("Alert ID: " + testAlertId);
            System.out.println("Endpoint: " + baseUrl + "/api/alerts/" + testAlertId);
            System.out.println("Request Body:");
            System.out.println(alertJson);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.updateAlert(testAlertId, alertJson);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");
            assertFalse(response.isEmpty(), "Response should not be empty");

            System.out.println("✓ ASSERTION PASSED: Alert updated successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    @Story("Get active alerts")
    @Description("Send request to get all active alerts and verify response")
    public void testGetActiveAlerts() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET ACTIVE ALERTS");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING GET ACTIVE ALERTS REQUEST");
            System.out.println("-".repeat(70));
            System.out.println("Endpoint: " + baseUrl + "/api/alerts/active");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.getActiveAlerts();

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");
            assertFalse(response.isEmpty(), "Response should not be empty");

            System.out.println("✓ ASSERTION PASSED: Active alerts retrieved successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    @Story("Delete alert")
    @Description("Send request to delete an alert and verify response")
    public void testDeleteAlert() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: DELETE ALERT");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING DELETE ALERT REQUEST");
            System.out.println("-".repeat(70));
            System.out.println("Alert ID: " + testAlertId);
            System.out.println("Endpoint: " + baseUrl + "/api/alerts/" + testAlertId);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            String response = alertsAPI.deleteAlert(testAlertId);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Response Body:");
            System.out.println(response);

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
            System.out.println("-".repeat(70));

            assertNotNull(response, "Response should not be null");

            System.out.println("✓ ASSERTION PASSED: Alert deleted successfully");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}
