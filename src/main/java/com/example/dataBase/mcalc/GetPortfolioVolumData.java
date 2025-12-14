package com.example.dataBase.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Portfolio Volume data from SEC_EQU_DAILY_PORTFOLIO table
 * Contains fetch methods for NIN, PORTFOLIO_DATE, SHARES_COUNT, and COMPANY_CODE
 */
public class GetPortfolioVolumData {

    private static final Logger logger = Logger.getLogger(GetPortfolioVolumData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetPortfolioVolumData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch portfolio volume data by NIN, Company Code, and Date (up to specified date)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @param portfolioDate Portfolio date in format 'DD-Mon-YYYY' (e.g., '25-Nov-2025')
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByNinAndCompanyAndDate(String nin, String companyCode, String portfolioDate) {
        try {
            logger.info("Fetching portfolio volume data for NIN: " + nin + ", Company: " + companyCode + ", up to Date: " + portfolioDate);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND COMPANY_CODE = ? " +
                          "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY PORTFOLIO_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, portfolioDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for NIN: " + nin + ", Company: " + companyCode + ", up to Date: " + portfolioDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio volume data by NIN and Company Code (all dates)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching portfolio volume data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND COMPANY_CODE = ? " +
                          "ORDER BY PORTFOLIO_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio volume data by NIN (all companies and dates)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByNin(String nin) {
        try {
            logger.info("Fetching portfolio volume data for NIN: " + nin);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "ORDER BY PORTFOLIO_DATE DESC, COMPANY_CODE";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio volume data by NIN and Date (all companies up to specified date)
     * @param nin NIN to query
     * @param portfolioDate Portfolio date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByNinAndDate(String nin, String portfolioDate) {
        try {
            logger.info("Fetching portfolio volume data for NIN: " + nin + " up to Date: " + portfolioDate);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY PORTFOLIO_DATE DESC, COMPANY_CODE";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, portfolioDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for NIN: " + nin + " up to Date: " + portfolioDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio volume data by Company Code and Date (all NIPs up to specified date)
     * @param companyCode Company code to query
     * @param portfolioDate Portfolio date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByCompanyAndDate(String companyCode, String portfolioDate) {
        try {
            logger.info("Fetching portfolio volume data for Company: " + companyCode + " up to Date: " + portfolioDate);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE COMPANY_CODE = ? " +
                          "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY PORTFOLIO_DATE DESC, NIN";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, companyCode, portfolioDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for Company: " + companyCode + " up to Date: " + portfolioDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch latest portfolio volume data by NIN and Company Code (most recent date)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchLatestPortfolioVolumByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching latest portfolio volume data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND COMPANY_CODE = ? " +
                          "AND PORTFOLIO_DATE = (SELECT MAX(PORTFOLIO_DATE) FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "                      WHERE NIN = ? AND COMPANY_CODE = ?)";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, nin, companyCode);

            if (!results.isEmpty()) {
                logger.info("Latest portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No latest portfolio volume data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching latest portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio volume data by NIN, Company Code, and Date Range
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @param startDate Start date in format 'DD-Mon-YYYY'
     * @param endDate End date in format 'DD-Mon-YYYY'
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioVolumByNinAndCompanyAndDateRange(String nin, String companyCode, String startDate, String endDate) {
        try {
            logger.info("Fetching portfolio volume data for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);

            String query = "SELECT NIN, PORTFOLIO_DATE, SHARES_COUNT, COMPANY_CODE FROM SEC_EQU_DAILY_PORTFOLIO " +
                          "WHERE NIN = ? " +
                          "AND COMPANY_CODE = ? " +
                          "AND PORTFOLIO_DATE >= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "AND PORTFOLIO_DATE <= TO_DATE(?, 'DD-Mon-YYYY') " +
                          "ORDER BY PORTFOLIO_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, startDate, endDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio volume data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio volume data found for NIN: " + nin + ", Company: " + companyCode + " between " + startDate + " and " + endDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio volume data: " + e.getMessage(), e);
            return false;
        }
    }
}
