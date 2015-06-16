package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletGroupConversationDetailPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationDetailPopoverPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletGroupConversationDetailPopoverPage getTabletGroupConversationDetailPopoverPage()
			throws Exception {
		return (TabletGroupConversationDetailPopoverPage) pagesCollecton
				.getPage(TabletGroupConversationDetailPopoverPage.class);
	}

	/**
	 * Opens the ellipses menu on the ipad popover
	 * 
	 * @step. ^I press conversation menu button on iPad$
	 * @throws Throwable
	 */
	@When("^I press conversation menu button on iPad$")
	public void IPressConversationMenuButtonOniPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.openConversationMenuOnPopover();
	}

	/**
	 * Presses leave button in ellipsis menu
	 * 
	 * @step. ^I press leave converstation button on iPad$
	 * @throws Throwable
	 */
	@When("^I press leave converstation button on iPad$")
	public void IPressLeaveConverstationButtonOniPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.leaveConversation();
	}

	/**
	 * Presses the confirmation leave button
	 * 
	 * @step. ^I press leave on iPad$
	 * @throws Throwable
	 */
	@Then("^I press leave on iPad$")
	public void i_press_leave_on_iPad() throws Throwable {
		getTabletGroupConversationDetailPopoverPage()
				.confirmLeaveConversation();
	}
	
	/**
	 * 
	 * @param name
	 * @throws Throwable
	 */
	@When("^I select user on iPad group popover (.*)$")
	public void ISelectUserOniPadGroupPopover(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getTabletGroupConversationDetailPopoverPage().selectUserByNameOniPadPopover(name);
	}
	
	

}
