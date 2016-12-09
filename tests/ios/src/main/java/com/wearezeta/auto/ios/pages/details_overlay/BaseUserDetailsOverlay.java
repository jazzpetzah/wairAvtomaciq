package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BaseUserDetailsOverlay extends BaseDetailsOverlay {
    protected static final By nameXButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By xpathProfilePicture = By.xpath(
            "//XCUIElementTypeButton[@name='DEVICES']/following::" +
                    "XCUIElementTypeOther[ ./XCUIElementTypeImage or ./XCUIElementTypeStaticText ][1]"
    );

    private static final Function<String, String> xpathStrNameByValue = name ->
            String.format("(//XCUIElementTypeStaticText[@name='name' and @value='%s'])[last()]", name);
    private static final By xpathName = By.xpath("(//XCUIElementTypeStaticText[@name='name'])[last()]");

    private static final Function<String, String> xpathStrABNameByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='correlation' and @value='%s']",
                    name.trim().length() > 0 ? (name + " in Address Book") : "in Address Book");
    private static final By nameABName = MobileBy.AccessibilityId("correlation");

    private static final Function<Integer, String> xpathStrCommonFriendsByCount = count ->
            String.format("//XCUIElementTypeStaticText[@name='correlation' and @value='%s']",
                    (count == 1) ? "1 friend in common" : count + " friends in common");
    private static final By nameCommonFriends = nameABName;

    private static final Function<String, String> xpathStrUniqueUsernameByUsername = username ->
            String.format("//XCUIElementTypeStaticText[(@name='username' or @name='handle') and @value='%s']",
                    username.startsWith("@") ? username : ("@" + username));
    private static final By xpathUniqueUsername =
            By.xpath("//XCUIElementTypeStaticText[@name='username' or @name='handle'");

    public BaseUserDetailsOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    protected void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    private By getUserDetailLocator(String detailName, String expectedValue) {
        switch (detailName.toLowerCase()) {
            case "name":
                return By.xpath(xpathStrNameByValue.apply(expectedValue));
            case "unique username":
                return By.xpath(xpathStrUniqueUsernameByUsername.apply(expectedValue));
            case "address book name":
                return By.xpath(xpathStrABNameByName.apply(expectedValue));
            case "common friends count":
                return By.xpath(xpathStrCommonFriendsByCount.apply(Integer.parseInt(expectedValue.trim())));
            default:
                throw new IllegalArgumentException(String.format("Unknown user detail name '%s'", detailName));
        }
    }

    private By getUserDetailLocator(String detailName) {
        switch (detailName.toLowerCase()) {
            case "name":
                return xpathName;
            case "unique username":
                return xpathUniqueUsername;
            case "address book name":
                return nameABName;
            case "common friends count":
                return nameCommonFriends;
            default:
                throw new IllegalArgumentException(String.format("Unknown user detail name '%s'", detailName));
        }
    }

    public boolean isUserDetailVisible(String detailName, String value) throws Exception {
        final By locator = getUserDetailLocator(detailName, value);
        return isLocatorDisplayed(locator);
    }

    public boolean isUserDetailInvisible(String detailName, String value) throws Exception {
        final By locator = getUserDetailLocator(detailName, value);
        return isLocatorInvisible(locator);
    }

    public boolean isUserDetailVisible(String detailName) throws Exception {
        final By locator = getUserDetailLocator(detailName);
        return isLocatorDisplayed(locator);
    }

    public boolean isUserDetailInvisible(String detailName) throws Exception {
        final By locator = getUserDetailLocator(detailName);
        return isLocatorInvisible(locator);
    }

    public BufferedImage getProfilePictureScreenshot() throws Exception {
        return getElementScreenshot(getElement(xpathProfilePicture)).orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot of profile picture")
        );
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return nameLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return nameRightActionButton;
    }
}
