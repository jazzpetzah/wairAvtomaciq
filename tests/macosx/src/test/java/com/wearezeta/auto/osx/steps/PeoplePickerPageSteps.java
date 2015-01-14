package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	@When("I search for user (.*)")
	public void WhenISearchForUser(String user) {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getPeoplePickerPage().searchForText(user);
	}
	
	@When("I search by email for user (.*)")
	public void ISearchByEmailForUser(String user) throws Exception {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(user);
		user = dstUser.getName();
		String email = dstUser.getEmail();
		CommonOSXSteps.senderPages.getPeoplePickerPage().searchForText(email);
	}
	
	@When("I see user (.*) in search results")
	public void WhenISeeUserFromSearchResults(String user) {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue(
				"User " + user + " not found in results",
				CommonOSXSteps.senderPages.getPeoplePickerPage().areSearchResultsContainUser(user));
		CommonOSXSteps.senderPages.getPeoplePickerPage().scrollToUserInSearchResults(user);
	}
	
	@When("I add user (.*) from search results")
	public void WhenIAddUserFromSearchResults(String user) {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getPeoplePickerPage().chooseUserInSearchResults(user);
	}
	
	 @Given("^I select user (.*) from search results")
	 public void ISelectUserFromSearchResults(String user) throws Exception {
		 user = usrMgr.findUserByNameOrNameAlias(user).getName();
		 PeoplePickerPage page = CommonOSXSteps.senderPages.getPeoplePickerPage();
		 page.selectUserInSearchResults(user);
		 
	 }
	 
	@When("I send invitation to user")
	public void WhenISendInvitationToUser() {
		CommonOSXSteps.senderPages.getPeoplePickerPage().sendInvitationToUserIfRequested();
	}
	
	@When("I unblock user")
	public void IUnblockUserInPeoplePicker() {
		CommonOSXSteps.senderPages.getPeoplePickerPage().unblockUser();
	}
}
