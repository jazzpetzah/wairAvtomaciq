package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.WebElement;

public class TabletIncomingConnectionsPage extends AndroidTabletPage {
	public static final String idListRoot = "crlv_connect_request_inbox__list";

	public static final Function<String, String> xpathIncomingRequestContainerByName = name -> String
			.format("//*[@id='taet__participants__header' and @value='Connect to %s?']"
					+ "/parent::*/parent::*/parent::*", name);

	public static final Function<String, String> xpathIncomingRequestEmailByValue = value -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']", value);

	public static final Function<String, String> xpathIncomingRequestIgnoreButtonByName = name -> String
			.format("%s//*[@id='zb__connect_request__ignore_button']",
					xpathIncomingRequestContainerByName.apply(name));

	public static final Function<String, String> xpathIncomingRequestAcceptButtonByName = name -> String
			.format("%s//*[@id='zb__connect_request__accept_button']",
					xpathIncomingRequestContainerByName.apply(name));

	public TabletIncomingConnectionsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idListRoot));
	}

	public void acceptIncomingConnectionFrom(String name) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestAcceptButtonByName.apply(name));
		final WebElement acceptBtn = getElement(locator, String.format("Accept request button for '%s' is not visible", name));
		acceptBtn.click();
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, 3)) {
			acceptBtn.click();
		}
	}

	public void ignoreIncomingConnectionFrom(String name) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestIgnoreButtonByName.apply(name));
		final WebElement ignoreBtn = getElement(locator, String.format("Ignore request button for '%s' is not visible", name));
		ignoreBtn.click();
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, 3)) {
			ignoreBtn.click();
		}
	}

	public boolean waitUntilEmailVisible(String expectedEmail) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestEmailByValue.apply(expectedEmail));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilNameVisible(String expectedName) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestContainerByName.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void scrollUntilEntryIsVisible(String name, int maxTimes) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestIgnoreButtonByName.apply(name));
		int ntry = 0;
		while (ntry < maxTimes) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
				return;
			}
			this.swipeUpCoordinates(1000, 50);
			ntry++;
		}
		throw new RuntimeException(String.format("Incoming pending contact '%s' is still not visible", name));
	}
}
