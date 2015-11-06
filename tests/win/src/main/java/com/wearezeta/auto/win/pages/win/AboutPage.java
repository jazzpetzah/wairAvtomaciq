package com.wearezeta.auto.win.pages.win;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWinDriver;
import com.wearezeta.auto.win.locators.WinLocators;

public class AboutPage extends WinPage {

	@FindBy(how = How.XPATH, using = WinLocators.AboutPage.xpathWindow)
	private WebElement window;

	@FindBy(how = How.XPATH, using = WinLocators.AboutPage.xpathMinimizeButton)
	private WebElement minimizeButton;

	@FindBy(how = How.XPATH, using = WinLocators.AboutPage.xpathCloseButton)
	private WebElement closeButton;

	public AboutPage(Future<ZetaWinDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WinLocators.AboutPage.xpathWindow));
	}

	public boolean isNotVisible() throws Exception {
		// TODO: should be waitUntilLocatorDissapears but that's broken with
		// Appium4Mac
		return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(WinLocators.AboutPage.xpathWindow));
	}

	public void minimizeWindow() throws Exception {
		minimizeButton.click();
	}

	public void closeWindow() {
		closeButton.click();
	}

}
