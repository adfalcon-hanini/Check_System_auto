package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * API Configuration Manager
 * Manages API configuration properties including endpoint URLs and session information
 * Automatically updates sessionID in properties file after each successful login
 */
public class APIConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(APIConfigManager.class);
    private static final String CONFIG_FILE = "api-config.properties";
    private static Properties properties;
    private static String configFilePath;

    static {
        loadProperties();
    }

    /**
     * Load properties from api-config.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            // Try to load from classpath first
            InputStream inputStream = APIConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

            if (inputStream != null) {
                properties.load(inputStream);

                // Get the actual file path for updates
                String classpathLocation = APIConfigManager.class.getClassLoader().getResource(CONFIG_FILE).getPath();

                // URL decode the path (fixes issues with spaces and special characters)
                try {
                    classpathLocation = java.net.URLDecoder.decode(classpathLocation, "UTF-8");
                } catch (Exception e) {
                    logger.warn("Failed to URL decode path: {}", e.getMessage());
                }

                // Clean up Windows path if needed
                if (classpathLocation.startsWith("/") && classpathLocation.contains(":")) {
                    classpathLocation = classpathLocation.substring(1);
                }

                // Determine the source file path
                // If running from target/classes, point to src/main/resources instead
                if (classpathLocation.contains("target\\classes") || classpathLocation.contains("target/classes")) {
                    // Calculate path to src/main/resources
                    String projectRoot = classpathLocation.substring(0, classpathLocation.indexOf("target"));
                    configFilePath = projectRoot + "src\\main\\resources\\" + CONFIG_FILE;
                    logger.info("Detected target/classes location. Using source file: {}", configFilePath);
                } else {
                    configFilePath = classpathLocation;
                }

                // Verify the file exists and is writable
                File configFile = new File(configFilePath);
                if (!configFile.exists()) {
                    logger.warn("Config file does not exist at: {}", configFilePath);
                    logger.warn("Will attempt to create it on first update");
                } else if (!configFile.canWrite()) {
                    logger.error("Config file is not writable: {}", configFilePath);
                } else {
                    logger.info("API configuration loaded successfully from: {}", configFilePath);
                }

                inputStream.close();
            } else {
                logger.error("Could not find {} in classpath", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to load API configuration: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reload properties from file
     */
    public static void reloadProperties() {
        try {
            // Reload directly from the config file path, not from classpath
            if (configFilePath != null && !configFilePath.isEmpty()) {
                File configFile = new File(configFilePath);
                if (configFile.exists()) {
                    logger.info("Reloading properties from: {}", configFilePath);
                    FileInputStream inputStream = new FileInputStream(configFile);
                    properties.load(inputStream);
                    inputStream.close();
                    logger.info("Properties reloaded successfully");
                } else {
                    logger.warn("Config file not found for reload: {}", configFilePath);
                    loadProperties(); // Fallback to normal load
                }
            } else {
                logger.warn("Config file path not set, using normal load");
                loadProperties(); // Fallback to normal load
            }
        } catch (IOException e) {
            logger.error("Failed to reload properties: {}", e.getMessage());
            loadProperties(); // Fallback to normal load
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }

    /**
     * Get property value with default value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Update property value and persist to file
     * @param key Property key
     * @param value New property value
     * @return true if update successful, false otherwise
     */
    public static boolean updateProperty(String key, String value) {
        try {
            logger.info("Attempting to update property '{}' to: {}", key, value);
            logger.info("Config file path: {}", configFilePath);

            // Verify file path is set
            if (configFilePath == null || configFilePath.isEmpty()) {
                logger.error("Config file path is null or empty!");
                return false;
            }

            // Check if file exists and create parent directories if needed
            File configFile = new File(configFilePath);
            File parentDir = configFile.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                logger.info("Creating parent directories: {}", parentDir.getAbsolutePath());
                parentDir.mkdirs();
            }

            // Update property in memory
            properties.setProperty(key, value);
            logger.info("Property updated in memory");

            // Save to file
            logger.info("Writing to file: {}", configFile.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(configFile);
            properties.store(outputStream, "API Configuration - Updated: " + new java.util.Date());
            outputStream.flush();
            outputStream.close();
            logger.info("File written successfully");

            // Verify the write was successful by reading back
            Properties verifyProps = new Properties();
            FileInputStream verifyStream = new FileInputStream(configFile);
            verifyProps.load(verifyStream);
            verifyStream.close();

            String verifyValue = verifyProps.getProperty(key);
            if (value.equals(verifyValue)) {
                logger.info("✓ Property '{}' updated and verified successfully", key);
                logger.info("✓ File location: {}", configFile.getAbsolutePath());
                return true;
            } else {
                logger.error("✗ Verification failed! Expected: {}, Got: {}", value, verifyValue);
                return false;
            }

        } catch (IOException e) {
            logger.error("Failed to update property '{}': {}", key, e.getMessage());
            logger.error("Exception details: ", e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error updating property '{}': {}", key, e.getMessage());
            logger.error("Exception details: ", e);
            return false;
        }
    }

    /**
     * Get API base URL
     * @return Base URL
     */
    public static String getBaseUrl() {
        return getProperty("api.baseUrl", "https://devuat.thegroup.com.qa");
    }

    /**
     * Get API endpoint URL
     * @return Endpoint URL
     */
    public static String getEndpointURL() {
        return getProperty("api.endpoint.url", "https://devuat.thegroup.com.qa/jetrade/process");
    }

    /**
     * Get current sessionID from configuration
     * @return SessionID or empty string if not set
     */
    public static String getSessionID() {
        String sessionID = getProperty("api.sessionID", "");
        if (sessionID.isEmpty()) {
            logger.warn("SessionID is not set in configuration");
        }
        return sessionID;
    }

    /**
     * Update sessionID in configuration file
     * This method is automatically called after successful login
     * @param sessionID New sessionID value
     * @return true if update successful, false otherwise
     */
    public static boolean updateSessionID(String sessionID) {
        if (sessionID == null || sessionID.isEmpty()) {
            logger.warn("Attempted to update sessionID with null or empty value");
            return false;
        }

        boolean updated = updateProperty("api.sessionID", sessionID);
        if (updated) {
            logger.info("SessionID updated successfully in configuration file");
        }
        return updated;
    }

    /**
     * Clear sessionID from configuration
     * @return true if cleared successfully, false otherwise
     */
    public static boolean clearSessionID() {
        logger.info("Clearing sessionID from configuration");
        return updateProperty("api.sessionID", "");
    }

    /**
     * Get API timeout
     * @return Timeout in milliseconds
     */
    public static int getTimeout() {
        String timeout = getProperty("api.timeout", "30000");
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            logger.warn("Invalid timeout value, using default: 30000");
            return 30000;
        }
    }

    /**
     * Get retry attempts
     * @return Number of retry attempts
     */
    public static int getRetryAttempts() {
        String retries = getProperty("api.retryAttempts", "3");
        try {
            return Integer.parseInt(retries);
        } catch (NumberFormatException e) {
            logger.warn("Invalid retry attempts value, using default: 3");
            return 3;
        }
    }

    /**
     * Print all configuration properties
     */
    public static void printConfiguration() {
        logger.info("========== API Configuration ==========");
        properties.forEach((key, value) -> {
            // Mask sessionID for security
            if (key.equals("api.sessionID") && !value.toString().isEmpty()) {
                logger.info("{} = {}", key, "****" + value.toString().substring(Math.max(0, value.toString().length() - 4)));
            } else {
                logger.info("{} = {}", key, value);
            }
        });
        logger.info("======================================");
    }

    /**
     * Get current date in dd-MM-yyyy format
     * @return Current date as String
     */
    public static String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new java.util.Date());
    }
}
