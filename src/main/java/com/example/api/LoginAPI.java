package com.example.api;

import com.example.api.dto.LoginRequestDTO;
import com.example.api.dto.LoginResponseDTO;
import com.example.utils.APIConfigManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * API Client for Login operations
 * Sends login request, extracts sessionID, and persists to config
 * Supports both traditional HttpURLConnection and RestAssured approaches
 */
public class LoginAPI extends BaseAPIClient {

    protected static final Logger logger = LoggerFactory.getLogger(LoginAPI.class);
    private static String sessionID;
    private final ObjectMapper objectMapper;

    public LoginAPI(String baseUrl) {
        super(baseUrl);
        this.objectMapper = new ObjectMapper();
    }

    public LoginAPI() {
        super(APIConfigManager.getBaseUrl());
        this.objectMapper = new ObjectMapper();
        loadSessionIDFromConfig();
    }

    /**
     * Send login request to the API endpoint
     * @param url Full API endpoint URL
     * @param jsonRequest JSON request body
     * @return APIResponse containing status code and response body
     */
    @Step("Send login request to: {url}")
    public APIResponse sendLoginRequest(String url, String jsonRequest) {
        try {
            logger.info("=".repeat(60));
            logger.info("SENDING LOGIN REQUEST");
            logger.info("=".repeat(60));
            logger.info("URL: {}", url);
            logger.info("Request Body: {}", jsonRequest);

            // Send POST request
            APIResponse response = executePostToFullURL(url, jsonRequest);

            logger.info("-".repeat(60));
            logger.info("RESPONSE RECEIVED");
            logger.info("-".repeat(60));
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getResponseBody());

            // Check if response is successful (200)
            if (response.getStatusCode() == 200) {
                logger.info("✓ Response Status: SUCCESS (200)");

                // Extract and save sessionID
                extractAndSaveSessionID(response.getResponseBody());
            } else {
                logger.error("✗ Response Status: FAILED ({})", response.getStatusCode());
            }

            logger.info("=".repeat(60));
            return response;

        } catch (Exception e) {
            logger.error("ERROR sending login request: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send login request", e);
        }
    }

    /**
     * Extract sessionID from JSON response and save to config file
     * Searches thoroughly through all levels of JSON structure
     * @param responseBody JSON response body
     */
    private void extractAndSaveSessionID(String responseBody) {
        try {
            logger.info("");
            logger.info("SEARCHING FOR SESSIONID IN RESPONSE...");
            logger.info("-".repeat(60));

            JSONObject json = new JSONObject(responseBody);
            String foundSessionID = null;  // Use local variable, not class variable
            String foundField = null;
            String foundLocation = "root";

            // List of possible sessionID field names
            String[] fieldNames = {
                "sessionID", "sessionId", "session_id", "SessionID", "SESSIONID",
                "token", "Token", "TOKEN",
                "accessToken", "access_token", "AccessToken",
                "authToken", "auth_token", "AuthToken",
                "jwt", "JWT",
                "sid", "SID", "Sid"
            };

            logger.info("Looking for sessionID in response...");
            logger.info("Response structure: {}", json.keySet());

            // STEP 1: Check root level
            logger.info("Step 1: Checking root level...");
            for (String field : fieldNames) {
                if (json.has(field)) {
                    Object value = json.get(field);
                    if (value instanceof String) {
                        foundSessionID = json.getString(field);
                        foundField = field;
                        foundLocation = "root";
                        logger.info("Found at root level: field='{}', value='{}'", field, foundSessionID);
                        break;
                    }
                }
            }

            // STEP 2: If not found, check common nested objects
            if (foundSessionID == null || foundSessionID.isEmpty()) {
                logger.info("Step 2: Checking nested objects...");
                String[] nestedNames = {
                    "data", "Data", "DATA",
                    "result", "Result", "RESULT",
                    "response", "Response", "RESPONSE",
                    "payload", "Payload", "PAYLOAD",
                    "body", "Body", "BODY",
                    "Message", "message", "MESSAGE"
                };

                for (String nested : nestedNames) {
                    if (json.has(nested)) {
                        Object nestedValue = json.get(nested);

                        if (nestedValue instanceof JSONObject) {
                            JSONObject nestedObj = (JSONObject) nestedValue;
                            logger.info("Checking '{}' object: {}", nested, nestedObj.keySet());

                            for (String field : fieldNames) {
                                if (nestedObj.has(field)) {
                                    Object value = nestedObj.get(field);
                                    if (value instanceof String) {
                                        foundSessionID = nestedObj.getString(field);
                                        foundField = field;
                                        foundLocation = nested;
                                        logger.info("Found in '{}': field='{}', value='{}'", nested, field, foundSessionID);
                                        break;
                                    }
                                }
                            }

                            if (foundSessionID != null && !foundSessionID.isEmpty()) {
                                break;
                            }
                        }
                    }
                }
            }

            // STEP 3: If still not found, do recursive search for any field containing "session" or "token"
            if (foundSessionID == null || foundSessionID.isEmpty()) {
                logger.info("Step 3: Performing deep recursive search...");
                foundSessionID = recursiveFindSessionID(json, "root");

                if (foundSessionID != null && !foundSessionID.isEmpty()) {
                    foundField = "found via recursive search";
                    foundLocation = "nested structure";
                }
            }

            // STEP 4: Save sessionID if found
            if (foundSessionID != null && !foundSessionID.isEmpty()) {
                // Update the class variable with the found sessionID
                sessionID = foundSessionID;

                logger.info("");
                logger.info("=".repeat(60));
                logger.info("✓✓✓ SessionID FOUND! ✓✓✓");
                logger.info("=".repeat(60));
                logger.info("  Location: {}", foundLocation);
                logger.info("  Field: {}", foundField);
                logger.info("  Value: {}", sessionID);
                logger.info("");
                logger.info("NOW SAVING SESSIONID TO CONFIG FILE...");
                logger.info("=".repeat(60));

                // Save sessionID to config file
                logger.info("Step 1: Calling APIConfigManager.updateSessionID()...");
                boolean saved = APIConfigManager.updateSessionID(sessionID);

                if (saved) {
                    logger.info("✓ Step 1 PASSED: updateSessionID() returned true");
                    logger.info("");
                    logger.info("Step 2: Verifying sessionID was saved...");

                    // Verify by reading back from config
                    String verifySessionID = APIConfigManager.getSessionID();
                    logger.info("  Read from config: {}", verifySessionID);

                    if (sessionID.equals(verifySessionID)) {
                        logger.info("✓ Step 2 PASSED: SessionID matches!");
                        logger.info("");
                        logger.info("=".repeat(60));
                        logger.info("✓✓✓ SUCCESS: SESSIONID SAVED TO CONFIG FILE ✓✓✓");
                        logger.info("=".repeat(60));
                        logger.info("Config File Location:");
                        logger.info("  → src/main/resources/api-config.properties");
                        logger.info("Config Property:");
                        logger.info("  → Key: api.sessionID");
                        logger.info("  → Value: {}", sessionID);
                        logger.info("");
                        logger.info("The sessionID is now persisted and can be reused!");
                        logger.info("=".repeat(60));
                    } else {
                        logger.error("");
                        logger.error("=".repeat(60));
                        logger.error("✗✗✗ ERROR: VERIFICATION FAILED! ✗✗✗");
                        logger.error("=".repeat(60));
                        logger.error("SessionID was saved but verification failed!");
                        logger.error("  Expected: {}", sessionID);
                        logger.error("  Got from config: {}", verifySessionID);
                        logger.error("=".repeat(60));
                    }
                } else {
                    logger.error("");
                    logger.error("=".repeat(60));
                    logger.error("✗✗✗ ERROR: FAILED TO SAVE SESSIONID! ✗✗✗");
                    logger.error("=".repeat(60));
                    logger.error("APIConfigManager.updateSessionID() returned false");
                    logger.error("Possible issues:");
                    logger.error("  1. File permission problem");
                    logger.error("  2. File not found");
                    logger.error("  3. IO error");
                    logger.error("Please check the logs above for detailed error messages.");
                    logger.error("=".repeat(60));
                }
            } else {
                logger.error("");
                logger.error("✗✗✗ SessionID NOT FOUND! ✗✗✗");
                logger.error("Complete response structure:");
                printJsonStructure(json, "", 0);
                logger.error("");
                logger.error("Searched for fields: {}", String.join(", ", fieldNames));
                logger.error("Please check the response structure above to identify the sessionID field.");
            }

        } catch (Exception e) {
            logger.error("ERROR extracting sessionID: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recursively search for sessionID in nested JSON objects
     * @param json JSON object to search
     * @param path Current path for logging
     * @return sessionID if found, null otherwise
     */
    private String recursiveFindSessionID(JSONObject json, String path) {
        try {
            for (String key : json.keySet()) {
                Object value = json.get(key);
                String currentPath = path + "." + key;

                // Check if this field name contains "session" or "token"
                String lowerKey = key.toLowerCase();
                if (lowerKey.contains("session") || lowerKey.contains("token") ||
                    lowerKey.contains("sid") || lowerKey.equals("jwt")) {

                    if (value instanceof String) {
                        String strValue = (String) value;
                        if (!strValue.isEmpty()) {
                            logger.info("Found potential sessionID at: {}", currentPath);
                            logger.info("  Key: {}", key);
                            logger.info("  Value: {}", strValue);
                            return strValue;
                        }
                    }
                }

                // Recursively search nested objects
                if (value instanceof JSONObject) {
                    String result = recursiveFindSessionID((JSONObject) value, currentPath);
                    if (result != null && !result.isEmpty()) {
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in recursive search: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Print JSON structure for debugging
     * @param json JSON object
     * @param indent Indentation string
     * @param level Nesting level
     */
    private void printJsonStructure(JSONObject json, String indent, int level) {
        if (level > 5) return; // Prevent too deep recursion

        try {
            for (String key : json.keySet()) {
                Object value = json.get(key);

                if (value instanceof JSONObject) {
                    logger.error("{}|- {} (Object)", indent, key);
                    printJsonStructure((JSONObject) value, indent + "  ", level + 1);
                } else if (value instanceof String) {
                    String strValue = (String) value;
                    String displayValue = strValue.length() > 50 ?
                        strValue.substring(0, 50) + "..." : strValue;
                    logger.error("{}|- {}: \"{}\"", indent, key, displayValue);
                } else {
                    logger.error("{}|- {}: {}", indent, key, value);
                }
            }
        } catch (Exception e) {
            logger.error("Error printing JSON structure: {}", e.getMessage());
        }
    }

    /**
     * Load sessionID from config file
     */
    private void loadSessionIDFromConfig() {
        String configSessionID = APIConfigManager.getSessionID();
        if (configSessionID != null && !configSessionID.isEmpty()) {
            sessionID = configSessionID;
            logger.info("SessionID loaded from config: {}", sessionID);
        }
    }

    /**
     * Get current sessionID from memory
     * @return sessionID value
     */
    public static String getSessionID() {
        return sessionID;
    }

    /**
     * Set sessionID manually
     * @param sessionId sessionID value
     */
    public static void setSessionID(String sessionId) {
        sessionID = sessionId;
    }

    // ========== RESTASSURED-BASED METHODS ==========

    /**
     * Send login request using RestAssured with DTO objects
     * Accepts request as Java object, serializes to JSON, and deserializes response to List
     *
     * @param url Full API endpoint URL
     * @param request LoginRequestDTO object to be serialized to JSON
     * @return List of LoginResponseDTO objects deserialized from JSON response
     */
    @Step("Send login request using RestAssured to: {url}")
    public List<LoginResponseDTO> sendLoginRequestWithDTO(String url, LoginRequestDTO request) {
        try {
            logger.info("=".repeat(60));
            logger.info("SENDING LOGIN REQUEST (RestAssured + DTO)");
            logger.info("=".repeat(60));
            logger.info("URL: {}", url);
            logger.info("Request Object: {}", objectMapper.writeValueAsString(request));

            // Build RestAssured request with headers
            RequestSpecification requestSpec = RestAssured.given()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(request);

            // Add auth token if present
            if (authToken != null && !authToken.isEmpty()) {
                requestSpec.header("Authorization", "Bearer " + authToken);
            }

            // Send POST request
            Response response = requestSpec.post(url);

            logger.info("-".repeat(60));
            logger.info("RESPONSE RECEIVED");
            logger.info("-".repeat(60));
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody().asString());

            // Deserialize response to List<LoginResponseDTO>
            List<LoginResponseDTO> responseList = new ArrayList<>();

            // Try to deserialize as array/list first
            try {
                responseList = objectMapper.readValue(
                    response.getBody().asString(),
                    new TypeReference<List<LoginResponseDTO>>() {}
                );
                logger.info("Response deserialized as List with {} items", responseList.size());
            } catch (Exception e) {
                // If not a list, try as single object and wrap in list
                logger.info("Response is not a list, deserializing as single object");
                LoginResponseDTO singleResponse = objectMapper.readValue(
                    response.getBody().asString(),
                    LoginResponseDTO.class
                );
                responseList.add(singleResponse);
            }

            // Check if response is successful (200)
            if (response.getStatusCode() == 200 && !responseList.isEmpty()) {
                logger.info("✓ Response Status: SUCCESS (200)");

                // Extract sessionID from first response object
                String extractedSessionID = responseList.get(0).getSessionID();
                if (extractedSessionID != null && !extractedSessionID.isEmpty()) {
                    sessionID = extractedSessionID;
                    logger.info("✓ SessionID extracted: {}", sessionID);

                    // Save to config
                    boolean saved = APIConfigManager.updateSessionID(sessionID);
                    if (saved) {
                        logger.info("✓ SessionID saved to config file");
                    }
                } else {
                    logger.warn("⚠ SessionID not found in response");
                }
            } else {
                logger.error("✗ Response Status: FAILED ({})", response.getStatusCode());
            }

            logger.info("=".repeat(60));
            return responseList;

        } catch (Exception e) {
            logger.error("ERROR sending login request with DTO: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send login request with DTO", e);
        }
    }

    /**
     * Generic POST request using RestAssured that accepts any request DTO
     * and returns response as List of specified type
     *
     * @param url Full API endpoint URL
     * @param request Request object to be serialized to JSON
     * @param responseType Class type for response deserialization
     * @param <T> Request type
     * @param <R> Response type
     * @return List of response objects of type R
     */
    @Step("Send POST request using RestAssured to: {url}")
    public <T, R> List<R> sendPostRequestWithDTO(String url, T request, Class<R> responseType) {
        try {
            logger.info("=".repeat(60));
            logger.info("SENDING POST REQUEST (RestAssured + Generic DTO)");
            logger.info("=".repeat(60));
            logger.info("URL: {}", url);
            logger.info("Request Type: {}", request.getClass().getSimpleName());
            logger.info("Response Type: {}", responseType.getSimpleName());

            // Build RestAssured request
            RequestSpecification requestSpec = RestAssured.given()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(request);

            // Add auth token if present
            if (authToken != null && !authToken.isEmpty()) {
                requestSpec.header("Authorization", "Bearer " + authToken);
            }

            // Send POST request
            Response response = requestSpec.post(url);

            logger.info("-".repeat(60));
            logger.info("RESPONSE RECEIVED");
            logger.info("-".repeat(60));
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody().asString());

            // Deserialize response to List
            List<R> responseList = new ArrayList<>();

            try {
                // Try as list first
                responseList = objectMapper.readValue(
                    response.getBody().asString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, responseType)
                );
                logger.info("Response deserialized as List with {} items", responseList.size());
            } catch (Exception e) {
                // If not a list, deserialize as single object
                logger.info("Response is not a list, deserializing as single object");
                R singleResponse = objectMapper.readValue(
                    response.getBody().asString(),
                    responseType
                );
                responseList.add(singleResponse);
            }

            logger.info("=".repeat(60));
            return responseList;

        } catch (Exception e) {
            logger.error("ERROR sending POST request with DTO: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send POST request with DTO", e);
        }
    }

    /**
     * Send login request using RestAssured and return single response object
     * Convenience method that returns single object instead of list
     *
     * @param url Full API endpoint URL
     * @param request LoginRequestDTO object
     * @return Single LoginResponseDTO object
     */
    @Step("Send login request (single response) using RestAssured to: {url}")
    public LoginResponseDTO sendLoginRequestSingle(String url, LoginRequestDTO request) {
        List<LoginResponseDTO> responseList = sendLoginRequestWithDTO(url, request);
        if (responseList != null && !responseList.isEmpty()) {
            return responseList.get(0);
        }
        return null;
    }

    /**
     * Get ObjectMapper instance for custom serialization/deserialization
     * @return ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    // ========== END RESTASSURED METHODS ==========

    /**
     * Clear sessionID from memory and config
     * @return true if cleared successfully
     */
    public static boolean clearSessionID() {
        sessionID = null;
        return APIConfigManager.clearSessionID();
    }

    /**
     * Get endpoint URL from config
     * @return endpoint URL
     */
    public static String getEndpointURL() {
        return APIConfigManager.getEndpointURL();
    }

    /**
     * Get base URL from config
     * @return base URL
     */
    public static String getConfigBaseUrl() {
        return APIConfigManager.getBaseUrl();
    }
}
