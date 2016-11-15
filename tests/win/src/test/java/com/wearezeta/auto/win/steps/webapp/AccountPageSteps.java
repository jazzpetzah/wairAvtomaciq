package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.AccountPage;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AccountPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AccountPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    
    @When("^I do not see logout in account preferences$")
    public void IDoNotSeeLogout() throws Exception {
        assertTrue("Logout should NOT be visible", webContext.getPagesCollection().getPage(AccountPage.class).
                isLogoutInvisible());
    }

}
