package com.example.screensData.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Total Sell transaction data from SEC_ACC_TRNXS table
 * Contains methods to calculate sum of amounts for SELL transactions between dates
 */
public class GetTotalSellInBetweenData {

    private static final Logger logger = Logger.getLogger(GetTotalSellInBetweenData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetTotalSellInBetweenData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Get total SELL amount for NIN, Company, and Date Range
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @param companyCode Company code to query
     * @return Total sell amount, or null if no data found
     */
    public Double getTotalSellByNinAndCompanyBetweenDates(String nin, String startDate, String endDate, String companyCode) {
        try {
            logger.info("Calculating total SELL amount for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = 'SELL' " +
                          "AND COMPANY_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate, companyCode);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total SELL amount calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No SELL transactions found for NIN: " + nin + ", Company: " + companyCode + " between dates");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total SELL amount: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get total SELL amount for NIN and Company (all dates)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return Total sell amount, or null if no data found
     */
    public Double getTotalSellByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Calculating total SELL amount for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS " +
                          "WHERE NIN = ? " +
                          "AND TRNX_TYPE = 'SELL' " +
                          "AND COMPANY_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total SELL amount calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No SELL transactions found for NIN: " + nin + " and Company: " + companyCode);
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total SELL amount: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get total amount for any transaction type (SELL, BUY, etc.)
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @param companyCode Company code to query
     * @param trnxType Transaction type (SELL, BUY, etc.)
     * @return Total amount, or null if no data found
     */
    public Double getTotalAmountByType(String nin, String startDate, String endDate, String companyCode, String trnxType) {
        try {
            logger.info("Calculating total " + trnxType + " amount for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = ? " +
                          "AND COMPANY_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate, trnxType, companyCode);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total " + trnxType + " amount calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No " + trnxType + " transactions found for NIN: " + nin + ", Company: " + companyCode + " between dates");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total " + trnxType + " amount: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get total SELL amount for NIN across all companies between dates
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return Total sell amount, or null if no data found
     */
    public Double getTotalSellByNinBetweenDates(String nin, String startDate, String endDate) {
        try {
            logger.info("Calculating total SELL amount for NIN: " + nin + " between " + startDate + " and " + endDate);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = 'SELL'";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total SELL amount calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No SELL transactions found for NIN: " + nin + " between dates");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total SELL amount: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get transaction count for NIN, Company, Type and Date Range
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @param companyCode Company code to query
     * @param trnxType Transaction type (SELL, BUY, etc.)
     * @return Transaction count, or null if no data found
     */
    public Integer getTransactionCount(String nin, String startDate, String endDate, String companyCode, String trnxType) {
        try {
            logger.info("Counting " + trnxType + " transactions for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT COUNT(*) AS TRNX_COUNT FROM SEC_ACC_TRNXS " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = ? " +
                          "AND COMPANY_CODE = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate, trnxType, companyCode);

            if (!results.isEmpty() && results.get(0).get("TRNX_COUNT") != null) {
                Integer count = ((Number) results.get(0).get("TRNX_COUNT")).intValue();
                logger.info("Transaction count: " + count);
                return count;
            } else {
                logger.warn("No transactions found");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error counting transactions: " + e.getMessage(), e);
            return null;
        }
    }
}
