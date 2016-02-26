package com.wearezeta.auto.android.steps;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

public class DialogPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private DialogPage getDialogPage() throws Exception {
        return pagesCollection.getPage(DialogPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private static final String ANDROID_LONG_MESSAGE = CommonUtils.generateRandomString(300);
    private static final String LONG_MESSAGE_ALIAS = "LONG_MESSAGE";

    private static String expandMessage(String message) {
        final Map<String, String> specialStrings = new HashMap<>();
        specialStrings.put(LONG_MESSAGE_ALIAS, ANDROID_LONG_MESSAGE);
        if (specialStrings.containsKey(message)) {
            return specialStrings.get(message);
        } else {
            return message;
        }
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
                    getDialogPage().waitForCursorInputVisible());
        } else {
            Assert.assertTrue("The cursor in the conversation view is still visible",
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
     * @param msg message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
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
     * @param msg message to type. There are several special shortcuts: LONG_MESSAGE - to type long message
     * @throws Exception
     * @step. ^I type the message \"(.*)\"$
     */
    @When("^I type the message \"(.*)\"$")
    public void ITypeMessage(String msg) throws Exception {
        getDialogPage().typeMessage(expandMessage(msg));
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
     * Tap on plus button in the text input area to reveal the different input options
     *
     * @throws Exception
     * @step. ^I tap plus( button)? i?o?n text input$
     */
    @When("^I tap plus( button)? i?o?n text input$")
    public void WhenITapPlusInTextInput(String ignore) throws Exception {
        getDialogPage().pressPlusButtonOnDialogPage();
    }

    /**
     * Press the corresponding button in the input controls
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Call|Ping|Add Picture|Video Call) button$ from input tools$
     */
    @When("^I tap (Call|Ping|Add Picture|Video Call) button from input tools$")
    public void WhenITapInputToolButton(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "call":
                getDialogPage().tapCallBtn();
                break;
            case "ping":
                getDialogPage().tapPingBtn();
                break;
            case "add picture":
                getDialogPage().tapAddPictureBtn();
                break;
            case "video call":
                getDialogPage().tapVideoCallBtn();
                break;
            case "sketch":
                getDialogPage().tapSketchBtn();
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
        getDialogPage().closeInputOptions();
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
        Assert.assertTrue("Youtube Play button is not visible, but it should be",
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
                    throw new PendingException(
                            "Device under test does not have front camera. " + "Skipping all the further verification...");
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
     * Used to check that a ping has been sent Not very clear what this step does
     *
     * @param message
     * @throws Exception
     * @step. ^I see Ping message (.*) in the dialog$
     */
    @Then("^I see Ping message (.*) in the dialog$")
    public void ThenISeePingMessageInTheDialog(String message) throws Exception {
        message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Ping message '%s' is not visible after the timeout", message),
                getDialogPage().waitForPingMessageWithText(message));
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
                getDialogPage().waitForMessage(expandMessage(msg)));
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
            Assert.assertTrue("No new photo is present in the chat", getDialogPage().isImageExists());
        } else {
            Assert.assertTrue("A photo is present in the chat, but it should not be vivible",
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
        Assert.assertTrue("Media Bar is not visible", getDialogPage().scrollUpUntilMediaBarVisible(MAX_SWIPES));
    }

    /**
     * Navigates back to the contact list page using back button (disabled using a swipe right)
     *
     * @throws Exception
     * @step. ^I navigate back from dialog page$
     */
    @When("^I navigate back from dialog page$")
    public void WhenINavigateBackFromDialogPage() throws Exception {
        getDialogPage().navigateBack(1000);
    }

    /**
     * Checks to see that a group chat exists, where the name of the group chat is the list of users
     *
     * @param participantNameAliases
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
                getDialogPage().isGroupChatDialogContainsNames(participantNames));
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
    public void ThenISeeMessageContactOnGroupPage(String message, String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMsg = message + " " + contact;
        Assert.assertTrue(String.format("The message '%s' is not visible in the conversation view", expectedMsg),
                getDialogPage().waitForMessage(expectedMsg));
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
                getDialogPage().waitForConversationNameChangedMessage(newConversationName));
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
                    getDialogPage().waitForOtrVerifiedMessage());
        } else if (userName == null) {
            Assert.assertTrue("The otr non verified conversation message has been shown in the conversation view",
                    getDialogPage().waitForOtrNonVerifiedMessage());
        } else {
            userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
            Assert.assertTrue(String.format(
                    "The otr non verified conversation message caused by user '%s' has been shown in the conversation view",
                    userName), getDialogPage().waitForOtrNonVerifiedMessageCausedByUser(userName));
        }

    }

    /**
     * Used once to check that the last message sent is the same as what is expected
     *
     * @param message
     * @throws Exception
     * @step. ^Last message is (.*)$
     */
    @Then("^Last message is (.*)$")
    public void ThenLastMessageIs(String message) throws Exception {
        Assert.assertTrue(String.format("The last conversation message is not equal to '%s'", message),
                getDialogPage().isLastMessageEqualTo(message, 30));
    }

    /**
     * Store the screenshot of current media control button state
     *
     * @throws Exception
     * @step. ^I remember the state of PlayPause media item button$
     */
    @When("^I remember the state of PlayPause media item button$")
    public void IRememeberMediaItemButtonState() throws Exception {
        getDialogPage().rememberMediaControlButtonState();
    }

    /**
     * Verify the current state of media control button has been changed since the last snapshot was made
     *
     * @throws Exception
     * @step. ^I verify the state of PlayPause media item button is changed$
     */
    @Then("^I verify the state of PlayPause media item button is changed$")
    public void IVerifyStateOfMediaControlButtonIsChanged() throws Exception {
        if (!getDialogPage().mediaControlButtonStateHasChanged()) {
            throw new AssertionError("State of PlayPause media item button has not changed");
        }
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
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        final String expectedMessage = contact + " CALLED";
        Assert.assertTrue(String.format("Missed call message '%s' is not visible in the conversation view", expectedMessage),
                getDialogPage().waitUntilMissedCallMessageIsVisible(expectedMessage));
    }

    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;

    private enum PictureDestination {
        DIALOG,
        PREVIEW;
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
                avgThreshold = ImageUtil.getAnimationThreshold(getDialogPage()::getRecentPictureScreenshot, maxFrames,
                        screenshotingDelay);
                Assert.assertTrue(String.format("The picture in the conversation view seems to be static (%.2f >= %.2f)",
                        avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
                break;
            case PREVIEW:
                avgThreshold = ImageUtil.getAnimationThreshold(getDialogPage()::getPreviewPictureScreenshot, maxFrames,
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

    /**
     * Store the screenshot of current convo view into internal variable
     *
     * @throws Exception
     * @step. ^I remember the conversation view$
     */
    @And("^I remember the conversation view$")
    public void IRememberConvoViewState() throws Exception {
        getDialogPage().rememberConversationView();
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
            if (!getDialogPage().conversationViewStateHasChanged()) {
                throw new AssertionError("State of PlayPause media item button has not changed");
            }
        } else if (!getDialogPage().conversationViewStateHasNotChanged()) {
            throw new AssertionError("State of PlayPause media item button has changed");
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
                getDialogPage().waitForXMessages(msg, times));
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
                getDialogPage().waitForXImages(expectedCount));
    }

    /**
     * Save the state of verified conversation shield into the internal field for the future comparison
     *
     * @throws Exception
     * @step. ^I remember verified conversation shield state$
     */
    @And("^I remember verified conversation shield state$")
    public void IRememberVerifiedConversationShieldState() throws Exception {
        getDialogPage().rememberVerifiedConversationShield();
    }

    /**
     * Verify whether verified conversation shield has changed in comparison to the previous state
     *
     * @throws Exception
     * @step. ^I see verified conversation shield state has changed$
     */
    @Then("^I see verified conversation shield state has changed$")
    public void ISeeVerifiedConversationShieldStateHasChanged() throws Exception {
        if (!getDialogPage().verifiedConversationShieldStateHasChanged()) {
            throw new AssertionError("State of verified conversation shield has not changed");
        }
    }
}
