package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

public class CallingOverlayPage extends IOSPage {
    private static final By nameCallStatusLabel = By.name("CallStatusLabel");

    private static final By nameEndCallButton = By.name("LeaveCallButton");

    private static final By nameSpeakersButton = By.name("CallSpeakerButton");

    private static final By nameIgnoreCallButton = By.name("IgnoreButton");

    private static final By nameAcceptVideoCallButton = By.name("AcceptVideoButton");

    private static final By nameMuteCallButton = By.name("CallMuteButton");

    private static final By nameCallVideoButton = By.name("CallVideoButton");

    private static final By nameSwitchCameraButton = By.name("SwitchCameraButton");

    private static final By nameAcceptCallButton = By.name("AcceptButton");

    private static final Function<String, String> xpathStrCallingMessageByText = text ->
            String.format("//*[@name='CallStatusLabel' and contains(@value, '%s')]", text);

    private static final By nameSecondCallAlert = By.name("Answer call?");

    private static final By nameAnswerCallAlertButton = By.name("Answer");

    private static final By xpathGroupCallAvatars = By.xpath(
            "//UIAWindow[@name='ZClientNotificationWindow']//UIACollectionCell");

    private static final By xpathGroupCallFullMessage = By.xpath("//UIAAlert[@name='The call is full']");


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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallFullMessage);
    }

    private By getButtonLocatorByName(final String name) {
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
