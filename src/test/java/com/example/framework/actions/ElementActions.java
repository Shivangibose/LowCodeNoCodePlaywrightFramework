package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.example.framework.data.LocatorResolver;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.Locale;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Generic UI action helper. Playwright provides auto-waiting for click/fill/assertion operations.
 */
public class ElementActions {
    private final LocatorResolver locators = LocatorResolver.defaultInstance();
    private final ScenarioContext scenarioContext;

    public ElementActions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    public void navigate(String url) {
        scenarioContext.page().navigate(url);
    }

    public void click(String elementName) {
        locator(elementName).click();
    }

    public void fill(String elementName, String value) {
        locator(elementName).fill(value);
    }

    public void press(String elementName, String key) {
        locator(elementName).press(key);
    }

    public void selectByLabel(String elementName, String label) {
        locator(elementName).selectOption(new SelectOption().setLabel(label));
    }

    public void check(String elementName) {
        locator(elementName).check();
    }

    public void uncheck(String elementName) {
        locator(elementName).uncheck();
    }

    public void hover(String elementName) {
        locator(elementName).hover();
    }

    public void shouldBeVisible(String elementName) {
        assertThat(locator(elementName)).isVisible();
    }

    public void shouldContainText(String elementName, String expectedText) {
        assertThat(locator(elementName)).containsText(expectedText);
    }

    public void shouldHaveCount(String elementName, int expectedCount) {
        assertThat(locator(elementName)).hasCount(expectedCount);
    }

    public void tableCellShouldContainText(String tableElementName, int rowNumber, int columnNumber, String expectedText) {
        assertThat(tableCell(tableElementName, rowNumber, columnNumber)).containsText(expectedText);
    }

    public void waitForState(String elementName, String state) {
        locator(elementName).waitFor(new Locator.WaitForOptions().setState(toWaitState(state)));
    }

    public void pageTitleShouldBe(String expectedTitle) {
        assertThat(scenarioContext.page()).hasTitle(expectedTitle);
    }

    public void pageTitleShouldContain(String expectedTitlePart) {
        assertThat(scenarioContext.page()).hasTitle(containsPattern(expectedTitlePart));
    }

    public void pageUrlShouldBe(String expectedUrl) {
        assertThat(scenarioContext.page()).hasURL(expectedUrl);
    }

    public void pageUrlShouldContain(String expectedUrlPart) {
        assertThat(scenarioContext.page()).hasURL(containsPattern(expectedUrlPart));
    }

    private Locator locator(String elementName) {
        return locators.locator(scenarioContext.page(), elementName);
    }

    private Locator tableCell(String tableElementName, int rowNumber, int columnNumber) {
        if (rowNumber < 1 || columnNumber < 1) {
            throw new IllegalArgumentException("Table row and column numbers are 1-based and must be greater than zero.");
        }

        return locator(tableElementName)
                .locator("tbody tr")
                .nth(rowNumber - 1)
                .locator("td")
                .nth(columnNumber - 1);
    }

    private WaitForSelectorState toWaitState(String state) {
        return switch (state.toLowerCase(Locale.ROOT)) {
            case "attached" -> WaitForSelectorState.ATTACHED;
            case "detached" -> WaitForSelectorState.DETACHED;
            case "visible" -> WaitForSelectorState.VISIBLE;
            case "hidden" -> WaitForSelectorState.HIDDEN;
            default -> throw new IllegalArgumentException(
                    "Unsupported element state '" + state + "'. Use attached, detached, visible, or hidden.");
        };
    }

    private Pattern containsPattern(String expectedText) {
        return Pattern.compile(".*" + Pattern.quote(expectedText) + ".*");
    }
}
