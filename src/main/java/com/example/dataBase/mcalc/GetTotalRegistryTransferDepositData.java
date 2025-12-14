package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Stock Transfer Deposit data from SEC_EQ_STOCKTRANFER table
 * Contains fetch methods for successful stock transfers with various filter combinations
 */
public class GetTotalRegistryTransferDepositData {

    private static final Logger logger = Logger.getLogger(GetTotalRegistryTransferDepositData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetTotalRegistryTransferDepositData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch stock transfer data by NIN, Symbol, Status and Date Range with volume > 0
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @param operationStatus Operation status to query (e.g., 'Success')
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByNinSymbolStatusAndDateRange(String nin, String symbol, String operationStatus, String startDate, String endDate) {
        try {
            logger.info("Fetching stock transfer data for NIN: " + nin + ", Symbol: " + symbol + ", Status: " + operationStatus + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE OPERATION_STATUS = ? " +
                          "AND NIN = ? " +
                          "AND SYMBOL = ? " +
                          "AND volume > 0 " +
                          "AND TRUNC(EXECUTION_TIME) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, operationStatus, nin, symbol, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found for NIN: " + nin + ", Symbol: " + symbol + ", Status: " + operationStatus);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch stock transfer data by NIN and Symbol with volume > 0 (all statuses and dates)
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByNinAndSymbol(String nin, String symbol) {
        try {
            logger.info("Fetching stock transfer data for NIN: " + nin + " and Symbol: " + symbol);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE NIN = ? " +
                          "AND SYMBOL = ? " +
                          "AND volume > 0 " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, symbol);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found for NIN: " + nin + " and Symbol: " + symbol);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch successful stock transfers by NIN and Symbol within date range with volume > 0
     * @param nin NIN to query
     * @param symbol Symbol/Company code to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchSuccessfulTransfersByNinSymbolAndDateRange(String nin, String symbol, String startDate, String endDate) {
        try {
            logger.info("Fetching successful stock transfers for NIN: " + nin + ", Symbol: " + symbol + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE OPERATION_STATUS = 'Success' " +
                          "AND NIN = ? " +
                          "AND SYMBOL = ? " +
                          "AND volume > 0 " +
                          "AND TRUNC(EXECUTION_TIME) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, symbol, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Successful stock transfers fetched. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No successful stock transfers found for NIN: " + nin + ", Symbol: " + symbol);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching successful stock transfers: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch stock transfer data by NIN only with volume > 0 (all symbols, statuses, and dates)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByNin(String nin) {
        try {
            logger.info("Fetching stock transfer data for NIN: " + nin);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE NIN = ? " +
                          "AND volume > 0 " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch stock transfer data by Operation Status with volume > 0 (all NIPs and symbols)
     * @param operationStatus Operation status to query (e.g., 'Success', 'Pending', 'Failed')
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByStatus(String operationStatus) {
        try {
            logger.info("Fetching stock transfer data with Status: " + operationStatus);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE OPERATION_STATUS = ? " +
                          "AND volume > 0 " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, operationStatus);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found with Status: " + operationStatus);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch stock transfer data by NIN and Status with volume > 0
     * @param nin NIN to query
     * @param operationStatus Operation status to query
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByNinAndStatus(String nin, String operationStatus) {
        try {
            logger.info("Fetching stock transfer data for NIN: " + nin + " with Status: " + operationStatus);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE NIN = ? " +
                          "AND OPERATION_STATUS = ? " +
                          "AND volume > 0 " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, operationStatus);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found for NIN: " + nin + " with Status: " + operationStatus);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch stock transfer data by NIN within date range with volume > 0 (all symbols and statuses)
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchStockTransferByNinAndDateRange(String nin, String startDate, String endDate) {
        try {
            logger.info("Fetching stock transfer data for NIN: " + nin + " between " + startDate + " and " + endDate);

            String query = "SELECT * FROM SEC_EQ_STOCKTRANFER " +
                          "WHERE NIN = ? " +
                          "AND volume > 0 " +
                          "AND TRUNC(EXECUTION_TIME) BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY EXECUTION_TIME DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Stock transfer data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No stock transfer data found for NIN: " + nin + " within date range");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching stock transfer data: " + e.getMessage(), e);
            return false;
        }
    }
}
