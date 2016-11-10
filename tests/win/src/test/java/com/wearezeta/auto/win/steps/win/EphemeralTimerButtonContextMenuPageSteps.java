package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.win.common.WrapperTestContext;
import com.wearezeta.auto.win.pages.win.EphemeralTimerButtonContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();
    
    private final WrapperTestContext context;

    public EphemeralTimerButtonContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public EphemeralTimerButtonContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        winPagesCollection.getPage(EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
