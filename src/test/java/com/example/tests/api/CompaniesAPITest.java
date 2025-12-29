package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.CompaniesAPI;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INITIALIZING COMPANIESAPI TEST");
        System.out.println("=".repeat(70));

        companiesAPI = new CompaniesAPI(BASE_URL);

        System.out.println("Base URL: " + BASE_URL);
        System.out.println("Endpoint URL: " + ENDPOINT_URL);
        System.out.println("CompaniesAPI client initialized successfully");
        System.out.println("=".repeat(70) + "\n");

        logger.info("CompaniesAPI Test Suite initialized");
        logger.info("Base URL: {}", BASE_URL);
        logger.info("Endpoint URL: {}", ENDPOINT_URL);
    }

    @Test(priority = 1)
    @Story("Send GET_COMPS Request")
    @Description("Test sending GET_COMPS request to /jetrade/process endpoint with JSON body")
    @Severity(SeverityLevel.CRITICAL)
    public void testSendGetCompsRequest() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET_COMPS REQUEST");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: PREPARE REQUEST ==========
            System.out.println("\nSTEP 1: PREPARING GET_COMPS REQUEST");
            System.out.println("-".repeat(70));

            String jsonRequest = "{\n" +
                    "    \"Srv\": \"GET_COMPS\",\n" +
                    "    \"Message\": {},\n" +
                    "    \"Version\": \"1.0\",\n" +
                    "    \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "    \"LstLogin\": \"08-07-2015\"\n" +
                    "}";

            System.out.println("Endpoint: " + ENDPOINT_URL);
            System.out.println("Service: GET_COMPS");
            System.out.println("Request Body:");
            System.out.println(jsonRequest);

            Allure.step("Prepare GET_COMPS request");
            logger.info("Endpoint: {}", ENDPOINT_URL);
            logger.info("Request JSON:\n{}", jsonRequest);

            // ========== STEP 2: SEND REQUEST ==========
            System.out.println("\nSTEP 2: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            Allure.step("Send GET_COMPS request to endpoint");
            APIResponse response = companiesAPI.sendRequest(ENDPOINT_URL, jsonRequest);

            System.out.println("Request sent successfully");
            logger.info("Request sent to: {}", ENDPOINT_URL);

            // ========== STEP 3: VERIFY RESPONSE RECEIVED ==========
            System.out.println("\nSTEP 3: VERIFY RESPONSE RECEIVED");
            System.out.println("-".repeat(70));

            Allure.step("Verify response is not null");
            Assert.assertNotNull(response, "API response should not be null");
            System.out.println("✓ ASSERTION PASSED: Response is not null");
            logger.info("✓ Response received successfully");

            // ========== STEP 4: PRINT RESPONSE DETAILS ==========
            System.out.println("\nSTEP 4: RESPONSE DETAILS");
            System.out.println("-".repeat(70));
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Request URL: " + response.getRequestUrl());
            System.out.println("Is Success: " + response.isSuccess());
            System.out.println("Response Body:");
            System.out.println(response.getResponseBody());

            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Request URL: {}", response.getRequestUrl());
            logger.info("Response Body: {}", response.getResponseBody());

            // ========== STEP 5: ASSERT STATUS CODE 200 ==========
            System.out.println("\nSTEP 5: ASSERTING RESPONSE STATUS CODE");
            System.out.println("-".repeat(70));

            System.out.println("Expected Status Code: 200");
            System.out.println("Actual Status Code: " + response.getStatusCode());

            Allure.step("Verify response status code is 200");
            Assert.assertEquals(response.getStatusCode(), 200,
                "Expected status code 200 but got: " + response.getStatusCode());

            System.out.println("✓ ASSERTION PASSED: Status code is 200 (SUCCESS)");
            logger.info("✓ Status code assertion PASSED: 200");

            // ========== STEP 6: VERIFY RESPONSE BODY NOT EMPTY ==========
            System.out.println("\nSTEP 6: VERIFYING RESPONSE BODY");
            System.out.println("-".repeat(70));

            Allure.step("Verify response body is not empty");
            Assert.assertNotNull(response.getResponseBody(), "Response body should not be null");
            Assert.assertFalse(response.getResponseBody().isEmpty(), "Response body should not be empty");

            System.out.println("✓ ASSERTION PASSED: Response body is not null");
            System.out.println("✓ ASSERTION PASSED: Response body is not empty");
            System.out.println("Response length: " + response.getResponseBody().length() + " characters");
            logger.info("✓ Response body validation PASSED");

            // ========== STEP 7: ATTACH TO ALLURE REPORT ==========
            System.out.println("\nSTEP 7: ATTACHING EVIDENCE TO ALLURE REPORT");
            System.out.println("-".repeat(70));

            Allure.addAttachment("Request JSON", "application/json", jsonRequest);
            Allure.addAttachment("Response Status Code", String.valueOf(response.getStatusCode()));
            Allure.addAttachment("Response Body", "application/json", response.getResponseBody());

            System.out.println("✓ Request JSON attached to Allure report");
            System.out.println("✓ Response Status attached to Allure report");
            System.out.println("✓ Response Body attached to Allure report");
            logger.info("✓ All evidence attached to Allure report");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: Request prepared");
            System.out.println("✓ Step 2: Request sent successfully");
            System.out.println("✓ Step 3: Response received");
            System.out.println("✓ Step 4: Response details printed");
            System.out.println("✓ Step 5: Status code verified (200)");
            System.out.println("✓ Step 6: Response body validated");
            System.out.println("✓ Step 7: Evidence attached to Allure report");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ ALL STEPS PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

            logger.info("╔════════════════════════════════════════════════════════════════╗");
            logger.info("║           TEST PASSED: GET_COMPS Request                      ║");
            logger.info("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✗✗✗ TEST FAILED ✗✗✗");
            System.out.println("=".repeat(70));
            System.out.println("Error: " + e.getMessage());
            System.out.println("=".repeat(70) + "\n");

            logger.error("╔════════════════════════════════════════════════════════════════╗");
            logger.error("║           TEST FAILED: GET_COMPS Request                      ║");
            logger.error("╚════════════════════════════════════════════════════════════════╝");
            logger.error("Error details: {}", e.getMessage(), e);

            Allure.addAttachment("Error Message", e.getMessage());
            Allure.addAttachment("Stack Trace", e.toString());

            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
}
