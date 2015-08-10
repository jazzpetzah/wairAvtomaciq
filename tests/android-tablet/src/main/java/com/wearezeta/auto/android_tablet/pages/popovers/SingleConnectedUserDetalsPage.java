package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SingleConnectedUserDetalsPage extends
		AbstractConversationDetailsPage {
	public final static Function<String, String> xpathNameByValue = value -> String
			.format("//*[@id='ttv__participants__header' and @value='%s']",
					value);

	public final static Function<String, String> xpathEmailByValue = value -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']",
					value);

	public SingleConnectedUserDetalsPage(Future<ZetaAndroidDriver> lazyDriver,
			SingleUserPopover container) throws Exception {
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
