package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletSelfProfilePage extends AndroidTabletPage {
	public static final Function<String, String> xpathSelfNameByContent = content -> String
			.format("//*[@id='ttv__profile__name' and @value='%s']", content);

	public TabletSelfProfilePage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidTabletPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		switch (direction) {
		case UP: {
			return this;
		}
		case RIGHT: {
			return new TabletConversationsListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public boolean isNameVisible(String name) throws Exception {
		final By locator = By.xpath(xpathSelfNameByContent.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

}
