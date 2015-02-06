package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
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
		Assert.assertTrue(PagesCollection.userProfilePopupPage
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
				PagesCollection.userProfilePopupPage.getUserName());
	}
	
	/**
	 * Verify that Add people button is shown on User profile popup
	 * 
	 * @step. I see Add people button on User Profile Popup Page
	 * 
	 */
	@When("I see Add people button on User Profile Popup Page")
	public void ISeeAddPeopleButtonOnUserProfilePopupPage() {
		Assert.assertTrue(PagesCollection.userProfilePopupPage
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
		Assert.assertTrue(PagesCollection.userProfilePopupPage
				.isBlockButtonVisible());
	}

}
