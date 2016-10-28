package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.EmojiKeyboardOverlayPage;
import com.wearezeta.auto.android.pages.SketchPage;

import cucumber.api.java.en.When;

public class SketchPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private static final String EMOJI_UNICODE = "\uD83D\uDE00";

    private SketchPage getSketchPage() throws Exception {
        return pagesCollection.getPage(SketchPage.class);
    }

    private EmojiKeyboardOverlayPage getEmojiKeyboardOverlayPage() throws Exception {
        return pagesCollection.getPage(EmojiKeyboardOverlayPage.class);
    }

    /**
     * Draws a sketch consisting of at least numColors colors in random patterns
     * around the canvas
     *
     * @throws Exception
     * @step. ^I draw a sketch(?: on image|) with (.*) colors$
     */
    @When("^I draw a sketch(?: on image|) with (.*) colors?$")
    public void IDrawASketchWithXColors(int numColors)
            throws Exception {
        SketchPage page = getSketchPage();
        // Should skip first emoji selection.
        if (numColors >= SketchPage.SketchColor.values().length) {
            throw new IllegalStateException(String.format("The number colors should be less than %d",
                    SketchPage.SketchColor.values().length));
        }

        for (int i = 1; i <= numColors; i++) {
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
     * Draws the first emoji of the keyboard on to the center of the canvas
     *
     * @throws Exception
     * @step. ^I draw an emoji sketch$
     */
    @When("^I draw an emoji sketch$")
    public void IDrawAnEmojiSketch() throws Exception {
        getSketchPage().setColor(SketchPage.SketchColor.EMOJI);
        getEmojiKeyboardOverlayPage().tapEmojiByValue(EMOJI_UNICODE);
        getSketchPage().drawEmojiOnCanvas();
    }

}
