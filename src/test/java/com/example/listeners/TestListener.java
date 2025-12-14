package com.example.listeners;

import com.example.base.BaseTest;
import com.example.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Field;

/**
 * TestListener - TestNG listener for test execution events
 * Handles screenshots on failure, logging, and Allure reporting
 */
public class TestListener implements ITestListener {

    private static final Logger logger = Logger.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Started: " + context.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished: " + context.getName());
        logger.info("Total Tests Run: " + context.getAllTestMethods().length);
        logger.info("Passed: " + context.getPassedTests().size());
        logger.info("Failed: " + context.getFailedTests().size());
        logger.info("Skipped: " + context.getSkippedTests().size());
        logger.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("─────────────────────────────────────────");
        logger.info("Test Started: " + result.getMethod().getMethodName());
        logger.info("Test Class: " + result.getTestClass().getName());
        logger.info("─────────────────────────────────────────");

        Allure.step("Test " + result.getMethod().getMethodName() + " started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("─────────────────────────────────────────");
        logger.info("✓ Test Passed: " + result.getMethod().getMethodName());
        logger.info("Execution Time: " + getExecutionTime(result) + "ms");
        logger.info("─────────────────────────────────────────");

        // Capture screenshot on success if configured
        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            ScreenshotUtil.captureSuccessScreenshot(driver, result.getMethod().getMethodName());
        }

        Allure.step("Test " + result.getMethod().getMethodName() + " passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("─────────────────────────────────────────");
        logger.error("✗ Test Failed: " + result.getMethod().getMethodName());
        logger.error("Failure Reason: " + result.getThrowable().getMessage());
        logger.error("Execution Time: " + getExecutionTime(result) + "ms");
        logger.error("─────────────────────────────────────────");

        // Log stack trace
        result.getThrowable().printStackTrace();

        // Capture screenshot on failure
        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            String screenshotPath = ScreenshotUtil.captureFailureScreenshot(
                driver, result.getMethod().getMethodName()
            );
            logger.info("Failure screenshot captured: " + screenshotPath);
        }

        // Add failure details to Allure
        Allure.step("Test " + result.getMethod().getMethodName() + " failed");
        Allure.addAttachment("Failure Reason", result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("─────────────────────────────────────────");
        logger.warn("⊘ Test Skipped: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            logger.warn("Skip Reason: " + result.getThrowable().getMessage());
        }
        logger.warn("─────────────────────────────────────────");

        Allure.step("Test " + result.getMethod().getMethodName() + " skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.error("─────────────────────────────────────────");
        logger.error("⏱ Test Failed with Timeout: " + result.getMethod().getMethodName());
        logger.error("Timeout Reason: " + result.getThrowable().getMessage());
        logger.error("─────────────────────────────────────────");

        onTestFailure(result);
    }

    /**
     * Get WebDriver instance from test class
     * @param result Test result
     * @return WebDriver instance or null
     */
    private WebDriver getDriverFromTest(ITestResult result) {
        Object testInstance = result.getInstance();

        try {
            // Try to get driver field from test class (assumes extends BaseTest)
            if (testInstance instanceof BaseTest) {
                Field driverField = BaseTest.class.getDeclaredField("driver");
                driverField.setAccessible(true);
                return (WebDriver) driverField.get(testInstance);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.warn("Could not access driver field: " + e.getMessage());
        }

        return null;
    }

    /**
     * Calculate test execution time
     * @param result Test result
     * @return Execution time in milliseconds
     */
    private long getExecutionTime(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
