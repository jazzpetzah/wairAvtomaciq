package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CallingOverlayPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingOverlayContainer")
	private WebElement callingOverlayContainer;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idOngoingCallMicrobar")
	private WebElement ongoingCallMicrobar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idOngoingCallMinibar")
	private WebElement ongoingCallMinibar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idIncominCallerAvatar")
	private WebElement incominCallerAvatar;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idIgnoreButton")
	private WebElement ignoreButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idAcceptButton")
	private WebElement acceptButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingUsersName")
	private WebElement callingUsersName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallMessage")
	private WebElement callMessage;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingDismiss")
	private WebElement callingDismiss;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingSpeaker")
	private WebElement callingSpeaker;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingMicMute")
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

	public void waitForCallersNameChanges(String name, int sec)
			throws InterruptedException {
		if (sec > 0 && callingUsersName.getText().equals(name)) {
			Thread.sleep(1000);
			sec--;
			waitForCallersNameChanges(name, sec);
		} else {
			return;
		}
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
