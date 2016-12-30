package com.wearezeta.auto.android.pages.details_overlay;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class BaseDetailsOverlay extends AndroidPage {
    private static final By xpathVerifiedShield = By.id("sv__otr__verified_shield");

    private static final By xpathLeftActionButton =
            By.xpath("//*[@id='gtv__participants__left__action' and @shown='true']");
    private static final By xpathRightActionButton =
            By.xpath("//*[@id='gtv__participants__right__action' and @shown='true']");
    private static final By xpathCloseButton =
            By.xpath("//*[@id='gtv__participants__close' and @shown='true']");

    public BaseDetailsOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        WebElement element = getElement(locator);
        if (!DriverUtils.waitUntilElementClickable(getDriver(), element)) {
            throw new IllegalStateException(String.format("The button/element '%s' is not clickable", name));
        }
        element.click();
    }

    public boolean waitUntilButtonVisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilButtonInvisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    protected abstract By getButtonLocatorByName(String name);

    protected By getLeftActionButtonLocator() {
        return xpathLeftActionButton;
    }

    protected By getRightActionButtonLocator() {
        return xpathRightActionButton;
    }

    protected By getCloseButtonLocator() {
        return xpathCloseButton;
    }

    protected boolean waitUntilShieldIconVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathVerifiedShield);
    }

    protected boolean waitUntilShieldIconInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathVerifiedShield);
    }
}