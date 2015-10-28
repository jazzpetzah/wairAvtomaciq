package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	private static final String xpathSettingsTitle = "//*[@id='action_bar_container' and .//*[@value='Settings']]";

	private static final Function<String, String> xpathSettingsMenuItemByText = text -> String
			.format("//*[@id='title' and @value='%s']", text);

	private static final Function<String, String> xpathConfirnBtnByName = name -> String
			.format("//*[starts-with(@id, 'button') and @value='%s']", name);

	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathSettingsTitle));
	}

	public void selectMenuItem(String name) throws Exception {
		final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
		assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator, 2) : String
				.format("Menu item '%s' is not present", name);
		getDriver().findElement(locator).click();
	}

	public boolean isMenuItemVisible(String name) throws Exception {
		final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void confirmSignOut() throws Exception {
		final By locator = By.xpath(xpathConfirnBtnByName.apply("Sign out"));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "Sign out confirmation is not visible";
		getDriver().findElement(locator).click();
	}
}
