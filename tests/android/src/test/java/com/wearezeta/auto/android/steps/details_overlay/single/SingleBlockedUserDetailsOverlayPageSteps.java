package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.single.SingleBlockedUserDetailsOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.jcodec.common.Assert;

public class SingleBlockedUserDetailsOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private SingleBlockedUserDetailsOverlayPage getSingleBlockedUserDetailsOverlayPage() throws Exception {
        return pagesCollection.getPage(SingleBlockedUserDetailsOverlayPage.class);
    }

    /**
     * Verify the button is visible
     *
     * @param shouldNotSee equals null means the button should be visible
     * @param name         button name
     * @throws Exception
     * @step. ^I( do not)? see (unblock) button on Single blocked user details page
     */
    @Then("^I( do not)? see (unblock) button on Single blocked user details page$")
    public void ISeeButton(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The button '%s' is invisible", name),
                    getSingleBlockedUserDetailsOverlayPage().waitUntilButtonVisible(name));
        } else {
            Assert.assertTrue(String.format("The button '%s' is still visible", name),
                    getSingleBlockedUserDetailsOverlayPage().waitUntilButtonInvisible(name));
        }
    }
}
