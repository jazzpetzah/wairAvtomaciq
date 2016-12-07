package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import static com.wire.picklejar.Config.SCREENSHOT_PATH;
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
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for deleting screenshots. This class should actually reside on the
 * test execution implementation but for testing purposes it's included in PickleJar for now.
 *
 */
public class PickleJarExecutionListener extends RunListener {

    private static final Logger LOG = LoggerFactory.getLogger(PickleJarExecutionListener.class.getSimpleName());
    
    static{
        File screenshotFolder = Paths.get(SCREENSHOT_PATH).toFile();
        LOG.info("Saving screenshots to: " + screenshotFolder);
        if (screenshotFolder.exists()) {
            File[] screenshotFiles = screenshotFolder.listFiles();
            for (int i = 0; i < screenshotFiles.length; i++) {
                LOG.info("Deleting old screenshots for testcase: " + screenshotFiles[i]);
                deleteDirectory(screenshotFiles[i]);
            }
        } else {
            LOG.info("Screenshot folder does not exists - skipping cleanup");
        }
    }
    
    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }

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
