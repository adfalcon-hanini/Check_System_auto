package com.example.dataproviders;

import com.example.utils.ConfigReader;
import com.example.utils.ExcelDataReader;
import com.example.utils.JsonDataReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * TestDataProvider - Provides test data from various sources
 * Supports JSON and Excel data sources for data-driven testing
 */
public class TestDataProvider {

    private static final Logger logger = Logger.getLogger(TestDataProvider.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Login data from JSON
     * @return Object[][] test data
     */
    @DataProvider(name = "loginDataFromJson")
    public static Object[][] getLoginDataFromJson() {
        JsonDataReader jsonReader = new JsonDataReader(config.getJsonDataPath());
        JsonArray loginData = jsonReader.getJsonArray("loginData");

        Object[][] data = new Object[loginData.size()][3];

        for (int i = 0; i < loginData.size(); i++) {
            JsonObject loginObj = loginData.get(i).getAsJsonObject();
            data[i][0] = loginObj.get("username").getAsString();
            data[i][1] = loginObj.get("password").getAsString();
            data[i][2] = loginObj.get("expectedResult").getAsString();
        }

        logger.info("Loaded " + data.length + " login test data sets from JSON");
        return data;
    }

    /**
     * Order data from JSON
     * @return Object[][] test data
     */
    @DataProvider(name = "orderDataFromJson")
    public static Object[][] getOrderDataFromJson() {
        JsonDataReader jsonReader = new JsonDataReader(config.getJsonDataPath());
        JsonArray orderData = jsonReader.getJsonArray("orderData");

        Object[][] data = new Object[orderData.size()][3];

        for (int i = 0; i < orderData.size(); i++) {
            JsonObject orderObj = orderData.get(i).getAsJsonObject();
            data[i][0] = orderObj.get("orderType").getAsString();
            data[i][1] = orderObj.get("quantity").getAsString();
            data[i][2] = orderObj.get("price").getAsString();
        }

        logger.info("Loaded " + data.length + " order test data sets from JSON");
        return data;
    }

    /**
     * Get user credentials by role from JSON
     * @return Object[][] test data
     */
    @DataProvider(name = "usersByRole")
    public static Object[][] getUsersByRole() {
        JsonDataReader jsonReader = new JsonDataReader(config.getJsonDataPath());
        JsonObject users = jsonReader.getJsonObject("users");

        List<Object[]> dataList = new ArrayList<>();

        for (String role : users.keySet()) {
            JsonObject user = users.get(role).getAsJsonObject();
            String username = user.get("username").getAsString();
            String password = user.get("password").getAsString();
            String userRole = user.get("role").getAsString();

            dataList.add(new Object[]{username, password, userRole});
        }

        Object[][] data = new Object[dataList.size()][3];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        logger.info("Loaded " + data.length + " user credentials from JSON");
        return data;
    }

    /**
     * Generic data provider from Excel
     * @param sheetName Sheet name in Excel file
     * @return Object[][] test data
     */
    public static Object[][] getDataFromExcel(String sheetName) {
        try {
            ExcelDataReader excelReader = new ExcelDataReader(config.getExcelDataPath());
            excelReader.setSheet(sheetName);
            Object[][] data = excelReader.getAllData();
            excelReader.close();

            logger.info("Loaded test data from Excel sheet: " + sheetName);
            return data;
        } catch (Exception e) {
            logger.error("Failed to load data from Excel: " + e.getMessage());
            return new Object[0][0];
        }
    }

    /**
     * Get data as map from Excel for flexible access
     * @param sheetName Sheet name
     * @return Iterator of Object arrays containing maps
     */
    public static Iterator<Object[]> getDataAsMapFromExcel(String sheetName) {
        try {
            ExcelDataReader excelReader = new ExcelDataReader(config.getExcelDataPath());
            excelReader.setSheet(sheetName);
            List<Map<String, String>> dataList = excelReader.getDataAsMapList();
            excelReader.close();

            List<Object[]> objectList = new ArrayList<>();
            for (Map<String, String> dataMap : dataList) {
                objectList.add(new Object[]{dataMap});
            }

            logger.info("Loaded map data from Excel sheet: " + sheetName);
            return objectList.iterator();

        } catch (Exception e) {
            logger.error("Failed to load map data from Excel: " + e.getMessage());
            return new ArrayList<Object[]>().iterator();
        }
    }

    /**
     * Generic JSON data provider
     * @param arrayKey Key for JSON array
     * @return Iterator of Object arrays
     */
    public static Iterator<Object[]> getDataFromJson(String arrayKey) {
        JsonDataReader jsonReader = new JsonDataReader(config.getJsonDataPath());
        List<Map<String, String>> dataList = jsonReader.getArrayDataAsMapList(arrayKey);

        List<Object[]> objectList = new ArrayList<>();
        for (Map<String, String> dataMap : dataList) {
            objectList.add(new Object[]{dataMap});
        }

        logger.info("Loaded data from JSON array: " + arrayKey);
        return objectList.iterator();
    }

    /**
     * Combine multiple data sources
     * @param jsonArray JSON array key
     * @param excelSheet Excel sheet name
     * @return Combined data
     */
    public static Object[][] getCombinedData(String jsonArray, String excelSheet) {
        List<Object[]> combinedData = new ArrayList<>();

        // Get JSON data
        JsonDataReader jsonReader = new JsonDataReader(config.getJsonDataPath());
        List<Map<String, String>> jsonData = jsonReader.getArrayDataAsMapList(jsonArray);
        for (Map<String, String> dataMap : jsonData) {
            combinedData.add(new Object[]{dataMap});
        }

        // Get Excel data if file exists
        try {
            ExcelDataReader excelReader = new ExcelDataReader(config.getExcelDataPath());
            excelReader.setSheet(excelSheet);
            List<Map<String, String>> excelData = excelReader.getDataAsMapList();
            for (Map<String, String> dataMap : excelData) {
                combinedData.add(new Object[]{dataMap});
            }
            excelReader.close();
        } catch (Exception e) {
            logger.warn("Excel data not available, using JSON only: " + e.getMessage());
        }

        Object[][] data = new Object[combinedData.size()][1];
        for (int i = 0; i < combinedData.size(); i++) {
            data[i] = combinedData.get(i);
        }

        logger.info("Loaded combined data: " + data.length + " records");
        return data;
    }
}
