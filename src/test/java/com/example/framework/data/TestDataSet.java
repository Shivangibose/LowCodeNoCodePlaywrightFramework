package com.example.framework.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POJO for one named test-data entry with input values and expected values.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDataSet {
    private Map<String, Object> inputs = new LinkedHashMap<>();
    private Map<String, Object> expected = new LinkedHashMap<>();

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Object> inputs) {
        this.inputs = inputs == null ? new LinkedHashMap<>() : inputs;
    }

    public Map<String, Object> getExpected() {
        return expected;
    }

    public void setExpected(Map<String, Object> expected) {
        this.expected = expected == null ? new LinkedHashMap<>() : expected;
    }
}
