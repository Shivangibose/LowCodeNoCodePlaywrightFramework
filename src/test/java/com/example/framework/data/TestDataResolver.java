package com.example.framework.data;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.StringJoiner;

/**
 * Resolves test data by dot-separated keys such as validLogin.inputs.username.
 */
public final class TestDataResolver {
    private static final String DEFAULT_RESOURCE = "testdata/test-data.json";

    private final TestDataFile testDataFile;
    private final JsonNode dataRoot;

    private TestDataResolver(TestDataFile testDataFile) {
        this.testDataFile = testDataFile;
        this.dataRoot = JsonResource.mapper().valueToTree(testDataFile.getData());
    }

    public static TestDataResolver defaultInstance() {
        return fromResource(DEFAULT_RESOURCE);
    }

    public static TestDataResolver fromResource(String resourcePath) {
        return new TestDataResolver(TestDataLoader.fromResource(resourcePath));
    }

    public String value(String key) {
        JsonNode node = node(key);
        return node.isValueNode() ? node.asText() : node.toString();
    }

    public String input(String dataSetName, String inputName) {
        return value(dataSetName + ".inputs." + inputName);
    }

    public String expected(String dataSetName, String expectedName) {
        return value(dataSetName + ".expected." + expectedName);
    }

    public TestDataSet dataSet(String dataSetName) {
        TestDataSet dataSet = testDataFile.getData().get(dataSetName);
        if (dataSet == null) {
            throw new MissingTestDataException(
                    "No test-data set found for key '" + dataSetName + "'. Known data sets: " + knownDataSetNames());
        }
        return dataSet;
    }

    private JsonNode node(String key) {
        JsonNode current = dataRoot;
        for (String part : key.split("\\.")) {
            current = current.path(part);
            if (current.isMissingNode()) {
                throw new MissingTestDataException(
                        "No test data found for key '" + key + "'. Known data sets: " + knownDataSetNames());
            }
        }
        return current;
    }

    private String knownDataSetNames() {
        if (testDataFile.getData().isEmpty()) {
            return "(none)";
        }

        StringJoiner joiner = new StringJoiner(", ");
        testDataFile.getData().keySet().forEach(joiner::add);
        return joiner.toString();
    }
}
