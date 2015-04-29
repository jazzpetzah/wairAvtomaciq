package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

class ConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	public ConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton;
	}

	@Override
	protected WebElement getSharedElement(String relativeXpath) {
		return super
				.getSharedElement(String
						.format("%s%s",
								PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathPageRootLocator,
								relativeXpath));
	}

	private WebElement getUserNameElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathUserName);
	}

	public String getUserName() {
		return getUserNameElement().getText();
	}

}
