package com.example.listeners;

import com.example.utils.AllurePDFReportGenerator;
import org.testng.ISuiteListener;
import org.testng.ISuite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener that automatically generates PDF report after test suite execution
 *
 * This listener is triggered when the test suite finishes and generates a PDF report
 * from the Allure results in target/allure-results directory.
 */
public class PDFReportListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        // No action needed on suite start
    }

    @Override
    public void onFinish(ISuite suite) {
        try {
            System.out.println("\n=========================================================================");
            System.out.println("  Generating PDF Report...");
            System.out.println("=========================================================================\n");

            String allureResultsPath = "target/allure-results";

            // Check if allure results exist
            File allureResultsDir = new File(allureResultsPath);
            if (!allureResultsDir.exists() || !allureResultsDir.isDirectory()) {
                System.err.println("[WARNING] Allure results directory not found: " + allureResultsPath);
                System.err.println("[WARNING] Skipping PDF report generation");
                return;
            }

            // Count result files
            File[] resultFiles = allureResultsDir.listFiles((dir, name) -> name.endsWith("-result.json"));
            if (resultFiles == null || resultFiles.length == 0) {
                System.err.println("[WARNING] No test results found in: " + allureResultsPath);
                System.err.println("[WARNING] Skipping PDF report generation");
                return;
            }

            System.out.println("[INFO] Found " + resultFiles.length + " test result files");

            // Generate timestamped filename
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String outputPath = "Oracle-DB-Test-Report-" + timestamp + ".pdf";

            // Generate PDF report
            System.out.println("[INFO] Generating PDF report: " + outputPath);
            AllurePDFReportGenerator.generateReport(allureResultsPath, outputPath);

            System.out.println("\n[SUCCESS] PDF Report generated successfully!");
            System.out.println("[INFO] Location: " + new File(outputPath).getAbsolutePath());
            System.out.println("\n=========================================================================");
            System.out.println("  Test Execution Completed");
            System.out.println("=========================================================================");
            System.out.println("  HTML Report: generate-allure-report.bat");
            System.out.println("  PDF Report:  " + outputPath);
            System.out.println("=========================================================================\n");

        } catch (Exception e) {
            System.err.println("\n[ERROR] Failed to generate PDF report: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nYou can manually generate the PDF report using:");
            System.err.println("  generate-pdf-report.bat\n");
        }
    }
}
