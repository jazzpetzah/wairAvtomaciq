package com.wearezeta.auto.common.driver;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.log.ZetaLogger;
import io.appium.java_client.AppiumDriver;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;

public class ZetaOSXDriver extends AppiumDriver<WebElement> implements ZetaDriver, TakesScreenshot {

    private static final Logger log = ZetaLogger.getLog(ZetaOSXDriver.class.getSimpleName());
    private ExecutorService pool;
    private final Robot robot;

    private static final String AX_POSITION = "AXPosition";
    private static final String AX_SIZE = "AXSize";
    private By windowLocator;
    private volatile boolean isSessionLost = false;

    public ZetaOSXDriver(URL remoteAddress, Capabilities desiredCapabilities) throws AWTException {
        super(remoteAddress, desiredCapabilities, JsonToWebElementConverter.class);
        this.robot = new Robot();
    }

    public void setWindowLocator(By windowLocator) {
        this.windowLocator = windowLocator;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return super.findElements(by).stream().map((e) -> wrapElement(e)).collect(Collectors.toList());
    }

    @Override
    public WebElement findElement(By by) {
        return wrapElement(super.findElement(by));
    }

    private WireRemoteWebElement wrapElement(WebElement element) {
        return new WireRemoteWebElement(element);
    }

    @Override
    public Options manage() {
        return new ZetaRemoteWebDriverOptions();
    }

    @Override
    public boolean isSessionLost() {
        return this.isSessionLost;
    }

    private void setSessionLost(boolean isSessionLost) {
        if (isSessionLost != this.isSessionLost) {
            log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
            this.isSessionLost = isSessionLost;
        }
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
    }

    @Override
    public void swipe(int i, int i1, int i2, int i3, int i4) {
        throw new RuntimeException("Not implemented for OSX");
    }

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        if (this.isSessionLost()) {
            log.warn(String.format("Driver session is dead. Skipping execution of '%s' command...", driverCommand));
            return null;
        }
        final Callable<Response> task = () -> super.execute(driverCommand, parameters);
        final Future<Response> future = getPool().submit(task);
        try {
            return future.get(DEFAULT_MAX_COMMAND_DURATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if ((e.getCause() instanceof UnreachableBrowserException)
                        || (e.getCause() instanceof SessionNotFoundException)) {
                    setSessionLost(true);
                }
                Throwables.propagate(e.getCause());
            } else {
                setSessionLost(true);
                Throwables.propagate(e);
            }
        }
        // This should never happen
        return super.execute(driverCommand, parameters);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        if (OutputType.BASE64.equals(outputType)) {
            throw new WebDriverException("Base64 screenshot not supported yet");
        } else if (OutputType.BYTES.equals(outputType)) {
            return (X) bufferedImageAsByteArray(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().
                    getScreenSize())));
        } else if (OutputType.FILE.equals(outputType)) {
            throw new WebDriverException("File screenshot not supported yet");
        } else {
            throw new WebDriverException("Unsupported OutputType selection");
        }
    }
    
    public Robot getRobot(){
        return robot;
    }

    private byte[] bufferedImageAsByteArray(BufferedImage image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    private synchronized ExecutorService getPool() {
        if (this.pool == null) {
            this.pool = Executors.newSingleThreadExecutor();
        }
        return this.pool;
    }

    protected class WireRemoteWebElement extends RemoteWebElement {

        private final WebElement originalElement;

        public WireRemoteWebElement(WebElement element) {
            this.originalElement = element;
        }

        @Override
        public void click() {
            originalElement.click();
        }

        @Override
        public void submit() {
            originalElement.submit();
        }

        @Override
        public void sendKeys(CharSequence... keysToSend) {
            originalElement.sendKeys(keysToSend);
        }

        @Override
        public void clear() {
            originalElement.clear();
        }

        @Override
        public String getTagName() {
            return originalElement.getTagName();
        }

        @Override
        public String getAttribute(String name) {
            return originalElement.getAttribute(name);
        }

        @Override
        public boolean isSelected() {
            return originalElement.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return originalElement.isEnabled();
        }

        @Override
        public String getText() {
            return originalElement.getText();
        }

        @Override
        public String getCssValue(String propertyName) {
            return originalElement.getCssValue(propertyName);
        }

        @Override
        public List<WebElement> findElements(By by) {
            return originalElement.findElements(by);
        }

        @Override
        public WebElement findElement(By by) {
            return originalElement.findElement(by);
        }

        @Override
        public boolean equals(Object obj) {
            return originalElement.equals(obj);
        }

        @Override
        public int hashCode() {
            return originalElement.hashCode();
        }

        @Override
        public boolean isDisplayed() {
            return originalElement.isDisplayed();
        }

        @Override
        public Dimension getSize() {
            final NSPoint elementSize = NSPoint.fromString(this.getAttribute(AX_SIZE));
            return new Dimension(elementSize.x(), elementSize.y());
        }

        @Override
        public Point getLocation() {
            final NSPoint elementLocation = NSPoint.fromString(this.getAttribute(AX_POSITION));
            return new Point(elementLocation.x(), elementLocation.y());
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
            Point elLocation = this.getLocation();
            Dimension elSize = this.getSize();
            if (OutputType.BASE64.equals(outputType)) {
                throw new WebDriverException("Base64 screenshot not supported yet");
            } else if (OutputType.BYTES.equals(outputType)) {
                return (X) bufferedImageAsByteArray(robot.createScreenCapture(new Rectangle(
                        elLocation.getX(), elLocation.getY(), elSize.getWidth(), elSize.getHeight())));
            } else if (OutputType.FILE.equals(outputType)) {
                throw new WebDriverException("File screenshot not supported yet");
            }
            return null;
        }

        @Override
        public String toString() {
            return originalElement.toString();
        }

    }

    protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {

        @Beta
        @Override
        public WebDriver.Window window() {
            final WebElement window = findElement(windowLocator);
            return new ZetaRemoteWindow(window);
        }

        @Beta
        protected class ZetaRemoteWindow extends RemoteWindow {

            private final WireRemoteWebElement window;

            public ZetaRemoteWindow(WebElement window) {
                this.window = new WireRemoteWebElement(window);
            }

            public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
                return window.getScreenshotAs(outputType);
            }

            @Override
            public Dimension getSize() {
                return window.getSize();
            }

            @Override
            public Point getPosition() {
                return window.getLocation();
            }

        }
    }

}
