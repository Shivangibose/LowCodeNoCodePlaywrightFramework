package com.example.framework.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonResource {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonResource() {
    }

    public static JsonNode readTree(String resourcePath) {
        try (InputStream input = JsonResource.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("JSON resource not found on classpath: " + resourcePath);
            }
            return MAPPER.readTree(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read JSON resource: " + resourcePath, e);
        }
    }

    public static <T> T readValue(String resourcePath, Class<T> type) {
        try (InputStream input = JsonResource.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("JSON resource not found on classpath: " + resourcePath);
            }
            return MAPPER.readValue(input, type);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read JSON resource: " + resourcePath, e);
        }
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
