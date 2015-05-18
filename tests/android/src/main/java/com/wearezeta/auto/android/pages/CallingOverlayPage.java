package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

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

	@FindBy(id = AndroidLocators.CallingOverlayPage.idCallingUsersName)
	private WebElement callingUsersName;

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

	public boolean isVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(callingOverlayContainer);
	}

	public void muteConversation() {
		ignoreButton.click();
	}

	public String getCallersName() {
		return callingUsersName.getText();
	}

	public void acceptCall() {
		acceptButton.click();
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
