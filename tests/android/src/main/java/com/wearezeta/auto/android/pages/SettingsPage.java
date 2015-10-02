package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	private static final String xpathServicesButton = "//*[@value='Services']";
	@FindBy(xpath = xpathServicesButton)
	private WebElement servicesButton;

	private static final String xpathSettingPageTitle = "//*[@id='title' and @value='Settings']";

	private static final String xpathSettingPageChangePassword = "//*[@id='title' and @value='Change Password']";

	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsPageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageTitle), 5);
	}

	public boolean isChangePasswordVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
			By.xpath(xpathSettingPageChangePassword));
	}

	public void clickServicesButton() throws Exception {
		servicesButton.click();
		DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathServicesButton));
	}
}
