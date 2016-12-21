package net.masterthought.cucumber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.masterthought.cucumber.charts.JsChartUtil;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.UnzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;

public class ReportBuilder {

    ReportInformation ri;
    private File reportDirectory;
    private String buildNumber;
    private String buildProject;
    private String pluginUrlPath;
    private boolean runWithJenkins;
    private boolean artifactsEnabled;
    private boolean parsingError;
    private String coverage;
    private String customDescription;
    private String realBuildNumber;

    public Map<String, String> getCustomHeader() { return customHeader; }

    public void setCustomHeader(Map<String, String> customHeader) {
        this.customHeader = customHeader;
    }

    private Map<String, String> customHeader;

    private final String VERSION = "cucumber-reporting-0.0.28";

    public ReportBuilder(List<String> jsonReports, File reportDirectory, String customDescription, String coverage,
                         String realBuildNumber, String pluginUrlPath, String buildNumber, String buildProject,
                         boolean skippedFails, boolean pendingFails, boolean undefinedFails, boolean allSkippedFails,
                         boolean skippedHaveScreenshots, boolean runWithJenkins, boolean artifactsEnabled,
                         String artifactConfig) throws Exception {
        try {
            this.reportDirectory = reportDirectory;
            this.customDescription = customDescription;
            this.coverage = coverage;
            this.realBuildNumber = realBuildNumber;
            this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
            this.buildNumber = buildNumber;
            this.buildProject = buildProject;
            ConfigurationOptions.setSkippedFailsBuild(skippedFails);
            ConfigurationOptions.setPendingFailsBuild(pendingFails);
            ConfigurationOptions.setUndefinedFailsBuild(undefinedFails);
            ConfigurationOptions.setAllSkippedFailsBuild(allSkippedFails);
            this.runWithJenkins = runWithJenkins;
            this.artifactsEnabled = artifactsEnabled;

            ConfigurationOptions.setArtifactsEnabled(artifactsEnabled);
            if (artifactsEnabled) {
                ArtifactProcessor artifactProcessor = new ArtifactProcessor(artifactConfig);
                ConfigurationOptions.setArtifactConfiguration(artifactProcessor.process());
            }

            ReportParser reportParser = new ReportParser(jsonReports);
            this.ri = new ReportInformation(reportParser.getFeatures(), skippedHaveScreenshots);
        } catch (Exception exception) {
            parsingError = true;
            generateErrorPage(exception);
            System.out.println(exception);
        }
    }

    public boolean getBuildStatus() {
        return (ri.getTotalScenariosFailed() == 0);
    }

    public int getTotalNumberOfSteps() { return ri.getTotalNumberOfSteps(); }

    public void generateReports() throws Exception {
        try {
            copyResource("themes", "blue.zip");
            copyResource("charts", "js.zip");
            if (artifactsEnabled) {
                copyResource("charts", "codemirror.zip");
            }
            generateFeatureOverview();
            generateFeatureReports();
        } catch (Exception exception) {
            if (!parsingError) {
                generateErrorPage(exception);
                System.out.println(exception);
                exception.printStackTrace();
            }
        }
    }

    public void generateFeatureReports() throws Exception {
        Iterator it = ri.getProjectFeatureMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();

            for (Feature feature : featureList) {
                VelocityEngine ve = new VelocityEngine();
                ve.init(getProperties());
                Template featureResult = ve.getTemplate("templates/featureReport.vm");
                VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
                contextMap.putAll(getGeneralParameters());
                contextMap.put("feature", feature);
                contextMap.put("report_status_colour", ri.getReportStatusColour(feature));
                contextMap.put("scenarios", feature.getElements().toList());
                contextMap.put("time_stamp", ri.timeStamp());
                contextMap.put("artifactsEnabled", ConfigurationOptions.artifactsEnabled());
                contextMap.put("esc", new EscapeTool());
                generateReport(feature.getFileName(), featureResult, contextMap.getVelocityContext());
            }
        }
    }

    private void generateFeatureOverview() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template featureOverview = ve.getTemplate("templates/featureOverview.vm");
        VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
        contextMap.putAll(getGeneralParameters());
        contextMap.put("features", ri.getFeatures());
        contextMap.put("total_features", ri.getTotalNumberOfFeatures());
        contextMap.put("total_scenarios", ri.getTotalNumberOfScenarios());
        contextMap.put("total_steps", ri.getTotalNumberOfSteps());
        contextMap.put("total_passes", ri.getTotalNumberPassingSteps());
        contextMap.put("total_fails", ri.getTotalNumberFailingSteps());
        contextMap.put("total_skipped", ri.getTotalNumberSkippedSteps());
        contextMap.put("total_pending", ri.getTotalNumberPendingSteps());
        contextMap.put("total_undefined", ri.getTotalNumberUndefinedSteps());
        contextMap.put("scenarios_passed", ri.getTotalScenariosPassed());
        contextMap.put("scenarios_failed", ri.getTotalScenariosFailed());
        JsChartUtil pie = new JsChartUtil();
        List<String> stepColours = pie.orderStepsByValue(ri.getTotalNumberPassingSteps(), ri.getTotalNumberFailingSteps(),
                ri.getTotalNumberSkippedSteps(), ri.getTotalNumberPendingSteps(), ri.getTotalNumberUndefinedSteps());
        contextMap.put("step_data", stepColours);
        List<String> scenarioColours = pie.orderScenariosByValue(ri.getTotalScenariosPassed(), ri.getTotalScenariosFailed());
        contextMap.put("scenario_data", scenarioColours);
        contextMap.put("time_stamp", ri.timeStamp());
        contextMap.put("total_duration", ri.getTotalDurationAsString());
        generateReport("feature-overview.html", featureOverview, contextMap.getVelocityContext());
    }

    public void generateErrorPage(Exception exception) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template errorPage = ve.getTemplate("templates/errorPage.vm");
        VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
        contextMap.putAll(getGeneralParameters());
        contextMap.put("error_message", exception);
        contextMap.put("time_stamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        generateReport("feature-overview.html", errorPage, contextMap.getVelocityContext());
    }

    private void copyResource(String resourceLocation, String resourceName) throws IOException, URISyntaxException {
        final File tmpResourcesArchive = File.createTempFile("temp", resourceName + ".zip");

        InputStream resourceArchiveInputStream = ReportBuilder.class.getResourceAsStream(resourceLocation + "/" + resourceName);
        if (resourceArchiveInputStream == null) {
            resourceArchiveInputStream = ReportBuilder.class.getResourceAsStream("/" + resourceLocation + "/" + resourceName);
        }
        OutputStream resourceArchiveOutputStream = new FileOutputStream(tmpResourcesArchive);
        try {
            IOUtils.copy(resourceArchiveInputStream, resourceArchiveOutputStream);
        } finally {
            IOUtils.closeQuietly(resourceArchiveInputStream);
            IOUtils.closeQuietly(resourceArchiveOutputStream);
        }
        UnzipUtils.unzipToFile(tmpResourcesArchive, reportDirectory);
        FileUtils.deleteQuietly(tmpResourcesArchive);
    }

    private String getPluginUrlPath(String path) {
        return path.isEmpty() ? "/" : path;
    }

    private void generateReport(String fileName, Template featureResult, VelocityContext context) throws Exception {
        FileOutputStream fileStream = new FileOutputStream(new File(reportDirectory, fileName));
        OutputStreamWriter writer = new OutputStreamWriter(fileStream, "UTF-8");
        featureResult.merge(context, writer);
        writer.flush();
        writer.close();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("runtime.log", new File(reportDirectory, "velocity.log").getPath());
        return props;
    }

    private HashMap<String, Object> getGeneralParameters() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        
        if (customDescription == null || customDescription.isEmpty())
            customDescription = "Feature Overview";
            if (coverage == null || coverage.isEmpty())
                coverage = "";
            else
                coverage = "COVERAGE: ".concat(coverage).replace("Run", "");
            if (realBuildNumber == null || realBuildNumber.isEmpty())
                realBuildNumber = "";
            else
                realBuildNumber = "BUILD: ".concat(realBuildNumber);

            result.put("version", VERSION);
            result.put("fromJenkins", runWithJenkins);
            result.put("jenkins_base", pluginUrlPath);
            result.put("build_project", buildProject);
            result.put("build_number", buildNumber);
            result.put("custom_description", customDescription);
            result.put("coverage", coverage);
            result.put("real_build_number", realBuildNumber);
            return result;
    }
}
