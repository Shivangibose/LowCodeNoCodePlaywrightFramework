# Hybrid Low-Code Playwright BDD Framework

Java UI automation framework combining Cucumber BDD, TestNG, Microsoft Playwright for Java, Jackson, PicoContainer dependency injection, and Masterthought Cucumber HTML reports.

The framework is "hybrid low-code": Java owns the reusable engine, while most day-to-day test authoring happens in Gherkin plus JSON. A tester can add common UI tests by adding locator entries, adding test data, and writing a scenario, without creating new Java step definitions.

## Architecture

The framework has three layers.

| Layer | What lives here | Main files |
| --- | --- | --- |
| 1. Generic Gherkin steps | Human-readable scenarios that reference logical element names and test-data keys. | `src/test/resources/features/*.feature` |
| 2. Step definitions and helpers | Thin Cucumber step definitions delegate to reusable helpers. Helpers resolve JSON, call Playwright actions/assertions, and manage browser lifecycle. | `UiStepDefinitions`, `ElementActions`, `LocatorResolver`, `TestDataResolver`, `DriverManager`, `ScenarioContext` |
| 3. JSON locators and test data | Low-code configuration for selectors, URLs, inputs, and expected values. | `src/test/resources/locators/locators.json`, `src/test/resources/testdata/test-data.json` |

Runtime flow:

```text
Gherkin scenario
  -> UiStepDefinitions
  -> ElementActions
  -> LocatorResolver and StepValueResolver
  -> Playwright Page
  -> Browser action or web-first assertion
```

## Folder Structure

```text
LowCodeNoCodePlaywright/
|-- .github/
|   `-- workflows/
|       `-- ui-tests.yml
|-- pom.xml
|-- README.md
|-- src/
|   `-- test/
|       |-- java/
|       |   `-- com/example/
|       |       |-- framework/
|       |       |   |-- actions/
|       |       |   |   `-- ElementActions.java
|       |       |   |-- config/
|       |       |   |   `-- Config.java
|       |       |   |-- core/
|       |       |   |   |-- BrowserSession.java
|       |       |   |   |-- DriverManager.java
|       |       |   |   |-- PlaywrightFactory.java
|       |       |   |   `-- ScenarioContext.java
|       |       |   |-- data/
|       |       |   |   |-- LocatorDefinition.java
|       |       |   |   |-- LocatorResolver.java
|       |       |   |   |-- ResolvedLocator.java
|       |       |   |   |-- SelectorType.java
|       |       |   |   |-- StepValueResolver.java
|       |       |   |   `-- TestDataResolver.java
|       |       |   `-- reporting/
|       |       |       `-- ArtifactPathResolver.java
|       |       `-- tests/
|       |           |-- hooks/
|       |           |   `-- Hooks.java
|       |           |-- runners/
|       |           |   `-- RunCucumberTest.java
|       |           `-- steps/
|       |               `-- UiStepDefinitions.java
|       `-- resources/
|           |-- config/
|           |   `-- config.properties
|           |-- features/
|           |   `-- playwright-docs.feature
|           |-- fixtures/
|           |   |-- locators-schema.json
|           |   `-- test-data-schema.json
|           |-- locators/
|           |   `-- locators.json
|           `-- testdata/
|               `-- test-data.json
`-- target/
    |-- cucumber-reports/
    |-- cucumber-html-reports/
    `-- playwright-artifacts/
```

`target/` is generated after test execution. It contains Cucumber JSON/HTML output, Masterthought HTML output, screenshots, and Playwright trace zips.

## Locator JSON

Locators live in `src/test/resources/locators/locators.json`. Each logical element name maps to a page, selector type, selector value, and optional metadata.

```json
{
  "version": "1.0",
  "locators": {
    "usernameInput": {
      "page": "login",
      "type": "css",
      "selector": "#user-name",
      "description": "Username input on the SauceDemo login page"
    },
    "loginButtonByRole": {
      "page": "login",
      "type": "role",
      "selector": "button",
      "name": "Login",
      "description": "Login button found by accessible role and name"
    },
    "inventoryTitleByText": {
      "page": "inventory",
      "type": "text",
      "selector": "Products",
      "description": "Inventory heading text"
    }
  }
}
```

Supported selector types:

| Type | JSON example | Playwright API used |
| --- | --- | --- |
| `css` | `"selector": "#login-button"` | `page.locator("#login-button")` |
| `xpath` | `"selector": "//button[@id='login-button']"` | `page.locator("xpath=//button[@id='login-button']")` |
| `text` | `"selector": "Products"` | `page.locator("text=Products")` |
| `role` | `"selector": "button", "name": "Login"` | `page.getByRole(AriaRole.BUTTON, name)` |
| `testid` | `"selector": "submit-login"` | `page.getByTestId("submit-login")` |

Prefer `role` and `testid` when the application supports them. They usually describe user-facing intent better than brittle CSS or XPath. Use XPath only when there is no better stable locator.

## Test Data JSON

Test data lives in `src/test/resources/testdata/test-data.json`. Values are read with dot-path keys.

```json
{
  "version": "1.0",
  "data": {
    "validLogin": {
      "inputs": {
        "username": "standard_user",
        "password": "secret_sauce"
      },
      "expected": {
        "inventoryTitle": "Products"
      }
    },
    "urls": {
      "inputs": {
        "sauceDemo": "https://www.saucedemo.com"
      }
    }
  }
}
```

In Gherkin, wrap data paths in `${...}` when a step accepts either a literal or a data reference:

```gherkin
When user enters "${validLogin.inputs.username}" into "usernameInput"
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
```

The URL-specific step resolves a key directly:

```gherkin
Given user opens url key "urls.inputs.sauceDemo"
```

## Generic Step Syntax

```gherkin
Given user opens "https://www.saucedemo.com"
Given user opens url key "urls.inputs.sauceDemo"

When user clicks on "loginButton"
When user enters "${validLogin.inputs.username}" into "usernameInput"
When user selects "${checkout.inputs.country}" from "countryDropdown"
When user checks "termsCheckbox"
When user unchecks "marketingOptInCheckbox"
When user hovers over "profileMenu"
When user waits for "inventoryTitle" to be "visible"

Then "inventoryTitle" should be visible
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
Then "inventoryItem" should have count 6
Then table "ordersTable" row 1 column 3 should contain text "${orders.expected.firstStatus}"
Then page title should be "Swag Labs"
Then page title should contain "Labs"
Then page url should be "https://www.saucedemo.com/inventory.html"
Then page url should contain "/inventory.html"
```

Supported wait states are `attached`, `detached`, `visible`, and `hidden`.

## Worked Example: Add A New Test Without Java

Goal: add a SauceDemo locked-out-user test.

1. Add a locator to `src/test/resources/locators/locators.json`.

```json
{
  "locators": {
    "loginErrorMessage": {
      "page": "login",
      "type": "css",
      "selector": "[data-test='error']",
      "description": "Login error banner"
    }
  }
}
```

Add only the new `loginErrorMessage` entry inside the existing `locators` object; do not replace the whole file.

2. Add data to `src/test/resources/testdata/test-data.json`.

```json
{
  "data": {
    "lockedOutLogin": {
      "inputs": {
        "username": "locked_out_user",
        "password": "secret_sauce"
      },
      "expected": {
        "errorMessage": "Epic sadface: Sorry, this user has been locked out."
      }
    }
  }
}
```

Add only the new `lockedOutLogin` block inside the existing `data` object.

3. Add a scenario to a feature file under `src/test/resources/features/`.

```gherkin
Feature: SauceDemo login

  Scenario: Locked-out user sees login error
    Given user opens url key "urls.inputs.sauceDemo"
    When user enters "${lockedOutLogin.inputs.username}" into "usernameInput"
    When user enters "${lockedOutLogin.inputs.password}" into "passwordInput"
    When user clicks on "loginButton"
    Then "loginErrorMessage" should contain text "${lockedOutLogin.expected.errorMessage}"
```

No Java changes are needed because the scenario uses existing generic steps.

## How To Run

Prerequisites:

- JDK 17 or newer
- Maven 3.9 or newer
- Network access the first time Playwright browser binaries are installed

Install Playwright browsers:

```bash
mvn -Pinstall-playwright-browsers generate-test-resources
```

Run tests:

```bash
mvn test
```

Run tests and generate the Masterthought report:

```bash
mvn verify
```

Run with browser/config overrides:

```bash
mvn test -Dbrowser=firefox -Dheadless=false
mvn test -Dbrowser=webkit -Dtimeout.ms=45000
mvn test -Dbase.url=https://www.saucedemo.com
```

The config lookup order is:

1. JVM system property, for example `-Dbrowser=chromium`
2. Environment variable, for example `BROWSER=chromium`
3. `src/test/resources/config/config.properties`
4. Code default

Supported browser values are `chromium`, `firefox`, and `webkit`.

## Reports And Failure Artifacts

`mvn test` writes Cucumber output:

```text
target/cucumber-reports/cucumber.json
target/cucumber-reports/cucumber.html
```

`mvn verify` generates the Masterthought HTML report:

```text
target/cucumber-html-reports/
```

On scenario failure, the `@After` hook automatically:

- captures a full-page screenshot
- stops and saves a Playwright trace zip
- attaches both artifacts to the Cucumber report
- stores files under `target/playwright-artifacts/`

GitHub Actions runs the same Maven workflow and uploads the HTML report plus Playwright traces/screenshots as build artifacts.

## Dependency Summary

| Dependency or plugin | Purpose |
| --- | --- |
| `com.microsoft.playwright:playwright` | Provides browser automation APIs, locators, assertions, tracing, screenshots, and the Playwright CLI entry point. |
| `io.cucumber:cucumber-java` | Provides Java annotations and runtime glue for Gherkin step definitions. |
| `io.cucumber:cucumber-testng` | Runs Cucumber scenarios through TestNG with `AbstractTestNGCucumberTests`. |
| `io.cucumber:cucumber-picocontainer` | Creates scenario-scoped objects and constructor-injects shared dependencies such as `ScenarioContext`. |
| `org.testng:testng` | Supplies the test runner used by Maven Surefire. |
| `com.fasterxml.jackson.core:jackson-databind` | Maps locator and test-data JSON into Java POJOs and JSON trees. |
| `net.masterthought:maven-cucumber-reporting` | Builds richer HTML reports from Cucumber JSON output during `mvn verify`. |
| `org.codehaus.mojo:exec-maven-plugin` | Runs `com.microsoft.playwright.CLI install --with-deps` from Maven. |
| `org.apache.maven.plugins:maven-surefire-plugin` | Discovers and executes the TestNG Cucumber runner during `mvn test`. |

## Low-Code Philosophy

The framework does not try to remove engineering from UI testing. It moves repeated mechanics into reusable Java helpers so testers can express routine flows through:

- a logical element name, such as `loginButton`
- a test-data key, such as `${validLogin.inputs.username}`
- a generic action, such as `When user clicks on "loginButton"`

This gives non-Java authors a smaller surface area while keeping strong code behind the scenes. The Java layer is still available when the product needs a new reusable action, a complex widget interaction, API setup, database setup, or custom assertion.
