package com.example.tests.steps;

import com.example.framework.actions.ElementActions;
import com.example.framework.data.StepValueResolver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Small generic step set for low-code UI tests.
 */
public class UiStepDefinitions {
    private final ElementActions actions;
    private final StepValueResolver values = StepValueResolver.defaultInstance();

    public UiStepDefinitions(ElementActions actions) {
        this.actions = actions;
    }

    @Given("user opens {string}")
    public void userOpens(String url) {
        actions.navigate(resolveValue(url));
    }

    @Given("user opens url key {string}")
    public void userOpensUrlKey(String urlKey) {
        actions.navigate(values.resolveKey(urlKey));
    }

    @When("user clicks on {string}")
    public void userClicksOn(String elementName) {
        actions.click(elementName);
    }

    @When("user enters {string} into {string}")
    public void userEntersInto(String value, String elementName) {
        actions.fill(elementName, resolveValue(value));
    }

    @When("user selects {string} from {string}")
    public void userSelectsFrom(String value, String elementName) {
        actions.selectByLabel(elementName, resolveValue(value));
    }

    @When("user checks {string}")
    public void userChecks(String elementName) {
        actions.check(elementName);
    }

    @When("user unchecks {string}")
    public void userUnchecks(String elementName) {
        actions.uncheck(elementName);
    }

    @When("user hovers over {string}")
    public void userHoversOver(String elementName) {
        actions.hover(elementName);
    }

    @When("user waits for {string} to be {string}")
    public void userWaitsForElementState(String elementName, String state) {
        actions.waitForState(elementName, state);
    }

    @Then("{string} should be visible")
    public void elementShouldBeVisible(String elementName) {
        actions.shouldBeVisible(elementName);
    }

    @Then("{string} should contain text {string}")
    public void elementShouldContainText(String elementName, String value) {
        actions.shouldContainText(elementName, resolveValue(value));
    }

    @Then("{string} should have count {int}")
    public void elementShouldHaveCount(String elementName, int expectedCount) {
        actions.shouldHaveCount(elementName, expectedCount);
    }

    @Then("table {string} row {int} column {int} should contain text {string}")
    public void tableCellShouldContainText(String tableElementName, int rowNumber, int columnNumber, String value) {
        actions.tableCellShouldContainText(tableElementName, rowNumber, columnNumber, resolveValue(value));
    }

    @Then("page title should be {string}")
    public void pageTitleShouldBe(String title) {
        actions.pageTitleShouldBe(resolveValue(title));
    }

    @Then("page title should contain {string}")
    public void pageTitleShouldContain(String titlePart) {
        actions.pageTitleShouldContain(resolveValue(titlePart));
    }

    @Then("page url should be {string}")
    public void pageUrlShouldBe(String url) {
        actions.pageUrlShouldBe(resolveValue(url));
    }

    @Then("page url should contain {string}")
    public void pageUrlShouldContain(String urlPart) {
        actions.pageUrlShouldContain(resolveValue(urlPart));
    }

    /**
     * Values wrapped as ${path.to.value} are read from test-data.json; other values are literals.
     */
    private String resolveValue(String value) {
        return values.resolve(value);
    }
}
