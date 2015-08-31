package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class OutgoingConnectionPopover extends AbstractPopoverContainer {
	static final String idRootLocator = "fl__participant_dialog__main__container";

	private NonConnectedUserOutgoingConnectionPage outgoingConnectionPage;

	public OutgoingConnectionPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.outgoingConnectionPage = new NonConnectedUserOutgoingConnectionPage(
				lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public boolean isNameVisible(String name) throws Exception {
		return this.outgoingConnectionPage.isNameVisible(name);
	}

	public String getMessage() {
		return this.outgoingConnectionPage.getMessage();
	}

	public void setMessage(String text) throws Exception {
		this.outgoingConnectionPage.setMessage(text);
	}

	public void tapConnectButton() throws Exception {
		this.outgoingConnectionPage.tapConnectButton();
	}

	public void tapCloseButton() {
		this.outgoingConnectionPage.tapCloseButton();
	}

	public boolean isConnectButtonTappable() throws Exception {
		return this.outgoingConnectionPage.isConnectButtonTappable();
	}
}
