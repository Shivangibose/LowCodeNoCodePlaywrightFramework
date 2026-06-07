package com.example.framework.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POJO for the full test-data JSON document.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDataFile {
    private String version;
    private Map<String, TestDataSet> data = new LinkedHashMap<>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, TestDataSet> getData() {
        return data;
    }

    public void setData(Map<String, TestDataSet> data) {
        this.data = data == null ? new LinkedHashMap<>() : data;
    }
}
