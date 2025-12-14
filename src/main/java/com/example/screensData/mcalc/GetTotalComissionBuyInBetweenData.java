package com.example.screensData.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Total Commission for BUY transactions from SEC_ACC_TRNXS table
 * Contains methods to calculate sum of commission amounts (INT type) for BUY transactions between dates
 */
public class GetTotalComissionBuyInBetweenData {

    private static final Logger logger = Logger.getLogger(GetTotalComissionBuyInBetweenData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetTotalComissionBuyInBetweenData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Get total commission amount (INT) for BUY transactions by NIN, Company, and Date Range
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @param companyCode Company code to query
     * @return Total commission amount for BUY transactions, or null if no data found
     */
    public Double getTotalCommissionBuyByNinAndCompanyBetweenDates(String nin, String startDate, String endDate, String companyCode) {
        try {
            logger.info("Calculating total commission for BUY transactions for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS a " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = 'INT' " +
                          "AND COMPANY_CODE = ? " +
                          "AND SL_NO IN (" +
                          "  SELECT SL_NO FROM SEC_ACC_TRNXS b " +
                          "  WHERE a.SL_NO = b.SL_NO " +
                          "  AND TRNX_TYPE = 'BUY'" +
                          ")";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate, companyCode);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total commission for BUY transactions calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No commission data found for BUY transactions for NIN: " + nin + ", Company: " + companyCode + " between dates");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total commission for BUY transactions: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get total commission amount (INT) for BUY transactions by NIN and Company (all dates)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return Total commission amount for BUY transactions, or null if no data found
     */
    public Double getTotalCommissionBuyByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Calculating total commission for BUY transactions for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS a " +
                          "WHERE NIN = ? " +
                          "AND TRNX_TYPE = 'INT' " +
                          "AND COMPANY_CODE = ? " +
                          "AND SL_NO IN (" +
                          "  SELECT SL_NO FROM SEC_ACC_TRNXS b " +
                          "  WHERE a.SL_NO = b.SL_NO " +
                          "  AND TRNX_TYPE = 'BUY'" +
                          ")";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total commission for BUY transactions calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No commission data found for BUY transactions for NIN: " + nin + " and Company: " + companyCode);
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total commission for BUY transactions: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get total commission amount (INT) for BUY transactions by NIN across all companies between dates
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return Total commission amount for BUY transactions, or null if no data found
     */
    public Double getTotalCommissionBuyByNinBetweenDates(String nin, String startDate, String endDate) {
        try {
            logger.info("Calculating total commission for BUY transactions for NIN: " + nin + " between " + startDate + " and " + endDate);

            String query = "SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM SEC_ACC_TRNXS a " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = 'INT' " +
                          "AND SL_NO IN (" +
                          "  SELECT SL_NO FROM SEC_ACC_TRNXS b " +
                          "  WHERE a.SL_NO = b.SL_NO " +
                          "  AND TRNX_TYPE = 'BUY'" +
                          ")";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate);

            if (!results.isEmpty() && results.get(0).get("TOTAL_AMOUNT") != null) {
                Double totalAmount = ((Number) results.get(0).get("TOTAL_AMOUNT")).doubleValue();
                logger.info("Total commission for BUY transactions calculated: " + totalAmount);
                return totalAmount;
            } else {
                logger.warn("No commission data found for BUY transactions for NIN: " + nin + " between dates");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error calculating total commission for BUY transactions: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get commission count for BUY transactions by NIN, Company, and Date Range
     * @param nin NIN to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @param companyCode Company code to query
     * @return Commission transaction count, or null if no data found
     */
    public Integer getCommissionBuyCount(String nin, String startDate, String endDate, String companyCode) {
        try {
            logger.info("Counting commission transactions for BUY for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT COUNT(*) AS TRNX_COUNT FROM SEC_ACC_TRNXS a " +
                          "WHERE NIN = ? " +
                          "AND TRNX_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRNX_TYPE = 'INT' " +
                          "AND COMPANY_CODE = ? " +
                          "AND SL_NO IN (" +
                          "  SELECT SL_NO FROM SEC_ACC_TRNXS b " +
                          "  WHERE a.SL_NO = b.SL_NO " +
                          "  AND TRNX_TYPE = 'BUY'" +
                          ")";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, startDate, endDate, companyCode);

            if (!results.isEmpty() && results.get(0).get("TRNX_COUNT") != null) {
                Integer count = ((Number) results.get(0).get("TRNX_COUNT")).intValue();
                logger.info("Commission transaction count for BUY: " + count);
                return count;
            } else {
                logger.warn("No commission transactions found for BUY");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Error counting commission transactions for BUY: " + e.getMessage(), e);
            return null;
        }
    }
}
