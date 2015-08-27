package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletOutgoingPendingConnectionPage extends AndroidTabletPage {

	private static final String idPageRoot = "fl__pending_connect_request";

	private static final Function<String, String> xpathNameByText = text -> String
			.format("//*[@id='taet__participants__header' and @value='%s']",
					text);

	@SuppressWarnings("unused")
	private static final Function<String, String> xpathFirstMessageByText = text -> String
			.format("//*[@id='ttv__connect_request__first_message' and @value='%s']",
					text);

	public TabletOutgoingPendingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idPageRoot));
	}

	public boolean waitUntilNameVisible(String expectedName) throws Exception {
		final By locator = By.xpath(xpathNameByText.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
