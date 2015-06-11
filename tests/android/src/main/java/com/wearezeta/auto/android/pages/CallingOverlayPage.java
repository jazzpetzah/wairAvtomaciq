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
	@SuppressWarnings("unused")
	private static final String idCallingMute = "cib__calling_mute";

	@SuppressWarnings("unused")
	private static final String idCallingAccept = "gtv__calling__accept";

	private static final String idCallingOverlayContainer = "coc__calling__overlay_container";
	@FindBy(id = idCallingOverlayContainer)
	private WebElement callingOverlayContainer;

	private static final String idOngoingCallMicrobar = "ocpv__ongoing";
	@FindBy(id = idOngoingCallMicrobar)
	private WebElement ongoingCallMicrobar;

	private static final String idOngoingCallMinibar = "ocpv__ongoing_small";
	@FindBy(id = idOngoingCallMinibar)
	private WebElement ongoingCallMinibar;

	private static final String idIncominCallerAvatar = "civ__calling";
	@FindBy(id = idIncominCallerAvatar)
	private WebElement incominCallerAvatar;

	private static final String idIgnoreButton = "cib__calling_mute";
	@FindBy(id = idIgnoreButton)
	private WebElement ignoreButton;

	private static final String idAcceptButton = "gtv__calling__accept";
	@FindBy(id = idAcceptButton)
	private WebElement acceptButton;

	private static final Function<String, String> xpathCallingBarCaptionByName = name -> String
			.format("//*[@id='ttv__calling__message' and @value = '%s']", name);

	private static final String idCallMessage = "ttv__calling__message";
	@FindBy(id = idCallMessage)
	private WebElement callMessage;

	private static final String idCallingDismiss = "cib__calling__dismiss";
	@FindBy(id = idCallingDismiss)
	private WebElement callingDismiss;

	private static final String idCallingSpeaker = "cib__calling__speaker";
	@FindBy(id = idCallingSpeaker)
	private WebElement callingSpeaker;

	private static final String idCallingMicMute = "cib__calling__mic_mute";
	@FindBy(id = idCallingMicMute)
	private WebElement callingMicMute;

	public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	private static final int VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean isVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilNotVisible() throws Exception {
		final By locator = By.id(idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public void muteConversation() {
		ignoreButton.click();
	}

	public boolean waitUntilNameAppearsOnCallingBar(String name)
			throws Exception {
		final By locator = By.xpath(xpathCallingBarCaptionByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public DialogPage acceptCall() throws Exception {
		acceptButton.click();
		return new DialogPage(getLazyDriver());
	}

	public boolean incominCallerAvatarIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callMessage);
	}

	public boolean callingMessageIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callMessage);
	}

	public boolean callingDismissIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callingDismiss);
	}

	public boolean callingSpeakerIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callingSpeaker);
	}

	public boolean callingMicMuteIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callingMicMute);
	}

	public boolean callingOverlayIsVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(callingOverlayContainer);
	}

	public boolean ongoingCallMicrobarIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(ongoingCallMicrobar);
	}

	public boolean ongoingCallMinibarIsVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(ongoingCallMinibar);
	}
}
