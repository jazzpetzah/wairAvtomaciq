package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class LeaveGroupConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.LeaveGroupConfirmationPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	public LeaveGroupConfirmationPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public void confirmLeaveGroupChat() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				confirmLeaveButton);
		confirmLeaveButton.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.LeaveGroupConfirmationPage.xpathConfirmLeaveButton;
	}
}
