package com.wearezeta.auto.android.steps.details_overlay.group;


import com.wearezeta.auto.android.pages.details_overlay.group.GroupBlockedUserDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.When;

public class GroupBlockedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private GroupBlockedUserDetailsOverlayPage getGroupBlockedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(GroupBlockedUserDetailsOverlayPage.class);
    }

    /**
     * Tap all buttons
     *
     * @param buttonName which could be +connect, -remove, connect
     * @throws Exception
     * @step ^I tap (blocked|remove|unblock|cancel) button on Group blocked user details page$
     */
    @When("^I tap (blocked|remove|unblock|cancel) button on Group blocked user details page$")
    public void ITapButton(String buttonName) throws Exception {
        getGroupBlockedUserDetailsOverlayPage().tapButton(buttonName);
    }
}
