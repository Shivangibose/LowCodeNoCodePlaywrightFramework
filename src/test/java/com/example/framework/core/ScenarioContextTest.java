package com.example.framework.core;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class ScenarioContextTest {
    @Test
    public void pageThrowsClearErrorWhenScenarioHasNotStarted() {
        ScenarioContext context = new ScenarioContext();

        IllegalStateException exception = expectThrows(IllegalStateException.class, context::page);

        assertTrue(exception.getMessage().contains("Page is not initialized"));
    }

    @Test
    public void storesScenarioVariablesAndThrowsClearErrorWhenMissing() {
        ScenarioContext context = new ScenarioContext();

        context.storeVariable("orderId", "ORD-12345");

        assertEquals(context.storedVariable("orderId"), "ORD-12345");

        IllegalArgumentException exception = expectThrows(
                IllegalArgumentException.class,
                () -> context.storedVariable("missing"));
        assertTrue(exception.getMessage().contains("missing"));
    }

    @Test
    public void recordsAndClearsConsoleErrors() {
        ScenarioContext context = new ScenarioContext();

        context.recordConsoleError("error: failed request");
        context.recordConsoleError("pageerror: undefined variable");

        assertEquals(context.consoleErrors().size(), 2);

        context.clearConsoleErrors();

        assertTrue(context.consoleErrors().isEmpty());
    }
}
