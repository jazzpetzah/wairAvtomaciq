package com.wearezeta.auto.common.driver;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.ocr.OnScreenKeyboardScanner;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.opencv.core.Rect;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZetaAndroidDriver extends AndroidDriver<WebElement> implements ZetaDriver, HasTouchScreen {
    public static final long MAX_COMMAND_DURATION_MILLIS = 120000;

    private static final Logger log = ZetaLogger.getLog(ZetaAndroidDriver.class
            .getSimpleName());

    public static String ADB_PREFIX = "";

    static {
        try {
            ADB_PREFIX = CommonUtils
                    .getAdbPrefixFromConfig(ZetaAndroidDriver.class).orElse("");
            log.info("ADB Prefix is set to " + ADB_PREFIX);
        } catch (Exception ex) {
            log.info("Could not load adb prefix - using empty prefix instead", ex);
        }
    }

    private volatile boolean isSessionLost = false;

    private RemoteTouchScreen touch;
    private String androidOSVersion;

    public enum SurfaceOrientation {

        ROTATION_0(0), ROTATION_90(1), ROTATION_180(2), ROTATION_270(3);

        final int code;

        SurfaceOrientation(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static SurfaceOrientation getByCode(int code) {
            for (SurfaceOrientation item : SurfaceOrientation.values()) {
                if (code == item.getCode()) {
                    return item;
                }
            }
            throw new NoSuchElementException(String.format(
                    "There is no SurfaceOrientation item with code '%s'", code));
        }
    }

    public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        this.touch = new RemoteTouchScreen(getExecuteMethod());
        try {
            initOSVersionString();
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public boolean isSessionLost() {
        return this.isSessionLost;
    }

    private static final String LOG_DECORATION_PREFIX = "*************APPIUM SERVER LOG START**************";
    private static final String LOG_DECORATION_SUFFIX = "*************APPIUM SERVER LOG END****************";

    private void setSessionLost(boolean isSessionLost) {
        if (isSessionLost != this.isSessionLost) {
            log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
            log.debug(LOG_DECORATION_PREFIX + "\n" + AppiumServer.getInstance().getLog().orElse("")
                    + "\n" + LOG_DECORATION_SUFFIX);
        }
        this.isSessionLost = isSessionLost;
    }

    private static int getNextCoord(double startC, double endC, double current,
                                    double duration) {
        return (int) Math.round(startC + (endC - startC) / duration * current);
    }

    public final static int SWIPE_STEP_DURATION_MILLISECONDS = 40;

    private void swipeViaTouchActions(int startx, int starty, int endx,
                                      int endy, int durationMilliseconds) {
        int duration = 1;
        if (durationMilliseconds > SWIPE_STEP_DURATION_MILLISECONDS) {
            duration = (durationMilliseconds % SWIPE_STEP_DURATION_MILLISECONDS == 0)
                    ? (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS)
                    : (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS + 1);
        }
        int current = 1;
        final TouchActions ta = new TouchActions(this);
        ta.down(startx, starty).perform();
        do {
            try {
                Thread.sleep(SWIPE_STEP_DURATION_MILLISECONDS);
            } catch (InterruptedException e) {
                Throwables.propagate(e);
            }
            ta.move(getNextCoord(startx, endx, current, duration),
                    getNextCoord(starty, endy, current, duration)).perform();
            current++;
        } while (current <= duration);
        ta.up(endx, endy).perform();
    }

    @Override
    public void swipe(int startx, int starty, int endx, int endy, int durationMilliseconds) {
        if (new DefaultArtifactVersion(androidOSVersion).compareTo(new DefaultArtifactVersion("4.3")) < 0) {
            // adb swipe command under 4.2 does not support duration parameter
            // and this fucks up all the tests
            swipeViaTouchActions(startx, starty, endx, endy, durationMilliseconds);
            return;
        }

        final String adbCommand = String.format(ADB_PREFIX
                        + "adb shell input touchscreen swipe %d %d %d %d %d", startx,
                starty, endx, endy, durationMilliseconds);
        log.debug("ADB swipe: " + adbCommand);
        try {
            Runtime.getRuntime()
                    .exec(new String[]{"/bin/bash", "-c", adbCommand})
                    .waitFor();
        } catch (Exception e) {
            throw new WebDriverException(e.getMessage(), e);
        }
    }


    public void longTap(WebElement el, int durationMilliseconds) {
        final Point location = el.getLocation();
        final Dimension size = el.getSize();
        this.longTap(location.x + size.width / 2, location.y + size.height / 2, durationMilliseconds);
    }

    public void longTap(int x, int y, int durationMilliseconds) {
        this.swipe(x, y, x, y, durationMilliseconds);
    }

    public void doubleTap(WebElement el) {
        final TouchActions ta = new TouchActions(this);
        ta.doubleTap(el).perform();
    }

    public void doubleTap(int x, int y) {
        tap(1, x, y, 50);
        try {
            //slow devices don't see 2nd tap without this delay
            Thread.sleep(200);
        } catch (Exception e) {
            throw new WebDriverException(e);
        }
        tap(1, x, y, 50);

    }

    public void tap(String tapType, int x, int y) {
        switch (tapType.toLowerCase()) {
            case "long tap":
                longTap(x, y, DriverUtils.LONG_TAP_DURATION);
                break;
            case "double tap":
                doubleTap(x, y);
                break;
            case "tap":
                tap(1, x, y, DriverUtils.SINGLE_TAP_DURATION);
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid tap type '%s'", tapType));
        }
    }

    public void tap(String tapType, WebElement el) {
        switch (tapType.toLowerCase()) {
            case "long tap":
                longTap(el, DriverUtils.LONG_TAP_DURATION);
                break;
            case "double tap":
                doubleTap(el);
                break;
            case "tap":
                el.click();
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid tap type '%s'", tapType));
        }
    }

    public DefaultArtifactVersion getOSVersion() {
        return new DefaultArtifactVersion(this.androidOSVersion);
    }

    private void initOSVersionString() throws Exception {
        this.androidOSVersion = getAdbOutput(
                "shell getprop ro.build.version.release").trim();
    }

    @Override
    public void tap(int fingers, WebElement element, int durationMilliseconds) {
        final TouchActions ta = new TouchActions(this);
        for (int i = 0; i < fingers; i++) {
            ta.singleTap(element);
        }
        ta.perform();
        try {
            Thread.sleep(durationMilliseconds);
        } catch (InterruptedException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void tap(int fingers, int x, int y, int durationMilliseconds) {
        final String adbCommand = String.format(ADB_PREFIX
                + "adb shell input touchscreen tap %d %d", x, y);
        try {
            Runtime.getRuntime().exec(
                    new String[]{"/bin/bash", "-c", adbCommand});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public TouchScreen getTouch() {
        return this.touch;
    }

    /**
     * Workaround for selendroid when it cannot take a screenshot of the screen if main app is not in foreground
     *
     * @return Selenium Response instance
     */
    private Response takeFullScreenShotWithAdb() {
        final Response result = new Response();
        File tmpScreenshot;
        try {
            tmpScreenshot = File.createTempFile("tmp", ".png");
        } catch (IOException e) {
            throw new WebDriverException(e);
        }
        try {
            CommonUtils.takeAndroidScreenshot(this, tmpScreenshot, false);
            result.setSessionId(this.getSessionId().toString());
            result.setStatus(HttpStatus.SC_OK);
            result.setValue(Base64.encodeBase64(IOUtils.toByteArray(new FileInputStream(tmpScreenshot))));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // Wrap generic error into WebDriverException
            throw new WebDriverException(e.getMessage(), e);
        } finally {
            tmpScreenshot.delete();
        }
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType)
            throws WebDriverException {
        final Object result = takeFullScreenShotWithAdb().getValue();
        final String base64EncodedPng = new String((byte[]) result);
        return outputType.convertFromBase64Png(base64EncodedPng);
    }

    private static final long DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS = 5000;
    private static final String SERVER_SIDE_ERROR_SIGNATURE = "unknown server-side error";
    private static final String NO_OPEN_WINDOWS_ERROR_SIGNATURE = "No open windows";

    private static boolean shouldRetryServerError(Throwable e) {
        return e.getMessage().contains(SERVER_SIDE_ERROR_SIGNATURE) ||
                e.getMessage().contains(NO_OPEN_WINDOWS_ERROR_SIGNATURE);
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

    /**
     * This is workaround for some Selendroid issues when driver just generates unknown error when some transition in AUT is
     * currently in progress. Retry helps
     */
    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        if (this.isSessionLost() && !driverCommand.equals(DriverCommand.SCREENSHOT)) {
            throw new IllegalStateException(
                    String.format("Appium session is dead. Skipping execution of '%s' command...", driverCommand));
        }
        final Callable<Response> task = () -> super.execute(driverCommand,
                parameters);
        final Future<Response> future = getPool().submit(task);
        try {
            return future.get(MAX_COMMAND_DURATION_MILLIS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (driverCommand.equals(HIDE_KEYBOARD_COMMAND)) {
                    log.debug("The keyboard seems to be already hidden.");
                    final Response response = new Response();
                    response.setSessionId(this.getSessionId().toString());
                    response.setStatus(HttpStatus.SC_OK);
                    return response;
                }
                if (shouldRetryServerError(e.getCause())) {
                    final long millisecondsStarted = System.currentTimeMillis();
                    while (System.currentTimeMillis() - millisecondsStarted <= DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e1) {
                            Throwables.propagate(e1);
                        }
                        try {
                            return super.execute(driverCommand, parameters);
                        } catch (WebDriverException e1) {
                            if (isSessionLostBecause(e1)) {
                                setSessionLost(true);
                            }
                            if (!shouldRetryServerError(e1)) {
                                throw e1;
                            }
                        }
                    } // while have time
                } // if getMessage contains
                log.error(String.format("Android driver is still not available after %s seconds timeout. "
                                + "The recent webdriver command was '%s'",
                        DRIVER_AVAILABILITY_TIMEOUT_MILLISECONDS / 1000, driverCommand));
                if (isSessionLostBecause(e.getCause())) {
                    setSessionLost(true);
                }
                if (e.getCause() instanceof WebDriverException) {
                    throw (WebDriverException) e.getCause();
                } else {
                    throw new WebDriverException(e.getCause());
                }
            } else {
                // if !(e instanceof ExecutionException)
                if (e instanceof TimeoutException) {
                    setSessionLost(true);
                }
                throw new WebDriverException(e);
            }
        }
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
    }

    private static String getAdbOutput(String cmdLine) throws Exception {
        final StringBuilder result = new StringBuilder();
        String adbCommand = ADB_PREFIX + "adb " + cmdLine;
        final Process process = Runtime.getRuntime().exec(
                new String[]{"/bin/bash", "-c", adbCommand});
        if (process == null) {
            throw new RuntimeException(String.format(
                    "Failed to execute command line '%s'", cmdLine));
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = in.readLine()) != null) {
                result.append(s).append("\n");
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result.toString().trim();
    }

    /**
     * @return 0 to 3. 0 is default portrait
     * @throws Exception
     */
    public SurfaceOrientation getSurfaceOrientation() throws Exception {
        final String output = getAdbOutput("shell dumpsys input").trim();
        String regex = "SurfaceOrientation:\\s+(\\d+)";
        Pattern p = Pattern.compile(regex);
        Matcher urlMatcher = p.matcher(output);
        if (urlMatcher.find()) {
            return SurfaceOrientation.getByCode(Integer.parseInt(urlMatcher
                    .group(1)));
        }
        throw new IllegalStateException(String.format(
                "Surface orientation cannot be parsed from the output\n%s",
                output));
    }

    /**
     * Workaround for Selendroid issue when correct screen orientation is returned only for the step where it is actually
     * changed :-@
     */
    @Override
    public ScreenOrientation getOrientation() {
        final Dimension dim = this.manage().window().getSize();
        return (dim.getWidth() > dim.getHeight()) ? ScreenOrientation.LANDSCAPE : ScreenOrientation.PORTRAIT;
    }

    /**
     * This method requires the on-screen keyboard to be already visible. Also, it's important, that keyboard look and feel is
     * set to Google Keyboard -> Holo White
     *
     * @throws Exception
     */
    public void tapSendButton() throws Exception {
        final File screenshot = File.createTempFile("tmp", ".png");
        CommonUtils.takeAndroidScreenshot(this, screenshot, false);
        try {
            final List<List<Rect>> keyboardButtons = new OnScreenKeyboardScanner()
                    .getButtonCoordinates(screenshot.getCanonicalPath());
            int sendButtonRow = -1;
            if (CommonUtils.getIsTabletFromConfig(this.getClass())) {
                sendButtonRow = -3;
            }
            assert keyboardButtons.size() >= Math.abs(sendButtonRow) : "Send button cannot be found on the keyboard";
            final List<Rect> dstRow = keyboardButtons.get(keyboardButtons.size() + sendButtonRow);
            final Rect dstRect = dstRow.get(dstRow.size() - 1);
            this.tap(1, dstRect.x + dstRect.width / 2, dstRect.y + dstRect.height / 2, 50);
        } finally {
            screenshot.delete();
        }
    }
}
