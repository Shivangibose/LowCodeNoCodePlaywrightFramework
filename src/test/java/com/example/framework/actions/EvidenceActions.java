package com.example.framework.actions;

import com.example.framework.core.ScenarioContext;
import com.example.framework.reporting.ArtifactPathResolver;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * On-demand evidence and controlled JavaScript escape hatches.
 */
public class EvidenceActions extends BaseActions {
    private final ArtifactPathResolver artifactPaths = ArtifactPathResolver.defaultInstance();

    public EvidenceActions(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public void takeScreenshot(String name) {
        Path screenshotPath = artifactPaths.screenshotPath(resolveValue(name));
        try {
            Files.createDirectories(screenshotPath.getParent());
            scenarioContext.page().screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(screenshotPath));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create screenshot directory for " + screenshotPath, e);
        }
    }

    public void executeJavaScript(String script) {
        scenarioContext.page().evaluate(resolveValue(script));
    }
}
