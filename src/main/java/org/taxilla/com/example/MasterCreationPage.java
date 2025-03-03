package org.taxilla.com.example;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

/**
 * Page Object class for Master Creation functionality
 * Supports cross-browser operations and provides robust error handling
 */
public class MasterCreationPage {
    private static final Logger logger = LogManager.getLogger(MasterCreationPage.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(60L);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500L);
    private static final int MAX_RETRIES = 3;
    private static final String SCREENSHOTS_DIR = "test-output/screenshots";
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    private final Actions actions;

    // Page Factory Elements
    @FindBy(xpath = "//span[text()='MASTERS']")
    private WebElement module;

    @FindBy(xpath = "//span[@mattooltip='Add new master']")
    private WebElement addMaster;

    @FindBy(xpath = "(//div[@type='button'])[1]")
    private WebElement addCreate;

    @FindBy(xpath = "(//input[@placeholder='Enter a valid name here'])[1]")
    private WebElement enterName;

    @FindBy(xpath = "//textarea[@placeholder='Enter description here']")
    private WebElement descriptioninput;


    @FindBy(xpath = "//span[text()='Save']")
    private WebElement saveMaster;

    @FindBy(xpath = "//button[.//span[text()='Save']]")
    private WebElement saveButton1;

    @FindBy(xpath = "//button[@mattooltip='Add new column']")
    private WebElement addButton;

    @FindBy(xpath = "(//input[@placeholder='Enter a valid name here'])[1]")
    private WebElement columnName;

    @FindBy(xpath = "//div//mat-checkbox[@name='primary']")
    private WebElement primaryCheckbox;

    @FindBy(xpath = "//p[contains(text(), 'Column [') and contains(text(), 'created successfully.')]")
    private WebElement createdSuccess;

    @FindBy(xpath = "//span[text()=' Publication View ']")
    private WebElement publicationMaster;

    @FindBy(xpath = "//span[text()='Submit']")
    private WebElement publicationSubmit;

    @FindBy(xpath = "//span[text()='Proceed']")
    private WebElement publicationWorkflowAction;

    @FindBy(xpath = "//span[text()='Publish']")
    private WebElement publicationWorkflowAction2;

    @FindBy(xpath = "//span[text()='Check for publication status']")
    private WebElement publicationWorkflowAction3;

    @FindBy(xpath = "//span[text()='Reject']")
    private WebElement publicationReject;

    @FindBy(xpath = "//div[@class='loader']")
    private WebElement loader;

    /**
     * Constructor initializes WebDriver and WebDriverWait
     * @param driver WebDriver instance
     */
   
     public MasterCreationPage(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
        createScreenshotsDirectory();
        logger.info("Initialized MasterCreationPage");
    }



     private void createScreenshotsDirectory() {
        File directory = new File(SCREENSHOTS_DIR);
        if (!directory.exists() && directory.mkdirs()) {
            logger.info("Created screenshots directory: {}", directory.getAbsolutePath());
        }
    }
   
    private <T> void waitWithFluentWait(Function<WebDriver, T> condition, String elementName) {
        // Parameterizing FluentWait with WebDriver type
        FluentWait<WebDriver> fluentWait = new FluentWait<>(this.driver)
            .withTimeout(TIMEOUT)
            .pollingEvery(POLLING_INTERVAL)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(NoSuchElementException.class)
            .ignoring(ElementNotInteractableException.class);
    
        try {
            fluentWait.until(condition);
        } catch (TimeoutException var5) {
            logger.error("Timeout waiting for element '{}': {}", elementName, var5.getMessage());
            this.takeScreenshot("timeout_" + elementName);
            throw var5;
        }
    }
    



  
   
    // public boolean validateFlashMessage() {
    //     try {
    //         WebElement message = new WebDriverWait(driver, Duration.ofSeconds(FLASH_MESSAGE_TIMEOUT))
    //                 .until(ExpectedConditions.visibilityOf(createdSuccess));
    //         String actualMessage = message.getText();
    //         logger.info("Flash message received: " + actualMessage);
    //         return actualMessage.contains("created successfully");
    //     } catch (TimeoutException e) {
    //         logger.warn("Flash message not displayed");
    //         return false;
    //     }
    // }

    private void waitForElementToBeClickable(WebElement element, String elementName) {
        this.waitWithFluentWait((driver) -> {
            try {
                return ExpectedConditions.and(new ExpectedCondition[]{ExpectedConditions.visibilityOf(element), ExpectedConditions.elementToBeClickable(element)}).apply(driver);
            } catch (StaleElementReferenceException var3) {
                return null;
            }
        }, elementName);
    }


    private void scrollIntoView(WebElement element) {
        try {
            this.jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'nearest'});", element);
            this.sleep(500L);
        } catch (Exception var3) {
            logger.warn("Failed to scroll element into view: {}", var3.getMessage());
        }

    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", var4);
        }
    }
   
    
   
    private void clickWithRetry(WebElement element, String elementName) {
        Exception lastException = null;
        
        for (int attempt = 1; true; attempt++) {
            try {
                waitForElementToBeClickable(element, elementName);
                scrollIntoView(element);
                
                try {
                    actions.moveToElement(element).click().perform();
                } catch (ElementClickInterceptedException e) {
                    jsExecutor.executeScript("arguments[0].click();", element);
                }
                
                logger.info("Successfully clicked '{}' on attempt {}", elementName, attempt);
                return;
            } catch (Exception e) {
                lastException = e;
                logger.warn("Attempt {} failed for clicking '{}': {}", attempt, elementName, e.getMessage());
                
                if (attempt == MAX_RETRIES) {
                    logger.error("Failed to click '{}' after {} attempts", elementName, MAX_RETRIES);
                    takeScreenshot("click_failure_" + elementName);
                    throw new RuntimeException("Failed to click element: " + elementName, lastException);
                }
                
                sleep(Duration.ofSeconds(attempt).toMillis());
            }
        }
    }

    private void waitForSpinner() {
        try {
            this.waitWithFluentWait((driver) -> {
                try {
                    return ExpectedConditions.or(new ExpectedCondition[]{ExpectedConditions.invisibilityOf(this.loader), ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'loader')]")))}).apply(driver);
                } catch (StaleElementReferenceException var3) {
                    return true;
                }
            }, "Loading Spinner");
            logger.debug("Loading spinner disappeared");
        } catch (TimeoutException var2) {
            logger.warn("Loading spinner timeout: {}", var2.getMessage());
            this.takeScreenshot("spinner_timeout");
        }

    }
       
    private void enterText(WebElement element, String text, String fieldName) {
        try {
            this.waitForElementToBeClickable(element, fieldName);
            this.scrollIntoView(element);
            this.actions.moveToElement(element).click().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).sendKeys(text).perform();
            logger.info("Entered text '{}' in field '{}'", text, fieldName);
        } catch (Exception var5) {
            logger.error("Failed to enter text in field '{}': {}", fieldName, var5.getMessage());
            this.takeScreenshot("text_entry_failure_" + fieldName);
            throw new RuntimeException("Failed to enter text in field: " + fieldName, var5);
        }
    }
    
        public void masterCreation() {
            logger.info("Creating new master entity");
             // Generate random strings for name and description
        String name = generateRandomString();
        String description = generateRandomString();
            
            // Click through the steps to create a master
            clickWithRetry(module, "Masters Module");
            clickWithRetry(addMaster, "Add Master");
            clickWithRetry(addCreate, "Create Master");
            enterText(enterName, name,"Master Name");
            enterText(descriptioninput, description, "Master Description");
            clickWithRetry(saveMaster, "Save Master");
            clickWithRetry(addButton, "Add Column");
            enterText(enterName, name,"Master Name");
            enterText(descriptioninput, description, "Master Description");
            scrollIntoView(primaryCheckbox);
            clickWithRetry(primaryCheckbox, "Primary Checkbox");
            clickWithRetry(saveButton1, "Save button");
            waitForSpinner();
            
            logger.info("Master creation process completed successfully");
        }

        // Random string generator
    public String generateRandomString() {
        return "M_" + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
    }



        public void publicationMaster() {
            clickWithRetry(publicationMaster, "Publication View");
            clickWithRetry(publicationSubmit, "Submit Button");
            clickWithRetry(publicationWorkflowAction, "Proceed Button");
            clickWithRetry(publicationWorkflowAction2, "Publish Button");
            clickWithRetry(publicationWorkflowAction, "Proceed Button");
            waitForSpinner();
            clickWithRetry(publicationWorkflowAction3, "Status Check Button");
clickWithRetry(publicationWorkflowAction, "Proceed Button");
            waitForSpinner();


            logger.info("Master created successfully");     
        }
    
        public void clickReject() {
            clickWithRetry(publicationReject, "Reject Button");
        }


        private void takeScreenshot(String fileName) {
            try {
                File screenshot = ((TakesScreenshot)this.driver).getScreenshotAs(OutputType.FILE);
                String var10003 = fileName.replaceAll("[^a-zA-Z0-9-_.]", "_");
                File destFile = new File("test-output/screenshots", var10003 + "_" + System.currentTimeMillis() + ".png");
                FileUtils.copyFile(screenshot, destFile);
                logger.info("Screenshot saved: {}", destFile.getAbsolutePath());
            } catch (IOException var4) {
                logger.error("Failed to take screenshot: {}", var4.getMessage());
            }
    
        }
    }