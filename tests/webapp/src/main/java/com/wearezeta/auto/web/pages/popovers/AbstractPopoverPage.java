package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

abstract class AbstractPopoverPage extends WebPage {
	private AbstractPopoverContainer container;

	public AbstractPopoverContainer getContainer() {
		return container;
	}

	public AbstractPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver);
		this.container = container;
	}

	protected abstract String getXpathLocator();

	public boolean isCurrent() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(getXpathLocator()));
	}

	protected WebElement getSharedElement(String relativeXpath)
			throws Exception {
		return this.getDriver().findElement(
				By.xpath(String.format("%s%s", this.getContainer()
						.getXpathLocator(), relativeXpath)));
	}
}
