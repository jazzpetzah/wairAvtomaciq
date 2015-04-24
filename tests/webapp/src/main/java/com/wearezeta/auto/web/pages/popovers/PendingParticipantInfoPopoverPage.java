package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

class PendingParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	private static final String TOOLTIP_OPEN_CONVERSATION = "Open conversation";

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingButton)
	private WebElement pendingButton;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.PendingParticipantPage.xpathPendingTextBox)
	private WebElement pendingTextBox;

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

	public boolean isPendingTextBoxDisplayed() {
		return pendingTextBox.isDisplayed();
	}

	boolean isPendingButtonToolTipCorrect() {
		return TOOLTIP_OPEN_CONVERSATION.equals(pendingButton
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}
}
