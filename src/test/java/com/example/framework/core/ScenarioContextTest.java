package com.example.framework.core;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class ScenarioContextTest {
    @Test
    public void pageThrowsClearErrorWhenScenarioHasNotStarted() {
        ScenarioContext context = new ScenarioContext();

        IllegalStateException exception = expectThrows(IllegalStateException.class, context::page);

        assertTrue(exception.getMessage().contains("Page is not initialized"));
    }
}
