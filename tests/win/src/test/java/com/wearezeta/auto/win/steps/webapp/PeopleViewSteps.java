package com.wearezeta.auto.win.steps.webapp;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.win.pages.webapp.GroupPeoplePopoverPage;
import com.wearezeta.auto.win.pages.webapp.SingleUserPeoplePopoverPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeopleViewSteps {

    private final WrapperTestContext context;

    public PeopleViewSteps() {
        this.context = new WrapperTestContext();
    }

    public PeopleViewSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I( do not)? see Single User Profile popover$")
    public void ISeeSingleUserPopup(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            context.getWebappPagesCollection().getPage(SingleUserPeoplePopoverPage.class).waitUntilVisibleOrThrowException();
        } else {
            context.getWebappPagesCollection().getPage(SingleUserPeoplePopoverPage.class).waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I see username (.*) on Single User Profile popover$")
    public void IseeUserNameOnUserProfilePage(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertEquals(name,
                context.getWebappPagesCollection()
                        .getPage(SingleUserPeoplePopoverPage.class)
                        .getUserName());
    }

    @When("^I( do not)? see Group Participants popover$")
    public void ISeeUserProfilePopupPage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).waitUntilVisibleOrThrowException();
        } else {
            context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I input user name (.*) in search field on Group Participants popover$")
    public void ISearchForUser(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).searchForUser(name);
    }

    @When("^I select user (.*) from Group Participants popover search results$")
    public void ISelectUserFromSearchResults(String user) throws Exception {
        user = context.getUserManager().replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).selectUserFromSearchResult(user);
    }

    @When("^I see Add to conversation button on Single User popover$")
    public void ISeeAddToConversationButton() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).
                isAddToConversationButtonShown());
    }

    @Then("^I see conversation title (.*) on Group Participants popover$")
    public void ISeeConversationTitle(String title) throws Exception {
        Assert.assertEquals(title, context.getWebappPagesCollection().getPage(GroupPeoplePopoverPage.class).
                getConversationTitle());
    }
}
