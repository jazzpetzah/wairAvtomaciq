package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CallingOverlayPage extends IOSPage {
    private static final String nameStrCallStatusLabel = "CallStatusLabel";
    private static final By nameCallStatusLabel = MobileBy.AccessibilityId(nameStrCallStatusLabel);

    private static final String nameStrEndCallButton = "LeaveCallButton";

    private static final String nameStrSpeakersButton = "CallSpeakerButton";

    private static final String nameStrIgnoreCallButton = "IgnoreButton";

    private static final String nameStrAcceptVideoCallButton = "AcceptVideoButton";

    protected static final String nameStrMuteCallButton = "CallMuteButton";
    protected static final By nameMuteCallButton = MobileBy.AccessibilityId(nameStrMuteCallButton);

    protected static final String nameStrCallVideoButton = "CallVideoButton";

    private static final String nameStrSwitchCameraButton = "SwitchCameraButton";

    private static final String nameStrAcceptCallButton = "AcceptButton";

    private static final By nameCallingMessage = MobileBy.AccessibilityId("CallStatusLabel");

    private static final By nameSecondCallAlert = MobileBy.AccessibilityId("Answer call?");

    private static final By nameAnswerCallAlertButton = MobileBy.AccessibilityId("Answer");


    private static final Function<Integer, String> xpathStrGroupCallAvatarsByCount = count ->
            String.format("//XCUIElementTypeStaticText[@name='%s']/" +
                    "following::XCUIElementTypeCollectionView[count(XCUIElementTypeCell)=%s]",
                    nameStrCallStatusLabel, count);

    private static final By xpathCallerAvatar = By.xpath(String.format(
            "//XCUIElementTypeStaticText[@name='%s']/following::*[@name='CallingUsersImage']",
            nameStrCallStatusLabel));

    private static final By nameGroupCallFullMessage = MobileBy.AccessibilityId("The call is full");

    private static final Integer WAIT_FOR_GROUPCALL_FULL_MSG = 20;

    public CallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCallStatusLabelVisible() throws Exception {
        return isLocatorDisplayed(nameCallStatusLabel);
    }

    public boolean isCallStatusLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCallStatusLabel);
    }

    public boolean isSecondCallAlertVisible() throws Exception {
        return isLocatorDisplayed(nameSecondCallAlert);
    }

    public void pressAnswerCallAlertButton() throws Exception {
        getElement(nameAnswerCallAlertButton).click();
    }

    public boolean isGroupCallFullMessageShown() throws Exception {
        return isLocatorDisplayed(nameGroupCallFullMessage, WAIT_FOR_GROUPCALL_FULL_MSG);
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
        // XPath locators are bloody slow here
        final long msStarted = System.currentTimeMillis();
        final WebElement el = getElement(nameCallingMessage, "No calling overlay is visible after the timeout", 15);
        do {
            if (el.getText().contains(text)) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - msStarted <= 5000);
        return false;
    }

    public boolean isButtonVisible(String name) throws Exception {
        return isLocatorDisplayed(getButtonLocatorByName(name), 20);
    }

    public boolean isButtonInvisible(String name) throws Exception {
        return isLocatorInvisible(getButtonLocatorByName(name));
    }

    public boolean isMuteButtonSelected() throws Exception {
        final long msStarted = System.currentTimeMillis();
        final WebElement el = getElement(nameMuteCallButton);
        do {
            if (el.getText().equals("1")) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - msStarted <= 5000);
        return false;
    }

    public boolean isMuteButtonNotSelected() throws Exception {
        final long msStarted = System.currentTimeMillis();
        final WebElement el = getElement(nameMuteCallButton);
        do {
            if (el.getText().equals("")) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - msStarted <= 5000);
        return false;
    }

    public boolean isVideoButtonSelected() throws Exception {
        return isMuteButtonSelected();
    }

    public boolean isVideoButtonNotSelected() throws Exception {
        return isMuteButtonNotSelected();
    }

    public BufferedImage getMuteButtonScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(nameMuteCallButton)).orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot of Mute button")
        );
    }

    public boolean isCountOfAvatarsEqualTo(int expectedNumberOfAvatars, int timeoutSeconds) throws Exception {
        assert expectedNumberOfAvatars > 0 : "The expected number of avatar should be greater than zero";
        By locator;
        if (expectedNumberOfAvatars == 1) {
            locator = xpathCallerAvatar;
        } else {
            locator = By.xpath(xpathStrGroupCallAvatarsByCount.apply(expectedNumberOfAvatars));
        }
        return isLocatorDisplayed(locator, timeoutSeconds);
    }
}
