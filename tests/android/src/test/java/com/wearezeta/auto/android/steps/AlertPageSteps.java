package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AboutPage;
import com.wearezeta.auto.android.pages.AlertPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AlertPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private AlertPage getAlertPage() throws Exception {
        return pagesCollection.getPage(AlertPage.class);
    }

    /**
     * Taps on the cancel button on alert page
     *
     * @step. ^I tap on cancel button on [Aa]lert page$
     *
     * @throws Exception
     *
     */
    @When("^I tap on cancel button on [Aa]lert page$")
    public void WhenITapOnCancel() throws Exception {
        getAlertPage().tapCancel();
    }

    /**
     * Taps on the positive button on alert page (positive option is the button on the right side)
     *
     * @step. ^I tap on positive button on [Aa]lert page$
     *
     * @throws Exception
     *
     */
    @When("^I tap on positive button on [Aa]lert page$")
    public void WhenITapOnPositive() throws Exception {
        getAlertPage().tapPositive();
    }

    /**
     * Taps on the negative button on alert page (negative option is the button on the left side)
     *
     * @step. ^I tap on negative button on [Aa]lert page$
     *
     * @throws Exception
     *
     */
    @When("^I tap on negative button on [Aa]lert page$")
    public void WhenITapOnNegative() throws Exception {
        getAlertPage().tapNegative();
    }

    /**
     * Confirms the alert page is visible or not
     *
     * @step. ^I( do not)? see [Aa]lert page$
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     *
     * @throws Exception
     *
     */
    @Then("^I( do not)? see [Aa]lert page$")
    public void ThenISeeAlertPage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Alert page is not visible after timeout",
                    getAlertPage().isVisible());
        } else {
            Assert.assertTrue("Alert page is still visible after timeout",
                    getAlertPage().isInvisible());
        }
    }
}
