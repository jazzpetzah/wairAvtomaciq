package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public abstract class PeoplePopoverContainer extends AbstractPopoverContainer {

	@FindBy(how = How.XPATH, using = PopoverLocators.Shared.xpathBackButton)
	private WebElement backButton;

	protected AddPeoplePopoverPage addPeoplePopoverPage;

	public PeoplePopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.addPeoplePopoverPage = new AddPeoplePopoverPage(lazyDriver, this);
	}

	public void searchForUser(String searchText) throws Exception {
		addPeoplePopoverPage.searchForUser(searchText);
	}

	public void clickCreateGroupConversation() throws Exception {
		addPeoplePopoverPage.clickCreateGroupConversation();
	}

	public String getBackButtonToolTip() {
		return backButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public void clickBackButton() {
		backButton.click();
	}
}
