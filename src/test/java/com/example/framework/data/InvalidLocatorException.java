package com.example.framework.data;

/**
 * Thrown when a locator entry exists but is incomplete or invalid for its selector type.
 */
public class InvalidLocatorException extends FrameworkDataException {
    public InvalidLocatorException(String message) {
        super(message);
    }
}
