package com.example.tests.hooks;

import com.example.framework.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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

    @Before
    public void beforeScenario(Scenario scenario) {
        driverManager.start(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        driverManager.stop(scenario);
    }
}
