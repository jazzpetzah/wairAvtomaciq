package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private PeoplePickerPage getPeoplePickerPage() throws Exception {
		return (PeoplePickerPage) pagesCollection
				.getPage(PeoplePickerPage.class);
	}

	/**
	 * Checks to see that the people picker page (search view) is visible
	 * 
	 * @step. ^I see People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Exception {
		Assert.assertTrue("People Picker is not visible", getPeoplePickerPage()
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
		getPeoplePickerPage().tapPeopleSearch();
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
		getPeoplePickerPage().tapOnContactInTopPeoples(contact);
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
		getPeoplePickerPage().tapCreateConversation();
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
		getPeoplePickerPage().tapClearButton();
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
		getPeoplePickerPage().swipeDown(500);
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
		getPeoplePickerPage().typeTextInPeopleSearch(contact);
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
		getPeoplePickerPage().typeTextInPeopleSearch(email);
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
		getPeoplePickerPage().typeTextInPeopleSearch(list[0]);
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
		getPeoplePickerPage().typeTextInPeopleSearch(contact);
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
		getPeoplePickerPage().addTextToPeopleSearch(contact);
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
		getPeoplePickerPage().waitUserPickerFindUser(contact);
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
		Assert.assertTrue("Some results were found in People Picker",
				getPeoplePickerPage().isNoResultsFoundVisible());
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
		getPeoplePickerPage().selectContact(contact);
	}

	/**
	 * Checks to see if the add to conversation button is visible
	 * 
	 * @step. ^I see Add to conversation button$
	 * @throws Exception
	 * 
	 */
	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton() throws Exception {
		Assert.assertTrue("Add to conversation button is not visible",
				getPeoplePickerPage().isAddToConversationBtnVisible());
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
		getPeoplePickerPage().tapOnSendInvitation();
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
		getPeoplePickerPage().clickOnAddToCoversationButton();
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
		getPeoplePickerPage().navigateBack();
	}

	private String rememberedPYMKItemName = null;

	/**
	 * Saves the name of the first PYMK item into an internal variable
	 * 
	 * @step. ^I remember the name of the first PYMK item$
	 * @throws Exception
	 * 
	 */
	@When("^I remember the name of the first PYMK item$")
	public void IRememeberTheNameOfFirstPYMKItem() throws Exception {
		rememberedPYMKItemName = getPeoplePickerPage().getPYMKItemName(1);
	}

	/**
	 * Click + button on the first PYMK item
	 * 
	 * @step. ^I click \\+ button on the first PYMK item$
	 * @throws Exception
	 * 
	 */
	@When("^I click \\+ button on the first PYMK item$")
	public void IClickPlusButtonOnTheFirstPYMKItem() throws Exception {
		getPeoplePickerPage().clickPlusOnPYMKItem(1);
	}

	/**
	 * Do short or long swipe right the first PYMK entry
	 * 
	 * @step. ^I do (short|long) swipe right on the first PYMK item$
	 * @param swipeType
	 *            either short or long
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I do (short|long) swipe right on the first PYMK item$")
	public void IDoShortOrLongSwipeRightOnFirstPYMKItem(String swipeType)
			throws Exception {
		if (swipeType.equals("short")) {
			getPeoplePickerPage().shortSwipeRigthOnPYMKItem(1);
		} else if (swipeType.equals("long")) {
			getPeoplePickerPage().longSwipeRigthOnPYMKItem(1);
		}
	}

	/**
	 * Hide random connect by Hide button
	 * 
	 * @step. ^I click hide button on the first PYMK item$
	 * @throws Exception
	 * 
	 */
	@When("^I click hide button on the first PYMK item$")
	public void IClickHideButtonOnTheFirstPYMKItem() throws Exception {
		getPeoplePickerPage().clickHideButtonOnPYMKItem(1);
	}

	/**
	 * Verify that the previously remembered PYMK item is not visible anymore
	 * 
	 * @step. ^I do not see the previously remembered PYMK item$
	 * @throws Exception
	 * 
	 */
	@Then("^I do not see the previously remembered PYMK item$")
	public void IDonotSeePreviouslyRememberedPYMKItem() throws Exception {
		if (rememberedPYMKItemName == null) {
			throw new IllegalStateException(
					"Please call the corresponding step to remember PYMK item name first");
		}
		Assert.assertTrue(getPeoplePickerPage().waitUntilPYMKItemIsInvisible(
				rememberedPYMKItemName));
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
		Assert.assertTrue(String.format(
				"User '%s' is not visible in People Picker", contact),
				getPeoplePickerPage().userIsVisible(contact));
	}

	/**
	 * Looks for a group chat in the people picker search view
	 * 
	 * @step. ^I see group (.*) in People picker$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@Then("^I see group (.*) in People picker$")
	public void ThenISeeGroupInPeoplePicker(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(String.format(
				"Group '%s' is not visible in conversations list", name),
				getPeoplePickerPage().groupIsVisible(name));
	}

	/**
	 * Check to see that the top people section is visible or not
	 * 
	 * @step. ^I( do not)? see TOP PEOPLE$
	 * @param shouldNotBeVisible
	 *            is set to null is "do not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see TOP PEOPLE$")
	public void ThenIDontSeeTopPeople(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue(
					"TOP PEOPLE overlay is hidden, but it should be visible",
					getPeoplePickerPage().isTopPeopleHeaderVisible());
		} else {
			Assert.assertTrue(
					"TOP PEOPLE overlay is visible, but it should be hidden",
					getPeoplePickerPage().waitUntilTopPeopleHeaderInvisible());
		}
	}

	private final static long PYMK_VISIBLITY_TIMEOUT_MILLISECONDS = 120 * 1000;

	/**
	 * Reopen People Picker until PYMK appear on the screen
	 * 
	 * @step. ^I keep reopening People Picker until PYMK are visible$
	 * 
	 * @throws Exception
	 */
	@When("^I keep reopening People Picker until PYMK are visible$")
	public void ReopenPeoplePickerUntilPYMKAppears() throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		while (!getPeoplePickerPage().waitUntilPYMKItemIsVisible(1)
				&& System.currentTimeMillis() - millisecondsStarted <= PYMK_VISIBLITY_TIMEOUT_MILLISECONDS) {
			getPeoplePickerPage().tapClearButton();
			Thread.sleep(3000);
			((ContactListPage) pagesCollection.getPage(ContactListPage.class))
					.openPeoplePicker();
			getPeoplePickerPage().hideKeyboard();
		}
		getPeoplePickerPage().hideKeyboard();
		Assert.assertTrue(String.format(
				"PYMK section has not been shown after %s seconds timeout",
				PYMK_VISIBLITY_TIMEOUT_MILLISECONDS / 1000),
				getPeoplePickerPage().waitUntilPYMKItemIsVisible(1));
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
		if (!getPeoplePickerPage().isTopPeopleHeaderVisible()) {
			// FIXME: Workaround for bug where Top People is sometimes not shown
			// if sign in for the first time
			getPeoplePickerPage().tapClearButton();
			((ContactListPage) pagesCollection.getPage(ContactListPage.class))
					.tapOnMyAvatar();
			((PersonalInfoPage) pagesCollection.getPage(PersonalInfoPage.class))
					.tapOptionsButton();
			((PersonalInfoPage) pagesCollection.getPage(PersonalInfoPage.class))
					.tapSignOutBtn();
			new EmailSignInSteps().GivenISignIn(
					usrMgr.getSelfUser().getEmail(), usrMgr.getSelfUser()
							.getPassword());
			new ContactListPageSteps().GivenISeeContactList();
			((ContactListPage) pagesCollection.getPage(ContactListPage.class))
					.openPeoplePicker();
		}
		if (!getPeoplePickerPage().isTopPeopleHeaderVisible()) {
			throw new AssertionError(String.format(
					"Top People list has not been shown after %s seconds",
					TOP_PEOPLE_VISIBILITY_TIMEOUT_MILLISECONDS / 1000));
		}
	}
}
