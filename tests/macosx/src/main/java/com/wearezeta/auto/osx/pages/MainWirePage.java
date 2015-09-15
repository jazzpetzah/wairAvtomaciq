package com.wearezeta.auto.osx.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class MainWirePage extends OSXPage {

	@FindBy(how = How.XPATH, using = OSXLocators.MainWirePage.xpathWindow)
	protected WebElement window;

	@FindBy(how = How.XPATH, using = OSXLocators.MainWirePage.xpathMinimizeButton)
	protected WebElement minimizeButton;

	@FindBy(how = How.XPATH, using = OSXLocators.MainWirePage.xpathZoomButton)
	protected WebElement zoomButton;

	@FindBy(how = How.XPATH, using = OSXLocators.MainWirePage.xpathCloseButton)
	protected WebElement closeButton;

	public MainWirePage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isMainWindowVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(OSXLocators.MainWirePage.xpathWindow));
	}

	public void minimizeWindow() throws Exception {
		minimizeButton.click();
	}

	public void maximizeWindow() {
		zoomButton.click();
	}

	public void closeWindow() {
		closeButton.click();
	}
}
