package com.example.framework.data;

/**
 * Base runtime exception for JSON-backed framework metadata failures.
 */
public class FrameworkDataException extends RuntimeException {
    public FrameworkDataException(String message) {
        super(message);
    }

    public FrameworkDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
