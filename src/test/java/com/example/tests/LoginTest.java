package com.example.tests;

import org.openqa.selenium.WebDriver;
import org.taxilla.com.example.LoginPage;
import org.testng.annotations.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cross-browser test suite for Login functionality
 * Supports parallel execution across different browsers
 */
@Test(groups = {"login", "regression"})
public class LoginTest {
    private static final Logger LOGGER = Logger.getLogger(LoginTest.class.getName());
    public WebDriver driver;
    private LoginPage loginPage;

    // Test Data
    private static final String BASE_URL = "https://vdm.uat.taxilla.com/"; // Add your application URL here
    private static final String VALID_USER = "Platform3553";
    private static final String VALID_PASSWORD = "Test@321";
    private static final String VALID_2FA_1 = "test";
    private static final String VALID_2FA_2 = "test";

    /**
     * Setup method runs before each test method
     * @param browserType browser to use for the test
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(@Optional("CHROME") LoginPage.Browser browserType) {
        try {
            LOGGER.info("Setting up test with browser: " + browserType);
            driver = LoginPage.createDriver(browserType);
            driver.get(BASE_URL); // Navigate to the application URL
            loginPage = new LoginPage(driver);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to setup test", e);
            throw e;
        }
    }

    /**
     * Cleanup method runs after each test method
     */
    @AfterMethod
    public void cleanup() {
        if (loginPage != null) {
            loginPage.closeBrowser();
        }
    }

   /* *//**
     * Test successful login with valid credentials
     *//*
    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LOGGER.info("Running successful login test");
        try {
            boolean loginSuccess = loginPage.login(VALID_USER, VALID_PASSWORD, VALID_2FA_1, VALID_2FA_2);
            Assert.assertTrue(loginSuccess, "Login should be successful with valid credentials");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Test failed", e);
            throw e;
        }
    }

    *//**
     * Test failed login with invalid credentials
     *//*
    @Test(description = "Verify login failure with invalid credentials")
    public void testFailedLogin() {
        LOGGER.info("Running failed login test");
        try {
            boolean loginSuccess = loginPage.login("InvalidUser", "InvalidPassword", VALID_2FA_1, VALID_2FA_2);
            Assert.assertFalse(loginSuccess, "Login should fail with invalid credentials");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Test failed", e);
            throw e;
        }
    }

    *//**
     * Test login with empty credentials
     *//*
    @Test(description = "Verify login behavior with empty credentials")
    public void testEmptyCredentials() {
        LOGGER.info("Running empty credentials test");
        try {
            boolean loginSuccess = loginPage.login("", "", "", "");
            Assert.assertFalse(loginSuccess, "Login should fail with empty credentials");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Test failed", e);
            throw e;
        }
    }

    *//**
     * Test login with special characters
     *//*
    @Test(description = "Verify login with special characters")
    public void testSpecialCharacters() {
        LOGGER.info("Running special characters test");
        try {
            boolean loginSuccess = loginPage.login("test@#$%", "pass@#$%", "123@#$", "456@#$");
            Assert.assertFalse(loginSuccess, "Login should handle special characters appropriately");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Test failed", e);
            throw e;
        }
    }*/
}
