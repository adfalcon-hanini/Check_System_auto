package com.example.api;

import com.example.api.dto.MarginContactAllowanceRequestDTO;
import com.example.api.dto.MarginContactAllowanceResponseDTO;
import com.example.utils.APIConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * API Client for Margin Contact Allowance operations (MARGIN_CONTACT_ALLOWANCE)
 * Uses RestAssured to send requests and Jackson for JSON serialization/deserialization
 * Reads SessionID from config file and injects it dynamically into requests
 */
public class MarginContactAllowanceAPI {

    private static final Logger logger = LoggerFactory.getLogger(MarginContactAllowanceAPI.class);
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private int lastHttpStatusCode; // Store last HTTP status code for test assertions

    /**
     * Constructor with base URL
     * @param baseUrl Base URL for API endpoints
     */
    public MarginContactAllowanceAPI(String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Default constructor - uses base URL from config
     */
    public MarginContactAllowanceAPI() {
        this.baseUrl = APIConfigManager.getBaseUrl();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get margin contact allowance using RestAssured with DTO objects
     * SessionID is read from config file and injected into the request
     *
     * @param url Full API endpoint URL
     * @param nin National Identification Number
     * @return List of MarginContactAllowanceResponseDTO objects
     */
    @Step("Get margin contact allowance for NIN: {nin}")
    public List<MarginContactAllowanceResponseDTO> getMarginContactAllowance(String url, String nin) {
        try {
            logger.info("=".repeat(60));
            logger.info("GETTING MARGIN CONTACT ALLOWANCE");
            logger.info("=".repeat(60));
            logger.info("URL: {}", url);
            logger.info("NIN: {}", nin);

            // Read SessionID from config file
            String sessionID = APIConfigManager.getSessionID();
            if (sessionID == null || sessionID.isEmpty()) {
                logger.warn("⚠ SessionID not found in config file!");
                throw new RuntimeException("SessionID is required but not found in config file. Please run login first.");
            }
            logger.info("SessionID loaded from config: {}", sessionID);

            // Build request DTO with SessionID from config
            MarginContactAllowanceRequestDTO request = new MarginContactAllowanceRequestDTO.Builder()
                    .nin(nin)
                    .sessionId(sessionID)
                    .lstLogin(APIConfigManager.getCurrentDate())
                    .build();

            logger.info("Request Object created: {}", request);

            // Send request using RestAssured
            return sendRequest(url, request);

        } catch (Exception e) {
            logger.error("ERROR getting margin contact allowance: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get margin contact allowance", e);
        }
    }

    /**
     * Send MarginContactAllowance request with custom request DTO
     *
     * @param url Full API endpoint URL
     * @param request MarginContactAllowanceRequestDTO object (sessionId will be auto-injected if null)
     * @return List of MarginContactAllowanceResponseDTO objects
     */
    @Step("Send MarginContactAllowance request with custom DTO")
    public List<MarginContactAllowanceResponseDTO> sendRequest(String url, MarginContactAllowanceRequestDTO request) {
        try {
            logger.info("=".repeat(60));
            logger.info("SENDING MARGIN CONTACT ALLOWANCE REQUEST (RestAssured + DTO)");
            logger.info("=".repeat(60));
            logger.info("URL: {}", url);

            // If sessionId is null in request, read from config
            if (request.getSessionId() == null || request.getSessionId().isEmpty()) {
                String sessionID = APIConfigManager.getSessionID();
                if (sessionID == null || sessionID.isEmpty()) {
                    throw new RuntimeException("SessionID is required but not found in config or request");
                }
                logger.info("SessionID injected from config");
            }

            logger.info("Request Object: {}", objectMapper.writeValueAsString(request));

            // Build RestAssured request with headers
            RequestSpecification requestSpec = RestAssured.given()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(request)
                    .log().all(); // Log request details

            // Send POST request
            Response response = requestSpec.post(url);

            // Store HTTP status code for test assertions
            this.lastHttpStatusCode = response.getStatusCode();

            logger.info("-".repeat(60));
            logger.info("RESPONSE RECEIVED");
            logger.info("-".repeat(60));
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody().asString());

            // Validate response status
            validateResponse(response);

            // Deserialize response to List
            List<MarginContactAllowanceResponseDTO> responseList = deserializeResponse(response);

            logger.info("=".repeat(60));
            return responseList;

        } catch (Exception e) {
            logger.error("ERROR sending margin contact allowance request: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send margin contact allowance request", e);
        }
    }

    /**
     * Validate response status code and content
     *
     * @param response RestAssured Response object
     */
    private void validateResponse(Response response) {
        int statusCode = response.getStatusCode();

        if (statusCode >= 200 && statusCode < 300) {
            logger.info("✓ Response Status: SUCCESS ({})", statusCode);
        } else if (statusCode >= 400 && statusCode < 500) {
            logger.error("✗ Response Status: CLIENT ERROR ({})", statusCode);
            throw new RuntimeException("Client error: " + statusCode + " - " + response.getBody().asString());
        } else if (statusCode >= 500) {
            logger.error("✗ Response Status: SERVER ERROR ({})", statusCode);
            throw new RuntimeException("Server error: " + statusCode + " - " + response.getBody().asString());
        } else {
            logger.warn("⚠ Response Status: UNEXPECTED ({})", statusCode);
        }

        // Check if response body is not empty
        if (response.getBody().asString().isEmpty()) {
            logger.error("✗ Response body is empty!");
            throw new RuntimeException("Response body is empty");
        }
    }

    /**
     * Deserialize response JSON to List of MarginContactAllowanceResponseDTO
     *
     * @param response RestAssured Response object
     * @return List of MarginContactAllowanceResponseDTO
     */
    private List<MarginContactAllowanceResponseDTO> deserializeResponse(Response response) {
        List<MarginContactAllowanceResponseDTO> responseList = new ArrayList<>();

        try {
            // Try to deserialize as array/list first
            MarginContactAllowanceResponseDTO[] responseArray = objectMapper.readValue(
                    response.getBody().asString(),
                    MarginContactAllowanceResponseDTO[].class
            );
            responseList = List.of(responseArray);
            logger.info("Response deserialized as List with {} items", responseList.size());
        } catch (Exception e) {
            // If not a list, try as single object and wrap in list
            logger.info("Response is not a list, deserializing as single object");
            try {
                MarginContactAllowanceResponseDTO singleResponse = objectMapper.readValue(
                        response.getBody().asString(),
                        MarginContactAllowanceResponseDTO.class
                );
                responseList.add(singleResponse);
                logger.info("Response deserialized as single object");
            } catch (Exception ex) {
                logger.error("Failed to deserialize response: {}", ex.getMessage());
                throw new RuntimeException("Failed to deserialize response", ex);
            }
        }

        return responseList;
    }

    /**
     * Get margin contact allowance and return single response object
     * Convenience method that returns single object instead of list
     *
     * @param url Full API endpoint URL
     * @param nin National Identification Number
     * @return Single MarginContactAllowanceResponseDTO object
     */
    @Step("Get margin contact allowance (single response) for NIN: {nin}")
    public MarginContactAllowanceResponseDTO getMarginContactAllowanceSingle(String url, String nin) {
        List<MarginContactAllowanceResponseDTO> responseList = getMarginContactAllowance(url, nin);
        if (responseList != null && !responseList.isEmpty()) {
            return responseList.get(0);
        }
        return null;
    }

    /**
     * Get base URL
     * @return Base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Get ObjectMapper instance
     * @return ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Get the last HTTP status code from the most recent API call
     * @return Last HTTP status code (e.g., 200, 404, 500)
     */
    public int getLastHttpStatusCode() {
        return lastHttpStatusCode;
    }
}
