package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for BankBal API (BNKBAL)
 * Maps the JSON response structure from bank balance endpoint
 * Flexible to handle various response formats
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankBalResponseDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Balances")
    private List<BalanceDTO> balances;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    // Default constructor
    public BankBalResponseDTO() {
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

    public List<BalanceDTO> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceDTO> balances) {
        this.balances = balances;
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

        @JsonProperty("BankBalances")
        private List<BalanceDTO> bankBalances;

        @JsonProperty("Balances")
        private List<BalanceDTO> balances;

        @JsonProperty("BalDlsResponse")
        private List<BalanceDTO> balDlsResponse;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Getters and Setters
        public List<BalanceDTO> getBankBalances() {
            return bankBalances;
        }

        public void setBankBalances(List<BalanceDTO> bankBalances) {
            this.bankBalances = bankBalances;
        }

        public List<BalanceDTO> getBalances() {
            return balances;
        }

        public void setBalances(List<BalanceDTO> balances) {
            this.balances = balances;
        }

        public List<BalanceDTO> getBalDlsResponse() {
            return balDlsResponse;
        }

        public void setBalDlsResponse(List<BalanceDTO> balDlsResponse) {
            this.balDlsResponse = balDlsResponse;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    /**
     * DTO for individual Bank Balance information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BalanceDTO {

        @JsonProperty("AccountNumber")
        private String accountNumber;

        @JsonProperty("AccountType")
        private String accountType;

        @JsonProperty("Currency")
        private String currency;

        @JsonProperty("AvailableBalance")
        private Double availableBalance;

        @JsonProperty("CurrentBalance")
        private Double currentBalance;

        @JsonProperty("BlockedBalance")
        private Double blockedBalance;

        @JsonProperty("TotalBalance")
        private Double totalBalance;

        @JsonProperty("BankName")
        private String bankName;

        @JsonProperty("BankCode")
        private String bankCode;

        @JsonProperty("IBAN")
        private String iban;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("RegisterType")
        private String registerType;

        // Default constructor
        public BalanceDTO() {
        }

        // Getters and Setters
        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(Double availableBalance) {
            this.availableBalance = availableBalance;
        }

        public Double getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(Double currentBalance) {
            this.currentBalance = currentBalance;
        }

        public Double getBlockedBalance() {
            return blockedBalance;
        }

        public void setBlockedBalance(Double blockedBalance) {
            this.blockedBalance = blockedBalance;
        }

        public Double getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(Double totalBalance) {
            this.totalBalance = totalBalance;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getIban() {
            return iban;
        }

        public void setIban(String iban) {
            this.iban = iban;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRegisterType() {
            return registerType;
        }

        public void setRegisterType(String registerType) {
            this.registerType = registerType;
        }

        @Override
        public String toString() {
            return "BalanceDTO{" +
                    "accountNumber='" + accountNumber + '\'' +
                    ", accountType='" + accountType + '\'' +
                    ", currency='" + currency + '\'' +
                    ", availableBalance=" + availableBalance +
                    ", currentBalance=" + currentBalance +
                    ", blockedBalance=" + blockedBalance +
                    ", bankName='" + bankName + '\'' +
                    ", iban='" + iban + '\'' +
                    '}';
        }
    }

    /**
     * Extract balances list from various possible locations in response
     * @return List of balances or null if not found
     */
    public List<BalanceDTO> extractBalances() {
        // Try root level balances first
        if (balances != null && !balances.isEmpty()) {
            return balances;
        }

        // Try message.bankBalances
        if (message != null && message.bankBalances != null && !message.bankBalances.isEmpty()) {
            return message.bankBalances;
        }

        // Try message.balances
        if (message != null && message.balances != null && !message.balances.isEmpty()) {
            return message.balances;
        }

        // Try message.balDlsResponse
        if (message != null && message.balDlsResponse != null && !message.balDlsResponse.isEmpty()) {
            return message.balDlsResponse;
        }

        return null;
    }

    @Override
    public String toString() {
        int balancesCount = extractBalances() != null ? extractBalances().size() : 0;
        return "BankBalResponseDTO{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", balancesCount=" + balancesCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
