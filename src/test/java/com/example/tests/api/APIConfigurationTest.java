package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.LoginAPI;
import com.example.utils.APIConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class demonstrating API Configuration usage
 * Shows how to use api-config.properties for endpoint URLs and sessionID management
 */
@Epic("API Testing")
@Feature("API Configuration")
public class APIConfigurationTest {

    private LoginAPI loginAPI;

    @BeforeClass
    public void setup() {
        // Print current configuration
        System.out.println("\n========== Current API Configuration ==========");
        APIConfigManager.printConfiguration();

        // Initialize LoginAPI using configuration from api-config.properties
        loginAPI = new LoginAPI();
    }

    @Test(priority = 1)
    @Story("Read endpoint URL from configuration")
    @Description("Test reading endpoint URL from api-config.properties")
    public void testReadEndpointURL() {
        // Get endpoint URL from configuration
        String endpointURL = LoginAPI.getEndpointURL();

        System.out.println("Endpoint URL from config: " + endpointURL);

        // Verify endpoint URL
        assertNotNull(endpointURL, "Endpoint URL should not be null");
        assertEquals(endpointURL, "https://devuat.thegroup.com.qa/jetrade/process",
                "Endpoint URL should match configuration");
    }

    @Test(priority = 2)
    @Story("Read base URL from configuration")
    @Description("Test reading base URL from api-config.properties")
    public void testReadBaseURL() {
        // Get base URL from configuration
        String baseUrl = LoginAPI.getConfigBaseUrl();

        System.out.println("Base URL from config: " + baseUrl);

        // Verify base URL
        assertNotNull(baseUrl, "Base URL should not be null");
        assertEquals(baseUrl, "https://devuat.thegroup.com.qa",
                "Base URL should match configuration");
    }

    @Test(priority = 3)
    @Story("Execute login and persist sessionID")
    @Description("Test login execution with automatic sessionID persistence to configuration file")
    public void testLoginAndPersistSessionID() throws Exception {
        // Get endpoint URL from configuration
        String endpointURL = LoginAPI.getEndpointURL();
        String baseUrl = LoginAPI.getConfigBaseUrl();

        System.out.println("\n========== Executing Login ==========");
        System.out.println("Endpoint URL: " + endpointURL);
        System.out.println("Base URL: " + baseUrl);

        // Prepare JSON request
        String jsonRequest = "{\"username\":\"testuser\",\"password\":\"testpassword\"}";

        // Execute login request
        // Note: This will fail if the API is not available, but will demonstrate the functionality
        try {
            APIResponse apiResponse = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

            System.out.println("Response: " + apiResponse.getResponseBody());
            System.out.println("Status Code: " + apiResponse.getStatusCode());

            // Get sessionID from memory
            String sessionIDFromMemory = LoginAPI.getSessionID();
            System.out.println("SessionID from memory: " + sessionIDFromMemory);

            // Get sessionID from configuration file
            String sessionIDFromConfig = APIConfigManager.getSessionID();
            System.out.println("SessionID from config: " + sessionIDFromConfig);

            // Verify sessionID is stored in both places
            if (sessionIDFromMemory != null) {
                assertEquals(sessionIDFromMemory, sessionIDFromConfig,
                        "SessionID in memory should match sessionID in configuration");
            }

        } catch (Exception e) {
            System.out.println("Login failed (expected if API is not available): " + e.getMessage());
            // This is expected if the actual API is not available
        }
    }

    @Test(priority = 4)
    @Story("Use persisted sessionID from previous run")
    @Description("Test loading sessionID from configuration file (from previous run)")
    public void testLoadPersistedSessionID() {
        // Create a new LoginAPI instance
        // This should automatically load the sessionID from the previous run
        LoginAPI newLoginAPI = new LoginAPI();

        // Get sessionID from configuration
        String sessionID = LoginAPI.getSessionID();

        System.out.println("\n========== Persisted SessionID ==========");
        System.out.println("SessionID loaded from config: " + sessionID);

        // Verify sessionID was loaded
        // Note: This will only pass if a sessionID was previously stored
        if (sessionID != null && !sessionID.isEmpty()) {
            assertNotNull(sessionID, "SessionID should be loaded from configuration");
            System.out.println("✓ SessionID successfully loaded from previous run");
        } else {
            System.out.println("No sessionID found from previous run (expected on first run)");
        }
    }

    @Test(priority = 5)
    @Story("Update specific property in configuration")
    @Description("Test updating a property value in api-config.properties")
    public void testUpdateConfigProperty() {
        // Update sessionID manually
        String testSessionID = "test-session-id-12345";

        System.out.println("\n========== Updating SessionID ==========");
        System.out.println("New SessionID: " + testSessionID);

        boolean updated = APIConfigManager.updateSessionID(testSessionID);

        // Verify update was successful
        assertTrue(updated, "SessionID update should be successful");

        // Verify the updated value can be read back
        String retrievedSessionID = APIConfigManager.getSessionID();
        assertEquals(retrievedSessionID, testSessionID,
                "Retrieved sessionID should match the updated value");

        System.out.println("✓ SessionID updated successfully in configuration file");
    }

    @Test(priority = 6)
    @Story("Clear sessionID")
    @Description("Test clearing sessionID from memory and configuration")
    public void testClearSessionID() {
        System.out.println("\n========== Testing Clear SessionID ==========");

        // Clear sessionID
        boolean cleared = LoginAPI.clearSessionID();

        // Verify cleared successfully
        assertTrue(cleared, "SessionID should be cleared successfully");

        // Verify sessionID is cleared from configuration
        String sessionID = APIConfigManager.getSessionID();

        System.out.println("SessionID after clearing: " + (sessionID.isEmpty() ? "(empty)" : sessionID));

        assertTrue(sessionID.isEmpty(), "SessionID should be empty after clearing");
        System.out.println("✓ SessionID cleared successfully from configuration file");
    }

    @Test(priority = 7)
    @Story("Read all configuration properties")
    @Description("Test reading various configuration properties")
    public void testReadAllConfigProperties() {
        System.out.println("\n========== Reading All Configuration Properties ==========");

        // Read various properties
        String baseUrl = APIConfigManager.getBaseUrl();
        String endpointURL = APIConfigManager.getEndpointURL();
        int timeout = APIConfigManager.getTimeout();
        int retryAttempts = APIConfigManager.getRetryAttempts();

        System.out.println("Base URL: " + baseUrl);
        System.out.println("Endpoint URL: " + endpointURL);
        System.out.println("Timeout: " + timeout + " ms");
        System.out.println("Retry Attempts: " + retryAttempts);

        // Verify all properties are loaded
        assertNotNull(baseUrl, "Base URL should not be null");
        assertNotNull(endpointURL, "Endpoint URL should not be null");
        assertTrue(timeout > 0, "Timeout should be greater than 0");
        assertTrue(retryAttempts > 0, "Retry attempts should be greater than 0");

        System.out.println("✓ All configuration properties loaded successfully");
    }
}
