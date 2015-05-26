package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class PendingOutgoingConnectionPopoverPage extends AbstractPopoverPage {
	@FindBy(xpath = PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathPendingConnectionButton)
	private WebElement pendingConnectionButton;

	public PendingOutgoingConnectionPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			ConnectToPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public void clickPendingButton() {
		pendingConnectionButton.click();
	}

	public boolean isPendingConnectionButtonVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(pendingConnectionButton);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathPendingConnectionButton;
	}
}
