package com.example.dataBase.portfolio;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve End Price data from MDF_TIME_SERIES table
 * Contains fetch methods for COMPANY_CODE, TRADE_DATE, and Close price
 */
public class GetEndpriceData {

    private static final Logger logger = Logger.getLogger(GetEndpriceData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetEndpriceData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch all end price data ordered by TRADE_DATE DESC
     * @return true if data found, false otherwise
     */
    public boolean fetchAllEndprice() {
        try {
            logger.info("Fetching all end price data");

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                logger.info("End price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No end price data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching end price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch end price data up to a specific date
     * @param tradeDate Trade date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchEndpriceByDate(String tradeDate) {
        try {
            logger.info("Fetching end price data up to date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE TRADE_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, tradeDate);

            if (!results.isEmpty()) {
                logger.info("End price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No end price data found up to date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching end price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch end price data by Company Code
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchEndpriceByCompanyCode(String companyCode) {
        try {
            logger.info("Fetching end price data for company: " + companyCode);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode);

            if (!results.isEmpty()) {
                logger.info("End price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No end price data found for company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching end price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch end price data by Company Code and Date
     * @param companyCode Company code to query
     * @param tradeDate Trade date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchEndpriceByCompanyAndDate(String companyCode, String tradeDate) {
        try {
            logger.info("Fetching end price data for company: " + companyCode + " up to date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND TRADE_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, tradeDate);

            if (!results.isEmpty()) {
                logger.info("End price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No end price data found for company: " + companyCode + " up to date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching end price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch end price data by exact Trade Date
     * @param tradeDate Trade date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchEndpriceByExactDate(String tradeDate) {
        try {
            logger.info("Fetching end price data for exact date: " + tradeDate);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE TRUNC(TRADE_DATE) = TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY COMPANY_CODE";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, tradeDate);

            if (!results.isEmpty()) {
                logger.info("End price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No end price data found for exact date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching end price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest end price data by Company Code (most recent trade date)
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchLatestEndpriceByCompanyCode(String companyCode) {
        try {
            logger.info("Fetching latest end price data for company: " + companyCode);

            String query = "SELECT COMPANY_CODE, TRADE_DATE, CLOSE FROM MDF_TIME_SERIES " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND TRADE_DATE = (SELECT MAX(TRADE_DATE) FROM MDF_TIME_SERIES WHERE COMPANY_CODE = ?)";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, companyCode);

            if (!results.isEmpty()) {
                logger.info("Latest end price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No latest end price data found for company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching latest end price data: " + e.getMessage(), e);
            return false;
        }
    }
}
