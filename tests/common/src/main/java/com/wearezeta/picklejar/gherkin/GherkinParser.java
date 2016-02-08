package com.wearezeta.picklejar.gherkin;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GherkinParser {

    private final List<String> features;
    private final String[] tagsToSelect;

    public GherkinParser(List<String> features, String[] tagsToSelect) {
        this.features = features;
        this.tagsToSelect = tagsToSelect;
    }

    public List<ScenarioOutline> getScenarios() {
        Parser<Feature> parser = new Parser<>(new AstBuilder());
        List<String> tagsToSelectList = Arrays.asList(tagsToSelect);
        ArrayList<ScenarioOutline> filteredScenraios = new ArrayList<>();
        for (String featureString : features) {
            Feature feature = parser.parse(featureString);
            for (ScenarioDefinition scenarioDefinition : feature.getScenarioDefinitions()) {
                if (((ScenarioOutline)scenarioDefinition).getTags().stream().anyMatch((t) -> tagsToSelectList.contains(t.getName()))) {
                    filteredScenraios.add(((ScenarioOutline)scenarioDefinition));
                }
            }
        }
        return filteredScenraios;
    }
}
