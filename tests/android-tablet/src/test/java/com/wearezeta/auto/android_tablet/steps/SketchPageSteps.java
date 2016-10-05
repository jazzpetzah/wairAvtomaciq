package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android.pages.SketchPage;
import com.wearezeta.auto.android_tablet.pages.TabletSketchPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class SketchPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletSketchPage getSketchPage() throws Exception {
        return pagesCollection.getPage(TabletSketchPage.class);
    }

    /**
     * Draw a random sketch image
     *
     * @param numColors the count of colors used while drawing (not more than 9)
     * @throws Exception
     * @step. ^I draw a sketch with (\\d+) colors? on [Ss]ketch page$
     */
    @When("^I draw a sketch with (\\d+) colors? on [Ss]ketch page$")
    public void IDrawSketch(int numColors) throws Exception {
        // Should skip first emoji selection.
        if (numColors >= SketchPage.SketchColor.values().length) {
            throw new IllegalStateException(String.format("The number colors should be less than %d",
                    SketchPage.SketchColor.values().length));
        }

        for (int i = 1; i <= numColors; i++) {
            getSketchPage().setColor(SketchPage.SketchColor.values()[i]);
            getSketchPage().drawRandomLines(1);
        }
    }

    /**
     * Tap Send button to send the sketch
     *
     * @throws Exception
     * @step. ^I tap Send button on Sketch page$
     */
    @And("^I tap Send button on Sketch page$")
    public void ITapSendButton() throws Exception {
        getSketchPage().tapSendButton();
    }

}
