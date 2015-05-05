package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import java.util.concurrent.Future;

class UnblockUserConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.UnblockUserConfirmationPage.xpathConfirmUnblockButton)
	private WebElement confirmUnblockButton;

	public UnblockUserConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.BlockUserConfirmationPage.xpathConfirmBlockButton;
	}

	public void clickConfirmUnblockButton() {
		confirmUnblockButton.click();
	}
}
