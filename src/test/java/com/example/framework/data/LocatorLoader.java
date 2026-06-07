package com.example.framework.data;

/**
 * Loads locator metadata from classpath JSON using Jackson.
 */
public final class LocatorLoader {
    private LocatorLoader() {
    }

    public static LocatorFile fromResource(String resourcePath) {
        try {
            return JsonResource.readValue(resourcePath, LocatorFile.class);
        } catch (RuntimeException e) {
            throw new FrameworkDataException("Unable to load locator JSON resource: " + resourcePath, e);
        }
    }
}
