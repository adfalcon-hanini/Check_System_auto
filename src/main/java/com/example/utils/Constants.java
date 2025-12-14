package com.example.utils;

/**
 * Constants class for storing application-wide constants
 * URLs, timeouts, and configuration values
 */
public class Constants {

    // Base URLs
    public static final String BASE_URL = "https://devuat.thegroup.com.qa/uat";
    public static final String BASE_URL_EN = BASE_URL + "/en";
    public static final String BASE_URL_AR = BASE_URL + "/ar";

    // Page URLs
    public static final String TRADING_PAGE_EN = BASE_URL_EN + "/trading";
    public static final String TRADING_PAGE_AR = BASE_URL_AR + "/trading";
    public static final String LOGIN_PAGE = BASE_URL_EN + "/login";

    // Timeouts (in seconds)
    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 15;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    public static final int SHORT_WAIT = 2;
    public static final int MEDIUM_WAIT = 5;
    public static final int LONG_WAIT = 10;

    // Browser Types
    public static final String CHROME = "CHROME";
    public static final String FIREFOX = "FIREFOX";
    public static final String EDGE = "EDGE";
    public static final String SAFARI = "SAFARI";

    // Test Data
    public static final String DEFAULT_QUANTITY = "10";
    public static final String DEFAULT_PRICE = "100.00";

    // Messages
    public static final String SUCCESS_MESSAGE = "Order placed successfully";
    public static final String ERROR_MESSAGE = "Error placing order";

    // Page Titles
    public static final String ORDERS_PAGE_TITLE = "Orders";
    public static final String TRADING_PAGE_TITLE_EN = "Trading";
    public static final String TRADING_PAGE_TITLE_AR = "الأوامر";

    // Log Messages
    public static final String TEST_STARTED = "Test started";
    public static final String TEST_COMPLETED = "Test completed";
    public static final String TEST_PASSED = "PASSED";
    public static final String TEST_FAILED = "FAILED";

    // Prevent instantiation
    private Constants() {
        throw new IllegalStateException("Constants class");
    }
}