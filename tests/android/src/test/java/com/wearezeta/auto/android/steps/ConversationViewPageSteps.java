package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ConversationViewPageSteps {
    private final ElementState mediaButtonState = new ElementState(
            () -> getConversationViewPage().getMediaButtonState());
    private final ElementState verifiedConversationShieldState = new ElementState(
            () -> getConversationViewPage().getShieldStateScreenshot());
    private final ElementState topToolbarState = new ElementState(
            () -> getConversationViewPage().getTopToolbarState());
    private final ElementState audiomessageSeekbarState = new ElementState(
            () -> getConversationViewPage().getAudioMessageSeekbarState());
    private final ElementState audiomessagePreviewSeekbarState = new ElementState(
            () -> getConversationViewPage().getAudioMessagePreviewSeekbarState());
    private final ElementState audiomessageSlideMicrophoneButtonState = new ElementState(
            () -> getConversationViewPage().getAudioMessagePreviewMicrophoneButtonState());
    private ElementState messageLikeButtonState;
    private ElementState messageContainerState;
    private Boolean wasShieldVisible = null;

    private static final String ANDROID_LONG_MESSAGE = CommonUtils.generateRandomString(300);
    private static final String LONG_MESSAGE_ALIAS = "LONG_MESSAGE";
    private static final String ANY_MESSAGE = "*ANY MESSAGE*";
    private static final Timedelta SWIPE_DURATION = Timedelta.fromMilliSeconds(1300);
    private static final int MAX_SWIPES = 5;
    private static final Timedelta MEDIA_BUTTON_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double MEDIA_BUTTON_MIN_SIMILARITY_SCORE = 0.97;
    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;
    private static final Timedelta CONVO_VIEW_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double CONVO_VIEW_MIN_SIMILARITY_SCORE = 0.5;
    private static final Timedelta SHIELD_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double SHIELD_MIN_SIMILARITY_SCORE = 0.97;
    private static final Timedelta TOP_TOOLBAR_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double TOP_TOOLBAR_MIN_SIMILARITY_SCORE = 0.97;
    private static final Timedelta LIKE_BUTTON_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double LIKE_BUTTON_MIN_SIMILARITY_SCORE = 0.6;
    private static final double LIKE_BUTTON_NOT_CHANGED_MIN_SCORE = -0.5;
    private static final double FILE_TRANSFER_ACTION_BUTTON_MIN_SIMILARITY_SCORE = 0.4;
    private static final Timedelta MESSAGE_CONTAINER_CHANGE_TIMEOUT = Timedelta.fromSeconds(15);
    private static final double MESSAGE_CONTAINER_MIN_SIMILARITY_SCORE = 0.6;
    private static final double MESSAGE_CONTAINER_NOT_CHANGED_MIN_SCORE = -0.5;
    private static final double MIN_UPLOAD_TO_PLAY_SCORE = 0.75;
    private static final double MIN_PLAY_BUTTON_SCORE = 0.82;
    private static final Timedelta PLAY_BUTTON_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(20);

    private static String expandMessage(String message) {
        final Map<String, String> specialStrings = new HashMap<>();
        specialStrings.put(LONG_MESSAGE_ALIAS, ANDROID_LONG_MESSAGE);
        if (specialStrings.containsKey(message)) {
            return specialStrings.get(message);
        } else {
            return message;
        }
    }

    private ConversationViewPage getConversationViewPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ConversationViewPage.class);
    }

    /**
     * Waits for the conversation page to appear
     *
     * @throws Exception
     * @step. ^I see conversation view$
     */
    @Then("^I see conversation view$")
    public void ISeeConversationPage() throws Exception {
        Assert.assertTrue("The cursor is not visible in the conversation view",
                getConversationViewPage().isCursorViewVisible());
    }

    /**
     * Taps on the text input
     *
     * @throws Exception
     * @step. ^I tap on text input$
     */
    @When("^I tap on text input$")
    public void ITapOnTextInput() throws Exception {
        getConversationViewPage().tapOnTextInput();
    }

    /**
     * Send message to the chat, there are 2 ways to send
     * 1) Send from Cursor Input Send button (By default)
     * 2) Send from Keyboard Enter button (Need to disable Send button in Settings->Options)
     *
     * @param msg               message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
     * @param sendFrom          identify send button
     * @param doNotHideKeyboard if it equals null, should hide keyboard
     * @throws Exception
     * @step. ^I type the message "(.*)" and send it by (keyboard|cursor) Send button( without hiding keyboard)?$
     */
    @When("^I type the message \"(.*)\" and send it by (keyboard|cursor) Send button( without hiding keyboard)?$")
    public void ITypeMessageAndSendIt(String msg, String sendFrom, String doNotHideKeyboard) throws Exception {
        getConversationViewPage().typeAndSendMessage(expandMessage(msg), sendFrom, doNotHideKeyboard == null);
    }

    /**
     * Inputs a custom message and does NOT send it
     *
     * @param msg message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
     * @throws Exception
     * @step. ^I type the message "(.*)"$
     */
    @When("^I type the message \"(.*)\"$")
    public void ITypeMessage(String msg) throws Exception {
        getConversationViewPage().typeMessage(expandMessage(msg));
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

    /**
     * Verify the cursor send/ephemera button is visible/invisible
     *
     * @param shouldNotSee equals null means the send button should be visible
     * @throws Exception
     * @step. ^I( do not)? see (Send|Ephemeral) button in cursor input$
     */
    @Then("^I( do not)? see (Send|Ephemeral) button in cursor input$")
    public void ISeeSendButtonInCursorInput(String shouldNotSee, String buttonType) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The send/ephemeral button in cursor is expected to be visible",
                    getConversationViewPage().waitUntilCursorInputButtonVisible(buttonType));
        } else {
            Assert.assertTrue("The send/ephemeral button in cursor is expected to be invisible",
                    getConversationViewPage().waitUntilCursorInputButtonInvisible(buttonType));
        }
    }

    /**
     * Tap the corresponding button in the input controls
     * Tap file button will send file directly when you installed testing_gallery-debug.apk
     *
     * @param tapType                could be tap/long tap/double tap,  be careful long tap only support audio message button, and double tap only support Ephemeral button
     * @param btnName                button name
     * @param longTapDurationSeconds long tap duration in seconds
     * @param shouldReleaseFinger    this does not equal to hull if one should not
     *                               release his finger after tap on an icon. Works for long tap on Audio Message
     *                               icon only
     * @throws Exception
     * @step. ^I (tap|long tap|double tap) (Video message|Ping|Add picture|Sketch|File|Audio message|Share location|Gif|Switch to emoji|Switch to text|Send|Ephemeral) button (\d+ seconds )? from cursor
     * toolbar( without releasing my finger)?$
     */
    @When("^I (tap|long tap|double tap) " +
            "(Video message|Ping|Add picture|Sketch|File|Audio message|Share location|Gif|Switch to emoji|Switch to text|Send|Ephemeral)" +
            " button (\\d+ seconds )?from cursor toolbar( without releasing my finger)?$")
    public void ITapCursorToolButton(String tapType, String btnName, String longTapDurationSeconds,
                                     String shouldReleaseFinger) throws Exception {
        switch (tapType) {
            case "tap":
                getConversationViewPage().tapCursorToolButton(btnName);
                break;
            case "long tap":
                int longTapDuration = (longTapDurationSeconds == null) ? DriverUtils.LONG_TAP_DURATION :
                        Integer.parseInt(longTapDurationSeconds.replaceAll("[\\D]", "")) * 1000;

                if (btnName.toLowerCase().equals("audio message")) {
                    if (shouldReleaseFinger == null) {
                        getConversationViewPage().longTapAudioMessageCursorBtn(longTapDuration);
                    } else {
                        getConversationViewPage().longTapAndKeepAudioMessageCursorBtn();
                    }
                } else {
                    throw new IllegalStateException(String.format("Unknow button name '%s' for long tap", btnName));

                }
                break;
            case "double tap":
                if (btnName.toLowerCase().equals("ephemeral")) {
                    getConversationViewPage().doubleTapOnEphemeralButton();
                } else {
                    throw new IllegalStateException(String.format("Unknow button name '%s' for double tap", btnName));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify tap type '%s'", tapType));
        }

    }

    /**
     * Long tap on Audio message cursor button , and then move finger up to send button within Audio message slide
     *
     * @param durationSeconds
     * @throws Exception
     * @step. ^I long tap Audio message cursor button (\d+) seconds and swipe up$
     */
    @When("^I long tap Audio message cursor button (\\d+) seconds and swipe up$")
    public void LongTapAudioMessageCursorAndSwipeUp(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtnAndSwipeUp(durationSeconds * 1000);
    }

    /**
     * Long tap on Audio message cursor button, and remember the icon state
     *
     * @param durationSeconds
     * @throws Exception
     * @step. ^I long tap Audio message microphone button (\d+) seconds? and remember icon$
     */
    @When("^I long tap Audio message microphone button (\\d+) seconds? and remember icon$")
    public void LongTapAudioMessageCursorAndRememberIcon(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtnAndRememberIcon(durationSeconds * 1000,
                audiomessageSlideMicrophoneButtonState);
    }

    /**
     * Tap the corresponding button in the top toolbar
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Audio Call|Video Call|Collections|Back) button from top toolbar$
     */
    @When("^I tap (Audio Call|Video Call|Collections|Back) button from top toolbar$")
    public void ITapTopToolbarButton(String btnName) throws Exception {
        getConversationViewPage().tapTopBarButton(btnName);
    }

    /**
     * Tap close button for input options
     *
     * @throws Exception
     * @step. ^I close input options$
     */
    @When("^I close input options$")
    public void ICloseInputOptions() throws Exception {
        getConversationViewPage().closeInputOptions();
    }

    /**
     * Tap on Dialog page bottom for scrolling page to the end
     *
     * @throws Exception
     * @step. ^I scroll to the bottom of conversation view$
     */
    @When("^I scroll to the bottom of conversation view$")
    public void IScrollToTheBottom() throws Exception {
        getConversationViewPage().scrollToTheBottom();
    }

    /**
     * Tap in Dialog page on converstaion title to open participants view
     *
     * @throws Exception
     * @step. ^I tap conversation name from top toolbar$
     */
    @When("^I tap conversation name from top toolbar$")
    public void ITapConversationDetailsBottom() throws Exception {
        getConversationViewPage().tapTopToolbarTitle();
    }

    /**
     * Tap on Play/Pause media item button
     *
     * @throws Exception
     * @step. ^I tap (?:Play|Pause) button on SoundCloud container$
     */
    @When("^I tap (?:Play|Pause) button on SoundCloud container$")
    public void ITapPlayPauseButton() throws Exception {
        getConversationViewPage().tapPlayPauseBtn();
    }

    /**
     * Verify that Play button is visible on youtube container
     *
     * @throws Exception
     * @step. ^I see Play button on [Yy]outube container$
     */
    @When("^I see Play button on [Yy]outube container$")
    public void ISeePlayButtonOnYoutubeContainer() throws Exception {
        Assert.assertTrue("Youtube Play button is not visible, but it should be",
                getConversationViewPage().waitUntilYoutubePlayButtonVisible());
    }

    /**
     * Tap on Play/Pause button on Media Bar
     *
     * @throws Exception
     * @step. ^I tap PlayPause on Mediabar button$
     */
    @When("^I tap PlayPause on Mediabar button$")
    public void ITapPlayPauseOnMediaBarButton() throws Exception {
        getConversationViewPage().tapPlayPauseMediaBarBtn();
    }

    /**
     * Used to check that a ping has been sent Not very clear what this step does
     *
     * @param doNotSee
     * @param message  message text
     * @throws Exception
     * @step. ^I (do not )?see Ping message "(.*)" in the conversation view$
     */
    @Then("^I (do not )?see Ping message \"(.*)\" in the conversation view$")
    public void ISeePingMessageInTheDialog(String doNotSee, String message) throws Exception {
        message = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        if (doNotSee == null) {
            Assert.assertTrue(String.format("Ping message '%s' is not visible after the timeout", message),
                    getConversationViewPage().waitUntilPingMessageWithTextVisible(message));
        } else {
            Assert.assertTrue(String.format("Ping message '%s' is visible after the timeout", message),
                    getConversationViewPage().waitUntilPingMessageWithTextInvisible(message));
        }
    }

    /**
     * Used to check that num of pings in the conversation
     *
     * @param expectedCount num of pings
     * @throws Exception
     * @step. ^I see (\d+) Ping messages? in the conversation view
     */
    @Then("^I see (\\d+) Ping messages? in the conversation view$")
    public void ISeeCountPingMessageInTheDialog(int expectedCount) throws Exception {
        Assert.assertTrue(String.format("The actual count of pings is not equal to expected: %d", expectedCount),
                getConversationViewPage().isCountOfPingsEqualTo(expectedCount));
    }

    /**
     * Checks to see that a message that has been sent appears in the chat history
     *
     * @param shouldNotSee equals to null if the message should be visible
     * @param msg          the expected message
     * @throws Exception
     * @step. ^I (do not )?see the message "(.*)" in the conversation view$
     */
    @Then("^I (do not )?see the message \"(.*)\" in the conversation view$")
    public void ISeeMyMessageInTheDialog(String shouldNotSee, String msg) throws Exception {
        msg = expandMessage(msg);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", msg),
                    getConversationViewPage().waitUntilMessageWithTextVisible(msg));
        } else {
            Assert.assertTrue(
                    String.format("The message '%s' is still visible in the conversation view, but should be hidden", msg),
                    getConversationViewPage().waitUntilMessageWithTextInvisible(msg));
        }
    }

    /**
     * Verify there is no text message in the conversation view
     *
     * @throws Exception
     * @step. ^I do not see any text messages? in the conversation view$
     */
    @Then("^I do not see any text messages? in the conversation view$")
    public void IDoNotSeeAnyTextMessage() throws Exception {
        Assert.assertTrue("Expect that no text message is visible in conversation view",
                getConversationViewPage().waitUntilAnyMessageInvisible());
    }

    /**
     * Checks to see that a link preview message that has been sent appears in the chat history
     *
     * @param shouldNotSee equals to null if the message should be visible
     * @param msg          the expected message
     * @throws Exception
     * @step. ^I (do not )?see the message "(.*)" in the conversation view$
     */
    @Then("^I (do not )?see the link preview message \"(.*)\" in the conversation view$")
    public void ISeeLinkPreviewMessage(String shouldNotSee, String msg) throws Exception {
        msg = expandMessage(msg);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The link preview message '%s' is not visible in the conversation view", msg),
                    getConversationViewPage().waitUntilLinkPreviewMessageVisible(msg));
        } else {
            Assert.assertTrue(
                    String.format("The link preview message '%s' is still visible in the conversation view", msg),
                    getConversationViewPage().waitUntilLinkPreviewMessageInvisible(msg));
        }
    }

    /**
     * Checks to see that a photo exists in the chat history. Does not check which photo though
     *
     * @param shouldNotSee equals to null if 'do not' part does not exist
     * @throws Exception
     * @step. ^I (do not )?see (?:any|a) (?:photos?|pictures?) in the conversation view$
     */
    @Then("^I (do not )?see (?:any|a) (?:photos?|pictures?) in the conversation view$")
    public void ISeeNewPicture(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The picture is invisible", getConversationViewPage().waitUntilImageVisible());
        } else {
            Assert.assertTrue("The picture is still visible",
                    getConversationViewPage().waitUntilImageInvisible());
        }
    }

    /**
     * Tap on Image container button
     *
     * @param buttonName which could be Sketch or Fullscreen
     * @throws Exception
     * @step. ^I tap on (Sketch|Sketch Emoji|Sketch Text|Fullscreen) button on the recent (?:image|picture) in the conversation view$
     */
    @When("^I tap on (Sketch|Sketch Emoji|Sketch Text|Fullscreen) button on the recent (?:image|picture)" +
            " in the conversation view$")
    public void ITapImageContainerButton(String buttonName) throws Exception {
        getConversationViewPage().tapImageContainerButton(buttonName);
    }

    /**
     * Verify the sketch buttons and fullscreen button is visible in Image Container
     *
     * @param shouldNotSee equals null means the button should be visible
     * @param buttonName   the name of button
     * @throws Exception
     * @step. ^I( do not)? see (Sketch|Sketch Emoji|Sketch Text|Fullscreen) button on the recent (?:image|picture) in the conversation view$
     */
    @Then("^I( do not)? see (Sketch|Sketch Emoji|Sketch Text|Fullscreen) button on the recent (?:image|picture)" +
            " in the conversation view$")
    public void ISeeImageContainerButton(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The %s button in Image container is not visible", buttonName),
                    getConversationViewPage().waitUntilImageContainerButtonVisible(buttonName));
        } else {
            Assert.assertTrue(String.format("The %s button in Image container is still visible", buttonName),
                    getConversationViewPage().waitUntilImageContainerButtonInvisible(buttonName));
        }

    }

    /**
     * Scroll the content of conversation view
     *
     * @param swipeDirection either up or down
     * @throws Exception
     * @step. ^I scroll (up|down) the conversation view$
     */
    @When("^I scroll (up|down) the conversation view$")
    public void IScroll(String swipeDirection) throws Exception {
        switch (swipeDirection.toLowerCase()) {
            case "up":
                getConversationViewPage().dialogsPagesSwipeUp(SWIPE_DURATION);
                break;
            case "down":
                getConversationViewPage().dialogsPagesSwipeDown(SWIPE_DURATION);
                break;
            default:
                throw new IllegalArgumentException((String.format("Unknonwn swipe direction '%s'", swipeDirection)));
        }
    }

    /**
     * Swipe down on dialog page until Mediabar appears
     *
     * @throws Exception
     * @step. ^I swipe down on conversation until Mediabar appears$
     */
    @When("^I swipe down on conversation until Mediabar appears$")
    public void ISwipedownOnDialogPageUntilMediaBarAppears() throws Exception {
        Assert.assertTrue("Media Bar is not visible",
                getConversationViewPage().scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    /**
     * Navigates back to the contact list page using back button (disabled using a swipe right)
     *
     * @throws Exception
     * @step. ^I navigate back from conversation
     */
    @When("^I navigate back from conversation$")
    public void INavigateBackFromConversation() throws Exception {
        getConversationViewPage().navigateBack(Timedelta.fromMilliSeconds(1000));
    }

    /**
     * Tap new message notification in conversation view
     *
     * @param message the message content of message notification
     * @throws Exception
     * @step. ^I tap new message notification "(.*)"$
     */
    @When("^I tap new message notification \"(.*)\"$")
    public void IChangeConversationByTapMessageNotification(String message) throws Exception {
        getConversationViewPage().tapMessageNotification(message);
    }

    /**
     * Tap on send button within Audio message slide
     *
     * @param name could be send or cancel or play
     * @throws Exception
     * @step. ^I tap audio recording (Send|Cancel|Play) button$
     */
    @When("^I tap audio recording (Send|Cancel|Play) button$")
    public void ITapAudioMessageSendButton(String name) throws Exception {
        getConversationViewPage().tapAudioRecordingButton(name);
    }

    /**
     * Verify the corresponding button exists on Audio Message recorder control
     *
     * @param buttonType could be send or cancel or play
     * @throws Exception
     * @step. ^I see (Send|Cancel|Play) button on audio message recorder$"
     */
    @When("^I see (Send|Cancel|Play) button on audio message recorder$")
    public void ISeeAudioRecorderButton(String buttonType) throws Exception {
        Assert.assertTrue(String.format("The %s button is exoected to be visible on audio recorder control",
                buttonType), getConversationViewPage().waitUntilAudioRecordingButtonVisible(buttonType));
    }

    /**
     * Checks to see that the new message notification is visible
     *
     * @param message the message content of message notification
     * @throws Exception
     * @step. ^I see new message notification "(.*)"$
     */
    @Then("^I see new message notification \"(.*)\"$")
    public void ISeeNewMessageNotification(String message) throws Exception {
        Assert.assertTrue(String.format("The notification message of '%s' should be visible", message),
                getConversationViewPage().waitForMessageNotification(message));
    }

    /**
     * Checks to see that a group chat exists, where the name of the group chat is the list of users
     *
     * @param participantNameAliases one or more comma-separated user names/aliases
     * @throws Exception
     * @step. ^I see group chat page with users (.*)$
     */
    @Then("^I see group chat page with users (.*)$")
    public void ISeeGroupChatPage(String participantNameAliases) throws Exception {
        List<String> participantNames = new ArrayList<>();
        for (String nameAlias : AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .splitAliases(participantNameAliases)) {
            participantNames.add(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(nameAlias).getName());
        }
        Assert.assertTrue(String.format("Group chat view with names %s is not visible", participantNames),
                getConversationViewPage().isConversationPeopleChangedMessageContainsNames(participantNames));
    }

    /**
     * Used to check that the message "YOU REMOVED XYZ" from group chat appears
     *
     * @param message the text of the message
     * @param contact user name/alias
     * @throws Exception
     * @step. ^I see message (.*) contact (.*) on group page$
     */
    @Then("^I see message (.*) contact (.*) on group page$")
    public void ISeeMessageContactOnGroupPage(String message, String contact) throws Exception {
        contact = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(contact).getName();
        final String expectedMsg = message + " " + contact;
        Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", expectedMsg),
                getConversationViewPage().waitUntilSystemMessageVisible(expectedMsg));
    }

    /**
     * Checks to see that after the group was renamed, the user is informed of the change in the dialog page
     *
     * @param newConversationName the new conversation name to check for
     * @throws Exception
     * @step. ^I see a message informing me that I renamed the conversation to (.*)$
     */
    @Then("^I see a message informing me that I renamed the conversation to (.*)$")
    public void ISeeMessageInformingGroupRename(String newConversationName) throws Exception {
        Assert.assertTrue(
                String.format("The new conversation name '%s' has not been shown in the conversation view",
                        newConversationName),
                getConversationViewPage().waitForConversationNameChangedMessage(newConversationName));
    }

    /**
     * Checks for verified or non-verified conversation message
     *
     * @param nonVerified weather the message should show verified or non verified conversation
     * @param userName    the user who caused the downgrade of the conversation
     * @throws Exception
     * @step. ^I see a message informing me conversation is (not )?verified(?: caused by user (.*))?$
     */
    @Then("^I see a message informing me conversation is (not )?verified(?: caused by user (.*))?$")
    public void ISeeVerifiedConversationMessage(String nonVerified, String userName) throws Exception {
        if (nonVerified == null) {
            Assert.assertTrue("The otr verified conversation message has not been shown in the conversation view",
                    getConversationViewPage().waitForOtrVerifiedMessage());
        } else if (userName == null) {
            Assert.assertTrue("The otr non verified conversation message has been shown in the conversation view",
                    getConversationViewPage().waitForOtrNonVerifiedMessage());
        } else {
            userName = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                    .findUserByNameOrNameAlias(userName).getName();
            Assert.assertTrue(String.format(
                    "The otr non verified conversation message caused by user '%s' has been shown in the conversation view",
                    userName), getConversationViewPage().waitForOtrNonVerifiedMessageCausedByUser(userName));
        }

    }

    /**
     * Used once to check that the last message sent is the same as what is expected
     *
     * @param message the text of convo
     * @param not     equals to null if the message should be visible
     * @throws Exception
     * @step. ^I see the most recent conversation message is (not )?"(.*)"$
     */
    @Then("^I see the most recent conversation message is (not )?\"(.*)\"$")
    public void ISeeLastMessage(String not, String message) throws Exception {
        if (not == null) {
            Assert.assertTrue(String.format("The most recent conversation message is not equal to '%s'", message),
                    getConversationViewPage().isLastMessageEqualTo(message, 30));
        } else {
            Assert.assertFalse(String.format("The most recent conversation message should not be equal to '%s'", message),
                    getConversationViewPage().isLastMessageEqualTo(message, 5));
        }
    }

    /**
     * Used once to check that the first message sent is the same as what is expected.
     * Note! For current view, not means whole message, but the first message in current view!
     *
     * @param message the text of convo
     * @param not     equals to null if the message should be visible
     * @throws Exception
     * @step. ^I see the most top conversation message is (not )?"(.*)"$"
     */
    @Then("^I see the most top conversation message is (not )?\"(.*)\"$")
    public void ISeeFirstMessage(String not, String message) throws Exception {
        if (not == null) {
            Assert.assertTrue(String.format("The most top conversation message is not equal to '%s'", message),
                    getConversationViewPage().isFirstMessageEqualTo(message, 30));
        } else {
            Assert.assertFalse(String.format("The most top conversation message should not be equal to '%s'", message),
                    getConversationViewPage().isFirstMessageEqualTo(message, 5));
        }
    }

    /**
     * Store the screenshot of current media control button state
     *
     * @throws Exception
     * @step. ^I remember the state of (?:Play|Pause) button on SoundCloud container$
     */
    @When("^I remember the state of (?:Play|Pause) button on SoundCloud container$")
    public void IRememberMediaItemButtonState() throws Exception {
        mediaButtonState.remember();
    }

    /**
     * Store the screenshot of current upper toolbar state
     *
     * @throws Exception
     * @step. ^I remember the state of upper toolbar$
     */
    @When("^I remember the state of upper toolbar$")
    public void IRememberUpperToolbarState() throws Exception {
        topToolbarState.remember();
    }

    /**
     * Remember the state of like button
     *
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
     * Store the screenshot of current audio message seekbar
     *
     * @throws Exception
     * @step. ^I remember the state of recent audio message seekbar$
     */
    @When("^I remember the state of recent audio message seekbar$")
    public void IRememberAudioMessageSeekbar() throws Exception {
        audiomessageSeekbarState.remember();
    }

    /**
     * Store the screenshot of current audio message preview seekbar
     *
     * @throws Exception
     * @step. ^I remember the state of audio message preview seekbar$
     */
    @When("^I remember the state of audio message preview seekbar$")
    public void IRememberAudioMessagePreviewSeekbar() throws Exception {
        audiomessagePreviewSeekbarState.remember();
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
     * Tap on file action button within File placeholder
     *
     * @throws Exception
     * @step. ^I tap (?:Retry|Download|View|Cancel) button on file (?:upload|download) placeholder$
     */
    @When("^I tap (?:Retry|Download|View|Cancel) button on file (?:upload|download) placeholder$")
    public void ITapOnFileActionButton() throws Exception {
        getConversationViewPage().tapFileActionButton();
    }

    /**
     * Save or Open file from File dialog
     *
     * @param action
     * @throws Exception
     * @step. ^I (save|open) file (?:in|from) file dialog$
     */
    @When("^I (save|open) file (?:in|from) file dialog$")
    public void ISaveOrOpenFile(String action) throws Exception {
        getConversationViewPage().tapFileDialogActionButton(action);
    }

    /**
     * Verify the current state of media control button has been changed since the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of (?:Play|Pause) button on SoundCloud container is changed$
     */
    @Then("^I verify the state of (?:Play|Pause) button on SoundCloud container is changed$")
    public void IVerifyStateOfMediaControlButtonIsChanged() throws Exception {
        Assert.assertTrue("State of PlayPause media item button has not changed",
                mediaButtonState.isChanged(MEDIA_BUTTON_STATE_CHANGE_TIMEOUT, MEDIA_BUTTON_MIN_SIMILARITY_SCORE));
    }

    /**
     * Verify the current state of upper toolbar has been not changed since the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of upper toolbar item is not changed$
     */
    @Then("^I verify the state of upper toolbar item is not changed$")
    public void IVerifyStateOfUpperToolbarIsNotChanged() throws Exception {
        Assert.assertTrue("State of upper toolbar has changed",
                topToolbarState.isNotChanged(TOP_TOOLBAR_STATE_CHANGE_TIMEOUT, TOP_TOOLBAR_MIN_SIMILARITY_SCORE));
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
     * Verify that Conversation contains missed call from contact
     *
     * @param contact contact name string
     * @throws Exception
     * @step. ^I see missed call from (.*) in the conversation$
     */
    @Then("^I see missed call from (.*) in the conversation$")
    public void ISeeMissedCallFrom(String contact) throws Exception {
        contact = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        final String expectedMessage = contact + " CALLED";
        Assert.assertTrue(String.format("Missed call message '%s' is not visible in the conversation view",
                expectedMessage),
                getConversationViewPage().waitUntilMissedCallMessageIsVisible(expectedMessage));
    }

    /**
     * Verify whether a picture in conversation/preview is animated
     *
     * @param destination either "conversation" or "preview"
     * @throws Exception
     * @step. ^I see the picture in the (conversation|preview) is animated$
     */
    @Then("^I see the picture in the (conversation|preview) is animated$")
    public void ISeePictureIsAnimated(String destination) throws Exception {
        final PictureDestination dst = PictureDestination.valueOf(destination.toUpperCase());
        double avgThreshold;
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        switch (dst) {
            case CONVERSATION:
                avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getRecentPictureScreenshot,
                        maxFrames, screenshotingDelay);
                Assert.assertTrue(String.format("The picture in the conversation view seems to be static (%.2f >= %.2f)",
                        avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
            case PREVIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getPreviewPictureScreenshot,
                        maxFrames,
                        screenshotingDelay);
                Assert.assertTrue(String.format("The picture in the image preview view seems to be static (%.2f >= %.2f)",
                        avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
        }
    }

    /**
     * Verify whether the corresponding message is present in conversation view X times
     *
     * @param msg   the message to check
     * @param times the expected count of message repetitions in the convo view
     * @throws Exception
     * @step. ^I see message (.*) (\d+) times? in the conversation view$
     */
    @Then("^I see message (.*) (\\d+) times? in the conversation view$")
    public void ISeeMessageXTimes(String msg, int times) throws Exception {
        Assert.assertTrue(
                String.format("Message '%s' is not present in the conversation view %s time(s)", msg, times),
                getConversationViewPage().waitForXMessages(msg, times));
    }

    /**
     * Verify whether X images are present in conversation view
     *
     * @param expectedCount the expected count of images in the convo view
     * @throws Exception
     * @step. ^I see (\d+) images? in the conversation view$
     */
    @Then("^I see (\\d+) images? in the conversation view$")
    public void ISeeXImages(int expectedCount) throws Exception {
        Assert.assertTrue(String.format("There are less then %s images in the conversation view", expectedCount),
                getConversationViewPage().waitForXImages(expectedCount));
    }

    /**
     * Save the state of verified conversation shield into the internal field for the future comparison
     *
     * @throws Exception
     * @step. ^I remember verified conversation shield state$
     */
    @And("^I remember verified conversation shield state$")
    public void IRememberVerifiedConversationShieldState() throws Exception {
        try {
            verifiedConversationShieldState.remember();
            wasShieldVisible = true;
        } catch (IllegalStateException e) {
            wasShieldVisible = false;
        }
    }

    /**
     * Verify whether verified conversation shield has changed in comparison to the previous state
     *
     * @throws Exception
     * @step. ^I see verified conversation shield state has changed$
     */
    @Then("^I see verified conversation shield state has changed$")
    public void ISeeVerifiedConversationShieldStateHasChanged() throws Exception {
        try {
            Assert.assertTrue("State of verified conversation shield has not changed",
                    verifiedConversationShieldState.isChanged(SHIELD_STATE_CHANGE_TIMEOUT,
                            SHIELD_MIN_SIMILARITY_SCORE));
        } catch (IllegalStateException e) {
            if (wasShieldVisible == null || wasShieldVisible) {
                throw e;
            }
        }
    }

    private enum PictureDestination {
        CONVERSATION, PREVIEW
    }

    /**
     * Checks to see that upper toolbar is visible
     *
     * @throws Exception
     * @step. ^I see the upper toolbar$
     */
    @Then("^I see the upper toolbar$")
    public void ISeeTopToolbar() throws Exception {
        Assert.assertTrue("The upper toolbar is invisible", getConversationViewPage().isTopToolbarVisible());
    }

    /**
     * Checks to see whether Audio/Video call button is visible in upper toolbar
     *
     * @param doNotSee equal null means the Video call button should be visible
     * @throws Exception
     * @step. I(do not)? see the (audio|video) call button in upper toolbar$
     */
    @Then("I( do not)? see the (audio|video) call button in upper toolbar$")
    public void ISeeVideoCallButtonInUpperToolbar(String doNotSee, String callType) throws Exception {
        if (doNotSee == null) {
            switch (callType) {
                case "audio":
                    Assert.assertTrue("The audio call button should be visible in upper toolbar",
                            getConversationViewPage().isAudioCallIconInToptoolbarVisible());
                    break;
                case "video":
                    Assert.assertTrue("The video call button should be visible in upper toolbar",
                            getConversationViewPage().isVideoCallIconInToptoolbarVisible());
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown button name '%s'", callType));
            }
        } else {
            switch (callType) {
                case "audio":
                    Assert.assertTrue("The audio call button should be visible in upper toolbar",
                            getConversationViewPage().isAudioCallIconInToptoolbarInvisible());
                    break;
                case "video":
                    Assert.assertTrue("The video call button should be visible in upper toolbar",
                            getConversationViewPage().isVideoCallIconInToptoolbarInvisible());
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown button name '%s'", callType));
            }
        }
    }

    /**
     * Verify user data (User name, Unique user name)
     *
     * @param shouldNotSee equals null means the item should be visible
     * @param type         which could be user name, unique user name
     * @param text         the name or unique username alias or user name alias
     * @throws Exception
     * @step.^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Conversation view$
     */
    @Then("^I( do not)? see (user name|unique user name|user info) \"(.*)\" on Conversation view")
    public void ISeeUserData(String shouldNotSee, String type, String text) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", type),
                    getConversationViewPage().waitUntilUserDataVisible(type, text));
        } else {
            Assert.assertTrue(String.format("%s should be invisible", type),
                    getConversationViewPage().waitUntilUserDataInvisible(type, text));
        }
    }

    /**
     * Checks that to see the media bar is just below the upper toolbar
     *
     * @throws Exception
     * @step. ^I see the media bar is below the upper toolbar$
     */
    @Then("^I see the media bar is below the upper toolbar$")
    public void ISeeTheMediaBarIsBelowUpperToolbar() throws Exception {
        Assert.assertTrue("The media bar should below the upper toolbar",
                getConversationViewPage().isMediaBarBelowUptoolbar());
    }

    /**
     * Check the cursor bar only contains ping, sketch, add picture, people and file buttons in cursor bar
     *
     * @throws Exception
     * @step. ^I( do not)? see cursor toolbar$
     */
    @Then("^I( do not)? see cursor toolbar$")
    public void ISeeCursorToolbar(String doNotSee) throws Exception {
        if (doNotSee == null) {
            Assert.assertTrue("Cursor toolbar is not visible",
                    getConversationViewPage().isCursorToolbarVisible());
        } else {
            Assert.assertTrue("Cursor toolbar is visible, but should be hidden",
                    getConversationViewPage().isCursorToolbarInvisible());
        }
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
     * @step. ^I( do not)? see the result of \"(.*)\" file (upload|received)? having name "(.*)" and extension "(\w+)"( in \d+
     * seconds)?( failed)?$
     */
    @Then("^I( do not)? see the result of \"(.*)\" file (upload|received)? having name \"(.*)\"" +
            " and extension \"(\\w+)\"( in \\d+ seconds)?( failed)?$")
    public void ISeeTheResultOfXFileUpload(String doNotSee, String size, String loadDirection, String fileFullName,
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
     * Check whether the text input is visible
     *
     * @param doNotSee equals null means that the text input should be visible
     * @throws Exception
     * @step. ^I( do not)? see text input$
     */
    @Then("^I( do not)? see text input$")
    public void ISeeTextInput(String doNotSee) throws Exception {
        if (doNotSee == null) {
            Assert.assertTrue("The text input should be visible",
                    getConversationViewPage().isTextInputVisible());
        } else {
            Assert.assertTrue("The text input should be invisible",
                    getConversationViewPage().isTextInputInvisible());
        }
    }

    /**
     * Check whether the tooltip of text input is visible
     *
     * @param doNotSee equals null means that the tooltip of text input should be visible
     * @throws Exception
     * @step. ^I( do not)? see tooltip of text input$
     */
    @Then("^I( do not)? see tooltip of text input$")
    public void ISeeTooltipOfTextInput(String doNotSee) throws Exception {
        if (doNotSee == null) {
            Assert.assertTrue("The tooltip of text input should be visible",
                    getConversationViewPage().isTooltipOfTextInputVisible());
        } else {
            Assert.assertTrue("The tooltip of text input should be invisible",
                    getConversationViewPage().isTooltipOfTextInputInvisible());
        }
    }

    /**
     * Check the corresponding message bottom menu button.
     *
     * @param name one of possible message bottom menu button name
     * @throws Exception
     * @step. ^I (do not )?see (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike|Open) button on the message bottom menu$
     */
    @Then("^I (do not )?see (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike|Open) button on the message bottom menu$")
    public void ISeeMessageBottomMenuButton(String shouldNotSee, String name) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().waitUntilMessageBottomMenuButtonVisible(name) :
                getConversationViewPage().waitUntilMessageBottomMenuButtonInvisible(name);
        Assert.assertTrue(String.format("The message menu button '%s' should be %s", name,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Tap the corresponding message bottom menu button.
     *
     * @param name one of possible message bottom menu button name
     * @throws Exception
     * @step. ^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike|Open) button on the message bottom menu$
     */
    @When("^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit|Like|Unlike|Open) button on the message bottom menu$")
    public void ITapMessageBottomMenuButton(String name) throws Exception {
        getConversationViewPage().tapMessageBottomMenuButton(name);
    }

    /**
     * Long tap an existing conversation message
     *
     * @param message     the message to tap
     * @param messageType the type of message which could be Ping or Text
     * @param tapType     the tap type (long tap, double tap, tap)
     * @throws Exception
     * @step. ^I (long tap|double tap|tap) the (Ping|Text) message "(.*)" in the conversation view$
     */
    @When("^I (long tap|double tap|tap) the( obfuscated)? (Ping|Text) message \"(.*)\" in the conversation view$")
    public void ITapTheNonTextMessage(String tapType, String isObfuscated, String messageType, String message)
            throws Exception {
        if (isObfuscated == null) {
            getConversationViewPage().tapMessage(messageType,
                    Optional.of(AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                            .replaceAliasesOccurences(message, FindBy.NAME_ALIAS)), tapType);
        } else {
            getConversationViewPage().tapMessage(messageType, Optional.empty(), tapType);
        }
    }

    /**
     * Verify whether container is visible in the conversation
     *
     * @param shouldNotSee  equals to null if the container should be visible
     * @param containerType which could be Image/Youtube/Soundcloud/File upload/Video message/Audio Message/Share location
     *                      /Link Preview
     * @throws Exception
     * @step. ^I (do not )?see (Image|Youtube|Soundcloud|File Upload|File Upload Placeholder|Video Message|Audio Message|Audio Message Placeholder|Share Location|Link Preview) container in the conversation view$
     */
    @Then("^I (do not )?see " +
            "(Image|Youtube|Soundcloud|File Upload|File Upload Placeholder|Video Message|Audio Message|Audio Message Placeholder|Share Location|Link Preview)" +
            " container in the conversation view$")
    public void ISeeContainer(String shouldNotSee, String containerType) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().isContainerVisible(containerType) :
                getConversationViewPage().isContainerInvisible(containerType);
        Assert.assertTrue(String.format("%s should be %s in the conversation view", containerType,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Remember the state of Message Container
     *
     * @param containerType which could be Image/Youtube/Soundcloud/File upload/Video message/Audio Message/Share location
     *                      /Link Preview
     * @throws Exception
     * @step. ^I remember the state of (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|
     * Share Location|Link Preview) container in the conversation view$
     */
    @When("^I remember the state of " +
            "(Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview)" +
            " container in the conversation view$")
    public void IRememberContainer(String containerType) throws Exception {
        messageContainerState = new ElementState(
                () -> getConversationViewPage().getMessageContainerState(containerType)
        );
        messageContainerState.remember();
    }

    /**
     * Verify the Message container is changed or not
     *
     * @param containerType
     * @param notChanged
     * @throws Exception
     * @step. ^I verify the state of (Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container is (not )?changed$
     */
    @Then("^I verify the state of " +
            "(Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview)" +
            " container is (not )?changed$")
    public void IVerifyStateOfMessageContainerChanged(String containerType, String notChanged) throws Exception {
        if (notChanged == null) {
            Assert.assertTrue(String.format("The state of %s container is expected to be changed", containerType),
                    messageContainerState.isChanged(MESSAGE_CONTAINER_CHANGE_TIMEOUT,
                            MESSAGE_CONTAINER_MIN_SIMILARITY_SCORE));
        } else {
            Assert.assertTrue(String.format("The state of %s container is expected to be changed", containerType),
                    messageContainerState.isNotChanged(MESSAGE_CONTAINER_CHANGE_TIMEOUT,
                            MESSAGE_CONTAINER_NOT_CHANGED_MIN_SCORE));
        }
    }

    /**
     * Verify the link preview URL
     *
     * @param url the expected url in Link Preview container
     * @throws Exception
     * @step. ^I see Link Preview URL (.*)$
     */
    @Then("^I see Link Preview URL (.*)$")
    public void ISeeLinkPreviewUrl(String url) throws Exception {
        Assert.assertTrue(String.format("The url '%s' should be visible in recent link preview container", url),
                getConversationViewPage().waitUntilLinkPreviewUrlVisible(url));
    }

    /**
     * Tap container
     *
     * @param tapType       Tap type
     * @param containerType one of available container types
     * @throws Exception
     * @step. ^I (long tap|double tap|tap) (Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container in the conversation view$
     */
    @When("^I (long tap|double tap|tap) " +
            "(Image|Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview)" +
            " container in the conversation view$")
    public void ITapContainer(String tapType, String containerType) throws Exception {
        getConversationViewPage().tapContainer(tapType, containerType);
    }

    /**
     * Tap a button on video message preview
     *
     * @param buttonType could be "audio message" or "video message"
     * @throws Exception
     * @step. ^I tap (?:Play|X|Retry|Pause) button on the recent (video message|audio message) in the conversation view$
     */
    @When("^I tap (?:Play|X|Retry|Pause) button on the recent (video message|audio message) in the conversation view$")
    public void ITapButtonOnAudioOrVideoMessage(String buttonType) throws Exception {
        switch (buttonType.toLowerCase()) {
            case "video message":
                getConversationViewPage().tapVideoMessageButton();
                break;
            case "audio message":
                getConversationViewPage().tapAudioMessageButton();
                break;
            default:
                throw new IllegalStateException(String.format("Cannot identify the button type '%s'", buttonType));
        }
    }

    /**
     * Check whether a button is visible on video message container
     *
     * @param messageType could be video or audio
     * @throws Exception
     * @step. ^I see (?:Play|X|Retry) button on the recent (video|audio) message in the conversation view$
     */
    @When("^I see (?:Play|X|Retry) button on the recent (video|audio) message in the conversation view$")
    public void ISeeButtonOnVideoMessage(String messageType) throws Exception {
        if (messageType.toLowerCase().equals("video")) {
            Assert.assertTrue("The button is not visible on the recent video message",
                    getConversationViewPage().isVideoMessageButtonVisible());
        } else {
            Assert.assertTrue("The button is not visible on the recent video message",
                    getConversationViewPage().isAudioMessageButtonVisible());
        }
    }

    private final ElementState videoMessagePlayButtonState = new ElementState(
            () -> getConversationViewPage().getVideoContainerButtonState().orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of the button on video message container")
            )
    );

    private final ElementState audioMessagePlayButtonState = new ElementState(
            () -> getConversationViewPage().getAudioContainerButtonState().orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of the button on audio message container")
            )
    );

    /**
     * Store current state of Play button into varibale
     *
     * @param buttonType could be "audio message" or "video message"
     * @throws Exception
     * @step. ^I remember the state of (?:Play|X|Retry|Pause) button on the recent (video message|audio message) in the
     * conversation view$
     */
    @When("^I remember the state of (?:Play|X|Retry|Pause) button on the recent (video message|audio message)" +
            " in the conversation view$")
    public void IRememberButtonStateInAudioMessageContainer(String buttonType) throws Exception {
        switch (buttonType.toLowerCase()) {
            case "video message":
                videoMessagePlayButtonState.remember();
                break;
            case "audio message":
                audioMessagePlayButtonState.remember();
                break;
            default:
                throw new IllegalStateException(String.format("Cannot identify the button type '%s'", buttonType));
        }
    }

    /**
     * Tap on all resend button in current view
     *
     * @throws Exception
     * @step. ^I resend all the visible messages in conversation view$
     */
    @When("^I resend all the visible messages in conversation view$")
    public void ITapResendButton() throws Exception {
        getConversationViewPage().tapAllResendButton();
    }

    /**
     * Wait until audio message upload completed
     *
     * @param timeoutSeconds seconds to wait for upload completed
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until (video message|audio message) (?:download|upload) is completed$
     */
    @Then("^I wait up to (\\d+) seconds? until (video message|audio message) (?:download|upload) is completed$")
    public void IWaitUntilMessageUploaded(int timeoutSeconds, String buttonType) throws Exception {
        FunctionalInterfaces.ISupplierWithException<Boolean> verificationFunc;
        final Timedelta timeout = Timedelta.fromSeconds(timeoutSeconds);
        switch (buttonType.toLowerCase()) {
            case "video message":
                verificationFunc = () -> videoMessagePlayButtonState.isChanged(timeout,
                        MIN_PLAY_BUTTON_SCORE);
                videoMessagePlayButtonState.remember();
                break;
            case "audio message":
                verificationFunc = () -> audioMessagePlayButtonState.isChanged(timeout,
                        MIN_UPLOAD_TO_PLAY_SCORE);
                final BufferedImage cancelBntInitialState = ImageUtil.readImageFromFile(
                        AndroidCommonUtils.getImagesPathFromConfig(AndroidCommonUtils.class)
                                + "android_audio_msg_cancel_btn.png");
                audioMessagePlayButtonState.remember(cancelBntInitialState);
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the button type '%s'", buttonType));
        }
        Assert.assertTrue(String.format("The current and previous state of the %s button seems to be the same",
                buttonType), verificationFunc.call());
    }

    /**
     * Wait until audio message play started (pause button shown)
     *
     * @param timeoutSeconds seconds to wait for play started
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until audio message play is started$
     */
    @Then("^I wait up to (\\d+) seconds? until audio message play is started$")
    public void IWaitForAudioMessagePlay(int timeoutSeconds) throws Exception {
        final BufferedImage pauseBntInitialState = ImageUtil.readImageFromFile(
                AndroidCommonUtils.getImagesPathFromConfig(AndroidCommonUtils.class)
                        + "android_audio_msg_pause_btn.png");
        audioMessagePlayButtonState.remember(pauseBntInitialState);
        Assert.assertTrue("Audio message pause button is not visible",
                audioMessagePlayButtonState.isNotChanged(Timedelta.fromSeconds(timeoutSeconds), MIN_PLAY_BUTTON_SCORE));
    }

    /**
     * Verify whether current button state differs from the previous one
     *
     * @param buttonType         could be "audio message" or "video message"
     * @param shouldNotBeChanged equals to null if the state should be different
     * @throws Exception
     * @step. ^I verify the state of (?:Play|X|Retry|Pause) button on the recent (video message|audio message) in the
     * conversation view is (not )?changed$
     */
    @Then("^I verify the state of (?:Play|X|Retry|Pause) button on the recent (video message|audio message)" +
            " in the conversation view is (not )?changed$")
    public void ISeePlayButtonStateChanged(String buttonType, String shouldNotBeChanged) throws Exception {
        FunctionalInterfaces.ISupplierWithException<Boolean> verificationFunc;
        switch (buttonType.toLowerCase()) {
            case "video message":
                verificationFunc = (shouldNotBeChanged == null) ?
                        () -> videoMessagePlayButtonState.isChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                                MIN_PLAY_BUTTON_SCORE) :
                        () -> videoMessagePlayButtonState.isNotChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                                MIN_PLAY_BUTTON_SCORE);
                break;
            case "audio message":
                verificationFunc = (shouldNotBeChanged == null) ?
                        () -> audioMessagePlayButtonState.isChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                                MIN_PLAY_BUTTON_SCORE) :
                        () -> audioMessagePlayButtonState.isNotChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                                MIN_PLAY_BUTTON_SCORE);
                break;
            default:
                throw new IllegalStateException(String.format("Cannot identify the button type '%s'", buttonType));
        }

        Assert.assertTrue(String.format("The current and previous state of the %s button seems to be %s", buttonType,
                (shouldNotBeChanged == null) ? "the same" : "changed"), verificationFunc.call());
    }

    private static final double MIN_AUDIOMESSAGE_SEEKBAR_SCORE = 0.8;
    private static final Timedelta AUDIOMESSAGE_SEEKBAR_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(20);

    /**
     * Verify whether current audio message seekbar differs from the previous one
     *
     * @throws Exception
     * @step. ^I verify the state of recent audio message seekbar in the conversation view is changed$
     */
    @Then("^I verify the state of recent audio message seekbar in the conversation view is changed$")
    public void ISeeAudioMessageSeekbarStateChanged() throws Exception {
        Assert.assertTrue("The current and previous state of audio message seekbar seems to be same",
                audiomessageSeekbarState.isChanged(AUDIOMESSAGE_SEEKBAR_STATE_CHANGE_TIMEOUT,
                        MIN_AUDIOMESSAGE_SEEKBAR_SCORE));
    }

    /**
     * Verify whether current audio message preview seekbar differs from the previous one
     *
     * @throws Exception
     * @step. ^I verify the state of audio message preview seekbar is changed$
     */
    @Then("^I verify the state of audio message preview seekbar is changed$")
    public void ISeeAudioMessagePreviewSeekbarStateChanged() throws Exception {
        Assert.assertTrue("The current and previous state of audio message preview seekbar seems to be same",
                audiomessagePreviewSeekbarState.isChanged(AUDIOMESSAGE_SEEKBAR_STATE_CHANGE_TIMEOUT,
                        MIN_AUDIOMESSAGE_SEEKBAR_SCORE));
    }

    private static final double MIN_AUDIOMESSAGE_MICROPHONE_SCORE = 0.9;
    private static final Timedelta AUDIOMESSAGE_MICROPHONE_STATE_CHANGE_TIMEOUT = Timedelta.fromSeconds(10);

    /**
     * Verify whether current audio message microphone button differs from the previous one
     *
     * @throws Exception
     * @step. ^I verify the state of audio message microphone button in the conversation view is changed$
     */
    @Then("^I verify the state of audio message microphone button in the conversation view is changed$")
    public void ISeeAudioMessageMicrophoneButtonStateChanged() throws Exception {
        Assert.assertTrue("The current and previous state of audio message microphone button seems to be same",
                audiomessageSlideMicrophoneButtonState.isChanged(AUDIOMESSAGE_MICROPHONE_STATE_CHANGE_TIMEOUT,
                        MIN_AUDIOMESSAGE_MICROPHONE_SCORE));
    }

    /**
     * Verify the audio message is recording
     *
     * @throws Exception
     * @step. ^I(do not)? see audio message is recording$
     */
    @Then("^I( do not)? see audio message is recording$")
    public void ISeeOngoingAudioMessageRecording(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The audio message recording slide should be visible",
                    getConversationViewPage().waitUntilAudioMessageRecordingSlideVisible());
            Assert.assertTrue("The audio message recording play button should be visible",
                    getConversationViewPage().waitUntilAudioRecordingButtonVisible("Play"));
            Assert.assertTrue("The audio message recording send button should be visible",
                    getConversationViewPage().waitUntilAudioRecordingButtonVisible("Send"));
            Assert.assertTrue("The audio message recording cancel button should be visible",
                    getConversationViewPage().waitUntilAudioRecordingButtonVisible("Cancel"));
            Assert.assertTrue("The audio message recording duration should be visible",
                    getConversationViewPage().isAudioMessageRecordingDurationVisible());
        } else {
            Assert.assertTrue("The audio message recording slide should be invisible",
                    getConversationViewPage().waitUntilAudioMessageRecordingSlideInvisible());
            Assert.assertTrue("The audio message recording play button should be visible",
                    getConversationViewPage().waitUntilAudioRecordingButtonInvisible("Play"));
            Assert.assertTrue("The audio message recording send button should be visible",
                    getConversationViewPage().waitUntilAudioRecordingButtonInvisible("Send"));
        }
    }

    /**
     * Verify the difference between the height of two strings
     *
     * @param msg1               the first conversation message text
     * @param isNot              equals to null is the current percentage should be greater or equal to the expected one
     * @param msg2               the second message text
     * @param expectedPercentage the expected diff percentage
     * @throws Exception
     * @step. ^I see that the difference in height of "(.*)" and "(.*)" messages is (not )?greater than (\d+) percent$
     */
    @Then("^I see that the difference in height of \"(.*)\" and \"(.*)\" messages is (not )?greater than (\\d+) percent$")
    public void ISeeMassagesHaveEqualHeight(String msg1, String msg2, String isNot, int expectedPercentage) throws Exception {
        final int msg1Height = getConversationViewPage().getMessageHeight(msg1);
        assert msg1Height > 0;
        final int msg2Height = getConversationViewPage().getMessageHeight(msg2);
        assert msg2Height > 0;
        int currentPercentage = 0;
        if (msg1Height > msg2Height) {
            currentPercentage = msg1Height * 100 / msg2Height - 100;
        } else if (msg1Height < msg2Height) {
            currentPercentage = msg2Height * 100 / msg1Height - 100;
        }
        if (isNot == null) {
            Assert.assertTrue(
                    String.format("The height of '%s' message (%s) is less than %s%% different than the height of '%s' message (%s)",
                            msg1, msg1Height, expectedPercentage, msg2, msg2Height),
                    currentPercentage >= expectedPercentage);
        } else {
            Assert.assertTrue(
                    String.format("The height of '%s' message (%s) is more than %s%% different than the height of '%s' message (%s)",
                            msg1, msg1Height, expectedPercentage, msg2, msg2Height),
                    currentPercentage <= expectedPercentage);
        }
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
        name = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
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
        name = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Cannot see the Pen icon next to the name '%s'", name),
                    getConversationViewPage().waitUntilPenIconVisible(name));
        } else {
            Assert.assertTrue(String.format("The Pen icon next to the name '%s' should be invisible", name),
                    getConversationViewPage().waitUntilPenIconInvisible(name));
        }
    }

    /**
     * Verify I can see users's message separator
     *
     * @param shouldNotSee
     * @param name         the user's name or name alias
     * @throws Exception
     * @step. ^I (do not )?see the message separator of (.*) in (\d+) seconds$
     */
    @Then("^I (do not )?see the message separator of (.*) in (\\d+) seconds$")
    public void ISeeMessageFromUser(String shouldNotSee, String name, int timeOutSeconds) throws Exception {
        name = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The message separator of user %s should be visible", name),
                    getConversationViewPage().waitUntilMessageSeparatorVisible(name, timeOutSeconds));
        } else {
            Assert.assertTrue(String.format("The message separator of user %s should be invisible", name),
                    getConversationViewPage().waitUntilMessageSeparatorInvisible(name, timeOutSeconds));
        }
    }

    /**
     * Verify I can see/cannot see the Any msg meta item
     *
     * @param shouldNotSee
     * @param itemType       Message Meta Item type
     * @param hasExpectedMsg equals null means you don't specify the expceted content for item
     * @param expectedMsg    specified expected content for item
     * @throws Exception
     * @step. ^I (do not )?see (Like button|Like description|Message status|First like avatar|Second like avatar)
     * (with expected text "(.*)" )?in conversation view$
     */
    @Then("^I (do not )?see (Like button|Like description|Message status|First like avatar|Second like avatar)" +
            " (with expected text \"(.*)\" )?in conversation view$")
    public void ISeeMessagMetaForText(String shouldNotSee, String itemType, String hasExpectedMsg,
                                      String expectedMsg) throws Exception {
        boolean isVisible;
        boolean shouldBeVisible = (shouldNotSee == null);
        if (shouldBeVisible) {
            if (hasExpectedMsg == null) {
                expectedMsg = ANY_MESSAGE;
                isVisible = getConversationViewPage().waitUntilMessageMetaItemVisible(itemType);
            } else {
                expectedMsg = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
                isVisible = getConversationViewPage().waitUntilMessageMetaItemVisible(itemType, expectedMsg);
            }
        } else {
            if (hasExpectedMsg == null) {
                expectedMsg = ANY_MESSAGE;
                isVisible = !getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType);
            } else {
                expectedMsg = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                        .replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
                isVisible = !getConversationViewPage().waitUntilMessageMetaItemInvisible(itemType, expectedMsg);
            }
        }
        Assert.assertEquals(
                String.format("The %s should be %s with expected text '%s'",
                        itemType, shouldBeVisible ? "visible" : "invisible", expectedMsg), shouldBeVisible, isVisible);
    }

    /**
     * Tap on Any msg meta item
     *
     * @param itemType Message Meta Item type
     * @throws Exception
     * @step. ^I tap (Like button|Like description|Message status|First like avatar|Second like avatar) in conversation view$
     */
    @When("^I tap (Like button|Like description|Message status|First like avatar|Second like avatar) in conversation view$")
    public void ITapMessageMeta(String itemType) throws Exception {
        getConversationViewPage().tapMessageMetaItem(itemType);
    }

    /**
     * Verify the count of Message status within current conversation
     *
     * @param expectedCount expect apperance count
     * @throws Exception
     * @step. ^I see (\d+) Message statu(?:s|ses) with expected text "(.*)" in conversation view$
     */
    @Then("^I see (\\d+) Message statu(?:s|ses) in conversation view$")
    public void ISeeMessageStatus(int expectedCount) throws Exception {
        final int actualCount = getConversationViewPage().getMessageStatusCount();
        Assert.assertTrue(String.format("The expect count is not equal to actual count, actual: %d, expect: %d",
                actualCount, expectedCount), actualCount == expectedCount);
    }

    /**
     * Verify Someone is/are typing
     *
     * @param userNames name or name alias comma separated list
     * @throws Exception
     * @step. ^I see (.*) (?:is|are) typing$
     */
    @Then("^I see (.*) (?:is|are) typing$")
    public void ISeeTyping(String userNames) throws Exception {
        String names = AndroidTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(userNames, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("%s are expected to be visible in typing list", userNames),
                getConversationViewPage().waitUntilTypingVisible(names));
    }
}
