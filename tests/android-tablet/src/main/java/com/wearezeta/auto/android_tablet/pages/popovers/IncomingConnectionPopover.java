package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class IncomingConnectionPopover extends AbstractPopoverContainer {
	private static final String idRootLocator = "fl__participant_dialog__main__container";

	private NonConnectedUserIncomingConnectionPage incomingConnectionPage;

	public IncomingConnectionPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.incomingConnectionPage = new NonConnectedUserIncomingConnectionPage(
				lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public boolean isNameVisible(String name) throws Exception {
		return this.incomingConnectionPage.isNameVisible(name);
	}

	public void tapAcceptButton() throws Exception {
		this.incomingConnectionPage.tapAcceptButton();
	}

	public void tapCloseButton() {
		this.incomingConnectionPage.tapCloseButton();
	}
}
