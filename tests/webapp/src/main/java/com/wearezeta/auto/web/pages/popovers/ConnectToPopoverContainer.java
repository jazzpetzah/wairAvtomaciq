package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class ConnectToPopoverContainer extends AbstractPopoverContainer {
	private ConnectToPopoverPage connectToPopoverPage;

	public ConnectToPopoverContainer(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		this.connectToPopoverPage = new ConnectToPopoverPage(driver, wait, this);
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
