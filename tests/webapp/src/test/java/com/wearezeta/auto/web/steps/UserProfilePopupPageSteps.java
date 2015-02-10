package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class UserProfilePopupPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that User profile popup is shown
	 * 
	 * @step. I see User Profile Popup Page
	 * 
	 */
	@When("I see User Profile Popup Page")
	public void ISeeUserProfilePopupPage() {
		Assert.assertTrue(PagesCollection.userProfilePopup
				.isUserProfilePopupPageVisible());
	}

	/**
	 * Compares if name on User profile popup page is same as expected
	 * 
	 * @step. I see on User Profile Popup Page User username (.*)
	 * 
	 * @param name
	 *            user name string
	 */
	@When("I see on User Profile Popup Page User username (.*)")
	public void IseeUserNameOnUserProfilePage(String name) {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				PagesCollection.userProfilePopup.getUserName());
	}
	
	/**
	 * Verify that Add people button is shown on User profile popup
	 * 
	 * @step. I see Add people button on User Profile Popup Page
	 * 
	 */
	@When("I see Add people button on User Profile Popup Page")
	public void ISeeAddPeopleButtonOnUserProfilePopupPage() {
		Assert.assertTrue(PagesCollection.userProfilePopup
				.isAddPeopleButtonVisible());
	}
	
	/**
	 * Verify that Block button is shown on User profile popup
	 * 
	 * @step. I see Block button on User Profile Popup Page
	 * 
	 */
	@When("I see Block button on User Profile Popup Page")
	public void ISeeBlockButtonOnUserProfilePopupPage() {
		Assert.assertTrue(PagesCollection.userProfilePopup
				.isBlockButtonVisible());
	}
	
	/**
	 * Click on leave group chat button in User profile popup
	 * 
	 * @step. ^I click leave group chat$
	 * 
	 */
	@When("^I click leave group chat$")
	public void IClickLeaveGroupChat() {
		PagesCollection.userProfilePopup.leaveGroupChat();
	}
	
	/**
	 * Confirm leaving group chat by clicking LEAVE button
	 * 
	 * @step. ^I confirm leave group chat$
	 * 
	 */
	@When("^I confirm leave group chat$")
	public void IClickConfirmLeaveGroupChat() {
		PagesCollection.userProfilePopup.confirmLeaveGroupChat();
	}
	
	/**
	 * Click on a participant in User profile popup
	 * 
	 * @step. ^I click on participant (.*)$
	 * 
	 * @param name
	 *            user name string
	 */
	@When("^I click on participant (.*)$") 
	public void IClickOnParticipant(String name){
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.userProfilePopup.clickOnParticipant(name);
	}

	/**
	 * Remove participant from group chat by clicking - button
	 * 
	 * @step. ^I remove user from group chat$
	 * 
	 */
	@When("^I remove user from group chat$")
	public void IRemoveUserFromGroupChat() {
		PagesCollection.userProfilePopup.removeFromGroupChat();
	}
	
	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 * 
	 * @step. ^I confirm remove from group chat$
	 * 
	 */
	@When("^I confirm remove from group chat$")
	public void IClickConfirmRemoveFromGroupChat() {
		PagesCollection.userProfilePopup.confirmRemoveFromGroupChat();
	}
}
