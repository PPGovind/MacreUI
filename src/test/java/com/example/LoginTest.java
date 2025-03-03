package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.taxilla.com.example.LoginPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;



public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setup() throws InterruptedException {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Initialize WebDriver and maximize the browser
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Navigate to the login page
        driver.get("https://vdm.qa.taxilla.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Initialize the LoginPage object
        loginPage = new LoginPage(driver);
        loginPage.signIn();

    }

    @Test
    public void loginTest() throws InterruptedException {
        System.out.println("staring....");
        Thread.sleep(2000);
        System.out.println("ending....");

        // Perform login with valid credentials
        loginPage.login("taxillauser", "test@123", "test", "test");
       // loginPage.clickSubmit();


    }

   /* @AfterMethod
    public void teardown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }*/
}
