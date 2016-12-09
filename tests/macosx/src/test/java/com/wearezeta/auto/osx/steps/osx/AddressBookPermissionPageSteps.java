package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.AddressBookPermissionPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AddressBookPermissionPageSteps {

    private final TestContext webContext;

    public AddressBookPermissionPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I allow address book access$")
    public void IClickOk() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).clickOk();
    }

    @When("^I do not allow address book access$")
    public void IClickNo() throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).clickNo();
    }

    @Then("^I verify address book permission window is( not)? visible$")
    public void IVerifyAboutWindowIsVisible(String not) throws Exception {
        if (not == null) {
            assertTrue("address book permission window not visible within timeout",
                    webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class).isVisible());
        } else {
            boolean notVisible = webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(AddressBookPermissionPage.class)
                    .isNotVisible();
            assertTrue("address book permission window is visible", notVisible);
        }
    }
}
