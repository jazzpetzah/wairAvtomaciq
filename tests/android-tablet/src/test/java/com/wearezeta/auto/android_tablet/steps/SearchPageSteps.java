package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletContactListPage;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSearchListPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SearchPageSteps {
    private TabletSearchListPage getSearchListPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletSearchListPage.class);
    }

    private TabletContactListPage getContactListPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletContactListPage.class);
    }

    /**
     * Verify that Search is visible or not.
     * Sometimes it's quite hard to make sure this page is not visible and that is why we check whether Top People
     * overlay is shown or not
     *
     * @param shouldNotBeVisible equals to null is "do not" part does not exist
     * @throws Exception
     * @step. ^I (do not )?see [Ss]earch page$
     */
    @When("^I (do not )?see [Ss]earch page$")
    public void WhenITapOnTabletCreateConversation(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Search page is not visible", getContactListPage().waitUntilPageVisible());
        } else {
            Assert.assertTrue(
                    "Search page is visible, but should be hidden", getContactListPage().waitUntilPageInvisible());
        }
    }

    /**
     * Enter user name or email into the corresponding Search field
     *
     * @param searchCriteria user name/email/phone number or the corresponding aliases
     * @throws Exception
     * @step. ^I enter "(.*)" into Search input on [Ss]earch page$
     */
    @When("^I enter \"(.*)\" into Search input on [Ss]earch page$")
    public void IEnterStringIntoSearchField(String searchCriteria) throws Exception {
        searchCriteria = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(searchCriteria, FindBy.EMAIL_ALIAS);
        searchCriteria = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(searchCriteria, FindBy.NAME_ALIAS);
        searchCriteria = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(searchCriteria, FindBy.PHONENUMBER_ALIAS);
        searchCriteria = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(searchCriteria, FindBy.UNIQUE_USERNAME_ALIAS);
        getContactListPage().typeTextInPeopleSearch(searchCriteria);
    }

    /**
     * Taps on a name found in the Search page
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I tap on user name found on [Ss]earch page (.*)$
     */
    @When("^I tap on user name found on [Ss]earch page (.*)$")
    public void ITapOnUserNameFoundOnSearchPage(String contact)
            throws Exception {
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.EMAIL_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.PHONENUMBER_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.UNIQUE_USERNAME_ALIAS);
        getSearchListPage().tapOnUserName(contact);
    }

    /**
     * Taps on a group found in the Search page
     *
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I tap on group found on [Ss]earch page (.*)$
     */
    @When("^I tap on group found on [Ss]earch page (.*)$")
    public void ITapOnGroupFoundOnSearchPage(String contact)
            throws Exception {
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.EMAIL_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.PHONENUMBER_ALIAS);
        contact = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(contact, FindBy.UNIQUE_USERNAME_ALIAS);
        getSearchListPage().tapOnGroupName(contact);
    }

    /**
     * Check whether the particular user avatar is visible
     *
     * @param shouldNotSee equals to null if "do not " part does not exist in the step
     *                     description
     * @param name         user name/alias
     * @param isGroupStr   is not null if group avatar should be verified instead of single user avatar
     * @param listType     determine currently worked on which list, which could be contact list or search result list
     * @throws Exception
     * @step. ^I (do not )?see "(.*)" (group )?avatar in (Search result|Contact)? list$
     */
    @When("^I (do not )?see \"(.*)\" (group )?avatar in (Search result|Contact)? list$")
    public void ISeeContactAvatar(String shouldNotSee, String name, String isGroupStr, String listType) throws Exception {
        boolean isGroup = (isGroupStr != null);
        name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);

        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format("The %s '%s' should be visible in %s list",
                            isGroup ? "group" : "user", name, listType),
                    listType.toLowerCase().equals("contact")
                            ? getContactListPage().waitUntilNameVisible(isGroup, name)
                            : getSearchListPage().waitUntilNameVisible(isGroup, name)
            );
        } else {
            Assert.assertTrue(
                    String.format("The %s '%s' should be invisible in %s list",
                            isGroup ? "group" : "user", name, listType),
                    listType.toLowerCase().equals("contact")
                            ? getContactListPage().waitUntilNameInvisible(isGroup, name)
                            : getSearchListPage().waitUntilNameInvisible(isGroup, name)
            );
        }
    }

    private ElementState rememberedAvatar = null;
    private final static double MAX_SIMILARITY_VALUE = 0.90;

    /**
     * Save the screenshot of current user avatar on Search page
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I remember (.*) avatar on [Ss]earch page$
     */
    @When("^I remember (.*) avatar on [Ss]earch page$")
    public void ITakeScreenshotOfContactAvatar(String name) throws Exception {
        final String convoName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        this.rememberedAvatar = new ElementState(
                () -> getSearchListPage().getUserAvatarScreenshot(convoName).orElseThrow(IllegalStateException::new)
        ).remember();
    }

    /**
     * Compare the screenshot of current user avatar on Search page with
     * the previous one
     *
     * @param name user name/alias
     * @throws Exception
     * @step. ^I verify (.*) avatar on [Ss]earch page is not the same
     * as the previous one$
     */
    @Then("^I verify (.*) avatar on [Ss]earch page is not the same as the previous one$")
    public void IVerifyAvatarIsNotTheSame(String name) throws Exception {
        final String convoName = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (this.rememberedAvatar == null) {
            throw new IllegalStateException("Please take a previous screenshot of user avatar first");
        }
        Assert.assertTrue(
                String.format("The current contact avatar of '%s' is very similar to the previous one", convoName),
                this.rememberedAvatar.isChanged(Timedelta.fromSeconds(10), MAX_SIMILARITY_VALUE));
    }

    /**
     * Close the X button to close Search
     *
     * @throws Exception
     * @step. ^I close (?:the |\\s*)Search$
     */
    @And("^I close (?:the |\\s*)Search$")
    public void ICloseSearch() throws Exception {
        getSearchListPage().tapClearButton();
    }

    /**
     * Tap the particular Top People avatar
     *
     * @param name name/alias
     * @throws Exception
     * @step. ^I tap (.*) avatar in Top People$
     */
    @When("^I tap (.*) avatar in Top People$")
    public void ITapAvatarInTopPeople(String name) throws Exception {
        name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUserManager()
                .findUserByNameOrNameAlias(name).getName();
        getContactListPage().tapOnTopPeople(name);
    }

    /**
     * Tap the corresponding action button
     *
     * @param buttonName one of possible action button names
     * @throws Exception
     * @step. ^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Ss]earch page$
     */
    @When("^I tap (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Ss]earch page$")
    public void ITapActionButtons(String buttonName) throws Exception {
        getSearchListPage().tapActionButton(buttonName);
    }

    /**
     * Verify action button presence
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param buttonName   one of possible action button names
     * @throws Exception
     * @step. ^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Ss]earch
     * page$
     */
    @Then("^I (do not )?see (Open Conversation|Create Conversation|Send Image|Call|Video Call) action button on [Ss]earch " +
            "page$")
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
     * Tap Backspace in the search field
     *
     * @throws Exception
     * @step. ^I type backspace in Search input on [Ss]earch page$
     */
    @When("^I type backspace in Search input on [Ss]earch page$")
    public void ITypeBackspace() throws Exception {
        getSearchListPage().typeBackspaceInSearchInput();
    }

    /**
     * Clicks on the Add to conversation button
     *
     * @throws Exception
     * @step. ^I tap on (?:Add to|Create) to conversation button on Search page$
     */
    @When("^I tap on (?:Add to|Create) conversation button on Search page$")
    public void ITapOnAddToConversationButton() throws Exception {
        getSearchListPage().tapPickUserConfirmationButton();
    }
}
