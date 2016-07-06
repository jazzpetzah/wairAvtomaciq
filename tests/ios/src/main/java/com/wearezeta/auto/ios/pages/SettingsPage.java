package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.*;

import java.util.concurrent.Future;
import java.util.function.Function;

public class SettingsPage extends IOSPage {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private static final String xpathStrMenuContainer = "//UIATableView";
    private static final By xpathMenuContainer = By.xpath(xpathStrMenuContainer);

    public static final By xpathSettingsPage = By.xpath("//UIANavigationBar[@name='Settings']");

    private static final By nameBackButton = MobileBy.AccessibilityId("Back");

    private static final By xpathAllSoundAlertsButton =
            By.xpath("//UIATableCell[@name='Sound Alerts']/*[@value='All']");

    private static final By nameEditButton = MobileBy.AccessibilityId("Edit");

    private static final Function<String, String> xpathDeleteDeviceButtonByName = devicename ->
            String.format("//UIAButton[contains(@name,'Delete %s')]", devicename);

    private static final Function<String, String> xpathDeviceListEntry = device ->
            String.format("//UIATableCell[contains(@name,'%s')]", device);

    private static final By nameDeleteButton = MobileBy.AccessibilityId("Delete");

    private static final By xpathDeleteDevicePasswordField =
            By.xpath("//UIASecureTextField[contains(@value,'Password')]");

    private static final FunctionFor2Parameters<String, String, String> xpathStrDeviceVerificationLabel =
            (deviceName, verificationLabel) -> String.format(
                    "//UIATableCell[@name='%s']/UIAStaticText[@name='%s']", deviceName, verificationLabel);

    private static final String xpathStrCurrentDevice = xpathStrMainWindow + "/UIATableView[1]/UIATableCell[1]";
    private static final By xpathCurrentDevices = By.xpath(xpathStrCurrentDevice);

    public SettingsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathSettingsPage);
    }

    public void selectItem(String itemName) throws Exception {
        ((IOSElement) getElement(xpathMenuContainer)).scrollTo(itemName).click();
    }

    public void goBack() throws Exception {
        getElement(nameBackButton).click();
    }

    public boolean isSoundAlertsSetToDefault() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathAllSoundAlertsButton);
    }

    public boolean isItemVisible(String itemName) throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), MobileBy.AccessibilityId(itemName));
    }

    public void pressEditButton() throws Exception {
        getElement(nameEditButton).click();
    }

    public void pressDeleteDeviceButton(String deviceName) throws Exception {
        final By locator = By.xpath(xpathDeleteDeviceButtonByName.apply(deviceName));
        getElement(locator, String.format("Device '%s' is not visible in Manage Device List", deviceName)).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator)) {
            throw new IllegalStateException("Delete device button is still visible");
        }
    }

    public void pressDeleteButton() throws Exception {
        final WebElement deleteButton = getElement(nameDeleteButton);
        deleteButton.click();

        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameDeleteButton)) {
            deleteButton.click();
        }
    }

    public void typePasswordToConfirmDeleteDevice(String password) throws Exception {
        password = usrMgr.findUserByPasswordAlias(password).getPassword();
        ((IOSElement) getElement(xpathDeleteDevicePasswordField)).setValue(password);
    }

    public boolean isDeviceVisibleInList(String device) throws Exception {
        final By locator = By.xpath(xpathDeviceListEntry.apply(device));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isDeviceInvisibleInList(String device) throws Exception {
        final By locator = By.xpath(xpathDeviceListEntry.apply(device));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean verificationLabelVisibility(String deviceName, String verificationLabel) throws Exception {
        final By locator = By.xpath(xpathStrDeviceVerificationLabel.apply(deviceName, verificationLabel));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapCurrentDevice() throws Exception {
        getElement(xpathCurrentDevices).click();
    }

    public boolean isItemInvisible(String itemName) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), MobileBy.AccessibilityId(itemName));
    }
}
