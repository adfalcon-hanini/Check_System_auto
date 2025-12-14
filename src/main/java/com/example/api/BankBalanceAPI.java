package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Bank Balance operations
 */
public class BankBalanceAPI extends BaseAPIClient {

    private static final String BALANCE_ENDPOINT = "/api/bank/balance";

    public BankBalanceAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get bank balance for NIN: {nin}")
    public String getBankBalance(String nin) throws Exception {
        String response = executeGet(BALANCE_ENDPOINT + "?nin=" + nin);
        logger.info("Bank balance retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get current balance for account: {accountNumber}")
    public String getCurrentBalance(String accountNumber) throws Exception {
        String response = executeGet(BALANCE_ENDPOINT + "/" + accountNumber);
        logger.info("Current balance retrieved for account: {}", accountNumber);
        return response;
    }

    @Step("Get balance history for account: {accountNumber}")
    public String getBalanceHistory(String accountNumber, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/%s/history?startDate=%s&endDate=%s",
            BALANCE_ENDPOINT, accountNumber, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Balance history retrieved for account: {}", accountNumber);
        return response;
    }
}
