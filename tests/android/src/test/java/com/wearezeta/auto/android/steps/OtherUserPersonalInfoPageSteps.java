package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.android.pages.UnknownUserDetailsPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

import static org.hamcrest.Matchers.is;

import org.junit.Assert;

public class OtherUserPersonalInfoPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage()
            throws Exception {
        return pagesCollection.getPage(OtherUserPersonalInfoPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Checks to see that the profile page of another user is visible after
     * swiping up from dialog
     *
     * @param name
     * @throws Exception
     * @step. ^I see (.*) user profile page$
     */
    @When("^I see (.*) user profile page$")
    public void WhenISeeOherUserProfilePage(String name) throws Exception {
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getOtherUserPersonalInfoPage().isOtherUserNameVisible(name);
    }

    /**
     * Removes a contact from a group conversation
     *
     * @throws Exception
     * @step. ^I click Remove$
     */
    @When("^I click Remove$")
    public void WhenIClickRemove() throws Exception {
        getOtherUserPersonalInfoPage().tapRightActionButton();
    }

    /**
     * Checks to see that the remove user warning message appears -unclear
     *
     * @throws Exception
     * @step. ^I see warning message$
     */
    @When("^I see warning message$")
    public void WhenISeeWarningMessage() throws Exception {
        Assert.assertTrue("Warning message is not shown", getOtherUserPersonalInfoPage().isConversationAlertVisible());
    }

    /**
     * Confirms the remove button after seeing the warning message
     *
     * @throws Exception
     * @step. ^I confirm remove$
     */
    @When("^I confirm remove$")
    public void WhenIConfirmRemove() throws Exception {
        getOtherUserPersonalInfoPage().tapConfirmRemoveButton();
    }

    /**
     * Confirms the block of another user when they send a connection request
     *
     * @throws Exception
     * @step. ^I confirm block$
     */
    @When("^I confirm block$")
    public void WhenIConfirmBlock() throws Exception {
        getOtherUserPersonalInfoPage().pressConfirmBtn();
    }

    /**
     * Presses the "add people to conversation" button from another user's
     * profile
     *
     * @throws Exception
     * @step. ^I press add contact button$
     */
    @When("^I press add contact button$")
    public void WhenIPressAddContactButton() throws Exception {
        getOtherUserPersonalInfoPage().tapLeftActionBtn();
    }

    /**
     * Checks to see that we can see a given user's profile -duplicate of
     * WhenISeeOherUserProfilePage(String)
     *
     * @param contact
     * @throws Exception
     * @step. ^I see (.*) user name and email$
     */
    @Then("^I see (.*) user name and email$")
    public void ISeeUserNameAndEmail(String contact) throws Exception {
        ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
        contact = dstUser.getName();
        String email = dstUser.getEmail();
        Assert.assertTrue("User name is not visible",
                getOtherUserPersonalInfoPage().isOtherUserNameVisible(contact));
        Assert.assertTrue("User email is not visible",
                getOtherUserPersonalInfoPage().isOtherUserMailVisible(email));
    }

    /**
     * -- returns fake false result
     * <p/>
     * Checks to see that a user has been blocked by looking at the "is blocked"
     * button on their profile page
     *
     * @throws Exception
     * @step. ^User info should be shown with Unblock button$
     */
    @Then("^User info should be shown with Unblock button$")
    public void UserShouldBeShownWithUnBlockButton() throws Exception {
        Assert.assertTrue("Unblock button is not visible",
                getOtherUserPersonalInfoPage().isUnblockBtnVisible());
    }

    /**
     * Selects a tab in the single participant view
     *
     * @param tabName Name of the tab to select
     * @throws Exception
     * @step. ^I select single participant tab (.*)$
     */
    @When("^I select single participant tab \"(.*)\"$")
    public void WhenISelectSingleParticipantTab(String tabName) throws Exception {
        getOtherUserPersonalInfoPage().selectSingleParticipantTab(tabName);
    }

    /**
     * Checks the number of devices in single participant devices tab
     *
     * @param expectedNumDevices Expected number of devices
     * @throws Exception
     * @step. ^(\\d+) devices? (?:are|is) shown in single participant devices
     * tab$
     */
    @When("^I see (\\d+) devices? (?:are|is) shown in single participant devices tab$")
    public void ICheckNumberOfDevicesInSingleParticipantDevicesTab(int expectedNumDevices) throws Exception {
        int numDevices = getOtherUserPersonalInfoPage().getParticipantDevices().size();
        Assert.assertTrue("expected size", expectedNumDevices == numDevices);
    }

    /**
     * Checks the ids of all devices displayed in single participant devices tab
     *
     * @param user The user with devices to check for
     * @throws Exception
     * @step. ^I verify all device ids of user (.*) are shown in single
     * participant devices tab$
     */
    @When("^I verify all device ids of user (.*) are shown in single participant devices tab$")
    public void IVerifyAllDeviceIdsOfUserXAreShown(String user) throws Exception {
        List<String> expectedDeviceIds = SEBridge.getInstance().getDeviceIds(usrMgr.findUserByNameOrNameAlias(user));
        List<String> actualDeviceIds = getOtherUserPersonalInfoPage().getParticipantDevices();
        Assert.assertThat("List does not contain all device ids", actualDeviceIds, is(expectedDeviceIds));
    }


    // ------ Group
    // Separate steps file?

    /**
     * Taps on a contact from the group details page (seems out of place)
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on group chat contact (.*)$
     */
    @When("^I tap on group chat contact (.*)$")
    public void WhenITapOnGroupChatContact(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getOtherUserPersonalInfoPage().tapOnParticipant(contact);
    }

    /**
     * Presses the ellipses ("...") on the right hand side of the other user's
     * profile page -unclear
     *
     * @throws Exception
     * @step. ^I press Right conversation button$
     */
    @When("^I press options menu button$")
    public void WhenIPressOptionsMenuButton() throws Exception {
        getOtherUserPersonalInfoPage().pressOptionsMenuButton();
    }

    /**
     * Tap the corresponding item in conversation options menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I press (.*) conversation menu button$
     */
    @And("^I press (.*) conversation menu button$")
    public void IPressConvOptionsMenuItem(String itemName) throws Exception {
        getOtherUserPersonalInfoPage().selectConvoSettingsMenuItem(itemName);
    }


    /**
     * Verifys the user profile menu item is visible
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I see (.*) button in option menu$
     */
    @Then("^I see (.*) button in option menu$")
    public void ISeeButtonInUserProfileMenuAtPosition(String itemName) throws Exception {
        Assert.assertTrue("The user profile menu item is not visible",
                getOtherUserPersonalInfoPage().isUserProfileMenuItemVisible(itemName));
    }


    /**
     * Confirms the current user's decision to leave a conversation -outofplace
     *
     * @throws Exception
     * @step. ^I confirm leaving$
     */
    @When("^I confirm leaving$")
    public void WhenIConfirmLeaving() throws Exception {
        getOtherUserPersonalInfoPage().pressConfirmBtn();
    }

    /**
     * -duplicate of WhenITapOnGroupChatContact(String)
     *
     * @param name
     * @throws Exception
     * @step. ^I select contact (.*)$
     */
    @When("^I select contact (.*)$")
    public void WhenISelectContact(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getOtherUserPersonalInfoPage().tapOnParticipant(name);
    }

    /**
     * Checks to see that the conversation name is what is expected -outofplace
     *
     * @param name
     * @throws Exception
     * @step. ^I see that the conversation name is (.*)$
     */
    @Then("^I see that the conversation name is (.*)$")
    public void IVerifyCorrectConversationName(String name) throws Exception {
        Assert.assertEquals(getOtherUserPersonalInfoPage()
                .getConversationName(), name);
    }

    /**
     * Checks to see that correct avatars for given users appear
     *
     * @param contacts one or more contacs separated by comma
     * @throws Exception
     * @step. ^I see the correct participant avatars for (.*)
     */
    @Then("^I see the correct participant avatars for (.*)")
    public void ISeeCorrectParticipantAvatars(String contacts) throws Exception {
        for (String contactName : CommonSteps.splitAliases(contacts)) {
            contactName = usrMgr.findUserByNameOrNameAlias(contactName)
                    .getName();
            Assert.assertTrue(
                    String.format("The avatar for '%s' is not visible",
                            contactName),
                    getOtherUserPersonalInfoPage().isParticipantAvatarVisible(
                            contactName));
        }
    }

    /**
     * Checks to see that the correct number of users appears in the group name
     * title -outofplace
     *
     * @param realNumberOfParticipants
     * @throws Exception
     * @step. ^I see the correct number of participants in the title (.*)$
     */
    @Then("^I see the correct number of participants in the title (.*)$")
    public void IVerifyParticipantNumber(String realNumberOfParticipants)
            throws Exception {
        Assert.assertEquals(realNumberOfParticipants + " people",
                getOtherUserPersonalInfoPage().getSubHeader().toLowerCase());
    }

    /**
     * Checks to see a given user does not exist in the group chat info page
     * -outofplace
     *
     * @param contact
     * @throws Exception
     * @step. ^I do not see (.*) on group chat info page$
     */
    @Then("^I do not see (.*) on group chat info page$")
    public void ThenIDoNotSeeOnGroupChatInfoPage(String contact)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(
                String.format(
                        "Chat participant '%s' should not be visible in participants list, but it is",
                        contact), getOtherUserPersonalInfoPage()
                        .isParticipantNotVisible(contact));
    }

    /**
     * Renames a group conversation by first tapping on the participants header
     * and then sending the message to the text input field
     *
     * @param newConversationName the new conversation name
     * @throws Exception
     * @step. ^I rename group conversation to (.*)$
     */
    @Then("^I rename group conversation to (.*)$")
    public void ThenIRenameGroupConversationTo(String newConversationName)
            throws Exception {
        getOtherUserPersonalInfoPage().tapOnParticipantsHeader();
        getOtherUserPersonalInfoPage().renameGroupChat(newConversationName);
    }

    /**
     * Check that all required 1:1 option menu elements are showed correctly
     *
     * @throws Exception
     * @step. ^I see correct 1:1 options menu$
     */
    @Then("^I see correct 1:1 options menu$")
    public void ThenISeeOneToOneOptionsMenu() throws Exception {
        Assert.assertTrue(getOtherUserPersonalInfoPage()
                .areOneToOneMenuOptionsVisible());
    }

    /**
     * Check that any UI content of participant page is not showed
     *
     * @throws Exception
     * @step. ^I do not see participants? page$
     */
    @Then("^I do not see participants? page$")
    public void IDoNotSeeParticipantPage() throws Exception {
        Assert.assertTrue(
                "Contact profile page is visible, but expected not to be.",
                getOtherUserPersonalInfoPage()
                        .isParticipatPageUIContentNotVisible());
    }

    /**
     * Check that any UI content of 1:1 options menu is not showed
     *
     * @throws Exception
     * @step. ^I do not see 1:1 options menu$
     */
    @Then("^I do not see 1:1 options menu$")
    public void ThenIDoNotSeeOptionsMenu() throws Exception {
        Assert.assertTrue(
                "1on1 options menu is visible, but expected not to be.",
                getOtherUserPersonalInfoPage()
                        .areOneToOneMenuOptionsNotVisible());
    }

    /**
     * Check that any UI content of participant page is showed
     *
     * @throws Exception
     * @step. ^I see participants? page$
     */
    @Then("^I see participants? page$")
    public void ISeeCorrectParticipantPage() throws Exception {
        Assert.assertTrue(getOtherUserPersonalInfoPage()
                .isParticipatPageUIContentVisible());
    }

    /**
     * Perform small swipe down on the page
     *
     * @throws Exception
     * @step. ^I do small swipe down$
     */
    @When("^I do small swipe down")
    public void IDoSmallSwipeDown() throws Exception {
        getOtherUserPersonalInfoPage().swipeByCoordinates(2000, 50, 50, 50, 60);
    }

    private UnknownUserDetailsPage getUnknownUserDetailsPage() throws Exception {
        return (UnknownUserDetailsPage) pagesCollection
                .getPage(UnknownUserDetailsPage.class);
    }

    /**
     * Checks to see that you are on the page to connect with a user who you
     * have not yet connected with, but are in a group conversation with
     *
     * @param username the user who your are not connected to
     * @throws Exception
     * @step. ^I see user name (.*) on non connected user page$
     */
    @Then("^I see user name (.*) on non connected user page$")
    public void ISeeConnectToUnconnectedUserPageWithUser(String username)
            throws Exception {
        username = usrMgr.findUserByNameOrNameAlias(username).getName();
        Assert.assertTrue(String.format(
                "User name '%s' does not exist in non connected page header",
                username),
                getUnknownUserDetailsPage().isNameExistInHeader(username));
    }

    /**
     * Clicks on the connect button when viewing the user details of an
     * unconnected user from a group chat
     *
     * @throws Exception
     * @step. ^I click Connect button on non connected user page$
     */
    @Then("^I click Connect button on non connected user page$")
    public void IClickOnUnconnectedUserConnectButton() throws Exception {
        getUnknownUserDetailsPage().tapConnectButton();
    }

    /**
     * Clicks on the open conversation button when viewing the user details of
     * already connected user from a group chat
     *
     * @throws Exception
     * @step. ^I click Open Conversation button on connected user page$
     */
    @Then("^I click Open Conversation button on connected user page$")
    public void IClickOpenConversationButton() throws Exception {
        getOtherUserPersonalInfoPage().tapLeftActionBtn();
    }

    /**
     * Clicks on the pending button when viewing the user details of a pending
     * user from a group chat
     *
     * @throws Exception
     * @step. ^I click Pending button on pending user page$
     */
    @When("^I click Pending button on pending user page$")
    public void IClickPendingButton() throws Exception {
        getUnknownUserDetailsPage().tapPendingButton();
    }

    /**
     * Verify whether Pending button is visible on pending user page in a group
     * chat
     *
     * @throws Exception
     * @step. ^I see Pending button on pending user page$
     */
    @Then("^I see Pending button on pending user page$")
    public void ISeePendingButton() throws Exception {
        Assert.assertTrue("Pending button is not visible, but it should be",
                getUnknownUserDetailsPage().isPendingButtonVisible());
    }

    /**
     * Closes participants page by UI button
     *
     * @throws Exception
     * @step. ^I close participants? page by UI button$
     */
    @Then("^I close participants? page by UI button$")
    public void ThenICloseParticipantPageByUIButton() throws Exception {
        getOtherUserPersonalInfoPage().tapCloseButton();
    }

    /**
     * Closes single participant page by UI button
     *
     * @throws Exception
     * @step. ^I close single participant page by UI button$
     */
    @Then("^I close single participant page by UI button$")
    public void ThenICloseSingleParticipantPageByUIButton() throws Exception {
        getOtherUserPersonalInfoPage().tapSingleParticipantCloseButton();
    }

}