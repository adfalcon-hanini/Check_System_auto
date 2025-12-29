package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for BankBal API (BNKBAL)
 * Maps to the JSON request structure for bank balance endpoint
 *
 * JSON Structure:
 * {
 *   "Srv": "BNKBAL",
 *   "Message": {
 *     "BalDlsRequest": {
 *       "NIN": "12240",
 *       "UserId": "12240",
 *       "UserType": "INVESTOR",
 *       "AccRegisterType": "ALL",
 *       "Lang": "ARB"
 *     }
 *   },
 *   "Version": "1",
 *   "AppId": "WEB_ORDERS.25",
 *   "LstLogin": "30-04-2025",
 *   "sessionId": "{{SessionID}}"
 * }
 */
public class BankBalRequestDTO {

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
    public BankBalRequestDTO() {
    }

    // Constructor with all fields
    public BankBalRequestDTO(String srv, MessageDTO message, String version,
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

        @JsonProperty("BalDlsRequest")
        private BalDlsRequestDTO balDlsRequest;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(BalDlsRequestDTO balDlsRequest) {
            this.balDlsRequest = balDlsRequest;
        }

        // Getter
        public BalDlsRequestDTO getBalDlsRequest() {
            return balDlsRequest;
        }
    }

    /**
     * Nested DTO for BalDlsRequest details
     */
    public static class BalDlsRequestDTO {

        @JsonProperty("NIN")
        private String nin;

        @JsonProperty("UserId")
        private String userId;

        @JsonProperty("UserType")
        private String userType;

        @JsonProperty("AccRegisterType")
        private String accRegisterType;

        @JsonProperty("Lang")
        private String lang;

        // Default constructor
        public BalDlsRequestDTO() {
        }

        // Constructor with all fields
        public BalDlsRequestDTO(String nin, String userId, String userType,
                                String accRegisterType, String lang) {
            this.nin = nin;
            this.userId = userId;
            this.userType = userType;
            this.accRegisterType = accRegisterType;
            this.lang = lang;
        }

        // Getters
        public String getNin() {
            return nin;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserType() {
            return userType;
        }

        public String getAccRegisterType() {
            return accRegisterType;
        }

        public String getLang() {
            return lang;
        }
    }

    /**
     * Builder pattern for easy construction
     */
    public static class Builder {
        private String srv = "BNKBAL";
        private String version = "1";
        private String appId = "WEB_ORDERS.25";
        private String lstLogin;
        private String nin;
        private String userId;
        private String userType = "INVESTOR";
        private String accRegisterType = "ALL";
        private String lang = "ARB";
        private String sessionId;

        public Builder nin(String nin) {
            this.nin = nin;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userType(String userType) {
            this.userType = userType;
            return this;
        }

        public Builder accRegisterType(String accRegisterType) {
            this.accRegisterType = accRegisterType;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
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

        public BankBalRequestDTO build() {
            // If userId not set, use nin as userId
            if (userId == null || userId.isEmpty()) {
                userId = nin;
            }

            BalDlsRequestDTO balDlsRequest = new BalDlsRequestDTO(nin, userId, userType, accRegisterType, lang);
            MessageDTO message = new MessageDTO(balDlsRequest);
            return new BankBalRequestDTO(srv, message, version, appId, lstLogin, sessionId);
        }
    }

    @Override
    public String toString() {
        return "BankBalRequestDTO{" +
                "srv='" + srv + '\'' +
                ", nin='" + (message != null && message.balDlsRequest != null ?
                    message.balDlsRequest.nin : "null") + '\'' +
                ", userId='" + (message != null && message.balDlsRequest != null ?
                    message.balDlsRequest.userId : "null") + '\'' +
                ", userType='" + (message != null && message.balDlsRequest != null ?
                    message.balDlsRequest.userType : "null") + '\'' +
                ", accRegisterType='" + (message != null && message.balDlsRequest != null ?
                    message.balDlsRequest.accRegisterType : "null") + '\'' +
                ", lang='" + (message != null && message.balDlsRequest != null ?
                    message.balDlsRequest.lang : "null") + '\'' +
                ", version='" + version + '\'' +
                ", sessionId='" + (sessionId != null ? "***" : "null") + '\'' +
                '}';
    }
}
