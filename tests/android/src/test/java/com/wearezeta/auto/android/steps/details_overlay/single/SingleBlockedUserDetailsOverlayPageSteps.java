package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.details_overlay.single.SingleBlockedUserDetailsOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleBlockedUserDetailsOverlayPageSteps {
    private SingleBlockedUserDetailsOverlayPage getSingleBlockedUserDetailsOverlayPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(SingleBlockedUserDetailsOverlayPage.class);
    }

    /**
     * Tap on buttons on Single blocked user details page
     *
     * @param name button name
     * @throws Exception
     * @step. ^I tap on (unblock) button on Single blocked user details page$
     */
    @When("^I tap on (unblock) button on Single blocked user details page$")
    public void ITapButton(String name) throws Exception {
        getSingleBlockedUserDetailsOverlayPage().tapButton(name);
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
