package com.example.api;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API Error Handling Utility
 * Provides methods for handling and parsing API errors
 */
public class ErrorHandlingAPI {

    protected static final Logger logger = LoggerFactory.getLogger(ErrorHandlingAPI.class);

    /**
     * Parse error response
     * @param errorResponse Error response string
     * @return Parsed error message
     */
    @Step("Parse error response")
    public static String parseErrorResponse(String errorResponse) {
        try {
            logger.error("API Error Response: {}", errorResponse);

            // Basic JSON error parsing
            if (errorResponse.contains("\"error\"")) {
                int startIndex = errorResponse.indexOf("\"error\":\"") + 9;
                int endIndex = errorResponse.indexOf("\"", startIndex);
                if (startIndex > 8 && endIndex > startIndex) {
                    return errorResponse.substring(startIndex, endIndex);
                }
            }

            if (errorResponse.contains("\"message\"")) {
                int startIndex = errorResponse.indexOf("\"message\":\"") + 11;
                int endIndex = errorResponse.indexOf("\"", startIndex);
                if (startIndex > 10 && endIndex > startIndex) {
                    return errorResponse.substring(startIndex, endIndex);
                }
            }

            return errorResponse;
        } catch (Exception e) {
            logger.error("Error parsing error response", e);
            return errorResponse;
        }
    }

    /**
     * Check if response indicates success
     * @param response API response
     * @return true if successful, false otherwise
     */
    @Step("Check response success")
    public static boolean isSuccessResponse(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        return !response.contains("\"error\"") &&
               !response.contains("\"success\":false") &&
               !response.contains("\"status\":\"error\"");
    }

    /**
     * Extract error code from response
     * @param errorResponse Error response string
     * @return Error code
     */
    @Step("Extract error code")
    public static String extractErrorCode(String errorResponse) {
        try {
            if (errorResponse.contains("\"errorCode\"")) {
                int startIndex = errorResponse.indexOf("\"errorCode\":\"") + 13;
                int endIndex = errorResponse.indexOf("\"", startIndex);
                if (startIndex > 12 && endIndex > startIndex) {
                    return errorResponse.substring(startIndex, endIndex);
                }
            }
            return "UNKNOWN_ERROR";
        } catch (Exception e) {
            logger.error("Error extracting error code", e);
            return "UNKNOWN_ERROR";
        }
    }

    /**
     * Log API error with details
     * @param endpoint API endpoint
     * @param errorResponse Error response
     */
    @Step("Log API error")
    public static void logAPIError(String endpoint, String errorResponse) {
        logger.error("═══════════════════════════════════════════════════════════");
        logger.error("API Error Details:");
        logger.error("Endpoint: {}", endpoint);
        logger.error("Error Response: {}", errorResponse);
        logger.error("Error Code: {}", extractErrorCode(errorResponse));
        logger.error("Error Message: {}", parseErrorResponse(errorResponse));
        logger.error("═══════════════════════════════════════════════════════════");
    }

    /**
     * Create error response object
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return Error JSON string
     */
    public static String createErrorResponse(String errorCode, String errorMessage) {
        return String.format("{\"error\":\"%s\",\"errorCode\":\"%s\",\"message\":\"%s\"}",
            errorMessage, errorCode, errorMessage);
    }
}
