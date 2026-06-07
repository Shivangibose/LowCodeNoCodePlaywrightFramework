package com.example.framework.core;

import com.example.framework.config.Config;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Creates Playwright browser resources from framework configuration.
 */
public class PlaywrightFactory {
    public BrowserSession createSession() {
        Playwright playwright = PlaywrightManager.playwright();
        Browser browser = null;
        BrowserContext browserContext = null;
        Page page = null;

        try {
            browser = launchBrowser(playwright);
            browserContext = browser.newContext();
            browserContext.setDefaultTimeout(Config.timeoutMs());
            page = browserContext.newPage();
            page.setDefaultTimeout(Config.timeoutMs());

            return new BrowserSession(playwright, browser, browserContext, page);
        } catch (RuntimeException e) {
            new BrowserSession(playwright, browser, browserContext, page).close();
            throw new IllegalStateException("Unable to start Playwright browser session.", e);
        }
    }

    private Browser launchBrowser(Playwright playwright) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Config.headless())
                .setSlowMo(Config.slowMoMs());

        return switch (Config.browser()) {
            case "chromium" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + Config.browser() + "'. Use chromium, firefox, or webkit.");
        };
    }
}
