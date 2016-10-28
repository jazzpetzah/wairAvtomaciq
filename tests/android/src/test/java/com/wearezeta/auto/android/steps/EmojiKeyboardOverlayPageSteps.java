package com.wearezeta.auto.android.steps;


import com.wearezeta.auto.android.pages.EmojiKeyboardOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class EmojiKeyboardOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();


    private EmojiKeyboardOverlayPage getCursorEmojiOverlayPage() throws Exception {
        return pagesCollection.getPage(EmojiKeyboardOverlayPage.class);
    }

    /**
     * Verify the Emoji keyboard is visible
     *
     * @param shouldNotSee equals null means the emoji keyboard should be visible
     * @throws Exception
     * @step. ^I( do not)? see Emoji keyboard$
     */
    @Then("^I( do not)? see Emoji keyboard$")
    public void ISeeEmojiKeyboard(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The Emoji keyboard is expected to be visible",
                    getCursorEmojiOverlayPage().waitUntilVisible());
        } else {
            Assert.assertTrue("The Emoji keyboard is expected to be invisible",
                    getCursorEmojiOverlayPage().waitUntilInvisible());
        }
    }

    /**
     * Tap on n-th emoji on Cursor Emoji Keyboard
     *
     * @param index the index
     * @throws Exception
     * @step. ^I tap on (\d+)(?:st|nd|rd|th) emoji at Emoji Keyboard$
     */
    @When("^I tap on (\\d+)(?:st|nd|rd|th) emoji at Emoji Keyboard$")
    public void ITapEmoji(int index) throws Exception {
        getCursorEmojiOverlayPage().tapEmojiByIndex(index);
    }

    /**
     * Tap on specified emoji item by emoji value
     *
     * @param emojiValue emoji unicode value
     * @throws Exception
     * @step. ^I tap on the emoji "(.*)" at Emoji keyboard$
     */
    @When("^I tap on the emoji \"(.*)\" at Emoji keyboard$")
    public void ITapSpecialEmoji(String emojiValue) throws Exception {
        getCursorEmojiOverlayPage().tapEmojiByValue(emojiValue);
    }

    /**
     * Tap on Backspace button of Emoji keyboard
     *
     * @throws Exception
     * @step. ^I tap on Backspace button at Emoji keyboard$
     */
    @When("^I tap on Backspace button at Emoji keyboard$")
    public void ITapDBackspaceButton() throws Exception {
        getCursorEmojiOverlayPage().tapEmojiKeyboardTab(EmojiKeyboardOverlayPage.KeboardTab.BACKSPACE);
    }
}
