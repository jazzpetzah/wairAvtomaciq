package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.rc.BasicScenarioResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.steps.CommonWebAppSteps;
import static com.wearezeta.auto.web.steps.CommonWebAppSteps.log;
import com.wire.picklejar.gherkin.model.Step;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Lifecycle {

    public static final int SAFARI_DRIVER_CREATION_RETRY = 3;
    public static final long DRIVER_INIT_TIMEOUT = 60 * 1000;
    private TestContext context;
    private TestContext compatContext;

    /**
     * The context is fully initialized after setting up the testcase
     *
     * @return
     */
    public TestContext getContext() {
        return context;
    }

    // #### START ############################################################ COMPATIBILITY INSTRUCTIONS
    @Before("~@performance")
    public void setUp(Scenario scenario) throws Exception {
        String id = scenario.getId().substring(
                scenario.getId().lastIndexOf(";") + 1);
        setUp(scenario.getName() + "_" + id);
    }
    @After
    public void tearDown(Scenario scenario) throws Exception {
        tearDown();
    }
    // #### END ############################################################## COMPATIBILITY INSTRUCTIONS

    public void setUp(String testname) throws Exception {

        String[] command = new String[]{"/bin/sh", "-c", String.format("killall %s", "grunt")};
        log.debug("Kill all grunt processes: " + Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);
        int returnCode = process.waitFor();
        log.debug("Process exited with: " + returnCode);

        String platform = WebAppExecutionContext.getPlatform();
        String osName = WebAppExecutionContext.getOsName();
        String osVersion = WebAppExecutionContext.getOsVersion();
        String browserName = WebAppExecutionContext.getBrowserName();
        String browserVersion = WebAppExecutionContext.getBrowserVersion();

        String uniqueTestname = getUniqueTestName(testname);
        log.debug("Unique name for this test: " + uniqueTestname);

        // get custom capabilities
        DesiredCapabilities capabilities = getCustomCapabilities(platform,
                osName, osVersion, browserName, browserVersion, uniqueTestname);

        final String hubHost = System.getProperty("hubHost");
        final String hubPort = System.getProperty("hubPort");
        final String url = CommonUtils
                .getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = new Callable<ZetaWebAppDriver>() {

            @Override
            public ZetaWebAppDriver call() throws Exception {
                ZetaWebAppDriver lazyWebDriver = null;
                URL hubUrl = new URL("http://" + hubHost + ":" + hubPort
                        + "/wd/hub");
                if (WebAppExecutionContext.getBrowser().equals(Browser.Safari)) {
                    int retries = SAFARI_DRIVER_CREATION_RETRY;
                    boolean failed = false;
                    do {
                        try {
                            retries--;
                            // wait for safari to close properly before starting
                            // it for
                            // a new test
                            Thread.sleep(5000);
                            lazyWebDriver = new ZetaWebAppDriver(hubUrl,
                                    capabilities);
                            failed = false;
                        } catch (WebDriverException e) {
                            log.warn("Safari driver init failed - retrying", e);
                            failed = true;
                        }
                    } while (failed && retries > 0);

                    if (failed) {
                        throw new Exception("Failed to init Safari driver");
                    }
                } else {
                    lazyWebDriver = new ZetaWebAppDriver(hubUrl, capabilities);
                }
                // setup of the browser
                lazyWebDriver.setFileDetector(new LocalFileDetector());
                if (WebAppExecutionContext.getBrowser().equals(Browser.Safari)) {
                    WebCommonUtils.closeAllAdditionalTabsInSafari(lazyWebDriver
                            .getNodeIp());
                    WebCommonUtils.clearHistoryInSafari(lazyWebDriver
                            .getNodeIp());
                }
                if (WebAppExecutionContext.getBrowser()
                        .isSupportingMaximizingTheWindow()) {
                    lazyWebDriver.manage().window().maximize();
                } else if (WebAppExecutionContext.getBrowser()
                        .isSupportingSettingWindowSize()) {
                    // http://stackoverflow.com/questions/14373371/ie-is-continously-maximizing-and-minimizing-when-test-suite-executes
                    lazyWebDriver
                            .manage()
                            .window()
                            .setSize(
                                    new Dimension(
                                            WebAppConstants.MIN_WEBAPP_WINDOW_WIDTH,
                                            WebAppConstants.MIN_WEBAPP_WINDOW_HEIGHT));
                }
                return lazyWebDriver;
            }
        };

        final Future<ZetaWebAppDriver> lazyWebDriver = pool.submit(callableWebAppDriver);

        /**
         * #### START ############################################################ COMPATIBILITY INSTRUCTIONS
         */
        TestContext.COMPAT_WEB_DRIVER = lazyWebDriver;
        compatContext = new TestContext();
        compatContext.getDriver().get(url);
        compatContext.getPagesCollection().setFirstPage(new RegistrationPage(lazyWebDriver, url));
        /**
         * #### END ############################################################## COMPATIBILITY INSTRUCTIONS
         */

        context = new TestContext(testname, lazyWebDriver);
        context.getDriver().get(url);
        context.getPagesCollection().setFirstPage(new RegistrationPage(lazyWebDriver, url));

        ZetaFormatter.setLazyDriver(lazyWebDriver);
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
    
    private Map<String, String> mapScenario(com.wire.picklejar.gherkin.model.Scenario scenario){
        Map<String, String> stepResultMap = new LinkedHashMap<>();
        for (Step step : scenario.getSteps()) {
            stepResultMap.put(step.getName(), step.getResult().getStatus());
        }
        return stepResultMap;
    }

    public void tearDown() {
        try {
            ZetaWebAppDriver driver = (ZetaWebAppDriver) context.getDriver();
            // save browser console if possible
            if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
                writeBrowserLogsIntoMainLog(driver);
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
            /**
             * #### START ############################################################ COMPATIBILITY INSTRUCTIONS
             */
            try {
                log.debug("COMPAT: Releasing devices");
                log.debug(compatContext.getUserManager().getCreatedUsers());
                compatContext.getDeviceManager().releaseDevicesOfUsers(compatContext.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                log.warn(e);
            }
            /**
             * #### END ############################################################## COMPATIBILITY INSTRUCTIONS
             */
            try {
                log.debug("Releasing devices");
                log.debug(context.getUserManager().getCreatedUsers());
                context.getDeviceManager().releaseDevicesOfUsers(context.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Closing webdriver");
                context.getDriver().quit();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Cleaning up calling instances");
                context.getCallingManager().cleanup();
                log.debug("COMPAT: Cleaning up calling instances");
                compatContext.getCallingManager().cleanup();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Clearing pages collection");
                context.getPagesCollection().clearAllPages();
                log.debug("COMPAT: Clearing pages collection");
                compatContext.getPagesCollection().clearAllPages();
                WebPage.clearPagesCollection();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Resetting users");
                context.getUserManager().resetUsers();
                log.debug("COMPAT: Resetting users");
                compatContext.getUserManager().resetUsers();
            } catch (Exception e) {
                log.warn(e);
            }
        }
    }

    private void writeBrowserLogsIntoMainLog(RemoteWebDriver driver) {
        List<LogEntry> logEntries = getBrowserLog(driver);
        if (!logEntries.isEmpty()) {
            log.debug("BROWSER CONSOLE LOGS:");
            for (LogEntry logEntry : logEntries) {
                log.debug(logEntry.getMessage().replaceAll("^.*z\\.", "z\\."));
            }
            log.debug("--- END OF LOG ---");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<LogEntry> getBrowserLog(RemoteWebDriver driver) {
        return IteratorUtils.toList((Iterator<LogEntry>) driver.manage().logs().get(LogType.BROWSER).iterator());
    }

    private static void setCustomChromeProfile(DesiredCapabilities capabilities)
            throws Exception {
        ChromeOptions options = new ChromeOptions();
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");

        // allow skipping the security prompt for notifications in chrome 46++
        Map<String, Object> prefs = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        Map<String, Object> contentSettings = new HashMap<>();
        contentSettings.put("notifications", 1);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        // prefs.put("profile.managed_default_content_settings.notifications",
        // 1);
        options.setExperimentalOption("prefs", prefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    private static void setCustomOperaProfile(DesiredCapabilities capabilities)
            throws Exception {
        final String userProfileRoot = WebCommonUtils.getOperaProfileRoot();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + userProfileRoot);
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    private static void setCustomFirefoxProfile(DesiredCapabilities capabilities) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("dom.webnotifications.enabled", false);
        // allow skipping the security prompt for sharing the media device
        profile.setPreference("media.navigator.permission.disabled", true);
        // do not store any form data automatically
        profile.setPreference("signon.autofillForms", false);
        profile.setPreference("signon.rememberSignons", false);
        profile.setPreference("signon.storeWhenAutocompleteOff", false);
        capabilities.setCapability("firefox_profile", profile);
        capabilities.setCapability("marionette", true);
    }

    private static void setCustomSafariProfile(DesiredCapabilities capabilities) {
        SafariOptions options = new SafariOptions();
        options.setUseCleanSession(true);
        capabilities.setCapability(SafariOptions.CAPABILITY, options);
    }

    private static void setCustomIEProfile(DesiredCapabilities capabilities) {
        capabilities.setCapability("ie.ensureCleanSession", true);
    }

    private static void setExtendedLoggingLevel(
            DesiredCapabilities capabilities, String loggingLevelName) {
        final LoggingPreferences logs = new LoggingPreferences();
        // set it to SEVERE by default
        Level level = Level.SEVERE;
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
        log.debug("Browser logging level has been set to '" + level.getName()
                + "'");
    }

    private String getUniqueTestName(String testname) {
        String browserName = WebAppExecutionContext.getBrowserName();
        String browserVersion = WebAppExecutionContext.getBrowserVersion();
        String platform = WebAppExecutionContext.getPlatform();

        return testname + " on " + platform + " with "
                + browserName + " " + browserVersion;
    }

    private static DesiredCapabilities getCustomCapabilities(String platform,
            String osName, String osVersion, String browserName,
            String browserVersion, String uniqueTestName) throws Exception {
        final DesiredCapabilities capabilities;
        Browser browser = Browser.fromString(browserName);
        switch (browser) {
            case Chrome:
                capabilities = DesiredCapabilities.chrome();
                setCustomChromeProfile(capabilities);
                break;
            case Opera:
                capabilities = DesiredCapabilities.chrome();
                setCustomOperaProfile(capabilities);
                break;
            case Firefox:
                capabilities = DesiredCapabilities.firefox();
                setCustomFirefoxProfile(capabilities);
                break;
            case Safari:
                capabilities = DesiredCapabilities.safari();
                setCustomSafariProfile(capabilities);
                break;
            case InternetExplorer:
                capabilities = DesiredCapabilities.internetExplorer();
                setCustomIEProfile(capabilities);
                break;
            case Edge:
                capabilities = DesiredCapabilities.edge();
                break;
            default:
                throw new NotImplementedException(
                        "Unsupported/incorrect browser name is set: " + browserName);
        }

        if (browser.isSupportingConsoleLogManagement()) {
            setExtendedLoggingLevel(
                    capabilities,
                    WebCommonUtils
                    .getExtendedLoggingLevelInConfig(CommonCallingSteps2.class));
        }

        capabilities.setCapability("platform", platform);
        capabilities.setCapability("version", browserVersion);
        capabilities.setCapability("os", osName);
        capabilities.setCapability("os_version", osVersion);
        capabilities.setCapability("browser_version", browserVersion);
        capabilities.setCapability("name", uniqueTestName);
        capabilities.setCapability("resolution", "1280x1024");
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("project", System.getenv("JOB_NAME"));
        capabilities.setCapability("build", System.getenv("BUILD_NUMBER"));

        return capabilities;
    }
}
