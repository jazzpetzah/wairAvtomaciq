package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;

	public SingleUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return String.format("%s%s", this.getContainer().getXpathLocator(),
				PopoverLocators.Shared.xpathAddButton);
	}

	private WebElement getUserNameElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathUserName);
	}

	public String getUserName() {
		return getUserNameElement().getText();
	}
}
