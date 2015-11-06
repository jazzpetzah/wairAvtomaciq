package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class PreferencesPage extends WebPage {
	@FindBy(how = How.XPATH, using = WebAppLocators.SettingsPage.xpathSettingsCloseButton)
	private WebElement settingsCloseButton;

	public PreferencesPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpath));
	}

	public void clickCloseButton() {
		settingsCloseButton.click();
	}

}
