package com.wearezeta.auto.ios.steps;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.pages.ConversationViewPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationViewPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    public static final String DEFAULT_PNG = "group-icon@3x.png";

    private ConversationViewPage getConversationViewPage() throws Exception {
        return pagesCollection.getPage(ConversationViewPage.class);
    }

    private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage() throws Exception {
        return pagesCollection.getPage(OtherUserPersonalInfoPage.class);
    }

    @When("^I see conversation view page$")
    public void WhenISeePage() throws Exception {
        ISeeTextInput(null);
    }

    @When("^I tap on text input$")
    public void WhenITapOnTextInput() throws Exception {
        getConversationViewPage().tapOnCursorInput();
        if (CommonUtils.getIsSimulatorFromConfig(getClass()) && !getConversationViewPage().isKeyboardVisible()) {
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
        getConversationViewPage().tapOnCursorInput();
        getConversationViewPage().hideKeyboard();
    }

    /**
     * Verify that text input is visible or not
     *
     * @param shouldNotSee equals to null if text input should be visible
     * @throws Exception
     * @step. ^I (do not )?see text input in conversation view$
     */
    @When("^I (do not )?see text input in conversation view$")
    public void ISeeTextInput(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Cursor input is not visible", getConversationViewPage().waitForCursorInputVisible());
        } else {
            Assert.assertTrue("Cursor input is visible, but should be hidden",
                    getConversationViewPage().waitForCursorInputInvisible());
        }
    }

    @When("^I type the (default|\".*\") message$")
    public void WhenITypeTheMessage(String msg) throws Exception {
        if (msg.equals("default")) {
            getConversationViewPage().typeMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        } else {
            getConversationViewPage().typeMessage(msg.replaceAll("^\"|\"$", ""));
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
                expectedMsg), getConversationViewPage().isSystemMessageVisible(expectedMsg));
    }

    @Then("^I see User (.*) Pinged message in the conversation$")
    public void ISeeUserPingedMessageTheDialog(String user) throws Throwable {
        String username = usrMgr.findUserByNameOrNameAlias(user).getName();
        String expectedPingMessage = username.toUpperCase() + " PINGED";
        Assert.assertTrue(getConversationViewPage().isPartOfTextMessageVisible(expectedPingMessage));
    }

    @When("^I type the (default|\".*\") message and send it$")
    public void ITypeTheMessageAndSendIt(String msg) throws Exception {
        if (msg.equals("default")) {
            getConversationViewPage().typeAndSendConversationMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        } else {
            getConversationViewPage().typeAndSendConversationMessage(msg.replaceAll("^\"|\"$", ""));
        }
    }

    /**
     * Taps "Paste" item in popup menu of an input field and commit pasted text
     *
     * @throws Exception
     * @step. ^I paste and commit the text$
     */
    @When("^I paste and commit the text$")
    public void IClickPopupPasteAndCommitText() throws Exception {
        getConversationViewPage().pasteAndCommit();
    }

    /**
     * Click open conversation details button in 1:1 dialog
     *
     * @throws Exception if other user personal profile page was not created
     * @step. ^I open (?:group |\s*)conversation details$
     */
    @When("^I open (?:group |\\s*)conversation details$")
    public void IOpenConversationDetails() throws Exception {
        getConversationViewPage().openConversationDetails();
    }

    @When("^I send the message$")
    public void WhenISendTheMessage() throws Throwable {
        getConversationViewPage().clickKeyboardCommitButton();
    }

    @Then("^I see (\\d+) (default )?messages? in the dialog$")
    public void ThenISeeMessageInTheDialog(int expectedCount, String isDefault) throws Exception {
        int actualCount;
        if (isDefault == null) {
            actualCount = getConversationViewPage().getMessagesCount();
        } else {
            actualCount = getConversationViewPage().getMessagesCount(
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
                            msg), getConversationViewPage().isLastMessageEqual(msg));
        } else {
            Assert.assertTrue(
                    String.format("The last message in the conversation does not contain the expected one '%s'",
                            msg), getConversationViewPage().isLastMessageContain(msg));
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
                    getConversationViewPage().isPartOfTextMessageVisible(expectedMsg));
        } else {
            Assert.assertTrue(
                    String.format("The expected message '%s' is not visible in the conversation view", expectedMsg),
                    getConversationViewPage().waitUntilPartOfTextMessageIsNotVisible(expectedMsg));
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
        getConversationViewPage().swipeLeftToShowInputCursor();
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
        getConversationViewPage().swipeRightToShowConversationTools();
    }

    /**
     * Tap the corresponding button from input tools palette
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Add Picture|Ping|Sketch|File Transfer) button from input tools$
     */
    @When("^I tap (Add Picture|Ping|Sketch|File Transfer) button from input tools$")
    public void IPressAddPictureButton(String btnName) throws Exception {
        getConversationViewPage().clickInputToolButtonByName(btnName);
    }

    /**
     * Verify visibility of the corresponding button in input tools palette
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I (do not)?see (Add Picture|Ping|Sketch|File Transfer) button in input tools palette$
     */
    @When("^I (do not)?see (Add Picture|Ping|Sketch|File Transfer) button in input tools palette$")
    public void VeirfyButtonVisibilityInInputTools(String shouldNot, String btnName) throws Exception {
        if (shouldNot == null) {
            Assert.assertTrue(btnName + "button in input tools palette is not visible",
                    getConversationViewPage().inputToolButtonByNameIsVisible(btnName));
        } else {
            Assert.assertTrue(btnName + "button in input tools palette is  visible",
                    getConversationViewPage().inputToolButtonByNameIsNotVisible((btnName)));
        }

    }

    /**
     * Click call button to start an audio or video call
     *
     * @param btnType either Audio or Video
     * @throws Exception
     * @step. ^I tap (Audio|Video) Call button$
     */
    @When("^I tap (Audio|Video) Call button$")
    public void ITapCallButton(String btnType) throws Exception {
        if (btnType.equals("Audio")) {
            getConversationViewPage().tapAudioCallButton();
        } else {
            getConversationViewPage().tapVideoCallButton();
        }
    }

    @Then("^I see Pending Connect to (.*) message on Dialog page$")
    public void ISeePendingConnectMessage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("Connecting to %s is not visible", contact),
                getConversationViewPage().isConnectingToUserConversationLabelVisible(contact));
    }

    private static final long IMAGE_VISIBILITY_TIMEOUT = 10000; //milliseconds

    @Then("^I see (\\d+) photos? in the dialog$")
    public void ISeeNewPhotoInTheDialog(int expectedCount) throws Exception {
        int actualCount = getConversationViewPage().getCountOfImages();
        if (actualCount > 0 && expectedCount > 1 && actualCount < expectedCount) {
            final long millisecondsStarted = System.currentTimeMillis();
            do {
                actualCount = getConversationViewPage().getCountOfImages();
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
        getConversationViewPage().clickOnPlayVideoButton();
    }

    @When("^I post media link (.*)$")
    public void IPostMediaLink(String link) throws Throwable {
        getConversationViewPage().typeAndSendConversationMessage(link);
    }

    /**
     * Copy to system clipboard, paste and send invitation link from pointed user in a conversation
     *
     * @param user username
     * @throws Exception
     * @step. ^I copy paste and send invitation link from user (.*)$
     */
    @When("^I copy paste and send invitation link from user (.*)$")
    public void ICopyPasteAndSendInvitationLinkFrom(String user) throws Exception {
        String link = CommonSteps.getInstance().GetInvitationUrl(user);
        CommonUtils.setStringValueInSystemClipboard(link);
        IOSSimulatorHelper.copySystemClipboardToSimulatorClipboard();
        ITapHoldTextInput();
        IClickPopupPasteAndCommitText();
    }

    @When("^I tap media container$")
    public void ITapMediaContainer() throws Throwable {
        getConversationViewPage().startMediaContent();
    }

    @When("^I scroll media out of sight until media bar appears$")
    public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception {
        Assert.assertTrue("Media bar is not displayed after the view has been scrolled to the top",
                getConversationViewPage().scrollDownTillMediaBarAppears());
    }

    @When("^I pause playing the media in media bar$")
    public void IPausePlayingTheMediaInMediaBar() throws Exception {
        getConversationViewPage().pauseMediaContent();
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
            if (getConversationViewPage().isMediaBarNotVisibled()) {
                return;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        throw new AssertionError(String.format("The media bar is still visible after %s seconds timeout",
                timeoutSeconds));
    }

    @When("^I press play in media bar$")
    public void IPressPlayInMediaBar() throws Exception {
        getConversationViewPage().playMediaContent();
    }

    @When("^I stop media in media bar$")
    public void IStopMediaInMediaBar() throws Exception {
        getConversationViewPage().stopMediaContent();
    }

    private ElementState previousMediaContainerState = new ElementState(
            () -> getConversationViewPage().getMediaContainerStateGlyphScreenshot()
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
            currentState = getConversationViewPage().getMediaStateFromMediaBar();
            if (expectedState.equals("playing") && currentState.equals(ConversationViewPage.MEDIA_STATE_PLAYING) ||
                    expectedState.equals("stopped") && currentState.equals(ConversationViewPage.MEDIA_STATE_STOPPED) ||
                    expectedState.equals("paused") && currentState.equals(ConversationViewPage.MEDIA_STATE_PAUSED)) {
                return;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= MEDIA_STATE_CHANGE_TIMEOUT);
        throw new AssertionError(String.format("The current media state '%s' is different from the expected one after " +
                "%s seconds timeout", currentState, MEDIA_STATE_CHANGE_TIMEOUT / 1000));
    }

    @Then("^I see media bar on dialog page$")
    public void ISeeMediaBarInDialog() throws Exception {
        Assert.assertTrue(getConversationViewPage().isMediaBarDisplayed());
    }

    @Then("^I dont see media bar on dialog page$")
    public void ISeeMediaBarDisappear() throws Exception {
        Assert.assertTrue(getConversationViewPage().waitMediabarClose());
    }

    @When("^I tap on the media bar$")
    public void ITapOnMediaBar() throws Exception {
        getConversationViewPage().tapOnMediaBar();
    }

    @Then("^I see conversation view is scrolled back to the playing media link (.*)")
    public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link) throws Throwable {
        Assert.assertTrue(String.format("The last conversation message does not contain text '%s'", link),
                getConversationViewPage().isLastMessageContain(link));
        Assert.assertTrue("View did not scroll back", getConversationViewPage()
                .isMediaContainerVisible());
    }

    @When("I tap and hold image to open full screen")
    public void ITapImageToOpenFullScreen() throws Throwable {
        getConversationViewPage().tapImageToOpen();
    }

    @Then("^I navigate back to conversations list")
    public void INavigateToConversationsList() throws Exception {
        getConversationViewPage().returnToConversationsList();
    }

    @When("I tap and hold on message input")
    public void ITapHoldTextInput() throws Exception {
        getConversationViewPage().tapHoldTextInput();
    }

    @When("^I scroll to the beginning of the conversation$")
    public void IScrollToTheBeginningOfTheConversation() throws Throwable {
        getConversationViewPage().scrollToBeginningOfConversation();
    }

    /**
     * Checks if the pasted message contains the particular email
     *
     * @param mail email address/alias
     * @throws Exception
     * @step. ^I verify that pasted content contains (.*)$
     */
    @Then("^I verify that pasted message contains (.*)$")
    public void ICheckCopiedContentFrom(String mail) throws Exception {
        final String finalString = usrMgr.replaceAliasesOccurences(mail, FindBy.EMAIL_ALIAS);
        Assert.assertTrue(String.format("The last message in the chat does not contain '%s' part",
                finalString), getConversationViewPage().isLastMessageContain(finalString));
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
                getConversationViewPage().isMissedCallButtonVisibleFor(username));
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
        getConversationViewPage().clickOnCallButtonForContact(contact.toUpperCase());
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
        boolean result = getConversationViewPage().isTypeOrSlideExists(TAP_OR_SLIDE);
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
        getConversationViewPage().clickOnPlayVideoButton();
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
        getConversationViewPage().typeMessage(message);
        getConversationViewPage().openGifPreviewPage();
    }

    /**
     * Verify giphy is presented in conversation
     *
     * @throws Exception
     * @step. I see giphy in conversation
     */
    @When("I see giphy in conversation")
    public void ISeeGiphyInConversation() throws Exception {
        Assert.assertTrue("There is no giphy in conversation", getConversationViewPage()
                .isGiphyImageVisible());
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
                getConversationViewPage().isMyNameInDialogDisplayed(name));
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
        Assert.assertTrue("Dialog with user is not visible", getConversationViewPage()
                .isConnectedToUserStartedConversationLabelVisible(contact));
    }

    /**
     * Verify is plus button is visible
     *
     * @param shouldNotBeVisible equals to null if the button should not be visible
     * @throws Exception
     * @step. ^I (do not )?see plus button next to text input$
     */
    @When("^I (do not )?see plus button next to text input$")
    public void ISeePlusButtonNextInput(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Plus button is not visible", getConversationViewPage().isPlusButtonVisible());
        } else {
            Assert.assertTrue("Plus button is still shown", getConversationViewPage().waitPlusButtonNotVisible());
        }
    }

    /**
     * Verify tools Buttons are presented after text input swipe
     *
     * @throws Exception
     * @step. ^I see conversation tools buttons$
     */
    @When("^I see conversation tools buttons$")
    public void ISeeToolsButtons() throws Exception {
        Assert.assertTrue("People button is not visible", getConversationViewPage().isPeopleButtonVisible());
        Assert.assertTrue("Some of expected input tools buttons are not visible",
                getConversationViewPage().areInputToolsVisible());
    }

    /**
     * Verify that only People button is shown. Rest button should not be
     * visible
     *
     * @throws Exception
     * @step. ^I see no other conversation tools buttons except of Details$
     */
    @When("^I see no other conversation tools buttons except of People")
    public void ISeeOnlyPeopleButtonRestNotShown() throws Exception {
        Assert.assertTrue("People button is not visible", getConversationViewPage().isPeopleButtonVisible());
        Assert.assertTrue("Some of input tools buttons are still visible",
                getConversationViewPage().areInputToolsInvisible());
    }

    /**
     * Verify Close button in options is NOT shown
     *
     * @throws Exception
     * @step. ^I see Close input options button is not visible$
     */
    @When("^I see Close input options button is not visible$")
    public void ISeeCloseButtonInputOptionsNotVisible() throws Exception {
        Assert.assertTrue("Close input options button is visible",
                getConversationViewPage().verifyInputOptionsCloseButtonNotVisible());
    }

    /**
     * Click on plus button next to text input
     *
     * @throws Exception
     * @step. ^I click plus button next to text input$
     */
    @When("^I click plus button next to text input$")
    public void IClickPlusButton() throws Exception {
        getConversationViewPage().clickPlusButton();
    }

    /**
     * Click on close button in input options
     *
     * @throws Exception
     * @step. ^I click close button next to text input$
     */
    @When("^I click Close input options button$")
    public void IClickCloseButtonInputOptions() throws Exception {
        getConversationViewPage().clickInputOptionsCloseButton();
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
        Assert.assertFalse("Media player is shown in dialog", getConversationViewPage()
                .isYoutubeContainerVisible());
        Assert.assertTrue(String.format("The last conversation message does not contain %s link", link),
                getConversationViewPage().isLastMessageContain(link));
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
        Assert.assertTrue("Media is missing in dialog", getConversationViewPage()
                .isYoutubeContainerVisible());
        Assert.assertTrue(String.format("The last conversation message does not contain %s link", link),
                getConversationViewPage().isLastMessageContain(link));
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
                expectedCount, getConversationViewPage().getNumberOfMessageEntries());
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
        getConversationViewPage().tapMessage(msgStartingWithLink);
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
                CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE.equals(getConversationViewPage().getStringFromInput()));
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
                username), getConversationViewPage().isConnectedToUserStartedConversationLabelVisible(username));
    }

    /**
     * Long press on the image displayed in the conversation
     *
     * @throws Exception
     * @step. ^I longpress on image in the conversation$
     */
    @When("^I longpress on image in the conversation$")
    public void ILongPressOnImage() throws Exception {
        getConversationViewPage().tapHoldImage();
    }

    /**
     * Clicking on copy badge/icon/window in conversation
     *
     * @throws Exception
     * @step. ^I tap on copy badge$
     */
    @When("^I tap on copy badge$")
    public void ITapCopyBadge() throws Exception {
        getConversationViewPage().clickPopupCopyButton();
    }

    /**
     * Verify plus icon is replaced with user avatar icon
     *
     * @throws Exception
     * @step. ^I see plus icon is changed to user avatar icon$
     */
    @When("^I see plus icon is changed to user avatar icon$")
    public void ISeePluseIconChangedToUserAvatar() throws Exception {
        Assert.assertFalse("Plus icon is still visible", getConversationViewPage()
                .isPlusButtonVisible());
        Assert.assertTrue("User avatar is not visible", getConversationViewPage()
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
        getConversationViewPage().clearTextInput();
    }

    /**
     * Verify that conversation is scrolled to the end by verifying that plus
     * button and text input is visible
     *
     * @throws Exception
     * @step. ^I see conversation is scrolled to the end$
     */
    @When("^I see conversation is scrolled to the end$")
    public void ISeeConversationIsScrolledToEnd() throws Exception {
        Assert.assertTrue("The input field state looks incorrect",
                getConversationViewPage().isPlusButtonVisible() && getConversationViewPage().waitForCursorInputVisible());
    }

    /**
     * Clicks the send button on the keyboard
     *
     * @throws Exception
     * @step. ^I click send button on keyboard
     */
    @When("^I click send button on keyboard$")
    public void iClickSendButtonOnKeyboard() throws Exception {
        getConversationViewPage().clickKeyboardCommitButton();
    }

    /**
     * Verify whether shield icon is visible next to convo input field
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon next to conversation input field$"
     */
    @Then("^I (do not )?see shield icon next to conversation input field$")
    public void ISeeShieldIconNextNextToInputField(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible next to the convo input field",
                    getConversationViewPage().isShieldIconVisibleNextToInputField());
        } else {
            Assert.assertTrue("The shield icon is visible next to the convo input field, but should be hidden",
                    getConversationViewPage().isShieldIconInvisibleNextToInputField());
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
        //getConversationViewPage().clickThisDeviceLink();
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
        getConversationViewPage().resendLastMessageInDialogToUser();
    }

    /**
     * Verifies that Upper Toolbar is visible in conversation
     *
     * @throws Exception
     * @step. ^I see Upper Toolbar on dialog page$
     */
    @Then("^I see Upper Toolbar on dialog page$")
    public void ISeeUpperToolbar() throws Exception {
        Assert.assertTrue(getConversationViewPage().isUpperToolbarVisible());
    }

    /**
     * Verify visibility of Audio call|Video call or both buttons on Upper Toolbar
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @param buttonName   Audio call, Video call or Calling for both buttons verification
     * @step. ^I (do not )?see ([Aa]udio call|[Vv]ideo call|[Cc]alling) buttons? on [Uu]pper [Tt]oolbar$
     */
    @Then("^I (do not )?see ([Aa]udio call|[Vv]ideo call|[Cc]alling) buttons? on [Uu]pper [Tt]oolbar$")
    public void IDonotSeeCallingButtonsOnUpperToolbar(String shouldNotSee, String buttonName) throws Exception {
        if (shouldNotSee == null) {
            switch (buttonName) {
                case "Audio call":
                    Assert.assertTrue("Audio call button is not visible on Upper Toolbar",
                            getConversationViewPage().isAudioCallButtonOnToolbarVisible());
                    break;
                case "Video call":
                    Assert.assertTrue("Video call button is not visible on Upper Toolbar",
                            getConversationViewPage().isVideoCallButtonOnToolbarVisible());
                    break;
                case "Calling":
                    Assert.assertTrue("Audio call button is not visible on Upper Toolbar",
                            getConversationViewPage().isAudioCallButtonOnToolbarVisible());
                    Assert.assertTrue("Video call button is not visible on Upper Toolbar",
                            getConversationViewPage().isVideoCallButtonOnToolbarVisible());
                    break;
            }
        } else {
            switch (buttonName) {
                case "Audio call":
                    Assert.assertTrue("Audio call button is visible on Upper Toolbar",
                            getConversationViewPage().isAudioCallButtonOnToolbarNotVisible());
                    break;
                case "Video call":
                    Assert.assertTrue("Video call button is visible on Upper Toolbar",
                            getConversationViewPage().isVideoCallButtonOnToolbarNotVisible());
                    break;
                case "Calling":
                    Assert.assertTrue("Audio call button is visible on Upper Toolbar",
                            getConversationViewPage().isAudioCallButtonOnToolbarNotVisible());
                    Assert.assertTrue("Video call button is visible on Upper Toolbar",
                            getConversationViewPage().isVideoCallButtonOnToolbarNotVisible());
                    break;
            }
        }
    }

    /**
     * Verify that too many people alert is visible
     *
     * @throws Exception
     * @step. ^I see Too many people alert$
     */
    @Then("^I see Too many people alert$")
    public void ISee10PeopleAlert() throws Exception {
        Assert.assertTrue(getConversationViewPage().isTooManyPeopleAlertVisible());
    }

    /**
     * Verify conversation name is displayed in Upper Toolbar
     *
     * @param convoName user or group chat name
     * @throws Exception
     * @step. ^I see conversation name (.*) in Upper Toolbar$
     */
    @Then("^I see conversation name (.*) in Upper Toolbar$")
    public void ISeeUsernameInUpperToolbar(String convoName) throws Exception {
        convoName = usrMgr.replaceAliasesOccurences(convoName, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation name '%s' is not displayed on Upper Toolbar", convoName),
                getConversationViewPage().isUserNameInUpperToolbarVisible(convoName));
    }

    /**
     * Verify that the YOU CALLED and button is shown
     *
     * @throws Exception
     * @step. ^I see You Called message and button$
     */
    @Then("^I see You Called message and button$")
    public void iSeeYouCalledMessageAndButton() throws Exception {
        Assert.assertTrue("YOU CALLED and phone button is not shown", getConversationViewPage().
                isYouCalledMessageAndButtonVisible());
    }

    private static final double MAX_SIMILARITY_THRESHOLD = 0.97;

    /**
     * Verify whether the particular picture is animated
     *
     * @throws Exception
     * @step. ^I see the picture in the (preview|conversation view) is animated$"
     */
    @Then("^I see the picture in the conversation view is animated$")
    public void ISeePictureIsAnimated() throws Exception {
        // no need to wait, since screenshoting procedure itself is quite long
        final long screenshotingDelay = 0;
        final int maxFrames = 4;
        final double avgThreshold = ImageUtil.getAnimationThreshold(getConversationViewPage()::getRecentPictureScreenshot,
                maxFrames, screenshotingDelay);
        Assert.assertTrue(String.format("The picture in the conversation view seems to be static (%.2f >= %.2f)",
                avgThreshold, MAX_SIMILARITY_THRESHOLD), avgThreshold < MAX_SIMILARITY_THRESHOLD);
    }

    /**
     * Tap on file transfer menu item by name
     *
     * @param itemName name of the item
     * @throws Exception
     * @step. ^I tap file transfer menu item (.*)$
     */
    @When("^I tap file transfer menu item (.*)$")
    public void ITapFileTransferMenuItem(String itemName) throws Exception {
        if (itemName.equals("DEFAULT_PNG")) {
            itemName = DEFAULT_PNG;
        }
        getConversationViewPage().tapFileTransferMenuItem(itemName);
    }

    /**
     * Verify file transfer placeholder visibility
     *
     * @throws Exception
     * @step. ^I see file transfer placeholder$
     */
    @When("^I see file transfer placeholder$")
    public void ISeeFileTransferPlaceHolder() throws Exception {
        Assert.assertTrue("File transfer placeholder is not visible",
                getConversationViewPage().fileTransferTopLabelIsVisible() &&
                        getConversationViewPage().fileTransferBottomLabelIsVisible());
    }
}
