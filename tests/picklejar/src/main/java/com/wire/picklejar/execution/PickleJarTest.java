package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import com.wire.picklejar.PickleJar;
import java.util.List;
import java.util.Map;
import com.wire.picklejar.PickleJarJUnitProvider;
import static com.wire.picklejar.execution.PickleExecutor.replaceExampleOccurences;
import com.wire.picklejar.gherkin.model.CucumberReport;
import com.wire.picklejar.gherkin.model.Feature;
import com.wire.picklejar.gherkin.model.Scenario;
import com.wire.picklejar.gherkin.model.Step;
import com.wire.picklejar.gherkin.model.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PickleJarTest {

    private static final Logger LOG = LoggerFactory.getLogger(PickleJarTest.class.getSimpleName());

    static final Map<Feature, List<Scenario>> FEATURE_SCENARIO_MAP = new ConcurrentHashMap<>();
    private static final AtomicInteger TEST_COUNTER = new AtomicInteger(0);
    private final PickleJar pickle = new PickleJarJUnitProvider();

    private final String feature;
    private final String testcase;
    private final List<String> steps;
    private final Map<String, String> exampleRow;

    private final Feature reportFeature;
    private final Scenario reportScenario;
    private final List<Step> reportSteps = new ArrayList<>();
    

    protected PickleJarTest(String feature, String testcase, Integer exampleNum, List<String> steps, Map<String, String> exampleRow)
            throws Exception {
        this.feature = feature;
        this.testcase = testcase;
        this.steps = steps;
        this.exampleRow = exampleRow;

        for (String rawStep : steps) {
            this.reportSteps.add(new Step(replaceExampleOccurences(rawStep, getExampleRow())));
        }
        this.reportScenario = new Scenario(feature, testcase, exampleNum, "Scenario Outline", reportSteps, Arrays.asList(
                new Tag[]{new Tag("@TODO")}));
        this.reportFeature = new Feature(feature);
    }

    @BeforeClass
    protected static void setUpClass() throws Exception {
        LOG.info("### Before full testrun");
    }

    @Before
    protected void setUp() throws Exception {
        LOG.info("### Before testcase Count: {}", TEST_COUNTER.incrementAndGet());
        pickle.reset();
        List<Scenario> scenariosForFeature = FEATURE_SCENARIO_MAP.getOrDefault(reportFeature, new ArrayList<>());
        scenariosForFeature.add(reportScenario);
        FEATURE_SCENARIO_MAP.put(reportFeature, scenariosForFeature);
    }

    @Test
    protected void test() throws Exception {
        LOG.info("Executing {}: {}", new Object[]{feature, testcase});
    }

    @After
    protected void tearDown() throws Exception {
        LOG.info("### After testcase Count: {}", TEST_COUNTER.decrementAndGet());

    }

    @AfterClass
    protected static void tearDownClass() throws Exception {
        LOG.info("### After full testrun");
    }

    protected void saveScreenshot(Step step, byte[] screenshot) throws IOException {
        final String featureName = reportFeature.getName().replaceAll("[^a-zA-Z0-9]", "_");
        final String scenarioName = reportScenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
        final String stepName = step.getName().replaceAll("[^a-zA-Z0-9]", "_");
        Path path = Paths.get("target/Images/" + featureName + "/" + scenarioName + "/");
        path.toFile().mkdirs();
        int index = 0;
        Path desiredPicture = Paths.get(path.toString(), stepName + "_" + index + ".png");
        // we abort the while loop when index exeeds 10. 
        // That means you have the same step 10 times in one scenario which is unlikely to happen
        while (desiredPicture.toFile().exists() && index < 10) {
            index++;
            desiredPicture = Paths.get(path.toString(), stepName + "_" + index + ".png");
        }
        Files.write(desiredPicture, screenshot);
    }

    protected PickleJar getPickle() {
        return pickle;
    }

    protected Scenario getReportScenario() {
        return reportScenario;
    }

    protected String getFeature() {
        return feature;
    }

    protected String getTestcase() {
        return testcase;
    }

    protected List<String> getSteps() {
        return steps;
    }

    protected Map<String, String> getExampleRow() {
        return exampleRow;
    }

}
