package com.wearezeta.picklejar;

import com.wearezeta.picklejar.gherkin.GherkinParser;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableRow;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickleJar {

    private static final String FEATURE_PACKAGE = "com.wearezeta.auto.web";
    private static final String FEATURE_EXTENSION = "feature";

    private GherkinParser parser;
    private List<String> featureFiles = new ArrayList<>();

    public PickleJar() throws IOException {
        Map<String, String> featureContents = getFeatureContents(getFeatureFiles());
        parser = new GherkinParser(featureContents);
    }

    public Collection<Object[]> mapToJUnit(String[] tagFilter) {
        Collection<Object[]> params = new ArrayList<>();
        List<Feature> features = getAllFeatures();
        for (Feature feature : features) {
            params.addAll(mapAndFlatFeature(feature, tagFilter));
        }
        System.out.println("Found "+params.size()+" Scenarios");
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
    private Collection<Object[]> mapAndFlatFeature(Feature feature, String[] tagFilter) {
        Collection<Object[]> scenarios = new ArrayList<>();
        List<ScenarioDefinition> filteredScenarios = getFilteredScenarios(feature, tagFilter);
        if (!filteredScenarios.isEmpty()) {

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
                            scenarioArray[0] = feature.getName();
                            scenarioArray[1] = scenarioDefinition.getName();
                            scenarioArray[2] = new Integer(j + 1);
                            scenarioArray[3] = steps;

                            Map<String, String> exampleRowWithHeader = new HashMap<>();
                            for (int k = 0; k < tableRow.getCells().size(); k++) {
                                String key = tableHeader.getCells().get(k).getValue();
                                String value = tableRow.getCells().get(k).getValue();
                                exampleRowWithHeader.put(key, value);
                            }
                            scenarioArray[4] = exampleRowWithHeader;

                            System.out.println("Adding scenario with example");
                            System.out.println("FeatureName: " + feature.getName());
                            System.out.println("ScenarioName: " + scenarioDefinition.getName());
                            System.out.println("ExampleNumber: " + scenarioArray[2]);
                            System.out.println("Steps: " + Arrays.toString(steps.toArray()));
                            System.out.println("Examples: " + Arrays.toString(exampleRowWithHeader.keySet().toArray()));
                            System.out.println("Examples: " + Arrays.toString(exampleRowWithHeader.values().toArray()));

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

                    System.out.println("Adding scenario without example");
                    System.out.println("FeatureName: " + feature.getName());
                    System.out.println("ScenarioName: " + scenarioDefinition.getName());
                    System.out.println("ExampleNumber: " + scenarioArray[2]);
                    System.out.println("Steps: " + Arrays.toString(steps.toArray()));
                    System.out.println("Examples: " + scenarioArray[4]);

                    scenarios.add(scenarioArray);
                }
            }
        }
        return scenarios;
    }

    private List<ScenarioDefinition> getAllScenarios() {
        return parser.getAllScenarios();
    }

    private List<Feature> getAllFeatures() {
        return parser.getAllFeatures();
    }

    private List<ScenarioDefinition> getFilteredScenarios(String[] tagFilter) {
        return parser.getFilteredScenarios(tagFilter);
    }

    private List<ScenarioDefinition> getFilteredScenarios(Feature feature, String[] tagFilter) {
        return parser.getFilteredScenarios(feature, tagFilter);
    }

    private Map<String, File> getFeatureFiles() throws IOException {
        final Map<String, File> featureFiles = new HashMap<>();
        Collection<File> resource = JavaSeeker.getResource(FEATURE_PACKAGE, FEATURE_EXTENSION);
        for (File file : resource) {
            System.out.println(file.getAbsolutePath());
            featureFiles.put(file.getName(), file);
        }
        return featureFiles;
    }

    private Map<String, String> getFeatureContents(Map<String, File> featureFiles) throws IOException {
        final Map<String, String> featureContents = new HashMap<>();
        for (Map.Entry<String, File> entry : featureFiles.entrySet()) {
            featureContents.put(entry.getKey(), PickleFeatureReader.readFile(entry.getValue()));
        }
        return featureContents;
    }

}
