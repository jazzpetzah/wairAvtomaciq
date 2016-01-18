package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.locators.ZetaSearchContext;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class BasePage {
    private final static long DEFAULT_DRIVER_INIT_TIMEOUT = 1000 * 60 * 3; // milliseconds

    private final Future<? extends RemoteWebDriver> lazyDriver;

    protected Future<? extends RemoteWebDriver> getLazyDriver() {
        return this.lazyDriver;
    }

    protected long getDriverInitializationTimeout() {
        // Override this in subclasses if necessary
        return DEFAULT_DRIVER_INIT_TIMEOUT;
    }

    protected RemoteWebDriver getDriver() throws Exception {
        return this.getLazyDriver().get(getDriverInitializationTimeout(),
                TimeUnit.MILLISECONDS);
    }

    private WebDriverWait wait;

    private final Semaphore waitGuard = new Semaphore(1);

    protected WebDriverWait getWait() throws Exception {
        waitGuard.acquire();
        try {
            if (this.wait == null) {
                this.wait = PlatformDrivers.createDefaultExplicitWait(this
                        .getDriver());
            }
        } finally {
            waitGuard.release();
        }
        return this.wait;
    }

    private static final Logger log = ZetaLogger.getLog(BasePage.class
            .getSimpleName());

    public BasePage(Future<? extends RemoteWebDriver> lazyDriver)
            throws Exception {
        this.lazyDriver = lazyDriver;
        PageFactory.initElements(new DefaultElementLocatorFactory(
                new ZetaSearchContext(lazyDriver, this.getDriverInitializationTimeout())), this);
    }

    public void close() throws Exception {
    }

    public Optional<BufferedImage> takeScreenshot() throws Exception {
        return DriverUtils.takeFullScreenShot((ZetaDriver) this.getDriver());
    }

    public Optional<BufferedImage> getElementScreenshot(WebElement element)
            throws Exception {
        Point elementLocation = element.getLocation();
        Dimension elementSize = element.getSize();
        int x = elementLocation.x;
        int y = elementLocation.y;
        int w = elementSize.width;
        int h = elementSize.height;
        final Optional<BufferedImage> screenshot = takeScreenshot();
        if (screenshot.isPresent()) {
            return Optional.of(screenshot.get().getSubimage(x, y, w, h));
        } else {
            return Optional.empty();
        }
    }

    protected WebElement verifyLocatorPresence(By locator) throws Exception {
        return DriverUtils.verifyPresence(getDriver(), locator);
    }

    protected WebElement verifyLocatorPresence(By locator, String message) throws Exception {
        return DriverUtils.verifyPresence(getDriver(), locator, message);
    }

    protected WebElement verifyLocatorPresence(By locator, String message, int timeoutSeconds) throws Exception {
        return DriverUtils.verifyPresence(getDriver(), locator, message, timeoutSeconds);
    }

    public Optional<BufferedImage> getScreenshotByCoordinates(int x, int y,
                                                              int w, int h) throws Exception {
        final Optional<BufferedImage> screenshot = takeScreenshot();
        if (screenshot.isPresent()) {
            try {
                return Optional.of(screenshot.get().getSubimage(x, y, w, h));
            } catch (Exception e) {
                log.debug("Screenshot object is out of borders");
            }
            return screenshot;
        } else {
            return Optional.empty();
        }
    }

    protected static void clearPagesCollection(
            Class<? extends AbstractPagesCollection<? extends BasePage>> collection,
            Class<? extends BasePage> baseClass)
            throws IllegalArgumentException, IllegalAccessException {
        for (Field f : collection.getFields()) {
            if (Modifier.isStatic(f.getModifiers())
                    && baseClass.isAssignableFrom(f.getType())) {
                f.set(null, null);
            }
        }
    }

    public String getPageSource() throws Exception {
        return this.getDriver().getPageSource();
    }

    /**
     * This method can only instantiate pages, which don't support direct
     * navigation and therefore accept only only one parameter of type 'Future<?
     * extends RemoteWebDriver>'. Main purpose of this method is to encapsulate
     * Selenium driver inside pages so no one can potentially break abstraction
     * layers by using the driver directly from steps ;-)
     *
     * @param <T>        the concrete page implementation
     * @param newPageCls page class to be instantiated
     * @return newly created page object
     * @throws Exception
     */
    public <T extends BasePage> T instantiatePage(Class<T> newPageCls)
            throws Exception {
        final Constructor<?> ctor = newPageCls.getConstructor(Future.class);
        return newPageCls.cast(ctor.newInstance(this.getLazyDriver()));
    }
}
