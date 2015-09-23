package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SendInvitationPopoverPageSteps {

	private String invitationLink = null;

	/**
	 * Verifies whether Bring Your Friends popover is visible or not
	 * 
	 * @step. ^I( do not)? see Bring Your Friends popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not provided
	 * @throws Exception
	 */
	@Then("^I( do not)? see Bring Your Friends popover$")
	public void ISeeSendInvitationPopover(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			WebappPagesCollection.bringYourFriendsPopover.isVisible();
		} else {
			WebappPagesCollection.bringYourFriendsPopover.isNotVisible();
		}
	}

	/**
	 * Click Invite button on Bring Your Friends popover
	 * 
	 * @step. ^I click Invite button on Bring Your Friends popover$
	 * 
	 * @throws Exception
	 */
	@Then("^I click Invite button on Bring Your Friends popover$")
	public void IClickInviteButton() throws Exception {
		WebappPagesCollection.bringYourFriendsPopover.clickInviteButton();
	}

	/**
	 * Save invitation link from the corresponding popover into internal
	 * variable
	 * 
	 * @step. ^I remember invitation link on Bring Your Friends popover$
	 * 
	 */
	@When("^I remember invitation link on Bring Your Friends popover$")
	public void IRemeberInvitationLink() {
		invitationLink = WebappPagesCollection.bringYourFriendsPopover.parseInvitationLink();
	}

	/**
	 * Navigates to previously remembered invitation link
	 * 
	 * @step. ^I navigate to previously remembered invitation link$
	 * 
	 * @throws Exception
	 */
	@When("^I navigate to previously remembered invitation link$")
	public void INavigateToNavigationLink() throws Exception {
		if (invitationLink == null) {
			throw new RuntimeException(
					"Invitation link has not been remembered before!");
		}

		WebappPagesCollection.youAreInvitedPage = (YouAreInvitedPage) WebappPagesCollection.loginPage
				.instantiatePage(YouAreInvitedPage.class);
		WebappPagesCollection.youAreInvitedPage.setUrl(invitationLink);
		WebappPagesCollection.youAreInvitedPage.navigateTo();
	}
}
