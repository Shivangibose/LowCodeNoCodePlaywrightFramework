package com.example.framework.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for one logical element entry in locators.json.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocatorDefinition {
    private String page;
    private SelectorType type;
    private String selector;
    private String name;
    private String description;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public SelectorType getType() {
        return type;
    }

    public void setType(SelectorType type) {
        this.type = type;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
