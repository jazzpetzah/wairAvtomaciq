package com.wire.picklejar.scan;

import com.wire.picklejar.Config;
import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.gherkin.GherkinParser;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableRow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleJarScanner {

    private static final Logger LOG = LoggerFactory.getLogger(PickleJarScanner.class.getSimpleName());

    public static Collection<Object[]> getTestcases() {
        return getTestcases(Config.EXECUTION_TAG);
    }

    public static Collection<Object[]> getTestcases(String[] filterTags) {
        Collection<Object[]> params = new ArrayList<>();
        for (Feature feature : GherkinParser.getAllFeatures()) {
            params.addAll(mapAndFlatFeature(feature, filterTags));
        }
        LOG.info("Found {} Scenarios", params.size());
        return params;
    }

    /**
     * 0 - Feature name<br>
     * 1 - Scenario name<br>
     * 2 - Example row number (starting with 1, 0 means no examples)<br>
     * 3 - List of scenario steps<br>
     * 4 - Map of tuples of examples with header<br>
     *
     * @param feature
     * @param tagFilter
     * @return List of mapped scenarios per feature
     */
    private static Collection<Object[]> mapAndFlatFeature(Feature feature, String[] tagFilter) {
        Collection<Object[]> scenarios = new ArrayList<>();
        List<ScenarioDefinition> filteredScenarios = GherkinParser.getFilteredScenarios(feature, tagFilter);
        for (ScenarioDefinition scenarioDefinition : filteredScenarios) {

            List<String> steps = new ArrayList<>();
            for (Step step : scenarioDefinition.getSteps()) {
                steps.add(step.getText());
            }

            // When the scenario is an instance of ScenarioOutline we have examples otherwise it's of type Scenario
            try {
                ScenarioOutline scenarioOutline = (ScenarioOutline) scenarioDefinition;

                for (int i = 0; i < scenarioOutline.getExamples().size(); i++) {
                    TableRow tableHeader = scenarioOutline.getExamples().get(i).getTableHeader();
                    List<TableRow> tableRows = scenarioOutline.getExamples().get(i).getTableBody();

                    for (int j = 0; j < tableRows.size(); j++) {
                        TableRow tableRow = tableRows.get(j);
                        Object[] scenarioArray = new Object[5];

                        Map<String, String> exampleRowWithHeader = new HashMap<>();
                        for (int k = 0; k < tableRow.getCells().size(); k++) {
                            String key = tableHeader.getCells().get(k).getValue();
                            String value = tableRow.getCells().get(k).getValue();
                            exampleRowWithHeader.put(key, value);
                        }

                        scenarioArray[0] = feature.getName();
                        // replacing placeholders with examples in scenario name
                        // Pattern quote escapes characters that are dangerous in regexes
                        scenarioArray[1] = PickleExecutor.replaceExampleOccurences(scenarioDefinition.
                                getName(), exampleRowWithHeader);
                        scenarioArray[2] = new Integer(j + 1);
                        scenarioArray[3] = steps;
                        scenarioArray[4] = exampleRowWithHeader;

                        LOG.debug("Adding scenario with example\n"
                                + "FeatureName: " + feature.getName() + "\n"
                                + "ScenarioName: " + scenarioDefinition.getName() + "\n"
                                + "ExampleNumber: " + scenarioArray[2] + "\n"
                                + "Steps: " + Arrays.toString(steps.toArray()) + "\n"
                                + "Examples: " + Arrays.toString(exampleRowWithHeader.keySet().toArray()) + "\n"
                                + "Examples: " + Arrays.toString(exampleRowWithHeader.values().toArray()));

                        scenarios.add(scenarioArray);
                    }

                }
            } catch (Exception e) {
                Object[] scenarioArray = new Object[5];
                scenarioArray[0] = feature.getName();
                scenarioArray[1] = scenarioDefinition.getName();
                // if we don't have examples we default to example row number 0
                scenarioArray[2] = new Integer(0);
                scenarioArray[3] = steps;
                scenarioArray[4] = new HashMap<String, String>();

                LOG.debug("Adding scenario without example\n"
                        + "FeatureName: " + feature.getName() + "\n"
                        + "ScenarioName: " + scenarioDefinition.getName() + "\n"
                        + "ExampleNumber: " + scenarioArray[2] + "\n"
                        + "Steps: " + Arrays.toString(steps.toArray()) + "\n"
                        + "Examples: " + scenarioArray[4]);

                scenarios.add(scenarioArray);
            }
        }
        return scenarios;
    }

}
