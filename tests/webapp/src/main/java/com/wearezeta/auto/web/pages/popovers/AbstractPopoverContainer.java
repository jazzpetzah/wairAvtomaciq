package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

import com.wearezeta.auto.web.pages.WebPage;

import org.openqa.selenium.By;

public abstract class AbstractPopoverContainer extends WebPage {

	private final static int VISIBILITY_TIMEOUT = 3; // seconds

	public AbstractPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected abstract String getXpathLocator();

	public void waitUntilVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()), VISIBILITY_TIMEOUT) : "Popover "
				+ this.getXpathLocator()
				+ " has not been shown within "
				+ VISIBILITY_TIMEOUT + " seconds";
	}

	public void waitUntilNotVisibleOrThrowException() throws Exception {
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(this.getXpathLocator()), VISIBILITY_TIMEOUT) : "Popover "
				+ this.getXpathLocator()
				+ " has not been hidden within "
				+ VISIBILITY_TIMEOUT + " seconds";
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()));
	}
}
