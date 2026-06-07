package com.example.framework.data;

/**
 * Thrown when a Gherkin step references a logical element name that is not in locators.json.
 */
public class MissingLocatorException extends FrameworkDataException {
    public MissingLocatorException(String message) {
        super(message);
    }
}
