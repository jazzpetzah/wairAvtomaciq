package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class CancelRequestConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(xpath = PopoverLocators.ConnectToPopover.CancelRequestConfirmationPage.xpathYesButton)
	private WebElement yesButton;
	@FindBy(xpath = PopoverLocators.ConnectToPopover.CancelRequestConfirmationPage.xpathNoButton)
	private WebElement noButton;

	public CancelRequestConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			ConnectToPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	protected String getXpathLocator() {
		return PopoverLocators.ConnectToPopover.CancelRequestConfirmationPage.xpathYesButton;
	}

	public void clickYesButton() {
		yesButton.click();
	}
	
	public void clickNoButton() {
		noButton.click();
	}
}
