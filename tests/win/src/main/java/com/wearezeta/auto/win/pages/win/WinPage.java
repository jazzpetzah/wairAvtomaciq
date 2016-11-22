package com.wearezeta.auto.win.pages.win;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.win.common.WinExecutionContext;
import com.wearezeta.auto.win.locators.WinLocators;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

public abstract class WinPage extends BasePage {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(WinPage.class.getName());

    private static final String MENU_ITEM_VERSION = "Version";
    private static final String MENUBAR_ITEM_HELP = "Help";

    private String path = null;

    @Override
    protected ZetaWinDriver getDriver() throws Exception {
        return (ZetaWinDriver) super.getDriver();
    }

    public WinPage(Future<ZetaWinDriver> winDriver) throws Exception {
        super(winDriver);
    }

    public WinPage(Future<ZetaWinDriver> winDriver, String path)
            throws Exception {
        super(winDriver);
        this.path = path;
    }

    public void switchEnvironment(String environmentName) throws Exception {
        // click version menu item twice to get dev and env menu items
        for (int i = 0; i < 2; i++) {
            clickMenuBarItem(MENUBAR_ITEM_HELP, MENU_ITEM_VERSION);
        }

        clickMenuBarItem(MENUBAR_ITEM_HELP, environmentName);
        // FIXME: Have to click environment twice to actually trigger page load
        // to reg page
        clickMenuBarItem(MENUBAR_ITEM_HELP, environmentName);
    }

    public void clickMenuBarItem(String firstItem, String... items)
            throws Exception {
        String locatorBar = WinLocators.AppMenu.xpathMenuBarItem
                .apply(firstItem);
        By locator = By.xpath(locatorBar);
        DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
        WebElement menuElement = getDriver().findElement(locator);
        DriverUtils.waitUntilElementClickable(getDriver(), menuElement);
        menuElement.click();
        for (String item : items) {
            By itemLocator = By.xpath(WinLocators.AppMenu.xpathMenuItem
                    .apply(item));
            DriverUtils.waitUntilLocatorAppears(getDriver(), itemLocator);
            WebElement itemElement = getDriver().findElement(itemLocator);
            DriverUtils.waitUntilElementClickable(getDriver(), itemElement);
            itemElement.click();
        }
    }

    public void showElementsForXpathLocator(String locator) throws Exception {
        By xpathLocator = By.xpath(locator);
        List<WebElement> elements = getDriver().findElements(xpathLocator);
        for (WebElement element : elements) {
            LOG.debug(element.getAttribute("Name") + element.getAttribute("ControlType"));
        }
    }

    public void navigateTo() throws Exception {
        if (this.path == null) {
            throw new RuntimeException(String.format(
                    "The page %s does not support direct navigation", this
                    .getClass().getName()));
        }
        this.getDriver().navigate().to(this.path);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    public void startApp() throws Exception {
        this.getDriver().navigate().to(WinExecutionContext.WIRE_APP_PATH);
    }

    public Dimension getDesktopSize() throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    public Optional<BufferedImage> getScreenshot() throws Exception {
        return DriverUtils.takeFullScreenShot(getDriver());
    }
    
    protected void selectByIndex(Robot robot, int index, long wait) throws Exception {
        Thread.sleep(wait);
        for (int i = 0; i < index; i++) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            Thread.sleep(wait);
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
