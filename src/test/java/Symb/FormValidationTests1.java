package Symb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class FormValidationTests1 {
    WebDriver driver;  // Class-level variable for WebDriver

    public static void main(String[] args) {
        FormValidationTests1 test = new FormValidationTests1();  // Create an instance of the test class
        test.runTests();
    }

    public void runTests() {
        // Set up WebDriver
        driver = new ChromeDriver();  // Use the class-level driver variable
        driver.get("https://d3pv22lioo8876.cloudfront.net/tiptop/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Test Case 1: Verify the input is disabled
            WebElement disabledInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@name='my-disabled']")));
            assert !disabledInput.isEnabled() : "Input field is not disabled";

            // Test Case 2: Verify the input is readonly
            WebElement readonlyInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@value='Readonly input']")));
            String readonlyAttr = readonlyInput.getAttribute("readonly");
            assert readonlyAttr != null : "Input field is not readonly";

            // Test Case 4: Verify submit button is disabled when Name is empty
            WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//button[@type='submit']")));
            assert !submitButton.isEnabled() : "Submit button is not disabled when Name is empty";

            // Test Case 5: Verify submit button is enabled when Name and Password are filled
            WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='my-name']")));
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='my-password']")));
            nameField.sendKeys("Test Name");
            passwordField.sendKeys("Test Password");
            assert submitButton.isEnabled() : "Submit button is not enabled when Name and Password are filled";

            driver.findElement(By.xpath("//button[@type='submit']")).click();

            // Wait for the new window to open
            String originalWindow = driver.getWindowHandle();
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait1.until(driver -> driver.getWindowHandles().size() > 1);

            // Switch to the new window
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            // Wait for the 'Received' message to appear in the new window
            WebElement receivedElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@id='message']")));
            System.out.println("Message found: " + receivedElement.getText());

            // Test Case 7: Verify all data is passed to the URL
            String currentUrl = driver.getCurrentUrl();
            assert currentUrl.contains("name=Test+Name") && currentUrl.contains("password=Test+Password")
                    : "Form data not passed correctly to the URL";

            System.out.println("All test cases passed!");
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {
            // Close the browser
            //driver.quit();
        }
    }
}

