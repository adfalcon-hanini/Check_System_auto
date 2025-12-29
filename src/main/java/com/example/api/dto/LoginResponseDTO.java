package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for Login API
 * Maps the JSON response structure from login endpoint
 * Flexible to handle various response formats and extract sessionID
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDTO {

    @JsonProperty("sessionID")
    private String sessionID;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("session_id")
    private String sessionId2;

    @JsonProperty("token")
    private String token;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("data")
    private Object data;

    // Default constructor
    public LoginResponseDTO() {
    }

    // Getters and Setters
    public String getSessionID() {
        // Try multiple possible field names for sessionID
        if (sessionID != null && !sessionID.isEmpty()) {
            return sessionID;
        }
        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }
        if (sessionId2 != null && !sessionId2.isEmpty()) {
            return sessionId2;
        }
        if (token != null && !token.isEmpty()) {
            return token;
        }
        // Try to extract from nested Message
        if (message != null) {
            return message.extractSessionID();
        }
        return null;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Nested DTO for Message object in response
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageDTO {

        @JsonProperty("sessionID")
        private String sessionID;

        @JsonProperty("sessionId")
        private String sessionId;

        @JsonProperty("session_id")
        private String sessionId2;

        @JsonProperty("token")
        private String token;

        @JsonProperty("Login")
        private Object login;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Method to extract sessionID from various possible locations
        public String extractSessionID() {
            if (sessionID != null && !sessionID.isEmpty()) {
                return sessionID;
            }
            if (sessionId != null && !sessionId.isEmpty()) {
                return sessionId;
            }
            if (sessionId2 != null && !sessionId2.isEmpty()) {
                return sessionId2;
            }
            if (token != null && !token.isEmpty()) {
                return token;
            }
            return null;
        }

        // Getters and Setters
        public String getSessionID() {
            return sessionID;
        }

        public void setSessionID(String sessionID) {
            this.sessionID = sessionID;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Object getLogin() {
            return login;
        }

        public void setLogin(Object login) {
            this.login = login;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "sessionID='" + getSessionID() + '\'' +
                ", status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                '}';
    }
}
