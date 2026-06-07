package com.example.framework.data;

/**
 * Loads test-data metadata from classpath JSON using Jackson.
 */
public final class TestDataLoader {
    private TestDataLoader() {
    }

    public static TestDataFile fromResource(String resourcePath) {
        try {
            return JsonResource.readValue(resourcePath, TestDataFile.class);
        } catch (RuntimeException e) {
            throw new FrameworkDataException("Unable to load test-data JSON resource: " + resourcePath, e);
        }
    }
}
