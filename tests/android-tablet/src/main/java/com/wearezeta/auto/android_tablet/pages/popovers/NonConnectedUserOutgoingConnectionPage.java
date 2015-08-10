package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class NonConnectedUserOutgoingConnectionPage extends AbstractOutgoingConnectionPage {

	private static final String idConnectButton = "zb__send_connect_request__connect_button";

	public NonConnectedUserOutgoingConnectionPage(Future<ZetaAndroidDriver> lazyDriver,
			OutgoingConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected By getBlockButtonLocator() {
		return By.id(idConnectButton);
	}

}
