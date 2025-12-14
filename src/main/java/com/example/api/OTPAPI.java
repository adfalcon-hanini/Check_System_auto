package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for OTP (One-Time Password) operations
 */
public class OTPAPI extends BaseAPIClient {

    private static final String OTP_ENDPOINT = "/api/otp";

    public OTPAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Generate OTP for user: {username}")
    public String generateOTP(String username, String method) throws Exception {
        String body = String.format("{\"username\":\"%s\",\"method\":\"%s\"}", username, method);
        String response = executePost(OTP_ENDPOINT + "/generate", body);
        logger.info("OTP generated for user: {} via {}", username, method);
        return response;
    }

    @Step("Verify OTP")
    public String verifyOTP(String username, String otp) throws Exception {
        String body = String.format("{\"username\":\"%s\",\"otp\":\"%s\"}", username, otp);
        String response = executePost(OTP_ENDPOINT + "/verify", body);
        logger.info("OTP verified for user: {}", username);
        return response;
    }

    @Step("Resend OTP for user: {username}")
    public String resendOTP(String username) throws Exception {
        String body = String.format("{\"username\":\"%s\"}", username);
        String response = executePost(OTP_ENDPOINT + "/resend", body);
        logger.info("OTP resent for user: {}", username);
        return response;
    }

    @Step("Check OTP status for user: {username}")
    public String getOTPStatus(String username) throws Exception {
        String response = executeGet(OTP_ENDPOINT + "/status?username=" + username);
        logger.info("OTP status retrieved for user: {}", username);
        return response;
    }

    @Step("Expire OTP for user: {username}")
    public String expireOTP(String username) throws Exception {
        String body = String.format("{\"username\":\"%s\"}", username);
        String response = executePost(OTP_ENDPOINT + "/expire", body);
        logger.info("OTP expired for user: {}", username);
        return response;
    }
}
