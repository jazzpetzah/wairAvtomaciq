package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.BasicScenarioResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wire.picklejar.gherkin.model.Scenario;
import com.wire.picklejar.gherkin.model.Step;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.safari.SafariOptions;

public class Lifecycle {

    private static final Logger log = ZetaLogger.getLog(Lifecycle.class.getSimpleName());
    private static final int MIN_WEBAPP_WINDOW_WIDTH = 1366;
    private static final int MIN_WEBAPP_WINDOW_HEIGHT = 768;
    private static final int SAFARI_DRIVER_CREATION_RETRY = 3;
    
    private boolean DEBUG = false;
    private WebAppTestContext context;

    public WebAppTestContext getContext() {
        return context;
    }

    public void setUp(Scenario scenario) throws Exception {
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

        String uniqueTestname = getUniqueTestName(scenario.getName());
        log.debug("Unique name for this test: " + uniqueTestname);

        // get custom capabilities
        DesiredCapabilities capabilities = getCustomCapabilities(platform,
                osName, osVersion, browserName, browserVersion, uniqueTestname);

        final String hubHost = System.getProperty("hubHost");
        final String hubPort = System.getProperty("hubPort");
        final String url = CommonUtils.getWebAppApplicationPathFromConfig(Lifecycle.class);
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = new Callable<ZetaWebAppDriver>() {

            @Override
            public ZetaWebAppDriver call() throws Exception {
                ZetaWebAppDriver lazyWebDriver = null;
                URL hubUrl = new URL("http://" + hubHost + ":" + hubPort + "/wd/hub");
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
                            if (DEBUG) {
                                lazyWebDriver = (ZetaWebAppDriver) new DebugWebAppDriver(hubUrl, capabilities);
                            } else {
                                lazyWebDriver = new ZetaWebAppDriver(hubUrl, capabilities);
                            }
                            failed = false;
                        } catch (WebDriverException e) {
                            log.warn("Safari driver init failed - retrying", e);
                            failed = true;
                        }
                    } while (failed && retries > 0);

                    if (failed) {
                        throw new Exception("Failed to init Safari driver");
                    }
                } else if (DEBUG) {
                    lazyWebDriver = (ZetaWebAppDriver) new DebugWebAppDriver(hubUrl, capabilities);
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
                                    new Dimension(MIN_WEBAPP_WINDOW_WIDTH, MIN_WEBAPP_WINDOW_HEIGHT));
                }
                return lazyWebDriver;
            }
        };

        final Future<ZetaWebAppDriver> lazyWebDriver = pool.submit(callableWebAppDriver);

        context = new WebAppTestContext(scenario.getName(), lazyWebDriver);
        context.getDriver().get(url);
        context.getPagesCollection().setFirstPage(new RegistrationPage(lazyWebDriver, url));
    }

    public void tearDown(Scenario scenario) throws Exception {
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

    private Map<String, String> mapScenario(Scenario scenario) {
        Map<String, String> stepResultMap = new LinkedHashMap<>();
        for (Step step : scenario.getSteps()) {
            stepResultMap.put(step.getName(), step.getResult().getStatus());
        }
        return stepResultMap;
    }

    public void tearDown() throws Exception {
        try {
            ZetaWebAppDriver driver = (ZetaWebAppDriver) context.getDriver();
            if (WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
                context.getLogManager().printBrowserLog();
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
                log.debug(context.getUsersManager().getCreatedUsers());
                context.getDevicesManager().releaseDevicesOfUsers(context.getUsersManager().getCreatedUsers());
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
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Clearing pages collection");
                context.getPagesCollection().clearAllPages();
            } catch (Exception e) {
                log.warn(e);
            }
            try {
                log.debug("Resetting users");
                context.getUsersManager().resetUsers();
            } catch (Exception e) {
                log.warn(e);
            }
        }
    }

    private static void setCustomChromeProfile(DesiredCapabilities capabilities) throws Exception {
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

    private static void setCustomOperaProfile(DesiredCapabilities capabilities) throws Exception {
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
    }

    private static void setCustomSafariProfile(DesiredCapabilities capabilities) {
        SafariOptions options = new SafariOptions();
        options.setUseCleanSession(true);
        capabilities.setCapability(SafariOptions.CAPABILITY, options);
    }

    private static void setCustomIEProfile(DesiredCapabilities capabilities) {
        capabilities.setCapability("ie.ensureCleanSession", true);
    }

    private String getUniqueTestName(String testname) {
        String browserName = WebAppExecutionContext.getBrowserName();
        String browserVersion = WebAppExecutionContext.getBrowserVersion();
        String platform = WebAppExecutionContext.getPlatform();

        return testname + " on " + platform + " with " + browserName + " " + browserVersion;
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

        // Set log level to ALL for browser logs when supported
        if (browser.isSupportingConsoleLogManagement()) {
            final LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
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
