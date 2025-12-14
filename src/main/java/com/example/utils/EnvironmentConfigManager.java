package com.example.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * EnvironmentConfigManager - Manages environment-specific configurations
 *
 * Features:
 * - Reads environment parameter from TestNG XML
 * - Loads appropriate config file based on environment
 * - Supports multiple environments (dev, qa, stage, prod)
 * - Thread-safe singleton pattern
 * - Fallback to default environment
 * - Property interpolation support
 *
 * Usage:
 * <pre>
 * // In testng.xml:
 * {@code <parameter name="environment" value="qa"/>}
 *
 * // In test class (using helper):
 * {@code @BeforeClass}
 * {@code @Parameters("environment")}
 * public void setup(@Optional("qa") String env, ITestContext context) {
 *     TestNGEnvironmentHelper.loadEnvironmentFromTestNG(context);
 *     String url = EnvironmentConfigManager.getInstance().getProperty("app.url");
 * }
 *
 * // Or directly:
 * EnvironmentConfigManager.getInstance().loadEnvironmentConfig("qa");
 * </pre>
 *
 * @author Test Automation Team
 */
public class EnvironmentConfigManager {

    private static final Logger logger = Logger.getLogger(EnvironmentConfigManager.class);
    private static EnvironmentConfigManager instance;
    private Properties properties;
    private String currentEnvironment;

    // Default environment if none specified
    private static final String DEFAULT_ENVIRONMENT = "qa";

    // Config file naming pattern
    private static final String CONFIG_FILE_PATTERN = "src/main/resources/config-%s.properties";
    private static final String DEFAULT_CONFIG_FILE = "src/main/resources/config.properties";

    /**
     * Private constructor to implement Singleton pattern
     */
    private EnvironmentConfigManager() {
        properties = new Properties();
        currentEnvironment = DEFAULT_ENVIRONMENT;
    }

    /**
     * Get singleton instance of EnvironmentConfigManager
     * @return EnvironmentConfigManager instance
     */
    public static synchronized EnvironmentConfigManager getInstance() {
        if (instance == null) {
            instance = new EnvironmentConfigManager();
        }
        return instance;
    }


    /**
     * Load environment-specific configuration directly (without TestNG context)
     * Useful for standalone execution or direct configuration
     *
     * @param environment Environment name (dev, qa, stage, prod)
     */
    public void loadEnvironmentConfig(String environment) {
        if (environment != null && !environment.isEmpty()) {
            currentEnvironment = environment.toLowerCase();
            logger.info("Loading configuration for environment: " + currentEnvironment);
        } else {
            logger.warn("Environment not specified, using default: " + DEFAULT_ENVIRONMENT);
            currentEnvironment = DEFAULT_ENVIRONMENT;
        }

        loadConfigFile();
    }

    /**
     * Load environment-specific configuration from system property
     * System property: -Denvironment=qa
     */
    public void loadEnvironmentConfigFromSystemProperty() {
        String environment = System.getProperty("environment");

        if (environment != null && !environment.isEmpty()) {
            currentEnvironment = environment.toLowerCase();
            logger.info("Loading configuration from system property for environment: " + currentEnvironment);
        } else {
            logger.warn("No environment system property found, using default: " + DEFAULT_ENVIRONMENT);
            currentEnvironment = DEFAULT_ENVIRONMENT;
        }

        loadConfigFile();
    }

    /**
     * Load configuration file based on current environment
     */
    private void loadConfigFile() {
        String configFilePath = String.format(CONFIG_FILE_PATTERN, currentEnvironment);
        properties = new Properties();

        try {
            // Try loading environment-specific config file
            logger.info("Attempting to load config file: " + configFilePath);
            loadPropertiesFromFile(configFilePath);
            logger.info("✓ Successfully loaded configuration for " + currentEnvironment.toUpperCase() + " environment");
            logger.info("  Config file: " + configFilePath);
            logger.info("  Properties loaded: " + properties.size());

        } catch (IOException e) {
            logger.error("Failed to load environment config: " + configFilePath);
            logger.info("Attempting to load default config file: " + DEFAULT_CONFIG_FILE);

            try {
                loadPropertiesFromFile(DEFAULT_CONFIG_FILE);
                logger.info("✓ Loaded default configuration");
            } catch (IOException ex) {
                logger.error("Failed to load default config: " + DEFAULT_CONFIG_FILE);
                logger.error("Attempting to load from classpath...");

                // Try loading from classpath as final fallback
                tryLoadFromClasspath();
            }
        }

        // Log environment information
        logEnvironmentInfo();
    }

    /**
     * Load properties from file
     * @param filePath Path to properties file
     * @throws IOException if file cannot be read
     */
    private void loadPropertiesFromFile(String filePath) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }
    }

    /**
     * Try to load configuration from classpath
     */
    private void tryLoadFromClasspath() {
        String classpathFile = "config-" + currentEnvironment + ".properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(classpathFile)) {
            if (input != null) {
                properties.load(input);
                logger.info("✓ Configuration loaded from classpath: " + classpathFile);
            } else {
                logger.error("✗ Configuration file not found in classpath: " + classpathFile);
                // Load generic config.properties as last resort
                try (InputStream fallback = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                    if (fallback != null) {
                        properties.load(fallback);
                        logger.info("✓ Loaded generic config.properties from classpath");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load from classpath: " + e.getMessage());
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);

        if (value == null) {
            logger.warn("Property not found: " + key);
            return null;
        }

        // Support property interpolation ${property.name}
        value = resolvePropertyPlaceholders(value);

        return value;
    }

    /**
     * Get property value with default
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    /**
     * Get property as integer
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Integer value
     */
    public int getPropertyAsInt(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for " + key + ": " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Get property as boolean
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Boolean value
     */
    public boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    /**
     * Get property as long
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Long value
     */
    public long getPropertyAsLong(String key, long defaultValue) {
        String value = getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for " + key + ": " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Resolve property placeholders in format ${property.name}
     * @param value Value with potential placeholders
     * @return Resolved value
     */
    private String resolvePropertyPlaceholders(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        String resolved = value;
        int maxIterations = 10; // Prevent infinite loops
        int iteration = 0;

        while (resolved.contains("${") && iteration < maxIterations) {
            int startIndex = resolved.indexOf("${");
            int endIndex = resolved.indexOf("}", startIndex);

            if (endIndex > startIndex) {
                String placeholder = resolved.substring(startIndex + 2, endIndex);
                String placeholderValue = properties.getProperty(placeholder);

                if (placeholderValue != null) {
                    resolved = resolved.replace("${" + placeholder + "}", placeholderValue);
                } else {
                    logger.warn("Unresolved property placeholder: " + placeholder);
                    break;
                }
            } else {
                break;
            }

            iteration++;
        }

        return resolved;
    }

    /**
     * Get current environment name
     * @return Current environment (dev, qa, stage, prod)
     */
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }

    /**
     * Check if running in specific environment
     * @param environment Environment name to check
     * @return true if current environment matches
     */
    public boolean isEnvironment(String environment) {
        return currentEnvironment.equalsIgnoreCase(environment);
    }

    /**
     * Check if running in development environment
     * @return true if DEV environment
     */
    public boolean isDevelopment() {
        return isEnvironment("dev");
    }

    /**
     * Check if running in QA environment
     * @return true if QA environment
     */
    public boolean isQA() {
        return isEnvironment("qa");
    }

    /**
     * Check if running in staging environment
     * @return true if STAGE environment
     */
    public boolean isStaging() {
        return isEnvironment("stage");
    }

    /**
     * Check if running in production environment
     * @return true if PROD environment
     */
    public boolean isProduction() {
        return isEnvironment("prod");
    }

    /**
     * Get all properties
     * @return Properties object
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }

    /**
     * Log environment information
     */
    private void logEnvironmentInfo() {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("  ENVIRONMENT CONFIGURATION LOADED");
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("  Environment: " + currentEnvironment.toUpperCase());
        logger.info("  Description: " + getProperty("environment.description", "N/A"));
        logger.info("  App URL: " + getProperty("app.url", "N/A"));
        logger.info("  Database: " + getProperty("db.url", "N/A"));
        logger.info("  Browser: " + getProperty("browser.default", "N/A"));
        logger.info("  Headless: " + getProperty("browser.headless", "N/A"));
        logger.info("  Parallel: " + getProperty("parallel.execution", "N/A"));
        logger.info("  Thread Count: " + getProperty("thread.count", "N/A"));
        logger.info("  Debug Mode: " + getProperty("debug.mode", "N/A"));
        logger.info("═══════════════════════════════════════════════════════════");
    }

    /**
     * Print all configuration properties (for debugging)
     */
    public void printAllProperties() {
        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("  ALL CONFIGURATION PROPERTIES");
        logger.info("═══════════════════════════════════════════════════════════");

        properties.stringPropertyNames().stream()
            .sorted()
            .forEach(key -> logger.info("  " + key + " = " + properties.getProperty(key)));

        logger.info("═══════════════════════════════════════════════════════════");
        logger.info("  Total Properties: " + properties.size());
        logger.info("═══════════════════════════════════════════════════════════");
    }

    /**
     * Reload configuration (useful for test cases)
     */
    public void reload() {
        logger.info("Reloading configuration for environment: " + currentEnvironment);
        loadConfigFile();
    }

    /**
     * Reset to default environment
     */
    public void reset() {
        logger.info("Resetting to default environment: " + DEFAULT_ENVIRONMENT);
        currentEnvironment = DEFAULT_ENVIRONMENT;
        loadConfigFile();
    }
}
