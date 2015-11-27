package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.AndroidPage;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AndroidTabletPage extends AndroidPage {

	public AndroidTabletPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected <T extends AndroidPage> T getAndroidPageInstance(
			Class<T> pageClass) throws Exception {
		return this.instantiatePage(pageClass);
	}
}
