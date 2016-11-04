package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.PendingOutgoingConnectionPage;

import cucumber.api.java.en.When;

public class OutgoingPendingConnectionPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private PendingOutgoingConnectionPage getPendingOutgoingConnectionPage() throws Exception {
        return pagesCollection.getPage(PendingOutgoingConnectionPage.class);
    }

    /**
     * Verify visibility of pending outgoing connection
     *
     * @param shouldNotBeVisible equals to null if the view should be visible
     * @throws Exception
     * @step. ^I (do not )?see Pending outgoing connection page$
     */
    @When("^I (do not )?see Pending outgoing connection page$")
    public void ISeeView(String shouldNotBeVisible) throws Exception {
        boolean condition;
        if (shouldNotBeVisible == null) {
            condition = getPendingOutgoingConnectionPage().isButtonVisible("Connect");
        } else {
            condition = getPendingOutgoingConnectionPage().isButtonInvisible("Connect");
        }
        Assert.assertTrue(String.format("Pending outgoing connection view is expected to be %s",
                (shouldNotBeVisible == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Tap the corresponding button on Pending outgoing connection page
     *
     * @param btnName one of possible button names
     * @throws Exception
     * @step. ^I tap (Cancel Request|Connect) button on Pending outgoing connection page$
     */
    @When("^I tap (Cancel Request|Connect) button on Pending outgoing connection page$")
    public void ITapButton(String btnName) throws Exception {
        getPendingOutgoingConnectionPage().tapButton(btnName);
    }

    /**
     * Verify button visibility on Pending outgoing connection pag
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (Cancel Request|Connect) button on Pending outgoing connection page$
     */
    @When("^I (do not )?see (Cancel Request|Connect) button on Pending outgoing connection page$")
    public void ITapButton(String shouldNotSee, String btnName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("the button '%s' is expected to be visible", btnName),
                    getPendingOutgoingConnectionPage().isButtonVisible(btnName));
        } else {
            Assert.assertTrue(String.format("the button '%s' is expected to be invisible", btnName),
                    getPendingOutgoingConnectionPage().isButtonInvisible(btnName));
        }
    }
}
