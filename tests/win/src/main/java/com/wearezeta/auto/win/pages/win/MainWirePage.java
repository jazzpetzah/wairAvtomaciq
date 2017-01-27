package com.wearezeta.auto.win.pages.win;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.win.locators.WinLocators;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/*

This page object is used to execute various tasks related to the size of the Wrapper (Resize, minimize etc.). Coordinates depend
on various different factors, like window decoration and screen size. This is a basic overview of the screen:

 ----------------------------------------
| Screen                                 |
|       _______________________________  |
|      | Window                        | |
|      | -------------------------     | |
|      ||  WebView                |    | |
|      | -------------------------     | |
|      --------------------------------  |
 ----------------------------------------

 The screen is the whole desktop size. The window is the wrapper with all window decoration, like title bar, menu bar, etc. The
 WebView is the Chrome instance that is embedded in the wrapper window.

 */
public class MainWirePage extends WinPage {

    public static final int APP_MAX_WIDTH = 1103;
    private static final int APP_MIN_WIDTH = 780;
    private static final int APP_MIN_HEIGHT = 600;

    private static final int TITLEBAR_HEIGHT = 22;
    private static final int SPACE_FOR_DOCK = 50;
    private static final int TITLEBAR_HANDLE_OFFSET = 100;

    private final Robot robot = new Robot();

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathWindow)
    protected WebElement window;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathMinimizeButton)
    protected WebElement minimizeButton;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathZoomButton)
    protected WebElement zoomButton;

    @FindBy(how = How.XPATH, using = WinLocators.MainWirePage.xpathCloseButton)
    protected WebElement closeButton;

    public MainWirePage(Future<ZetaWinDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void focusApp() {
        window.click();
    }

    public boolean isMainWindowVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(WinLocators.MainWirePage.xpathWindow));
    }

    public void minimizeWindow() throws Exception {
        minimizeButton.click();
    }

    public void closeWindow() {
        closeButton.click();
    }

    public void pressShortCutForQuit() throws Exception {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
    }

    public void pressShortCutForPreferences() throws Exception {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_COMMA);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public boolean isMini() throws Exception {
        Dimension size = getDriver().manage().window().getSize();
        // TODO adjust for retina and non-retina
        boolean minWidth = size.getWidth() == APP_MIN_WIDTH;
        boolean minHeight = size.getHeight() == APP_MIN_HEIGHT;
        return minWidth && minHeight;
    }

    public int getX() throws Exception {
        return getDriver().manage().window().getPosition().getX();
    }

    public int getY() throws Exception {
        return getDriver().manage().window().getPosition().getY();
    }

    public int getWidth() throws Exception {
        return getDriver().manage().window().getSize().getWidth();
    }

    public int getHeight() throws Exception {
        return getDriver().manage().window().getSize().getHeight();
    }

    public boolean isApproximatelyWidth(int width) throws Exception {
        int plusMinus = 5;
        return getWidth() > (width - plusMinus)
                && getWidth() < (width + plusMinus);
    }

    public boolean isApproximatelyHeight(int height) throws Exception {
        int plusMinus = 5;
        return getHeight() > (height - plusMinus)
                && getHeight() < (height + plusMinus);
    }

    public void resizeByHand(int width, int height) throws Exception {
        final Dimension windowDimensions = getDriver().manage().window()
                .getSize();
        final Point windowPosition = getDriver().manage().window()
                .getPosition();
        final Point lowerRightWindowHandle = getLowerRightWindowHandle(
                windowDimensions, windowPosition);

        long newWidthOverflow = windowPosition.getX() + (long) width;
        long newHeightOverflow = windowPosition.getY() + (long) height;

        if (newWidthOverflow >= Integer.MAX_VALUE) {
            newWidthOverflow = Integer.MAX_VALUE;
        }
        if (newHeightOverflow >= Integer.MAX_VALUE) {
            newHeightOverflow = Integer.MAX_VALUE;
        }

        robot.mouseMove(lowerRightWindowHandle.getX(),
                lowerRightWindowHandle.getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove((int) newWidthOverflow, (int) newHeightOverflow);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void positionByHand(int x, int y) throws Exception {
        final Point windowPosition = getDriver().manage().window()
                .getPosition();

        final Point titleBar = new Point(windowPosition.getX()
                + TITLEBAR_HANDLE_OFFSET, windowPosition.getY()
                + TITLEBAR_HEIGHT / 2);
        final Point customPosition = new Point(SPACE_FOR_DOCK
                + TITLEBAR_HANDLE_OFFSET + x, TITLEBAR_HEIGHT
                + (TITLEBAR_HEIGHT / 2) + y);

        robot.mouseMove(titleBar.getX(), titleBar.getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(customPosition.getX(), customPosition.getY());
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void resizeToMaxByHand() throws Exception {
        resizeByHand(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void resizeToMinByHand() throws Exception {
        resizeByHand(0, 0);
    }

    public void ensurePosition() throws Exception {
        positionByHand(0, 0);
    }

    private Point getLowerRightWindowHandle(Dimension windowDimensions,
            Point windowPosition) {
        // we have to subtract 1 to get the handle
        return new Point(windowPosition.getX() + windowDimensions.getWidth()
                - 1, windowPosition.getY() + windowDimensions.getHeight() - 1);
    }

    public void clickMaximizeButton() {
        zoomButton.click();
    }

    public void rightClickInWebView(int x, int y) throws Exception {
        x = getX() + x;
        y = getY() + y;
        rightClick(x, y);
    }

    public void rightClick(int x, int y) throws InterruptedException {
        robot.mouseMove(x, y);
        Thread.sleep(50);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
}
