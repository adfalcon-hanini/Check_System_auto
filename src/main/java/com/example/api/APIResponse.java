package com.example.api;

/**
 * API Response wrapper class
 * Contains response body and HTTP status code
 */
public class APIResponse {

    private String responseBody;
    private int statusCode;
    private String requestUrl;

    public APIResponse(String responseBody, int statusCode, String requestUrl) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.requestUrl = requestUrl;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "statusCode=" + statusCode +
                ", requestUrl='" + requestUrl + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
