package com.example.tests.fix;

import com.example.pages.LoginPage;
import com.example.utils.DataTestIdExtractor;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class to extract all data-testid elements from the Portfolio screen tab
 * URL: https://devuat.thegroup.com.qa/uat/en/trading
 * Tab: Portfolio
 */
public class PortfolioDataTestIdExtractorTest {

    private static final Logger logger = Logger.getLogger(PortfolioDataTestIdExtractorTest.class);
    private static final String TRADING_URL = "https://devuat.thegroup.com.qa/uat/en/trading";
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        logger.info("Setting up WebDriver for Portfolio data-testid extraction");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Initialize LoginPage and perform login
        loginPage = new LoginPage(driver);
        driver.get(TRADING_URL);
        try {
            Thread.sleep(2000);

            logger.info("Performing login before extracting Portfolio data");
            loginPage.clickLoginInTrading();
            loginPage.login("12240", "12345");
            loginPage.waitForPageLoad(3000);

            logger.info("Login completed successfully");

            // Wait for any modals/overlays to disappear after login
            logger.info("Waiting for any overlays to disappear...");
            Thread.sleep(3000);

            // Try to close any open modal by pressing ESC key
            try {
                List<WebElement> overlays = driver.findElements(By.cssSelector("[data-state='open']"));
                if (!overlays.isEmpty()) {
                    logger.info("Found open overlay, attempting to close it by pressing ESC");
                    driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                logger.info("No overlay to close or already closed");
            }

        } catch (InterruptedException e) {
            logger.error("Error during login process: " + e.getMessage(), e);
        }

        logger.info("WebDriver setup completed");
    }

    @Test(priority = 1, description = "Extract all data-testid elements from Portfolio tab")
    public void testExtractPortfolioDataTestIds() {
        printTestHeader("EXTRACT DATA-TESTID ELEMENTS FROM PORTFOLIO TAB");

        try {
            // Navigation and login already done in setup
            Thread.sleep(1000); // Brief wait for page stability

            // Click on Portfolio tab
            logger.info("Looking for Portfolio tab...");
            List<WebElement> portfolioTabs = findPortfolioTab();

            if (!portfolioTabs.isEmpty()) {
                logger.info("Found Portfolio tab, clicking...");
                portfolioTabs.get(0).click();
                Thread.sleep(2000); // Wait for tab content to load

                // Extract all data-testid elements
                logger.info("Extracting all data-testid elements from Portfolio tab...");
                List<Map<String, String>> elements = extractDataTestIdElements();

                logger.info("Successfully extracted " + elements.size() + " elements with data-testid");

                // Print elements in table format
                printDataTestIdElements(elements);

                // Export to CSV
                exportToCSV(elements, "portfolio-data-testid");

            } else {
                logger.warn("Portfolio tab not found. Available tabs/elements:");
                printAvailableTabs();
            }

        } catch (Exception e) {
            logger.error("Error during Portfolio data-testid extraction: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Find the Portfolio tab element
     */
    private List<WebElement> findPortfolioTab() {
        List<WebElement> portfolioTabs = new ArrayList<>();

        // Try different selectors to find Portfolio tab
        String[] selectors = {
            "[data-testid*='portfolio']",
            "[data-testid*='Portfolio']",
            "//button[contains(text(), 'Portfolio')]",
            "//a[contains(text(), 'Portfolio')]",
            "//div[contains(text(), 'Portfolio')]",
            "[role='tab'][aria-label*='Portfolio']",
            "[role='tab'][aria-label*='portfolio']"
        };

        for (String selector : selectors) {
            try {
                if (selector.startsWith("//")) {
                    // XPath selector
                    portfolioTabs = driver.findElements(By.xpath(selector));
                } else {
                    // CSS selector
                    portfolioTabs = driver.findElements(By.cssSelector(selector));
                }

                if (!portfolioTabs.isEmpty()) {
                    logger.info("Found Portfolio tab using selector: " + selector);
                    break;
                }
            } catch (Exception e) {
                // Continue to next selector
            }
        }

        return portfolioTabs;
    }

    /**
     * Extract all elements with data-testid attribute
     */
    private List<Map<String, String>> extractDataTestIdElements() {
        List<Map<String, String>> elements = new ArrayList<>();

        try {
            // Find all elements with data-testid attribute
            List<WebElement> dataTestIdElements = driver.findElements(By.cssSelector("[data-testid]"));

            logger.info("Found " + dataTestIdElements.size() + " elements with data-testid attribute");

            for (WebElement element : dataTestIdElements) {
                try {
                    Map<String, String> elementData = new HashMap<>();

                    elementData.put("data-testid", element.getAttribute("data-testid"));
                    elementData.put("tagName", element.getTagName());
                    elementData.put("text", element.getText());
                    elementData.put("type", element.getAttribute("type"));
                    elementData.put("class", element.getAttribute("class"));
                    elementData.put("id", element.getAttribute("id"));
                    elementData.put("placeholder", element.getAttribute("placeholder"));
                    elementData.put("aria-label", element.getAttribute("aria-label"));
                    elementData.put("role", element.getAttribute("role"));
                    elementData.put("visible", String.valueOf(element.isDisplayed()));

                    elements.add(elementData);
                } catch (Exception e) {
                    // Skip stale elements
                }
            }

        } catch (Exception e) {
            logger.error("Error extracting data-testid elements: " + e.getMessage(), e);
        }

        return elements;
    }

    /**
     * Print data-testid elements in table format
     */
    private void printDataTestIdElements(List<Map<String, String>> elements) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    PORTFOLIO TAB - DATA-TESTID ELEMENTS                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.printf("%-5s %-40s %-15s %-30s %-10s%n", "#", "DATA-TESTID", "TAG", "TEXT", "VISIBLE");
        System.out.println("─".repeat(120));

        int index = 1;
        for (Map<String, String> element : elements) {
            String dataTestId = element.get("data-testid");
            String tagName = element.get("tagName");
            String text = element.get("text");
            String visible = element.get("visible");

            // Truncate text if too long
            if (text != null && text.length() > 30) {
                text = text.substring(0, 27) + "...";
            }

            System.out.printf("%-5d %-40s %-15s %-30s %-10s%n",
                index++,
                dataTestId != null ? dataTestId : "",
                tagName != null ? tagName.toUpperCase() : "",
                text != null ? text : "",
                visible != null ? visible : ""
            );
        }

        System.out.println("─".repeat(120));
        System.out.println("Total elements with data-testid: " + elements.size());
        System.out.println();
    }

    /**
     * Export elements to CSV file
     */
    private void exportToCSV(List<Map<String, String>> elements, String filePrefix) {
        try {
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
            String fileName = "data-testid-exports/" + filePrefix + "_" + timestamp + ".csv";

            java.io.File directory = new java.io.File("data-testid-exports");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(fileName));

            // Write header
            writer.println("Index,DataTestId,TagName,Text,Type,Class,ID,Placeholder,AriaLabel,Role,Visible");

            // Write data
            int index = 1;
            for (Map<String, String> element : elements) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    index++,
                    escapeCSV(element.get("data-testid")),
                    escapeCSV(element.get("tagName")),
                    escapeCSV(element.get("text")),
                    escapeCSV(element.get("type")),
                    escapeCSV(element.get("class")),
                    escapeCSV(element.get("id")),
                    escapeCSV(element.get("placeholder")),
                    escapeCSV(element.get("aria-label")),
                    escapeCSV(element.get("role")),
                    escapeCSV(element.get("visible"))
                );
            }

            writer.close();

            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                           CSV EXPORT SUCCESSFUL                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
            System.out.println("\nFile saved to: " + fileName);
            logger.info("CSV export completed successfully. File: " + fileName);

        } catch (Exception e) {
            logger.error("Error exporting to CSV: " + e.getMessage(), e);
            System.err.println("\n✗ CSV export failed: " + e.getMessage());
        }
    }

    /**
     * Escape CSV special characters
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    /**
     * Print available tabs for debugging
     */
    private void printAvailableTabs() {
        try {
            List<WebElement> allButtons = driver.findElements(By.cssSelector("button, a[role='tab']"));
            System.out.println("\nAvailable clickable elements:");
            for (WebElement button : allButtons) {
                String text = button.getText();
                String dataTestId = button.getAttribute("data-testid");
                if (text != null && !text.isEmpty()) {
                    System.out.println("  • Text: \"" + text + "\" | data-testid: " + dataTestId);
                }
            }
        } catch (Exception e) {
            logger.error("Error printing available tabs: " + e.getMessage());
        }
    }

    /**
     * Helper method to print test header
     */
    private void printTestHeader(String title) {
        int totalWidth = 80;
        int padding = (totalWidth - title.length() - 4) / 2;
        System.out.println("\n╔" + "═".repeat(totalWidth) + "╗");
        System.out.println("║" + " ".repeat(padding) + "  " + title + "  " + " ".repeat(padding) + "║");
        System.out.println("╚" + "═".repeat(totalWidth) + "╝");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing WebDriver");
            driver.quit();
        }
    }
}
