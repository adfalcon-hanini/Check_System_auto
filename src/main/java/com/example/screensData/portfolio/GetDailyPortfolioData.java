package com.example.screensData.portfolio;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Daily Portfolio data from SEC_EQU_DAILY_PORTFOLIO table
 */
public class GetDailyPortfolioData {

    private static final Logger logger = Logger.getLogger(GetDailyPortfolioData.class);
    private OracleDBConnection dbConnection;

    // Daily Portfolio data fields - All 18 columns from SEC_EQU_DAILY_PORTFOLIO table
    private String portfolioDate;  // DATE
    private String nin;  // VARCHAR2
    private String cAccount;  // VARCHAR2
    private String companyCode;  // VARCHAR2
    private String sharesCount;  // NUMBER
    private String sharePrice;  // NUMBER
    private String sharesValue;  // NUMBER
    private String creationDate;  // DATE
    private String createdBy;  // VARCHAR2
    private String createdByIpAddress;  // VARCHAR2
    private String createdByPcName;  // VARCHAR2
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String userIdIpAddress;  // VARCHAR2
    private String userIdPcName;  // VARCHAR2
    private String blockedShares;  // NUMBER
    private String masterNin;  // VARCHAR2
    private String avgPrice;  // NUMBER

    // Store all rows
    private List<Map<String, Object>> allDailyPortfolioData;
    private Map<String, String> firstRecordData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetDailyPortfolioData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allDailyPortfolioData = new ArrayList<>();
        this.firstRecordData = new HashMap<>();
    }

    /**
     * Fetch daily portfolio data by NIN
     * @param nin National Identification Number
     * @return true if data found, false otherwise
     */
    public boolean fetchDailyPortfolioData(String nin) {
        try {
            logger.info("Fetching daily portfolio data for NIN: " + nin);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO WHERE NIN = '" + nin + "'";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allDailyPortfolioData = results;
                parseFirstRecord(results.get(0));
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch daily portfolio data for multiple NINs
     * @param nins List of National Identification Numbers
     * @return true if data found, false otherwise
     */
    public boolean fetchDailyPortfolioDataForMultipleNins(List<String> nins) {
        try {
            logger.info("Fetching daily portfolio data for multiple NINs");

            StringBuilder ninClause = new StringBuilder();
            for (int i = 0; i < nins.size(); i++) {
                ninClause.append("'").append(nins.get(i)).append("'");
                if (i < nins.size() - 1) {
                    ninClause.append(", ");
                }
            }

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO WHERE NIN IN (" + ninClause.toString() + ")";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allDailyPortfolioData = results;
                parseFirstRecord(results.get(0));
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse first record data
     */
    private void parseFirstRecord(Map<String, Object> row) {
        firstRecordData.clear();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            firstRecordData.put(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "");
        }
        // Parse into specific fields
        parseData(row);
    }

    /**
     * Parse daily portfolio data from database row - All 18 SEC_EQU_DAILY_PORTFOLIO columns
     * @param row Database row containing daily portfolio data
     */
    private void parseData(Map<String, Object> row) {
        this.portfolioDate = row.get("PORTFOLIO_DATE") != null ? row.get("PORTFOLIO_DATE").toString() : "";
        this.nin = row.get("NIN") != null ? row.get("NIN").toString() : "";
        this.cAccount = row.get("C_ACCOUNT") != null ? row.get("C_ACCOUNT").toString() : "";
        this.companyCode = row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "";
        this.sharesCount = row.get("SHARES_COUNT") != null ? row.get("SHARES_COUNT").toString() : "";
        this.sharePrice = row.get("SHARE_PRICE") != null ? row.get("SHARE_PRICE").toString() : "";
        this.sharesValue = row.get("SHARES_VALUE") != null ? row.get("SHARES_VALUE").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.createdByIpAddress = row.get("CREATED_BY_IP_ADDRESS") != null ? row.get("CREATED_BY_IP_ADDRESS").toString() : "";
        this.createdByPcName = row.get("CREATED_BY_PC_NAME") != null ? row.get("CREATED_BY_PC_NAME").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.userIdIpAddress = row.get("USER_ID_IP_ADDRESS") != null ? row.get("USER_ID_IP_ADDRESS").toString() : "";
        this.userIdPcName = row.get("USER_ID_PC_NAME") != null ? row.get("USER_ID_PC_NAME").toString() : "";
        this.blockedShares = row.get("BLOCKED_SHARES") != null ? row.get("BLOCKED_SHARES").toString() : "";
        this.masterNin = row.get("MASTER_NIN") != null ? row.get("MASTER_NIN").toString() : "";
        this.avgPrice = row.get("AVG_PRICE") != null ? row.get("AVG_PRICE").toString() : "";
    }

    /**
     * Get value from first record by column name
     */
    public String getValue(String columnName) {
        return firstRecordData.getOrDefault(columnName, "");
    }

    /**
     * Get all daily portfolio records
     */
    public List<Map<String, Object>> getAllRecords() {
        return allDailyPortfolioData;
    }

    /**
     * Get record count
     */
    public int getRecordCount() {
        return allDailyPortfolioData.size();
    }

    /**
     * Print all daily portfolio data
     */
    public void printAllData() {
        System.out.println("\n========== Daily Portfolio Data (" + allDailyPortfolioData.size() + " records) ==========");
        for (int i = 0; i < allDailyPortfolioData.size(); i++) {
            System.out.println("\n--- Record " + (i + 1) + " ---");
            Map<String, Object> record = allDailyPortfolioData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("================================================================");
    }

    /**
     * Print first record
     */
    public void printFirstRecord() {
        if (!allDailyPortfolioData.isEmpty()) {
            System.out.println("\n========== Daily Portfolio Data (First Record) ==========");
            for (Map.Entry<String, String> entry : firstRecordData.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("==========================================================");
        }
    }

    // Getters for all 18 SEC_EQU_DAILY_PORTFOLIO columns
    public String getPortfolioDate() { return portfolioDate; }
    public String getNin() { return nin; }
    public String getCAccount() { return cAccount; }
    public String getCompanyCode() { return companyCode; }
    public String getSharesCount() { return sharesCount; }
    public String getSharePrice() { return sharePrice; }
    public String getSharesValue() { return sharesValue; }
    public String getCreationDate() { return creationDate; }
    public String getCreatedBy() { return createdBy; }
    public String getCreatedByIpAddress() { return createdByIpAddress; }
    public String getCreatedByPcName() { return createdByPcName; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getUserIdIpAddress() { return userIdIpAddress; }
    public String getUserIdPcName() { return userIdPcName; }
    public String getBlockedShares() { return blockedShares; }
    public String getMasterNin() { return masterNin; }
    public String getAvgPrice() { return avgPrice; }

}
