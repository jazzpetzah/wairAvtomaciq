package com.wearezeta.auto.osx.common;

import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaOSXWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rc.BasicScenarioResultToTestrailTransformer;
import com.wearezeta.auto.common.testrail.TestrailSyncUtilities;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAddressbookPermission;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAppData;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.startAppium4Mac;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.APPIUM_HUB_URL;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.KEEP_DATABASE;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.WIRE_APP_PATH;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wire.picklejar.gherkin.model.Step;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Lifecycle {

    private static final Logger LOG = ZetaLogger.getLog(Lifecycle.class.getName());

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

    private Future<ZetaWebAppDriver> createWebDriver(Future<ZetaOSXDriver> osxDriver) throws IOException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-web-security");
        options.addArguments("env=" + OSXExecutionContext.ENV_URL);
        options.addArguments("enable-logging");
        options.setBinary(WIRE_APP_PATH + OSXExecutionContext.ELECTRON_SUFFIX);

        // allow skipping the security prompt for notifications in chrome 46++
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.notifications", 1);
        options.setExperimentalOption("prefs", prefs);

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("platformName", OSXExecutionContext.CURRENT_SECONDARY_PLATFORM.name());

        // Set log level to ALL for browser logs
        final LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);

        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(OSXExecutionContext.CHROMEDRIVER_PATH))
                .usingAnyFreePort().build();
        service.start();
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = () -> new ZetaOSXWebAppDriver(
                service.getUrl(), capabilities, osxDriver.get());

        return pool.submit(callableWebAppDriver);
    }

    private Future<ZetaOSXDriver> createOSXDriver() throws MalformedURLException {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(CapabilityType.PLATFORM, "MAC");
        capabilities.setCapability("platformName", "Mac");

        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaOSXDriver> callableOSXDriver = () -> {
            ZetaOSXDriver zetaOSXDriver = new ZetaOSXDriver(new URL(APPIUM_HUB_URL), capabilities);
            // necessary for calculating the size of the window etc. because this is not supported by AppiumForMac
            zetaOSXDriver.setWindowLocator(By.xpath(OSXLocators.MainWirePage.xpathWindow));
            return zetaOSXDriver;
        };
        return pool.submit(callableOSXDriver);
    }

    public void startApp() throws Exception {
        Future<ZetaOSXDriver> osxDriverFuture;
        Future<ZetaWebAppDriver> webDriverFuture;

        LOG.debug("Create OS X Driver");
        osxDriverFuture = createOSXDriver();
        LOG.debug("Init OS X Driver");
        final ZetaOSXDriver osxDriver = osxDriverFuture.get();
        LOG.debug("Create Chrome Driver");
        webDriverFuture = createWebDriver(osxDriverFuture);
        LOG.debug("Init Chrome Driver");
        final ZetaWebAppDriver webappDriver = webDriverFuture.get();
        LOG.debug("Setting formatter");
        ZetaFormatter.setLazyDriver(osxDriverFuture);
        webContext = new TestContext(testname, webDriverFuture);
        wrapperContext = webContext.fromPrimaryContext(osxDriverFuture, new OSXPagesCollection());
        LOG.debug("Setting first OS X Page");
        wrapperContext.getPagesCollection(OSXPagesCollection.class).setFirstPage(new MainWirePage(osxDriverFuture));
        LOG.debug("Setting first Webapp Page");
        webContext.getPagesCollection().setFirstPage(new RegistrationPage(webDriverFuture));
        LOG.debug("Opening app");
        // necessary to enable the driver
        wrapperContext.getDriver().navigate().to(WIRE_APP_PATH);
    }

    public void setUp(String testname) throws Exception {
        this.testname = testname;
        try {
            startAppium4Mac();
            killAllApps();
            if (!KEEP_DATABASE) {
                clearAppData();
            }
            clearAddressbookPermission();
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
            LOG.warn(e);
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
                webContext.getLogManager().printBrowserLog();
            }
            if (driver instanceof ZetaWebAppDriver) {
                // logout with JavaScript because otherwise backend will block
                // us because of top many login requests
                String logoutScript = "(typeof wire !== 'undefined') && wire.auth.repository.logout();";
                driver.executeScript(logoutScript);
            }
        } catch (Exception e) {
            LOG.warn(e);
        } finally {
            try {
                LOG.debug("Releasing devices");
                LOG.debug(webContext.getUserManager().getCreatedUsers());
                webContext.getDeviceManager().releaseDevicesOfUsers(webContext.getUserManager().getCreatedUsers());
            } catch (Exception e) {
                LOG.warn(e);
            }
            try {
                LOG.debug("Closing webdriver");
                webContext.getDriver().quit();
            } catch (Exception e) {
                LOG.warn(e);
            }
            try {
                LOG.debug("Closing osxdriver");
                wrapperContext.getDriver().quit();
            } catch (Exception e) {
                LOG.warn(e);
            }
            try {
                LOG.debug("Cleaning up calling instances");
                webContext.getCallingManager().cleanup();
            } catch (Exception e) {
                LOG.warn(e);
            }
            try {
                LOG.debug("Clearing pages collection");
                webContext.getPagesCollection().clearAllPages();
                wrapperContext.getPagesCollection().clearAllPages();
            } catch (Exception e) {
                LOG.warn(e);
            }
            try {
                LOG.debug("Resetting users");
                webContext.getUserManager().resetUsers();
            } catch (Exception e) {
                LOG.warn(e);
            }
        }

        if (service != null && service.isRunning()) {
            service.stop();
        }
    }
}
