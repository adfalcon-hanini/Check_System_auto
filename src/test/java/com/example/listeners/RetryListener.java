package com.example.listeners;

import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryListener - Automatically applies RetryAnalyzer to all test methods
 * This enables retry functionality for all tests without needing to manually add it to each test
 */
public class RetryListener implements IAnnotationTransformer {

    private static final Logger logger = Logger.getLogger(RetryListener.class);

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Apply RetryAnalyzer to all test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);

        if (testMethod != null) {
            logger.debug("Applied RetryAnalyzer to test: " + testMethod.getName());
        }
    }
}
