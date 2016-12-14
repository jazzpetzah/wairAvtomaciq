package com.wearezeta.auto.android_tablet.steps.details_overlay.group;


import com.wearezeta.auto.android_tablet.pages.details_overlay.group.TabletGroupBlockedUserDetailsOverlayPage;
import com.wearezeta.auto.android_tablet.steps.AndroidTabletPagesCollection;
import cucumber.api.java.en.When;

public class GroupBlockedUserDetailsOverlayPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletGroupBlockedUserDetailsOverlayPage getGroupBlockedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(TabletGroupBlockedUserDetailsOverlayPage.class);
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
