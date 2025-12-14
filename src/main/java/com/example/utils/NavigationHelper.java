package com.example.utils;

import com.example.pages.LoginPage;
import com.example.pages.OrdersPage;
import com.example.pages.PortfolioPage;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * NavigationHelper class for centralized navigation management
 * Provides clear and consistent methods for navigating between pages
 * Follows Page Object Model pattern by returning page objects
 *
 * Benefits:
 * - Single source of truth for navigation logic
 * - Consistent navigation patterns across tests
 * - Better error handling and logging
 * - Allure integration for reporting
 * - Easy to maintain and extend
 *
 * @author Test Automation Team
 */
public class NavigationHelper {

    private final WebDriver driver;
    private static final Logger logger = Logger.getLogger(NavigationHelper.class);

    // Page instances cache (optional - for performance optimization)
    private LoginPage loginPage;
    private OrdersPage ordersPage;
    private PortfolioPage portfolioPage;

    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
        logger.info("NavigationHelper initialized");
    }

    // ============================================
    // PRIMARY NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Login page
     * @return LoginPage instance
     */
    @Step("Navigate to Login page")
    public LoginPage navigateToLoginPage() {
        logger.info("Navigating to Login page");
        try {
            navigateToUrl(Constants.LOGIN_PAGE);
            loginPage = new LoginPage(driver);
            logger.info("Successfully navigated to Login page");
            return loginPage;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Login page: " + e.getMessage());
            throw new NavigationException("Navigation to Login page failed", e);
        }
    }

    /**
     * Navigate to Trading page (English)
     * @return OrdersPage instance
     */
    @Step("Navigate to Trading page")
    public OrdersPage navigateToTradingPage() {
        logger.info("Navigating to Trading page (English)");
        try {
            navigateToUrl(Constants.TRADING_PAGE_EN);
            ordersPage = new OrdersPage(driver);
            logger.info("Successfully navigated to Trading page");
            return ordersPage;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Trading page: " + e.getMessage());
            throw new NavigationException("Navigation to Trading page failed", e);
        }
    }

    /**
     * Navigate to Trading page (Arabic)
     * @return OrdersPage instance
     */
    @Step("Navigate to Trading page (Arabic)")
    public OrdersPage navigateToTradingPageArabic() {
        logger.info("Navigating to Trading page (Arabic)");
        try {
            navigateToUrl(Constants.TRADING_PAGE_AR);
            ordersPage = new OrdersPage(driver);
            logger.info("Successfully navigated to Trading page (Arabic)");
            return ordersPage;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Trading page (Arabic): " + e.getMessage());
            throw new NavigationException("Navigation to Trading page (Arabic) failed", e);
        }
    }

    /**
     * Navigate to Orders page (alias for Trading page)
     * @return OrdersPage instance
     */
    @Step("Navigate to Orders page")
    public OrdersPage navigateToOrdersPage() {
        logger.info("Navigating to Orders page");
        return navigateToTradingPage();
    }

    /**
     * Navigate to Portfolio page
     * @return PortfolioPage instance
     */
    @Step("Navigate to Portfolio page")
    public PortfolioPage navigateToPortfolioPage() {
        logger.info("Navigating to Portfolio page");
        try {
            // Assuming portfolio page follows same pattern
            String portfolioUrl = Constants.BASE_URL_EN + "/portfolio";
            navigateToUrl(portfolioUrl);
            portfolioPage = new PortfolioPage();
            logger.info("Successfully navigated to Portfolio page");
            return portfolioPage;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Portfolio page: " + e.getMessage());
            throw new NavigationException("Navigation to Portfolio page failed", e);
        }
    }

    /**
     * Navigate to home page (English)
     * @return NavigationHelper for method chaining
     */
    @Step("Navigate to Home page")
    public NavigationHelper navigateToHomePage() {
        logger.info("Navigating to Home page (English)");
        try {
            navigateToUrl(Constants.BASE_URL_EN);
            logger.info("Successfully navigated to Home page");
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Home page: " + e.getMessage());
            throw new NavigationException("Navigation to Home page failed", e);
        }
    }

    /**
     * Navigate to home page (Arabic)
     * @return NavigationHelper for method chaining
     */
    @Step("Navigate to Home page (Arabic)")
    public NavigationHelper navigateToHomePageArabic() {
        logger.info("Navigating to Home page (Arabic)");
        try {
            navigateToUrl(Constants.BASE_URL_AR);
            logger.info("Successfully navigated to Home page (Arabic)");
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to Home page (Arabic): " + e.getMessage());
            throw new NavigationException("Navigation to Home page (Arabic) failed", e);
        }
    }

    // ============================================
    // LANGUAGE-SPECIFIC NAVIGATION
    // ============================================

    /**
     * Switch to English version of current page
     * @return NavigationHelper for method chaining
     */
    @Step("Switch to English version")
    public NavigationHelper switchToEnglish() {
        logger.info("Switching to English version");
        try {
            String currentUrl = driver.getCurrentUrl();
            String englishUrl = currentUrl.replace("/ar/", "/en/");
            if (!currentUrl.equals(englishUrl)) {
                navigateToUrl(englishUrl);
                logger.info("Successfully switched to English version");
            } else {
                logger.info("Already on English version");
            }
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to switch to English: " + e.getMessage());
            throw new NavigationException("Language switch to English failed", e);
        }
    }

    /**
     * Switch to Arabic version of current page
     * @return NavigationHelper for method chaining
     */
    @Step("Switch to Arabic version")
    public NavigationHelper switchToArabic() {
        logger.info("Switching to Arabic version");
        try {
            String currentUrl = driver.getCurrentUrl();
            String arabicUrl = currentUrl.replace("/en/", "/ar/");
            if (!currentUrl.equals(arabicUrl)) {
                navigateToUrl(arabicUrl);
                logger.info("Successfully switched to Arabic version");
            } else {
                logger.info("Already on Arabic version");
            }
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to switch to Arabic: " + e.getMessage());
            throw new NavigationException("Language switch to Arabic failed", e);
        }
    }

    // ============================================
    // BROWSER NAVIGATION CONTROLS
    // ============================================

    /**
     * Navigate to a specific URL
     * @param url URL to navigate to
     * @return NavigationHelper for method chaining
     */
    @Step("Navigate to URL: {url}")
    public NavigationHelper navigateToUrl(String url) {
        logger.info("Navigating to URL: " + url);
        try {
            driver.get(url);
            waitForPageLoad();
            logger.info("Successfully navigated to: " + url);
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate to URL: " + url + " - " + e.getMessage());
            throw new NavigationException("Navigation to URL failed: " + url, e);
        }
    }

    /**
     * Navigate back in browser history
     * @return NavigationHelper for method chaining
     */
    @Step("Navigate back")
    public NavigationHelper goBack() {
        logger.info("Navigating back");
        try {
            driver.navigate().back();
            waitForPageLoad();
            logger.info("Successfully navigated back");
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate back: " + e.getMessage());
            throw new NavigationException("Navigate back failed", e);
        }
    }

    /**
     * Navigate forward in browser history
     * @return NavigationHelper for method chaining
     */
    @Step("Navigate forward")
    public NavigationHelper goForward() {
        logger.info("Navigating forward");
        try {
            driver.navigate().forward();
            waitForPageLoad();
            logger.info("Successfully navigated forward");
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to navigate forward: " + e.getMessage());
            throw new NavigationException("Navigate forward failed", e);
        }
    }

    /**
     * Refresh current page
     * @return NavigationHelper for method chaining
     */
    @Step("Refresh page")
    public NavigationHelper refresh() {
        logger.info("Refreshing page");
        try {
            driver.navigate().refresh();
            waitForPageLoad();
            logger.info("Successfully refreshed page");
            return this;
        } catch (WebDriverException e) {
            logger.error("Failed to refresh page: " + e.getMessage());
            throw new NavigationException("Page refresh failed", e);
        }
    }

    // ============================================
    // PAGE INSTANCE GETTERS
    // ============================================

    /**
     * Get LoginPage instance (creates new if not cached)
     * @return LoginPage instance
     */
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    /**
     * Get OrdersPage instance (creates new if not cached)
     * @return OrdersPage instance
     */
    public OrdersPage getOrdersPage() {
        if (ordersPage == null) {
            ordersPage = new OrdersPage(driver);
        }
        return ordersPage;
    }

    /**
     * Get PortfolioPage instance (creates new if not cached)
     * @return PortfolioPage instance
     */
    public PortfolioPage getPortfolioPage() {
        if (portfolioPage == null) {
            portfolioPage = new PortfolioPage();
        }
        return portfolioPage;
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    /**
     * Get current URL
     * @return Current page URL
     */
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        logger.debug("Current URL: " + currentUrl);
        return currentUrl;
    }

    /**
     * Get current page title
     * @return Current page title
     */
    public String getCurrentPageTitle() {
        String title = driver.getTitle();
        logger.debug("Current page title: " + title);
        return title;
    }

    /**
     * Verify current URL contains expected text
     * @param expectedUrlPart Expected URL part
     * @return true if URL contains expected text
     */
    public boolean verifyUrlContains(String expectedUrlPart) {
        String currentUrl = getCurrentUrl();
        boolean contains = currentUrl.contains(expectedUrlPart);
        logger.info("URL verification: " + currentUrl + " contains '" + expectedUrlPart + "': " + contains);
        return contains;
    }

    /**
     * Verify current page title contains expected text
     * @param expectedTitle Expected title text
     * @return true if title contains expected text
     */
    public boolean verifyPageTitle(String expectedTitle) {
        String currentTitle = getCurrentPageTitle();
        boolean matches = currentTitle.contains(expectedTitle);
        logger.info("Title verification: '" + currentTitle + "' contains '" + expectedTitle + "': " + matches);
        return matches;
    }

    /**
     * Wait for page to load completely
     * Uses implicit wait mechanism
     */
    private void waitForPageLoad() {
        try {
            Thread.sleep(500); // Brief wait for page load to start
            logger.debug("Page load wait completed");
        } catch (InterruptedException e) {
            logger.warn("Page load wait interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for page to load with custom timeout
     * @param timeoutInMillis Timeout in milliseconds
     */
    public void waitForPageLoad(long timeoutInMillis) {
        try {
            Thread.sleep(timeoutInMillis);
            logger.debug("Custom page load wait completed: " + timeoutInMillis + "ms");
        } catch (InterruptedException e) {
            logger.warn("Page load wait interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // ============================================
    // COMPOSITE NAVIGATION METHODS
    // ============================================

    /**
     * Navigate to Trading page and perform login
     * Combines navigation and login in one method
     * @param username Username
     * @param password Password
     * @return OrdersPage instance (after successful login)
     */
    @Step("Navigate to Trading page and login as {username}")
    public OrdersPage navigateAndLogin(String username, String password) {
        logger.info("Navigating to Trading page and logging in as: " + username);
        try {
            LoginPage loginPage = navigateToLoginPage();
            loginPage.navigateToLoginPage();
            loginPage.loginWithTradingLoginClick(username, password);

            // Wait for login to complete
            waitForPageLoad(2000);

            // Verify login was successful
            if (!loginPage.isLoginSuccessful()) {
                logger.warn("Login verification failed - errors may be present");
            }

            ordersPage = new OrdersPage(driver);
            logger.info("Successfully navigated and logged in");
            return ordersPage;

        } catch (Exception e) {
            logger.error("Navigate and login failed: " + e.getMessage());
            throw new NavigationException("Navigate and login failed", e);
        }
    }

    /**
     * Navigate to Orders page directly (assumes already logged in)
     * @return OrdersPage instance
     */
    @Step("Navigate to Orders page (direct)")
    public OrdersPage navigateToOrdersPageDirect() {
        logger.info("Navigating directly to Orders page");
        return navigateToTradingPage();
    }

    // ============================================
    // CUSTOM EXCEPTION CLASS
    // ============================================

    /**
     * Custom exception for navigation failures
     * Provides better error context
     */
    public static class NavigationException extends RuntimeException {
        public NavigationException(String message) {
            super(message);
        }

        public NavigationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
