package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for My Calc (Calculator/Study) operations
 */
public class MyCalcAPI extends BaseAPIClient {

    private static final String MYCALC_ENDPOINT = "/api/mycalc";

    public MyCalcAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get calculator data for NIN: {nin}")
    public String getCalculatorData(String nin) throws Exception {
        String response = executeGet(MYCALC_ENDPOINT + "?nin=" + nin);
        logger.info("Calculator data retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Create calculator study")
    public String createCalculatorStudy(String studyJson) throws Exception {
        String response = executePost(MYCALC_ENDPOINT, studyJson);
        logger.info("Calculator study created");
        return response;
    }

    @Step("Update calculator study: {studyId}")
    public String updateCalculatorStudy(String studyId, String studyJson) throws Exception {
        String response = executePut(MYCALC_ENDPOINT + "/" + studyId, studyJson);
        logger.info("Calculator study {} updated", studyId);
        return response;
    }

    @Step("Delete calculator study: {studyId}")
    public String deleteCalculatorStudy(String studyId) throws Exception {
        String response = executeDelete(MYCALC_ENDPOINT + "/" + studyId);
        logger.info("Calculator study {} deleted", studyId);
        return response;
    }

    @Step("Get calculator study by ID: {studyId}")
    public String getCalculatorStudyById(String studyId) throws Exception {
        String response = executeGet(MYCALC_ENDPOINT + "/" + studyId);
        logger.info("Calculator study {} retrieved", studyId);
        return response;
    }

    @Step("Calculate portfolio value")
    public String calculatePortfolioValue(String nin, String calculationJson) throws Exception {
        String response = executePost(MYCALC_ENDPOINT + "/calculate/portfolio?nin=" + nin, calculationJson);
        logger.info("Portfolio value calculated for NIN: {}", nin);
        return response;
    }

    @Step("Get study history for NIN: {nin}")
    public String getStudyHistory(String nin) throws Exception {
        String response = executeGet(MYCALC_ENDPOINT + "/history?nin=" + nin);
        logger.info("Study history retrieved for NIN: {}", nin);
        return response;
    }
}
