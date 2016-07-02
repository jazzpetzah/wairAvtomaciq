package com.wearezeta.auto.web.common;

import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.execution.PickleJarTest;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.gherkin.model.Result;
import com.wire.picklejar.gherkin.model.Step;
import java.io.IOException;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.OutputType;

@RunWith(Parameterized.class)
public class PickleJarInheritedTest extends PickleJarTest {

    private Lifecycle lifecycle;
    
    private static final String SKIPPED = "skipped";
    private static final String UNDEFINED = "undefined";
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";

    @Parameters(name = "{0}: {1} {2}")
    public static Collection<Object[]> getTestcases() throws IOException {
        return PickleJar.getTestcases();
    }

    public PickleJarInheritedTest(String feature, String testcase, Integer exampleNum, List<String> steps,
            Map<String, String> examples, List<String> tags) throws Exception {
        super(feature, testcase, exampleNum, steps, examples, tags);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PickleJarTest.setUpClass();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getReportScenario().getSteps().stream().forEach((s) -> s.setResult(new Result(1L, SKIPPED, null)));
        try {
            lifecycle = new Lifecycle();
            lifecycle.setUp(getTestcase());
        } catch (Exception e) {
            getReportScenario().getSteps().stream().findFirst().ifPresent((s) -> s.setResult(new Result(1L, FAILED,
                    PickleExecutor.getThrowableStacktraceString(e))));
            throw e;
        }
    }

    @Test
    @Override
    public void test() throws Throwable {
        super.test();
        boolean failed = false;
        Throwable ex = null;
        List<Step> reportSteps = getReportScenario().getSteps();
        for (int i = 0; i < getSteps().size(); i++) {
            final String rawStep = getSteps().get(i);
            final Step reportStep = reportSteps.get(i);
            if (!failed) {
                try {
                    long execTime = getPickle().getExecutor().invokeMethodForStep(rawStep, getExampleRow(), lifecycle.
                            getContext());
                    reportStep.setResult(new Result(execTime, PASSED, null));
                    byte[] screenshot = lifecycle.getContext().getDriver().getScreenshotAs(OutputType.BYTES);
                    saveScreenshot(reportStep, screenshot);
                } catch (Throwable e) {
                    long execTime = 1L;
                    if (e instanceof StepNotExecutableException) {
                        execTime = ((StepNotExecutableException) e).getExecutionTime();
                        ex = PickleExecutor.getLastCause(e);
                    } else {
                        ex = e;
                    }
                    failed = true;
                    String stacktrace = PickleExecutor.getThrowableStacktraceString(ex);
                    reportStep.setResult(new Result(execTime, FAILED, stacktrace));
                    byte[] screenshot = lifecycle.getContext().getDriver().getScreenshotAs(OutputType.BYTES);
                    saveScreenshot(reportStep, screenshot);

                    continue;
                }
            } else {
                reportStep.setResult(new Result(1L, SKIPPED, null));
            }
        }
        if (failed) {
            throw ex;
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        lifecycle.tearDown(getReportScenario());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PickleJarTest.tearDownClass();
    }
    
}
