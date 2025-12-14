package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Oracle Database connection using TNS
 */
public class OracleDBConnection {

    private static final Logger logger = LoggerFactory.getLogger(OracleDBConnection.class);
    private Connection connection;
    private String tnsEntry;
    private String username;
    private String password;

    /**
     * Constructor with TNS entry
     * @param tnsEntry TNS name or full TNS connection string
     * @param username Database username
     * @param password Database password
     */
    public OracleDBConnection(String tnsEntry, String username, String password) {
        this.tnsEntry = tnsEntry;
        this.username = username;
        this.password = password;
    }

    /**
     * Establishes connection to Oracle database using TNS
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection connect() throws SQLException {
        try {
            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Build connection URL with TNS
            String url = "jdbc:oracle:thin:@" + tnsEntry;

            logger.info("Connecting to Oracle database using TNS: {}", tnsEntry);
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Successfully connected to Oracle database");

            return connection;
        } catch (ClassNotFoundException e) {
            logger.error("Oracle JDBC Driver not found", e);
            throw new SQLException("Oracle JDBC Driver not found", e);
        }
    }

    /**
     * Get the current connection
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Execute a SELECT query and return results as list of maps
     * @param query SQL SELECT query
     * @return List of maps containing column name and value pairs
     * @throws SQLException if query execution fails
     */
    public List<Map<String, Object>> executeQuery(String query) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established. Please call connect() first.");
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }

            logger.info("Query executed successfully. Retrieved {} rows", results.size());
        } catch (SQLException e) {
            logger.error("Error executing query: {}", query, e);
            throw e;
        }

        return results;
    }

    /**
     * Execute a SELECT query with parameters and return results
     * @param query SQL SELECT query with ? placeholders
     * @param parameters Parameters to bind to the query
     * @return List of maps containing column name and value pairs
     * @throws SQLException if query execution fails
     */
    public List<Map<String, Object>> executeQueryWithParams(String query, Object... parameters) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established. Please call connect() first.");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        row.put(columnName, value);
                    }
                    results.add(row);
                }
            }

            logger.info("Parameterized query executed successfully. Retrieved {} rows", results.size());
        } catch (SQLException e) {
            logger.error("Error executing parameterized query: {}", query, e);
            throw e;
        }

        return results;
    }

    /**
     * Execute an UPDATE, INSERT, or DELETE statement
     * @param query SQL DML statement
     * @return Number of rows affected
     * @throws SQLException if execution fails
     */
    public int executeUpdate(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established. Please call connect() first.");
        }

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(query);
            logger.info("Update executed successfully. {} rows affected", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            logger.error("Error executing update: {}", query, e);
            throw e;
        }
    }

    /**
     * Execute an UPDATE, INSERT, or DELETE statement with parameters
     * @param query SQL DML statement with ? placeholders
     * @param parameters Parameters to bind to the query
     * @return Number of rows affected
     * @throws SQLException if execution fails
     */
    public int executeUpdateWithParams(String query, Object... parameters) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established. Please call connect() first.");
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Parameterized update executed successfully. {} rows affected", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            logger.error("Error executing parameterized update: {}", query, e);
            throw e;
        }
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.info("Database connection closed successfully");
                }
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }

    /**
     * Check if connection is active
     * @return true if connection is active, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("Error checking connection status", e);
            return false;
        }
    }
}
