package com.example.framework.reporting;

import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.testng.Assert.assertEquals;

public class ArtifactPathResolverTest {
    @Test
    public void createsSafeFailureArtifactPathsFromScenarioName() {
        ArtifactPathResolver resolver = new ArtifactPathResolver(Path.of("target", "playwright-artifacts"));

        assertEquals(
                resolver.screenshotPath("Login: invalid/user?").toString(),
                Path.of("target", "playwright-artifacts", "screenshots", "Login-invalid-user.png").toString());
        assertEquals(
                resolver.tracePath("Login: invalid/user?").toString(),
                Path.of("target", "playwright-artifacts", "traces", "Login-invalid-user.zip").toString());
    }
}
