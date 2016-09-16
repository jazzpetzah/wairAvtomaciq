package com.wearezeta.auto.common.driver;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.facebook_ios_driver.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.rest.RESTError;
import io.appium.java_client.MobileCommand;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;


public class ZetaIOSDriver extends IOSDriver<WebElement> implements ZetaDriver, FindsByFBPredicate,
        FindsByFBAccessibilityId, FindsByFBXPath, FindsByFBClassName {
    public static final long MAX_COMMAND_DURATION_MILLIS = 150000;

    private static final Logger log = ZetaLogger.getLog(ZetaIOSDriver.class.getSimpleName());

    private volatile boolean isSessionLost = false;
    private FBDriverAPI fbDriverAPI;

    public ZetaIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        this.fbDriverAPI = new FBDriverAPI();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        try {
            if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
                final Object result = takeFullScreenShot();
                final String base64EncodedPng = new String((byte[]) result);
                return outputType.convertFromBase64Png(base64EncodedPng);
            }
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
        return super.getScreenshotAs(outputType);
    }

    private byte[] takeFullScreenShot() throws Exception {
        File result = File.createTempFile("tmp", ".png", null);
        byte[] output;
        try {
            CommonUtils.executeUIShellScript(
                    new String[]{
                            String.format("%s/simshot \"%s\"", CommonUtils.getIOSToolsRoot(CommonUtils.class),
                                    result.getCanonicalPath())
                    }).get(CommonUtils.SCREENSHOT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            output = Base64.encodeBase64(FileUtils.readFileToByteArray(result));
        } finally {
            //noinspection ResultOfMethodCallIgnored
            result.delete();
        }
        return output;
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
        if (this.isSessionLost() && !driverCommand.equals(DriverCommand.SCREENSHOT)) {
            throw new IllegalStateException(
                    String.format("Appium session is dead. Skipping execution of '%s' command...", driverCommand));
        }

        final Callable<Response> task = () -> super.execute(driverCommand, parameters);
        final Future<Response> future = getPool().submit(task);
        try {
            return future.get(MAX_COMMAND_DURATION_MILLIS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (driverCommand.equals(MobileCommand.HIDE_KEYBOARD) && (e.getCause() instanceof WebDriverException)) {
                    log.debug("The keyboard seems to be already hidden.");
                    final Response response = new Response();
                    response.setSessionId(this.getSessionId().toString());
                    response.setStatus(HttpStatus.SC_OK);
                    return response;
                }
                if (isSessionLostBecause(e.getCause())) {
                    if (!isSessionLost()) {
                        try {
                            super.execute(DriverCommand.QUIT);
                        } catch (Exception eq) {
                            // ignore
                        } finally {
                            setSessionLost(true);
                        }
                    }
                }
                if (e.getCause() instanceof WebDriverException) {
                    throw (WebDriverException) e.getCause();
                } else {
                    throw new WebDriverException(e.getCause());
                }
            } else {
                if (e instanceof TimeoutException) {
                    if (!isSessionLost()) {
                        try {
                            super.execute(DriverCommand.QUIT);
                        } catch (Exception eq) {
                            // ignore
                        } finally {
                            setSessionLost(true);
                        }
                    }
                }
                throw new WebDriverException(e);
            }
        }
    }

    @Override
    public FBElement findElementByFBPredicate(String value) {
        try {
            return fbDriverAPI.findElementByFBPredicate(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using predicate '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBPredicate(String value) {
        try {
            return fbDriverAPI.findElementsByFBPredicate(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findElementByFBAccessibilityId(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using accessibility id '%s'",
                    FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findElementsByFBAccessibilityId(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBClassName(String value) {
        try {
            return fbDriverAPI.findElementByFBClassName(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using class name '%s'",
                    FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBClassName(String value) {
        try {
            return fbDriverAPI.findElementsByFBClassName(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBXPath(String value) {
        try {
            return fbDriverAPI.findElementByFBXPath(value)
                    .orElseThrow(() -> new NotFoundException(String.format("Cannot find %s using XPath '%s'",
                            FBElement.class.getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBXPath(String value) {
        try {
            return fbDriverAPI.findElementsByFBXPath(value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }
}
