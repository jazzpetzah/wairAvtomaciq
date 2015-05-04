package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public abstract class PeoplePopoverContainer extends AbstractPopoverContainer {
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
}
