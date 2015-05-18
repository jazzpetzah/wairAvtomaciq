package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class NonConnectedParticipantInfoPopoverPage extends
		AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.NonConnectedParticipantPage.xpathConnectButton)
	private WebElement connectButton;

	public NonConnectedParticipantInfoPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.NonConnectedParticipantPage.xpathConnectButton;
	}
}
