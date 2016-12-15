package net.masterthought.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.googlecode.totallylazy.Sequence;
import net.masterthought.cucumber.json.Artifact;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

public class ReportInformation {

    private Map<String, List<Feature>> projectFeatureMap;
    private List<Feature> features;
    private int numberOfScenarios;
    private int numberOfSteps;
    private List<Step> totalPassingSteps = new ArrayList<Step>();
    private List<Step> totalFailingSteps = new ArrayList<Step>();
    private List<Step> totalSkippedSteps = new ArrayList<Step>();
    private List<Step> totalPendingSteps = new ArrayList<Step>();
    private List<Step> totalUndefinedSteps = new ArrayList<Step>();
    private List<Step> totalMissingSteps = new ArrayList<Step>();
    private List<Element> numberPassingScenarios = new ArrayList<Element>();
    private List<Element> numberFailingScenarios = new ArrayList<Element>();
    private Long totalDuration = 0l;
    private Background backgroundInfo;
    private boolean skippedHaveScreenshots;

    public ReportInformation(Map<String, List<Feature>> projectFeatureMap, boolean skippedHaveScreenshots) {
        this.projectFeatureMap = projectFeatureMap;
        this.features = listAllFeatures();
        backgroundInfo = new Background();
        this.skippedHaveScreenshots = skippedHaveScreenshots;
        processFeatures();
    }

    private List<Feature> listAllFeatures() {
        List<Feature> allFeatures = new ArrayList<Feature>();
        Iterator it = projectFeatureMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();
            allFeatures.addAll(featureList);
        }
        return allFeatures;
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public Map<String, List<Feature>> getProjectFeatureMap() {
        return this.projectFeatureMap;
    }

    public int getTotalNumberOfScenarios() {
        return numberOfScenarios;
    }

    public int getTotalNumberOfFeatures() {
        return features.size();
    }

    public int getTotalNumberOfSteps() {
        return numberOfSteps;
    }

    public int getTotalNumberPassingSteps() {
        return totalPassingSteps.size();
    }

    public int getTotalNumberFailingSteps() {
        return totalFailingSteps.size();
    }

    public int getTotalNumberSkippedSteps() {
        return totalSkippedSteps.size();
    }

    public int getTotalNumberPendingSteps() {
        return totalPendingSteps.size();
    }

    public int getTotalNumberUndefinedSteps() {
        return totalUndefinedSteps.size();
    }

    public int getTotalNumberMissingSteps() {
        return totalMissingSteps.size();
    }

    public String getTotalDurationAsString() {
        return Util.formatDuration(totalDuration);
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public String timeStamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public String getReportStatusColour(Feature feature) {
        return feature.getStatus() == Util.Status.PASSED ? "#C5D88A" : "#D88A8A";
    }

    public int getTotalScenariosPassed() {
        return numberPassingScenarios.size();
    }

    public int getTotalScenariosFailed() {
        return numberFailingScenarios.size();
    }

    private void processFeatures() {
        StepsProcessor stepsProcessor = new StepsProcessor(this);
        stepsProcessor.loadReportInformationToMap();
        stepsProcessor.preprareStepsIndexes(skippedHaveScreenshots);
        for (Feature feature : features) {
            stepsProcessor.updateStepsIndexesInFeature(feature);
            Sequence<Element> scenarios = feature.getElements();
            if (Util.itemExists(scenarios)) {
                numberOfScenarios = getNumberOfScenarios(scenarios);
                for (Element scenario : scenarios) {
                    if (!scenario.getKeyword().equals("Background")) {
                        numberPassingScenarios = Util.setScenarioStatus(numberPassingScenarios, scenario, scenario.getStatus(),
                                Util.Status.PASSED);
                        numberFailingScenarios = Util.setScenarioStatus(numberFailingScenarios, scenario, scenario.getStatus(),
                                Util.Status.FAILED);
                    } else {
                        setBackgroundInfo(scenario);
                    }
                    adjustStepsForScenario(scenario, feature);
                }
            }
        }
    }

    private void setBackgroundInfo(Element e) {
        backgroundInfo.addTotalScenarios(1);
        if (e.getStatus() == Util.Status.PASSED) {
            backgroundInfo.addTotalScenariosPassed(1);
        } else {
            backgroundInfo.addTotalScenariosFailed(1);
        }
        backgroundInfo.addTotalSteps(e.getSteps().size());
        for (Step step : e.getSteps().toList()) {
            backgroundInfo.addTotalDuration(step.getDuration());
            switch (step.getStatus()) {
                case PASSED:
                    backgroundInfo.addTotalStepsPassed(1);
                    break;
                case FAILED:
                    backgroundInfo.addTotalStepsFailed(1);
                    break;
                case SKIPPED:
                    backgroundInfo.addTotalStepsSkipped(1);
                    break;
                case PENDING:
                    backgroundInfo.addTotalStepsPending(1);
                    break;
                case UNDEFINED:
                    backgroundInfo.addTotalStepsUndefined(1);
                    break;
                default:
                    break;
            }
        }

    }

    private void adjustStepsForScenario(Element element, Feature feature) {

        String scenarioName = element.getRawName();

        if (Util.hasSteps(element)) {
            Sequence<Step> steps = element.getSteps(true);
            numberOfSteps = numberOfSteps + steps.size();
            for (Step step : steps) {
                String stepName = step.getRawName();
                //apply artifacts
                if (ConfigurationOptions.artifactsEnabled()) {
                    Map<String, Artifact> map = ConfigurationOptions.artifactConfig();
                    String mapKey = scenarioName + stepName;
                    if (map.containsKey(mapKey)) {
                        Artifact artifact = map.get(mapKey);
                        String keyword = artifact.getKeyword();
                        String contentType = artifact.getContentType();
                        step.setName(stepName.replaceFirst(keyword, getArtifactFile(mapKey, keyword, artifact.getArtifactFile(), contentType)));
                    }
                }

                Util.Status stepStatus = step.getStatus();
                totalPassingSteps = Util.setStepStatus(totalPassingSteps, step, stepStatus, Util.Status.PASSED);
                totalFailingSteps = Util.setStepStatus(totalFailingSteps, step, stepStatus, Util.Status.FAILED);
                totalSkippedSteps = Util.setStepStatus(totalSkippedSteps, step, stepStatus, Util.Status.SKIPPED);
                totalPendingSteps = Util.setStepStatus(totalPendingSteps, step, stepStatus, Util.Status.PENDING);
                totalUndefinedSteps = Util.setStepStatus(totalUndefinedSteps, step, stepStatus, Util.Status.UNDEFINED);
                totalMissingSteps = Util.setStepStatus(totalMissingSteps, step, stepStatus, Util.Status.MISSING);
                totalDuration = totalDuration + step.getDuration();
            }
        }
    }

    private int getNumberOfScenarios(Sequence<Element> scenarios) {
        List<Element> scenarioList = new ArrayList<Element>();
        for (Element scenario : scenarios) {
            if (!scenario.getKeyword().equals("Background")) {
                scenarioList.add(scenario);
            }
        }
        return numberOfScenarios + scenarioList.size();
    }

    private String getArtifactFile(String mapKey, String keyword, String artifactFile, String contentType) {
        mapKey = mapKey.replaceAll(" ", "_");
        String link = "";
        if (contentType.equals("xml")) {
            link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\" class=\"brush: xml;\"></textarea></div><a onclick=\"applyArtifact('" + mapKey + "','" + artifactFile + "')\" href=\"#\">" + keyword + "</a>";
        } else {
            link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\"></textarea></div><script>\\$('#" + mapKey + "').load('" + artifactFile + "')</script><a onclick=\"\\$('#" + mapKey + "').dialog();\" href=\"#\">" + keyword + "</a>";
        }
        return link;
    }

    public Background getBackgroundInfo() {
        return backgroundInfo;
    }
}
