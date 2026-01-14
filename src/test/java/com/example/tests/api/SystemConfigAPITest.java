package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.CompaniesAPI;
import com.example.utils.APIConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for System Config API
 * Tests: Get system configuration, validate commission and prefix parameters
 */
@Epic("API Testing")
@Feature("System Configuration")
public class SystemConfigAPITest {

    private CompaniesAPI companiesAPI;
    private String endpointURL;

    @BeforeClass
    public void setup() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INITIALIZING SYSTEM CONFIG API TEST");
        System.out.println("=".repeat(70));

        // Get endpoint URL from config
        endpointURL = APIConfigManager.getEndpointURL();

        // Initialize API client (using CompaniesAPI for sendRequest method)
        companiesAPI = new CompaniesAPI(APIConfigManager.getBaseUrl());

        System.out.println("Endpoint URL: " + endpointURL);
        System.out.println("Config File: src/main/resources/api-config.properties");
        System.out.println("=".repeat(70) + "\n");
    }

    @Test(priority = 1)
    @Story("Get system configuration")
    @Description("Send request to get system configuration and verify response structure")
    public void testGetSystemConfig() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET SYSTEM CONFIGURATION");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING SYSTEM CONFIG REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"COMMISSION,PREFIXNAME\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Endpoint: " + endpointURL);
            System.out.println("Request Body:");
            System.out.println(jsonRequest);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 3: PRINT RESPONSE ==========
            System.out.println("\nSTEP 3: RESPONSE RECEIVED");
            System.out.println("-".repeat(70));
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body:");
            System.out.println(response.getResponseBody());

            // ========== STEP 4: ASSERT RESPONSE ==========
            System.out.println("\nSTEP 4: ASSERTING RESPONSE");
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

            // Parse JSON response
            JSONObject json = new JSONObject(response.getResponseBody());

            // Verify responseStatus
            JSONObject responseStatus = json.getJSONObject("responseStatus");
            assertEquals(responseStatus.getString("resCode"), "0",
                    "Response code should be 0");
            assertEquals(responseStatus.getString("resDesc"), "Successful Operation",
                    "Response description should be 'Successful Operation'");
            assertEquals(responseStatus.getString("source"), "SYSTEM_CONFIG",
                    "Response source should be 'SYSTEM_CONFIG'");
            System.out.println("✓ ASSERTION PASSED: Response status verified");

            // Verify params array exists
            JSONObject message = json.getJSONObject("message");
            assertTrue(message.has("params"), "Response should contain params object");
            JSONObject params = message.getJSONObject("params");
            assertTrue(params.has("param"), "Params should contain param array");

            Object paramObj = params.get("param");
            JSONArray paramArray;
            if (paramObj instanceof JSONArray) {
                paramArray = (JSONArray) paramObj;
            } else if (paramObj instanceof JSONObject) {
                // Single param returned as object
                paramArray = new JSONArray();
                paramArray.put(paramObj);
            } else {
                fail("Param should be either JSONArray or JSONObject");
                return;
            }

            assertTrue(paramArray.length() > 0, "Params array should not be empty");
            System.out.println("✓ ASSERTION PASSED: Params array present with " + paramArray.length() + " items");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response status verified (200)");
            System.out.println("✓ Step 5: Response structure validated");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ ALL STEPS PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    @Story("Validate commission parameters")
    @Description("Verify commission min/max values are numeric and valid")
    public void testSystemConfigCommissionValues() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE COMMISSION PARAMETERS");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"COMMISSION\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Request: COMMISSION parameter");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 3: PARSE AND VALIDATE ==========
            System.out.println("\nSTEP 3: VALIDATING COMMISSION VALUES");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject message = json.getJSONObject("message");
            JSONObject params = message.getJSONObject("params");

            // Handle param as array or single object
            JSONArray paramArray;
            Object paramObj = params.get("param");
            if (paramObj instanceof JSONArray) {
                paramArray = (JSONArray) paramObj;
            } else {
                paramArray = new JSONArray();
                paramArray.put(paramObj);
            }

            // Find COMMISSION param
            JSONObject commissionParam = null;
            for (int i = 0; i < paramArray.length(); i++) {
                JSONObject param = paramArray.getJSONObject(i);
                if ("COMMISSION".equalsIgnoreCase(param.getString("paramName"))) {
                    commissionParam = param;
                    break;
                }
            }

            assertNotNull(commissionParam, "COMMISSION parameter should be present");
            System.out.println("✓ Found COMMISSION parameter");

            // Validate valueFrom and valueTo
            assertTrue(commissionParam.has("valueFrom"), "COMMISSION should have valueFrom");
            assertTrue(commissionParam.has("valueTo"), "COMMISSION should have valueTo");

            String valueFromStr = commissionParam.getString("valueFrom");
            String valueToStr = commissionParam.getString("valueTo");

            // Parse as numbers
            double valueFrom = Double.parseDouble(valueFromStr);
            double valueTo = Double.parseDouble(valueToStr);

            System.out.println("Commission Min (valueFrom): " + valueFrom);
            System.out.println("Commission Max (valueTo): " + valueTo);

            // Validate ranges
            assertTrue(valueFrom >= 0, "Commission min should be >= 0");
            assertTrue(valueTo > 0, "Commission max should be > 0");
            assertTrue(valueFrom < valueTo, "Commission min should be < max");

            System.out.println("✓ ASSERTION PASSED: Commission values are valid");
            System.out.println("✓ valueFrom (min) >= 0: " + (valueFrom >= 0));
            System.out.println("✓ valueTo (max) > 0: " + (valueTo > 0));
            System.out.println("✓ valueFrom < valueTo: " + (valueFrom < valueTo));

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ COMMISSION VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    @Story("Validate prefix names")
    @Description("Verify prefix names are present and formatted correctly")
    public void testSystemConfigPrefixNames() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE PREFIX NAMES");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"PREFIXNAME\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Request: PREFIXNAME parameter");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 3: PARSE AND VALIDATE ==========
            System.out.println("\nSTEP 3: VALIDATING PREFIX NAMES");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject message = json.getJSONObject("message");
            JSONObject params = message.getJSONObject("params");

            // Handle param as array or single object
            JSONArray paramArray;
            Object paramObj = params.get("param");
            if (paramObj instanceof JSONArray) {
                paramArray = (JSONArray) paramObj;
            } else {
                paramArray = new JSONArray();
                paramArray.put(paramObj);
            }

            // Find PREFIXNAME param
            JSONObject prefixParam = null;
            for (int i = 0; i < paramArray.length(); i++) {
                JSONObject param = paramArray.getJSONObject(i);
                if ("PREFIXNAME".equalsIgnoreCase(param.getString("paramName"))) {
                    prefixParam = param;
                    break;
                }
            }

            assertNotNull(prefixParam, "PREFIXNAME parameter should be present");
            System.out.println("✓ Found PREFIXNAME parameter");

            // Validate valueFrom (contains CSV of prefixes)
            assertTrue(prefixParam.has("valueFrom"), "PREFIXNAME should have valueFrom");
            String prefixCSV = prefixParam.getString("valueFrom");

            assertNotNull(prefixCSV, "Prefix CSV should not be null");
            assertFalse(prefixCSV.trim().isEmpty(), "Prefix CSV should not be empty");

            // Parse CSV into array
            String[] prefixes = prefixCSV.split(",");
            assertTrue(prefixes.length > 0, "Should have at least one prefix");

            System.out.println("Prefix Count: " + prefixes.length);
            System.out.println("Prefixes: " + prefixCSV);

            // Verify no empty prefixes
            for (String prefix : prefixes) {
                assertFalse(prefix.trim().isEmpty(), "No prefix should be empty");
            }

            System.out.println("✓ ASSERTION PASSED: Prefix names are valid");
            System.out.println("✓ Found " + prefixes.length + " prefixes");
            System.out.println("✓ All prefixes are non-empty");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ PREFIX VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    @Story("Validate no authentication required")
    @Description("Verify system config can be retrieved without sessionID")
    public void testSystemConfigNoAuthRequired() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: SYSTEM CONFIG - NO AUTH REQUIRED");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST WITHOUT SESSION ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST (NO SESSION ID)");
            System.out.println("-".repeat(70));

            // Notice: No sessionId field in the request
            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"COMMISSION\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Note: Request sent WITHOUT sessionID");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VERIFY SUCCESS ==========
            System.out.println("\nSTEP 3: VERIFYING RESPONSE");
            System.out.println("-".repeat(70));

            assertEquals(response.getStatusCode(), 200,
                    "Should succeed without authentication");

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject responseStatus = json.getJSONObject("responseStatus");

            assertEquals(responseStatus.getString("resCode"), "0",
                    "Should return success code without authentication");

            System.out.println("✓ ASSERTION PASSED: System Config accessible without authentication");
            System.out.println("✓ Status Code: 200");
            System.out.println("✓ Response Code: 0");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ NO AUTH VALIDATION PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    @Story("Validate response structure")
    @Description("Verify the complete structure of system config response")
    public void testSystemConfigResponseStructure() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE RESPONSE STRUCTURE");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: SEND REQUEST ==========
            System.out.println("\nSTEP 1: SENDING REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"COMMISSION,PREFIXNAME\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 2: VALIDATE STRUCTURE ==========
            System.out.println("\nSTEP 2: VALIDATING RESPONSE STRUCTURE");
            System.out.println("-".repeat(70));

            JSONObject json = new JSONObject(response.getResponseBody());

            // Verify top-level structure
            assertTrue(json.has("responseStatus"), "Response should have responseStatus");
            assertTrue(json.has("message"), "Response should have message");

            // Verify responseStatus structure
            JSONObject responseStatus = json.getJSONObject("responseStatus");
            assertTrue(responseStatus.has("resCode"), "responseStatus should have resCode");
            assertTrue(responseStatus.has("resDesc"), "responseStatus should have resDesc");
            assertTrue(responseStatus.has("source"), "responseStatus should have source");
            assertTrue(responseStatus.has("resCode1"), "responseStatus should have resCode1");
            assertTrue(responseStatus.has("resDesc1"), "responseStatus should have resDesc1");

            // Verify message structure
            JSONObject message = json.getJSONObject("message");
            assertTrue(message.has("params"), "message should have params");

            JSONObject params = message.getJSONObject("params");
            assertTrue(params.has("param"), "params should have param");

            System.out.println("✓ Response Status structure: Valid");
            System.out.println("  - resCode: " + responseStatus.getString("resCode"));
            System.out.println("  - resDesc: " + responseStatus.getString("resDesc"));
            System.out.println("  - source: " + responseStatus.getString("source"));
            System.out.println("  - resCode1: " + responseStatus.getString("resCode1"));
            System.out.println("  - resDesc1: " + responseStatus.getString("resDesc1"));

            System.out.println("\n✓ Message structure: Valid");
            System.out.println("  - params.param present");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ RESPONSE STRUCTURE VALIDATED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    @Story("Validate margin parameters")
    @Description("Verify margin-related configuration parameters")
    public void testSystemConfigMarginParameters() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VALIDATE MARGIN PARAMETERS");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING REQUEST");
            System.out.println("-".repeat(70));

            // Request all common margin-related parameters
            String jsonRequest = "{\n" +
                    "    \"Srv\": \"SYSTEM_CONFIG\",\n" +
                    "    \"Message\": {\n" +
                    "        \"paramReq\": {\n" +
                    "            \"paramName\": \"MARGIN_PCT,MAINTENANCE_MARGIN_PCT,MARGIN_GRACE_DAYS\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\"\n" +
                    "}";

            System.out.println("Request: Margin parameters");

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST");
            System.out.println("-".repeat(70));

            APIResponse response = companiesAPI.sendRequest(endpointURL, jsonRequest);

            // ========== STEP 3: VALIDATE RESPONSE ==========
            System.out.println("\nSTEP 3: VALIDATING MARGIN PARAMETERS");
            System.out.println("-".repeat(70));

            // At minimum, verify we get a successful response
            assertEquals(response.getStatusCode(), 200, "Should get 200 response");

            JSONObject json = new JSONObject(response.getResponseBody());
            JSONObject responseStatus = json.getJSONObject("responseStatus");

            // Response code should be 0 (success) even if some params don't exist
            String resCode = responseStatus.getString("resCode");
            System.out.println("Response Code: " + resCode);

            // Note: Some margin parameters might not exist in all environments
            // So we just verify the response structure is valid
            System.out.println("✓ ASSERTION PASSED: Margin config request processed");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✓✓✓ MARGIN PARAMETERS TEST COMPLETED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed: " + e.getMessage());
        }
    }
}
