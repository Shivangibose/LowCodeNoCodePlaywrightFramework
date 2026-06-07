package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.MouseButton;

import java.nio.file.Path;

/**
 * Generic user interactions against locators resolved by logical element name.
 */
public class ElementActions extends BaseActions {
    public ElementActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void click(String elementName) {
        locator(elementName).click();
    }

    public void doubleClick(String elementName) {
        locator(elementName).dblclick();
    }

    public void rightClick(String elementName) {
        locator(elementName).click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    public void fill(String elementName, String value) {
        locator(elementName).fill(resolveValue(value));
    }

    public void fillFromTestData(String elementName, String dataKey) {
        locator(elementName).fill(testDataValue(dataKey));
    }

    public void clear(String elementName) {
        locator(elementName).clear();
    }

    public void append(String elementName, String value) {
        locator(elementName).pressSequentially(resolveValue(value));
    }

    public void hover(String elementName) {
        locator(elementName).hover();
    }

    public void press(String key) {
        scenarioContext.page().keyboard().press(key);
    }

    public void press(String elementName, String key) {
        locator(elementName).press(key);
    }

    public void uploadFile(String elementName, String filePath) {
        locator(elementName).setInputFiles(Path.of(resolveValue(filePath)));
    }

    public void scrollTo(String elementName) {
        locator(elementName).scrollIntoViewIfNeeded();
    }

    public void dragAndDrop(String sourceElementName, String targetElementName) {
        locator(sourceElementName).dragTo(locator(targetElementName));
    }
}
