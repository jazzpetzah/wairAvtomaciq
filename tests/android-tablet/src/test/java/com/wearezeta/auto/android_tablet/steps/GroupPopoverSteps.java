package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.GroupPopover;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupPopoverSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private GroupPopover getGroupPopover() throws Exception {
		return (GroupPopover) pagesCollection.getPage(GroupPopover.class);
	}

	/**
	 * Verifies whether group popover page is currently visible or not
	 * 
	 * @step. ^I (do not )?see (?:the )[Gg]roup popover$
	 * @param shouldNotBeVisible
	 *            equals to null if "do not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:the )[Gg]roup popover$")
	public void ISeeThePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("The group popover is not currently visible",
					getGroupPopover().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The group popover is still visible, but should be hidden",
					getGroupPopover().waitUntilInvisible());
		}
	}

	/**
	 * Tap Leave button on the Group popover
	 * 
	 * @step. ^I tap Leave button on (?:the )[Gg]roup popover$"
	 * 
	 * @throws Exception
	 */
	@When("^I tap Leave button on (?:the )[Gg]roup popover$")
	public void ITapLeaveButton(String itemName) throws Exception {
		getGroupPopover().selectMenuItem(itemName);
	}
	
	/**
	 * Tap confirm button on the Group popover
	 * 
	 * @step. ^I confirm leaving the group chat on (?:the )[Gg]roup popover$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm leaving the group chat on (?:the )[Gg]roup popover$")
	public void IConfirmLeave() throws Exception {
		getGroupPopover().tapConfirmButton();
	}

}
