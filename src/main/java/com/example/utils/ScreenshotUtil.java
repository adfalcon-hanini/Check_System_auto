package com.example.utils;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Utility class for capturing and managing screenshots
 * Supports automatic screenshot capture on test failure
 */
public class ScreenshotUtil {

    private static final Logger logger = Logger.getLogger(ScreenshotUtil.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Capture screenshot and save to file
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name
     * @return Screenshot file path
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Create screenshots directory if not exists
            String screenshotDir = config.getScreenshotPath();
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = screenshotDir + fileName;

            // Capture screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);

            // Copy screenshot to destination
            FileUtils.copyFile(source, destination);

            logger.info("Screenshot captured: " + filePath);
            return filePath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return "";
        }
    }

    /**
     * Capture screenshot and attach to Allure report
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name for Allure
     */
    public static void captureScreenshotForAllure(WebDriver driver, String screenshotName) {
        try {
            // Capture screenshot as bytes
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            // Attach to Allure report
            Allure.addAttachment(screenshotName, "image/png",
                                new ByteArrayInputStream(screenshot), "png");

            logger.info("Screenshot attached to Allure report: " + screenshotName);

        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure: " + e.getMessage());
        }
    }

    /**
     * Capture screenshot and save to both file and Allure
     * @param driver WebDriver instance
     * @param screenshotName Screenshot name
     * @return Screenshot file path
     */
    public static String captureScreenshotWithAllure(WebDriver driver, String screenshotName) {
        String filePath = captureScreenshot(driver, screenshotName);
        captureScreenshotForAllure(driver, screenshotName);
        return filePath;
    }

    /**
     * Capture screenshot on test failure
     * @param driver WebDriver instance
     * @param testMethodName Test method name
     * @return Screenshot file path
     */
    public static String captureFailureScreenshot(WebDriver driver, String testMethodName) {
        if (config.shouldTakeScreenshotOnFailure()) {
            String screenshotName = "FAILURE_" + testMethodName;
            return captureScreenshotWithAllure(driver, screenshotName);
        }
        return "";
    }

    /**
     * Capture screenshot on test success
     * @param driver WebDriver instance
     * @param testMethodName Test method name
     * @return Screenshot file path
     */
    public static String captureSuccessScreenshot(WebDriver driver, String testMethodName) {
        if (config.shouldTakeScreenshotOnSuccess()) {
            String screenshotName = "SUCCESS_" + testMethodName;
            return captureScreenshotWithAllure(driver, screenshotName);
        }
        return "";
    }

    /**
     * Get screenshot as Base64 string
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot
     */
    public static String getScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to get screenshot as Base64: " + e.getMessage());
            return "";
        }
    }

    /**
     * Clean old screenshots (older than specified days)
     * @param days Number of days to keep
     */
    public static void cleanOldScreenshots(int days) {
        try {
            String screenshotDir = config.getScreenshotPath();
            File directory = new File(screenshotDir);

            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    long cutoffTime = System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000);

                    for (File file : files) {
                        if (file.lastModified() < cutoffTime) {
                            if (file.delete()) {
                                logger.info("Deleted old screenshot: " + file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error cleaning old screenshots: " + e.getMessage());
        }
    }
}
