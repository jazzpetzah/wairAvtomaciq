package com.wearezeta.auto.common.driver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.facebook_ios_driver.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.*;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.security.Credentials;


public class ZetaIOSDriver extends IOSDriver<WebElement> implements ZetaDriver, FindsByFBPredicate,
        FindsByFBAccessibilityId, FindsByFBXPath, FindsByFBClassName {
    public static final long MAX_COMMAND_DURATION_MILLIS = 90000;
    public static final long MAX_SESSION_INIT_DURATION_MILLIS = 120000;
    public static final long MAX_GET_SOURCE_DURATION_MILLIS = 15000;

    public static final String AUTOMATION_NAME_CAPABILITY_NAME = "automationName";
    public static final String AUTOMATION_MODE_XCUITEST = "XCUITest";

    private static final Logger log = ZetaLogger.getLog(ZetaIOSDriver.class.getSimpleName());

    private volatile boolean isSessionLost = false;
    private FBDriverAPI fbDriverAPI;
    private String osVersion;

    public ZetaIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        if (isRealDevice()) {
            verifyLockdownPermissions();
        }
        initOSVersionString();
        this.fbDriverAPI = new FBDriverAPI();
    }

    public boolean isRealDevice() {
        return this.getCapabilities().getCapability("udid") != null;
    }

    private FBDriverAPI getFbDriverAPI() {
        if (isSessionLost()) {
            throw new IllegalStateException("Appium session is dead. No more commands can be scheduled.");
        }
        return this.fbDriverAPI;
    }

    private static final File LOCKDOWN = new File("/var/db/lockdown");

    private static void verifyLockdownPermissions() {
        final Set<PosixFilePermission> perms;
        try {
            perms = Files.getPosixFilePermissions(LOCKDOWN.toPath());
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
        if (!perms.containsAll(Arrays.asList(
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OTHERS_WRITE,
                PosixFilePermission.OTHERS_EXECUTE))) {
            throw new IllegalStateException(String.format(
                    "You cannot perform real device tests unless %s has no RWX permissions. " +
                            "Execute `sudo chmod 777 %s` to fix the problem and restart the test`",
                    LOCKDOWN.getAbsolutePath(), LOCKDOWN.getAbsolutePath()
            ));
        }
    }

    public DefaultArtifactVersion getOSVersion() {
        return new DefaultArtifactVersion(this.osVersion);
    }

    private void initOSVersionString() {
        this.osVersion = (String) getCapabilities().getCapability("platformVersion");
    }

    @Override
    public boolean isSessionLost() {
        return this.isSessionLost;
    }

    private void setSessionLost(boolean isSessionLost) {
        if (isSessionLost != this.isSessionLost) {
            log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
            log.debug(LOG_DECORATION_PREFIX + "\n" + AppiumServer.getInstance().getLog().orElse("")
                    + "\n" + LOG_DECORATION_SUFFIX);
        }
        this.isSessionLost = isSessionLost;
    }

    private ExecutorService pool;

    private synchronized ExecutorService getPool() {
        if (this.pool == null) {
            this.pool = Executors.newFixedThreadPool(1);
        }
        return this.pool;
    }

    private boolean isSessionLostBecause(Throwable e) {
        return (e instanceof UnreachableBrowserException) || (e instanceof SessionNotFoundException);
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
    }

    private static final String LOG_DECORATION_PREFIX = "*************APPIUM SERVER LOG START**************";
    private static final String LOG_DECORATION_SUFFIX = "*************APPIUM SERVER LOG END****************";

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        if (this.isSessionLost()) {
            throw new IllegalStateException(
                    String.format("Appium session is dead. Skipping execution of '%s' command...", driverCommand));
        }

        final Callable<Response> task = () -> super.execute(driverCommand, parameters);
        final Future<Response> future = getPool().submit(task);
        long timeout = MAX_COMMAND_DURATION_MILLIS;
        switch (driverCommand) {
            case DriverCommand.GET_PAGE_SOURCE:
                timeout = MAX_GET_SOURCE_DURATION_MILLIS;
                break;
            case DriverCommand.NEW_SESSION:
                timeout = MAX_SESSION_INIT_DURATION_MILLIS;
                break;
        }
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (isSessionLostBecause(e.getCause())) {
                    setSessionLost(true);
                }
                if (e.getCause() instanceof WebDriverException) {
                    throw (WebDriverException) e.getCause();
                } else {
                    throw new WebDriverException(e.getCause());
                }
            } else {
                if (e instanceof TimeoutException) {
                    setSessionLost(true);
                }
                throw new WebDriverException(e);
            }
        }
    }

    @Override
    public FBElement findElementByFBPredicate(String value) {
        try {
            return getFbDriverAPI().findElementByFBPredicate(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using predicate '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBPredicate(String value) {
        try {
            return getFbDriverAPI().findElementsByFBPredicate(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBAccessibilityId(String value) {
        try {
            return getFbDriverAPI().findElementByFBAccessibilityId(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using accessibility id '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBAccessibilityId(String value) {
        try {
            return getFbDriverAPI().findElementsByFBAccessibilityId(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBClassName(String value) {
        try {
            return getFbDriverAPI().findElementByFBClassName(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using class name '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBClassName(String value) {
        try {
            return getFbDriverAPI().findElementsByFBClassName(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBXPath(String value) {
        try {
            return getFbDriverAPI().findElementByFBXPath(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using XPath '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBXPath(String value) {
        try {
            return getFbDriverAPI().findElementsByFBXPath(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public void runAppInBackground(int seconds) {
        try {
            getFbDriverAPI().deactivateApp(seconds);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void forceAcceptAlert() {
        try {
            this.fbDriverAPI.acceptAlert();
        } catch (Exception e) {
            // just ignore
        }
    }

    protected void acceptAlert() {
        try {
            getFbDriverAPI().acceptAlert();
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    protected void dismissAlert() {
        try {
            getFbDriverAPI().dismissAlert();
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    protected String getAlertText() {
        try {
            return getFbDriverAPI().getAlertText();
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public Options manage() {
        return new ZetaRemoteWebDriverOptions();
    }

    protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {
        @Override
        public WebDriver.Window window() {
            return new ZetaRemoteWindow();
        }

        protected class ZetaRemoteWindow extends RemoteWindow {
            public ZetaRemoteWindow() {

            }

            @Override
            public Dimension getSize() {
                try {
                    final Dimension originalDimension = FBElement.apiStringToDimension(
                            getFbDriverAPI().getWindowSize(CommonUtils.generateGUID().toUpperCase())
                    );
                    // FIXME: workaround for webdriver bug https://github.com/facebook/WebDriverAgent/issues/303
                    if (ZetaIOSDriver.this.getOrientation() == ScreenOrientation.LANDSCAPE &&
                            originalDimension.getHeight() > originalDimension.getWidth()) {
                        return new Dimension(originalDimension.getHeight(), originalDimension.getWidth());
                    } else {
                        return originalDimension;
                    }
                } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
                    throw new WebDriverException(e);
                }
            }

            @Override
            public Point getPosition() {
                return new Point(0, 0);
            }
        }
    }

    public TargetLocator switchTo() {
        return new InnerTargetLocator();
    }

    class IOSAlert implements Alert {
        @Override
        public void dismiss() {
            ZetaIOSDriver.this.dismissAlert();
        }

        @Override
        public void accept() {
            ZetaIOSDriver.this.acceptAlert();
        }

        @Override
        public String getText() {
            return ZetaIOSDriver.this.getAlertText();
        }

        @Override
        public void sendKeys(String s) {
            throw new IllegalStateException("IOS alerts don't support sending keys");
        }

        @Override
        public void setCredentials(Credentials credentials) {
            throw new IllegalStateException("IOS alerts don't support settings credentials");
        }

        @Override
        public void authenticateUsing(Credentials credentials) {
            throw new IllegalStateException("IOS alerts don't support authentication");
        }
    }

    private class InnerTargetLocator extends RemoteTargetLocator {
        private InnerTargetLocator() {
        }

        public Alert alert() {
            return new IOSAlert();
        }
    }

    public void tapScreenAt(int x, int y) {
        try {
            getFbDriverAPI().tap(x, y);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void doubleTapScreenAt(int x, int y) {
        try {
            getFbDriverAPI().doubleTap(x, y);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void longTapScreenAt(int x, int y, double durationSeconds) {
        try {
            getFbDriverAPI().touchAndHold(x, y, durationSeconds);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void pressHomeButton() {
        try {
            getFbDriverAPI().switchToHomescreen();
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }
}
