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

	private static final String idOngoingCallMicrobar = "ocpv__ongoing";

	private static final String idOngoingCallMinibar = "ocpv__ongoing_small";

	private static final String idIncominCallerAvatar = "civ__calling";

	private static final String idIgnoreButton = "cib__calling_mute";
	@FindBy(id = idIgnoreButton)
	private WebElement ignoreButton;

	private static final String idAcceptButton = "gtv__calling__accept";
	@FindBy(id = idAcceptButton)
	private WebElement acceptButton;

	private static final String idCallMessage = "ttv__calling__message";

	private static final Function<String, String> xpathCallingBarCaptionByName = name -> String
			.format("//*[@id='%s' and contains(@value, '%s')]", idCallMessage,
					name.toUpperCase());

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

	public void muteConversation() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idIgnoreButton)) : "Ignore button is not visible";
		ignoreButton.click();
	}

	public boolean waitUntilNameAppearsOnCallingBar(String name)
			throws Exception {
		final By locator = By.xpath(xpathCallingBarCaptionByName.apply(name));
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
				By.id(idIncominCallerAvatar));
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

	public boolean ongoingCallMicrobarIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idOngoingCallMicrobar));
	}

	public boolean ongoingCallMinibarIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idOngoingCallMinibar));
	}
}
