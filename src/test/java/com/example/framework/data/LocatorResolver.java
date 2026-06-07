package com.example.framework.data;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.FrameLocator;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Resolves logical element names from Gherkin into Playwright-ready locators.
 */
public final class LocatorResolver {
    private static final String DEFAULT_RESOURCE = "locators/locators.json";

    private final LocatorFile locatorFile;

    private LocatorResolver(LocatorFile locatorFile) {
        this.locatorFile = locatorFile;
    }

    public static LocatorResolver defaultInstance() {
        return fromResource(DEFAULT_RESOURCE);
    }

    public static LocatorResolver fromResource(String resourcePath) {
        return new LocatorResolver(LocatorLoader.fromResource(resourcePath));
    }

    public ResolvedLocator resolve(String logicalName) {
        Map<String, LocatorDefinition> locators = locatorFile.getLocators();
        LocatorDefinition definition = locators.get(logicalName);
        if (definition == null) {
            throw new MissingLocatorException(
                    "No locator found for logical element name '" + logicalName + "'. Known locators: "
                            + knownLocatorNames(locators));
        }

        validate(logicalName, definition);
        return new ResolvedLocator(
                logicalName,
                definition.getPage(),
                definition.getType(),
                definition.getSelector(),
                definition.getName(),
                definition.getDescription());
    }

    public String selector(String logicalName) {
        return resolve(logicalName).playwrightSelector();
    }

    public Locator locator(Page page, String logicalName) {
        return resolve(logicalName).locator(page);
    }

    public Locator locator(FrameLocator frameLocator, String logicalName) {
        return resolve(logicalName).locator(frameLocator);
    }

    private void validate(String logicalName, LocatorDefinition definition) {
        if (definition.getType() == null) {
            throw new InvalidLocatorException("Locator '" + logicalName + "' is missing required field 'type'.");
        }
        if (definition.getSelector() == null || definition.getSelector().isBlank()) {
            throw new InvalidLocatorException("Locator '" + logicalName + "' is missing required field 'selector'.");
        }
    }

    private String knownLocatorNames(Map<String, LocatorDefinition> locators) {
        if (locators.isEmpty()) {
            return "(none)";
        }

        StringJoiner joiner = new StringJoiner(", ");
        locators.keySet().forEach(joiner::add);
        return joiner.toString();
    }
}
