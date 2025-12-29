package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for PREFERRED_COMPANIES API
 * Maps to the JSON request structure for preferred companies endpoint
 */
public class PreferredCompaniesRequestDTO {

    @JsonProperty("Srv")
    private String srv;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("AppId")
    private String appId;

    @JsonProperty("LstLogin")
    private String lstLogin;

    @JsonProperty("sessionId")
    private String sessionId;

    // Default constructor
    public PreferredCompaniesRequestDTO() {
    }

    // Constructor with all fields
    public PreferredCompaniesRequestDTO(String srv, MessageDTO message, String version,
                                        String appId, String lstLogin, String sessionId) {
        this.srv = srv;
        this.message = message;
        this.version = version;
        this.appId = appId;
        this.lstLogin = lstLogin;
        this.sessionId = sessionId;
    }

    // Getters
    public String getSrv() {
        return srv;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }

    public String getAppId() {
        return appId;
    }

    public String getLstLogin() {
        return lstLogin;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * Nested DTO for Message object
     */
    public static class MessageDTO {

        @JsonProperty("CompaniesRequest")
        private CompaniesRequestDTO companiesRequest;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(CompaniesRequestDTO companiesRequest) {
            this.companiesRequest = companiesRequest;
        }

        // Getter
        public CompaniesRequestDTO getCompaniesRequest() {
            return companiesRequest;
        }
    }

    /**
     * Nested DTO for CompaniesRequest details
     */
    public static class CompaniesRequestDTO {

        @JsonProperty("NIN")
        private String nin;

        // Default constructor
        public CompaniesRequestDTO() {
        }

        // Constructor
        public CompaniesRequestDTO(String nin) {
            this.nin = nin;
        }

        // Getter
        public String getNin() {
            return nin;
        }
    }

    /**
     * Builder pattern for easy construction
     */
    public static class Builder {
        private String srv = "PREFERRED_COMPANIES";
        private String version = "1.0";
        private String appId = "WEB_ORDERS.25";
        private String lstLogin;
        private String nin;
        private String sessionId;

        public Builder nin(String nin) {
            this.nin = nin;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder lstLogin(String lstLogin) {
            this.lstLogin = lstLogin;
            return this;
        }

        public Builder srv(String srv) {
            this.srv = srv;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public PreferredCompaniesRequestDTO build() {
            CompaniesRequestDTO companiesRequest = new CompaniesRequestDTO(nin);
            MessageDTO message = new MessageDTO(companiesRequest);
            return new PreferredCompaniesRequestDTO(srv, message, version, appId, lstLogin, sessionId);
        }
    }

    @Override
    public String toString() {
        return "PreferredCompaniesRequestDTO{" +
                "srv='" + srv + '\'' +
                ", nin='" + (message != null && message.companiesRequest != null ?
                    message.companiesRequest.nin : "null") + '\'' +
                ", version='" + version + '\'' +
                ", sessionId='" + (sessionId != null ? "***" : "null") + '\'' +
                '}';
    }
}
