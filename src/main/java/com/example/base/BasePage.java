package com.example.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage class containing common methods for all page objects
 * Implements reusable functionality and best practices for POM
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = Logger.getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 15;

    /**
     * Constructor to initialize BasePage
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
        logger.info(this.getClass().getSimpleName() + " initialized");
    }

    /**
     * Navigate to specified URL
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        logger.info("Navigating to URL: " + url);
        driver.get(url);
        logger.info("Successfully navigated to: " + url);
    }

    /**
     * Click element with wait
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                // Add small delay for headless mode stability
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                element.click();
                logger.info("Element clicked successfully");
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                logger.warn("StaleElementReferenceException on click attempt " + (i + 1) + ", retrying...");
                if (i == maxRetries - 1) {
                    logger.error("Failed to click after " + maxRetries + " attempts");
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                logger.error("Failed to click element: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Click element with retry mechanism
     * @param element WebElement to click
     * @param maxRetries Maximum number of retries
     */
    protected void clickWithRetry(WebElement element, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                click(element);
                return;
            } catch (Exception e) {
                logger.warn("Click attempt " + (i + 1) + " failed: " + e.getMessage());
                if (i == maxRetries - 1) {
                    throw e;
                }
            }
        }
    }

    /**
     * Enter text into input field
     * @param element WebElement to enter text
     * @param text Text to enter
     */
    protected void enterText(WebElement element, String text) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                // Add small delay for headless mode stability
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                element.clear();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                element.sendKeys(text);
                logger.info("Text entered successfully: " + text);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                logger.warn("StaleElementReferenceException on attempt " + (i + 1) + ", retrying...");
                if (i == maxRetries - 1) {
                    logger.error("Failed to enter text after " + maxRetries + " attempts");
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                logger.error("Failed to enter text: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Get text from element
     * @param element WebElement to get text from
     * @return Element text
     */
    protected String getText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText();
            logger.info("Retrieved text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text: " + e.getMessage());
            return "";
        }
    }

    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is present with timeout
     * @param element WebElement to check
     * @return true if present, false otherwise
     */
    protected boolean isElementPresent(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     */
    protected void waitForVisibility(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            logger.info("Element is now visible");
        } catch (Exception e) {
            logger.error("Element not visible within timeout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     */
    protected void waitForClickability(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            logger.info("Element is now clickable");
        } catch (Exception e) {
            logger.error("Element not clickable within timeout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for all elements to be visible
     * @param elements List of WebElements
     */
    protected void waitForAllVisible(List<WebElement> elements) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            logger.info("All elements are now visible");
        } catch (Exception e) {
            logger.error("Not all elements visible within timeout: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: " + title);
        return title;
    }

    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: " + url);
        return url;
    }

    /**
     * Wait for page to load (thread sleep)
     * @param milliseconds Time to wait in milliseconds
     */
    public void waitForPageLoad(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            logger.info("Waited for " + milliseconds + "ms");
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for page to load with default timeout
     */
    public void waitForPageLoad() {
        waitForPageLoad(2000);
    }

    /**
     * Scroll to element
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + e.getMessage());
        }
    }

    /**
     * Click element using JavaScript
     * @param element WebElement to click
     */
    protected void clickUsingJS(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("Element clicked using JavaScript");
        } catch (Exception e) {
            logger.error("Failed to click using JavaScript: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get element attribute value
     * @param element WebElement
     * @param attribute Attribute name
     * @return Attribute value
     */
    protected String getAttributeValue(WebElement element, String attribute) {
        try {
            String value = element.getAttribute(attribute);
            logger.info("Attribute '" + attribute + "' value: " + value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute: " + e.getMessage());
            return "";
        }
    }

    /**
     * Check if element is enabled
     * @param element WebElement to check
     * @return true if enabled, false otherwise
     */
    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is selected
     * @param element WebElement to check
     * @return true if selected, false otherwise
     */
    protected boolean isElementSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of elements
     * @param elements List of WebElements
     * @return Count of elements
     */
    protected int getElementsCount(List<WebElement> elements) {
        try {
            int count = elements.size();
            logger.info("Elements count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get elements count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Accept alert
     */
    protected void acceptAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.error("No alert present: " + e.getMessage());
        }
    }

    /**
     * Dismiss alert
     */
    protected void dismissAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.dismiss();
            logger.info("Alert dismissed");
        } catch (Exception e) {
            logger.error("No alert present: " + e.getMessage());
        }
    }

    /**
     * Get alert text
     * @return Alert text
     */
    protected String getAlertText() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            logger.info("Alert text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("No alert present: " + e.getMessage());
            return "";
        }
    }

    /**
     * Switch to frame by index
     * @param index Frame index
     */
    protected void switchToFrame(int index) {
        try {
            driver.switchTo().frame(index);
            logger.info("Switched to frame: " + index);
        } catch (Exception e) {
            logger.error("Failed to switch to frame: " + e.getMessage());
        }
    }

    /**
     * Switch to frame by element
     * @param frameElement Frame WebElement
     */
    protected void switchToFrame(WebElement frameElement) {
        try {
            driver.switchTo().frame(frameElement);
            logger.info("Switched to frame element");
        } catch (Exception e) {
            logger.error("Failed to switch to frame: " + e.getMessage());
        }
    }

    /**
     * Switch to default content
     */
    protected void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            logger.info("Switched to default content");
        } catch (Exception e) {
            logger.error("Failed to switch to default content: " + e.getMessage());
        }
    }

    /**
     * Refresh page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
    }

    /**
     * Navigate back
     */
    protected void navigateBack() {
        driver.navigate().back();
        logger.info("Navigated back");
    }

    /**
     * Navigate forward
     */
    protected void navigateForward() {
        driver.navigate().forward();
        logger.info("Navigated forward");
    }
}