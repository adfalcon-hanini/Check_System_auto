package com.example.screensData.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Virtual Trade data from sec_virtual_trade table
 */
public class GetVirtualTradeData {

    private static final Logger logger = Logger.getLogger(GetVirtualTradeData.class);
    private OracleDBConnection dbConnection;

    // Virtual Trade data fields - All 18 columns from sec_virtual_trade table
    private String seq;  // NUMBER
    private String tradeDate;  // DATE
    private String nin;  // VARCHAR2
    private String companyCode;  // VARCHAR2
    private String trnxType;  // VARCHAR2
    private String volume;  // NUMBER
    private String price;  // NUMBER
    private String reason;  // VARCHAR2
    private String refNo;  // NUMBER
    private String createdBy;  // VARCHAR2
    private String creationDate;  // DATE
    private String createdByIpAddress;  // VARCHAR2
    private String createdByPcName;  // VARCHAR2
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String userIdIpAddress;  // VARCHAR2
    private String userIdPcName;  // VARCHAR2
    private String isObsolete;  // VARCHAR2

    // Store all rows
    private List<Map<String, Object>> allTradeData;
    private Map<String, String> firstRecordData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetVirtualTradeData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allTradeData = new ArrayList<>();
        this.firstRecordData = new HashMap<>();
    }

    /**
     * Fetch virtual trade data by NIN
     * @param nin National Identification Number
     * @return true if data found, false otherwise
     */
    public boolean fetchTradeData(String nin) {
        try {
            logger.info("Fetching virtual trade data for NIN: " + nin);

            String query = "SELECT * FROM sec_virtual_trade WHERE NIN = '" + nin + "'";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradeData = results;
                parseData(results.get(0));
                parseFirstRecord(results.get(0));
                logger.info("Virtual trade data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No virtual trade data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching virtual trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch virtual trade data for multiple NINs
     * @param nins List of National Identification Numbers
     * @return true if data found, false otherwise
     */
    public boolean fetchTradeDataForMultipleNins(List<String> nins) {
        try {
            logger.info("Fetching virtual trade data for multiple NINs");

            StringBuilder ninClause = new StringBuilder();
            for (int i = 0; i < nins.size(); i++) {
                ninClause.append("'").append(nins.get(i)).append("'");
                if (i < nins.size() - 1) {
                    ninClause.append(", ");
                }
            }

            String query = "SELECT * FROM sec_virtual_trade WHERE NIN IN (" + ninClause.toString() + ")";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allTradeData = results;
                parseData(results.get(0));
                parseFirstRecord(results.get(0));
                logger.info("Virtual trade data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No virtual trade data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching virtual trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse data from database row - All 18 sec_virtual_trade columns
     * @param row Database row containing virtual trade data
     */
    private void parseData(Map<String, Object> row) {
        this.seq = row.get("SEQ") != null ? row.get("SEQ").toString() : "";
        this.tradeDate = row.get("TRADE_DATE") != null ? row.get("TRADE_DATE").toString() : "";
        this.nin = row.get("NIN") != null ? row.get("NIN").toString() : "";
        this.companyCode = row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "";
        this.trnxType = row.get("TRNX_TYPE") != null ? row.get("TRNX_TYPE").toString() : "";
        this.volume = row.get("VOLUME") != null ? row.get("VOLUME").toString() : "";
        this.price = row.get("PRICE") != null ? row.get("PRICE").toString() : "";
        this.reason = row.get("REASON") != null ? row.get("REASON").toString() : "";
        this.refNo = row.get("REF_NO") != null ? row.get("REF_NO").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.createdByIpAddress = row.get("CREATED_BY_IP_ADDRESS") != null ? row.get("CREATED_BY_IP_ADDRESS").toString() : "";
        this.createdByPcName = row.get("CREATED_BY_PC_NAME") != null ? row.get("CREATED_BY_PC_NAME").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.userIdIpAddress = row.get("USER_ID_IP_ADDRESS") != null ? row.get("USER_ID_IP_ADDRESS").toString() : "";
        this.userIdPcName = row.get("USER_ID_PC_NAME") != null ? row.get("USER_ID_PC_NAME").toString() : "";
        this.isObsolete = row.get("IS_OBSOLETE") != null ? row.get("IS_OBSOLETE").toString() : "";
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
     * Get all trade records
     */
    public List<Map<String, Object>> getAllRecords() {
        return allTradeData;
    }

    /**
     * Get record count
     */
    public int getRecordCount() {
        return allTradeData.size();
    }

    /**
     * Print all virtual trade data
     */
    public void printAllData() {
        System.out.println("\n========== Virtual Trade Data (" + allTradeData.size() + " records) ==========");
        for (int i = 0; i < allTradeData.size(); i++) {
            System.out.println("\n--- Record " + (i + 1) + " ---");
            Map<String, Object> record = allTradeData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("=============================================================");
    }

    /**
     * Print first record
     */
    public void printFirstRecord() {
        if (!allTradeData.isEmpty()) {
            System.out.println("\n========== Virtual Trade Data (First Record) ==========");
            for (Map.Entry<String, String> entry : firstRecordData.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("========================================================");
        }
    }

    // Getters for all 18 sec_virtual_trade columns
    public String getSeq() { return seq; }
    public String getTradeDate() { return tradeDate; }
    public String getNin() { return nin; }
    public String getCompanyCode() { return companyCode; }
    public String getTrnxType() { return trnxType; }
    public String getVolume() { return volume; }
    public String getPrice() { return price; }
    public String getReason() { return reason; }
    public String getRefNo() { return refNo; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getCreatedByIpAddress() { return createdByIpAddress; }
    public String getCreatedByPcName() { return createdByPcName; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getUserIdIpAddress() { return userIdIpAddress; }
    public String getUserIdPcName() { return userIdPcName; }
    public String getIsObsolete() { return isObsolete; }

}
