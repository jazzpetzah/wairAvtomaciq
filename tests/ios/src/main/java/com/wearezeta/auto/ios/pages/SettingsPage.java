package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class SettingsPage extends IOSPage {
    private static final String xpathStrMenuContainer = "//UIATableView";
    private static final By xpathMenuContainer = By.xpath(xpathStrMenuContainer);

    public static final By xpathSettingsPage = By.xpath("//UIANavigationBar[@name='Settings']");

    private static final By nameBackButton = By.name("Back");

    private static final By nameDoneButton = By.name("Done");

    private static final By xpathAllSoundAlertsButton = By.xpath("//UIATableCell[@name='All']");

    public SettingsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsPage);
    }

    public void selectItem(String itemName) throws Exception {
        ((IOSElement) getElement(xpathMenuContainer)).scrollTo(itemName).click();
    }

    public void goBack() throws Exception {
        getElement(nameBackButton).click();
    }

    public void apply() throws Exception {
        getElement(nameDoneButton).click();
    }

    public boolean isSoundAlertsSetToDefault() throws Exception {
        return getElement(xpathAllSoundAlertsButton).getAttribute("value").equals("1");
    }

    public boolean isItemVisible(String itemName) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(itemName));
    }
}
