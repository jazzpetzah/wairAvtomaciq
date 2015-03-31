package com.wearezeta.auto.osx.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	public MainWirePage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void minimizeWindow() {
		minimizeButton.click();
	}

	public void zoomWindow() {
		zoomButton.click();
	}

	public void closeWindow() {
		closeButton.click();
	}
}
