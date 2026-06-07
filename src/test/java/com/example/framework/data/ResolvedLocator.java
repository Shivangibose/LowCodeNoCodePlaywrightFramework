package com.example.framework.data;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Locale;

/**
 * Runtime-ready locator resolved from a logical element name.
 */
public record ResolvedLocator(
        String logicalName,
        String page,
        SelectorType type,
        String selector,
        String name,
        String description) {

    /**
     * Returns a string representation that matches Playwright selector syntax where possible.
     */
    public String playwrightSelector() {
        return switch (type) {
            case CSS -> selector;
            case XPATH -> withPrefix("xpath=", selector);
            case TEXT -> withPrefix("text=", selector);
            case ROLE -> roleSelector();
            case TESTID -> "data-testid=" + selector;
        };
    }

    /**
     * Builds the actual Playwright Locator using the strongest API for each selector type.
     */
    public Locator locator(Page page) {
        return switch (type) {
            case CSS -> page.locator(selector);
            case XPATH, TEXT -> page.locator(playwrightSelector());
            case TESTID -> page.getByTestId(selector);
            case ROLE -> roleLocator(page);
        };
    }

    private Locator roleLocator(Page page) {
        AriaRole ariaRole = AriaRole.valueOf(selector.toUpperCase(Locale.ROOT).replace("-", "_"));
        if (name == null || name.isBlank()) {
            return page.getByRole(ariaRole);
        }
        return page.getByRole(ariaRole, new Page.GetByRoleOptions().setName(name));
    }

    private String roleSelector() {
        if (name == null || name.isBlank()) {
            return "role=" + selector;
        }
        return "role=" + selector + "[name=\"" + escape(name) + "\"]";
    }

    private static String withPrefix(String prefix, String value) {
        return value.startsWith(prefix) ? value : prefix + value;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
