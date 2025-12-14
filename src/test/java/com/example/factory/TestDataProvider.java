package com.example.factory;

/**
 * Data holder class for parameterized tests
 * Used by Factory classes to pass test parameters
 */
public class TestDataProvider {

    private String testName;
    private String clientId;
    private String nin;
    private String date;
    private String startDate;
    private String endDate;
    private String companyCode;
    private String expectedResult;

    // Constructor with all parameters
    public TestDataProvider(String testName, String clientId, String nin, String date,
                           String startDate, String endDate, String companyCode, String expectedResult) {
        this.testName = testName;
        this.clientId = clientId;
        this.nin = nin;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companyCode = companyCode;
        this.expectedResult = expectedResult;
    }

    // Constructor for Client ID testing
    public TestDataProvider(String testName, String clientId) {
        this(testName, clientId, null, null, null, null, null, "success");
    }

    // Constructor for NIN testing
    public TestDataProvider(String testName, String nin, String date) {
        this(testName, null, nin, date, null, null, null, "success");
    }

    // Constructor for NIN and date range testing
    public TestDataProvider(String testName, String nin, String startDate, String endDate) {
        this(testName, null, nin, null, startDate, endDate, null, "success");
    }

    // Getters
    public String getTestName() {
        return testName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getNin() {
        return nin;
    }

    public String getDate() {
        return date;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    // Builder pattern for flexible object creation
    public static class Builder {
        private String testName;
        private String clientId;
        private String nin;
        private String date;
        private String startDate;
        private String endDate;
        private String companyCode;
        private String expectedResult = "success";

        public Builder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder nin(String nin) {
            this.nin = nin;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder companyCode(String companyCode) {
            this.companyCode = companyCode;
            return this;
        }

        public Builder expectedResult(String expectedResult) {
            this.expectedResult = expectedResult;
            return this;
        }

        public TestDataProvider build() {
            return new TestDataProvider(testName, clientId, nin, date,
                                       startDate, endDate, companyCode, expectedResult);
        }
    }

    @Override
    public String toString() {
        return "TestDataProvider{" +
                "testName='" + testName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", nin='" + nin + '\'' +
                ", date='" + date + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                '}';
    }
}
