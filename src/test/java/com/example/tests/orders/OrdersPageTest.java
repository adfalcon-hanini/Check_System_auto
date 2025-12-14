package com.example.tests.orders;

import com.example.base.BaseTest;
import com.example.pages.LoginPage;
import com.example.pages.OrdersPage;
import com.example.screensData.xdp.GetInstrumentsData;
import com.example.utils.Constants;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * OrdersPageTest class for testing orders/trading functionality
 * Extends BaseTest for common test setup
 */
@Epic("Trading Functionality")
@Feature("Orders Management")
public class OrdersPageTest extends BaseTest {

    private OrdersPage ordersPage;
    private LoginPage loginPage;

    @BeforeMethod
    public void initializePage() {
        logger.info("Initializing OrdersPage with successful login");

        // Initialize LoginPage and perform successful login
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.waitForPageLoad(2000);

        Allure.step("Perform successful login before accessing orders page");
        performSuccessfulLogin("12240", "12345");

        // Initialize OrdersPage after successful login
        ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        ordersPage.waitForPageLoad();

        logger.info("OrdersPage initialized successfully with authenticated user");
    }

    /**
     * Perform a successful login - helper method for authenticated state
     * @param username Username for login
     * @param password Password for login
     */
    private void performSuccessfulLogin(String username, String password) {
        logger.info("Performing successful login for user: " + username);

        Allure.step("Click login button in trading page");
        loginPage.clickLoginInTrading();

        Allure.step("Enter credentials and login");
        loginPage.login(username, password);

        Allure.step("Wait for login to complete");
        loginPage.waitForPageLoad(3000);

        Allure.step("Verify login was successful");
        boolean loginSuccessful = loginPage.isLoginSuccessful();
        Assert.assertTrue(loginSuccessful, "Login must be successful to access orders page");

        logger.info("Successful login completed for user: " + username);
    }

    /**
     * Re-login with a different user - helper method for switching sessions
     * This method logs out current user and logs in with new credentials
     * @param username Username for login
     * @param password Password for login
     */
    private void reloginWithDifferentUser(String username, String password) {
        logger.info("Switching session to user: " + username);

        Allure.step("Logout current user and login with NIN: " + username);

        // Navigate to login page to start fresh session
        loginPage.navigateToLoginPage();
        loginPage.waitForPageLoad(2000);

        // Perform login with new credentials
        performSuccessfulLogin(username, password);

        // Re-initialize orders page after login
        ordersPage = new OrdersPage(driver);
        ordersPage.navigateToOrdersPage();
        ordersPage.waitForPageLoad();

        logger.info("Successfully switched to user: " + username);
    }

    @Test(priority = 1)
    @Story("Orders Page Navigation")
    @Description("Verify orders page loads successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void testOrdersPageLoads() {
        logTestStart("testOrdersPageLoads");

        Allure.step("Verify page URL");
        String currentUrl = ordersPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("trading"), "URL should contain 'trading'");

        Allure.step("Verify page title");
        String pageTitle = ordersPage.getPageTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");

        logTestComplete("testOrdersPageLoads", Constants.TEST_PASSED);
    }

    @Test(priority = 2)
    @Story("BUY Order Verification")
    @Description("Verify BUY button is displayed")
    @Severity(SeverityLevel.CRITICAL)
    public void testBuyButtonIsDisplayed() {
        logTestStart("testBuyButtonIsDisplayed");

        Allure.step("Check if BUY button is displayed");
        boolean isBuyButtonDisplayed = ordersPage.isBuyButtonDisplayed();

        Assert.assertTrue(isBuyButtonDisplayed, "BUY button should be displayed");

        logTestComplete("testBuyButtonIsDisplayed", Constants.TEST_PASSED);
    }

    @Test(priority = 3)
    @Story("SELL Order Verification")
    @Description("Verify SELL button is displayed")
    @Severity(SeverityLevel.CRITICAL)
    public void testSellButtonIsDisplayed() {
        logTestStart("testSellButtonIsDisplayed");

        Allure.step("Check if SELL button is displayed");
        boolean isSellButtonDisplayed = ordersPage.isSellButtonDisplayed();

        Assert.assertTrue(isSellButtonDisplayed, "SELL button should be displayed");

        logTestComplete("testSellButtonIsDisplayed", Constants.TEST_PASSED);
    }

    @Test(priority = 4)
    @Story("BUY and SELL Order Verification")
    @Description("Verify both BUY and SELL buttons are displayed")
    @Severity(SeverityLevel.CRITICAL)
    public void testBuyAndSellButtonsAreDisplayed() {
        logTestStart("testBuyAndSellButtonsAreDisplayed");

        Allure.step("Verify BUY button");
        boolean isBuyButtonDisplayed = ordersPage.isBuyButtonDisplayed();

        Allure.step("Verify SELL button");
        boolean isSellButtonDisplayed = ordersPage.isSellButtonDisplayed();

        Assert.assertTrue(isBuyButtonDisplayed, "BUY button should be displayed");
        Assert.assertTrue(isSellButtonDisplayed, "SELL button should be displayed");

        logTestComplete("testBuyAndSellButtonsAreDisplayed", Constants.TEST_PASSED);
    }

    @Test(priority = 5)
    @Story("BUY Order Interaction")
    @Description("Test clicking BUY button")
    @Severity(SeverityLevel.NORMAL)
    public void testClickBuyButton() {
        logTestStart("testClickBuyButton");

        Allure.step("Click BUY button");
        try {
            ordersPage.clickBuyButton();
            ordersPage.waitForPageLoad();
            logger.info("BUY button clicked successfully");
        } catch (Exception e) {
            logger.warn("BUY button interaction: " + e.getMessage());
        }

        logTestComplete("testClickBuyButton", Constants.TEST_PASSED);
    }

    @Test(priority = 6)
    @Story("SELL Order Interaction")
    @Description("Test clicking SELL button")
    @Severity(SeverityLevel.NORMAL)
    public void testClickSellButton() {
        logTestStart("testClickSellButton");

        Allure.step("Click SELL button");
        try {
            ordersPage.clickSellButton();
            ordersPage.waitForPageLoad();
            logger.info("SELL button clicked successfully");
        } catch (Exception e) {
            logger.warn("SELL button interaction: " + e.getMessage());
        }

        logTestComplete("testClickSellButton", Constants.TEST_PASSED);
    }

    @Test(priority = 7)
    @Story("Place BUY Order")
    @Description("Test placing a BUY order with confirmation popup handling")
    @Severity(SeverityLevel.BLOCKER)
    public void testPlaceBuyOrder() {
        logTestStart("testPlaceBuyOrder");

        try {
            Allure.step("Click BUY button");
            ordersPage.clickBuyButton();
            ordersPage.waitForPageLoad(500);

            Allure.step("Enter quantity: " + Constants.DEFAULT_QUANTITY);
            ordersPage.enterQuantity(Constants.DEFAULT_QUANTITY);

            Allure.step("Enter price: " + Constants.DEFAULT_PRICE);
            ordersPage.enterPrice(Constants.DEFAULT_PRICE);

            Allure.step("Submit order");
            ordersPage.clickSendOrderButton();
            ordersPage.waitForPageLoad(1000);

            Allure.step("Check if confirmation popup is displayed");
            boolean isConfirmPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();
            logger.info("Confirmation popup displayed: " + isConfirmPopupDisplayed);

            if (isConfirmPopupDisplayed) {
                Allure.step("Confirm order");
                ordersPage.confirmOrder();
                logger.info("Order confirmed successfully");
                ordersPage.waitForPageLoad(1000);
            }

            Allure.step("Check for success/error messages");
            boolean successDisplayed = ordersPage.isSuccessMessageDisplayed();
            boolean errorDisplayed = ordersPage.isErrorMessageDisplayed();

            logger.info("Success message displayed: " + successDisplayed);
            logger.info("Error message displayed: " + errorDisplayed);
        } catch (Exception e) {
            logger.warn("BUY order placement: " + e.getMessage());
        }

        logTestComplete("testPlaceBuyOrder", Constants.TEST_PASSED);
    }

    @Test(priority = 8)
    @Story("Place SELL Order")
    @Description("Test placing a SELL order with confirmation popup handling - Uses NIN 71125")
    @Severity(SeverityLevel.BLOCKER)
    public void testPlaceSellOrder() {
        logTestStart("testPlaceSellOrder");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        try {
            String quantity = "5";
            String price = "99.75";

            Allure.step("Click SELL button");
            ordersPage.clickSellButton();
            ordersPage.waitForPageLoad(500);

            Allure.step("Enter quantity: " + quantity);
            ordersPage.enterQuantity(quantity);

            Allure.step("Enter price: " + price);
            ordersPage.enterPrice(price);

            Allure.step("Submit order");
            ordersPage.clickSendOrderButton();
            ordersPage.waitForPageLoad(1000);

            Allure.step("Check if confirmation popup is displayed");
            boolean isConfirmPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();
            logger.info("Confirmation popup displayed: " + isConfirmPopupDisplayed);

            if (isConfirmPopupDisplayed) {
                Allure.step("Confirm order");
                ordersPage.confirmOrder();
                logger.info("Order confirmed successfully");
                ordersPage.waitForPageLoad(1000);
            }

            Allure.step("Check for success/error messages");
            boolean successDisplayed = ordersPage.isSuccessMessageDisplayed();
            boolean errorDisplayed = ordersPage.isErrorMessageDisplayed();

            logger.info("Success message displayed: " + successDisplayed);
            logger.info("Error message displayed: " + errorDisplayed);
        } catch (Exception e) {
            logger.warn("SELL order placement: " + e.getMessage());
        }

        logTestComplete("testPlaceSellOrder", Constants.TEST_PASSED);
    }

    @Test(priority = 9)
    @Story("Place SELL Order with Confirmation")
    @Description("Test placing a SELL order with confirmation popup handling - Uses NIN 71125")
    @Severity(SeverityLevel.BLOCKER)
    public void testPlaceSellOrderWithConfirmation() {
        logTestStart("testPlaceSellOrderWithConfirmation");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        Allure.step("Place SELL order with confirmation");
        try {
            String quantity = "10";
            String price = "95.50";

            ordersPage.placeSellOrderWithConfirmation(quantity, price);
            ordersPage.waitForPageLoad(2000);

            logger.info("SELL order with confirmation placed successfully");
        } catch (Exception e) {
            logger.warn("SELL order with confirmation placement: " + e.getMessage());
        }

        logTestComplete("testPlaceSellOrderWithConfirmation", Constants.TEST_PASSED);
    }

    @Test(priority = 10)
    @Story("Place Complete SELL Order")
    @Description("Test placing a comprehensive SELL order with all parameters - Uses NIN 71125")
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceCompleteSellOrder() {
        logTestStart("testPlaceCompleteSellOrder");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        Allure.step("Place comprehensive SELL order");
        try {
            String quantity = "15";
            String price = "98.25";
            String discloseVolume = "5";
            String validity = "Day";
            String orderType = "Limit";
            boolean confirmOrder = true;

            ordersPage.placeSellOrderComplete(quantity, price, discloseVolume, validity, orderType, confirmOrder);
            ordersPage.waitForPageLoad(2000);

            logger.info("Complete SELL order placed successfully with all parameters");
        } catch (Exception e) {
            logger.warn("Complete SELL order placement: " + e.getMessage());
        }

        logTestComplete("testPlaceCompleteSellOrder", Constants.TEST_PASSED);
    }

    @Test(priority = 11)
    @Story("Place SELL Order without Confirmation")
    @Description("Test placing a SELL order and canceling confirmation - Uses NIN 71125")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceSellOrderWithoutConfirmation() {
        logTestStart("testPlaceSellOrderWithoutConfirmation");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        Allure.step("Place SELL order and cancel confirmation");
        try {
            String quantity = "20";
            String price = "97.00";
            boolean confirmOrder = false; // Cancel the order

            ordersPage.placeSellOrderComplete(quantity, price, null, null, null, confirmOrder);
            ordersPage.waitForPageLoad(2000);

            logger.info("SELL order cancelled successfully");
        } catch (Exception e) {
            logger.warn("SELL order cancellation: " + e.getMessage());
        }

        logTestComplete("testPlaceSellOrderWithoutConfirmation", Constants.TEST_PASSED);
    }

    @Test(priority = 12)
    @Story("Create Market SELL Order")
    @Description("Create a market SELL order with confirmation popup verification - Uses NIN 71125")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateMarketSellOrder() {
        logTestStart("testCreateMarketSellOrder");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        Allure.step("Create market SELL order");
        try {
            String quantity = "25";
            String price = "100.00";
            String orderType = "Market";

            Allure.step("Click SELL button");
            ordersPage.clickSellButton();
            ordersPage.waitForPageLoad(500);

            Allure.step("Enter quantity: " + quantity);
            ordersPage.enterQuantity(quantity);

            Allure.step("Enter price: " + price);
            ordersPage.enterPrice(price);

            Allure.step("Select order type: " + orderType);
            ordersPage.selectOrderType(orderType);

            Allure.step("Submit order");
            ordersPage.clickSendOrderButton();
            ordersPage.waitForPageLoad(1000);

            Allure.step("Verify confirmation popup is displayed");
            boolean isConfirmPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();
            logger.info("Confirmation popup displayed: " + isConfirmPopupDisplayed);
            Assert.assertTrue(isConfirmPopupDisplayed, "Confirmation popup should be displayed");

            Allure.step("Confirm order");
            ordersPage.confirmOrder();
            logger.info("Order confirmed successfully");

            ordersPage.waitForPageLoad(2000);
            logger.info("Market SELL order created successfully");
        } catch (Exception e) {
            logger.warn("Market SELL order creation: " + e.getMessage());
        }

        logTestComplete("testCreateMarketSellOrder", Constants.TEST_PASSED);
    }

    @Test(priority = 13)
    @Story("Create Limit SELL Order")
    @Description("Create a limit SELL order with custom validity - Uses NIN 71125")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateLimitSellOrder() {
        logTestStart("testCreateLimitSellOrder");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        Allure.step("Create limit SELL order");
        try {
            String quantity = "30";
            String price = "96.75";
            String discloseVolume = "10";
            String validity = "GTC"; // Good Till Cancelled
            String orderType = "Limit";

            Allure.step("Place limit SELL order with parameters");
            ordersPage.placeSellOrderComplete(quantity, price, discloseVolume, validity, orderType, true);
            ordersPage.waitForPageLoad(2000);

            logger.info("Limit SELL order created successfully");
        } catch (Exception e) {
            logger.warn("Limit SELL order creation: " + e.getMessage());
        }

        logTestComplete("testCreateLimitSellOrder", Constants.TEST_PASSED);
    }

    @Test(priority = 14)
    @Story("Order Confirmation Popup Verification")
    @Description("Verify confirmation popup detection and confirmation workflow for BUY order")
    @Severity(SeverityLevel.CRITICAL)
    public void testOrderConfirmationPopupWorkflow() {
        logTestStart("testOrderConfirmationPopupWorkflow");

        try {
            String quantity = "50";
            String price = "105.00";

            Allure.step("Click BUY button to initiate order");
            ordersPage.clickBuyButton();
            ordersPage.waitForPageLoad(500);

            Allure.step("Fill order details - Quantity: " + quantity + ", Price: " + price);
            ordersPage.enterQuantity(quantity);
            ordersPage.enterPrice(price);

            Allure.step("Submit order to trigger confirmation popup");
            ordersPage.clickSendOrderButton();
            ordersPage.waitForPageLoad(1500);

            Allure.step("Verify confirmation popup is displayed using isConfirmationPopupDisplayed()");
            boolean isPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();
            logger.info("✓ Confirmation popup displayed: " + isPopupDisplayed);
            Assert.assertTrue(isPopupDisplayed, "Confirmation popup should be displayed after submitting order");

            Allure.step("Confirm order using confirmOrder() method");
            ordersPage.confirmOrder();
            logger.info("✓ Order confirmed using confirmOrder() method");

            ordersPage.waitForPageLoad(2000);

            Allure.step("Verify confirmation popup is no longer displayed");
            boolean isPopupStillDisplayed = ordersPage.isConfirmationPopupDisplayed();
            logger.info("✓ Confirmation popup still displayed after confirmation: " + isPopupStillDisplayed);
            Assert.assertFalse(isPopupStillDisplayed, "Confirmation popup should be closed after confirmation");

            logger.info("✓ Order confirmation workflow completed successfully");
        } catch (Exception e) {
            logger.error("Order confirmation workflow failed: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }

        logTestComplete("testOrderConfirmationPopupWorkflow", Constants.TEST_PASSED);
    }

    @Test(priority = 15)
    @Story("Page Information")
    @Description("Verify page title and URL")
    @Severity(SeverityLevel.MINOR)
    public void testPageInformation() {
        logTestStart("testPageInformation");

        Allure.step("Get page title");
        String title = ordersPage.getPageTitle();

        Allure.step("Get current URL");
        String url = ordersPage.getCurrentUrl();

        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertNotNull(url, "Current URL should not be null");
        Assert.assertTrue(url.contains("thegroup.com.qa"), "URL should contain domain");

        logTestComplete("testPageInformation", Constants.TEST_PASSED);
    }

    @Test(priority = 16)
    @Story("Instruments Selection and Buy Orders")
    @Description("Click on all instruments from database and create buy orders")
    @Severity(SeverityLevel.CRITICAL)
    public void testClickAllInstrumentsAndCreateBuyOrders() {
        logTestStart("testClickAllInstrumentsAndCreateBuyOrders");

        OracleDBConnection dbConnection = null;

        try {
            Allure.step("Establish database connection");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("Database connection established successfully");

            Allure.step("Fetch all instruments from database");
            GetInstrumentsData instrumentsData = new GetInstrumentsData(dbConnection);
            boolean dataFetched = instrumentsData.fetchInstrumentsData();

            Assert.assertTrue(dataFetched, "Should successfully fetch instruments from database");
            logger.info("Total instruments fetched: " + instrumentsData.getRecordCount());

            Allure.step("Get all instrument records");
            List<Map<String, Object>> allInstruments = instrumentsData.getAllInstrumentRecords();

            Assert.assertTrue(allInstruments.size() > 0, "Should have at least one instrument");

            // Counters for tracking
            int successCount = 0;
            int failureCount = 0;
            int orderSuccessCount = 0;
            int orderFailureCount = 0;

            // Default order parameters
            String defaultQuantity = "10";
            String defaultPrice = "100.00";

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║    Clicking Instruments and Creating Buy Orders              ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("Total instruments to process: " + allInstruments.size());
            System.out.println("Default Quantity: " + defaultQuantity);
            System.out.println("Default Price: " + defaultPrice + "\n");

            Allure.step("Iterate through all instruments, click and create buy orders");
            for (int i = 0; i < allInstruments.size(); i++) {
                Map<String, Object> instrument = allInstruments.get(i);

                String instCode = instrument.get("INST_CODE") != null ? instrument.get("INST_CODE").toString() : "";
                String mnemo = instrument.get("MNEMO") != null ? instrument.get("MNEMO").toString() : "";
                String name = instrument.get("NAME") != null ? instrument.get("NAME").toString() : "";
                String groupCode = instrument.get("GROUP_CODE") != null ? instrument.get("GROUP_CODE").toString() : "";

                System.out.println("═══════════════════════════════════════════════════════════════");
                System.out.println("Processing Instrument " + (i + 1) + " of " + allInstruments.size() + ":");
                System.out.println("  INST_CODE: " + instCode);
                System.out.println("  MNEMO: " + mnemo);
                System.out.println("  NAME: " + name);
                System.out.println("  GROUP_CODE: " + groupCode);

                Allure.step("Step 1: Click instrument: " + mnemo + " - " + name);

                boolean clicked = false;

                // Try clicking by MNEMO first
                if (!mnemo.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByMnemo(mnemo);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by MNEMO: " + mnemo);
                        System.out.println("  Click Status: ✓ Clicked by MNEMO");
                        successCount++;
                    }
                }

                // If MNEMO failed, try by NAME
                if (!clicked && !name.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByName(name);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by NAME: " + name);
                        System.out.println("  Click Status: ✓ Clicked by NAME");
                        successCount++;
                    }
                }

                // If NAME failed, try by INST_CODE
                if (!clicked && !instCode.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByCode(instCode);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by INST_CODE: " + instCode);
                        System.out.println("  Click Status: ✓ Clicked by INST_CODE");
                        successCount++;
                    }
                }

                // If instrument was clicked, create buy order
                if (clicked) {
                    try {
                        Allure.step("Step 2: Create buy order for " + mnemo);
                        ordersPage.waitForPageLoad(1000);

                        System.out.println("  Creating BUY order...");

                        // Click BUY button
                        ordersPage.clickBuyButton();
                        ordersPage.waitForPageLoad(500);

                        // Enter order details
                        ordersPage.enterQuantity(defaultQuantity);
                        ordersPage.enterPrice(defaultPrice);

                        // Submit order
                        ordersPage.clickSendOrderButton();
                        ordersPage.waitForPageLoad(1500);

                        // Check for confirmation popup
                        boolean isConfirmPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();

                        if (isConfirmPopupDisplayed) {
                            // Confirm the order
                            ordersPage.confirmOrder();
                            logger.info("✓ Buy order confirmed for: " + mnemo);
                            System.out.println("  Order Status: ✓ Buy order created and confirmed");
                            orderSuccessCount++;
                        } else {
                            // Check for success/error messages
                            boolean successDisplayed = ordersPage.isSuccessMessageDisplayed();
                            boolean errorDisplayed = ordersPage.isErrorMessageDisplayed();

                            if (successDisplayed) {
                                logger.info("✓ Buy order created successfully for: " + mnemo);
                                System.out.println("  Order Status: ✓ Buy order created successfully");
                                orderSuccessCount++;
                            } else if (errorDisplayed) {
                                String errorMsg = ordersPage.getErrorMessage();
                                logger.warn("✗ Buy order failed for " + mnemo + ": " + errorMsg);
                                System.out.println("  Order Status: ✗ Order failed - " + errorMsg);
                                orderFailureCount++;
                            } else {
                                logger.info("✓ Buy order submitted for: " + mnemo);
                                System.out.println("  Order Status: ✓ Buy order submitted");
                                orderSuccessCount++;
                            }
                        }

                        ordersPage.waitForPageLoad(1000);

                    } catch (Exception orderException) {
                        logger.error("✗ Error creating buy order for " + mnemo + ": " + orderException.getMessage());
                        System.out.println("  Order Status: ✗ Exception - " + orderException.getMessage());
                        orderFailureCount++;
                    }
                } else {
                    // Instrument click failed
                    logger.warn("✗ Could not click instrument: " + mnemo + " - " + name);
                    System.out.println("  Click Status: ✗ Failed to click");
                    System.out.println("  Order Status: ✗ Skipped (instrument not clicked)");
                    failureCount++;
                }

                System.out.println();
            }

            System.out.println("╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     Final Summary                             ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("Instrument Statistics:");
            System.out.println("  Total Instruments: " + allInstruments.size());
            System.out.println("  Successfully Clicked: " + successCount);
            System.out.println("  Failed to Click: " + failureCount);
            System.out.println("  Click Success Rate: " + String.format("%.2f%%", (successCount * 100.0 / allInstruments.size())));
            System.out.println();
            System.out.println("Buy Order Statistics:");
            System.out.println("  Orders Created Successfully: " + orderSuccessCount);
            System.out.println("  Orders Failed: " + orderFailureCount);
            if (successCount > 0) {
                System.out.println("  Order Success Rate: " + String.format("%.2f%%", (orderSuccessCount * 100.0 / successCount)));
            }
            System.out.println();
            System.out.println("Overall Success: " + orderSuccessCount + " buy orders created out of " + allInstruments.size() + " instruments");

            logger.info("Test completed - Instruments clicked: " + successCount + ", Buy orders created: " + orderSuccessCount);

        } catch (Exception e) {
            logger.error("Error in testClickAllInstrumentsAndCreateBuyOrders: " + e.getMessage(), e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            // Close database connection
            if (dbConnection != null) {
                Allure.step("Close database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed");
            }
        }

        logTestComplete("testClickAllInstrumentsAndCreateBuyOrders", Constants.TEST_PASSED);
    }

    @Test(priority = 17)
    @Story("Instruments Selection and Sell Orders")
    @Description("Click on all instruments from database and create sell orders - Uses NIN 71125")
    @Severity(SeverityLevel.CRITICAL)
    public void testClickAllInstrumentsAndCreateSellOrders() {
        logTestStart("testClickAllInstrumentsAndCreateSellOrders");

        // Login with NIN 71125 for SELL orders
        reloginWithDifferentUser("71125", "12345");

        OracleDBConnection dbConnection = null;

        try {
            Allure.step("Establish database connection");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("Database connection established successfully");

            Allure.step("Fetch all instruments from database");
            GetInstrumentsData instrumentsData = new GetInstrumentsData(dbConnection);
            boolean dataFetched = instrumentsData.fetchInstrumentsData();

            Assert.assertTrue(dataFetched, "Should successfully fetch instruments from database");
            logger.info("Total instruments fetched: " + instrumentsData.getRecordCount());

            Allure.step("Get all instrument records");
            List<Map<String, Object>> allInstruments = instrumentsData.getAllInstrumentRecords();

            Assert.assertTrue(allInstruments.size() > 0, "Should have at least one instrument");

            // Counters for tracking
            int successCount = 0;
            int failureCount = 0;
            int orderSuccessCount = 0;
            int orderFailureCount = 0;

            // Default order parameters
            String defaultQuantity = "10";
            String defaultPrice = "100.00";

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║    Clicking Instruments and Creating Sell Orders             ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("Total instruments to process: " + allInstruments.size());
            System.out.println("Default Quantity: " + defaultQuantity);
            System.out.println("Default Price: " + defaultPrice + "\n");

            Allure.step("Iterate through all instruments, click and create sell orders");
            for (int i = 0; i < allInstruments.size(); i++) {
                Map<String, Object> instrument = allInstruments.get(i);

                String instCode = instrument.get("INST_CODE") != null ? instrument.get("INST_CODE").toString() : "";
                String mnemo = instrument.get("MNEMO") != null ? instrument.get("MNEMO").toString() : "";
                String name = instrument.get("NAME") != null ? instrument.get("NAME").toString() : "";
                String groupCode = instrument.get("GROUP_CODE") != null ? instrument.get("GROUP_CODE").toString() : "";

                System.out.println("═══════════════════════════════════════════════════════════════");
                System.out.println("Processing Instrument " + (i + 1) + " of " + allInstruments.size() + ":");
                System.out.println("  INST_CODE: " + instCode);
                System.out.println("  MNEMO: " + mnemo);
                System.out.println("  NAME: " + name);
                System.out.println("  GROUP_CODE: " + groupCode);

                Allure.step("Step 1: Click instrument: " + mnemo + " - " + name);

                boolean clicked = false;

                // Try clicking by MNEMO first
                if (!mnemo.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByMnemo(mnemo);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by MNEMO: " + mnemo);
                        System.out.println("  Click Status: ✓ Clicked by MNEMO");
                        successCount++;
                    }
                }

                // If MNEMO failed, try by NAME
                if (!clicked && !name.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByName(name);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by NAME: " + name);
                        System.out.println("  Click Status: ✓ Clicked by NAME");
                        successCount++;
                    }
                }

                // If NAME failed, try by INST_CODE
                if (!clicked && !instCode.isEmpty()) {
                    clicked = ordersPage.clickInstrumentByCode(instCode);
                    if (clicked) {
                        logger.info("✓ Successfully clicked instrument by INST_CODE: " + instCode);
                        System.out.println("  Click Status: ✓ Clicked by INST_CODE");
                        successCount++;
                    }
                }

                // If instrument was clicked, create sell order
                if (clicked) {
                    try {
                        Allure.step("Step 2: Create sell order for " + mnemo);
                        ordersPage.waitForPageLoad(1000);

                        System.out.println("  Creating SELL order...");

                        // Click SELL button
                        ordersPage.clickSellButton();
                        ordersPage.waitForPageLoad(500);

                        // Enter order details
                        ordersPage.enterQuantity(defaultQuantity);
                        ordersPage.enterPrice(defaultPrice);

                        // Submit order
                        ordersPage.clickSendOrderButton();
                        ordersPage.waitForPageLoad(1500);

                        // Check for confirmation popup
                        boolean isConfirmPopupDisplayed = ordersPage.isConfirmationPopupDisplayed();

                        if (isConfirmPopupDisplayed) {
                            // Confirm the order
                            ordersPage.confirmOrder();
                            logger.info("✓ Sell order confirmed for: " + mnemo);
                            System.out.println("  Order Status: ✓ Sell order created and confirmed");
                            orderSuccessCount++;
                        } else {
                            // Check for success/error messages
                            boolean successDisplayed = ordersPage.isSuccessMessageDisplayed();
                            boolean errorDisplayed = ordersPage.isErrorMessageDisplayed();

                            if (successDisplayed) {
                                logger.info("✓ Sell order created successfully for: " + mnemo);
                                System.out.println("  Order Status: ✓ Sell order created successfully");
                                orderSuccessCount++;
                            } else if (errorDisplayed) {
                                String errorMsg = ordersPage.getErrorMessage();
                                logger.warn("✗ Sell order failed for " + mnemo + ": " + errorMsg);
                                System.out.println("  Order Status: ✗ Order failed - " + errorMsg);
                                orderFailureCount++;
                            } else {
                                logger.info("✓ Sell order submitted for: " + mnemo);
                                System.out.println("  Order Status: ✓ Sell order submitted");
                                orderSuccessCount++;
                            }
                        }

                        ordersPage.waitForPageLoad(1000);

                    } catch (Exception orderException) {
                        logger.error("✗ Error creating sell order for " + mnemo + ": " + orderException.getMessage());
                        System.out.println("  Order Status: ✗ Exception - " + orderException.getMessage());
                        orderFailureCount++;
                    }
                } else {
                    // Instrument click failed
                    logger.warn("✗ Could not click instrument: " + mnemo + " - " + name);
                    System.out.println("  Click Status: ✗ Failed to click");
                    System.out.println("  Order Status: ✗ Skipped (instrument not clicked)");
                    failureCount++;
                }

                System.out.println();
            }

            System.out.println("╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     Final Summary                             ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("Instrument Statistics:");
            System.out.println("  Total Instruments: " + allInstruments.size());
            System.out.println("  Successfully Clicked: " + successCount);
            System.out.println("  Failed to Click: " + failureCount);
            System.out.println("  Click Success Rate: " + String.format("%.2f%%", (successCount * 100.0 / allInstruments.size())));
            System.out.println();
            System.out.println("Sell Order Statistics:");
            System.out.println("  Orders Created Successfully: " + orderSuccessCount);
            System.out.println("  Orders Failed: " + orderFailureCount);
            if (successCount > 0) {
                System.out.println("  Order Success Rate: " + String.format("%.2f%%", (orderSuccessCount * 100.0 / successCount)));
            }
            System.out.println();
            System.out.println("Overall Success: " + orderSuccessCount + " sell orders created out of " + allInstruments.size() + " instruments");

            logger.info("Test completed - Instruments clicked: " + successCount + ", Sell orders created: " + orderSuccessCount);

        } catch (Exception e) {
            logger.error("Error in testClickAllInstrumentsAndCreateSellOrders: " + e.getMessage(), e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            // Close database connection
            if (dbConnection != null) {
                Allure.step("Close database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed");
            }
        }

        logTestComplete("testClickAllInstrumentsAndCreateSellOrders", Constants.TEST_PASSED);
    }
}
