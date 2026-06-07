package com.example.framework.config;

import com.example.framework.data.TestDataResolver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

public class NavigationTargetResolverTest {
    private static final String CONFIG_URL_KEY = "urls.configOnly";
    private static final String CONFIG_PAGE_KEY = "pages.login.url";

    @AfterMethod
    public void clearSystemProperties() {
        System.clearProperty(CONFIG_URL_KEY);
        System.clearProperty(CONFIG_PAGE_KEY);
    }

    @Test
    public void resolvesUrlKeysFromConfigFirstThenTestData() {
        System.setProperty(CONFIG_URL_KEY, "https://config.example");
        NavigationTargetResolver resolver = resolver();

        assertEquals(resolver.urlKey(CONFIG_URL_KEY), "https://config.example");
        assertEquals(resolver.urlKey("urls.inputs.sauceDemo"), "https://www.saucedemo.com");
    }

    @Test
    public void resolvesLogicalPageNamesFromConfigThenKnownDataPaths() {
        System.setProperty(CONFIG_PAGE_KEY, "https://config.example/login");
        NavigationTargetResolver resolver = resolver();

        assertEquals(resolver.page("login"), "https://config.example/login");
        assertEquals(resolver.page("sauceDemo"), "https://www.saucedemo.com");
    }

    @Test
    public void throwsClearExceptionForUnknownNavigationTarget() {
        NavigationTargetResolver resolver = resolver();

        IllegalArgumentException exception = expectThrows(
                IllegalArgumentException.class,
                () -> resolver.urlKey("urls.inputs.missing"));

        assertTrue(exception.getMessage().contains("urls.inputs.missing"));
    }

    private NavigationTargetResolver resolver() {
        return new NavigationTargetResolver(TestDataResolver.fromResource("fixtures/test-data-schema.json"));
    }
}
