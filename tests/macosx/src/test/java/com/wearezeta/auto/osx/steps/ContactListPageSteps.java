package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	@Given ("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws IOException {
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().isContactWithNameExists(name));
	}
	
	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String value) {
		Assert.assertTrue("Login finished", CommonSteps.senderPages.getLoginPage().waitForLogin());
		Assert.assertTrue(CommonSteps.senderPages.getLoginPage().isLoginFinished(value));
	}
	
	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String value) throws MalformedURLException, IOException {
		Assert.assertTrue(CommonSteps.senderPages.getContactListPage().openConversation(value));
		CommonSteps.senderPages.setConversationPage(new ConversationPage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
	 }
	
	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList() throws MalformedURLException, IOException {
		CommonSteps.senderPages.getContactListPage().openPeoplePicker();
		CommonSteps.senderPages.setPeoplePickerPage(new PeoplePickerPage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
	}
	
	@Given("I go to user (.*) profile") 
	public void GivenIGoToUserProfile(String user) throws MalformedURLException, IOException {
		GivenIOpenConversationWith(user);
		CommonSteps.senderPages.setUserProfilePage(new UserProfilePage(
				CommonUtils.getUrlFromConfig(ContactListPageSteps.class),
				CommonUtils.getAppPathFromConfig(ContactListPageSteps.class)));
	}
}
