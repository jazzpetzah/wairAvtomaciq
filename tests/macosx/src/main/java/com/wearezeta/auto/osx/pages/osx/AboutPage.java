package com.wearezeta.auto.osx.pages.osx;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class AboutPage extends OSXPage {

	@FindBy(how = How.XPATH, using = OSXLocators.AboutPage.xpathWindow)
	private WebElement window;

	@FindBy(how = How.XPATH, using = OSXLocators.AboutPage.xpathMinimizeButton)
	private WebElement minimizeButton;

	@FindBy(how = How.XPATH, using = OSXLocators.AboutPage.xpathCloseButton)
	private WebElement closeButton;

	public AboutPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(OSXLocators.AboutPage.xpathWindow));
	}

	public void minimizeWindow() throws Exception {
		minimizeButton.click();
	}

	public void closeWindow() {
		closeButton.click();
	}

}
