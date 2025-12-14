package com.example.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Utility class to read configuration from properties file
 * Implements Singleton pattern for centralized configuration management
 */
public class ConfigReader {

    private static final Logger logger = Logger.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    /**
     * Private constructor to implement Singleton pattern
     */
    private ConfigReader() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReader
     * @return ConfigReader instance
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Load properties from config file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
            logger.info("Configuration loaded successfully from: " + CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + e.getMessage());
            // Try loading from classpath as fallback
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) {
                    properties.load(input);
                    logger.info("Configuration loaded from classpath");
                }
            } catch (IOException ex) {
                logger.error("Failed to load from classpath: " + ex.getMessage());
            }
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
            return "";
        }
        // Handle property references like ${app.url}
        return resolvePropertyReferences(value);
    }

    /**
     * Get property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    /**
     * Resolve property references in values (e.g., ${app.url})
     * @param value Property value
     * @return Resolved value
     */
    private String resolvePropertyReferences(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        String result = value;
        int startIndex;
        while ((startIndex = result.indexOf("${")) != -1) {
            int endIndex = result.indexOf("}", startIndex);
            if (endIndex == -1) break;

            String refKey = result.substring(startIndex + 2, endIndex);
            String refValue = properties.getProperty(refKey, "");
            result = result.substring(0, startIndex) + refValue + result.substring(endIndex + 1);
        }
        return result;
    }

    // Application URLs
    public String getBaseUrl() {
        return getProperty("app.url");
    }

    public String getUrlEn() {
        return getProperty("app.url.en");
    }

    public String getUrlAr() {
        return getProperty("app.url.ar");
    }

    public String getLoginUrl() {
        return getProperty("app.login.url");
    }

    // Browser Configuration
    public String getDefaultBrowser() {
        return getProperty("browser.default", "CHROME");
    }

    public boolean isBrowserHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    public boolean shouldMaximizeBrowser() {
        return Boolean.parseBoolean(getProperty("browser.maximize", "true"));
    }

    public boolean shouldClearCookies() {
        return Boolean.parseBoolean(getProperty("browser.clear.cookies", "true"));
    }

    // Wait Times
    public int getImplicitWait() {
        return Integer.parseInt(getProperty("wait.implicit", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(getProperty("wait.explicit", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("wait.page.load", "30"));
    }

    public int getShortWait() {
        return Integer.parseInt(getProperty("wait.short", "5"));
    }

    public int getMediumWait() {
        return Integer.parseInt(getProperty("wait.medium", "10"));
    }

    public int getLongWait() {
        return Integer.parseInt(getProperty("wait.long", "20"));
    }

    // Screenshot Configuration
    public boolean shouldTakeScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    public boolean shouldTakeScreenshotOnSuccess() {
        return Boolean.parseBoolean(getProperty("screenshot.on.success", "false"));
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path", "screenshots/");
    }

    // Retry Configuration
    public boolean shouldRetryFailedTests() {
        return Boolean.parseBoolean(getProperty("retry.failed.tests", "true"));
    }

    public int getMaxRetryCount() {
        return Integer.parseInt(getProperty("retry.max.count", "2"));
    }

    // Parallel Execution
    public boolean isParallelExecutionEnabled() {
        return Boolean.parseBoolean(getProperty("parallel.execution", "false"));
    }

    public int getThreadCount() {
        return Integer.parseInt(getProperty("thread.count", "3"));
    }

    // Test Data Paths
    public String getExcelDataPath() {
        return getProperty("testdata.excel.path");
    }

    public String getJsonDataPath() {
        return getProperty("testdata.json.path");
    }

    // Environment
    public String getEnvironment() {
        return getProperty("environment", "UAT");
    }

    /**
     * Reload properties from file
     */
    public void reloadProperties() {
        logger.info("Reloading configuration...");
        loadProperties();
    }
}
