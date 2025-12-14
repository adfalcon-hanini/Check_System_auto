package com.example.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * DynamicElementHandler - Utility for handling dynamic elements
 * Supports multiple locator strategies and dynamic waits
 */
public class DynamicElementHandler {

    private static final Logger logger = Logger.getLogger(DynamicElementHandler.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private WebDriver driver;
    private WebDriverWait wait;

    public DynamicElementHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
    }

    /**
     * Find element using dynamic locator strategy
     * @param locatorType Locator type (id, name, xpath, css, class, tagname, linktext, partiallinktext)
     * @param locatorValue Locator value
     * @return WebElement
     */
    public WebElement findElement(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Element found using " + locatorType + ": " + locatorValue);
            return element;
        } catch (Exception e) {
            logger.error("Element not found using " + locatorType + ": " + locatorValue);
            throw e;
        }
    }

    /**
     * Find elements using dynamic locator strategy
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return List of WebElements
     */
    public List<WebElement> findElements(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            List<WebElement> elements = driver.findElements(locator);
            logger.info("Found " + elements.size() + " elements using " + locatorType + ": " + locatorValue);
            return elements;
        } catch (Exception e) {
            logger.error("Elements not found using " + locatorType + ": " + locatorValue);
            throw e;
        }
    }

    /**
     * Find element with fallback locators
     * @param locators Array of locator pairs [type, value, type, value, ...]
     * @return WebElement
     */
    public WebElement findElementWithFallback(String... locators) {
        if (locators.length % 2 != 0) {
            throw new IllegalArgumentException("Locators must be in pairs (type, value)");
        }

        for (int i = 0; i < locators.length; i += 2) {
            try {
                String type = locators[i];
                String value = locators[i + 1];
                return findElement(type, value);
            } catch (Exception e) {
                logger.warn("Fallback locator " + (i / 2 + 1) + " failed: " + locators[i] + "=" + locators[i + 1]);
                if (i >= locators.length - 2) {
                    logger.error("All fallback locators failed");
                    throw e;
                }
            }
        }

        throw new RuntimeException("No element found with any fallback locator");
    }

    /**
     * Wait for element to be visible
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return WebElement
     */
    public WebElement waitForVisibility(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element visible: " + locatorType + "=" + locatorValue);
            return element;
        } catch (Exception e) {
            logger.error("Element not visible within timeout: " + locatorType + "=" + locatorValue);
            throw e;
        }
    }

    /**
     * Wait for element to be clickable
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return WebElement
     */
    public WebElement waitForClickable(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element clickable: " + locatorType + "=" + locatorValue);
            return element;
        } catch (Exception e) {
            logger.error("Element not clickable within timeout: " + locatorType + "=" + locatorValue);
            throw e;
        }
    }

    /**
     * Check if element exists (without waiting)
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return true if exists, false otherwise
     */
    public boolean isElementPresent(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for element to disappear
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return true if disappeared, false otherwise
     */
    public boolean waitForInvisibility(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);

        try {
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.info("Element disappeared: " + locatorType + "=" + locatorValue);
            return invisible;
        } catch (Exception e) {
            logger.error("Element still visible after timeout: " + locatorType + "=" + locatorValue);
            return false;
        }
    }

    /**
     * Get By locator based on type
     * @param locatorType Locator type
     * @param locatorValue Locator value
     * @return By locator
     */
    private By getByLocator(String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "css":
            case "cssselector":
                return By.cssSelector(locatorValue);
            case "class":
            case "classname":
                return By.className(locatorValue);
            case "tag":
            case "tagname":
                return By.tagName(locatorValue);
            case "linktext":
                return By.linkText(locatorValue);
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            default:
                logger.error("Invalid locator type: " + locatorType);
                throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        }
    }

    /**
     * Get custom wait with timeout
     * @param timeoutInSeconds Timeout in seconds
     * @return WebDriverWait
     */
    public WebDriverWait getCustomWait(int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
}
