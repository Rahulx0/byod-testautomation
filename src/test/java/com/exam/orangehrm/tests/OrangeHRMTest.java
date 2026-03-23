package com.exam.orangehrm.tests;

import com.exam.orangehrm.pages.AddEmployeePage;
import com.exam.orangehrm.pages.DashboardPage;
import com.exam.orangehrm.pages.EmployeeListPage;
import com.exam.orangehrm.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Map;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OrangeHRMTest {

    private static final String APP_URL = "https://opensource-demo.orangehrmlive.com";

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private AddEmployeePage addEmployeePage;
    private EmployeeListPage employeeListPage;
    private Map<String, String> fallbackParameters;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().browserVersion("143").setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        
        driver = new ChromeDriver(options);

        fallbackParameters = loadFallbackParameters();

        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        addEmployeePage = new AddEmployeePage(driver);
        employeeListPage = new EmployeeListPage(driver);
    }

    @Test
    @Parameters({"username", "password"})
    public void loginTest(@Optional("") String username, @Optional("") String password) {
        String resolvedUsername = resolveParameter("username", username);
        String resolvedPassword = resolveParameter("password", password);

        Assert.assertFalse(resolvedUsername.isBlank(), "Missing 'username' parameter.");
        Assert.assertFalse(resolvedPassword.isBlank(), "Missing 'password' parameter.");

        loginPage.goTo(APP_URL);
        Assert.assertTrue(loginPage.isAt(), "Login page is not displayed.");
        loginPage.login(resolvedUsername, resolvedPassword);
    }

    @Test(dependsOnMethods = "loginTest")
    public void clickPimTest() {
        Assert.assertTrue(dashboardPage.isAt(), "Dashboard page is not displayed.");
        Assert.assertEquals(dashboardPage.getHeading(), "Dashboard", "Unexpected dashboard heading.");
        dashboardPage.clickPim();
    }

    @Test(dependsOnMethods = "clickPimTest")
    @Parameters({"firstName", "middleName", "lastName"})
    public void addEmployeeTest(
            @Optional("") String firstName,
            @Optional("") String middleName,
            @Optional("") String lastName
    ) {
        String resolvedFirstName = resolveParameter("firstName", firstName);
        String resolvedMiddleName = resolveParameter("middleName", middleName);
        String resolvedLastName = resolveParameter("lastName", lastName);

        Assert.assertFalse(resolvedFirstName.isBlank(), "Missing 'firstName' parameter.");
        Assert.assertFalse(resolvedMiddleName.isBlank(), "Missing 'middleName' parameter.");
        Assert.assertFalse(resolvedLastName.isBlank(), "Missing 'lastName' parameter.");

        addEmployeePage.openAddEmployeeForm();
        Assert.assertTrue(addEmployeePage.isAt(), "Add Employee page is not displayed.");
        addEmployeePage.enterEmployeeDetails(resolvedFirstName, resolvedMiddleName, resolvedLastName);
        addEmployeePage.save();
    }

    @Test(dependsOnMethods = "addEmployeeTest")
    @Parameters({"lastName"})
    public void searchEmployeeTest(@Optional("") String lastName) {
        String resolvedLastName = resolveParameter("lastName", lastName);
        Assert.assertFalse(resolvedLastName.isBlank(), "Missing 'lastName' parameter.");

        employeeListPage.openEmployeeList();
        Assert.assertTrue(employeeListPage.isAt(), "Employee List page is not displayed.");
        employeeListPage.searchByName(resolvedLastName);
        Assert.assertTrue(
                employeeListPage.getRecordsFoundText().contains("Record"),
                "Expected employee record was not found."
        );
    }

    @Test(dependsOnMethods = "searchEmployeeTest")
    public void logoutTest() {
        dashboardPage.logout();
        Assert.assertTrue(loginPage.isAt(), "Failed to return to login page after logout.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private Map<String, String> loadFallbackParameters() {
        Map<String, String> parameters = new HashMap<>();

        try {
            var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            var document = builder.parse("src/test/resources/test-suite.xml");
            NodeList parameterNodes = document.getElementsByTagName("parameter");

            for (int i = 0; i < parameterNodes.getLength(); i++) {
                Element element = (Element) parameterNodes.item(i);
                String name = element.getAttribute("name");
                String value = element.getAttribute("value");

                if (!name.isBlank()) {
                    parameters.put(name, value);
                }
            }
        } catch (Exception ignored) {
            // If parsing fails, missing values are reported by assertions in test methods.
        }

        return parameters;
    }

    private String resolveParameter(String key, String providedValue) {
        if (providedValue != null && !providedValue.isBlank()) {
            return providedValue;
        }

        if (fallbackParameters == null) {
            return "";
        }

        return fallbackParameters.getOrDefault(key, "");
    }
}

