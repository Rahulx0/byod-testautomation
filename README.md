# OrangeHRM Selenium Automation (BYOD1)

This project contains a Selenium WebDriver + TestNG automation flow for OrangeHRM.

## Implemented Scope

- Page Object Model classes under `src/test/java/com/exam/orangehrm/pages`
- End-to-end TestNG class under `src/test/java/com/exam/orangehrm/tests/OrangeHRMTest.java`
- Parameterized suite config in `src/test/resources/test-suite.xml`
- Maven Surefire configured to run the TestNG suite file

## Run Tests

```bash
mvn test
```

## Screenshot Checklist (for submission)

1. `test-suite.xml` open in IDE showing all 5 parameters
2. IntelliJ test runner (or terminal) showing all 6 methods pass
3. One additional passing view in task order (for PDF evidence)

