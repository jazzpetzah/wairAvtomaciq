package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.win.pages.win.EphemeralTimerButtonContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final WebAppTestContext webContext;

    public EphemeralTimerButtonContextMenuPageSteps(WebAppTestContext webContext) {
        this.webContext = webContext;
    }

    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        webContext.getChildContext().getPagesCollection(WinPagesCollection.class).getPage(
                EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
