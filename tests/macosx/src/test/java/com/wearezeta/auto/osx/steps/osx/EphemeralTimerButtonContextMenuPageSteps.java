package com.wearezeta.auto.osx.steps.osx;

import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.osx.pages.osx.EphemeralTimerButtonContextMenuPage;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final WrapperTestContext context;

    public EphemeralTimerButtonContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public EphemeralTimerButtonContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }
    
    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        context.getOSXPagesCollection().getPage(EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
