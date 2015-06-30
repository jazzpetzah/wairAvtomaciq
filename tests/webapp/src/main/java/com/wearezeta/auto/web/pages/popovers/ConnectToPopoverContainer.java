package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class ConnectToPopoverContainer extends AbstractPopoverContainer {
	private ConnectToPopoverPage connectToPopoverPage;
	private PendingOutgoingConnectionPopoverPage pendingOutgoingConnectionPopoverPage;
	private CancelRequestConfirmationPopoverPage cancelRequestConfirmationPopoverPage;

	public ConnectToPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.connectToPopoverPage = new ConnectToPopoverPage(lazyDriver, this);
		this.pendingOutgoingConnectionPopoverPage = new PendingOutgoingConnectionPopoverPage(
				lazyDriver, this);
		this.cancelRequestConfirmationPopoverPage = new CancelRequestConfirmationPopoverPage(
				lazyDriver, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.xpathRootLocator;
	}

	public void clickConnectButton() throws Exception {
		this.waitUntilVisibleOrThrowException();
		this.connectToPopoverPage.clickConnectButton();
	}

	// public void clickPendingButton() throws Exception {
	// this.pendingOutgoingConnectionPopoverPage.clickPendingButton();
	// }

	public CancelRequestConfirmationPopoverPage clickCancelRequestButton() {
		this.pendingOutgoingConnectionPopoverPage.clickCancelRequestButton();
		return this.cancelRequestConfirmationPopoverPage;
	}

	public void clickNoButton() {
		this.cancelRequestConfirmationPopoverPage.clickNoButton();
	}

	public void clickYesButton() {
		this.cancelRequestConfirmationPopoverPage.clickYesButton();
	}
}
