package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.SketchPage;

import com.wearezeta.auto.common.driver.DriverUtils;
import cucumber.api.java.en.When;

public class SketchPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private SketchPage getSketchPage() throws Exception {
        return pagesCollection.getPage(SketchPage.class);
    }

    /**
     * Draws a sketch consisting of at least numColors colors in random patterns
     * around the canvas
     *
     * @throws Exception
     * @step. ^I draw a sketch( on image)? with (.*) colors$
     */
    @When("^I draw a sketch( on image)? with (.*) colors?$")
    public void IDrawASketchWithXColors(String onImage, int numColors)
            throws Exception {
        SketchPage page = getSketchPage();
        for (int i = 1; i <= numColors; i++) {
            page.setColor(i);
            page.drawRandomLines(1);
        }
    }

    /**
     * Presses the send button from the sketch page
     *
     * @throws Exception
     * @step. ^I send my sketch$
     */
    @When("^I send my sketch$")
    public void ISendMySketch() throws Exception {
        getSketchPage().tapSendButton();
    }

    /**
     * Draws the first emoji of the keyboard on to the center of the canvas
     *
     * @throws Exception
     * @step. ^I draw an emoji sketch$
     */
    @When("^I draw an emoji sketch$")
    public void IDrawAnEmojiSketch() throws Exception {
        SketchPage page = getSketchPage();
        page.setColor(0);
        page.pickEmoji();
        page.drawEmojiOnCanvas();
    }

}
