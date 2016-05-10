package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import com.wire.picklejar.gherkin.model.CucumberReport;
import com.wire.picklejar.gherkin.model.Feature;
import com.wire.picklejar.gherkin.model.Scenario;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import static com.wire.picklejar.execution.PickleJarTest.FEATURE_SCENARIO_MAP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateCucumberReportListener extends RunListener {
    
    private static final Logger LOG = LoggerFactory.getLogger(GenerateCucumberReportListener.class.getSimpleName());

    @Override
    public void testRunFinished(Result result) throws Exception {
        LOG.info("Test execution finished - Generating Cucumber report...");
        CucumberReport cucumberReport = new CucumberReport();
        for (Map.Entry<Feature, List<Scenario>> entry : FEATURE_SCENARIO_MAP.entrySet()) {
            Feature f = entry.getKey();
            f.setScenarios(entry.getValue());
            cucumberReport.add(f);
        }
        Files.write(Paths.get(Config.CUCUMBER_REPORT_PATH + "cucumber-report.json"), cucumberReport.toString().getBytes(
                StandardCharsets.UTF_8));
    }
    
}
