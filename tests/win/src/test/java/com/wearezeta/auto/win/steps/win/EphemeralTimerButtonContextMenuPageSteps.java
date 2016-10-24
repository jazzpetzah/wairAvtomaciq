package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.win.pages.win.EphemeralTimerButtonContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import cucumber.api.java.en.When;

public class EphemeralTimerButtonContextMenuPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();

    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        winPagesCollection.getPage(EphemeralTimerButtonContextMenuPage.class).setEphemeralTimer(label.toUpperCase());
    }
}
