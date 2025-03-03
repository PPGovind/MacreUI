package org.taxilla.com.example;

import java.util.Random;

public class RandomStringGenerator {

    // Method to generate a random string of a specified length
    public static String generateRandomString(int length) {
        // Define the characters you want to include in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        // Generate the random string
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    // Example usage
    public static void main(String[] args) {
        String randomString = generateRandomString(10); // Length of 10 characters
        System.out.println("Generated Random String: " + randomString);
    }
}

