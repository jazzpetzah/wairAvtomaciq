package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;

abstract class AbstractPopoverPage extends WebPage {
	private AbstractPopoverContainer container;

	public AbstractPopoverContainer getContainer() {
		return container;
	}

	public AbstractPopoverPage(ZetaWebAppDriver driver, WebDriverWait wait,
			AbstractPopoverContainer container) throws Exception {
		super(driver, wait);
		this.container = container;
	}

	protected abstract String getXpathLocator();

	public boolean isCurrent() throws Exception {
		return DriverUtils.isElementDisplayed(driver,
				By.xpath(getXpathLocator()));
	}

	protected WebElement getSharedElement(String relativeXpath) {
		return this.getDriver().findElement(
				By.xpath(String.format("%s%s", this.getContainer()
						.getXpathLocator(), relativeXpath)));
	}
}
