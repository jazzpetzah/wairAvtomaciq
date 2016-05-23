package com.wearezeta.auto.ios.steps;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import cucumber.api.PendingException;
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

    private static final String FTRANSFER_MENU_DEFAULT_PNG = "group-icon@3x.png";
    private static final String FTRANSFER_MENU_TOO_BIG = "Big file";

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
     * Scroll to the bottom of the current conversation view
     *
     * @throws Exception
     * @step. ^I scroll to the bottom of the conversation$$
     */
    @When("^I scroll to the bottom of the conversation$")
    public void ScrollToTheBottom() throws Exception {
        getConversationViewPage().scrollToTheBottom();
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
     * Tap the corresponding button from input tools palette
     *
     * @param isLongTap equals to null if simple tap should be performed
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Add Picture|Ping|Sketch|File Transfer|Video Message|Audio Message) button from input tools$
     */
    @When("^I (long )?tap (Add Picture|Ping|Sketch|File Transfer|Video Message|Audio Message) button from input tools$")
    public void IPressAddPictureButton(String isLongTap, String btnName) throws Exception {
        if (isLongTap == null) {
            getConversationViewPage().tapInputToolButtonByName(btnName);
        } else {
            getConversationViewPage().longTapInputToolButtonByName(btnName);
        }
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
                    getConversationViewPage().isInputToolButtonByNameVisible(btnName));
        } else {
            Assert.assertTrue(btnName + "button in input tools palette is  visible",
                    getConversationViewPage().isInputToolButtonByNameNotVisible((btnName)));
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
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            throw new PendingException("Known Bug: Media bar disappears unexpectedly on Simulator");
        } else {
            Assert.assertTrue("Media bar is not displayed after the view has been scrolled to the top",
                    getConversationViewPage().scrollDownTillMediaBarAppears());
        }
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

    /**
     * Check whether text input placeholder text is visible or not
     *
     * @param shouldNotBeVisible equals to null if the placeholder should be visible
     * @throws Exception
     * @step. ^I (do not )?see input placeholder text$
     */
    @Then("^I (do not )?see input placeholder text$")
    public void ISeeInputPlaceholderText(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Input placeholder text is not visible",
                    getConversationViewPage().isInputPlaceholderTextVisible());
        } else {
            Assert.assertTrue("Input placeholder text is visible",
                    getConversationViewPage().isInputPlaceholderTextInvisible());
        }
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
    public void ISeeUserName(String name) throws Exception {
        Assert.assertTrue("My name: " + name + " is not displayed in dialog",
                getConversationViewPage().isUserNameDisplayedInConversationView(name));
    }

    /**
     * Verify if dialog page with pointed user is shown. It's ok to use only if
     * there is not or small amount of messages in dialog.
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I see the conversation with (.*)$
     */
    @When("^I see the conversation with (.*)$")
    public void ISeeConversationWith(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue("Dialog with user is not visible", getConversationViewPage()
                .isConnectedToUserStartedConversationLabelVisible(contact));
    }

    /**
     * Verify that all buttons in toolbar are visible or not
     *
     * @param shouldNotBeVisible equals to null if the toolbar should be visible
     * @throws Exception
     * @step. ^I (do not )?see conversation tools buttons$
     */
    @When("^I (do not )?see conversation tools buttons$")
    public void ISeeOnlyPeopleButtonRestNotShown(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Some of input tools buttons are not visible",
                    getConversationViewPage().areInputToolsVisible());
        } else {
            Assert.assertTrue("Some of input tools buttons are still visible",
                    getConversationViewPage().areInputToolsInvisible());
        }
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
        Assert.assertTrue(String.format("The text input field contain content, which is different from '%s'",
                CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE),
                getConversationViewPage().isCurrentInputTextEqualTo(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE));
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
     * Long tap on the image displayed in the conversation
     *
     * @throws Exception
     * @step. ^I long tap on image in the conversation$
     */
    @When("^I long tap on image in the conversation$")
    public void ILongTapOnImage() throws Exception {
        getConversationViewPage().tapHoldImage();
    }

    /**
     * Tap on pointed badge item
     *
     * @param badgeItem the badge item name
     * @throws Exception
     * @step. ^I tap on (Select All|Copy|Delete|Paste) badge item$
     */
    @When("^I tap on (Select All|Copy|Delete|Paste) badge item$")
    public void ITapBadge(String badgeItem) throws Exception {
        switch (badgeItem) {
            case "Select All":
                getConversationViewPage().tapPopupSelectAllButton();
                break;
            case "Copy":
                getConversationViewPage().tapPopupCopyButton();
                break;
            case "Delete":
                getConversationViewPage().tapPopupDeleteButton();
                break;
            case "Paste":
                getConversationViewPage().tapPopupPasteButton();
                break;
            default:
                throw new IllegalArgumentException("Only (Select All|Copy|Delete|Paste) are allowed options");
        }
    }

    /**
     * Verify whether the corresponding badge item is visible
     *
     * @param shouldNotSee equals to null if the corresponding item should be visible
     * @param badgeItem    the badge item name
     * @throws Exception
     * @step. ^I (do not )?see (Select All|Copy|Delete|Paste) badge item$
     */
    @Then("^I (do not )?see (Select All|Copy|Delete|Paste) badge item$")
    public void ISeeBadge(String shouldNotSee, String badgeItem) throws Exception {
        FunctionalInterfaces.ISupplierWithException<Boolean> verificationFunc;
        switch (badgeItem) {
            case "Select All":
                verificationFunc = (shouldNotSee == null) ? getConversationViewPage()::isPopupSelectAllButtonVisible :
                        getConversationViewPage()::isPopupSelectAllButtonInvisible;
                break;
            case "Copy":
                verificationFunc = (shouldNotSee == null) ? getConversationViewPage()::isPopupCopyButtonVisible :
                        getConversationViewPage()::isPopupCopyButtonInvisible;
                break;
            case "Delete":
                verificationFunc = (shouldNotSee == null) ? getConversationViewPage()::isPopupDeleteButtonVisible :
                        getConversationViewPage()::isPopupDeleteButtonInvisible;
                break;
            case "Paste":
                verificationFunc = (shouldNotSee == null) ? getConversationViewPage()::isPopupPasteButtonVisible :
                        getConversationViewPage()::isPopupPasteButtonInvisible;
                break;
            default:
                throw new IllegalArgumentException("Only (Select All|Copy|Delete|Paste) are allowed options");
        }
        Assert.assertTrue(String.format("The '%s' badge item is %s", badgeItem,
                (shouldNotSee == null) ? "not visible" : "still visible"), verificationFunc.call());
    }

    /**
     * Verify whether user avatar is visible near convo input field
     *
     * @param shouldNotBeVisible equals to nuill is the avatar should be invisible
     * @throws Exception
     * @step. ^I (do not )?see plus icon is changed to user avatar icon$
     */
    @When("^I (do not )?see user avatar icon near the conversation input field$")
    public void ISeeUserAvatar(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("User avatar is not visible", getConversationViewPage().isUserAvatarNextToInputVisible());
        } else {
            Assert.assertTrue("User avatar is visible, but should be hidden",
                    getConversationViewPage().isUserAvatarNextToInputInvisible());
        }
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
                getConversationViewPage().waitForCursorInputVisible());
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
     * @step. ^I tap file transfer menu item (.*)
     */
    @When("^I tap file transfer menu item (.*)")
    public void ITapFileTransferMenuItem(String itemName) throws Exception {
        String realName = itemName;
        switch (itemName) {
            case "FTRANSFER_MENU_DEFAULT_PNG":
                realName = FTRANSFER_MENU_DEFAULT_PNG;
                break;
            case "TOO_BIG":
                realName = FTRANSFER_MENU_TOO_BIG;
                break;
        }
        getConversationViewPage().tapFileTransferMenuItem(realName);
    }

    /**
     * Verify file transfer placeholder visibility
     *
     * @param shouldNotBeVisible equals to null if the placeholder should be visible
     * @throws Exception
     * @step. ^I (do not )?see file transfer placeholder$
     */
    @When("^I (do not )?see file transfer placeholder$")
    public void ISeeFileTransferPlaceHolder(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("File transfer placeholder is not visible",
                    getConversationViewPage().isFileTransferTopLabelVisible() &&
                            getConversationViewPage().isFileTransferBottomLabelVisible());
        } else {
            Assert.assertTrue("File transfer placeholder is visible, but should be hidden",
                    getConversationViewPage().isFileTransferTopLabelInvisible());
        }
    }

    /**
     * Tap the most recent visible transfer placeholder
     *
     * @throws Exception
     * @step. ^I tap file transfer placeholder$
     */
    @When("^I tap file transfer placeholder$")
    public void ITapFileTransferPlaceholder() throws Exception {
        getConversationViewPage().tapFileTransferPlaceholder();
    }

    /**
     * Verify whether File Transfer placeholder is visible in the conversation view
     *
     * @param timeoutSeconds   timeout in seconds
     * @param expectedFileName the file name, which should be visible in the placeholder
     * @param expectedSize     the expected file size to be shown in the placeholder
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until the file (.*) with size (.*) is ready for download from conversation view$
     */
    @Then("^I wait up to (\\d+) seconds? until the file (.*) with size (.*) is ready for download from conversation view$")
    public void IWaitUntilDownloadFinished(int timeoutSeconds, String expectedFileName, String expectedSize)
            throws Exception {
        Assert.assertTrue(String.format(
                "Cannot detect the Download Finished placeholder for a file '%s' in the conversation view after %s seconds",
                expectedFileName, timeoutSeconds),
                getConversationViewPage().waitUntilDownloadReadyPlaceholderVisible(expectedFileName, expectedSize,
                        timeoutSeconds));
    }

    /**
     * Verify whether file preview is shown after the timeout
     *
     * @param secondsTimeout   timeout in seconds
     * @param expectedFileName file name in preview header
     * @throws Exception
     * @step. ^I wait up to (\d+) seconds? until I see a preview of the file named "(.*)"$
     */
    @Then("^I wait up to (\\d+) seconds? until I see a preview of the file named \"(.*)\"$")
    public void IWaitForFilePreview(int secondsTimeout, String expectedFileName) throws Exception {
        Assert.assertTrue(String.format("The preview was not shown for '%s' after %s seconds timeout", expectedFileName,
                secondsTimeout),
                getConversationViewPage().waitUntilFilePreviewIsVisible(secondsTimeout, expectedFileName));
    }

    /**
     * Verify whether generic file share menu is shown
     *
     * @param timeoutSeconds timeout in seconds
     * @throws Exception
     * @step. ^I wait up to (\\d+) seconds until I see generic file share menu$
     */
    @Then("^I wait up to (\\d+) seconds until I see generic file share menu$")
    public void ISeeGenericFileShareMenu(int timeoutSeconds) throws Exception {
        Assert.assertTrue("Generic file share menu has not been shown",
                getConversationViewPage().isGenericFileShareMenuVisible(timeoutSeconds));
    }

    /**
     * Wait until Uploading label disappears
     *
     * @param timeoutSeconds seconds to wait label to disappear
     * @throws Exception
     * @step. ^I wait up to (\\d+) seconds until the file is uploaded$
     */
    @When("^I wait up to (\\d+) seconds until the file is uploaded$")
    public void IWaitFileToUpload(int timeoutSeconds) throws Exception {
        Assert.assertTrue(String.format("File is still uploading after %s seconds", timeoutSeconds),
                getConversationViewPage().fileUploadingLabelNotVisible(timeoutSeconds));
    }

    /**
     * Tap Share button on file preview
     *
     * @throws Exception
     * @step. ^I tap Share button on file preview page$
     */
    @When("^I tap Share button on file preview page$")
    public void ITapShareButtonOnPreview() throws Exception {
        getConversationViewPage().tapShareButton();
    }

    /**
     * Tap share menu item by name
     *
     * @param itemName title in Share menu
     * @throws Exception
     * @step. ^I tap (Save Image|Copy) share menu item$
     */
    @When("^I tap (Save Image|Copy) share menu item$")
    public void ITapShareMenuItem(String itemName) throws Exception {
        getConversationViewPage().tapShareMenuItem(itemName);
    }

    /**
     * long tap on pointed text in conversation view
     *
     * @param msg message text
     * @throws Exception
     * @step. ^I long tap last (default\".*\") message in conversation view$
     */
    @When("^I long tap (default|\".*\") message in conversation view$")
    public void ITapAndHoldTextMessage(String msg) throws Exception {
        if (msg.equals("default")) {
            getConversationViewPage().tapAndHoldTextMessageByText(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        } else {
            msg = msg.replaceAll("^\"|\"$", "");
            getConversationViewPage().tapAndHoldTextMessageByText(msg);
        }
    }

    /**
     * Verifies if media container is visible or not in the conversation view
     *
     * @param shouldNotBeVisible equals to null if the media container should be visible
     * @throws Exception
     * @step. ^I (do not )?see the media container in the conversation view$
     */
    @Then("^I (do not )?see the media container in the conversation view$")
    public void ISeeTheMediaContainerInTheConversationView(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Media container is not visible in the conversation view",
                    getConversationViewPage().isMediaContainerVisible());
        } else {
            Assert.assertTrue("Media container is visible in the conversation view",
                    getConversationViewPage().isMediaContainerInvisible());
        }
    }

    /**
     * Does a long tap on a media container to get delete/copy menu
     *
     * @throws Exception
     * @step. ^I long tap on media container in the conversation$
     */
    @When("^I long tap on media container in the conversation$")
    public void ILongTapOnMediaContainerInTheConversation() throws Exception {
        getConversationViewPage().tapAndHoldMediaContainer();
    }

    /**
     * Does a long tap on the shared file placeholder
     *
     * @throws Exception
     * @step. ^I long tap on file transfer placeholder in conversation view$
     */
    @When("^I long tap on file transfer placeholder in conversation view$")
    public void ILongTapOnFileTransferPlaceholderInConversationView() throws Exception {
        getConversationViewPage().tapAndHoldFileTransferPlaceholder();
    }

    /**
     * Wait for a while until video message container is shown in the conversation view
     *
     * @throws Exception
     * @step. ^I see a preview of video message$"
     */
    @Then("^I see a preview of video message$")
    public void IWaitForVideoMessage() throws Exception {
        Assert.assertTrue("Video message container has not been shown",
               getConversationViewPage().isVideoMessageContainerVisible());
    }

    /**
     * Verify whether audio message record progress control is visible
     *
     * @step. ^I see audio message record progress$
     *
     * @throws Exception
     */
    @Then("^I see audio message record progress$")
    public void ISeeAudioRecordProgress() throws Exception {
        Assert.assertTrue("Audio message record progress control has not been shown",
                getConversationViewPage().isAudioMessageProgressVisible());
    }
}
