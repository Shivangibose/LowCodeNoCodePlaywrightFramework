package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Stores values captured from the UI so later steps can reuse them.
 */
public class ContextActions extends BaseActions {
    public ContextActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void storeText(String elementName, String variableName) {
        scenarioContext.storeVariable(variableName, locator(elementName).innerText());
    }

    public void storeAttribute(String elementName, String attributeName, String variableName) {
        String value = locator(elementName).getAttribute(attributeName);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Attribute '" + attributeName + "' was not found on element '" + elementName + "'.");
        }
        scenarioContext.storeVariable(variableName, value);
    }

    public void fillFromStoredVariable(String variableName, String elementName) {
        locator(elementName).fill(scenarioContext.storedVariable(variableName));
    }

    public void shouldContainStoredVariable(String elementName, String variableName) {
        assertThat(locator(elementName)).containsText(scenarioContext.storedVariable(variableName));
    }
}
