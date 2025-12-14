package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Banks Information operations
 */
public class BanksInfoAPI extends BaseAPIClient {

    private static final String BANKS_ENDPOINT = "/api/banks";
    private static final String BANK_TRANSFER_ENDPOINT = "/api/banks/transfer";
    private static final String QIIB_IBAN_ENDPOINT = "/api/banks/qiib/iban";

    public BanksInfoAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get all banks information")
    public String getAllBanks() throws Exception {
        String response = executeGet(BANKS_ENDPOINT);
        logger.info("All banks information retrieved");
        return response;
    }

    @Step("Get bank info by code: {bankCode}")
    public String getBankByCode(String bankCode) throws Exception {
        String response = executeGet(BANKS_ENDPOINT + "/" + bankCode);
        logger.info("Bank {} information retrieved", bankCode);
        return response;
    }

    @Step("Get bank accounts for NIN: {nin}")
    public String getBankAccounts(String nin) throws Exception {
        String response = executeGet(BANKS_ENDPOINT + "/accounts?nin=" + nin);
        logger.info("Bank accounts retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Add new bank account")
    public String addNewBankAccount(String accountJson) throws Exception {
        String response = executePost(BANKS_ENDPOINT + "/accounts", accountJson);
        logger.info("New bank account added");
        return response;
    }

    @Step("Remove bank account: {accountId}")
    public String removeBankAccount(String accountId) throws Exception {
        String response = executeDelete(BANKS_ENDPOINT + "/accounts/" + accountId);
        logger.info("Bank account {} removed", accountId);
        return response;
    }

    @Step("Activate bank account: {accountId}")
    public String activateBankAccount(String accountId) throws Exception {
        String body = String.format("{\"accountId\":\"%s\",\"active\":true}", accountId);
        String response = executePost(BANKS_ENDPOINT + "/accounts/activate", body);
        logger.info("Bank account {} activated", accountId);
        return response;
    }

    @Step("Transfer between banks")
    public String bankTransfer(String transferJson) throws Exception {
        String response = executePost(BANK_TRANSFER_ENDPOINT, transferJson);
        logger.info("Bank transfer executed");
        return response;
    }

    @Step("Get QIIB IBAN list")
    public String getQIIBIBANList() throws Exception {
        String response = executeGet(QIIB_IBAN_ENDPOINT);
        logger.info("QIIB IBAN list retrieved");
        return response;
    }

    @Step("Get QIIB IBAN for account: {accountNumber}")
    public String getQIIBIBAN(String accountNumber) throws Exception {
        String response = executeGet(QIIB_IBAN_ENDPOINT + "/" + accountNumber);
        logger.info("QIIB IBAN retrieved for account: {}", accountNumber);
        return response;
    }
}
