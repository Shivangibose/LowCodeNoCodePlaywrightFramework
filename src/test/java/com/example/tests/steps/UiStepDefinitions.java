package com.example.tests.steps;

import com.example.framework.actions.AssertionActions;
import com.example.framework.actions.ContextActions;
import com.example.framework.actions.ElementActions;
import com.example.framework.actions.EvidenceActions;
import com.example.framework.actions.FormActions;
import com.example.framework.actions.NavigationActions;
import com.example.framework.actions.WaitActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Generic low-code step vocabulary. Each step delegates to a helper method.
 */
public class UiStepDefinitions {
    private final NavigationActions navigation;
    private final ElementActions elements;
    private final FormActions forms;
    private final AssertionActions assertions;
    private final WaitActions waits;
    private final ContextActions context;
    private final EvidenceActions evidence;

    public UiStepDefinitions(
            NavigationActions navigation,
            ElementActions elements,
            FormActions forms,
            AssertionActions assertions,
            WaitActions waits,
            ContextActions context,
            EvidenceActions evidence) {
        this.navigation = navigation;
        this.elements = elements;
        this.forms = forms;
        this.assertions = assertions;
        this.waits = waits;
        this.context = context;
        this.evidence = evidence;
    }

    @Given("user opens {string}")
    public void userOpens(String url) {
        navigation.navigate(url);
    }

    @Given("user opens url key {string}")
    public void userOpensUrlKey(String urlKey) {
        navigation.navigateToUrlKey(urlKey);
    }

    @Given("user navigates to {string}")
    public void userNavigatesTo(String urlKey) {
        navigation.navigateToUrlKey(urlKey);
    }

    @Given("user navigates to the {string} page")
    public void userNavigatesToThePage(String pageName) {
        navigation.navigateToPage(pageName);
    }

    @When("user refreshes the page")
    public void userRefreshesThePage() {
        navigation.refresh();
    }

    @When("user navigates back")
    public void userNavigatesBack() {
        navigation.back();
    }

    @When("user navigates forward")
    public void userNavigatesForward() {
        navigation.forward();
    }

    @When("user switches to tab {int}")
    public void userSwitchesToTab(int index) {
        navigation.switchToTab(index);
    }

    @When("user switches to the new tab")
    public void userSwitchesToTheNewTab() {
        navigation.switchToNewestTab();
    }

    @When("user closes the current tab")
    public void userClosesTheCurrentTab() {
        navigation.closeCurrentTab();
    }

    @When("user switches to iframe {string}")
    public void userSwitchesToIframe(String elementName) {
        navigation.switchToIframe(elementName);
    }

    @When("user switches back to main content")
    public void userSwitchesBackToMainContent() {
        navigation.switchToMainContent();
    }

    @When("user clicks on {string}")
    public void userClicksOn(String elementName) {
        elements.click(elementName);
    }

    @When("user double-clicks on {string}")
    public void userDoubleClicksOn(String elementName) {
        elements.doubleClick(elementName);
    }

    @When("user right-clicks on {string}")
    public void userRightClicksOn(String elementName) {
        elements.rightClick(elementName);
    }

    @When("user enters {string} into {string}")
    public void userEntersInto(String value, String elementName) {
        elements.fill(elementName, value);
    }

    @When("user enters test data {string} into {string}")
    public void userEntersTestDataInto(String dataKey, String elementName) {
        elements.fillFromTestData(elementName, dataKey);
    }

    @When("user clears {string}")
    public void userClears(String elementName) {
        elements.clear(elementName);
    }

    @When("user appends {string} to {string}")
    public void userAppendsTo(String value, String elementName) {
        elements.append(elementName, value);
    }

    @When("user hovers over {string}")
    public void userHoversOver(String elementName) {
        elements.hover(elementName);
    }

    @When("user presses {string}")
    public void userPresses(String key) {
        elements.press(key);
    }

    @When("user uploads file {string} to {string}")
    public void userUploadsFileTo(String filePath, String elementName) {
        elements.uploadFile(elementName, filePath);
    }

    @When("user scrolls to {string}")
    public void userScrollsTo(String elementName) {
        elements.scrollTo(elementName);
    }

    @When("user drags {string} and drops on {string}")
    public void userDragsAndDropsOn(String sourceElement, String targetElement) {
        elements.dragAndDrop(sourceElement, targetElement);
    }

    @When("user selects {string} from {string}")
    public void userSelectsFrom(String option, String elementName) {
        forms.selectByLabel(elementName, option);
    }

    @When("user selects {string} from dropdown {string}")
    public void userSelectsFromDropdown(String option, String elementName) {
        forms.selectByLabel(elementName, option);
    }

    @When("user selects option by value {string} from {string}")
    public void userSelectsOptionByValueFrom(String value, String elementName) {
        forms.selectByValue(elementName, value);
    }

    @When("user checks {string}")
    public void userChecks(String elementName) {
        forms.check(elementName);
    }

    @When("user checks the checkbox {string}")
    public void userChecksTheCheckbox(String elementName) {
        forms.check(elementName);
    }

    @When("user unchecks {string}")
    public void userUnchecks(String elementName) {
        forms.uncheck(elementName);
    }

    @When("user unchecks the checkbox {string}")
    public void userUnchecksTheCheckbox(String elementName) {
        forms.uncheck(elementName);
    }

    @When("user selects the radio button {string}")
    public void userSelectsTheRadioButton(String elementName) {
        forms.selectRadio(elementName);
    }

    @When("user toggles {string}")
    public void userToggles(String elementName) {
        forms.toggle(elementName);
    }

    @Then("{string} should be visible")
    public void elementShouldBeVisible(String elementName) {
        assertions.shouldBeVisible(elementName);
    }

    @Then("{string} should not be visible")
    public void elementShouldNotBeVisible(String elementName) {
        assertions.shouldNotBeVisible(elementName);
    }

    @Then("{string} should be enabled")
    public void elementShouldBeEnabled(String elementName) {
        assertions.shouldBeEnabled(elementName);
    }

    @Then("{string} should be disabled")
    public void elementShouldBeDisabled(String elementName) {
        assertions.shouldBeDisabled(elementName);
    }

    @Then("{string} should be checked")
    public void elementShouldBeChecked(String elementName) {
        assertions.shouldBeChecked(elementName);
    }

    @Then("{string} should be editable")
    public void elementShouldBeEditable(String elementName) {
        assertions.shouldBeEditable(elementName);
    }

    @Then("{string} should be focused")
    public void elementShouldBeFocused(String elementName) {
        assertions.shouldBeFocused(elementName);
    }

    @Then("{string} should contain text {string}")
    public void elementShouldContainText(String elementName, String value) {
        assertions.shouldContainText(elementName, value);
    }

    @Then("{string} should have exact text {string}")
    public void elementShouldHaveExactText(String elementName, String value) {
        assertions.shouldHaveExactText(elementName, value);
    }

    @Then("{string} should match text {string}")
    public void elementShouldMatchText(String elementName, String regex) {
        assertions.shouldMatchText(elementName, regex);
    }

    @Then("{string} should have attribute {string} equal to {string}")
    public void elementShouldHaveAttributeEqualTo(String elementName, String attribute, String value) {
        assertions.shouldHaveAttribute(elementName, attribute, value);
    }

    @Then("{string} should have value {string}")
    public void elementShouldHaveValue(String elementName, String value) {
        assertions.shouldHaveValue(elementName, value);
    }

    @Then("{string} text should equal test data {string}")
    public void elementTextShouldEqualTestData(String elementName, String dataKey) {
        assertions.textShouldEqualTestData(elementName, dataKey);
    }

    @Then("page title should be {string}")
    public void pageTitleShouldBe(String title) {
        assertions.pageTitleShouldBe(title);
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String title) {
        assertions.pageTitleShouldBe(title);
    }

    @Then("page title should contain {string}")
    public void pageTitleShouldContain(String titlePart) {
        assertions.pageTitleShouldContain(titlePart);
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String titlePart) {
        assertions.pageTitleShouldContain(titlePart);
    }

    @Then("page url should be {string}")
    public void pageUrlShouldBe(String url) {
        assertions.pageUrlShouldBe(url);
    }

    @Then("the page URL should be {string}")
    public void thePageUrlShouldBe(String url) {
        assertions.pageUrlShouldBe(url);
    }

    @Then("page url should contain {string}")
    public void pageUrlShouldContain(String urlPart) {
        assertions.pageUrlShouldContain(urlPart);
    }

    @Then("the page URL should contain {string}")
    public void thePageUrlShouldContain(String urlPart) {
        assertions.pageUrlShouldContain(urlPart);
    }

    @Then("{string} should have count {int}")
    public void elementShouldHaveCount(String elementName, int expectedCount) {
        assertions.shouldHaveCount(elementName, expectedCount);
    }

    @Then("the list {string} should contain {string}")
    public void listShouldContain(String elementName, String value) {
        assertions.listShouldContain(elementName, value);
    }

    @Then("the table {string} should contain row with {string}")
    public void tableShouldContainRowWith(String tableElementName, String value) {
        assertions.tableShouldContainRowWith(tableElementName, value);
    }

    @Then("the cell at row {int} column {int} in {string} should be {string}")
    public void cellAtRowColumnInShouldBe(int rowNumber, int columnNumber, String tableElementName, String value) {
        assertions.tableCellShouldBe(tableElementName, rowNumber, columnNumber, value);
    }

    @Then("table {string} row {int} column {int} should contain text {string}")
    public void tableCellShouldContainText(String tableElementName, int rowNumber, int columnNumber, String value) {
        assertions.tableCellShouldContainText(tableElementName, rowNumber, columnNumber, value);
    }

    @When("user waits for {string} to be {string}")
    public void userWaitsForElementState(String elementName, String state) {
        waits.waitForState(elementName, state);
    }

    @When("user waits for {string} to be visible")
    public void userWaitsForElementToBeVisible(String elementName) {
        waits.waitForVisible(elementName);
    }

    @When("user waits for {string} to be hidden")
    public void userWaitsForElementToBeHidden(String elementName) {
        waits.waitForHidden(elementName);
    }

    @When("user waits for the page to load")
    public void userWaitsForThePageToLoad() {
        waits.waitForPageLoad();
    }

    @When("user waits for {string} in the URL")
    public void userWaitsForInTheUrl(String urlPart) {
        waits.waitForUrlContains(urlPart);
    }

    @When("user stores text of {string} as {string}")
    public void userStoresTextOfAs(String elementName, String variableName) {
        context.storeText(elementName, variableName);
    }

    @When("user stores attribute {string} of {string} as {string}")
    public void userStoresAttributeOfAs(String attribute, String elementName, String variableName) {
        context.storeAttribute(elementName, attribute, variableName);
    }

    @When("user enters stored variable {string} into {string}")
    public void userEntersStoredVariableInto(String variableName, String elementName) {
        context.fillFromStoredVariable(variableName, elementName);
    }

    @Then("{string} should contain stored variable {string}")
    public void elementShouldContainStoredVariable(String elementName, String variableName) {
        context.shouldContainStoredVariable(elementName, variableName);
    }

    @When("user takes a screenshot named {string}")
    public void userTakesAScreenshotNamed(String name) {
        evidence.takeScreenshot(name);
    }

    @When("user executes JavaScript {string}")
    public void userExecutesJavaScript(String script) {
        evidence.executeJavaScript(script);
    }

    @Then("no console errors should be present")
    public void noConsoleErrorsShouldBePresent() {
        assertions.noConsoleErrorsShouldBePresent();
    }
}
