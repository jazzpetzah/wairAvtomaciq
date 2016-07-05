package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.VoiceFiltersOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class VoiceFiltersOverlayPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private VoiceFiltersOverlayPage getVoiceFiltersOverlayPage() throws Exception {
        return pagesCollection.getPage(VoiceFiltersOverlayPage.class);
    }

    /**
     * Tap on buttons on voice filters overlay
     *
     * @param buttonName the button name within voice filters overlay
     * @step. ^I tap (Start Record|Stop Record|Approve) button on Voice filters overlay$
     */
    @When("^I tap (Start Record|Stop Record|Approve) button on Voice filters overlay$")
    public void ITapOnVoiceFiltersOverlayButton(String buttonName) throws Exception {
        getVoiceFiltersOverlayPage().tapButton(buttonName);
    }

    /**
     * Verify the voice recording dialog is visible
     *
     * @throws Exception
     * @step. ^I see Voice filters overlay$
     */
    @Then("^I see Voice filters overlay$")
    public void ISeeVoiceRecordingDialog() throws Exception {
        Assert.assertTrue("The voice recording dialog should be visible", getVoiceFiltersOverlayPage().isVoiceRecordingDialogVisible());
    }
}
