package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletCallingOverlayPage extends AndroidTabletPage {

	public TabletCallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private CallingOverlayPage getCallingOverlayPage() throws Exception {
		return this.getAndroidPageInstance(CallingOverlayPage.class);
	}

	public boolean callingOverlayIsVisible() throws Exception {
		return getCallingOverlayPage().waitUntilVisible();
	}

	public boolean callingOverlayNotVisible() throws Exception {
		return getCallingOverlayPage().waitUntilNotVisible();
	}

	public void tapAcceptButton() throws Exception {
		getCallingOverlayPage().acceptCall();
	}

	public void tapMuteMicButton() throws Exception {
		getCallingOverlayPage().tapMuteMicButton();
	}
	
	public void tapIgnoreButton() throws Exception {
		getCallingOverlayPage().ignoreCall();
	}

	public void tapSpeakerButton() throws Exception {
		getCallingOverlayPage().tapSpeakerButton();
	}

	public Optional<BufferedImage> getMuteButtonScreenshot() throws Exception {
		return this.getElementScreenshot(getCallingOverlayPage().muteMicButton);
	}

	public boolean waitUntilSpeakerButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(CallingOverlayPage.idCallingSpeaker));
	}

	public boolean waitUntilSpeakerButtonInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(CallingOverlayPage.idCallingSpeaker));
	}

	public void tapDismissButton() throws Exception {
		getCallingOverlayPage().tapDismissButton();
	}

	public boolean ongoingCallMinibarIsVisible() throws Exception {
		return getCallingOverlayPage().ongoingCallMinibarIsVisible();
	}

	public boolean ongoingCallMinibarIsInvisible() throws Exception {
		return getCallingOverlayPage().ongoingCallMinibarIsInvisible();
	}

	public void dismissBySwipeUp() throws Exception {
		getCallingOverlayPage().dismissBySwipeUp();
	}

	public boolean waitForCallParticipantVisible(String participant)
			throws Exception {
		return getCallingOverlayPage().waitUntilNameAppearsOnCallingBarAvatar(
				participant);
	}
}
