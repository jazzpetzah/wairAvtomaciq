package com.wearezeta.auto.ios.steps;

import java.awt.datatransfer.UnsupportedFlavorException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.IOSPage;
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
		while (!getPeoplePickerPage().isConnectLabelVisible()) {
			IClickCloseButtonDismissPeopleView();
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				getСontactListPage().swipeDown(1000);
			} else {
				getСontactListPage().swipeDownSimulator();
			}
		}
	}

	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
		getPeoplePickerPage().tapOnPeoplePickerSearch();
	}

	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
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
		getPeoplePickerPage().fillTextInPeoplePickerSearch(email);
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
		Assert.assertTrue("User :" + contact
				+ " is not presented on Pepople picker page",
				getPeoplePickerPage().waitUserPickerFindUser(contact));
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
		Assert.assertTrue("Avatar does not have a clock icon",
				getPeoplePickerPage().checkAvatarClockIcon(contact) > 0.50);
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
	 * 
	 */
	@When("^I unblock user$")
	public void IUnblockUser() throws Exception {
		getPeoplePickerPage().unblockUser();
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
		Assert.assertEquals(number, numberOfSelectedTopPeople);
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

}
