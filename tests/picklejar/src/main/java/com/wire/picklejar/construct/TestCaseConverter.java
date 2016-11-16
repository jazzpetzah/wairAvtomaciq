package com.wire.picklejar.construct;

import com.wire.picklejar.Config;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

public class TestCaseConverter {
    
    public String generateTemplate(TestCase testcase, String template){
        return template
                .replaceAll("\\$\\(TESTNAME\\)", toClassName(testcase))
                .replaceAll("\\$\\(TESTPACKAGE\\)", Config.GENERATED_TEST_PACKAGE)
                .replace("$(DATA)", toData(testcase));
    }
    
    public String toClassName(TestCase testcase) {
        return (testcase.getFeatureName() + "__" + testcase.getScenarioName() + "_" + testcase.getExampleNum()).replaceAll("[^a-zA-Z0-9]", "_");
    }
    
    public String toData(TestCase testcase) {
        StringBuilder data = new StringBuilder("List<Object[]> testcases = new ArrayList<>();\n");
        data.append(buildExamplesMap(testcase));
        data.append(buildStepList(testcase));
        data.append(buildTagList(testcase));
        data.append(String.format("testcases.add(new Object[]{\"%s\", \"%s\", %d, steps, examples, tags});\n",
                testcase.getFeatureName(), testcase.getScenarioName(), testcase.getExampleNum()));
        data.append("return testcases;");
        return data.toString();
    }

    private String buildExamplesMap(TestCase testcase) {
        StringBuilder mapString = new StringBuilder("Map<String, String> examples = new HashMap<>();\n");
        for (Map.Entry<String, String> entry : testcase.getExamples().entrySet()) {
            mapString.append(String.format("examples.put(\"%s\", \"%s\");\n", entry.getKey(), StringEscapeUtils.escapeJava(
                    entry.getValue())));
        }
        return mapString.toString();
    }

    private String buildStepList(TestCase testcase) {
        StringBuilder listString = new StringBuilder("List<String> steps = new ArrayList<>();\n");
        for (String step : testcase.getSteps()) {
            listString.append(String.format("steps.add(\"%s\");\n", StringEscapeUtils.escapeJava(step)));
        }
        return listString.toString();
    }

    private String buildTagList(TestCase testcase) {
        StringBuilder listString = new StringBuilder("List<String> tags = new ArrayList<>();\n");
        for (String tag : testcase.getTags()) {
            listString.append(String.format("tags.add(\"%s\");\n", StringEscapeUtils.escapeJava(tag)));
        }
        return listString.toString();
    }
}
