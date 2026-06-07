package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.SelectOption;

/**
 * Generic helpers for select, checkbox, radio, and toggle controls.
 */
public class FormActions extends BaseActions {
    public FormActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void selectByLabel(String elementName, String label) {
        locator(elementName).selectOption(new SelectOption().setLabel(resolveValue(label)));
    }

    public void selectByValue(String elementName, String value) {
        locator(elementName).selectOption(new SelectOption().setValue(resolveValue(value)));
    }

    public void check(String elementName) {
        locator(elementName).check();
    }

    public void uncheck(String elementName) {
        locator(elementName).uncheck();
    }

    public void selectRadio(String elementName) {
        locator(elementName).check();
    }

    public void toggle(String elementName) {
        Locator target = locator(elementName);
        target.setChecked(!target.isChecked());
    }
}
