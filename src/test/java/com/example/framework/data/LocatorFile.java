package com.example.framework.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POJO for the full locators JSON document.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocatorFile {
    private String version;
    private Map<String, LocatorDefinition> locators = new LinkedHashMap<>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, LocatorDefinition> getLocators() {
        return locators;
    }

    public void setLocators(Map<String, LocatorDefinition> locators) {
        this.locators = locators == null ? new LinkedHashMap<>() : locators;
    }
}
