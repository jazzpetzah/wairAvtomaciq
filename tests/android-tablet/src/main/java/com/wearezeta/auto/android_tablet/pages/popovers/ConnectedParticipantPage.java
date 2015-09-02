package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class ConnectedParticipantPage extends AbstractParticipantPage {
	private final static Function<String, String> xpathNameByValue = value -> String
			.format("//*[@id='ttv__single_participants__header' and @value='%s']",
					value);

	private final static Function<String, String> xpathEmailByValue = value -> String
			.format("//*[@id='ttv__single_participants__sub_header' and @value='%s']",
					value);

	public ConnectedParticipantPage(Future<ZetaAndroidDriver> lazyDriver,
			GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public boolean waitUntilUserNameVisible(String expectedName)
			throws Exception {
		final By locator = By.xpath(xpathNameByValue.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilUserEmailVisible(String expectedEmail)
			throws Exception {
		final By locator = By.xpath(xpathEmailByValue.apply(expectedEmail));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}
}
