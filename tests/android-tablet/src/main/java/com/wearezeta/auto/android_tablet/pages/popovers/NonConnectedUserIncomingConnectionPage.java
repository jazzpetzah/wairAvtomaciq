package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class NonConnectedUserIncomingConnectionPage extends
		AbstractIncomingConnectionPage {

	private static final String idAcceptButton = "zb__connect_request__accept_button";
	@SuppressWarnings("unused")
	private static final String idIgnoreButton = "zb__connect_request__ignore_button";

	public NonConnectedUserIncomingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver,
			IncomingConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapAcceptButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idAcceptButton)) : "Connect button is not visible on the popover";
		getDriver().findElement(By.id(idAcceptButton)).click();
	}

}
