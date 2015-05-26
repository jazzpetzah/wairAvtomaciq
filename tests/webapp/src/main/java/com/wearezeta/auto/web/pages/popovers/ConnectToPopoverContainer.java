package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class ConnectToPopoverContainer extends AbstractPopoverContainer {
	private ConnectToPopoverPage connectToPopoverPage;
	private PendingOutgoingConnectionPopoverPage pendingOutgoingConnectionPopoverPage;

	public ConnectToPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.connectToPopoverPage = new ConnectToPopoverPage(lazyDriver, this);
		this.pendingOutgoingConnectionPopoverPage = new PendingOutgoingConnectionPopoverPage(
				lazyDriver, this);
	}
	private AbstractPopoverPage getCurrentPage()
			throws Exception {
		if (this.connectToPopoverPage.isCurrent()) {
			return this.connectToPopoverPage;
		} else if (this.pendingOutgoingConnectionPopoverPage.isCurrent()) {
			return this.pendingOutgoingConnectionPopoverPage;
		} else {
			throw new RuntimeException(
					"The current popover page is unknown.");
		}
	}
	
	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.xpathRootLocator;
	}

	public void clickConnectButton() throws Exception {
		this.waitUntilVisibleOrThrowException();
		this.connectToPopoverPage.clickConnectButton();
	}

}
