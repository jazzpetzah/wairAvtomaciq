package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class WarningPage extends WebPage {

	@FindBy(how = How.XPATH, using = WebAppLocators.WarningPage.xpathWarningClose)
	private WebElement warningCloseButton;

	public WarningPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		final String xpath = WebAppLocators.WarningPage.xpathWarning;
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpath));
	}

	public boolean isLinkWithCaptionVisible(String caption) throws Exception {
		final String locator = WebAppLocators.WarningPage.xpathLinkByCaption
				.apply(caption);
		return DriverUtils.waitUntilElementClickable(this.getDriver(),
				getDriver().findElement(By.xpath(locator)), 5);
	}

	public void closeWarning() {
		warningCloseButton.click();
	}
}
