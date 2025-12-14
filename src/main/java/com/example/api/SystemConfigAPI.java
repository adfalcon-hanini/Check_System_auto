package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for System Configuration operations
 */
public class SystemConfigAPI extends BaseAPIClient {

    private static final String CONFIG_ENDPOINT = "/api/system/config";

    public SystemConfigAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get system configuration")
    public String getSystemConfig() throws Exception {
        String response = executeGet(CONFIG_ENDPOINT);
        logger.info("System configuration retrieved");
        return response;
    }

    @Step("Update system configuration")
    public String updateSystemConfig(String configJson) throws Exception {
        String response = executePut(CONFIG_ENDPOINT, configJson);
        logger.info("System configuration updated");
        return response;
    }

    @Step("Get specific config parameter: {paramName}")
    public String getConfigParameter(String paramName) throws Exception {
        String response = executeGet(CONFIG_ENDPOINT + "/" + paramName);
        logger.info("Config parameter {} retrieved", paramName);
        return response;
    }
}
