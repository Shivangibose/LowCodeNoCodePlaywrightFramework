package com.example.framework.config;

import com.example.framework.data.MissingTestDataException;
import com.example.framework.data.TestDataResolver;

import java.util.Arrays;
import java.util.Optional;

/**
 * Resolves navigation targets from config first and test-data JSON second.
 */
public class NavigationTargetResolver {
    private final TestDataResolver testData;

    public NavigationTargetResolver(TestDataResolver testData) {
        this.testData = testData;
    }

    public static NavigationTargetResolver defaultInstance() {
        return new NavigationTargetResolver(TestDataResolver.defaultInstance());
    }

    public String urlKey(String key) {
        return Config.optional(key)
                .or(() -> testDataValue(key))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No URL found for key '" + key
                                + "'. Add it to config.properties, an environment variable, or test-data.json."));
    }

    public String page(String pageName) {
        String[] candidates = {
                "pages." + pageName + ".url",
                "pages." + pageName,
                "urls." + pageName,
                "urls.inputs." + pageName
        };

        return firstConfigValue(candidates)
                .or(() -> firstTestDataValue(candidates))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No navigation target found for page '" + pageName
                                + "'. Tried keys: " + String.join(", ", candidates)));
    }

    private Optional<String> firstConfigValue(String[] keys) {
        return Arrays.stream(keys)
                .map(Config::optional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<String> firstTestDataValue(String[] keys) {
        return Arrays.stream(keys)
                .map(this::testDataValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<String> testDataValue(String key) {
        try {
            return Optional.of(testData.value(key));
        } catch (MissingTestDataException e) {
            return Optional.empty();
        }
    }
}
