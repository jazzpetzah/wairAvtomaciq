package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.CallingLockscreenPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletFullScreenCallingOverlayPage extends AndroidTabletPage {

	public TabletFullScreenCallingOverlayPage(
			Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	private CallingLockscreenPage getCallingLockscreenPage() throws Exception {
		return (CallingLockscreenPage) this
				.getAndroidPageInstance(CallingLockscreenPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		try {
			return getCallingLockscreenPage().isVisible();
		} finally {
			ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
		}
	}

	public void acceptCall() throws Exception {
		getCallingLockscreenPage().acceptCall();
	}
}
