package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.AddressBookPermissionPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AddressBookPermissionPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AddressBookPermissionPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I allow address book access$")
    public void IClickOk() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).clickOk();
    }

    @When("^I do not allow address book access$")
    public void IClickNo() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).clickNo();
    }

    @Then("^I verify address book permission window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("address book permission window not visible within timeout",
                    wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).isVisible());
        } else {
            boolean notVisible = wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class)
                    .isNotVisible();
            assertTrue("address book permission window is visible", notVisible);
        }
    }
}
