package org.taxilla.com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Advanced Login Page implementation using Page Object Model
 * Supports cross-browser testing and provides robust error handling
 */
public class LoginPage {
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class.getName());
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor jsExecutor;
    private static final int TIMEOUT_SECONDS = 10;
    private final String browserName;

    // Page Factory annotations for elements
    @FindBy(how = How.XPATH, using = "//input[@id='loginUserId']")
    private WebElement userIdField;

    @FindBy(how = How.XPATH, using = "//input[@id='loginUserPassword']")
    private WebElement passwordField;

    @FindBy(how = How.XPATH, using = "(//span[text()=' Submit '])[2]")
    private WebElement submitButton;

    @FindBy(how = How.XPATH, using = "//span[contains(@class, 'mat-simple-snack-bar-content')]")
    private WebElement errMsg;

    @FindBy(how = How.XPATH, using = "(//input[@type='password'])[3]")
    private WebElement firstInput;

    @FindBy(how = How.XPATH, using = "(//input[@type='password'])[4]")
    private WebElement secondInput;

    @FindBy(how = How.XPATH, using = "(//span[text()=' Submit '])[3]")
    private WebElement submitButton1;

    @FindBy(how = How.XPATH, using = "//span[text()=' Sign in with taxilla ']")
    private WebElement sign;

    @FindBy(how = How.XPATH, using = "//div[@class='mdc-circular-progress__spinner-layer']")
    private WebElement spinner;

    /**
     * Enum for supported browsers
     */
    public enum Browser {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI
    }

    /**
     * Factory method to create WebDriver instance based on browser type
     * @param browserType type of browser to create
     * @return WebDriver instance
     */
    public static WebDriver createDriver(Browser browserType) {
        WebDriver driver = switch (browserType) {
            case CHROME -> new ChromeDriver();
            case FIREFOX -> new FirefoxDriver();
            case EDGE -> new EdgeDriver();
            case SAFARI -> new SafariDriver();
            default -> throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        };
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_SECONDS));
        return driver;
    }

    /**
     * Constructor initializes WebDriver and WebDriverWait with cross-browser support
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        this.jsExecutor = (JavascriptExecutor) driver;
        this.browserName = driver.getClass().getSimpleName();
        PageFactory.initElements(driver, this);
        LOGGER.info("Initialized LoginPage with browser: " + browserName);
    }

    /**
     * Handles browser-specific element interactions
     * @param element WebElement to interact with
     */
    private void browserSpecificClick(WebElement element) {
        try {
            switch (browserName) {
                case "ChromeDriver" -> element.click();
                case "FirefoxDriver" -> jsExecutor.executeScript("arguments[0].click();", element);
                default -> {
                    try {
                        element.click();
                    } catch (ElementClickInterceptedException e) {
                        jsExecutor.executeScript("arguments[0].click();", element);
                    }
                }
            }
            LOGGER.info("Clicked element in " + browserName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to click element in " + browserName, e);
            throw new RuntimeException("Failed to click element", e);
        }
    }

    /**
     * Handles browser-specific element input
     * @param element WebElement to input text into
     * @param text text to enter
     */
    private void browserSpecificSendKeys(WebElement element, String text) {
        try {
            if (browserName.equals("FirefoxDriver")) {
                jsExecutor.executeScript("arguments[0].value=arguments[1]", element, text);
            } else {
                element.clear();
                element.sendKeys(text);
            }
            LOGGER.info("Entered text in " + browserName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to enter text in " + browserName, e);
            throw new RuntimeException("Failed to enter text", e);
        }
    }

    /**
     * Waits for element to be clickable and enters text
     * @param element WebElement to input text into
     * @param text text to enter
     */
    private void SendKeys(WebElement element, String text) {
        try {
          //  wait.until(ExpectedConditions.elementToBeClickable(element));
            browserSpecificSendKeys(element, text);
            LOGGER.info("Entered text in element");
        } catch (TimeoutException e) {
            LOGGER.log(Level.SEVERE, "Element not found", e);
            throw new RuntimeException("Element not found", e);
        }
    }

    /**
     * Clicks the given WebElement.
     * @param element WebElement to click
     */
    private void clickElement(WebElement element) {
        try {
            browserSpecificClick(element);
            LOGGER.info("Clicked element");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to click element", e);
            throw new RuntimeException("Failed to click element", e);
        }
    }


    private void waitForSpinnerToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(spinner));
            LOGGER.info("Spinner disappeared");
        } catch (TimeoutException e) {
            LOGGER.warning("Spinner still visible after timeout");
        }
    }

    public void enterUserId(String userId) {
        SendKeys(userIdField, userId);
    }

    public void enterPassword(String password) {
        SendKeys(passwordField, password);
    }

    public void clickSubmit() {
        clickElement(submitButton);
        waitForSpinnerToDisappear();
    }

    public void inputFirstValue(String fInput) {
        SendKeys(firstInput, fInput);
    }

    public void inputSecondValue(String sInput) {
        SendKeys(secondInput, sInput);
    }

    public void clickSubmit1() {
        clickElement(submitButton1);
        waitForSpinnerToDisappear();
    }

    public void signIn() {
        clickElement(sign);
        waitForSpinnerToDisappear();
    }

    /**
     * Gets error message text if present
     * @return error message text or null if not present
     */
    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errMsg));
            String errorText = errMsg.getText();
            LOGGER.warning("Error message displayed: " + errorText);
            return errorText;
        } catch (TimeoutException e) {
            LOGGER.info("No error message displayed");
            return null;
        }
    }

    public boolean isLoginSuccessful() {
        return getErrorMessage() == null;
    }

    /**
     * Performs complete login flow with two-factor authentication
     * @param userId user ID
     * @param password password
     * @param firstInput first 2FA input
     * @param secondInput second 2FA input
     * @return true if login successful
     */
    public boolean login(String userId, String password, String firstInput, String secondInput) {
        try {
            LOGGER.info("Starting login process for user: " + userId + " in " + browserName);
            
            enterUserId(userId);
            enterPassword(password);
            clickSubmit();
            
            if (!isLoginSuccessful()) {
                LOGGER.warning("First stage login failed in " + browserName);
                return false;
            }

            inputFirstValue(firstInput);
            inputSecondValue(secondInput);
            clickSubmit1();
            
            boolean loginSuccess = isLoginSuccessful();
            LOGGER.info("Login " + (loginSuccess ? "successful" : "failed") + " for user: " + userId + " in " + browserName);
            return loginSuccess;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login process failed with exception in " + browserName, e);
            throw new RuntimeException("Login process failed", e);
        }
    }

    /**
     * Closes the browser and cleans up resources
     */
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            LOGGER.info("Closed browser: " + browserName);
        }
    }
}
