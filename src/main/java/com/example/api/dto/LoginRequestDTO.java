package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for Login API
 * Maps to the JSON request structure for login endpoint
 */
public class LoginRequestDTO {

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

    // Default constructor
    public LoginRequestDTO() {
    }

    // Constructor with all fields
    public LoginRequestDTO(String srv, MessageDTO message, String version, String appId, String lstLogin) {
        this.srv = srv;
        this.message = message;
        this.version = version;
        this.appId = appId;
        this.lstLogin = lstLogin;
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

    /**
     * Nested DTO for Message object
     */
    public static class MessageDTO {

        @JsonProperty("Login")
        private LoginDetailsDTO login;

        // Default constructor
        public MessageDTO() {
        }

        // Constructor
        public MessageDTO(LoginDetailsDTO login) {
            this.login = login;
        }

        // Getter
        public LoginDetailsDTO getLogin() {
            return login;
        }
    }

    /**
     * Nested DTO for Login details
     */
    public static class LoginDetailsDTO {

        @JsonProperty("UserName")
        private String userName;

        @JsonProperty("Password")
        private String password;

        @JsonProperty("Lang")
        private String lang;

        // Default constructor
        public LoginDetailsDTO() {
        }

        // Constructor
        public LoginDetailsDTO(String userName, String password, String lang) {
            this.userName = userName;
            this.password = password;
            this.lang = lang;
        }

        // Getters
        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public String getLang() {
            return lang;
        }
    }

    /**
     * Builder pattern for easy construction
     */
    public static class Builder {
        private String srv = "Login";
        private String version = "1.0";
        private String appId = "WEB_ORDERS.25";
        private String lstLogin;
        private String userName;
        private String password;
        private String lang = "ARB";

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
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

        public Builder lstLogin(String lstLogin) {
            this.lstLogin = lstLogin;
            return this;
        }

        public LoginRequestDTO build() {
            LoginDetailsDTO loginDetails = new LoginDetailsDTO(userName, password, lang);
            MessageDTO message = new MessageDTO(loginDetails);
            return new LoginRequestDTO(srv, message, version, appId, lstLogin);
        }
    }
}
