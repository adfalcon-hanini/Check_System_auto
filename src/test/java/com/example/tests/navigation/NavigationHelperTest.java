package com.example.tests.navigation;

import com.example.base.BaseTest;
import com.example.pages.LoginPage;
import com.example.pages.OrdersPage;
import com.example.pages.PortfolioPage;
import com.example.utils.ConfigReader;
import com.example.utils.Constants;
import com.example.utils.NavigationHelper;
import io.qameta.allure.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Comprehensive test class demonstrating NavigationHelper usage
 * Shows best practices for using NavigationHelper in tests
 *
 * Key Features Demonstrated:
 * 1. Basic page navigation
 * 2. Language switching
 * 3. Browser controls (back, forward, refresh)
 * 4. URL and title verification
 * 5. Composite navigation (navigate + login)
 * 6. Method chaining
 */
@Epic("Navigation Management")
@Feature("NavigationHelper")
public class NavigationHelperTest extends BaseTest {

    private static final Logger logger = Logger.getLogger(NavigationHelperTest.class);
    private NavigationHelper navigationHelper;
    private String testUsername;
    private String testPassword;

    @BeforeClass
    public void setupNavigationHelper() {
        logger.info("Setting up NavigationHelper test");
        navigationHelper = new NavigationHelper(driver);

        // Load test credentials from configuration
        testUsername = ConfigReader.getInstance().getProperty("username");
        testPassword = ConfigReader.getInstance().getProperty("password");

        logger.info("NavigationHelper test setup completed");
    }

    // ============================================
    // BASIC NAVIGATION TESTS
    // ============================================

    @Test(priority = 1, description = "Test navigation to Login page")
    @Story("Basic Navigation")
    @Description("Verify NavigationHelper can navigate to Login page and return LoginPage instance")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigateToLoginPage() {
        logger.info("=== TEST: Navigate to Login Page ===");

        // Navigate using NavigationHelper
        LoginPage loginPage = navigationHelper.navigateToLoginPage();

        // Verify navigation was successful
        Assert.assertNotNull(loginPage, "LoginPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("login") ||
                         navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'login' or 'trading'");

        logger.info("✓ Successfully navigated to Login page");
    }

    @Test(priority = 2, description = "Test navigation to Trading page")
    @Story("Basic Navigation")
    @Description("Verify NavigationHelper can navigate to Trading page and return OrdersPage instance")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigateToTradingPage() {
        logger.info("=== TEST: Navigate to Trading Page ===");

        // Navigate using NavigationHelper
        OrdersPage ordersPage = navigationHelper.navigateToTradingPage();

        // Verify navigation was successful
        Assert.assertNotNull(ordersPage, "OrdersPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'trading'");

        logger.info("✓ Successfully navigated to Trading page");
    }

    @Test(priority = 3, description = "Test navigation to Orders page")
    @Story("Basic Navigation")
    @Description("Verify NavigationHelper can navigate to Orders page (alias for Trading page)")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigateToOrdersPage() {
        logger.info("=== TEST: Navigate to Orders Page ===");

        // Navigate using NavigationHelper
        OrdersPage ordersPage = navigationHelper.navigateToOrdersPage();

        // Verify navigation was successful
        Assert.assertNotNull(ordersPage, "OrdersPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'trading'");

        logger.info("✓ Successfully navigated to Orders page");
    }

    @Test(priority = 4, description = "Test navigation to Portfolio page")
    @Story("Basic Navigation")
    @Description("Verify NavigationHelper can navigate to Portfolio page")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigateToPortfolioPage() {
        logger.info("=== TEST: Navigate to Portfolio Page ===");

        // Navigate using NavigationHelper
        PortfolioPage portfolioPage = navigationHelper.navigateToPortfolioPage();

        // Verify navigation was successful
        Assert.assertNotNull(portfolioPage, "PortfolioPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("portfolio"),
                         "URL should contain 'portfolio'");

        logger.info("✓ Successfully navigated to Portfolio page");
    }

    @Test(priority = 5, description = "Test navigation to Home page")
    @Story("Basic Navigation")
    @Description("Verify NavigationHelper can navigate to Home page")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigateToHomePage() {
        logger.info("=== TEST: Navigate to Home Page ===");

        // Navigate using NavigationHelper
        navigationHelper.navigateToHomePage();

        // Verify navigation was successful
        String currentUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(Constants.BASE_URL_EN),
                         "URL should contain base URL");

        logger.info("✓ Successfully navigated to Home page");
    }

    // ============================================
    // LANGUAGE SWITCHING TESTS
    // ============================================

    @Test(priority = 10, description = "Test switching between English and Arabic")
    @Story("Language Switching")
    @Description("Verify NavigationHelper can switch between English and Arabic versions")
    @Severity(SeverityLevel.NORMAL)
    public void testLanguageSwitching() {
        logger.info("=== TEST: Language Switching ===");

        // Start with English Trading page
        navigationHelper.navigateToTradingPage();
        String englishUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(englishUrl.contains("/en/"), "Should be on English version");
        logger.info("✓ On English version: " + englishUrl);

        // Switch to Arabic
        navigationHelper.switchToArabic();
        String arabicUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(arabicUrl.contains("/ar/"), "Should be on Arabic version");
        logger.info("✓ Switched to Arabic: " + arabicUrl);

        // Switch back to English
        navigationHelper.switchToEnglish();
        String backToEnglishUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(backToEnglishUrl.contains("/en/"), "Should be back on English version");
        logger.info("✓ Switched back to English: " + backToEnglishUrl);
    }

    @Test(priority = 11, description = "Test Arabic Trading page navigation")
    @Story("Language Switching")
    @Description("Verify NavigationHelper can navigate directly to Arabic Trading page")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigateToTradingPageArabic() {
        logger.info("=== TEST: Navigate to Arabic Trading Page ===");

        // Navigate to Arabic Trading page
        OrdersPage ordersPage = navigationHelper.navigateToTradingPageArabic();

        // Verify navigation was successful
        Assert.assertNotNull(ordersPage, "OrdersPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("/ar/"),
                         "URL should contain '/ar/'");
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'trading'");

        logger.info("✓ Successfully navigated to Arabic Trading page");
    }

    // ============================================
    // BROWSER NAVIGATION CONTROLS
    // ============================================

    @Test(priority = 20, description = "Test browser navigation controls")
    @Story("Browser Controls")
    @Description("Verify NavigationHelper can control browser navigation (back, forward, refresh)")
    @Severity(SeverityLevel.NORMAL)
    public void testBrowserNavigationControls() {
        logger.info("=== TEST: Browser Navigation Controls ===");

        // Navigate to Home page
        navigationHelper.navigateToHomePage();
        String homeUrl = navigationHelper.getCurrentUrl();
        logger.info("Started at: " + homeUrl);

        // Navigate to Trading page
        navigationHelper.navigateToTradingPage();
        String tradingUrl = navigationHelper.getCurrentUrl();
        logger.info("Navigated to: " + tradingUrl);
        Assert.assertTrue(tradingUrl.contains("trading"), "Should be on Trading page");

        // Go back to Home page
        navigationHelper.goBack();
        String backUrl = navigationHelper.getCurrentUrl();
        logger.info("Went back to: " + backUrl);
        Assert.assertTrue(backUrl.contains(Constants.BASE_URL_EN), "Should be back on Home page");

        // Go forward to Trading page
        navigationHelper.goForward();
        String forwardUrl = navigationHelper.getCurrentUrl();
        logger.info("Went forward to: " + forwardUrl);
        Assert.assertTrue(forwardUrl.contains("trading"), "Should be forward on Trading page");

        // Refresh page
        navigationHelper.refresh();
        String refreshedUrl = navigationHelper.getCurrentUrl();
        logger.info("Refreshed page: " + refreshedUrl);
        Assert.assertEquals(refreshedUrl, forwardUrl, "URL should remain same after refresh");

        logger.info("✓ Browser navigation controls working correctly");
    }

    @Test(priority = 21, description = "Test custom URL navigation")
    @Story("Browser Controls")
    @Description("Verify NavigationHelper can navigate to custom URLs")
    @Severity(SeverityLevel.NORMAL)
    public void testCustomUrlNavigation() {
        logger.info("=== TEST: Custom URL Navigation ===");

        // Navigate to custom URL
        String customUrl = Constants.TRADING_PAGE_EN;
        navigationHelper.navigateToUrl(customUrl);

        // Verify navigation was successful
        String currentUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("trading"),
                         "URL should contain 'trading'");

        logger.info("✓ Successfully navigated to custom URL: " + customUrl);
    }

    // ============================================
    // VERIFICATION METHODS TESTS
    // ============================================

    @Test(priority = 30, description = "Test URL verification")
    @Story("Verification Methods")
    @Description("Verify NavigationHelper URL verification methods work correctly")
    @Severity(SeverityLevel.NORMAL)
    public void testUrlVerification() {
        logger.info("=== TEST: URL Verification ===");

        // Navigate to Trading page
        navigationHelper.navigateToTradingPage();

        // Test URL verification
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'trading'");
        Assert.assertTrue(navigationHelper.verifyUrlContains("/en/"),
                         "URL should contain '/en/'");
        Assert.assertFalse(navigationHelper.verifyUrlContains("nonexistent"),
                          "URL should not contain 'nonexistent'");

        logger.info("✓ URL verification methods working correctly");
    }

    @Test(priority = 31, description = "Test page title verification")
    @Story("Verification Methods")
    @Description("Verify NavigationHelper page title verification methods work correctly")
    @Severity(SeverityLevel.NORMAL)
    public void testPageTitleVerification() {
        logger.info("=== TEST: Page Title Verification ===");

        // Navigate to Trading page
        navigationHelper.navigateToTradingPage();

        // Get and verify page title
        String pageTitle = navigationHelper.getCurrentPageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");

        logger.info("✓ Page title: " + pageTitle);
        logger.info("✓ Page title verification methods working correctly");
    }

    // ============================================
    // PAGE INSTANCE GETTERS TESTS
    // ============================================

    @Test(priority = 40, description = "Test page instance getters")
    @Story("Page Instance Management")
    @Description("Verify NavigationHelper can get cached page instances")
    @Severity(SeverityLevel.NORMAL)
    public void testPageInstanceGetters() {
        logger.info("=== TEST: Page Instance Getters ===");

        // Get LoginPage instance
        LoginPage loginPage1 = navigationHelper.getLoginPage();
        Assert.assertNotNull(loginPage1, "LoginPage instance should not be null");

        // Get same instance again (should be cached)
        LoginPage loginPage2 = navigationHelper.getLoginPage();
        Assert.assertSame(loginPage1, loginPage2, "Should return cached instance");

        // Get OrdersPage instance
        OrdersPage ordersPage1 = navigationHelper.getOrdersPage();
        Assert.assertNotNull(ordersPage1, "OrdersPage instance should not be null");

        // Get PortfolioPage instance
        PortfolioPage portfolioPage1 = navigationHelper.getPortfolioPage();
        Assert.assertNotNull(portfolioPage1, "PortfolioPage instance should not be null");

        logger.info("✓ Page instance getters working correctly with caching");
    }

    // ============================================
    // METHOD CHAINING TESTS
    // ============================================

    @Test(priority = 50, description = "Test method chaining")
    @Story("Method Chaining")
    @Description("Verify NavigationHelper supports fluent API / method chaining")
    @Severity(SeverityLevel.NORMAL)
    public void testMethodChaining() {
        logger.info("=== TEST: Method Chaining ===");

        // Test method chaining with multiple operations
        navigationHelper
            .navigateToHomePage()
            .navigateToUrl(Constants.TRADING_PAGE_EN)
            .switchToArabic()
            .switchToEnglish()
            .refresh();

        // Verify final state
        String finalUrl = navigationHelper.getCurrentUrl();
        Assert.assertTrue(finalUrl.contains("trading"), "Should be on Trading page");
        Assert.assertTrue(finalUrl.contains("/en/"), "Should be on English version");

        logger.info("✓ Method chaining working correctly");
    }

    // ============================================
    // COMPOSITE NAVIGATION TESTS
    // ============================================

    @Test(priority = 60, description = "Test composite navigation (navigate and login)",
          enabled = false) // Disabled as it requires valid credentials
    @Story("Composite Navigation")
    @Description("Verify NavigationHelper can perform composite navigation with login")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigateAndLogin() {
        logger.info("=== TEST: Navigate and Login ===");

        // Navigate and login in one step
        OrdersPage ordersPage = navigationHelper.navigateAndLogin(testUsername, testPassword);

        // Verify navigation and login were successful
        Assert.assertNotNull(ordersPage, "OrdersPage instance should not be null");
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"),
                         "URL should contain 'trading'");

        logger.info("✓ Navigate and login completed successfully");
    }

    // ============================================
    // ERROR HANDLING TESTS
    // ============================================

    @Test(priority = 70, description = "Test navigation to invalid URL",
          expectedExceptions = NavigationHelper.NavigationException.class)
    @Story("Error Handling")
    @Description("Verify NavigationHelper handles invalid URLs properly")
    @Severity(SeverityLevel.NORMAL)
    public void testNavigationToInvalidUrl() {
        logger.info("=== TEST: Navigation to Invalid URL ===");

        // This should throw NavigationException
        navigationHelper.navigateToUrl("invalid-url");

        logger.error("✗ Should have thrown NavigationException");
    }

    // ============================================
    // REAL-WORLD USAGE SCENARIOS
    // ============================================

    @Test(priority = 80, description = "Real-world scenario: Multi-page workflow")
    @Story("Real-World Scenarios")
    @Description("Demonstrate NavigationHelper usage in a realistic multi-page workflow")
    @Severity(SeverityLevel.NORMAL)
    public void testRealWorldMultiPageWorkflow() {
        logger.info("=== TEST: Real-World Multi-Page Workflow ===");

        logger.info("Step 1: Navigate to Home page");
        navigationHelper.navigateToHomePage();
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains(Constants.BASE_URL_EN));

        logger.info("Step 2: Navigate to Trading page");
        OrdersPage ordersPage = navigationHelper.navigateToTradingPage();
        Assert.assertNotNull(ordersPage);

        logger.info("Step 3: Navigate to Portfolio page");
        PortfolioPage portfolioPage = navigationHelper.navigateToPortfolioPage();
        Assert.assertNotNull(portfolioPage);

        logger.info("Step 4: Go back to Trading page");
        navigationHelper.goBack();
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"));

        logger.info("Step 5: Refresh Trading page");
        navigationHelper.refresh();
        Assert.assertTrue(navigationHelper.verifyUrlContains("trading"));

        logger.info("✓ Real-world multi-page workflow completed successfully");
    }

    @Test(priority = 81, description = "Real-world scenario: Language preference workflow")
    @Story("Real-World Scenarios")
    @Description("Demonstrate NavigationHelper usage for language preference workflow")
    @Severity(SeverityLevel.NORMAL)
    public void testRealWorldLanguagePreferenceWorkflow() {
        logger.info("=== TEST: Real-World Language Preference Workflow ===");

        logger.info("Step 1: User starts on English Home page");
        navigationHelper.navigateToHomePage();
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains("/en/"));

        logger.info("Step 2: User switches to Arabic");
        navigationHelper.switchToArabic();
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains("/ar/"));

        logger.info("Step 3: User navigates to Trading page (stays in Arabic)");
        navigationHelper.navigateToUrl(Constants.TRADING_PAGE_AR);
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains("/ar/"));
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains("trading"));

        logger.info("Step 4: User switches back to English");
        navigationHelper.switchToEnglish();
        Assert.assertTrue(navigationHelper.getCurrentUrl().contains("/en/"));

        logger.info("✓ Language preference workflow completed successfully");
    }

    // ============================================
    // SUMMARY TEST
    // ============================================

    @Test(priority = 100, description = "Print NavigationHelper capabilities summary")
    @Story("Summary")
    @Description("Print comprehensive summary of NavigationHelper capabilities")
    @Severity(SeverityLevel.TRIVIAL)
    public void printCapabilitiesSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("NAVIGATIONHELPER CAPABILITIES SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("PRIMARY NAVIGATION METHODS:");
        System.out.println("  ✓ navigateToLoginPage() -> LoginPage");
        System.out.println("  ✓ navigateToTradingPage() -> OrdersPage");
        System.out.println("  ✓ navigateToTradingPageArabic() -> OrdersPage");
        System.out.println("  ✓ navigateToOrdersPage() -> OrdersPage (alias)");
        System.out.println("  ✓ navigateToPortfolioPage() -> PortfolioPage");
        System.out.println("  ✓ navigateToHomePage() -> NavigationHelper");
        System.out.println("  ✓ navigateToHomePageArabic() -> NavigationHelper");
        System.out.println();
        System.out.println("LANGUAGE SWITCHING:");
        System.out.println("  ✓ switchToEnglish() -> NavigationHelper");
        System.out.println("  ✓ switchToArabic() -> NavigationHelper");
        System.out.println();
        System.out.println("BROWSER CONTROLS:");
        System.out.println("  ✓ navigateToUrl(String url) -> NavigationHelper");
        System.out.println("  ✓ goBack() -> NavigationHelper");
        System.out.println("  ✓ goForward() -> NavigationHelper");
        System.out.println("  ✓ refresh() -> NavigationHelper");
        System.out.println();
        System.out.println("PAGE INSTANCE GETTERS:");
        System.out.println("  ✓ getLoginPage() -> LoginPage (cached)");
        System.out.println("  ✓ getOrdersPage() -> OrdersPage (cached)");
        System.out.println("  ✓ getPortfolioPage() -> PortfolioPage (cached)");
        System.out.println();
        System.out.println("UTILITY METHODS:");
        System.out.println("  ✓ getCurrentUrl() -> String");
        System.out.println("  ✓ getCurrentPageTitle() -> String");
        System.out.println("  ✓ verifyUrlContains(String) -> boolean");
        System.out.println("  ✓ verifyPageTitle(String) -> boolean");
        System.out.println("  ✓ waitForPageLoad(long) -> void");
        System.out.println();
        System.out.println("COMPOSITE METHODS:");
        System.out.println("  ✓ navigateAndLogin(username, password) -> OrdersPage");
        System.out.println("  ✓ navigateToOrdersPageDirect() -> OrdersPage");
        System.out.println();
        System.out.println("KEY FEATURES:");
        System.out.println("  ✓ Method chaining support (fluent API)");
        System.out.println("  ✓ Page instance caching for performance");
        System.out.println("  ✓ Comprehensive error handling");
        System.out.println("  ✓ Allure reporting integration");
        System.out.println("  ✓ Detailed logging for debugging");
        System.out.println("  ✓ Custom NavigationException for failures");
        System.out.println();
        System.out.println("USAGE EXAMPLE:");
        System.out.println("  NavigationHelper nav = new NavigationHelper(driver);");
        System.out.println("  OrdersPage page = nav.navigateToTradingPage()");
        System.out.println("                       .switchToArabic()");
        System.out.println("                       .refresh()");
        System.out.println("                       .getOrdersPage();");
        System.out.println("=".repeat(80) + "\n");
    }
}
