package com.example.tests.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG entry point for Cucumber feature execution.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.tests.steps",
                "com.example.tests.hooks"
        },
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json",
                "html:target/cucumber-reports/cucumber.html"
        },
        monochrome = true
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}
