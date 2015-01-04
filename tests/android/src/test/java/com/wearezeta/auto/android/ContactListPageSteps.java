package com.wearezeta.auto.android;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.user_management.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = usrMgr.findUserByNameAlias(name).getName();
		PagesCollection.contactListPage.pressLaterButton();
		// TODO: revisit later
		Thread.sleep(2000);
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}

		// disableHint(name);

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));

	}

	@Given("^I do not see Contact list with name (.*)$")
	public void GivenIDoNotSeeContactListWithName(String value)
			throws Throwable {
		value = usrMgr.findUserByNameAlias(value).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(value));
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws Throwable {
		name = usrMgr.findUserByNameAlias(name).getName();
		PagesCollection.androidPage = PagesCollection.contactListPage
				.tapOnName(name);
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws Exception {
		name = usrMgr.findUserByNameAlias(name).getName();
		PagesCollection.personalInfoPage = (PersonalInfoPage) PagesCollection.contactListPage
				.tapOnName(name);
	}

	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
				.swipeDown(1000);
		PagesCollection.contactListPage.pressLaterButton();
	}

	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Throwable {
		contact1 = usrMgr.findUserByNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameAlias(contact2).getName();
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
		final String[] names = new String[] { contact1, contact2 };
		groupChatSteps.ThenISeeGroupChatPage(StringUtils.join(names,
				CommonSteps.ALIASES_SEPARATOR));
	}

	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws IOException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		PagesCollection.contactListPage.swipeRightOnContact(1000, contact);
	}

	@When("^I click mute conversation (.*)$")
	public void IClickMuteConversation(String contact) throws IOException,
			InterruptedException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		PagesCollection.contactListPage.clickOnMute(contact);
	}

	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws InterruptedException {
		contact1 = usrMgr.findUserByNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameAlias(contact2).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactExists(contact1 + ", " + contact2)
				|| PagesCollection.contactListPage.isContactExists(contact2
						+ ", " + contact1));
	}

	@Then("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws Throwable {
		name = usrMgr.findUserByNameAlias(name).getName();
		PagesCollection.contactListPage.pressLaterButton();
		// TODO: revisit later
		Thread.sleep(2000);
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}

		disableHint(name);

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}

	@Then("^I see contact list loaded with User name (.*)$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
		value = usrMgr.findUserByNameAlias(value).getName();
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(value));
	}

	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws Throwable {

		Assert.assertTrue(PagesCollection.contactListPage.isContactMuted());
	}

	@Then("^Contact (.*) is not muted$")
	public void ThenContactIsNotMuted(String contact) throws Throwable {

		Assert.assertFalse(PagesCollection.contactListPage.isContactMuted());
	}

	@Then("^Contact name (.*) is not in list$")
	public void ThenContactNameIsNotInList(String value) throws Throwable {
		value = usrMgr.findUserByNameAlias(value).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(value));
	}
}
