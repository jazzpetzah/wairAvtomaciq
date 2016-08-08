package com.wearezeta.auto.osx.steps.webapp;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.webapp.StartUIPage;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.*;
import org.openqa.selenium.WebDriverException;
import static org.hamcrest.MatcherAssert.assertThat;

public class StartUIPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    /**
     * Verifies the presence of the People Picker
     *
     * @step. ^I see [Pp]eople [Pp]icker$
     *
     * @throws Exception
     */
    @When("^I see [Pp]eople [Pp]icker$")
    public void ISeeStartUI() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class).isVisible();
    }

    /**
     * Selects user from search results in People Picker
     *
     * @step. ^I select (.*) from People Picker results$
     *
     * @param user user name or email
     * @throws Exception
     */
    @When("^I select (.*) from People Picker results$")
    public void ISelectUserFromStartUIResults(String user)
            throws Exception {
        user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(StartUIPage.class)
                .selectUserFromSearchResult(user);
    }

    /**
     * Workaround for bug WEBAPP-1386
     *
     * @step. ^I wait for the search field of People Picker to be empty$
     *
     * @throws Exception
     */
    @When("^I wait for the search field of People Picker to be empty$")
    public void IWaitForSearchFieldToBeEmpty() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class)
                .waitForSearchFieldToBeEmpty();
    }

    /**
     * Input user name/email in search field of People Picker
     *
     * @step. ^I type (.*) in search field of People Picker$
     *
     * @param nameOrEmail
     * @throws Exception
     */
    @When("^I type (.*) in search field of People Picker$")
    public void ISearchForUser(String nameOrEmail) throws Exception {
        nameOrEmail = usrMgr.replaceAliasesOccurences(nameOrEmail,
                FindBy.NAME_ALIAS);
        nameOrEmail = usrMgr.replaceAliasesOccurences(nameOrEmail,
                FindBy.EMAIL_ALIAS);
        // adding spaces to ensure trimming of input
        webappPagesCollection.getPage(StartUIPage.class).searchForUser(
                " " + nameOrEmail + " ");
    }

    /**
     * Verify if user is found by Search in People Picker
     *
     * @step. ^I( do not)? see user (.*) found in People Picker$
     *
     * @param donot if null method returns true if found otherwise true if not found
     * @param name user name string
     * @throws Exception
     */
    @When("^I( do not)? see user (.*) found in People Picker$")
    public void ISeeUserFoundInStartUI(String donot, String name)
            throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);

        if (donot == null) {
            Assert.assertTrue(webappPagesCollection.getPage(StartUIPage.class).isUserFound(name));
        } else {
            Assert.assertTrue(webappPagesCollection.getPage(StartUIPage.class).isUserNotFound(name));
        }
    }

    /**
     * Click on the X button next to the suggested contact
     *
     * @step. ^I remove user (.*) from suggestions in People Picker$
     *
     * @param contact name of contact
     * @throws Exception
     */
    @When("^I remove user (.*) from suggestions in People Picker$")
    public void IClickRemoveButton(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(StartUIPage.class)
                .clickRemoveButtonOnSuggestion(contact);
    }

    /**
     * Click on the + button next to the suggested contact
     *
     * @step. ^I make a connection request for user (.*) directly from People Picker$
     *
     * @param contact name of contact
     * @throws Exception
     */
    @When("^I make a connection request for user (.*) directly from People Picker$")
    public void IClickPlusButton(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(StartUIPage.class)
                .clickPlusButtonOnSuggestion(contact);
    }

    /**
     * Click X button to close People Picker page
     *
     * @step. ^I close People Picker page$
     *
     * @throws Exception
     */
    @When("^I close People Picker$")
    public void ICloseStartUI() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class).closeStartUI();
    }

    /**
     * Clicks on user found by search to open connect dialog
     *
     * @step. ^I click on (not connected|pending) user (.*) found in People Picker$
     *
     * @param userType either "not connected" or "pending"
     * @param name user name string
     *
     * @throws Exception
     */
    @When("^I click on (not connected|pending) user (.*) found in People Picker$")
    public void IClickNotConnecteUserFoundInStartUI(String userType,
            String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (userType.equalsIgnoreCase("not connected")) {
            webappPagesCollection.getPage(StartUIPage.class)
                    .clickNotConnectedUserName(name);
        } else if (userType.equalsIgnoreCase("pending")) {
            webappPagesCollection.getPage(StartUIPage.class)
                    .clickPendingUserName(name);
        }
    }

    /**
     * Creates conversation with users selected in People Picker
     *
     * @step. ^I choose to create conversation from People Picker$
     * @throws Exception
     */
    @When("^I choose to create conversation from People Picker$")
    public void IChooseToCreateConversationFromStartUI() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class)
                .createConversation();
    }

    @Then("^I see more than (\\d+) suggestions? in people picker$")
    public void ISeeMoreThanXSuggestionsInStartUI(int count)
            throws Exception {
        assertThat("people suggestions",
                webappPagesCollection.getPage(StartUIPage.class)
                .getNumberOfSuggestions(), greaterThan(count));
    }

    /**
     * Verify whether Bring Your Friends or Invite People button is visible
     *
     * @step. ^I see Bring Your Friends or Invite People button$
     *
     * @throws Exception
     */
    @When("^I see Bring Your Friends or Invite People button$")
    public void ISeeSendInvitationButton() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class)
                .waitUntilBringYourFriendsOrInvitePeopleButtonIsVisible();
    }

    /**
     * Verify whether Gmail Import button is visible on People Picker page
     *
     * @step. ^I do not see Gmail Import button on People Picker page$
     *
     * @throws Exception
     */
    @When("^I do not see Gmail Import button on People Picker page$")
    public void IDoNotSeeGmailImportButton() throws Exception {
        webappPagesCollection.getPage(BringYourFriendsPopoverPage.class)
                .waitUntilGmailImportButtonIsNotVisible();
    }

    /**
     * Click button to bring friends from Gmail
     *
     * @step. ^I click button to bring friends from Gmail$
     *
     */
    @And("^I click button to bring friends from Gmail$")
    public void IClickButtonToBringFriendsFromGmail() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class).clickBringFriendsFromGmailButton();
    }

    /**
     * Verifies whether Google login prompt is visible
     *
     * @step. ^I see Google login popup$
     *
     * @throws Exception
     */
    @And("^I see Google login popup$")
    public void ISeeGoogleLoginPopup() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class)
                .switchToGooglePopup();
    }

    /**
     * Enter gmail login and password into corresponding window
     *
     * @step. ^I sign up at Google with email (.*) and password (.*)$"
     *
     * @param email
     * @param password
     * @throws Exception
     */
    @When("^I sign up at Google with email (.*) and password (.*)$")
    public void ISignUpAtGoogleWithEmail(String email, String password)
            throws Exception {
        GoogleLoginPage googleLoginPage = webappPagesCollection
                .getPage(GoogleLoginPage.class);
        // we use callable to handle exceptions
        googleLoginPage.waitForWindowClose(() -> {
            // sometimes Google already shows the email
            googleLoginPage.setEmail(email);
            // sometimes google shows a next button and you have to enter the
            // password separately
            if (googleLoginPage.hasNextButton()) {
                googleLoginPage.clickNext();
            }
            googleLoginPage.setPassword(password);
            // sign in might be the last action
            googleLoginPage.clickSignIn();
            try {
                // sometimes we have to allow requested permissions
                if (googleLoginPage.hasApproveButton()) {
                    googleLoginPage.clickApprove();
                }
            } catch (WebDriverException ex) {
                // NOOP window already closed
            }
            // in order to handle exceptions we can't use Runnable thus we have to return something
            return true;
        });
    }

    /**
     * Click Bring Your Friends or Invite People button on People Picker page
     *
     * @step. ^I click Bring Your Friends or Invite People button$
     *
     * @throws Exception
     */
    @When("^I click Bring Your Friends or Invite People button$")
    public void IClickBringYourFriendsOrInvitePeopleButton() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class)
                .clickBringYourFriendsOrInvitePeopleButton();
    }

    /**
     * Click Call button on People Picker page
     *
     * @step. ^I click Call button on People Picker page$
     *
     * @throws Exception
     */
    @When("^I click Call button on People Picker page$")
    public void IClickCallButton() throws Exception {
        webappPagesCollection.getPage(StartUIPage.class).clickCallButton();
    }

    /**
     * Closes and opens People Picker until Top People list is visible on People Picker page
     *
     * @step. ^I wait till Top People list appears$
     *
     * @throws Exception
     */
    @When("^I wait till Top People list appears$")
    public void IwaitTillTopPeopleListAppears() throws Exception {
        if (!webappPagesCollection.getPage(StartUIPage.class)
                .isTopPeopleLabelVisible()) {
            webappPagesCollection.getPage(StartUIPage.class).closeStartUI();
        }
        webappPagesCollection.getPage(ContactListPage.class).openStartUI();
        Assert.assertTrue("Top People list is not shown", webappPagesCollection
                .getPage(StartUIPage.class).isTopPeopleLabelVisible());
    }

    /**
     * Selects users from Top People in People Picker
     *
     * @step. ^I select (.*) from Top People$
     *
     * @param namesOfTopPeople comma separated list of names of top people to select
     * @throws Exception
     */
    @When("^I select (.*) from Top People$")
    public void ISelectUsersFromTopPeople(String namesOfTopPeople)
            throws Exception {
        for (String alias : CommonSteps.splitAliases(namesOfTopPeople)) {
            final String userName = usrMgr.findUserByNameOrNameAlias(alias)
                    .getName();
            webappPagesCollection.getPage(StartUIPage.class)
                    .clickNameInTopPeople(userName);
        }
    }

    private static List<String> selectedTopPeople;

    public static List<String> getSelectedTopPeople() {
        return selectedTopPeople;
    }

    @When("^I remember user names selected in Top People$")
    public void IRememberUserNamesSelectedInTopPeople() throws Exception {
        selectedTopPeople = webappPagesCollection.getPage(StartUIPage.class).getNamesOfSelectedTopPeople();
    }

    /**
     * Verifies whether Search is opened on People Picker Page
     *
     * @step. I see Search is opened$
     *
     * @throws Exception
     */
    @Then("^I see Search is opened$")
    public void ISeeSearchIsOpened() throws Exception {
        final String searchMissingMessage = "Search is not visible on Start UI Page";
        Assert.assertTrue(searchMissingMessage,
                webappPagesCollection.getPage(StartUIPage.class)
                .isSearchOpened());
    }

    /**
     * Verify if group conversation is found by Search in People Picker
     *
     * @step. ^I( do not)? see group conversation (.*) found in People Picker$
     *
     * @param donot if null method returns true if found otherwise true if not found
     * @param name group conversation name string
     * @throws Exception
     */
    @When("^I( do not)? see group conversation (.*) found in People Picker$")
    public void ISeeGroupFoundInStartUI(String donot, String name)
            throws Exception {

        if (donot == null) {
            Assert.assertTrue(webappPagesCollection.getPage(StartUIPage.class).isGroupConversationFound(name));
        } else {
            Assert.assertTrue(webappPagesCollection.getPage(StartUIPage.class).isGroupConversationNotFound(name));
        }
    }

    /**
     * Verify if More button is shown in People Picker
     *
     * @step. ^I see (\\d+) people in Top people list$
     *
     */
    @Then("^I see (\\d+) people in Top people list$")
    public void ISeeXPeopleInTopPeopleList(int count) throws Exception {
        assertThat("people suggestions",
                webappPagesCollection.getPage(StartUIPage.class)
                .getNumberOfTopPeople(), equalTo(count));
    }
    
    /**
     * Click on import contacts from address book via search UI
     *
     * @throws Exception
     */
    @When("^I click button to import contacts from address book via search UI")
    public void IClickAddressbookImportButton() throws Exception {
        WebappPagesCollection.getInstance().getPage(StartUIPage.class).clickImportAddressbookButton();
    }
}
