package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

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

    private static final By nameRemoveFromConversation = By.name("OtherUserMetaControllerRightButton");

    private static final By nameOtherProfilePageCloseButton = By.name("OtherUserProfileCloseButton");

    private static final By nameCancelRequestConfirmationLabel = By.name("Cancel Request?");

    public OtherUserOnPendingProfilePage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isClosePageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameOtherProfilePageCloseButton);
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOtherProfileCancelRequestButton);
    }

    public void clickCancelRequestButton() throws Exception {
        getElement(xpathOtherProfileCancelRequestButton).click();
    }

    public boolean isCancelRequestConfirmationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCancelRequestConfirmationLabel, 3);
    }

    public void clickConfirmCancelRequestButton() throws Exception {
        getElement(xpathCancelRequestYesButton).click();
    }

    public boolean isCancelRequestLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathOtherProfilePageCancelRequestLabel);
    }

    public void clickStartConversationButton() throws Exception {
        getElement(xpathOtherProfileCancelRequestButton).click();
    }

    public boolean isUserNameDisplayed(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(name), 10);
    }

    public boolean isRemoveFromGroupConversationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameRemoveFromConversation);
    }
}
