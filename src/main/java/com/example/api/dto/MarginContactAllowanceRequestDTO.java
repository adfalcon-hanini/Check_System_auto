package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for MarginContactAllowance API (MARGIN_CONTACT_ALLOWANCE)
 * Maps to the JSON request structure for margin contract allowance endpoint
 *
 * JSON Structure:
 * {
 *   "Srv": "MARGIN_CONTACT_ALLOWANCE",
 *   "Message": {
 *     "MarginContractAllowanceRequest": {
 *       "nin": "12240"
 *     }
 *   },
 *   "Version": "1",
 *   "AppId": "WEB_ORDERS.25",
 *   "LstLogin": "30-04-2025",
 *   "sessionId": "{{SessionID}}"
 * }
 */
public class MarginContactAllowanceRequestDTO {

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
    public MarginContactAllowanceRequestDTO() {
    }

    // Constructor with all fields
    public MarginContactAllowanceRequestDTO(String srv, MessageDTO message, String version,
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

        @JsonProperty("MarginContractAllowanceRequest")
        private MarginContractAllowanceRequestDTO marginContractAllowanceRequest;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(MarginContractAllowanceRequestDTO marginContractAllowanceRequest) {
            this.marginContractAllowanceRequest = marginContractAllowanceRequest;
        }

        // Getter
        public MarginContractAllowanceRequestDTO getMarginContractAllowanceRequest() {
            return marginContractAllowanceRequest;
        }
    }

    /**
     * Nested DTO for MarginContractAllowanceRequest details
     */
    public static class MarginContractAllowanceRequestDTO {

        @JsonProperty("nin")
        private String nin;

        // Default constructor
        public MarginContractAllowanceRequestDTO() {
        }

        // Constructor
        public MarginContractAllowanceRequestDTO(String nin) {
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
        private String srv = "MARGIN_CONTACT_ALLOWANCE";
        private String version = "1";
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

        public MarginContactAllowanceRequestDTO build() {
            MarginContractAllowanceRequestDTO marginRequest = new MarginContractAllowanceRequestDTO(nin);
            MessageDTO message = new MessageDTO(marginRequest);
            return new MarginContactAllowanceRequestDTO(srv, message, version, appId, lstLogin, sessionId);
        }
    }

    @Override
    public String toString() {
        return "MarginContactAllowanceRequestDTO{" +
                "srv='" + srv + '\'' +
                ", nin='" + (message != null && message.marginContractAllowanceRequest != null ?
                    message.marginContractAllowanceRequest.nin : "null") + '\'' +
                ", version='" + version + '\'' +
                ", sessionId='" + (sessionId != null ? "***" : "null") + '\'' +
                '}';
    }
}
