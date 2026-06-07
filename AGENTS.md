# AI Agent Helper For Low-Code Playwright Tests

Use this guide when Codex, Copilot, or another AI coding agent adds or modifies tests in this framework.

The goal is reusability: prefer JSON and Gherkin changes first, reuse existing generic steps, and add Java only when the framework genuinely lacks a reusable capability.

## Framework Contract

This is a hybrid low-code UI testing framework:

1. Gherkin scenarios describe behavior with generic reusable steps.
2. Java step definitions stay thin and delegate to helper classes.
3. JSON files hold locators and test data.

Do not hard-code selectors, credentials, test data, sleeps, or page-specific logic inside step definitions.

## Add A New Test: Preferred Workflow

1. Check whether the needed action/assertion already exists in `UiStepDefinitions`.
2. Add any missing logical element locators to `src/test/resources/locators/locators.json`.
3. Add inputs, URLs, and expected values to `src/test/resources/testdata/test-data.json`.
4. Write or update a `.feature` file under `src/test/resources/features/`.
5. Run a dry run first if only Gherkin changed:

```bash
mvn -Dtest=RunCucumberTest -Dcucumber.execution.dry-run=true test
```

6. Run the real test suite:

```bash
mvn test
```

7. Generate the report when needed:

```bash
mvn -DskipTests verify
```

## Locator Rules

All element references in Gherkin must be logical names:

```gherkin
When user clicks on "loginButton"
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
```

The logical name must resolve through `src/test/resources/locators/locators.json`.

Supported selector types:

| Type | Example |
| --- | --- |
| `css` | `"selector": "#login-button"` |
| `xpath` | `"selector": "//button[@id='login-button']"` |
| `text` | `"selector": "Products"` |
| `role` | `"selector": "button", "name": "Login"` |
| `testid` | `"selector": "submit-login"` |

Selector preference:

1. `testid`
2. `role`
3. stable CSS
4. text
5. XPath only when no better option exists

Do not put raw selectors in feature files.

## Test Data Rules

Use `src/test/resources/testdata/test-data.json` for:

- URLs
- usernames/passwords
- input values
- expected UI text
- expected table/list values

Use `${...}` when a generic step accepts either a literal value or a data key:

```gherkin
When user enters "${validLogin.inputs.username}" into "usernameInput"
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
```

Use raw data keys for steps that explicitly say `test data`:

```gherkin
When user enters test data "validLogin.inputs.username" into "usernameInput"
Then "inventoryTitle" text should equal test data "validLogin.expected.inventoryTitle"
```

## Reusable Step Catalog

Before adding Java, reuse these step patterns.

Navigation:

```gherkin
Given user navigates to "urls.sauceDemo"
Given user navigates to the "login" page
When user refreshes the page
When user navigates back
When user navigates forward
When user switches to tab 1
When user switches to the new tab
When user closes the current tab
When user switches to iframe "paymentIframe"
When user switches back to main content
```

Actions:

```gherkin
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
```

Forms:

```gherkin
When user selects "United States" from dropdown "countryDropdown"
When user selects option by value "US" from "countryDropdown"
When user checks the checkbox "termsCheckbox"
When user unchecks the checkbox "marketingOptInCheckbox"
When user selects the radio button "standardShippingRadio"
When user toggles "notificationsToggle"
```

Assertions:

```gherkin
Then "inventoryTitle" should be visible
Then "loadingSpinner" should not be visible
Then "submitButton" should be enabled
Then "disabledButton" should be disabled
Then "termsCheckbox" should be checked
Then "firstNameInput" should be editable
Then "usernameInput" should be focused
Then "inventoryTitle" should contain text "${validLogin.expected.inventoryTitle}"
Then "inventoryTitle" should have exact text "Products"
Then "orderId" should match text "ORD-[0-9]+"
Then "profileLink" should have attribute "href" equal to "/profile"
Then "usernameInput" should have value "standard_user"
Then "inventoryTitle" text should equal test data "validLogin.expected.inventoryTitle"
Then the page title should be "Swag Labs"
Then the page title should contain "Labs"
Then the page URL should contain "/inventory.html"
Then "inventoryItem" should have count 6
Then the list "inventoryList" should contain "Sauce Labs Backpack"
Then the table "ordersTable" should contain row with "ORD-12345"
Then the cell at row 1 column 3 in "ordersTable" should be "Shipped"
```

Waits:

```gherkin
When user waits for "inventoryTitle" to be visible
When user waits for "loadingSpinner" to be hidden
When user waits for the page to load
When user waits for "/inventory.html" in the URL
```

Do not add fixed-duration sleep steps.

Data chaining:

```gherkin
When user stores text of "orderId" as "createdOrderId"
When user stores attribute "data-order-id" of "orderRow" as "createdOrderId"
When user enters stored variable "createdOrderId" into "orderSearchInput"
Then "orderSummary" should contain stored variable "createdOrderId"
```

Evidence:

```gherkin
When user takes a screenshot named "checkout-summary"
When user executes JavaScript "window.scrollTo(0, 0)"
Then no console errors should be present
```

Use JavaScript sparingly. Prefer normal Playwright user-level actions.

## When Java Is Allowed

Add Java only when the test needs a reusable capability that does not exist yet, such as:

- custom date picker interaction
- complex table model assertion
- file download verification
- API setup or teardown for UI preconditions
- multi-step business helper used by many scenarios
- new artifact type or reporting behavior

If Java is needed:

1. Add logic to a helper class in `src/test/java/com/example/framework/actions/`.
2. Keep the Cucumber step body one or two lines.
3. Reuse `ScenarioContext`, `LocatorResolver`, `TestDataResolver`, and `StepValueResolver`.
4. Add a focused test for the new resolver/context/step behavior.
5. Update `README.md` and this file if the public step catalog changes.

Do not put business logic directly in `UiStepDefinitions`.

## Helper Ownership

Use the existing helper boundaries:

| Helper | Responsibility |
| --- | --- |
| `NavigationActions` | URLs, page names, tabs, iframes, browser navigation |
| `ElementActions` | clicks, typing, upload, hover, scroll, drag/drop |
| `FormActions` | dropdowns, checkboxes, radios, toggles |
| `AssertionActions` | element, page, list, and table assertions |
| `WaitActions` | explicit state/condition waits, never sleeps |
| `ContextActions` | stored variables and UI data chaining |
| `EvidenceActions` | screenshots and limited JavaScript execution |

## Example: Add A New SauceDemo Test

Add locator:

```json
"loginErrorMessage": {
  "page": "login",
  "type": "css",
  "selector": "[data-test='error']",
  "description": "Login error banner"
}
```

Add data:

```json
"lockedOutLogin": {
  "inputs": {
    "username": "locked_out_user",
    "password": "secret_sauce"
  },
  "expected": {
    "errorMessage": "Epic sadface: Sorry, this user has been locked out."
  }
}
```

Add scenario:

```gherkin
Scenario: Locked-out user sees login error
  Given user navigates to "urls.inputs.sauceDemo"
  When user enters "${lockedOutLogin.inputs.username}" into "usernameInput"
  When user enters "${lockedOutLogin.inputs.password}" into "passwordInput"
  When user clicks on "loginButton"
  Then "loginErrorMessage" should contain text "${lockedOutLogin.expected.errorMessage}"
```

No Java should be added for this test.

## Review Checklist For Agents

Before finishing, confirm:

- No raw selectors were added to `.feature` files.
- No fixed sleeps were added.
- No duplicate step definition was added when an existing generic step works.
- New locators use stable selector types where possible.
- Test data is in JSON, not hard-coded in Java.
- Step definitions remain thin.
- Browser lifecycle remains in hooks/driver classes.
- Failure evidence still comes from the existing screenshot and trace hooks.
- `mvn test` passes, or a clear reason is documented if a browser run is not possible.

