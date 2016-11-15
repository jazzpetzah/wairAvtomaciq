package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.win.EphemeralTimerButtonContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public EphemeralTimerButtonContextMenuPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
