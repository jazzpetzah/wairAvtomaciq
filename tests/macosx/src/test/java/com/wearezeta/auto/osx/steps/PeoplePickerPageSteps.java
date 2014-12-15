package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class PeoplePickerPageSteps {
	
	@When("I search for user (.*)")
	public void WhenISearchForUser(String user) {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.getPeoplePickerPage().searchForText(user);
	}
	
	@When("I search by email for user (.*)")
	public void ISearchByEmailForUser(String user) {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		String email = CommonUtils.retrieveRealUserEmailValue(user);
		CommonSteps.senderPages.getPeoplePickerPage().searchForText(email);
	}
	
	@When("I see user (.*) in search results")
	public void WhenISeeUserFromSearchResults(String user) {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		Assert.assertTrue(
				"User " + user + " not found in results",
				CommonSteps.senderPages.getPeoplePickerPage().areSearchResultsContainUser(user));
		CommonSteps.senderPages.getPeoplePickerPage().scrollToUserInSearchResults(user);
	}
	
	@When("I add user (.*) from search results")
	public void WhenIAddUserFromSearchResults(String user) {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.getPeoplePickerPage().chooseUserInSearchResults(user);
	}
	
	 @Given("^I select user (.*) from search results")
	 public void ISelectUserFromSearchResults(String user) {
		 user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		 PeoplePickerPage page = CommonSteps.senderPages.getPeoplePickerPage();
		 page.selectUserInSearchResults(user);
		 
	 }
	 
	@When("I send invitation to user")
	public void WhenISendInvitationToUser() {
		CommonSteps.senderPages.getPeoplePickerPage().sendInvitationToUserIfRequested();
	}
	
	@Given("I send invitation to (.*) by (.*)")
	public void ISendInvitationToUserByContact(String user, String contact) throws Throwable {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		
		ClientUser contactUser = null;
		ClientUser userUser = null;
		
		for (ClientUser us: CommonUtils.yourUsers) {
			if (us.getName().equals(contact)) contactUser = us;
			if (us.getName().equals(user)) userUser = us;
		}
		
		for (ClientUser us: CommonUtils.contacts) {
			if (us.getName().equals(contact)) contactUser = us;
			if (us.getName().equals(user)) userUser = us;
		}
		
		Assert.assertNotNull("Can't find contact with name " + contact, contactUser);
		Assert.assertNotNull("Can't find user with name " + user, userUser);
		
		BackEndREST.autoTestSendRequest(contactUser, userUser);
	}
	
	@Then("^I see Top People list in People Picker$")
	public void ISeeTopPeopleListInPeoplePicker() throws Throwable {
		boolean topPeopleisVisible = CommonSteps.senderPages.getPeoplePickerPage().isTopPeopleVisible();
		if (!topPeopleisVisible){
			CommonSteps.senderPages.getPeoplePickerPage().closePeoplePicker();
			Thread.sleep(1000);
			CommonSteps.senderPages.getContactListPage().openPeoplePicker();
			topPeopleisVisible = CommonSteps.senderPages.getPeoplePickerPage().isTopPeopleVisible();
		}
		Assert.assertTrue("Top People not shown ", topPeopleisVisible);
	}
}
