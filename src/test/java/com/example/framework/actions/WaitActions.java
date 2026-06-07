package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Explicit condition waits. These wait for states, never fixed durations.
 */
public class WaitActions extends BaseActions {
    public WaitActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void waitForState(String elementName, String state) {
        locator(elementName).waitFor(new Locator.WaitForOptions().setState(toWaitState(state)));
    }

    public void waitForVisible(String elementName) {
        waitForState(elementName, "visible");
    }

    public void waitForHidden(String elementName) {
        waitForState(elementName, "hidden");
    }

    public void waitForPageLoad() {
        scenarioContext.page().waitForLoadState(LoadState.LOAD);
    }

    public void waitForUrlContains(String urlPart) {
        scenarioContext.page().waitForURL(containsPattern(resolveValue(urlPart)));
    }

    private WaitForSelectorState toWaitState(String state) {
        return switch (state.toLowerCase()) {
            case "attached" -> WaitForSelectorState.ATTACHED;
            case "detached" -> WaitForSelectorState.DETACHED;
            case "visible" -> WaitForSelectorState.VISIBLE;
            case "hidden" -> WaitForSelectorState.HIDDEN;
            default -> throw new IllegalArgumentException(
                    "Unsupported element state '" + state + "'. Use attached, detached, visible, or hidden.");
        };
    }
}
