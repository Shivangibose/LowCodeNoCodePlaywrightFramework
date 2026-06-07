package com.example.framework.reporting;

import java.nio.file.Path;

/**
 * Builds stable filesystem paths for scenario failure artifacts.
 */
public class ArtifactPathResolver {
    public static final Path DEFAULT_ROOT = Path.of("target", "playwright-artifacts");

    private final Path artifactRoot;

    public ArtifactPathResolver(Path artifactRoot) {
        this.artifactRoot = artifactRoot;
    }

    public static ArtifactPathResolver defaultInstance() {
        return new ArtifactPathResolver(DEFAULT_ROOT);
    }

    public Path screenshotPath(String scenarioName) {
        return artifactRoot.resolve("screenshots").resolve(safeName(scenarioName) + ".png");
    }

    public Path tracePath(String scenarioName) {
        return artifactRoot.resolve("traces").resolve(safeName(scenarioName) + ".zip");
    }

    private String safeName(String value) {
        String safe = value.replaceAll("[^A-Za-z0-9._-]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
        return safe.isBlank() ? "scenario" : safe;
    }
}
