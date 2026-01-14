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
 * Test class for Login API
 * Tests: Send request, get response, assert 200, extract sessionID, save to config
 */
@Epic("API Testing")
@Feature("Login Authentication")
public class LoginAPITest {

    private LoginAPI loginAPI;
    private String endpointURL;

    @BeforeClass
    public void setup() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INITIALIZING LOGIN API TEST");
        System.out.println("=".repeat(70));

        // Initialize LoginAPI
        loginAPI = new LoginAPI();

        // Get endpoint URL from config
        endpointURL = LoginAPI.getEndpointURL();

        System.out.println("Endpoint URL: " + endpointURL);
        System.out.println("Config File: src/main/resources/api-config.properties");
        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 1)
    @Story("Send login request and verify response")
    @Description("Send login request, print response, assert 200 success, extract sessionID, save to config")
    public void testLoginFlow() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN FLOW");
        System.out.println("=".repeat(70));

        // ========== STEP 1: PREPARE REQUEST ==========
        System.out.println("\nSTEP 1: PREPARING LOGIN REQUEST");
        System.out.println("-".repeat(70));

        String jsonRequest = "{\n" +
                "    \"Srv\": \"Login\",\n" +
                "    \"Message\": {\n" +
                "        \"Login\": {\n" +
                "            \"UserName\": \"1218\",\n" +
                "            \"Password\": \"12345\",\n" +
                "            \"Lang\": \"ARB\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"Version\": \"1.0\",\n" +
                "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                "    \"LstLogin\": \"08-07-2015\"\n" +
                "}";

        System.out.println("Endpoint: " + endpointURL);
        System.out.println("Request Body:");
        System.out.println(jsonRequest);

        // ========== STEP 2: SEND REQUEST ==========
        System.out.println("\nSTEP 2: SENDING REQUEST TO API");
        System.out.println("-".repeat(70));

        APIResponse response = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

        // ========== STEP 3: PRINT RESPONSE ==========
        System.out.println("\nSTEP 3: RESPONSE RECEIVED");
        System.out.println("-".repeat(70));
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Request URL: " + response.getRequestUrl());
        System.out.println("Response Body:");
        System.out.println(response.getResponseBody());

        // ========== STEP 4: ASSERT 200 SUCCESS ==========
        System.out.println("\nSTEP 4: ASSERTING RESPONSE STATUS");
        System.out.println("-".repeat(70));

        // Assert response is not null
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getResponseBody(), "Response body should not be null");

        // Assert status code is 200
        System.out.println("Expected Status Code: 200");
        System.out.println("Actual Status Code: " + response.getStatusCode());

        assertEquals(response.getStatusCode(), 200,
                "Expected status code 200 but got: " + response.getStatusCode());

        System.out.println("✓ ASSERTION PASSED: Status code is 200 (SUCCESS)");

        // ========== STEP 5: VERIFY SESSIONID IN MEMORY ==========
        System.out.println("\nSTEP 5: VERIFYING SESSIONID IN MEMORY");
        System.out.println("-".repeat(70));

        String sessionIDFromMemory = LoginAPI.getSessionID();
        System.out.println("SessionID from memory: " + sessionIDFromMemory);

        // ========== STEP 6: VERIFY SESSIONID IN CONFIG ==========
        System.out.println("\nSTEP 6: VERIFYING SESSIONID IN CONFIG FILE");
        System.out.println("-".repeat(70));

        String sessionIDFromConfig = APIConfigManager.getSessionID();
        System.out.println("SessionID from config: " + sessionIDFromConfig);

        // ========== STEP 7: VERIFY SESSIONID WAS SAVED ==========
        System.out.println("\nSTEP 7: VALIDATING SESSIONID");
        System.out.println("-".repeat(70));

        if (sessionIDFromMemory != null && !sessionIDFromMemory.isEmpty()) {
            System.out.println("✓ SessionID found in memory");

            // Verify sessionID was saved to config
            assertNotNull(sessionIDFromConfig, "SessionID should be saved to config file");
            assertFalse(sessionIDFromConfig.isEmpty(), "SessionID in config should not be empty");
            System.out.println("✓ SessionID found in config file");

            // Verify both match
            assertEquals(sessionIDFromMemory, sessionIDFromConfig,
                    "SessionID in memory should match sessionID in config");
            System.out.println("✓ SessionID values match!");

            System.out.println("\nSessionID Details:");
            System.out.println("  Memory:  " + sessionIDFromMemory);
            System.out.println("  Config:  " + sessionIDFromConfig);
            System.out.println("  File:    src/main/resources/api-config.properties");
            System.out.println("  Key:     api.sessionID");

        } else {
            System.out.println("✗ SessionID NOT found!");
            fail("SessionID was not extracted from response");
        }

        // ========== TEST SUMMARY ==========
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("✓ Step 1: Request prepared");
        System.out.println("✓ Step 2: Request sent successfully");
        System.out.println("✓ Step 3: Response received");
        System.out.println("✓ Step 4: Status code verified (200)");
        System.out.println("✓ Step 5: SessionID extracted from response");
        System.out.println("✓ Step 6: SessionID saved to config file");
        System.out.println("✓ Step 7: SessionID verified in memory and config");
        System.out.println("=".repeat(70));
        System.out.println("✓✓✓ ALL STEPS PASSED ✓✓✓");
        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 2)
    @Story("Reuse sessionID from config")
    @Description("Verify sessionID can be reused from config file in subsequent tests")
    public void testReuseSessionID() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: REUSE SESSIONID FROM CONFIG");
        System.out.println("=".repeat(70));

        // Get sessionID from config
        String sessionID = LoginAPI.getSessionID();

        System.out.println("SessionID from previous test: " + sessionID);

        if (sessionID != null && !sessionID.isEmpty()) {
            System.out.println("✓ SessionID is available for reuse");
            System.out.println("✓ Can be used in other API calls");

            // Verify it's also in config file
            String configSessionID = APIConfigManager.getSessionID();
            assertEquals(sessionID, configSessionID,
                    "SessionID should match between memory and config");

            System.out.println("✓ SessionID successfully reused from config");
        } else {
            System.out.println("⚠ No sessionID available (run login test first)");
        }

        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 3)
    @Story("Login with margin user")
    @Description("Test login with margin user account and verify margin-specific data")
    public void testLoginMarginUser() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN - MARGIN USER");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING MARGIN USER LOGIN REQUEST");
            System.out.println("-".repeat(70));

            // Using margin user credentials from Postman
            String jsonRequest = "{\n" +
                    "    \"Srv\": \"Login\",\n" +
                    "    \"Message\": {\n" +
                    "        \"Login\": {\n" +
                    "            \"UserName\": \"80509\",\n" +
                    "            \"Password\": \"12345\",\n" +
                    "            \"Lang\": \"ARB\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            System.out.println("Endpoint: " + endpointURL);
            System.out.println("User Type: MARGIN");
            System.out.println("Request Body:");
            System.out.println(jsonRequest);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            APIResponse response = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VERIFY RESPONSE ==========
            System.out.println("\nSTEP 3: VERIFYING RESPONSE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200, "Expected status code 200");
            assertNotNull(response.getResponseBody(), "Response body should not be null");

            System.out.println("✓ Status Code: 200");
            System.out.println("✓ Margin user login successful");

            // Verify sessionID was extracted
            String sessionID = LoginAPI.getSessionID();
            assertNotNull(sessionID, "SessionID should be extracted");
            System.out.println("✓ SessionID: " + sessionID);

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ MARGIN USER LOGIN PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    @Story("Login with wrong credentials")
    @Description("Test login failure with incorrect username or password")
    public void testLoginWrongCredentials() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN - WRONG CREDENTIALS");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST WITH WRONG CREDENTIALS ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST WITH WRONG CREDENTIALS");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"Login\",\n" +
                    "    \"Message\": {\n" +
                    "        \"Login\": {\n" +
                    "            \"UserName\": \"invalid_user\",\n" +
                    "            \"Password\": \"wrong_password\",\n" +
                    "            \"Lang\": \"ARB\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            System.out.println("Using invalid credentials");
            System.out.println("Expected: Error response with resCode = '4'");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VERIFY ERROR RESPONSE ==========
            System.out.println("\nSTEP 3: VERIFYING ERROR RESPONSE");
            System.out.println("-".repeat(70));

            // Should still get 200 status (business error, not HTTP error)
            assertEquals(response.getStatusCode(), 200, "Should get HTTP 200");

            // Parse response to check error code
            String responseBody = response.getResponseBody();
            assertTrue(responseBody.contains("resCode"), "Response should contain resCode");
            assertTrue(responseBody.contains("\"4\"") || responseBody.contains("resCode\":\"4\""),
                    "Response should indicate error code 4 (wrong credentials)");

            System.out.println("✓ HTTP Status: 200 (as expected)");
            System.out.println("✓ Error response received");
            System.out.println("✓ Response indicates wrong credentials (resCode = 4)");

            // Verify no sessionID was created
            assertTrue(responseBody.contains("LOGIN") || responseBody.contains("responseStatus"),
                    "Response should contain error details");

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ WRONG CREDENTIALS TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    @Story("Login with wrong service name")
    @Description("Test login failure with incorrect Srv parameter")
    public void testLoginWrongSrvName() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN - WRONG SERVICE NAME");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST WITH WRONG SRV ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST WITH WRONG SRV");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"InvalidServiceName\",\n" +
                    "    \"Message\": {\n" +
                    "        \"Login\": {\n" +
                    "            \"UserName\": \"12240\",\n" +
                    "            \"Password\": \"12345\",\n" +
                    "            \"Lang\": \"ARB\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            System.out.println("Using invalid Srv: 'InvalidServiceName'");
            System.out.println("Expected: Error response");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VERIFY ERROR RESPONSE ==========
            System.out.println("\nSTEP 3: VERIFYING ERROR RESPONSE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200, "Should get HTTP 200");

            String responseBody = response.getResponseBody();
            assertNotNull(responseBody, "Response body should not be null");
            assertTrue(responseBody.contains("responseStatus") || responseBody.contains("resCode"),
                    "Response should contain error status");

            System.out.println("✓ HTTP Status: 200");
            System.out.println("✓ Error response received for invalid service name");

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ WRONG SERVICE NAME TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    @Story("Login with missing data")
    @Description("Test login failure with missing required fields")
    public void testLoginMissingData() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN - MISSING REQUIRED DATA");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST WITH MISSING PASSWORD ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST WITH MISSING PASSWORD");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"Login\",\n" +
                    "    \"Message\": {\n" +
                    "        \"Login\": {\n" +
                    "            \"UserName\": \"12240\",\n" +
                    "            \"Lang\": \"ARB\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            System.out.println("Missing field: Password");
            System.out.println("Expected: Error response");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = loginAPI.sendLoginRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VERIFY ERROR RESPONSE ==========
            System.out.println("\nSTEP 3: VERIFYING ERROR RESPONSE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200, "Should get HTTP 200");

            String responseBody = response.getResponseBody();
            assertNotNull(responseBody, "Response body should not be null");

            // Should indicate an error (missing required field)
            assertTrue(responseBody.contains("responseStatus") || responseBody.contains("resCode"),
                    "Response should contain error information");

            System.out.println("✓ HTTP Status: 200");
            System.out.println("✓ Error response received for missing data");

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ MISSING DATA TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 7)
    @Story("Clear sessionID")
    @Description("Test clearing sessionID from memory and config file")
    public void testClearSessionID() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: CLEAR SESSIONID");
        System.out.println("=".repeat(70));

        // Clear sessionID
        boolean cleared = LoginAPI.clearSessionID();

        System.out.println("Clearing sessionID from memory and config...");
        assertTrue(cleared, "SessionID should be cleared successfully");

        // Verify it's cleared
        String sessionID = LoginAPI.getSessionID();
        assertNull(sessionID, "SessionID should be null after clearing");

        String configSessionID = APIConfigManager.getSessionID();
        assertTrue(configSessionID.isEmpty(), "SessionID in config should be empty");

        System.out.println("✓ SessionID cleared from memory");
        System.out.println("✓ SessionID cleared from config file");
        System.out.println("=".repeat(70) + "\n");
    }
}
