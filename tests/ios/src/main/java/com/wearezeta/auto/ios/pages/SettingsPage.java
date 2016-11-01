package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

public class SettingsPage extends IOSPage {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final Function<String, String> xpathStrMenuItemByName = name ->
            String.format("//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[@name='%s'] ]", name);

    public static final By xpathSettingsPage = By.xpath("//XCUIElementTypeNavigationBar[@name='Settings']");

    private static final FunctionFor2Parameters<String, String, String> xpathStrSettingsValue =
            (itemName, expectedValue) -> String.format("%s/*[@value='%s']",
                    xpathStrMenuItemByName.apply(itemName), expectedValue);

    private static final By fbXpathSelfNameEditField =
            FBBy.xpath(String.format("%s/XCUIElementTypeTextField[last()]",
                    xpathStrMenuItemByName.apply("Name")));

    private static final Function<String, String> xpathDeleteDeviceButtonByName = devicename ->
            String.format("//XCUIElementTypeButton[contains(@name,'Delete %s')]", devicename);

    private static final Function<String, String> xpathDeviceListEntry = device ->
            String.format("//*[contains(@name,'%s')]", device);

    private static final By nameDeleteButton = MobileBy.AccessibilityId("Delete");

    private static final By fbXpathDeleteDevicePasswordField =
            FBBy.xpath("//XCUIElementTypeSecureTextField[contains(@value,'Password')]");

    private static final FunctionFor2Parameters<String, String, String> xpathStrDeviceVerificationLabel =
            (deviceName, verificationLabel) -> String.format(
                    "//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]" +
                            "/XCUIElementTypeStaticText[@name='%s']", deviceName, verificationLabel);

    private static final String xpathStrCurrentDevice = "//XCUIElementTypeTable/XCUIElementTypeCell";
    private static final By xpathCurrentDevices = By.xpath(xpathStrCurrentDevice);

    private static final By xpathChangePasswordPageChangePasswordButton =
            By.xpath("//XCUIElementTypeButton[@name='RESET PASSWORD']");

    private static final By classSupportSearchField = By.className("XCUIElementTypeSearchField");

    private static final String xpathStrColorPicker = "//*[@name='COLOR']/following::XCUIElementTypeTable[1]";
    private static final By xpathColorPicker = By.xpath(xpathStrColorPicker);

    // indexation starts from 1
    private static final Function<Integer, String> xpathSreColorByIdx = idx ->
            String.format("%s/XCUIElementTypeCell[%s]", xpathStrColorPicker, idx);

    public SettingsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return isLocatorExist(xpathSettingsPage);
    }

    public void selectItem(String itemName) throws Exception {
        final By locator = FBBy.xpath(xpathStrMenuItemByName.apply(itemName));
        final FBElement dstElement = (FBElement) getElementIfExists(locator).orElseThrow(
                () -> new IllegalStateException(String.format("Menu element '%s' does not exist", itemName))
        );
        if (!dstElement.isDisplayed()) {
            dstElement.scrollTo();
        }
        dstElement.click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isItemVisible(String itemName) throws Exception {
        return isLocatorExist(By.xpath(xpathStrMenuItemByName.apply(itemName)));
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
        password = usrMgr.findUserByPasswordAlias(password).getPassword();
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

    public boolean verificationLabelVisibility(String deviceName, String verificationLabel) throws Exception {
        final By locator = By.xpath(xpathStrDeviceVerificationLabel.apply(deviceName, verificationLabel));
        return isLocatorDisplayed(locator);
    }

    public void tapCurrentDevice() throws Exception {
        getElement(xpathCurrentDevices).click();
    }

    public boolean isItemInvisible(String itemName) throws Exception {
        return isLocatorInvisible(By.xpath(xpathStrMenuItemByName.apply(itemName)));
    }

    public void tapNavigationButton(String name) throws Exception {
        getElement(MobileBy.AccessibilityId(name)).click();
        // Wait for transition animation
        Thread.sleep(1000);
    }

    public boolean isResetPasswordPageVisible() throws Exception {
        return isLocatorExist(xpathChangePasswordPageChangePasswordButton);
    }

    public boolean isSupportWebPageVisible() throws Exception {
        return isLocatorExist(classSupportSearchField, 15);
    }

    public boolean isSettingItemValueEqualTo(String itemName, String expectedValue) throws Exception {
        final By locator = FBBy.xpath(xpathStrSettingsValue.apply(itemName, expectedValue));
        return isLocatorDisplayed(locator);
    }

    public void clearSelfName() throws Exception {
        final WebElement selfName = getElement(fbXpathSelfNameEditField);
        this.tapByPercentOfElementSize((FBElement) selfName, 98, 50);
        selfName.clear();
    }

    public void setSelfName(String newName) throws Exception {
        final WebElement selfName = getElement(fbXpathSelfNameEditField);
        this.tapByPercentOfElementSize((FBElement) selfName, 98, 50);
        selfName.sendKeys(newName);
    }

    public BufferedImage getColorPickerStateScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(xpathColorPicker)).orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot of Color Picker control")
        );
    }

    public void selectAccentColor(AccentColor byName) throws Exception {
        final By locator = By.xpath(xpathSreColorByIdx.apply(byName.getId()));
        getElement(locator).click();
    }
}
