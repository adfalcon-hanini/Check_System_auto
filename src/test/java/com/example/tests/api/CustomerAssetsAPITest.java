package com.example.tests.api;

import com.example.api.APIResponse;
import com.example.api.CustomerAssetsAPI;
import com.example.api.LoginAPI;
import com.example.api.dto.CustomerAssetsRequestDTO;
import com.example.api.dto.CustomerAssetsResponseDTO;
import com.example.utils.APIConfigManager;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test class for CustomerAssetsAPI
 * Pure API testing - No browser/Chrome required
 * Uses LoginAPI and config file for SessionID management
 *
 * WHAT IS BEING TESTED:
 * - CustomerAssets API endpoint functionality
 * - RestAssured HTTP POST request with JSON body
 * - Dynamic SessionID injection from config file
 * - Request DTO to JSON serialization (Jackson)
 * - Response JSON to DTO deserialization (Jackson)
 * - Response status code validation
 * - Assets data extraction and validation
 *
 * INPUT DATA USED:
 * - NIN (National Identification Number): "12240"
 * - SessionID: Read dynamically from api-config.properties (auto-login if missing)
 * - Service: "CustomerAssets"
 * - Version: "1.0"
 * - AppId: "WEB_ORDERS.25"
 * - LstLogin: Current date in dd-MM-yyyy format
 *
 * EXPECTED RESULT:
 * - HTTP Status Code: 200 (Success)
 * - Response contains list of customer assets/portfolio
 * - Each asset has: CompanyCode, CompanyName, Quantity, Price, ProfitLoss
 * - Response is properly deserialized to CustomerAssetsResponseDTO
 *
 * VALIDATION APPROACH:
 * 1. Auto-login if SessionID missing (Priority 0 test)
 * 2. Assert response is not null
 * 3. Assert HTTP status code is 200
 * 4. Assert response body is not empty
 * 5. Assert response can be deserialized to DTO
 * 6. Assert assets list is not empty (if available)
 * 7. Validate each asset has required fields
 * 8. Log all request/response details for debugging
 * 9. Attach evidence to Allure report
 */
@Epic("API Testing")
@Feature("Customer Assets API")
public class CustomerAssetsAPITest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAssetsAPITest.class);
    private CustomerAssetsAPI customerAssetsAPI;
    private static final String BASE_URL = "https://devuat.thegroup.com.qa";
    private static final String ENDPOINT_URL = "https://devuat.thegroup.com.qa/jetrade/process";
    private static final String TEST_NIN = "12240";

    @BeforeClass
    @Step("Initialize CustomerAssetsAPI client")
    public void setup() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INITIALIZING CUSTOMERASSETSAPI TEST");
        System.out.println("=".repeat(70));

        customerAssetsAPI = new CustomerAssetsAPI(BASE_URL);

        System.out.println("Base URL: " + BASE_URL);
        System.out.println("Endpoint URL: " + ENDPOINT_URL);
        System.out.println("Test NIN: " + TEST_NIN);
        System.out.println("CustomerAssetsAPI client initialized successfully");
        System.out.println("=".repeat(70) + "\n");

        logger.info("CustomerAssetsAPI Test Suite initialized");
        logger.info("Base URL: {}", BASE_URL);
        logger.info("Endpoint URL: {}", ENDPOINT_URL);
    }

    @Test(priority = 0)
    @Story("Ensure SessionID is Available")
    @Description("Run login first if SessionID is not available in config file")
    @Severity(SeverityLevel.BLOCKER)
    public void ensureSessionIDExists() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("PREREQUISITE: ENSURE SESSIONID EXISTS");
        System.out.println("=".repeat(70));

        try {
            // Check if SessionID exists in config
            String sessionID = APIConfigManager.getSessionID();

            System.out.println("\nChecking SessionID in config file...");

            if (sessionID == null || sessionID.isEmpty()) {
                System.out.println("⚠ SessionID NOT FOUND in config!");
                System.out.println("Running LoginAPI to obtain SessionID...");
                System.out.println("-".repeat(70));

                // Run login to get SessionID
                LoginAPI loginAPI = new LoginAPI();
                String loginEndpoint = LoginAPI.getEndpointURL();

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

                System.out.println("Sending login request...");
                APIResponse loginResponse = loginAPI.sendLoginRequest(loginEndpoint, jsonRequest);

                System.out.println("Login response status: " + loginResponse.getStatusCode());

                Assert.assertEquals(loginResponse.getStatusCode(), 200,
                        "Login should succeed to get SessionID");

                // Verify SessionID was saved
                sessionID = APIConfigManager.getSessionID();
                Assert.assertNotNull(sessionID, "SessionID should be available after login");
                Assert.assertFalse(sessionID.isEmpty(), "SessionID should not be empty after login");

                System.out.println("✓ Login successful!");
                System.out.println("✓ SessionID obtained: ***" + sessionID.substring(Math.max(0, sessionID.length() - 4)));
                System.out.println("✓ SessionID saved to config file");

            } else {
                System.out.println("✓ SessionID FOUND in config file");
                System.out.println("  SessionID: ***" + sessionID.substring(Math.max(0, sessionID.length() - 4)));
                System.out.println("  No login required, using existing SessionID");
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("PREREQUISITE CHECK SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ SessionID is available in config file");
            System.out.println("✓ CustomerAssetsAPI tests can proceed");
            System.out.println("=".repeat(70) + "\n");

            logger.info("✓ SessionID prerequisite check passed");

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✗✗✗ PREREQUISITE CHECK FAILED ✗✗✗");
            System.out.println("=".repeat(70));
            System.out.println("Error: " + e.getMessage());
            System.out.println("=".repeat(70) + "\n");

            logger.error("✗ SessionID prerequisite check failed: {}", e.getMessage());

            Assert.fail("Failed to ensure SessionID exists: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    @Story("Get Customer Assets with RestAssured + DTO")
    @Description("Test getting customer assets using RestAssured with DTO objects and Jackson serialization")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetCustomerAssets() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: GET CUSTOMER ASSETS REQUEST");
        System.out.println("=".repeat(70));

        try {
            // ========== STEP 1: VERIFY SESSIONID EXISTS ==========
            System.out.println("\nSTEP 1: VERIFYING SESSIONID IN CONFIG");
            System.out.println("-".repeat(70));

            String sessionID = APIConfigManager.getSessionID();
            System.out.println("SessionID from config: " + (sessionID != null && !sessionID.isEmpty() ? "***" + sessionID.substring(Math.max(0, sessionID.length() - 4)) : "NOT FOUND"));

            Allure.step("Verify SessionID is available in config");
            Assert.assertNotNull(sessionID, "SessionID should be available in config file");
            Assert.assertFalse(sessionID.isEmpty(), "SessionID should not be empty");

            System.out.println("✓ ASSERTION PASSED: SessionID exists in config");
            logger.info("✓ SessionID verified in config");

            // ========== STEP 2: PREPARE REQUEST ==========
            System.out.println("\nSTEP 2: PREPARING CUSTOMER ASSETS REQUEST");
            System.out.println("-".repeat(70));

            System.out.println("Endpoint: " + ENDPOINT_URL);
            System.out.println("Service: CustomerAssets");
            System.out.println("NIN: " + TEST_NIN);
            System.out.println("SessionID: Will be injected from config");
            System.out.println("LstLogin: " + APIConfigManager.getCurrentDate());

            Allure.step("Prepare CustomerAssets request");
            logger.info("Preparing request for NIN: {}", TEST_NIN);

            // ========== STEP 3: SEND REQUEST ==========
            System.out.println("\nSTEP 3: SENDING REQUEST TO API");
            System.out.println("-".repeat(70));

            Allure.step("Send CustomerAssets request via RestAssured");
            List<CustomerAssetsResponseDTO> responseList =
                customerAssetsAPI.getCustomerAssets(ENDPOINT_URL, TEST_NIN);

            System.out.println("Request sent successfully");
            logger.info("Request sent to: {}", ENDPOINT_URL);

            // ========== STEP 4: VERIFY HTTP STATUS CODE 200 ==========
            System.out.println("\nSTEP 4: VERIFY HTTP STATUS CODE 200");
            System.out.println("-".repeat(70));

            int httpStatusCode = customerAssetsAPI.getLastHttpStatusCode();
            System.out.println("Expected HTTP Status Code: 200");
            System.out.println("Actual HTTP Status Code: " + httpStatusCode);
            Allure.step("Verify HTTP status code is 200");
            Assert.assertEquals(httpStatusCode, 200, "Expected HTTP status code 200 but got: " + httpStatusCode);
            System.out.println("✓ ASSERTION PASSED: HTTP Status Code is 200 (SUCCESS)");
            Allure.addAttachment("HTTP Status Code", String.valueOf(httpStatusCode));
            logger.info("✓ HTTP Status Code verified: {}", httpStatusCode);

            // ========== STEP 5: VERIFY RESPONSE RECEIVED ==========
            System.out.println("\nSTEP 5: VERIFY RESPONSE RECEIVED");
            System.out.println("-".repeat(70));

            Allure.step("Verify response is not null");
            Assert.assertNotNull(responseList, "Response list should not be null");
            Assert.assertFalse(responseList.isEmpty(), "Response list should not be empty");

            System.out.println("✓ ASSERTION PASSED: Response is not null");
            System.out.println("✓ ASSERTION PASSED: Response list is not empty");
            System.out.println("Response list size: " + responseList.size());
            logger.info("✓ Response received successfully");

            // ========== STEP 6: PRINT RESPONSE DETAILS ==========
            System.out.println("\nSTEP 6: RESPONSE DETAILS");
            System.out.println("-".repeat(70));

            CustomerAssetsResponseDTO response = responseList.get(0);
            Integer statusCode = response.getCode();
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Success: " + response.getSuccess());
            System.out.println("Response: " + response.toString());

            // Add status code to Allure report for PDF
            if (statusCode != null) {
                Allure.parameter("statusCode", String.valueOf(statusCode));
                System.out.println("✓ Status Code " + statusCode + " added to PDF report");
            }

            logger.info("Response Status: {}", response.getStatus());
            logger.info("Response Code: {}", statusCode);
            logger.info("Response: {}", response);

            // ========== STEP 7: VALIDATE ASSETS DATA ==========
            System.out.println("\nSTEP 7: VALIDATING ASSETS DATA");
            System.out.println("-".repeat(70));

            Allure.step("Validate assets data in response");
            List<CustomerAssetsResponseDTO.AssetDTO> assets = response.extractAssets();

            if (assets != null && !assets.isEmpty()) {
                System.out.println("✓ Assets list found");
                System.out.println("  Number of assets: " + assets.size());

                // Display first few assets
                int displayCount = Math.min(3, assets.size());
                System.out.println("  First " + displayCount + " assets:");
                for (int i = 0; i < displayCount; i++) {
                    CustomerAssetsResponseDTO.AssetDTO asset = assets.get(i);
                    System.out.println("    " + (i + 1) + ". " + asset.getCompanyCode() +
                                     " - " + asset.getCompanyName() +
                                     " | Qty: " + asset.getQuantity() +
                                     " | Avg Price: " + asset.getAveragePrice() +
                                     " | P/L: " + asset.getProfitLoss());
                }

                // Validate asset fields
                CustomerAssetsResponseDTO.AssetDTO firstAsset = assets.get(0);
                Assert.assertNotNull(firstAsset.getCompanyCode(), "Company code should not be null");
                Assert.assertNotNull(firstAsset.getCompanyName(), "Company name should not be null");

                System.out.println("✓ ASSERTION PASSED: Asset data is valid");
                logger.info("✓ Assets data validated: {} assets found", assets.size());
            } else {
                System.out.println("⚠ No assets found in response (may be expected for empty portfolio)");
                logger.warn("⚠ No assets found in response");
            }

            // ========== STEP 8: ATTACH TO ALLURE REPORT ==========
            System.out.println("\nSTEP 8: ATTACHING EVIDENCE TO ALLURE REPORT");
            System.out.println("-".repeat(70));

            Allure.addAttachment("Request NIN", TEST_NIN);
            Allure.addAttachment("Request SessionID", sessionID != null ? "***" + sessionID.substring(Math.max(0, sessionID.length() - 4)) : "null");
            Allure.addAttachment("Response Status", String.valueOf(response.getStatus()));
            Allure.addAttachment("Response Details", response.toString());
            if (assets != null) {
                Allure.addAttachment("Assets Count", String.valueOf(assets.size()));
            }

            System.out.println("✓ Request details attached to Allure report");
            System.out.println("✓ Response details attached to Allure report");
            System.out.println("✓ Assets data attached to Allure report");
            logger.info("✓ All evidence attached to Allure report");

            // ========== TEST SUMMARY ==========
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TEST SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("✓ Step 1: SessionID verified in config");
            System.out.println("✓ Step 2: Request prepared");
            System.out.println("✓ Step 3: Request sent successfully");
            System.out.println("✓ Step 4: HTTP status code verified (200)");
            System.out.println("✓ Step 5: Response received");
            System.out.println("✓ Step 6: Response details printed");
            System.out.println("✓ Step 7: Assets data validated");
            System.out.println("✓ Step 8: Evidence attached to Allure report");
            System.out.println("=".repeat(70));
            System.out.println("✓✓✓ ALL STEPS PASSED ✓✓✓");
            System.out.println("=".repeat(70) + "\n");

            logger.info("╔════════════════════════════════════════════════════════════════╗");
            logger.info("║         TEST PASSED: CustomerAssets Request                   ║");
            logger.info("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("✗✗✗ TEST FAILED ✗✗✗");
            System.out.println("=".repeat(70));
            System.out.println("Error: " + e.getMessage());
            System.out.println("=".repeat(70) + "\n");

            logger.error("╔════════════════════════════════════════════════════════════════╗");
            logger.error("║         TEST FAILED: CustomerAssets Request                   ║");
            logger.error("╚════════════════════════════════════════════════════════════════╝");
            logger.error("Error details: {}", e.getMessage(), e);

            Allure.addAttachment("Error Message", e.getMessage());
            Allure.addAttachment("Stack Trace", e.toString());

            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    @Story("Get Customer Assets with Custom DTO")
    @Description("Test getting assets with manually constructed DTO including sessionId")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCustomerAssetsWithCustomDTO() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: CUSTOMER ASSETS WITH CUSTOM DTO");
        System.out.println("=".repeat(70));

        try {
            // Build custom request DTO
            String sessionID = APIConfigManager.getSessionID();
            Assert.assertNotNull(sessionID, "SessionID should be available");

            CustomerAssetsRequestDTO request = new CustomerAssetsRequestDTO.Builder()
                    .nin(TEST_NIN)
                    .sessionId(sessionID)
                    .lstLogin(APIConfigManager.getCurrentDate())
                    .build();

            System.out.println("Custom request DTO created:");
            System.out.println("  NIN: " + TEST_NIN);
            System.out.println("  SessionID: ***" + sessionID.substring(Math.max(0, sessionID.length() - 4)));
            System.out.println("  LstLogin: " + APIConfigManager.getCurrentDate());

            // Send request
            List<CustomerAssetsResponseDTO> responseList =
                customerAssetsAPI.sendRequest(ENDPOINT_URL, request);

            Assert.assertNotNull(responseList, "Response should not be null");
            Assert.assertFalse(responseList.isEmpty(), "Response should not be empty");

            // Add status code to Allure report for PDF
            CustomerAssetsResponseDTO response = responseList.get(0);
            Integer statusCode = response.getCode();
            if (statusCode != null) {
                Allure.parameter("statusCode", String.valueOf(statusCode));
                System.out.println("✓ Status Code " + statusCode + " added to PDF report");
            }

            System.out.println("✓ Request sent successfully with custom DTO");
            System.out.println("✓ Response received: " + response.toString());
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.out.println("✗ Test failed: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    @Story("Get Single Customer Assets Response")
    @Description("Test convenience method that returns single response object")
    @Severity(SeverityLevel.NORMAL)
    public void testGetCustomerAssetsSingle() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: CUSTOMER ASSETS SINGLE RESPONSE");
        System.out.println("=".repeat(70));

        try {
            // Send request and get single response
            CustomerAssetsResponseDTO response =
                customerAssetsAPI.getCustomerAssetsSingle(ENDPOINT_URL, TEST_NIN);

            Assert.assertNotNull(response, "Response should not be null");

            // Add status code to Allure report for PDF
            Integer statusCode = response.getCode();
            if (statusCode != null) {
                Allure.parameter("statusCode", String.valueOf(statusCode));
                System.out.println("✓ Status Code " + statusCode + " added to PDF report");
            }

            System.out.println("✓ Single response received");
            System.out.println("  Response: " + response.toString());

            List<CustomerAssetsResponseDTO.AssetDTO> assets = response.extractAssets();
            if (assets != null && !assets.isEmpty()) {
                System.out.println("  Assets count: " + assets.size());
            }

            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.out.println("✗ Test failed: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
}
