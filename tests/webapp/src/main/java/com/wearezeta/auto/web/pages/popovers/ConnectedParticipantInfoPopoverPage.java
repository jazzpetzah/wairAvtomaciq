package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

class ConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton)
	private WebElement openConversationButton;

	public ConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton;
	}

}
