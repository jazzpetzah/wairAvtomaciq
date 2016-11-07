package com.wearezeta.auto.ios.pages.details_overlay.common;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.ios.pages.devices_overlay.BaseUserDevicesOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class UserSettingsDevicesOverlay extends BaseUserDevicesOverlay {
    private static final String xpathStrDevicesRoot =
            "//XCUIElementTypeNavigationBar[@name='Devices']/following::XCUIElementTypeTable";

    private static final Function<String, String> xpathDeleteDeviceButtonByName = devicename ->
            String.format("//XCUIElementTypeButton[contains(@name,'Delete %s')]", devicename);

    private static final Function<String, String> xpathDeviceListEntry = device ->
            String.format("//*[contains(@name,'%s')]", device);

    private static final By nameDeleteButton = MobileBy.AccessibilityId("Delete");

    private static final By fbXpathDeleteDevicePasswordField =
            FBBy.xpath("//XCUIElementTypeSecureTextField[contains(@value,'Password')]");

    private static final String xpathStrCurrentDevice = "//XCUIElementTypeTable/XCUIElementTypeCell";
    private static final By xpathCurrentDevices = By.xpath(xpathStrCurrentDevice);

    public UserSettingsDevicesOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected String getDevicesListRootXPath() {
        return xpathStrDevicesRoot;
    }

    public void tapDeleteDeviceButton(String deviceName) throws Exception {
        final By locator = By.xpath(xpathDeleteDeviceButtonByName.apply(deviceName));
        getElement(locator, String.format("Device '%s' is not visible in Manage Device List", deviceName)).click();
        if (!isLocatorInvisible(locator)) {
            throw new IllegalStateException("Delete device button is still visible");
        }
    }

    public void tapDeleteButton() throws Exception {
        final WebElement deleteButton = getElement(nameDeleteButton);
        deleteButton.click();

        if (!isLocatorInvisible(nameDeleteButton)) {
            deleteButton.click();
        }
    }

    public void typePasswordToConfirmDeleteDevice(String password) throws Exception {
        ((FBElement) getElement(fbXpathDeleteDevicePasswordField)).setValue(password);
    }

    public boolean isDeviceVisibleInList(String device) throws Exception {
        final By locator = By.xpath(xpathDeviceListEntry.apply(device));
        return isLocatorDisplayed(locator);
    }

    public boolean isDeviceInvisibleInList(String device) throws Exception {
        final By locator = By.xpath(xpathDeviceListEntry.apply(device));
        return isLocatorInvisible(locator);
    }

    public void tapCurrentDevice() throws Exception {
        getElement(xpathCurrentDevices).click();
    }
}
