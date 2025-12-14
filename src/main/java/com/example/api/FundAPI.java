package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Fund operations
 */
public class FundAPI extends BaseAPIClient {

    private static final String FUND_ENDPOINT = "/api/fund";

    public FundAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get fund information")
    public String getFundInfo() throws Exception {
        String response = executeGet(FUND_ENDPOINT);
        logger.info("Fund information retrieved");
        return response;
    }

    @Step("Get fund by ID: {fundId}")
    public String getFundById(String fundId) throws Exception {
        String response = executeGet(FUND_ENDPOINT + "/" + fundId);
        logger.info("Fund {} information retrieved", fundId);
        return response;
    }

    @Step("Get fund performance")
    public String getFundPerformance(String fundId, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/%s/performance?startDate=%s&endDate=%s",
            FUND_ENDPOINT, fundId, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Fund {} performance retrieved", fundId);
        return response;
    }

    @Step("Get fund holdings for fund: {fundId}")
    public String getFundHoldings(String fundId) throws Exception {
        String response = executeGet(FUND_ENDPOINT + "/" + fundId + "/holdings");
        logger.info("Fund {} holdings retrieved", fundId);
        return response;
    }

    @Step("Subscribe to fund: {fundId}")
    public String subscribeToFund(String fundId, String subscriptionJson) throws Exception {
        String response = executePost(FUND_ENDPOINT + "/" + fundId + "/subscribe", subscriptionJson);
        logger.info("Subscription to fund {} submitted", fundId);
        return response;
    }

    @Step("Redeem from fund: {fundId}")
    public String redeemFromFund(String fundId, String redemptionJson) throws Exception {
        String response = executePost(FUND_ENDPOINT + "/" + fundId + "/redeem", redemptionJson);
        logger.info("Redemption from fund {} submitted", fundId);
        return response;
    }
}
