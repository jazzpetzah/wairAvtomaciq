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

	private void disableHint(String name) throws Throwable {
		Thread.sleep(2000);
		if (PagesCollection.contactListPage.isHintVisible()) {
			PagesCollection.contactListPage.closeHint();
			Thread.sleep(1000);
			ISwipeDownContactList();
			if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
				PagesCollection.peoplePickerPage.tapClearButton();
			}
			
			WhenITapOnMyName(name);
			PagesCollection.contactListPage.navigateBack();
			
			String contactName = "aqaContact1";
			WhenITapOnContactName(contactName);
			
			if (PagesCollection.contactListPage.isHintVisible()) {
				PagesCollection.contactListPage.closeHint();
			}
			PagesCollection.contactListPage.navigateBack();
			
			WhenITapOnContactName(contactName);
			
			if (PagesCollection.contactListPage.isHintVisible()) {
				PagesCollection.contactListPage.closeHint();
			}
			DialogPageSteps steps = new DialogPageSteps();
			steps.WhenISeeDialogPage();
			steps.WhenISwipeOnTextInput();
			steps.WhenISwipeLeftOnTextInput();
			PagesCollection.contactListPage.navigateBack();
		}
	}
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable{
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.contactListPage.pressLaterButton();
		//TODO: revisit later
		Thread.sleep(2000);
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}
		
		//disableHint(name);
		
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
		PagesCollection.contactListPage.pressLaterButton();
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

		DialogPageSteps groupChatSteps = new DialogPageSteps();
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
	
	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws InterruptedException {

		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactExists(contact1 + ", " + contact2) || PagesCollection.contactListPage
				.isContactExists(contact2 + ", " + contact1));
	}

	@Then ("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws Throwable {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.contactListPage.pressLaterButton();
		//TODO: revisit later
		Thread.sleep(2000);
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}

		disableHint(name);
		
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
