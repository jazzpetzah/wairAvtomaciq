package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingOverlayPage extends AndroidPage {
	private static final String idCallingOverlayContainer = "coc__calling__overlay_container";

	private static final String idGroupCallingJoinOverlayContainer = "ll__group_call__not_joined_container";
	@FindBy(id = idGroupCallingJoinOverlayContainer)
	private WebElement joinGroupCallButton;

	private static final String idOngoingCallMicrobar = "v__calling__top_bar";

	private static final String idOngoingCallMinibar = "coc__calling__overlay_container";

	private static final String idIgnoreButton = "cib__calling_mute";
	@FindBy(id = idIgnoreButton)
	private WebElement ignoreButton;

	private static final String idAcceptButton = "gtv__calling__accept";
	@FindBy(id = idAcceptButton)
	private WebElement acceptButton;

	private static final String idJoinButton = "ttv__group_call__not_joined_text";
	private static final Function<String, String> xpathJoinButton = name -> String
			.format("//*[@id='%s' and contains(@value, '%s')]", idJoinButton,
					name.toUpperCase());

	private static final String idCallMessage = "ttv__calling__message";

	private static final Function<String, String> xpathCallingBarCaptionByName = name -> String
			.format("//*[@id='%s' and contains(@value, '%s')]", idCallMessage,
					name.toUpperCase());

	private static final String idIncomingCallerAvatarsContainer = "rv__calling__container";

	private static final Function<String, String> xpathCallingBarAvatarByName = name -> String
			.format("//*[@id='%s']//*[@value='%s']",
					idIncomingCallerAvatarsContainer, name.toUpperCase());

	private static final String idCallingDismiss = "cib__calling__dismiss";

	private static final String idCallingSpeaker = "cib__calling__speaker";

	private static final String idCallingMicMute = "cib__calling__mic_mute";

	public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean waitUntilVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilNotVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
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
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public DialogPage acceptCall() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idAcceptButton)) : "Accept button is not visible";
		acceptButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean incominCallerAvatarIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idIncomingCallerAvatarsContainer));
	}

	public boolean callingMessageIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallMessage));
	}

	public boolean callingDismissIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
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

	public boolean callingOverlayIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCallingOverlayContainer));
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
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, VISIBILITY_TIMEOUT_SECONDS);
	}
	
	public boolean waitUntilJoinGroupCallButtonNotVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathJoinButton.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, VISIBILITY_TIMEOUT_SECONDS);
	}
	
	public DialogPage joinGroupCall() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idGroupCallingJoinOverlayContainer)) : "Accept button is not visible";
		joinGroupCallButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean ongoingCallMicrobarIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idOngoingCallMicrobar));
	}

	public boolean ongoingCallMinibarIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idOngoingCallMinibar));
	}
}
