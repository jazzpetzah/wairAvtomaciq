package com.wire.picklejar.construct;

import java.util.List;
import java.util.Map;

public class TestCase {

    private final String featureName;
    private final String scenarioName;
    private final int exampleNum;
    private final List<String> steps;
    private final Map<String, String> examples;
    private final List<String> tags;
    private final String template;

    /**
     * 0 - Feature name<br>
     * 1 - Scenario name<br>
     * 2 - Example row number (starting with 1, 0 means no examples)<br>
     * 3 - List of scenario steps<br>
     * 4 - Map of tuples of examples with header<br>
     *
     */
    public TestCase(Object[] testcase) {
        this(testcase, null);
    }
    public TestCase(Object[] testcase, String template) {
        this.featureName = (String) testcase[0];
        this.scenarioName = (String) testcase[1];
        this.exampleNum = (Integer) testcase[2];
        this.steps = (List<String>) testcase[3];
        this.examples = (Map<String, String>) testcase[4];
        this.tags = (List<String>) testcase[5];

        if (template != null) {
            this.template = new TestCaseConverter().generateTemplate(this, template);
        }else{
            this.template = null;
        }
    }

    public String toSource() {
        return template;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public int getExampleNum() {
        return exampleNum;
    }

    public List<String> getSteps() {
        return steps;
    }

    public Map<String, String> getExamples() {
        return examples;
    }

    public List<String> getTags() {
        return tags;
    }
    
}
