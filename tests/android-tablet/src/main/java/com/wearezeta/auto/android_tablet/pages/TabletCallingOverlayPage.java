package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.CallingOverlayPage;
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
}
