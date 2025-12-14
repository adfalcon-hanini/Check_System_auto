package com.example.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelDataReader - Utility class to read test data from Excel files
 * Supports data-driven testing with dynamic data retrieval
 */
public class ExcelDataReader {

    private static final Logger logger = Logger.getLogger(ExcelDataReader.class);
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    /**
     * Constructor to initialize Excel file
     * @param filePath Path to Excel file
     */
    public ExcelDataReader(String filePath) {
        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            logger.info("Excel file loaded successfully: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to load Excel file: " + e.getMessage());
        }
    }

    /**
     * Get sheet by name
     * @param sheetName Sheet name
     */
    public void setSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            logger.error("Sheet not found: " + sheetName);
        } else {
            logger.info("Sheet selected: " + sheetName);
        }
    }

    /**
     * Get sheet by index
     * @param sheetIndex Sheet index (0-based)
     */
    public void setSheet(int sheetIndex) {
        sheet = workbook.getSheetAt(sheetIndex);
        logger.info("Sheet selected at index: " + sheetIndex);
    }

    /**
     * Get cell data as string
     * @param rowNum Row number (0-based)
     * @param colNum Column number (0-based)
     * @return Cell value as string
     */
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) return "";

            Cell cell = row.getCell(colNum);
            if (cell == null) return "";

            return getCellValueAsString(cell);
        } catch (Exception e) {
            logger.error("Error reading cell data: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get cell data by column name
     * @param rowNum Row number (0-based)
     * @param columnName Column name (from header row)
     * @return Cell value as string
     */
    public String getCellData(int rowNum, String columnName) {
        try {
            int colNum = getColumnIndex(columnName);
            if (colNum == -1) {
                logger.error("Column not found: " + columnName);
                return "";
            }
            return getCellData(rowNum, colNum);
        } catch (Exception e) {
            logger.error("Error reading cell data by column name: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get column index by column name
     * @param columnName Column name
     * @return Column index or -1 if not found
     */
    private int getColumnIndex(String columnName) {
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && getCellValueAsString(cell).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get cell value as string regardless of type
     * @param cell Excel cell
     * @return Cell value as string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get row count (excluding header)
     * @return Row count
     */
    public int getRowCount() {
        return sheet.getLastRowNum();
    }

    /**
     * Get column count
     * @return Column count
     */
    public int getColumnCount() {
        Row row = sheet.getRow(0);
        return row != null ? row.getLastCellNum() : 0;
    }

    /**
     * Get all data from sheet as 2D array
     * @return 2D array of test data
     */
    public Object[][] getAllData() {
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        Object[][] data = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = getCellData(i, j);
            }
        }

        logger.info("Retrieved all data: " + rowCount + " rows, " + colCount + " columns");
        return data;
    }

    /**
     * Get data as list of maps (column name -> value)
     * @return List of data maps
     */
    public List<Map<String, String>> getDataAsMapList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        // Get header row
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < colCount; i++) {
            headers.add(getCellValueAsString(headerRow.getCell(i)));
        }

        // Get data rows
        for (int i = 1; i <= rowCount; i++) {
            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < colCount; j++) {
                rowData.put(headers.get(j), getCellData(i, j));
            }
            dataList.add(rowData);
        }

        logger.info("Retrieved data as map list: " + dataList.size() + " rows");
        return dataList;
    }

    /**
     * Get specific rows based on filter
     * @param columnName Column name to filter
     * @param value Value to match
     * @return Filtered data as map list
     */
    public List<Map<String, String>> getFilteredData(String columnName, String value) {
        List<Map<String, String>> allData = getDataAsMapList();
        List<Map<String, String>> filteredData = new ArrayList<>();

        for (Map<String, String> row : allData) {
            if (value.equals(row.get(columnName))) {
                filteredData.add(row);
            }
        }

        logger.info("Filtered data: " + filteredData.size() + " rows matching " + columnName + "=" + value);
        return filteredData;
    }

    /**
     * Close workbook
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
                logger.info("Excel file closed");
            }
        } catch (IOException e) {
            logger.error("Error closing Excel file: " + e.getMessage());
        }
    }
}
