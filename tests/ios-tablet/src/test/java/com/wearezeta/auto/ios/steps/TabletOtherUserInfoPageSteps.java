package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletOtherUserInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletOtherUserInfoPageSteps {
	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletOtherUserInfoPage getTabletOtherUserInfoPage()
			throws Exception {
		return (TabletOtherUserInfoPage) pagesCollecton
				.getPage(TabletOtherUserInfoPage.class);
	}

	/**
	 * Clicks remove button on the other user info popover
	 * 
	 * @step. ^I click Remove on iPad$
	 * @throws Throwable
	 */
	@When("^I click Remove on iPad$")
	public void IClickRemoveOniPad() throws Throwable {
		getTabletOtherUserInfoPage().removeFromConversationOniPad();
	}

	/**
	 * Verifies that it sees the about to remove someone message
	 * 
	 * @step. ^I see remove warning message on iPad$
	 * @throws Throwable
	 */
	@When("^I see remove warning message on iPad$")
	public void ISeeRemoveWarningMessageOniPad() throws Throwable {
		Assert.assertTrue(getTabletOtherUserInfoPage()
				.isRemoveFromConversationAlertVisible());
	}

	/**
	 * Clicks the confirm REMOVE button
	 * 
	 * @step. ^I confirm remove on iPad$
	 * @throws Throwable
	 */
	@When("^I confirm remove on iPad$")
	public void IConfirmRemoveOniPad() throws Throwable {
		getTabletOtherUserInfoPage().confirmRemove();
	}

	/**
	 * Verifies that singleuser mail and name is seen
	 * 
	 * @step. ^I see email and name of user (.*) on iPad popover$
	 * @param user
	 *            that I check name and mail for
	 * @throws Throwable
	 */
	@Then("^I see email and name of user (.*) on iPad popover$")
	public void ISeeEmailAndNameOfUserOniPadPopover(String user)
			throws Throwable {

		String participantNameTextFieldValue = "";
		String participantEmailTextFieldValue = "";

		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		String email = usrMgr.findUserByNameOrNameAlias(user).getEmail();

		participantNameTextFieldValue = getTabletOtherUserInfoPage()
				.getNameFieldValueOniPadPopover();
		participantEmailTextFieldValue = getTabletOtherUserInfoPage()
				.getEmailFieldValueOniPadPopover();
		Assert.assertTrue("Participant Name is incorrect and/or not displayed",
				participantNameTextFieldValue.equalsIgnoreCase(user));
		Assert.assertTrue(
				"Participant Email is incorrect and/or not displayed",
				participantEmailTextFieldValue.equalsIgnoreCase(email));
	}

}
