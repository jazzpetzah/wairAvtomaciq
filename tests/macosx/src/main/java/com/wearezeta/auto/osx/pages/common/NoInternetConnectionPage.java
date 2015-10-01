package com.wearezeta.auto.osx.pages.common;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.osx.OSXPage;

public class NoInternetConnectionPage extends OSXPage {

	@FindBy(how = How.ID, using = OSXLocators.NoInternetConnectionPage.idOKButton)
	private WebElement okButton;

	@FindBy(how = How.XPATH, using = OSXLocators.NoInternetConnectionPage.xpathNoInternetMessage)
	private WebElement noInternetMessage;

	public NoInternetConnectionPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(
						this.getDriver(),
						By.xpath(OSXLocators.NoInternetConnectionPage.xpathNoInternetMessage),
						60);
	}

	public void closeDialog() {
		okButton.click();
	}

}
