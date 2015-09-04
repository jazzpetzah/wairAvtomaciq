package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class NonConnectedUserIncomingConnectionPage extends
		AbstractIncomingConnectionPage {

	private static final String idAcceptButton = "zb__connect_request__accept_button";
	@FindBy(id = idAcceptButton)
	private WebElement acceptButton;

	private static final String idIgnoreButton = "zb__connect_request__ignore_button";
	@FindBy(id = idIgnoreButton)
	private WebElement ignoreButton;

	public NonConnectedUserIncomingConnectionPage(
			Future<ZetaAndroidDriver> lazyDriver,
			IncomingConnectionPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapAcceptButton() throws Exception {
		acceptButton.click();
	}

	public void tapIgnoreButton() {
		ignoreButton.click();
	}

}
