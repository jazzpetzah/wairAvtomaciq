package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.Collectors;

import org.openqa.selenium.Beta;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

public class ZetaWinDriver extends AppiumDriver<WebElement> implements ZetaDriver {
    private static final String APP_NAME = "Wire";

    public ZetaWinDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return super.findElements(by).stream().map(e -> wrapElement(e)).collect(Collectors.toList());
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
    public MobileElement scrollTo(String text) {
        throw new RuntimeException("Not implemented for OSX");
    }

    @Override
    public MobileElement scrollToExact(String text) {
        throw new RuntimeException("Not implemented for OSX");
    }

    @Override
    public boolean isSessionLost() {
        return false;
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
            String bounds = this.getAttribute("BoundingRectangle");
            WinSize winSize = new WinSize(bounds);
            return new Dimension(winSize.getWidth(), winSize.getHeight());
        }

        @Override
        public Point getLocation() {
            String bounds = this.getAttribute("BoundingRectangle");
            WinPoint winPoint = new WinPoint(bounds);
            return new Point(winPoint.getX(), winPoint.getY());
        }

        @Beta
        @Override
        public <X> X getScreenshotAs(OutputType<X> outputType)
                throws WebDriverException {
            return originalElement.getScreenshotAs(outputType);
        }

        @Override
        public String toString() {
            return originalElement.toString();
        }

    }

    protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {

        private static final String WINDOW_LOCATOR = "/*[@ClassName='Chrome_WidgetWin_1' and contains(@Name,'"
                + APP_NAME + "')]";

        @Beta
        @Override
        public WebDriver.Window window() {
            final String xpathWindow = WINDOW_LOCATOR;
            final WebElement window = findElement(By.xpath(xpathWindow));
            return new ZetaRemoteWindow(window);
        }

        @Beta
        protected class ZetaRemoteWindow extends RemoteWindow {

            private final WebElement window;

            public ZetaRemoteWindow(WebElement window) {
                this.window = window;
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

    private class WinPoint {

        private final int x;
        private final int y;

        private static final String POINT_PARSING_STRING = "^([\\-]?[0-9]*),([\\-]?[0-9]*).*";

        public WinPoint(String string) {
            Pattern pattern = Pattern
                    .compile(POINT_PARSING_STRING);
            Matcher matcher = pattern.matcher(string);

            WinPoint point = null;
            while (matcher.find()) {
                point = new WinPoint(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            this.x = point.getX();
            this.y = point.getY();
        }

        public WinPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    private class WinSize {

        private final int width;
        private final int height;

        private static final String POINT_PARSING_STRING = ".*,([0-9]*),([0-9]*)$";

        public WinSize(String string) {
            Pattern pattern = Pattern
                    .compile(POINT_PARSING_STRING);
            Matcher matcher = pattern.matcher(string);

            WinSize location = null;
            while (matcher.find()) {
                location = new WinSize(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }
            this.width = location.getWidth();
            this.height = location.getHeight();
        }

        public WinSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

    }
}
