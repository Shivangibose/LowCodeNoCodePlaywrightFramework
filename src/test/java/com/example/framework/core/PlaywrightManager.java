package com.example.framework.core;

import com.microsoft.playwright.Playwright;

/**
 * Owns the single Playwright instance for a Cucumber test run.
 */
public final class PlaywrightManager {
    private static Playwright playwright;

    private PlaywrightManager() {
    }

    public static synchronized void start() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
    }

    public static synchronized Playwright playwright() {
        start();
        return playwright;
    }

    public static synchronized void stop() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
