package com.example.screensData.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Open Price data from MDF_TIME_SERIES table
 * Contains fetch methods for COMPANY_CODE, TRADE_DATE, and Close price
 */
public class GetOpenPriceData {

    private static final Logger logger = Logger.getLogger(GetOpenPriceData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetOpenPriceData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch open price data by Company Code and Date (before specified date)
     * @param companyCode Company code to query
     * @param tradeDate Trade date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchOpenPriceByCompanyAndDate(String companyCode, String tradeDate) {
        try {
            logger.info("Fetching open price data for company: " + companyCode + " before date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, tradeDate);

            if (!results.isEmpty()) {
                logger.info("Open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No open price data found for company: " + companyCode + " before date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching open price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch open price data by Company Code (all dates)
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchOpenPriceByCompanyCode(String companyCode) {
        try {
            logger.info("Fetching open price data for company: " + companyCode);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode);

            if (!results.isEmpty()) {
                logger.info("Open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No open price data found for company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching open price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all open price data before a specific date
     * @param tradeDate Trade date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchOpenPriceBeforeDate(String tradeDate) {
        try {
            logger.info("Fetching open price data before date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, tradeDate);

            if (!results.isEmpty()) {
                logger.info("Open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No open price data found before date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching open price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all open price data
     * @return true if data found, false otherwise
     */
    public boolean fetchAllOpenPrice() {
        try {
            logger.info("Fetching all open price data");

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                logger.info("Open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No open price data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching open price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch open price data by Company Code between two dates
     * @param companyCode Company code to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchOpenPriceByCompanyAndDateRange(String companyCode, String startDate, String endDate) {
        try {
            logger.info("Fetching open price data for company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND TRADE_DATE >= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No open price data found for company: " + companyCode + " between " + startDate + " and " + endDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching open price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest open price data by Company Code (most recent trade date before specified date)
     * @param companyCode Company code to query
     * @param tradeDate Trade date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchLatestOpenPriceByCompanyBeforeDate(String companyCode, String tradeDate) {
        try {
            logger.info("Fetching latest open price data for company: " + companyCode + " before date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND TRADE_DATE = (SELECT MAX(TRADE_DATE) FROM MDF_TIME_SERIES " +
                          "                   WHERE COMPANY_CODE = ? AND TRADE_DATE < TO_DATE(?, 'DD-Mon-YYYY'))";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, tradeDate, companyCode, tradeDate);

            if (!results.isEmpty()) {
                logger.info("Latest open price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No latest open price data found for company: " + companyCode + " before date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching latest open price data: " + e.getMessage(), e);
            return false;
        }
    }
}
