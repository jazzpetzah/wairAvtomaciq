package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.TabletPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletGroupConversationDetailPopoverPageSteps {

	/**
	 * Opens the ellipses menu on the ipad popover
	 * 
	 * @step. ^I press conversation menu button on iPad$
	 * @throws Throwable
	 */
	@When("^I press conversation menu button on iPad$")
	public void IPressConversationMenuButtonOniPad() throws Throwable {
		TabletPagesCollection.tabletGroupConversationDetailPopoverPage
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
		TabletPagesCollection.tabletGroupConversationDetailPopoverPage
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
		TabletPagesCollection.tabletGroupConversationDetailPopoverPage
				.confirmLeaveConversation();
	}

}
