package com.example.dataBase.agm;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store data from Mycalculator_Study table
 * Returns: Selected columns - NIN, TRNX_DATE, SHARES_VALUE, CASH, DIVIDEND, ASSETS,
 *          WITHDRAW, DEPOSIT, PROFIT_LOSS, FUND_AMOUNT, FEES_AMOUNT, BONUS
 * Ordered by TIME_STAMP DESC
 */
public class GetMyCalculatorStudyData {

    private static final Logger logger = Logger.getLogger(GetMyCalculatorStudyData.class);
    private OracleDBConnection dbConnection;

    // Column list for queries
    private static final String COLUMN_LIST = "NIN, TRNX_DATE, SHARES_VALUE, CASH, DIVIDEND, ASSETS, " +
                                               "WITHDRAW, DEPOSIT, PROFIT_LOSS, FUND_AMOUNT, FEES_AMOUNT, BONUS";

    // Store all rows
    private List<Map<String, Object>> allData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetMyCalculatorStudyData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allData = new ArrayList<>();
    }

    /**
     * Fetch all data from Mycalculator_Study table
     * @return true if data found, false otherwise
     */
    public boolean fetchAllData() {
        try {
            logger.info("Fetching all data from Mycalculator_Study table");

            String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allData = results;
                logger.info("Mycalculator_Study data fetched successfully. Found " + results.size() + " record(s)");
                printAllData();
                return true;
            } else {
                logger.warn("No data found in Mycalculator_Study table");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Mycalculator_Study data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch data by specific column value
     * @param columnName Column name to filter by
     * @param value Value to search for
     * @return true if data found, false otherwise
     */
    public boolean fetchDataByColumn(String columnName, Object value) {
        try {
            logger.info("Fetching Mycalculator_Study data where " + columnName + " = " + value);

            String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study WHERE " + columnName + " = ? ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, value);

            if (!results.isEmpty()) {
                allData = results;
                logger.info("Mycalculator_Study data fetched successfully. Found " + results.size() + " record(s)");
                printAllData();
                return true;
            } else {
                logger.warn("No data found in Mycalculator_Study table for " + columnName + " = " + value);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Mycalculator_Study data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch top N records
     * @param limit Number of records to fetch
     * @return true if data found, false otherwise
     */
    public boolean fetchTopNRecords(int limit) {
        try {
            logger.info("Fetching top " + limit + " records from Mycalculator_Study table");

            String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study WHERE ROWNUM <= ? ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, limit);

            if (!results.isEmpty()) {
                allData = results;
                logger.info("Mycalculator_Study data fetched successfully. Found " + results.size() + " record(s)");
                printAllData();
                return true;
            } else {
                logger.warn("No data found in Mycalculator_Study table");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Mycalculator_Study data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch data by NIN (National Identification Number)
     * @param nin NIN to search for
     * @return true if data found, false otherwise
     */
    public boolean fetchDataByNIN(String nin) {
        try {
            logger.info("Fetching Mycalculator_Study data for NIN: " + nin);

            String query = "SELECT " + COLUMN_LIST + " FROM Mycalculator_Study WHERE NIN = ? ORDER BY TIME_STAMP DESC";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                allData = results;
                logger.info("Mycalculator_Study data fetched successfully for NIN " + nin + ". Found " + results.size() + " record(s)");
                printAllData();
                return true;
            } else {
                logger.warn("No data found in Mycalculator_Study table for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching Mycalculator_Study data by NIN: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get all data records
     * @return List of all data rows
     */
    public List<Map<String, Object>> getAllRecords() {
        return allData;
    }

    /**
     * Get record by index
     * @param index Index of the record
     * @return Map containing data for that index
     */
    public Map<String, Object> getRecordByIndex(int index) {
        if (index >= 0 && index < allData.size()) {
            return allData.get(index);
        }
        return null;
    }

    /**
     * Get total number of records
     * @return Number of records
     */
    public int getRecordCount() {
        return allData.size();
    }

    /**
     * Get value from first record by column name
     * @param columnName Column name
     * @return Value of the column in first record
     */
    public Object getValueFromFirstRecord(String columnName) {
        if (!allData.isEmpty() && allData.get(0).containsKey(columnName)) {
            return allData.get(0).get(columnName);
        }
        return null;
    }

    /**
     * Print all records in a formatted table
     */
    public void printAllData() {
        System.out.println("\n" + "=".repeat(150));
        System.out.println("MYCALCULATOR_STUDY DATA - Total Records: " + allData.size());
        System.out.println("=".repeat(150));

        if (allData.isEmpty()) {
            System.out.println("No data found");
        } else {
            // Get column names from first record
            Map<String, Object> firstRecord = allData.get(0);
            List<String> columnNames = new ArrayList<>(firstRecord.keySet());

            // Print header
            for (String columnName : columnNames) {
                System.out.printf("%-25s | ", columnName);
            }
            System.out.println();
            System.out.println("-".repeat(150));

            // Print data rows (limit to first 20 for readability)
            int displayLimit = Math.min(allData.size(), 20);
            for (int i = 0; i < displayLimit; i++) {
                Map<String, Object> record = allData.get(i);
                for (String columnName : columnNames) {
                    Object value = record.get(columnName);
                    String displayValue = value != null ? value.toString() : "";
                    // Truncate long values
                    if (displayValue.length() > 23) {
                        displayValue = displayValue.substring(0, 20) + "...";
                    }
                    System.out.printf("%-25s | ", displayValue);
                }
                System.out.println();
            }

            if (allData.size() > 20) {
                System.out.println("\nShowing first 20 of " + allData.size() + " records");
            }
        }

        System.out.println("=".repeat(150));
    }

    /**
     * Print specific record by index
     * @param index Index of the record to print
     */
    public void printRecord(int index) {
        if (index >= 0 && index < allData.size()) {
            System.out.println("\n========== Record " + index + " ==========");
            Map<String, Object> record = allData.get(index);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("==============================");
        } else {
            System.out.println("Invalid index: " + index);
        }
    }
}
