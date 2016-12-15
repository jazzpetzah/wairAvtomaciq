package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PendingConnectionsPage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class PendingConnectionsPageSteps {

    private final TestContext context;

    public PendingConnectionsPageSteps(TestContext context) {
        this.context = context;
    }

    @Then("^I see unique username in connection request from user (.*)$")
    public void ICanSeeUniqueUsernameFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        // username given. strict check for username
        String uniqueUsername = user.getUniqueUsername();
        assertThat(
                context.getPagesCollection().getPage(PendingConnectionsPage.class)
                        .getUniqueUsernameByName(user.getId()).toLowerCase(),
                equalTo(uniqueUsername));
    }

    @Then("^I do not see mail in connection request from user (.*)$")
    public void ICanSeeEmailFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        String email = user.getEmail();
            assertThat(
                    context.getPagesCollection().getPage(PendingConnectionsPage.class)
                            .getAllTextOfRequestById(user.getId()).toLowerCase(),
                    not(containsString(email)));
    }

    @Then("^I see connection message \"(.*)\" in connection request from user (.*)$")
    public void ISeeConnectionMessageFromUser(String message, String user)
            throws Exception {
        user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getPagesCollection()
                .getPage(PendingConnectionsPage.class).getMessageByName(user)
                .equals(message));

    }

    @Then("^I see avatar in connection request from user (.*)$")
    public void ISeeAvatarFromUser(String nameAlias) throws Exception {
        // user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(nameAlias);
        Assert.assertTrue(context.getPagesCollection().getPage(
                PendingConnectionsPage.class).isAvatarByIdVisible(user.getId()));

    }

    @Then("^I see accept button in connection request from user (.*)$")
    public void ISeeAcceptButtonConnectionFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getPagesCollection().getPage(
                PendingConnectionsPage.class)
                .isAcceptRequestButtonForUserVisible(user.getId()));
    }

    @Then("^I see ignore button in connection request from user (.*)$")
    public void ISeeIgnoreButtonConnectionFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        Assert.assertTrue(context.getPagesCollection().getPage(
                PendingConnectionsPage.class)
                .isIgnoreRequestButtonForUserVisible(user.getId()));
    }

    @Then("^I see correct color for accept button in connection request from user (.*)$")
    public void ISeeCorrectColorForAcceptButtonConnectionFromUser(
            String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        AccentColor accentColor = context.getUserManager().getSelfUserOrThrowError().getAccentColor();
        assertThat(context.getPagesCollection().getPage(PendingConnectionsPage.class)
                .getAcceptRequestButtonBgColor(user.getId()),
                equalTo(accentColor));
    }

    @Then("^I see correct color for ignore button in connection request from user (.*)$")
    public void ISeeCorrectColorForIgnoreButtonConnectionFromUser(
            String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        AccentColor accentColor = context.getUserManager().getSelfUserOrThrowError().getAccentColor();
        Assert.assertTrue(context.getPagesCollection()
                .getPage(PendingConnectionsPage.class)
                .getIgnoreRequestButtonBorderColor(user.getId())
                .equals(accentColor));
    }

    @When("^I see an amount of (\\d+) avatars? in known connections in connection request from user (.*)$")
    public void ISeeXAvatarsInConnectionRequest(int amount, String nameAlias)
            throws Throwable {
        ClientUser user = context.getUserManager().findUserBy(nameAlias, FindBy.NAME_ALIAS);
        assertThat(context.getPagesCollection().getPage(PendingConnectionsPage.class)
                .getAmountOfKnownConnectionAvatars(user.getId()),
                equalTo(amount));
    }

    @When("^I see an amount of (\\d+) others in known connections in connection request from user (.*)$")
    public void ISeeXOthersInConnectionRequest(int amount, String nameAlias)
            throws Throwable {
        ClientUser user = context.getUserManager().findUserBy(nameAlias, FindBy.NAME_ALIAS);
        assertThat(context.getPagesCollection().getPage(PendingConnectionsPage.class)
                .getOthersTextOfKnownConnections(user.getId()), equalTo("+"
                + amount));
    }

    @When("^I accept connection request from user (.*)$")
    public void IAcceptConnectionRequestFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(PendingConnectionsPage.class)
                .acceptRequestFromUser(user.getId());
    }

    @When("^I ignore connection request from user (.*)$")
    public void IIgnoreConnectionRequestFromUser(String userAlias)
            throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(PendingConnectionsPage.class)
                .ignoreRequestFromUser(user.getId());
    }

    @Then("^I see (\\d+) common friends$")
    public void ISeeCommonFriends(int count) throws Exception {
        String commonFriends = context.getPagesCollection().getPage(PendingConnectionsPage.class).getCommonFriends();
        Assert.assertThat("Wrong amount of common friends", commonFriends, startsWith(Integer.toString(count)));
    }

}
