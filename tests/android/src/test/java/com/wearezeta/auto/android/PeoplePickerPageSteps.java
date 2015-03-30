package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isPeoplePickerPageVisible());
	}

	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
		PagesCollection.peoplePickerPage.tapPeopleSearch();
	}

	@When("^I tap on (.*) in Top People$")
	public void WhenITapInTopPeople(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.tapOnContactInTopPeoples(contact);
	}

	@When("^I tap on create conversation$")
	public void WhenITapOnCreateConversation() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage
				.tapCreateConversation();
		;
	}

	@When("^I press Clear button$")
	public void WhenIPressClearButton() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage
				.tapClearButton();
	}

	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
	}

	@When("^I input in People picker search field user email (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email)
			throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.typeTextInPeopleSearch(email);
	}

	@When("^I input in search field part (.*) of user name to connect to (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldPartOfUserName(String part,
			String contact) throws Throwable {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		String[] list = contact.split("(?<=\\G.{" + part + "})");
		PagesCollection.peoplePickerPage.typeTextInPeopleSearch(list[0]);
	}

	@When("^I input in search field user name to connect to (.*)$")
	public void WhenIInputInSearchFieldUserNameToConnectTo(String contact)
			throws Throwable {
		// FIXME : ambiguous use of email field when the step states only user
		// name
		try {
			ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
			contact = dstUser.getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.typeTextInPeopleSearch(contact);
	}

	@When("^I add in search field user name to connect to (.*)$")
	public void WhenIAddInSearchFieldUserNameToConnectTo(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.addTextToPeopleSearch(contact);
	}

	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
	}

	@Then("^I see that no results found$")
	public void ISeeNoResultsFound() {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isNoResultsFoundVisible());
	}

	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
		PagesCollection.androidPage = PagesCollection.peoplePickerPage
				.selectContact(contact);

		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
	}

	@When("^I  long tap on user name found on People picker page (.*)$")
	public void WhenILongTapOnUserNameFoundOnPeoplePickerPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.selectContactByLongTap(contact);
	}

	/**
	 * Tap on Gmail link
	 * 
	 * @step. ^I tap on Gmail link$
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	@When("^I tap on Gmail link$")
	public void WhenITapOnGmailLink() throws NumberFormatException, Exception {
		PagesCollection.commonAndroidPage = PagesCollection.peoplePickerPage
				.tapOnGmailLink();
	}

	@When("^I tap on group name found on People picker page (.*)$")
	public void WhenITapOnGroupNameFoundOnPeoplePickerPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.androidPage = PagesCollection.peoplePickerPage
				.selectGroup(contact);
	}

	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton() {
		Assert.assertTrue("Add to conversation button is not visible",
				PagesCollection.peoplePickerPage
						.isAddToConversationBtnVisible());
	}

	/**
	 * Tap on Send an invitation
	 * 
	 * @step. ^I tap on Send an invitation$
	 * 
	 */
	@When("^I tap on Send an invitation$")
	public void WhenITapOnSendAnInvitation() {
		PagesCollection.peoplePickerPage.tapOnSendInvitation();
	}

	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Exception {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage
				.clickOnAddToCoversationButton();
	}

	@When("^I navigate back to Conversations List")
	public void WhenINavigateBackToConversationsList() throws Exception {
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage.navigateBack();
	}
	
	@Then("^I see user (.*)  in People picker$")
	public void ThenISeeUserInPeoplePicker(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.userIsVisible(contact));
	}

	@Then("^I see group (.*)  in People picker$")
	public void ThenISeeGroupInPeoplePicker(String contact) throws Throwable {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.groupIsVisible(contact));
	}

}
