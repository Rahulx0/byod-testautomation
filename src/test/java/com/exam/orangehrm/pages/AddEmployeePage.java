package com.exam.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddEmployeePage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@href, 'addEmployee') or contains(text(), 'Add Employee')]")
    private WebElement addEmployeeMenu;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "middleName")
    private WebElement middleNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement saveButton;

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            wait.until(ExpectedConditions.visibilityOf(firstNameInput));
            return firstNameInput.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openAddEmployeeForm() {
        // Wait for PIM submenu to be visible and click Add Employee
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[contains(@href, 'addEmployee') or contains(text(), 'Add Employee')]")));
        wait.until(ExpectedConditions.elementToBeClickable(addEmployeeMenu)).click();
        // Wait for the Add Employee form to load
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
    }

    public void enterEmployeeDetails(String firstName, String middleName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        middleNameInput.clear();
        middleNameInput.sendKeys(middleName);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    public void save() {
        // Wait for any form loader to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-form-loader")));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        // Wait for the save operation to complete (page transitions to Personal Details)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-form-loader")));
    }
}

