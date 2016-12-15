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

    LinkedHashMap reportMap = new LinkedHashMap<LinkedHashMap<String, String>, ArrayList<Step>>();

    private ReportInformation ri;

    StepsProcessor(ReportInformation ri) {
        this.ri = ri;
    }

    void loadReportInformationToMap() {
        List<Feature> features = ri.getFeatures();
        for (Feature feature : features) {
            Sequence<Element> scenarios = feature.getElements();
            if (Util.itemExists(scenarios)) {
                for (Element scenario : scenarios) {
                    ArrayList<Step> stepsList = new ArrayList<Step>();
                    LinkedHashMap key = new LinkedHashMap<String, String>();
                    if (Util.hasSteps(scenario)) {
                        key.put(feature.getRawName(), scenario.getRawName());
                        if (reportMap.get(key) != null) {
                            stepsList.addAll((ArrayList<Step>) reportMap.get(key));
                        }
                        stepsList.addAll(scenario.getSteps().toList());
                    }
                    reportMap.put(key, stepsList);
                }
            }
        }
    }

    void preprareStepsIndexes(boolean skippedHaveScreenshots) {
        for (Object key : reportMap.keySet()) {
            ArrayList<Step> steps = (ArrayList<Step>) reportMap.get(key);
            //Set default index
            for (Step step : steps) {
                step.setIndex(1);
            }
            //Find duplicate steps and set index
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

    Feature updateStepsIndexesInFeature(Feature feature) {
        Sequence<Element> scenarios = feature.getElements();
        if (Util.itemExists(scenarios)) {
            scenarios = updateStepIndexesInScenario(feature.getRawName(), scenarios);
        }
        feature.setElements(scenarios);
        return feature;
    }

    private Sequence<Element> updateStepIndexesInScenario(String feature, Sequence<Element> scenarios) {
        if (Util.itemExists(scenarios)) {
            for (Element scenario : scenarios) {
                if (Util.hasSteps(scenario)) {
                    Sequence<Step> steps = scenario.getSteps();
                    LinkedHashMap key = new LinkedHashMap<String, String>();
                    Iterator iterator = steps.iterator();
                    key.put(feature, scenario.getRawName());
                    ArrayList<Step> indexedSteps = (ArrayList<Step>) reportMap.get(key);
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
