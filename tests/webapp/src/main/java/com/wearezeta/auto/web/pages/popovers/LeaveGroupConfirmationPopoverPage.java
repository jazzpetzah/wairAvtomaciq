package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class LeaveGroupConfirmationPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	public LeaveGroupConfirmationPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	public void confirmLeaveGroupChat() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(driver, confirmLeaveButton);
		confirmLeaveButton.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ParticipantsListPage.xpathConfirmLeaveButton;
	}
}
