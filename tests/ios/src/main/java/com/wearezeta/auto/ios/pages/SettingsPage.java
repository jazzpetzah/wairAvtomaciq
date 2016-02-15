package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.misc.Interfaces.FunctionFor2Parameters;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class SettingsPage extends IOSPage {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private static final String xpathStrMenuContainer = "//UIATableView";
    private static final By xpathMenuContainer = By.xpath(xpathStrMenuContainer);

    public static final By xpathSettingsPage = By.xpath("//UIANavigationBar[@name='Settings']");

    private static final By nameBackButton = By.name("Back");

    private static final By nameDoneButton = By.name("Done");

    private static final By xpathAllSoundAlertsButton = By.xpath("//UIATableCell[@name='All']");

    private static final By nameEditButton = By.name("Edit");

    private static final Function<String, String> xpathDeleteDeviceButtonByName = devicename ->
            String.format("//UIAButton[contains(@name,'Delete %s')]", devicename);

    private static final Function<String, String> xpathDeviceListEntry = device ->
            String.format("//UIATableCell[contains(@name,'%s')]", device);

    private static final By nameDeleteButton = By.name("Delete");

    private static final By xpathDeleteDevicePasswordField = By.xpath("//UIASecureTextField[contains(@value,'Password')]");

    private static final FunctionFor2Parameters<String, String, String> xpathStrDeviceVerificationLabel =
            (deviceName, verificationLabel) ->
                    String.format("//UIATableCell[@name='%s']/UIAStaticText[@name='%s']", deviceName, verificationLabel);

    private static final By currentLabel = By.name("Current");

    private static final String xpathStrCurrentDevice = xpathStrMainWindow + "/UIATableView[1]/UIATableCell[1]";
    private static final By xpathCurrentDevices = By.xpath(xpathStrCurrentDevice);

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

    public void pressEditButton() throws Exception {
        getElement(nameEditButton).click();
    }

    public void pressDeleteDeviceButton(String deviceName) throws Exception {
        final By locator = By.xpath(xpathDeleteDeviceButtonByName.apply(deviceName));
        getElement(locator, String.format("Device '%s' is not visible in Manage Device List", deviceName)).click();
    }

    public void pressDeleteButton() throws Exception {
        getElement(nameDeleteButton).click();
    }

    public void typePasswordToConfirmDeleteDevice(String password) throws Exception {
        password = usrMgr.findUserByPasswordAlias(password).getPassword();
        ((IOSElement) getElement(xpathDeleteDevicePasswordField)).setValue(password);
    }

    public boolean isDeviceVisibleInList(String device) throws Exception {
        final By locator = By.xpath(xpathDeviceListEntry.apply(device));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean verificationLabelVisibility(String deviceName, String verificaitonLabel) throws Exception {
        final By locator = By.xpath(xpathStrDeviceVerificationLabel.apply(deviceName, verificaitonLabel));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public void tapCurrentDevice() throws Exception {
        getElement(xpathCurrentDevices).click();
    }

    public boolean isItemInvisible(String itemName) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(itemName));
    }
}
