package com.wearezeta.auto.android.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingOverlayPage extends AndroidPage {
    private static final String idStrCallingContainer = "fl__calling__container";
    private static final By idCallingContainer = By.id(idStrCallingContainer);

    private static final By idCallingOverlay = By.id("fl__calling__overlay");

    private static final By idGroupCallingJoinOverlayContainer = By.id("ll__group_call__not_joined_container");

    private static final By idCallingControls = By.id("iccv__incoming_call_controls");

    private static final String idStrJoinButton = "ttv__group_call__not_joined_text";
    private static final Function<String, String> xpathJoinButton = name -> String
        .format("//*[@id='%s' and contains(@value, '%s') and @shown='true']", idStrJoinButton, name.toUpperCase());

    private static final String idStrCallMessage = "ttv__calling__header__name";
    private static final Function<String, String> xpathCallingBarCaptionByName = name -> String
        .format("//*[@id='%s' and contains(@value, '%s')]", idStrCallMessage, name);

    private static final String idStrCallingDuration = "ttv__calling__header__duration";
    private static final By idCallingDuration = By.id(idStrCallingDuration);
    private static final Function<String, String> idCallingDurationByText = txt -> String
        .format("//*[@id='%s' and contains(@value, '%s')]", idStrCallingDuration, txt);

    private static final By idCallingDismiss = By.id("ccbv__calling_controls__hangup");

    public static final By idCallingSpeaker = By.id("ccbv__calling_controls__right_button");

    public static final By idCallingMicMute = By.id("ccbv__calling_controls__mute");

    private static final By xpathGroupCallParticipantChathead = By
        .xpath("//*[@id='rv__calling__container']//*[@id='ttv__calling_user_chathead_view']");

    private static final By xpathGroupCallIsFullAlertTitle = By
        .xpath("//DialogTitle[@id='alertTitle' and @value='The call is full']");

    private static final By idGroupCallIsFullOKButton = By.id("button3");

    private static final By xpathAnswerCallAlertTitle = By.xpath("//DialogTitle[@id='alertTitle' and @value='Answer call?']");

    private static final By idAnswerCallCancelButton = By.id("button2");

    private static final By idAnswerCallContinueButton = By.id("button1");

    private static final By xpathEndCurrentCallAlertTitle = By.xpath("//*[@value='Hang up current call?']");

    private static final By idEndCurrentCallCancelButton = By.id("button2");

    private static final By idEndCurrentCallContinueButton = By.id("button1");

    public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingContainer, VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idCallingContainer);
    }

    public void ignoreCall() throws Exception {
        DriverUtils.tapOnPercentOfElement(getDriver(), getElement(idCallingControls), 10, 50);
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        final By locator = By.xpath(xpathCallingBarCaptionByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    private static final String ESTABLISHED_CALL_TEXT = "00:07";
    public boolean waitUntilCallEstablished() throws Exception {
        final By locator = By.xpath(idCallingDurationByText.apply(ESTABLISHED_CALL_TEXT.toUpperCase()));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator, 30);
    }

    public void acceptCall() throws Exception {
        final Optional<WebElement> acceptButton = getElementIfDisplayed(idCallingControls);
        if (acceptButton.isPresent()) {
            DriverUtils.tapOnPercentOfElement(getDriver(), acceptButton.get(), 90, 50);
        } else {
            final Optional<WebElement> joinGroupCallButton = getElementIfDisplayed(idGroupCallingJoinOverlayContainer, 1);
            joinGroupCallButton.orElseThrow(() -> new IllegalStateException("No Accept or JOIN CALL button visible")).click();
        }
    }

    public boolean callingDismissIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingDismiss);
    }

    public boolean callingSpeakerIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingSpeaker);
    }

    public boolean callingMicMuteIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingMicMute);
    }

    public boolean waitUntilGroupCallJoinVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGroupCallingJoinOverlayContainer,
            VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilGroupCallJoinNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idGroupCallingJoinOverlayContainer,
            VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilJoinGroupCallButtonVisible(String name) throws Exception {
        final By locator = By.xpath(xpathJoinButton.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilJoinGroupCallButtonNotVisible(String name) throws Exception {
        final By locator = By.xpath(xpathJoinButton.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, VISIBILITY_TIMEOUT_SECONDS);
    }

    public void joinGroupCall() throws Exception {
        getElement(idGroupCallingJoinOverlayContainer, "JOIN CALL button is not visible").click();
    }

    public boolean ongoingCallMicrobarIsVisible() throws Exception {
        // FIXME: Find a better way to detect microbar visibility
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingOverlay);
    }

    public boolean ongoingCallMinibarIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCallingOverlay)
            && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallParticipantChathead);
    }

    public int numberOfParticipantsInGroupCall() throws Exception {
        return selectVisibleElements(xpathGroupCallParticipantChathead).size();
    }

    public boolean isGroupCallFullAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGroupCallIsFullAlertTitle);
    }

    public void closeGroupCallFullAlert() throws Exception {
        getElement(idGroupCallIsFullOKButton).click();
    }

    public boolean isAnswerCallAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathAnswerCallAlertTitle);
    }

    public void answerCallContinue() throws Exception {
        getElement(idAnswerCallContinueButton).click();
    }

    public void answerCallCancel() throws Exception {
        getElement(idAnswerCallCancelButton).click();
    }

    public boolean isEndCurrentCallAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathEndCurrentCallAlertTitle);
    }

    public void endCurrentCallContinue() throws Exception {
        getElement(idEndCurrentCallContinueButton).click();
    }

    public void endCurrentCallCancel() throws Exception {
        getElement(idEndCurrentCallCancelButton).click();
    }

    public void tapMuteMicButton() throws Exception {
        getElement(idCallingMicMute).click();
    }

    public void tapSpeakerButton() throws Exception {
        getElement(idCallingSpeaker).click();
    }

    public void tapDismissButton() throws Exception {
        getElement(idCallingDismiss).click();
    }

    public boolean ongoingCallMinibarIsInvisible() throws Exception {
        return ongoingCallMicrobarIsVisible();
    }
}
