package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Cash Distribution operations
 */
public class CashDistributionAPI extends BaseAPIClient {

    private static final String CASH_DISTRIBUTION_ENDPOINT = "/api/cash/distribution";

    public CashDistributionAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get cash distribution info for NIN: {nin}")
    public String getCashDistribution(String nin) throws Exception {
        String response = executeGet(CASH_DISTRIBUTION_ENDPOINT + "?nin=" + nin);
        logger.info("Cash distribution info retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Apply for cash distribution")
    public String applyCashDistribution(String applicationJson) throws Exception {
        String response = executePost(CASH_DISTRIBUTION_ENDPOINT + "/apply", applicationJson);
        logger.info("Cash distribution application submitted");
        return response;
    }

    @Step("Get cash distribution application status: {applicationId}")
    public String getCashDistributionStatus(String applicationId) throws Exception {
        String response = executeGet(CASH_DISTRIBUTION_ENDPOINT + "/status/" + applicationId);
        logger.info("Cash distribution application {} status retrieved", applicationId);
        return response;
    }

    @Step("Get cash distribution history for NIN: {nin}")
    public String getCashDistributionHistory(String nin) throws Exception {
        String response = executeGet(CASH_DISTRIBUTION_ENDPOINT + "/history?nin=" + nin);
        logger.info("Cash distribution history retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Cancel cash distribution application: {applicationId}")
    public String cancelCashDistribution(String applicationId) throws Exception {
        String response = executePost(CASH_DISTRIBUTION_ENDPOINT + "/cancel/" + applicationId, "{}");
        logger.info("Cash distribution application {} cancelled", applicationId);
        return response;
    }

    @Step("Get available cash distributions")
    public String getAvailableCashDistributions(String nin) throws Exception {
        String response = executeGet(CASH_DISTRIBUTION_ENDPOINT + "/available?nin=" + nin);
        logger.info("Available cash distributions retrieved for NIN: {}", nin);
        return response;
    }
}
