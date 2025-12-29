package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AllurePDFReportGenerator {

    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(41, 128, 185);
    private static final DeviceRgb SUCCESS_COLOR = new DeviceRgb(39, 174, 96);
    private static final DeviceRgb FAILURE_COLOR = new DeviceRgb(231, 76, 60);
    private static final DeviceRgb WARNING_COLOR = new DeviceRgb(241, 196, 15);

    public static void generateReport(String allureResultsPath, String outputPath) {
        try {
            System.out.println("Starting PDF report generation...");
            System.out.println("Reading Allure results from: " + allureResultsPath);

            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Parse Allure results
            Map<String, Object> testResults = parseAllureResults(allureResultsPath);

            // Add Title
            addTitle(document);

            // Add Executive Summary
            addExecutiveSummary(document, testResults);

            // Add Test Suite Summary
            addTestSuiteSummary(document, testResults);

            // Add Detailed Test Results
            addDetailedResults(document, testResults);

            // Add Environment Information
            addEnvironmentInfo(document, allureResultsPath);

            document.close();
            System.out.println("PDF report generated successfully: " + outputPath);

        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addTitle(Document document) {
        Paragraph title = new Paragraph("Check System Auto Tool")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(title);

        Paragraph subtitle = new Paragraph("Test Execution Report")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitle);

        Paragraph date = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(date);
    }

    private static void addExecutiveSummary(Document document, Map<String, Object> results) {
        int total = (int) results.get("total");
        int passed = (int) results.get("passed");
        int failed = (int) results.get("failed");
        int broken = (int) results.get("broken");
        int skipped = (int) results.get("skipped");
        long duration = (long) results.get("duration");

        Paragraph header = new Paragraph("Executive Summary")
                .setFontSize(18)
                .setBold()
                .setMarginBottom(15);
        document.add(header);

        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth();

        addSummaryRow(summaryTable, "Total Tests", String.valueOf(total), ColorConstants.BLACK);
        addSummaryRow(summaryTable, "Passed", String.valueOf(passed), SUCCESS_COLOR);
        addSummaryRow(summaryTable, "Failed", String.valueOf(failed), failed > 0 ? FAILURE_COLOR : ColorConstants.BLACK);
        addSummaryRow(summaryTable, "Broken", String.valueOf(broken), broken > 0 ? WARNING_COLOR : ColorConstants.BLACK);
        addSummaryRow(summaryTable, "Skipped", String.valueOf(skipped), skipped > 0 ? WARNING_COLOR : ColorConstants.BLACK);
        addSummaryRow(summaryTable, "Success Rate", String.format("%.2f%%", (passed * 100.0 / total)),
                      passed == total ? SUCCESS_COLOR : WARNING_COLOR);
        addSummaryRow(summaryTable, "Total Duration", formatDuration(duration), ColorConstants.BLACK);

        document.add(summaryTable);
        document.add(new Paragraph("\n"));
    }

    private static void addSummaryRow(Table table, String label, String value, Color color) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setBold())
                .setBorder(Border.NO_BORDER)
                .setPadding(5));
        table.addCell(new Cell()
                .add(new Paragraph(value).setFontColor(color))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(TextAlignment.RIGHT));
    }

    private static void addTestSuiteSummary(Document document, Map<String, Object> results) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Integer>> suites = (Map<String, Map<String, Integer>>) results.get("suites");

        Paragraph header = new Paragraph("Test Suite Summary")
                .setFontSize(18)
                .setBold()
                .setMarginTop(20)
                .setMarginBottom(15);
        document.add(header);

        Table suiteTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1, 1}))
                .useAllAvailableWidth();

        // Header
        addHeaderCell(suiteTable, "Test Suite");
        addHeaderCell(suiteTable, "Total");
        addHeaderCell(suiteTable, "Passed");
        addHeaderCell(suiteTable, "Failed");
        addHeaderCell(suiteTable, "Success Rate");

        // Data rows
        for (Map.Entry<String, Map<String, Integer>> entry : suites.entrySet()) {
            String suiteName = entry.getKey();
            Map<String, Integer> stats = entry.getValue();
            int total = stats.get("total");
            int passed = stats.get("passed");
            int failed = stats.get("failed");
            double successRate = (passed * 100.0 / total);

            suiteTable.addCell(createCell(suiteName, TextAlignment.LEFT));
            suiteTable.addCell(createCell(String.valueOf(total), TextAlignment.CENTER));
            suiteTable.addCell(createCell(String.valueOf(passed), TextAlignment.CENTER)
                    .setFontColor(SUCCESS_COLOR));
            suiteTable.addCell(createCell(String.valueOf(failed), TextAlignment.CENTER)
                    .setFontColor(failed > 0 ? FAILURE_COLOR : ColorConstants.BLACK));
            suiteTable.addCell(createCell(String.format("%.1f%%", successRate), TextAlignment.CENTER)
                    .setFontColor(successRate == 100 ? SUCCESS_COLOR : WARNING_COLOR));
        }

        document.add(suiteTable);
        document.add(new Paragraph("\n"));
    }

    private static void addDetailedResults(Document document, Map<String, Object> results) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tests = (List<Map<String, Object>>) results.get("tests");

        Paragraph header = new Paragraph("Detailed Test Results")
                .setFontSize(18)
                .setBold()
                .setMarginTop(20)
                .setMarginBottom(15);
        document.add(header);

        // Group tests by status
        Map<String, List<Map<String, Object>>> groupedTests = tests.stream()
                .collect(Collectors.groupingBy(t -> (String) t.get("status")));

        // Show failed tests first
        if (groupedTests.containsKey("failed")) {
            addTestGroup(document, "Failed Tests", groupedTests.get("failed"), FAILURE_COLOR);
        }

        // Then broken tests
        if (groupedTests.containsKey("broken")) {
            addTestGroup(document, "Broken Tests", groupedTests.get("broken"), WARNING_COLOR);
        }

        // Show detailed table of passed tests
        if (groupedTests.containsKey("passed")) {
            addTestGroup(document, "Passed Tests", groupedTests.get("passed"), SUCCESS_COLOR);
        }
    }

    private static void addTestGroup(Document document, String groupName, List<Map<String, Object>> tests, Color color) {
        Paragraph groupHeader = new Paragraph(groupName + " (" + tests.size() + ")")
                .setFontSize(14)
                .setBold()
                .setFontColor(color)
                .setMarginTop(10);
        document.add(groupHeader);

        Table testTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 1, 1, 1}))
                .useAllAvailableWidth();

        addHeaderCell(testTable, "Test Name");
        addHeaderCell(testTable, "Class Name");
        addHeaderCell(testTable, "Status");
        addHeaderCell(testTable, "Status Code");
        addHeaderCell(testTable, "Duration");

        for (Map<String, Object> test : tests) {
            String name = (String) test.get("name");
            String className = (String) test.getOrDefault("className", "N/A");
            String status = (String) test.get("status");
            String statusCode = (String) test.getOrDefault("statusCode", "-");
            long duration = ((Number) test.get("duration")).longValue();

            testTable.addCell(createCell(name, TextAlignment.LEFT));
            testTable.addCell(createCell(className, TextAlignment.LEFT));
            testTable.addCell(createCell(status.toUpperCase(), TextAlignment.CENTER)
                    .setFontColor(color));
            testTable.addCell(createCell(statusCode, TextAlignment.CENTER)
                    .setFontColor(statusCode.equals("200") ? SUCCESS_COLOR : ColorConstants.BLACK));
            testTable.addCell(createCell(formatDuration(duration), TextAlignment.CENTER));
        }

        document.add(testTable);
        document.add(new Paragraph("\n"));
    }

    private static void addEnvironmentInfo(Document document, String allureResultsPath) {
        try {
            String envFile = allureResultsPath + File.separator + "environment.properties";
            if (new File(envFile).exists()) {
                Paragraph header = new Paragraph("Environment Information")
                        .setFontSize(18)
                        .setBold()
                        .setMarginTop(20)
                        .setMarginBottom(15);
                document.add(header);

                Properties props = new Properties();
                props.load(new FileReader(envFile));

                Table envTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                        .useAllAvailableWidth();

                for (String key : props.stringPropertyNames()) {
                    envTable.addCell(createCell(key, TextAlignment.LEFT).setBold());
                    envTable.addCell(createCell(props.getProperty(key), TextAlignment.LEFT));
                }

                document.add(envTable);
            }
        } catch (Exception e) {
            System.err.println("Could not load environment info: " + e.getMessage());
        }
    }

    private static void addHeaderCell(Table table, String text) {
        table.addHeaderCell(new Cell()
                .add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8));
    }

    private static Cell createCell(String text, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(text))
                .setTextAlignment(alignment)
                .setPadding(5)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
    }

    private static String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    private static Map<String, Object> parseAllureResults(String allureResultsPath) throws Exception {
        Map<String, Object> results = new HashMap<>();
        Gson gson = new Gson();

        File resultsDir = new File(allureResultsPath);
        File[] jsonFiles = resultsDir.listFiles((dir, name) -> name.endsWith("-result.json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            throw new Exception("No Allure result files found in: " + allureResultsPath);
        }

        int total = 0, passed = 0, failed = 0, broken = 0, skipped = 0;
        long totalDuration = 0;
        Map<String, Map<String, Integer>> suites = new LinkedHashMap<>();
        List<Map<String, Object>> tests = new ArrayList<>();

        for (File jsonFile : jsonFiles) {
            String content = new String(Files.readAllBytes(jsonFile.toPath()));
            JsonObject testResult = gson.fromJson(content, JsonObject.class);

            // Skip container files and files without status/name
            if (!testResult.has("status") || !testResult.has("name")) {
                continue;
            }

            String status = testResult.get("status").getAsString();
            String name = testResult.get("name").getAsString();
            long duration = testResult.has("stop") && testResult.has("start")
                    ? testResult.get("stop").getAsLong() - testResult.get("start").getAsLong()
                    : 0;

            total++;
            totalDuration += duration;

            switch (status) {
                case "passed": passed++; break;
                case "failed": failed++; break;
                case "broken": broken++; break;
                case "skipped": skipped++; break;
            }

            // Extract suite name and class name from labels
            String suiteName = "Unknown Suite";
            String className = "N/A";
            if (testResult.has("labels")) {
                JsonArray labels = testResult.getAsJsonArray("labels");
                for (int i = 0; i < labels.size(); i++) {
                    JsonObject label = labels.get(i).getAsJsonObject();
                    String labelName = label.get("name").getAsString();
                    String labelValue = label.get("value").getAsString();

                    if (labelName.equals("suite")) {
                        suiteName = labelValue;
                    } else if (labelName.equals("testClass")) {
                        // Extract simple class name (remove package)
                        String[] parts = labelValue.split("\\.");
                        className = parts[parts.length - 1];
                    }
                }
            }

            // Extract status code from parameters
            String statusCode = "-";
            if (testResult.has("parameters")) {
                JsonArray parameters = testResult.getAsJsonArray("parameters");
                for (int i = 0; i < parameters.size(); i++) {
                    JsonObject param = parameters.get(i).getAsJsonObject();
                    if (param.has("name") && param.get("name").getAsString().equals("statusCode")) {
                        statusCode = param.get("value").getAsString();
                        break;
                    }
                }
            }

            // Update suite statistics
            suites.putIfAbsent(suiteName, new HashMap<>());
            Map<String, Integer> suiteStats = suites.get(suiteName);
            suiteStats.put("total", suiteStats.getOrDefault("total", 0) + 1);
            suiteStats.put("passed", suiteStats.getOrDefault("passed", 0) + (status.equals("passed") ? 1 : 0));
            suiteStats.put("failed", suiteStats.getOrDefault("failed", 0) + (status.equals("failed") ? 1 : 0));

            // Add test details
            Map<String, Object> testInfo = new HashMap<>();
            testInfo.put("name", name);
            testInfo.put("className", className);
            testInfo.put("status", status);
            testInfo.put("statusCode", statusCode);
            testInfo.put("duration", duration);
            testInfo.put("suite", suiteName);
            tests.add(testInfo);
        }

        results.put("total", total);
        results.put("passed", passed);
        results.put("failed", failed);
        results.put("broken", broken);
        results.put("skipped", skipped);
        results.put("duration", totalDuration);
        results.put("suites", suites);
        results.put("tests", tests);

        return results;
    }

    public static void main(String[] args) {
        String allureResultsPath = args.length > 0 ? args[0] : "target/allure-results";
        String outputPath = args.length > 1 ? args[1] : "Check-System-Auto-Report.pdf";

        generateReport(allureResultsPath, outputPath);
    }
}
