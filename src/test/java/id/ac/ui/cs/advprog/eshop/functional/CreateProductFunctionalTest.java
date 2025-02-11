package id.ac.ui.cs.advprog.eshop.functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Functional test for verifying that a user can create a product and
 * see it in the product list.
 */
public class CreateProductFunctionalTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Set the ChromeDriver path if necessary, e.g.:
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateProduct() {
        // Navigate to the Create Product page
        driver.get("http://localhost:8080/product/create");

        // Wait until the form is visible (we use the form tag here since it’s the only one on the page)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));

        // Locate the product name input using its id "nameInput" (or By.name("productName"))
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput")));
        String testProductName = "Test Product " + System.currentTimeMillis();
        nameInput.sendKeys(testProductName);

        // Locate the product quantity input using its id "quantityInput" (or By.name("productQuantity"))
        WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantityInput")));
        quantityInput.sendKeys("10");

        // Locate and click the submit button (using its type attribute)
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();

        // Wait for redirection to the product list page (which should include "/product/list" in the URL)
        wait.until(ExpectedConditions.urlContains("/product/list"));
        assertTrue(driver.getCurrentUrl().contains("/product/list"),
                "Expected to be redirected to /product/list, but got: " + driver.getCurrentUrl());

        // Verify that the newly created product appears on the product list page.
        // This example assumes that the product name is displayed somewhere on the list page.
        WebElement productElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'" + testProductName + "')]")
        ));
        assertNotNull(productElement,
                "The newly created product (" + testProductName + ") should be visible in the product list.");
    }
}
