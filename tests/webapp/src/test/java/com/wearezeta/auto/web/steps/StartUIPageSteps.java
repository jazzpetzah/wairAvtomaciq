package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.List;

import com.wearezeta.auto.web.pages.ConversationPage;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.StartUIPage;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class StartUIPageSteps {
    
    private static final Logger LOG = ZetaLogger.getLog(StartUIPageSteps.class.getSimpleName());

    private final TestContext context;

    private static String rememberedUser;

    public StartUIPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I select( group)? (.*) from People Picker results$")
    public void ISelectUserFromStartUIResults(String group, String user)
            throws Exception {
        if (group == null) {
            user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
            context.getPagesCollection().getPage(StartUIPage.class)
                    .selectUserFromSearchResult(user);
        } else {
            context.getPagesCollection().getPage(StartUIPage.class)
                    .selectGroupFromSearchResult(user);
        }
    }

    @When("^I wait for the search field of People Picker to be empty$")
    public void IWaitForSearchFieldToBeEmpty() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .waitForSearchFieldToBeEmpty();
    }

    @When("^I type (.*) in search field of People Picker$")
    public void ISearchForUser(String nameOrEmailOrUniqueUsername) throws Exception {
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.NAME_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.EMAIL_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.UNIQUE_USERNAME_ALIAS);
        // adding spaces to ensure trimming of input
        context.getPagesCollection().getPage(StartUIPage.class).searchForUser(" " + nameOrEmailOrUniqueUsername + " ");
    }

    @When("^I type (.*) in search field of People Picker in uppercase$")
    public void ISearchForUserInUppercase(String nameOrEmailOrUniqueUsername) throws Exception {
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.NAME_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.EMAIL_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.UNIQUE_USERNAME_ALIAS);
        // adding spaces to ensure trimming of input
        context.getPagesCollection().getPage(StartUIPage.class).searchForUser(" " + nameOrEmailOrUniqueUsername.toUpperCase() + " ");
    }

    @When("^I type (.*) in search field of People Picker only partially$")
    public void ISearchForUserOnlyPartially(String nameOrEmailOrUniqueUsername) throws Exception {
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.NAME_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.EMAIL_ALIAS);
        nameOrEmailOrUniqueUsername = context.getUserManager().replaceAliasesOccurences(nameOrEmailOrUniqueUsername,
                FindBy.UNIQUE_USERNAME_ALIAS);
        // adding spaces to ensure trimming of input
        context.getPagesCollection().getPage(StartUIPage.class).searchForUser(" " + nameOrEmailOrUniqueUsername.substring(0,
                nameOrEmailOrUniqueUsername.length() - 2) + " ");
    }

    @When("^I clear the search field of People Picker$")
    public void IClearSearch() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class).clearSearch();
    }

    @When("^I( do not)? see user (.*) found in People Picker$")
    public void ISeeUserFoundInStartUI(String donot, String name) throws Exception {
        LOG.debug("NAME: "+name);
        name = context.getUserManager().findUserByNameOrNameAlias(name).getName();
        LOG.debug("MODIFIED NAME: "+name);
        if (donot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(name));
        } else {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(name));
        }
    }

    @When("^I( do not)? see user (.*) with username (.*) found in People Picker$")
    public void ISeeUserFoundInStartUI(String donot, String name, String uniqueUsername) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        uniqueUsername = context.getUserManager().replaceAliasesOccurences(uniqueUsername, FindBy.UNIQUE_USERNAME_ALIAS);

        if (donot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(name, uniqueUsername));
        } else {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(name, uniqueUsername));
        }
    }
    
    @When("^I( do not)? see user (.*) with (\\d+) common friends found in People Picker$")
    public void ISeeUserFoundWithFriendsInStartUI(String donot, String name, int friends) throws Exception {
        name = context.getUserManager().findUserByNameOrNameAlias(name).getName();
        if (donot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(name, friends));
        } else {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(name, friends));
        }
    }

    @When("^I remove user (.*) from suggestions in People Picker$")
    public void IClickRemoveButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickRemoveButtonOnSuggestion(contact);
    }

    @When("^I make a connection request for user (.*) directly from People Picker$")
    public void IClickPlusButton(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickPlusButtonOnSuggestion(contact);
    }

    @When("^I close People Picker$")
    public void ICloseStartUI() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class).closeStartUI();
    }

    @When("^I click on (not connected|pending) user (.*) found in People Picker$")
    public void IClickNotConnecteUserFoundInStartUI(String userType, String name) throws Exception {
        name = context.getUserManager().findUserByNameOrNameAlias(name).getName();
        if (userType.equalsIgnoreCase("not connected")) {
            context.getPagesCollection().getPage(StartUIPage.class)
                    .clickNotConnectedUserName(name);
        } else if (userType.equalsIgnoreCase("pending")) {
            context.getPagesCollection().getPage(StartUIPage.class)
                    .clickPendingUserName(name);
        }
    }

    @When("^I click on remembered (not connected|pending) contact found in People Picker$")
    public void IClickRememberedNotConnecteUserFoundInStartUI(String userType) throws Exception {
        rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
        if (userType.equalsIgnoreCase("not connected")) {
            context.getPagesCollection().getPage(StartUIPage.class)
                    .clickNotConnectedUserName(rememberedUser);
        } else if (userType.equalsIgnoreCase("pending")) {
            context.getPagesCollection().getPage(StartUIPage.class)
                    .clickPendingUserName(rememberedUser);
        }
    }

    @When("^I choose to create conversation from People Picker$")
    public void IChooseToCreateConversationFromStartUI() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .createConversation();
    }

    @Then("^I see more than (\\d+) suggestions? in people picker$")
    public void ISeeMoreThanXSuggestionsInStartUI(int count)
            throws Exception {
        assertThat("people suggestions",
                context.getPagesCollection().getPage(StartUIPage.class)
                        .getNumberOfSuggestions(), greaterThan(count));
    }

    @When("^I see Bring Your Friends or Invite People button$")
    public void ISeeSendInvitationButton() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .waitUntilBringYourFriendsOrInvitePeopleButtonIsVisible();
    }

    @When("^I do not see Gmail Import button on People Picker page$")
    public void IDoNotSeeGmailImportButton() throws Exception {
        context.getPagesCollection().getPage(BringYourFriendsPopoverPage.class)
                .waitUntilGmailImportButtonIsNotVisible();
    }

    @And("^I click button to bring friends from Gmail$")
    public void IClickButtonToBringFriendsFromGmail() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickBringFriendsFromGmailButton();
    }

    @And("^I see Google login popup$")
    public void ISeeGoogleLoginPopup() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .switchToGooglePopup();
    }

    @When("^I sign up at Google with email (.*) and password (.*)$")
    public void ISignUpAtGoogleWithEmail(String email, String password) throws Exception {
        GoogleLoginPage googleLoginPage = context.getPagesCollection()
                .getPage(GoogleLoginPage.class);
        // sometimes Google already shows the email
        googleLoginPage.setEmail(email);
        // sometimes google shows a next button and you have to enter the
        // password separately
        if (googleLoginPage.hasNextButton()) {
            googleLoginPage.clickNext();
        }
        googleLoginPage.setPassword(password);
        googleLoginPage.clickSignInWithWindowSwitch();
    }

    @When("^I click Bring Your Friends or Invite People button$")
    public void IClickBringYourFriendsOrInvitePeopleButton() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickBringYourFriendsOrInvitePeopleButton();
    }

    @When("^I click Call button on People Picker page$")
    public void IClickCallButton() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class).clickCallButton();
    }

    @When("^I wait till Top People list appears$")
    public void IwaitTillTopPeopleListAppears() throws Exception {
        Assert.assertTrue("Top People list is NOT shown", context.getPagesCollection()
                .getPage(StartUIPage.class).isTopPeopleLabelVisible());
    }

    @When("^I select (.*) from Top People$")
    public void ISelectUsersFromTopPeople(String namesOfTopPeople) throws Exception {
        for (String alias : context.getUserManager().splitAliases(namesOfTopPeople)) {
            final String userName = context.getUserManager().findUserByNameOrNameAlias(alias).getName();
            context.getPagesCollection().getPage(StartUIPage.class).clickNameInTopPeople(userName);
        }
    }

    private static List<String> selectedTopPeople;

    public static List<String> getSelectedTopPeople() {
        return selectedTopPeople;
    }

    @When("^I remember user names selected in Top People$")
    public void IRememberUserNamesSelectedInTopPeople() throws Exception {
        selectedTopPeople = context.getPagesCollection().getPage(StartUIPage.class).getNamesOfSelectedTopPeople();
    }

    @When("^I remember first suggested user$")
    public void IRememberSuggestedUser() throws Exception {
        List<String> suggestedUsers = context.getPagesCollection().getPage(StartUIPage.class).getNamesOfSuggestedContacts();
        rememberedUser = suggestedUsers.get(0);
    }

    @When("^I( do not)? see (.*) remembered user in People Picker$")
    public void ISeeRememberedUserInStartUI(String donot, String count) throws Exception {
        if (donot != null && count.contains("first")) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserNotFound(rememberedUser));
        } else if (count.contains("first")) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isUserFound(rememberedUser));
        }
    }

    @When("^I remove first remembered user from suggestions in People Picker$")
    public void IRemoveFirstRememberedUser() throws Exception {
        rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickRemoveButtonOnSuggestion(rememberedUser);
    }

    @When("^I make a connection request for remembered user directly from People Picker$")
    public void IMakeAConnectionRequestForSecondRememberedUser() throws Exception {
        rememberedUser = context.getUserManager().replaceAliasesOccurences(rememberedUser, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(StartUIPage.class)
                .clickPlusButtonOnSuggestion(rememberedUser);
    }

    @When("^I( do not)? see Contact list with remembered user$")
    public void ISeeContactListWithSecondRememberedUser(String donot) throws Exception {
        if (donot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(ContactListPage.class)
                    .isConvoListEntryWithNameExist(rememberedUser));
        } else {
            Assert.assertFalse(context.getPagesCollection().getPage(ContactListPage.class)
                    .isConvoListEntryWithNameExist(rememberedUser));
        }
    }

    @When("^I open remembered users conversation$")
    public void IOpenSecondRememberedUsersConversation() throws Exception {
        ContactListPage contactListPage = context.getPagesCollection().getPage(ContactListPage.class);
        Assert.assertTrue(String.format("Conversation with name '%s' is not visible", rememberedUser),
                contactListPage.isConversationVisible(rememberedUser));
        contactListPage.openConversation(rememberedUser);
        Assert.assertTrue(String.format("Conversation '%s' should be selected", rememberedUser),
                contactListPage.isConversationSelected(rememberedUser));
    }

    @When("^I see connecting message in conversation with remembered contact$")
    public void ISeeConnectingMsgFromSecondRememberedUser() throws Exception {
        assertThat("User name", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageUser(),
                equalTo(rememberedUser));
        assertThat("Label", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageLabel(),
                equalTo("CONNECTING"));
    }

    @Then("^I see Search is opened$")
    public void ISeeSearchIsOpened() throws Exception {
        final String searchMissingMessage = "Search is not visible on Start UI Page";
        Assert.assertTrue(searchMissingMessage,
                context.getPagesCollection().getPage(StartUIPage.class)
                        .isSearchOpened());
    }

    @When("^I( do not)? see group conversation (.*) found in People Picker$")
    public void ISeeGroupFoundInStartUI(String donot, String name)
            throws Exception {

        if (donot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isGroupConversationFound(name));
        } else {
            Assert.assertTrue(context.getPagesCollection().getPage(StartUIPage.class).isGroupConversationNotFound(name));
        }
    }

    @Then("^I see (\\d+) people in Top people list$")
    public void ISeeXPeopleInTopPeopleList(int count) throws Exception {
        assertThat("people suggestions",
                context.getPagesCollection().getPage(StartUIPage.class)
                        .getNumberOfTopPeople(), equalTo(count));
    }

    @When("^I click Video Call button on People Picker page$")
    public void IClickVideoCallButton() throws Exception {
        context.getPagesCollection().getPage(StartUIPage.class).clickVideoCallButton();
    }

    @And("^I( do not)? see Video Call button on People Picker page$")
    public void ISeeVideoCallButton(String doNot) throws Exception {
        if (doNot == null) {
            final String searchMissingMessage = "Video Call button is not shown on Start UI Page";
            Assert.assertTrue(searchMissingMessage,
                    context.getPagesCollection().getPage(StartUIPage.class)
                            .isVideoCallButtonVisible());
        } else {
            final String searchMissingMessage = "Video Call button is shown on Start UI Page";
            Assert.assertTrue(searchMissingMessage,
                    context.getPagesCollection().getPage(StartUIPage.class)
                            .isVideoCallButtonNotVisible());
        }

    }
}
