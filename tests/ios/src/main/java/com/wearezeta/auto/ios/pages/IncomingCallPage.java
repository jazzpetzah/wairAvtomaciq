package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class IncomingCallPage extends CallPage {
    private static final By nameAcceptCallButton = By.name("AcceptButton");

    private static final Function<String, String> xpathStrCallingMessageByName = name ->
            String.format("//*[contains(@value, '%s') and contains(@value, ' IS CALLING')]",
                    name.toUpperCase());

    private static final By xpathCallingMessage = By.xpath("//*[contains(@value, ' IS CALLING')]");

    private static final By nameIgnoreCallButton = By.name("IgnoreButton");

    private static final By nameCallingMessageUser = By.name("CallStatusLabel");

    private static final By nameJoinCallButton = By.name("JOIN CALL");

    private static final By nameSecondCallAlert = By.name("Answer call?");

    private static final By nameAnswerCallAlertButton = By.name("Answer");

    private static final By xpathGroupCallAvatars = By.xpath(
            "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell");

    private static final By xpathGroupCallFullMessage =
            By.xpath(xpathStrMainWindow + "/UIAAlert[@name='The call is full']");

    public IncomingCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCallingMessageVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathStrCallingMessageByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void acceptIncomingCallClick() throws Exception {
        getElement(nameAcceptCallButton, "Accept call button is not visible").click();
    }

    public void ignoreIncomingCallClick() throws Exception {
        getElement(nameIgnoreCallButton).click();
    }

    public boolean isCallingMessageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCallingMessage);
    }

    public boolean isGroupCallingMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), nameCallingMessageUser, 15);
    }

    public boolean isJoinCallBarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameJoinCallButton);
    }

    public boolean isSecondCallAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSecondCallAlert);
    }

    public void pressAnswerCallAlertButton() throws Exception {
        getElement(nameAnswerCallAlertButton).click();
    }

    public int getNumberOfGroupCallAvatar() throws Exception {
        return getElements(xpathGroupCallAvatars).size();
    }

    public void clickJoinCallButton() throws Exception {
        getElement(nameJoinCallButton).click();
    }

    public boolean isGroupCallFullMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallFullMessage);
    }

}



