package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingOverlayPage extends AndroidPage {

	@FindBy(id = AndroidLocators.CallingOverlayPage.idCallingOverlayContainer)
	private WebElement callingOverlayContainer;

	@FindBy(id = AndroidLocators.CallingOverlay.idOngoingCallMicrobar)
	private WebElement ongoingCallMicrobar;

	@FindBy(id = AndroidLocators.CallingOverlay.idOngoingCallMinibar)
	private WebElement ongoingCallMinibar;

	@FindBy(id = AndroidLocators.CallingOverlay.idIncominCallerAvatar)
	private WebElement incominCallerAvatar;

	@FindBy(id = AndroidLocators.CallingOverlayPage.idIgnoreButton)
	private WebElement ignoreButton;

	@FindBy(id = AndroidLocators.CallingOverlayPage.idAcceptButton)
	private WebElement acceptButton;

	public static final Function<String, String> xpathCallingBarCaptionByName = name -> String
			.format("//*[@id='ttv__calling__message' and @value = '%s']", name);

	@FindBy(id = AndroidLocators.CallingOverlay.idCallMessage)
	private WebElement callMessage;

	@FindBy(id = AndroidLocators.CallingOverlay.idCallingDismiss)
	private WebElement callingDismiss;

	@FindBy(id = AndroidLocators.CallingOverlay.idCallingSpeaker)
	private WebElement callingSpeaker;

	@FindBy(id = AndroidLocators.CallingOverlay.idCallingMicMute)
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
		final By locator = By
				.id(AndroidLocators.CallingOverlayPage.idCallingOverlayContainer);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				VISIBILITY_TIMEOUT_SECONDS);
	}

	public boolean waitUntilNotVisible() throws Exception {
		final By locator = By
				.id(AndroidLocators.CallingOverlayPage.idCallingOverlayContainer);
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
