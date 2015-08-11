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
		return (CallingOverlayPage) this
				.getAndroidPageInstance(CallingOverlayPage.class);
	}

	public boolean callingOverlayIsVisible() throws Exception {
		return getCallingOverlayPage().callingOverlayIsVisible();
	}

	public boolean incomingCallerAvatarIsVisible() throws Exception {
		return getCallingOverlayPage().incomingCallerAvatarIsVisible();
	}

	public boolean incomingCallerAvatarIsInvisible() throws Exception {
		return getCallingOverlayPage().incomingCallerAvatarIsInvisible();
	}

	public void tapAcceptButton() throws Exception {
		getCallingOverlayPage().acceptCall();
	}

	public void tapMuteButton() throws Exception {
		getCallingOverlayPage().tapMuteMicButton();
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
}
