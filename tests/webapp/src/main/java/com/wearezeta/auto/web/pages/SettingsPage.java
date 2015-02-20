package com.wearezeta.auto.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {

	public SettingsPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
	}
}
