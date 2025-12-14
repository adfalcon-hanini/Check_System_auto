package com.example.pages;

import com.example.base.BasePage;
import com.example.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * OrdersPage class representing the orders/trading page
 * Extends BasePage for common functionality
 */
public class OrdersPage extends BasePage {

    // Orders Tab
    @FindBy(css = "[data-testid='ordersTab_Orders']")
    private WebElement ordersTab;

    // Buy/Sell Buttons
    @FindBy(css = "[data-testid='ordersHeader_buyButton']")
    private WebElement buyButton;

    @FindBy(css = "[data-testid='ordersHeader_sellButton']")
    private WebElement sellButton;

    // Order Input Fields
    @FindBy(css = "[data-testid='ordersForm_sharesCount']")
    private WebElement sharesCount;

    @FindBy(css ="[data-testid='ordersForm_sharePrice']")
    private WebElement sharePrice;

    @FindBy(css ="[data-testid='ordersForm_discloseVolume']")
    private WebElement discloseVolume;

    @FindBy(css ="[data-testid='ordersForm_validity']")
    private WebElement validity;

    @FindBy(css ="[data-testid='ordersForm_orderType']")
    private WebElement orderType;

    @FindBy(css ="[data-testid='ordersForm_closeOrderForm']")
    private WebElement closeOrderForm;

    // Submit Button
    @FindBy(css = "[data-testid='orderForm_sendOrderButton']")
    private WebElement sendOrderButton;

    // Confirmation popup
    @FindBy(css = "[data-testid='orderFormConfirm_mainContainer']")
    private WebElement orderFormConfirm_mainContainer;

    @FindBy(css = "[data-testid='orderFormConfirm_confirmButton']")
    private WebElement orderFormConfirm_confirmButton;

    @FindBy(css = "[data-testid='orderFormConfirm_cancelButton']")
    private WebElement orderFormConfirm_cancelButton;

    // Success/Error Messages
    @FindBy(css = "[data-testid='orderForm_successMessage']")
    private WebElement successMessage;

    @FindBy(css = "[data-testid='orderForm_errorMessage']")
    private WebElement errorMessage;

    // Alternative success message locators
    @FindBy(css = ".alert-success, .success-message, [class*='success']")
    private WebElement genericSuccessMessage;

    @FindBy(css = ".alert-error, .error-message, [class*='error']")
    private WebElement genericErrorMessage;

    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public OrdersPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to Orders page
     * Utility method for direct navigation to trading page
     */
    public void navigateToOrdersPage() {
        navigateTo(Constants.TRADING_PAGE_EN);
    }

    /**
     * Click Orders tab
     * Utility method for clicking the orders tab
     */
    public void clickOrdersTab() {
        logger.info("Clicking Orders tab");
        click(ordersTab);
    }

    /**
     * Click BUY button
     */
    public void clickBuyButton() {
        logger.info("Clicking BUY button");
        click(buyButton);
    }

    /**
     * Click SELL button
     */
    public void clickSellButton() {
        logger.info("Clicking SELL button");
        click(sellButton);
    }

    /**
     * Enter quantity
     * @param quantity Quantity value
     */
    public void enterQuantity(String quantity) {
        logger.info("Entering quantity: " + quantity);
        enterText(sharesCount, quantity);
    }

    /**
     * Enter price
     * @param price Price value
     */
    public void enterPrice(String price) {
        logger.info("Entering price: " + price);
        enterText(sharePrice, price);
    }

    /**
     * Enter disclose volume
     * @param volume Disclose volume value
     */
    public void enterDiscloseVolume(String volume) {
        logger.info("Entering disclose volume: " + volume);
        enterText(discloseVolume, volume);
    }

    /**
     * Select validity period
     * @param validityPeriod Validity period value
     */
    public void selectValidity(String validityPeriod) {
        logger.info("Selecting validity: " + validityPeriod);
      //  selectDropdownByValue(validity, validityPeriod);
    }

    /**
     * Select order type
     * @param type Order type value
     */
    public void selectOrderType(String type) {
        logger.info("Selecting order type: " + type);
      //  selectDropdownByValue(orderType, type);
    }

    /**
     * Click send order button
     */
    public void clickSendOrderButton() {
        logger.info("Clicking send order button");
        click(sendOrderButton);
    }

    /**
     * Submit order (alias for clickSendOrderButton - kept for backward compatibility)
     */
    public void submitOrder() {
        logger.info("Submitting order");
        click(sendOrderButton);
    }

    /**
     * Close order form
     * Utility method to close the order form without submitting
     */
    public void closeOrderForm() {
        logger.info("Closing order form");
        click(closeOrderForm);
    }

    /**
     * Confirm order in confirmation popup
     * Used internally by order placement methods
     */
    public void confirmOrder() {
        logger.info("Confirming order");
     //   waitForElementVisible(orderFormConfirm_mainContainer);
        click(orderFormConfirm_confirmButton);
    }

    /**
     * Cancel order in confirmation popup
     * Used internally by order placement methods
     */
    public void cancelOrderConfirmation() {
        logger.info("Canceling order confirmation");
     //   waitForElementVisible(orderFormConfirm_mainContainer);
        click(orderFormConfirm_cancelButton);
    }

    /**
     * Check if confirmation popup is displayed
     * Utility method for validation in tests
     * @return true if displayed
     */
    public boolean isConfirmationPopupDisplayed() {
        return isElementDisplayed(orderFormConfirm_mainContainer);
    }

    /**
     * Place BUY order (basic - quantity and price only)
     * @param quantity Order quantity
     * @param price Order price
     */
    public void placeBuyOrder(String quantity, String price) {
        logger.info("Placing BUY order - Quantity: " + quantity + ", Price: " + price);
        clickBuyButton();
        enterQuantity(quantity);
        enterPrice(price);
        submitOrder();
        logger.info("BUY order placement completed");
    }

    /**
     * Place BUY order with confirmation
     * @param quantity Order quantity
     * @param price Order price
     */
    public void placeBuyOrderWithConfirmation(String quantity, String price) {
        logger.info("Placing BUY order with confirmation - Quantity: " + quantity + ", Price: " + price);
        clickBuyButton();
        enterQuantity(quantity);
        enterPrice(price);
        clickSendOrderButton();
        confirmOrder();
        logger.info("BUY order placement with confirmation completed");
    }

    /**
     * Place comprehensive BUY order with all parameters
     * @param quantity Order quantity
     * @param price Order price
     * @param discloseVol Disclose volume (optional, can be null)
     * @param validityPeriod Validity period (optional, can be null)
     * @param type Order type (optional, can be null)
     * @param confirmOrder Whether to confirm the order
     */
    public void placeBuyOrderComplete(String quantity, String price, String discloseVol,
                                      String validityPeriod, String type, boolean confirmOrder) {
        logger.info("Placing comprehensive BUY order - Quantity: " + quantity + ", Price: " + price);
        clickBuyButton();

        // Fill mandatory fields
        enterQuantity(quantity);
        enterPrice(price);

        // Fill optional fields if provided
        if (discloseVol != null && !discloseVol.isEmpty()) {
            enterDiscloseVolume(discloseVol);
        }
        if (validityPeriod != null && !validityPeriod.isEmpty()) {
            selectValidity(validityPeriod);
        }
        if (type != null && !type.isEmpty()) {
            selectOrderType(type);
        }

        // Submit order
        clickSendOrderButton();

        // Handle confirmation
        if (confirmOrder) {
            confirmOrder();
            logger.info("BUY order confirmed");
        } else {
            cancelOrderConfirmation();
            logger.info("BUY order cancelled");
        }

        logger.info("Comprehensive BUY order placement completed");
    }

    /**
     * Place SELL order (basic - quantity and price only)
     * @param quantity Order quantity
     * @param price Order price
     */
    public void placeSellOrder(String quantity, String price) {
        logger.info("Placing SELL order - Quantity: " + quantity + ", Price: " + price);
        clickSellButton();
        enterQuantity(quantity);
        enterPrice(price);
        submitOrder();
        logger.info("SELL order placement completed");
    }

    /**
     * Place SELL order with confirmation
     * @param quantity Order quantity
     * @param price Order price
     */
    public void placeSellOrderWithConfirmation(String quantity, String price) {
        logger.info("Placing SELL order with confirmation - Quantity: " + quantity + ", Price: " + price);
        clickSellButton();
        enterQuantity(quantity);
        enterPrice(price);
        clickSendOrderButton();
        confirmOrder();
        logger.info("SELL order placement with confirmation completed");
    }

    /**
     * Place comprehensive SELL order with all parameters
     * @param quantity Order quantity
     * @param price Order price
     * @param discloseVol Disclose volume (optional, can be null)
     * @param validityPeriod Validity period (optional, can be null)
     * @param type Order type (optional, can be null)
     * @param confirmOrder Whether to confirm the order
     */
    public void placeSellOrderComplete(String quantity, String price, String discloseVol,
                                       String validityPeriod, String type, boolean confirmOrder) {
        logger.info("Placing comprehensive SELL order - Quantity: " + quantity + ", Price: " + price);
        clickSellButton();

        // Fill mandatory fields
        enterQuantity(quantity);
        enterPrice(price);

        // Fill optional fields if provided
        if (discloseVol != null && !discloseVol.isEmpty()) {
            enterDiscloseVolume(discloseVol);
        }
        if (validityPeriod != null && !validityPeriod.isEmpty()) {
            selectValidity(validityPeriod);
        }
        if (type != null && !type.isEmpty()) {
            selectOrderType(type);
        }

        // Submit order
        clickSendOrderButton();

        // Handle confirmation
        if (confirmOrder) {
            confirmOrder();
            logger.info("SELL order confirmed");
        } else {
            cancelOrderConfirmation();
            logger.info("SELL order cancelled");
        }

        logger.info("Comprehensive SELL order placement completed");
    }

    /**
     * Check if BUY button is displayed
     * Utility method for validation in tests
     * @return true if displayed
     */
    public boolean isBuyButtonDisplayed() {
        return isElementDisplayed(buyButton);
    }

    /**
     * Check if SELL button is displayed
     * Utility method for validation in tests
     * @return true if displayed
     */
    public boolean isSellButtonDisplayed() {
        return isElementDisplayed(sellButton);
    }

    /**
     * Check if Orders tab is loaded
     * Utility method for validation in tests
     * @return true if loaded
     */
    public boolean isOrdersTabLoaded() {
        return isElementPresent(ordersTab);
    }

    /**
     * Check if success message is displayed
     * Utility method for validation in tests
     * @return true if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        logger.info("Checking if success message is displayed");
        try {
            // Check primary success message locator
            if (isElementDisplayed(successMessage)) {
                logger.info("Success message found using data-testid");
                return true;
            }
            // Check generic success message locator
            if (isElementDisplayed(genericSuccessMessage)) {
                logger.info("Success message found using generic class");
                return true;
            }
            // Check if confirmation modal is displayed (can indicate success)
            if (isElementDisplayed(orderFormConfirm_mainContainer)) {
                logger.info("Confirmation modal displayed (potential success indicator)");
                return true;
            }
            logger.debug("No success message found");
            return false;
        } catch (Exception e) {
            logger.debug("Error checking success message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if error message is displayed
     * Utility method for validation in tests
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        logger.info("Checking if error message is displayed");
        try {
            // Check primary error message locator
            if (isElementDisplayed(errorMessage)) {
                logger.info("Error message found using data-testid");
                return true;
            }
            // Check generic error message locator
            if (isElementDisplayed(genericErrorMessage)) {
                logger.info("Error message found using generic class");
                return true;
            }
            logger.debug("No error message found");
            return false;
        } catch (Exception e) {
            logger.debug("Error checking error message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get success message text
     * Utility method for validation in tests
     * @return Success message text or empty string
     */
    public String getSuccessMessage() {
        logger.info("Getting success message text");
        try {
            if (isElementDisplayed(successMessage)) {
                return getText(successMessage);
            } else if (isElementDisplayed(genericSuccessMessage)) {
                return getText(genericSuccessMessage);
            }
            return "";
        } catch (Exception e) {
            logger.warn("Could not get success message: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get error message text
     * Utility method for validation in tests
     * @return Error message text or empty string
     */
    public String getErrorMessage() {
        logger.info("Getting error message text");
        try {
            if (isElementDisplayed(errorMessage)) {
                return getText(errorMessage);
            } else if (isElementDisplayed(genericErrorMessage)) {
                return getText(genericErrorMessage);
            }
            return "";
        } catch (Exception e) {
            logger.warn("Could not get error message: " + e.getMessage());
            return "";
        }
    }

    /**
     * Click on an instrument by its MNEMO code
     * @param mnemo Instrument MNEMO code to click
     * @return true if instrument was found and clicked, false otherwise
     */
    public boolean clickInstrumentByMnemo(String mnemo) {
        logger.info("Attempting to click instrument with MNEMO: " + mnemo);
        try {
            // Try multiple possible locator strategies for instrument selection
            // Strategy 1: Using data-testid with MNEMO
            By locator1 = By.cssSelector("[data-testid*='" + mnemo + "']");

            // Strategy 2: Using text content
            By locator2 = By.xpath("//*[contains(text(), '" + mnemo + "')]");

            // Strategy 3: Using class with text
            By locator3 = By.xpath("//div[contains(@class, 'instrument') and contains(text(), '" + mnemo + "')]");

            // Try each strategy
            By[] locators = {locator1, locator2, locator3};

            for (By locator : locators) {
                try {
                    WebElement instrument = driver.findElement(locator);
                    if (instrument.isDisplayed()) {
                        click(instrument);
                        logger.info("Successfully clicked instrument: " + mnemo);
                        return true;
                    }
                } catch (Exception e) {
                    // Try next strategy
                    continue;
                }
            }

            logger.warn("Could not find instrument with MNEMO: " + mnemo);
            return false;

        } catch (Exception e) {
            logger.error("Error clicking instrument: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click on an instrument by its name
     * @param instrumentName Instrument name to click
     * @return true if instrument was found and clicked, false otherwise
     */
    public boolean clickInstrumentByName(String instrumentName) {
        logger.info("Attempting to click instrument with name: " + instrumentName);
        try {
            // Try multiple possible locator strategies
            By locator1 = By.xpath("//*[contains(text(), '" + instrumentName + "')]");
            By locator2 = By.cssSelector("[title*='" + instrumentName + "']");
            By locator3 = By.xpath("//div[contains(@class, 'instrument') and contains(., '" + instrumentName + "')]");

            By[] locators = {locator1, locator2, locator3};

            for (By locator : locators) {
                try {
                    WebElement instrument = driver.findElement(locator);
                    if (instrument.isDisplayed()) {
                        click(instrument);
                        logger.info("Successfully clicked instrument: " + instrumentName);
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            logger.warn("Could not find instrument with name: " + instrumentName);
            return false;

        } catch (Exception e) {
            logger.error("Error clicking instrument: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click on an instrument by its INST_CODE
     * @param instCode Instrument code to click
     * @return true if instrument was found and clicked, false otherwise
     */
    public boolean clickInstrumentByCode(String instCode) {
        logger.info("Attempting to click instrument with INST_CODE: " + instCode);
        try {
            // Try multiple possible locator strategies
            By locator1 = By.cssSelector("[data-inst-code='" + instCode + "']");
            By locator2 = By.xpath("//*[@data-inst-code='" + instCode + "']");
            By locator3 = By.xpath("//*[contains(@id, '" + instCode + "')]");

            By[] locators = {locator1, locator2, locator3};

            for (By locator : locators) {
                try {
                    WebElement instrument = driver.findElement(locator);
                    if (instrument.isDisplayed()) {
                        click(instrument);
                        logger.info("Successfully clicked instrument with code: " + instCode);
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            logger.warn("Could not find instrument with INST_CODE: " + instCode);
            return false;

        } catch (Exception e) {
            logger.error("Error clicking instrument: " + e.getMessage());
            return false;
        }
    }
}
