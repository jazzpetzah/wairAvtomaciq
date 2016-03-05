package com.wearezeta.picklejar.gherkin;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GherkinParser {

    private final Map<String, String> featureContents;
    private final Parser<Feature> parser = new Parser<>(new AstBuilder());

    public GherkinParser(Map<String, String> featureContents) {
        this.featureContents = featureContents;
    }

    public List<Feature> getAllFeatures() {
        Parser<Feature> parser = new Parser<>(new AstBuilder());
        List<Feature> features = new ArrayList<>();
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            features.add(feature);
        }
        return features;
    }

    public List<Feature> getFilteredFeatures(String[] tagFilter) {
        List<Feature> features = new ArrayList<>();
        List<String> tagsToSelectList = Arrays.asList(tagFilter);
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            List<ScenarioDefinition> scenarioDefinitions = feature.getScenarioDefinitions();
            List<ScenarioDefinition> selectedScenarios = scenarioDefinitions.stream().filter(
                    (scenarioDefinition) -> ((ScenarioOutline) scenarioDefinition).getTags().stream().anyMatch(
                            (t) -> tagsToSelectList.contains(t.getName()))
            ).collect(Collectors.toList());
            scenarioDefinitions.retainAll(selectedScenarios);
            if (!scenarioDefinitions.isEmpty()) {
                features.add(feature);
            }
        }
        return features;
    }

    public List<ScenarioDefinition> getAllScenarios() {
        ArrayList<ScenarioDefinition> filteredScenraios = new ArrayList<>();
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            for (ScenarioDefinition scenarioDefinition : feature.getScenarioDefinitions()) {
                filteredScenraios.add(scenarioDefinition);
            }
        }
        return filteredScenraios;
    }

    public List<ScenarioDefinition> getFilteredScenarios(String[] tagFilter) {
        List<String> tagsToSelectList = Arrays.asList(tagFilter);
        ArrayList<ScenarioDefinition> filteredScenarios = new ArrayList<>();
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            for (ScenarioDefinition scenarioDefinition : feature.getScenarioDefinitions()) {
                if (scenarioDefinition.getTags().stream().anyMatch((t) -> tagsToSelectList.contains(t.getName()))) {
                    filteredScenarios.add(scenarioDefinition);
                }
            }
        }
        return filteredScenarios;
    }

    public List<ScenarioDefinition> getFilteredScenarios(Feature feature, String[] tagFilter) {
        List<String> tagsToSelectList = Arrays.asList(tagFilter);
        ArrayList<ScenarioDefinition> filteredScenarios = new ArrayList<>();
        for (ScenarioDefinition scenarioDefinition : feature.getScenarioDefinitions()) {
            if (scenarioDefinition.getTags().stream().anyMatch((t) -> tagsToSelectList.contains(t.getName()))) {
                filteredScenarios.add(scenarioDefinition);
            }
        }
        return filteredScenarios;
    }
}
