package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class OutgoingConnectionPopover extends AbstractPopoverContainer {
	static final String idStrRootLocator = "fl__participant_dialog__main__container";
	static final By idRootLocator = By.id(idStrRootLocator);

	private NonConnectedUserOutgoingConnectionPage outgoingConnectionPage;

	public OutgoingConnectionPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.outgoingConnectionPage = new NonConnectedUserOutgoingConnectionPage(
				lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return idRootLocator;
	}

	public boolean isNameVisible(String name) throws Exception {
		return this.outgoingConnectionPage.isNameVisible(name);
	}

	public void tapConnectButton() throws Exception {
		this.outgoingConnectionPage.tapConnectButton();
	}

	public void tapCloseButton() throws Exception {
		this.outgoingConnectionPage.tapCloseButton();
	}

	public boolean isConnectButtonTappable() throws Exception {
		return this.outgoingConnectionPage.isConnectButtonTappable();
	}
}
