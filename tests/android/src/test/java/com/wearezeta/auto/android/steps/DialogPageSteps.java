package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.CallingOverlayPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private DialogPage getDialogPage() throws Exception {
        return pagesCollection.getPage(DialogPage.class);
    }

    private CallingOverlayPage getCallingOverlayPage() throws Exception {
        return pagesCollection.getPage(CallingOverlayPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final String ANDROID_LONG_MESSAGE = CommonUtils
            .generateRandomString(300);
    private static final String LONG_MESSAGE_ALIAS = "LONG_MESSAGE";

    private static String expandMessage(String message) {
        final Map<String, String> specialStrings = new HashMap<String, String>();
        specialStrings.put(LONG_MESSAGE_ALIAS, ANDROID_LONG_MESSAGE);
        if (specialStrings.containsKey(message)) {
            return specialStrings.get(message);
        } else {
            return message;
        }
    }

    /**
     * Waits for the dialog page to appear This step makes no assertions and
     * doesn't fail if the dialog page doesn't appear.
     *
     * @throws Exception
     * @step. ^I see dialog page$
     */
    @When("^I( do not)? see dialog page$")
    public void WhenISeeDialogPage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    "The cursor is not visible in the conversation view",
                    getDialogPage().waitForCursorInputVisible());
        } else {
            Assert.assertTrue(
                    "The cursor in the conversation view is still visible",
                    getDialogPage().waitForCursorInputNotVisible());
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
        getDialogPage().tapOnCursorInput();
    }

    /**
     * Send message to the chat
     *
     * @param msg message to type. There are several special shortcuts:
     *            LONG_MESSAGE - to type long message
     * @throws Exception
     * @step. ^I type the message \"(.*)\" and send it$
     */
    @When("^I type the message \"(.*)\" and send it$")
    public void ITypeMessageAndSendIt(String msg) throws Exception {
        getDialogPage().typeAndSendMessage(expandMessage(msg));
    }

    /**
     * Inputs a custom message and does NOT send it
     *
     * @param msg message to type. There are several special shortcuts:
     *            LONG_MESSAGE - to type long message
     * @throws Exception
     * @step. ^I type the message \"(.*)\"$
     */
    @When("^I type the message \"(.*)\"$")
    public void ITypeMessage(String msg) throws Exception {
        getDialogPage().typeMessage(expandMessage(msg));
    }

    /**
     * Sends the message by pressing the keyboard send button
     *
     * @throws Exception
     * @step. ^I send the message$
     */
    @When("^I send the message$")
    public void ISendTheMessage() throws Exception {
        getDialogPage().pressKeyboardSendButton();
    }

    /**
     * Swipes the text input area to reveal the different input options
     *
     * @throws Exception
     * @step. ^I swipe on text input$
     */
    @When("^I swipe on text input$")
    public void WhenISwipeOnTextInput() throws Exception {
        getDialogPage().swipeRightOnCursorInput();
    }

    /**
     * Presses the image input button to open the camera or gallery
     *
     * @throws Exception
     * @step. ^I press Add Picture button$
     */
    @When("^I press Add Picture button$")
    public void WhenIPressAddPictureButton() throws Exception {
        getDialogPage().tapAddPictureBtn();
    }

    /**
     * Press on the ping button in the input controls
     *
     * @throws Exception
     * @step. ^I press Ping button$
     */
    @When("^I press Ping button$")
    public void WhenIPressPButton() throws Exception {
        getDialogPage().tapPingBtn();
    }

    /**
     * Press on the call button in the input controls
     *
     * @throws Exception
     * @step. ^I press Call button$
     */
    @When("^I press Call button$")
    public void WhenIPressCallButton() throws Exception {
        getDialogPage().tapCallBtn();
    }

    /**
     * Press close button for input options
     *
     * @throws Exception
     * @step. ^I close input options$
     */
    @When("^I close input options$")
    public void ICloseInputOptions() throws Exception {
        getDialogPage().closeInputOptions();
    }

    /**
     * Press on the sketch button in the input controls
     *
     * @throws Exception
     * @step. ^I press Sketch button$
     */
    @When("^I press Sketch button$")
    public void WhenIPressOnSketchButton() throws Exception {
        getDialogPage().tapSketchBtn();
    }

    /**
     * Press on the mute button in the calling controls
     *
     * @throws Exception
     * @step. ^I press Mute button$
     */
    @When("^I press Mute button$")
    public void WhenIPressMuteButton() throws Exception {
        getDialogPage().tapMuteBtn();
    }

    /**
     * Press on the Speaker button in the calling controls
     *
     * @throws Exception
     * @step. ^I press Speaker button$
     */
    @When("^I press Speaker button$")
    public void WhenIPressSpeakerButton() throws Exception {
        getDialogPage().tapSpeakerBtn();
    }

    /**
     * Press on the Cancel call button in the Calling controls
     *
     * @throws Exception
     * @step. ^I press Cancel call button$
     */
    @When("^I press Cancel call button$")
    public void WhenIPressCancelCallButton() throws Exception {
        getDialogPage().tapCancelCallBtn();
    }

    private final Map<String, BufferedImage> savedButtonStates = new HashMap<String, BufferedImage>();

    /**
     * Takes screenshot of current button state for the further comparison
     *
     * @param buttonName the name of the button to take screenshot. Available values:
     *                   MUTE, SPEAKER
     * @throws Exception
     * @step. ^I remember the current state of (\\w+) button$
     */
    @When("^I remember the current state of (\\w+) button$")
    public void IRememberButtonState(String buttonName) throws Exception {
        savedButtonStates.put(buttonName, getDialogPage()
                .getCurrentButtonStateScreenshot(buttonName));
    }

    private static final long BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS = 15000;
    private static final double BUTTON_STATE_OVERLAP_MAX_SCORE = 0.4d;

    /**
     * Checks to see if a certain calling button state is changed. Make sure,
     * that the screenshot of previous state is already taken for this
     * particular button
     *
     * @param buttonName the name of the button to check. Available values: MUTE,
     *                   SPEAKER
     * @throws Exception
     * @step. ^I see (\\w+) button state is changed$
     */
    @Then("^I see (\\w+) button state is changed$")
    public void ICheckButtonStateIsChanged(String buttonName) throws Exception {
        if (!savedButtonStates.containsKey(buttonName)) {
            throw new IllegalStateException(
                    String.format(
                            "Please call the corresponding step to take the screenshot of previous '%s' button state first",
                            buttonName));
        }
        final BufferedImage previousStateScreenshot = savedButtonStates
                .get(buttonName);
        final long millisecondsStarted = System.currentTimeMillis();
        double overlapScore;
        do {
            final BufferedImage currentStateScreenshot = getDialogPage()
                    .getCurrentButtonStateScreenshot(buttonName);
            overlapScore = ImageUtil.getOverlapScore(currentStateScreenshot,
                    previousStateScreenshot, ImageUtil.RESIZE_TO_MAX_SCORE);
            if (overlapScore <= BUTTON_STATE_OVERLAP_MAX_SCORE) {
                return;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS);
        throw new AssertionError(
                String.format(
                        "Button state has not been changed within %s seconds timeout. Current overlap score: %.2f, expected overlap score: <= %.2f",
                        BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS / 1000,
                        overlapScore, BUTTON_STATE_OVERLAP_MAX_SCORE));
    }

    /**
     * Checks to see if call overlay is present or not
     *
     * @param shouldNotSee is set to null if " do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see call overlay$
     */
    @Then("^I( do not)? see call overlay$")
    public void WhenISeeCallOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Call overlay not visible",
                    getCallingOverlayPage().waitUntilVisible());
        } else {
            Assert.assertTrue(
                    "Call overlay is visible, it should have been dismissed",
                    getCallingOverlayPage().waitUntilNotVisible());
        }
    }

    /**
     * Tap on Dialog page bottom for scrolling page to the end
     *
     * @throws Exception
     * @step. ^I scroll to the bottom of conversation view$
     */
    @When("^I scroll to the bottom of conversation view$")
    public void IScrollToTheBottom() throws Exception {
        getDialogPage().scrollToTheBottom();
    }

    /**
     * Tap in Dialog page on details button to open participants view
     *
     * @throws Exception
     * @step. ^I tap conversation details button$
     */
    @When("^I tap conversation details button$")
    public void WhenITapConversationDetailsBottom() throws Exception {
        getDialogPage().pressPlusButtonOnDialogPage();
        getDialogPage().tapConversationDetailsButton();
    }

    /**
     * Tap on Play/Pause media item button
     *
     * @throws Exception
     * @step. ^I press PlayPause media item button$
     */
    @When("^I press PlayPause media item button$")
    public void WhenIPressPlayPauseButton() throws Exception {
        getDialogPage().tapPlayPauseBtn();
    }

    /**
     * Verify that Play button is visible on youtube container
     *
     * @throws Exception
     * @step. ^I see Play button on youtube container$
     */
    @When("^I see Play button on [Yy]outube container$")
    public void ISeePlayButtonOnYoutubeContainer() throws Exception {
        Assert.assertTrue(
                "Youtube Play button is not visible, but it should be",
                getDialogPage().waitUntilYoutubePlayButtonVisible());
    }

    /**
     * Tap on Play/Pause button on Media Bar
     *
     * @throws Exception
     * @step. ^I press PlayPause on Mediabar button$
     */
    @When("^I press PlayPause on Mediabar button$")
    public void WhenIPressPlayPauseOnMediaBarButton() throws Exception {
        getDialogPage().tapPlayPauseMediaBarBtn();
    }

    /**
     * Presses a given button name Not clear which page is returned from a given
     * action
     *
     * @param buttonName the button to press
     * @throws Exception
     * @step. ^I press \"(.*)\" button$
     */
    @When("^I press \"(.*)\" button$")
    public void WhenIPressButton(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "take photo":
                getDialogPage().takePhoto();
                break;
            case "confirm":
                getDialogPage().confirm();
                break;
            case "gallery":
                getDialogPage().openGallery();
                break;
            case "image close":
                getDialogPage().closeFullScreenImage();
                break;
            case "switch camera":
                if (!getDialogPage().tapSwitchCameraButton()) {
                    throw new PendingException("Device under test does not have front camera. " +
                            "Skipping all the further verification...");
                }
                break;
            case "sketch image paint":
                getDialogPage().tapSketchOnImageButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", buttonName));
        }
    }

    /**
     * Used to check that a ping has been sent Not very clear what this step
     * does
     *
     * @param message
     * @throws Exception
     * @step. ^I see Ping message (.*) in the dialog$
     */
    @Then("^I see Ping message (.*) in the dialog$")
    public void ThenISeePingMessageInTheDialog(String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format(
                "Ping message '%s' is not visible after the timeout", message),
                getDialogPage().waitForPingMessageWithText(message));
    }

    /**
     * Checks to see that a message that has been sent appears in the chat
     * history
     *
     * @throws Exception
     * @step. ^I see my message \"(.*)\" in the dialog$
     */
    @Then("^I see my message \"(.*)\" in the dialog$")
    public void ThenISeeMyMessageInTheDialog(String msg) throws Exception {
        Assert.assertTrue(
                String.format(
                        "The message '%s' is not visible in the conversation view",
                        msg), getDialogPage()
                        .waitForMessage(expandMessage(msg)));
    }

    /**
     * Checks to see that an unsent indicator is present next to the particular
     * message in the chat history
     *
     * @throws Exception
     * @step. ^I see unsent indicator next to the message \"(.*)\" in the
     * dialog$
     */
    @Then("^I see unsent indicator next to the message \"(.*)\" in the dialog$")
    public void ThenISeeUnsentIndicatorNextToTheMessage(String msg)
            throws Exception {
        Assert.assertTrue(
                String.format(
                        "Unsent indicator has not been shown next to the '%s' message in the conversation view",
                        msg), getDialogPage().waitForUnsentIndicator(msg));
    }

    /**
     * Checks to see that a photo exists in the chat history. Does not check
     * which photo though
     *
     * @param shouldNotSee equals to null if 'do not' part does not exist
     * @throws Throwable
     * @step. ^I (do not )?see new (?:photo|picture) in the dialog$
     */
    @Then("^I (do not )?see new (?:photo|picture) in the dialog$")
    public void ThenISeeNewPhotoInTheDialog(String shouldNotSee)
            throws Throwable {
        if (shouldNotSee == null) {
            Assert.assertTrue("No new photo is present in the chat",
                    getDialogPage().isImageExists());
        } else {
            Assert.assertTrue(
                    "A photo is present in the chat, but it should not be vivible",
                    getDialogPage().isImageInvisible());
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
        getDialogPage().clickLastImageFromDialog();
    }

    private static final int SWIPE_DURATION_MILLISECONDS = 1300;

    /**
     * @throws Exception
     * @step. ^I swipe up on dialog page
     */
    @When("^I swipe up on dialog page$")
    public void WhenISwipeUpOnDialogPage() throws Exception {
        getDialogPage().dialogsPagesSwipeUp(SWIPE_DURATION_MILLISECONDS);
    }

    /**
     * Swipe down on dialog page
     *
     * @throws Exception
     * @step. ^I swipe down on dialog page$
     */
    @When("^I swipe down on dialog page$")
    public void WhenISwipedownOnDialogPage() throws Exception {
        getDialogPage().dialogsPagesSwipeDown(SWIPE_DURATION_MILLISECONDS);
    }

    private static final int MAX_SWIPES = 5;

    /**
     * Swipe down on dialog page until Mediabar appears
     *
     * @throws Exception
     * @step. ^I swipe down on dialog page until Mediabar appears$
     */
    @When("^I swipe down on dialog page until Mediabar appears$")
    public void ISwipedownOnDialogPageUntilMediaBarAppears() throws Exception {
        Assert.assertTrue("Media Bar is not visible", getDialogPage()
                .scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    /**
     * Navigates back to the contact list page using back button (disabled using
     * a swipe right)
     *
     * @throws Exception
     * @step. ^I navigate back from dialog page$
     */
    @When("^I navigate back from dialog page$")
    public void WhenINavigateBackFromDialogPage() throws Exception {
        getDialogPage().navigateBack(1000);
    }

    /**
     * Checks to see that the "Connected To XYZ" appears at the start of a new
     * dialog (Should changed step name to "Connected to")
     *
     * @param contact
     * @throws Exception
     * @step. ^I see Connect to (.*) Dialog page$
     */
    @Then("^I see Connect to (.*) Dialog page$")
    public void ThenIseeConnectToDialogPage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String actualLabel = getDialogPage().getConnectRequestChatLabel();
        Assert.assertTrue(String.format(
                "The actual label '%s' does not contain '%s' part",
                actualLabel, contact),
                actualLabel.toLowerCase().contains(contact.toLowerCase()));
    }

    /**
     * Seems to currently be blocked out in all tests
     *
     * @param iconLabel
     * @throws Exception
     * @step. ^I see (.*) icon$
     */
    @Then("^I see (.*) icon$")
    public void ThenIseeIcon(String iconLabel) throws Exception {
        final double score = getDialogPage().checkPingIcon(iconLabel);
        Assert.assertTrue(
                "Overlap between two images has not enough score. Expected >= 0.75, current = "
                        + score, score >= 0.75d);
    }

    // ------- From Group Chat Page
    public static final String userRemovedMessage = "YOU REMOVED ";

    /**
     * Checks to see that a group chat exists, where the name of the group chat
     * is the list of users
     *
     * @param participantNameAliases
     * @throws Exception
     * @step. ^I see group chat page with users (.*)$
     */
    @Then("^I see group chat page with users (.*)$")
    public void ThenISeeGroupChatPage(String participantNameAliases)
            throws Exception {
        assert getDialogPage().isDialogVisible() : "Group chat view is not visible";
        List<String> participantNames = new ArrayList<String>();
        for (String nameAlias : CommonSteps
                .splitAliases(participantNameAliases)) {
            participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias)
                    .getName());
        }
        Assert.assertTrue(getDialogPage().isGroupChatDialogContainsNames(
                participantNames));
    }

    /**
     * Used to check that the message "YOU REMOVED XYZ" from group chat appears
     *
     * @param message
     * @param contact
     * @throws Exception
     * @step. ^I see message (.*) contact (.*) on group page$
     */
    @Then("^I see message (.*) contact (.*) on group page$")
    public void ThenISeeMessageContactOnGroupPage(String message, String contact)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMsg = message + " " + contact;
        Assert.assertTrue(String.format(
                "The message '%s' is not visible in the conversation view",
                expectedMsg), getDialogPage().waitForMessage(expectedMsg));
    }

    /**
     * Checks to see that after the group was renamed, the user is informed of
     * the change in the dialog page
     *
     * @param newConversationName the new conversation name to check for
     * @throws Exception
     * @step. ^I see a message informing me that I renamed the conversation to
     * (.*)$
     */
    @Then("^I see a message informing me that I renamed the conversation to (.*)$")
    public void ThenISeeMessageInformingGroupRename(String newConversationName)
            throws Exception {
        Assert.assertTrue(
                String.format(
                        "The new conversation name '%s' has not been shown in the conversation view",
                        newConversationName),
                getDialogPage().waitForConversationNameChangedMessage(
                        newConversationName));
    }

    /**
     * Used once to check that the last message sent is the same as what is
     * expected
     *
     * @param message
     * @throws Exception
     * @step. ^Last message is (.*)$
     */
    @Then("^Last message is (.*)$")
    public void ThenLastMessageIs(String message) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        final int secondsTimeout = 10;
        while (System.currentTimeMillis() - millisecondsStarted <= secondsTimeout * 1000) {
            if (message
                    .toLowerCase()
                    .trim()
                    .equals(getDialogPage().getLastMessageFromDialog()
                            .toLowerCase().trim())) {
                return;
            }
            Thread.sleep(500);
        }
        Assert.assertEquals(message.toLowerCase().trim(), getDialogPage()
                .getLastMessageFromDialog().toLowerCase().trim());
    }

    /**
     * Verify that I see Play or Pause button on Mediabar
     *
     * @throws Exception
     * @step. ^I see (.*) on Mediabar$
     */
    @Then("^I see (.*) on Mediabar$")
    public void ThenIseeOnMediaBar(String iconLabel) throws Exception {
        final double score = getDialogPage()
                .getMediaBarControlIconOverlapScore(iconLabel);
        Assert.assertTrue(
                "Overlap between two images has not enough score. Expected >= 0.75, current = "
                        + score, score >= 0.75d);
    }

    private BufferedImage previousMediaButtonState = null;

    /**
     * Store the screenshot of current media control button state
     *
     * @throws Exception
     * @step. ^I remember the state of PlayPause media item button$
     */
    @When("^I remember the state of PlayPause media item button$")
    public void IRememeberMediaItemButtonState() throws Exception {
        previousMediaButtonState = getDialogPage()
                .getMediaControlButtonScreenshot();
    }

    final static double MAX_OVERLAP_SCORE = 0.97;

    /**
     * Verify the current state of media control button has been changed since
     * the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of PlayPause media item button is changed$
     */
    @Then("^I verify the state of PlayPause media item button is changed$")
    public void IVerifyStateOfMediaControlButtonIsChanged() throws Exception {
        if (previousMediaButtonState == null) {
            throw new IllegalStateException(
                    "Please take a screenshot of previous button state first");
        }
        int ntry = 1;
        double overlapScore = 1.0;
        do {
            final BufferedImage currentMediaButtonState = getDialogPage()
                    .getMediaControlButtonScreenshot();
            overlapScore = ImageUtil.getOverlapScore(previousMediaButtonState,
                    currentMediaButtonState, ImageUtil.RESIZE_NORESIZE);
            if (overlapScore < MAX_OVERLAP_SCORE) {
                return;
            }
            Thread.sleep(1500);
            ntry++;
        } while (ntry <= 3);
        throw new AssertionError(
                String.format(
                        "It seems like the state of media control button has not been changed (%.2f >= %.2f)",
                        overlapScore, MAX_OVERLAP_SCORE));
    }

    /**
     * Verify that dialog page contains missed call from contact
     *
     * @param contact contact name string
     * @throws Exception
     * @step. ^I see dialog with missed call from (.*)$
     */
    @Then("^I see dialog with missed call from (.*)$")
    public void ThenISeeDialogWithMissedCallFrom(String contact)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMessage = contact + " CALLED";
        Assert.assertTrue(
                String.format(
                        "Missed call message '%s' is not visible in the conversation view",
                        expectedMessage), getDialogPage()
                        .waitUntilMissedCallMessageIsVisible(expectedMessage));
    }

    /**
     * Swipes on calling bar to dismiss a call
     *
     * @throws Exception
     * @step. ^I dismiss calling bar by swipe$
     */
    @When("I dismiss calling bar by swipe$")
    public void IDismissCalling() throws Exception {
        Assert.assertTrue("Call overlay is not visible, nothing to swipe",
                getCallingOverlayPage().waitUntilVisible());
        getDialogPage().swipeByCoordinates(1500, 30, 25, 30, 5);
    }

    /**
     * Checks to see if join group call overlay is present or not
     *
     * @param shouldNotSee is set to null if " do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see join group call overlay$
     */
    @Then("^I( do not)? see join group call overlay$")
    public void WhenISeeGroupCallJoinOverlay(String shouldNotSee)
            throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Join group call overlay not visible",
                    getCallingOverlayPage().waitUntilGroupCallJoinVisible());
        } else {
            Assert.assertTrue(
                    "Join group call overlay is visible, it should have been dismissed",
                    getCallingOverlayPage().waitUntilGroupCallJoinNotVisible());
        }
    }

    /**
     * Checks to see if join group call overlay is present or not
     *
     * @param name text on the button
     * @throws Exception
     * @step. ^I see \"(.*)\" button$
     */
    @Then("^I( do not)? see \"(.*)\" button$")
    public void WhenISeeGroupCallJoinButton(String shouldNotSee, String name)
            throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(name
                            + " button with not visible in group call overlay",
                    getCallingOverlayPage()
                            .waitUntilJoinGroupCallButtonVisible(name));
        } else {
            Assert.assertTrue(name
                            + " button with not visible in group call overlay",
                    getCallingOverlayPage()
                            .waitUntilJoinGroupCallButtonNotVisible(name));
        }
    }

    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;

    private static enum PictureDestination {
        DIALOG, PREVIEW;
    }

    /**
     * Verify whether a picture in dialog/preview is animated
     *
     * @param destination either "dialog" or "preview"
     * @throws Exception
     * @step. ^I see the picture in the (dialog|preview) is animated$
     */
    @Then("^I see the picture in the (dialog|preview) is animated$")
    public void ISeeDialogPictureIsAnimated(String destination)
            throws Exception {
        final PictureDestination dst = PictureDestination.valueOf(destination
                .toUpperCase());
        double avgThreshold;
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        switch (dst) {
            case DIALOG:
                avgThreshold = ImageUtil.getAnimationThreshold(
                        getDialogPage()::getRecentPictureScreenshot, maxFrames,
                        screenshotingDelay);
                Assert.assertTrue(
                        String.format(
                                "The picture in the conversation view seems to be static (%.2f >= %.2f)",
                                avgThreshold, MAX_SIMILARITY_THRESHOLD),
                        avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
            case PREVIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(
                        getDialogPage()::getPreviewPictureScreenshot, maxFrames,
                        screenshotingDelay);
                Assert.assertTrue(
                        String.format(
                                "The picture in the image preview view seems to be static (%.2f >= %.2f)",
                                avgThreshold, MAX_SIMILARITY_THRESHOLD),
                        avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
        }
    }

    /**
     * Check whether unsent indicator is shown next to a new picture in the
     * convo view
     *
     * @throws Exception
     * @step. ^I see unsent indicator next to new picture in the dialog$
     */
    @Then("^I see unsent indicator next to new picture in the dialog$")
    public void ISeeUnsentIndictatorNextToAPicture() throws Exception {
        Assert.assertTrue(
                "There is no unsent indicator next to a picture in the conversation view",
                getDialogPage().waitForAPictureWithUnsentIndicator());
    }

    /**
     * Verifies that after deleting there is no content in the conversation view
     *
     * @throws Exception
     * @step. ^I see there is no content in the conversation$
     */
    @Then("^I see there is no content in the conversation$")
    public void ISeeThereIsNoContentInTheConversation() throws Exception {
        int actualValue = getDialogPage().getCurrentNumberOfItemsInDialog();
        Assert.assertEquals("It looks like the conversation has some content", actualValue, 0);
    }

    private BufferedImage previousConvoViewScreenshot = null;

    /**
     * Store the screenshot of current convo view into internal variable
     *
     * @throws Exception
     * @step. ^I remember the conversation view$
     */
    @And("^I remember the conversation view$")
    public void IRememberConvoViewState() throws Exception {
        previousConvoViewScreenshot = getDialogPage().getConvoViewScreenshot();
    }

    private final static double MAX_CONVO_VIEW_SIMILARIITY = 0.97;

    /**
     * Verify that conversation view is different from what was remembered before
     *
     * @param shouldNotBeChanged equals to null is the view should be changed
     * @throws Exception
     * @step. ^I see the conversation view is (not )?changed$
     */
    @Then("^I see the conversation view is (not )?changed$")
    public void ISeeTheConvoViewISChanged(String shouldNotBeChanged) throws Exception {
        if (previousConvoViewScreenshot == null) {
            throw new IllegalStateException(
                    "Please remember the previous state of conversation view first");
        }
        final BufferedImage currentConvoViewScreenshot = getDialogPage().getConvoViewScreenshot();
        final double similarity = ImageUtil.getOverlapScore(previousConvoViewScreenshot,
                currentConvoViewScreenshot, ImageUtil.RESIZE_TO_MAX_SCORE);
        if (shouldNotBeChanged == null) {
            Assert.assertTrue(String.format(
                    "Current state of conversation view is similar to what what was remembered before (%.2f >= %.2f)",
                    similarity, MAX_CONVO_VIEW_SIMILARIITY),
                    similarity < MAX_CONVO_VIEW_SIMILARIITY);
        } else {
            Assert.assertTrue(String.format(
                    "Current state of conversation view is different to what what was remembered before (%.2f < %.2f)",
                    similarity, MAX_CONVO_VIEW_SIMILARIITY),
                    similarity >= MAX_CONVO_VIEW_SIMILARIITY);
        }
    }
}
