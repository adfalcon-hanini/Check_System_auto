package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for MarginContactAllowance API (MARGIN_CONTACT_ALLOWANCE)
 * Maps the JSON response structure from margin contract allowance endpoint
 * Flexible to handle various response formats
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarginContactAllowanceResponseDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("MarginData")
    private List<MarginAllowanceDTO> marginData;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    // Default constructor
    public MarginContactAllowanceResponseDTO() {
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

    public List<MarginAllowanceDTO> getMarginData() {
        return marginData;
    }

    public void setMarginData(List<MarginAllowanceDTO> marginData) {
        this.marginData = marginData;
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

        @JsonProperty("MarginContractAllowance")
        private List<MarginAllowanceDTO> marginContractAllowance;

        @JsonProperty("MarginAllowance")
        private List<MarginAllowanceDTO> marginAllowance;

        @JsonProperty("Allowances")
        private List<MarginAllowanceDTO> allowances;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Getters and Setters
        public List<MarginAllowanceDTO> getMarginContractAllowance() {
            return marginContractAllowance;
        }

        public void setMarginContractAllowance(List<MarginAllowanceDTO> marginContractAllowance) {
            this.marginContractAllowance = marginContractAllowance;
        }

        public List<MarginAllowanceDTO> getMarginAllowance() {
            return marginAllowance;
        }

        public void setMarginAllowance(List<MarginAllowanceDTO> marginAllowance) {
            this.marginAllowance = marginAllowance;
        }

        public List<MarginAllowanceDTO> getAllowances() {
            return allowances;
        }

        public void setAllowances(List<MarginAllowanceDTO> allowances) {
            this.allowances = allowances;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    /**
     * DTO for individual Margin Allowance information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MarginAllowanceDTO {

        @JsonProperty("NIN")
        private String nin;

        @JsonProperty("AllowanceType")
        private String allowanceType;

        @JsonProperty("AllowanceAmount")
        private Double allowanceAmount;

        @JsonProperty("AvailableAmount")
        private Double availableAmount;

        @JsonProperty("UsedAmount")
        private Double usedAmount;

        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("ExpiryDate")
        private String expiryDate;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("ContractNumber")
        private String contractNumber;

        @JsonProperty("InterestRate")
        private Double interestRate;

        @JsonProperty("Limit")
        private Double limit;

        @JsonProperty("RemainingLimit")
        private Double remainingLimit;

        // Default constructor
        public MarginAllowanceDTO() {
        }

        // Getters and Setters
        public String getNin() {
            return nin;
        }

        public void setNin(String nin) {
            this.nin = nin;
        }

        public String getAllowanceType() {
            return allowanceType;
        }

        public void setAllowanceType(String allowanceType) {
            this.allowanceType = allowanceType;
        }

        public Double getAllowanceAmount() {
            return allowanceAmount;
        }

        public void setAllowanceAmount(Double allowanceAmount) {
            this.allowanceAmount = allowanceAmount;
        }

        public Double getAvailableAmount() {
            return availableAmount;
        }

        public void setAvailableAmount(Double availableAmount) {
            this.availableAmount = availableAmount;
        }

        public Double getUsedAmount() {
            return usedAmount;
        }

        public void setUsedAmount(Double usedAmount) {
            this.usedAmount = usedAmount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContractNumber() {
            return contractNumber;
        }

        public void setContractNumber(String contractNumber) {
            this.contractNumber = contractNumber;
        }

        public Double getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(Double interestRate) {
            this.interestRate = interestRate;
        }

        public Double getLimit() {
            return limit;
        }

        public void setLimit(Double limit) {
            this.limit = limit;
        }

        public Double getRemainingLimit() {
            return remainingLimit;
        }

        public void setRemainingLimit(Double remainingLimit) {
            this.remainingLimit = remainingLimit;
        }

        @Override
        public String toString() {
            return "MarginAllowanceDTO{" +
                    "nin='" + nin + '\'' +
                    ", allowanceType='" + allowanceType + '\'' +
                    ", allowanceAmount=" + allowanceAmount +
                    ", availableAmount=" + availableAmount +
                    ", usedAmount=" + usedAmount +
                    ", currency='" + currency + '\'' +
                    ", status='" + status + '\'' +
                    ", contractNumber='" + contractNumber + '\'' +
                    '}';
        }
    }

    /**
     * Extract margin allowance list from various possible locations in response
     * @return List of margin allowances or null if not found
     */
    public List<MarginAllowanceDTO> extractMarginAllowances() {
        // Try root level marginData first
        if (marginData != null && !marginData.isEmpty()) {
            return marginData;
        }

        // Try message.marginContractAllowance
        if (message != null && message.marginContractAllowance != null && !message.marginContractAllowance.isEmpty()) {
            return message.marginContractAllowance;
        }

        // Try message.marginAllowance
        if (message != null && message.marginAllowance != null && !message.marginAllowance.isEmpty()) {
            return message.marginAllowance;
        }

        // Try message.allowances
        if (message != null && message.allowances != null && !message.allowances.isEmpty()) {
            return message.allowances;
        }

        return null;
    }

    @Override
    public String toString() {
        int allowancesCount = extractMarginAllowances() != null ? extractMarginAllowances().size() : 0;
        return "MarginContactAllowanceResponseDTO{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", allowancesCount=" + allowancesCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
