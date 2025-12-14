package com.example.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to extract all elements with data-testid attribute from a given URL
 */
public class DataTestIdExtractor {

    private static final Logger logger = Logger.getLogger(DataTestIdExtractor.class);
    private WebDriver driver;
    private String url;
    private List<Map<String, String>> elementsWithDataTestId;

    /**
     * Constructor
     * @param url URL to extract data-testid elements from
     */
    public DataTestIdExtractor(String url) {
        this.url = url;
        this.elementsWithDataTestId = new ArrayList<>();
    }

    /**
     * Initialize the WebDriver
     */
    private void initializeDriver() {
        try {
            logger.info("Initializing Chrome WebDriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            // Uncomment for headless mode
            // options.addArguments("--headless");

            driver = new ChromeDriver(options);
            logger.info("Chrome WebDriver initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing WebDriver: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    /**
     * Extract all elements with data-testid attribute from the URL
     * @return List of maps containing element information
     */
    public List<Map<String, String>> extractDataTestIdElements() {
        try {
            initializeDriver();

            logger.info("Navigating to URL: " + url);
            driver.get(url);

            // Wait for page to load
            Thread.sleep(3000);

            logger.info("Finding all elements with data-testid attribute");
            List<WebElement> elements = driver.findElements(By.cssSelector("[data-testid]"));

            logger.info("Found " + elements.size() + " elements with data-testid attribute");

            for (WebElement element : elements) {
                Map<String, String> elementInfo = new HashMap<>();

                try {
                    // Get data-testid value
                    String dataTestId = element.getAttribute("data-testid");
                    elementInfo.put("data-testid", dataTestId);

                    // Get tag name
                    String tagName = element.getTagName();
                    elementInfo.put("tagName", tagName);

                    // Get element text (if any)
                    String text = element.getText();
                    elementInfo.put("text", text != null && !text.isEmpty() ? text : "");

                    // Get element type (for input elements)
                    String type = element.getAttribute("type");
                    elementInfo.put("type", type != null ? type : "");

                    // Get element class
                    String className = element.getAttribute("class");
                    elementInfo.put("class", className != null ? className : "");

                    // Get element id
                    String id = element.getAttribute("id");
                    elementInfo.put("id", id != null ? id : "");

                    // Get element name
                    String name = element.getAttribute("name");
                    elementInfo.put("name", name != null ? name : "");

                    // Get element placeholder (for input elements)
                    String placeholder = element.getAttribute("placeholder");
                    elementInfo.put("placeholder", placeholder != null ? placeholder : "");

                    // Check if element is displayed
                    boolean isDisplayed = element.isDisplayed();
                    elementInfo.put("isDisplayed", String.valueOf(isDisplayed));

                    // Check if element is enabled
                    boolean isEnabled = element.isEnabled();
                    elementInfo.put("isEnabled", String.valueOf(isEnabled));

                    elementsWithDataTestId.add(elementInfo);

                } catch (Exception e) {
                    logger.warn("Error extracting information for element with data-testid: " + e.getMessage());
                }
            }

            logger.info("Successfully extracted information for " + elementsWithDataTestId.size() + " elements");
            return elementsWithDataTestId;

        } catch (Exception e) {
            logger.error("Error extracting data-testid elements: " + e.getMessage(), e);
            throw new RuntimeException("Failed to extract data-testid elements", e);
        } finally {
            closeDriver();
        }
    }

    /**
     * Extract and print all elements with data-testid attribute
     */
    public void extractAndPrintDataTestIdElements() {
        List<Map<String, String>> elements = extractDataTestIdElements();
        printDataTestIdElements(elements);
    }

    /**
     * Print all extracted elements in a formatted table
     */
    public void printDataTestIdElements(List<Map<String, String>> elements) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                          Elements with data-testid from: " + url);
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-40s ║ %-12s ║ %-30s ║ %-15s ║ %-10s ║%n",
                "data-testid", "Tag", "Text", "Type", "Displayed");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, String> element : elements) {
            String dataTestId = truncate(element.get("data-testid"), 40);
            String tagName = truncate(element.get("tagName"), 12);
            String text = truncate(element.get("text"), 30);
            String type = truncate(element.get("type"), 15);
            String isDisplayed = element.get("isDisplayed");

            System.out.printf("║ %-40s ║ %-12s ║ %-30s ║ %-15s ║ %-10s ║%n",
                    dataTestId, tagName, text, type, isDisplayed);
        }

        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total elements found: " + elements.size());
    }

    /**
     * Print all extracted elements with full details
     */
    public void printDetailedDataTestIdElements(List<Map<String, String>> elements) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Detailed Elements with data-testid from: " + url);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < elements.size(); i++) {
            Map<String, String> element = elements.get(i);
            System.out.println("─────────────────── Element " + (i + 1) + " ───────────────────");
            System.out.println("  data-testid:  " + element.get("data-testid"));
            System.out.println("  Tag Name:     " + element.get("tagName"));
            System.out.println("  Text:         " + element.get("text"));
            System.out.println("  Type:         " + element.get("type"));
            System.out.println("  Class:        " + element.get("class"));
            System.out.println("  ID:           " + element.get("id"));
            System.out.println("  Name:         " + element.get("name"));
            System.out.println("  Placeholder:  " + element.get("placeholder"));
            System.out.println("  Is Displayed: " + element.get("isDisplayed"));
            System.out.println("  Is Enabled:   " + element.get("isEnabled"));
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Total elements found: " + elements.size());
    }

    /**
     * Export elements to CSV format (console output)
     */
    public void exportToCSV() {
        System.out.println("\nCSV Export:");
        System.out.println("data-testid,tagName,text,type,class,id,name,placeholder,isDisplayed,isEnabled");

        for (Map<String, String> element : elementsWithDataTestId) {
            System.out.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    escapeCSV(element.get("data-testid")),
                    escapeCSV(element.get("tagName")),
                    escapeCSV(element.get("text")),
                    escapeCSV(element.get("type")),
                    escapeCSV(element.get("class")),
                    escapeCSV(element.get("id")),
                    escapeCSV(element.get("name")),
                    escapeCSV(element.get("placeholder")),
                    element.get("isDisplayed"),
                    element.get("isEnabled"));
        }
    }

    /**
     * Export elements to CSV file
     * @param filePath Path to save the CSV file
     * @return true if export successful, false otherwise
     */
    public boolean exportToCSVFile(String filePath) {
        try {
            File file = new File(filePath);

            // Create parent directories if they don't exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write("data-testid,tagName,text,type,class,id,name,placeholder,isDisplayed,isEnabled");
                writer.newLine();

                // Write data rows
                for (Map<String, String> element : elementsWithDataTestId) {
                    String csvLine = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                            escapeCSV(element.get("data-testid")),
                            escapeCSV(element.get("tagName")),
                            escapeCSV(element.get("text")),
                            escapeCSV(element.get("type")),
                            escapeCSV(element.get("class")),
                            escapeCSV(element.get("id")),
                            escapeCSV(element.get("name")),
                            escapeCSV(element.get("placeholder")),
                            element.get("isDisplayed"),
                            element.get("isEnabled"));
                    writer.write(csvLine);
                    writer.newLine();
                }

                logger.info("CSV file exported successfully to: " + file.getAbsolutePath());
                System.out.println("\n✓ CSV file exported successfully to: " + file.getAbsolutePath());
                System.out.println("  Total records: " + elementsWithDataTestId.size());
                return true;

            } catch (IOException e) {
                logger.error("Error writing to CSV file: " + e.getMessage(), e);
                System.err.println("✗ Error writing to CSV file: " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            logger.error("Error exporting to CSV file: " + e.getMessage(), e);
            System.err.println("✗ Error exporting to CSV file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export elements to CSV file with auto-generated filename
     * @return Path to the generated CSV file, or null if export failed
     */
    public String exportToCSVFile() {
        try {
            // Create data-testid-exports directory if it doesn't exist
            String exportDir = "data-testid-exports";
            File dir = new File(exportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            String fileName = "data-testid-elements_" + timestamp + ".csv";
            String filePath = exportDir + File.separator + fileName;

            // Export to file
            boolean success = exportToCSVFile(filePath);
            return success ? filePath : null;

        } catch (Exception e) {
            logger.error("Error generating CSV file: " + e.getMessage(), e);
            System.err.println("✗ Error generating CSV file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get elements filtered by tag name
     * @param tagName Tag name to filter by
     * @return Filtered list of elements
     */
    public List<Map<String, String>> getElementsByTagName(String tagName) {
        List<Map<String, String>> filtered = new ArrayList<>();
        for (Map<String, String> element : elementsWithDataTestId) {
            if (element.get("tagName").equalsIgnoreCase(tagName)) {
                filtered.add(element);
            }
        }
        return filtered;
    }

    /**
     * Get all unique tag names
     * @return List of unique tag names
     */
    public List<String> getUniqueTagNames() {
        List<String> tagNames = new ArrayList<>();
        for (Map<String, String> element : elementsWithDataTestId) {
            String tagName = element.get("tagName");
            if (!tagNames.contains(tagName)) {
                tagNames.add(tagName);
            }
        }
        return tagNames;
    }

    /**
     * Print summary statistics
     */
    public void printSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     Summary Statistics                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        System.out.println("\nURL: " + url);
        System.out.println("Total elements with data-testid: " + elementsWithDataTestId.size());

        // Count by tag name
        Map<String, Integer> tagCount = new HashMap<>();
        for (Map<String, String> element : elementsWithDataTestId) {
            String tagName = element.get("tagName");
            tagCount.put(tagName, tagCount.getOrDefault(tagName, 0) + 1);
        }

        System.out.println("\nElements by Tag Name:");
        for (Map.Entry<String, Integer> entry : tagCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        // Count visible vs hidden
        int visibleCount = 0;
        int hiddenCount = 0;
        for (Map<String, String> element : elementsWithDataTestId) {
            if ("true".equals(element.get("isDisplayed"))) {
                visibleCount++;
            } else {
                hiddenCount++;
            }
        }

        System.out.println("\nVisibility:");
        System.out.println("  Visible: " + visibleCount);
        System.out.println("  Hidden: " + hiddenCount);
    }

    /**
     * Get all extracted elements
     * @return List of all elements
     */
    public List<Map<String, String>> getAllElements() {
        return elementsWithDataTestId;
    }

    /**
     * Close the WebDriver
     */
    private void closeDriver() {
        if (driver != null) {
            try {
                logger.info("Closing WebDriver");
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error closing WebDriver: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Truncate string to specified length
     */
    private String truncate(String str, int maxLength) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Escape CSV special characters
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }
}
