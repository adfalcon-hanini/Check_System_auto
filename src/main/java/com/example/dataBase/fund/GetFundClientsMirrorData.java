package com.example.dataBase.fund;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Fund Clients Mirror data from FUND_CLIENTS_MIRROR table
 */
public class GetFundClientsMirrorData {

    private static final Logger logger = Logger.getLogger(GetFundClientsMirrorData.class);
    private OracleDBConnection dbConnection;

    // Fund client mirror data fields (common columns in fund_clients_mirror table)
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
    private String mirrorDate;
    private String syncStatus;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allFundClientsMirrorData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetFundClientsMirrorData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allFundClientsMirrorData = new ArrayList<>();
    }

    /**
     * Fetch fund clients mirror data by client ID
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchFundClientsMirrorByClientId(String clientId) {
        try {
            logger.info("Fetching fund clients mirror data for client ID: " + clientId);

            String query = "SELECT * FROM fund_clients_mirror WHERE cl_id = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allFundClientsMirrorData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientMirrorData(row);
                logger.info("Fund clients mirror data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients mirror data found for client ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients mirror data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all fund clients mirror data
     * @return true if data found, false otherwise
     */
    public boolean fetchAllFundClientsMirror() {
        try {
            logger.info("Fetching all fund clients mirror data");

            String query = "SELECT * FROM fund_clients_mirror";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allFundClientsMirrorData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientMirrorData(row);
                logger.info("Fund clients mirror data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients mirror data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients mirror data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch fund clients mirror data by fund code
     * @param fundCode Fund code to filter
     * @return true if data found, false otherwise
     */
    public boolean fetchFundClientsMirrorByFundCode(String fundCode) {
        try {
            logger.info("Fetching fund clients mirror data for fund code: " + fundCode);

            String query = "SELECT * FROM fund_clients_mirror WHERE FUND_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, fundCode);

            if (!results.isEmpty()) {
                allFundClientsMirrorData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientMirrorData(row);
                logger.info("Fund clients mirror data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients mirror data found for fund code: " + fundCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients mirror data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch active fund clients mirror by client ID
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchActiveFundClientsMirrorByClientId(String clientId) {
        try {
            logger.info("Fetching active fund clients mirror data for client ID: " + clientId);

            String query = "SELECT * FROM fund_clients_mirror " +
                          "WHERE cl_id = ? " +
                          "AND ACCOUNT_STATUS = 'ACTIVE'";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clientId);

            if (!results.isEmpty()) {
                allFundClientsMirrorData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientMirrorData(row);
                logger.info("Active fund clients mirror data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No active fund clients mirror data found for client ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching active fund clients mirror data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch fund clients mirror data by mirror date
     * @param mirrorDate Mirror date to filter (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchFundClientsMirrorByDate(String mirrorDate) {
        try {
            logger.info("Fetching fund clients mirror data for date: " + mirrorDate);

            String query = "SELECT * FROM fund_clients_mirror " +
                          "WHERE TRUNC(MIRROR_DATE) = TO_DATE(?, 'YYYY-MM-DD')";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, mirrorDate);

            if (!results.isEmpty()) {
                allFundClientsMirrorData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseFundClientMirrorData(row);
                logger.info("Fund clients mirror data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund clients mirror data found for date: " + mirrorDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund clients mirror data by date: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse fund client mirror data from database row
     * @param row Database row containing fund client mirror data
     */
    private void parseFundClientMirrorData(Map<String, Object> row) {
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
        this.mirrorDate = row.get("MIRROR_DATE") != null ? row.get("MIRROR_DATE").toString() : "";
        this.syncStatus = row.get("SYNC_STATUS") != null ? row.get("SYNC_STATUS").toString() : "";
    }

    /**
     * Get all fund client mirror data as a map (first row)
     * @return Map containing all fund client mirror fields
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
        data.put("mirrorDate", this.mirrorDate);
        data.put("syncStatus", this.syncStatus);
        return data;
    }

    /**
     * Get all fund client mirror records
     * @return List of all fund client mirror data rows
     */
    public List<Map<String, Object>> getAllFundClientMirrorRecords() {
        return allFundClientsMirrorData;
    }

    /**
     * Get fund client mirror record by index
     * @param index Index of the record
     * @return Map containing fund client mirror data for that index
     */
    public Map<String, Object> getFundClientMirrorRecordByIndex(int index) {
        if (index >= 0 && index < allFundClientsMirrorData.size()) {
            return allFundClientsMirrorData.get(index);
        }
        return null;
    }

    /**
     * Get total number of fund client mirror records
     * @return Number of records
     */
    public int getRecordCount() {
        return allFundClientsMirrorData.size();
    }

    /**
     * Print fund client mirror data to console (first record)
     */
    public void printFundClientMirrorData() {
        System.out.println("\n========== Fund Client Mirror Data (First Record) ==========");
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
        System.out.println("Mirror Date: " + mirrorDate);
        System.out.println("Sync Status: " + syncStatus);
        System.out.println("============================================================");
    }

    /**
     * Print all fund client mirror records
     */
    public void printAllFundClientMirrorData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║       All Fund Client Mirror Records (" + allFundClientsMirrorData.size() + " total)          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allFundClientsMirrorData.size(); i++) {
            System.out.println("─────────── Fund Client Mirror " + (i + 1) + " ───────────");
            Map<String, Object> record = allFundClientsMirrorData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-20s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print fund clients mirror in a formatted table
     */
    public void printFundClientsMirrorTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                              Fund Clients Mirror Table                                                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-10s ║ %-12s ║ %-25s ║ %-15s ║ %-15s ║ %-10s ║ %-12s ║%n",
                          "CL_ID", "FUND_CODE", "FUND_NAME", "BALANCE", "AVAIL_BALANCE", "STATUS", "MIRROR_DATE");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allFundClientsMirrorData) {
            String clId = record.get("CL_ID") != null ? record.get("CL_ID").toString() : "";
            String fundCode = record.get("FUND_CODE") != null ? record.get("FUND_CODE").toString() : "";
            String fundName = record.get("FUND_NAME") != null ? record.get("FUND_NAME").toString() : "";
            String balance = record.get("BALANCE") != null ? record.get("BALANCE").toString() : "";
            String availBalance = record.get("AVAILABLE_BALANCE") != null ? record.get("AVAILABLE_BALANCE").toString() : "";
            String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "";
            String mirrorDate = record.get("MIRROR_DATE") != null ? record.get("MIRROR_DATE").toString() : "";

            // Truncate long values
            if (fundCode.length() > 12) fundCode = fundCode.substring(0, 9) + "...";
            if (fundName.length() > 25) fundName = fundName.substring(0, 22) + "...";
            if (status.length() > 10) status = status.substring(0, 7) + "...";
            if (mirrorDate.length() > 12) mirrorDate = mirrorDate.substring(0, 9) + "...";

            System.out.printf("║ %-10s ║ %-12s ║ %-25s ║ %-15s ║ %-15s ║ %-10s ║ %-12s ║%n",
                            clId, fundCode, fundName, balance, availBalance, status, mirrorDate);
        }

        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Fund Client Mirror Records: " + allFundClientsMirrorData.size());
    }

    /**
     * Print fund clients mirror summary
     */
    public void printFundClientsMirrorSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║             Fund Clients Mirror Summary                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        if (allFundClientsMirrorData.isEmpty()) {
            System.out.println("No fund client mirror records found.");
            return;
        }

        // Calculate statistics
        double totalBalance = 0;
        double totalAvailableBalance = 0;
        Map<String, Integer> fundCount = new HashMap<>();
        Map<String, Integer> statusCount = new HashMap<>();
        Map<String, Integer> syncStatusCount = new HashMap<>();
        int activeAccounts = 0;

        for (Map<String, Object> record : allFundClientsMirrorData) {
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

            // Count by account status
            String status = record.get("ACCOUNT_STATUS") != null ? record.get("ACCOUNT_STATUS").toString() : "UNKNOWN";
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            if ("ACTIVE".equalsIgnoreCase(status)) {
                activeAccounts++;
            }

            // Count by sync status
            String syncStatus = record.get("SYNC_STATUS") != null ? record.get("SYNC_STATUS").toString() : "UNKNOWN";
            syncStatusCount.put(syncStatus, syncStatusCount.getOrDefault(syncStatus, 0) + 1);
        }

        System.out.println("\nClient ID: " + (!allFundClientsMirrorData.isEmpty() && allFundClientsMirrorData.get(0).get("CL_ID") != null ?
                          allFundClientsMirrorData.get(0).get("CL_ID").toString() : "N/A"));
        System.out.println("Total Fund Mirror Records: " + allFundClientsMirrorData.size());
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

        System.out.println("\nAccounts by Sync Status:");
        for (Map.Entry<String, Integer> entry : syncStatusCount.entrySet()) {
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

    public String getMirrorDate() {
        return mirrorDate;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

}
