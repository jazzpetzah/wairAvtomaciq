package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.OutgoingPendingConnectionPage;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletOutgoingPendingConnectionPage extends AndroidTabletPage {

	public TabletOutgoingPendingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	private OutgoingPendingConnectionPage getAndroidOPCPage() throws Exception {
		return this.getAndroidPageInstance(OutgoingPendingConnectionPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return getAndroidOPCPage().waitUntilVisible();
	}

	public boolean waitUntilNameVisible(String expectedName) throws Exception {
		return getAndroidOPCPage().waitUntilNameVisible(expectedName);
	}
}
