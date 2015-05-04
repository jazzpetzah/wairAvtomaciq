package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class PendingUserConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ConnectParticipantConfirmationPage.xpathConfirmConnectButton)
	private WebElement confirmConnectButton;
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ConnectParticipantConfirmationPage.xpathIgnoreConnectButton)
	private WebElement ignoreButton;

	public PendingUserConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ConnectParticipantConfirmationPage.xpathConfirmConnectButton;
	}

	public void clickConfirmConnectButton() {
		confirmConnectButton.click();
	}
}
