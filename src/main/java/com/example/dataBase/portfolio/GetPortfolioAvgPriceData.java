package com.example.dataBase.portfolio;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Portfolio Average Price data from SEC_PORTFOLIO_AVG_PRICE table
 * Contains only 4 fetch methods
 */
public class GetPortfolioAvgPriceData {

    private static final Logger logger = Logger.getLogger(GetPortfolioAvgPriceData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetPortfolioAvgPriceData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch portfolio average price data by NIN (excludes records with 0 or null AVG_PRICE and AMOUNT)
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioAvgPriceByNin(String nin) {
        try {
            logger.info("Fetching portfolio average price data for NIN: " + nin);

            String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                          "WHERE NIN = ? " +
                          "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                          "AND AMOUNT IS NOT NULL AND AMOUNT != 0 " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Portfolio average price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio average price data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio average price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio average price data by NIN and Company Code (excludes records with 0 or null AVG_PRICE and AMOUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioAvgPriceByNinAndCompany(String nin, String companyCode) {
        try {
            logger.info("Fetching portfolio average price data for NIN: " + nin + " and Company: " + companyCode);

            String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? " +
                          "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                          "AND AMOUNT IS NOT NULL AND AMOUNT != 0 " +
                          "ORDER BY TRADE_DATE DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode);

            if (!results.isEmpty()) {
                logger.info("Portfolio average price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio average price data found for NIN: " + nin + " and Company: " + companyCode);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio average price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio average price data by NIN and Trade Date (excludes records with 0 or null AVG_PRICE and AMOUNT)
     * @param nin NIN to query
     * @param tradeDate Trade date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioAvgPriceByNinAndDate(String nin, String tradeDate) {
        try {
            logger.info("Fetching portfolio average price data for NIN: " + nin + " and Trade Date: " + tradeDate);

            String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                          "WHERE NIN = ? AND TRUNC(TRADE_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                          "AND AMOUNT IS NOT NULL AND AMOUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, tradeDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio average price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio average price data found for NIN: " + nin + " and Trade Date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio average price data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch portfolio average price data by NIN, Company Code, and Trade Date (excludes records with 0 or null AVG_PRICE and AMOUNT)
     * @param nin NIN to query
     * @param companyCode Company code to query
     * @param tradeDate Trade date (format: YYYY-MM-DD)
     * @return true if data found, false otherwise
     */
    public boolean fetchPortfolioAvgPriceByNinAndCompanyAndDate(String nin, String companyCode, String tradeDate) {
        try {
            logger.info("Fetching portfolio average price data for NIN: " + nin + ", Company: " + companyCode + ", and Trade Date: " + tradeDate);

            String query = "SELECT * FROM SEC_PORTFOLIO_AVG_PRICE " +
                          "WHERE NIN = ? AND COMPANY_CODE = ? AND TRUNC(TRADE_DATE) = TO_DATE(?, 'YYYY-MM-DD') " +
                          "AND AVG_PRICE IS NOT NULL AND AVG_PRICE != 0 " +
                          "AND AMOUNT IS NOT NULL AND AMOUNT != 0";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin, companyCode, tradeDate);

            if (!results.isEmpty()) {
                logger.info("Portfolio average price data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No portfolio average price data found for NIN: " + nin + ", Company: " + companyCode + ", and Trade Date: " + tradeDate);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching portfolio average price data: " + e.getMessage(), e);
            return false;
        }
    }
}
