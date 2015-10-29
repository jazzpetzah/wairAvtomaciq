package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.SettingsPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSettingsPage extends AndroidTabletPage {

	public TabletSettingsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private SettingsPage getSettingsPage() throws Exception {
		return this.getAndroidPageInstance(SettingsPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return getSettingsPage().waitUntilVisible();
	}

	public void selectMenuItem(String itemName) throws Exception {
		getSettingsPage().selectMenuItem(itemName);
	}

	public void confirmSignOut() throws Exception {
		getSettingsPage().confirmSignOut();
	}

}
