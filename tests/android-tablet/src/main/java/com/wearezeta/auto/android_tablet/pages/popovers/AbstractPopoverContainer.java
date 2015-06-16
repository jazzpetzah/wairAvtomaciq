package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

	public void tapInTheCenter() throws Exception {
		final WebElement self = this.getDriver().findElement(getLocator());
		DriverUtils.tapInTheCenterOfTheElement(getDriver(), self);
	}

	public void tapOutside() throws Exception {
		final WebElement self = this.getDriver().findElement(getLocator());
		DriverUtils.tapOutsideOfTheElement(getDriver(), self, 30, 10);
	}
}
