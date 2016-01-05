package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BringYourFriendsPopoverPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();
	private String invitationLink = null;

	/**
	 * Verifies whether Invite People popover is visible or not
	 * 
	 * @step. ^I( do not)? see Invite People popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not provided
	 * @throws Exception
	 */
	@Then("^I( do not)? see Invite People popover$")
	public void ISeeInvitatePeoplePopover(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
					.isVisible();
		} else {
			webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
					.isNotVisible();
		}
	}

	/**
	 * Verifies whether Share Contacts button is visible or not
	 * 
	 * @step. ^I( do not)? see Share Contacts button$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part is not provided
	 * @throws Exception
	 */
	@Then("^I( do not)? see Share Contacts button$")
	public void ISeeShareContactsButton(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
					.isShareContactsButtonVisible();
		} else {
			webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
					.isShareContactsButtonNotVisible();
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
		webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
				.clickInviteButton();
	}

	/**
	 * Save invitation link from the corresponding popover into internal
	 * variable
	 * 
	 * @step. ^I remember invitation link on Bring Your Friends popover$
	 * 
	 */
	@When("^I remember invitation link on Bring Your Friends popover$")
	public void IRemeberInvitationLink() throws Exception {
		invitationLink = webappPagesCollection.getPage(
				BringYourFriendsPopoverPage.class).parseInvitationLink();
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

		YouAreInvitedPage youAreInvitedPage = webappPagesCollection
				.getPage(YouAreInvitedPage.class);
		youAreInvitedPage.setUrl(invitationLink);
		youAreInvitedPage.navigateTo();
	}
}
