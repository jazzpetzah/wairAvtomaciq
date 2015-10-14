package com.wearezeta.auto.ios.steps;

import java.awt.datatransfer.UnsupportedFlavorException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final Logger log = ZetaLogger
			.getLog(PeoplePickerPageSteps.class.getSimpleName());

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private PeoplePickerPage getPeoplePickerPage() throws Exception {
		return (PeoplePickerPage) pagesCollecton
				.getPage(PeoplePickerPage.class);
	}

	private ContactListPage getСontactListPage() throws Exception {
		return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
	}

	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Exception {
		Assert.assertTrue(getPeoplePickerPage().isPeoplePickerPageVisible());
	}

	@When("I see Upload contacts dialog")
	public void WhenISeeUploadContactsDialog() throws Exception {
		Assert.assertTrue("Upload dialog is not shown", getPeoplePickerPage()
				.isUploadDialogShown());
	}

	@When("I dont see Upload contacts dialog")
	public void WhenIDontSeeUploadContactsDialog() throws Exception {
		Assert.assertFalse("Upload dialog is shown", getPeoplePickerPage()
				.isUploadDialogShown());
	}

	@When("I click Later button on Upload dialog")
	public void IClickLaterButtonOnUploadDialog() throws Exception {
		getPeoplePickerPage().clickLaterButton();
	}

	@When("I click Continue button on Upload dialog")
	public void IClickContinueButtonOnUploadDialog() throws Exception {
		getPeoplePickerPage().clickContinueButton();
	}

	/**
	 * Verifies that CONNECT label is visible
	 * 
	 * @step. I see CONNECT label
	 * @throws Exception
	 */

	@When("I see CONNECT label")
	public void ISeePeopleYouMayKnowLabel() throws Exception {
		Assert.assertTrue("CONNECT label is not visible", getPeoplePickerPage()
				.isPeopleYouMayKnowLabelVisible());
	}

	/**
	 * Verifies that CONNECT label is not visible
	 * 
	 * @step. I dont see CONNECT label
	 * @throws Exception
	 */

	@When("I dont see CONNECT label")
	public void IDontSeePeopleYouMayKnowLabel() throws Exception {
		Assert.assertFalse("CONNECT label is visible", getPeoplePickerPage()
				.isPeopleYouMayKnowLabelVisible());
	}

	/**
	 * Click maybe later to dismiss contacts import
	 * 
	 * @throws Exception
	 */
	@When("^I press maybe later button$")
	public void IPressMaybeLater() throws Exception {
		getPeoplePickerPage().clickMaybeLaterButton();
	}

	/**
	 * Hides the keyboard on main people picker page
	 * 
	 * @step. I hide peoplepicker keyboard
	 * @throws Exception
	 */

	@When("I hide peoplepicker keyboard")
	public void HidePeoplePickerKeyboard() throws Exception {
		getPeoplePickerPage().hidePeoplePickerKeyboard();
	}

	/**
	 * Swipes a suggested contact half-way to reveal HIDE button
	 * 
	 * @step. I swipe to reveal hide button for suggested contact (.*)
	 * @param contact
	 *            name of suggested contact to swipe
	 * @throws Exception
	 */

	@When("I swipe to reveal hide button for suggested contact (.*)")
	public void SwipeToRevealHideButton(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			log.debug("No user found. " + e);
		}
		getPeoplePickerPage().swipeToRevealHideSuggestedContact(contact);
	}

	/**
	 * Swipes a suggested contact away completely to dismiss
	 * 
	 * @step. I swipe completely to dismiss suggested contact (.*)
	 * @param contact
	 *            name of suggested contact to swipe
	 * @throws Exception
	 */

	@When("I swipe completely to dismiss suggested contact (.*)")
	public void SwipeCompletelyToDismiss(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getPeoplePickerPage().swipeCompletelyToDismissSuggestedContact(contact);
	}

	/**
	 * Taps the hide button for a suggested contact
	 * 
	 * @step. I tap hide for suggested contact
	 * @throws Exception
	 */

	@When("I tap hide for suggested contact (.*)")
	public void ITapHideSuggestedContact(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			log.debug("No user found. " + e);
		}
		getPeoplePickerPage().tapHideSuggestedContact(contact);
	}

	/**
	 * Verifies that a user's name is not present in suggested contacts
	 * 
	 * @step. I do not see suggested contact (.*)
	 * @param contact
	 *            name of suggested contact to check
	 * @throws Exception
	 */

	@Then("I do not see suggested contact (.*)")
	public void IDoNotSeeSuggestedContact(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse("Suggested contact is still visible",
				getPeoplePickerPage().isSuggestedContactVisible(contact));
	}

	/**
	 * A workaround for top people not loading immediately
	 * 
	 * @step. I re-enter the people picker if top people list is not there
	 * @throws Exception
	 */

	@When("I re-enter the people picker if top people list is not there")
	public void IRetryPeoplePickerIfNotLoaded() throws Exception {
		for (int i = 0; i < 3; i++) {
			if (!getPeoplePickerPage().isTopPeopleLabelVisible()) {
				IClickCloseButtonDismissPeopleView();
				Thread.sleep(5000);
				getСontactListPage().openSearch();
				getPeoplePickerPage().closeShareContactsIfVisible();
			} else {
				break;
			}
		}
	}

	/**
	 * A workaround for CONNECT label not loading immediately
	 * 
	 * @step. I re-enter the people picker if CONNECT label is not there
	 * @throws Exception
	 */

	@When("I re-enter the people picker if CONNECT label is not there")
	public void IRetryPeoplePickerIfNoConnectLabel() throws Exception {
		for (int i = 0; i < 3; i++) {
			if (!getPeoplePickerPage().isConnectLabelVisible()) {
				IClickCloseButtonDismissPeopleView();
				Thread.sleep(5000);
				getСontactListPage().openSearch();
				getPeoplePickerPage().closeShareContactsIfVisible();
			} else {
				break;
			}
		}
	}

	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
		getPeoplePickerPage().tapOnPeoplePickerSearch();
	}

	@When("^I fill in Search field user name (.*)$")
	public void WhenIFillInSearchFieldUserName(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getPeoplePickerPage().inputTextInSearch(contact);
	}

	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getPeoplePickerPage().closeShareContactsIfVisible();
		getPeoplePickerPage().fillTextInPeoplePickerSearch(contact);
	}

	@When("^I input in People picker search field user email (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email)
			throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getPeoplePickerPage().closeShareContactsIfVisible();
		getPeoplePickerPage().fillTextInPeoplePickerSearch(email);
	}

	/**
	 * Input conversation name in Search input
	 * 
	 * @step. ^I input conversation name (.*) in Search input$
	 * 
	 * @param name
	 *            exact conversation name
	 * 
	 * @throws Exception
	 */
	@When("^I input conversation name (.*) in Search input$")
	public void IInputConversationNameInSearchInput(String name)
			throws Exception {
		WhenIFillInSearchFieldUserName(name);
	}

	@When("I tap go to enter conversation")
	public void IEnterConversation() throws Exception {
		getPeoplePickerPage().goIntoConversation();
	}

	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue("User: " + contact
				+ " is not presented on People picker page",
				getPeoplePickerPage().waitUserPickerFindUser(contact));
	}

	/**
	 * Verify user is not found on people picker
	 * 
	 * @step. ^I see that user (.*) is NOT found on People picker page$
	 * 
	 * @param contact
	 *            user name
	 * 
	 * @throws Exception
	 */
	@When("^I see that user (.*) is NOT found on People picker page$")
	public void WhenISeeUserNotFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse("User: " + contact
				+ " is presented on People picker page", getPeoplePickerPage()
				.waitUserPickerFindUser(contact));
	}

	/**
	 * Verify that conversation is not presented in search results
	 * 
	 * @step. ^I see conversation (.*) is NOT presented in Search results$
	 * 
	 * @param name
	 *            conversation name to search
	 * @throws Exception
	 */
	@When("^I see conversation (.*) is NOT presented in Search results$")
	public void ISeeConversationIsNotFoundInSearchResult(String name)
			throws Exception {
		Assert.assertFalse("Conversation: " + name
				+ " is presented in Search results", getPeoplePickerPage()
				.waitUserPickerFindUser(name));
	}

	@When("^I tap on NOT connected user name on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getPeoplePickerPage().clickOnNotConnectedUser(contact);
	}

	@When("^I tap on user on pending name on People picker page (.*)$")
	public void WhenITapOnUserOnPendingFoundOnPeoplePickerPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getPeoplePickerPage().clickOnUserOnPending(contact);
	}

	@When("^I search for user name (.*) and tap on it on People picker page$")
	public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(
			String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getPeoplePickerPage().pickUserAndTap(contact);
	}

	/**
	 * Verifies that a clock is present on a pending user's searched avatar
	 * 
	 * @step. ^I see the user avatar with a clock$
	 * @throws Exception
	 */

	@Then("^I see the user (.*) avatar with a clock$")
	public void ISeeUserWithAvatarClock(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		double score = getPeoplePickerPage().checkAvatarClockIcon(contact);
		Assert.assertTrue(
				"Avatar with clock icon is not correct - overlap score is only: "
						+ score, score > 0.49);
	}

	/**
	 * Verifies that pending clock is not visible on searched avatar
	 * 
	 * @step. ^I see the user (.*) avatar without the pending clock$
	 * @param name
	 *            of contact without pending clock
	 * @throws Throwable
	 */
	@Then("^I see the user (.*) avatar without the pending clock$")
	public void ISeeTheUserAvatarWithoutThePendingClock(String name)
			throws Throwable {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		double score = getPeoplePickerPage().checkAvatarClockIcon(name);
		Assert.assertFalse("Avatar icon still has a clock - overlap score is: "
				+ score, score < 0.50);
	}

	@When("^I search for ignored user name (.*) and tap on it$")
	public void WhenISearchForIgnoredUserNameAndTapOnItOnPeoplePickerPage(
			String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getPeoplePickerPage().pickIgnoredUserAndTap(contact);
	}

	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton() throws Exception {
		Assert.assertTrue("Add to conversation button is not visible",
				getPeoplePickerPage().isAddToConversationBtnVisible());
	}

	@When("^I don't see Add to conversation button$")
	public void WhenIDontSeeAddToConversationButton() throws Exception {
		Assert.assertTrue("Add to conversation button is visible",
				getPeoplePickerPage().addToConversationNotVisible());
	}

	@When("^I click Go button to create 1:1 conversation$")
	public void WhenIClickOnGoButtonForSingle() throws Exception {
		getPeoplePickerPage().clickOnGoButton(false);
	}

	@When("^I click on Go button$")
	public void WhenIClickOnGoButton() throws Exception {
		getPeoplePickerPage().clickOnGoButton(true);
	}

	@When("^I click clear button$")
	public void WhenIClickClearButton() throws Exception {
		getPeoplePickerPage().dismissPeoplePicker();
	}

	/**
	 * Select pointed amount of contacts from top people in a row starting from
	 * first
	 * 
	 * @param numberOfTopConnections
	 *            amount of contacts that should be selected
	 * @throws Exception
	 */
	@Then("I tap on first (.*) top connections")
	public void WhenITapOnTopConnections(int numberOfTopConnections)
			throws Exception {
		getPeoplePickerPage().tapNumberOfTopConnections(numberOfTopConnections);
	}

	/**
	 * Tap on pointed amount of users from top people skipping pointed contact
	 * 
	 * @param numberOfTopConnections
	 *            number of top contacts to tap
	 * @param contact
	 *            name of contact that shouldn't be tapped
	 * @throws Exception
	 */
	@Then("I tap on (.*) top connections but not (.*)")
	public void WhenITapOnTopConnectionsButNotUser(int numberOfTopConnections,
			String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getPeoplePickerPage().tapNumberOfTopConnectionsButNotUser(
				numberOfTopConnections, contact);
	}

	/**
	 * Tap on top connection contact avatar by pointed id order
	 * 
	 * @step. I tap on (\\d+)\\w+ top connection contact
	 * 
	 * @param i
	 *            contact order in top peoples
	 * 
	 * @throws Exception
	 */
	@When("^I tap on (\\d+)\\w+ top connection contact$")
	public void IClickOnTopConnectionByOrder(int i) throws Exception {
		getPeoplePickerPage().tapOnTopConnectionAvatarByOrder(i);
	}

	@When("I click on connected user (.*) avatar on People picker page")
	public void IClickOnUserIconToAddItToExistingGroupChat(String contact)
			throws Throwable {
		String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getPeoplePickerPage().clickConnectedUserAvatar(name);
	}

	@When("I see contact list on People picker page")
	public void ISeeContactListOnPeoplePickerPage() throws Exception {
		Assert.assertTrue("Contacts label is not shown", getPeoplePickerPage()
				.isContactsLabelVisible());
	}

	@When("I see top people list on People picker page")
	public void ISeeTopPeopleListOnPeoplePickerPage() throws Exception {
		Assert.assertTrue("Top People label is not shown",
				getPeoplePickerPage().isTopPeopleLabelVisible());
	}

	@When("I tap on connected user (.*) on People picker page")
	public void ISelectUserOnPeoplePickerPage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getPeoplePickerPage().selectUser(name);
	}

	@When("I see Create Conversation button on People picker page")
	public void ISeeCreateConversationButton() throws Exception {
		Assert.assertTrue("Create Conversation button is not visible.",
				getPeoplePickerPage().isCreateConversationButtonVisible());
	}

	/**
	 * Click on Open button from Search to start conversation with single user
	 * 
	 * @step. ^I click open conversation button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I click open conversation button on People picker page$")
	public void IClickOpenConversationButtonOnPeoplePickerPage()
			throws Exception {
		getPeoplePickerPage().clickOpenConversationButton();
	}

	@When("I click Create Conversation button on People picker page")
	public void IClickCreateConversationButton() throws Throwable {
		if (getPeoplePickerPage().isCreateConversationButtonVisible()) {
			getPeoplePickerPage().clickCreateConversationButton();
		} else {
			WhenIClickOnGoButton();
		}
	}

	@When("I see user (.*) on People picker page is selected")
	public void ISeeUserIsSelectedOnPeoplePickerPage(String name)
			throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(getPeoplePickerPage().isUserSelected(name));
	}

	@When("I see user (.*) on People picker page is NOT selected")
	public void ISeeUserIsNotSelectedOnPeoplePickerPage(String name)
			throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertFalse(getPeoplePickerPage().isUserSelected(name));
	}

	@When("I press backspace button")
	public void IPressBackspaceBtn() throws Exception {
		getPeoplePickerPage().hitDeleteButton();
	}

	@When("I swipe up on People picker page")
	public void ISwipeUpPeoplePickerPage() throws Throwable {
		getPeoplePickerPage().swipeUp(500);
	}

	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Exception {
		if (getPeoplePickerPage().isKeyboardVisible()) {
			getPeoplePickerPage().clickOnGoButton(true);
		} else {
			getPeoplePickerPage().clickAddToCoversationButton();
		}
	}

	@When("I click close button to dismiss people view")
	public void IClickCloseButtonDismissPeopleView() throws Exception {
		getPeoplePickerPage().tapOnPeoplePickerClearBtn();
	}

	/**
	 * Unblocks a blocked user by clicking the unblock button
	 * 
	 * @step. I unblock user
	 * @throws Exception
	 * 
	 */
	@When("^I unblock user$")
	public void IUnblockUser() throws Exception {
		getPeoplePickerPage().unblockUser();
	}

	/**
	 * Unblocks a blocked user by clicking the unblock button for iPad
	 * 
	 * @step. I unblock user on iPad
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I unblock user on iPad$")
	public void IUnblockUserOniPad() throws Exception {
		getPeoplePickerPage().unblockUserOniPad();
	}

	/**
	 * This step checks if the number of the selected contacts is correct.
	 * 
	 * @step. ^I see that (\\d+) contacts are selected$
	 * 
	 * @param number
	 *            expected number of contacts
	 * @throws Exception
	 * 
	 */
	@Then("^I see that (\\d+) contacts are selected$")
	public void ISeeThatContactsAreSelected(int number) throws Exception {
		int numberOfSelectedTopPeople = getPeoplePickerPage()
				.getNumberOfSelectedTopPeople();
		Assert.assertEquals(
				"Expected selected contacts: " + number
						+ " but actual selected contacts: "
						+ numberOfSelectedTopPeople, number,
				numberOfSelectedTopPeople);
	}

	/**
	 * Presses the Send An Invite button in the people picker. To invite people
	 * via mail.
	 * 
	 * @step. ^I press the send an invite button$
	 * @throws Exception
	 * 
	 */
	@When("^I press the send an invite button$")
	public void IPressTheSendAnInviteButton() throws Exception {
		getPeoplePickerPage().tapSendInviteButton();
	}

	/**
	 * Presses the Copy button on the Send Invitation pop up
	 * 
	 * @step. ^I press the copy button$
	 * @throws Exception
	 * @throws UnsupportedFlavorException
	 * 
	 */
	@When("^I press the copy button$")
	public void IPressTheCopyButton() throws UnsupportedFlavorException,
			Exception {
		getPeoplePickerPage().tapSendInviteCopyButton();
	}

	/**
	 * Presses the instant connect plus button
	 * 
	 * @step. ^I press the instant connect button$
	 * @throws Exception
	 */
	@When("^I press the instant connect button$")
	public void IPressTheInstantConnectButton() throws Exception {
		getPeoplePickerPage().pressInstantConnectButton();
	}

	/**
	 * Verify that Call action button is visible
	 * 
	 * @step. ^I see call action button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see call action button on People picker page$")
	public void ISeeCallActionButtonOnPeoplePickerPage() throws Exception {
		Assert.assertTrue("Call action button is not visible",
				getPeoplePickerPage().isCallButtonVisible());
	}

	/**
	 * Click on Call action button from Search to start call
	 * 
	 * @step. ^I click call action button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I click call action button on People picker page$")
	public void IClickCallActionButtonOnPeoplePickerPage() throws Exception {
		getPeoplePickerPage().clickCallButton();
	}

	/**
	 * Verify that Send image action button is visible
	 * 
	 * @step. ^I see Send image action button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see Send image action button on People picker page$")
	public void ISeeSendImageActionButtonOnPeoplePickerPage() throws Exception {
		Assert.assertTrue("Send image action button is not visible",
				getPeoplePickerPage().isSendImageButtonVisible());
	}

	/**
	 * Click on Send image action button from Search to start call
	 * 
	 * @step. ^I click Send image action button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I click Send image action button on People picker page$")
	public void IClickSendImageActionButtonOnPeoplePickerPage()
			throws Exception {
		getPeoplePickerPage().clickSendImageButton();
	}

	/**
	 * Verify if Open conversation button is visible
	 * 
	 * @step. ^I see open conversation action button on People picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see open conversation action button on People picker page$")
	public void ISeeOpenConversationActionButton() throws Exception {
		Assert.assertTrue("Open conversation button is not visible",
				getPeoplePickerPage().isOpenConversationButtonVisible());
	}

	/**
	 * Verify if Open, Call and Send image action buttons are visible
	 * 
	 * @step. ^I see action buttons appeared on People picker page
	 * 
	 * @throws Exception
	 */
	@When("^I see action buttons appeared on People picker page$")
	public void ISeeActionButttonsAppearedOnPeoplePickerPage() throws Exception {
		ISeeOpenConversationActionButton();
		ISeeCallActionButtonOnPeoplePickerPage();
		ISeeSendImageActionButtonOnPeoplePickerPage();
	}

	/**
	 * Verify that Open, Call and Send image action buttons are NOT visible
	 * 
	 * @step. ^I see action buttons disappeared on People picker page
	 * 
	 * @throws Exception
	 */
	@When("^I see action buttons disappeared on People picker page$")
	public void ISeeActionButttonsDisappearedOnPeoplePickerPage()
			throws Exception {
		Assert.assertFalse("Open conversation button is still visible",
				getPeoplePickerPage().isOpenConversationButtonVisible());
		Assert.assertFalse("Call action button is still visible",
				getPeoplePickerPage().isCallButtonVisible());
		Assert.assertFalse("Send image action button is still visible",
				getPeoplePickerPage().isSendImageButtonVisible());
	}

	/**
	 * Opens conversation from the action button in people picker
	 * 
	 * @step. ^I click open conversation action button on People picker page$
	 * @throws Throwable
	 */
	@When("^I click open conversation action button on People picker page$")
	public void IClickOpenConversationActionButtonOnPeoplePickerPage()
			throws Throwable {
		getPeoplePickerPage().clickOpenConversationButton();
	}
}
