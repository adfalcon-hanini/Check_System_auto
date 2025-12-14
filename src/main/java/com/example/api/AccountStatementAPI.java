package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Account Statement operations
 */
public class AccountStatementAPI extends BaseAPIClient {

    private static final String STATEMENT_ENDPOINT = "/api/account/statement";

    public AccountStatementAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get account statement for NIN: {nin}")
    public String getAccountStatement(String nin, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s?nin=%s&startDate=%s&endDate=%s",
            STATEMENT_ENDPOINT, nin, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Account statement retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get account statement by account number: {accountNumber}")
    public String getAccountStatementByAccount(String accountNumber, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/account/%s?startDate=%s&endDate=%s",
            STATEMENT_ENDPOINT, accountNumber, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Account statement retrieved for account: {}", accountNumber);
        return response;
    }

    @Step("Download account statement as PDF")
    public String downloadAccountStatementPDF(String nin, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/pdf?nin=%s&startDate=%s&endDate=%s",
            STATEMENT_ENDPOINT, nin, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Account statement PDF downloaded for NIN: {}", nin);
        return response;
    }

    @Step("Download account statement as Excel")
    public String downloadAccountStatementExcel(String nin, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/excel?nin=%s&startDate=%s&endDate=%s",
            STATEMENT_ENDPOINT, nin, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Account statement Excel downloaded for NIN: {}", nin);
        return response;
    }

    @Step("Get statement summary for NIN: {nin}")
    public String getStatementSummary(String nin) throws Exception {
        String response = executeGet(STATEMENT_ENDPOINT + "/summary?nin=" + nin);
        logger.info("Statement summary retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get recent transactions for NIN: {nin}")
    public String getRecentTransactions(String nin, int limit) throws Exception {
        String endpoint = String.format("%s/recent?nin=%s&limit=%d", STATEMENT_ENDPOINT, nin, limit);
        String response = executeGet(endpoint);
        logger.info("Recent transactions retrieved for NIN: {}", nin);
        return response;
    }
}
