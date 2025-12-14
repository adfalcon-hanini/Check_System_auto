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
}
