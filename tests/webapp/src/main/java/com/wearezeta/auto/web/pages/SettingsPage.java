package com.wearezeta.auto.web.pages;

import java.io.IOException;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {

	public SettingsPage(String URL, String path) throws IOException {
		super(URL, path);
	}

	public boolean isVisible() {
		final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
	}
}
