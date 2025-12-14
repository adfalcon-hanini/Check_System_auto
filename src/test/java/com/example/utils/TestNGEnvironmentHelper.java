package com.example.utils;

import org.apache.log4j.Logger;
import org.testng.ITestContext;

/**
 * Helper class to integrate EnvironmentConfigManager with TestNG
 * Provides utility methods for loading configuration from TestNG context
 *
 * Usage:
 * <pre>
 * {@code @BeforeClass}
 * {@code @Parameters("environment")}
 * public void setup(@Optional("qa") String env, ITestContext context) {
 *     TestNGEnvironmentHelper.loadEnvironmentFromTestNG(context);
 *     // OR
 *     TestNGEnvironmentHelper.loadEnvironmentFromParameter(env);
 * }
 * </pre>
 *
 * @author Test Automation Team
 */
public class TestNGEnvironmentHelper {

    private static final Logger logger = Logger.getLogger(TestNGEnvironmentHelper.class);

    /**
     * Load environment configuration from TestNG context parameter
     * Reads the "environment" parameter from testng.xml
     *
     * @param context TestNG ITestContext
     */
    public static void loadEnvironmentFromTestNG(ITestContext context) {
        String environment = context.getCurrentXmlTest().getParameter("environment");

        if (environment != null && !environment.isEmpty()) {
            logger.info("Loading environment from TestNG parameter: " + environment);
            EnvironmentConfigManager.getInstance().loadEnvironmentConfig(environment);
        } else {
            logger.warn("No environment parameter found in TestNG XML, loading default");
            EnvironmentConfigManager.getInstance().loadEnvironmentConfigFromSystemProperty();
        }
    }

    /**
     * Load environment configuration from TestNG parameter (direct)
     * Use when you have the parameter value directly
     *
     * @param environment Environment name from @Parameters
     */
    public static void loadEnvironmentFromParameter(String environment) {
        if (environment != null && !environment.isEmpty()) {
            logger.info("Loading environment from parameter: " + environment);
            EnvironmentConfigManager.getInstance().loadEnvironmentConfig(environment);
        } else {
            logger.warn("Environment parameter is null/empty, loading from system property");
            EnvironmentConfigManager.getInstance().loadEnvironmentConfigFromSystemProperty();
        }
    }

    /**
     * Load environment configuration with fallback chain:
     * 1. Try TestNG context parameter
     * 2. Try @Parameters value
     * 3. Try system property
     * 4. Use default (qa)
     *
     * @param context TestNG ITestContext
     * @param paramValue Value from @Parameters annotation
     */
    public static void loadEnvironmentWithFallback(ITestContext context, String paramValue) {
        // Try TestNG context parameter first
        String envFromContext = context.getCurrentXmlTest().getParameter("environment");

        if (envFromContext != null && !envFromContext.isEmpty()) {
            logger.info("Using environment from TestNG XML: " + envFromContext);
            EnvironmentConfigManager.getInstance().loadEnvironmentConfig(envFromContext);
            return;
        }

        // Try parameter value
        if (paramValue != null && !paramValue.isEmpty()) {
            logger.info("Using environment from @Parameters: " + paramValue);
            EnvironmentConfigManager.getInstance().loadEnvironmentConfig(paramValue);
            return;
        }

        // Try system property
        String envFromSystem = System.getProperty("environment");
        if (envFromSystem != null && !envFromSystem.isEmpty()) {
            logger.info("Using environment from system property: " + envFromSystem);
            EnvironmentConfigManager.getInstance().loadEnvironmentConfig(envFromSystem);
            return;
        }

        // Use default
        logger.warn("No environment found, using default");
        EnvironmentConfigManager.getInstance().loadEnvironmentConfigFromSystemProperty();
    }
}
