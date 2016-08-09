package com.wearezeta.auto.osx.steps.webapp;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.webapp.GroupPeoplePopoverPage;
import com.wearezeta.auto.osx.pages.webapp.SingleUserPeoplePopoverPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeopleViewSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    /**
     * Verify that Single User Profile popover is visible or not
     *
     * @step. ^I( do not)? see Single User Profile popover$
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     *
     * @throws Exception
     *
     */
    @When("^I( do not)? see Single User Profile popover$")
    public void ISeeSingleUserPopup(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            webappPagesCollection.getPage(SingleUserPeoplePopoverPage.class)
                    .waitUntilVisibleOrThrowException();
        } else {
            webappPagesCollection.getPage(SingleUserPeoplePopoverPage.class)
                    .waitUntilNotVisibleOrThrowException();
        }
    }

    /**
     * Compares if name on Single User Profile popover is same as expected
     *
     * @throws java.lang.Exception
     * @step. ^I see username (.*) on Single User Profile popover$
     *
     * @param name user name string
     */
    @When("^I see username (.*) on Single User Profile popover$")
    public void IseeUserNameOnUserProfilePage(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        Assert.assertEquals(name,
                webappPagesCollection
                .getPage(SingleUserPeoplePopoverPage.class)
                .getUserName());
    }

    /**
     * Verify that Group Participants popover is shown or not
     *
     * @step. ^I( do not)? see Group Participants popover$
     *
     * @param shouldNotBeVisible is set to null if "do not" part does not exist
     *
     * @throws Exception
     *
     */
    @When("^I( do not)? see Group Participants popover$")
    public void ISeeUserProfilePopupPage(String shouldNotBeVisible)
            throws Exception {
        if (shouldNotBeVisible == null) {
            webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
                    .waitUntilVisibleOrThrowException();
        } else {
            webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
                    .waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I input user name (.*) in search field on Group Participants popover$")
    public void ISearchForUser(String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(GroupPeoplePopoverPage.class).searchForUser(name);
    }

    @When("^I select user (.*) from Group Participants popover search results$")
    public void ISelectUserFromSearchResults(String user) throws Exception {
        user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(GroupPeoplePopoverPage.class).selectUserFromSearchResult(user);
    }

    /**
     * Verifies there is a button to add people
     *
     * @step. ^I see Add to conversation button on Single User popover$
     * @throws Exception
     *
     */
    @When("^I see Add to conversation button on Single User popover$")
    public void ISeeAddToConversationButton() throws Exception {
        Assert.assertTrue(webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
                .isAddToConversationButtonShown());
    }

    /**
     * Verify conversation title on Group Participants popover
     *
     * @step. ^I see conversation title (.*) on Group Participants popover$
     *
     * @param title expected title string
     * @throws Exception
     */
    @Then("^I see conversation title (.*) on Group Participants popover$")
    public void ISeeConversationTitle(String title) throws Exception {
        Assert.assertEquals(title,
                webappPagesCollection.getPage(GroupPeoplePopoverPage.class)
                .getConversationTitle());
    }
}
