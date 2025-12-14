package com.example.screensData.mcalc;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve Client Current Balance (CUR_BAL) from Sec_Clients_Balances table
 * Simplified to return only current balance information
 */
public class GetCashTodayData {

    private static final Logger logger = Logger.getLogger(GetCashTodayData.class);
    private OracleDBConnection dbConnection;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetCashTodayData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Fetch client current balance by NIN
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalancesByNin(String nin) {
        try {
            logger.info("Fetching current balance for NIN: " + nin);

            String query = "SELECT NIN, CUR_BAL FROM Sec_Clients_Balances WHERE nin = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                logger.info("Current balance data fetched successfully. Found " + results.size() + " record(s)");

                // Print retrieved values
                for (Map<String, Object> row : results) {
                    logger.info("NIN: " + row.get("NIN") + ", Current Balance: " + row.get("CUR_BAL"));
                }

                return true;
            } else {
                logger.warn("No current balance data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching current balance data: " + e.getMessage(), e);
            return false;
        }
    }
}
