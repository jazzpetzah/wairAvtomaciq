package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Random;

public class PeoplePickerPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private static final Random random = new Random();

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
     * @throws Exception
     * @step. ^I tap on Search input on People picker page$
     */
    @When("^I tap on Search input on People picker page$")
    public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
        getPeoplePickerPage().tapPeopleSearch();
    }

    /**
     * Selects a contact from the top people section in the people picker page
     *
     * @param contact user name/alias
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
     * @throws Exception
     * @step. ^I tap on create conversation$
     */
    @When("^I tap on create conversation$")
    public void WhenITapOnCreateConversation() throws Exception {
        getPeoplePickerPage().tapCreateConversation();
    }

    /**
     * Presses the close button in the people picker page
     *
     * @throws Exception
     * @step. ^I press Clear button$
     */
    @When("^I press Clear button$")
    public void WhenIPressClearButton() throws Exception {
        getPeoplePickerPage().tapClearButton();
    }

    /**
     * Random to clear search result by click clear button or click navigate back button
     *
     * @throws Exception
     * @step. ^I clear search result by press clear button or back button$
     */
    @When("^I clear search result by press clear button or back button$")
    public void WhenIClearSearchResultByPressClearButtonOrPressBackButton() throws Exception {
        if (random.nextInt(100) % 2 == 0) {
            WhenIPressClearButton();
        } else {
            pagesCollection.getCommonPage().navigateBack();
        }
    }

    /**
     * Types a user name into the people picker search field.
     *
     * @param contact user name/alias
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
     * @param email user email/alias
     * @throws Exception
     * @step. ^I input in People picker search field user email (.*)$
     */
    @When("^I input in People picker search field user email (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email) throws Exception {
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
     * @param part    a part of user name
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I input in search field part (.*) of user name to connect to
     * (.*)$
     */
    @When("^I input in search field part (.*) of user name to connect to (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldPartOfUserName(String part,
                                                                  String contact) throws Exception {
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
    public void IEnterStringIntoSearchField(String searchCriteria) throws Exception {
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.EMAIL_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.NAME_ALIAS);
        searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria, FindBy.PHONENUMBER_ALIAS);
        getPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
        CommonSteps.getInstance().WaitForTime(2);
    }

    /**
     * Adds user name to search field (existing content is not cleaned)
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I add in search field user name to connect to (.*)$
     */
    @When("^I add in search field user name to connect to (.*)$")
    public void WhenIAddInSearchFieldUserNameToConnectTo(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getPeoplePickerPage().addTextToPeopleSearch(contact);
    }

    /**
     * Wait for a user in the people picker search list
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I see user (.*) found on People picker page$
     */
    @When("^I( do not)? see user (.*) found on People picker page$")
    public void WhenISeeUserFoundOnPeoplePickerPage(String shouldNotSee, String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("User '%s' is not visible on People Picker page", contact),
                    getPeoplePickerPage().isUserVisible(contact));
        } else {
            Assert.assertTrue(String.format("User '%s' should not visible on People Picker page", contact),
                    getPeoplePickerPage().isUserInvisible(contact));
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
                getPeoplePickerPage().isNoResultsFoundVisible());
    }

    /**
     * Taps on a name found in the people picker page
     *
     * @param contact user name/alias
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
     * @param contact user name/alias
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
        getPeoplePickerPage().clickOnAddToConversationButton();
    }

    /**
     * Check that user exists in People picker
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I see user (.*) in [Pp]eople [Pp]icker$
     */
    @Then("^I see user (.*) in [Pp]eople [Pp]icker$")
    public void ThenISeeUserInPeoplePicker(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("User '%s' is not visible in People Picker", contact),
                getPeoplePickerPage().isUserVisible(contact));
    }

    /**
     * Looks for a group chat in the people picker search view
     *
     * @param shouldNotSee equals to null if "do not" part does not exist
     * @param name         group name/alias
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
                    getPeoplePickerPage().waitUntilActionButtonIsVisible(buttonName));
        } else {
            Assert.assertTrue(String.format("'%s' action button is not visible", buttonName),
                    getPeoplePickerPage().waitUntilActionButtonIsInvisible(buttonName));
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
        Assert.assertTrue("The search text should be empty", getPeoplePickerPage().isPeopleSearchTextEmpty());
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
        getPeoplePickerPage().tapActionButton(buttonName);
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
