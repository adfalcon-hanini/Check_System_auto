package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Session Token operations
 */
public class SessionTokenAPI extends BaseAPIClient {

    private static final String SESSION_ENDPOINT = "/api/session";
    private static final String TOKEN_ENDPOINT = "/api/session/token";

    public SessionTokenAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Create new session")
    public String createSession(String username) throws Exception {
        String body = String.format("{\"username\":\"%s\"}", username);
        String response = executePost(SESSION_ENDPOINT, body);
        logger.info("Session created for user: {}", username);
        return response;
    }

    @Step("Get session token")
    public String getSessionToken() throws Exception {
        String response = executeGet(TOKEN_ENDPOINT);
        logger.info("Session token retrieved");
        return response;
    }

    @Step("Validate session token")
    public String validateSessionToken(String token) throws Exception {
        String body = String.format("{\"token\":\"%s\"}", token);
        String response = executePost(TOKEN_ENDPOINT + "/validate", body);
        logger.info("Session token validated");
        return response;
    }

    @Step("Refresh session token")
    public String refreshSessionToken(String token) throws Exception {
        String body = String.format("{\"token\":\"%s\"}", token);
        String response = executePost(TOKEN_ENDPOINT + "/refresh", body);
        logger.info("Session token refreshed");
        return response;
    }

    @Step("Invalidate session token")
    public String invalidateSessionToken(String token) throws Exception {
        String body = String.format("{\"token\":\"%s\"}", token);
        String response = executePost(TOKEN_ENDPOINT + "/invalidate", body);
        logger.info("Session token invalidated");
        return response;
    }

    @Step("Get active sessions for user: {username}")
    public String getActiveSessions(String username) throws Exception {
        String response = executeGet(SESSION_ENDPOINT + "/active?username=" + username);
        logger.info("Active sessions retrieved for user: {}", username);
        return response;
    }

    @Step("Terminate session: {sessionId}")
    public String terminateSession(String sessionId) throws Exception {
        String response = executeDelete(SESSION_ENDPOINT + "/" + sessionId);
        logger.info("Session {} terminated", sessionId);
        return response;
    }
}
