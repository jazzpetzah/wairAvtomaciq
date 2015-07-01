package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class PendingOutgoingConnectionPopoverPage extends AbstractPopoverPage {
//	@FindBy(xpath = PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathPendingConnectionButton)
//	private WebElement pendingConnectionButton;
	@FindBy(xpath = PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathCancelRequestButton)
	private WebElement cancelRequestButton;

	public PendingOutgoingConnectionPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			ConnectToPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}
// Old workaround - when we used to have Pending button, not Cancel a pending connection request
//	public void clickPendingButton() {
//		pendingConnectionButton.click();
//	}
//
//	public boolean isPendingConnectionButtonVisible() throws Exception {
//		return DriverUtils
//				.isElementPresentAndDisplayed(getDriver(), pendingConnectionButton);
//	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.PendingOutgoingConnectionPage.xpathCancelRequestButton;
	}

	public void clickCancelRequestButton() {
		cancelRequestButton.click();
	}
}
