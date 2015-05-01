package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class ConnectToPopoverContainer extends AbstractPopoverContainer {
	private ConnectToPopoverPage connectToPopoverPage;

	public ConnectToPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.connectToPopoverPage = new ConnectToPopoverPage(lazyDriver, this);
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
