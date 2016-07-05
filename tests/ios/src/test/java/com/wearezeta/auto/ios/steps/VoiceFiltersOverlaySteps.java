package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.VoiceFiltersOverlay;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.jcodec.common.Assert;

public class VoiceFiltersOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private VoiceFiltersOverlay getVoiceFiltersOverlay() throws Exception {
        return pagesCollection.getPage(VoiceFiltersOverlay.class);
    }

    /**
     * Tap the corresponding button on Voice Filters overlay
     *
     * @param name one of available button names
     * @throws Exception
     * @step. ^I tap (Start Recording|Stop Recording|Confirm|Redo) button on Voice Filters overlay$
     */
    @When("^I tap (Start Recording|Stop Recording|Confirm|Redo) button on Voice Filters overlay$")
    public void ITapButton(String name) throws Exception {
        getVoiceFiltersOverlay().tapButton(name);
    }

    /**
     * Tap random effect buttons X times with a small delay
     *
     * @param count count of buttons to tap
     * @throws Exception
     * @step. ^I tap (\d+) random effect buttons? on Voice Filters overlay$
     */
    @And("^I tap (\\d+) random effect buttons? on Voice Filters overlay$")
    public void ITapXRandomEffectButtons(int count) throws Exception {
        getVoiceFiltersOverlay().tapRandomEffectButtons(count);
    }

    /**
     * Verify whether the particular button is visible on Voice Filters overlay
     *
     * @param name         one of available button names
     * @param shouldNotSee equals to null if the button should be visible
     * @throws Exception
     * @step. ^I (do not )?see (Start Recording|Stop Recording|Confirm|Redo) button on Voice Filters overlay$
     */
    @Then("^I (do not )?see (Start Recording|Stop Recording|Confirm|Redo) button on Voice Filters overlay$")
    public void ISeeButton(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The '%s' button is not visible on Voice Filters overlay",
                    name), getVoiceFiltersOverlay().isButtonVisible(name));
        } else {
            Assert.assertTrue(String.format("The '%s' button is visible on Voice Filters overlay, but should be hidden",
                    name), getVoiceFiltersOverlay().isButtonInvisible(name));
        }
    }
}