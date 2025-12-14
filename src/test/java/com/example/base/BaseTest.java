package com.example.base;

import com.example.utils.BrowserManager;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * BaseTest class containing common setup and teardown for all test classes
 * Implements reusable test configuration and best practices
 */
public abstract class BaseTest {

    protected static final Logger logger = Logger.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected BrowserManager browserManager;

    // Static block to suppress Selenium CDP warnings
    static {
        try {
            // Suppress Selenium DevTools/CDP warnings
            java.util.logging.Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder").setLevel(Level.SEVERE);
            java.util.logging.Logger.getLogger("org.openqa.selenium.chromium.ChromiumDriver").setLevel(Level.SEVERE);
        } catch (Exception e) {
            // Ignore any errors during logger configuration
        }
    }

    /**
     * Setup method to initialize browser before each test
     * @param browser Browser type (CHROME, FIREFOX, EDGE, SAFARI)
     */
    @BeforeMethod
    @Step("Initialize browser and setup test environment")
    @Parameters("browser")
    public void setUp(@Optional("CHROME") String browser) {
        logger.info("==================================================");
        logger.info("Setting up test environment");
        logger.info("Browser: " + browser);
        logger.info("==================================================");

        // Get browser from system property or parameter
        String browserType = System.getProperty("browser", browser);
        logger.info("Initializing browser: " + browserType);

        // Handle Chrome browser initialization with custom ChromeOptions
        if (browserType.equalsIgnoreCase("CHROME")) {
            driver = initializeChromeWithOptions();
        } else {
            // Use BrowserManager for other browsers
            browserManager = new BrowserManager();
            driver = browserManager.initializeBrowser(browserType);
        }

        logger.info("Test setup completed successfully");
        logger.info("==================================================");
    }

    /**
     * Initialize Chrome browser with custom ChromeOptions
     * Disables Chrome Password Manager
     * @return ChromeDriver instance
     */
    private WebDriver initializeChromeWithOptions() {
        logger.info("Configuring Chrome browser with custom options");

        ChromeOptions options = new ChromeOptions();

        // Standard Chrome arguments
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-save-password-bubble");

        // Disable Chrome Password Manager
        // Disable all password-manager related features
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);  // 🔥 REQUIRED FOR YOUR POPUP
        prefs.put("password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);

        // Disable CDP to avoid version mismatch warnings
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-logging"});
        options.setCapability("goog:loggingPrefs", java.util.Collections.emptyMap());

        logger.info("Chrome Password Manager: DISABLED");

        return new ChromeDriver(options);
    }

    /**
     * Teardown method to close browser after each test
     */
    @AfterMethod
    @Step("Close browser and cleanup test environment")
    public void tearDown() {
        logger.info("==================================================");
        logger.info("Tearing down test environment");
        logger.info("==================================================");

        if (browserManager != null) {
            browserManager.quitBrowser();
        } else if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
            logger.info("Browser closed successfully");
        }

        logger.info("Test teardown completed");
        logger.info("==================================================");
    }

    /**
     * Get WebDriver instance
     * @return WebDriver
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Log test info
     * @param testName Test name
     * @param message Message to log
     */
    protected void logTestInfo(String testName, String message) {
        logger.info(">>> Test: " + testName + " - " + message);
    }

    /**
     * Log test start
     * @param testName Test name
     */
    protected void logTestStart(String testName) {
        logger.info("╔════════════════════════════════════════════════╗");
        logger.info("║  Starting test: " + testName);
        logger.info("╚════════════════════════════════════════════════╝");
    }

    /**
     * Log test completion
     * @param testName Test name
     * @param status Test status (PASSED/FAILED)
     */
    protected void logTestComplete(String testName, String status) {
        logger.info("╔════════════════════════════════════════════════╗");
        logger.info("║  Test completed: " + testName + " - " + status);
        logger.info("╚════════════════════════════════════════════════╝");
    }
}
