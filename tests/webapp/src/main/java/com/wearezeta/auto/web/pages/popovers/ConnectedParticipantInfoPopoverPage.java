package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

class ConnectedParticipantInfoPopoverPage extends AbstractUserInfoPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ConnectedParticipantPage.xpathOpenConversationButton)
	private WebElement openConversationButton;

	public ConnectedParticipantInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
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
