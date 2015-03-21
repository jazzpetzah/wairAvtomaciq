package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

abstract class AbstractUserInfoPopoverPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathRemoveButton)
	private WebElement removeButton;

	public AbstractUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	public void clickRemoveFromGroupChat() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, removeButton);
		removeButton.click();
	}
}
