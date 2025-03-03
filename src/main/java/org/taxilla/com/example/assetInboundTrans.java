package org.taxilla.com.example;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

public class assetInboundTrans {
    private static final Logger logger = LogManager.getLogger(TransformationCreation.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(60);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);
    private static final int MAX_RETRIES = 3;
    private static final String SCREENSHOTS_DIR = "test-output/screenshots";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor jsExecutor;
    private final Actions actions;

    @FindBy(xpath = "//span[text()='MASTERS']")
    private WebElement mastersModule;

    @FindBy(xpath = "//span[text()='ASSETS']")
    private WebElement assetsModule;

    @FindBy(xpath = "//div[contains(@class,'mat-grid-tile')][1]")
    private WebElement assetTile;

    @FindBy(xpath = "//button[contains(@mattooltip,'Go to Transformations')]")
    private WebElement transformationsBtn;

    @FindBy(xpath = "//span[@mattooltip='Add new transformation']")
    private WebElement addTransformationBtn;

    @FindBy(xpath = "//div//p[text()='Create transformation.']")
    private WebElement createBtn;

    @FindBy(xpath = "//div//p[text()='Create a new ETL transformation.']")
    private WebElement createBtnetl;

    @FindBy(xpath = "(//input[@placeholder='Enter a valid name here'])[1]")
    private WebElement nameInput1;
    @FindBy(xpath = "(//input[@placeholder='Enter a valid name here'])[2]")
    private WebElement nameInput;

    @FindBy(xpath = "//button[@mattooltip='Add new source']")
    private WebElement addSourceBtn;

    @FindBy(xpath = "//mat-select[@name='sourceDataFormat']")
    private WebElement  sourceFormatSelect;

    @FindBy(xpath = "//span[contains(text(),'EXCEL')]")
    private WebElement excelOption;

    @FindBy(xpath = "//mat-select[@name='fileExtension']")
    private WebElement fileExtensionSelect;

    @FindBy(xpath = "//mat-label[contains(text(),'Choose File')]")
    private WebElement chooseFileLabel;

    @FindBy(xpath = "//span[text()='Add']")
    private WebElement addButton;

    @FindBy(xpath = "//button[@mattooltip='Add new target']")
    private WebElement addTargetBtn;

    @FindBy(xpath = "//mat-select[@name='format']")
    private WebElement targetFormatSelect;

    @FindBy(xpath = "//span[contains(text(),'Master')]")
    private WebElement masterOption;

    @FindBy(xpath = "//span[text()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//button[@mattooltip='Go to visual data mapper(VDM)']")
    private WebElement vdmButton;

    @FindBy(xpath = "//span[text()='Update']")
    private WebElement updateVDM;

    @FindBy(xpath = "(//div[@role='tab'])[2]")
    private WebElement targetTab;


    @FindBy(xpath = "(//div[@class='settings-icon justify-content-end ng-star-inserted'])[1]")
    private WebElement setting1;

    @FindBy(xpath = "//mat-icon[text()=' swap_horiz ']")
    private WebElement swapHoriz;

    @FindBy(xpath = "//input[@placeholder='Search or Select entity']")
    private WebElement searchSelect;

    @FindBy(xpath = "(//span[@class='ng-star-inserted'])[1]")
    private WebElement selectF1;

    @FindBy(xpath = "(//input[@placeholder='Search or Select field'])[1]")
    private WebElement selectSourceField;

    @FindBy(xpath = "(//input[@placeholder='Search or Select field'])[2]")
    private WebElement selectF11;


    @FindBy(xpath = "//mat-icon[text()='menu']")
    private WebElement dropdownMenu;

    @FindBy(xpath = "//mat-icon[text()='save']")
    private WebElement saveButton1;

    @FindBy(xpath = "//li[@mattooltip='Close visual data mapper(VDM)']")
    private WebElement closeDialog;

    @FindBy(xpath = "//button[@mattooltip='Close Dialog']")
    private WebElement vdmcloseDialog;

    @FindBy(xpath = "//mat-select[@name='sourceType']")
    private WebElement srcType;

    @FindBy(xpath = "//span[text()=' Master ']")
    private WebElement selectMaster;





    @FindBy(xpath = "//div[contains(@class,'loader')]")
    private WebElement loader;

    @FindBy(xpath = "//button[@mattooltip='Generate chain']")
    private WebElement generateChainBtn;

    @FindBy(xpath = "//button[@mattooltip='Save chain']")
    private WebElement saveChainBtn;

    @FindBy(xpath = "//button[contains(@class,'close')]")
    private WebElement closeButton;

    @FindBy(xpath = "//span[contains(text(),' Publication View ')]")
    private WebElement publicationViewBtn;

    @FindBy(xpath = "//span[text()='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//span[text()='Proceed']")
    private WebElement proceedButton;

    @FindBy(xpath = "//span[text()='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//span[contains(text(),'Check for publication status')]")
    private WebElement checkStatusButton;

    @FindBy(xpath = "//button[@mattooltip='Generate chain']")
    private WebElement generateChain;

    @FindBy(xpath = "//mat-icon[text()='save']")
    private WebElement saveButton2;


    public assetInboundTrans(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT, POLLING_INTERVAL);
        this.jsExecutor = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
        createScreenshotsDirectory();
        logger.info("Initialized TransformationCreation page");
    }

    private void createScreenshotsDirectory() {
        try {
            File directory = new File(SCREENSHOTS_DIR);
            if (!directory.exists() && !directory.mkdirs()) {
                logger.error("Failed to create screenshots directory: {}", SCREENSHOTS_DIR);
                throw new IOException("Failed to create screenshots directory");
            }
            logger.debug("Screenshots directory created/verified at: {}", SCREENSHOTS_DIR);
        } catch (SecurityException | IOException e) {
            logger.error("Security exception while creating screenshots directory: {}", e.getMessage());
            throw new RuntimeException("Security error creating screenshots directory", e);
        }
    }

    private <T> void waitWithFluentWait(Function<WebDriver, T> condition, String elementName) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(TIMEOUT)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotInteractableException.class);

        try {
            fluentWait.until(condition);
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for element '{}': {}", elementName, e.getMessage());
            takeScreenshot("timeout_" + elementName);
            throw e;
        }
    }

    private void waitForElementToBeClickable(WebElement element, String elementName) {
        waitWithFluentWait(driver -> {
            try {
                return ExpectedConditions.and(
                        ExpectedConditions.visibilityOf(element),
                        ExpectedConditions.elementToBeClickable(element)
                ).apply(driver);
            } catch (StaleElementReferenceException e) {
                return null;
            }
        }, elementName);
    }

    private void clickWithRetry(WebElement element, String elementName) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
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

    private void scrollIntoView(WebElement element) {
        try {
            jsExecutor.executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'nearest'});",
                    element
            );
            sleep(500);
        } catch (Exception e) {
            logger.warn("Failed to scroll element into view: {}", e.getMessage());
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }

    private void waitForSpinner() {
        try {
            waitWithFluentWait(driver -> {
                try {
                    return ExpectedConditions.or(
                            ExpectedConditions.invisibilityOf(loader),
                            ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(
                                    By.xpath("//div[contains(@class,'loader')]")
                            ))
                    ).apply(driver);
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            }, "Loading Spinner");
            logger.debug("Loading spinner disappeared");
        } catch (TimeoutException e) {
            logger.warn("Loading spinner timeout: {}", e.getMessage());
            takeScreenshot("spinner_timeout");
        }
    }

    private void enterText(WebElement element, String text, String fieldName) {
        try {
            waitForElementToBeClickable(element, fieldName);
            scrollIntoView(element);

            actions.moveToElement(element)
                    .click()
                    .keyDown(Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .sendKeys(text)
                    .perform();

            logger.info("Entered text '{}' in field '{}'", text, fieldName);
        } catch (Exception e) {
            logger.error("Failed to enter text in field '{}': {}", fieldName, e.getMessage());
            takeScreenshot("text_entry_failure_" + fieldName);
            throw new RuntimeException("Failed to enter text in field: " + fieldName, e);
        }
    }

    public void navigateToTransformations() {
        logger.info("Navigating to transformations");
        clickWithRetry(assetsModule,"Assets Module");
        waitForSpinner();
        clickWithRetry(assetTile, "Asset Tile");
        waitForSpinner();
        clickWithRetry(transformationsBtn, "Transformations Button");

        waitForSpinner();
    }

    public void createNewTransformation(String name) {
        logger.info("Creating new transformation: {}", name);
        clickWithRetry(addTransformationBtn, "Add Transformation");
        clickWithRetry(createBtn, "Create");
        clickWithRetry(createBtnetl,"Select ETL");
        enterText(nameInput1, name, "Transformation Name");
        //
        waitForSpinner();
    }

    public void configureSource() throws InterruptedException,AWTException {
        logger.info("Configuring source");
        String name = generateRandomString();

        clickWithRetry(addSourceBtn, "Add Source");
        enterText(nameInput, name, "Transformation Name");
        // clickWithRetry(srcType, "Source Type");

        clickWithRetry(sourceFormatSelect, "Source Format");
        clickWithRetry(excelOption, "Excel Option");
        uploadFile(driver,"D:\\Macre UI\\excelfile\\src.xlsx");
        clickWithRetry(addButton, "Add");
        waitForSpinner();
    }

    public String generateRandomString() {
        return "M_" + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
    }

    public void configureTarget(){
        logger.info("Configuring target");
        String name = generateRandomString1();

        clickWithRetry(addTargetBtn, "Add Target");
        enterText(nameInput, name, "Transformation Name");
        //  clickWithRetry(selectMaster, "Master Option");
        clickWithRetry(targetFormatSelect, "Target Format");
        clickWithRetry(masterOption, "Master Option");
        clickWithRetry(addButton, "Add");
        clickWithRetry(saveButton, "Save");
        waitForSpinner();
    }

    public String generateRandomString1() {
        return "M_" + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
    }

    public void configureMapping() {
        logger.info("Configuring visual data mapper");
        clickWithRetry(vdmButton, "VDM Button");
        clickWithRetry(targetTab,"Target Tab");
        clickWithRetry(updateVDM,"Update VDM");
        clickWithRetry(setting1, "Source Settings");
        clickWithRetry(swapHoriz,"swapHoriz");
        clickWithRetry(searchSelect, "search select");
        clickWithRetry(selectF1,"select Field");
        clickWithRetry(selectSourceField,"select source Field");
        clickWithRetry(selectF1,"select Field");
        clickWithRetry(selectSourceField,"selectsourceField");
        clickWithRetry(selectF11,"select Field");
        clickWithRetry(selectF1,"selecttargetField");
        clickWithRetry(vdmcloseDialog,"vdmcloseDialog");
        waitForSpinner();
        // Add mapping configuration logic here
    }


    public void hoverAndClickSaveInDropdown() {
        // Create an Actions object
        Actions actions = new Actions(driver);

        try {
            // Ensure the dropdown is visible before hovering
            if (dropdownMenu.isDisplayed()) {
                actions.moveToElement(dropdownMenu).perform(); // Hover over the dropdown
            } else {
                System.out.println("Dropdown menu is not visible. in VDM");
            }

            // Ensure the Save button is visible before clicking
            if (saveButton1.isDisplayed()) {
                saveButton1.click(); // Click on the Save button
            } else {
                System.out.println("Save button is not visible.in VDM");
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        clickWithRetry(closeDialog, "closeDialog in VDM");

    }



    public void publishTransformation() {
        logger.info("Publishing transformation");
        clickWithRetry(publicationViewBtn, "Publication View");
        clickWithRetry(submitButton, "Submit");
        clickWithRetry(proceedButton, "Proceed");
        clickWithRetry(publishButton, "Publish");
        clickWithRetry(proceedButton, "Proceed");
        waitForSpinner();
        clickWithRetry(checkStatusButton, "Check Status");
        clickWithRetry(proceedButton, "Proceed");
        waitForSpinner();
    }

    public void chainCreation() {
        logger.info("Chain Creation");
        clickWithRetry(generateChain, "GenerateChain");
        clickWithRetry(saveButton2, "SaveButtonforchain");
        waitForSpinner();
    }

    public void publishChain() {
        logger.info("Publishing chain creation");
        clickWithRetry(publicationViewBtn, "Publication View");
        clickWithRetry(submitButton, "Submit");
        clickWithRetry(proceedButton, "Proceed");
        clickWithRetry(publishButton, "Publish");
        clickWithRetry(proceedButton, "Proceed");
        waitForSpinner();
        clickWithRetry(checkStatusButton, "Check Status");
        clickWithRetry(proceedButton, "Proceed");
        waitForSpinner();
    }








  /*  public boolean executeTransformationWorkflow(String transformationName) {
        try {
            navigateToTransformations();
            createNewTransformation(transformationName);
            configureSource();
            configureTarget();
            configureMapping();
            publishTransformation();
            logger.info("Transformation workflow completed successfully");
            return true;
        } catch (Exception e) {
            logger.error("Failed to execute transformation workflow: {}", e.getMessage(), e);
            takeScreenshot("transformation_workflow_error");
            return false;
        }
    }*/



    /**
     * Uploads a file using the Robot class.
     *
     * @param driver   The Selenium WebDriver instance.
     * @param filePath The absolute path of the file to upload.
     * @throws AWTException If an error occurs while using the Robot class.
     * @throws InterruptedException If the thread is interrupted during delays.
     */
    public  void uploadFile(WebDriver driver, String filePath) throws AWTException {
        // Locate the "Choose File" button and click it
        WebElement uploadButton = driver.findElement(By.xpath("//mat-label[contains(text(),'Choose File')]"));
        uploadButton.click();

        // Use Robot class to handle the file upload dialog
        Robot robot = new Robot();

        // Set the file path to clipboard
        StringSelection filePathSelection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePathSelection, null);

        // Simulate CTRL+V to paste the file path
        robot.delay(1000); // Wait for the dialog to appear
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        // Simulate pressing Enter to confirm the file selection
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        // Optional: Wait for the upload to complete
        robot.delay(2000);
        System.out.println("File uploaded successfully!");
    }







    public void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(SCREENSHOTS_DIR,
                    fileName.replaceAll("[^a-zA-Z0-9-_.]", "_") +
                            "_" + System.currentTimeMillis() + ".png"
            );
            FileUtils.copyFile(screenshot, destFile);
            logger.info("Screenshot saved: {}", destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
        }
    }
}
