package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response DTO for CustomerAssets API
 * Maps the JSON response structure from customer assets endpoint
 * Flexible to handle various response formats
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAssetsResponseDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Message")
    private MessageDTO message;

    @JsonProperty("Assets")
    private List<AssetDTO> assets;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    // Default constructor
    public CustomerAssetsResponseDTO() {
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

    public List<AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDTO> assets) {
        this.assets = assets;
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

        @JsonProperty("CustomerAssets")
        private List<AssetDTO> customerAssets;

        @JsonProperty("Assets")
        private List<AssetDTO> assets;

        @JsonProperty("Portfolio")
        private List<AssetDTO> portfolio;

        @JsonProperty("Result")
        private Object result;

        // Default constructor
        public MessageDTO() {
        }

        // Getters and Setters
        public List<AssetDTO> getCustomerAssets() {
            return customerAssets;
        }

        public void setCustomerAssets(List<AssetDTO> customerAssets) {
            this.customerAssets = customerAssets;
        }

        public List<AssetDTO> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetDTO> assets) {
            this.assets = assets;
        }

        public List<AssetDTO> getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(List<AssetDTO> portfolio) {
            this.portfolio = portfolio;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }

    /**
     * DTO for individual Asset information
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetDTO {

        @JsonProperty("CompanyCode")
        private String companyCode;

        @JsonProperty("CompanyName")
        private String companyName;

        @JsonProperty("CompanyNameAr")
        private String companyNameAr;

        @JsonProperty("Quantity")
        private Integer quantity;

        @JsonProperty("AvailableQuantity")
        private Integer availableQuantity;

        @JsonProperty("BlockedQuantity")
        private Integer blockedQuantity;

        @JsonProperty("AveragePrice")
        private Double averagePrice;

        @JsonProperty("CurrentPrice")
        private Double currentPrice;

        @JsonProperty("MarketValue")
        private Double marketValue;

        @JsonProperty("TotalCost")
        private Double totalCost;

        @JsonProperty("ProfitLoss")
        private Double profitLoss;

        @JsonProperty("ProfitLossPercentage")
        private Double profitLossPercentage;

        @JsonProperty("Sector")
        private String sector;

        @JsonProperty("Market")
        private String market;

        // Default constructor
        public AssetDTO() {
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

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getAvailableQuantity() {
            return availableQuantity;
        }

        public void setAvailableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public Integer getBlockedQuantity() {
            return blockedQuantity;
        }

        public void setBlockedQuantity(Integer blockedQuantity) {
            this.blockedQuantity = blockedQuantity;
        }

        public Double getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(Double averagePrice) {
            this.averagePrice = averagePrice;
        }

        public Double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(Double currentPrice) {
            this.currentPrice = currentPrice;
        }

        public Double getMarketValue() {
            return marketValue;
        }

        public void setMarketValue(Double marketValue) {
            this.marketValue = marketValue;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

        public Double getProfitLoss() {
            return profitLoss;
        }

        public void setProfitLoss(Double profitLoss) {
            this.profitLoss = profitLoss;
        }

        public Double getProfitLossPercentage() {
            return profitLossPercentage;
        }

        public void setProfitLossPercentage(Double profitLossPercentage) {
            this.profitLossPercentage = profitLossPercentage;
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
            return "AssetDTO{" +
                    "companyCode='" + companyCode + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", quantity=" + quantity +
                    ", availableQty=" + availableQuantity +
                    ", avgPrice=" + averagePrice +
                    ", currentPrice=" + currentPrice +
                    ", profitLoss=" + profitLoss +
                    '}';
        }
    }

    /**
     * Extract assets list from various possible locations in response
     * @return List of assets or null if not found
     */
    public List<AssetDTO> extractAssets() {
        // Try root level assets first
        if (assets != null && !assets.isEmpty()) {
            return assets;
        }

        // Try message.customerAssets
        if (message != null && message.customerAssets != null && !message.customerAssets.isEmpty()) {
            return message.customerAssets;
        }

        // Try message.assets
        if (message != null && message.assets != null && !message.assets.isEmpty()) {
            return message.assets;
        }

        // Try message.portfolio
        if (message != null && message.portfolio != null && !message.portfolio.isEmpty()) {
            return message.portfolio;
        }

        return null;
    }

    @Override
    public String toString() {
        int assetsCount = extractAssets() != null ? extractAssets().size() : 0;
        return "CustomerAssetsResponseDTO{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", assetsCount=" + assetsCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
