package com.wearezeta.auto.win.common;

import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.driver.ZetaWinWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.BasicScenarioResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.RegistrationPage;
import static com.wearezeta.auto.web.steps.CommonWebAppSteps.log;
import static com.wearezeta.auto.win.common.WinCommonUtils.clearAppData;
import static com.wearezeta.auto.win.common.WinCommonUtils.killAllApps;
import static com.wearezeta.auto.win.common.WinExecutionContext.KEEP_DATABASE;
import static com.wearezeta.auto.win.common.WinExecutionContext.WINIUM_URL;
import static com.wearezeta.auto.win.common.WinExecutionContext.WIRE_APP_FOLDER;
import static com.wearezeta.auto.win.common.WinExecutionContext.WIRE_APP_PATH;
import com.wearezeta.auto.win.pages.win.MainWirePage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import com.wire.picklejar.gherkin.model.Step;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Lifecycle {
    
    public static final Logger LOG = ZetaLogger.getLog(Lifecycle.class.getName());

    private TestContext webContext;
    private TestContext wrapperContext;
    private ChromeDriverService service;
    private String testname;

    /**
     * The context is fully initialized after setting up the testcase
     *
     * @return
     */
    public TestContext getWebContext() {
        return webContext;
    }

    public TestContext getWrapperContext() {
        return wrapperContext;
    }

    private Future<ZetaWebAppDriver> createWebDriver(Future<ZetaWinDriver> winDriver) throws IOException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-web-security");
        options.addArguments("env=" + WinExecutionContext.ENV_URL);
        options.addArguments("enable-logging");
        options.setBinary(WIRE_APP_FOLDER + WIRE_APP_PATH);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("platformName", WinExecutionContext.CURRENT_SECONDARY_PLATFORM.name());

        setExtendedLoggingLevel(capabilities, WinExecutionContext.EXTENDED_LOGGING_LEVEL);

        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(WinExecutionContext.CHROMEDRIVER_PATH))
                .usingAnyFreePort()
                .build();
        service.start();
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = () -> new ZetaWinWebAppDriver(
                service.getUrl(), capabilities, winDriver.get());

        return pool.submit(callableWebAppDriver);
    }

    private Future<ZetaWinDriver> createWinDriver() throws MalformedURLException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(CapabilityType.PLATFORM, "WIN");
        capabilities.setCapability("platformName", "Win");
        capabilities.setCapability("app", WIRE_APP_FOLDER + WIRE_APP_PATH);
        capabilities.setCapability("debugConnectToRunningApp", "true");
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWinDriver> callableWinDriver = () -> new ZetaWinDriver(new URL(WINIUM_URL), capabilities);
        return pool.submit(callableWinDriver);
    }

    private static void setExtendedLoggingLevel(DesiredCapabilities capabilities, String loggingLevelName) {
        final LoggingPreferences logs = new LoggingPreferences();
        // set it to SEVERE by default
        Level level = Level.ALL;
        try {
            level = Level.parse(loggingLevelName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Just continue with the default logging level
        }
        logs.enable(LogType.BROWSER, level);
        // logs.enable(LogType.CLIENT, Level.ALL);
        // logs.enable(LogType.DRIVER, Level.ALL);
        // logs.enable(LogType.PERFORMANCE, Level.ALL);
        // logs.enable(LogType.PROFILER, Level.ALL);
        // logs.enable(LogType.SERVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        log.debug("Browser logging level has been set to '" + level.getName() + "'");
    }

    public void startApp() throws Exception {
        Future<ZetaWinDriver> winDriverFuture;
        Future<ZetaWebAppDriver> webDriverFuture;

        LOG.debug("Create Win Driver");
        winDriverFuture = createWinDriver();
        LOG.debug("Init Win Driver");
        final ZetaWinDriver winDriver = winDriverFuture.get();
        LOG.debug("Create Chrome Driver");
        webDriverFuture = createWebDriver(winDriverFuture);
        LOG.debug("Init Chrome Driver");
        final ZetaWebAppDriver webappDriver = webDriverFuture.get();
        
        // reducing the timeout to fail fast with
        // "Timed out receiving message from renderer" on endless spinner
        webappDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
        webappDriver.manage().timeouts().setScriptTimeout(4, TimeUnit.MINUTES);
        webappDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        LOG.debug("Waiting for App to be started");
        
        LOG.debug("Setting formatter");
        ZetaFormatter.setLazyDriver(winDriverFuture);
        
        webContext = new TestContext(testname, webDriverFuture);
        wrapperContext = webContext.fromPrimaryContext(winDriverFuture, new WinPagesCollection());
        LOG.debug("Setting first Win Page");
        wrapperContext.getPagesCollection(WinPagesCollection.class).setFirstPage(new MainWirePage(winDriverFuture));
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(MainWirePage.class).focusApp();
        LOG.debug("Setting first Webapp Page");
        webContext.getPagesCollection().setFirstPage(new RegistrationPage(webDriverFuture));
    }

    public void setUp(String testname) throws Exception {
        this.testname = testname;
        try {
            killAllApps();
            if (!KEEP_DATABASE) {
                clearAppData();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        startApp();
    }

    public void tearDown(com.wire.picklejar.gherkin.model.Scenario scenario) throws Exception {
        try {
            Set<String> tagSet = scenario.getTags().stream()
                    .map((tag) -> tag.getName())
                    .collect(Collectors.toSet());
            TestrailSyncUtilities.syncExecutedScenarioWithTestrail(scenario.getName(),
                    new BasicScenarioResultToTestrailTransformer(mapScenario(scenario)).transform(), tagSet);
        } catch (Exception e) {
            log.warn(e);
        }
        tearDown();
    }

    private Map<String, String> mapScenario(com.wire.picklejar.gherkin.model.Scenario scenario) {
        Map<String, String> stepResultMap = new LinkedHashMap<>();
        for (Step step : scenario.getSteps()) {
            stepResultMap.put(step.getName(), step.getResult().getStatus());
        }
        return stepResultMap;
    }

    public void tearDown() throws Exception {
        try {
            ZetaWebAppDriver driver = (ZetaWebAppDriver) webContext.getDriver();
            // save browser console if possible
            if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
                writeBrowserLogsIntoMainLog(webContext);
            }
            if (driver instanceof ZetaWebAppDriver) {
                // logout with JavaScript because otherwise backend will block
                // us because of top many login requests
                String logoutScript = "(typeof wire !== 'undefined') && wire.auth.repository.logout();";
                driver.executeScript(logoutScript);
            }
        } catch (Exception e) {
            log.warn(e);
        } finally {
            try {
                log.debug("Releasing devices");
                log.debug(webContext.getUserManager().getCreatedUsers());
                webContext.getDeviceManager().releaseDevicesOfUsers(webContext.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Closing webdriver");
                webContext.getDriver().quit();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Closing windriver");
                wrapperContext.getDriver().quit();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Cleaning up calling instances");
                webContext.getCallingManager().cleanup();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Clearing pages collection");
                webContext.getPagesCollection().clearAllPages();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Resetting users");
                webContext.getUserManager().resetUsers();
            } catch (Exception e) {
                log.warn(e);
            }
        }
        
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }

    private void writeBrowserLogsIntoMainLog(TestContext context) throws InterruptedException, ExecutionException, TimeoutException {
        List<LogEntry> logEntries = context.getBrowserLog();
        if (!logEntries.isEmpty()) {
            log.debug("BROWSER CONSOLE LOGS:");
            for (LogEntry logEntry : logEntries) {
                log.debug(logEntry.getMessage().replaceAll("^.*z\\.", "z\\."));
            }
            log.debug("--- END OF LOG ---");
        }
    }
}
