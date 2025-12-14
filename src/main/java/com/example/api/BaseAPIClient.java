package com.example.api;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Base API Client for making HTTP requests
 * Provides common functionality for all API endpoint classes
 */
public class BaseAPIClient {

    protected static final Logger logger = LoggerFactory.getLogger(BaseAPIClient.class);
    protected String baseUrl = "https://devuat.thegroup.com.qa";
    protected String authToken;
    protected Map<String, String> defaultHeaders;

    /**
     * Constructor with base URL
     * @param baseUrl Base URL for API endpoints
     */
    public BaseAPIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Set authentication token
     * @param token Auth token
     */
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Set default headers
     * @param headers Map of default headers
     */
    public void setDefaultHeaders(Map<String, String> headers) {
        this.defaultHeaders = headers;
    }

    /**
     * Execute GET request
     * @param endpoint API endpoint
     * @return Response as String
     */
    @Step("GET Request: {endpoint}")
    protected String executeGet(String endpoint) throws Exception {
        return executeRequest(endpoint, "GET", null);
    }

    /**
     * Execute POST request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response as String
     */
    @Step("POST Request: {endpoint}")
    protected String executePost(String endpoint, String body) throws Exception {
        return executeRequest(endpoint, "POST", body);
    }

    /**
     * Execute PUT request
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response as String
     */
    @Step("PUT Request: {endpoint}")
    protected String executePut(String endpoint, String body) throws Exception {
        return executeRequest(endpoint, "PUT", body);
    }

    /**
     * Execute DELETE request
     * @param endpoint API endpoint
     * @return Response as String
     */
    @Step("DELETE Request: {endpoint}")
    protected String executeDelete(String endpoint) throws Exception {
        return executeRequest(endpoint, "DELETE", null);
    }

    /**
     * Generic HTTP request execution
     * @param endpoint API endpoint
     * @param method HTTP method
     * @param body Request body (can be null)
     * @return Response as String
     */
    private String executeRequest(String endpoint, String method, String body) throws Exception {
        APIResponse apiResponse = executeRequestWithResponse(endpoint, method, body);
        return apiResponse.getResponseBody();
    }

    /**
     * Execute POST request to a full URL (not baseUrl + endpoint)
     * @param fullUrl Complete URL to send request to
     * @param body Request body
     * @return APIResponse object with status code and response body
     */
    @Step("POST Request to full URL: {fullUrl}")
    protected APIResponse executePostToFullURL(String fullUrl, String body) throws Exception {
        return executeRequestToFullURL(fullUrl, "POST", body);
    }

    /**
     * Execute HTTP request to a full URL (not baseUrl + endpoint)
     * @param fullUrl Complete URL to send request to
     * @param method HTTP method
     * @param body Request body (can be null)
     * @return APIResponse object with status code and response body
     */
    protected APIResponse executeRequestToFullURL(String fullUrl, String method, String body) throws Exception {
        logger.info("Executing {} request to full URL: {}", method, fullUrl);

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // Set default headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Add auth token if present
        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        // Add custom headers if present
        if (defaultHeaders != null) {
            for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        // Send request body if present
        if (body != null && (method.equals("POST") || method.equals("PUT"))) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        // Read response
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        BufferedReader in;
        if (responseCode >= 200 && responseCode < 300) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info("Response: {}", response.toString());

        return new APIResponse(response.toString(), responseCode, fullUrl);
    }

    /**
     * Generic HTTP request execution with full response details
     * @param endpoint API endpoint
     * @param method HTTP method
     * @param body Request body (can be null)
     * @return APIResponse object with status code and response body
     */
    protected APIResponse executeRequestWithResponse(String endpoint, String method, String body) throws Exception {
        String fullUrl = baseUrl + endpoint;
        logger.info("Executing {} request to: {}", method, fullUrl);

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // Set default headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Add auth token if present
        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        // Add custom headers if present
        if (defaultHeaders != null) {
            for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        // Send request body if present
        if (body != null && (method.equals("POST") || method.equals("PUT"))) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        // Read response
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        BufferedReader in;
        if (responseCode >= 200 && responseCode < 300) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        logger.info("Response: {}", response.toString());

        return new APIResponse(response.toString(), responseCode, fullUrl);
    }

    /**
     * Parse JSON response (basic implementation)
     * @param response JSON response string
     * @return Parsed response
     */
    protected String parseResponse(String response) {
        return response;
    }

    /**
     * Get base URL
     * @return Base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }
}
