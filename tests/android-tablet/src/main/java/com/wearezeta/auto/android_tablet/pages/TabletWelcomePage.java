package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletWelcomePage extends AndroidTabletPage {
	public TabletWelcomePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private WelcomePage getWelcomePage() throws Exception {
		return (WelcomePage) this.getAndroidPageInstance(WelcomePage.class);
	}

	public boolean waitForInitialScreen() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(WelcomePage.idHaveAccountButton), 15);
	}

	public void clickIHaveAnAccount() throws Exception {
		getWelcomePage().clickIHaveAnAccount();
	}
}
