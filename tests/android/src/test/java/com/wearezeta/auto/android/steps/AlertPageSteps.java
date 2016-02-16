package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AboutPage;
import com.wearezeta.auto.android.pages.AlertPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.function.Function;
import static org.junit.Assert.assertEquals;

public class AlertPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final Function<String, String> UNVERIFIED_CONVERSATION_PAGE_HEADER_TEXT = username -> String.format("%s started using a new device.", username);

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
     * Confirms presence of alert page header and optionally checks for text content
     *
     * @step. ^I see [Aa]lert page header(?: is \"?(.*)\"?)$
     *
     * @throws Exception
     *
     */
    @When("^I see [Aa]lert page header(?: is \"?(.*)\"?)$")
    public void ISeeAlertPageHeader(String pageHeader) throws Exception {
        if (pageHeader != null) {
            getAlertPage().getHeaderText().equals(pageHeader);
        } else {
            getAlertPage().getHeaderText();
        }
    }

    /**
     * Confirms presence of unverified conversation alert page header caused by given user
     *
     * @step. ^I see unverified conversation [Aa]lert page header caused by user (.*)$
     *
     * @throws Exception
     *
     */
    @When("^I see unverified conversation [Aa]lert page header caused by user (.*)$")
    public void ISeeUnverifiedConversationAlertPageHeaderCausedByUser(String userCause) throws Exception {
        userCause = usrMgr.findUserByNameOrNameAlias(userCause)
                .getName();
        assertEquals("Header does not match expected", UNVERIFIED_CONVERSATION_PAGE_HEADER_TEXT.apply(userCause), getAlertPage().getHeaderText());
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
