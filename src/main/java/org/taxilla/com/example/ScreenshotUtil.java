package org.taxilla.com.example;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private final WebDriver driver;

    // Constructor
    public ScreenshotUtil(WebDriver driver) {
        this.driver = driver;
    }

    // Method to capture and save a screenshot
    public void captureScreenshot(String testClassName, String testMethodName, String fileName) {
        try {
            // Define folder structure
            String baseDir = "./screenshots";
            String classDir = baseDir + "/" + testClassName;
            String methodDir = classDir + "/" + testMethodName;

            // Create directories if they don't exist
            File directory = new File(methodDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Capture screenshot
            TakesScreenshot scrShot = (TakesScreenshot) driver;
            File srcFile = scrShot.getScreenshotAs(OutputType.FILE);

            // Define timestamped filename
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File destFile = new File(methodDir + "/" + fileName + "_" + timestamp + ".png");

            // Save screenshot
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("Screenshot saved at: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}

