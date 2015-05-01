package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class AddPeopleConfirmationPopoverPage extends AbstractPopoverPage {

	public AddPeopleConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
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
