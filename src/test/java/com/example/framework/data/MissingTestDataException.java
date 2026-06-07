package com.example.framework.data;

/**
 * Thrown when a Gherkin step references a test-data key that is not in test-data.json.
 */
public class MissingTestDataException extends FrameworkDataException {
    public MissingTestDataException(String message) {
        super(message);
    }
}
