package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.*;

public class SettingsPage extends AndroidPage {

	private static final String xpathSettingsTitle = "//*[@id='action_bar_container' and .//*[@value='Settings']]";

	private static final Function<String, String> xpathSettingsMenuItemByText = text -> String
			.format("//*[@id='title' and @value='%s']", text);

	public SettingsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isSettingsPageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathSettingsTitle));
	}

	public void selectMenuItem(String name) throws Exception {
		final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 2) : String
				.format("Menu item '%s' is not visible", name);
		getDriver().findElement(locator).click();
	}

	public boolean isMenuItemVisible(String name) throws Exception {
		final By locator = By.xpath(xpathSettingsMenuItemByText.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
