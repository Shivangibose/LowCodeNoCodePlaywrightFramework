package com.example.framework.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves values used by generic Cucumber steps.
 */
public class StepValueResolver {
    private static final Pattern DATA_REFERENCE = Pattern.compile("^\\$\\{(.+)}$");

    private final TestDataResolver testData;

    public StepValueResolver(TestDataResolver testData) {
        this.testData = testData;
    }

    public static StepValueResolver defaultInstance() {
        return new StepValueResolver(TestDataResolver.defaultInstance());
    }

    /**
     * Supports literal values and ${dot.separated.key} test-data references.
     */
    public String resolve(String value) {
        Matcher matcher = DATA_REFERENCE.matcher(value);
        return matcher.matches() ? resolveKey(matcher.group(1)) : value;
    }

    /**
     * Resolves a raw dot-separated key from test-data.json.
     */
    public String resolveKey(String key) {
        return testData.value(key);
    }
}
