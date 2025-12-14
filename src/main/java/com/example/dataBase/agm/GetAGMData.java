package com.example.dataBase.agm;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store AGM data from Fund_Agm_Dates table
 * Returns: COMPANY_CODE, AGM_DATE, CASH_DISTRIBUTION, SHARE_DISTRIBUTION,
 *          NEXT_WORK_DAY, ADJUSTMENT_FACTOR, APPROVED
 */
public class GetAGMData {

    private static final Logger logger = Logger.getLogger(GetAGMData.class);
    private OracleDBConnection dbConnection;

    // AGM data fields
    private String companyCode;
    private String agmDate;
    private String cashDistribution;
    private String shareDistribution;
    private String nextWorkDay;
    private String adjustmentFactor;
    private String approved;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allAGMData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetAGMData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allAGMData = new ArrayList<>();
    }

    /**
     * Fetch AGM data by Company Code
     * @param companyCode Company Code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchAGMDataByCompanyCode(String companyCode) {
        try {
            logger.info("Fetching AGM data for Company Code: " + companyCode);

            String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                          "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                          "FROM Fund_Agm_Dates AGM " +
                          "WHERE AGM.COMPANY_CODE = ? " +
                          "ORDER BY AGM.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode);

            if (!results.isEmpty()) {
                allAGMData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseAGMData(row);
                logger.info("AGM data fetched successfully. Found " + results.size() + " record(s)");
                printAllAGMData();
                return true;
            } else {
                logger.warn("No AGM data found for Company Code: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching AGM data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all AGM data (all company codes)
     * @return true if data found, false otherwise
     */
    public boolean fetchAllAGMData() {
        try {
            logger.info("Fetching all AGM data");

            String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                          "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                          "FROM Fund_Agm_Dates AGM " +
                          "ORDER BY AGM.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAGMData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseAGMData(row);
                logger.info("AGM data fetched successfully. Found " + results.size() + " record(s)");
                printAllAGMData();
                return true;
            } else {
                logger.warn("No AGM data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching AGM data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch AGM data by Next Work Day
     * @param nextWorkDay Next work day to query (format: dd-mm-yyyy, e.g., "27-11-2025")
     * @return true if data found, false otherwise
     */
    public boolean fetchAGMDataByNextWorkDay(String nextWorkDay) {
        try {
            logger.info("Fetching AGM data for Next Work Day: " + nextWorkDay);

            String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                          "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                          "FROM Fund_Agm_Dates AGM " +
                          "WHERE TRUNC(AGM.NEXT_WORK_DAY) = TO_DATE(?, 'DD-MM-YYYY') " +
                          "ORDER BY AGM.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nextWorkDay);

            if (!results.isEmpty()) {
                allAGMData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseAGMData(row);
                logger.info("AGM data fetched successfully. Found " + results.size() + " record(s)");
                printAllAGMData();
                return true;
            } else {
                logger.warn("No AGM data found for Next Work Day: " + nextWorkDay);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching AGM data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch AGM data with cash distribution (non-zero cash distribution)
     * @return true if data found, false otherwise
     */
    public boolean fetchAGMDataWithCashDistribution() {
        try {
            logger.info("Fetching AGM data with cash distribution");

            String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                          "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                          "FROM Fund_Agm_Dates AGM " +
                          "WHERE AGM.CASH_DISTRIBUTION IS NOT NULL AND AGM.CASH_DISTRIBUTION > 0 " +
                          "ORDER BY AGM.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAGMData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseAGMData(row);
                logger.info("AGM data with cash distribution fetched successfully. Found " + results.size() + " record(s)");
                printAllAGMData();
                return true;
            } else {
                logger.warn("No AGM data with cash distribution found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching AGM data with cash distribution: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch AGM data with shares distribution (non-zero shares distribution)
     * @return true if data found, false otherwise
     */
    public boolean fetchAGMDataWithSharesDistribution() {
        try {
            logger.info("Fetching AGM data with shares distribution");

            String query = "SELECT AGM.COMPANY_CODE, AGM.AGM_DATE, AGM.CASH_DISTRIBUTION, " +
                          "AGM.SHARE_DISTRIBUTION, AGM.NEXT_WORK_DAY, AGM.ADJUSTMENT_FACTOR, AGM.APPROVED " +
                          "FROM Fund_Agm_Dates AGM " +
                          "WHERE AGM.SHARE_DISTRIBUTION IS NOT NULL AND AGM.SHARE_DISTRIBUTION > 0 " +
                          "ORDER BY AGM.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allAGMData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseAGMData(row);
                logger.info("AGM data with shares distribution fetched successfully. Found " + results.size() + " record(s)");
                printAllAGMData();
                return true;
            } else {
                logger.warn("No AGM data with shares distribution found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching AGM data with shares distribution: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse AGM data from database row
     * @param row Database row containing AGM data
     */
    private void parseAGMData(Map<String, Object> row) {
        this.companyCode = row.get("COMPANY_CODE") != null ? row.get("COMPANY_CODE").toString() : "";
        this.agmDate = row.get("AGM_DATE") != null ? row.get("AGM_DATE").toString() : "";
        this.cashDistribution = row.get("CASH_DISTRIBUTION") != null ? row.get("CASH_DISTRIBUTION").toString() : "";
        this.shareDistribution = row.get("SHARE_DISTRIBUTION") != null ? row.get("SHARE_DISTRIBUTION").toString() : "";
        this.nextWorkDay = row.get("NEXT_WORK_DAY") != null ? row.get("NEXT_WORK_DAY").toString() : "";
        this.adjustmentFactor = row.get("ADJUSTMENT_FACTOR") != null ? row.get("ADJUSTMENT_FACTOR").toString() : "";
        this.approved = row.get("APPROVED") != null ? row.get("APPROVED").toString() : "";
    }

    /**
     * Get all AGM data as a map (first row)
     * @return Map containing all AGM fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("companyCode", this.companyCode);
        data.put("agmDate", this.agmDate);
        data.put("cashDistribution", this.cashDistribution);
        data.put("shareDistribution", this.shareDistribution);
        data.put("nextWorkDay", this.nextWorkDay);
        data.put("adjustmentFactor", this.adjustmentFactor);
        data.put("approved", this.approved);
        return data;
    }

    /**
     * Get all AGM records
     * @return List of all AGM data rows
     */
    public List<Map<String, Object>> getAllAGMRecords() {
        return allAGMData;
    }

    /**
     * Get AGM record by index
     * @param index Index of the record
     * @return Map containing AGM data for that index
     */
    public Map<String, Object> getAGMRecordByIndex(int index) {
        if (index >= 0 && index < allAGMData.size()) {
            return allAGMData.get(index);
        }
        return null;
    }

    /**
     * Get total number of AGM records
     * @return Number of records
     */
    public int getRecordCount() {
        return allAGMData.size();
    }

    /**
     * Print AGM data to console (first record)
     */
    public void printAGMData() {
        System.out.println("\n========== AGM Data ==========");
        System.out.println("Company Code: " + companyCode);
        System.out.println("AGM Date: " + agmDate);
        System.out.println("Cash Distribution: " + cashDistribution);
        System.out.println("Share Distribution: " + shareDistribution);
        System.out.println("Next Work Day: " + nextWorkDay);
        System.out.println("Adjustment Factor: " + adjustmentFactor);
        System.out.println("Approved: " + approved);
        System.out.println("==============================");
    }

    /**
     * Print all AGM records
     */
    public void printAllAGMData() {
        System.out.println("\n" + "=".repeat(140));
        System.out.println("AGM DATA - Total Records: " + allAGMData.size());
        System.out.println("=".repeat(140));

        if (allAGMData.isEmpty()) {
            System.out.println("No data found");
        } else {
            // Print header
            System.out.printf("%-15s %-25s %-20s %-20s %-25s %-20s %-10s%n",
                "COMPANY_CODE", "AGM_DATE", "CASH_DIST", "SHARE_DIST", "NEXT_WORK_DAY", "ADJUST_FACTOR", "APPROVED");
            System.out.println("-".repeat(140));

            // Print data rows (limit to first 20 for readability)
            int displayLimit = Math.min(allAGMData.size(), 20);
            for (int i = 0; i < displayLimit; i++) {
                Map<String, Object> record = allAGMData.get(i);
                System.out.printf("%-15s %-25s %-20s %-20s %-25s %-20s %-10s%n",
                    record.get("COMPANY_CODE") != null ? record.get("COMPANY_CODE").toString() : "",
                    record.get("AGM_DATE") != null ? record.get("AGM_DATE").toString() : "",
                    record.get("CASH_DISTRIBUTION") != null ? record.get("CASH_DISTRIBUTION").toString() : "",
                    record.get("SHARE_DISTRIBUTION") != null ? record.get("SHARE_DISTRIBUTION").toString() : "",
                    record.get("NEXT_WORK_DAY") != null ? record.get("NEXT_WORK_DAY").toString() : "",
                    record.get("ADJUSTMENT_FACTOR") != null ? record.get("ADJUSTMENT_FACTOR").toString() : "",
                    record.get("APPROVED") != null ? record.get("APPROVED").toString() : "");
            }

            if (allAGMData.size() > 20) {
                System.out.println("\nShowing first 20 of " + allAGMData.size() + " records");
            }
        }

        System.out.println("=".repeat(140));
    }

    // Getters
    public String getCompanyCode() {
        return companyCode;
    }

    public String getAgmDate() {
        return agmDate;
    }

    public String getCashDistribution() {
        return cashDistribution;
    }

    public String getShareDistribution() {
        return shareDistribution;
    }

    public String getNextWorkDay() {
        return nextWorkDay;
    }

    public String getAdjustmentFactor() {
        return adjustmentFactor;
    }

    public String getApproved() {
        return approved;
    }
}
