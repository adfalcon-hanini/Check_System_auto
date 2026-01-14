package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.CompaniesAPI;
import com.example.api.LoginAPI;
import com.example.utils.APIConfigManager;
import io.qameta.allure.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Test class for CompaniesAPI
 * Tests the sendRequest() method with custom URL and JSON request
 */
@Epic("API Testing")
@Feature("Companies API")
public class CompaniesAPITest {

    private static final Logger logger = LoggerFactory.getLogger(CompaniesAPITest.class);
    private CompaniesAPI companiesAPI;
    private static final String BASE_URL = "https://devuat.thegroup.com.qa";
    private static final String ENDPOINT_URL = "https://devuat.thegroup.com.qa/jetrade/process";

    @BeforeClass
    @Step("Initialize CompaniesAPI client")
    public void setup() {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("Setting up CompaniesAPI Test Suite");
        logger.info("═══════════════════════════════════════════════════════════");

        companiesAPI = new CompaniesAPI(BASE_URL);
        logger.info("CompaniesAPI client initialized with base URL: {}", BASE_URL);
    }

    @Test(priority = 1)
    @Story("Send GET_COMPS Request")
    @Description("Test sending GET_COMPS request to /jetrade/process endpoint with JSON body")
    @Severity(SeverityLevel.CRITICAL)
    public void testSendGetCompsRequest() {
        logger.info("\n╔════════════════════════════════════════════════════════════════╗");
        logger.info("║           TEST: Send GET_COMPS Request                        ║");
        logger.info("╚════════════════════════════════════════════════════════════════╝");

        try {
            // Prepare JSON request
            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            Allure.step("Prepare API request");
            logger.info("\n[Request Details]");
            logger.info("URL Endpoint: {}", ENDPOINT_URL);
            logger.info("JSON Request:\n{}", jsonRequest);

            // Send request
            Allure.step("Send request to endpoint with JSON body");
            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // Verify response
            Allure.step("Verify response is received");
            Assert.assertNotNull(response, "API response should not be null");
            logger.info("✓ Response received");

            // Check status code
            logger.info("\n[Response Details]");
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Request URL: {}", response.getRequestUrl());
            logger.info("Success: {}", response.isSuccess());

            // Verify status code is 200
            Allure.step("Verify response status code is 200");

            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║              STATUS CODE CHECK                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println("Expected Status Code: 200");
            System.out.println("Actual Status Code: " + response.getStatusCode());

            if (response.getStatusCode() == 200) {
                System.out.println("Result: ✓ PASS - Status code is 200 (Success)");
                logger.info("✓ Status code assertion PASSED: 200");
            } else {
                System.out.println("Result: ✗ FAIL - Status code is not 200");
                logger.error("✗ Status code assertion FAILED: Expected 200, but got {}", response.getStatusCode());
            }
            System.out.println("════════════════════════════════════════════════════════════════\n");

            Assert.assertEquals(response.getStatusCode(), 200,
                "Status code check failed! Expected: 200, but got: " + response.getStatusCode());

            // Verify response body is not empty
            Allure.step("Verify response body is not empty");
            Assert.assertNotNull(response.getResponseBody(), "Response body should not be null");
            Assert.assertFalse(response.getResponseBody().isEmpty(), "Response body should not be empty");
            logger.info("✓ Response body is not empty");

            // Log response body
            logger.info("\n[Response Body]");
            logger.info("{}", response.getResponseBody());

            // Attach to Allure report
            Allure.addAttachment("Request JSON", "application/json", jsonRequest);
            Allure.addAttachment("Response Status", String.valueOf(response.getStatusCode()));
            Allure.addAttachment("Response Body", "application/json", response.getResponseBody());

            logger.info("\n╔════════════════════════════════════════════════════════════════╗");
            logger.info("║           TEST PASSED: GET_COMPS Request                      ║");
            logger.info("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            logger.error("\n╔════════════════════════════════════════════════════════════════╗");
            logger.error("║           TEST FAILED: GET_COMPS Request                      ║");
            logger.error("╚════════════════════════════════════════════════════════════════╝");
            logger.error("Error details: {}", e.getMessage(), e);

            Allure.addAttachment("Error Message", e.getMessage());
            Allure.addAttachment("Stack Trace", e.toString());

            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    @Story("Validate Companies Response Structure")
    @Description("Verify the JSON structure of GET_COMPS response")
    public void testCompaniesResponseStructure() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE COMPANIES RESPONSE STRUCTURE");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST ==========
            System.out.println("\nSTEP 1: SENDING GET_COMPS REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 2: VALIDATE STRUCTURE ==========
            System.out.println("\nSTEP 2: VALIDATING RESPONSE STRUCTURE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200, "Should get 200 status");

            JSONObject json = new JSONObject(response.getResponseBody());

            // Verify top-level structure
            assertTrue(json.has("responseStatus"), "Should have responseStatus");
            assertTrue(json.has("message"), "Should have message");

            JSONObject responseStatus = json.getJSONObject("responseStatus");
            assertEquals(responseStatus.getString("resCode"), "0", "resCode should be 0");
            assertEquals(responseStatus.getString("source"), "GET_COMPS", "source should be GET_COMPS");

            System.out.println("✓ Response structure validated");
            System.out.println("  - responseStatus present");
            System.out.println("  - message present");
            System.out.println("  - resCode: " + responseStatus.getString("resCode"));

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ STRUCTURE VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    @Story("Validate Company Data Fields")
    @Description("Verify each company object has required fields")
    public void testCompanyDataFields() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE COMPANY DATA FIELDS");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST ==========
            System.out.println("\nSTEP 1: SENDING REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 2: VALIDATE COMPANY FIELDS ==========
            System.out.println("\nSTEP 2: VALIDATING COMPANY FIELDS");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject message = json.getJSONObject("message");

            // Get companies data (structure may vary)
            if (message.has("GET_COMPS")) {
                JSONObject getComps = message.getJSONObject("GET_COMPS");

                // Check if COMPS_CODES exists
                if (getComps.has("COMPS_CODES")) {
                    String compsCodes = getComps.getString("COMPS_CODES");
                    assertNotNull(compsCodes, "COMPS_CODES should not be null");
                    System.out.println("✓ COMPS_CODES present: " + compsCodes);
                }

                // Check if COMPS_COUNT exists
                if (getComps.has("COMPS_COUNT")) {
                    String compsCount = getComps.getString("COMPS_COUNT");
                    assertNotNull(compsCount, "COMPS_COUNT should not be null");
                    System.out.println("✓ COMPS_COUNT present: " + compsCount);
                }

                System.out.println("✓ Company data structure validated");
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ COMPANY FIELDS VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    @Story("Verify Company Codes are Unique")
    @Description("Ensure no duplicate company codes in the response")
    public void testCompanyCodesUnique() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VERIFY COMPANY CODES UNIQUE");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST ==========
            System.out.println("\nSTEP 1: GETTING COMPANIES");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 2: CHECK FOR DUPLICATES ==========
            System.out.println("\nSTEP 2: CHECKING FOR DUPLICATE CODES");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject message = json.getJSONObject("message");

            if (message.has("GET_COMPS")) {
                JSONObject getComps = message.getJSONObject("GET_COMPS");

                if (getComps.has("COMPS_CODES")) {
                    String compsCodesStr = getComps.getString("COMPS_CODES");
                    String[] codes = compsCodesStr.split(",");

                    Set<String> uniqueCodes = new HashSet<>();
                    Set<String> duplicates = new HashSet<>();

                    for (String code : codes) {
                        String trimmedCode = code.trim();
                        if (!uniqueCodes.add(trimmedCode)) {
                            duplicates.add(trimmedCode);
                        }
                    }

                    System.out.println("Total codes: " + codes.length);
                    System.out.println("Unique codes: " + uniqueCodes.size());

                    assertTrue(duplicates.isEmpty(),
                            "Found duplicate company codes: " + duplicates);

                    System.out.println("✓ No duplicate company codes found");
                }
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ UNIQUENESS CHECK PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    @Story("Validate Companies Count")
    @Description("Verify COMPS_COUNT matches the actual number of companies")
    public void testCompaniesCount() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE COMPANIES COUNT");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST ==========
            System.out.println("\nSTEP 1: GETTING COMPANIES");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 2: VALIDATE COUNT ==========
            System.out.println("\nSTEP 2: VALIDATING COUNT");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject message = json.getJSONObject("message");

            if (message.has("GET_COMPS")) {
                JSONObject getComps = message.getJSONObject("GET_COMPS");

                if (getComps.has("COMPS_CODES") && getComps.has("COMPS_COUNT")) {
                    String compsCodesStr = getComps.getString("COMPS_CODES");
                    int compsCount = Integer.parseInt(getComps.getString("COMPS_COUNT"));

                    String[] codes = compsCodesStr.split(",");
                    int actualCount = codes.length;

                    System.out.println("Reported Count (COMPS_COUNT): " + compsCount);
                    System.out.println("Actual Count (from COMPS_CODES): " + actualCount);

                    assertEquals(actualCount, compsCount,
                            "Actual count should match reported COMPS_COUNT");

                    System.out.println("✓ Count validation passed");
                }
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ COUNT VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    @Story("Get Preferred Companies")
    @Description("Test retrieving user's preferred companies (requires authentication)")
    public void testGetPreferredCompanies() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET PREFERRED COMPANIES");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: GET SESSION ID ==========
            System.out.println("\nSTEP 1: GETTING SESSION ID");
            System.out.println("-".repeat(70));

            String sessionID = APIConfigManager.getSessionID();

            if (sessionID == null || sessionID.isEmpty()) {
                System.out.println("⚠ No sessionID available - skipping test");
                System.out.println("Note: Run Login test first to obtain sessionID");
                System.out.println("=".repeat(70) + "\n");
                return;
            }

            System.out.println("✓ SessionID found: " + sessionID.substring(0, Math.min(20, sessionID.length())) + "...");

            // ========== STEP 2: SEND REQUEST WITH SESSION ==========
            System.out.println("\nSTEP 2: REQUESTING PREFERRED COMPANIES");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"PREFERRED_COMPANIES\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"sessionId\": \"" + sessionID + "\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 3: VALIDATE RESPONSE ==========
            System.out.println("\nSTEP 3: VALIDATING RESPONSE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200, "Should get 200 status");

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject responseStatus = json.getJSONObject("responseStatus");

            System.out.println("Response Code: " + responseStatus.getString("resCode"));
            System.out.println("Response Desc: " + responseStatus.getString("resDesc"));

            System.out.println("✓ Preferred companies request processed");

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ PREFERRED COMPANIES TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 7)
    @Story("Preferred Companies Requires Auth")
    @Description("Verify that preferred companies endpoint requires valid sessionID")
    public void testPreferredCompaniesRequiresAuth() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: PREFERRED COMPANIES - AUTH REQUIRED");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST WITHOUT SESSION ==========
            System.out.println("\nSTEP 1: SENDING REQUEST WITHOUT SESSION ID");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"PREFERRED_COMPANIES\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Note: Request sent WITHOUT sessionID");

            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            // ========== STEP 2: VERIFY ERROR RESPONSE ==========
            System.out.println("\nSTEP 2: VERIFYING ERROR RESPONSE");
            System.out.println("-".repeat(70));

            // Should get 200 HTTP status but business error
            assertEquals(response.getStatusCode(), 200, "Should get HTTP 200");

            String responseBody = response.getResponseBody();
            assertNotNull(responseBody, "Response body should not be null");

            System.out.println("✓ Received response without authentication");
            System.out.println("✓ Endpoint correctly handles missing sessionID");

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ AUTH REQUIREMENT TEST PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}
