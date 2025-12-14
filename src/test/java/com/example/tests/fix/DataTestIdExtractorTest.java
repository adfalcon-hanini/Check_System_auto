package com.example.tests.fix;

import com.example.utils.DataTestIdExtractor;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class to demonstrate DataTestIdExtractor usage
 * Extracts all elements with data-testid attribute from a given URL
 */
public class DataTestIdExtractorTest {

    private static final Logger logger = Logger.getLogger(DataTestIdExtractorTest.class);

    // URL to extract data-testid elements from
    private static final String TARGET_URL = "https://devuat.thegroup.com.qa/uat/ar/analytics";

    @Test(priority = 1, description = "Extract and print all data-testid elements")
    public void testExtractDataTestIdElements() {
        printTestHeader("EXTRACT ALL DATA-TESTID ELEMENTS");

        logger.info("Creating DataTestIdExtractor for URL: " + TARGET_URL);
        DataTestIdExtractor extractor = new DataTestIdExtractor(TARGET_URL);

        logger.info("Extracting data-testid elements from URL");
        List<Map<String, String>> elements = extractor.extractDataTestIdElements();

        logger.info("Successfully extracted " + elements.size() + " elements with data-testid");

        // Print elements in table format
        extractor.printDataTestIdElements(elements);

        // Print summary statistics
        extractor.printSummary();
    }

    @Test(priority = 2, description = "Extract and print detailed data-testid elements")
    public void testExtractDetailedDataTestIdElements() {
        printTestHeader("EXTRACT DETAILED DATA-TESTID ELEMENTS");

        logger.info("Creating DataTestIdExtractor for URL: " + TARGET_URL);
        DataTestIdExtractor extractor = new DataTestIdExtractor(TARGET_URL);

        logger.info("Extracting data-testid elements from URL");
        List<Map<String, String>> elements = extractor.extractDataTestIdElements();

        logger.info("Successfully extracted " + elements.size() + " elements with data-testid");

        // Print detailed information for all elements
        extractor.printDetailedDataTestIdElements(elements);
    }

    @Test(priority = 3, description = "Extract and filter data-testid elements by tag name")
    public void testExtractAndFilterByTagName() {
        printTestHeader("FILTER DATA-TESTID ELEMENTS BY TAG NAME");

        logger.info("Creating DataTestIdExtractor for URL: " + TARGET_URL);
        DataTestIdExtractor extractor = new DataTestIdExtractor(TARGET_URL);

        logger.info("Extracting data-testid elements from URL");
        extractor.extractDataTestIdElements();

        // Get unique tag names
        List<String> uniqueTags = extractor.getUniqueTagNames();
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Unique Tag Names Found                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        for (String tag : uniqueTags) {
            System.out.println("  • " + tag);
        }

        // Filter by specific tag names
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Filter by Tag Name: BUTTON                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        List<Map<String, String>> buttons = extractor.getElementsByTagName("button");
        System.out.println("Found " + buttons.size() + " button elements with data-testid");
        for (Map<String, String> button : buttons) {
            System.out.println("  • " + button.get("data-testid") + " - Text: \"" + button.get("text") + "\"");
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 Filter by Tag Name: INPUT                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        List<Map<String, String>> inputs = extractor.getElementsByTagName("input");
        System.out.println("Found " + inputs.size() + " input elements with data-testid");
        for (Map<String, String> input : inputs) {
            System.out.println("  • " + input.get("data-testid") +
                             " - Type: " + input.get("type") +
                             ", Placeholder: \"" + input.get("placeholder") + "\"");
        }
    }

    @Test(priority = 4, description = "Extract and export data-testid elements to CSV file")
    public void testExtractAndExportToCSV() {
        printTestHeader("EXPORT DATA-TESTID ELEMENTS TO CSV FILE");

        logger.info("Creating DataTestIdExtractor for URL: " + TARGET_URL);
        DataTestIdExtractor extractor = new DataTestIdExtractor(TARGET_URL);

        logger.info("Extracting data-testid elements from URL");
        extractor.extractDataTestIdElements();

        logger.info("Exporting elements to CSV file with auto-generated filename");
        String csvFilePath = extractor.exportToCSVFile();

        if (csvFilePath != null) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     CSV Export Successful                      ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println("\n  File saved to: " + csvFilePath);
            logger.info("CSV export completed successfully. File: " + csvFilePath);
        } else {
            System.err.println("\n✗ CSV export failed");
            logger.error("CSV export failed");
        }

        // Also test exporting to a custom file path
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Export to Custom File Path                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        String customFilePath = "data-testid-exports/custom-export.csv";
        boolean customSuccess = extractor.exportToCSVFile(customFilePath);

        if (customSuccess) {
            System.out.println("✓ Custom CSV export successful");
            logger.info("Custom CSV export completed successfully");
        } else {
            System.err.println("✗ Custom CSV export failed");
            logger.error("Custom CSV export failed");
        }
    }

    @Test(priority = 5, description = "Extract data-testid elements from custom URL")
    public void testExtractFromCustomURL() {
        // You can change this URL to any page you want to extract data-testid elements from
        String customURL = "https://devuat.thegroup.com.qa/uat/en/trading";

        printTestHeader("EXTRACT FROM CUSTOM URL");
        System.out.println("Custom URL: " + customURL);

        logger.info("Creating DataTestIdExtractor for custom URL: " + customURL);
        DataTestIdExtractor extractor = new DataTestIdExtractor(customURL);

        logger.info("Extracting data-testid elements from custom URL");
        extractor.extractAndPrintDataTestIdElements();
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
}
