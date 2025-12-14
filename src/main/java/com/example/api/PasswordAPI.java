package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Password Management
 */
public class PasswordAPI extends BaseAPIClient {

    private static final String PASSWORD_ENDPOINT = "/api/auth/password";

    public PasswordAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Change password")
    public String changePassword(String oldPassword, String newPassword) throws Exception {
        String body = String.format("{\"oldPassword\":\"%s\",\"newPassword\":\"%s\"}",
            oldPassword, newPassword);
        String response = executePost(PASSWORD_ENDPOINT + "/change", body);
        logger.info("Password changed successfully");
        return response;
    }

    @Step("Reset password for user: {username}")
    public String resetPassword(String username, String email) throws Exception {
        String body = String.format("{\"username\":\"%s\",\"email\":\"%s\"}", username, email);
        String response = executePost(PASSWORD_ENDPOINT + "/reset", body);
        logger.info("Password reset initiated for user: {}", username);
        return response;
    }

    @Step("Verify reset token")
    public String verifyResetToken(String token) throws Exception {
        String body = String.format("{\"token\":\"%s\"}", token);
        String response = executePost(PASSWORD_ENDPOINT + "/verify-token", body);
        logger.info("Reset token verified");
        return response;
    }

    @Step("Set new password with token")
    public String setNewPassword(String token, String newPassword) throws Exception {
        String body = String.format("{\"token\":\"%s\",\"newPassword\":\"%s\"}", token, newPassword);
        String response = executePost(PASSWORD_ENDPOINT + "/set-new", body);
        logger.info("New password set successfully");
        return response;
    }
}
