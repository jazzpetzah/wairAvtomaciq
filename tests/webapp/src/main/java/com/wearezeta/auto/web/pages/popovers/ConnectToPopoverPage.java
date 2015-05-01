package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class ConnectToPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.ConnectToPopover.ConnectToPage.xpathConnectButton)
	private WebElement connectButton;

	public ConnectToPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			ConnectToPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public void clickConnectButton() {
		connectButton.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.ConnectToPage.xpathConnectButton;
	}
}
