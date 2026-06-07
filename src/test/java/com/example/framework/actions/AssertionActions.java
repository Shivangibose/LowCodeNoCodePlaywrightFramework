package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.microsoft.playwright.Locator;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Web-first assertions built on Playwright's retrying assertion API.
 */
public class AssertionActions extends BaseActions {
    public AssertionActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void shouldBeVisible(String elementName) {
        assertThat(locator(elementName)).isVisible();
    }

    public void shouldNotBeVisible(String elementName) {
        assertThat(locator(elementName)).not().isVisible();
    }

    public void shouldBeEnabled(String elementName) {
        assertThat(locator(elementName)).isEnabled();
    }

    public void shouldBeDisabled(String elementName) {
        assertThat(locator(elementName)).isDisabled();
    }

    public void shouldBeChecked(String elementName) {
        assertThat(locator(elementName)).isChecked();
    }

    public void shouldBeEditable(String elementName) {
        assertThat(locator(elementName)).isEditable();
    }

    public void shouldBeFocused(String elementName) {
        assertThat(locator(elementName)).isFocused();
    }

    public void shouldContainText(String elementName, String expectedText) {
        assertThat(locator(elementName)).containsText(resolveValue(expectedText));
    }

    public void shouldHaveExactText(String elementName, String expectedText) {
        assertThat(locator(elementName)).hasText(resolveValue(expectedText));
    }

    public void shouldMatchText(String elementName, String regex) {
        assertThat(locator(elementName)).hasText(Pattern.compile(regex));
    }

    public void shouldHaveAttribute(String elementName, String attribute, String expectedValue) {
        assertThat(locator(elementName)).hasAttribute(attribute, resolveValue(expectedValue));
    }

    public void shouldHaveValue(String elementName, String expectedValue) {
        assertThat(locator(elementName)).hasValue(resolveValue(expectedValue));
    }

    public void textShouldEqualTestData(String elementName, String dataKey) {
        assertThat(locator(elementName)).hasText(testDataValue(dataKey));
    }

    public void pageTitleShouldBe(String expectedTitle) {
        assertThat(scenarioContext.page()).hasTitle(resolveValue(expectedTitle));
    }

    public void pageTitleShouldContain(String expectedTitlePart) {
        assertThat(scenarioContext.page()).hasTitle(containsPattern(resolveValue(expectedTitlePart)));
    }

    public void pageUrlShouldBe(String expectedUrl) {
        assertThat(scenarioContext.page()).hasURL(resolveValue(expectedUrl));
    }

    public void pageUrlShouldContain(String expectedUrlPart) {
        assertThat(scenarioContext.page()).hasURL(containsPattern(resolveValue(expectedUrlPart)));
    }

    public void shouldHaveCount(String elementName, int expectedCount) {
        assertThat(locator(elementName)).hasCount(expectedCount);
    }

    public void listShouldContain(String elementName, String expectedValue) {
        assertThat(locator(elementName)).containsText(resolveValue(expectedValue));
    }

    public void tableShouldContainRowWith(String tableElementName, String expectedValue) {
        Locator matchingRows = locator(tableElementName)
                .locator("tr")
                .filter(new Locator.FilterOptions().setHasText(resolveValue(expectedValue)));
        assertThat(matchingRows.first()).isVisible();
    }

    public void tableCellShouldBe(String tableElementName, int rowNumber, int columnNumber, String expectedValue) {
        assertThat(tableCell(tableElementName, rowNumber, columnNumber)).hasText(resolveValue(expectedValue));
    }

    public void tableCellShouldContainText(String tableElementName, int rowNumber, int columnNumber, String expectedText) {
        assertThat(tableCell(tableElementName, rowNumber, columnNumber)).containsText(resolveValue(expectedText));
    }

    public void noConsoleErrorsShouldBePresent() {
        if (!scenarioContext.consoleErrors().isEmpty()) {
            throw new AssertionError("Console errors were captured:\n - "
                    + String.join("\n - ", scenarioContext.consoleErrors()));
        }
    }
}
