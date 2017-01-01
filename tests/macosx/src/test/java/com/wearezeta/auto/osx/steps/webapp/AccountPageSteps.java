package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.AccountPage;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertTrue;

public class AccountPageSteps {

    private final WebAppTestContext webContext;

    public AccountPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }
    
    @When("^I do not see logout in account preferences$")
    public void IDoNotSeeLogout() throws Exception {
        assertTrue("Logout should NOT be visible", webContext.getPagesCollection().getPage(AccountPage.class).isLogoutInvisible());
    }

}
