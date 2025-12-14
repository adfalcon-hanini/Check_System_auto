package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Calculator Study data from MYCALCULATOR_STUDY table
 */
public class GetCalculatorStudyData {

    private static final Logger logger = Logger.getLogger(GetCalculatorStudyData.class);
    private OracleDBConnection dbConnection;

    // Calculator Study data fields - All 24 columns from MYCALCULATOR_STUDY table
    private String nin;  // VARCHAR2
    private String trnxDate;  // DATE
    private String sharesValue;  // NUMBER
    private String cash;  // NUMBER
    private String dividend;  // NUMBER
    private String assets;  // NUMBER
    private String withdraw;  // NUMBER
    private String deposit;  // NUMBER
    private String profitLoss;  // NUMBER
    private String portfolioDate;  // DATE
    private String prevPortfolioDate;  // DATE
    private String registryTransfer;  // NUMBER
    private String stockTransfer;  // NUMBER
    private String createdBy;  // VARCHAR2
    private String creationDate;  // DATE
    private String createdByIpAddress;  // VARCHAR2
    private String createdByPcName;  // VARCHAR2
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String userIdIpAddress;  // VARCHAR2
    private String userIdPcName;  // VARCHAR2
    private String fundAmount;  // NUMBER
    private String feesAmount;  // NUMBER
    private String bonus;  // NUMBER

    // Store all rows
    private List<Map<String, Object>> allStudyData;
    private Map<String, String> firstRecordData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetCalculatorStudyData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allStudyData = new ArrayList<>();
        this.firstRecordData = new HashMap<>();
    }

    /**
     * Fetch calculator study data by NIN
     * @param nin National Identification Number
     * @return true if data found, false otherwise
     */
    public boolean fetchStudyData(String nin) {
        try {
            logger.info("Fetching calculator study data for NIN: " + nin);

            String query = "SELECT * FROM MYCALCULATOR_STUDY WHERE NIN = '" + nin + "'";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allStudyData = results;
                parseData(results.get(0));
                parseFirstRecord(results.get(0));
                logger.info("Calculator study data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No calculator study data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching calculator study data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch calculator study data for multiple NINs
     * @param nins List of National Identification Numbers
     * @return true if data found, false otherwise
     */
    public boolean fetchStudyDataForMultipleNins(List<String> nins) {
        try {
            logger.info("Fetching calculator study data for multiple NINs");

            StringBuilder ninClause = new StringBuilder();
            for (int i = 0; i < nins.size(); i++) {
                ninClause.append("'").append(nins.get(i)).append("'");
                if (i < nins.size() - 1) {
                    ninClause.append(", ");
                }
            }

            String query = "SELECT * FROM MYCALCULATOR_STUDY WHERE NIN IN (" + ninClause.toString() + ")";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allStudyData = results;
                parseData(results.get(0));
                parseFirstRecord(results.get(0));
                logger.info("Calculator study data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No calculator study data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching calculator study data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse data from database row - All 24 MYCALCULATOR_STUDY columns
     * @param row Database row containing calculator study data
     */
    private void parseData(Map<String, Object> row) {
        this.nin = row.get("NIN") != null ? row.get("NIN").toString() : "";
        this.trnxDate = row.get("TRNX_DATE") != null ? row.get("TRNX_DATE").toString() : "";
        this.sharesValue = row.get("SHARES_VALUE") != null ? row.get("SHARES_VALUE").toString() : "";
        this.cash = row.get("CASH") != null ? row.get("CASH").toString() : "";
        this.dividend = row.get("DIVIDEND") != null ? row.get("DIVIDEND").toString() : "";
        this.assets = row.get("ASSETS") != null ? row.get("ASSETS").toString() : "";
        this.withdraw = row.get("WITHDRAW") != null ? row.get("WITHDRAW").toString() : "";
        this.deposit = row.get("DEPOSIT") != null ? row.get("DEPOSIT").toString() : "";
        this.profitLoss = row.get("PROFIT_LOSS") != null ? row.get("PROFIT_LOSS").toString() : "";
        this.portfolioDate = row.get("PORTFOLIO_DATE") != null ? row.get("PORTFOLIO_DATE").toString() : "";
        this.prevPortfolioDate = row.get("PREV_PORTFOLIO_DATE") != null ? row.get("PREV_PORTFOLIO_DATE").toString() : "";
        this.registryTransfer = row.get("REGISTRY_TRANSFER") != null ? row.get("REGISTRY_TRANSFER").toString() : "";
        this.stockTransfer = row.get("STOCK_TRANSFER") != null ? row.get("STOCK_TRANSFER").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.createdByIpAddress = row.get("CREATED_BY_IP_ADDRESS") != null ? row.get("CREATED_BY_IP_ADDRESS").toString() : "";
        this.createdByPcName = row.get("CREATED_BY_PC_NAME") != null ? row.get("CREATED_BY_PC_NAME").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.userIdIpAddress = row.get("USER_ID_IP_ADDRESS") != null ? row.get("USER_ID_IP_ADDRESS").toString() : "";
        this.userIdPcName = row.get("USER_ID_PC_NAME") != null ? row.get("USER_ID_PC_NAME").toString() : "";
        this.fundAmount = row.get("FUND_AMOUNT") != null ? row.get("FUND_AMOUNT").toString() : "";
        this.feesAmount = row.get("FEES_AMOUNT") != null ? row.get("FEES_AMOUNT").toString() : "";
        this.bonus = row.get("BONUS") != null ? row.get("BONUS").toString() : "";
    }

    /**
     * Parse first record data
     */
    private void parseFirstRecord(Map<String, Object> row) {
        firstRecordData.clear();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            firstRecordData.put(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "");
        }
    }

    /**
     * Get value from first record by column name
     */
    public String getValue(String columnName) {
        return firstRecordData.getOrDefault(columnName, "");
    }

    /**
     * Get all study records
     */
    public List<Map<String, Object>> getAllRecords() {
        return allStudyData;
    }

    /**
     * Get record count
     */
    public int getRecordCount() {
        return allStudyData.size();
    }

    /**
     * Print all calculator study data
     */
    public void printAllData() {
        System.out.println("\n========== Calculator Study Data (" + allStudyData.size() + " records) ==========");
        for (int i = 0; i < allStudyData.size(); i++) {
            System.out.println("\n--- Record " + (i + 1) + " ---");
            Map<String, Object> record = allStudyData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("=================================================================");
    }

    /**
     * Print first record
     */
    public void printFirstRecord() {
        if (!allStudyData.isEmpty()) {
            System.out.println("\n========== Calculator Study Data (First Record) ==========");
            for (Map.Entry<String, String> entry : firstRecordData.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("==========================================================");
        }
    }

    // Getters for all 24 MYCALCULATOR_STUDY columns
    public String getNin() { return nin; }
    public String getTrnxDate() { return trnxDate; }
    public String getSharesValue() { return sharesValue; }
    public String getCash() { return cash; }
    public String getDividend() { return dividend; }
    public String getAssets() { return assets; }
    public String getWithdraw() { return withdraw; }
    public String getDeposit() { return deposit; }
    public String getProfitLoss() { return profitLoss; }
    public String getPortfolioDate() { return portfolioDate; }
    public String getPrevPortfolioDate() { return prevPortfolioDate; }
    public String getRegistryTransfer() { return registryTransfer; }
    public String getStockTransfer() { return stockTransfer; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getCreatedByIpAddress() { return createdByIpAddress; }
    public String getCreatedByPcName() { return createdByPcName; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getUserIdIpAddress() { return userIdIpAddress; }
    public String getUserIdPcName() { return userIdPcName; }
    public String getFundAmount() { return fundAmount; }
    public String getFeesAmount() { return feesAmount; }
    public String getBonus() { return bonus; }

}
