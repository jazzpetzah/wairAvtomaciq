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

	@FindBy(how = How.XPATH, using = WebAppLocators.WarningPage.xpathMissingWebRTCSupportWarningBarClose)
	private WebElement missingWebRTCSupportWarningBarCloseButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.WarningPage.xpathAnotherCallWarningModalClose)
	private WebElement anotherCallWarningModalCloseButton;

	public WarningPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isMissingWebRTCSupportWarningBarVisible() throws Exception {
		final String xpath = WebAppLocators.WarningPage.xpathMissingWebRTCSupportWarningBar;
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpath));
	}

	public boolean isLinkWithCaptionInMissingWebRTCSupportWarningBarVisible(
			String caption) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.WarningPage.xpathMissingWebRTCSupportWarningBarLinkByCaption
						.apply(caption));
		return DriverUtils.waitUntilElementClickable(this.getDriver(),
				getDriver().findElement(locator));
	}

	public void clickCloseMissingWebRTCSupportWarningBar() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				missingWebRTCSupportWarningBarCloseButton);
		missingWebRTCSupportWarningBarCloseButton.click();
	}

	public boolean isAnotherCallWarningModalVisible() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.WarningPage.xpathAnotherCallWarningModal);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
	}

	public void clickCloseAnotherCallWarningModal() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				anotherCallWarningModalCloseButton);
		final By locator = By
				.xpath(WebAppLocators.WarningPage.xpathAnotherCallWarningModal);

		anotherCallWarningModalCloseButton.click();
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public boolean clickButtonWithCaptionInAnotherCallWarningModal(
			String caption) throws Exception {
		final By buttonLocator = By
				.xpath(WebAppLocators.WarningPage.xpathAnotherCallWarningModalButtonByCaption
						.apply(caption));
		WebElement button = getDriver().findElement(buttonLocator);
		final By modalLocator = By
				.xpath(WebAppLocators.WarningPage.xpathAnotherCallWarningModal);
		boolean clickable = DriverUtils.waitUntilElementClickable(
				this.getDriver(), button);

		button.click();
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);

		return clickable;
	}
}
