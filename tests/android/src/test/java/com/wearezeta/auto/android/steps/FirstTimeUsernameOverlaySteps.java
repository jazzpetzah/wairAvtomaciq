package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.ConversationsListPage;
import com.wearezeta.auto.android.pages.FirstTimeUsernameOverlay;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class FirstTimeUsernameOverlaySteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private FirstTimeUsernameOverlay getFirstTimeUsernameOverlay() throws Exception {
        return pagesCollection.getPage(FirstTimeUsernameOverlay.class);
    }

    private ConversationsListPage getConversationsListPage() throws Exception {
        return pagesCollection.getPage(ConversationsListPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Confirms the Unique Username Takeover page is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I( do not)? see Unique Username Takeover page$"
     */
    @Then("^I( do not)? see Unique Username Takeover page$")
    public void ThenISeeFirstTimeOverlay(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Unique Username Takeover page is not visible after timeout",
                    getFirstTimeUsernameOverlay().isVisible());
        } else {
            Assert.assertTrue("Unique Username Takeover page is still visible after timeout",
                    getFirstTimeUsernameOverlay().isInvisible());
        }
    }

    /**
     * Tap Keep this one button or Choose your own button on Unique Username Takeover page
     *
     * @param buttonName "Keep This One" or "Choose Your Own"
     * @throws Exception
     * @step. ^I tap Keep This One button on Unique Username Takeover page$
     */
    @When("^I tap (Keep This One|Choose Your Own) button on Unique Username Takeover page$")
    public void ITapKeepThisOneButton(String buttonName) throws Exception {
        if (buttonName.equals("Keep This One")) {
            getFirstTimeUsernameOverlay().tapKeepThisOneButton();
        } else {
            getFirstTimeUsernameOverlay().tapChooseYourOwnButton();
        }

    }

    /**
     * Accept the Unique Username Takeover page as soon as it is visible
     *
     * @throws Exception
     * @step. ^I tap Keep This One on Unique Username Takeover page as soon as it is visible$
     */
    @And("^I tap Keep This One on Unique Username Takeover page as soon as it is visible$")
    public void IAcceptTheOverLayWhenItIsVisible() throws Exception {
        getFirstTimeUsernameOverlay().tapKeepThisOneIfVisible(CommonAndroidSteps.FIRST_TIME_OVERLAY_TIMEOUT);
    }

    /**
     * Confirms the fact, if username is visible on Unique Username Takeover page or not
     *
     * @param shouldNotBeVisible null if should be visible. Otherwise - any string
     * @throws Exception
     * @step. ^I see username "(.*)" on Unique Username Takeover page$
     */
    @Then("^I( do not)? see username on Unique Username Takeover page$")
    public void ISeeUsername(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("There is no username on Unique Username Takeover page"),
                    getFirstTimeUsernameOverlay().isOfferedUsernameVisible());
        } else {
            Assert.assertTrue(String.format("There is username on Unique Username Takeover page"),
                    getFirstTimeUsernameOverlay().isOfferedUsernameInvisible());
        }
    }
}