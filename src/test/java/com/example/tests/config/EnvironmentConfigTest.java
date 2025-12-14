package com.example.tests.config;

import com.example.utils.EnvironmentConfigManager;
import io.qameta.allure.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

/**
 * Comprehensive test class demonstrating Environment Configuration System
 *
 * Features Tested:
 * 1. Loading environment-specific configurations
 * 2. Reading environment from TestNG XML parameter
 * 3. Property access and validation
 * 4. Environment detection methods
 * 5. Property interpolation
 * 6. Type conversion (String, Integer, Boolean, Long)
 *
 * @author Test Automation Team
 */
@Epic("Configuration Management")
@Feature("Environment Configuration")
public class EnvironmentConfigTest {

    private static final Logger logger = Logger.getLogger(EnvironmentConfigTest.class);
    private EnvironmentConfigManager configManager;
    private String environment;

    @BeforeClass
    @Parameters("environment")
    public void setupEnvironment(@Optional("qa") String env, ITestContext context) {
        logger.info("========== ENVIRONMENT CONFIGURATION TEST SETUP ==========");
        logger.info("Environment parameter from TestNG: " + env);

        // Get singleton instance
        configManager = EnvironmentConfigManager.getInstance();

        // Load environment configuration from TestNG using helper
        com.example.utils.TestNGEnvironmentHelper.loadEnvironmentFromTestNG(context);

        // Store environment for assertions
        environment = env.toLowerCase();

        logger.info("Configuration Manager initialized for: " + environment.toUpperCase());
        logger.info("==========================================================");
    }

    // ============================================
    // BASIC CONFIGURATION TESTS
    // ============================================

    @Test(priority = 1, description = "Verify environment is loaded correctly")
    @Story("Basic Configuration")
    @Description("Verify that the environment configuration is loaded correctly from TestNG XML")
    @Severity(SeverityLevel.BLOCKER)
    public void testEnvironmentLoaded() {
        logger.info("=== TEST: Verify Environment Loaded ===");

        // Get current environment from config manager
        String currentEnv = configManager.getCurrentEnvironment();

        logger.info("Expected environment: " + environment);
        logger.info("Loaded environment: " + currentEnv);

        // Verify environment matches
        Assert.assertEquals(currentEnv, environment,
            "Environment should match TestNG parameter");

        logger.info("✓ Environment loaded correctly: " + currentEnv.toUpperCase());
    }

    @Test(priority = 2, description = "Verify environment-specific URL is loaded")
    @Story("Basic Configuration")
    @Description("Verify that environment-specific app URL is loaded correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void testEnvironmentSpecificUrl() {
        logger.info("=== TEST: Verify Environment-Specific URL ===");

        String appUrl = configManager.getProperty("app.url");

        Assert.assertNotNull(appUrl, "App URL should not be null");
        Assert.assertFalse(appUrl.isEmpty(), "App URL should not be empty");

        logger.info("App URL for " + environment.toUpperCase() + ": " + appUrl);

        // Verify URL contains environment indicator (not strict check)
        boolean isValid = appUrl.contains("thegroup.com.qa");
        Assert.assertTrue(isValid, "URL should be valid");

        logger.info("✓ Environment-specific URL loaded correctly");
    }

    @Test(priority = 3, description = "Verify environment-specific database config")
    @Story("Basic Configuration")
    @Description("Verify that environment-specific database configuration is loaded")
    @Severity(SeverityLevel.CRITICAL)
    public void testEnvironmentSpecificDatabase() {
        logger.info("=== TEST: Verify Environment-Specific Database Config ===");

        String dbUrl = configManager.getProperty("db.url");
        String dbUser = configManager.getProperty("db.user");
        String dbSchema = configManager.getProperty("db.schema");

        Assert.assertNotNull(dbUrl, "DB URL should not be null");
        Assert.assertNotNull(dbUser, "DB User should not be null");
        Assert.assertNotNull(dbSchema, "DB Schema should not be null");

        logger.info("Database Configuration for " + environment.toUpperCase() + ":");
        logger.info("  URL: " + dbUrl);
        logger.info("  User: " + dbUser);
        logger.info("  Schema: " + dbSchema);

        logger.info("✓ Environment-specific database config loaded correctly");
    }

    // ============================================
    // PROPERTY ACCESS TESTS
    // ============================================

    @Test(priority = 10, description = "Test getting string properties")
    @Story("Property Access")
    @Description("Verify getting string properties from configuration")
    @Severity(SeverityLevel.NORMAL)
    public void testGetStringProperty() {
        logger.info("=== TEST: Get String Property ===");

        String environmentName = configManager.getProperty("environment.name");
        String environmentDesc = configManager.getProperty("environment.description");

        Assert.assertNotNull(environmentName, "Environment name should not be null");
        Assert.assertNotNull(environmentDesc, "Environment description should not be null");

        logger.info("Environment Name: " + environmentName);
        logger.info("Environment Description: " + environmentDesc);

        logger.info("✓ String properties retrieved successfully");
    }

    @Test(priority = 11, description = "Test getting integer properties")
    @Story("Property Access")
    @Description("Verify getting integer properties with type conversion")
    @Severity(SeverityLevel.NORMAL)
    public void testGetIntegerProperty() {
        logger.info("=== TEST: Get Integer Property ===");

        int waitImplicit = configManager.getPropertyAsInt("wait.implicit", 10);
        int waitExplicit = configManager.getPropertyAsInt("wait.explicit", 15);
        int threadCount = configManager.getPropertyAsInt("thread.count", 3);

        Assert.assertTrue(waitImplicit > 0, "Wait implicit should be positive");
        Assert.assertTrue(waitExplicit > 0, "Wait explicit should be positive");
        Assert.assertTrue(threadCount > 0, "Thread count should be positive");

        logger.info("Wait Times:");
        logger.info("  Implicit: " + waitImplicit + "s");
        logger.info("  Explicit: " + waitExplicit + "s");
        logger.info("Thread Count: " + threadCount);

        logger.info("✓ Integer properties retrieved successfully");
    }

    @Test(priority = 12, description = "Test getting boolean properties")
    @Story("Property Access")
    @Description("Verify getting boolean properties with type conversion")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBooleanProperty() {
        logger.info("=== TEST: Get Boolean Property ===");

        boolean headless = configManager.getPropertyAsBoolean("browser.headless", false);
        boolean maximize = configManager.getPropertyAsBoolean("browser.maximize", true);
        boolean retryEnabled = configManager.getPropertyAsBoolean("retry.failed.tests", true);

        logger.info("Browser Configuration:");
        logger.info("  Headless: " + headless);
        logger.info("  Maximize: " + maximize);
        logger.info("  Retry Enabled: " + retryEnabled);

        logger.info("✓ Boolean properties retrieved successfully");
    }

    @Test(priority = 13, description = "Test getting long properties")
    @Story("Property Access")
    @Description("Verify getting long properties with type conversion")
    @Severity(SeverityLevel.NORMAL)
    public void testGetLongProperty() {
        logger.info("=== TEST: Get Long Property ===");

        long connectionTimeout = configManager.getPropertyAsLong("timeout.connection", 30000L);
        long socketTimeout = configManager.getPropertyAsLong("timeout.socket", 30000L);

        Assert.assertTrue(connectionTimeout > 0, "Connection timeout should be positive");
        Assert.assertTrue(socketTimeout > 0, "Socket timeout should be positive");

        logger.info("Timeouts (ms):");
        logger.info("  Connection: " + connectionTimeout);
        logger.info("  Socket: " + socketTimeout);

        logger.info("✓ Long properties retrieved successfully");
    }

    @Test(priority = 14, description = "Test property with default value")
    @Story("Property Access")
    @Description("Verify getting property with default value when property doesn't exist")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPropertyWithDefault() {
        logger.info("=== TEST: Get Property with Default Value ===");

        // Try to get non-existent property
        String nonExistent = configManager.getProperty("non.existent.property", "DEFAULT_VALUE");

        Assert.assertEquals(nonExistent, "DEFAULT_VALUE",
            "Should return default value for non-existent property");

        logger.info("Non-existent property returned default: " + nonExistent);

        // Try to get existing property
        String existing = configManager.getProperty("environment.name", "DEFAULT");

        Assert.assertNotEquals(existing, "DEFAULT",
            "Should return actual value for existing property");

        logger.info("Existing property returned actual value: " + existing);

        logger.info("✓ Default value mechanism working correctly");
    }

    // ============================================
    // PROPERTY INTERPOLATION TESTS
    // ============================================

    @Test(priority = 20, description = "Test property interpolation")
    @Story("Property Interpolation")
    @Description("Verify property interpolation with ${property.name} syntax")
    @Severity(SeverityLevel.NORMAL)
    public void testPropertyInterpolation() {
        logger.info("=== TEST: Property Interpolation ===");

        // Get properties that use interpolation
        String appUrl = configManager.getProperty("app.url");
        String appUrlEn = configManager.getProperty("app.url.en");

        Assert.assertNotNull(appUrl, "Base app URL should not be null");
        Assert.assertNotNull(appUrlEn, "English app URL should not be null");

        // Verify interpolation worked
        Assert.assertTrue(appUrlEn.contains(appUrl),
            "Interpolated URL should contain base URL");

        logger.info("Base URL: " + appUrl);
        logger.info("English URL: " + appUrlEn);
        logger.info("✓ Property interpolation working correctly");
    }

    // ============================================
    // ENVIRONMENT DETECTION TESTS
    // ============================================

    @Test(priority = 30, description = "Test environment detection methods")
    @Story("Environment Detection")
    @Description("Verify environment detection helper methods")
    @Severity(SeverityLevel.NORMAL)
    public void testEnvironmentDetection() {
        logger.info("=== TEST: Environment Detection Methods ===");

        boolean isDev = configManager.isDevelopment();
        boolean isQa = configManager.isQA();
        boolean isStage = configManager.isStaging();
        boolean isProd = configManager.isProduction();

        logger.info("Environment Checks:");
        logger.info("  Is DEV: " + isDev);
        logger.info("  Is QA: " + isQa);
        logger.info("  Is STAGE: " + isStage);
        logger.info("  Is PROD: " + isProd);

        // Exactly one should be true
        int trueCount = (isDev ? 1 : 0) + (isQa ? 1 : 0) +
                       (isStage ? 1 : 0) + (isProd ? 1 : 0);

        Assert.assertEquals(trueCount, 1,
            "Exactly one environment should be true");

        // Verify correct environment is detected
        switch (environment.toLowerCase()) {
            case "dev":
                Assert.assertTrue(isDev, "Should detect DEV environment");
                break;
            case "qa":
                Assert.assertTrue(isQa, "Should detect QA environment");
                break;
            case "stage":
                Assert.assertTrue(isStage, "Should detect STAGE environment");
                break;
            case "prod":
                Assert.assertTrue(isProd, "Should detect PROD environment");
                break;
        }

        logger.info("✓ Environment detection working correctly");
    }

    @Test(priority = 31, description = "Test isEnvironment method")
    @Story("Environment Detection")
    @Description("Verify isEnvironment() method with various inputs")
    @Severity(SeverityLevel.NORMAL)
    public void testIsEnvironmentMethod() {
        logger.info("=== TEST: isEnvironment() Method ===");

        String currentEnv = configManager.getCurrentEnvironment();

        // Test with correct environment
        Assert.assertTrue(configManager.isEnvironment(currentEnv),
            "Should return true for current environment");

        // Test with uppercase
        Assert.assertTrue(configManager.isEnvironment(currentEnv.toUpperCase()),
            "Should be case-insensitive");

        // Test with wrong environment
        Assert.assertFalse(configManager.isEnvironment("invalid"),
            "Should return false for invalid environment");

        logger.info("✓ isEnvironment() method working correctly");
    }

    // ============================================
    // ENVIRONMENT-SPECIFIC BEHAVIOR TESTS
    // ============================================

    @Test(priority = 40, description = "Verify dev environment has debug enabled", enabled = false)
    @Story("Environment-Specific Behavior")
    @Description("Verify DEV environment has debug mode enabled")
    @Severity(SeverityLevel.NORMAL)
    public void testDevEnvironmentHasDebug() {
        if (configManager.isDevelopment()) {
            logger.info("=== TEST: DEV Environment Debug Settings ===");

            boolean debugMode = configManager.getPropertyAsBoolean("debug.mode", false);
            Assert.assertTrue(debugMode, "DEV should have debug mode enabled");

            logger.info("✓ DEV environment has debug enabled");
        }
    }

    @Test(priority = 41, description = "Verify production environment is read-only", enabled = false)
    @Story("Environment-Specific Behavior")
    @Description("Verify PROD environment has read-only flag")
    @Severity(SeverityLevel.CRITICAL)
    public void testProdEnvironmentReadOnly() {
        if (configManager.isProduction()) {
            logger.info("=== TEST: PROD Environment Read-Only Settings ===");

            boolean readOnly = configManager.getPropertyAsBoolean("production.read.only", false);
            Assert.assertTrue(readOnly, "PROD should have read-only flag enabled");

            logger.info("✓ PROD environment is read-only");
        }
    }

    // ============================================
    // CONFIGURATION VALIDATION TESTS
    // ============================================

    @Test(priority = 50, description = "Validate all required properties exist")
    @Story("Configuration Validation")
    @Description("Verify all required configuration properties are present")
    @Severity(SeverityLevel.BLOCKER)
    public void testRequiredPropertiesExist() {
        logger.info("=== TEST: Required Properties Validation ===");

        String[] requiredProperties = {
            "environment.name",
            "app.url",
            "browser.default",
            "wait.implicit",
            "wait.explicit"
        };

        for (String property : requiredProperties) {
            String value = configManager.getProperty(property);
            Assert.assertNotNull(value,
                "Required property should exist: " + property);
            Assert.assertFalse(value.isEmpty(),
                "Required property should not be empty: " + property);

            logger.info("✓ " + property + " = " + value);
        }

        logger.info("✓ All required properties exist");
    }

    @Test(priority = 51, description = "Validate URLs are properly formatted")
    @Story("Configuration Validation")
    @Description("Verify all URL properties are properly formatted")
    @Severity(SeverityLevel.CRITICAL)
    public void testUrlsAreValid() {
        logger.info("=== TEST: URL Validation ===");

        String[] urlProperties = {
            "app.url",
            "app.url.en",
            "app.url.ar",
            "app.login.url"
        };

        for (String property : urlProperties) {
            String url = configManager.getProperty(property);
            Assert.assertNotNull(url, "URL should not be null: " + property);

            // Basic URL validation
            Assert.assertTrue(url.startsWith("http") || url.startsWith("https"),
                "URL should start with http/https: " + property);

            logger.info("✓ " + property + " = " + url);
        }

        logger.info("✓ All URLs are properly formatted");
    }

    // ============================================
    // UTILITY METHOD TESTS
    // ============================================

    @Test(priority = 60, description = "Test getAllProperties method")
    @Story("Utility Methods")
    @Description("Verify getAllProperties returns all configuration properties")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllProperties() {
        logger.info("=== TEST: Get All Properties ===");

        java.util.Properties allProps = configManager.getAllProperties();

        Assert.assertNotNull(allProps, "Properties should not be null");
        Assert.assertTrue(allProps.size() > 0, "Should have at least some properties");

        logger.info("Total properties loaded: " + allProps.size());
        logger.info("✓ getAllProperties() working correctly");
    }

    @Test(priority = 61, description = "Test print all properties (debug)")
    @Story("Utility Methods")
    @Description("Verify printAllProperties displays all configuration")
    @Severity(SeverityLevel.TRIVIAL)
    public void testPrintAllProperties() {
        logger.info("=== TEST: Print All Properties ===");

        // This method logs all properties
        configManager.printAllProperties();

        logger.info("✓ Properties printed to log");
    }

    // ============================================
    // REAL-WORLD USAGE SCENARIO
    // ============================================

    @Test(priority = 70, description = "Real-world usage scenario")
    @Story("Real-World Scenarios")
    @Description("Demonstrate real-world usage of environment configuration")
    @Severity(SeverityLevel.NORMAL)
    public void testRealWorldUsageScenario() {
        logger.info("=== TEST: Real-World Usage Scenario ===");

        // Step 1: Get application URL
        String appUrl = configManager.getProperty("app.url.en");
        logger.info("Step 1: Application URL = " + appUrl);

        // Step 2: Get test credentials
        String username = configManager.getProperty("username");
        logger.info("Step 2: Test Username = " + username);

        // Step 3: Get browser configuration
        String browser = configManager.getProperty("browser.default");
        boolean headless = configManager.getPropertyAsBoolean("browser.headless", false);
        logger.info("Step 3: Browser = " + browser + ", Headless = " + headless);

        // Step 4: Get wait times
        int implicitWait = configManager.getPropertyAsInt("wait.implicit", 10);
        int explicitWait = configManager.getPropertyAsInt("wait.explicit", 15);
        logger.info("Step 4: Waits - Implicit: " + implicitWait + "s, Explicit: " + explicitWait + "s");

        // Step 5: Conditional logic based on environment
        if (configManager.isDevelopment()) {
            logger.info("Step 5: Running in DEV - enable verbose logging");
        } else if (configManager.isProduction()) {
            logger.info("Step 5: Running in PROD - use minimal logging");
        } else {
            logger.info("Step 5: Running in " + configManager.getCurrentEnvironment().toUpperCase());
        }

        // Verify we got all necessary configuration
        Assert.assertNotNull(appUrl, "Should have app URL");
        Assert.assertNotNull(username, "Should have username");
        Assert.assertNotNull(browser, "Should have browser config");

        logger.info("✓ Real-world usage scenario completed successfully");
    }

    // ============================================
    // SUMMARY TEST
    // ============================================

    @Test(priority = 100, description = "Print environment configuration summary")
    @Story("Summary")
    @Description("Print comprehensive summary of environment configuration")
    @Severity(SeverityLevel.TRIVIAL)
    public void printConfigurationSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ENVIRONMENT CONFIGURATION SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("CURRENT ENVIRONMENT: " + configManager.getCurrentEnvironment().toUpperCase());
        System.out.println();
        System.out.println("APPLICATION CONFIGURATION:");
        System.out.println("  URL: " + configManager.getProperty("app.url"));
        System.out.println("  English URL: " + configManager.getProperty("app.url.en"));
        System.out.println("  Arabic URL: " + configManager.getProperty("app.url.ar"));
        System.out.println("  Login URL: " + configManager.getProperty("app.login.url"));
        System.out.println();
        System.out.println("DATABASE CONFIGURATION:");
        System.out.println("  URL: " + configManager.getProperty("db.url"));
        System.out.println("  User: " + configManager.getProperty("db.user"));
        System.out.println("  Schema: " + configManager.getProperty("db.schema"));
        System.out.println();
        System.out.println("BROWSER CONFIGURATION:");
        System.out.println("  Default: " + configManager.getProperty("browser.default"));
        System.out.println("  Headless: " + configManager.getProperty("browser.headless"));
        System.out.println("  Maximize: " + configManager.getProperty("browser.maximize"));
        System.out.println();
        System.out.println("WAIT CONFIGURATION (seconds):");
        System.out.println("  Implicit: " + configManager.getProperty("wait.implicit"));
        System.out.println("  Explicit: " + configManager.getProperty("wait.explicit"));
        System.out.println("  Page Load: " + configManager.getProperty("wait.page.load"));
        System.out.println();
        System.out.println("EXECUTION CONFIGURATION:");
        System.out.println("  Parallel: " + configManager.getProperty("parallel.execution"));
        System.out.println("  Thread Count: " + configManager.getProperty("thread.count"));
        System.out.println("  Retry Failed: " + configManager.getProperty("retry.failed.tests"));
        System.out.println("  Max Retries: " + configManager.getProperty("retry.max.count"));
        System.out.println();
        System.out.println("DEBUG SETTINGS:");
        System.out.println("  Debug Mode: " + configManager.getProperty("debug.mode"));
        System.out.println("  Screenshots: " + configManager.getProperty("debug.screenshots"));
        System.out.println("  Verbose Logging: " + configManager.getProperty("debug.verbose.logging"));
        System.out.println();
        System.out.println("ENVIRONMENT DETECTION:");
        System.out.println("  Is Development: " + configManager.isDevelopment());
        System.out.println("  Is QA: " + configManager.isQA());
        System.out.println("  Is Staging: " + configManager.isStaging());
        System.out.println("  Is Production: " + configManager.isProduction());
        System.out.println("=".repeat(80) + "\n");
    }
}
