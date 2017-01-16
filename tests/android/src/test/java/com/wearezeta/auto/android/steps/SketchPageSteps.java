package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.cursor.EmojiKeyboardOverlayPage;
import com.wearezeta.auto.android.pages.SketchPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SketchPageSteps {
    private static final String EMOJI_UNICODE = "\uD83D\uDE00";

    private SketchPage getSketchPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(SketchPage.class);
    }

    private EmojiKeyboardOverlayPage getEmojiKeyboardOverlayPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(EmojiKeyboardOverlayPage.class);
    }

    /**
     * Draws a sketch consisting of at least numColors colors in random patterns
     * around the canvas
     *
     * @param numColors count of line you want to draw, which should smaller than 9
     * @throws Exception
     * @step. ^I draw a sketch(?: on image|) with (.*) colors$
     */
    @When("^I draw a sketch(?: on image|) with (.*) colors?$")
    public void IDrawASketchWithXColors(int numColors)
            throws Exception {
        SketchPage page = getSketchPage();
        if (numColors > SketchPage.SketchColor.values().length) {
            throw new IllegalStateException(String.format("The number colors should be less than %d",
                    SketchPage.SketchColor.values().length));
        }

        for (int i = 0; i < numColors; i++) {
            page.setColor(SketchPage.SketchColor.values()[i]);
            page.drawRandomLines(1);
        }
    }

    /**
     * Tap the send button from the sketch page
     *
     * @throws Exception
     * @step. ^I send my sketch$
     */
    @When("^I send my sketch$")
    public void ISendMySketch() throws Exception {
        getSketchPage().tapSendButton();
    }

    /**
     * Tap on any button in Sketch View
     *
     * @param buttonName
     * @throws Exception
     * @step. ^I tap on (Emoji|Text|Sketch|Gallery) button in sketch page$
     */
    @When("^I tap on (Emoji|Text|Sketch|Gallery) button in sketch page$")
    public void ITapOnButton(String buttonName) throws Exception {
        getSketchPage().tapOnButton(buttonName);
    }

    /**
     * Draws the first emoji of the keyboard on to the center of the canvas
     *
     * @throws Exception
     * @step. ^I draw an emoji sketch$
     */
    @When("^I draw an emoji sketch$")
    public void IDrawAnEmojiSketch() throws Exception {
        getSketchPage().tapOnButton("emoji");
        getEmojiKeyboardOverlayPage().tapEmojiByValue(EMOJI_UNICODE);
        getSketchPage().drawEmojiOnCanvas();
    }

    /**
     * Draw text to the canvas
     *
     * @param text
     * @throws Exception
     * @step. ^I type text "(.*)" on sketch$
     */
    @When("^I type text \"(.*)\" on sketch$")
    public void IDrawTextSketch(String text) throws Exception {
        getSketchPage().tapOnButton("text");
        getSketchPage().typeTextOnSketch(text);
    }

    /**
     * Verify the text sketch input is visible
     *
     * @throws Exception
     * @step. ^I see text sketch input on sketch page$
     */
    @Then("^I see text sketch input on sketch page$")
    public void ISeeTextSketchInput() throws Exception {
        Assert.assertTrue("The text sketch input is invisible",getSketchPage().waitUntilSketchTextInputVisible());
    }

}
