package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class ConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton)
	private WebElement openConversationButton;

	public ConnectedParticipantInfoPopoverPage(
			Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton;
	}

	public boolean isOpenConvButtonVisible() {
		return openConversationButton.isDisplayed();
	}

	public String getOpenConvButtonCaption() {
		return openConversationButton.getText();
	}

	public String getOpenConvButtonToolTip() {
		return openConversationButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public void clickOpenConversationButton() {
		openConversationButton.click();
	}
}
