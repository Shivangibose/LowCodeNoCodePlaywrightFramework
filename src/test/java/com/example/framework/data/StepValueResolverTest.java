package com.example.framework.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class StepValueResolverTest {
    @Test
    public void returnsLiteralValuesAndDereferencesTestDataExpressions() {
        StepValueResolver resolver = new StepValueResolver(
                TestDataResolver.fromResource("fixtures/test-data-schema.json"));

        assertEquals(resolver.resolve("literal text"), "literal text");
        assertEquals(resolver.resolve("${validLogin.inputs.username}"), "standard_user");
    }

    @Test
    public void resolvesUrlKeysThroughTestData() {
        StepValueResolver resolver = new StepValueResolver(
                TestDataResolver.fromResource("fixtures/test-data-schema.json"));

        assertEquals(resolver.resolveKey("urls.inputs.sauceDemo"), "https://www.saucedemo.com");
    }
}
