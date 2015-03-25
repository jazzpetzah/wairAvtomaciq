package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class NonConnectedParticipantInfoPopoverPage extends
		AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.NonConnectedParticipantPage.xpathConnectButton)
	private WebElement connectButton;

	public NonConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.NonConnectedParticipantPage.xpathConnectButton;
	}
}
