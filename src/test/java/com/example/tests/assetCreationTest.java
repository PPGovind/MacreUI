package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.taxilla.com.example.LoginPage;
import org.taxilla.com.example.assetCreationPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class assetCreationTest {
    private static final Logger logger = LoggerFactory.getLogger(assetCreationTest.class);
    WebDriver driver;
    LoginPage loginPage;
    assetCreationPage assetCreationPage;

    @BeforeMethod(alwaysRun = true)
    public void loginDetails() throws Exception {
        WebDriverManager.chromedriver().setup();

        // Initialize WebDriver with optimized options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        
        driver = new ChromeDriver(options);

        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(2));

        // Navigate to the login page
        driver.get("https://vdm.qa.taxilla.com");

        // Initialize the LoginPage object and perform login
        loginPage = new LoginPage(driver);
        loginPage.signIn();
        loginPage.login("platform3553", "Test@321", "test", "test");

        // Initialize the asset creation page
        assetCreationPage = new assetCreationPage(driver);
    }

    @Test(enabled = true, priority = 0)
    public void testAssetCreation() throws Throwable {
        try {
            assetCreationPage.createNewAsset();
            assetCreationPage.createNewEntity();
            assetCreationPage.createNewField();
            assetCreationPage.assetBusinesskey();
            assetCreationPage.routerAssetlink();
            assetCreationPage.createNewEntity();
            assetCreationPage.createNewField();
            assetCreationPage.attachementField();
            assetCreationPage.gridField();
            assetCreationPage.subEntity();
            assetCreationPage.createNewField();
            assetCreationPage.attachementField();
            assetCreationPage.gridField();
            assetCreationPage.routerAssetlink();
            assetCreationPage.assetlookup();
            assetCreationPage.routerAssetlink();
            assetCreationPage.createworkflow();
            assetCreationPage.publishAsset();


    
        } catch (Exception e) {
            logger.error("Failed to create asset: " + e.getMessage(), e);
            throw e;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit(); // Close the browser and clean up resources
            } catch (Exception e) {
                System.err.println("Exception while closing the browser: " + e.getMessage());
            }
        }
    }
}
