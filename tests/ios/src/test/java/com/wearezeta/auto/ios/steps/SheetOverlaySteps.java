package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.SheetOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SheetOverlaySteps {
    private SheetOverlay getSheetOverlay() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(SheetOverlay.class);
    }

    /**
     * Tap the corresponding button on Sheet overlay
     *
     * @param name one of available button names
     * @throws Exception
     * @step. ^I tap (Cancel) button on Sheet overlay$
     */
    @When("^I tap (Cancel) button on Sheet overlay$")
    public void ITapButton(String name) throws Exception {
        getSheetOverlay().tapButton(name);
    }

    /**
     * Verify whether currently visible sheet contains particular text
     *
     * @param expectedText
     * @throws Exception
     * @step. ^I see sheet contains text (.*)
     */
    @Then("^I see sheet contains text (.*)")
    public void ISeeSheetContainsTextFlightNumber(String expectedText) throws Exception {
        Assert.assertTrue(String.format("There is no sheet containing text '%s'", expectedText),
                getSheetOverlay().isSheetContainingTextVisible(expectedText));
    }
}
