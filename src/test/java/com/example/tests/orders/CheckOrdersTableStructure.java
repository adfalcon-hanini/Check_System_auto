package com.example.tests.orders;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CheckOrdersTableStructure {

    private static final Logger logger = Logger.getLogger(CheckOrdersTableStructure.class);
    private OracleDBConnection dbConnection;

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test
    public void checkTableStructure() {
        try {
            logger.info("Checking SEC_ORDERS table structure");

            // Try to get any existing records to see column names
            String query = "SELECT * FROM SEC_ORDERS WHERE ROWNUM = 1";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                logger.info("Table columns found:");
                Map<String, Object> firstRow = results.get(0);
                for (String columnName : firstRow.keySet()) {
                    logger.info("  - " + columnName);
                    System.out.println("Column: " + columnName);
                }
            } else {
                logger.warn("No records found in SEC_ORDERS table");
            }

        } catch (SQLException e) {
            logger.error("Error checking table structure: " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardownDatabase() {
        try {
            if (dbConnection != null) {
                logger.info("Closing database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection: " + e.getMessage(), e);
        }
    }
}
