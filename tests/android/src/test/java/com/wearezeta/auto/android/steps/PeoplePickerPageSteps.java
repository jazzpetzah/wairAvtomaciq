package com.wearezeta.auto.android.steps;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public static String randomConnectName = "";
	private static WebElement randomConnect;

	/**
	 * Checks to see that the people picker page (search view) is visible
	 * 
	 * @step. ^I see People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isPeoplePickerPageVisible());
	}

	/**
	 * Taps on the search bar in the people picker page
	 * 
	 * @step. ^I tap on Search input on People picker page$
	 * 
	 */
	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
		PagesCollection.peoplePickerPage.tapPeopleSearch();
	}

	/**
	 * Selects a contact from the top people section in the people picker page
	 * 
	 * @step. ^I tap on (.*) in Top People$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@When("^I tap on (.*) in Top People$")
	public void WhenITapInTopPeople(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.tapOnContactInTopPeoples(contact);
	}

	/**
	 * Creates a conversation from any selected users
	 * 
	 * @step. ^I tap on create conversation$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap on create conversation$")
	public void WhenITapOnCreateConversation() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage
				.tapCreateConversation();
	}

	/**
	 * Presses the close button in the people picker page
	 * 
	 * @step. ^I press Clear button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Clear button$")
	public void WhenIPressClearButton() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage
				.tapClearButton();
	}

	/**
	 * Swipe down people picker
	 * 
	 * @step. ^I swipe down people picker$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe down people picker$")
	public void ISwipeDownContactList() throws Exception {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.peoplePickerPage
				.swipeDown(500);
	}

	/**
	 * Types a user name into the people picker search field.
	 * 
	 * @step. ^I input in People picker search field user name (.*)$
	 * 
	 * @param contact
	 * @throws Exception
	 */
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

	/**
	 * Types a user email address into the people picker search field
	 * 
	 * @step. ^I input in People picker search field user email (.*)$
	 * 
	 * @param email
	 * @throws Exception
	 */
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

	/**
	 * Inputs a part of a username into the search field.
	 * 
	 * @step. ^I input in search field part (.*) of user name to connect to
	 *        (.*)$
	 * 
	 * @param part
	 * @param contact
	 * @throws Throwable
	 */
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

	/**
	 * -duplicate of WhenIInputInPeoplePickerSearchFieldUserName(String)
	 * 
	 * @step. ^I input in search field user name to connect to (.*)$
	 * 
	 */
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

	/**
	 * Adds user name to search field (existing content is not cleaned)
	 * 
	 * @step. ^I add in search field user name to connect to (.*)$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
	@When("^I add in search field user name to connect to (.*)$")
	public void WhenIAddInSearchFieldUserNameToConnectTo(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.addTextToPeopleSearch(contact);
	}

	/**
	 * Wait for a user in the people picker search list
	 * 
	 * @step. ^I see user (.*) found on People picker page$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
	}

	/**
	 * Checks to see that there are no results in the search field
	 * 
	 * @step. ^I see that no results found$
	 * @throws Exception
	 * 
	 */
	@Then("^I see that no results found$")
	public void ISeeNoResultsFound() throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isNoResultsFoundVisible());
	}

	/**
	 * Taps on a name found in the people picker page
	 * 
	 * @step. ^I tap on user name found on People picker page (.*)$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@When("^I tap on user name found on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		// PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
		PagesCollection.currentPage = PagesCollection.peoplePickerPage
				.selectContact(contact);

		if (PagesCollection.currentPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.currentPage;
		}
	}

	/**
	 * -unused
	 * 
	 * @step. ^I long tap on user name found on People picker page (.*)$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
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
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 * 
	 */
	@When("^I tap on Gmail link$")
	public void WhenITapOnGmailLink() throws NumberFormatException, Exception {
		PagesCollection.commonAndroidPage = PagesCollection.peoplePickerPage
				.tapOnGmailLink();
	}

	/**
	 * Checks to see if the add to conversation button is visible
	 * 
	 * @step. ^I see Add to conversation button$
	 * 
	 */
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
	 * @throws Exception
	 * 
	 */
	@When("^I tap on Send an invitation$")
	public void WhenITapOnSendAnInvitation() throws Exception {
		PagesCollection.peoplePickerPage.tapOnSendInvitation();
	}

	/**
	 * Clicks on the Add to conversation button
	 * 
	 * @step. ^I click on Add to conversation button$
	 * 
	 * @throws Exception
	 */
	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Exception {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage
				.clickOnAddToCoversationButton();
	}

	/**
	 * Navigates back to the conversation list by swiping down
	 * 
	 * @step. ^I navigate back to Conversations List$
	 * 
	 * @throws Exception
	 */
	@When("^I navigate back to Conversations List")
	public void WhenINavigateBackToConversationsList() throws Exception {
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage
				.navigateBack();
	}

	/**
	 * Press + button on Random contact from PYMK
	 * 
	 * @step. ^I press \\+ button on a random Connect$
	 * @throws Exception
	 * 
	 */
	@When("^I press \\+ button on a random Connect$")
	public void WhenIPressPlusButtonOnARandomConnect() throws Exception {
		randomConnect = PagesCollection.peoplePickerPage.selectRandomConnect();
		randomConnectName = PagesCollection.peoplePickerPage
				.pressPlusOnContact(randomConnect);
	}

	/**
	 * Tap on Random contact from PYMK
	 * 
	 * @step. ^I tap on a random contact from PYMK$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I tap on a random contact from PYMK$")
	public void WhenITapOnRandomContactFromPYMK() throws Exception {
		randomConnect = PagesCollection.peoplePickerPage.selectRandomConnect();
		randomConnectName = PagesCollection.peoplePickerPage
				.getPYMKContactName(randomConnect);
		PagesCollection.connectToPage = PagesCollection.peoplePickerPage
				.tapOnPYMKContact(randomConnect);
	}

	/**
	 * Tap on Random contact from PYMK and save it name
	 * 
	 * @step. ^I tap on a random contact from PYMK and set it name to (.*)$
	 * 
	 * @param contact
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I tap on a random contact from PYMK and set it name to (.*)$")
	public void WhenITapOnRandomContactFromPYMKAndSetItNameTo(String contact)
			throws Exception {
		randomConnect = PagesCollection.peoplePickerPage.selectRandomConnect();
		randomConnectName = PagesCollection.peoplePickerPage
				.getPYMKContactName(randomConnect);
		PagesCollection.connectToPage = PagesCollection.peoplePickerPage
				.tapOnPYMKContact(randomConnect);
		try {
			usrMgr.findUserByNameOrNameAlias(contact)
					.setName(randomConnectName);
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
	}

	/**
	 * Swipe on Random contact from PYMK
	 * 
	 * @step. ^I swipe on random connect$
	 * @throws Exception
	 * 
	 */
	@When("^I swipe on random connect$")
	public void WhenISwipeOnRandomConnect() throws Exception {
		randomConnect = PagesCollection.peoplePickerPage.selectRandomConnect();
		randomConnectName = PagesCollection.peoplePickerPage
				.swipePYMKContact(randomConnect);
	}

	/**
	 * Hide random connect by swipe (not the same as
	 * "Swipe on Random contact from PYMK")
	 * 
	 * @step. ^I hide random connect by swipe$
	 * @throws Exception
	 * 
	 */
	@When("^I hide random connect by swipe$")
	public void WhenIHideRandomConnectBySwipe() throws Exception {
		PagesCollection.peoplePickerPage.swipeRightPYMKHideMenu();
	}

	/**
	 * Hide random connect by Hide button
	 * 
	 * @step. ^I click on PYMK hide button$
	 * @throws Exception
	 * 
	 */
	@When("^I click on PYMK hide button$")
	public void WhenIClickOnPYMKHideButton() throws Exception {
		PagesCollection.peoplePickerPage.clickPYMKHideButton();
	}

	/**
	 * Verify that random connect is not visible
	 * 
	 * @step. ^I do not see random connect$
	 * @throws Exception
	 * 
	 */
	@Then("^I do not see random connect$")
	public void ThenIDonotSeeRandomConnect() throws Exception {
		Assert.assertFalse(PagesCollection.peoplePickerPage
				.pYMKcontactIsVisible(randomConnectName, randomConnect));
	}

	/**
	 * Check that user exists in People picker
	 * 
	 * @step. ^I see user (.*) in People picker$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
	@Then("^I see user (.*) in People picker$")
	public void ThenISeeUserInPeoplePicker(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.userIsVisible(contact));
	}

	/**
	 * Looks for a group chat in the people picker search view
	 * 
	 * @step. ^I see group (.*) in People picker$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
	@Then("^I see group (.*) in People picker$")
	public void ThenISeeGroupInPeoplePicker(String contact) throws Throwable {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.groupIsVisible(contact));
	}

	/**
	 * checks to see that the top people section is visible
	 * 
	 * @step. ^I see TOP PEOPLE$
	 * 
	 * @throws Exception
	 */
	@Then("^I see TOP PEOPLE$")
	public void ThenISeeTopPeople() throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isTopPeopleHeaderVisible());
	}

	/**
	 * checks to see that the top people section is NOT visible
	 * 
	 * @step. ^I see TOP PEOPLE$
	 * 
	 * @throws Exception
	 */
	@Then("^I do not see TOP PEOPLE$")
	public void ThenIDontSeeTopPeople() throws Exception {
		Assert.assertFalse(PagesCollection.peoplePickerPage
				.isTopPeopleHeaderVisible());
	}

	/**
	 * Waiting for PYMK loading
	 * 
	 * @step. ^I wait for PYMK for (.*) secs$
	 * 
	 * @param time
	 * 
	 * @throws Exception
	 */
	@When("I wait for PYMK for (.*) secs$")
	public void WhenIWaitForPYMKForSec(int time) throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.waitForPYMKForSecs(time));
	}

	private static final long TOP_PEOPLE_VISIBILITY_TIMEOUT_MILLISECONDS = 120 * 1000;

	/**
	 * Wait for Top People list to appear in People picker
	 * 
	 * @step. ^I wait until Top People list appears$
	 * 
	 * @throws Exception
	 */
	@When("^I wait until Top People list appears$")
	public void WaitForTopPeople() throws Exception {
		if (!PagesCollection.peoplePickerPage.isTopPeopleHeaderVisible()) {
			// FIXME: Workaround for bug where Top People is sometimes not shown
			// if sign in for the first time
			PagesCollection.contactListPage = PagesCollection.peoplePickerPage
					.tapClearButton();
			PagesCollection.personalInfoPage = PagesCollection.contactListPage
					.tapOnMyAvatar();
			PagesCollection.personalInfoPage.tapOptionsButton();
			PagesCollection.personalInfoPage.tapSignOutBtn();
			new LoginPageSteps().GivenISignIn(usrMgr.getSelfUser().getEmail(),
					usrMgr.getSelfUser().getPassword());
			new ContactListPageSteps().GivenISeeContactList();
			PagesCollection.peoplePickerPage = PagesCollection.contactListPage
					.openPeoplePicker();
		}
		if (!PagesCollection.peoplePickerPage.isTopPeopleHeaderVisible()) {
			throw new AssertionError(String.format(
					"Top People list has not been shown after %s seconds",
					TOP_PEOPLE_VISIBILITY_TIMEOUT_MILLISECONDS / 1000));
		}
	}
}
