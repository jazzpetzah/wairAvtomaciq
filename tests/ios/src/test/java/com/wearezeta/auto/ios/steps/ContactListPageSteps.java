package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private LoginPage getLoginPage() throws Exception {
        return pagesCollection.getPage(LoginPage.class);
    }

    @Given("^I see conversations list$")
    public void GivenISeeConversationsList() throws Exception {
        Assert.assertTrue("Conversations list is not visible after the timeout", getLoginPage().isLoginFinished());
    }

    private Map<String, BufferedImage> savedConvoItemScreenshots = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param nameAlias conversation name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) conversation item$
     */
    @When("^I remember the state of (.*) conversation item$")
    public void IRememberConvoItemState(String nameAlias) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        this.savedConvoItemScreenshots.put(name, getContactListPage().getConversationEntryScreenshot(name));
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param nameAlias          conversation name/alias
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of (.*) conversation item is (not )?changed$"
     */
    @Then("^I see the state of (.*) conversation item is (not )?changed$")
    public void IVerifyConvoState(String nameAlias, String shouldNotBeChanged) throws Exception {
        final String name = usrMgr.replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        if (!this.savedConvoItemScreenshots.containsKey(name)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of '%s' conversation entry first", name));
        }
        final BufferedImage actualConvoItemScreenshot = getContactListPage().getConversationEntryScreenshot(name);
        final double score = ImageUtil.getOverlapScore(this.savedConvoItemScreenshots.get(name),
                actualConvoItemScreenshot, ImageUtil.RESIZE_NORESIZE);
        final double minScore = 0.999;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be the same (%.3f >= %.3f)",
                            name, score, minScore), score < minScore);
        } else {
            Assert.assertTrue(
                    String.format("The state of '%s' conversation item seems to be changed (%.3f < %.3f)",
                            name, score, minScore), score >= minScore);
        }
    }

    private Map<Integer, BufferedImage> savedConvoItemScreenshotsByIdx = new HashMap<>();

    /**
     * Store the screenshot of a particular conversation list entry
     *
     * @param convoIdx conversation index, starts from 1
     * @throws Exception
     * @step. ^I remember the state of conversation item number (\\d+)$
     */
    @When("^I remember the state of conversation item number (\\d+)$")
    public void IRememberConvoItemStateByIdx(int convoIdx) throws Exception {
        this.savedConvoItemScreenshotsByIdx.put(convoIdx, getContactListPage().getConversationEntryScreenshot(convoIdx));
    }

    /**
     * Verify whether the previous conversation state is the same or different to the current state
     *
     * @param convoIdx           conversation index, starts from 1
     * @param shouldNotBeChanged equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of conversation item number (\\d+) is (not )?changed$"
     */
    @Then("^I see the state of conversation item number (\\d+) is (not )?changed$")
    public void IVerifyConvoStateByIdx(int convoIdx, String shouldNotBeChanged) throws Exception {
        if (!this.savedConvoItemScreenshotsByIdx.containsKey(convoIdx)) {
            throw new IllegalStateException(String.format(
                    "Please take a screenshot of conversation entry number %s first", convoIdx));
        }
        final BufferedImage actualConvoItemScreenshot = getContactListPage().getConversationEntryScreenshot(convoIdx);
        final double score = ImageUtil.getOverlapScore(this.savedConvoItemScreenshotsByIdx.get(convoIdx),
                actualConvoItemScreenshot, ImageUtil.RESIZE_NORESIZE);
        final double minScore = 0.97;
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(
                    String.format("The state of conversation item number %s seems to be the same (%.2f >= %.2f)",
                            convoIdx, score, minScore), score < minScore);
        } else {
            Assert.assertTrue(
                    String.format("The state of conversation item number %s seems to be changed (%.2f < %.2f)",
                            convoIdx, score, minScore), score >= minScore);
        }
    }

    /**
     * Click MAYBE LATER on settings warning screen
     *
     * @throws Exception
     * @step. ^I dismiss settings warning$
     */
    @When("^I dismiss settings warning$")
    public void IDismissSettingsWarning() throws Exception {
        getLoginPage().dismissSettingsWarning();
    }

    @When("^I tap my avatar$")
    public void WhenITapOnMyName() throws Exception {
        getContactListPage().tapMyAvatar();
    }

    @When("^I tap on contact name (.*)")
    public void WhenITapOnContactName(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getContactListPage().tapOnName(name);
    }

    /**
     * Tap conversation list item by its index
     *
     * @param idx convo index in list, starts with 1
     * @throws Exception
     * @step. ^I tap on conversation item number (\d+)$
     */
    @When("^I tap on conversation item number (\\d+)$")
    public void WhenITapOnConvoByIdx(int idx) throws Exception {
        getContactListPage().tapConvoItemByIdx(idx);
    }

    @When("^I tap on group chat with name (.*)")
    public void WhenITapOnGroupChatName(String chatName) throws Exception {
        WhenITapOnContactName(chatName);
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

    private final static long CONVO_LIST_UPDATE_TIMEOUT = 10000; // milliseconds

    /**
     * Verify whether the first items in conversations list is the given item
     *
     * @step. ^I see first item in contact list named (.*)
     *
     * @param convoName conversation name
     * @throws Exception
     */
    @Then("^I see first item in contact list named (.*)")
    public void ISeeUserNameFirstInContactList(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            Thread.sleep(500);
            if (getContactListPage().isFirstConversationName(convoName)) {
                return;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= CONVO_LIST_UPDATE_TIMEOUT);
        throw new AssertionError(
                String.format("The conversation '%s' is not the first conversation in the list after " +
                        "%s seconds timeout", convoName, CONVO_LIST_UPDATE_TIMEOUT / 1000));

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
        dialogSteps.IOpenConversationDetails();

        OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
        infoPageSteps.WhenIPressAddButton();

        PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
        pickerSteps.WhenITapOnSearchInputOnPeoplePickerPage();
        pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
        pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
        pickerSteps.ITapOnConversationFromSearch(contact2);
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
        // This is to make sure that we are not in some transition state from the previous step
        Thread.sleep(3000);
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.swipe(0.2, 0.7, 0.2, 0.1);
        } else {
            getContactListPage().swipeUp(1000);
        }
    }

    @When("I see play/pause button next to username (.*) in contact list")
    public void ISeePlayPauseButtonNextToUserName(String contact) throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue("Play pause button is not shown",
                getContactListPage().isPlayPauseButtonVisible(name));
    }

    @When("I tap on play/pause button in contact list")
    public void ITapOnPlayPauseButtonInContactList() throws Exception {
        getContactListPage().tapPlayPauseButton();
    }

    @When("I tap play/pause button in contact list next to username (.*)")
    public void ITapPlayPauseButtonInContactListNextTo(String contact) throws Exception {
        String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getContactListPage().tapPlayPauseButtonNextTo(name);
    }

    @When("I see in contact list group chat named (.*)")
    public void ISeeInContactListGroupChatWithName(String name) throws Exception {
        Assert.assertTrue(getContactListPage().isChatInContactList(name));
    }

    @When("I click on Pending request link in contact list")
    public void ICcickPendingRequestLinkContactList() throws Exception {
        getContactListPage().clickPendingRequest();
    }

    @When("I (dont )?see Pending request link in contact list")
    public void ISeePendingRequestLinkInContacts(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Pending request link is not in Contact list",
                    getContactListPage().isPendingRequestInContactList());
        } else {
            Assert.assertTrue("Pending request link is shown in contact list",
                    getContactListPage().pendingRequestInContactListIsNotShown());
        }
    }

    @When("I (don't )?see in contact list group chat with (.*)")
    public void ISeeInContactsGroupChatWith(String shouldNotSee, String participantNameAliases) throws Exception {
        participantNameAliases = usrMgr.replaceAliasesOccurences(participantNameAliases,
                ClientUsersManager.FindBy.NAME_ALIAS);
        final List<String> participantNames = CommonSteps.splitAliases(participantNameAliases);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("There is no conversation with '%s' in the list", participantNames),
                    getContactListPage().isConversationWithUsersExist(participantNames, 5));
        } else {
            Assert.assertFalse(String.format("There is conversation with '%s' in the list, which should be hidden",
                    participantNames), getContactListPage().isConversationWithUsersExist(participantNames, 2));
        }
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
        Assert.assertTrue(String.format("Conversation '%s' is still displayed", name),
                getContactListPage().contactIsNotDisplayed(name));
    }

    /**
     * Click on archive button for a conversation
     *
     * @throws Exception if conversation is not found
     * @step. ^I click archive button for conversation$
     */
    @When("^I click archive button for conversation$")
    public void IClickArchiveConversationButton() throws Exception {
        getContactListPage().clickArchiveConversationButton();
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
        Assert.assertEquals("Conversation is not selected", "1",
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

    private BufferedImage previousSelfAvatarState = null;

    /**
     * Remember the current state of self avatar
     *
     * @throws Exception
     * @step. ^I remember the state of my self avatar$
     */
    @When("^I remember the state of my avatar$")
    public void IRememberAvatarState() throws Exception {
        previousSelfAvatarState = getContactListPage().getAvatarStateScreenshot();
    }

    private static final long AVATAR_CHANGE_TIMEOUT_MILLISECONDS = 10000;

    /**
     * Verify whether avatar state is changed within the timeout
     *
     * @param shouldNotChange equals to null if the state should be changed
     * @throws Exception
     * @step. ^I wait until my avatar is (not )?changed$
     */
    @Then("^I wait until my avatar is (not )?changed$")
    public void IWaitUntilAvatarIsChanged(String shouldNotChange) throws Exception {
        if (previousSelfAvatarState == null) {
            throw new IllegalStateException("Please take the initial screenshot of the avatar first");
        }
        double score;
        final double minScore = 0.97;
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            final BufferedImage currentAvatarState = getContactListPage().getAvatarStateScreenshot();
            score = ImageUtil.getOverlapScore(currentAvatarState,
                    previousSelfAvatarState, ImageUtil.RESIZE_NORESIZE);
            if (shouldNotChange == null) {
                if (score < minScore) {
                    return;
                }
            } else {
                if (score >= minScore) {
                    return;
                }
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= AVATAR_CHANGE_TIMEOUT_MILLISECONDS);
        if (shouldNotChange == null) {
            throw new AssertionError(String.format("The previous and the current state of self avatar " +
                            "icon seems to be equal after %s seconds (%.2f >= %.2f)",
                    AVATAR_CHANGE_TIMEOUT_MILLISECONDS / 1000, score, minScore));
        } else {
            throw new AssertionError(String.format("The previous and the current state of self avatar " +
                            "icon seems to be different after %s seconds (%.2f < %.2f)",
                    AVATAR_CHANGE_TIMEOUT_MILLISECONDS / 1000, score, minScore));
        }
    }

    /**
     * Taps on the name you are in a call with in conversation list
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I tap on chat I am in a call with name (.*)$
     */
    @When("^I tap on chat I am in a call with name (.*)$")
    public void ITapOnChatIAmInACallWithName(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        getContactListPage().tapOnNameYourInCallWith(name);
    }

}
