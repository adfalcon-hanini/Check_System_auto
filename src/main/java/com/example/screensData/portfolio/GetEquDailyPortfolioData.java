package com.example.screensData.portfolio;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Daily Portfolio data from SEC_EQU_DAILY_PORTFOLIO table
 * Contains only 5 fetch methods
 */
public class GetEquDailyPortfolioData {

    private static final Logger logger = Logger.getLogger(GetEquDailyPortfolioData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetEquDailyPortfolioData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch daily portfolio data by NIN (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchEquDailyPortfolioByNin(String nin) {
        try {
            logger.info("Fetching daily portfolio data for NIN: " + nin);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                          "ORDER BY PORTFOLIO_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch daily portfolio data by NIN and Company Code (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchEquDailyPortfolioByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching daily portfolio data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                          "ORDER BY PORTFOLIO_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty()) {
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch daily portfolio data by NIN and Date (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param date Date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchEquDailyPortfolioByNinAndDate(String nin, String date) {
        try {
            logger.info("Fetching daily portfolio data for NIN: " + nin + " and Date: " + date);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? AND TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, date);

            if (!results.isEmpty()) {
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for NIN: " + nin + " and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch daily portfolio data by NIN, Company Code, and Date (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @param date Date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchEquDailyPortfolioByNinAndCompanyAndDate(String nin, String companyCode, String date) {
        try {
            logger.info("Fetching daily portfolio data for NIN: " + nin + ", Company: " + companyCode + ", and Date: " + date);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, date);

            if (!results.isEmpty()) {
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for NIN: " + nin + ", Company: " + companyCode + ", and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch daily portfolio data by Portfolio Date only (excludes records with 0 or null SHARES_COUNT)
     * @param portfolioDate Portfolio date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchEquDailyPortfolioByPortfolioDate(String portfolioDate) {
        try {
            logger.info("Fetching daily portfolio data for Portfolio Date: " + portfolioDate);

            String query = "SELECT * FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE TRUNC(PORTFOLIO_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, portfolioDate);

            if (!results.isEmpty()) {
                logger.info("Daily portfolio data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No daily portfolio data found for Portfolio Date: " + portfolioDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching daily portfolio data: " + e.getMessage(), e);
            return false;
        }
    }
}
