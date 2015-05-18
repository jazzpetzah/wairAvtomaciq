package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class PendingParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingButton)
	private WebElement pendingButton;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingTextBox)
	private WebElement pendingTextBox;

	public PendingParticipantInfoPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
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

	public boolean isPendingTextBoxDisplayed() {
		return pendingTextBox.isDisplayed();
	}

	public String getPendingButtonToolTip() {
		return pendingButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}
}
