package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.AddressBookPermissionPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AddressBookPermissionPageSteps {

    private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
            .getInstance();

    /**
     * Clicks "OK" button in address book permission dialog
     *
     * @step. ^I allow address book access$
     *
     * @throws Exception
     */
    @When("^I allow address book access$")
    public void IClickOk() throws Exception {
        osxPagesCollection.getPage(AddressBookPermissionPage.class).clickOk();
    }

    /**
     * Clicks "Don't Allow" button in address book permission dialog
     *
     * @step. ^I do not allow address book access$
     *
     * @throws Exception
     */
    @When("^I do not allow address book access$")
    public void IClickNo() throws Exception {
        osxPagesCollection.getPage(AddressBookPermissionPage.class).clickNo();
    }

    /**
     * Verifies whether the address book permission window is visible or not
     *
     * @step. ^I verify address book permission window is( not)? visible$
     * @param not is set to null if "not" part does not exist
     *
     * @throws Exception
     */
    @Then("^I verify address book permission window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("address book permission window not visible within timeout",
                    osxPagesCollection.getPage(AddressBookPermissionPage.class).isVisible());
        } else {
            boolean notVisible = osxPagesCollection.getPage(AddressBookPermissionPage.class)
                    .isNotVisible();
            assertTrue("address book permission window is visible", notVisible);
        }

    }

}
