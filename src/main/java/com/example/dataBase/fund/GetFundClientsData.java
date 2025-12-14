package com.example.dataBase.fund;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Fund Clients data from FUND_CLIENTS table
 */
public class GetFundClientsData {

    private static final Logger logger = Logger.getLogger(GetFundClientsData.class);
    private OracleDBConnection dbConnection;

    // Fund client data fields (common columns in fund_clients table)
    private String clientId;
    private String fundCode;
    private String fundName;
    private String accountNumber;
    private String balance;
    private String availableBalance;
    private String currency;
    private String accountStatus;
    private String openDate;
    private String closeDate;
    private String lastUpdateDate;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allFundClientsData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetFundClientsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allFundClientsData = new ArrayList<>();
    }

    /**
     * Fetch fund clients data by client ID
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchFundClientsByClientId(String clientId) {
        try {
            logger.info("Fetching fund clients data for client ID: " + clientId);

            String query = "SELECT * FROM fund_clients WHERE cl_id = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allFundClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientData(row);
                logger.info("Fund clients data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients data found for client ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all fund clients data
     * @return true if data found, false otherwise
     */
    public boolean fetchAllFundClients() {
        try {
            logger.info("Fetching all fund clients data");

            String query = "SELECT * FROM fund_clients";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allFundClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientData(row);
                logger.info("Fund clients data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch fund clients data by fund code
     * @param fundCode Fund code to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchFundClientsByFundCode(String fundCode) {
        try {
            logger.info("Fetching fund clients data for fund code: " + fundCode);

            String query = "SELECT * FROM fund_clients WHERE FUND_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, fundCode);

            if (!results.isEmpty()) {
                allFundClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientData(row);
                logger.info("Fund clients data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients data found for fund code: " + fundCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch active fund clients by client ID
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchActiveFundClientsByClientId(String clientId) {
        try {
            logger.info("Fetching active fund clients data for client ID: " + clientId);

            String query = "SELECT * FROM fund_clients " +
                          "WHERE cl_id = ? " +
                          "AND ACCOUNT_STATUS = 'ACTIVE'";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allFundClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientData(row);
                logger.info("Active fund clients data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No active fund clients data found for client ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching active fund clients data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse fund client data from database row
     * @param row Database row containing fund client data
     */
    private void parseFundClientData(Map<String, Object> row) {
        this.clientId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
        this.fundCode = row.get("FUND_CODE") != null ? row.get("FUND_CODE").toString() : "";
        this.fundName = row.get("FUND_NAME") != null ? row.get("FUND_NAME").toString() : "";
        this.accountNumber = row.get("ACCOUNT_NUMBER") != null ? row.get("ACCOUNT_NUMBER").toString() : "";
        this.balance = row.get("BALANCE") != null ? row.get("BALANCE").toString() : "";
        this.availableBalance = row.get("AVAILABLE_BALANCE") != null ? row.get("AVAILABLE_BALANCE").toString() : "";
        this.currency = row.get("CURRENCY") != null ? row.get("CURRENCY").toString() : "";
        this.accountStatus = row.get("ACCOUNT_STATUS") != null ? row.get("ACCOUNT_STATUS").toString() : "";
        this.openDate = row.get("OPEN_DATE") != null ? row.get("OPEN_DATE").toString() : "";
        this.closeDate = row.get("CLOSE_DATE") != null ? row.get("CLOSE_DATE").toString() : "";
        this.lastUpdateDate = row.get("LAST_UPDATE_DATE") != null ? row.get("LAST_UPDATE_DATE").toString() : "";
    }

    /**
     * Get all fund client data as a map (first row)
     * @return Map containing all fund client fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("clientId", this.clientId);
        data.put("fundCode", this.fundCode);
        data.put("fundName", this.fundName);
        data.put("accountNumber", this.accountNumber);
        data.put("balance", this.balance);
        data.put("availableBalance", this.availableBalance);
        data.put("currency", this.currency);
        data.put("accountStatus", this.accountStatus);
        data.put("openDate", this.openDate);
        data.put("closeDate", this.closeDate);
        data.put("lastUpdateDate", this.lastUpdateDate);
        return data;
    }

    /**
     * Get all fund client records
     * @return List of all fund client data rows
     */
    public List<Map<String, Object>> getAllFundClientRecords() {
        return allFundClientsData;
    }

    /**
     * Get fund client record by index
     * @param index Index of the record
     * @return Map containing fund client data for that index
     */
    public Map<String, Object> getFundClientRecordByIndex(int index) {
        if (index >= 0 && index < allFundClientsData.size()) {
            return allFundClientsData.get(index);
        }
        return null;
    }

    /**
     * Get total number of fund client records
     * @return Number of records
     */
    public int getRecordCount() {
        return allFundClientsData.size();
    }

    /**
     * Print fund client data to console (first record)
     */
    public void printFundClientData() {
        System.out.println("\n========== Fund Client Data (First Record) ==========");
        System.out.println("Client ID: " + clientId);
        System.out.println("Fund Code: " + fundCode);
        System.out.println("Fund Name: " + fundName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
        System.out.println("Available Balance: " + availableBalance);
        System.out.println("Currency: " + currency);
        System.out.println("Account Status: " + accountStatus);
        System.out.println("Open Date: " + openDate);
        System.out.println("Close Date: " + closeDate);
        System.out.println("Last Update Date: " + lastUpdateDate);
        System.out.println("=====================================================");
    }

    /**
     * Print all fund client records
     */
    public void printAllFundClientData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          All Fund Client Records (" + allFundClientsData.size() + " total)               ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allFundClientsData.size(); i++) {
            System.out.println("─────────────── Fund Client " + (i + 1) + " ───────────────");
            Map<String, Object> record = allFundClientsData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-20s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print fund clients in a formatted table
     */
    public void printFundClientsTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                              Fund Clients Table                                                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-10s ║ %-12s ║ %-25s ║ %-15s ║ %-15s ║ %-10s ║%n",
                          "CL_ID", "FUND_CODE", "FUND_NAME", "BALANCE", "AVAIL_BALANCE", "STATUS");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allFundClientsData) {
            String clId = record.get("CL_ID") != null ? record.get("CL_ID").toString() : "";
            String fundCode = record.get("FUND_CODE") != null ? record.get("FUND_CODE").toString() : "";
            String fundName = record.get("FUND_NAME") != null ? record.get("FUND_NAME").toString() : "";
            String balance = record.get("BALANCE") != null ? record.get("BALANCE").toString() : "";
            String availBalance = record.get("AVAILABLE_BALANCE") != null ? record.get("AVAILABLE_BALANCE").toString() : "";
            String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "";

            // Truncate long values
            if (fundCode.length() > 12) fundCode = fundCode.substring(0, 9) + "...";
            if (fundName.length() > 25) fundName = fundName.substring(0, 22) + "...";
            if (status.length() > 10) status = status.substring(0, 7) + "...";

            System.out.printf("║ %-10s ║ %-12s ║ %-25s ║ %-15s ║ %-15s ║ %-10s ║%n",
                            clId, fundCode, fundName, balance, availBalance, status);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Fund Client Records: " + allFundClientsData.size());
    }

    /**
     * Print fund clients summary
     */
    public void printFundClientsSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Fund Clients Summary                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allFundClientsData.isEmpty()) {
            System.out.println("No fund client records found.");
            return;
        }

        // Calculate statistics
        double totalBalance = 0;
        double totalAvailableBalance = 0;
        Map<String, Integer> fundCount = new HashMap<>();
        Map<String, Integer> statusCount = new HashMap<>();
        int activeAccounts = 0;

        for (Map<String, Object> record : allFundClientsData) {
            // Sum balances
            try {
                if (record.get("BALANCE") != null) {
                    totalBalance += Double.parseDouble(record.get("BALANCE").toString());
                }
                if (record.get("AVAILABLE_BALANCE") != null) {
                    totalAvailableBalance += Double.parseDouble(record.get("AVAILABLE_BALANCE").toString());
                }
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }

            // Count by fund
            String fund = record.get("FUND_CODE") != null ? record.get("FUND_CODE").toString() : "UNKNOWN";
            fundCount.put(fund, fundCount.getOrDefault(fund, 0) + 1);

            // Count by status
            String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "UNKNOWN";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            if ("ACTIVE".equalsIgnoreCase(status)) {
                activeAccounts++;
            }
        }

        System.out.println("\nClient ID: " + (!allFundClientsData.isEmpty() && allFundClientsData.get(0).get("CL_ID") != null ?
                          allFundClientsData.get(0).get("CL_ID").toString() : "N/A"));
        System.out.println("Total Fund Accounts: " + allFundClientsData.size());
        System.out.println("Active Accounts: " + activeAccounts);
        System.out.println();
        System.out.println("Financial Summary:");
        System.out.println("  Total Balance: " + String.format("%.2f", totalBalance));
        System.out.println("  Total Available Balance: " + String.format("%.2f", totalAvailableBalance));

        System.out.println("\nAccounts by Fund:");
        for (Map.Entry<String, Integer> entry : fundCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " account(s)");
        }

        System.out.println("\nAccounts by Status:");
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " account(s)");
        }
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    // Getters
    public String getClientId() {
        return clientId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBalance() {
        return balance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

}
