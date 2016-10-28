package com.wearezeta.auto.common.driver;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

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

import org.apache.log4j.Logger;
import org.openqa.selenium.Beta;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class ZetaOSXWebAppDriver extends ZetaWebAppDriver {

    private static final Logger log = ZetaLogger.getLog(ZetaOSXWebAppDriver.class.getSimpleName());
    private ExecutorService pool;
    private volatile boolean isSessionLost = false;

    private ZetaOSXDriver osxDriver;

    public ZetaOSXWebAppDriver(URL remoteAddress, Capabilities desiredCapabilities, ZetaOSXDriver osxDriver) {
        super(remoteAddress, desiredCapabilities);
        this.osxDriver = osxDriver;
    }

    @Override
    public Options manage() {
        return new ZetaRemoteWebDriverOptions();
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
        return new WireRemoteWebElement((RemoteWebElement) element);
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
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return super.getScreenshotAs(outputType);
    }

    @Override
    protected Response execute(String command) {
        return this.execute(command, ImmutableMap.<String, Object>of());
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

    private synchronized ExecutorService getPool() {
        if (this.pool == null) {
            this.pool = Executors.newSingleThreadExecutor();
        }
        return this.pool;
    }

    public ZetaOSXDriver getOsxDriver() {
        return osxDriver;
    }

    protected class WireRemoteWebElement extends RemoteWebElement {

        private final RemoteWebElement originalElement;

        public WireRemoteWebElement(RemoteWebElement element) {
            this.originalElement = element;
        }

        @Override
        public String getId() {
            return originalElement.getId();
        }

        @Override
        public void setId(String id) {
            originalElement.setId(id);
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
            return originalElement.getSize();
        }

        @Override
        public Point getLocation() {
            return originalElement.getLocation();
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
            Point windowPosition = manage().window().getPosition();
            Dimension windowSize = manage().window().getSize();
            Point elLocation = this.getLocation();
            Dimension elSize = this.getSize();

            if (OutputType.BASE64.equals(outputType)) {
                throw new WebDriverException("Base64 screenshot not supported yet");
            } else if (OutputType.BYTES.equals(outputType)) {
                BufferedImage fullScreenshot = getOsxDriver().getRobot().createScreenCapture(new Rectangle(Toolkit.
                        getDefaultToolkit().
                        getScreenSize()));
                BufferedImage webappScreenshot = fullScreenshot.getSubimage(windowPosition.getX(), windowPosition.getY(),
                        windowSize.getWidth(), windowSize.getHeight());
                BufferedImage elementScreenshot = webappScreenshot.getSubimage(elLocation.getX(), elLocation.getY(), elSize.
                        getWidth(), elSize.getHeight());
                return (X) getOsxDriver().bufferedImageAsByteArray(elementScreenshot);
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
            return new ZetaRemoteWindow();
        }

        @Beta
        protected class ZetaRemoteWindow extends RemoteWebDriverOptions.RemoteWindow {

            @Override
            public Dimension getSize() {
                return osxDriver.manage().window().getSize();
            }

            @Override
            public Point getPosition() {
                return osxDriver.manage().window().getPosition();
            }

        }
    }
}
