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

	@FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBarClose)
	private WebElement missingWebRTCSupportWarningBarCloseButton;

	@FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssAnotherCallWarningModalClose)
	private WebElement anotherCallWarningModalCloseButton;

	@FindBy(how = How.CSS, using = WebAppLocators.WarningPage.cssFullCallWarningModalClose)
	private WebElement fullCallWarningModalCloseButton;

	public WarningPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isMissingWebRTCSupportWarningBarVisible() throws Exception {
		final String css = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBar;
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.cssSelector(css));
	}

	public boolean isMissingWebRTCSupportWarningBarInvisible() throws Exception {
		final String css = WebAppLocators.WarningPage.cssMissingWebRTCSupportWarningBar;
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.cssSelector(css));
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
				.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
	}

	public boolean isAnotherCallWarningModalInvisible() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
		return DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public void clickCloseAnotherCallWarningModal() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				anotherCallWarningModalCloseButton);
		final By locator = By
				.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);

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
				.cssSelector(WebAppLocators.WarningPage.cssAnotherCallWarningModal);
		boolean clickable = DriverUtils.waitUntilElementClickable(
				this.getDriver(), button);

		button.click();
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);

		return clickable;
	}

	public boolean isFullCallWarningModalVisible() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
	}

	public boolean isFullCallWarningModalInvisible() throws Exception {
		final By locator = By
				.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
		return DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public void clickCloseFullCallWarningModal() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				fullCallWarningModalCloseButton);
		final By locator = By
				.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModalClose);

		fullCallWarningModalCloseButton.click();
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public boolean clickButtonWithCaptionInFullCallWarningModal(String caption)
			throws Exception {
		final By buttonLocator = By
				.xpath(WebAppLocators.WarningPage.xpathFullCallWarningModalButtonByCaption
						.apply(caption));
		WebElement button = getDriver().findElement(buttonLocator);
		final By modalLocator = By
				.cssSelector(WebAppLocators.WarningPage.cssFullCallWarningModal);
		boolean clickable = DriverUtils.waitUntilElementClickable(
				this.getDriver(), button);

		button.click();
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(), modalLocator);

		return clickable;
	}
}
