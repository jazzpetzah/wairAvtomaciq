package com.wearezeta.auto.web.common;

import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.execution.PickleJarTest;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.gherkin.model.Result;
import static com.wire.picklejar.gherkin.model.Result.FAILED;
import static com.wire.picklejar.gherkin.model.Result.PASSED;
import static com.wire.picklejar.gherkin.model.Result.SKIPPED;
import com.wire.picklejar.gherkin.model.Step;
import cucumber.api.PendingException;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;

@RunWith(Parameterized.class)
public class PickleJarInheritedTest extends PickleJarTest {

    private final int MAX_LOG_TAIL_SIZE = 20;
    private Lifecycle lifecycle;

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
        try {
            lifecycle = new Lifecycle();
            lifecycle.setUp(getTestcase());
        } catch (Exception e) {
            getReportScenario().getSteps().stream().findFirst().ifPresent((s) -> setResult(s, new Result(1L, FAILED,
                    PickleExecutor.getThrowableStacktraceString(e))));
            throw e;
        }
    }

    @Test
    @Override
    public void test() throws Throwable {
        super.test();
        Throwable ex = null;
        List<Step> reportSteps = getReportScenario().getSteps();
        for (int i = 0; i < getSteps().size(); i++) {
            final String rawStep = getSteps().get(i);
            final Step reportStep = reportSteps.get(i);
            long execTime = 1L;
            try {
                execTime = getPickle().getExecutor().invokeMethodForStep(rawStep, getExampleRow(), lifecycle.getContext());
                setResult(reportStep, new Result(execTime, PASSED, null));
            } catch (Throwable e) {
                ex = e;
                if (ex instanceof StepNotExecutableException) {
                    execTime = ((StepNotExecutableException) e).getExecutionTime();
                    ex = PickleExecutor.getLastCause(e);
                }
                if (ex instanceof PendingException) {
                    setResult(reportStep, new Result(execTime, SKIPPED, PickleExecutor.getThrowableStacktraceString(ex)));
                    break;
                }
                setResult(reportStep, new Result(execTime, FAILED,
                        PickleExecutor.getThrowableStacktraceString(ex) + "\n" + tailBrowserLog(MAX_LOG_TAIL_SIZE)));
            }
            try {
                byte[] screenshot = lifecycle.getContext().getDriver().getScreenshotAs(OutputType.BYTES);
                saveScreenshot(reportStep, screenshot);
            } catch (Exception e) {
                LOG.warn("Can not make sceenshot", e);
            }
            if (ex != null) {
                throw ex;
            }
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

    private void setResult(Step reportStep, Result result) {
        LOG.info("\n::          {}", result.getStatus().toUpperCase());
        reportStep.setResult(result);
    }

    private String tailBrowserLog(int maxLogTailSize) throws InterruptedException, ExecutionException, TimeoutException {
        List<LogEntry> browserLog = Lifecycle.getBrowserLog(lifecycle.getContext().getDriver());
        if (browserLog.size() >= maxLogTailSize) {
            browserLog = browserLog.subList(browserLog.size() - maxLogTailSize, browserLog.size());
        }
        return browserLog.stream()
                .map((l) -> l.getMessage().replaceAll("^.*z\\.", "z\\."))
                .collect(Collectors.joining("\n"));
    }

}
