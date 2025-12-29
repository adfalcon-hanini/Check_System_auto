package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for PREFERRED_COMPANIES API
 * Maps the JSON response structure from preferred companies endpoint
 * Flexible to handle various response formats
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreferredCompaniesResponseDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Companies")
    private List<CompanyDTO> companies;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    // Default constructor
    public PreferredCompaniesResponseDTO() {
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

    public List<CompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDTO> companies) {
        this.companies = companies;
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

        @JsonProperty("PreferredCompanies")
        private List<CompanyDTO> preferredCompanies;

        @JsonProperty("Companies")
        private List<CompanyDTO> companies;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Getters and Setters
        public List<CompanyDTO> getPreferredCompanies() {
            return preferredCompanies;
        }

        public void setPreferredCompanies(List<CompanyDTO> preferredCompanies) {
            this.preferredCompanies = preferredCompanies;
        }

        public List<CompanyDTO> getCompanies() {
            return companies;
        }

        public void setCompanies(List<CompanyDTO> companies) {
            this.companies = companies;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    /**
     * DTO for individual Company information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompanyDTO {

        @JsonProperty("CompanyCode")
        private String companyCode;

        @JsonProperty("CompanyName")
        private String companyName;

        @JsonProperty("CompanyNameAr")
        private String companyNameAr;

        @JsonProperty("IsPreferred")
        private Boolean isPreferred;

        @JsonProperty("Sector")
        private String sector;

        @JsonProperty("Market")
        private String market;

        // Default constructor
        public CompanyDTO() {
        }

        // Getters and Setters
        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyNameAr() {
            return companyNameAr;
        }

        public void setCompanyNameAr(String companyNameAr) {
            this.companyNameAr = companyNameAr;
        }

        public Boolean getIsPreferred() {
            return isPreferred;
        }

        public void setIsPreferred(Boolean isPreferred) {
            this.isPreferred = isPreferred;
        }

        public String getSector() {
            return sector;
        }

        public void setSector(String sector) {
            this.sector = sector;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        @Override
        public String toString() {
            return "CompanyDTO{" +
                    "companyCode='" + companyCode + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", isPreferred=" + isPreferred +
                    '}';
        }
    }

    /**
     * Extract companies list from various possible locations in response
     * @return List of companies or null if not found
     */
    public List<CompanyDTO> extractCompanies() {
        // Try root level companies first
        if (companies != null && !companies.isEmpty()) {
            return companies;
        }

        // Try message.preferredCompanies
        if (message != null && message.preferredCompanies != null && !message.preferredCompanies.isEmpty()) {
            return message.preferredCompanies;
        }

        // Try message.companies
        if (message != null && message.companies != null && !message.companies.isEmpty()) {
            return message.companies;
        }

        return null;
    }

    @Override
    public String toString() {
        int companiesCount = extractCompanies() != null ? extractCompanies().size() : 0;
        return "PreferredCompaniesResponseDTO{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", companiesCount=" + companiesCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
