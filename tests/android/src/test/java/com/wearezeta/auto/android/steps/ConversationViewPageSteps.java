package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ConversationViewPageSteps {

    private static final String ANDROID_LONG_MESSAGE = CommonUtils.generateRandomString(300);
    private static final String LONG_MESSAGE_ALIAS = "LONG_MESSAGE";
    private static final int SWIPE_DURATION_MILLISECONDS = 1300;
    private static final int MAX_SWIPES = 5;
    private static final int MEDIA_BUTTON_STATE_CHANGE_TIMEOUT = 15;
    private static final double MEDIA_BUTTON_MIN_SIMILARITY_SCORE = 0.97;
    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;
    private static final int CONVO_VIEW_STATE_CHANGE_TIMEOUT = 15;
    private static final double CONVO_VIEW_MIN_SIMILARITY_SCORE = 0.5;
    private static final int SHIELD_STATE_CHANGE_TIMEOUT = 15;
    private static final double SHIELD_MIN_SIMILARITY_SCORE = 0.97;
    private static final int TOP_TOOLBAR_STATE_CHANGE_TIMEOUT = 15;
    private static final double TOP_TOOLBAR_MIN_SIMILARITY_SCORE = 0.97;
    private static final double FILE_TRANSFER_ACTION_BUTTON_MIN_SIMILARITY_SCORE = 0.4;
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final ElementState mediaButtonState = new ElementState(
            () -> getConversationViewPage().getMediaButtonState());
    private final ElementState conversationViewState = new ElementState(
            () -> getConversationViewPage().getConvoViewStateScreenshot());
    private final ElementState verifiedConversationShieldState = new ElementState(
            () -> getConversationViewPage().getShieldStateScreenshot());
    private final ElementState topToolbarState = new ElementState(
            () -> getConversationViewPage().getTopToolbarState());
    private final ElementState filePlaceHolderActionButtonState = new ElementState(
            () -> getConversationViewPage().getFilePlaceholderActionButtonState());
    private final ElementState audiomessageSeekbarState = new ElementState(
            () -> getConversationViewPage().getAudioMessageSeekbarState());
    private final ElementState audiomessagePreviewSeekbarState = new ElementState(
            () -> getConversationViewPage().getAudioMessagePreviewSeekbarState());
    private final ElementState audiomessageSlideMicrophoneButtonState = new ElementState(
            () -> getConversationViewPage().getAudioMessagePreviewMicrophoneButtonState());
    private Boolean wasShieldVisible = null;

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
        return pagesCollection.getPage(ConversationViewPage.class);
    }

    /**
     * Waits for the conversation page to appear
     *
     * @throws Exception
     * @step. ^I see conversation view$
     */
    @Then("^I see conversation view$")
    //TODO : Refactory See dialog page,
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
     * Send message to the chat
     *
     * @param msg               message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
     * @param doNotHideKeyboard if it equals null, should hide keyboard
     * @throws Exception
     * @step. ^I type the message "(.*)" and send it( without hiding keyboard)?$
     */
    @When("^I type the message \"(.*)\" and send it( without hiding keyboard)?$")
    public void ITypeMessageAndSendIt(String msg, String doNotHideKeyboard) throws Exception {
        getConversationViewPage().typeAndSendMessage(expandMessage(msg), doNotHideKeyboard == null);
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
     * Press the corresponding button in the input controls
     * Tap file button will send file directly when you installed testing_gallery-debug.apk
     *
     * @param longTap                equals not null means long tap on the cursor button
     * @param btnName                button name
     * @param longTapDurationSeconds long tap duration in seconds
     * @param shouldReleaseFinger    this does not equal to hull if one should not
     *                               release his finger after tap on an icon. Works for long tap on Audio Message
     *                               icon only
     * @throws Exception
     * @step. ^I (long )?tap (Video message|Ping|Add picture|Sketch|File|Audio message|Share location) button (\d+ seconds )? from cursor
     * toolbar( without releasing my finger)?$
     */
    @When("^I (long )?tap (Video message|Ping|Add picture|Sketch|File|Audio message|Share location) button " +
            "(\\d+ seconds )?from cursor toolbar( without releasing my finger)?$")
    public void ITapCursorToolButton(String longTap, String btnName, String longTapDurationSeconds,
                                     String shouldReleaseFinger) throws Exception {
        if (longTap == null) {
            switch (btnName.toLowerCase()) {
                case "video message":
                case "audio message":
                case "ping":
                case "add picture":
                case "sketch":
                case "file":
                case "share location":
                    getConversationViewPage().tapCursorToolButton(btnName);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
            }
        } else {
            int longTapDuration = (longTapDurationSeconds == null) ? DriverUtils.LONG_TAP_DURATION :
                    Integer.parseInt(longTapDurationSeconds.replaceAll("[\\D]", "")) * 1000;

            switch (btnName.toLowerCase()) {
                case "audio message":
                    if (shouldReleaseFinger == null) {
                        getConversationViewPage().longTapAudioMessageCursorBtn(longTapDuration);
                    } else {
                        getConversationViewPage().longTapAndKeepAudioMessageCursorBtn();
                    }
                    break;
                default:
                    throw new IllegalStateException(String.format("Unknow button name '%s' for long tap", btnName));
            }
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
     * @step. ^I long tap Audio message microphone button (\d+) seconds and remember icon$
     */
    @When("^I long tap Audio message microphone button (\\d+) seconds and remember icon$")
    public void LongTapAudioMessageCursorAndRememberIcon(int durationSeconds) throws Exception {
        getConversationViewPage().longTapAudioMessageCursorBtnAndRememberIcon(durationSeconds * 1000,
                audiomessageSlideMicrophoneButtonState);
    }

    /**
     * Press the corresponding button in the top toolbar
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Audio Call|Video Call|Back) button from top toolbar$
     */
    @When("^I tap (Audio Call|Video Call|Back) button from top toolbar$")
    public void ITapTopToolbarButton(String btnName) throws Exception {
        getConversationViewPage().tapTopBarButton(btnName);
    }

    /**
     * Press close button for input options
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
    public void IPressPlayPauseButton() throws Exception {
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
     * @step. ^I press PlayPause on Mediabar button$
     */
    @When("^I press PlayPause on Mediabar button$")
    public void IPressPlayPauseOnMediaBarButton() throws Exception {
        getConversationViewPage().tapPlayPauseMediaBarBtn();
    }

    /**
     * Used to check that a ping has been sent Not very clear what this step does
     *
     * @param doNotSee
     * @param message  message text
     * @throws Exception
     * @step. ^I (do not )?see Ping message "(.*)" in the conversation view
     */
    @Then("^I (do not )?see Ping message \"(.*)\" in the conversation view")
    public void ISeePingMessageInTheDialog(String doNotSee, String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        if (doNotSee == null) {
            Assert.assertTrue(String.format("Ping message '%s' is not visible after the timeout", message),
                    getConversationViewPage().waitForPingMessageWithText(message));
        } else {
            Assert.assertTrue(String.format("Ping message '%s' is visible after the timeout", message),
                    getConversationViewPage().waitForPingMessageWithTextDisappears(message));
        }
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
                    getConversationViewPage().waitForMessage(msg));
        } else {
            Assert.assertTrue(
                    String.format("The message '%s' is still visible in the conversation view, but should be hidden", msg),
                    getConversationViewPage().isMessageInvisible(msg));
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
    public void ISeeNewPhotoInTheDialog(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("No new photo is present in the chat", getConversationViewPage().isImageVisible());
        } else {
            Assert.assertTrue("A photo is present in the chat, but it should not be vivible",
                    getConversationViewPage().isImageInvisible());
        }
    }

    /**
     * Selects the last picture sent in a conversation view dialog
     *
     * @param isLogTap equals to null if it should be simple tap
     * @throws Exception
     * @step. ^I (long )?tap the recent (?:image|picture) in the conversation view$
     */
    @When("^I (long )?tap the recent (?:image|picture) in the conversation view$")
    public void ITapRecentImage(String isLogTap) throws Exception {
        if (isLogTap == null) {
            getConversationViewPage().tapRecentImage();
        } else {
            getConversationViewPage().longTapRecentImage();
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
                getConversationViewPage().dialogsPagesSwipeUp(SWIPE_DURATION_MILLISECONDS);
                break;
            case "down":
                getConversationViewPage().dialogsPagesSwipeDown(SWIPE_DURATION_MILLISECONDS);
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
        Assert.assertTrue("Media Bar is not visible", getConversationViewPage().scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    /**
     * Navigates back to the contact list page using back button (disabled using a swipe right)
     *
     * @throws Exception
     * @step. ^I navigate back from conversation
     */
    @When("^I navigate back from conversation$")
    public void INavigateBackFromConversation() throws Exception {
        getConversationViewPage().navigateBack(1000);
    }

    /**
     * Tap new message notification in conversation view
     *
     * @param message the message content of message notification
     * @throws Exception
     * @step. ^I tap new message notification "(.*)"$
     */
    @When("^I tap new message notification \"(.*)\"$")
    public void IChangeConversationByClickMessageNotification(String message) throws Exception {
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
                buttonType), getConversationViewPage().isAudioRecordingButtonVisible(buttonType));
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
        for (String nameAlias : CommonSteps.splitAliases(participantNameAliases)) {
            participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias).getName());
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
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMsg = message + " " + contact;
        Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", expectedMsg),
                getConversationViewPage().waitForPeopleMessage(expectedMsg));
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
            userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
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
     * Verify that Conversation contains missed call from contact
     *
     * @param contact contact name string
     * @throws Exception
     * @step. ^I see missed call from (.*) in the conversation$
     */
    @Then("^I see missed call from (.*) in the conversation$")
    public void ISeeMissedCallFrom(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        final String expectedMessage = contact + " CALLED";
        Assert.assertTrue(String.format("Missed call message '%s' is not visible in the conversation view", expectedMessage),
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
    public void ISeeDialogPictureIsAnimated(String destination) throws Exception {
        final PictureDestination dst = PictureDestination.valueOf(destination.toUpperCase());
        double avgThreshold;
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        switch (dst) {
            case CONVERSATION:
                avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getRecentPictureScreenshot, maxFrames,
                        screenshotingDelay);
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
     * Check whether unsent indicator is shown next to a new picture in the convo view
     *
     * @throws Exception
     * @step. ^I see unsent indicator next to new picture in the conversation
     */
    @Then("^I see unsent indicator next to new picture in the conversation")
    public void ISeeUnsentIndictatorNextToAPicture() throws Exception {
        Assert.assertTrue("There is no unsent indicator next to a picture in the conversation view",
                getConversationViewPage().waitForAPictureWithUnsentIndicator());
    }

    /**
     * Verifies that after deleting there is no content in the conversation view
     *
     * @throws Exception
     * @step. ^I see there is no content in the conversation$
     */
    @Then("^I see there is no content in the conversation$")
    public void ISeeThereIsNoContentInTheConversation() throws Exception {
        int actualValue = getConversationViewPage().getCurrentNumberOfItemsInConversation();
        Assert.assertEquals("It looks like the conversation has some content", actualValue, 0);
    }

    /**
     * Store the screenshot of current convo view into internal variable
     *
     * @throws Exception
     * @step. ^I remember the conversation view$
     */
    @And("^I remember the conversation view$")
    public void IRememberConvoViewState() throws Exception {
        conversationViewState.remember();
    }

    /**
     * Verify that conversation view is different from what was remembered before
     *
     * @param shouldNotBeChanged equals to null is the view should be changed
     * @throws Exception
     * @step. ^I see the conversation view is (not )?changed$
     */
    @Then("^I see the conversation view is (not )?changed$")
    public void ISeeTheConvoViewISChanged(String shouldNotBeChanged) throws Exception {
        if (shouldNotBeChanged == null) {
            Assert.assertTrue("State of conversation view has not been changed",
                    conversationViewState.isChanged(CONVO_VIEW_STATE_CHANGE_TIMEOUT, CONVO_VIEW_MIN_SIMILARITY_SCORE));
        } else {
            Assert.assertTrue("State of conversation view has been changed",
                    conversationViewState.isNotChanged(CONVO_VIEW_STATE_CHANGE_TIMEOUT, CONVO_VIEW_MIN_SIMILARITY_SCORE));
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

    /**
     * Checks to see that an unsent indicator is present next to the particular message in the chat history
     *
     * @param msg the expected conversation message
     * @throws Exception
     * @step. ^I see unsent indicator next to "(.*)" in the conversation view$
     */
    @Then("^I see unsent indicator next to \"(.*)\" in the conversation view$")
    public void ISeeUnsentIndicatorNextToTheMessage(String msg) throws Exception {
        Assert.assertTrue(String.format(
                "Unsent indicator has not been shown next to the '%s' message in the conversation view", msg),
                getConversationViewPage().waitForUnsentIndicator(msg));
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
     * Checks the conversation title should be <conversationNameAliases>
     *
     * @param conversationNameAliases The expected conversation name aliases
     * @throws Exception
     * @step. ^the conversation title should be "(.*)"$
     */
    @Then("^the conversation title should be \"(.*)\"$")
    public void TheConversationTitleShouldBe(String conversationNameAliases) throws Exception {
        String expectedConversationNames = usrMgr.replaceAliasesOccurences(conversationNameAliases, FindBy.NAME_ALIAS)
                .replaceAll(",", ", ");
        Assert.assertTrue(String.format("The conversation title should be %s", expectedConversationNames),
                getConversationViewPage().isConversationTitleVisible(expectedConversationNames));
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
     * @step. ^I( do not)? see the result of (.*) file (upload|received)? having name "(.*)" and extension "(\w+)"( in \d+
     * seconds)?( failed)?$
     */
    @Then("^I( do not)? see the result of (.*) file (upload|received)? having name \"(.*)\"" +
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
            Assert.assertTrue("The text input should be visible", getConversationViewPage().isTextInputVisible());
        } else {
            Assert.assertTrue("The text input should be invisible", getConversationViewPage().isTextInputInvisible());
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
     * Check the self avatar on text input
     *
     * @throws Exception
     * @step. ^I see self avatar on text input$
     */
    @Then("^I see self avatar on text input$")
    public void ISeeSelfAvatarOnTextInput() throws Exception {
        Assert.assertTrue("The self avatar should be visible on text input",
                getConversationViewPage().isSelfAvatarOnTextInputVisible());
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
        Assert.assertTrue(String.format("The message menu button '%s' should be %s", name,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Tap the corresponding message bottom menu button.
     *
     * @param name one of possible message bottom menu button name
     * @throws Exception
     * @step. ^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit) button on the message bottom menu$
     */
    @When("^I tap (Delete only for me|Delete for everyone|Copy|Forward|Edit) button on the message bottom menu$")
    public void ITapMessageBottomMenuButton(String name) throws Exception {
        getConversationViewPage().tapMessageBottomMenuButton(name);
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
    @When("^I (long )?tap the (Ping|Text) message \"(.*)\" in the conversation view$")
    public void ITapTheNonTextMessage(String isLongTap, String messageType, String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        if (isLongTap == null) {
            switch (messageType.toLowerCase()) {
                case "ping":
                    getConversationViewPage().tapPingMessage(message);
                    break;
                case "text":
                    getConversationViewPage().tapMessage(message);
                    break;
                default:
                    throw new IllegalStateException(String.format("Cannot tap on %s message", messageType));
            }
        } else {
            switch (messageType.toLowerCase()) {
                case "ping":
                    getConversationViewPage().longTapPingMessage(message);
                    break;
                case "text":
                    getConversationViewPage().longTapMessage(message);
                    break;
                default:
                    throw new IllegalStateException(String.format("Cannot long tap on %s message", messageType));
            }
        }
    }

    /**
     * Verify whether container is visible in the conversation
     *
     * @param shouldNotSee  equals to null if the container should be visible
     * @param containerType euiter Youtube or Soundcloud or File Upload or Video Message
     * @throws Exception
     * @step. ^I (do not )?see (Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container in the conversation view$
     */
    @Then("^I (do not )?see (Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) " +
            "container in the conversation view$")
    public void ISeeContainer(String shouldNotSee, String containerType) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().isContainerVisible(containerType) :
                getConversationViewPage().isContainerInvisible(containerType);
        Assert.assertTrue(String.format("%s should be %s in the conversation view", containerType,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
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
     * @param isLongTap     equals to null if this should be ordinary single tap
     * @param containerType one of available container types
     * @throws Exception
     * @step. ^I (long )?tap (Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) container in the conversation view$
     */
    @When("^I (long )?tap (Youtube|Soundcloud|File Upload|Video Message|Audio Message|Share Location|Link Preview) " +
            "container in the conversation view$")
    public void ITapContainer(String isLongTap, String containerType) throws Exception {
        if (isLongTap == null) {
            getConversationViewPage().tapContainer(containerType);
        } else {
            getConversationViewPage().longTapContainer(containerType);
        }
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
     * @throws Exception
     * @step. ^I see (?:Play|X|Retry) button on the recent video message in the conversation view$
     */
    @When("^I see (?:Play|X|Retry) button on the recent video message in the conversation view$")
    public void ISeeButtonOnVideoMessage() throws Exception {
        Assert.assertTrue("The button is not visible on the recent video message",
                getConversationViewPage().isVideoMessageButtonVisible());
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
    public void IRememberPlayButtonState(String buttonType) throws Exception {
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

    private static final double MIN_UPLOAD_TO_PLAY_SCORE = 0.75;

    /**
     * Wait until audio message upload completed
     *
     * @param timeoutSeconds seconds to wait for upload completed
     * @throws Exception
     * @step. ^I wait for (\d+) seconds? until audio message (?:download|upload) completed$
     */
    @Then("^I wait for (\\d+) seconds? until audio message (?:download|upload) completed$")
    public void IWaitUntilMessageUploaded(int timeoutSeconds) throws Exception {
        final BufferedImage cancelBntInitialState = ImageUtil.readImageFromFile(
                AndroidCommonUtils.getImagesPath(AndroidCommonUtils.class) + "android_audio_msg_cancel_btn.png");
        audioMessagePlayButtonState.remember(cancelBntInitialState);
        Assert.assertTrue(String.format(
                "After %s seconds audio message is still being uploaded", timeoutSeconds),
                audioMessagePlayButtonState.isChanged(timeoutSeconds, MIN_UPLOAD_TO_PLAY_SCORE, ImageUtil.RESIZE_TO_MAX_SCORE));
    }

    private static final double MIN_PLAY_BUTTON_SCORE = 0.82;
    private static final int PLAY_BUTTON_STATE_CHANGE_TIMEOUT = 10; //seconds

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
    private static final int AUDIOMESSAGE_SEEKBAR_STATE_CHANGE_TIMEOUT = 20; //seconds

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
    private static final int AUDIOMESSAGE_MICROPHONE_STATE_CHANGE_TIMEOUT = 10; //seconds

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
     * @step. ^I see audio message is recording$
     */
    @Then("^I see audio message is recording$")
    public void ISeeOngoingAudioMessageRecording() throws Exception {
        Assert.assertTrue("The audio message recording slide should be visible",
                getConversationViewPage().isAudioMessageRecordingSlideVisible());
        Assert.assertTrue("The audio message recording play button should be visible",
                getConversationViewPage().isAudioRecordingButtonVisible("Play"));
        Assert.assertTrue("The audio message recording send button should be visible",
                getConversationViewPage().isAudioRecordingButtonVisible("Send"));
        Assert.assertTrue("The audio message recording cancel button should be visible",
                getConversationViewPage().isAudioRecordingButtonVisible("Cancel"));
        Assert.assertTrue("The audio message recording duration should be visible",
                getConversationViewPage().isAudioMessageRecordingDurationVisible());

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
        name = ClientUsersManager.getInstance().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Cannot see the trashcan next to the name '%s'", name),
                    getConversationViewPage().waitUntilTrashIconVisible(name));
        } else {
            Assert.assertTrue(String.format("The trashcan next to the name '%s' should be invisible", name),
                    getConversationViewPage().waitUntilTrashIconInvisible(name));
        }
    }
}
