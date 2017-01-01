package com.wearezeta.auto.android.steps.cursor;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.cursor.VoiceFiltersOverlayPage;
import com.wearezeta.auto.android.pages.AndroidPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class VoiceFiltersOverlayPageSteps {
    private VoiceFiltersOverlayPage getVoiceFiltersOverlayPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext()
                .getPagesCollection().getPage(VoiceFiltersOverlayPage.class);
    }

    /**
     * Tap on buttons on voice filters overlay
     *
     * @param buttonName the button name within voice filters overlay
     * @step. ^I tap (Start Record|Stop Record|Approve|the \d+(?:st|nd|rd|th) button on Voice filters overlay$
     */
    @When("^I tap (Start Record|Stop Record|Approve|the \\d+(?:st|nd|rd|th) Filter) button on Voice filters overlay$")
    public void ITapOnVoiceFiltersOverlayButton(String buttonName) throws Exception {
        if (buttonName.startsWith("the")) {
            int index = Integer.parseInt(buttonName.replaceAll("[\\D]", ""));
            getVoiceFiltersOverlayPage().tapFilterButton(index);
        } else {
            getVoiceFiltersOverlayPage().tapButton(buttonName);
        }
    }

    /**
     * Verify the voice recording/filters dialog is visible
     *
     * @param overlayName overlay name
     * @throws Exception
     * @step. ^I see Voice (recording|filters) overlay$
     */
    @Then("^I see Voice (recording|filters) overlay$")
    public void ISeeVoiceRecordingDialog(String overlayName) throws Exception {
        switch (overlayName.toLowerCase()) {
            case "recording":
                Assert.assertTrue("The voice recording overlay should be visible", getVoiceFiltersOverlayPage()
                        .isVoiceRecordingDialogVisible());
                break;
            case "filters":
                Assert.assertTrue("The voice filters overlay should be visible", getVoiceFiltersOverlayPage()
                        .isVoiceFiltersDialogVisible());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown overlay name '%s'", overlayName));
        }
    }

    /**
     * Verify the voice graph is visible
     *
     * @throws Exception
     * @step. ^I see voice graph on Voice filters overlay$
     */
    @Then("^I see voice graph on Voice filters overlay$")
    public void ISeeRecordGraph() throws Exception {
        Assert.assertTrue("The voice graph should be visible", getVoiceFiltersOverlayPage().isVoiceGraphVisible());
    }
}
