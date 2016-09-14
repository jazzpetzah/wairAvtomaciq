package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.common.driver.DriverUtils;
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
    private static final String ANY_MESSAGE = "*ANY MESSAGE*";
    private static final int LIKE_BUTTON_CHANGE_TIMEOUT = 15;
    private static final double LIKE_BUTTON_MIN_SIMILARITY_SCORE = 0.6;
    private static final double LIKE_BUTTON_NOT_CHANGED_MIN_SCORE = -0.5;

    private ElementState messageLikeButtonState;

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
     * @step. ^I tap (Ping|Add picture|Sketch|File|Share location|Audio message|Video message) button from cursor toolbar$
     */
    @When("^I (long )?tap (Ping|Add picture|Sketch|File|Share location|Audio message|Video message) " +
            "button from cursor toolbar( for \\d+ seconds?)?$")
    public void WhenITapCursorToolButton(String isLongTap, String btnName, String duration) throws Exception {
        if (isLongTap == null) {
            getConversationViewPage().tapCursorToolButton(btnName);
        } else {
            if (!btnName.equals("Audio message")) {
                throw new IllegalArgumentException("Long tap is inl;y supported for audio messages");
            }
            if (duration == null) {
                getConversationViewPage().longTapAudioMessageCursorBtn();
            } else {
                getConversationViewPage().longTapAudioMessageCursorBtn(
                        Integer.parseInt(duration.replaceAll("[\\D]", "")));
            }
        }
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
     * Tap container
     *
     * @param tapType       Tap type
     * @param containerType one of available container types
     * @throws Exception
     * @step. ^I (long tap|double tap|tap) (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container in the conversation view$
     */
    @When("^I (long tap|double tap|tap) (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) " +
            "container in the conversation view$")
    public void ITapContainer(String tapType, String containerType) throws Exception {
        getConversationViewPage().tapContainer(tapType, containerType);
    }

    /**
     * Tap on Image container button
     *
     * @param buttonName which could be Sketch or Fullscreen
     * @throws Exception
     * @step. ^I tap on (Sketch|Fullscreen) button on the recent (?:image|picture) in the conversation view$
     */
    @When("^I tap on (Sketch|Fullscreen) button on the recent (?:image|picture) in the conversation view$")
    public void ITapImageContainerButton(String buttonName) throws Exception {
        getConversationViewPage().tapImageContainerButton(buttonName);
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

    /**
     * Long tap an existing conversation message
     *
     * @param message     the message to tap
     * @param messageType the type of message which could be Ping or Text
     * @param isLongTap   equals to null if the tap should be simple tap
     * @throws Exception
     * @step. ^I (long )?tap the (Ping|Text) message "(.*)" in the conversation view
     */
    @When("^I (long tap|double tap|tap) the (Ping|Text) message \"(.*)\" in the conversation view$")
    public void ITapTheNonTextMessage(String tapType, String messageType, String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        getConversationViewPage().tapMessage(messageType, message, tapType);
    }

    /**
     * Tap the corresponding message bottom menu button.
     *
     * @param name one of possible message bottom menu button name
     * @throws Exception
     * @step. ^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike) button on the message bottom menu$
     */
    @When("^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike) " +
            "button on the message bottom menu$")
    public void ITapMessageBottomMenuButton(String name) throws Exception {
        getConversationViewPage().tapMessageBottomMenuButton(name);
    }

    /**
     * Check the corresponding message bottom menu button.
     *
     * @param name one of possible message bottom menu button name
     * @throws Exception
     * @step. ^I (do not )?see (Delete only for me|Delete for everyone|Copy|Forward|Edit) button on the message bottom menu$
     */
    @Then("^I (do not )?see (Delete only for me|Delete for everyone|Copy|Forward|Edit) button on the message bottom menu$")
    public void ISeeMessageBottomMenuButton(String shouldNotSee, String name) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().waitUntilMessageBottomMenuButtonVisible(name) :
                getConversationViewPage().waitUntilMessageBottomMenuButtonInvisible(name);
        Assert.assertTrue(String.format("The message bottom menu button '%s' should be %s", name,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Verify whether container is visible in the conversation
     *
     * @param shouldNotSee  equals to null if the container should be visible
     * @param containerType euiter Youtube or Soundcloud or File Upload or Video Message
     * @throws Exception
     * @step. ^I (do not )?see (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container in the conversation view$
     */
    @Then("^I (do not )?see (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) " +
            "container in the conversation view$")
    public void ISeeContainer(String shouldNotSee, String containerType) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().isContainerVisible(containerType) :
                getConversationViewPage().isContainerInvisible(containerType);
        Assert.assertTrue(String.format("%s should be %s in the conversation view", containerType,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Long tap on Audio message cursor button , and then move finger up to send button within Audio message slide
     *
     * @param durationSeconds number of seconds to keep audio message button pressed
     * @throws Exception
     * @step. ^I long tap Audio message cursor button (\d+) seconds and swipe up$
     */
    @When("^I long tap Audio message cursor button (\\d+) seconds and swipe up$")
    public void LongTapAudioMessageCursorAndSwipeUp(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtnAndSwipeUp(durationSeconds);
    }

    /**
     * Check the cursor bar only contains ping, sketch, add picture, people and file buttons in cursor bar
     *
     * @throws Exception
     * @step. ^I( do not)? see cursor toolbar$
     */
    @Then("^I( do not)? see cursor toolbar$")
    public void ThenISeeCursorToolbar(String doNotSee) throws Exception {
        if (doNotSee == null) {
            Assert.assertTrue("Cursor toolbar is not visible",
                    getConversationViewPage().isCursorToolbarVisible());
        } else {
            Assert.assertTrue("Cursor toolbar is visible, but should be hidden",
                    getConversationViewPage().isCursorToolbarInvisible());
        }
    }

    /**
     * Tap on send button within Audio message slide
     *
     * @param name could be send or cancel or play
     * @throws Exception
     * @step. ^I tap audio recording (Send|Cancel|Play) button$
     */
    @When("^I tap audio recording (Send|Cancel|Play) button$")
    public void WhenITapAudioMessageSendButton(String name) throws Exception {
        getConversationViewPage().tapAudioRecordingButton(name);
    }

    /**
     * Press the corresponding button in the top toolbar
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Audio Call|Video Call|Back) button from top toolbar$
     */
    @When("^I tap (Audio Call|Video Call|Back) button from top toolbar$")
    public void WhenITapTopToolbarButton(String btnName) throws Exception {
        getConversationViewPage().tapTopBarButton(btnName);
    }

    private static final double FILE_TRANSFER_ACTION_BUTTON_MIN_SIMILARITY_SCORE = 0.4;
    private final ElementState filePlaceHolderActionButtonState = new ElementState(
            () -> getConversationViewPage().getFilePlaceholderActionButtonState());

    /**
     * Store the screenshot of current file placeholder action button
     *
     * @throws Exception
     * @step. ^I remember the state of (?:Download|View) button on file (?:upload|download) placeholder$
     */
    @When("^I remember the state of (?:Download|View) button on file (?:upload|download) placeholder$")
    public void IRememberFileTransferActionBtnState() throws Exception {
        filePlaceHolderActionButtonState.remember();
    }

    /**
     * Wait until the file uploading completely
     *
     * @param timeoutSeconds the timeout in seconds for uploading
     * @param size           should be good formated value, such as 5.00MB rather tha 5MB
     * @param extension
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until (.*) file with extension "(\w+)" is uploaded$"
     */
    @When("^I wait up to (\\d+) seconds? until (.*) file with extension \"(\\w+)\" is uploaded$")
    public void IWaitFileUploadingComplete(int timeoutSeconds, String size, String extension) throws Exception {
        getConversationViewPage().waitUntilFileUploadIsCompleted(timeoutSeconds, size, extension);
    }

    /**
     * Check whether the file transfer placeholder of expected filew is visible
     *
     * @param doNotSee      equal null means should see the place holder
     * @param size          the expected size displayed, value should be good formatted, such as 3.00MB rather than 3MB
     * @param loadDirection could be upload or received
     * @param fileFullName  the expected file name displayed
     * @param extension     the extension of the file uploaded
     * @param timeout       (optional) to define the validation should be complete within timeout
     * @param actionFailed  equals null means current action successfully
     * @throws Exception
     * @step. ^I( do not)? see the result of (.*) file (upload|received)? having name "(.*)" and extension "(\w+)"( in \d+
     * seconds)?( failed)?$
     */
    @Then("^I( do not)? see the result of (.*) file (upload|received)? having name \"(.*)\"" +
            " and extension \"(\\w+)\"( in \\d+ seconds)?( failed)?$")
    public void ThenISeeTheResultOfXFileUpload(String doNotSee, String size, String loadDirection, String fileFullName,
                                               String extension, String timeout, String actionFailed) throws Exception {
        int lookUpTimeoutSeconds = (timeout == null) ? DriverUtils.getDefaultLookupTimeoutSeconds()
                : Integer.parseInt(timeout.replaceAll("[\\D]", ""));
        boolean isUpload = loadDirection.equals("upload");
        boolean isSuccess = (actionFailed == null);
        if (doNotSee == null) {
            Assert.assertTrue("The placeholder of sending file should be visible",
                    getConversationViewPage().isFilePlaceHolderVisible(fileFullName, size, extension, isUpload,
                            isSuccess, lookUpTimeoutSeconds));
        } else {
            Assert.assertTrue("The placeholder of sending file should be invisible",
                    getConversationViewPage().isFilePlaceHolderInvisible(fileFullName, size, extension, isUpload,
                            isSuccess, lookUpTimeoutSeconds));
        }
    }

    /**
     * Wait to check whether the file placeholder action button is changed
     *
     * @param timeout            timeout in seconds
     * @param shouldNotBeChanged is not null if the button should not be changed
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until the state of (?:Download|View) button on file (?:upload|download)
     * placeholder is changed$
     */
    @When("^I wait up to (\\d+) seconds? until the state of (?:Download|View) button on file (?:upload|download)" +
            " placeholder is (not )?changed$")
    public void IWaitFileTransferActionButtonChanged(int timeout, String shouldNotBeChanged) throws Exception {
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(String.format("State of file transfer action button has not been changed after %s seconds",
                    timeout), filePlaceHolderActionButtonState.isChanged(timeout,
                    FILE_TRANSFER_ACTION_BUTTON_MIN_SIMILARITY_SCORE));
        } else {
            Assert.assertTrue(String.format("State of file transfer action button has been changed after %s seconds",
                    timeout), filePlaceHolderActionButtonState.isNotChanged(timeout,
                    FILE_TRANSFER_ACTION_BUTTON_MIN_SIMILARITY_SCORE));
        }
    }

    /**
     * Tap sketch image paint button on Picture preview overlay
     *
     * @throws Exception
     * @step. ^I tap Sketch Image Paint button on Picture preview overlay$
     */
    @When("^I tap Sketch Image Paint button on Picture preview overlay$")
    public void ITapSketchOnPictureView() throws Exception {
        getConversationViewPage().tapSketchOnPicturePreviewOverlay();
    }

    /**
     * Verify I can see/cannot see the Any msg meta item
     *
     * @param shouldNotSee
     * @param itemType       Message Meta Item type
     * @param hasExpectedMsg equals null means you don't specify the expceted content for item
     * @param expectedMsg    specified expected content for item
     * @param messageType    the message type
     * @throws Exception
     * @step. ^I (do not )?see (Like button|Like description|Message status|First like avatar|Second like avatar)
     * (with expected text "(.*)" )?in conversation view$
     */
    @Then("^I (do not )?see (Like button|Like description|Message status|First like avatar|Second like avatar)" +
            " (with expected text \"(.*)\" )?in conversation view$")
    public void ISeeMessagMeta(String shouldNotSee, String itemType, String hasExpectedMsg,
                               String expectedMsg) throws Exception {
        boolean isVisible;
        boolean shouldBeVisible = (shouldNotSee == null);
        if (shouldBeVisible) {
            if (hasExpectedMsg == null) {
                expectedMsg = ANY_MESSAGE;
                isVisible = getConversationViewPage().waitUntilMessageMetaItemVisible(itemType);
            } else {
                expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
                isVisible = getConversationViewPage().waitUntilMessageMetaItemVisible(itemType, expectedMsg);
            }
        } else {
            if (hasExpectedMsg == null) {
                expectedMsg = ANY_MESSAGE;
                isVisible = !getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType);
            } else {
                expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
                isVisible = !getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType, expectedMsg);
            }
        }
        Assert.assertEquals(
                String.format("The %s should be %s with expected text '%s'",
                        itemType, shouldBeVisible ? "visible" : "invisible", expectedMsg), shouldBeVisible, isVisible);
    }

    /**
     * Remember the state of like button
     *
     * @param messageType Specified message type
     * @throws Exception
     * @step. ^I remember the state of like button$
     */
    @When("^I remember the state of like button$")
    public void IRememberLikeButton() throws Exception {
        messageLikeButtonState = new ElementState(
                () -> getConversationViewPage().getMessageLikeButtonState()
        );
        messageLikeButtonState.remember();
    }

    /**
     * Verify the current state of like button has been changed since the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of like button item is (not )?changed$
     */
    @Then("^I verify the state of like button item is (not )?changed$")
    public void IVerifyStateOfLikeButtonChanged(String notChanged) throws Exception {
        if (notChanged == null) {
            Assert.assertTrue("The state of Like button is expected to be changed",
                    messageLikeButtonState.isChanged(LIKE_BUTTON_CHANGE_TIMEOUT, LIKE_BUTTON_MIN_SIMILARITY_SCORE));
        } else {
            Assert.assertTrue("The state of Like button is expected to be changed",
                    messageLikeButtonState.isNotChanged(LIKE_BUTTON_CHANGE_TIMEOUT, LIKE_BUTTON_NOT_CHANGED_MIN_SCORE));
        }
    }

    /**
     * Tap on Any msg meta item
     *
     * @param itemType    Message Meta Item type
     * @param messageType The message type
     * @throws Exception
     * @step. ^^I tap (Like button|Like description|Message status|First like avatar|Second like avatar)
     * in conversation view$
     */
    @When("^I tap (Like button|Like description|Message status|First like avatar|Second like avatar)" +
            " in conversation view$")
    public void ITapMessageMeta(String itemType) throws Exception {
        getConversationViewPage().tapMessageMetaItem(itemType);
    }

    /**
     * Verify the count of Message status within current conversation
     *
     * @param expectedCount expect apperance count
     * @param expectedText  the expected text within Message Status
     * @throws Exception
     * @step. ^I see (\d+) Message statu(?:s|ses) with expected text "(.*)" in conversation view$
     */
    @Then("^I see (\\d+) Message statu(?:s|ses) in conversation view$")
    public void ISeeMessageStatus(int expectedCount) throws Exception {
        int actualCount = getConversationViewPage().getMessageStatusCount();
        Assert.assertTrue(
                String.format("The expect count is not equal to actual count, actual: %d, expect: %d",
                        actualCount, expectedCount), actualCount == expectedCount);
    }

    /**
     * Verify the trashcan is visible next the expected name
     *
     * @param shouldNotSee equals null means the trashcan should be visible next to the expected name
     * @param name         the contact name
     * @throws Exception
     * @step. ^I see the trashcan next to the name of (.*) in the conversation view$
     */
    @Then("^I (do not )?see the trashcan next to the name of (.*) in the conversation view$")
    public void ISeeTrashNextToName(String shouldNotSee, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Cannot see the trashcan next to the name '%s'", name),
                    getConversationViewPage().waitUntilTrashIconVisible(name));
        } else {
            Assert.assertTrue(String.format("The trashcan next to the name '%s' should be invisible", name),
                    getConversationViewPage().waitUntilTrashIconInvisible(name));
        }
    }

    /**
     * Verify the pen is visible next to the expected name
     *
     * @param shouldNotSee equals null means the pen should be visible next to the expected name
     * @param name         the contact name
     * @throws Exception
     * @step. ^I (do not )?see the pen icon next to the name of (.*) in the conversation view$
     */
    @Then("^I (do not )?see the pen icon next to the name of (.*) in the conversation view$")
    public void ISeePenNextToName(String shouldNotSee, String name) throws Exception {
        name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Cannot see the Pen icon next to the name '%s'", name),
                    getConversationViewPage().waitUntilPenIconVisible(name));
        } else {
            Assert.assertTrue(String.format("The Pen icon next to the name '%s' should be invisible", name),
                    getConversationViewPage().waitUntilPenIconInvisible(name));
        }
    }

    /**
     * Clear content in cursor input
     *
     * @throws Exception
     * @step. ^I clear cursor input$
     */
    @When("^I clear cursor input$")
    public void IClearCursorInput() throws Exception {
        getConversationViewPage().clearMessageInCursorInput();
    }

    /**
     * Verify the text message in cursor input is visible
     *
     * @param message the expected message
     * @throws Exception
     * @step. ^I see the message "(.*)" in cursor input$
     */
    @Then("^I see the message \"(.*)\" in cursor input$")
    public void ISeeMessageInCursorInput(String message) throws Exception {
        Assert.assertTrue(String.format("The expected message '%s' is not visible in cursor input", message),
                getConversationViewPage().waitUntilCursorInputTextVisible(message));
    }
}
