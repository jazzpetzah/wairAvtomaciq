package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletConversationDetailPopoverPage;

import cucumber.api.java.en.When;

public class TabletConversationDetailPopoverPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletConversationDetailPopoverPage getTabletConversationDetailPopoverPage()
			throws Exception {
		return (TabletConversationDetailPopoverPage) pagesCollecton
				.getPage(TabletConversationDetailPopoverPage.class);
	}

	/**
	 * Verifies that other peoples profile page in the popover is visible
	 * 
	 * @step. ^I see (.*) user profile page in iPad popover$
	 * @param name
	 * @throws Throwable
	 */
	@When("^I see (.*) user profile page in iPad popover$")
	public void ISeeUserProfilePageIniPadPopover(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		boolean nameVisible = getTabletConversationDetailPopoverPage()
				.isOtherUserProfileNameVisible(name);
		Assert.assertTrue("Other user name is not visible", nameVisible);
	}

	/**
	 * Presses the + button on the ipad popover
	 * 
	 * @step. ^I press Add button on iPad popover$
	 * @throws Throwable
	 */
	@When("^I press Add button on iPad popover$")
	public void IPressAddButtonOniPadPopover() throws Throwable {
		getTabletConversationDetailPopoverPage().addContactTo1on1Chat();
	}

}
