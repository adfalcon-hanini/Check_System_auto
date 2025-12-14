package com.example.screensData.agm;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Virtual Trade data from sec_virtual_trade table
 * Returns all columns from the table ordered by TIME_STAMP DESC
 */
public class GetVirtualTradeData {

    private static final Logger logger = Logger.getLogger(GetVirtualTradeData.class);
    private OracleDBConnection dbConnection;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allVirtualTradeData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetVirtualTradeData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allVirtualTradeData = new ArrayList<>();
    }

    /**
     * Fetch all Virtual Trade data
     * @return true if data found, false otherwise
     */
    public boolean fetchAllVirtualTradeData() {
        try {
            logger.info("Fetching all Virtual Trade data");

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch Virtual Trade data by NIN
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchVirtualTradeDataByNin(String nin) {
        try {
            logger.info("Fetching Virtual Trade data for NIN: " + nin);

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "WHERE vt.NIN = ? " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch Virtual Trade data by Company Code
     * @param companyCode Company Code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchVirtualTradeDataByCompany(String companyCode) {
        try {
            logger.info("Fetching Virtual Trade data for Company: " + companyCode);

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "WHERE vt.COMPANY_CODE = ? " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found for Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch Virtual Trade data by Reason
     * @param reason Reason to query
     * @return true if data found, false otherwise
     */
    public boolean fetchVirtualTradeDataByReason(String reason) {
        try {
            logger.info("Fetching Virtual Trade data for Reason: " + reason);

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "WHERE vt.REASON = ? " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, reason);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found for Reason: " + reason);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch Virtual Trade data by NIN and Company
     * @param nin NIN to query
     * @param companyCode Company Code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchVirtualTradeDataByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching Virtual Trade data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "WHERE vt.NIN = ? AND vt.COMPANY_CODE = ? " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch Virtual Trade data by NIN, Company, and Reason
     * @param nin NIN to query
     * @param companyCode Company Code to query
     * @param reason Reason to query
     * @return true if data found, false otherwise
     */
    public boolean fetchVirtualTradeDataByNinCompanyAndReason(String nin, String companyCode, String reason) {
        try {
            logger.info("Fetching Virtual Trade data for NIN: " + nin +
                       ", Company: " + companyCode + ", Reason: " + reason);

            String query = "SELECT vt.Trade_Date, vt.NIN, vt.company_code, vt.Trnx_Type, " +
                          "vt.volume, vt.price, vt.reason " +
                          "FROM sec_virtual_trade vt " +
                          "WHERE vt.NIN = ? AND vt.COMPANY_CODE = ? AND vt.REASON = ? " +
                          "ORDER BY vt.TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, reason);

            if (!results.isEmpty()) {
                allVirtualTradeData = results;
                logger.info("Virtual Trade data fetched successfully. Found " + results.size() + " record(s)");
                printAllVirtualTradeData();
                return true;
            } else {
                logger.warn("No Virtual Trade data found for NIN: " + nin +
                           ", Company: " + companyCode + ", Reason: " + reason);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Virtual Trade data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get all Virtual Trade records
     * @return List of all Virtual Trade data rows
     */
    public List<Map<String, Object>> getAllVirtualTradeRecords() {
        return allVirtualTradeData;
    }

    /**
     * Get Virtual Trade record by index
     * @param index Index of the record
     * @return Map containing Virtual Trade data for that index
     */
    public Map<String, Object> getVirtualTradeRecordByIndex(int index) {
        if (index >= 0 && index < allVirtualTradeData.size()) {
            return allVirtualTradeData.get(index);
        }
        return null;
    }

    /**
     * Get total number of Virtual Trade records
     * @return Number of records
     */
    public int getRecordCount() {
        return allVirtualTradeData.size();
    }

    /**
     * Print all Virtual Trade records with dynamic columns
     */
    public void printAllVirtualTradeData() {
        System.out.println("\n" + "=".repeat(160));
        System.out.println("VIRTUAL TRADE DATA - Total Records: " + allVirtualTradeData.size());
        System.out.println("=".repeat(160));

        if (allVirtualTradeData.isEmpty()) {
            System.out.println("No data found");
        } else {
            // Get column names from first row
            Map<String, Object> firstRow = allVirtualTradeData.get(0);
            List<String> columnNames = new ArrayList<>(firstRow.keySet());

            // Print header
            for (String column : columnNames) {
                System.out.printf("%-20s ", column);
            }
            System.out.println();
            System.out.println("-".repeat(160));

            // Print data rows (limit to first 20 for readability)
            int displayLimit = Math.min(allVirtualTradeData.size(), 20);
            for (int i = 0; i < displayLimit; i++) {
                Map<String, Object> record = allVirtualTradeData.get(i);
                for (String column : columnNames) {
                    Object value = record.get(column);
                    String displayValue = value != null ? value.toString() : "";
                    // Truncate long values
                    if (displayValue.length() > 18) {
                        displayValue = displayValue.substring(0, 15) + "...";
                    }
                    System.out.printf("%-20s ", displayValue);
                }
                System.out.println();
            }

            if (allVirtualTradeData.size() > 20) {
                System.out.println("\nShowing first 20 of " + allVirtualTradeData.size() + " records");
            }
        }

        System.out.println("=".repeat(160));
    }
}
