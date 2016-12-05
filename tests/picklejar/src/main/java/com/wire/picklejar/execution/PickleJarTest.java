package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import com.wire.picklejar.PickleJar;
import java.util.List;
import java.util.Map;
import com.wire.picklejar.PickleJarJUnitProvider;
import static com.wire.picklejar.execution.PickleExecutor.replaceExampleOccurences;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.gherkin.model.Feature;
import com.wire.picklejar.gherkin.model.Result;
import static com.wire.picklejar.gherkin.model.Result.FAILED;
import static com.wire.picklejar.gherkin.model.Result.PASSED;
import static com.wire.picklejar.gherkin.model.Result.SKIPPED;
import com.wire.picklejar.gherkin.model.Scenario;
import com.wire.picklejar.gherkin.model.Step;
import com.wire.picklejar.gherkin.model.Tag;
import com.wire.picklejar.scan.PickleAnnotationSeeker;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PickleJarTest {

    protected static final Logger LOG = LoggerFactory.getLogger(PickleJarTest.class.getSimpleName());

    static final Map<Feature, List<Scenario>> FEATURE_SCENARIO_MAP = new ConcurrentHashMap<>();
    private final PickleJarTestHook hook;
    private final PickleJar pickle = new PickleJarJUnitProvider();

    private final List<String> rawSteps;
    private final Map<String, String> rawExampleRow;
    private final Scenario reportScenario;
    private final List<Step> reportSteps = new ArrayList<>();

    protected PickleJarTest(PickleJarTestHook hook, String feature, String scenarioName, Integer exampleNum, List<String> steps,
            Map<String, String> exampleRow, List<String> tags) throws Exception {
        this.hook = hook;
        hook.setPickle(pickle);
        this.rawSteps = steps;
        this.rawExampleRow = exampleRow;
        for (String rawStep : rawSteps) {
            this.reportSteps.add(new Step(replaceExampleOccurences(rawStep, rawExampleRow)));
        }
        this.reportScenario = new Scenario(new Feature(feature), scenarioName, exampleNum, "Scenario Outline", reportSteps,
                tags.stream()
                        .map((t) -> new Tag(t))
                        .collect(Collectors.toList()));
    }

    @BeforeClass
    protected static void setUpClass() throws Exception {
    }

    @Before
    protected void setUp() throws Throwable {
        pickle.reset();
        List<Scenario> scenariosForFeature = FEATURE_SCENARIO_MAP.getOrDefault(getReportScenario().getFeature(),
                new ArrayList<>());
        scenariosForFeature.add(getReportScenario());
        FEATURE_SCENARIO_MAP.put(getReportScenario().getFeature(), scenariosForFeature);
        try {
            hook.onBeforeScenario(getReportScenario());
        } catch (Exception e) {
            getReportScenario().getSteps().stream().findFirst().ifPresent((s) -> s.setResult(new Result(1L, FAILED,
                    PickleExecutor.getThrowableStacktraceString(e))));
            throw e;
        }
    }

    @Test
    protected void test() throws Throwable {
        Throwable ex = null;
        boolean skipAll = false;
        for (int i = 0; i < rawSteps.size(); i++) {
            final String rawStep = rawSteps.get(i);
            final Step reportStep = reportSteps.get(i);
            long execTime = 1L;
            hook.onBeforeStep(reportScenario, reportStep);
            if (!skipAll) {
                try {
                    execTime = hook.invoke(rawStep, getRawExampleRow());
                    reportStep.setResult(new Result(execTime, PASSED, null));
                    hook.onStepPassed(reportScenario, reportStep);
                } catch (Throwable e) {
                    ex = e;
                    if (ex instanceof StepNotExecutableException) {
                        execTime = ((StepNotExecutableException) e).getExecutionTime();
                        ex = PickleExecutor.getLastCause(e);
                    }
                    if (PickleAnnotationSeeker.isClassWithAnnotation(ex.getClass(), Config.SKIP_ANNOTATION_CLASS)) {
                        reportStep.setResult(new Result(execTime, SKIPPED, PickleExecutor.getThrowableStacktraceString(ex)));
                        hook.onStepSkipped(reportScenario, reportStep);
                        skipAll = true;
                    } else {
                        reportStep.setResult(new Result(execTime, FAILED, PickleExecutor.getThrowableStacktraceString(ex)));
                        hook.onStepFailed(reportScenario, reportStep);
                        skipAll = true;
                    }
                }
            } else {
                reportStep.setResult(new Result(execTime, SKIPPED, null));
                hook.onStepSkipped(reportScenario, reportStep);
            }
            hook.onAfterStep(reportScenario, reportStep);
        }
        if (ex != null) {
            throw ex;
        }
    }

    @After
    protected void tearDown() throws Throwable {
        hook.onAfterScenario(getReportScenario());
    }

    @AfterClass
    protected static void tearDownClass() throws Exception {
    }

    protected PickleJar getPickle() {
        return pickle;
    }

    protected Scenario getReportScenario() {
        return reportScenario;
    }

    protected List<String> getRawSteps() {
        return rawSteps;
    }

    public Map<String, String> getRawExampleRow() {
        return rawExampleRow;
    }
}
