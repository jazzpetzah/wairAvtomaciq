package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection
            .getInstance();

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollecton.getPage(ContactListPage.class);
    }

    private LoginPage getLoginPage() throws Exception {
        return pagesCollecton.getPage(LoginPage.class);
    }

    private PersonalInfoPage getPersonalInfoPage() throws Exception {
        return pagesCollecton.getPage(PersonalInfoPage.class);
    }

    @Given("^I see conversations list$")
    public void GivenISeeConversationsList() throws Exception {
        Assert.assertTrue("Conversations list is not visible after the timeout", getLoginPage().isLoginFinished());
    }

    /**
     * Verify label in Self button
     *
     * @param name username
     * @throws Exception
     * @step. ^I see my name (.*) first letter as label of Self Button$
     */
    @When("^I see my name (.*) first letter as label of Self Button$")
    public void ISeeFirstLetterAsLabelSelfButton(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue(getContactListPage()
                .isSelfButtonContainingFirstNameLetter(name));
    }

    /**
     * Click MAYBE LATER on settings warning screen
     *
     * @throws Exception
     * @step. ^I dismiss settings warning$
     */
    @When("^I dismiss settings warning$")
    public void IDismissSettingsWarning() throws Exception {
        getLoginPage().dismissSettingsWaring();
    }

    @When("^I tap on my name (.*)$")
    public void WhenITapOnMyName(String name) throws Exception {
        getContactListPage().tapOnMyName();
        getPersonalInfoPage().waitSelfProfileVisible();
    }

    @When("^I tap on contact name (.*)$")
    public void WhenITapOnContactName(String name) throws Exception {
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getContactListPage().tapOnName(name);
    }

    @When("^I tap on group chat with name (.*)$")
    public void WhenITapOnGroupChatName(String chatName) throws Exception {
        getContactListPage().tapOnGroupChat(chatName);
    }

    @When("^I swipe down contact list$")
    public void ISwipeDownContactList() throws Throwable {
        if (!CommonUtils.getIsSimulatorFromConfig(IOSPage.class)) {
            getContactListPage().swipeDown(500);
        } else {
            getContactListPage().swipeDownSimulator();
        }
    }

    /**
     * Open search by taping on search field
     *
     * @throws Exception
     * @step. ^I open search by taping on it$
     */
    @When("^I open search by taping on it$")
    public void IOpenSearchByTap() throws Exception {
        getContactListPage().openSearch();
    }

    @Then("^I see first item in contact list named (.*)$")
    public void ISeeUserNameFirstInContactList(String value) throws Throwable {
        try {
            value = usrMgr.findUserByNameOrNameAlias(value).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        Assert.assertEquals("User name doesn't appeared in contact list.",
                value, getContactListPage().getFirstConversationName());
    }

    /**
     * verifies the visibility of a specific user in the contact list
     *
     * @param value username value string
     * @throws AssertionError if the user does not exist
     * @step. ^I see user (.*) in contact list$
     */
    @Then("^I see user (.*) in contact list$")
    public void ISeeUserInContactList(String value) throws Throwable {
        value = usrMgr.replaceAliasesOccurences(value, FindBy.NAME_ALIAS);
        Assert.assertTrue(getContactListPage().isChatInContactList(value));
    }

    @When("^I create group chat with (.*) and (.*)$")
    public void ICreateGroupChat(String contact1, String contact2) throws Exception {
        WhenITapOnContactName(contact1);
        DialogPageSteps dialogSteps = new DialogPageSteps();
        dialogSteps.WhenISeeDialogPage();
        dialogSteps.IOpenConversationDetails();

        OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
        infoPageSteps.WhenISeeOtherUserProfilePage(contact1);
        infoPageSteps.WhenIPressAddButton();

        PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
        pickerSteps.WhenISeePeoplePickerPage();
        pickerSteps.WhenITapOnSearchInputOnPeoplePickerPage();
        pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
        pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
        pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
        pickerSteps.WhenIClickOnAddToConversationButton();

        GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
        groupChatSteps.ThenISeeGroupChatPage(String.format("%s%s%s",
                contact1, CommonSteps.ALIASES_SEPARATOR, contact2));
    }

    @When("^I swipe right on a (.*)$")
    public void ISwipeRightOnContact(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        getContactListPage().swipeRightConversationToRevealActionButtons(
                contact);
    }

    @Then("^I open archived conversations$")
    public void IOpenArchivedConversations() throws Exception {
        getContactListPage().swipeUp(1000);
    }

    @When("I see play/pause button next to username (.*) in contact list")
    public void ISeePlayPauseButtonNextToUserName(String contact)
            throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("Play pause button is not shown",
                getContactListPage().isPlayPauseButtonVisible(name));
    }

    @When("I tap on play/pause button in contact list")
    public void ITapOnPlayPauseButtonInContactList() throws Exception {
        getContactListPage().tapPlayPauseButton();
    }

    @When("I tap play/pause button in contact list next to username (.*)")
    public void ITapPlayPauseButtonInContactListNextTo(String contact)
            throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getContactListPage().tapPlayPauseButtonNextTo(name);
    }

    /**
     * Verify pause media button in contact list
     *
     * @throws Exception
     * @step. I see pause media button in contact list
     */
    @When("I see pause media button in contact list")
    public void ISeePauseMediaButtonContactList() throws Exception {
        Assert.assertTrue("Pause media button is not shown",
                getContactListPage().isPauseButtonVisible());
    }

    /**
     * Verify play media button in contact list
     *
     * @throws Exception
     * @step. I see play media button in contact list
     */
    @When("I see play media button in contact list")
    public void ISeePlayMediaButtonContactList() throws Exception {
        Assert.assertTrue("Play media button is not shown",
                getContactListPage().isPlayButtonVisible());
    }

    @When("I see in contact list group chat named (.*)")
    public void ISeeInContactListGroupChatWithName(String name)
            throws Exception {
        Assert.assertTrue(getContactListPage().isChatInContactList(name));
    }

    @When("I click on Pending request link in contact list")
    public void ICcickPendingRequestLinkContactList() throws Throwable {
        getContactListPage().clickPendingRequest();
    }

    @When("I see Pending request link in contact list")
    public void ISeePendingRequestLinkInContacts() throws Exception {
        Assert.assertTrue("Pending request link is not in Contact list",
                getContactListPage().isPendingRequestInContactList());
    }

    @When("I dont see Pending request link in contact list")
    public void IDontSeePendingRequestLinkInContacts() throws Exception {
        Assert.assertTrue("Pending request link is shown in contact list",
                getContactListPage().pendingRequestInContactListIsNotShown());
    }

    @When("I see conversation with not connected user (.*)")
    public void ISeeConversationWithUser(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertTrue(getContactListPage().isDisplayedInContactList(name));
    }

    @When("I don't see conversation with not connected user (.*)")
    public void IDontSeeConversationWithUser(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        Assert.assertFalse(getContactListPage().isDisplayedInContactList(name));
    }

    @When("I see in contact list group chat with (.*) (.*) (.*)")
    public void ISeeInContactsGroupChatWith(String name1, String name2,
                                            String name3) throws Exception {
        name1 = usrMgr.findUserByNameOrNameAlias(name1).getName();
        name2 = usrMgr.findUserByNameOrNameAlias(name2).getName();
        name3 = usrMgr.findUserByNameOrNameAlias(name3).getName();
        boolean chatExists = getContactListPage()
                .conversationWithUsersPresented(name1, name2, name3);
        Assert.assertTrue("Convesation with : " + name1 + ", " + name2 + ", "
                + name3 + ", " + " is not in chat list", chatExists);
    }

    @When("I don't see in contact list group chat with (.*) (.*) (.*)")
    public void IDontSeeInContactsGroupChatWith(String name1, String name2,
                                                String name3) throws Exception {
        name1 = usrMgr.findUserByNameOrNameAlias(name1).getName();
        name2 = usrMgr.findUserByNameOrNameAlias(name2).getName();
        name3 = usrMgr.findUserByNameOrNameAlias(name3).getName();
        boolean chatExists = getContactListPage()
                .conversationWithUsersPresented(name1, name2, name3);
        Assert.assertFalse("Convesation with : " + name1 + ", " + name2 + ", "
                + name3 + ", " + " is in chat list", chatExists);
    }

    /**
     * Verify that conversation with pointed name is not displayed in contact
     * list
     *
     * @param name conversation name to verify
     * @throws Exception
     * @step. I dont see conversation (.*) in contact list
     */
    @When("I dont see conversation (.*) in contact list")
    public void IDoNotSeeConversationInContactList(String name)
            throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertTrue("Conversation is displayed", getContactListPage()
                .contactIsNotDisplayed(name));
    }

    /**
     * Verifies, that the conversation is really silenced
     *
     * @param conversation conversation name to silence
     * @throws Exception
     * @step. ^I see conversation (.*) is silenced$
     */
    @Then("^I see conversation (.*) is silenced$")
    public void ISeeConversationIsSilenced(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        boolean isSilenced = getContactListPage().isConversationSilenced(
                conversation, true);
        Assert.assertTrue("Conversation is not silenced", isSilenced);
    }

    /**
     * Verifies, that the conversation got silenced before from backend
     *
     * @param conversation conversation name to silence
     * @throws Exception
     * @step. ^I see conversation (.*) got silenced before$
     */
    @Then("^I see conversation (.*) got silenced before$")
    public void ISeeConversationGotSilencedBefore(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        boolean isSilenced = getContactListPage().isConversationSilencedBefore(
                conversation);
        Assert.assertTrue("Conversation is not silenced", isSilenced);
    }

    /**
     * Verifies, that the conversation is unsilenced
     *
     * @param conversation conversation name to unsilence
     * @throws Exception
     * @step. ^I see conversation (.*) is unsilenced$
     */
    @Then("^I see conversation (.*) is unsilenced$")
    public void ISeeConversationIsUnSilenced(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        boolean isSilenced = getContactListPage().isConversationSilenced(
                conversation, false);
        Assert.assertTrue("Conversation is unsilenced", isSilenced);

    }

    /**
     * Click on archive button for a conversation
     *
     * @param conversation conversation name to archive
     * @throws Exception if conversation is not found
     * @step. ^I click archive button for conversation (.*)$
     */
    @When("^I click archive button for conversation (.*)$")
    public void IClickArchiveConversationButton(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        // TODO: select button by conversation name
        getContactListPage().clickArchiveConversationButton();
    }

    private BufferedImage referenceImage = null;
    private static final double MAX_OVERLAP_SCORE = 0.70;

    /**
     * Takes screenshot of conversation cell of first contact
     *
     * @throws Exception
     * @step. ^I remember the state of the first conversation cell$
     */
    @When("^I remember the state of the first conversation cell$")
    public void IRememberConversationState() throws Exception {
        referenceImage = getContactListPage().getScreenshotFirstContact();
    }

    /**
     * Verifies that the change of state of first conversation cell is visible
     *
     * @throws Exception
     * @step. ^I see change of state for first conversation cell$
     */
    @Then("^I see change of state for first conversation cell$")
    public void ISeeFirstConvCellChange() throws Exception {
        if (referenceImage == null) {
            throw new IllegalStateException(
                    "This step requires you to remember the initial state of the conversation cell");
        }
        double score = -1;
        final BufferedImage convCellState = getContactListPage()
                .getScreenshotFirstContact();
        score = ImageUtil.getOverlapScore(convCellState, referenceImage,
                ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
        Assert.assertTrue("Ping symbol not visible. Score is : " + score,
                score <= MAX_OVERLAP_SCORE);

    }

    /**
     * Verify that missed call indicator is seen in conversation list
     *
     * @param contact the missed call is from
     * @throws Exception
     * @step. ^I see missed call indicator in list for contact (.*)$
     */
    @Then("^I see missed call indicator in list for contact (.*)$")
    public void ISeeMissedCallIndicatorInListForContact(String contact)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        boolean missedCallSeen = getContactListPage()
                .missedCallIndicatorIsVisible(contact);
        Assert.assertTrue("No missed call indicator visible.", missedCallSeen);
    }

    /**
     * Verify that missed call indicator got moved down and is still seen in
     * conversation list
     *
     * @param contact the missed call is from
     * @throws Exception
     * @step. ^I see missed call indicator got moved down in list for contact
     * (.*)$
     */
    @Then("^I see missed call indicator got moved down in list for contact (.*)$")
    public void ISeeMissedCallIndicatorGotMovedDownInListForContact(
            String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        boolean missedCallSeen = getContactListPage()
                .missedCallIndicatorIsVisible(contact);
        Assert.assertTrue("No missed call indicator visible.", missedCallSeen);

    }

    /**
     * Verify that relevant unread message indicator is seen in conversation
     * list
     *
     * @param contact the missed call is from
     * @throws Exception
     * @step. ^I see (.*) unread message indicator in list for contact (.*)$
     */
    @Then("^I see (.*) unread message indicator in list for contact (.*)$")
    public void ISeeUnreadMessageIndicatorInListForContact(int numOfMessage,
                                                           String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        boolean unreadIconVisibility = getContactListPage()
                .unreadMessageIndicatorIsVisible(numOfMessage, contact);
        Assert.assertTrue("No unread message indicator is visible.",
                unreadIconVisibility);
    }

    /**
     * Verify that unread icon is not shown next to contact
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I dont see unread message indicator in list for contact (.*)$
     */
    @Then("^I dont see unread message indicator in list for contact (.*)$")
    public void IDontSeeUnreadIndicator(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        boolean unreadIconVisibility = getContactListPage()
                .unreadMessageIndicatorIsVisible(0, contact);
        Assert.assertTrue("Unread message indicator is visible.",
                unreadIconVisibility);
    }

    /**
     * Verify that play icon is not shown next to contact
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I see Play media button next to user (.*)$
     */
    @Then("^I see Play media button next to user (.*)$")
    public void ISeeMediaButtonChangedToPlay(String contact)
            throws IllegalStateException, Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("No play button next to user: " + contact,
                getContactListPage().isPlayButtonVisible());

    }

    /**
     * Verify that pause icon is not shown next to contact
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I see Pause media button next to user (.*)$
     */
    @Then("^I see Pause media button next to user (.*)$")
    public void ISeeMediaButtonChangedToPause(String contact)
            throws IllegalStateException, Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("No pause button next to user: " + contact,
                getContactListPage().isPauseButtonVisible());

    }

    /**
     * Verify that mute call button is shown in conversation list
     *
     * @throws Exception
     * @step. ^I see mute call button in conversation list$
     */
    @Then("^I see mute call button in conversation list$")
    public void ISeeMuteCallButtonInConversationList() throws Exception {
        Assert.assertTrue("Mute call button is not shown in conversation list",
                getContactListPage().isMuteCallButtonVisible());
    }

    /**
     * Click on mute call button in conversation list
     *
     * @throws Exception
     * @step. ^I click mute call button in conversation list
     */
    @Then("^I click mute call button in conversation list$")
    public void IClickMuteCallButtonInConversationList() throws Exception {
        getContactListPage().clickMuteCallButton();
    }

    /**
     * Checks that conversation name appears in displayed action menu
     *
     * @param conversation conversation name
     * @throws Exception
     * @step. ^I see conversation (.*) name in action menu in [Cc]ontact
     * [Ll]ist$
     */
    @And("^I see conversation (.*) name in action menu in [Cc]ontact [Ll]ist$")
    public void ISeeConversationNameInActionMenu(String conversation)
            throws Exception {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        Assert.assertTrue("There is no conversation name " + conversation
                + " in opened action menu.", getContactListPage()
                .isActionMenuVisibleForConversation(conversation));
    }

    /**
     * Checks that specified button is exist in displayed action menu
     *
     * @param buttonTitle Silence | Delete | Leave | Archive | Block | Cancel
     * @throws Exception
     * @step. ^I see (Silence|Delete|Leave|Archive|Block|Cancel) button in
     * action menu in [Cc]ontact [Ll]ist$
     */
    @And("^I see (Silence|Delete|Leave|Archive|Block|Cancel) button in action menu in [Cc]ontact [Ll]ist$")
    public void ISeeXButtonInActionMenu(String buttonTitle) throws Exception {
        Assert.assertTrue("There is no button " + buttonTitle.toUpperCase()
                + " in opened action menu.", getContactListPage()
                .isButtonVisibleInActionMenu(buttonTitle));
    }

    /**
     * Clicks the Archive button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Archive button in action menu in Contact List$
     */
    @When("^I press Archive button in action menu in Contact List$")
    public void IPressArchiveButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickArchiveButtonInActionMenu();
    }

    /**
     * Clicks the Leave button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Leave button in action menu in Contact List$
     */
    @When("^I press Leave button in action menu in Contact List$")
    public void IPressLeaveButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickLeaveButtonInActionMenu();
    }

    /**
     * Clicks the Cancel button in action menu of contact list
     *
     * @throws Throwable
     * @step. ^I press Cancel button in action menu in Contact list$
     */
    @Then("^I press Cancel button in action menu in Contact List$")
    public void IPressCancelButtonInActionMenuInContactList() throws Throwable {
        getContactListPage().clickCancelButtonInActionMenu();
    }

    /**
     * Verifies that next conversation is selected in list
     *
     * @param conversation that is selected now
     * @throws Throwable
     * @step. ^I see conversation (.*) is selected in list$
     */
    @Then("^I see conversation (.*) is selected in list$")
    public void ISeeConversationIsSelectedInList(String conversation)
            throws Throwable {
        conversation = usrMgr.replaceAliasesOccurences(conversation,
                FindBy.NAME_ALIAS);
        Assert.assertEquals("Converstaion is not selected", "1",
                getContactListPage().getSelectedConversationCellValue(conversation).
                        orElseThrow(() -> new IllegalStateException("No conversations are selected in the list")));
    }

    /**
     * Verify if Invite more people button is shown in contact list
     *
     * @throws Exception
     * @step. ^I see Invite more people button$
     */
    @When("^I see Invite more people button$")
    public void ISeeInviteMorePeopleButton() throws Exception {
        Assert.assertTrue("Invite more people button is not shown",
                getContactListPage().isInviteMorePeopleButtonVisible());
    }

    /**
     * Verify if Invite more people button is NOT shown in contact list
     *
     * @throws Exception
     * @step. ^I DONT see Invite more people button$
     */
    @When("^I DONT see Invite more people button$")
    public void IDontSeeInviteMorePeopleButton() throws Exception {
        Assert.assertTrue("Invite more people button is shown",
                getContactListPage().isInviteMorePeopleButtonNotVisible());
    }

}
