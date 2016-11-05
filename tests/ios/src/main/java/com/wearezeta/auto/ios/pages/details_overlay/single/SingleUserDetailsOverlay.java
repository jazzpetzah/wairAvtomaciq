package com.wearezeta.auto.ios.pages.details_overlay.single;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class SingleUserDetailsOverlay extends BaseUserDetailsOverlay {
    public static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    public static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final Function<String, String> xpathStrUserNameByTitle = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrABNameByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrEmailByText = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());

    public SingleUserDetailsOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return nameLeftActionButton;
    }

    @Override
    protected By getRightActionButtonLocator() {
        return nameRightActionButton;
    }

    public boolean isProfileNameVisible(String name) throws Exception {
        final By locator = MobileBy.AccessibilityId(name);
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isUserNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserNameByTitle.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserAddressBookNameVisible(String addressbookName) throws Exception {
        final By locator = By.xpath(xpathStrABNameByName.apply(addressbookName));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrEmailByText.apply(email));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserEmailNotVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrEmailByText.apply(email));
        return isLocatorInvisible(locator);
    }
}
