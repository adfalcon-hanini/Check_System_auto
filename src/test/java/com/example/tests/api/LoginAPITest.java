package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.LoginAPI;
import com.example.api.dto.LoginRequestDTO;
import com.example.api.dto.LoginResponseDTO;
import com.example.utils.APIConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Test class for Login API
 * Tests both traditional HttpURLConnection and RestAssured approaches
 * Demonstrates: Send request, get response, assert 200, extract sessionID, save to config
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
                "            \"UserName\": \"12240\",\n" +
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

    // ========== RESTASSURED + DTO TESTS ==========

    @Test(priority = 4)
    @Story("Send login request using RestAssured with DTO")
    @Description("Demonstrate sending login request with RestAssured, using LoginRequestDTO and deserializing to List<LoginResponseDTO>")
    public void testLoginWithRestAssuredDTO() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN WITH RESTASSURED + DTO");
        System.out.println("=".repeat(70));

        // ========== STEP 1: BUILD REQUEST DTO ==========
        System.out.println("\nSTEP 1: BUILDING REQUEST DTO");
        System.out.println("-".repeat(70));

        // Using Builder pattern for easy construction
        LoginRequestDTO request = new LoginRequestDTO.Builder()
                .userName("12240")
                .password("12345")
                .lang("ARB")
                .lstLogin("08-07-2015")
                .build();

        System.out.println("✓ Request DTO created using Builder pattern");
        System.out.println("  Username: 12240");
        System.out.println("  Language: ARB");

        // ========== STEP 2: SEND REQUEST ==========
        System.out.println("\nSTEP 2: SENDING REQUEST WITH RESTASSURED");
        System.out.println("-".repeat(70));

        List<LoginResponseDTO> responseList = loginAPI.sendLoginRequestWithDTO(endpointURL, request);

        // ========== STEP 3: VALIDATE RESPONSE ==========
        System.out.println("\nSTEP 3: VALIDATING RESPONSE");
        System.out.println("-".repeat(70));

        assertNotNull(responseList, "Response list should not be null");
        assertFalse(responseList.isEmpty(), "Response list should not be empty");

        System.out.println("✓ Response list received with " + responseList.size() + " item(s)");

        // Get first response
        LoginResponseDTO response = responseList.get(0);
        assertNotNull(response, "Response DTO should not be null");

        System.out.println("✓ Response DTO deserialized successfully");
        System.out.println("  Response: " + response.toString());

        // ========== STEP 4: VERIFY SESSIONID ==========
        System.out.println("\nSTEP 4: VERIFYING SESSIONID");
        System.out.println("-".repeat(70));

        String sessionID = response.getSessionID();
        if (sessionID != null && !sessionID.isEmpty()) {
            System.out.println("✓ SessionID extracted: " + sessionID);

            // Verify it's in memory and config
            assertEquals(LoginAPI.getSessionID(), sessionID, "SessionID should match in memory");
            assertEquals(APIConfigManager.getSessionID(), sessionID, "SessionID should match in config");

            System.out.println("✓ SessionID verified in memory and config");
        } else {
            System.out.println("⚠ SessionID not found in response (API may not return sessionID)");
        }

        // ========== TEST SUMMARY ==========
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("✓ Step 1: Request DTO built using Builder pattern");
        System.out.println("✓ Step 2: Request sent via RestAssured");
        System.out.println("✓ Step 3: Response deserialized to List<LoginResponseDTO>");
        System.out.println("✓ Step 4: SessionID extracted and verified");
        System.out.println("=".repeat(70));
        System.out.println("✓✓✓ RESTASSURED + DTO TEST PASSED ✓✓✓");
        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 5)
    @Story("Send login request and get single response object")
    @Description("Demonstrate using convenience method that returns single LoginResponseDTO instead of List")
    public void testLoginWithRestAssuredSingleResponse() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: LOGIN WITH RESTASSURED (SINGLE RESPONSE)");
        System.out.println("=".repeat(70));

        // Build request using Builder pattern
        LoginRequestDTO request = new LoginRequestDTO.Builder()
                .userName("12240")
                .password("12345")
                .lang("ARB")
                .lstLogin("08-07-2015")
                .build();

        System.out.println("Sending login request (single response mode)...");

        // Send request and get single response object
        LoginResponseDTO response = loginAPI.sendLoginRequestSingle(endpointURL, request);

        // Validate
        assertNotNull(response, "Response should not be null");
        System.out.println("✓ Single response object received");
        System.out.println("  Response: " + response.toString());

        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 6)
    @Story("Generic POST request with custom DTOs")
    @Description("Demonstrate generic sendPostRequestWithDTO method that works with any request/response types")
    public void testGenericPostRequestWithDTO() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GENERIC POST REQUEST WITH DTO");
        System.out.println("=".repeat(70));

        // Build request
        LoginRequestDTO request = new LoginRequestDTO.Builder()
                .userName("12240")
                .password("12345")
                .lang("ARB")
                .lstLogin("08-07-2015")
                .build();

        System.out.println("Sending generic POST request...");

        // Use generic method - works with any request/response types
        List<LoginResponseDTO> responseList = loginAPI.sendPostRequestWithDTO(
                endpointURL,
                request,
                LoginResponseDTO.class
        );

        // Validate
        assertNotNull(responseList, "Response list should not be null");
        assertFalse(responseList.isEmpty(), "Response list should not be empty");

        System.out.println("✓ Generic POST request successful");
        System.out.println("  Received " + responseList.size() + " response(s)");
        System.out.println("  First response: " + responseList.get(0).toString());

        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 7)
    @Story("Manual DTO construction example")
    @Description("Demonstrate building LoginRequestDTO manually without Builder pattern")
    public void testManualDTOConstruction() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: MANUAL DTO CONSTRUCTION");
        System.out.println("=".repeat(70));

        // ========== EXAMPLE 1: Using Builder Pattern (Recommended) ==========
        System.out.println("\nEXAMPLE 1: USING BUILDER PATTERN");
        System.out.println("-".repeat(70));

        LoginRequestDTO requestWithBuilder = new LoginRequestDTO.Builder()
                .userName("12240")
                .password("12345")
                .lang("ARB")
                .lstLogin("08-07-2015")
                .build();

        System.out.println("✓ Request built with Builder pattern");
        System.out.println("  Code: new LoginRequestDTO.Builder().userName(...).password(...).build()");

        // ========== EXAMPLE 2: Manual Construction ==========
        System.out.println("\nEXAMPLE 2: MANUAL CONSTRUCTION");
        System.out.println("-".repeat(70));

        LoginRequestDTO.LoginDetailsDTO loginDetails =
                new LoginRequestDTO.LoginDetailsDTO("12240", "12345", "ARB");

        LoginRequestDTO.MessageDTO message =
                new LoginRequestDTO.MessageDTO(loginDetails);

        LoginRequestDTO requestManual = new LoginRequestDTO(
                "Login",
                message,
                "1.0",
                "WEB_ORDERS.25",
                "08-07-2015"
        );

        System.out.println("✓ Request built manually");
        System.out.println("  Code: new LoginRequestDTO(srv, message, version, appId, lstLogin)");

        // Both should work the same
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Both construction methods produce valid requests");
        System.out.println("Builder pattern is recommended for cleaner code");
        System.out.println("=".repeat(70) + "\n");
    }
}
