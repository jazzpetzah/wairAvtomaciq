package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.googlecode.totallylazy.Sequence;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;


public class StepsProcessor {

    //feature <~> scenario dependency
    LinkedHashMap scenariosMap = new LinkedHashMap<String, String>();
    //scenario <~> steps dependency
    LinkedHashMap stepsMap = new LinkedHashMap<String, ArrayList<Step>>();

    private ReportInformation ri;

    StepsProcessor(ReportInformation ri) {
        this.ri = ri;
    }

    void loadSteps() {
        List<Feature> features = ri.getFeatures();
        for (Feature feature : features) {
            Sequence<Element> scenarios = feature.getElements();
            if (Util.itemExists(scenarios)) {
                for (Element scenario : scenarios) {
                    String scenarioName = scenario.getRawName();
                    scenariosMap.put(scenarioName, feature.getRawName());
                    ArrayList<Step> stepsList = new ArrayList<Step>();
                    if (Util.hasSteps(scenario)) {
                        Sequence<Step> steps = scenario.getSteps();
                        if (stepsMap.get(scenarioName) != null) {
                            stepsList.addAll((ArrayList<Step>) stepsMap.get(scenarioName));
                        }
                        stepsList.addAll(steps.toList());
                    }
                    stepsMap.put(scenarioName, stepsList);
                }
            }
        }
    }

    void preprareStepsIndex(boolean skippedHaveScreenshots) {
        for (Object key : stepsMap.keySet()) {
            ArrayList<Step> steps = (ArrayList<Step>) stepsMap.get(key);
            //Set default index
            for (Step step : steps) {
                step.setIndex(1);
            }
            //Find duplicate steps and test index
            for (int i = 0; i < steps.size(); i++) {
                if (skippedHaveScreenshots) {
                    int index = steps.get(i).getIndex();
                    for (int j = i + 1; j < steps.size(); j++) {
                        if (steps.get(i).getRawName().equals(steps.get(j).getRawName())) {
                            steps.get(j).setIndex(++index);
                        }
                    }
                } else if (steps.get(i).getStatus().equals(Util.Status.SKIPPED)) {
                    steps.get(i).setIndex(0);
                } else {
                    int index = steps.get(i).getIndex();
                    for (int j = i + 1; j < steps.size(); j++) {
                        if (steps.get(i).getRawName().equals(steps.get(j).getRawName())) {
                            if (steps.get(j).getStatus().equals(Util.Status.SKIPPED)) {
                                steps.get(j).setIndex(0);
                            } else {
                                steps.get(j).setIndex(++index);
                            }
                        }
                    }
                }
            }
        }
    }

    Feature updateIndexesInFeature(Feature feature) {
        Sequence<Element> scenarios = feature.getElements();
        if (Util.itemExists(scenarios)) {
            scenarios = updateIndexesInScenario(scenarios);
        }
        feature.setElements(scenarios);
        return feature;
    }

    Sequence<Element> updateIndexesInScenario(Sequence<Element> scenarios) {
        if (Util.itemExists(scenarios)) {
            for (Element scenario : scenarios) {
                if (Util.hasSteps(scenario)) {
                    Sequence<Step> steps = scenario.getSteps();
                    Iterator iterator = steps.iterator();
                    ArrayList<Step> indexedSteps = (ArrayList<Step>) stepsMap.get(scenario.getRawName());
                    for (int i = 0; i < steps.size(); i++) {
                        if (iterator.hasNext()) {
                            Step step = (Step) iterator.next();
                            if (indexedSteps.size() > 0) {
                                step.setIndex(indexedSteps.get(0).getIndex());
                                indexedSteps.remove(0);
                            } else {
                                step.setIndex(1);
                            }
                        }
                    }
                    scenario.setSteps(steps);
                }
            }
        }
        return scenarios;
    }
}
