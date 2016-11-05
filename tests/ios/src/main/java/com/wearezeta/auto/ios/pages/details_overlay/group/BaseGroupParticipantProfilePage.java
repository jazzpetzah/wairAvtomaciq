package com.wearezeta.auto.ios.pages.details_overlay.group;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class BaseGroupParticipantProfilePage extends BaseUserDetailsOverlay {
    private static final Function<String, String> xpathStrOtherPersonalInfoPageNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageAddressBookNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageEmailFieldByEmail = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());


    public BaseGroupParticipantProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void switchToTab(String tabName) throws Exception {
        super.switchToTab(tabName);
    }

    @Override
    protected By getRightActionButtonLocator() {
        return GroupDetailsOverlay.nameRightActionButton;
    }

    @Override
    protected By getLeftActionButtonLocator() {
        return GroupDetailsOverlay.namLeftActionButton;
    }

    public boolean isOtherUserProfileNameVisible(String name) throws Exception {
        final By locator = MobileBy.AccessibilityId(name);
        return selectVisibleElements(locator).size() > 0;
    }

    public boolean isUserNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageNameFieldByName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserAddressBookNameVisible(String addressbookName) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageAddressBookNameFieldByName.apply(addressbookName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUserEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserEmailNotVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }
}
