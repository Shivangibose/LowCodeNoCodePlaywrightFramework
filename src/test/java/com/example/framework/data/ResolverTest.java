package com.example.framework.data;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class ResolverTest {
    @Test
    public void resolvesLocatorSchemaEntriesToPlaywrightSelectorSyntax() {
        LocatorResolver resolver = LocatorResolver.fromResource("fixtures/locators-schema.json");

        ResolvedLocator username = resolver.resolve("usernameInput");
        assertEquals(username.page(), "login");
        assertEquals(username.type(), SelectorType.CSS);
        assertEquals(username.playwrightSelector(), "#user-name");

        ResolvedLocator loginButton = resolver.resolve("loginButtonByRole");
        assertEquals(loginButton.type(), SelectorType.ROLE);
        assertEquals(loginButton.playwrightSelector(), "role=button[name=\"Login\"]");

        ResolvedLocator cartBadge = resolver.resolve("cartBadge");
        assertEquals(cartBadge.type(), SelectorType.TESTID);
        assertEquals(cartBadge.playwrightSelector(), "data-testid=shopping-cart-badge");
    }

    @Test
    public void throwsClearExceptionWhenLocatorIsMissing() {
        LocatorResolver resolver = LocatorResolver.fromResource("fixtures/locators-schema.json");

        MissingLocatorException exception = expectThrows(
                MissingLocatorException.class,
                () -> resolver.resolve("doesNotExist"));

        assertTrue(exception.getMessage().contains("doesNotExist"));
    }

    @Test
    public void resolvesTestDataByDotSeparatedKey() {
        TestDataResolver resolver = TestDataResolver.fromResource("fixtures/test-data-schema.json");

        assertEquals(resolver.value("validLogin.inputs.username"), "standard_user");
        assertEquals(resolver.value("validLogin.inputs.password"), "secret_sauce");
        assertEquals(resolver.value("validLogin.expected.inventoryTitle"), "Products");
        assertEquals(resolver.input("validLogin", "username"), "standard_user");
        assertEquals(resolver.expected("validLogin", "inventoryTitle"), "Products");
    }

    @Test
    public void throwsClearExceptionWhenTestDataKeyIsMissing() {
        TestDataResolver resolver = TestDataResolver.fromResource("fixtures/test-data-schema.json");

        MissingTestDataException exception = expectThrows(
                MissingTestDataException.class,
                () -> resolver.value("validLogin.inputs.missing"));

        assertTrue(exception.getMessage().contains("validLogin.inputs.missing"));
    }
}
