package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	
	@When("I search for user (.*)")
	public void WhenISearchForUser(String user) {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.getPeoplePickerPage().searchForText(user);
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
	
	@When("I send invitation to user")
	public void WhenISendInvitationToUser() {
		CommonSteps.senderPages.getPeoplePickerPage().sendInvitationToUserIfRequested();
	}
}
