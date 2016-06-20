package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.common.misc.ElementState;
import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationViewPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationViewPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletConversationViewPage getConversationViewPage() throws Exception {
        return pagesCollection.getPage(TabletConversationViewPage.class);
    }

    /**
     * Verifies whether conversation view is currently visible or not
     *
     * @param shouldNotSee equals to null is 'do not ' does not exist in step signature
     * @throws Exception
     * @step. ^I (do not )?see (?:the |\\s*)[Cc]onversation view$
     */
    @When("^I (do not )?see (?:the |\\s*)[Cc]onversation view$")
    public void ISeeConversationView(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The conversation view is not currently visible",
                    getConversationViewPage().waitUntilVisible());
        } else {
            Assert.assertTrue(
                    "The conversation view is visible, but should be hidden",
                    getConversationViewPage().waitUntilInvisible());
        }
    }

    /**
     * Tap in Dialog page on converstaion title to open participants view
     *
     * @throws Exception
     * @step. ^I tap conversation name from top toolbar$
     */
    @When("^I tap conversation name from top toolbar$")
    public void WhenITapConversationDetailsBottom() throws Exception {
        getConversationViewPage().tapTopToolbarTitle();
    }

    /**
     * Verify the last conversation message contains expected text
     *
     * @param expectedMessage the message to verify
     * @throws Exception
     * @step. ^I see the system message contains \"(.*)\" text on
     * [Cc]onversation view page$
     */
    @Then("^I see the system message contains \"(.*)\" text on [Cc]onversation view page$")
    public void ISeeTheLastSystemMessage(String expectedMessage)
            throws Exception {
        expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(
                String.format(
                        "The system message containing text '%s' is not visible in the conversation view",
                        expectedMessage), getConversationViewPage()
                        .waitForSystemMessageContains(expectedMessage));
    }

    /**
     * Verify the last conversation name message text
     *
     * @param expectedMessage the expected conversation name
     * @throws Exception
     * @step. ^I see the conversation name system message \"(.*)\" on
     * [Cc]onversation view page$
     */
    @Then("^I see the conversation name system message \"(.*)\" on [Cc]onversation view page$")
    public void ISeeTheConversationNameSystemMessage(String expectedMessage)
            throws Exception {
        expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format(
                "The conversation name system message does not equal to '%s'",
                expectedMessage), getConversationViewPage()
                .waitForConversationNameSystemMessage(expectedMessage));
    }

    /**
     * Verify the connection system conversation message contains expected text
     *
     * @param expectedMessage the message to verify
     * @throws Exception
     * @step. ^I see the system connection message contains \"(.*)\" text on
     * [Cc]onversation view page$
     */
    @Then("^I see the system connection message contains \"(.*)\" text on [Cc]onversation view page$")
    public void ISeeTheSystemConnectionMessage(String expectedMessage)
            throws Exception {
        expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(
                String.format(
                        "The system connection message containing text '%s' is not visible in the conversation view",
                        expectedMessage),
                getConversationViewPage()
                        .waitForSystemConnectionMessageContains(expectedMessage));
    }

    /**
     * Verify whether the particular outgoing invitation message is visible in
     * conversation view
     *
     * @param expectedMessage the expected message text
     * @throws Exception
     * @step. ^I see the outgoing invitation message \"(.*)\" on [Cc]onversation
     * view page$
     */
    @Then("^I see the outgoing invitation message \"(.*)\" on [Cc]onversation view page$")
    public void ISeeOutgoungInvitationMessage(String expectedMessage)
            throws Exception {
        expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
                FindBy.NAME_ALIAS);
        Assert.assertTrue(
                String.format(
                        "The outgoing invitation message containing text '%s' is not visible in the conversation view",
                        expectedMessage), getConversationViewPage()
                        .waitForOutgoingInvitationMessage(expectedMessage));
    }

    /**
     * Tap the text input field in the conversation view to start typing
     *
     * @throws Exception
     * @step. ^I tap on text input$
     */
    @And("^I tap on text input$")
    public void ITapTheTextInput() throws Exception {
        getConversationViewPage().tapTextInput();
    }

    /**
     * Type a message into the active conversation. The text input should be
     * already active. The message is NOT sent automatically
     *
     * @param message the text to type
     * @throws Exception
     * @step. ^I type (?:the |\\s*)message \"(.*)\" in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @And("^I type (?:the |\\s*)message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
    public void ITypeMessage(String message) throws Exception {
        getConversationViewPage().typeMessage(message);
    }

    /**
     * Tap Enter to send the typed message into the conversation
     *
     * @throws Exception
     * @step. ^I send the typed message in (?:the |\\s*)[Cc]onversation view$
     */
    @And("^I send the typed message in (?:the |\\s*)[Cc]onversation view$")
    public void ISendMessage() throws Exception {
        getConversationViewPage().sendMessage();
    }

    /**
     * Verify whether the message is visible in the conversation view
     *
     * @param expectedMessage the message to check
     * @param shouldNotSee    equals to null if the message should be visible in the convo
     *                        view
     * @throws Exception
     * @step. ^I (do not )?see the message \"(.*)\" in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @Then("^I (do not )?see the message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
    public void ISeeMessage(String shouldNotSee, String expectedMessage)
            throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format(
                            "The expected message '%s' is not visible in the conversation view",
                            expectedMessage), getConversationViewPage()
                            .waitUntilMessageIsVisible(expectedMessage));
        } else {
            Assert.assertTrue(
                    String.format(
                            "The expected message '%s' is visible in the conversation view, but it should not",
                            expectedMessage), getConversationViewPage()
                            .waitUntilMessageIsNotVisible(expectedMessage));
        }
    }

    /**
     * Press the corresponding button in the input controls
     * Tap file button will send file directly when you installed testing_gallery-debug.apk
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Ping|Add picture|Sketch|File) button$ from cursor toolbar$
     */
    @When("^I tap (Ping|Add picture|Sketch|File) button from cursor toolbar$")
    public void WhenITapCursorToolButton(String btnName) throws Exception {
        getConversationViewPage().tapCursorToolButton(btnName);
    }

    /**
     * Verify whether there is at least one picture in the conversation view
     *
     * @param shouldNotSee equals to null if 'do not' exists in step signature
     * @throws Exception
     * @step. ^I (do not )?see (?:a|any) new pictures? in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @Then("^I (do not )?see (?:a|any) new pictures? in (?:the |\\s*)[Cc]onversation view$")
    public void ISeeNewPicture(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    "No new pictures are visible in the conversation view",
                    getConversationViewPage().waitUntilAPictureAppears());
        } else {
            Assert.assertTrue(
                    "Some pictures are still visible in the conversation view",
                    getConversationViewPage().waitUntilPicturesNotVisible());
        }
    }

    /**
     * Tap the recent picture in the conversation view to open a preview
     *
     * @throws Exception
     * @step. ^I tap the new picture in (?:the |\\s*)[Cc]onversation view$
     */
    @When("^I tap the new picture in (?:the |\\s*)[Cc]onversation view$")
    public void ITapNewPicture() throws Exception {
        getConversationViewPage().tapRecentPicture();
    }

    /**
     * Verify whether ping message is visible in the current conversation view
     *
     * @param shouldNotBeVisible equals to null if "do not" part does not exist in the step
     *                           signature
     * @param expectedMessage    the text of expected ping message
     * @throws Exception
     * @step. ^I (do not )?see the [Pp]ing message \"<(.*)>\" in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @Then("^I (do not )?see the [Pp]ing message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
    public void ISeePingMessage(String shouldNotBeVisible,
                                String expectedMessage) throws Exception {
        expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
                FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(
                    String.format(
                            "The expected ping message '%s' is not visible in the conversation view",
                            expectedMessage), getConversationViewPage()
                            .waitUntilPingMessageIsVisible(expectedMessage));
        } else {
            Assert.assertTrue(
                    String.format(
                            "The ping message '%s' is still visible in the conversation view",
                            expectedMessage), getConversationViewPage()
                            .waitUntilPingMessageIsInvisible(expectedMessage));
        }
    }

    /**
     * Verify whether missed call notification is visible in conversation view
     *
     * @throws Exception
     * @step. ^I see missed (?:group |\\s*)call notification in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @Then("^I see missed (?:group |\\s*)call notification in (?:the |\\s*)[Cc]onversation view$")
    public void ISeeMissedCallNotification() throws Exception {
        // Notifications for both group and 1:1 calls have the same locators so
        // we don't really care
        Assert.assertTrue(
                "The expected missed call notification is not visible in the conversation view",
                getConversationViewPage().waitUntilGCNIsVisible());
    }

    /**
     * Swipe right to show the convo list
     *
     * @throws Exception
     * @step. ^I swipe right to show (?:the |\\s*)conversations list$
     */
    @When("^I swipe right to show (?:the |\\s*)conversations list$")
    public void ISwipeRight() throws Exception {
        getConversationViewPage().doSwipeRight();
    }

    /**
     * Scroll to the bottom side of conversation view
     *
     * @throws Exception
     * @step. ^I scroll to the bottom of the [Cc]onversation view$
     */
    @When("^I scroll to the bottom of the [Cc]onversation view$")
    public void IScrollToTheBottom() throws Exception {
        getConversationViewPage().scrollToTheBottom();
    }

    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;

    private enum PictureDestination {
        CONVERSATION_VIEW, PREVIEW
    }

    /**
     * Verify whether the recent picture in convo view is animated
     *
     * @param destination either 'preview' or 'conversation view'
     * @throws Exception
     * @step. ^I see the picture in (?:the |\\s*)(preview|[Cc]onversation view)
     * is animated$
     */
    @Then("^I see the picture in (?:the |\\s*)(preview|[Cc]onversation view) is animated$")
    public void ISeePictureIsAnimated(String destination) throws Exception {
        final PictureDestination dst = PictureDestination.valueOf(destination
                .toUpperCase().replace(" ", "_"));
        double avgThreshold;
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 700;
        final int maxFrames = 4;
        switch (dst) {
            case CONVERSATION_VIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(
                        getConversationViewPage()::getRecentPictureScreenshot,
                        maxFrames, screenshotingDelay);
                Assert.assertTrue(
                        String.format(
                                "The picture in the conversation view seems to be static (%.2f > %.2f)",
                                avgThreshold, MAX_SIMILARITY_THRESHOLD),
                        avgThreshold <= MAX_SIMILARITY_THRESHOLD);
                break;
            case PREVIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(
                        getConversationViewPage()::getPreviewPictureScreenshot,
                        maxFrames, screenshotingDelay);
                Assert.assertTrue(
                        String.format(
                                "The picture in the image preview view seems to be static (%.2f > %.2f)",
                                avgThreshold, MAX_SIMILARITY_THRESHOLD),
                        avgThreshold <= MAX_SIMILARITY_THRESHOLD);
                break;
        }
    }

    /**
     * Verify whether unsent indicator is visible next to the particular message
     *
     * @param msg the expected message text
     * @throws Exception
     * @step. ^I see unsent indicator next to the message \"(.*)\" in the
     * [Cc]onversation view$
     */
    @Then("^I see unsent indicator next to the message \"(.*)\" in the [Cc]onversation view$")
    public void ISeeUnsentIndicatorNextTo(String msg) throws Exception {
        Assert.assertTrue(
                String.format(
                        "Unsent indicator is not visible next to the '%s' message",
                        msg), getConversationViewPage()
                        .waitUntilUnsentIndicatorIsVisible(msg));
    }

    /**
     * Verify whether unsent indicator is visible next to a picture
     *
     * @throws Exception
     * @step. ^I see unsent indicator next to new picture in the [Cc]onversation
     * view$
     */
    @Then("^I see unsent indicator next to new picture in the [Cc]onversation view$")
    public void ISeeUnsentIndicatorNextToAPicture() throws Exception {
        Assert.assertTrue("Unsent indicator is not visible next to a picture",
                getConversationViewPage()
                        .waitUntilUnsentIndicatorIsVisibleForAPicture());
    }

    /**
     * Verify whether Close Picture Preview button is visible
     *
     * @param shouldNotBeVisible equals to null if 'do not' part does not exist in step
     *                           signature
     * @throws Exception
     * @step. ^I (do not )?see Close Picture Preview button in the
     * [Cc]onversation view$
     */
    @Then("^I (do not )?see Close Picture Preview button in the [Cc]onversation view$")
    public void ISeeClosePreview(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Close Picture Preview button is not visible",
                    getConversationViewPage()
                            .waitUntilClosePicturePreviewButtonVisible());
        } else {
            Assert.assertTrue("Close Picture Preview button is still visible",
                    getConversationViewPage()
                            .waitUntilClosePicturePreviewButtonInvisible());
        }
    }

    /**
     * Tap the Close Picture Preview button
     *
     * @throws Exception
     * @step. ^I tap Close Picture Preview button in the [Cc]onversation view$
     */
    @When("^I tap Close Picture Preview button in the [Cc]onversation view$")
    public void ITapClosePreviewButton() throws Exception {
        getConversationViewPage().tapClosePicturePreviewButton();
    }

    /**
     * Tap Play/Pause button in the recent SoundCloud player preview
     *
     * @throws Exception
     * @step. ^I tap (?:Play|Puase) button in the [Cc]onversation view$
     */
    @When("^I tap (?:Play|Puase) button in the [Cc]onversation view$")
    public void ITapPlayPauseButton() throws Exception {
        getConversationViewPage().tapPlayPauseButton();
    }

    /**
     * Verify whether Giphy button is visible in the convo view
     *
     * @param shouldNotSee equals to null if 'do not' sentence does not exist in step
     *                     signature
     * @throws Exception
     * @step. ^I (do not )?see Giphy button in the [Cc]onversation view$
     */
    @Then("^I (do not )?see Giphy button in the [Cc]onversation view$")
    public void ISeeGiphyButton(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    "Giphy button is not visible in the conversation view",
                    getConversationViewPage().waitUntilGiphyButtonVisible());
        } else {
            Assert.assertTrue(
                    "Giphy button is visible in the conversation view, but should be hidden",
                    getConversationViewPage().waitUntilGiphyButtonInvisible());
        }
    }

    /**
     * Tap Giphy button
     *
     * @throws Exception
     * @step. ^I tap Giphy button in the [Cc]onversation view$
     */
    @When("^I tap Giphy button in the [Cc]onversation view$")
    public void ITapGiphyButton() throws Exception {
        getConversationViewPage().tapGiphyButton();
    }

    /**
     * Tap Media Bar control button to start/pause media playback
     *
     * @throws Exception
     * @step. ^I tap (?:Pause|Play) button on Media Bar in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @When("^I tap (?:Pause|Play) button on Media Bar in (?:the |\\s*)[Cc]onversation view$")
    public void ITapMediaBarControlButton() throws Exception {
        getConversationViewPage().tapMediaBarControlButton();
    }

    private final static int MAX_SWIPES = 8;

    /**
     * Scroll up until media bar is shown
     *
     * @throws Exception
     * @step. ^I scroll up until Media Bar is visible in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @And("^I scroll up until Media Bar is visible in (?:the |\\s*)[Cc]onversation view$")
    public void IScrollUpUntilMediaBarVisible() throws Exception {
        Assert.assertTrue("Media Bar is not visible", getConversationViewPage()
                .scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    private static final int MEDIA_BUTTON_STATE_CHANGE_TIMEOUT = 15;
    private static final double MEDIA_BUTTON_MIN_SIMILARITY_SCORE = 0.97;

    private ElementState mediaButtonState = new ElementState(
            () -> getConversationViewPage().getMediaControlButtonState()
    );

    /**
     * Store the screenshot of current media button state in the internal
     * variable for further comparison
     *
     * @throws Exception
     * @step. ^I remember the state of media button in (?:the
     * |\\s*)[Cc]onversation view$
     */
    @And("^I remember the state of media button in (?:the |\\s*)[Cc]onversation view$")
    public void IRememberMediaButtonState() throws Exception {
        mediaButtonState.remember();
    }

    /**
     * Verify whether the current state of media control button is changed in
     * comparison to the previously screenshoted one
     *
     * @param shouldNotBeChanged equals to null if "not " part does not exist in step signature
     * @throws Exception
     * @step. ^I see the state of media button in (?:the |\\s*)[Cc]onversation
     * view is changed$
     */
    @Then("^I see the state of media button in (?:the |\\s*)[Cc]onversation view is (not )?changed$")
    public void ISeeMediaButtonStateIsChanged(String shouldNotBeChanged)
            throws Exception {
        if (shouldNotBeChanged == null) {
            Assert.assertTrue("Media control button state has not changed",
                    mediaButtonState.isChanged(MEDIA_BUTTON_STATE_CHANGE_TIMEOUT, MEDIA_BUTTON_MIN_SIMILARITY_SCORE));
        } else {
            Assert.assertTrue("Media control button state has changed",
                    mediaButtonState.isNotChanged(MEDIA_BUTTON_STATE_CHANGE_TIMEOUT, MEDIA_BUTTON_MIN_SIMILARITY_SCORE));
        }
    }
}
