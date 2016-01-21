package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class NonConnectedUserIncomingConnectionPage extends AbstractIncomingConnectionPage {

	private static final By idAcceptButton = By.id("zb__connect_request__accept_button");

	private static final By idIgnoreButton = By.id("zb__connect_request__ignore_button");

	public NonConnectedUserIncomingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver,
			IncomingConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapAcceptButton() throws Exception {
		getElement(idAcceptButton).click();
	}

	public void tapIgnoreButton() throws Exception {
		getElement(idIgnoreButton).click();
	}

}
