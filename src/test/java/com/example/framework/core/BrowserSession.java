package com.example.framework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import java.nio.file.Path;

/**
 * Holds the Playwright browser objects created for one Cucumber scenario.
 */
public class BrowserSession implements AutoCloseable {
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext browserContext;
    private final Page page;
    private boolean tracingStarted;

    public BrowserSession(Playwright playwright, Browser browser, BrowserContext browserContext, Page page) {
        this.playwright = playwright;
        this.browser = browser;
        this.browserContext = browserContext;
        this.page = page;
    }

    public Playwright playwright() {
        return playwright;
    }

    public Browser browser() {
        return browser;
    }

    public BrowserContext browserContext() {
        return browserContext;
    }

    public Page page() {
        return page;
    }

    /**
     * Starts Playwright tracing before scenario actions execute.
     */
    public void startTracing(String scenarioName) {
        browserContext.tracing().start(new Tracing.StartOptions()
                .setTitle(scenarioName)
                .setScreenshots(true)
                .setSnapshots(true));
        tracingStarted = true;
    }

    /**
     * Saves the current trace to a zip file for a failed scenario.
     */
    public void saveTrace(Path tracePath) {
        if (!tracingStarted) {
            return;
        }

        browserContext.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        tracingStarted = false;
    }

    /**
     * Stops tracing without saving an artifact for a passing scenario.
     */
    public void discardTrace() {
        if (!tracingStarted) {
            return;
        }

        browserContext.tracing().stop();
        tracingStarted = false;
    }

    /**
     * Closes scenario resources in the reverse order in which they were created.
     * The Playwright instance is owned by PlaywrightManager for the whole test run.
     */
    @Override
    public void close() {
        try {
            discardTrace();
        } catch (RuntimeException ignored) {
            // Continue closing browser resources even if trace cleanup fails.
        }
        closeQuietly(page);
        closeQuietly(browserContext);
        closeQuietly(browser);
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception ignored) {
            // Cleanup is best effort so one close failure does not mask the scenario result.
        }
    }
}
