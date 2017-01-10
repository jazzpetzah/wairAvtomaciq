package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import com.wearezeta.auto.common.misc.Timedelta;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public class SettingsPage extends IOSPage {
    private static final Function<String, String> xpathStrMenuItemByName = name ->
            String.format("//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[@name='%s'] ]", name);

    private static final By fbClassNameOptionsRoot = FBBy.className("XCUIElementTypeTable");

    private static final By xpathSettingsPage = By.xpath("//XCUIElementTypeNavigationBar[@name='Settings']");

    private static final FunctionFor2Parameters<String, String, String> xpathStrSettingsValue =
            (itemName, expectedValue) -> String.format("%s/*[@value='%s']",
                    xpathStrMenuItemByName.apply(itemName), expectedValue);

    private static final By fbXpathSelfNameEditField =
            FBBy.xpath(String.format("%s/XCUIElementTypeTextField[last()]",
                    xpathStrMenuItemByName.apply("Name")));

    private static final FunctionFor2Parameters<String, String, String> xpathStrDeviceVerificationLabel =
            (deviceName, verificationLabel) -> String.format(
                    "//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]" +
                            "/XCUIElementTypeStaticText[@name='%s']", deviceName, verificationLabel);

    private static final By xpathChangePasswordPageChangePasswordButton =
            By.xpath("//XCUIElementTypeButton[@name='RESET PASSWORD']");

    private static final By classSupportSearchField = By.className("XCUIElementTypeSearchField");

    private static final String xpathStrColorPicker = "//*[@name='COLOR']/following::XCUIElementTypeTable[1]";
    private static final By xpathColorPicker = By.xpath(xpathStrColorPicker);

    // indexation starts from 1
    private static final Function<Integer, String> xpathSreColorByIdx = idx ->
            String.format("%s/XCUIElementTypeCell[%s]", xpathStrColorPicker, idx);

    private static final Function<String, String> xpathStrUniqueUsernameInSettings = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name.startsWith("@") ? name : "@" + name);

    private static final By xpathSettingsProfilePicturePreview = By.xpath("//XCUIElementTypeImage[" +
            "@name='imagePreview' and @value='image']");

    public SettingsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return isLocatorExist(xpathSettingsPage);
    }

    private static final int MAX_SCROLLS = 2;

    private FBElement scrollToItem(String itemName) throws Exception {
        final By locator = FBBy.xpath(xpathStrMenuItemByName.apply(itemName));
        FBElement optionsRoot = null;
        int nScrolls = 0;
        do {
            final Optional<WebElement> dstElement = getElementIfDisplayed(locator, Timedelta.fromSeconds(2));
            if (dstElement.isPresent()) {
                return (FBElement) dstElement.get();
            }
            if (optionsRoot == null) {
                optionsRoot = (FBElement) getElement(fbClassNameOptionsRoot);
            }
            optionsRoot.scrollDown();
            nScrolls++;
        } while (nScrolls < MAX_SCROLLS);
        throw new IllegalStateException(String.format("Menu item '%s' does not exist", itemName));
    }

    public void selectItem(String itemName) throws Exception {
        scrollToItem(itemName).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isItemVisible(String itemName) throws Exception {
        return isLocatorExist(By.xpath(xpathStrMenuItemByName.apply(itemName)));
    }

    public boolean isVerificationLabelVisible(String deviceName, String verificationLabel) throws Exception {
        final By locator = By.xpath(xpathStrDeviceVerificationLabel.apply(deviceName, verificationLabel));
        return isLocatorDisplayed(locator);
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
        return isLocatorExist(classSupportSearchField, Timedelta.fromSeconds(15));
    }

    public boolean isSettingItemValueEqualTo(String itemName, String expectedValue) throws Exception {
        final By locator = FBBy.xpath(xpathStrSettingsValue.apply(itemName, expectedValue));
        return isLocatorDisplayed(locator);
    }

    public void clearSelfName() throws Exception {
        final WebElement selfName = getElementIfExists(fbXpathSelfNameEditField).orElseThrow(
                () -> new IllegalStateException("Name input is not present on the page")
        );
        selfName.click();
        selfName.clear();
    }

    public void setSelfName(String newName) throws Exception {
        final WebElement selfName = getElementIfExists(fbXpathSelfNameEditField).orElseThrow(
                () -> new IllegalStateException("Name input is not present on the page")
        );
        selfName.click();
        selfName.clear();
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

    public boolean isUniqueUsernameInSettingsDisplayed(String uniqueName) throws Exception {
        By locator = By.xpath(xpathStrUniqueUsernameInSettings.apply(uniqueName));
        return isLocatorDisplayed(locator);
    }

    public boolean isProfilePicturePreviewVisible() throws Exception {
        final WebElement picturePreview = getElement(xpathSettingsProfilePicturePreview);
        return isElementVisible(picturePreview);
    }
}