package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Cash/Client Daily Balance data from sec_cltdaily_balances table
 * Contains fetch methods for client balance information with various filter combinations
 */
public class GetCashData {

    private static final Logger logger = Logger.getLogger(GetCashData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetCashData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch client daily balance by NIN and balance date
     * @param nin NIN to query
     * @param balanceDate Balance date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByNinAndDate(String nin, String balanceDate) {
        try {
            logger.info("Fetching client daily balance for NIN: " + nin + ", Balance Date: " + balanceDate);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(balance_date) = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY balance_date";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, balanceDate);

            if (!results.isEmpty()) {
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Balance Date: " + row.get("BALANCE_DATE") + ", Current Balance: " + row.get("CUR_BAL"));
                }

                return true;
            } else {
                logger.warn("No client daily balance data found for NIN: " + nin + ", Balance Date: " + balanceDate);
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
            logger.info("Fetching client daily balance for NIN: " + nin);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? " +
                          "ORDER BY balance_date DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Balance Date: " + row.get("BALANCE_DATE") + ", Current Balance: " + row.get("CUR_BAL"));
                }

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
     * Fetch client daily balance by NIN within date range
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByNinAndDateRange(String nin, String startDate, String endDate) {
        try {
            logger.info("Fetching client daily balance for NIN: " + nin + " between " + startDate + " and " + endDate);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(balance_date) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY balance_date";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Balance Date: " + row.get("BALANCE_DATE") + ", Current Balance: " + row.get("CUR_BAL"));
                }

                return true;
            } else {
                logger.warn("No client daily balance data found for NIN: " + nin + " within date range");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest client daily balance by NIN (most recent date)
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
                logger.info("Latest client daily balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Balance Date: " + row.get("BALANCE_DATE") + ", Current Balance: " + row.get("CUR_BAL"));
                }

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
     * Fetch all client daily balances by balance date (all clients)
     * @param balanceDate Balance date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalanceByDate(String balanceDate) {
        try {
            logger.info("Fetching client daily balance for Balance Date: " + balanceDate);

            String query = "SELECT NIN, balance_date, CUR_BAL FROM sec_cltdaily_balances " +
                          "WHERE TRUNC(balance_date) = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY NIN";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, balanceDate);

            if (!results.isEmpty()) {
                logger.info("Client daily balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Balance Date: " + row.get("BALANCE_DATE") + ", Current Balance: " + row.get("CUR_BAL"));
                }

                return true;
            } else {
                logger.warn("No client daily balance data found for Balance Date: " + balanceDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client daily balance data: " + e.getMessage(), e);
            return false;
        }
    }
}
