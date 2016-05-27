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

    private static final String nameStrEndCallButton = "LeaveCallButton";

    private static final String nameStrSpeakersButton = "CallSpeakerButton";

    private static final String nameStrIgnoreCallButton = "IgnoreButton";

    private static final String nameStrAcceptVideoCallButton = "AcceptVideoButton";

    protected static final String nameStrMuteCallButton = "CallMuteButton";
    protected static final By nameMuteCallButton = MobileBy.AccessibilityId(nameStrMuteCallButton);

    protected static final String nameStrCallVideoButton = "CallVideoButton";
    protected static final By nameCallVideoButton = MobileBy.AccessibilityId(nameStrCallVideoButton);

    private static final String nameStrSwitchCameraButton = "SwitchCameraButton";

    private static final String nameStrAcceptCallButton = "AcceptButton";

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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallFullMessage,
                WAIT_FOR_GROUPCALL_FULL_MSG);
    }

    protected String getButtonAccessibilityIdByName(final String name) {
        switch (name) {
            case "Ignore":
                return nameStrIgnoreCallButton;
            case "Mute":
                return nameStrMuteCallButton;
            case "Leave":
                return nameStrEndCallButton;
            case "Accept":
                return nameStrAcceptCallButton;
            case "Accept Video":
                return nameStrAcceptVideoCallButton;
            case "Call Video":
                return nameStrCallVideoButton;
            case "Call Speaker":
                return nameStrSpeakersButton;
            case "Switch Camera":
                return nameStrSwitchCameraButton;
            default:
                throw new IllegalArgumentException(String.format("Button name '%s' is unknown", name));
        }
    }

    protected By getButtonLocatorByName(final String name) {
        return MobileBy.AccessibilityId(getButtonAccessibilityIdByName(name));
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
