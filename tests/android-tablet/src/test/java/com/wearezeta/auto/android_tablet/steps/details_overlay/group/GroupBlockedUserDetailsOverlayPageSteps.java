package com.wearezeta.auto.android_tablet.steps.details_overlay.group;


import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.details_overlay.group.TabletGroupBlockedUserDetailsOverlayPage;
import cucumber.api.java.en.When;

public class GroupBlockedUserDetailsOverlayPageSteps {
    private TabletGroupBlockedUserDetailsOverlayPage getGroupBlockedUserDetailsOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletGroupBlockedUserDetailsOverlayPage.class);
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
