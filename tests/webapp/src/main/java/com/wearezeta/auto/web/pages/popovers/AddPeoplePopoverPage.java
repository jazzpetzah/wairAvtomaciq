package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class AddPeoplePopoverPage extends AbstractPopoverPage {
	public AddPeoplePopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return String.format("%s%s", this.getContainer().getXpathLocator(),
				PopoverLocators.Shared.xpathSearchInputField);
	}

	private WebElement getSearchFieldElement() {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathSearchInputField);
	}

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				getSearchFieldElement());
		getSearchFieldElement().clear();
		getSearchFieldElement().sendKeys(searchText);
	}

	private WebElement getCreateGroupConversationButton() {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathCreateConversationButton);
	}

	public void clickCreateGroupConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				getCreateGroupConversationButton());
		getCreateGroupConversationButton().click();
	}

	private WebElement getFoundItemElement(String name) {
		return this
				.getSharedElement(PopoverLocators.Shared.xpathSearchResultByName
						.apply(name));
	}

	public void selectUserFromSearchResult(String name) {
		this.getFoundItemElement(name).click();
	}
}
