package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

import org.openqa.selenium.By;

public abstract class AbstractPopoverContainer extends WebPage {

	public AbstractPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private static int getDefaultLookupTimeoutSeconds() throws Exception {
		return Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(DriverUtils.class));
	}

	protected abstract String getXpathLocator();

	public void waitUntilVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()),
				getDefaultLookupTimeoutSeconds()) : "Popover "
				+ this.getXpathLocator() + " has not been shown within "
				+ getDefaultLookupTimeoutSeconds() + " seconds";
	}

	public void waitUntilNotVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(this.getXpathLocator()),
				getDefaultLookupTimeoutSeconds()) : "Popover "
				+ this.getXpathLocator() + " has not been hidden within "
				+ getDefaultLookupTimeoutSeconds() + " seconds";
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()));
	}
}
