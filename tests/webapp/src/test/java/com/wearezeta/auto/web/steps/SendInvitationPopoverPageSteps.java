package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;
import com.wearezeta.auto.web.pages.popovers.SendInvitationPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SendInvitationPopoverPageSteps {

	private String invitationLink = null;

	/**
	 * Verifies whether Send Invitation popover is visible or not
	 * 
	 * @step. ^I( do not)? see Send Invitation popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not provided
	 * @throws Exception
	 */
	@Then("^I( do not)? see Send Invitation popover$")
	public void ISeeSendInvitationPopover(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			((SendInvitationPopoverContainer) PagesCollection.popoverPage)
					.waitUntilVisibleOrThrowException();
		} else {
			((SendInvitationPopoverContainer) PagesCollection.popoverPage)
					.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Save invitation link from the corresponding popover into internal
	 * variable
	 * 
	 * @step. ^I remember invitation link on Send Invitation popover$
	 * 
	 */
	@When("^I remember invitation link on Send Invitation popover$")
	public void IRemeberInvitationLink() {
		invitationLink = ((SendInvitationPopoverContainer) PagesCollection.popoverPage)
				.parseInvitationLink();
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

		PagesCollection.youAreInvitedPage = (YouAreInvitedPage) PagesCollection.loginPage
				.instantiatePage(YouAreInvitedPage.class);
		PagesCollection.youAreInvitedPage.setUrl(invitationLink);
		PagesCollection.youAreInvitedPage.navigateTo();
	}
}
