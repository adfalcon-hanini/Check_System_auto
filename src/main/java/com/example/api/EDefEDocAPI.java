package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for E_DEF / E_DOC operations
 */
public class EDefEDocAPI extends BaseAPIClient {

    private static final String EDEF_ENDPOINT = "/api/edef";
    private static final String EDOC_ENDPOINT = "/api/edoc";

    public EDefEDocAPI(String baseUrl) {
        super(baseUrl);
    }

    @Step("Get E_DEF documents")
    public String getEDefDocuments() throws Exception {
        String response = executeGet(EDEF_ENDPOINT);
        logger.info("E_DEF documents retrieved");
        return response;
    }

    @Step("Get E_DEF document by ID: {documentId}")
    public String getEDefDocumentById(String documentId) throws Exception {
        String response = executeGet(EDEF_ENDPOINT + "/" + documentId);
        logger.info("E_DEF document {} retrieved", documentId);
        return response;
    }

    @Step("Add E_DEF document")
    public String addEDefDocument(String documentJson) throws Exception {
        String response = executePost(EDEF_ENDPOINT, documentJson);
        logger.info("E_DEF document added successfully");
        return response;
    }

    @Step("Get E_DOC documents")
    public String getEDocDocuments() throws Exception {
        String response = executeGet(EDOC_ENDPOINT);
        logger.info("E_DOC documents retrieved");
        return response;
    }

    @Step("Get E_DOC document by ID: {documentId}")
    public String getEDocDocumentById(String documentId) throws Exception {
        String response = executeGet(EDOC_ENDPOINT + "/" + documentId);
        logger.info("E_DOC document {} retrieved", documentId);
        return response;
    }

    @Step("Add E_DOC document")
    public String addEDocDocument(String documentJson) throws Exception {
        String response = executePost(EDOC_ENDPOINT, documentJson);
        logger.info("E_DOC document added successfully");
        return response;
    }

    @Step("Update E_DEF document: {documentId}")
    public String updateEDefDocument(String documentId, String documentJson) throws Exception {
        String response = executePut(EDEF_ENDPOINT + "/" + documentId, documentJson);
        logger.info("E_DEF document {} updated", documentId);
        return response;
    }

    @Step("Delete E_DEF document: {documentId}")
    public String deleteEDefDocument(String documentId) throws Exception {
        String response = executeDelete(EDEF_ENDPOINT + "/" + documentId);
        logger.info("E_DEF document {} deleted", documentId);
        return response;
    }
}
