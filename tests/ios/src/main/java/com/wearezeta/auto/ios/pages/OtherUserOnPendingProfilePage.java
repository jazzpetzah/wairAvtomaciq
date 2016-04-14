package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserOnPendingProfilePage extends IOSPage {

    private static final By xpathOtherProfilePageCancelRequestLabel =
            By.xpath("//UIAStaticText[contains(@name, 'CANCEL REQUEST')]");

    private static final By xpathOtherProfileCancelRequestButton = By.xpath(
            "//UIAStaticText[contains(@name, 'CANCEL REQUEST')]" +
                    "/preceding-sibling::UIAButton[@name='OtherUserMetaControllerLeftButton']");

    private static final By xpathCancelRequestYesButton = By.xpath(
            "//UIAStaticText[@name='Cancel Request?']/following-sibling::UIAButton[@name='YES']");

    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameOtherProfilePageCloseButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By nameConnectConfirmButton = MobileBy.AccessibilityId("CONNECT");

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isClosePageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameOtherProfilePageCloseButton);
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOtherProfileCancelRequestButton);
    }

    public void tapCancelRequestButton() throws Exception {
        getElement(xpathOtherProfileCancelRequestButton).click();
    }

    public void confirmCancelRequest() throws Exception {
        getElement(xpathCancelRequestYesButton).click();
    }

    public boolean isCancelRequestLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathOtherProfilePageCancelRequestLabel);
    }

    public void tapStartConversationButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), MobileBy.AccessibilityId(name), 10);
    }

    public boolean isRemoveFromGroupConversationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameRightActionButton);
    }

    public void tapConnectButton() throws Exception {
        getElement(nameLeftActionButton).click();
    }

    public void confirmConnect() throws Exception {
        getElement(nameConnectConfirmButton).click();
    }
}
