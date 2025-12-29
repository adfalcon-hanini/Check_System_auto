package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for CustomerAssets API
 * Maps to the JSON request structure for customer assets endpoint
 *
 * JSON Structure:
 * {
 *   "Srv": "CustomerAssets",
 *   "Message": {
 *     "CustReq": {
 *       "NIN": "12240",
 *       "UserId": "12240",
 *       "UserType": "INVESTOR",
 *       "Lang": "ARB",
 *       "IsAddBuyBal": "F"
 *     }
 *   },
 *   "Version": "1",
 *   "AppId": "WEB_ORDERS.25",
 *   "LstLogin": "30-04-2025",
 *   "sessionId": "{{SessionID}}"
 * }
 */
public class CustomerAssetsRequestDTO {

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
    public CustomerAssetsRequestDTO() {
    }

    // Constructor with all fields
    public CustomerAssetsRequestDTO(String srv, MessageDTO message, String version,
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

        @JsonProperty("CustReq")
        private CustReqDTO custReq;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(CustReqDTO custReq) {
            this.custReq = custReq;
        }

        // Getter
        public CustReqDTO getCustReq() {
            return custReq;
        }
    }

    /**
     * Nested DTO for CustReq details
     */
    public static class CustReqDTO {

        @JsonProperty("NIN")
        private String nin;

        @JsonProperty("UserId")
        private String userId;

        @JsonProperty("UserType")
        private String userType;

        @JsonProperty("Lang")
        private String lang;

        @JsonProperty("IsAddBuyBal")
        private String isAddBuyBal;

        // Default constructor
        public CustReqDTO() {
        }

        // Constructor with all fields
        public CustReqDTO(String nin, String userId, String userType, String lang, String isAddBuyBal) {
            this.nin = nin;
            this.userId = userId;
            this.userType = userType;
            this.lang = lang;
            this.isAddBuyBal = isAddBuyBal;
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

        public String getLang() {
            return lang;
        }

        public String getIsAddBuyBal() {
            return isAddBuyBal;
        }
    }

    /**
     * Builder pattern for easy construction
     */
    public static class Builder {
        private String srv = "CustomerAssets";
        private String version = "1";
        private String appId = "WEB_ORDERS.25";
        private String lstLogin;
        private String nin;
        private String userId;
        private String userType = "INVESTOR";
        private String lang = "ARB";
        private String isAddBuyBal = "F";
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

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder isAddBuyBal(String isAddBuyBal) {
            this.isAddBuyBal = isAddBuyBal;
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

        public CustomerAssetsRequestDTO build() {
            // If userId not set, use nin as userId
            if (userId == null || userId.isEmpty()) {
                userId = nin;
            }

            CustReqDTO custReq = new CustReqDTO(nin, userId, userType, lang, isAddBuyBal);
            MessageDTO message = new MessageDTO(custReq);
            return new CustomerAssetsRequestDTO(srv, message, version, appId, lstLogin, sessionId);
        }
    }

    @Override
    public String toString() {
        return "CustomerAssetsRequestDTO{" +
                "srv='" + srv + '\'' +
                ", nin='" + (message != null && message.custReq != null ?
                    message.custReq.nin : "null") + '\'' +
                ", userId='" + (message != null && message.custReq != null ?
                    message.custReq.userId : "null") + '\'' +
                ", userType='" + (message != null && message.custReq != null ?
                    message.custReq.userType : "null") + '\'' +
                ", lang='" + (message != null && message.custReq != null ?
                    message.custReq.lang : "null") + '\'' +
                ", version='" + version + '\'' +
                ", sessionId='" + (sessionId != null ? "***" : "null") + '\'' +
                '}';
    }
}
