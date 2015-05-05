package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import java.util.concurrent.Future;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class BlockedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.BlockedParticipantPage.xpathUnblockButton)
	private WebElement unblockButton;

	public BlockedParticipantInfoPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
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
