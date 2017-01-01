package com.wearezeta.auto.android.steps.details_overlay.common;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.details_overlay.common.E2EEConfirmOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class E2EEConfirmOverlayPageSteps {
    private E2EEConfirmOverlayPage getE2EEConfirmOverlayPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(E2EEConfirmOverlayPage.class);
    }

    /**
     * Check if takeover screen appars for specified user and all content inside it is correct
     *
     * @throws Exception
     * @step. ^I see E2EE confirm overlay page from users? "(.*)"$
     */
    @When("^I see E2EE confirm overlay page from users? \"(.*)\"$")
    public void ISeeTakeoverScreen(String nameAliases) throws Exception {
        Assert.assertTrue("Takeover screeen text is not visible or not as expected",
                getE2EEConfirmOverlayPage().waitUntilE2EETextVisible());
        String name;
        final String headerText = getE2EEConfirmOverlayPage().getHeaderText();
        for (String nameAlias : AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(nameAliases)) {
            name = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(nameAlias).getName();
            Assert.assertTrue(String.format("Takeover header from user %s is not visible", name), headerText.contains(name));
        }
    }

    /**
     * Check if takeover screen is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I( do not)? see E2EE confirm overlay page$$
     */
    @Then("^I( do not)? see E2EE confirm overlay page$")
    public void IDoNotSeeTakeoverScreen(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("E2EE confirm overlay page is not visible after timeout",
                    getE2EEConfirmOverlayPage().waitUntilPageVisible());
        } else {
            Assert.assertTrue("E2EE confirm overlay page is still visible after timeout",
                    getE2EEConfirmOverlayPage().waitUntilPageInvisible());
        }
    }

    /**
     * Tap the corresponding button
     *
     * @param itemName button name
     * @throws Exception
     * @step. ^I tap (SHOW DEVICE|SHOW PEOPLE|SEND ANYWAY) button on E2EE confirm overlay page$
     */
    @When("^I tap (SHOW DEVICE|SHOW PEOPLE|SEND ANYWAY) button on E2EE confirm overlay page$")
    public void ITapOptionsMenuItem(String itemName) throws Exception {
        getE2EEConfirmOverlayPage().tapOnButton(itemName);
    }
}
