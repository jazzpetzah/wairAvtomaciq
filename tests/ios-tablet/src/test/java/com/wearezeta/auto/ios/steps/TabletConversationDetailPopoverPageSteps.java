package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletPagesCollection;
import com.wearezeta.auto.ios.pages.TabletPeoplePickerPage;

import cucumber.api.java.en.When;

public class TabletConversationDetailPopoverPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
		boolean nameVisible = TabletPagesCollection.tabletConversationDetailPopoverPage
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
		TabletPagesCollection.tabletPeoplePickerPage = (TabletPeoplePickerPage) TabletPagesCollection.tabletConversationDetailPopoverPage
				.addContactTo1on1Chat();
	}

}
