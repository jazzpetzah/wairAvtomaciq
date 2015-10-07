package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingOverlayPage extends AndroidPage {
	private static final String idCallingOverlayContainer = "rv__calling__container";
	@FindBy(id = idCallingOverlayContainer)
	private WebElement callingOverlayContainer;

	private static final String idGroupCallingJoinOverlayContainer = "ll__group_call__not_joined_container";
	@FindBy(id = idGroupCallingJoinOverlayContainer)
	private WebElement joinGroupCallButton;

	private static final String idIgnoreButton = "cib__calling_mute";
	@FindBy(id = idIgnoreButton)
	private WebElement ignoreButton;

	private static final String idAcceptButton = "gtv__calling__accept";
	@FindBy(id = idAcceptButton)
	private WebElement acceptButton;

	private static final String idJoinButton = "ttv__group_call__not_joined_text";
	private static final Function<String, String> xpathJoinButton = name -> String
			.format("//*[@id='%s' and contains(@value, '%s') and @shown='true']",
					idJoinButton, name.toUpperCase());

	private static final String idCallMessage = "ttv__calling__message";

	private static final Function<String, String> xpathCallingBarCaptionByName = name -> String
			.format("//*[@id='%s' and contains(@value, '%s')]", idCallMessage,
					name.toUpperCase());

	private static final Function<String, String> xpathCallingBarAvatarByName = name -> String
			.format("//*[@id='%s']//*[@value='%s']", idCallingOverlayContainer,
					name.toUpperCase());

	private static final String idCallingDismiss = "cib__calling__dismiss";
	@FindBy(id = idCallingDismiss)
	private WebElement dismissButton;

	public static final String idCallingSpeaker = "cib__calling__speaker";
	@FindBy(id = idCallingSpeaker)
	public WebElement speakerButton;

	private static final String idCallingMicMute = "cib__calling__mic_mute";
	@FindBy(id = idCallingMicMute)
	public WebElement muteMicButton;

	private static final String xpathGroupCallParticipantChathead = "//*[@id='rv__calling__container']/*";

	private static final String xpathGroupCallIsFullAlertTitle = "//DialogTitle[@id='alertTitle' and @value='The call is full']";

	private static final String idGroupCallIsFullOKButton = "button3";
	@FindBy(id = idGroupCallIsFullOKButton)
	private WebElement groupCallIsFullOKButton;

	private static final String xpathAnswerCallAlertTitle = "//DialogTitle[@id='alertTitle' and @value='Answer call?']";

	private static final String idAnswerCallCancelButton = "button2";
	@FindBy(id = idAnswerCallCancelButton)
	private WebElement answerCallCancelButton;

	private static final String idAnswerCallContinueButton = "button1";
	@FindBy(id = idAnswerCallContinueButton)
	private WebElement answerCallContinueButton;

	private static final String xpathEndCurrentCallAlertTitle = "//DialogTitle[@id='alertTitle' and @value='End current call?']";

	private static final String idEndCurrentCallCancelButton = "button2";
	@FindBy(id = idEndCurrentCallCancelButton)
	private WebElement endCurrentCallCancelButton;

	private static final String idEndCurrentCallContinueButton = "button1";
	@FindBy(id = idEndCurrentCallContinueButton)
	private WebElement endCurrentCallContinueButton;

	public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

	public boolean waitUntilVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilNotVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void muteConversation() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idIgnoreButton)) : "Ignore button is not visible";
		ignoreButton.click();
	}

	public boolean waitUntilNameAppearsOnCallingBarCaption(String name)
			throws Exception {
		final By locator = By.xpath(xpathCallingBarCaptionByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilNameAppearsOnCallingBarAvatar(String name)
			throws Exception {
		final By locator = By.xpath(xpathCallingBarAvatarByName.apply(name));
		// isDisplayed sometimes does not work properly here
		return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
	}

	public DialogPage acceptCall() throws Exception {
		boolean acceptButtonVisible = DriverUtils.waitUntilLocatorIsDisplayed(
				getDriver(), By.id(idAcceptButton));
		boolean joinGroupCallVisible = true;
		if (acceptButtonVisible) {
			acceptButton.click();
		} else {
			joinGroupCallVisible = DriverUtils.waitUntilLocatorIsDisplayed(
					getDriver(), By.id(idGroupCallingJoinOverlayContainer));
			if (joinGroupCallVisible) {
				joinGroupCallButton.click();
			}
		}
		assert acceptButtonVisible || joinGroupCallVisible : "No Accept or JOIN CALL button visible";
		return new DialogPage(getLazyDriver());
	}

	public boolean callingMessageIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallMessage));
	}

	public boolean callingDismissIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallingDismiss));
	}

	public boolean callingDismissIsNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idCallingDismiss));
	}

	public boolean callingSpeakerIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallingSpeaker));
	}

	public boolean callingMicMuteIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallingMicMute));
	}

	public boolean callingMicMuteIsNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idCallingMicMute));
	}

	public boolean waitUntilGroupCallJoinVisible() throws Exception {
		final By locator = By.id(idGroupCallingJoinOverlayContainer);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilGroupCallJoinNotVisible() throws Exception {
		final By locator = By.id(idGroupCallingJoinOverlayContainer);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilJoinGroupCallButtonVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathJoinButton.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilJoinGroupCallButtonNotVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathJoinButton.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public DialogPage joinGroupCall() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idGroupCallingJoinOverlayContainer)) : "JOIN CALL button is not visible";
		joinGroupCallButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean ongoingCallMicrobarIsVisible() throws Exception {
		return waitUntilVisible() && callingMicMuteIsNotVisible()
				&& callingDismissIsNotVisible();
	}

	public boolean ongoingCallMinibarIsVisible() throws Exception {
		return waitUntilVisible() && callingMicMuteIsVisible()
				&& callingDismissIsVisible();
	}

	public int numberOfParticipantsInGroupCall() throws Exception {
		final By searchCriteria = By.xpath(xpathGroupCallParticipantChathead);
		if (DriverUtils.waitUntilLocatorAppears(getDriver(), searchCriteria, 5)) {
			int ntry = 1;
			final int maxRetries = 3;
			do {
				try {
					return getDriver().findElements(searchCriteria).size();
				} catch (WebDriverException e) {
					e.printStackTrace();
					Thread.sleep(1500);
					ntry++;
				}
			} while (ntry <= maxRetries);
		}
		return 0;
	}

	public boolean isGroupCallFullAlertVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathGroupCallIsFullAlertTitle));
	}

	public DialogPage closeGroupCallFullAlert() throws Exception {
		groupCallIsFullOKButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean isAnswerCallAlertVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathAnswerCallAlertTitle));
	}

	public DialogPage answerCallContinue() throws Exception {
		answerCallContinueButton.click();
		return new DialogPage(getLazyDriver());
	}

	public DialogPage answerCallCancel() throws Exception {
		answerCallCancelButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean isEndCurrentCallAlertVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathEndCurrentCallAlertTitle));
	}

	public DialogPage endCurrentCallContinue() throws Exception {
		endCurrentCallContinueButton.click();
		return new DialogPage(getLazyDriver());
	}

	public DialogPage endCurrentCallCancel() throws Exception {
		endCurrentCallCancelButton.click();
		return new DialogPage(getLazyDriver());
	}

	public void tapMuteMicButton() {
		muteMicButton.click();
	}

	public void tapSpeakerButton() {
		speakerButton.click();
	}

	public void tapDismissButton() {
		dismissButton.click();
	}

	public boolean ongoingCallMinibarIsInvisible() throws Exception {
		return ongoingCallMicrobarIsVisible();
	}

	public void dismissBySwipeUp() throws Exception {
		final Point coords = callingOverlayContainer.getLocation();
		final Dimension elementSize = callingOverlayContainer.getSize();
		// We cannot swipe in the middle because of
		// https://wearezeta.atlassian.net/browse/AN-2568
		this.getDriver().swipe(coords.x + elementSize.width / 4,
				coords.y + elementSize.height - 20,
				coords.x + elementSize.width / 4, coords.y, 1000);
	}
}
