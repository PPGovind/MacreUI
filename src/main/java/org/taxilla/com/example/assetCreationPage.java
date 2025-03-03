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
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

public class assetCreationPage {
    private static final Logger logger = LogManager.getLogger(assetCreationPage.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(60);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);
    private static final int MAX_RETRIES = 3;
    private static final String SCREENSHOTS_DIR = "test-output/screenshots";

    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    private final Actions actions;

    @FindBy(xpath = "//span[text()='ASSETS']")
    private WebElement mastersModule;

   /* @FindBy(xpath = "//div[contains(@class,'mat-grid-tile')][1]")
    private WebElement assetTile;*/

    @FindBy(xpath = "//span[contains(@mattooltip,'Add new asset')]")
    private WebElement addAssetBtn;

    @FindBy(xpath = "//div[@type='button'][1]")
    private WebElement createBtn;

    @FindBy(xpath = "//input[@placeholder='Enter a valid name here'][1]")
    private WebElement nameInput;

    @FindBy(xpath = "//mat-select[@name='assetType']")
    private WebElement assetTypeSelect;


    @FindBy(xpath = "//textarea[@placeholder='Enter description here']")
    private WebElement descriptionInput;

    @FindBy(xpath = "//input[@placeholder='Enter a valid date format']")
    private WebElement datesFormateSelect;

    @FindBy(xpath = "(//mat-option[@role='option'])[1]")
    private WebElement dateFormatOption;

    @FindBy(xpath = "//mat-label[text()='Default Decimal Format']")
    private WebElement defaultDecimalSelect;

    @FindBy(xpath = "//input[@placeholder='Enter a valid default decimal format']")
    private WebElement defaultDecimalFormatInput;


    @FindBy(xpath = "//span[text()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class,'loader')]")
    private WebElement loader;

    
    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addEntityBtn;

   

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[1]")
    private WebElement entityCreate;


    @FindBy(xpath = "//div//mat-checkbox[@name='primary']")
    private WebElement entityPrimaryChk;

    @FindBy(xpath = "//span[text()=' Save ']")
    private WebElement entitySaveButton;


    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addFieldBtn;
    
    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[1]")
    private WebElement fieldCreate;
        

    @FindBy(xpath = "//mat-select[@name='datatype']")
    private WebElement fieldDatatype;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[1]")
    private WebElement fieldListBox1;

    @FindBy(xpath = "//mat-label[text()='Default Value']")
    private WebElement fieldDefaultVal;

    @FindBy(xpath = "//mat-checkbox[@name='mandatory']")
    private WebElement fieldMandatoryChk;

    @FindBy(xpath = "//mat-checkbox[@name='autoCalculate']")
    private WebElement fieldAutoCalculateChk;

    @FindBy(xpath = "//mat-checkbox[@name='readOnly']")
    private WebElement fieldReadOnlyChk;

    @FindBy(xpath = "//mat-checkbox[@name='masterDataCheck']")
    private WebElement fieldMasterDataChk;

    @FindBy(xpath = "//mat-checkbox[@name='searchable']")
    private WebElement fieldSearchableChk;

    @FindBy(xpath = "//mat-checkbox[@name='sortable']")
    private WebElement fieldSortableChk;

    @FindBy(xpath = "//span[text()='Save']")
    private WebElement fieldSaveButton;

    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addattachmentBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[2]")
    private WebElement attachmentCreate;

    @FindBy(xpath = "//input[@placeholder='Select']")
    private WebElement attachmentSelect;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[1]")
    private WebElement attachmentpdf;

    @FindBy(xpath = "//span[text()='Save']")
    private WebElement attachmentSaveButton;

    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addgridBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[3]")
    private WebElement gridCreate;
  
    @FindBy(xpath = "(//span[@class='mdc-tab__text-label'])[5]")
    private WebElement gridProps;


    @FindBy(xpath = "//mat-label[text()='Number of Rows']")
    private WebElement gridRows;

    @FindBy(xpath = "//mat-label[text()='Number of Columns']")
    private WebElement gridColumns;

    @FindBy(xpath = "(//input[@type='text'])[1]")
    private WebElement gridRowName;

    @FindBy(xpath = "(//input[@type='text'])[2]")
    private WebElement gridColumnName;

    @FindBy(xpath = "//span[text()='Save']")
    private WebElement gridSaveButton;


    @FindBy(xpath = "(//a[@routerlinkactive='active'])[2]")
    private WebElement routerAssetlink;

    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addlookupBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[2]")
    private WebElement lookupCreate;

    @FindBy(xpath = "//button[@mattooltip='Business key settings']")
    private WebElement businessKeyBtn;

    @FindBy(xpath = "//input[@placeholder='Search or Select field(s)']")
    private WebElement addBusinessKeyBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[2]")
    private WebElement businessKeyCreate;

    @FindBy(xpath = "//span[text()='Save']")
    private WebElement businessKeySaveButton;


    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement lookupaddField;


    @FindBy(xpath = "//mat-icon[text()='add']")
    private WebElement addSubEntityBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[4]")
    private WebElement subEntityCreate;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[2]")
    private WebElement selectLookupbtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[1]")
    private WebElement lookupCreate1;

    @FindBy(xpath = "//input[@placeholder='Enter a valid name here'][1]")
    private WebElement lookupnameInput;

  
    @FindBy(xpath = "//textarea[@placeholder='Enter description here']")
    private WebElement lookupdescriptionInput;



    @FindBy(xpath = " //mat-checkbox[@name='caseSensitive']")
    private WebElement lookupCasesensitive;

    @FindBy(xpath = "//span[text()=' Save ']")
    private WebElement lookupSaveButton;

    @FindBy(xpath = "(//a[contains(@class, 'mat-mdc-tab-link')])[3]")
    private WebElement lookupproperties;




    @FindBy(xpath = "//button[@mattooltip='Add new field']")
    private WebElement addnewFieldBtn;

    @FindBy(xpath = "(//span[@class='mdc-list-item__primary-text'])[2]")
    private WebElement selectdisplayField;


    @FindBy(xpath = "//mat-select[@name='displayFieldId']")
    private WebElement displayFieldId;

    @FindBy(xpath = "//mat-select[@name='valueFieldId']")
    private WebElement valueFieldId;

    @FindBy(xpath = "//mat-select[@name='orderByFieldId']")
    private WebElement orderByFieldId;

    @FindBy(xpath = "//span[text()=' Save Settings ']")
    private WebElement lookupsaveSettings;

    @FindBy(xpath = "//input[@placeholder='Enter a valid value here'][1]")
    private WebElement FieldData;

    @FindBy(xpath = "(//span[contains(text(),' Workflow ')])[1]")
    private WebElement workflowbtn;

    @FindBy(xpath = "//button[@mattooltip='Create workflow']")
    private WebElement addworkflowBtn;

    @FindBy(xpath = "//span[(text()='Create')]")
    private WebElement workflowcreateBtn;

    @FindBy(xpath = "//button[@mattooltip='Save workflow stage positions']")
    private WebElement savebtn;

   

    @FindBy(xpath = "//span[contains(text(),'Publication View')]")
    private WebElement publicationViewBtn;

    @FindBy(xpath = "//span[text()='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//span[text()='Proceed']")
    private WebElement proceedButton;

    @FindBy(xpath = "//span[text()='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//span[contains(text(),'Check for publication status')]")
    private WebElement checkStatusButton;

    public assetCreationPage(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
        createScreenshotsDirectory();
        logger.info("Initialized assetCreationPage");
    }

    private void createScreenshotsDirectory() {
        File directory = new File(SCREENSHOTS_DIR);
        if (!directory.exists() && directory.mkdirs()) {
            logger.info("Created screenshots directory: {}", directory.getAbsolutePath());
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

    public void navigateToAssets() {
        logger.info("Navigating to assets");
        clickWithRetry(mastersModule, "Masters Module");
        waitForSpinner();
        /*clickWithRetry(assetTile, "Asset Tile");
        waitForSpinner();*/
    }

    public void createNewAsset( ) {
        // Generate random strings for name and description
        String name = generateRandomString();
        String description = generateRandomString();
        String defaultdecimal = "0.00";
        logger.info("Creating new asset: {}", name);
        clickWithRetry(addAssetBtn, "Add Asset");
        clickWithRetry(createBtn, "Create");
        enterText(nameInput, name, "Asset Name");
        enterText(descriptionInput, description, "Description");
        clickWithRetry(datesFormateSelect,"DateFmtSelect");
        clickWithRetry(dateFormatOption,"DateFmtOpt");
        enterText(defaultDecimalSelect,defaultdecimal, "DefaultDecimal");
        clickWithRetry(saveButton, "Save");
        waitForSpinner();

    }

    // Random string generator
    public String generateRandomString() {
        return "Asset_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

     
    public void createNewEntity() {
        // Generate random strings for name and description
        String name = generateRandomString1();
        String description = generateRandomString1();
        logger.info("Creating new entity");
    try {
        // Hover over and hold the 'Add Entity' button
        Actions actions = new Actions(driver);
        actions.moveToElement(addEntityBtn).clickAndHold().perform();
        logger.info("Mouse hovered and held on 'Add Entity' button");
           // Wait for the popup to appear
           waitForPopupToAppear(entityCreate);

           // Move to the 'Entity Create' element and click
           actions.moveToElement(entityCreate).click().perform();
           logger.info("Clicked on 'Entity Create' input field");
   
           // Wait for the spinner to disappear
           waitForSpinner();
       } catch (Exception e) {
           logger.error("Failed to create entity: {}", e.getMessage());
           takeScreenshot("entity_creation_failure");
           throw e;
       }
        enterText(nameInput, name, "Asset Name");
        enterText(descriptionInput, description, "Description");
        clickWithRetry(entityPrimaryChk, "entityPrimaryChk");
        clickWithRetry(entitySaveButton, "Save");
        waitForSpinner();
    }

    private void waitForPopupToAppear(WebElement element) {
    // Wait until the specified element is visible
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOf(element));
    logger.info("Popup appeared successfully with the target element");
}


 // Random string generator
 public String generateRandomString1() {
    return "Entity" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
}

public void createNewField() {
    // Generate random strings for name and description
    String name = generateRandomString2();
    String description = generateRandomString2();
    String defaultValue = "TestingQA";
    logger.info("Creating new field");
    try {
        // Hover over and hold the 'Add Field' button
        Actions actions = new Actions(driver);
        actions.moveToElement(addFieldBtn).clickAndHold().perform();
        logger.info("Mouse hovered and held on 'Add Field' button");
           // Wait for the popup to appear
           waitForPopupToAppear(fieldCreate);

           // Move to the 'Field Create' element and click
           actions.moveToElement(fieldCreate).click().perform();
           logger.info("Clicked on 'Field Create' input field");
   
           // Wait for the spinner to disappear
           waitForSpinner();
       } catch (Exception e) {
           logger.error("Failed to create field: {}", e.getMessage());
           takeScreenshot("field_creation_failure");
           throw e;
       }

    enterText(nameInput, name, "Asset Name");
    enterText(descriptionInput, description, "Description");
    clickWithRetry(fieldDatatype, "fieldDatatype");
    clickWithRetry(fieldListBox1, "fieldListBox1");
    enterText(fieldDefaultVal, defaultValue, "fieldDefaultVal");
    clickWithRetry(fieldMandatoryChk, "fieldMandatoryChk");
    clickWithRetry(fieldAutoCalculateChk, "fieldAutoCalculateChk");
    clickWithRetry(fieldReadOnlyChk, "fieldReadOnlyChk");
   // clickWithRetry(fieldMasterDataChk, "fieldMasterDataChk");
    clickWithRetry(fieldSearchableChk, "fieldSearchableChk");
    clickWithRetry(fieldSortableChk, "fieldSortableChk");
    clickWithRetry(fieldSaveButton, "Save");
    waitForSpinner();
}


// Random string generator
public String generateRandomString2() {
    return "Field" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
}



 public void routerAssetlink() {
  clickWithRetry(routerAssetlink, "routerAssetlink");
 }



 public void assetBusinesskey() {
     // Generate random strings for name and description
     String name = generateRandomString2();
     String description = generateRandomString2();
     String defaultValue = "TestingQA";

     try {
        // Hover over and hold the 'Add Field' button
        Actions actions = new Actions(driver);
        actions.moveToElement(addFieldBtn).clickAndHold().perform();
        logger.info("Mouse hovered and held on 'Add Field' button");
           // Wait for the popup to appear
           waitForPopupToAppear(fieldCreate);

           // Move to the 'Field Create' element and click
           actions.moveToElement(fieldCreate).click().perform();
           logger.info("Clicked on 'Field Create' input field");
   
           // Wait for the spinner to disappear
           waitForSpinner();
       } catch (Exception e) {
           logger.error("Failed to create field: {}", e.getMessage());
           takeScreenshot("field_creation_failure");
           throw e;
       }

  enterText(nameInput, name, "Asset Name");
  enterText(descriptionInput, description, "Description");
  clickWithRetry(fieldDatatype, "fieldDatatype");
  clickWithRetry(fieldListBox1, "fieldListBox1");
  enterText(fieldDefaultVal, defaultValue, "fieldDefaultVal");
  clickWithRetry(fieldMandatoryChk, "fieldMandatoryChk");
  clickWithRetry(fieldSaveButton, "Save");
  clickWithRetry(businessKeyBtn, "businesskey");
  clickWithRetry(addBusinessKeyBtn, "addBusinessKeyBtn");
  clickWithRetry(businessKeyCreate, "businessKeyCreate");
  clickWithRetry(businessKeySaveButton, "businessKeySaveButton");

 }


 public void assetlookup() {
    // Generate random strings for name and description
    String name = generateRandomString5();
    String description = generateRandomString5();

    logger.info("Creating new subentity");
    try {
        // Hover over and hold the 'Add lookup' button
        Actions actions = new Actions(driver);
        actions.moveToElement(addlookupBtn).clickAndHold().perform();
        logger.info("Mouse hovered and held on 'Add lookup' button");
           // Wait for the popup to appear
           waitForPopupToAppear(lookupCreate);

           // Move to the 'lookup Create' element and click
           actions.moveToElement(lookupCreate).click().perform();
           logger.info("Clicked on 'lookup Create' input field");
   
           // Wait for the spinner to disappear
           waitForSpinner();
       } catch (Exception e) {
           logger.error("Failed to create lookup: {}", e.getMessage());
           takeScreenshot("lookup_creation_failure");
           throw e;
       }
 enterText(lookupnameInput, name, "lookup Name");
 enterText(lookupdescriptionInput, description, "Description");
 clickWithRetry(lookupCasesensitive, "lookupCase sensitive");
 clickWithRetry(lookupSaveButton, "lookupSaveButton");
 clickWithRetry(addnewFieldBtn,"addnewFieldBtn");
 enterText(lookupnameInput, name, "lookup Field name");
 enterText(lookupdescriptionInput, description, "Description");
 clickWithRetry(lookupSaveButton, "FieldSaveButton");
 clickWithRetry(lookupproperties, "lookupproperties");
 clickWithRetry(displayFieldId , "displayFieldId");
 clickWithRetry(selectdisplayField, "selectdisplayField");
 clickWithRetry(valueFieldId, "valueFieldId");
 clickWithRetry(selectdisplayField, "selectdisplayField");
     clickWithRetry(orderByFieldId, "orderByFieldId");

 clickWithRetry(selectdisplayField, "selectdisplayField");
clickWithRetry(lookupsaveSettings, "lookupSaveSettings");
clickWithRetry(addlookupBtn, "addlookupBtn");
enterText(FieldData, name, "New Data");
clickWithRetry(businessKeySaveButton, "lookupSaveButton");
  }

    // Random string generator
public String generateRandomString5() {
    return "lookup" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
}

 public void subEntity() {

     // Generate random strings for name and description
     String name = generateRandomString1();
     String description = generateRandomString1();

     logger.info("Creating new subentity");
     try {
         // Hover over and hold the 'Add subEntity' button
         Actions actions = new Actions(driver);
         actions.moveToElement(addSubEntityBtn).clickAndHold().perform();
         logger.info("Mouse hovered and held on 'Add subEntity' button");
            // Wait for the popup to appear
            waitForPopupToAppear(subEntityCreate);
 
            // Move to the 'subEntity Create' element and click
            actions.moveToElement(subEntityCreate).click().perform();
            logger.info("Clicked on 'subEntity Create' input field");
    
            // Wait for the spinner to disappear
            waitForSpinner();
        } catch (Exception e) {
            logger.error("Failed to create subentity: {}", e.getMessage());
            takeScreenshot("Subentity_creation_failure");
            throw e;
        }
  enterText(nameInput, name, "Asset Name");
  enterText(descriptionInput, description, "Description");
  clickWithRetry(entityPrimaryChk, "entityPrimaryChk");
  clickWithRetry(entitySaveButton, "Save");
  waitForSpinner();
 }



public void attachementField() {
    // Generate random strings for name and description
    String name = generateRandomString3();
    String description = generateRandomString3();
//String defaultValue = "TestingQA";
    logger.info("Creating new attachment field");
    try {
        // Hover over and hold the 'Add attachment Field' button
        Actions actions = new Actions(driver);
        actions.moveToElement(addattachmentBtn).clickAndHold().perform();
        logger.info("Mouse hovered and held on 'Add attachment Field' button");
           // Wait for the popup to appear
           waitForPopupToAppear(attachmentCreate);

           // Move to the 'attachment Field Create' element and click
           actions.moveToElement(attachmentCreate).click().perform();
           logger.info("Clicked on 'attachment Field Create' input field");
   
           // Wait for the spinner to disappear
           waitForSpinner();
       } catch (Exception e) {
           logger.error("Failed to create attachment field: {}", e.getMessage());
           takeScreenshot("attachment_field_creation_failure");
           throw e;
       }
       enterText(nameInput, name, "Asset Name");
       enterText(descriptionInput, description, "Description");
       clickWithRetry(fieldMandatoryChk, "fieldMandatoryChk");
       clickWithRetry(attachmentSelect, "attachmentSelect");
       clickWithRetry(attachmentpdf, "attachmentpdf");
       clickWithRetry(attachmentSaveButton, "Save");
       waitForSpinner();



    }


    // Random string generator
public String generateRandomString3() {
    return "attachement" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
}



    public void gridField() {
        // Generate random strings for name and description
        String name = generateRandomString4();
        String description = generateRandomString4();
        String gridrows = "1";
        String gridcols = "1";
        String gridrowsName = "Row";
        String gridcolsName = "Column";
    //String defaultValue = "TestingQA";
        logger.info("Creating new grid field");
        try {
            // Hover over and hold the 'Add grid Field' button
            Actions actions = new Actions(driver);
            actions.moveToElement(addgridBtn).clickAndHold().perform();
            logger.info("Mouse hovered and held on 'Add grid Field' button");
               // Wait for the popup to appear
               waitForPopupToAppear(gridCreate);
    
               // Move to the 'grid Field Create' element and click
               actions.moveToElement(gridCreate).click().perform();
               logger.info("Clicked on 'grid Field Create' input field");
       
               // Wait for the spinner to disappear
               waitForSpinner();
           } catch (Exception e) {
               logger.error("Failed to create grid field: {}", e.getMessage());
               takeScreenshot("grid_field_creation_failure");
               throw e;
           }
           enterText(nameInput, name, "Asset Name");
           enterText(descriptionInput, description, "Description");
           clickWithRetry(fieldMandatoryChk, "fieldMandatoryChk");
           clickWithRetry(gridProps, "gridProps");
           enterText(gridRows, gridrows, "gridrows");
           enterText(gridColumns, gridcols, "gridcols");
           enterText(gridRowName, gridrowsName, "gridrowsName");
           enterText(gridColumnName, gridcolsName, "gridcolsName");
           clickWithRetry(gridSaveButton, "Save");
           waitForSpinner();

        }
    

        
    // Random string generator
public String generateRandomString4() {
    return "grid" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
}



  public void createworkflow() {
    logger.info("Creating new workflow");
    clickWithRetry(workflowbtn, "workflowbtn");
    clickWithRetry(addworkflowBtn, "addworkflowBtn");
    clickWithRetry(workflowcreateBtn, "CreateBtn");
    clickWithRetry(savebtn, "Save");
    waitForSpinner();
}
    


    public void publishAsset() {
        logger.info("Publishing asset");
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



    private void takeScreenshot(String fileName) {
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
