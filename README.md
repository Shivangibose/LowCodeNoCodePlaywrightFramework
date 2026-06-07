# Hybrid Low-Code Playwright BDD Framework

Java UI automation framework combining Cucumber BDD, TestNG, Microsoft Playwright for Java, Jackson, PicoContainer dependency injection, and Masterthought Cucumber HTML reports.

The framework is "hybrid low-code": Java owns the reusable engine, while most day-to-day test authoring happens in Gherkin plus JSON. A tester can add common UI tests by adding locator entries, adding test data, and writing a scenario, without creating new Java step definitions.

## Architecture

The framework has three layers.

| Layer | What lives here | Main files |
| --- | --- | --- |
| 1. Generic Gherkin steps | Human-readable scenarios that reference logical element names and test-data keys. | `src/test/resources/features/*.feature` |
| 2. Step definitions and helpers | Thin Cucumber step definitions delegate to reusable helpers. Helpers resolve JSON, call Playwright actions/assertions, and manage browser lifecycle. | `UiStepDefinitions`, `NavigationActions`, `ElementActions`, `FormActions`, `AssertionActions`, `WaitActions`, `ContextActions`, `EvidenceActions`, `DriverManager`, `ScenarioContext` |
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
|       |       |   |   |-- AssertionActions.java
|       |       |   |   |-- ContextActions.java
|       |       |   |   |-- ElementActions.java
|       |       |   |   |-- EvidenceActions.java
|       |       |   |   |-- FormActions.java
|       |       |   |   |-- NavigationActions.java
|       |       |   |   `-- WaitActions.java
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

## Generic Step Catalog

```gherkin
# Navigation
Given user navigates to "urls.sauceDemo"
Given user navigates to "urls.inputs.sauceDemo"
Given user navigates to the "login" page
When user refreshes the page
When user navigates back
When user navigates forward
When user switches to tab 1
When user switches to the new tab
When user closes the current tab
When user switches to iframe "paymentIframe"
When user switches back to main content

# Interaction / actions
When user clicks on "loginButton"
When user double-clicks on "inventoryItem"
When user right-clicks on "inventoryItem"
When user enters "${validLogin.inputs.username}" into "usernameInput"
When user enters test data "validLogin.inputs.username" into "usernameInput"
When user clears "usernameInput"
When user appends " Jr" to "firstNameInput"
When user hovers over "profileMenu"
When user presses "Enter"
When user uploads file "src/test/resources/files/sample.pdf" to "uploadInput"
When user scrolls to "checkoutButton"
When user drags "sourceCard" and drops on "targetColumn"

# Form controls
When user selects "United States" from dropdown "countryDropdown"
When user selects option by value "US" from "countryDropdown"
When user checks the checkbox "termsCheckbox"
When user unchecks the checkbox "marketingOptInCheckbox"
When user selects the radio button "standardShippingRadio"
When user toggles "notificationsToggle"

# Visibility and state assertions
Then "inventoryTitle" should be visible
Then "loadingSpinner" should not be visible
Then "submitButton" should be enabled
Then "disabledButton" should be disabled
Then "termsCheckbox" should be checked
Then "firstNameInput" should be editable
Then "usernameInput" should be focused

# Text and content assertions
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
Then "inventoryTitle" should have exact text "Products"
Then "orderId" should match text "ORD-[0-9]+"
Then "profileLink" should have attribute "href" equal to "/profile"
Then "usernameInput" should have value "standard_user"
Then "inventoryTitle" text should equal test data "validLogin.expected.inventoryTitle"

# Page-level assertions
Then the page title should be "Swag Labs"
Then the page title should contain "Labs"
Then the page URL should be "https://www.saucedemo.com/inventory.html"
Then the page URL should contain "/inventory.html"

# Collections and tables
Then "inventoryItem" should have count 6
Then the list "inventoryList" should contain "Sauce Labs Backpack"
Then the table "ordersTable" should contain row with "ORD-12345"
Then the cell at row 1 column 3 in "ordersTable" should be "Shipped"

# Waits for states or conditions, not fixed durations
When user waits for "inventoryTitle" to be "visible"
When user waits for "inventoryTitle" to be visible
When user waits for "loadingSpinner" to be hidden
When user waits for the page to load
When user waits for "/inventory.html" in the URL

# Data chaining / context
When user stores text of "orderId" as "createdOrderId"
When user stores attribute "data-order-id" of "orderRow" as "createdOrderId"
When user enters stored variable "createdOrderId" into "orderSearchInput"
Then "orderSummary" should contain stored variable "createdOrderId"

# Utility / evidence
When user takes a screenshot named "checkout-summary"
When user executes JavaScript "window.scrollTo(0, 0)"
Then no console errors should be present
```

Supported wait states are `attached`, `detached`, `visible`, and `hidden`.

Older aliases such as `Given user opens "https://www.saucedemo.com"`, `Given user opens url key "urls.inputs.sauceDemo"`, `When user selects "United States" from "countryDropdown"`, and `Then page url should contain "/inventory.html"` are still supported for backwards compatibility.

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

## Interview Questions And Strong Answers

### 1. Why store locators in JSON instead of hard-coding them in step definitions?

Storing locators in JSON separates test intent from selector mechanics. A scenario can say `When user clicks on "loginButton"` while the actual selector is maintained in one place. If the login button changes from `#login-button` to a `data-testid`, we update JSON, not every feature file or Java step. It also makes reviews easier because locator changes are visible as data changes.

### 2. What is the risk of JSON locators, and how do you control it?

The risk is that JSON can become an untyped dumping ground with duplicate names, stale selectors, or unclear ownership. This framework controls that with a defined schema, strong Java POJOs, clear exceptions for missing locators, logical naming conventions, and a preference order for selectors: `testid` or `role` first, CSS next, XPath last.

### 3. How does Playwright auto-waiting reduce flakiness?

Playwright actions like `click()` and `fill()` wait for the element to be actionable before executing. Web-first assertions like `assertThat(locator).isVisible()` retry until the condition is met or the timeout expires. That removes many manual sleeps and timing races that often make Selenium tests flaky.

### 4. Does auto-waiting mean we never need explicit waits?

No. Auto-waiting handles actionability and assertion retries, but tests may still need to wait for a specific business state, such as a success banner becoming visible or a row disappearing after deletion. The framework supports explicit element states with `When user waits for "<element>" to be "visible"`, but it still uses Playwright's wait APIs rather than `Thread.sleep`.

### 5. How is the Playwright `Page` shared between Cucumber steps?

Cucumber PicoContainer creates a new object graph for each scenario. `Hooks`, `UiStepDefinitions`, and `ElementActions` receive shared objects through constructor injection. The `@Before` hook creates a browser session and stores the `Page` in `ScenarioContext`; every step in that scenario uses the same injected context. The `@After` hook captures artifacts if needed and closes the session.

### 6. Why is PicoContainer better than a static driver?

A static driver is easy at first, but it becomes fragile with parallel execution, retries, and scenario isolation. PicoContainer gives each scenario its own context without global mutable state. That makes lifecycle ownership clearer and reduces cross-test contamination.

### 7. What are the genuine limits of low-code UI automation?

Low-code works well for repeated UI flows: navigate, click, fill, select, check, hover, and assert common states. It is weaker for complex widgets, conditional business logic, drag-and-drop workflows, visual validation, file handling, multi-tab flows, and dynamic test setup. UI automation is also more stateful and brittle than API automation, so API tests can usually be parameterized more cleanly and pushed closer to no-code than UI tests.

### 8. When should a tester ask for a new Java helper instead of forcing everything into Gherkin?

Ask for Java when the scenario needs a new reusable behavior, not just a new selector or value. Examples include uploading files, handling a custom calendar, validating a complex table model, creating backend data through an API, or performing a repeated business workflow that would make feature files too verbose.

### 9. How does this compare to a typical Selenium+Cucumber framework at work?

The BDD shape is familiar: feature files, step definitions, hooks, and a runner. The main difference is the browser engine and waiting model. Playwright gives stronger locator APIs, browser-context isolation, built-in tracing, screenshots, videos if enabled, and web-first assertions. Selenium has a broader legacy ecosystem and may already be integrated with an existing Grid, but it often needs more custom wait code and driver-management plumbing.

### 10. Why not put all locators and data directly in feature files?

Feature files should describe behavior, not implementation details. If selectors and large data values live in Gherkin, scenarios become noisy and harder for business readers to review. Keeping selectors in locator JSON and values in test-data JSON makes scenarios shorter, encourages reuse, and lets selector/data changes happen without rewriting the behavioral test.
