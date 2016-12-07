package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.pages.osx.EphemeralTimerButtonContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final TestContext webContext;
    
    public EphemeralTimerButtonContextMenuPageSteps(TestContext webContext) {
        this.webContext = webContext;
    }
    
    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        webContext.getChildContext().getPagesCollection(OSXPagesCollection.class).getPage(EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
