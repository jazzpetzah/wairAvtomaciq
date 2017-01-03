package com.wearezeta.auto.android.steps.details_overlay.common;

import com.wearezeta.auto.android.pages.details_overlay.common.ConfirmOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jcodec.common.Assert;

public class ConfirmOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ConfirmOverlayPage getConfirmOverlayPage() throws Exception {
        return pagesCollection.getPage(ConfirmOverlayPage.class);
    }

    /**
     * Tap the corresponding button
     *
     * @param itemName button name
     * @throws Exception
     * @step. ^I tap (DELETE|REMOVE|LEAVE|BLOCK|CANCEL) button on Confirm overlay page$
     */
    @When("^I tap (DELETE|REMOVE|LEAVE|BLOCK|CANCEL) button on Confirm overlay page$")
    public void ITapOptionsMenuItem(String itemName) throws Exception {
        getConfirmOverlayPage().tapOnButton(itemName);
    }

    /**
     * Tap on the checkbox
     *
     * @throws Exception
     * @step. ^I tap on (?:leave) checkbox on Confirm overlay page$
     */
    @When("^I tap on (?:leave) checkbox on Confirm overlay page$")
    public void ITapOnCheckbox() throws Exception {
        getConfirmOverlayPage().tapOnCheckbox();
    }

    /**
     * Check the checkbox visibility
     *
     * @param shouldNotSee equals null means the checkbox should be visible
     * @throws Exception
     * @step. ^I( do not)? see (?:leave) checkbox on Confirm overlay page$
     */
    @Then("^I( do not)? see (?:leave) checkbox on Confirm overlay page$")
    public void ISeeCheckbox(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The checkbox is invisible", getConfirmOverlayPage().waitUntilCheckboxVisible());
        } else {
            Assert.assertTrue("The checkbox is still visible", getConfirmOverlayPage().waitUntilCheckboxInvisible());
        }
    }

}
