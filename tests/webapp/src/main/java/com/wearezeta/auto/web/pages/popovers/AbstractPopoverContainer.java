package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

public abstract class AbstractPopoverContainer extends WebPage {
	private final static int VISIBILITY_TIMEOUT = 3; // seconds

	public AbstractPopoverContainer(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	protected abstract String getXpathLocator();

	public void waitUntilVisibleOrThrowException() throws Exception {
		assert DriverUtils.isElementDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()), VISIBILITY_TIMEOUT) : "Popover "
				+ this.getXpathLocator()
				+ " has not been shown within "
				+ VISIBILITY_TIMEOUT + " seconds";
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()));
	}
}
