package com.example.utils;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.lang.reflect.Method;

public class DynamicRunner {
    public static void runTests(String xmlFilePath) {
        try {
            // Parse XML File
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            NodeList tests = doc.getElementsByTagName("test");

            // Iterate through each test in the XML
            for (int i = 0; i < tests.getLength(); i++) {
                Element testElement = (Element) tests.item(i);

                // Get class and method names
                String className = testElement.getElementsByTagName("class").item(0).getTextContent();
                String methodName = testElement.getElementsByTagName("method").item(0).getTextContent();

                // Load the class and method dynamically
                Class<?> cls = Class.forName(className);
                Object instance = cls.getDeclaredConstructor().newInstance();
                Method method = cls.getMethod(methodName);

                // Execute the method
                System.out.println("Executing: " + className + "." + methodName);
                method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
