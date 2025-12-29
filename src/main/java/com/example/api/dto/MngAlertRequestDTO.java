package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for MngAlert API (MNGALERT)
 * Maps to the JSON request structure for manage alerts endpoint
 *
 * JSON Structure:
 * {
 *   "Srv": "MNGALERT",
 *   "Message": {
 *     "AlertRequest": {
 *       "ReqOpCode": "GETDATA",
 *       "ReqAlertNin": "12240",
 *       "ReqAlertSource": "",
 *       "Lang": "en",
 *       "UserId": "12240",
 *       "UserType": "INVESTOR"
 *     }
 *   },
 *   "Version": 1,
 *   "AppId": "WEB_ORDERS.25",
 *   "LstLogin": "2025-01-07",
 *   "SessionID": "{{SessionID}}"
 * }
 */
public class MngAlertRequestDTO {

    @JsonProperty("Srv")
    private String srv;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Version")
    private Integer version;

    @JsonProperty("AppId")
    private String appId;

    @JsonProperty("LstLogin")
    private String lstLogin;

    @JsonProperty("SessionID")
    private String sessionID;

    // Default constructor
    public MngAlertRequestDTO() {
    }

    // Constructor with all fields
    public MngAlertRequestDTO(String srv, MessageDTO message, Integer version,
                              String appId, String lstLogin, String sessionID) {
        this.srv = srv;
        this.message = message;
        this.version = version;
        this.appId = appId;
        this.lstLogin = lstLogin;
        this.sessionID = sessionID;
    }

    // Getters
    public String getSrv() {
        return srv;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public Integer getVersion() {
        return version;
    }

    public String getAppId() {
        return appId;
    }

    public String getLstLogin() {
        return lstLogin;
    }

    public String getSessionID() {
        return sessionID;
    }

    /**
     * Nested DTO for Message object
     */
    public static class MessageDTO {

        @JsonProperty("AlertRequest")
        private AlertRequestDTO alertRequest;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(AlertRequestDTO alertRequest) {
            this.alertRequest = alertRequest;
        }

        // Getter
        public AlertRequestDTO getAlertRequest() {
            return alertRequest;
        }
    }

    /**
     * Nested DTO for AlertRequest details
     */
    public static class AlertRequestDTO {

        @JsonProperty("ReqOpCode")
        private String reqOpCode;

        @JsonProperty("ReqAlertNin")
        private String reqAlertNin;

        @JsonProperty("ReqAlertSource")
        private String reqAlertSource;

        @JsonProperty("Lang")
        private String lang;

        @JsonProperty("UserId")
        private String userId;

        @JsonProperty("UserType")
        private String userType;

        // Default constructor
        public AlertRequestDTO() {
        }

        // Constructor with all fields
        public AlertRequestDTO(String reqOpCode, String reqAlertNin, String reqAlertSource,
                               String lang, String userId, String userType) {
            this.reqOpCode = reqOpCode;
            this.reqAlertNin = reqAlertNin;
            this.reqAlertSource = reqAlertSource;
            this.lang = lang;
            this.userId = userId;
            this.userType = userType;
        }

        // Getters
        public String getReqOpCode() {
            return reqOpCode;
        }

        public String getReqAlertNin() {
            return reqAlertNin;
        }

        public String getReqAlertSource() {
            return reqAlertSource;
        }

        public String getLang() {
            return lang;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserType() {
            return userType;
        }
    }

    /**
     * Builder pattern for easy construction
     */
    public static class Builder {
        private String srv = "MNGALERT";
        private Integer version = 1;
        private String appId = "WEB_ORDERS.25";
        private String lstLogin;
        private String reqOpCode = "GETDATA";
        private String reqAlertNin;
        private String reqAlertSource = "";
        private String lang = "en";
        private String userId;
        private String userType = "INVESTOR";
        private String sessionID;

        public Builder reqOpCode(String reqOpCode) {
            this.reqOpCode = reqOpCode;
            return this;
        }

        public Builder reqAlertNin(String reqAlertNin) {
            this.reqAlertNin = reqAlertNin;
            return this;
        }

        public Builder reqAlertSource(String reqAlertSource) {
            this.reqAlertSource = reqAlertSource;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
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

        public Builder sessionID(String sessionID) {
            this.sessionID = sessionID;
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

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public MngAlertRequestDTO build() {
            // If userId not set, use reqAlertNin as userId
            if (userId == null || userId.isEmpty()) {
                userId = reqAlertNin;
            }

            AlertRequestDTO alertRequest = new AlertRequestDTO(
                    reqOpCode, reqAlertNin, reqAlertSource, lang, userId, userType);
            MessageDTO message = new MessageDTO(alertRequest);
            return new MngAlertRequestDTO(srv, message, version, appId, lstLogin, sessionID);
        }
    }

    @Override
    public String toString() {
        return "MngAlertRequestDTO{" +
                "srv='" + srv + '\'' +
                ", reqOpCode='" + (message != null && message.alertRequest != null ?
                    message.alertRequest.reqOpCode : "null") + '\'' +
                ", reqAlertNin='" + (message != null && message.alertRequest != null ?
                    message.alertRequest.reqAlertNin : "null") + '\'' +
                ", lang='" + (message != null && message.alertRequest != null ?
                    message.alertRequest.lang : "null") + '\'' +
                ", userId='" + (message != null && message.alertRequest != null ?
                    message.alertRequest.userId : "null") + '\'' +
                ", userType='" + (message != null && message.alertRequest != null ?
                    message.alertRequest.userType : "null") + '\'' +
                ", version=" + version +
                ", sessionID='" + (sessionID != null ? "***" : "null") + '\'' +
                '}';
    }
}
