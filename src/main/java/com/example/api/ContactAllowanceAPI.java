package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Contact Allowance Management
 */
public class ContactAllowanceAPI extends BaseAPIClient {

    private static final String CONTACT_ALLOWANCE_ENDPOINT = "/api/contact/allowance";

    public ContactAllowanceAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get contact allowance settings")
    public String getContactAllowance() throws Exception {
        String response = executeGet(CONTACT_ALLOWANCE_ENDPOINT);
        logger.info("Contact allowance settings retrieved");
        return response;
    }

    @Step("Update contact allowance settings")
    public String updateContactAllowance(String settingsJson) throws Exception {
        String response = executePut(CONTACT_ALLOWANCE_ENDPOINT, settingsJson);
        logger.info("Contact allowance settings updated");
        return response;
    }

    @Step("Enable contact method: {method}")
    public String enableContactMethod(String method) throws Exception {
        String body = String.format("{\"method\":\"%s\",\"enabled\":true}", method);
        String response = executePost(CONTACT_ALLOWANCE_ENDPOINT + "/enable", body);
        logger.info("Contact method {} enabled", method);
        return response;
    }

    @Step("Disable contact method: {method}")
    public String disableContactMethod(String method) throws Exception {
        String body = String.format("{\"method\":\"%s\",\"enabled\":false}", method);
        String response = executePost(CONTACT_ALLOWANCE_ENDPOINT + "/disable", body);
        logger.info("Contact method {} disabled", method);
        return response;
    }
}
