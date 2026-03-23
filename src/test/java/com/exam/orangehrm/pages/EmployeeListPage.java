package com.exam.orangehrm.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeListPage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@href,'viewEmployeeList') or contains(text(),'Employee List')]")
    private WebElement employeeListMenu;

    // Exact XPath from browser for Employee Name input
    @FindBy(xpath = "/html/body/div/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[1]/div/div[2]/div/div/input")
    private WebElement employeeNameSearchInput;

    // Exact XPath from browser for Search button
    @FindBy(xpath = "//*[@id='app']/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[2]/button[2]")
    private WebElement searchButton;

    // Exact XPath from browser for Record count
    @FindBy(xpath = "//*[@id='app']/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/span")
    private WebElement recordsFoundText;

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            wait.until(ExpectedConditions.urlContains("viewEmployeeList"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openEmployeeList() {
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'viewEmployeeList') or contains(text(),'Employee List')]"))).click();
        wait.until(ExpectedConditions.urlContains("viewEmployeeList"));
        // Wait for page to fully load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void searchByName(String name) {
        // Wait longer for page to stabilize
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Use longer wait for the input
        WebDriverWait longerWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement searchInput = longerWait.until(ExpectedConditions.elementToBeClickable(employeeNameSearchInput));
        searchInput.click();
        searchInput.clear();
        searchInput.sendKeys(name);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getRecordsFoundText() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.visibilityOf(recordsFoundText));
        return recordsFoundText.getText();
    }
}

