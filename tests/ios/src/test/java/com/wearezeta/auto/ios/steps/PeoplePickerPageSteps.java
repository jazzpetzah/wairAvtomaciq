package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private PeoplePickerPage getPeoplePickerPage() throws Exception {
        return pagesCollection.getPage(PeoplePickerPage.class);
    }

    @When("^I see [Pp]eople [Pp]icker page$")
    public void WhenISeePeoplePickerPage() throws Exception {
        Assert.assertTrue(getPeoplePickerPage().isPeoplePickerPageVisible());
    }

    /**
     * Verifies that CONNECT label is visible
     *
     * @throws Exception
     * @step. I see CONNECT label
     */

    @When("I (dont )?see CONNECT label")
    public void ISeePeopleYouMayKnowLabel(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("CONNECT label is not visible", getPeoplePickerPage().isPeopleYouMayKnowLabelVisible());
        } else {
            Assert.assertTrue("CONNECT label is visible", getPeoplePickerPage().isPeopleYouMayKnowLabelInvisible());
        }
    }

    @When("^I tap on Search input on People picker page$")
    public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
        getPeoplePickerPage().tapOnPeoplePickerSearch();
    }

    @When("^I input in People picker search field (?:user|conversation) name (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserName(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        getPeoplePickerPage().fillTextInPeoplePickerSearch(name);
    }

    /**
     * Fills in search field pointed amount of letters from username/conversation starting from the first one
     *
     * @param number amount of letters to be input
     * @param name   user name
     * @throws Exception
     * @step. ^I input in People picker search field first (\\d+) letters? of (?:user|conversation) name (.*)$
     */
    @When("^I input in People picker search field first (\\d+) letters? of (?:user|conversation) name (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserName(int number, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        if (name.length() > number) {
            getPeoplePickerPage().fillTextInPeoplePickerSearch(name.substring(0, number));
        } else {
            throw new IllegalArgumentException(String.format("Name is only %s chars length. Put in step a less value",
                    name.length()));
        }
    }

    @When("^I input in People picker search field user email (.*)$")
    public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email) throws Exception {
        try {
            email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        getPeoplePickerPage().fillTextInPeoplePickerSearch(email);
    }

    /**
     * Verify that conversation is presented in search results
     *
     * @param name           conversation name to search
     * @param shouldNotExist equals to null if the converssation should be visible
     * @throws Exception
     * @step. ^I see conversation (.*) is (NOT )?presented in Search results$
     */
    @When("^I see the conversation \"(.*)\" (does not )?exists? in Search results$")
    public void ISeeConversationIsFoundInSearchResult(String name, String shouldNotExist) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        if (shouldNotExist == null) {
            Assert.assertTrue(String.format("The conversation '%s' does not exist in Search results", name),
                    getPeoplePickerPage().getSearchResultsElement(name).isPresent());
        } else {
            Assert.assertTrue(
                    String.format("The conversation '%s' exists in Search results, but it should not", name),
                    getPeoplePickerPage().isElementNotFoundInSearch(name));
        }
    }

    /**
     * Verifies that contact is the 1st item in search result
     *
     * @param name that should be the 1st item in search result
     * @throws Exception
     * @step. ^I see the first item in Search result is (.*)$
     */
    @When("^I see the first item in Search result is (.*)$")
    public void ISeeConversationIsFirstSearchResult(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Contact %s is NOT first result in search", name),
                getPeoplePickerPage().isFirstItemInSearchResult(name));
    }

    /**
     * Verify whether No Results label is visible in search results
     *
     * @throws Exception
     * @step. ^I see No Results label in People picker search result$
     */
    @Then("^I see No Results label in People picker search result$")
    public void ISeeNoResultsLabel() throws Exception {
        Assert.assertTrue("'No Results' label is not visible",
                getPeoplePickerPage().waitUntilNoResultsLabelIsVisible());
    }

    @When("^I don't see Add to conversation button$")
    public void WhenIDontSeeAddToConversationButton() throws Exception {
        Assert.assertTrue("Add to conversation button is visible", getPeoplePickerPage().addToConversationNotVisible());
    }

    /**
     * Click on close button to dismiss Invite list
     *
     * @throws Exception
     * @step. ^I tap Close Invite list button$
     */
    @When("^I tap Close Invite list button$")
    public void WhenIClickCloseInviteListButton() throws Exception {
        getPeoplePickerPage().closeInviteList();
    }

    /**
     * Select pointed amount of contacts from top people in a row starting from first
     *
     * @param numberOfTopConnections amount of contacts that should be selected
     * @throws Exception
     */
    @Then("^I tap on first (\\d+) top connections?$")
    public void WhenITapOnTopConnections(int numberOfTopConnections) throws Exception {
        getPeoplePickerPage().tapNumberOfTopConnections(numberOfTopConnections);
    }

    /**
     * Tap on pointed amount of users from top people skipping pointed contact
     *
     * @param numberOfTopConnections number of top contacts to tap
     * @param contact                name of contact that shouldn't be tapped
     * @throws Exception
     */
    @Then("^I tap on (\\d+) top connections? but not (.*)")
    public void WhenITapOnTopConnectionsButNotUser(int numberOfTopConnections, String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getPeoplePickerPage().tapNumberOfTopConnectionsButNotUser(numberOfTopConnections, contact);
    }

    /**
     * Tap on top connection contact avatar by pointed id order
     *
     * @param i contact order in top peoples
     * @throws Exception
     * @step. I tap on (\\d+)\\w+ top connection contact
     */
    @When("^I tap on (\\d+)\\w+ top connection contact$")
    public void IClickOnTopConnectionByOrder(int i) throws Exception {
        getPeoplePickerPage().tapOnTopConnectionAvatarByOrder(i);
    }

    @When("I see top people list on People picker page")
    public void ISeeTopPeopleListOnPeoplePickerPage() throws Exception {
        Assert.assertTrue("Top People label is not shown", getPeoplePickerPage().isTopPeopleLabelVisible());
    }

    @When("^I press Backspace button in search field$")
    public void IPressBackspaceBtn() throws Exception {
        getPeoplePickerPage().pressBackspaceKeyboardButton();
    }

    /**
     * Click on conversation in search result with pointed name
     *
     * @param name conversation name
     * @throws Exception
     * @step. ^I tap on conversation (.*) in search result$
     */
    @When("^I tap on conversation (.*) in search result$")
    public void ITapOnConversationFromSearch(String name) throws Exception {
        name = usrMgr.findUserByNameOrNameAlias(name).getName();
        getPeoplePickerPage().selectElementInSearchResults(name);
    }

    @When("^I tap X button in People Picker input field$")
    public void ITapXButton() throws Exception {
        getPeoplePickerPage().tapOnPeoplePickerClearBtn();
    }

    /**
     * Unblocks a blocked user by clicking the unblock button
     *
     * @throws Exception
     * @step. ^I tap Unblock button
     */
    @When("^I tap Unblock button$")
    public void IUnblockUser() throws Exception {
        getPeoplePickerPage().unblockUser();
    }

    /**
     * Presses the Send An Invite button in the people picker. To invite people via mail.
     *
     * @throws Exception
     * @step. ^I tap Send Invite button$
     */
    @When("^I tap Send Invite button$")
    public void ITapSendAnInviteButton() throws Exception {
        getPeoplePickerPage().tapSendInviteButton();
    }

    /**
     * Presses the Copy button on the Send Invitation pop up
     *
     * @throws Exception
     * @step. ^I tap Copy button$
     */
    @When("^I tap Copy button$")
    public void ITapCopyButton() throws Exception {
        getPeoplePickerPage().tapSendInviteCopyButton();
    }

    /**
     * Presses the instant connect plus button
     *
     * @param nameAlias user name/aias
     * @throws Exception
     * @step. ^I tap the instant connect button next to (.*)
     */
    @When("^I tap the instant connect button next to (.*)")
    public void ITapInstantConnectButton(String nameAlias) throws Exception {
        nameAlias = usrMgr.replaceAliasesOccurences(nameAlias, ClientUsersManager.FindBy.NAME_ALIAS);
        getPeoplePickerPage().pressInstantConnectButton(nameAlias);
    }

    /**
     * click the corresponding action button on People Picker page
     *
     * @throws Exception
     * @step. ^I tap (Create conversation|Open conversation|Video call|Send image|Call) action button on People picker page$
     */
    @When("^I tap (Create conversation|Open conversation|Video call|Send image|Call) action button on People picker page$")
    public void ITapActionButtonOnPeoplePickerPage(String actionButtonName) throws Exception {
        getPeoplePickerPage().clickActionButton(actionButtonName);
    }


    /**
     * Verify if an action button is visible
     *
     * @throws Exception
     * @step. ^I (do not )?see (Create conversation|Open conversation|Video call|Send image|Call) action button on People picker page$
     */
    @When("^I (do not )?see (Create conversation|Open conversation|Video call|Send image|Call) action button on People picker page$")
    public void ISeenActionButton(String shouldNotBeVisible, String actionButtonName) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("'%s' action button is not visible", actionButtonName),
                    getPeoplePickerPage().isActionButtonVisible(actionButtonName));
        } else {
            Assert.assertTrue(String.format("'%s' action button is visible, but should be hidden", actionButtonName),
                    getPeoplePickerPage().isActionButtonInvisible(actionButtonName));
        }
    }

    /**
     * Verifies that Share your contacts settings message is shown
     *
     * @throws Exception
     * @step. I see Share Contacts settings warning
     */
    @Then("^I see Share Contacts settings warning$")
    public void ISeeShareContactsSettingsWarning() throws Exception {
        Assert.assertTrue("Share Contacts settings warning is not visible",
                getPeoplePickerPage().isShareContactsSettingsWarningShown());
    }
}
