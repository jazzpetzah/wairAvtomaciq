package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wire.picklejar.PickleJar;
import java.util.List;
import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.gherkin.model.Result;
import static com.wire.picklejar.gherkin.model.Result.FAILED;
import com.wire.picklejar.gherkin.model.Step;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.logging.LogEntry;
import com.wire.picklejar.execution.PickleJarTestHook;
import com.wire.picklejar.execution.TestScreenshotHelper;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.execution.exception.StepNotFoundException;
import com.wire.picklejar.gherkin.model.Scenario;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class PickleJarHook implements PickleJarTestHook {

    private static final Logger LOG = ZetaLogger.getLog(PickleJarHook.class.getSimpleName());
    private static final AtomicInteger CURRENTLY_RUNNING_TESTS = new AtomicInteger(0);
    private final TestScreenshotHelper screenshotHelper = new TestScreenshotHelper();
    private PickleJar pickle;
    private final int MAX_LOG_TAIL_SIZE = 20;
    private final Lifecycle lifecycle = new Lifecycle();
    
    @Override
    public long invoke(String rawStep, Map<String, String> exampleRow) throws StepNotExecutableException, StepNotFoundException {
        return getPickle().getExecutor().invokeMethodForStep(rawStep, exampleRow, new Object[]{lifecycle.getContext()});
    }

    @Override
    public void onBeforeScenario(Scenario scenario) throws Throwable {
        LOG.info(String.format("### Before testcase Count: %d", CURRENTLY_RUNNING_TESTS.incrementAndGet()));
        LOG.info(String.format("\n"
                + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
                + "::          [%s %s: %s]\n"
                + "''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''",
                new Object[]{
                    Arrays.toString(scenario.getTags().stream().map((t) -> t.getName()).toArray()),
                    scenario.getFeature().getName(),
                    scenario.getName()}));
        lifecycle.setUp(scenario);
    }

    @Override
    public void onBeforeStep(Scenario scenario, Step step) throws Throwable {
        LOG.info(String.format("\n"
                + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
                + "::          %s\n"
                + "''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''", step.getName()));
    }

    @Override
    public void onStepPassed(Scenario scenario, Step step) throws Throwable {
    }

    @Override
    public void onStepFailed(Scenario scenario, Step step) throws Throwable {
        step.setResult(new Result(step.getResult().getDuration(), FAILED,
                step.getResult().getErrorMessage() + "\n" + tailBrowserLog(MAX_LOG_TAIL_SIZE)));
    }

    @Override
    public void onStepSkipped(Scenario scenario, Step step) throws Throwable {
    }

    @Override
    public void onAfterStep(Scenario scenario, Step step) throws Throwable {
        if ("SKIPPED".equalsIgnoreCase(step.getResult().getStatus())) {
            screenshotHelper.saveScreenshot(step, scenario, scenario.getFeature(), new byte[]{});
        } else {
            try {
                byte[] screenshot = lifecycle.getContext().getDriver().getScreenshotAs(OutputType.BYTES);
                screenshotHelper.saveScreenshot(step, scenario, scenario.getFeature(), screenshot);
            } catch (Exception e) {
                LOG.warn("Can not make sceenshot", e);
            }
        }
        LOG.info(String.format("\n::          %s", step.getResult().getStatus().toUpperCase()));
    }

    @Override
    public void onAfterScenario(Scenario scenario) throws Throwable {
        List<Step> reportSteps = scenario.getSteps();
        Exception ex = null;
        try {
            checkLogForErrors();
        } catch (Exception e) {
            if (!reportSteps.isEmpty()) {
                Step lastStep = reportSteps.get(reportSteps.size() - 1);
                lastStep.setResult(new Result(lastStep.getResult().getDuration(), FAILED,
                        PickleExecutor.getThrowableStacktraceString(e) + "\n" + tailBrowserLog(MAX_LOG_TAIL_SIZE)));
            }
            ex = e;
        }
        lifecycle.tearDown(scenario);
        LOG.info(String.format("### After testcase Count: %d", CURRENTLY_RUNNING_TESTS.decrementAndGet()));
        reportSteps.forEach((step)
                -> LOG.info(String.format("::          %s - %s", step.getResult().getStatus().toUpperCase(), step.getName()))
        );
        if (ex != null) {
            throw ex;
        }
    }

    private String tailBrowserLog(int maxLogTailSize) throws InterruptedException, ExecutionException, TimeoutException {
        if (!WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            return "No tailed log available";
        }
        try {
            List<LogEntry> browserLog = lifecycle.getContext().getLogManager().getBrowserLog();
            if (browserLog.size() >= maxLogTailSize) {
                browserLog = browserLog.subList(browserLog.size() - maxLogTailSize, browserLog.size());
            }
            return browserLog.stream()
                    .map((l) -> l.getMessage().replaceAll("^.*z\\.", "z\\."))
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOG.warn("No tailed log available");
            return "No tailed log available";
        }
    }

    private void checkLogForErrors() throws Exception {
        List<LogEntry> browserLog = new ArrayList<>();
        if (!WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            LOG.warn("No error log check available");
            return;
        }
        try {
            browserLog = lifecycle.getContext().getLogManager().getBrowserLog();
            browserLog = browserLog.stream()
                    .filter((entry)
                            -> entry.getLevel().intValue() >= Level.SEVERE.intValue())
                    // filter auto login attempts
                    .filter((entry)
                            -> !entry.getMessage().contains("/access") && !entry.getMessage().contains("403"))
                    .filter((entry)
                            -> !entry.getMessage().contains("/self") && !entry.getMessage().contains("401"))
                    .filter((entry)
                            -> !entry.getMessage().contains("attempt"))
                    // filter failed logins
                    .filter((entry)
                            -> !entry.getMessage().contains("/login") && !entry.getMessage().contains("403"))
                    // filter already used email on registration
                    .filter((entry)
                            -> !entry.getMessage().contains("/register?challenge_cookie=true") && !entry.getMessage().contains(
                    "409"))
                    // filter ignored image previews
                    .filter((entry)
                            -> !entry.getMessage().contains("Ignored image preview"))
                    // filter ignored hot knocks
                    .filter((entry)
                            -> !entry.getMessage().contains("Ignored hot knock"))
                    // filter e2ee error (breaking session)
                    .filter((entry)
                            -> !entry.getMessage().contains("Received message is for client"))
                    // filter youtube cast_sender
                    .filter((entry)
                            -> !entry.getMessage().contains("cast_sender.js"))
                    // filter spotify
                    .filter((entry)
                            -> !entry.getMessage().contains("/service/version.json") && !entry.getMessage().contains(
                    "Failed to load resource"))
                    // filter removed temporary devices
                    .filter((entry)
                            -> !entry.getMessage().contains("Caused by: Local client does not exist on backend"))
                    // filter too many clients
                    .filter((entry)
                            -> !entry.getMessage().contains("/clients") && !entry.getMessage().contains("403"))
                    .filter((entry)
                            -> !entry.getMessage().contains("User has reached the maximum of allowed clients"))
                    // filter broken sessions
                    .filter((entry)
                            -> !entry.getMessage().contains(
                            "broken or out of sync. Reset the session and decryption is likely to work again."))
                    .filter((entry)
                            -> !entry.getMessage().contains("and we have client ID"))
                    .filter((entry)
                            -> !entry.getMessage().contains("Unhandled event type"))
                    // filter full call
                    .filter((entry)
                            -> !entry.getMessage().contains("/call/state") && !entry.getMessage().contains("409"))
                    .filter((entry)
                            -> !entry.getMessage().contains("failed: the voice channel is full"))
                    .filter((entry)
                            -> !entry.getMessage().contains("Too many participants in call"))
                    // filter failed asset download
                    .filter((entry)
                            -> !entry.getMessage().contains("Failed to download asset"))
                    // filter failed group renaming
                    .filter((entry)
                            -> !entry.getMessage().contains("Failed to rename conversation"))
                    // filter encryption precondition
                    .filter((entry)
                            -> !entry.getMessage().contains("otr") && !entry.getMessage().contains("412 (Precondition Failed)"))
                    //filter youtube javascript error
                    .filter((entry)
                            -> !entry.getMessage().contains("ytcfg is not defined"))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LOG.warn("No error log check available", e);
        }
        if (!browserLog.isEmpty()) {
            throw new Exception("BrowserLog does have errors: \n" + browserLog.stream()
                    .map((entry) -> String.format("%s: %s", entry.getLevel(), entry.getMessage()))
                    .collect(Collectors.joining("\n")));
        }
    }

    @Override
    public void setPickle(PickleJar pickle) {
        this.pickle = pickle;
    }

    @Override
    public PickleJar getPickle() {
        return pickle;
    }
}
