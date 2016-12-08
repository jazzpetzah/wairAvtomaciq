package com.wearezeta.auto.ios.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.SearchUIPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;

public class SearchUIPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SearchUIPage getSearchUIPage() throws Exception {
        return pagesCollection.getPage(SearchUIPage.class);
    }

    /**
     * Verify whether Search UI is visible
     *
     * @throws Exception
     * @step. ^I see Search UI$
     */
    @Then("^I see Search UI$")
    public void ISeeSearchUI() throws Exception {
        Assert.assertTrue("Search UI is not visible", getSearchUIPage().isVisible());
    }

    @When("^I tap input field on Search UI page$")
    public void ITapSearchInput() throws Exception {
        getSearchUIPage().tapSearchInput();
    }

    /**
     * Type in text in Search input field
     *
     * @param text    text to input
     * @param isUpper null if should be input as it is
     * @throws Exception
     * @step. ^I type "(.*)" in Search UI input field( in upper case)?$
     */
    @When("^I type \"(.*)\" in Search UI input field( in upper case)?$")
    public void ITypeInSearchInput(String text, String isUpper) throws Exception {
        text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
        text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.EMAIL_ALIAS);
        text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        getSearchUIPage().typeText((isUpper == null) ? text : text.toUpperCase());
    }

    /**
     * Fills in search field pointed amount of letters from username/conversation starting from the first one
     *
     * @param count amount of letters to be input
     * @param name  user name
     * @throws Exception
     * @step. ^I type first (\d+) letters? of (?:user|conversation) name "(.*)" into Search UI input field$
     */
    @When("^I type first (\\d+) letters? of (?:user|conversation) name \"(.*)\" into Search UI input field$")
    public void ITypeXLettersIntoSearchInput(int count, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
        if (name.length() > count) {
            getSearchUIPage().typeText(name.substring(0, count));
        } else {
            throw new IllegalArgumentException(String.format("Name is only %s chars length. Put in step a less value",
                    name.length()));
        }
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
                    getSearchUIPage().getSearchResultsElement(name).isPresent());
        } else {
            Assert.assertTrue(
                    String.format("The conversation '%s' exists in Search results, but it should not", name),
                    getSearchUIPage().isElementNotFoundInSearch(name));
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
                getSearchUIPage().isFirstItemInSearchResult(name));
    }

    /**
     * Verify whether No Results label is visible in search results
     *
     * @throws Exception
     * @step. ^I see No Results label on Search UI page$
     */
    @Then("^I see No Results label on Search UI page$")
    public void ISeeNoResultsLabel() throws Exception {
        Assert.assertTrue("'No Results' label is not visible",
                getSearchUIPage().waitUntilNoResultsLabelIsVisible());
    }

    /**
     * Select pointed amount of contacts from top people in a row starting from first
     *
     * @param numberOfTopConnections amount of contacts that should be selected
     * @throws Exception
     */
    @Then("^I tap on first (\\d+) top connections?$")
    public void WhenITapOnTopConnections(int numberOfTopConnections) throws Exception {
        getSearchUIPage().tapNumberOfTopConnections(numberOfTopConnections);
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
        getSearchUIPage().tapNumberOfTopConnectionsButNotUser(numberOfTopConnections, contact);
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
        getSearchUIPage().tapOnTopConnectionAvatarByOrder(i);
    }

    @When("^I see top people list on Search UI page$")
    public void ISeeTopPeopleList() throws Exception {
        Assert.assertTrue("Top People label is not shown", getSearchUIPage().isTopPeopleLabelVisible());
    }

    @When("^I press Backspace button in search field$")
    public void IPressBackspaceBtn() throws Exception {
        getSearchUIPage().pressBackspaceKeyboardButton();
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
        getSearchUIPage().selectElementInSearchResults(name);
    }

    @When("^I tap (X|Unblock|Send Invite|Copy Invite|(?:Close|Clear) Group Participants Picker) button on Search UI page$")
    public void ITapButton(String btnName) throws Exception {
        getSearchUIPage().tapButton(btnName);
    }

    /**
     * Clear the text from search input field
     *
     * @throws Exception
     * @step. ^I clear search input on Search UI page$
     */
    @And("^I clear search input on Search UI page$")
    public void IClearSearchInput() throws Exception {
        getSearchUIPage().clearSearchInput();
    }

    /**
     * Verify button visiblity on Search UI page
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (X|Unblock|Send Invite|Copy Invite|Close Group Participants Picker) button on Search UI page$
     */
    @Then("^I (do not )?see (X|Unblock|Send Invite|Copy Invite|(?:Close|Clear) Group Participants Picker) button on Search UI page$")
    public void ISeeButton(String shouldNotSee, String btnName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The button '%s' is expected to be visible", btnName),
                    getSearchUIPage().isButtonVisible(btnName));
        } else {
            Assert.assertTrue(String.format("The button '%s' is expected to be invisible", btnName),
                    getSearchUIPage().isButtonInvisible(btnName));
        }
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
        getSearchUIPage().tapInstantConnectButton(nameAlias);
    }

    /**
     * click the corresponding action button on People Picker page
     *
     * @throws Exception
     * @step. ^I tap (Create conversation|Open conversation|Video call|Send image|Call) action button on Search UI page$
     */
    @When("^I tap (Create conversation|Open conversation|Video call|Send image|Call) action button on Search UI page$")
    public void ITapActionButton(String actionButtonName) throws Exception {
        getSearchUIPage().tapActionButton(actionButtonName);
    }


    /**
     * Verify if an action button is visible
     *
     * @throws Exception
     * @step. ^I (do not )?see (Create conversation|Open conversation|Video call|Send image|Call) action button on Search UI page$
     */
    @When("^I (do not )?see (Create conversation|Open conversation|Video call|Send image|Call) action button on Search UI page$")
    public void ISeeActionButton(String shouldNotBeVisible, String actionButtonName) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("'%s' action button is not visible", actionButtonName),
                    getSearchUIPage().isActionButtonVisible(actionButtonName));
        } else {
            Assert.assertTrue(String.format("'%s' action button is visible, but should be hidden", actionButtonName),
                    getSearchUIPage().isActionButtonInvisible(actionButtonName));
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
                getSearchUIPage().isShareContactsSettingsWarningShown());
    }

    /**
     * Verify avatars details for the found users
     *
     * @param table data table containing 2 columns: user name/alias to search for and the
     *              expected user details string
     * @throws Exception
     */
    @Then("^I verify correct details are shown for the found users$")
    public void IVerifyFoundUsersDetails(DataTable table) throws Exception {
        getSearchUIPage().tapSearchInput();
        final List<List<String>> data = table.raw();
        final List<String> failuresList = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            String textToEnter = data.get(i).get(0);
            textToEnter = usrMgr.replaceAliasesOccurences(textToEnter, ClientUsersManager.FindBy.NAME_ALIAS);
            textToEnter = usrMgr.replaceAliasesOccurences(textToEnter,
                    ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
            getSearchUIPage().clearSearchInput();
            getSearchUIPage().typeText(textToEnter);
            String expectedDetails = data.get(i).get(1);
            expectedDetails = usrMgr.replaceAliasesOccurences(expectedDetails, ClientUsersManager.FindBy.NAME_ALIAS);
            expectedDetails = usrMgr.replaceAliasesOccurences(expectedDetails,
                    ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
            if (!getSearchUIPage().isSearchResultDetailsVisible(textToEnter, expectedDetails)) {
                failuresList.add(String.format("The expected details string '%s' is not shown for search result '%s'",
                        expectedDetails, textToEnter));
            }
        }
        Assert.assertTrue(String.join("\n", failuresList), failuresList.isEmpty());
    }
}
