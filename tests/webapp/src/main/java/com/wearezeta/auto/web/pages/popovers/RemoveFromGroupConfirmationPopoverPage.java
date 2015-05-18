package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class RemoveFromGroupConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.RemoveParticipantConfirmationPage.xpathConfirmRemoveButton)
	private WebElement confirmRemoveButton;

	public RemoveFromGroupConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public void confirmRemoveFromGroupChat() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				confirmRemoveButton);
		confirmRemoveButton.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.RemoveParticipantConfirmationPage.xpathConfirmRemoveButton;
	}
}
