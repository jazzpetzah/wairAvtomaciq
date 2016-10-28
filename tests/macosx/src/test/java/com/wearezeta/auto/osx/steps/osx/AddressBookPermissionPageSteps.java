package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.osx.pages.osx.AddressBookPermissionPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AddressBookPermissionPageSteps {

    private final WrapperTestContext context;

    public AddressBookPermissionPageSteps() {
        this.context = new WrapperTestContext();
    }

    public AddressBookPermissionPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I allow address book access$")
    public void IClickOk() throws Exception {
        context.getOSXPagesCollection().getPage(AddressBookPermissionPage.class).clickOk();
    }

    @When("^I do not allow address book access$")
    public void IClickNo() throws Exception {
        context.getOSXPagesCollection().getPage(AddressBookPermissionPage.class).clickNo();
    }

    @Then("^I verify address book permission window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("address book permission window not visible within timeout",
                    context.getOSXPagesCollection().getPage(AddressBookPermissionPage.class).isVisible());
        } else {
            boolean notVisible = context.getOSXPagesCollection().getPage(AddressBookPermissionPage.class)
                    .isNotVisible();
            assertTrue("address book permission window is visible", notVisible);
        }
    }
}
