package com.example.screensData.portfolio;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Equity Shares data from SEC_EQ_SHARES table
 * Contains only 4 fetch methods
 */
public class GetEqSharesData {

    private static final Logger logger = Logger.getLogger(GetEqSharesData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetEqSharesData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch equity shares data by NIN (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchEqSharesByNin(String nin) {
        try {
            logger.info("Fetching equity shares data for NIN: " + nin);

            String query = "SELECT * FROM SEC_EQ_SHARES " +
                          "WHERE NIN = ? " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Equity shares data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No equity shares data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching equity shares data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch equity shares data by NIN and Company Code (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchEqSharesByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching equity shares data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT * FROM SEC_EQ_SHARES " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0 " +
                          "ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty()) {
                logger.info("Equity shares data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No equity shares data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching equity shares data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch equity shares data by NIN and Date (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param date Date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchEqSharesByNinAndDate(String nin, String date) {
        try {
            logger.info("Fetching equity shares data for NIN: " + nin + " and Date: " + date);

            String query = "SELECT * FROM SEC_EQ_SHARES " +
                          "WHERE NIN = ? AND TRUNC(TIME_STAMP) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, date);

            if (!results.isEmpty()) {
                logger.info("Equity shares data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No equity shares data found for NIN: " + nin + " and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching equity shares data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch equity shares data by NIN, Company Code, and Date (excludes records with 0 or null SHARES_COUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @param date Date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchEqSharesByNinAndCompanyAndDate(String nin, String companyCode, String date) {
        try {
            logger.info("Fetching equity shares data for NIN: " + nin + ", Company: " + companyCode + ", and Date: " + date);

            String query = "SELECT * FROM SEC_EQ_SHARES " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(TIME_STAMP) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND SHARES_COUNT IS NOT NULL AND SHARES_COUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, date);

            if (!results.isEmpty()) {
                logger.info("Equity shares data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No equity shares data found for NIN: " + nin + ", Company: " + companyCode + ", and Date: " + date);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching equity shares data: " + e.getMessage(), e);
            return false;
        }
    }
}
