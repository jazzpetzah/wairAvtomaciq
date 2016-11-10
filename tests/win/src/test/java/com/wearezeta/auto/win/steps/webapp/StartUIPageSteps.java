package com.wearezeta.auto.win.steps.webapp;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.StartUIPage;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.*;
import org.openqa.selenium.WebDriverException;
import static org.hamcrest.MatcherAssert.assertThat;

public class StartUIPageSteps {

    private final WrapperTestContext context;

    private static List<String> selectedTopPeople;

    public StartUIPageSteps() {
        this.context = new WrapperTestContext();
    }

    public StartUIPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I see [Pp]eople [Pp]icker$")
    public void ISeeStartUI() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).isVisible();
    }

    @When("^I select (.*) from People Picker results$")
    public void ISelectUserFromStartUIResults(String user) throws Exception {
        user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(StartUIPage.class).selectUserFromSearchResult(user);
    }

    @When("^I wait for the search field of People Picker to be empty$")
    public void IWaitForSearchFieldToBeEmpty() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).waitForSearchFieldToBeEmpty();
    }

    @When("^I type (.*) in search field of People Picker$")
    public void ISearchForUser(String nameOrEmail) throws Exception {
        nameOrEmail = context.getUserManager().replaceAliasesOccurences(nameOrEmail, FindBy.NAME_ALIAS);
        nameOrEmail = context.getUserManager().replaceAliasesOccurences(nameOrEmail, FindBy.EMAIL_ALIAS);
        // adding spaces to ensure trimming of input
        context.getWebappPagesCollection().getPage(StartUIPage.class).searchForUser(" " + nameOrEmail + " ");
    }

    @When("^I( do not)? see user (.*) found in People Picker$")
    public void ISeeUserFoundInStartUI(String donot, String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (donot == null) {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(StartUIPage.class).isUserFound(name));
        } else {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(StartUIPage.class).isUserNotFound(name));
        }
    }

    @When("^I remove user (.*) from suggestions in People Picker$")
    public void IClickRemoveButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(StartUIPage.class).clickRemoveButtonOnSuggestion(contact);
    }

    @When("^I make a connection request for user (.*) directly from People Picker$")
    public void IClickPlusButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(StartUIPage.class).clickPlusButtonOnSuggestion(contact);
    }

    @When("^I close People Picker$")
    public void ICloseStartUI() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).closeStartUI();
    }

    @When("^I click on (not connected|pending) user (.*) found in People Picker$")
    public void IClickNotConnecteUserFoundInStartUI(String userType, String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (userType.equalsIgnoreCase("not connected")) {
            context.getWebappPagesCollection().getPage(StartUIPage.class).clickNotConnectedUserName(name);
        } else if (userType.equalsIgnoreCase("pending")) {
            context.getWebappPagesCollection().getPage(StartUIPage.class).clickPendingUserName(name);
        }
    }

    @When("^I choose to create conversation from People Picker$")
    public void IChooseToCreateConversationFromStartUI() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).createConversation();
    }

    @Then("^I see more than (\\d+) suggestions? in people picker$")
    public void ISeeMoreThanXSuggestionsInStartUI(int count) throws Exception {
        assertThat("people suggestions", context.getWebappPagesCollection().getPage(StartUIPage.class).getNumberOfSuggestions(),
                greaterThan(count));
    }

    @When("^I see Bring Your Friends or Invite People button$")
    public void ISeeSendInvitationButton() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).waitUntilBringYourFriendsOrInvitePeopleButtonIsVisible();
    }

    @When("^I do not see Gmail Import button on People Picker page$")
    public void IDoNotSeeGmailImportButton() throws Exception {
        context.getWebappPagesCollection().getPage(BringYourFriendsPopoverPage.class).waitUntilGmailImportButtonIsNotVisible();
    }

    @And("^I click button to bring friends from Gmail$")
    public void IClickButtonToBringFriendsFromGmail() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).clickBringFriendsFromGmailButton();
    }

    @And("^I see Google login popup$")
    public void ISeeGoogleLoginPopup() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).switchToGooglePopup();
    }

    @When("^I sign up at Google with email (.*) and password (.*)$")
    public void ISignUpAtGoogleWithEmail(String email, String password) throws Exception {
        GoogleLoginPage googleLoginPage = context.getWebappPagesCollection().getPage(GoogleLoginPage.class);
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

    @When("^I click Bring Your Friends or Invite People button$")
    public void IClickBringYourFriendsOrInvitePeopleButton() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).clickBringYourFriendsOrInvitePeopleButton();
    }

    @When("^I click Call button on People Picker page$")
    public void IClickCallButton() throws Exception {
        context.getWebappPagesCollection().getPage(StartUIPage.class).clickCallButton();
    }

    @When("^I wait till Top People list appears$")
    public void IwaitTillTopPeopleListAppears() throws Exception {
        if (!context.getWebappPagesCollection().getPage(StartUIPage.class).isTopPeopleLabelVisible()) {
            context.getWebappPagesCollection().getPage(StartUIPage.class).closeStartUI();
        }
        context.getWebappPagesCollection().getPage(ContactListPage.class).openStartUI();
        Assert.assertTrue("Top People list is not shown", context.getWebappPagesCollection().getPage(StartUIPage.class).
                isTopPeopleLabelVisible());
    }

    @When("^I select (.*) from Top People$")
    public void ISelectUsersFromTopPeople(String namesOfTopPeople)
            throws Exception {
        for (String alias : CommonSteps.splitAliases(namesOfTopPeople)) {
            final String userName = context.getUserManager().findUserByNameOrNameAlias(alias)
                    .getName();
            context.getWebappPagesCollection().getPage(StartUIPage.class)
                    .clickNameInTopPeople(userName);
        }
    }

    @When("^I remember user names selected in Top People$")
    public void IRememberUserNamesSelectedInTopPeople() throws Exception {
        selectedTopPeople = context.getWebappPagesCollection().getPage(StartUIPage.class).getNamesOfSelectedTopPeople();
    }

    @Then("^I see Search is opened$")
    public void ISeeSearchIsOpened() throws Exception {
        final String searchMissingMessage = "Search is not visible on Start UI Page";
        Assert.assertTrue(searchMissingMessage, context.getWebappPagesCollection().getPage(StartUIPage.class).isSearchOpened());
    }

    @When("^I( do not)? see group conversation (.*) found in People Picker$")
    public void ISeeGroupFoundInStartUI(String donot, String name) throws Exception {
        if (donot == null) {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(StartUIPage.class).isGroupConversationFound(name));
        } else {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(StartUIPage.class).isGroupConversationNotFound(name));
        }
    }

    @Then("^I see (\\d+) people in Top people list$")
    public void ISeeXPeopleInTopPeopleList(int count) throws Exception {
        assertThat("people suggestions", context.getWebappPagesCollection().getPage(StartUIPage.class).getNumberOfTopPeople(),
                equalTo(count));
    }

    public static List<String> getSelectedTopPeople() {
        return selectedTopPeople;
    }
}
