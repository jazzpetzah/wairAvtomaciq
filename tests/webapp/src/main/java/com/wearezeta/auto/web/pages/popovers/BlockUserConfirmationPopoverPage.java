package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class BlockUserConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.BlockUserConfirmationPage.xpathConfirmBlockButton)
	private WebElement confirmBlockButton;

	public BlockUserConfirmationPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.BlockUserConfirmationPage.xpathConfirmBlockButton;
	}

	public void clickConfirmButton() {
		confirmBlockButton.click();
	}
}
