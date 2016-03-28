package com.wire.picklejar.gherkin;

import com.wire.picklejar.Config;
import com.wire.picklejar.scan.JavaSeeker;
import com.wire.picklejar.scan.PickleFeatureReader;
import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GherkinParser {
    
    private static final Logger LOG = LoggerFactory.getLogger(GherkinParser.class);

    private static final Map<String, String> featureContents;
    private static final Parser<Feature> parser = new Parser<>(new AstBuilder());
    
    static {
        featureContents = getFeatureContents(getFeatureFiles());
    }
    
    private static Map<String, File> getFeatureFiles() {
        final Map<String, File> featureFiles = new HashMap<>();
        try {
            Collection<File> resource = JavaSeeker.getResource(Config.FEATURE_PACKAGE, Config.FEATURE_EXTENSION);
            for (File file : resource) {
                LOG.debug(file.getAbsolutePath());
                featureFiles.put(file.getName(), file);
            }
        } catch (IOException ex) {
            LOG.error("Could not get feature files", ex);
        }
        return featureFiles;
    }

    private static Map<String, String> getFeatureContents(Map<String, File> featureFiles) {
        final Map<String, String> featureContents = new ConcurrentHashMap<>();
        for (Map.Entry<String, File> entry : featureFiles.entrySet()) {
            try {
                featureContents.put(entry.getKey(), PickleFeatureReader.readFile(entry.getValue()));
            } catch (IOException ex) {
                LOG.error("Could not read feature file", ex);
            }
        }
        return featureContents;
    }

    public static List<Feature> getAllFeatures() {
        List<Feature> features = new ArrayList<>();
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            features.add(feature);
        }
        return features;
    }

    public static List<Feature> getFilteredFeatures(String[] tagFilter) {
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

    public static List<ScenarioDefinition> getAllScenarios() {
        ArrayList<ScenarioDefinition> filteredScenraios = new ArrayList<>();
        for (Map.Entry<String, String> featureEntry : featureContents.entrySet()) {
            Feature feature = parser.parse(featureEntry.getValue());
            for (ScenarioDefinition scenarioDefinition : feature.getScenarioDefinitions()) {
                filteredScenraios.add(scenarioDefinition);
            }
        }
        return filteredScenraios;
    }

    public static List<ScenarioDefinition> getFilteredScenarios(String[] tagFilter) {
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

    public static List<ScenarioDefinition> getFilteredScenarios(Feature feature, String[] tagFilter) {
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
