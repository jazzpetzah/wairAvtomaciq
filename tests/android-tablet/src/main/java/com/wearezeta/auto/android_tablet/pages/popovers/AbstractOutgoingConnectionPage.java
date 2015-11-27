package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

abstract class AbstractOutgoingConnectionPage extends AbstractConnectionPage {
	public AbstractOutgoingConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	protected abstract By getConnectButtonLocator();

	public void tapConnectButton() throws Exception {
		final WebElement connectButton = getDriver().findElement(
				getConnectButtonLocator());
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), connectButton);
		connectButton.click();
	}

	public boolean isConnectButtonTappable() throws Exception {
		final WebElement connectButton = getDriver().findElement(
				getConnectButtonLocator());
		return DriverUtils.waitUntilElementClickable(getDriver(),
				connectButton, 2);
	}
}
