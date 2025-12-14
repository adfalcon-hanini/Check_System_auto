package com.example.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * BrowserManager class for managing browser initialization and configuration
 * Implements Factory pattern for browser creation
 */
public class BrowserManager {

    private static final Logger logger = Logger.getLogger(BrowserManager.class);
    private WebDriver driver;

    /**
     * Initialize browser based on browser type
     * @param browserType Type of browser (CHROME, FIREFOX, EDGE, SAFARI)
     * @return WebDriver instance
     */
    public WebDriver initializeBrowser(String browserType) {
        logger.info("Initializing browser: " + browserType);

        switch (browserType.toUpperCase()) {
            case Constants.CHROME:
                driver = initializeChrome();
                break;
            case Constants.FIREFOX:
                driver = initializeFirefox();
                break;
            case Constants.EDGE:
                driver = initializeEdge();
                break;
            case Constants.SAFARI:
                driver = initializeSafari();
                break;
            default:
                logger.warn("Unknown browser type: " + browserType + ". Defaulting to Chrome");
                driver = initializeChrome();
        }

        logger.info(browserType + " browser initialized successfully");
        return driver;
    }

    /**
     * Initialize Chrome browser
     * @return ChromeDriver instance
     */
    private WebDriver initializeChrome() {
        logger.info("Configuring Chrome browser");

        // Selenium 4.6+ has built-in Selenium Manager - no need for WebDriverManager
        // It will automatically download and manage ChromeDriver

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        // Disable Chrome Password Manager
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Disable CDP to avoid version mismatch warnings
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-logging"});
        options.setCapability("goog:loggingPrefs", java.util.Collections.emptyMap());

        return new ChromeDriver(options);
    }

    /**
     * Initialize Firefox browser
     * @return FirefoxDriver instance
     */
    private WebDriver initializeFirefox() {
        logger.info("Configuring Firefox browser");

        // Selenium 4.6+ has built-in Selenium Manager - no need for WebDriverManager

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("dom.push.enabled", false);

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Initialize Edge browser
     * @return EdgeDriver instance
     */
    private WebDriver initializeEdge() {
        logger.info("Configuring Edge browser");

        // Selenium 4.6+ has built-in Selenium Manager - no need for WebDriverManager

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");

        // Disable Edge Password Manager
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new EdgeDriver(options);
    }

    /**
     * Initialize Safari browser
     * @return SafariDriver instance
     */
    private WebDriver initializeSafari() {
        logger.info("Configuring Safari browser");

        SafariOptions options = new SafariOptions();
        options.setAutomaticInspection(false);

        WebDriver driver = new SafariDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Quit browser
     */
    public void quitBrowser() {
        if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
            logger.info("Browser closed successfully");
        }
    }

    /**
     * Get WebDriver instance
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return driver;
    }
}
