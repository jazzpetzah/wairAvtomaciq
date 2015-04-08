package com.wearezeta.auto.android;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@SuppressWarnings("unused")
	private void disableHint(String name) throws Exception {
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

	/**
	 * Close People Picker and open contact list without contacts
	 * 
	 * @step. ^I see Contact list with no contacts and my name (.*)$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with no contacts and my name (.*)$")
	public void GivenISeeContactListWithNoContactsAndMyNameAnd(String name)
			throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));

	}

	/**
	 * Close People Picker and open contact list with contacts
	 * 
	 * @step. ^I see Contact list with no contacts and my name (.*)$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		PagesCollection.contactListPage.waitForContactListLoadFinished();

	}

	@Given("^I do not see Contact list with name (.*)$")
	public void GivenIDoNotSeeContactListWithName(String value)
			throws Exception {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(value));
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String value) throws Exception {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		AndroidPage page = PagesCollection.contactListPage
				.tapOnName(value);
		PagesCollection.androidPage = page;
		PagesCollection.dialogPage = (DialogPage) page;
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage = (PersonalInfoPage) PagesCollection.contactListPage
				.tapOnName(name);
	}

	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Exception {
		PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
				.swipeDown(1000);
	}

	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Exception {
		try {
			contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
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
	public void ISwipeRightOnContact(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		AndroidPage page = PagesCollection.contactListPage.swipeRightOnContact(1000, contact);
		if (page instanceof DialogPage) {
			PagesCollection.dialogPage = (DialogPage) page;
		}
	}

	@When("^I swipe archive conversation (.*)$")
	public void ISwipeArchiveConversation(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		AndroidPage page = PagesCollection.contactListPage
				.swipeOnArchiveUnarchive(contact);
		if (page instanceof DialogPage) {
			PagesCollection.dialogPage = (DialogPage) page;
		}
	}


	@When("^I press Open StartUI Button")
	public void WhenIPressOpenStartUIButton() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage.pressOpenStartUIButton();
	}
	
	@When("^I swipe up contact list$")
	public void ISwipeUpContactList() throws Exception {
		PagesCollection.contactListPage.contactListSwipeUp(1000);
	}

	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws Exception {
		contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactExists(contact1 + ", " + contact2)
				|| PagesCollection.contactListPage.isContactExists(contact2
						+ ", " + contact1));
	}

	@Then("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}

	@Then("^I see contact list loaded with User name (.*)$")
	public void ISeeUserNameFirstInContactList(String value) throws Exception {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.contactListPage.waitForConversationListLoad();
		Assert.assertTrue(PagesCollection.contactListPage.isContactExists(value,5));
	}

	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws Exception {

		Assert.assertTrue(PagesCollection.contactListPage.isContactMuted());
	}

	@Then("^Contact (.*) is not muted$")
	public void ThenContactIsNotMuted(String contact) throws Exception {

		Assert.assertFalse(PagesCollection.contactListPage.isContactMuted());
	}

	@Then("^Contact name (.*) is not in list$")
	public void ThenContactNameIsNotInList(String value) throws Exception {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(value));
	}

	/**
	 * Verify that Play/Pause media content button is visible in Conversation
	 * List
	 * 
	 * @step. ^I see PlayPause media content button in Conversations List$
	 * 
	 */
	@Then("^I see PlayPause media content button in Conversations List$")
	public void ThenISeePlayPauseMediaContentButtonInConvLst()
			throws NumberFormatException, Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isPlayPauseMediaButtonVisible());
	}
	
	/**
	 * Check that conversation list contains missed call icon
	 * 
	 * @step. ^Conversation List contains missed call icon$
	 * 
	 * @throws Exception
	 */
	@Then("^Conversation List contains missed call icon$")
	public void ThenISeeMissedCallIcon() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage.isVisibleMissedCallIcon());
	}
}
