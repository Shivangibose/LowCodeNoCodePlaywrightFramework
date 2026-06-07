package com.example.framework.core;

import com.example.framework.reporting.ArtifactPathResolver;
import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Starts and stops one Playwright session per Cucumber scenario.
 */
public class DriverManager {
    private final ScenarioContext scenarioContext;
    private final PlaywrightFactory playwrightFactory;
    private final ArtifactPathResolver artifactPaths;

    public DriverManager(ScenarioContext scenarioContext, PlaywrightFactory playwrightFactory) {
        this(scenarioContext, playwrightFactory, ArtifactPathResolver.defaultInstance());
    }

    public DriverManager(
            ScenarioContext scenarioContext,
            PlaywrightFactory playwrightFactory,
            ArtifactPathResolver artifactPaths) {
        this.scenarioContext = scenarioContext;
        this.playwrightFactory = playwrightFactory;
        this.artifactPaths = artifactPaths;
    }

    public void start() {
        start("Cucumber scenario");
    }

    public void start(String scenarioName) {
        BrowserSession browserSession = playwrightFactory.createSession();
        try {
            browserSession.startTracing(scenarioName);
            scenarioContext.setBrowserSession(browserSession);
        } catch (RuntimeException e) {
            browserSession.close();
            throw e;
        }
    }

    public Page page() {
        return scenarioContext.page();
    }

    public void stop(Scenario scenario) {
        try {
            if (scenario.isFailed() && scenarioContext.hasBrowserSession()) {
                attachFailureArtifacts(scenario);
            } else if (scenarioContext.hasBrowserSession()) {
                scenarioContext.browserSession().discardTrace();
            }
        } finally {
            scenarioContext.closeBrowserSession();
        }
    }

    private void attachFailureArtifacts(Scenario scenario) {
        try {
            Path screenshotPath = artifactPaths.screenshotPath(scenario.getName());
            Files.createDirectories(screenshotPath.getParent());
            byte[] screenshot = scenarioContext.page().screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(screenshotPath));
            scenario.attach(screenshot, "image/png", "failure screenshot");
            scenario.log("Screenshot: " + screenshotPath);

            Path tracePath = artifactPaths.tracePath(scenario.getName());
            Files.createDirectories(tracePath.getParent());
            scenarioContext.browserSession().saveTrace(tracePath);
            scenario.attach(Files.readAllBytes(tracePath), "application/zip", "playwright trace");
            scenario.log("Playwright trace: " + tracePath);
        } catch (IOException | RuntimeException e) {
            scenario.log("Unable to attach Playwright failure artifacts: " + e.getMessage());
        }
    }
}
