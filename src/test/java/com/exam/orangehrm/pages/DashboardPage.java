package com.exam.orangehrm.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends AbstractPage {

    @FindBy(css = "h6.oxd-topbar-header-breadcrumb-module")
    private WebElement pageHeading;

    @FindBy(xpath = "//span[normalize-space()='PIM']")
    private WebElement pimMenuItem;

    @FindBy(css = "span.oxd-userdropdown-tab")
    private WebElement userDropdown;

    @FindBy(xpath = "//*[@id='app']/div[1]/div[1]/header/div[1]/div[3]/ul/li/ul/li[4]/a")
    private WebElement logoutOption;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageHeading));
            return pageHeading.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getHeading() {
        return pageHeading.getText();
    }

    public void clickPim() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenuItem)).click();
    }

    public void logout() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(userDropdown));
        clickWithFallback(dropdown);

        // Wait until dropdown content is rendered before trying Logout options.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.oxd-dropdown-menu")));

        List<By> logoutLocators = List.of(
                By.xpath("//*[@id='app']/div[1]/div[1]/header/div[1]/div[3]/ul/li/ul/li[4]/a"),
                By.xpath("//a[normalize-space()='Logout']"),
                By.xpath("//a[contains(@href,'logout') and contains(normalize-space(.),'Logout')]"),
                By.cssSelector("a[role='menuitem']")
        );

        WebElement logout = null;
        try {
            logout = wait.until(ExpectedConditions.visibilityOf(logoutOption));
        } catch (TimeoutException ignored) {
            logout = findFirstVisible(logoutLocators);
        }

        if (logout == null || !logout.getText().trim().equalsIgnoreCase("Logout")) {
            throw new TimeoutException("Logout option is not visible in the user dropdown.");
        }

        clickWithFallback(logout);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("auth/login"),
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        ));
    }

    private WebElement findFirstVisible(List<By> locators) {
        for (By locator : locators) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (TimeoutException | NoSuchElementException ignored) {
                // Try next locator strategy.
            }
        }
        return null;
    }

    private void clickWithFallback(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
}


