package com.example.framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scenario-scoped state shared by PicoContainer between hooks, actions, and steps.
 */
public class ScenarioContext {
    private BrowserSession browserSession;
    private Page activePage;
    private FrameLocator activeFrame;
    private final Map<String, String> variables = new HashMap<>();
    private final List<String> consoleErrors = new ArrayList<>();

    public void setBrowserSession(BrowserSession browserSession) {
        this.browserSession = browserSession;
        this.activePage = browserSession.page();
        this.activeFrame = null;
    }

    public BrowserSession browserSession() {
        ensureStarted();
        return browserSession;
    }

    public boolean hasBrowserSession() {
        return browserSession != null;
    }

    public Playwright playwright() {
        ensureStarted();
        return browserSession.playwright();
    }

    public Browser browser() {
        ensureStarted();
        return browserSession.browser();
    }

    public BrowserContext browserContext() {
        ensureStarted();
        return browserSession.browserContext();
    }

    public Page page() {
        ensureStarted();
        return activePage;
    }

    public void setActivePage(Page page) {
        if (page == null) {
            throw new IllegalArgumentException("Active page cannot be null.");
        }
        this.activePage = page;
        this.activeFrame = null;
    }

    public boolean hasActiveFrame() {
        return activeFrame != null;
    }

    public FrameLocator activeFrame() {
        ensureStarted();
        return activeFrame;
    }

    public void switchToFrame(FrameLocator frameLocator) {
        ensureStarted();
        if (frameLocator == null) {
            throw new IllegalArgumentException("Frame locator cannot be null.");
        }
        this.activeFrame = frameLocator;
    }

    public void switchToMainContent() {
        this.activeFrame = null;
    }

    public void storeVariable(String name, String value) {
        variables.put(name, value);
    }

    public String storedVariable(String name) {
        String value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("No stored variable found for key '" + name + "'.");
        }
        return value;
    }

    public void recordConsoleError(String message) {
        consoleErrors.add(message);
    }

    public List<String> consoleErrors() {
        return Collections.unmodifiableList(consoleErrors);
    }

    public void clearConsoleErrors() {
        consoleErrors.clear();
    }

    /**
     * Clears the scenario state after hooks close Playwright.
     */
    public void clear() {
        browserSession = null;
        activePage = null;
        activeFrame = null;
        variables.clear();
        consoleErrors.clear();
    }

    /**
     * Closes all browser resources if they were created for the scenario.
     */
    public void closeBrowserSession() {
        try {
            if (browserSession != null) {
                browserSession.close();
            }
        } finally {
            clear();
        }
    }

    private void ensureStarted() {
        if (browserSession == null || activePage == null) {
            throw new IllegalStateException("Page is not initialized. Check that the Cucumber @Before hook started Playwright.");
        }
    }
}
