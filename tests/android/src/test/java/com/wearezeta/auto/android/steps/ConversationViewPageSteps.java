package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Waits for the dialog page to appear This step makes no assertions and doesn't fail if the dialog page doesn't appear.
     *
     * @throws Exception
     * @step. ^I see dialog page$
     */
    @When("^I( do not)? see dialog page$")
    public void WhenISeeDialogPage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The cursor is not visible in the conversation view",
                    getConversationViewPage().waitForCursorInputVisible());
        } else {
            Assert.assertTrue("The cursor in the conversation view is still visible",
                    getConversationViewPage().waitForCursorInputNotVisible());
        }
    }

    /**
     * Taps on the text input
     *
     * @throws Exception
     * @step. ^I tap on text input$
     */
    @When("^I tap on text input$")
    public void WhenITapOnTextInput() throws Exception {
        getConversationViewPage().tapOnCursorInput();
    }

    /**
     * Send message to the chat
     *
     * @param msg               message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
     * @param doNotHideKeyboard if it equals null, should hide keyboard
     * @throws Exception
     * @step. I type the message "(.*)" and send it( without hiding keyboard)?$
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
     * @step. ^I type the message \"(.*)\"$
     */
    @When("^I type the message \"(.*)\"$")
    public void ITypeMessage(String msg) throws Exception {
        getConversationViewPage().typeMessage(expandMessage(msg));
    }

    /**
     * Swipes the text input area to reveal the different input options
     *
     * @throws Exception
     * @step. ^I swipe on text input$
     */
    @When("^I swipe on text input$")
    public void WhenISwipeOnTextInput() throws Exception {
        getConversationViewPage().swipeRightOnCursorInput();
    }

    /**
     * Tap on plus button in the text input area to reveal the different input options
     *
     * @throws Exception
     * @step. ^I tap plus( button)? i?o?n text input$
     */
    @When("^I tap plus( button)? i?o?n text input$")
    public void WhenITapPlusInTextInput(String ignore) throws Exception {
        getConversationViewPage().pressPlusButtonOnDialogPage();
    }

    /**
     * Press the corresponding button in the input controls
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Add people|Ping|Add Picture|Sketch) button$ from input tools$
     */
    @When("^I tap (Add people|Ping|Add Picture|Sketch) button from input tools$")
    public void WhenITapInputToolButton(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "ping":
                getConversationViewPage().tapPingBtn();
                break;
            case "add picture":
                getConversationViewPage().tapAddPictureBtn();
                break;
            case "sketch":
                getConversationViewPage().tapSketchBtn();
                break;
            case "add people":
                getConversationViewPage().tapPeopleBtn();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }

    /**
     * Press the corresponding button in the top toolbar
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Audio Call|Video Call) button from top toolbar$
     */
    @When("^I tap (Audio Call|Video Call) button from top toolbar$")
    public void WhenITapTopToolbarButton(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "audio call":
                getConversationViewPage().tapAudioCallBtn();
                break;
            case "video call":
                getConversationViewPage().tapVideoCallBtn();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
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
    public void WhenITapConversationDetailsBottom() throws Exception {
        getConversationViewPage().tapTopToolbarTitle();
    }

    /**
     * Tap on Play/Pause media item button
     *
     * @throws Exception
     * @step. ^I press PlayPause media item button$
     */
    @When("^I press PlayPause media item button$")
    public void WhenIPressPlayPauseButton() throws Exception {
        getConversationViewPage().tapPlayPauseBtn();
    }

    /**
     * Verify that Play button is visible on youtube container
     *
     * @throws Exception
     * @step. ^I see Play button on youtube container$
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
    public void WhenIPressPlayPauseOnMediaBarButton() throws Exception {
        getConversationViewPage().tapPlayPauseMediaBarBtn();
    }

    /**
     * Presses a given button name Not clear which page is returned from a given action
     *
     * @param buttonName the button to press
     * @throws Exception
     * @step. ^I press \"(.*)\" button$
     */
    @When("^I press \"(.*)\" button$")
    public void WhenIPressButton(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "take photo":
                getConversationViewPage().takePhoto();
                break;
            case "confirm":
                getConversationViewPage().confirm();
                break;
            case "gallery":
                getConversationViewPage().openGallery();
                break;
            case "image close":
                getConversationViewPage().closeFullScreenImage();
                break;
            case "switch camera":
                if (!getConversationViewPage().tapSwitchCameraButton()) {
                    throw new PendingException(
                            "Device under test does not have front camera. " + "Skipping all the further verification...");
                }
                break;
            case "sketch image paint":
                getConversationViewPage().tapSketchOnImageButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", buttonName));
        }
    }

    /**
     * Used to check that a ping has been sent Not very clear what this step does
     *
     * @param message message text
     * @throws Exception
     * @step. ^I see Ping message (.*) in the dialog$
     */
    @Then("^I see Ping message (.*) in the dialog$")
    public void ThenISeePingMessageInTheDialog(String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Ping message '%s' is not visible after the timeout", message),
                getConversationViewPage().waitForPingMessageWithText(message));
    }

    /**
     * Checks to see that a message that has been sent appears in the chat history
     *
     * @throws Exception
     * @step. ^I see my message \"(.*)\" in the dialog$
     */
    @Then("^I see my message \"(.*)\" in the dialog$")
    public void ThenISeeMyMessageInTheDialog(String msg) throws Exception {
        Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", msg),
                getConversationViewPage().waitForMessage(expandMessage(msg)));
    }

    /**
     * Checks to see that a photo exists in the chat history. Does not check which photo though
     *
     * @param shouldNotSee equals to null if 'do not' part does not exist
     * @throws Throwable
     * @step. ^I (do not )?see new (?:photo|picture) in the dialog$
     */
    @Then("^I (do not )?see new (?:photo|picture) in the dialog$")
    public void ThenISeeNewPhotoInTheDialog(String shouldNotSee) throws Throwable {
        if (shouldNotSee == null) {
            Assert.assertTrue("No new photo is present in the chat", getConversationViewPage().isImageExists());
        } else {
            Assert.assertTrue("A photo is present in the chat, but it should not be vivible",
                    getConversationViewPage().isImageInvisible());
        }
    }

    /**
     * Selects the last picture sent in a conversation view dialog
     *
     * @throws Throwable
     * @step. ^I select last photo in dialog$
     */
    @When("^I select last photo in dialog$")
    public void WhenISelectLastPhotoInDialog() throws Throwable {
        getConversationViewPage().clickLastImageFromDialog();
    }

    /**
     * @throws Exception
     * @step. ^I swipe up on dialog page
     */
    @When("^I swipe up on dialog page$")
    public void WhenISwipeUpOnDialogPage() throws Exception {
        getConversationViewPage().dialogsPagesSwipeUp(SWIPE_DURATION_MILLISECONDS);
    }

    /**
     * Swipe down on dialog page
     *
     * @throws Exception
     * @step. ^I swipe down on dialog page$
     */
    @When("^I swipe down on dialog page$")
    public void WhenISwipedownOnDialogPage() throws Exception {
        getConversationViewPage().dialogsPagesSwipeDown(SWIPE_DURATION_MILLISECONDS);
    }

    /**
     * Swipe down on dialog page until Mediabar appears
     *
     * @throws Exception
     * @step. ^I swipe down on dialog page until Mediabar appears$
     */
    @When("^I swipe down on dialog page until Mediabar appears$")
    public void ISwipedownOnDialogPageUntilMediaBarAppears() throws Exception {
        Assert.assertTrue("Media Bar is not visible", getConversationViewPage().scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    /**
     * Navigates back to the contact list page using back button (disabled using a swipe right)
     *
     * @throws Exception
     * @step. ^I navigate back from dialog page$
     */
    @When("^I navigate back from dialog page$")
    public void WhenINavigateBackFromDialogPage() throws Exception {
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
    public void WhenIChangeConversationByClickMessageNotification(String message) throws Exception {
        getConversationViewPage().tapMessageNotification(message);
    }

    /**
     * Checks to see that the new message notification is visible
     *
     * @param message the message content of message notification
     * @throws Exception
     * @step. ^I see new message notification "(.*)"$
     */
    @Then("^I see new message notification \"(.*)\"$")
    public void WhenISeeNewMessageNotification(String message) throws Exception {
        getConversationViewPage().waitForMessageNotification(message);
    }

    /**
     * Checks to see that a group chat exists, where the name of the group chat is the list of users
     *
     * @param participantNameAliases one or more comma-separated user names/aliases
     * @throws Exception
     * @step. ^I see group chat page with users (.*)$
     */
    @Then("^I see group chat page with users (.*)$")
    public void ThenISeeGroupChatPage(String participantNameAliases) throws Exception {
        List<String> participantNames = new ArrayList<>();
        for (String nameAlias : CommonSteps.splitAliases(participantNameAliases)) {
            participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias).getName());
        }
        Assert.assertTrue(String.format("Group chat view with names %s is not visible", participantNames),
                getConversationViewPage().isConversationMessageContainsNames(participantNames));
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
    public void ThenISeeMessageContactOnGroupPage(String message, String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMsg = message + " " + contact;
        Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", expectedMsg),
                getConversationViewPage().waitForMessage(expectedMsg));
    }

    /**
     * Checks to see that after the group was renamed, the user is informed of the change in the dialog page
     *
     * @param newConversationName the new conversation name to check for
     * @throws Exception
     * @step. ^I see a message informing me that I renamed the conversation to (.*)$
     */
    @Then("^I see a message informing me that I renamed the conversation to (.*)$")
    public void ThenISeeMessageInformingGroupRename(String newConversationName) throws Exception {
        Assert.assertTrue(
                String.format("The new conversation name '%s' has not been shown in the conversation view", newConversationName),
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
    public void ThenISeeVerifiedConversationMessage(String nonVerified, String userName) throws Exception {
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
     * @step. ^^I see the most recent conversation message is (not )?"(.*)"
     */
    @Then("^I see the most recent conversation message is (not )?\"(.*)\"")
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
     * @step. ^I remember the state of PlayPause media item button$
     */
    @When("^I remember the state of PlayPause media item button$")
    public void IRememeberMediaItemButtonState() throws Exception {
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
     * Tap back arrow button in upper toolbar
     *
     * @throws Exception
     * @step. ^I tap back button in upper toolbar$
     */
    @When("^I tap back button in upper toolbar$")
    public void TapBackbuttonInUpperToolbar() throws Exception {
        getConversationViewPage().tapTopToolbarBackButton();
    }

    /**
     * Verify the current state of media control button has been changed since the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of PlayPause media item button is changed$
     */
    @Then("^I verify the state of PlayPause media item button is changed$")
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
     * Verify that dialog page contains missed call from contact
     *
     * @param contact contact name string
     * @throws Exception
     * @step. ^I see dialog with missed call from (.*)$
     */
    @Then("^I see dialog with missed call from (.*)$")
    public void ThenISeeDialogWithMissedCallFrom(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        final String expectedMessage = contact + " CALLED";
        Assert.assertTrue(String.format("Missed call message '%s' is not visible in the conversation view", expectedMessage),
                getConversationViewPage().waitUntilMissedCallMessageIsVisible(expectedMessage));
    }

    /**
     * Verify whether a picture in dialog/preview is animated
     *
     * @param destination either "dialog" or "preview"
     * @throws Exception
     * @step. ^I see the picture in the (dialog|preview) is animated$
     */
    @Then("^I see the picture in the (dialog|preview) is animated$")
    public void ISeeDialogPictureIsAnimated(String destination) throws Exception {
        final PictureDestination dst = PictureDestination.valueOf(destination.toUpperCase());
        double avgThreshold;
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        switch (dst) {
            case DIALOG:
                avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getRecentPictureScreenshot, maxFrames,
                        screenshotingDelay);
                Assert.assertTrue(String.format("The picture in the conversation view seems to be static (%.2f >= %.2f)",
                        avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
            case PREVIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getPreviewPictureScreenshot, maxFrames,
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
     * @step. ^I see unsent indicator next to new picture in the dialog$
     */
    @Then("^I see unsent indicator next to new picture in the dialog$")
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
        int actualValue = getConversationViewPage().getCurrentNumberOfItemsInDialog();
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
     * @step. ^I see message (.*) (\\d+) times? in the conversation view$
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
     * @step. ^I see unsent indicator next to \"(.*)\" in the conversation view$
     */
    @Then("^I see unsent indicator next to \"(.*)\" in the conversation view$")
    public void ThenISeeUnsentIndicatorNextToTheMessage(String msg) throws Exception {
        Assert.assertTrue(String.format(
                "Unsent indicator has not been shown next to the '%s' message in the conversation view", msg),
                getConversationViewPage().waitForUnsentIndicator(msg));
    }

    private enum PictureDestination {
        DIALOG, PREVIEW
    }

    /**
     * Checks to see that upper toolbar is visible
     *
     * @throws Exception
     * @step. ^I see the upper toolbar$
     */
    @Then("^I see the upper toolbar$")
    public void ThenISeeTopToolbar() throws Exception {
        Assert.assertTrue("The upper toolbar is invisible", getConversationViewPage().isTopToolbarVisible());
    }

    /**
     * Checks to see whether Audio/Video call button is visible in upper toolbar
     *
     * @param doNotSee equal null means the Video call button should be visible
     * @throws Exception
     */
    @Then("I( do not)? see the (audio|video) call button in upper toolbar$")
    public void ThenIseeVideoCallButtonInUpperToolbar(String doNotSee, String callType) throws Exception {
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
    public void ThenTheConversationTitleShouldBe(String conversationNameAliases) throws Exception {
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
    public void ThenISeeTheMediaBarIsBelowUpperToolbar() throws Exception {
        Assert.assertTrue("The media bar should below the upper toolbar", getConversationViewPage().isMediaBarBelowUptoolbar());
    }

    /**
     * Check the cursor bar only contains ping, sketch, add picture, people and file buttons in cursor bar
     *
     * @throws Exception
     * @step. ^I only see ping, sketch, camera, people and file buttons in cursor menu
     */
    @Then("^I only see ping, sketch, camera, people and file buttons in cursor menu")
    public void ThenIOnlySeePingSketchAddPicturePeopleButton() throws Exception {
        Assert.assertTrue("Ping button should be visible in cursor menu", getConversationViewPage().isPingButtonVisible());
        Assert.assertTrue("Sketch button should be visible in cursor menu", getConversationViewPage().isSketchButtonVisible());
        Assert.assertTrue("Camera button should be visible in cursor menu", getConversationViewPage().isCameraButtonVisible());
        Assert.assertTrue("People button should be visible in cursor menu", getConversationViewPage().isPeopleButtonVisible());
        Assert.assertTrue("File button should be visible in cursor menu", getConversationViewPage().isFileButtonVisible());
    }
}
