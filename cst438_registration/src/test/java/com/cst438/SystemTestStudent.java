package com.cst438;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SystemTestStudent {

    private static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
    private static final String URL = "http://your-website-url-here"; // Replace with your website URL

    @Test
    public void addStudentTest() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(URL);

            WebElement addStudentButton = driver.findElement(By.id("addStudentButton"));
            addStudentButton.click();

            WebElement nameField = driver.findElement(By.id("nameField"));
            nameField.sendKeys("John Doe");

            WebElement emailField = driver.findElement(By.id("emailField"));
            emailField.sendKeys("johndoe@example.com");

            // Submit the form to add the student
            WebElement submitButton = driver.findElement(By.id("submitButton"));
            submitButton.click();

            // Verify that the student is added successfully
            WebElement successMessage = driver.findElement(By.id("successMessage"));
            assert(successMessage.isDisplayed());

        } finally {
            driver.quit();
        }
    }

    @Test
    public void updateStudentTest() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(URL);

            // Find and click the link to edit a student's information
            WebElement editLink = driver.findElement(By.id("editLink"));
            editLink.click();

            // Modify the student's information (e.g., change the name)
            WebElement nameField = driver.findElement(By.id("nameField"));
            nameField.clear();
            nameField.sendKeys("Updated Name");

            // Submit the form to update the student
            WebElement updateButton = driver.findElement(By.id("updateButton"));
            updateButton.click();

            // Verify that the student is updated successfully
            WebElement successMessage = driver.findElement(By.id("successMessage"));
            assert(successMessage.isDisplayed());

        } finally {
            driver.quit();
        }
    }

    @Test
    public void deleteStudentTest() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(URL);

            // Find and click the link to delete a student
            WebElement deleteLink = driver.findElement(By.id("deleteLink"));
            deleteLink.click();

            WebElement confirmButton = driver.findElement(By.id("confirmButton"));
            confirmButton.click();

            // Verify that the student is deleted successfully
            WebElement successMessage = driver.findElement(By.id("successMessage"));
            assert(successMessage.isDisplayed());

        } finally {
            driver.quit();
        }
    }
}
