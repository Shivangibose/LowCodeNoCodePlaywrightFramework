package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.example.framework.data.LocatorResolver;
import com.example.framework.data.StepValueResolver;
import com.example.framework.data.TestDataResolver;
import com.microsoft.playwright.Locator;

import java.util.regex.Pattern;

/**
 * Shared locator, value, and table helpers used by the low-code action classes.
 */
abstract class BaseActions {
    protected final ScenarioContext scenarioContext;
    protected final LocatorResolver locators = LocatorResolver.defaultInstance();
    protected final TestDataResolver testData = TestDataResolver.defaultInstance();
    protected final StepValueResolver values = StepValueResolver.defaultInstance();

    BaseActions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    protected Locator locator(String elementName) {
        if (scenarioContext.hasActiveFrame()) {
            return locators.locator(scenarioContext.activeFrame(), elementName);
        }
        return locators.locator(scenarioContext.page(), elementName);
    }

    protected String resolveValue(String value) {
        return values.resolve(value);
    }

    protected String testDataValue(String key) {
        return testData.value(key);
    }

    protected Locator tableCell(String tableElementName, int rowNumber, int columnNumber) {
        if (rowNumber < 1 || columnNumber < 1) {
            throw new IllegalArgumentException("Table row and column numbers are 1-based and must be greater than zero.");
        }

        return locator(tableElementName)
                .locator("tbody tr")
                .nth(rowNumber - 1)
                .locator("td")
                .nth(columnNumber - 1);
    }

    protected Pattern containsPattern(String expectedText) {
        return Pattern.compile(".*" + Pattern.quote(expectedText) + ".*");
    }
}
