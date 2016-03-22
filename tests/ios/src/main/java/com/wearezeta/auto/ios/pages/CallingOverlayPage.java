package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class CallingOverlayPage extends IOSPage {
    private static final By nameCallStatusLabel = MobileBy.AccessibilityId("CallStatusLabel");

    private static final By nameEndCallButton = MobileBy.AccessibilityId("LeaveCallButton");

    private static final By nameSpeakersButton = MobileBy.AccessibilityId("CallSpeakerButton");

    private static final By nameIgnoreCallButton = MobileBy.AccessibilityId("IgnoreButton");

    private static final By nameAcceptVideoCallButton = MobileBy.AccessibilityId("AcceptVideoButton");

    protected static final By nameMuteCallButton = MobileBy.AccessibilityId("CallMuteButton");

    protected static final By nameCallVideoButton = MobileBy.AccessibilityId("CallVideoButton");

    private static final By nameSwitchCameraButton = MobileBy.AccessibilityId("SwitchCameraButton");

    private static final By nameAcceptCallButton = MobileBy.AccessibilityId("AcceptButton");

    private static final Function<String, String> xpathStrCallingMessageByText = text ->
            String.format("//*[@name='CallStatusLabel' and contains(@value, '%s')]", text);

    private static final By nameSecondCallAlert = MobileBy.AccessibilityId("Answer call?");

    private static final By nameAnswerCallAlertButton = MobileBy.AccessibilityId("Answer");

    private static final By xpathGroupCallAvatars = By.xpath(
            "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell");

    private static final By xpathMuteButtonSelected = By.xpath("//UIAButton[@name='CallMuteButton' and @value='1']");

    private static final By xpathMuteButtonNotSelected = By.xpath("//UIAButton[@name='CallMuteButton' and @value='']");

    private static final By xpathGroupCallFullMessage = By.xpath("//UIAAlert[@name='The call is full']");

    private static final Integer WAIT_FOR_GROUPCALL_FULL_MSG  = 20;

    public CallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCallStatusLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCallStatusLabel);
    }

    public boolean isCallStatusLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCallStatusLabel);
    }

    public boolean isSecondCallAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSecondCallAlert);
    }

    public void pressAnswerCallAlertButton() throws Exception {
        getElement(nameAnswerCallAlertButton).click();
    }

    public int getNumberOfParticipantsAvatars() throws Exception {
        return getElements(xpathGroupCallAvatars).size();
    }

    public boolean isGroupCallFullMessageShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallFullMessage, WAIT_FOR_GROUPCALL_FULL_MSG);
    }

    protected By getButtonLocatorByName(final String name) {
        switch (name) {
            case "Ignore":
                return nameIgnoreCallButton;
            case "Mute":
                return nameMuteCallButton;
            case "Leave":
                return nameEndCallButton;
            case "Accept":
                return nameAcceptCallButton;
            case "Accept Video":
                return nameAcceptVideoCallButton;
            case "Call Video":
                return nameCallVideoButton;
            case "Call Speaker":
                return nameSpeakersButton;
            case "Switch Camera":
                return nameSwitchCameraButton;
            default:
                throw new IllegalArgumentException(String.format("Button name '%s' is unknown", name));
        }
    }

    public void tapButtonByName(String name) throws Exception {
        getElement(getButtonLocatorByName(name)).click();
    }

    public boolean isCallingMessageContainingVisible(String text) throws Exception {
        final By locator = By.xpath(xpathStrCallingMessageByText.apply(text));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 20);
    }

    public boolean isButtonVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getButtonLocatorByName(name), 20);
    }

    public boolean isButtonInvisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getButtonLocatorByName(name));
    }

    public boolean isMuteButtonSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathMuteButtonSelected);
    }

    public boolean isMuteButtonNotSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathMuteButtonNotSelected);
    }

    public boolean isVideoButtonSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathMuteButtonSelected);
    }

    public boolean isVideoButtonNotSelected() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathMuteButtonNotSelected);
    }

    public BufferedImage getMuteButtonScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(nameMuteCallButton)).orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot of Mute button")
        );
    }
}
