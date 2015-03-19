package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class AddPeopleConfirmationPopoverPage extends AbstractPopoverPage {

	public AddPeopleConfirmationPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return String.format("%s%s", this.getContainer().getXpathLocator(),
				PopoverLocators.Shared.xpathContinueButton);
	}

	private WebElement getContinueButtonElement() {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathContinueButton);
	}

	public void clickContinueButton() {
		this.getContinueButtonElement().click();
	}
}
