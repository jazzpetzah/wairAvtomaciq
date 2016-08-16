package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.InvitationMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;

public class PeoplePickerPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private static final Random random = new Random();

    private SearchListPage getSearchListPage() throws Exception {
        return pagesCollection.getPage(SearchListPage.class);
    }

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private ElementState avatarState = null;

    /**
     * Checks to see that the people picker page (search view) is visible
     *
     * @throws Exception
     * @step. ^I see People picker page$
     */
    @Then("^I see People picker page$")
    public void ISeePeoplePickerPage() throws Exception {
        Assert.assertTrue("People Picker is not visible", getSearchListPage().waitUntilPageVisible());
    }

    /**
     * Taps on the search bar in the people picker page
     *
     * @throws Exception
     * @step. ^I tap on Search input on People picker page$
     */
    @When("^I tap on Search input on People picker page$")
    public void ITapOnSearchInputOnPeoplePickerPage() throws Exception {
        getSearchListPage().tapPeopleSearch();
    }

    /**
     * Selects a contact from the top people section in the people picker page
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I tap on (.*) in Top People$
     */
    @When("^I tap on (.*) in Top People$")
    public void ITapInTopPeople(String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getContactListPage().tapOnTopPeople(contact);
    }

    /**
     * Creates a conversation from any selected users
     *
     * @throws Exception
     * @step. ^I tap on create conversation$
     */
    @When("^I tap on create conversation$")
    public void ITapOnCreateConversation() throws Exception {
        getSearchListPage().tapCreateConversation();
    }

    /**
     * Presses the close button in the people picker page
     *
     * @throws Exception
     * @step. ^I press Clear button$
     */
    @When("^I press Clear button$")
    public void IPressClearButton() throws Exception {
        getSearchListPage().tapClearButton();
    }

    /**
     * Random to clear search result by click clear button or click navigate back button
     *
     * @throws Exception
     * @step. ^I clear search result by press clear button or back button$
     */
    @When("^I clear search result by tap clear button or back button$")
    public void IClearSearchResultByPressClearButtonOrPressBackButton() throws Exception {
        if (random.nextInt(100) % 2 == 0) {
            IPressClearButton();
        } else {
            pagesCollection.getCommonPage().navigateBack();
        }
    }

    /**
     * Type UserName/UserNameAlias, UserEmail/UserEmailAlias, UserPhone/UserPhoneAlias into search filed
     * Support type partial words
     *
     * @param partialWords if not null, means only type the part of word[Start from index 0]
     * @param text         the text to type
     * @throws Exception
     * @step. ^I type (the first \d+ chars? of )?(?:user name|user email|user phone number|group name) "(.*)" in search field$
     */
    @When("^I type (the first \\d+ chars? of )?(?:user name|user email|user phone number|group name) \"(.*)\" in search field$")
    public void ITypeWordInSearchFiled(String partialWords, String text) throws Exception {
        text = usrMgr.replaceAliasesOccurences(text, FindBy.EMAIL_ALIAS);
        text = usrMgr.replaceAliasesOccurences(text, FindBy.NAME_ALIAS);
        text = usrMgr.replaceAliasesOccurences(text, FindBy.PHONENUMBER_ALIAS);
        if (partialWords != null) {
            int partialSize = Integer.parseInt(partialWords.replaceAll("[\\D]", ""));
            text = (partialSize < text.length()) ? text.substring(0, partialSize) : text;
        }
        getSearchListPage().typeTextInPeopleSearch(text);
        CommonSteps.getInstance().WaitForTime(2);
    }

    /**
     * Wait for a user/group in the search result/contact list
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I( do not)? see (user|group)? (.*) in (Search result|Contact)? list$
     */
    @When("^I( do not)? see (user|group)? (.*) in (Search result|Contact)? list$")
    public void ISeeUser(String shouldNotSee, String group, String contact, String listType) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        boolean isGroup = group.toLowerCase().equals("group");

        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The %s '%s' should be visible in %s list", group, contact, listType),
                    listType.toLowerCase().equals("contact")
                            ? getContactListPage().waitUntilNameVisible(isGroup, contact)
                            : getSearchListPage().waitUntilNameVisible(isGroup, contact)
            );
        } else {
            Assert.assertTrue(String.format("The %s '%s' should be invisible in %s list", group, contact, listType),
                    listType.toLowerCase().equals("contact")
                            ? getContactListPage().waitUntilNameInvisible(isGroup, contact)
                            : getSearchListPage().waitUntilNameInvisible(isGroup, contact)
            );
        }
    }

    /**
     * Checks to see that there are no results in the search field
     *
     * @throws Exception
     * @step. ^I see that no results found$
     */
    @Then("^I see that no results found$")
    public void ISeeNoResultsFound() throws Exception {
        Assert.assertTrue("Some results were found in People Picker",
                getSearchListPage().isErrorVisible());
    }

    /**
     * Check the add people error message is visible
     *
     * @param shouldNotSee equal null means the error message should be visible
     * @throws Exception
     * @step. ^I( do not)? see No matching result placeholder on People picker page$
     */
    @Then("^I( do not)? see No matching result placeholder on People picker page$")
    public void ISeeTheAddPeopleErrorMessage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Add people error message should be visible", getSearchListPage().isErrorVisible());
        } else {
            Assert.assertTrue("Add people error message should be invisible", getSearchListPage().isErrorInvisible());
        }
    }

    /**
     * Taps on a name found in the people picker page
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I tap on user name found on People picker page (.*)$
     */
    @When("^I tap on user name found on People picker page (.*)$")
    public void ITapOnUserNameFoundOnPeoplePickerPage(String contact)
            throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getSearchListPage().tapOnUserName(contact);
    }

    /**
     * Taps on a group found in the people picker page
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I tap on group found on People picker page (.*)$
     */
    @When("^I tap on group found on People picker page (.*)$")
    public void ITapOnGroupFoundOnPeoplePickerPage(String contact)
            throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        getSearchListPage().tapOnGroupName(contact);
    }

    /**
     * Checks to see if the add to conversation button is visible
     *
     * @throws Exception
     * @step. ^I see (?:Add to|Create) to conversation button$
     */
    @When("^I see (?:Add to|Create) conversation button$")
    public void ISeeAddToConversationButton() throws Exception {
        Assert.assertTrue("Add to/Create conversation button is not visible",
                getSearchListPage().isPickUserConfirmationBtnVisible());
    }

    /**
     * Clicks on the Add to conversation button
     *
     * @throws Exception
     * @step. ^I click on (?:Add to|Create) to conversation button$
     */
    @When("^I click on (?:Add to|Create) conversation button$")
    public void IClickOnAddToConversationButton() throws Exception {
        getSearchListPage().tapPickUserConfirmationButton();
    }

    /**
     * Check to see that the top people section is visible or not
     *
     * @param shouldNotBeVisible is set to null is "do not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see TOP PEOPLE$
     */
    @Then("^I( do not)? see TOP PEOPLE$")
    public void ThenIDontSeeTopPeople(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(
                    "TOP PEOPLE overlay is hidden, but it should be visible",
                    getContactListPage().waitUntilTopPeopleHeaderVisible());
        } else {
            Assert.assertTrue(
                    "TOP PEOPLE overlay is visible, but it should be hidden",
                    getContactListPage().waitUntilTopPeopleHeaderInvisible());
        }
    }

    /**
     * Wait for Top People list to appear in People picker
     *
     * @throws Exception
     * @step. ^I wait until Top People list appears$
     */
    @When("^I wait until Top People list appears$")
    public void WaitForTopPeople() throws Exception {
        Assert.assertTrue("Top People list is not visible",
                getContactListPage().waitUntilTopPeopleHeaderVisible());
    }

    /**
     * Verify action button presence
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param buttonName   one of possible action button names
     * @throws Exception
     * @step. ^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on People Picker page$
     */
    @Then("^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on People Picker page$")
    public void ISeeActionButton(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("'%s' action button is not visible", buttonName),
                    getSearchListPage().waitUntilActionButtonIsVisible(buttonName));
        } else {
            Assert.assertTrue(String.format("'%s' action button is not visible", buttonName),
                    getSearchListPage().waitUntilActionButtonIsInvisible(buttonName));
        }
    }

    /**
     * Verify the search text is empty
     *
     * @throws Exception
     * @step. ^I see the search text is empty$"
     */
    @Then("^I see the search text is empty$")
    public void SearchTextIsEmpty() throws Exception {
        Assert.assertTrue("The search text should be empty", getSearchListPage().isPeopleSearchTextEmpty());
    }

    /**
     * Verify the search suggestion is visible
     *
     * @param shouldNotSee if shouldnotSee equals null, then the search suggestions should be presented
     * @throws Exception
     * @step. ^I( do not)? see search suggestions$
     */
    @Then("^I( do not)? see search suggestions$")
    public void ISeeSearchSuggestions(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The search suggestion should not be empty", getSearchListPage().isSuggestionVisible());
        } else {
            Assert.assertTrue("The search suggestion should be presented, but cannot see any suggestions",
                    getSearchListPage().isSuggestionInvisible());
        }
    }

    /**
     * Tap the corresponding action button
     *
     * @param buttonName one of possible action button names
     * @throws Exception
     * @step. ^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on People Picker page$
     */
    @When("^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on People Picker page$")
    public void ITapActionButtons(String buttonName) throws Exception {
        getSearchListPage().tapActionButton(buttonName);
    }

    /**
     * Swipe right on caontact avatar found on People Picker page
     *
     * @param name contact name/alias
     * @throws Exception
     * @step. ^I swipe right on contact avatar (.*) in [Pp]eople [Pp]icker$
     */
    @When("^I swipe right on contact avatar (.*) in [Pp]eople [Pp]icker$")
    public void ISwipeRightOnContact(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getSearchListPage().swipeRightOnContactAvatar(name);
    }

    /**
     * Store a screenshot of user avatar for a future comparison
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I remember the state of (.*) avatar in contact list$
     */
    @When("^I remember the state of (.*) avatar in Contact list$")
    public void IRememberTheStateOfAvatar(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        this.avatarState = new ElementState(
                () -> getContactListPage().getUserAvatarScreenshot(name).orElseThrow(IllegalStateException::new)
        ).remember();
    }

    /**
     * Verify that avatar state of a user in the list is changed since the last snapshot
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I verify the state of (.*) avatar in contact list is changed$"
     */
    @Then("^I verify the state of (.*) avatar in Contact list is changed$")
    public void IVerifyTheAvatarStateIsChanged(String alias) throws Exception {
        if (this.avatarState == null) {
            throw new IllegalStateException("Please take a screenshot of previous avatar state first");
        }
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        final double minSimilarity = 0.97;
        Assert.assertTrue(String.format("User avatar for '%s' seems to be the same", name),
                this.avatarState.isChanged(10, minSimilarity));
    }

    /**
     * Tap the Invote button next to a particular user name
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I tap Invite button next to (.*) in Contact list
     */
    @When("^I tap Invite button next to (.*) in Contact list")
    public void ITapInviteButton(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        getContactListPage().tapInviteButtonFor(name);
    }

    /**
     * Select the corresponding email on the invitation alert
     *
     * @param alias email address/alias
     * @throws Exception
     * @step. ^I select (.*) email on invitation sending alert$
     */
    @When("^I select (.*) email on invitation sending alert$")
    public void ISelectEmailOnInvitationAlert(String alias) throws Exception {
        final String email = usrMgr.replaceAliasesOccurences(alias, ClientUsersManager.FindBy.EMAIL_ALIAS);
        getContactListPage().selectEmailOnAlert(email);
    }

    /**
     * Verify that invitation email exists in user's mailbox
     *
     * @param alias user name/alias
     * @throws Exception
     * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
     */
    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Exception {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(alias);
        if (!invitationMessages.containsKey(user)) {
            throw new IllegalStateException(String.format("Please start invitation message listener for '%s' first",
                    user.getName()));
        }
        final String receivedMessage = invitationMessages.get(user).get();
        Assert.assertTrue(String.format("Invitation email for %s has not been received", user.getEmail()),
                new InvitationMessage(receivedMessage).isValid());
    }

    private Map<ClientUser, Future<String>> invitationMessages = new HashMap<>();

    /**
     * Start invitation messages listener for the particular user
     *
     * @param forUser user name/alias
     * @throws Exception
     * @step. ^I start listening to invitation messages for (.*)
     */
    @When("^I start listening to invitation messages for (.*)")
    public void IStartListeningToInviteMessages(String forUser) throws Exception {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(forUser);
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, user.getEmail());
        invitationMessages.put(user,
                mbox.getMessage(expectedHeaders, BackendAPIWrappers.INVITATION_RECEIVING_TIMEOUT));
    }

    /**
     * Broadcast the link parsed from the recent invitation email for receiver
     *
     * @param receiver email/alias
     * @throws Exception
     * @step. ^I broadcast the invitation for (.*)
     */
    @When("^I broadcast the invitation for (.*)")
    public void IBroadcastInvitation(String receiver) throws Exception {
        final ClientUser user = usrMgr.findUserByEmailOrEmailAlias(receiver);
        if (!invitationMessages.containsKey(user)) {
            throw new IllegalStateException(String.format("There are no invitation messages for user %s",
                    user.getName()));
        }
        final String receivedMessage = invitationMessages.get(user).get();
        final String invitationLink = new InvitationMessage(receivedMessage).extractInvitationLink();
        final String code = invitationLink.substring(invitationLink.indexOf("/i/") + 3, invitationLink.length());
        AndroidCommonUtils.broadcastInvitationCode(code);
    }
}
