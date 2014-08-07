package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws InterruptedException, IOException{
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.contactListPage.pressLaterButton();
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));

	}
	
	@Given("^I do not see Contact list with name (.*)$")
	public void GivenIDoNotSeeContactListWithName(String value) throws Throwable {
		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		Assert.assertFalse(PagesCollection.contactListPage.isContactExists(value));
	}
	
	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws Throwable  {		
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.androidPage = PagesCollection.contactListPage.tapOnName(name);
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws Exception  {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.personalInfoPage = (PersonalInfoPage) PagesCollection.contactListPage.tapOnName(name);
	}

	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		PagesCollection.peoplePickerPage = (PeoplePickerPage)PagesCollection.contactListPage.swipeDown(1000);
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

	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws IOException {

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.contactListPage.swipeRightOnContact(500, contact);
	}

	@When("^I click mute conversation$")
	public void IClickMuteConversation() throws IOException, InterruptedException {

		PagesCollection.contactListPage.clickOnMute();
	}

	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws InterruptedException {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.contactListPage.pressLaterButton();
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}


	@Then("^I see contact list loaded with User name (.*)$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));
	}


	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Then("^Contact (.*) is not muted$")
	public void ThenContactIsNotMuted(String contact) throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}
	
	@Then("^Contact name (.*) is not in list$")
	public void ThenContactNameIsNotInList(String value) throws Throwable {
		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		Assert.assertFalse(PagesCollection.contactListPage.isContactExists(value));
	}
}
