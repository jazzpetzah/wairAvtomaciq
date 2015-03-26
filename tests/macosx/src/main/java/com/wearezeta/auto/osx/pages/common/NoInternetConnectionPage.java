package com.wearezeta.auto.osx.pages.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;

public class NoInternetConnectionPage extends OSXPage {

	@FindBy(how = How.ID, using = OSXLocators.NoInternetConnectionPage.idOKButton)
	private WebElement okButton;

	@FindBy(how = How.XPATH, using = OSXLocators.NoInternetConnectionPage.xpathNoInternetMessage)
	private WebElement noInternetMessage;

	public NoInternetConnectionPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(OSXLocators.NoInternetConnectionPage.xpathNoInternetMessage),
						60);
	}

	public void closeDialog() {
		okButton.click();
	}

}
