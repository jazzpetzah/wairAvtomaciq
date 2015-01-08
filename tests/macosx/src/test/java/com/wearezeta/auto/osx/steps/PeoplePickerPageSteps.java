package com.wearezeta.auto.osx.steps;

import java.util.NoSuchElementException;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	@When("I search for user (.*)")
	public void WhenISearchForUser(String user) {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getPeoplePickerPage().searchForText(user);
	}
	
	@When("I search by email for user (.*)")
	public void ISearchByEmailForUser(String user) throws InterruptedException {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(user);
		user = dstUser.getName();
		String email = dstUser.getEmail();
		CommonOSXSteps.senderPages.getPeoplePickerPage().searchForText(email);
	}
	
	@When("I see user (.*) in search results")
	public void WhenISeeUserFromSearchResults(String user) throws InterruptedException {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
			} catch (NoSuchElementException e) {
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
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		CommonOSXSteps.senderPages.getPeoplePickerPage().chooseUserInSearchResults(user);
	}
	
	 @Given("^I select user (.*) from search results")
	 public void ISelectUserFromSearchResults(String user) {
		 user = usrMgr.findUserByNameOrNameAlias(user).getName();
		 PeoplePickerPage page = CommonOSXSteps.senderPages.getPeoplePickerPage();
		 page.selectUserInSearchResults(user);
		 
	 }
	 
	@When("I send invitation to user")
	public void WhenISendInvitationToUser() {
		CommonOSXSteps.senderPages.getPeoplePickerPage().sendInvitationToUserIfRequested();
	}
	
	@Then("^I see Top People list in People Picker$")
	public void ISeeTopPeopleListInPeoplePicker() throws Throwable {
		Thread.sleep(2000);
		boolean topPeopleisVisible = CommonOSXSteps.senderPages.getPeoplePickerPage().isTopPeopleVisible();
		if (!topPeopleisVisible){
			CommonOSXSteps.senderPages.getPeoplePickerPage().closePeoplePicker();
			Thread.sleep(1000);
			CommonOSXSteps.senderPages.getContactListPage().openPeoplePicker();
			topPeopleisVisible = CommonOSXSteps.senderPages.getPeoplePickerPage().isTopPeopleVisible();
		}
		Assert.assertTrue("Top People not shown ", topPeopleisVisible);
	}
}
