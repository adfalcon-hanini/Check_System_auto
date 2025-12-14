package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonDataReader - Utility class to read test data from JSON files
 * Supports dynamic JSON data retrieval for data-driven testing
 */
public class JsonDataReader {

    private static final Logger logger = Logger.getLogger(JsonDataReader.class);
    private JsonObject jsonObject;
    private String filePath;

    /**
     * Constructor to load JSON file
     * @param filePath Path to JSON file
     */
    public JsonDataReader(String filePath) {
        this.filePath = filePath;
        loadJsonFile();
    }

    /**
     * Load JSON file
     */
    private void loadJsonFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            jsonObject = gson.fromJson(reader, JsonObject.class);
            logger.info("JSON file loaded successfully: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to load JSON file: " + e.getMessage());
        }
    }

    /**
     * Get string value by key
     * @param key JSON key
     * @return String value
     */
    public String getString(String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsString();
            }
            logger.warn("Key not found in JSON: " + key);
            return "";
        } catch (Exception e) {
            logger.error("Error reading string value: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get integer value by key
     * @param key JSON key
     * @return Integer value
     */
    public int getInt(String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsInt();
            }
            logger.warn("Key not found in JSON: " + key);
            return 0;
        } catch (Exception e) {
            logger.error("Error reading int value: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get boolean value by key
     * @param key JSON key
     * @return Boolean value
     */
    public boolean getBoolean(String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsBoolean();
            }
            logger.warn("Key not found in JSON: " + key);
            return false;
        } catch (Exception e) {
            logger.error("Error reading boolean value: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get JSON object by key
     * @param key JSON key
     * @return JsonObject
     */
    public JsonObject getJsonObject(String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsJsonObject();
            }
            logger.warn("JSON object not found for key: " + key);
            return new JsonObject();
        } catch (Exception e) {
            logger.error("Error reading JSON object: " + e.getMessage());
            return new JsonObject();
        }
    }

    /**
     * Get JSON array by key
     * @param key JSON key
     * @return JsonArray
     */
    public JsonArray getJsonArray(String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.get(key).getAsJsonArray();
            }
            logger.warn("JSON array not found for key: " + key);
            return new JsonArray();
        } catch (Exception e) {
            logger.error("Error reading JSON array: " + e.getMessage());
            return new JsonArray();
        }
    }

    /**
     * Get array data as list of maps
     * @param arrayKey Key for JSON array
     * @return List of maps containing array data
     */
    public List<Map<String, String>> getArrayDataAsMapList(String arrayKey) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            JsonArray jsonArray = getJsonArray(arrayKey);

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                Map<String, String> dataMap = new HashMap<>();

                for (String key : obj.keySet()) {
                    dataMap.put(key, obj.get(key).getAsString());
                }
                dataList.add(dataMap);
            }

            logger.info("Retrieved array data as map list: " + dataList.size() + " items");
        } catch (Exception e) {
            logger.error("Error converting array to map list: " + e.getMessage());
        }

        return dataList;
    }

    /**
     * Get nested value using dot notation (e.g., "user.credentials.username")
     * @param path Dot-separated path to value
     * @return String value
     */
    public String getNestedValue(String path) {
        try {
            String[] keys = path.split("\\.");
            JsonObject currentObj = jsonObject;

            for (int i = 0; i < keys.length - 1; i++) {
                currentObj = currentObj.get(keys[i]).getAsJsonObject();
            }

            return currentObj.get(keys[keys.length - 1]).getAsString();
        } catch (Exception e) {
            logger.error("Error reading nested value for path: " + path + " - " + e.getMessage());
            return "";
        }
    }

    /**
     * Check if key exists
     * @param key JSON key
     * @return true if exists, false otherwise
     */
    public boolean hasKey(String key) {
        return jsonObject.has(key);
    }

    /**
     * Get entire JSON object
     * @return JsonObject
     */
    public JsonObject getFullJsonObject() {
        return jsonObject;
    }

    /**
     * Reload JSON file
     */
    public void reload() {
        logger.info("Reloading JSON file...");
        loadJsonFile();
    }
}
