package com.wearezeta.auto.ios.steps;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private DialogPage getDialogPage() throws Exception {
        return pagesCollection.getPage(DialogPage.class);
    }

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(OtherUserPersonalInfoPage.class);
    }

    @When("^I see dialog page$")
    public void WhenISeeDialogPage() throws Exception {
        Assert.assertTrue("Cursor input is not visible", getDialogPage().waitForCursorInputVisible());
    }

    @When("^I tap on text input$")
    public void WhenITapOnTextInput() throws Exception {
        getDialogPage().tapOnCursorInput();
        if (CommonUtils.getIsSimulatorFromConfig(getClass()) && !getDialogPage().isKeyboardVisible()) {
            IOSSimulatorHelper.toggleSoftwareKeyboard();
        }
    }

    /**
     * Tap on text input to scroll to the end of conversation
     *
     * @throws Exception
     * @step. ^I tap on text input to scroll to the end$
     */
    @When("^I tap on text input to scroll to the end$")
    public void WhenITapOnTextInputToScroll() throws Exception {
        getDialogPage().tapOnCursorInput();
        getDialogPage().hideKeyboard();
    }

    /**
     * Verify that text input is not allowed
     *
     * @throws Exception
     * @step. I see text input in dialog is not allowed
     */
    @When("I see text input in dialog is not allowed")
    public void ISeeTextInputIsNotAllowed() throws Exception {
        Assert.assertFalse("Text input is allowed", getDialogPage().isCursorInputVisible());
    }

    @When("^I type the (default|\".*\") message$")
    public void WhenITypeTheMessage(String msg) throws Exception {
        if (msg.equals("default")) {
            getDialogPage().typeMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        } else {
            getDialogPage().typeMessage(msg.replaceAll("^\"|\"$", ""));
        }
    }

    /**
     * Verify whether the particular system message is visible in the conversation view
     *
     * @param expectedMsg the expected system message. may contyain user name aliases
     * @throws Exception
     * @step. ^I see "(.*)" system message in the conversation view$
     */
    @Then("^I see \"(.*)\" system message in the conversation view$")
    public void ISeeSystemMessage(String expectedMsg) throws Exception {
        expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("The expected system message '%s' is not visible in the conversation",
                expectedMsg), getDialogPage().isSystemMessageVisible(expectedMsg));
    }

    @Then("^I see User (.*) Pinged message in the conversation$")
    public void ISeeUserPingedMessageTheDialog(String user) throws Throwable {
        String username = usrMgr.findUserByNameOrNameAlias(user).getName();
        String expectedPingMessage = username.toUpperCase() + " PINGED";
        Assert.assertTrue(getDialogPage().isPartOfTextMessageVisible(expectedPingMessage));
    }

    @When("^I type the (default|\".*\") message and send it$")
    public void ITypeTheMessageAndSendIt(String msg) throws Exception {
        if (msg.equals("default")) {
            getDialogPage().typeAndSendConversationMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        } else {
            getDialogPage().typeAndSendConversationMessage(msg.replaceAll("^\"|\"$", ""));
        }
    }

    /**
     * Taps "Paste" item in popup menu of an input field and commit pasted text
     *
     * @throws Exception
     * @step. ^I paste and commit the text$
     */
    @When("^I paste and commit the text$")
    public void IClickPopupPaste() throws Exception {
        getDialogPage().pasteAndCommit();
    }

    /**
     * Click open conversation details button in 1:1 dialog
     *
     * @throws Exception if other user personal profile page was not created
     * @step. ^I open conversation details$
     */
    @When("^I open conversation details$")
    public void IOpenConversationDetails() throws Exception {
        getDialogPage().openConversationDetails();
    }

    @When("^I send the message$")
    public void WhenISendTheMessage() throws Throwable {
        getDialogPage().clickKeyboardCommitButton();
    }

    @When("^I swipe up on dialog page to open other user personal page$")
    public void WhenISwipeUpOnDialogPage() throws Exception {
        getDialogPage().swipeUp(1000);
    }

    @Then("^I see (\\d+) (default )?messages? in the dialog$")
    public void ThenISeeMessageInTheDialog(int expectedCount, String isDefault) throws Exception {
        int actualCount;
        if (isDefault == null) {
            actualCount = getDialogPage().getMessagesCount();
        } else {
            actualCount = getDialogPage().getMessagesCount(
                    CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        }
        Assert.assertTrue(
                String.format("The actual messages count is different " +
                                "from the expected count: %s != %s",
                        actualCount, expectedCount), actualCount == expectedCount);
    }

    @Then("^I see last message in dialog (is|contains) expected message (.*)")
    public void ThenISeeLasMessageInTheDialogIsExpected(String operation, String msg) throws Exception {
        if (!Normalizer.isNormalized(msg, Form.NFC)) {
            msg = Normalizer.normalize(msg, Form.NFC);
        }
        if (operation.equals("is")) {
            Assert.assertTrue(
                    String.format("The last message in the conversation is different from the expected one '%s'",
                            msg), getDialogPage().isLastMessageEqual(msg));
        } else {
            Assert.assertTrue(
                    String.format("The last message in the conversation does not contain the expected one '%s'",
                            msg), getDialogPage().isLastMessageContain(msg));
        }
    }

    /**
     * Verify whether the expected message exists in the convo view
     *
     * @param expectedMsg the expected message. It can contain user name aliases
     * @param shouldNot   equals to null if the message is visible in conversation view
     * @throws Exception
     * @step. ^I (do not )?see the conversation view contains message (.*)
     */
    @Then("^I (do not )?see the conversation view contains message (.*)")
    public void ISeeConversationMessage(String shouldNot, String expectedMsg) throws Exception {
        expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
        if (shouldNot == null) {
            Assert.assertTrue(
                    String.format("The expected message '%s' is not visible in the conversation view", expectedMsg),
                    getDialogPage().isPartOfTextMessageVisible(expectedMsg));
        } else {
            Assert.assertTrue(
                    String.format("The expected message '%s' is not visible in the conversation view", expectedMsg),
                    getDialogPage().waitUntilPartOfTextMessageIsNotVisible(expectedMsg));
        }
    }

    /**
     * Swipe left on text input to close options buttons
     *
     * @throws Exception
     * @step. ^I swipe left on options buttons$
     */
    @When("^I swipe left on options buttons$")
    public void ISwipeLeftTextInput() throws Exception {
        getDialogPage().swipeLeftToShowInputCursor();
    }

    /**
     * Swipe right text input to reveal option buttons
     * <p>
     * !!! The step is unstable on Simulator
     *
     * @throws Exception
     * @step. ^I swipe right text input to reveal option buttons$
     */
    @When("^I swipe right text input to reveal option buttons$")
    public void ISwipeTheTextInputCursor() throws Exception {
        getDialogPage().swipeRightToShowConversationTools();
    }

    @When("^I press Add Picture button$")
    public void IPressAddPictureButton() throws Exception {
        getDialogPage().pressAddPictureButton();
    }

    /**
     * Click call button to start a call
     *
     * @throws Exception
     * @step. ^I press call button$
     */
    @When("^I press call button$")
    public void IPressCallButton() throws Exception {
        getDialogPage().pressCallButton();
    }

    @When("^I click Ping button$")
    public void IPressPingButton() throws Exception {
        getDialogPage().pressPingButton();
    }

    /**
     * Click on Video call button
     *
     * @throws Exception
     * @step. ^I click Video Call button$
     */
    @When("^I click Video Call button$")
    public void IPressVideoCallButton() throws Exception {
        getDialogPage().pressVideoCallButton();
    }

    @Then("^I see Pending Connect to (.*) message on Dialog page$")
    public void ISeePendingConnectMessage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("Connecting to %s is not visible", contact),
                getDialogPage().isConnectingToUserConversationLabelVisible(contact));
    }

    private static final long IMAGE_VISIBILITY_TIMEOUT = 10000; //milliseconds

    @Then("^I see (\\d+) photos? in the dialog$")
    public void ISeeNewPhotoInTheDialog(int expectedCount) throws Exception {
        int actualCount = getDialogPage().getCountOfImages();
        if (actualCount > 0 && expectedCount > 1 && actualCount < expectedCount) {
            final long millisecondsStarted = System.currentTimeMillis();
            do {
                actualCount = getDialogPage().getCountOfImages();
                if (actualCount >= expectedCount) {
                    break;
                }
                Thread.sleep(500);
            } while (System.currentTimeMillis() - millisecondsStarted <= IMAGE_VISIBILITY_TIMEOUT);
        }
        Assert.assertTrue(String.format("The actual count of images in the conversation view %s " +
                "does not equal to the expected count %s", actualCount, expectedCount), actualCount == expectedCount);
    }

    @When("I click video container for the first time")
    public void IPlayVideoFirstTime() throws Exception {
        getDialogPage().clickOnPlayVideoButton();
    }

    @When("I swipe right on Dialog page")
    public void ISwipeRightOnDialogPage() throws Exception {
        for (int i = 0; i < 3; i++) {
            getDialogPage().swipeRight(1000,
                    DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 28);
            if (getContactListPage().waitUntilSelfButtonIsDisplayed()) {
                break;
            }
        }
    }

    @When("^I post media link (.*)$")
    public void IPostMediaLink(String link) throws Throwable {
        getDialogPage().typeAndSendConversationMessage(link);
    }

    /**
     * Type and send invitation link from pointed user in a conversation
     *
     * @param user username
     * @throws Throwable
     * @step. ^I type and send invitaion link from user (.*)$
     */
    @When("^I type and send invitation link from user (.*)$")
    public void ITypeAndSendInvitationLinkFrom(String user) throws Throwable {
        String link = CommonSteps.getInstance().GetInvitationUrl(user);
        getDialogPage().typeAndSendConversationMessage(link);
    }

    @When("^I tap media container$")
    public void ITapMediaContainer() throws Throwable {
        getDialogPage().startMediaContent();
    }

    @When("^I scroll media out of sight until media bar appears$")
    public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception {
        Assert.assertTrue("Media bar is not displayed after the view has been scrolled to the top",
                getDialogPage().scrollDownTillMediaBarAppears());
    }

    @When("^I pause playing the media in media bar$")
    public void IPausePlayingTheMediaInMediaBar() throws Exception {
        getDialogPage().pauseMediaContent();
    }

    /**
     * Verify that Media disappears after the timeout
     *
     * @param timeoutSeconds number of seconds to wait
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds for media bar to disappear$
     */
    @Then("^I wait up to (\\d+) seconds for media bar to disappear$")
    public void IVerifyMediaBarIsNotVisible(int timeoutSeconds) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            if (getDialogPage().isMediaBarNotVisibled()) {
                return;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        throw new AssertionError(String.format("The media bar is still visible after %s seconds timeout",
                timeoutSeconds));
    }

    @When("^I press play in media bar$")
    public void IPressPlayInMediaBar() throws Exception {
        getDialogPage().playMediaContent();
    }

    @When("^I stop media in media bar$")
    public void IStopMediaInMediaBar() throws Exception {
        getDialogPage().stopMediaContent();
    }

    private ElementState previousMediaContainerState = new ElementState(
            () -> getDialogPage().getMediaContainerStateGlyphScreenshot()
    );

    /**
     * Store the current media container state into an internal varibale
     *
     * @throws Exception
     * @step. ^I remember media container state$
     */
    @When("^I remember media container state$")
    public void IRememberContainerState() throws Exception {
        previousMediaContainerState.remember();
    }

    private static final int MEDIA_STATE_CHANGE_TIMEOUT = 10;

    /**
     * Verify whether the state of a media container is changed
     *
     * @param shouldNotChange equals to null if the state should not be changed
     * @throws Exception
     * @step. ^I see media container state is (not )?changed$
     */
    @Then("^I see media container state is (not )?changed$")
    public void IVerifyContainerState(String shouldNotChange) throws Exception {
        if (this.previousMediaContainerState == null) {
            throw new IllegalStateException("Please remember the previous container state first");
        }
        final double minScore = 0.8;
        if (shouldNotChange == null) {
            Assert.assertTrue(String.format("The current media state is not different from the expected one after " +
                            "%s seconds timeout", MEDIA_STATE_CHANGE_TIMEOUT),
                    previousMediaContainerState.isChanged(MEDIA_STATE_CHANGE_TIMEOUT, minScore));
        } else {
            Assert.assertTrue(String.format("The current media state is different from the expected one after " +
                            "%s seconds timeout", MEDIA_STATE_CHANGE_TIMEOUT),
                    previousMediaContainerState.isNotChanged(MEDIA_STATE_CHANGE_TIMEOUT, minScore));
        }
    }

    @Then("^I see media is (playing|stopped|paused) on [Mm]edia [Bb]ar$")
    public void TheMediaIs(String expectedState) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        String currentState;
        do {
            currentState = getDialogPage().getMediaStateFromMediaBar();
            if (expectedState.equals("playing") && currentState.equals(DialogPage.MEDIA_STATE_PLAYING) ||
                    expectedState.equals("stopped") && currentState.equals(DialogPage.MEDIA_STATE_STOPPED) ||
                    expectedState.equals("paused") && currentState.equals(DialogPage.MEDIA_STATE_PAUSED)) {
                return;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= MEDIA_STATE_CHANGE_TIMEOUT);
        throw new AssertionError(String.format("The current media state '%s' is different from the expected one after " +
                "%s seconds timeout", currentState, MEDIA_STATE_CHANGE_TIMEOUT / 1000));
    }

    @Then("^I see media bar on dialog page$")
    public void ISeeMediaBarInDialog() throws Exception {
        Assert.assertTrue(getDialogPage().isMediaBarDisplayed());
    }

    @Then("^I dont see media bar on dialog page$")
    public void ISeeMediaBarDisappear() throws Exception {
        Assert.assertTrue(getDialogPage().waitMediabarClose());
    }

    @When("^I tap on the media bar$")
    public void ITapOnMediaBar() throws Exception {
        getDialogPage().tapOnMediaBar();
    }

    @Then("^I see conversation view is scrolled back to the playing media link (.*)")
    public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link) throws Throwable {
        Assert.assertTrue(String.format("The last conversation message does not contain text '%s'", link),
                getDialogPage().isLastMessageContain(link));
        Assert.assertTrue("View did not scroll back", getDialogPage()
                .isMediaContainerVisible());
    }

    @When("I tap and hold image to open full screen")
    public void ITapImageToOpenFullScreen() throws Throwable {
        getDialogPage().tapImageToOpen();
    }

    @Then("^I navigate back to conversations list")
    public void INavigateToConversationsList() throws Exception {
        getDialogPage().returnToConversationsList();
    }

    @When("I tap and hold on message input")
    public void ITapHoldTextInput() throws Exception {
        getDialogPage().tapHoldTextInput();
    }

    @When("^I scroll to the beginning of the conversation$")
    public void IScrollToTheBeginningOfTheConversation() throws Throwable {
        getDialogPage().scrollToBeginningOfConversation();
    }

    /**
     * Checks if the copied content from send an invite via mail is correct
     *
     * @param mail Email thats the invite sent from
     * @throws Exception
     * @step. ^I check copied content from (.*)$
     */
    @Then("^I check copied content from (.*)$")
    public void ICheckCopiedContentFrom(String mail) throws Exception {
        final String finalString = String.format("I’m on Wire. Search for %s",
                usrMgr.findUserByNameOrNameAlias(mail).getEmail());
        Assert.assertTrue(String.format("The last message in the chat does not contain '%s' part",
                finalString), getDialogPage().isLastMessageContain(finalString));
    }

    /**
     * Check that missed call UI is visible in dialog
     *
     * @param contact User name who called
     * @throws Exception
     * @step. ^I see missed call from contact (.*)$
     */
    @When("^I see missed call from contact (.*)$")
    public void ISeeMissedCall(String contact) throws Exception {
        String username = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Missed call message for '%s' is missing in conversation view", username),
                getDialogPage().isMissedCallButtonVisibleFor(username));
    }

    /**
     * Start a call by clicking missed call button in dialog
     *
     * @param contact User name who called
     * @throws Exception
     * @step. ^I click missed call button to call contact (.*)$
     */
    @When("^I click missed call button to call contact (.*)$")
    public void IClickMissedCallButton(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getDialogPage().clickOnCallButtonForContact(contact.toUpperCase());
    }

    public static final String TAP_OR_SLIDE = "TAP OR SLIDE";

    /**
     * Observing tutorial "swipe right" aka "tap or slide"
     *
     * @throws Exception
     * @step. ^I see TAPORSLIDE text$
     */
    @Then("^I see TAPORSLIDE text$")
    public void ISeeTapOrSlideText() throws Exception {
        boolean result = getDialogPage().isTypeOrSlideExists(TAP_OR_SLIDE);
        Assert.assertTrue(result);
    }

    /**
     * Clicking on video play button in youtube player
     *
     * @throws Exception
     * @step. ^I click play video button$
     */
    @When("I click play video button")
    public void IClickPlayButton() throws Exception {
        getDialogPage().clickOnPlayVideoButton();
    }

    /**
     * Types in the tag for giphy and opens preview page
     *
     * @param message Tag to be fetched from giphy
     * @throws Exception
     * @step. ^I type tag for giphy preview (.*) and open preview overlay$
     */
    @When("^I type tag for giphy preview (.*) and open preview overlay$")
    public void ITypeGiphyTagAndOpenPreview(String message) throws Exception {
        getDialogPage().typeMessage(message);
        getDialogPage().openGifPreviewPage();
    }

    /**
     * Verify giphy is presented in conversation
     *
     * @throws Exception
     * @step. I see giphy in conversation
     */
    @When("I see giphy in conversation")
    public void ISeeGiphyInConversation() throws Exception {
        Assert.assertTrue("There is no giphy in conversation", getDialogPage()
                .isGiphyImageVisible());
    }

    /**
     * Opens the sketch feature
     *
     * @throws Exception
     * @step. ^I tap on sketch button in cursor$
     */
    @When("^I tap on sketch button in cursor$")
    public void ITapSketchButton() throws Exception {
        getDialogPage().openSketch();
    }

    /**
     * Verify my user name in conversation view
     *
     * @param name String name - my user name
     * @throws Exception
     * @step. I see my user name (.*) in conversation
     */
    @When("I see my user name (.*) in conversation")
    public void ISeeMyNameInDialog(String name) throws Exception {
        Assert.assertTrue("My name: " + name + " is not displayed in dialog",
                getDialogPage().isMyNameInDialogDisplayed(name));
    }

    /**
     * Verify if dialog page with pointed user is shown. It's ok to use only if
     * there is not or small amount of messages in dialog.
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I see dialog page with contact (.*)$
     */
    @When("^I see dialog page with contact (.*)$")
    public void ISeeDialogPageWithContact(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue("Dialog with user is not visible", getDialogPage()
                .isConnectedToUserStartedConversationLabelVisible(contact));
    }

    /**
     * Verify is plus button is visible
     *
     * @throws Exception
     * @step. ^I see plus button next to text input$
     */
    @When("^I see plus button next to text input$")
    public void ISeePlusButtonNextInput() throws Exception {
        Assert.assertTrue("Plus button is not visible", getDialogPage().isPlusButtonVisible());
    }

    /**
     * Verify is plus button is not visible
     *
     * @throws Exception
     * @step. ^I see plus button is not shown$
     */
    @When("^I see plus button is not shown$")
    public void ISeePlusButtonNotShown() throws Exception {
        Assert.assertTrue("Plus button is still shown", getDialogPage().waitPlusButtonNotVisible());
    }

    /**
     * Verify Details button is visible
     *
     * @throws Exception
     * @step. ^I see Details button is visible$
     */
    @When("^I see Details button is visible$")
    public void ISeeDetailsButtonShown() throws Exception {
        Assert.assertTrue("Details button is not visible", getDialogPage().isOpenConversationDetailsButtonVisible());
    }

    /**
     * Verify Call button is visible
     *
     * @throws Exception
     * @step. ^I see Call button is visible$
     */
    @When("^I see Call button is visible$")
    public void ISeeCalButtonShown() throws Exception {
        Assert.assertTrue("Call button is not visible", getDialogPage().isCallButtonVisible());
    }

    /**
     * Verify Camera button is visible
     *
     * @throws Exception
     * @step. ^I see Camera button is visible$
     */
    @When("^I see Camera button is visible$")
    public void ISeeCameraButtonShown() throws Exception {
        Assert.assertTrue("Camera button is not visible", getDialogPage().isCameraButtonVisible());
    }

    /**
     * Verify Sketch button is visible
     *
     * @throws Exception
     * @step. ^I see Sketch button is visible$
     */
    @When("^I see Sketch button is visible$")
    public void ISeeSketchButtonShown() throws Exception {
        Assert.assertTrue("Sketch button is not visible", getDialogPage().isOpenSketchButtonVisible());
    }

    /**
     * Verify Buttons: Details, Call, Camera, Sketch are visible
     *
     * @throws Exception
     * @step. ^I see conversation tools buttons$
     */
    @When("^I see conversation tools buttons$")
    public void ISeeButtonsDetailsCallCameraSketchPing() throws Exception {
        ISeeDetailsButtonShown();
        Assert.assertTrue("Some of expected input tools buttons are not visible",
                getDialogPage().areInputToolsVisible());
    }

    /**
     * Verify that only Details button is shown. Rest button should not be
     * visible
     *
     * @throws Exception
     * @step. ^I see no other conversation tools buttons except of Details$
     */
    @When("^I see no other conversation tools buttons except of Details$")
    public void ISeeOnlyDetailsButtonRestNotShown() throws Exception {
        ISeeDetailsButtonShown();
        Assert.assertTrue("Some of input tools buttons are still visible",
                getDialogPage().areInputToolsInvisible());
    }

    /**
     * Verify Close button in options is NOT shown
     *
     * @throws Exception
     * @step. ^I see Close input options button is not visible$
     */
    @When("^I see Close input options button is not visible$")
    public void ISeeCloseButtonInputOptionsNotVisible() throws Exception {
        Assert.assertTrue("Close input options button is visible", getDialogPage()
                .verifyInputOptionsCloseButtonNotVisible());
    }

    /**
     * Click on plus button next to text input
     *
     * @throws Exception
     * @step. ^I click plus button next to text input$
     */
    @When("^I click plus button next to text input$")
    public void IClickPlusButton() throws Exception {
        getDialogPage().clickPlusButton();
    }

    /**
     * Click on close button in input options
     *
     * @throws Exception
     * @step. ^I click close button next to text input$
     */
    @When("^I click Close input options button$")
    public void IClickCloseButtonInputOptions() throws Exception {
        getDialogPage().clickInputOptionsCloseButton();
    }

    /**
     * Verifies that vimeo link without ID is shown but NO player
     *
     * @param link of the vimeo video without ID
     * @throws Exception
     * @step. ^I see vimeo link (.*) but NO media player$
     */
    @Then("^I see vimeo link (.*) but NO media player$")
    public void ISeeVimeoLinkButNOMediaPlayer(String link) throws Exception {
        Assert.assertFalse("Media player is shown in dialog", getDialogPage()
                .isYoutubeContainerVisible());
        Assert.assertTrue(String.format("The last conversation message does not contain %s link", link),
                getDialogPage().isLastMessageContain(link));
    }

    /**
     * Verifies that vimeo link and the video container is visible
     *
     * @param link of vimeo video
     * @throws Exception
     * @step. ^I see vimeo link (.*) and media in dialog$
     */
    @Then("^I see vimeo link (.*) and media in dialog$")
    public void ISeeVimeoLinkAndMediaInDialog(String link) throws Exception {
        Assert.assertTrue("Media is missing in dialog", getDialogPage()
                .isYoutubeContainerVisible());
        Assert.assertTrue(String.format("The last conversation message does not contain %s link", link),
                getDialogPage().isLastMessageContain(link));
    }

    /**
     * Verifies amount of messages in conversation
     *
     * @param expectedCount expected number of messages
     * @throws Exception
     * @step. ^I see (\\d+) conversation (?:entries|entry)$
     */
    @When("^I see (\\d+) conversation (?:entries|entry)$")
    public void ISeeXConvoEntries(int expectedCount) throws Exception {
        Assert.assertEquals("The expected count of conversation entries is not equal to the actual count",
                expectedCount, getDialogPage().getNumberOfMessageEntries());
    }

    /**
     * Tap on the sent link to open it
     * There is no way to simply detect the position of the link in the message cell
     * That is why we assume it is located at the beginning of the string
     *
     * @param msgStartingWithLink the message containing a clickable link at the beginning
     * @throws Exception
     * @step. ^I tap on message "(.*)"$
     */
    @When("^I tap on message \"(.*)\"$")
    public void ITapOnLink(String msgStartingWithLink) throws Exception {
        getDialogPage().tapMessage(msgStartingWithLink);
    }

    /**
     * Verify that input field contains expected text message
     *
     * @throws Exception
     * @step. ^I see the default message in input field$
     */
    @When("^I see the default message in input field$")
    public void WhenISeeMessageInInputField() throws Exception {
        Assert.assertTrue("Input field has incorrect message or empty",
                CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE.equals(getDialogPage().getStringFromInput()));
    }

    /**
     * Verifies that 'Connected to username' message is the only message in
     * dialog
     *
     * @param username name of the contact
     * @throws Exception
     * @step. ^I see the only message in dialog is system message CONNECTED TO
     * (.*)$
     */
    @When("^I see the system message CONNECTED TO (.*) in the conversation view$")
    public void ISeeLastMessageIsSystem(String username) throws Exception {
        username = usrMgr.findUserByNameOrNameAlias(username).getName();
        Assert.assertTrue(String.format("The 'CONNECTED' TO %s' system message is not visible in the conversation view",
                username), getDialogPage().isConnectedToUserStartedConversationLabelVisible(username));
    }

    /**
     * Long press on the image displayed in the conversation
     *
     * @throws Exception
     * @step. ^I longpress on image in the conversation$
     */
    @When("^I longpress on image in the conversation$")
    public void ILongPressOnImage() throws Exception {
        getDialogPage().tapHoldImage();
    }

    /**
     * Clicking on copy badge/icon/window in conversation
     *
     * @throws Exception
     * @step. ^I tap on copy badge$
     */
    @When("^I tap on copy badge$")
    public void ITapCopyBadge() throws Exception {
        getDialogPage().clickPopupCopyButton();
    }

    /**
     * Verify plus icon is replaced with user avatar icon
     *
     * @throws Exception
     * @step. ^I see plus icon is changed to user avatar icon$
     */
    @When("^I see plus icon is changed to user avatar icon$")
    public void ISeePluseIconChangedToUserAvatar() throws Exception {
        Assert.assertFalse("Plus icon is still visible", getDialogPage()
                .isPlusButtonVisible());
        Assert.assertTrue("User avatar is not visible", getDialogPage()
                .isUserAvatarNextToInputVisible());
    }

    /**
     * Clear conversation text input
     *
     * @throws Exception
     * @step. I clear conversation text input$
     */
    @When("^I clear conversation text input$")
    public void IClearConversationTextInput() throws Exception {
        getDialogPage().clearTextInput();
    }

    /**
     * Verify that conversation is scrolled to the end by verifying that plus
     * button and text input is visible
     *
     * @throws Throwable
     * @step. ^I see conversation is scrolled to the end$
     */
    @When("^I see conversation is scrolled to the end$")
    public void ISeeConversationIsScrolledToEnd() throws Throwable {
        Assert.assertTrue("The input field state looks incorrect",
                getDialogPage().isPlusButtonVisible() && getDialogPage().isCursorInputVisible());
    }

    /**
     * Clicks the send button on the keyboard
     *
     * @throws Exception
     * @step. ^I click send button on keyboard
     */
    @When("^I click send button on keyboard$")
    public void iClickSendButtonOnKeyboard() throws Exception {
        getDialogPage().clickKeyboardCommitButton();
    }

    /**
     * Verify whether shield icon is visible next to convo input field
     *
     * @param shouldNotSee equals to null is the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon next to conversation input field$"
     */
    @Then("^I (do not )?see shield icon next to conversation input field$")
    public void ISeeShieldIconNextNextToInputField(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible next to the convo input field",
                    getDialogPage().isShieldIconVisibleNextToInputField());
        } else {
            Assert.assertTrue("The shield icon is visible next to the convo input field, but should be hidden",
                    getDialogPage().isShieldIconInvisibleNextToInputField());
        }
    }

    /**
     * Click on THIS DEVICE link from YOU STARTED USING system message
     *
     * @throws Exception
     * @step. "^I tap on THIS DEVICE link$
     */
    @When("^I tap on THIS DEVICE link$")
    public void ITapThisDeviceLink() throws Exception {
        //getDialogPage().clickThisDeviceLink();
        //this is the fix because it can not locate system message label
        getOtherUserPersonalInfoPage().openDeviceDetailsPage(1);
    }

    /**
     * Presses Resend button in dialog to send last msg again
     *
     * @throws Exception
     * @step. ^I resend the last message in the conversation with Resend button$
     */
    @When("^I resend the last message in the conversation with Resend button$")
    public void IResendTheLastMessageToUserInDialog() throws Exception {
        getDialogPage().resendLastMessageInDialogToUser();
    }

}
