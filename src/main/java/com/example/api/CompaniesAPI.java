package com.example.api;

import io.qameta.allure.Step;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * API Client for Companies operations
 */
public class CompaniesAPI extends BaseAPIClient {

    public CompaniesAPI(String baseUrl) {
        super(baseUrl);
    }

    /**
     * Send request to URL with JSON body and return response
     * @param urlEndpoint Complete URL endpoint (e.g., "https://api.example.com/v1/data")
     * @param jsonRequest JSON request body as String
     * @return APIResponse object containing status code and JSON response body
     */
    @Step("Send request to: {urlEndpoint}")
    public APIResponse sendRequest(String urlEndpoint, String jsonRequest) throws Exception {
        logger.info("=== API Request ===");
        logger.info("URL: {}", urlEndpoint);
        logger.info("JSON Request: {}", jsonRequest);

        // Create URL object
        URL url = new URL(urlEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method to POST
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Send JSON request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get response code
        int statusCode = connection.getResponseCode();
        logger.info("Response Status Code: {}", statusCode);

        // Read response
        BufferedReader in;
        if (statusCode >= 200 && statusCode < 300) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseBody = response.toString();

        // Log and print response
        logger.info("=== API Response ===");
        logger.info("Status Code: {}", statusCode);
        logger.info("Response Body: {}", responseBody);

        System.out.println("\n=== API Response ===");
        System.out.println("Status Code: " + statusCode);
        System.out.println("JSON Response: " + responseBody);
        System.out.println("====================\n");

        // Create and return APIResponse object
        APIResponse apiResponse = new APIResponse(responseBody, statusCode, urlEndpoint);
        return apiResponse;
    }
}
