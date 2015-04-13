package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class PendingParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingButton)
	private WebElement pendingButton;

	public PendingParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingButton;
	}

	public boolean isPendingButtonVisible() {
		return pendingButton.isDisplayed();
	}

	public void clickPendingButton() {
		pendingButton.click();
	}

	public String getPendingButtonCaption() {
		return pendingButton.getText();
	}
}
