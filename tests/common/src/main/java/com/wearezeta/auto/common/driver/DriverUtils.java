package com.wearezeta.auto.common.driver;

import io.appium.java_client.AppiumDriver;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class DriverUtils {
    public static final int DEFAULT_PERCENTAGE = 50;

    private static final Logger log = ZetaLogger.getLog(DriverUtils.class
            .getSimpleName());

    public static int getDefaultLookupTimeoutSeconds() throws Exception {
        return Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(DriverUtils.class));
    }

    /**
     * https://code.google.com/p/selenium/issues/detail?id=1880
     * <p>
     * DO NOT use this method if you want to check whether the element is NOT
     * visible, because it will wait at least "imlicitTimeout" seconds until the
     * actual result is returned. This slows down automated tests!
     * <p>
     * Use "waitUntilLocatorDissapears" method instead. That's quick and does
     * exactly what you need
     *
     * @param driver  selenium driver instance
     * @param element web element to verify
     * @return wither true or false
     */
    public static boolean isElementPresentAndDisplayed(RemoteWebDriver driver, final WebElement element) {
        try {
            final boolean result = element.isDisplayed();
            if (driver instanceof ZetaIOSDriver) {
                return result;
            } else {
                return result && isElementInScreenRect(driver, element);
            }
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static boolean isElementInScreenRect(WebDriver driver, final WebElement el) {
        final Rectangle elementRect = new Rectangle(el.getLocation().x, el.getLocation().y,
                el.getSize().width, el.getSize().height);
        final Dimension dim = driver.manage().window().getSize();
        final Rectangle windowRect = new Rectangle(dim.getWidth(), dim.getHeight());
        return elementRect.intersects(windowRect);
    }

    public static boolean waitUntilLocatorIsDisplayed(RemoteWebDriver driver, By by) throws Exception {
        return waitUntilLocatorIsDisplayed(driver, by, getDefaultLookupTimeoutSeconds());
    }

    public static boolean waitUntilLocatorIsDisplayed(RemoteWebDriver driver,
                                                      final By by, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(InvalidElementStateException.class);
            try {
                return wait.until(drv -> {
                    final List<WebElement> foundElements = drv.findElements(by);
                    if (foundElements.size() > 0) {
                        for (WebElement element : foundElements) {
                            if (isElementPresentAndDisplayed(driver, element)) {
                                return true;
                            } else {
                                log.debug("Element is not in viewport: " + element);
                            }
                        }
                    }
                    return false;
                });
            } catch (TimeoutException e) {
                return false;
            }
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static Optional<WebElement> getElementIfDisplayed(RemoteWebDriver driver, final By by) throws Exception {
        return getElementIfDisplayed(driver, by, getDefaultLookupTimeoutSeconds());
    }

    public static Optional<WebElement> getElementIfDisplayed(RemoteWebDriver driver,
                                                             final By by, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            final long millisecondsStarted = System.currentTimeMillis();
            do {
                try {
                    final List<WebElement> foundElements = driver.findElements(by);
                    if (foundElements.size() > 0) {
                        for (WebElement foundElement : foundElements) {
                            if (isElementPresentAndDisplayed(driver, foundElement)) {
                                return Optional.of(foundElement);
                            }
                        }
                    }
                } catch (NoSuchElementException | StaleElementReferenceException | InvalidElementStateException e) {
                    // Ignore silently
                }
                Thread.sleep(100);
            } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
            return Optional.empty();
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static boolean waitUntilLocatorDissapears(RemoteWebDriver driver,
                                                     final By by) throws Exception {
        return waitUntilLocatorDissapears(driver, by, getDefaultLookupTimeoutSeconds());
    }

    public static boolean waitUntilLocatorDissapears(RemoteWebDriver driver,
                                                     final By by, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(InvalidElementStateException.class);
            return wait.until(drv -> {
                try {
                    final List<WebElement> foundElements = drv.findElements(by);
                    return (foundElements.size() == 0) || !isElementPresentAndDisplayed(driver, foundElements.get(0));
                } catch (SessionNotFoundException e) {
                    log.debug(e.getMessage());
                    return true;
                } catch (WebDriverException e) {
                    return true;
                }
            });
        } catch (TimeoutException ex) {
            return false;
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static boolean waitUntilLocatorAppears(RemoteWebDriver driver,
                                                  final By locator) throws Exception {
        return waitUntilLocatorAppears(driver, locator, getDefaultLookupTimeoutSeconds());
    }

    public static boolean waitUntilLocatorAppears(RemoteWebDriver driver,
                                                  final By locator, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(InvalidElementStateException.class);
            return wait.until(drv -> drv.findElements(locator).size() > 0);
        } catch (TimeoutException ex) {
            return false;
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static Optional<WebElement> getElementIfPresentInDOM(RemoteWebDriver driver, final By by) throws Exception {
        return getElementIfPresentInDOM(driver, by, getDefaultLookupTimeoutSeconds());
    }

    public static Optional<WebElement> getElementIfPresentInDOM(RemoteWebDriver driver,
                                                                final By by, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            final long millisecondsStarted = System.currentTimeMillis();
            do {
                final List<WebElement> foundElements = driver.findElements(by);
                try {
                    if (foundElements.size() > 0) {
                        return Optional.of(foundElements.get(0));
                    }
                } catch (NoSuchElementException | StaleElementReferenceException | InvalidElementStateException e) {
                    // Ignore silently
                }
                Thread.sleep(100);
            } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
            return Optional.empty();
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static boolean waitUntilElementClickable(RemoteWebDriver driver,
                                                    final WebElement element) throws Exception {
        return waitUntilElementClickable(driver, element, getDefaultLookupTimeoutSeconds());
    }

    public static boolean waitUntilElementClickable(RemoteWebDriver driver,
                                                    final WebElement element, int timeoutSeconds) throws Exception {
        if (!PlatformDrivers.isMobileDriver(driver)) {
            turnOffImplicitWait(driver);
        }
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
                    .pollingEvery(1, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        } finally {
            if (!PlatformDrivers.isMobileDriver(driver)) {
                restoreImplicitWait(driver);
            }
        }
    }

    public static final int SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL = 100;

    public static void swipeRight(AppiumDriver<? extends WebElement> driver,
                                  WebElement element, int timeMillis, int percentX, int percentY) {
        final Point coords = element.getLocation();
        final Dimension elementSize = element.getSize();
        final int xOffset = elementSize.width * percentX / 100;
        final int yOffset = elementSize.height * percentY / 100;
        driver.swipe(coords.x, coords.y + yOffset, coords.x + xOffset, coords.y
                + yOffset, timeMillis);
    }

    public static void swipeRight(AppiumDriver<? extends WebElement> driver,
                                  WebElement element, int timeMillis, int startPercentX, int startPercentY,
                                  int endPercentX, int endPercentY) {
        final Point coords = element.getLocation();
        final Dimension elementSize = element.getSize();

        final int xStartOffset = elementSize.width * startPercentX / 100;
        final int yStartOffset = elementSize.height * startPercentY / 100;
        final int xEndOffset = elementSize.width * endPercentX / 100;
        final int yEndOffset = elementSize.height * endPercentY / 100;

        driver.swipe(coords.x + xStartOffset, coords.y + yStartOffset, coords.x
                + xEndOffset, coords.y + yEndOffset, timeMillis);

    }

    public static void swipeRight(AppiumDriver<? extends WebElement> driver, WebElement element, int timeMillis) {
        swipeRight(driver, element, timeMillis, SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, DEFAULT_PERCENTAGE);
    }

    public static void swipeElementPointToPoint(
            AppiumDriver<? extends WebElement> driver, WebElement element,
            int timeMillis, int startPercentX, int startPercentY, int endPercentX,
            int endPercentY) {

        final Point coords = element.getLocation();
        final Dimension size = element.getSize();

        final int startX = coords.x + size.getWidth() * startPercentX / 100;
        final int startY = coords.y + size.getHeight() * startPercentY / 100;
        final int endX = coords.x + size.getWidth() * endPercentX / 100;
        final int endY = coords.y + size.getHeight() * endPercentY / 100;

        driver.swipe(startX, startY, endX, endY, timeMillis);
    }

    public static void swipeByCoordinates(AppiumDriver<? extends WebElement> driver, int timeMillis,
                                          int startPercentX, int startPercentY, int endPercentX,
                                          int endPercentY) throws Exception {
        final Dimension screenSize = driver.manage().window().getSize();

        final int startX = screenSize.width * startPercentX / 100;
        final int startY = screenSize.height * startPercentY / 100;
        final int endX = screenSize.width * endPercentX / 100;
        final int endY = screenSize.height * endPercentY / 100;

        driver.swipe(startX, startY, endX, endY, timeMillis);
    }

    public static void tapByCoordinates(AppiumDriver<? extends WebElement> driver, WebElement element,
                                        int offsetX, int offsetY) {
        final Point coords = element.getLocation();
        final Dimension elementSize = element.getSize();
        driver.tap(1, (coords.x + offsetX + elementSize.width) - elementSize.width / 2,
                (coords.y + offsetY + elementSize.height) - elementSize.height / 2,
                SINGLE_TAP_DURATION);
    }

    public static void tapByCoordinates(AppiumDriver<? extends WebElement> driver, WebElement element) {
        tapByCoordinates(driver, element, 0, 0);
    }

    public static void addClass(RemoteWebDriver driver, WebElement element,
                                String cssClass) {
        String addHoverClassScript = "arguments[0].classList.add('" + cssClass + "');";
        driver.executeScript(addHoverClassScript, element);
    }

    public static void addClassToParent(RemoteWebDriver driver,
                                        WebElement element, String cssClass) {
        String addHoverClassScript = "arguments[0].parentNode.classList.add('"
                + cssClass + "');";
        driver.executeScript(addHoverClassScript, element);
    }

    public static void removeClass(RemoteWebDriver driver, WebElement element,
                                   String cssClass) {
        String script = "arguments[0].classList.remove('" + cssClass + "');";
        driver.executeScript(script, element);
    }

    public static void removeClassFromParent(RemoteWebDriver driver,
                                             WebElement element, String cssClass) {
        String script = "arguments[0].parentNode.classList.remove('" + cssClass
                + "');";
        driver.executeScript(script, element);
    }

    public static void turnOffImplicitWait(RemoteWebDriver driver) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    public static void restoreImplicitWait(RemoteWebDriver driver) throws Exception {
        PlatformDrivers.setDefaultImplicitWaitTimeout(driver);
    }

    public static Optional<BufferedImage> takeFullScreenShot(ZetaDriver driver) throws Exception {
        try {
            final byte[] srcImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            final BufferedImage bImageFromConvert = ImageIO.read(new ByteArrayInputStream(srcImage));
            return Optional.ofNullable(bImageFromConvert);
        } catch (WebDriverException | NoClassDefFoundError e) {
            // e.printStackTrace();
            log.error("Selenium driver has failed to take the screenshot of the current screen!");
        }
        return Optional.empty();
    }

    public static void moveMouserOver(RemoteWebDriver driver, WebElement element) {
        /**
         * Method seems to work for Chrome and FireFox but is not working for
         * Safari <= 8. https://code.google.com/p/selenium/issues/detail?id=4136
         */
        Actions action = new Actions(driver);
        action.moveToElement(element);
        action.perform();
    }

    // in milliseconds (http://stackoverflow.com/questions/13670094/duration-of-a-single-tap-and-long-tap-in-android)
    public static final int SINGLE_TAP_DURATION = 125;
    public static final int LONG_TAP_DURATION = 2000;

    public static void tapInTheCenterOfTheElement(AppiumDriver<? extends WebElement> driver, WebElement element) {
        final Point coords = element.getLocation();
        final Dimension size = element.getSize();
        final int x = coords.x + size.getWidth() / 2;
        final int y = coords.y + size.getHeight() / 2;
        log.info("Tap on " + x + ":" + y);
        driver.tap(1, x, y, SINGLE_TAP_DURATION);
    }

    public static void tapOnPercentOfElement(AppiumDriver<? extends WebElement> driver, WebElement element,
                                             int percentX, int percentY, int durationMs) {
        final Point coords = element.getLocation();
        final Dimension size = element.getSize();
        final int x = coords.x + size.getWidth() * percentX / 100;
        final int y = coords.y + size.getHeight() * percentY / 100;
        log.info("Tap on " + x + ":" + y);
        driver.tap(1, x, y, durationMs);
    }

    public static void tapOnPercentOfElement(
            AppiumDriver<? extends WebElement> driver, WebElement element, int percentX, int percentY) {
        final Point coords = element.getLocation();
        final Dimension size = element.getSize();
        final int x = coords.x + size.getWidth() * percentX / 100;
        final int y = coords.y + size.getHeight() * percentY / 100;
        log.info("Tap on " + x + ":" + y);
        driver.tap(1, x, y, SINGLE_TAP_DURATION);
    }

    public static void tapOutsideOfTheElement(
            AppiumDriver<? extends WebElement> driver, WebElement element, int xOffset, int yOffset) {
        assert xOffset != 0 || yOffset != 0;
        final Point coords = element.getLocation();
        final Dimension size = element.getSize();
        final Dimension screenSize = driver.manage().window().getSize();
        int dstX, dstY;
        if (xOffset > 0) {
            dstX = coords.getX() + size.getWidth() + xOffset;
        } else {
            dstX = coords.getX() - xOffset;
        }
        if (yOffset > 0) {
            dstY = coords.getY() + size.getHeight() + yOffset;
        } else {
            dstY = coords.getY() - yOffset;
        }
        dstY = (screenSize.getHeight() < dstY) ? screenSize.getHeight() : dstY;
        dstX = (screenSize.getWidth() < dstX) ? screenSize.getWidth() : dstX;
        dstY = (dstY < 0) ? 0 : dstY;
        dstX = (dstX < 0) ? 0 : dstX;
        log.info("Tap on " + dstX + ":" + dstY);
        driver.tap(1, dstX, dstY, SINGLE_TAP_DURATION);
    }

    public static WebElement verifyPresence(RemoteWebDriver driver, By locator, String message, int timeoutSeconds)
            throws Exception {
        return getElementIfDisplayed(driver, locator, timeoutSeconds).orElseThrow(() ->
                new IllegalStateException(message));
    }

    public static WebElement verifyPresence(RemoteWebDriver driver, By locator, String message) throws Exception {
        return getElementIfDisplayed(driver, locator).orElseThrow(() ->
                new IllegalStateException(message));
    }

    public static WebElement verifyPresence(RemoteWebDriver driver, By locator) throws Exception {
        return getElementIfDisplayed(driver, locator).orElseThrow(() ->
                new IllegalStateException(String.format("The element '%s' is not visible", locator)));
    }
}
