package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Customer Assets operations
 */
public class CustomerAssetsAPI extends BaseAPIClient {

    private static final String ASSETS_ENDPOINT = "/api/customer/assets";

    public CustomerAssetsAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get customer assets for NIN: {nin}")
    public String getCustomerAssets(String nin) throws Exception {
        String response = executeGet(ASSETS_ENDPOINT + "?nin=" + nin);
        logger.info("Customer assets retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get customer asset by ID: {assetId}")
    public String getAssetById(String assetId) throws Exception {
        String response = executeGet(ASSETS_ENDPOINT + "/" + assetId);
        logger.info("Asset {} details retrieved", assetId);
        return response;
    }

    @Step("Get customer portfolio summary for NIN: {nin}")
    public String getPortfolioSummary(String nin) throws Exception {
        String response = executeGet(ASSETS_ENDPOINT + "/portfolio/" + nin);
        logger.info("Portfolio summary retrieved for NIN: {}", nin);
        return response;
    }
}
