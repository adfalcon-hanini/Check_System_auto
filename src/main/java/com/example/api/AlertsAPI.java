package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Alerts Management
 */
public class AlertsAPI extends BaseAPIClient {

    private static final String ALERTS_ENDPOINT = "/api/alerts";

    public AlertsAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get all alerts for NIN: {nin}")
    public String getAlerts(String nin) throws Exception {
        String response = executeGet(ALERTS_ENDPOINT + "?nin=" + nin);
        logger.info("Alerts retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get alert by ID: {alertId}")
    public String getAlertById(String alertId) throws Exception {
        String response = executeGet(ALERTS_ENDPOINT + "/" + alertId);
        logger.info("Alert {} retrieved", alertId);
        return response;
    }

    @Step("Create new alert")
    public String createAlert(String alertJson) throws Exception {
        String response = executePost(ALERTS_ENDPOINT, alertJson);
        logger.info("New alert created");
        return response;
    }

    @Step("Update alert: {alertId}")
    public String updateAlert(String alertId, String alertJson) throws Exception {
        String response = executePut(ALERTS_ENDPOINT + "/" + alertId, alertJson);
        logger.info("Alert {} updated", alertId);
        return response;
    }

    @Step("Delete alert: {alertId}")
    public String deleteAlert(String alertId) throws Exception {
        String response = executeDelete(ALERTS_ENDPOINT + "/" + alertId);
        logger.info("Alert {} deleted", alertId);
        return response;
    }

    @Step("Get active alerts")
    public String getActiveAlerts() throws Exception {
        String response = executeGet(ALERTS_ENDPOINT + "/active");
        logger.info("Active alerts retrieved");
        return response;
    }
}
