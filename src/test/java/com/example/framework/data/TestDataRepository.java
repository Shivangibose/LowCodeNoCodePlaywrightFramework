package com.example.framework.data;

/**
 * Compatibility facade for callers that resolve dot-separated data keys.
 */
public final class TestDataRepository {
    private static final TestDataRepository INSTANCE = new TestDataRepository(TestDataResolver.defaultInstance());

    private final TestDataResolver resolver;

    private TestDataRepository(TestDataResolver resolver) {
        this.resolver = resolver;
    }

    public static TestDataRepository instance() {
        return INSTANCE;
    }

    public String value(String key) {
        return resolver.value(key);
    }
}
