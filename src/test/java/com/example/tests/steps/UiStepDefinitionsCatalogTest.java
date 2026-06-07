package com.example.tests.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertTrue;

public class UiStepDefinitionsCatalogTest {
    @Test
    public void exposesTheLowCodeStepCatalog() {
        Set<String> steps = stepExpressions();

        assertContains(steps, "Given:user navigates to {string}");
        assertContains(steps, "Given:user navigates to the {string} page");
        assertContains(steps, "When:user refreshes the page");
        assertContains(steps, "When:user navigates back");
        assertContains(steps, "When:user navigates forward");
        assertContains(steps, "When:user switches to tab {int}");
        assertContains(steps, "When:user switches to the new tab");
        assertContains(steps, "When:user closes the current tab");
        assertContains(steps, "When:user switches to iframe {string}");
        assertContains(steps, "When:user switches back to main content");

        assertContains(steps, "When:user clicks on {string}");
        assertContains(steps, "When:user double-clicks on {string}");
        assertContains(steps, "When:user right-clicks on {string}");
        assertContains(steps, "When:user enters {string} into {string}");
        assertContains(steps, "When:user enters test data {string} into {string}");
        assertContains(steps, "When:user clears {string}");
        assertContains(steps, "When:user appends {string} to {string}");
        assertContains(steps, "When:user hovers over {string}");
        assertContains(steps, "When:user presses {string}");
        assertContains(steps, "When:user uploads file {string} to {string}");
        assertContains(steps, "When:user scrolls to {string}");
        assertContains(steps, "When:user drags {string} and drops on {string}");

        assertContains(steps, "When:user selects {string} from dropdown {string}");
        assertContains(steps, "When:user selects option by value {string} from {string}");
        assertContains(steps, "When:user checks the checkbox {string}");
        assertContains(steps, "When:user unchecks the checkbox {string}");
        assertContains(steps, "When:user selects the radio button {string}");
        assertContains(steps, "When:user toggles {string}");

        assertContains(steps, "Then:{string} should be visible");
        assertContains(steps, "Then:{string} should not be visible");
        assertContains(steps, "Then:{string} should be enabled");
        assertContains(steps, "Then:{string} should be disabled");
        assertContains(steps, "Then:{string} should be checked");
        assertContains(steps, "Then:{string} should be editable");
        assertContains(steps, "Then:{string} should be focused");

        assertContains(steps, "Then:{string} should contain text {string}");
        assertContains(steps, "Then:{string} should have exact text {string}");
        assertContains(steps, "Then:{string} should match text {string}");
        assertContains(steps, "Then:{string} should have attribute {string} equal to {string}");
        assertContains(steps, "Then:{string} should have value {string}");
        assertContains(steps, "Then:{string} text should equal test data {string}");

        assertContains(steps, "Then:the page title should be {string}");
        assertContains(steps, "Then:the page title should contain {string}");
        assertContains(steps, "Then:the page URL should be {string}");
        assertContains(steps, "Then:the page URL should contain {string}");

        assertContains(steps, "Then:{string} should have count {int}");
        assertContains(steps, "Then:the list {string} should contain {string}");
        assertContains(steps, "Then:the table {string} should contain row with {string}");
        assertContains(steps, "Then:the cell at row {int} column {int} in {string} should be {string}");

        assertContains(steps, "When:user waits for {string} to be visible");
        assertContains(steps, "When:user waits for {string} to be hidden");
        assertContains(steps, "When:user waits for the page to load");
        assertContains(steps, "When:user waits for {string} in the URL");

        assertContains(steps, "When:user stores text of {string} as {string}");
        assertContains(steps, "When:user stores attribute {string} of {string} as {string}");
        assertContains(steps, "When:user enters stored variable {string} into {string}");
        assertContains(steps, "Then:{string} should contain stored variable {string}");

        assertContains(steps, "When:user takes a screenshot named {string}");
        assertContains(steps, "When:user executes JavaScript {string}");
        assertContains(steps, "Then:no console errors should be present");
    }

    private Set<String> stepExpressions() {
        Set<String> steps = new HashSet<>();
        for (Method method : UiStepDefinitions.class.getDeclaredMethods()) {
            Given given = method.getAnnotation(Given.class);
            When when = method.getAnnotation(When.class);
            Then then = method.getAnnotation(Then.class);
            if (given != null) {
                steps.add("Given:" + given.value());
            }
            if (when != null) {
                steps.add("When:" + when.value());
            }
            if (then != null) {
                steps.add("Then:" + then.value());
            }
        }
        return steps;
    }

    private void assertContains(Set<String> steps, String expected) {
        assertTrue(steps.contains(expected), "Missing step expression: " + expected);
    }
}
