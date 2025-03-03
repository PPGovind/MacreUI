
package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.taxilla.com.example.LoginPage;
import org.taxilla.com.example.MasterCreationPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class MasterCreationTest {
    private static final Logger logger = LoggerFactory.getLogger(MasterCreationTest.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final Duration TIMEOUTPAGE = Duration.ofSeconds(10);
    private static final Duration TIMEOUTSCRIPT = Duration.ofSeconds(2);
    
    private WebDriver driver;
    private LoginPage loginPage;
    private MasterCreationPage masterCreationPage;

    @BeforeMethod(alwaysRun = true)
    public void loginDetails() throws Exception {
        logger.info("Setting up WebDriver and performing login");
        try {
            WebDriverManager.chromedriver().setup();

            // Initialize WebDriver with optimized options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            driver = new ChromeDriver(options);

            // Set timeouts
            driver.manage().timeouts().implicitlyWait(TIMEOUT);
            driver.manage().timeouts().pageLoadTimeout(TIMEOUTPAGE);
            driver.manage().timeouts().scriptTimeout(TIMEOUTSCRIPT);

            // Navigate to the login page
            driver.get("https://vdm.uat.taxilla.com/");

            // Initialize pages and perform login
            loginPage = new LoginPage(driver);
            masterCreationPage = new MasterCreationPage(driver);

            loginPage.signIn();
            loginPage.login("platform3553", "Test@321", "test", "test");
            logger.info("Login successful");
            
        } catch (Exception e) {
            logger.error("Setup failed: {}", e.getMessage(), e);
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }
    }

    @Test(enabled = true, priority = 0)
    public void masterCreationTest() throws Throwable {
        logger.info("Starting master creation test");
        try {
            masterCreationPage.masterCreation();
            masterCreationPage.publicationMaster();
            // Execute master creation workflow
            // boolean isMasterCreated = masterCreationPage.masterCreation();
            
            // // Assert that master creation was successful
            // org.testng.Assert.assertTrue(isMasterCreated, "Master creation failed");
            logger.info("Master creation test completed successfully");
            
        } catch (Exception e) {
            logger.error("Master creation test failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        logger.info("Cleaning up resources");
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Browser closed successfully");
            } catch (Exception e) {
                logger.error("Failed to close browser: {}", e.getMessage(), e);
            }
        }
    }
}

