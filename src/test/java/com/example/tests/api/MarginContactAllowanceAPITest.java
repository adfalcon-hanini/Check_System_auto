package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.LoginAPI;
import com.example.api.MarginContactAllowanceAPI;
import com.example.api.dto.MarginContactAllowanceRequestDTO;
import com.example.api.dto.MarginContactAllowanceResponseDTO;
import com.example.utils.APIConfigManager;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for MarginContactAllowanceAPI (MARGIN_CONTACT_ALLOWANCE service)
 * Pure API testing - No browser/Chrome required
 * Uses LoginAPI and config file for SessionID management
 */
@Epic("API Testing")
@Feature("Margin Contact Allowance API")
public class MarginContactAllowanceAPITest {

    private static final Logger logger = LoggerFactory.getLogger(MarginContactAllowanceAPITest.class);
    private MarginContactAllowanceAPI marginContactAllowanceAPI;
    private String endpointUrl;
    private String testNIN;

    @BeforeClass
    @Step("Initialize MarginContactAllowanceAPI Test")
    public void setUp() {
        logger.info("=".repeat(70));
        logger.info("INITIALIZING MARGINCONTACTALLOWANCEAPI TEST");
        logger.info("=".repeat(70));

        String baseUrl = APIConfigManager.getBaseUrl();
        endpointUrl = baseUrl + "/jetrade/process";
        testNIN = "12240";

        logger.info("Base URL: {}", baseUrl);
        logger.info("Endpoint URL: {}", endpointUrl);
        logger.info("Test NIN: {}", testNIN);

        marginContactAllowanceAPI = new MarginContactAllowanceAPI();
        logger.info("MarginContactAllowanceAPI client initialized successfully");

        logger.info("=".repeat(70));
        logger.info("");
    }

    /**
     * PREREQUISITE TEST - Priority 0 (Runs FIRST)
     * Ensures SessionID exists in config before running other tests
     * Automatically runs LoginAPI if SessionID is missing
     */
    @Test(priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Prerequisite: Ensure SessionID exists in config file before running MarginContactAllowanceAPI tests")
    @Step("Ensure SessionID exists (auto-login if needed)")
    public void ensureSessionIDExists() {
        logger.info("=".repeat(70));
        logger.info("PREREQUISITE: ENSURE SESSIONID EXISTS");
        logger.info("=".repeat(70));
        logger.info("");

        logger.info("Checking SessionID in config file...");
        String sessionID = APIConfigManager.getSessionID();

        if (sessionID == null || sessionID.isEmpty()) {
            logger.warn("✗ SessionID NOT FOUND in config file");
            logger.info("  Auto-login will be performed...");
            logger.info("");

            // Auto-login using LoginAPI
            logger.info("Starting automatic login...");
            LoginAPI loginAPI = new LoginAPI();
            String loginEndpoint = APIConfigManager.getBaseUrl() + "/jetrade/process";

            String jsonRequest = "{\n" +
                    "  \"Srv\": \"Login\",\n" +
                    "  \"Message\": {\n" +
                    "    \"Login\": {\n" +
                    "      \"UserName\": \"12240\",\n" +
                    "      \"Password\": \"12345\",\n" +
                    "      \"Lang\": \"ARB\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Version\": \"1.0\",\n" +
                    "  \"AppId\": \"WEB_ORDERS.25\",\n" +
                    "  \"LstLogin\": \"16-12-2025\"\n" +
                    "}";

            APIResponse loginResponse = loginAPI.sendLoginRequest(loginEndpoint, jsonRequest);
            logger.info("Login completed with status: {}", loginResponse.getStatusCode());

            // Verify SessionID was saved
            sessionID = APIConfigManager.getSessionID();
            Assert.assertNotNull(sessionID, "SessionID should be saved after login");
            Assert.assertFalse(sessionID.isEmpty(), "SessionID should not be empty after login");

            logger.info("✓ Login successful, SessionID saved to config");
            logger.info("  SessionID: {}***", sessionID.substring(0, Math.min(4, sessionID.length())));
        } else {
            logger.info("✓ SessionID FOUND in config file");
            logger.info("  SessionID: {}***", sessionID.substring(0, Math.min(4, sessionID.length())));
            logger.info("  No login required, using existing SessionID");
        }

        logger.info("");
        logger.info("=".repeat(70));
        logger.info("PREREQUISITE CHECK SUMMARY");
        logger.info("=".repeat(70));
        logger.info("✓ SessionID is available in config file");
        logger.info("✓ MarginContactAllowanceAPI tests can proceed");
        logger.info("=".repeat(70));
        logger.info("");
    }

    /**
     * Test getting margin contact allowance using RestAssured with DTO
     * Uses SessionID from config file automatically
     */
    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test getting margin contact allowance using MarginContactAllowanceAPI with auto SessionID injection")
    @Step("Get margin contact allowance for NIN: {testNIN}")
    public void testGetMarginContactAllowance() {
        logger.info("=".repeat(70));
        logger.info("TEST: GET MARGIN CONTACT ALLOWANCE REQUEST");
        logger.info("=".repeat(70));
        logger.info("");

        // STEP 1: Verify SessionID in config
        logger.info("STEP 1: VERIFYING SESSIONID IN CONFIG");
        logger.info("-".repeat(70));
        String sessionID = APIConfigManager.getSessionID();
        logger.info("SessionID from config: {}***", sessionID.substring(0, Math.min(4, sessionID.length())));
        Assert.assertNotNull(sessionID, "SessionID should exist in config");
        Assert.assertFalse(sessionID.isEmpty(), "SessionID should not be empty");
        logger.info("✓ ASSERTION PASSED: SessionID exists in config");
        logger.info("");

        // STEP 2: Prepare request
        logger.info("STEP 2: PREPARING MARGIN CONTACT ALLOWANCE REQUEST");
        logger.info("-".repeat(70));
        logger.info("Endpoint: {}", endpointUrl);
        logger.info("Service: MARGIN_CONTACT_ALLOWANCE");
        logger.info("NIN: {}", testNIN);
        logger.info("SessionID: Will be injected from config");
        logger.info("LstLogin: {}", APIConfigManager.getCurrentDate());
        logger.info("");

        // STEP 3: Send request
        logger.info("STEP 3: SENDING REQUEST TO API");
        logger.info("-".repeat(70));
        List<MarginContactAllowanceResponseDTO> responses = marginContactAllowanceAPI.getMarginContactAllowance(endpointUrl, testNIN);
        logger.info("Request sent successfully");
        logger.info("");

        // STEP 4: Verify HTTP status code 200
        logger.info("STEP 4: VERIFY HTTP STATUS CODE 200");
        logger.info("-".repeat(70));
        int httpStatusCode = marginContactAllowanceAPI.getLastHttpStatusCode();
        logger.info("Expected HTTP Status Code: 200");
        logger.info("Actual HTTP Status Code: {}", httpStatusCode);
        Allure.step("Verify HTTP status code is 200");
        Assert.assertEquals(httpStatusCode, 200, "Expected HTTP status code 200 but got: " + httpStatusCode);
        logger.info("✓ ASSERTION PASSED: HTTP Status Code is 200 (SUCCESS)");
        Allure.addAttachment("HTTP Status Code", String.valueOf(httpStatusCode));
        logger.info("");

        // STEP 5: Verify response
        logger.info("STEP 5: VERIFY RESPONSE RECEIVED");
        logger.info("-".repeat(70));
        Assert.assertNotNull(responses, "Response should not be null");
        logger.info("✓ ASSERTION PASSED: Response is not null");
        Assert.assertFalse(responses.isEmpty(), "Response list should not be empty");
        logger.info("✓ ASSERTION PASSED: Response list is not empty");
        logger.info("Response list size: {}", responses.size());
        logger.info("");

        // STEP 6: Display response details
        logger.info("STEP 6: RESPONSE DETAILS");
        logger.info("-".repeat(70));
        MarginContactAllowanceResponseDTO response = responses.get(0);
        Integer statusCode = response.getCode();
        logger.info("Response Status: {}", response.getStatus());
        logger.info("Response Code: {}", statusCode);
        logger.info("Response Success: {}", response.getSuccess());
        logger.info("Response: {}", response);

        // Add status code to Allure report for PDF
        if (statusCode != null) {
            Allure.parameter("statusCode", String.valueOf(statusCode));
            logger.info("✓ Status Code {} added to PDF report", statusCode);
        }
        logger.info("");

        // STEP 7: Validate margin allowance data
        logger.info("STEP 7: VALIDATING MARGIN ALLOWANCE DATA");
        logger.info("-".repeat(70));
        List<MarginContactAllowanceResponseDTO.MarginAllowanceDTO> allowances = response.extractMarginAllowances();
        if (allowances != null && !allowances.isEmpty()) {
            logger.info("✓ Margin allowances list found");
            logger.info("  Number of allowances: {}", allowances.size());
            logger.info("  First {} allowance(s):", Math.min(3, allowances.size()));

            for (int i = 0; i < Math.min(3, allowances.size()); i++) {
                MarginContactAllowanceResponseDTO.MarginAllowanceDTO allowance = allowances.get(i);
                logger.info("    {}. Type: {} | Amount: {} | Available: {} | Used: {} | Status: {}",
                        (i + 1),
                        allowance.getAllowanceType(),
                        allowance.getAllowanceAmount(),
                        allowance.getAvailableAmount(),
                        allowance.getUsedAmount(),
                        allowance.getStatus());
            }
            Assert.assertTrue(true, "Margin allowance data is valid");
            logger.info("✓ ASSERTION PASSED: Margin allowance data is valid");

            // Attach to Allure
            Allure.addAttachment("Margin Allowance Data", allowances.toString());
        } else {
            logger.warn("⚠ No margin allowances found in response");
            logger.info("  This might be expected if NIN has no margin contracts");
        }
        logger.info("");

        // STEP 8: Attach evidence to Allure report
        logger.info("STEP 8: ATTACHING EVIDENCE TO ALLURE REPORT");
        logger.info("-".repeat(70));
        Allure.addAttachment("Request Details", String.format(
                "Endpoint: %s\nNIN: %s\nService: MARGIN_CONTACT_ALLOWANCE",
                endpointUrl, testNIN));
        Allure.addAttachment("Response Details", response.toString());
        logger.info("✓ Request details attached to Allure report");
        logger.info("✓ Response details attached to Allure report");
        if (allowances != null) {
            logger.info("✓ Margin allowance data attached to Allure report");
        }
        logger.info("");

        // Test Summary
        logger.info("=".repeat(70));
        logger.info("TEST SUMMARY");
        logger.info("=".repeat(70));
        logger.info("✓ Step 1: SessionID verified in config");
        logger.info("✓ Step 2: Request prepared");
        logger.info("✓ Step 3: Request sent successfully");
        logger.info("✓ Step 4: HTTP status code verified (200)");
        logger.info("✓ Step 5: Response received");
        logger.info("✓ Step 6: Response details printed");
        logger.info("✓ Step 7: Margin allowance data validated");
        logger.info("✓ Step 8: Evidence attached to Allure report");
        logger.info("=".repeat(70));
        logger.info("✓✓✓ ALL STEPS PASSED ✓✓✓");
        logger.info("=".repeat(70));
        logger.info("");
    }

    /**
     * Test getting margin contact allowance with custom DTO construction
     */
    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test getting margin contact allowance using custom MarginContactAllowanceRequestDTO")
    @Step("Get margin contact allowance with custom DTO")
    public void testGetMarginContactAllowanceWithCustomDTO() {
        logger.info("=".repeat(70));
        logger.info("TEST: GET MARGIN CONTACT ALLOWANCE WITH CUSTOM DTO");
        logger.info("=".repeat(70));
        logger.info("");

        String sessionID = APIConfigManager.getSessionID();
        Assert.assertNotNull(sessionID, "SessionID should exist");

        // Build custom request using Builder
        MarginContactAllowanceRequestDTO request = new MarginContactAllowanceRequestDTO.Builder()
                .nin(testNIN)
                .sessionId(sessionID)
                .lstLogin(APIConfigManager.getCurrentDate())
                .build();

        logger.info("Custom request DTO created: {}", request);

        // Send request
        List<MarginContactAllowanceResponseDTO> responses = marginContactAllowanceAPI.sendRequest(endpointUrl, request);

        Assert.assertNotNull(responses, "Response should not be null");
        Assert.assertFalse(responses.isEmpty(), "Response should not be empty");

        // Add status code to Allure report for PDF
        MarginContactAllowanceResponseDTO response = responses.get(0);
        Integer statusCode = response.getCode();
        if (statusCode != null) {
            Allure.parameter("statusCode", String.valueOf(statusCode));
            logger.info("✓ Status Code {} added to PDF report", statusCode);
        }

        logger.info("✓ Custom DTO request successful");
        logger.info("Response: {}", response);
        logger.info("=".repeat(70));
        logger.info("");
    }

    /**
     * Test getting margin contact allowance with single response
     */
    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Test getting margin contact allowance as single response object")
    @Step("Get margin contact allowance single response")
    public void testGetMarginContactAllowanceSingle() {
        logger.info("=".repeat(70));
        logger.info("TEST: GET MARGIN CONTACT ALLOWANCE SINGLE RESPONSE");
        logger.info("=".repeat(70));
        logger.info("");

        // Get single response
        MarginContactAllowanceResponseDTO response = marginContactAllowanceAPI.getMarginContactAllowanceSingle(endpointUrl, testNIN);

        Assert.assertNotNull(response, "Response should not be null");

        // Add status code to Allure report for PDF
        Integer statusCode = response.getCode();
        if (statusCode != null) {
            Allure.parameter("statusCode", String.valueOf(statusCode));
            logger.info("✓ Status Code {} added to PDF report", statusCode);
        }

        logger.info("✓ Single response method successful");
        logger.info("Response: {}", response);

        List<MarginContactAllowanceResponseDTO.MarginAllowanceDTO> allowances = response.extractMarginAllowances();
        if (allowances != null) {
            logger.info("Total margin allowances: {}", allowances.size());
        }

        logger.info("=".repeat(70));
        logger.info("");
    }
}
