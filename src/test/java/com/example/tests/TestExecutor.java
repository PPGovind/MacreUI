package com.example.tests;

//import com.example.utils.DriverManager;
import com.example.utils.DynamicRunner;


public class TestExecutor {

    public static void main(String[] args) {
        // Initialize WebDriver
    //   DriverManager.initializeDriver();


        // Path to XML file
        String xmlFilePath = "dynamic-tests.xml";

        // Run tests dynamically
        System.out.println("Starting dynamic test execution...");
        DynamicRunner.runTests(xmlFilePath);
        System.out.println("Dynamic test execution completed.");

        // Quit WebDriver
       // DriverManager.quitDriver();
    }
}
