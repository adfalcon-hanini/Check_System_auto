package com.example.listeners;

import com.example.utils.ConfigReader;
import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Automatically retry failed tests
 * Configurable retry count from config.properties
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = Logger.getLogger(RetryAnalyzer.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private int retryCount = 0;
    private int maxRetryCount;

    public RetryAnalyzer() {
        this.maxRetryCount = config.getMaxRetryCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (!config.shouldRetryFailedTests()) {
            return false;
        }

        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.info("Retrying test: " + result.getName() + " - Attempt " + retryCount + " of " + maxRetryCount);
            return true;
        }

        logger.info("Max retry count reached for test: " + result.getName());
        return false;
    }

    /**
     * Reset retry count
     */
    public void resetRetryCount() {
        retryCount = 0;
    }

    /**
     * Get current retry count
     * @return Current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
}
