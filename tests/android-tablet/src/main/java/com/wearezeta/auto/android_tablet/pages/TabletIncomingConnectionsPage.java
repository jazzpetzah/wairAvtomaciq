package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletIncomingConnectionsPage extends AndroidTabletPage {
	public static final String idListRoot = "crlv_connect_request_inbox__list";

	public static final Function<String, String> xpathIncomingRequestContainerByName = name -> String
			.format("//*[@id='taet__participants__header' and @value='Connect to %s?']"
					+ "/parent::*/parent::*/parent::*", name);

	public static final Function<String, String> xpathIncomingRequestEmailByValue = value -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']",
					value);

	public static final Function<String, String> xpathIncomingRequestMessageByValue = value -> String
			.format("//*[@id='ttv__connect_request__first_message' and @value='%s']",
					value);

	public static final Function<String, String> xpathIncomingRequestIgnoreButtonByName = name -> String
			.format("%s//*[@id='zb__connect_request__ignore_button']",
					xpathIncomingRequestContainerByName.apply(name));

	public static final Function<String, String> xpathIncomingRequestAcceptButtonByName = name -> String
			.format("%s//*[@id='zb__connect_request__accept_button']",
					xpathIncomingRequestContainerByName.apply(name));

	public TabletIncomingConnectionsPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idListRoot));
	}

	public void acceptIncomingConnectionFrom(String name) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestAcceptButtonByName
				.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Accept request button for '%s' is not visible", name);
		getDriver().findElement(locator).click();
	}

	public void ignoreIncomingConnectionFrom(String name) throws Exception {
		final By locator = By.xpath(xpathIncomingRequestIgnoreButtonByName
				.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("Ignore request button for '%s' is not visible", name);
		getDriver().findElement(locator).click();
	}
}
