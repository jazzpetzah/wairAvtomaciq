package com.wearezeta.auto.web.pages.popovers;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public abstract class PeoplePopoverContainer extends AbstractPopoverContainer {
	protected AddPeoplePopoverPage addPeoplePopoverPage;

	public PeoplePopoverContainer(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
		this.addPeoplePopoverPage = new AddPeoplePopoverPage(driver, wait, this);
	}

	public void searchForUser(String searchText) throws Exception {
		addPeoplePopoverPage.searchForUser(searchText);
	}

	public void clickCreateConversation() throws Exception {
		addPeoplePopoverPage.clickCreateConversation();
	}
}
