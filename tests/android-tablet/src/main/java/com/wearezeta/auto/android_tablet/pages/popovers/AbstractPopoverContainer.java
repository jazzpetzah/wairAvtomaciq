package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractPopoverContainer extends AndroidTabletPage {

	public AbstractPopoverContainer(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected abstract By getLocator();

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				getLocator());
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(getDriver(), getLocator());
	}
}
