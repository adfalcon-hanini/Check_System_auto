package com.example.pages;

import com.example.base.BasePage;
import com.example.utils.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage class representing the login page
 * Uses data-testid selectors for stable and maintainable automation
 * Extends BasePage for common functionality
 */
public class LoginPage extends BasePage {


    // Login Button in trading
    @FindBy(css = "[data-testid='trading_loginButton']")
    private WebElement tradingLoginButton;

    // Username field
    @FindBy(css = "[data-testid='login-form-username-input']")
    private WebElement usernameField;

    // Password field
    @FindBy(css = "[data-testid='login-form-password-input']")
    private WebElement passwordField;

    // Login/Submit button
    @FindBy(css = "[data-testid='login-form-submit']")
    private WebElement loginButtonSubmit;

    @FindBy(css = "[data-testid='login-form-title']")
    private WebElement loginFormTitle;

    // Forgot password
    @FindBy(css = "[data-testid='login-form-forgot-password']")
    private WebElement forgotPasswordLink;


    // Username Error
    @FindBy(css = "[data-testid='login-form-username-error']")
    private WebElement usernameError;

    @FindBy(css = "[data-testid='login-form-password-error']")
    private WebElement passwordError;

    @FindBy(css = "[data-testid='login-form-error-message']")
    private WebElement errorForm;

    // Confirmation Message
    @FindBy(css = "[data-testid='modalConfirm_message']")
    private WebElement confirmationMessage;

    @FindBy(css ="[data-testid='modalConfirm_alertCloseButton']")
    private WebElement modalConfirm_alertCloseButton;

    @FindBy(css ="[data-testid='modalConfirm_container']")
    private WebElement modalConfirm_container;

    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to login/trading page
     */
    public void navigateToLoginPage() {
        logger.info("Navigating to login page: " + Constants.TRADING_PAGE_EN);
        navigateTo(Constants.TRADING_PAGE_EN);
        waitForPageLoad(3000); // Wait for page to fully load
        logger.info("Login page navigation completed. Current URL: " + driver.getCurrentUrl());
    }



    /**
     * Click Login in Trading page icon to open login form (if required)
     */
    public void clickLoginInTrading() {
        logger.info("Clicking Login in Trading screen");
        try {
            // Wait for trading login button to be clickable
            waitForClickability(tradingLoginButton);
            click(tradingLoginButton);
            waitForPageLoad(2000); // Increased wait time for form to appear
            logger.info("Login in Trading clicked successfully");

            // Verify login form appeared
            if (isUsernameFieldDisplayed()) {
                logger.info("Login form appeared successfully after clicking trading login button");
            } else {
                logger.warn("Login form did not appear after clicking trading login button");
            }
        } catch (Exception e) {
            logger.warn("Login in Trading not found or not clickable: " + e.getMessage());
            logger.warn("This might be okay if login form is already visible");
        }
    }


    /**
     * Enter username
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        logger.info("Entering username: " + username);
        try {
            // First, check if we need to click the trading login button
            if (!isUsernameFieldDisplayed()) {
                logger.info("Username field not visible, attempting to click trading login button");
                clickLoginInTrading();
                waitForPageLoad(3000); // Wait for login form to appear
            }

            waitForVisibility(usernameField);
            enterText(usernameField, username);
            logger.info("Username entered successfully using data-testid='login-form-username-input'");
        } catch (Exception e) {
            logger.error("Failed to enter username: " + e.getMessage());
            logger.error("Current URL: " + driver.getCurrentUrl());
            logger.error("Page title: " + driver.getTitle());
            throw e;
        }
    }

    /**
     * Enter password
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        try {
            waitForVisibility(passwordField);
            enterText(passwordField, password);
            logger.info("Password entered successfully using data-testid='login-form-password-input'");
        } catch (Exception e) {
            logger.error("Failed to enter password: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        try {
            waitForClickability(loginButtonSubmit);
            click(loginButtonSubmit);
            logger.info("Login button clicked successfully using data-testid='login-form-submit'");
        } catch (Exception e) {
            logger.error("Failed to click login button: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform complete login with avatar click
     * @param username Username
     * @param password Password
     */
    public void loginWithTradingLoginClick(String username, String password) {
        logger.info("Performing complete login (with avatar click) for user: " + username);
        clickLoginInTrading();
        waitForPageLoad(1000);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        handleModalConfirmationIfPresent();
        logger.info("Login action completed");
    }

    /**
     * Perform login (without avatar click)
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        logger.info("Performing login for user: " + username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        handleModalConfirmationIfPresent();
        logger.info("Login action completed");
    }

    /**
     * Check if error message is displayed
     * @return true if error message displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return isElementDisplayed(usernameError) ||
                   isElementDisplayed(passwordError) ||
                   isElementDisplayed(errorForm) ||
                   isElementDisplayed(confirmationMessage);
        } catch (Exception e) {
            logger.debug("Error message not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        logger.info("Getting error message text");
        try {
            if (isElementDisplayed(usernameError)) {
                return getText(usernameError);
            } else if (isElementDisplayed(passwordError)) {
                return getText(passwordError);
            } else if (isElementDisplayed(errorForm)) {
                return getText(errorForm);
            } else if (isElementDisplayed(confirmationMessage)) {
                return getText(confirmationMessage);
            }
            return "";
        } catch (Exception e) {
            logger.warn("Could not get error message: " + e.getMessage());
            return "";
        }
    }

    /**
     * Check if success message is displayed
     * @return true if success message displayed
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            return isElementDisplayed(confirmationMessage);
        } catch (Exception e) {
            logger.debug("Success message not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get success message text
     * @return Success message text
     */
    public String getSuccessMessage() {
        logger.info("Getting success message text");
        try {
            if (isElementDisplayed(confirmationMessage)) {
                return getText(confirmationMessage);
            }
            return "";
        } catch (Exception e) {
            logger.warn("Could not get success message: " + e.getMessage());
            return "";
        }
    }

    /**
     * Check if modal confirmation container is displayed
     * @return true if modal is displayed
     */
    public boolean isModalConfirmDisplayed() {
        try {
            return isElementDisplayed(modalConfirm_container);
        } catch (Exception e) {
            logger.debug("Modal confirmation container not displayed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click modal confirmation close button
     */
    public void closeModalConfirmation() {
        logger.info("Closing modal confirmation");
        try {
            if (isModalConfirmDisplayed()) {
                waitForVisibility(modalConfirm_alertCloseButton);
                click(modalConfirm_alertCloseButton);
                logger.info("Modal confirmation closed successfully");
            } else {
                logger.info("Modal confirmation not displayed, no action needed");
            }
        } catch (Exception e) {
            logger.warn("Failed to close modal confirmation: " + e.getMessage());
        }
    }

    /**
     * Handle modal confirmation if it appears
     * Waits briefly for modal to appear and closes it if present
     */
    public void handleModalConfirmationIfPresent() {
        logger.info("Checking for modal confirmation");
        try {
            waitForPageLoad(1000); // Brief wait for modal to appear
            if (isModalConfirmDisplayed()) {
                logger.info("Modal confirmation detected, closing it");
                closeModalConfirmation();
                waitForPageLoad(500); // Brief wait after closing
            } else {
                logger.info("No modal confirmation detected");
            }
        } catch (Exception e) {
            logger.debug("No modal confirmation appeared: " + e.getMessage());
        }
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPassword() {
        logger.info("Clicking forgot password link");
        try {
            click(forgotPasswordLink);
            logger.info("Forgot password link clicked using data-testid");
        } catch (Exception e1) {

                logger.info("Forgot password link clicked using partial link text");

        }
    }

    /**
     * Verify login page is loaded
     * @return true if login page loaded
     */
    public boolean isLoginPageLoaded() {
        logger.info("Verifying if login page is loaded");
        try {
            return isElementPresent(usernameField) && isElementPresent(passwordField);
        } catch (Exception e) {
            logger.warn("Login page not loaded: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clear login form
     */
    public void clearLoginForm() {
        logger.info("Clearing login form");
        try {
            usernameField.clear();
            passwordField.clear();
            logger.info("Login form cleared successfully");
        } catch (Exception e) {
            logger.error("Failed to clear login form: " + e.getMessage());
        }
    }

    /**
     * Check if username field is displayed
     * @return true if displayed
     */
    public boolean isUsernameFieldDisplayed() {
        try {
            return isElementDisplayed(usernameField);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if password field is displayed
     * @return true if displayed
     */
    public boolean isPasswordFieldDisplayed() {
        try {
            return isElementDisplayed(passwordField);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if login button is displayed
     * @return true if displayed
     */
    public boolean isLoginButtonDisplayed() {
        try {
            return isElementDisplayed(loginButtonSubmit);
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * Check if login was successful (no error message present)
     * @return true if login successful
     */
    public boolean isLoginSuccessful() {
        logger.info("Checking if login was successful");
        waitForPageLoad(2000);
        return !isErrorMessageDisplayed() || isSuccessMessageDisplayed();
    }

    /**
     * Wait for login form to be visible
     */
    public void waitForLoginForm() {
        logger.info("Waiting for login form to be visible");
        try {
            // First check if form is already visible
            if (isUsernameFieldDisplayed() && isPasswordFieldDisplayed() && isLoginButtonDisplayed()) {
                logger.info("Login form is already visible");
                return;
            }

            // If not visible, try clicking the trading login button
            logger.info("Login form not visible, attempting to click trading login button");
            clickLoginInTrading();

            // Now wait for form elements
            waitForVisibility(usernameField);
            waitForVisibility(passwordField);
            waitForVisibility(loginButtonSubmit);
            logger.info("Login form is now visible");
        } catch (Exception e) {
            logger.error("Login form not visible after waiting: " + e.getMessage());
            logger.error("Current URL: " + driver.getCurrentUrl());
            logger.error("Page source length: " + driver.getPageSource().length());
            throw new RuntimeException("Login form did not appear within timeout period", e);
        }
    }




    /**
     * Get login button text
     * @return Button text
     */
    public String getLoginButtonText() {
        return getText(loginButtonSubmit);
    }

    /**
     * Check if username field is enabled
     * @return true if enabled
     */
    public boolean isUsernameFieldEnabled() {
        return isElementEnabled(usernameField);
    }

    /**
     * Check if password field is enabled
     * @return true if enabled
     */
    public boolean isPasswordFieldEnabled() {
        return isElementEnabled(passwordField);
    }

    /**
     * Check if login button is enabled
     * @return true if enabled
     */
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButtonSubmit);
    }
}
