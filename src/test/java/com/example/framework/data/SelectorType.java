package com.example.framework.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Supported locator strategies in the low-code locator schema.
 */
public enum SelectorType {
    CSS("css"),
    XPATH("xpath"),
    TEXT("text"),
    ROLE("role"),
    TESTID("testid");

    private final String jsonValue;

    SelectorType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @JsonCreator
    public static SelectorType fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.toLowerCase(Locale.ROOT).replace("-", "").replace("_", "");
        for (SelectorType type : values()) {
            if (type.jsonValue.equals(normalized)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Unsupported selector type '" + value + "'. Supported selector types: " + supportedValues());
    }

    @JsonValue
    public String jsonValue() {
        return jsonValue;
    }

    private static String supportedValues() {
        return Arrays.stream(values())
                .map(SelectorType::jsonValue)
                .collect(Collectors.joining(", "));
    }
}
