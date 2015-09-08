package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.ConversationActionsPopover;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationActionsPopoverSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private ConversationActionsPopover getConversationActionsPopover()
			throws Exception {
		return (ConversationActionsPopover) pagesCollection
				.getPage(ConversationActionsPopover.class);
	}

	/**
	 * Verify whether Conversation Actions popover is visible or not
	 * 
	 * @step. ^I( do not)? see Conversation Actions popover$
	 * @param shouldNotBeVisible
	 *            equals to null is "no not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see Conversation Actions popover$")
	public void ISeePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("Conversation Actions popover is not visible",
					getConversationActionsPopover().waitUntilVisible());
		} else {
			Assert.assertTrue("Conversation Actions popover is still visible",
					getConversationActionsPopover().waitUntilInvisible());
		}
	}

	/**
	 * Select the appropriate menu item on the popover
	 * 
	 * @step. ^I select (.*) menu item on Conversation Actions popover$
	 * 
	 * @param itemName
	 *            menu item name
	 * @throws Exception
	 */
	@When("^I select (.*) menu item on Conversation Actions popover$")
	public void ISelectMenuItem(String itemName) throws Exception {
		getConversationActionsPopover().selectMenuItem(itemName);
	}
}
