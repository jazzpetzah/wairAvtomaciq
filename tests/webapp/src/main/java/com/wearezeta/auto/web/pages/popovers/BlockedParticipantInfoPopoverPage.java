package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

class BlockedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.BlockedParticipantPage.xpathUnblockButton)
	private WebElement unblockButton;

	public BlockedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.BlockedParticipantPage.xpathUnblockButton;
	}

	public boolean isUnblockButtonVisible() {
		return unblockButton.isDisplayed();
	}

	public void clickUnblockButton() {
		unblockButton.click();
	}

	public String getUnblockButtonCaption() {
		return unblockButton.getText();
	}

	public String getUnblockButtonToolTip() {
		return unblockButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

}
