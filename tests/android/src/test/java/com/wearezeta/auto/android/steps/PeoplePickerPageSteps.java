package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
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
        return pagesCollection.getPage(PeoplePickerPage.class);
    }

    /**
     * Checks to see that the people picker page (search view) is visible
     *
     * @throws Exception
     * @step. ^I see People picker page$
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
     */
    @When("^I tap on Search input on People picker page$")
    public void WhenITapOnSearchInputOnPeoplePickerPage() throws Throwable {
        getPeoplePickerPage().tapPeopleSearch();
    }

    /**
     * Selects a contact from the top people section in the people picker page
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on (.*) in Top People$
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
     * @throws Throwable
     * @step. ^I tap on create conversation$
     */
    @When("^I tap on create conversation$")
    public void WhenITapOnCreateConversation() throws Throwable {
        getPeoplePickerPage().tapCreateConversation();
    }

    /**
     * Presses the close button in the people picker page
     *
     * @throws Throwable
     * @step. ^I press Clear button$
     */
    @When("^I press Clear button$")
    public void WhenIPressClearButton() throws Throwable {
        getPeoplePickerPage().tapClearButton();
    }

    /**
     * Swipe down people picker
     *
     * @throws Exception
     * @step. ^I swipe down people picker$
     */
    @When("^I swipe down people picker$")
    public void ISwipeDownContactList() throws Exception {
        getPeoplePickerPage().hideKeyboard();
        getPeoplePickerPage().swipeDown(500);
    }

    /**
     * Types a user name into the people picker search field.
     *
     * @param contact
     * @throws Exception
     * @step. ^I input in People picker search field user name (.*)$
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
     * @param email
     * @throws Exception
     * @step. ^I input in People picker search field user email (.*)$
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
     * @param part
     * @param contact
     * @throws Throwable
     * @step. ^I input in search field part (.*) of user name to connect to
     * (.*)$
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
     * Enter user name or email into the corresponding People Picker field
     *
     * @param searchCriteria user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I enter \"(.*)\" into Search input on People [Pp]icker page$
     */
    @When("^I enter \"(.*)\" into Search input on People [Pp]icker page")
    public void IEnterStringIntoSearchField(String searchCriteria)
            throws Exception {
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.EMAIL_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.NAME_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
                FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
        CommonSteps.getInstance().WaitForTime(2);
    }

    /**
     * Adds user name to search field (existing content is not cleaned)
     *
     * @param contact
     * @throws Throwable
     * @step. ^I add in search field user name to connect to (.*)$
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
     * @param contact
     * @throws Exception
     * @step. ^I see user (.*) found on People picker page$
     */
    @When("^I see user (.*) found on People picker page$")
    public void WhenISeeUserFoundOnPeoplePickerPage(String contact)
            throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
        }
        Assert.assertTrue(String.format("User '%s' is not visible on People Picker page", contact),
                getPeoplePickerPage().isUserVisible(contact));
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
                getPeoplePickerPage().isNoResultsFoundVisible());
    }

    /**
     * Taps on a name found in the people picker page
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on user name found on People picker page (.*)$
     */
    @When("^I tap on user name found on People picker page (.*)$")
    public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact)
            throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getPeoplePickerPage().selectUser(contact);
    }

    /**
     * Taps on a group found in the people picker page
     *
     * @param contact
     * @throws Exception
     * @step. ^I tap on group found on People picker page (.*)$
     */
    @When("^I tap on group found on People picker page (.*)$")
    public void WhenITapOnGroupFoundOnPeoplePickerPage(String contact)
            throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        getPeoplePickerPage().selectGroup(contact);
    }

    /**
     * Checks to see if the add to conversation button is visible
     *
     * @throws Exception
     * @step. ^I see Add to conversation button$
     */
    @When("^I see Add to conversation button$")
    public void WhenISeeAddToConversationButton() throws Exception {
        Assert.assertTrue("Add to conversation button is not visible",
                getPeoplePickerPage().isAddToConversationBtnVisible());
    }

    /**
     * Clicks on the Add to conversation button
     *
     * @throws Exception
     * @step. ^I click on Add to conversation button$
     */
    @When("^I click on Add to conversation button$")
    public void WhenIClickOnAddToConversationButton() throws Exception {
        getPeoplePickerPage().clickOnAddToCoversationButton();
    }

    /**
     * Navigates back to the conversation list by swiping down
     *
     * @throws Exception
     * @step. ^I navigate back to Conversations List$
     */
    @When("^I navigate back to Conversations List$")
    public void WhenINavigateBackToConversationsList() throws Exception {
        getPeoplePickerPage().navigateBack();
    }

    /**
     * Check that user exists in People picker
     *
     * @param contact
     * @throws Throwable
     * @step. ^I see user (.*) in [Pp]eople [Pp]icker$
     */
    @Then("^I see user (.*) in [Pp]eople [Pp]icker$")
    public void ThenISeeUserInPeoplePicker(String contact) throws Throwable {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
        }
        Assert.assertTrue(String.format(
                "User '%s' is not visible in People Picker", contact),
                getPeoplePickerPage().isUserVisible(contact));
    }

    /**
     * Looks for a group chat in the people picker search view
     *
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @param name group name/alias
     * @throws Exception
     * @step. ^I (do not )?see group (.*) in [Pp]eople [Pp]icker$
     */
    @Then("^I (do not )?see group (.*) in [Pp]eople [Pp]icker$")
    public void ThenISeeGroupInPeoplePicker(String shouldNotSee, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Group '%s' is not visible in search results", name),
                    getPeoplePickerPage().isGroupVisible(name));
        } else {
            Assert.assertTrue(String.format("Group '%s' is visible in search results, but should be hidden", name),
                    getPeoplePickerPage().isGroupInvisible(name));
        }
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
                    getPeoplePickerPage().isTopPeopleHeaderVisible());
        } else {
            Assert.assertTrue(
                    "TOP PEOPLE overlay is visible, but it should be hidden",
                    getPeoplePickerPage().waitUntilTopPeopleHeaderInvisible());
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
                getPeoplePickerPage().isTopPeopleHeaderVisible());
    }

    /**
     * Verify that Call action button is visible
     *
     * @throws Exception
     * @step. ^I see call action button on People picker page$
     */
    @When("^I see call action button on People picker page$")
    public void ISeeCallActionButtonOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Call action button is not visible",
                getPeoplePickerPage().isCallButtonVisible());
    }

    /**
     * Verify that Send image action button is visible
     *
     * @throws Exception
     * @step. ^I see Send image action button on People picker page$
     */
    @When("^I see Send image action button on People picker page$")
    public void ISeeSendImageActionButtonOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Send image action button is not visible",
                getPeoplePickerPage().isSendImageButtonVisible());
    }

    /**
     * Verify if Open conversation button is visible
     *
     * @throws Exception
     * @step. ^I see open conversation action button on People picker page$
     */
    @When("^I see open conversation action button on People picker page$")
    public void ISeeOpenConversationActionButton() throws Exception {
        Assert.assertTrue("Open conversation button is not visible",
                getPeoplePickerPage()
                        .waitUntilOpenOrCreateConversationButtonIsVisible());
    }

    /**
     * Verify whether Open/Create Conversation button is visible
     *
     * @param shouldBeVisible equals to null if the button should be visible
     * @param expectedCaption either 'Open' or 'Create'
     * @throws Exception
     * @step. ^I (do not )?see (?:the |\\s*)(Open|Create) Conversation button on
     * [Pp]eople [Pp]icker page$"
     */
    @Then("^I (do not )?see (?:the |\\s*)(Open|Create) Conversation button on [Pp]eople [Pp]icker page$")
    public void ISeeOpenConversationButton(String shouldBeVisible,
                                           String expectedCaption) throws Exception {
        if (shouldBeVisible == null) {
            Assert.assertTrue(
                    String.format("%s Conversation button is not visible",
                            expectedCaption),
                    getPeoplePickerPage()
                            .waitUntilOpenOrCreateConversationButtonIsVisible(
                                    expectedCaption));
        } else {
            Assert.assertTrue(
                    String.format(
                            "%s Conversation button is still visible, but should be hidden",
                            expectedCaption),
                    getPeoplePickerPage()
                            .waitUntilOpenOrCreateConversationButtonIsInvisible());
        }
    }

    /**
     * Verify if Open, Call and Send image action buttons are visible
     *
     * @throws Exception
     * @step. ^I see action buttons appeared on People picker page
     */
    @When("^I see action buttons appeared on People picker page$")
    public void ISeeActionButttonsAppearedOnPeoplePickerPage() throws Exception {
        ISeeOpenConversationActionButton();
        ISeeCallActionButtonOnPeoplePickerPage();
        ISeeSendImageActionButtonOnPeoplePickerPage();
    }

    /**
     * Opens the conversation by clicking the conversation action button
     *
     * @throws Throwable
     * @step. ^I click on open conversation action button on People picker page$
     */
    @When("^I click on open conversation action button on People picker page$")
    public void IClickOnOpenConversationActionButtonOnPeoplePickerPage()
            throws Throwable {
        getPeoplePickerPage().tapOpenConversationButton();
    }

    /**
     * Tap the Create/Open Conversation button
     *
     * @throws Exception
     * @step. ^I tap (?:Open|Create) Conversation button on [Pp]eople [Pp]icker
     * page$
     */
    @When("^I tap (?:Open|Create) Conversation button on [Pp]eople [Pp]icker page$")
    public void ITapConversationActionButton() throws Exception {
        getPeoplePickerPage().tapOpenConversationButton();
    }

    /**
     * Opens the picture gallery by clicking the send image action button
     *
     * @throws Throwable
     * @step. ^I click Send image action button on People picker page$
     */
    @When("^I click Send image action button on People picker page$")
    public void IClickSendImageActionButtonOnPeoplePickerPage()
            throws Throwable {
        getPeoplePickerPage().tapCameraButton();
    }

    /**
     * Starts a call by clicking the call action button
     *
     * @throws Throwable
     * @step. ^I click Call action button on People picker page$
     */
    @When("^I click Call action button on People picker page$")
    public void IClickCallActionButtonOnPeoplePickerPage() throws Throwable {
        getPeoplePickerPage().tapCallButton();
    }

    /**
     * Action buttons disappear when contact gets unchecked from search
     *
     * @throws Exception
     * @step. ^I see action buttons disappear from People Picker page$
     */
    @Then("^I see action buttons disappear from People Picker page$")
    public void ISeeActionButtonsDisappearFromPeoplePickerPage()
            throws Exception {
        getPeoplePickerPage()
                .waitUntilOpenOrCreateConversationButtonIsInvisible();
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
        getPeoplePickerPage().swipeRightOnContactAvatar(name);
    }
}
