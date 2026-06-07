package com.example.framework.data;

/**
 * Compatibility facade for callers that only need the Playwright selector string.
 */
public final class LocatorRepository {
    private static final LocatorRepository INSTANCE = new LocatorRepository(LocatorResolver.defaultInstance());

    private final LocatorResolver resolver;

    private LocatorRepository(LocatorResolver resolver) {
        this.resolver = resolver;
    }

    public static LocatorRepository instance() {
        return INSTANCE;
    }

    public String selector(String logicalName) {
        return resolver.selector(logicalName);
    }
}
