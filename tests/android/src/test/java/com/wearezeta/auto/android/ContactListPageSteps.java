package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name){
		if(name.contains(CommonUtils.YOUR_USER_1)){
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.yourUsers.get(0).getName()));
		}
		else{
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		}
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws IOException  {
		if(name.contains(CommonUtils.CONTACT_1)){
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(CommonUtils.contacts.get(0).getName());
		}
		else if(name.contains(CommonUtils.YOUR_USER_2)){
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(CommonUtils.yourUsers.get(1).getName());
		}
		else{
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
		}
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException  {
		if(name.contains(CommonUtils.YOUR_USER_1)){
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(CommonUtils.yourUsers.get(0).getName());
		}
		else{
			PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
		}
	}

	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) {
		Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
		if(name.contains(CommonUtils.YOUR_USER_1)){
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.yourUsers.get(0).getName()));
		}
		else{
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		}

	}

	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		PagesCollection.peoplePickerPage= (PeoplePickerPage)PagesCollection.contactListPage.swipeDown(500);
	}
	
	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2) throws Throwable {
		
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeLeftOnDialogPage();
		
		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOherUserProfilePage(contact1);
		infoPageSteps.WhenISwipeDownOtherUserProfilePage();
		
		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();
		
		GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
		groupChatSteps.ThenISeeGroupChatPage(contact1, contact2);
	}

	@Then("^I see contact list loaded with User name (.*)$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
		if(value.contains(CommonUtils.CONTACT_1)){
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.contacts.get(0).getName()));
		}
		else if(value.contains(CommonUtils.YOUR_USER_2)){
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(CommonUtils.yourUsers.get(1).getName()));
		}
		else{
			Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));
		}
	}

}
