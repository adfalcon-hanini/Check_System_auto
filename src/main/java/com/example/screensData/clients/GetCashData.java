package com.example.screensData.clients;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Client Daily Balance data from sec_cltdaily_balances table
 * Returns: NIN, Balance Date, and Balance
 */
public class GetCashData {

    private static final Logger logger = Logger.getLogger(GetCashData.class);
    private OracleDBConnection dbConnection;

    // Client daily balance data fields
    private String nin;
    private String balanceDate;
    private String balance;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allBalanceData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetCashData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allBalanceData = new ArrayList<>();
    }

    /**
     * Fetch client daily balance by NIN and Date
     * @param nin NIN to query
     * @param date Balance date to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByNinAndDate(String nin, String date) {
        try {
            logger.info("Fetching client daily balance for NIN: " + nin + " and Date: " + date);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? AND TRUNC(balance_date) = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY balance_date";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, date);

            if (!results.isEmpty()) {
                allBalanceData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseBalanceData(row);
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");
                printAllBalanceData();
                return true;
            } else {
                logger.warn("No client daily balance data found for NIN: " + nin + " and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch client daily balance by NIN (all dates)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByNin(String nin) {
        try {
            logger.info("Fetching all client daily balances for NIN: " + nin);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? ORDER BY balance_date DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                allBalanceData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseBalanceData(row);
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");
                printAllBalanceData();
                return true;
            } else {
                logger.warn("No client daily balance data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch client daily balance by NIN and Date Range
     * @param nin NIN to query
     * @param startDate Start date of range
     * @param endDate End date of range
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByNinAndDateRange(String nin, String startDate, String endDate) {
        try {
            logger.info("Fetching client daily balance for NIN: " + nin +
                       " between " + startDate + " and " + endDate);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(balance_date) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY balance_date";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty()) {
                allBalanceData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseBalanceData(row);
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");
                printAllBalanceData();
                return true;
            } else {
                logger.warn("No client daily balance data found for NIN: " + nin +
                           " between " + startDate + " and " + endDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest client daily balance by NIN
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchLatestClientBalanceByNin(String nin) {
        try {
            logger.info("Fetching latest client daily balance for NIN: " + nin);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? " +
                          "AND balance_date = (SELECT MAX(balance_date) FROM sec_cltdaily_balances WHERE NIN = ?)";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, nin);

            if (!results.isEmpty()) {
                allBalanceData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseBalanceData(row);
                logger.info("Latest client daily balance data fetched successfully");
                printAllBalanceData();
                return true;
            } else {
                logger.warn("No latest client daily balance data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching latest client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch client daily balance by Date (all clients)
     * @param date Balance date to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByDate(String date) {
        try {
            logger.info("Fetching client daily balance for all clients on Date: " + date);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE TRUNC(balance_date) = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY NIN";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, date);

            if (!results.isEmpty()) {
                allBalanceData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseBalanceData(row);
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");
                printAllBalanceData();
                return true;
            } else {
                logger.warn("No client daily balance data found for Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse balance data from database row
     * @param row Database row containing balance data
     */
    private void parseBalanceData(Map<String, Object> row) {
        this.nin = row.get("NIN") != null ? row.get("NIN").toString() : "";
        this.balanceDate = row.get("BALANCE_DATE") != null ? row.get("BALANCE_DATE").toString() : "";
        this.balance = row.get("CUR_BAL") != null ? row.get("CUR_BAL").toString() : "";
    }

    /**
     * Get all balance data as a map (first row)
     * @return Map containing all balance fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("nin", this.nin);
        data.put("balanceDate", this.balanceDate);
        data.put("balance", this.balance);
        return data;
    }

    /**
     * Get all balance records
     * @return List of all balance data rows
     */
    public List<Map<String, Object>> getAllBalanceRecords() {
        return allBalanceData;
    }

    /**
     * Get balance record by index
     * @param index Index of the record
     * @return Map containing balance data for that index
     */
    public Map<String, Object> getBalanceRecordByIndex(int index) {
        if (index >= 0 && index < allBalanceData.size()) {
            return allBalanceData.get(index);
        }
        return null;
    }

    /**
     * Get total number of balance records
     * @return Number of records
     */
    public int getRecordCount() {
        return allBalanceData.size();
    }

    /**
     * Print balance data to console (first record)
     */
    public void printBalanceData() {
        System.out.println("\n========== Client Daily Balance Data ==========");
        System.out.println("NIN: " + nin);
        System.out.println("Balance Date: " + balanceDate);
        System.out.println("Balance: " + balance);
        System.out.println("===============================================");
    }

    /**
     * Print all balance records
     */
    public void printAllBalanceData() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("CLIENT DAILY BALANCE DATA - Total Records: " + allBalanceData.size());
        System.out.println("=".repeat(100));

        if (allBalanceData.isEmpty()) {
            System.out.println("No data found");
        } else {
            // Print header
            System.out.printf("%-15s %-25s %-20s%n", "NIN", "BALANCE DATE", "BALANCE");
            System.out.println("-".repeat(100));

            // Print data rows
            for (Map<String, Object> record : allBalanceData) {
                String ninValue = record.get("NIN") != null ? record.get("NIN").toString() : "";
                String dateValue = record.get("BALANCE_DATE") != null ? record.get("BALANCE_DATE").toString() : "";
                String balValue = record.get("CUR_BAL") != null ? record.get("CUR_BAL").toString() : "";

                System.out.printf("%-15s %-25s %-20s%n", ninValue, dateValue, balValue);
            }
        }

        System.out.println("=".repeat(100));
    }

    // Getters
    public String getNin() {
        return nin;
    }

    public String getBalanceDate() {
        return balanceDate;
    }

    public String getBalance() {
        return balance;
    }
}
