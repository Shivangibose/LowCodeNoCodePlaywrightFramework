package com.example.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public final class Config {
    private static final Properties PROPERTIES = loadProperties();

    private Config() {
    }

    public static String baseUrl() {
        return get("base.url", "https://playwright.dev/java/");
    }

    public static String browser() {
        return get("browser", "chromium").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "true"));
    }

    public static double slowMoMs() {
        return Double.parseDouble(get("slow.mo.ms", "0"));
    }

    public static double timeoutMs() {
        return Double.parseDouble(get("timeout.ms", "30000"));
    }

    public static String value(String key) {
        return optional(key).orElseThrow(() ->
                new IllegalArgumentException("No configuration value found for key '" + key + "'."));
    }

    public static Optional<String> optional(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return Optional.of(systemValue);
        }

        String envValue = System.getenv(toEnvKey(key));
        if (envValue != null && !envValue.isBlank()) {
            return Optional.of(envValue);
        }

        String propertyValue = PROPERTIES.getProperty(key);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return Optional.of(propertyValue);
        }

        return Optional.empty();
    }

    private static String get(String key, String defaultValue) {
        return optional(key).orElse(defaultValue);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input != null) {
                properties.load(input);
            }
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config/config.properties", e);
        }
    }

    private static String toEnvKey(String key) {
        return key.toUpperCase().replace('.', '_').replace('-', '_');
    }
}
