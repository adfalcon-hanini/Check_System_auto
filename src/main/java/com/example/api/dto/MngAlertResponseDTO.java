package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for MngAlert API (MNGALERT)
 * Maps the JSON response structure from manage alerts endpoint
 * Flexible to handle various response formats
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MngAlertResponseDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Alerts")
    private List<AlertDTO> alerts;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    // Default constructor
    public MngAlertResponseDTO() {
    }

    // Getters and Setters
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

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    public List<AlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Nested DTO for Message object in response
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MessageDTO {

        @JsonProperty("AlertResponse")
        private List<AlertDTO> alertResponse;

        @JsonProperty("Alerts")
        private List<AlertDTO> alerts;

        @JsonProperty("AlertList")
        private List<AlertDTO> alertList;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Getters and Setters
        public List<AlertDTO> getAlertResponse() {
            return alertResponse;
        }

        public void setAlertResponse(List<AlertDTO> alertResponse) {
            this.alertResponse = alertResponse;
        }

        public List<AlertDTO> getAlerts() {
            return alerts;
        }

        public void setAlerts(List<AlertDTO> alerts) {
            this.alerts = alerts;
        }

        public List<AlertDTO> getAlertList() {
            return alertList;
        }

        public void setAlertList(List<AlertDTO> alertList) {
            this.alertList = alertList;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    /**
     * DTO for individual Alert information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AlertDTO {

        @JsonProperty("AlertId")
        private String alertId;

        @JsonProperty("AlertType")
        private String alertType;

        @JsonProperty("AlertTitle")
        private String alertTitle;

        @JsonProperty("AlertMessage")
        private String alertMessage;

        @JsonProperty("AlertDate")
        private String alertDate;

        @JsonProperty("AlertTime")
        private String alertTime;

        @JsonProperty("AlertStatus")
        private String alertStatus;

        @JsonProperty("AlertPriority")
        private String alertPriority;

        @JsonProperty("AlertSource")
        private String alertSource;

        @JsonProperty("NIN")
        private String nin;

        @JsonProperty("IsRead")
        private Boolean isRead;

        @JsonProperty("ReadDate")
        private String readDate;

        @JsonProperty("Category")
        private String category;

        @JsonProperty("Subject")
        private String subject;

        // Default constructor
        public AlertDTO() {
        }

        // Getters and Setters
        public String getAlertId() {
            return alertId;
        }

        public void setAlertId(String alertId) {
            this.alertId = alertId;
        }

        public String getAlertType() {
            return alertType;
        }

        public void setAlertType(String alertType) {
            this.alertType = alertType;
        }

        public String getAlertTitle() {
            return alertTitle;
        }

        public void setAlertTitle(String alertTitle) {
            this.alertTitle = alertTitle;
        }

        public String getAlertMessage() {
            return alertMessage;
        }

        public void setAlertMessage(String alertMessage) {
            this.alertMessage = alertMessage;
        }

        public String getAlertDate() {
            return alertDate;
        }

        public void setAlertDate(String alertDate) {
            this.alertDate = alertDate;
        }

        public String getAlertTime() {
            return alertTime;
        }

        public void setAlertTime(String alertTime) {
            this.alertTime = alertTime;
        }

        public String getAlertStatus() {
            return alertStatus;
        }

        public void setAlertStatus(String alertStatus) {
            this.alertStatus = alertStatus;
        }

        public String getAlertPriority() {
            return alertPriority;
        }

        public void setAlertPriority(String alertPriority) {
            this.alertPriority = alertPriority;
        }

        public String getAlertSource() {
            return alertSource;
        }

        public void setAlertSource(String alertSource) {
            this.alertSource = alertSource;
        }

        public String getNin() {
            return nin;
        }

        public void setNin(String nin) {
            this.nin = nin;
        }

        public Boolean getIsRead() {
            return isRead;
        }

        public void setIsRead(Boolean isRead) {
            this.isRead = isRead;
        }

        public String getReadDate() {
            return readDate;
        }

        public void setReadDate(String readDate) {
            this.readDate = readDate;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        @Override
        public String toString() {
            return "AlertDTO{" +
                    "alertId='" + alertId + '\'' +
                    ", alertType='" + alertType + '\'' +
                    ", alertTitle='" + alertTitle + '\'' +
                    ", alertDate='" + alertDate + '\'' +
                    ", alertStatus='" + alertStatus + '\'' +
                    ", alertPriority='" + alertPriority + '\'' +
                    ", isRead=" + isRead +
                    '}';
        }
    }

    /**
     * Extract alerts list from various possible locations in response
     * @return List of alerts or null if not found
     */
    public List<AlertDTO> extractAlerts() {
        // Try root level alerts first
        if (alerts != null && !alerts.isEmpty()) {
            return alerts;
        }

        // Try message.alertResponse
        if (message != null && message.alertResponse != null && !message.alertResponse.isEmpty()) {
            return message.alertResponse;
        }

        // Try message.alerts
        if (message != null && message.alerts != null && !message.alerts.isEmpty()) {
            return message.alerts;
        }

        // Try message.alertList
        if (message != null && message.alertList != null && !message.alertList.isEmpty()) {
            return message.alertList;
        }

        return null;
    }

    @Override
    public String toString() {
        int alertsCount = extractAlerts() != null ? extractAlerts().size() : 0;
        return "MngAlertResponseDTO{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", alertsCount=" + alertsCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
