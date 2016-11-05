package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.IOSPage;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class BaseDetailsOverlay extends IOSPage {
    private static final By xpathVerifiedShield = MobileBy.AccessibilityId("VerifiedShield");

    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    public BaseDetailsOverlay(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    protected abstract By getButtonLocatorByName(String name);

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }

    public boolean isButtonVisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return isLocatorDisplayed(locator);
    }

    public boolean isButtonInvisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return isLocatorInvisible(locator);
    }

    public boolean isActionMenuVisible() throws Exception {
        return isLocatorDisplayed(xpathActionsMenu);
    }

    protected abstract By getLeftActionButtonLocator();

    protected abstract By getRightActionButtonLocator();

    protected boolean isShieldIconVisible() throws Exception {
        return isLocatorDisplayed(xpathVerifiedShield);
    }

    protected boolean isShieldIconNotVisible() throws Exception {
        return isLocatorInvisible(xpathVerifiedShield);
    }
}
