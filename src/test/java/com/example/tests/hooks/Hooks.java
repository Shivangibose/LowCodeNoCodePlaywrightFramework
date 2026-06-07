package com.example.tests.hooks;

import com.example.framework.core.DriverManager;
import com.example.framework.core.PlaywrightManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

/**
 * Cucumber lifecycle hooks. PicoContainer injects the same DriverManager/ScenarioContext
 * into hooks and step definitions for a single scenario.
 */
public class Hooks {
    private final DriverManager driverManager;

    public Hooks(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @BeforeAll
    public static void beforeAll() {
        PlaywrightManager.start();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        driverManager.start(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        driverManager.stop(scenario);
    }

    @AfterAll
    public static void afterAll() {
        PlaywrightManager.stop();
    }
}
