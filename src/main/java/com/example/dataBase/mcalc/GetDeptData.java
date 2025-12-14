package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Fund Daily Summary (Dept) data from fund_daily_summary table
 * Contains fetch methods for client fund information with various filter combinations
 */
public class GetDeptData {

    private static final Logger logger = Logger.getLogger(GetDeptData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetDeptData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch fund daily summary by client ID and summary date
     * @param clId Client ID to query
     * @param summaryDate Summary date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchFundSummaryByClientIdAndDate(String clId, String summaryDate) {
        try {
            logger.info("Fetching fund daily summary for Client ID: " + clId + ", Summary Date: " + summaryDate);

            String query = "SELECT cl_id, SUMMARY_DATE, LAST_ACTUAL_FUND, LAST_TOTAL_DELAY_FEES FROM fund_daily_summary " +
                          "WHERE cl_id = ? " +
                          "AND SUMMARY_DATE = TO_DATE(?, 'DD-Mon-YYYY')";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clId, summaryDate);

            if (!results.isEmpty()) {
                logger.info("Fund daily summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund daily summary data found for Client ID: " + clId + ", Summary Date: " + summaryDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund daily summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch fund daily summary by client ID (all dates)
     * @param clId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchFundSummaryByClientId(String clId) {
        try {
            logger.info("Fetching fund daily summary for Client ID: " + clId);

            String query = "SELECT cl_id, SUMMARY_DATE, LAST_ACTUAL_FUND, LAST_TOTAL_DELAY_FEES FROM fund_daily_summary " +
                          "WHERE cl_id = ? " +
                          "ORDER BY SUMMARY_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clId);

            if (!results.isEmpty()) {
                logger.info("Fund daily summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund daily summary data found for Client ID: " + clId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund daily summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch fund daily summary by client ID within date range
     * @param clId Client ID to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchFundSummaryByClientIdAndDateRange(String clId, String startDate, String endDate) {
        try {
            logger.info("Fetching fund daily summary for Client ID: " + clId + " between " + startDate + " and " + endDate);

            String query = "SELECT cl_id, SUMMARY_DATE, LAST_ACTUAL_FUND, LAST_TOTAL_DELAY_FEES FROM fund_daily_summary " +
                          "WHERE cl_id = ? " +
                          "AND SUMMARY_DATE BETWEEN TO_DATE(?, 'DD-Mon-YYYY') AND TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY SUMMARY_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clId, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Fund daily summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund daily summary data found for Client ID: " + clId + " within date range");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund daily summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest fund daily summary by client ID (most recent date)
     * @param clId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchLatestFundSummaryByClientId(String clId) {
        try {
            logger.info("Fetching latest fund daily summary for Client ID: " + clId);

            String query = "SELECT cl_id, SUMMARY_DATE, LAST_ACTUAL_FUND, LAST_TOTAL_DELAY_FEES FROM fund_daily_summary " +
                          "WHERE cl_id = ? " +
                          "AND SUMMARY_DATE = (SELECT MAX(SUMMARY_DATE) FROM fund_daily_summary WHERE cl_id = ?)";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, clId, clId);

            if (!results.isEmpty()) {
                logger.info("Latest fund daily summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No latest fund daily summary data found for Client ID: " + clId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching latest fund daily summary data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all fund daily summaries by summary date (all clients)
     * @param summaryDate Summary date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchFundSummaryByDate(String summaryDate) {
        try {
            logger.info("Fetching fund daily summary for Summary Date: " + summaryDate);

            String query = "SELECT cl_id, SUMMARY_DATE, LAST_ACTUAL_FUND, LAST_TOTAL_DELAY_FEES FROM fund_daily_summary " +
                          "WHERE SUMMARY_DATE = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY cl_id";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, summaryDate);

            if (!results.isEmpty()) {
                logger.info("Fund daily summary data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No fund daily summary data found for Summary Date: " + summaryDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching fund daily summary data: " + e.getMessage(), e);
            return false;
        }
    }
}
