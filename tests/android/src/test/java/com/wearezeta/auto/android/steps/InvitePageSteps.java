package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.InvitePage;

import cucumber.api.java.en.When;

public class InvitePageSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private InvitePage getInvitePage() throws Exception {
		return pagesCollection.getPage(InvitePage.class);
	}

	/**
	 * Waits for the invite button to appear or disappear in the conversations
	 * list
	 *
	 * @throws Exception
	 * @step. ^I( do not)? see invite more people button$
	 */
	@When("^I( do not)? see invite more people button$")
	public void WhenISeeInvitePeopleButton(String shouldNotSee)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(
					"The invite more people button is not visible in the conversations list",
					getInvitePage().waitForInviteMorePeopleButtonVisible());
		} else {
			Assert.assertTrue("The invite more people button is still visible",
					getInvitePage().waitForInviteMorePeopleButtonNotVisible());
		}
	}

}