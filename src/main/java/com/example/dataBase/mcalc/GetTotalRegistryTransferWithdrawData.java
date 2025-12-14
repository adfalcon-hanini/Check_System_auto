package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Registry Transfer Withdraw data from GRP_REGISTERY_TRANSFER table
 * Contains fetch methods for successful transfers with various filter combinations
 */
public class GetTotalRegistryTransferWithdrawData {

    private static final Logger logger = Logger.getLogger(GetTotalRegistryTransferWithdrawData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetTotalRegistryTransferWithdrawData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch registry transfer data by NIN, Symbol, Status and Date Range
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @param status Status to query (e.g., 'Success')
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByNinSymbolStatusAndDateRange(String nin, String symbol, String status, String startDate, String endDate) {
        try {
            logger.info("Fetching registry transfer data for NIN: " + nin + ", Symbol: " + symbol + ", Status: " + status + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "AND SYMBOL = ? " +
                          "AND Status = ? " +
                          "AND TRUNC(CREATION_Time) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, symbol, status, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found for NIN: " + nin + ", Symbol: " + symbol + ", Status: " + status);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch registry transfer data by NIN and Symbol (all statuses and dates)
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByNinAndSymbol(String nin, String symbol) {
        try {
            logger.info("Fetching registry transfer data for NIN: " + nin + " and Symbol: " + symbol);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "AND SYMBOL = ? " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, symbol);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found for NIN: " + nin + " and Symbol: " + symbol);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch successful registry transfers by NIN and Symbol within date range
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchSuccessfulTransfersByNinSymbolAndDateRange(String nin, String symbol, String startDate, String endDate) {
        try {
            logger.info("Fetching successful registry transfers for NIN: " + nin + ", Symbol: " + symbol + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "AND SYMBOL = ? " +
                          "AND Status = 'Success' " +
                          "AND TRUNC(CREATION_Time) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, symbol, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Successful registry transfers fetched. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No successful registry transfers found for NIN: " + nin + ", Symbol: " + symbol);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching successful registry transfers: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch registry transfer data by NIN only (all symbols, statuses, and dates)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByNin(String nin) {
        try {
            logger.info("Fetching registry transfer data for NIN: " + nin);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch registry transfer data by Status only (all NIPs and symbols)
     * @param status Status to query (e.g., 'Success', 'Pending', 'Failed')
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByStatus(String status) {
        try {
            logger.info("Fetching registry transfer data with Status: " + status);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE Status = ? " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, status);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found with Status: " + status);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch registry transfer data by NIN and Status
     * @param nin NIN to query
     * @param status Status to query
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByNinAndStatus(String nin, String status) {
        try {
            logger.info("Fetching registry transfer data for NIN: " + nin + " with Status: " + status);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "AND Status = ? " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, status);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found for NIN: " + nin + " with Status: " + status);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch registry transfer data by NIN within date range (all symbols and statuses)
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchRegistryTransferByNinAndDateRange(String nin, String startDate, String endDate) {
        try {
            logger.info("Fetching registry transfer data for NIN: " + nin + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM GRP_REGISTERY_TRANSFER " +
                          "WHERE NIN = ? " +
                          "AND TRUNC(CREATION_Time) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY CREATION_Time DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Registry transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No registry transfer data found for NIN: " + nin + " within date range");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching registry transfer data: " + e.getMessage(), e);
            return false;
        }
    }
}
