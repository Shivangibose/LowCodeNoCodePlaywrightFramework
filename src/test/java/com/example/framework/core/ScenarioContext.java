package com.example.framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Scenario-scoped state shared by PicoContainer between hooks, actions, and steps.
 */
public class ScenarioContext {
    private BrowserSession browserSession;

    public void setBrowserSession(BrowserSession browserSession) {
        this.browserSession = browserSession;
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
        return browserSession.page();
    }

    /**
     * Clears the scenario state after hooks close Playwright.
     */
    public void clear() {
        browserSession = null;
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
        if (browserSession == null || browserSession.page() == null) {
            throw new IllegalStateException("Page is not initialized. Check that the Cucumber @Before hook started Playwright.");
        }
    }
}
