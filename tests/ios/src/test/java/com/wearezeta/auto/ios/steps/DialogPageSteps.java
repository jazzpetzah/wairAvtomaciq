package com.wearezeta.auto.ios.steps;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;

import com.google.common.base.Throwables;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
    private static final Logger log = ZetaLogger.getLog(DialogPageSteps.class
            .getSimpleName());
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection
            .getInstance();

    private DialogPage getDialogPage() throws Exception {
        return pagesCollecton.getPage(DialogPage.class);
    }

    private GroupChatPage getGroupChatPage() throws Exception {
        return pagesCollecton.getPage(GroupChatPage.class);
    }

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollecton.getPage(ContactListPage.class);
    }

    private String mediaState;
    public static long sendDate;
    private static final int SWIPE_DURATION = 1000;
    private static final String ONLY_SPACES_MESSAGE = "     ";
    public static long memTime;
    public String pingId;
    private int beforeNumberOfImages = 0;
    final String sendInviteMailContent = "I’m on Wire. Search for %s";

    @When("^I see dialog page$")
    public void WhenISeeDialogPage() throws Exception {
        Assert.assertTrue("Cursor input is not visible",
                getDialogPage().waitForCursorInputVisible());
    }

    @When("^I tap on text input$")
    public void WhenITapOnTextInput() throws Exception {
        for (int i = 0; i < 3; i++) {
            getDialogPage().tapOnCursorInput();
            if (getDialogPage().isKeyboardVisible()) {
                break;
            }
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
        for (int i = 0; i < 3; i++) {
            getDialogPage().tapOnCursorInput();
            if (getDialogPage().isPlusButtonVisible()) {
                break;
            }
        }
    }

    /**
     * Verify that text input is not allowed
     *
     * @throws Exception
     * @step. I see text input in dialog is not allowed
     */
    @When("I see text input in dialog is not allowed")
    public void ISeeTextInputIsNotAllowed() throws Exception {
        Assert.assertFalse("Text input is allowed", getDialogPage()
                .isCursorInputVisible());
    }

    @When("^I type the default message$")
    public void WhenITypeTheMessage() throws Exception {
        getDialogPage().typeConversationMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
    }

    private static final String YOU_PINGED_MESSAGE = "YOU PINGED";

    @Then("^I see You Pinged message in the dialog$")
    public void ISeeHelloMessageFromMeInTheDialog() throws Throwable {
        Assert.assertTrue(getDialogPage().isMessageVisible(YOU_PINGED_MESSAGE));
    }

    @Then("^I see User (.*) Pinged message in the conversation$")
    public void ISeeUserPingedMessageTheDialog(String user) throws Throwable {
        String username = usrMgr.findUserByNameOrNameAlias(user).getName();
        String expectedPingMessage = username.toUpperCase() + " PINGED";
        Assert.assertTrue(getDialogPage().isMessageVisible(expectedPingMessage)
                || getGroupChatPage().isMessageVisible(expectedPingMessage));
    }

    @When("^I type the default message and send it$")
    public void ITypeTheMessageAndSendIt() throws Exception {
        getDialogPage().typeAndSendConversationMessage(
                CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
    }

    /**
     * Verifies that the title bar is present with a certain conversation name
     *
     * @param convName name of the conversation
     * @throws Exception
     * @step. ^I see title bar in conversation name (.*)$
     */
    @Then("^I see title bar in conversation name (.*)$")
    public void ThenITitleBar(String convName) throws Exception {
        String chatName = usrMgr.findUserByNameOrNameAlias(convName).getName();
        // Title bar is gone quite fast so it may fail because of this
        Assert.assertTrue("Title bar with name - " + chatName
                        + " is not on the page",
                getDialogPage().isTitleBarDisplayed(chatName));
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
        getDialogPage().inputStringFromKeyboard("\n");
    }

    @When("^I swipe up on dialog page to open other user personal page$")
    public void WhenISwipeUpOnDialogPage() throws Exception {
        getDialogPage().swipeUp(1000);
    }

    /**
     * Swipes up on the pending dialog page in order to access the pending
     * personal info page
     *
     * @throws Throwable
     * @step. ^I swipe up on pending dialog page to open other user pending
     * personal page$
     */

    @When("^I swipe up on pending dialog page to open other user pending personal page$")
    public void WhenISwipeUpOnPendingDialogPage() throws Throwable {
        getDialogPage().swipePendingDialogPageUp(500);
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

    @Then("I see last message in dialog is expected message (.*)")
    public void ThenISeeLasMessageInTheDialogIsExpected(String msg)
            throws Throwable {
        String dialogLastMessage = getDialogPage().getLastMessageFromDialog().orElseThrow(() ->
                new AssertionError("No messages are present in the conversation view")
        );
        if (!Normalizer.isNormalized(dialogLastMessage, Form.NFC)) {
            dialogLastMessage = Normalizer.normalize(dialogLastMessage,
                    Form.NFC);
        }

        if (!Normalizer.isNormalized(msg, Form.NFC)) {
            dialogLastMessage = Normalizer.normalize(msg, Form.NFC);
        }

        Assert.assertTrue("Message is different, actual: " + dialogLastMessage
                + " expected: " + msg, dialogLastMessage.equals(msg));
    }

    /**
     * Swipe left on text input to close options buttons
     *
     * @throws Exception
     * @step. ^I swipe left on options buttons$
     */
    @When("^I swipe left on options buttons$")
    public void ISwipeLeftTextInput() throws Exception {
        for (int i = 0; i < 3; i++) {
            getDialogPage().swipeLeftOptionsButtons();
            if (getDialogPage().isPlusButtonVisible()) {
                break;
            }
        }
    }

    @When("^I press Add Picture button$")
    public void IPressAddPictureButton() throws Throwable {
        getDialogPage().pressAddPictureButton();
    }

    /**
     * Click call button to start a call
     *
     * @throws Throwable
     * @step. ^I press call button$
     */
    @When("^I press call button$")
    public void IPressCallButton() throws Throwable {
        getDialogPage().pressCallButton();
    }

    @When("^I click Ping button$")
    public void IPressPingButton() throws Throwable {
        getDialogPage().pressPingButton();
    }

    @Then("^I see Pending Connect to (.*) message on Dialog page from user (.*)$")
    public void ISeePendingConnectMessage(String contact, String user)
            throws Throwable {
        user = usrMgr.findUserByNameOrNameAlias(user).getName();
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        String expectedConnectingLabel = getDialogPage()
                .getExpectedConnectingLabel(contact);
        String actualConnectingLabel = getDialogPage().getConnectMessageLabel();

        Assert.assertTrue(actualConnectingLabel
                .contains(expectedConnectingLabel));
    }

    @Then("^I see new photo in the dialog$")
    public void ISeeNewPhotoInTheDialog() throws Throwable {
        int afterNumberOfImages = -1;

        boolean isNumberIncreased = false;
        for (int i = 0; i < 3; i++) {
            afterNumberOfImages = getDialogPage().getNumberOfImages();
            if (afterNumberOfImages == beforeNumberOfImages + 1) {
                isNumberIncreased = true;
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        Assert.assertTrue("Incorrect images count: before - "
                        + beforeNumberOfImages + ", after - " + afterNumberOfImages,
                isNumberIncreased);
    }

    @When("I type and send long message and media link (.*)")
    public void ITypeAndSendLongTextAndMediaLink(String link) throws Exception {
        getDialogPage().typeAndSendConversationMessage(IOSConstants.LONG_MESSAGE);
        getDialogPage().waitLoremIpsumText();
        getDialogPage().typeAndSendConversationMessage(link);
        getDialogPage().waitSoundCloudLoad();
    }

    @When("^I memorize message send time$")
    public void IMemorizeMessageSendTime() throws Exception {
        sendDate = new Date().getTime();
    }

    @Then("I see youtube link (.*) and media in dialog")
    public void ISeeYoutubeLinkAndMediaInDialog(String link) throws Exception {
        Assert.assertTrue("Media is missing in dialog", getDialogPage()
                .isYoutubeContainerVisible());
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
    }

    @Then("I see media link (.*) and media in dialog")
    public void ISeeMediaLinkAndMediaInDialog(String link) throws Exception {
        Assert.assertTrue("Media is missing in dialog", getDialogPage()
                .isMediaContainerVisible());
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
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

    @When("I send long message")
    public void ISendLongMessage() throws Exception {
        getDialogPage().typeAndSendConversationMessage(IOSConstants.LONG_MESSAGE);
    }

    @When("^I post media link (.*)$")
    public void IPostMediaLink(String link) throws Throwable {
        getDialogPage().typeAndSendConversationMessage(link);
    }

    @When("^I tap media link$")
    public void ITapMediaLink() throws Throwable {
        getDialogPage().startMediaContent();
        memTime = System.currentTimeMillis();
    }

    @When("^I scroll media out of sight until media bar appears$")
    public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception {
        getDialogPage().scrollDownTilMediaBarAppears();
        Assert.assertTrue("Media bar is not displayed", getDialogPage()
                .isMediaBarDisplayed());
    }

    @When("^I pause playing the media in media bar$")
    public void IPausePlayingTheMediaInMediaBar() throws Exception {
        getDialogPage().pauseMediaContent();
    }

    @When("^I press play in media bar$")
    public void IPressPlayInMediaBar() throws Exception {
        getDialogPage().playMediaContent();
    }

    @When("^I stop media in media bar$")
    public void IStopMediaInMediaBar() throws Exception {
        getDialogPage().stopMediaContent();
    }

    @Then("I see playing media is paused")
    public void ThePlayingMediaIsPaused() throws Exception {
        String pausedState = IOSConstants.MEDIA_STATE_PAUSED;
        mediaState = getDialogPage().getMediaState();
        Assert.assertEquals(pausedState, mediaState);
    }

    @Then("I see media is playing")
    public void TheMediaIsPlaying() throws Exception {
        String playingState = IOSConstants.MEDIA_STATE_PLAYING;
        mediaState = getDialogPage().getMediaState();
        Assert.assertEquals(playingState, mediaState);
    }

    @Then("The media stops playing")
    public void TheMediaStoppsPlaying() throws Exception {
        String endedState = IOSConstants.MEDIA_STATE_STOPPED;
        mediaState = getDialogPage().getMediaState();
        Assert.assertEquals(endedState, mediaState);
    }

    @When("I wait (.*) seconds for media to stop playing")
    public void IWaitForMediaStopPlaying(int time) throws Throwable {
        long deltaTime = 0;
        long currentTime = System.currentTimeMillis();
        if ((memTime + time * 1000) > currentTime) {
            deltaTime = time * 1000 - (currentTime - memTime);
            log.debug("Waiting " + deltaTime + " ms playback to finish");
            Thread.sleep(deltaTime + 5000);
            log.debug("Playback finished");
        } else {
            log.debug("Playback finished");
        }

    }

    @Then("I see media bar on dialog page")
    public void ISeeMediaBarInDialog() throws Exception {
        Assert.assertTrue(getDialogPage().isMediaBarDisplayed());
    }

    @Then("I dont see media bar on dialog page")
    public void ISeeMediaBarDisappear() throws Exception {
        Assert.assertTrue(getDialogPage().waitMediabarClose());
    }

    @When("^I tap on the media bar$")
    public void ITapOnMediaBar() throws Exception {
        getDialogPage().tapOnMediaBar();
    }

    @Then("I see conversation view is scrolled back to the playing media link (.*)")
    public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link) throws Throwable {
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
        getDialogPage().workaroundUITreeRefreshIssue(
                () -> Assert.assertTrue("View did not scroll back", getDialogPage()
                        .isMediaContainerVisible())
        );
    }

    @When("I tap and hold image to open full screen")
    public void ITapImageToOpenFullScreen() throws Throwable {
        getDialogPage().tapImageToOpen();
    }

    @Then("^I scroll away the keyboard$")
    public void IScrollKeyboardAway() throws Throwable {
        getDialogPage().swipeDialogPageDown(500);
        Thread.sleep(2000);
    }

    @Then("^I navigate back to conversations view$")
    public void INavigateToConversationsView() throws Exception {
        for (int i = 0; i < 3; i++) {
            getDialogPage().swipeRight(SWIPE_DURATION);
            if (getContactListPage().isMyUserNameDisplayedFirstInContactList(
                    usrMgr.getSelfUser().getName())) {
                break;
            }
        }
    }

    @When("I try to send message with only spaces")
    public void ISendMessageWithOnlySpaces() throws Throwable {
        getDialogPage().typeAndSendConversationMessage(ONLY_SPACES_MESSAGE);
    }

    /**
     * Send message with leading empty spaces by script
     *
     * @throws Throwable
     * @step. I input message with leading empty spaces
     */
    @When("I input message with leading empty spaces")
    public void IInpuMessageWithLeadingEmptySpace() throws Throwable {
        getDialogPage().typeAndSendConversationMessage(
                ONLY_SPACES_MESSAGE + CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);

    }

    /**
     * Send message with trailing empty spaces by script
     *
     * @throws Throwable
     * @step. I input message with trailing empty spaces
     */
    @When("I input message with trailing emtpy spaces")
    public void IInputMessageWithTrailingEmptySpace() throws Throwable {
        getDialogPage().typeAndSendConversationMessage(
                CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE + ONLY_SPACES_MESSAGE);
    }

    @When("I input message with lower case and upper case")
    public void IInputMessageWithLowerAndUpperCase() throws Throwable {
        final String message = CommonUtils.generateRandomString(7).toLowerCase()
                + CommonUtils.generateRandomString(7).toUpperCase();
        getDialogPage().typeAndSendConversationMessage(message);
    }

    @When("I input more than 200 chars message and send it")
    public void ISend200CharsMessage() throws Exception {
        final String message = CommonUtils.generateRandomString(210).toLowerCase()
                .replace("x", " ");
        getDialogPage().typeAndSendConversationMessage(message);
    }

    @When("I tap and hold on message input")
    public void ITapHoldTextInput() throws Exception {
        getDialogPage().tapHoldTextInput();
    }

    @When("^I scroll to the beginning of the conversation$")
    public void IScrollToTheBeginningOfTheConversation() throws Throwable {
        getDialogPage().scrollToBeginningOfConversation();
    }

    @When("^I send using script predefined message (.*)$")
    public void ISendUsingScriptPredefinedMessage(String message)
            throws Exception {
        getDialogPage().typeAndSendConversationMessage(message);
    }

    /**
     * Verify last image in dialog is same as template
     *
     * @param filename template file name
     * @throws Exception
     * @step. ^I verify image in dialog is same as template (.*)$
     */
    @When("^I verify image in dialog is same as template (.*)$")
    public void IVerifyImageInDialogSameAsTemplate(String filename) throws Exception {
        // FIXME: replace with dynamic image comparison
    }

    @Then("^I see (.*) icon in conversation$")
    public void ThenIseeIcon(String iconLabel) throws Exception {
        // FIXME: replace with dynamic comparison or remove
    }

    /**
     * Scrolls to the end of the conversation
     *
     * @throws Exception
     * @step. ^I scroll to the end of the conversation$
     */
    @When("^I scroll to the end of the conversation$")
    public void IScrollToTheEndOfTheConversation() throws Exception {
        getDialogPage().scrollToEndOfConversation();
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
        mail = usrMgr.findUserByNameOrNameAlias(mail).getEmail();
        final String finalString = String.format(sendInviteMailContent, mail);
        String lastMessage = getDialogPage().getLastMessageFromDialog().orElseThrow(() ->
                new AssertionError("No messages are present in the conversation view")
        );
        boolean messageContainsContent = lastMessage.contains(finalString);
        Assert.assertTrue("Mail Invite content is not shown in lastMessage",
                messageContainsContent);
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
        String username = usrMgr.findUserByNameOrNameAlias(contact).getName();
        String expectedCallMessage = username.toUpperCase() + " CALLED";
        Assert.assertTrue(username + " called message is missing in dialog",
                getDialogPage().isMessageVisible(expectedCallMessage));
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
     * Checks if a chathaed is visible with message and avatar for 5sec
     *
     * @param contact you see the chathead of
     * @throws Exception
     * @step. ^I see chathead of contact (.*)
     */
    @Then("^I see chathead of contact (.*)")
    public void ISeeChatheadOfContact(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        boolean chatheadIsVisible = getDialogPage().chatheadIsVisible(contact);
        Assert.assertTrue("No Chathead visible.", chatheadIsVisible);
        boolean chAvatarImageIsVisible = getDialogPage()
                .chatheadAvatarImageIsVisible();
        Assert.assertTrue("No Chathead avatar visible.", chAvatarImageIsVisible);
    }

    /**
     * Verify that the chathaed is not seen after 5 seconds
     *
     * @param contact you not see the chathead of
     * @throws Exception
     * @step. I do not see chathead of contact (.*)
     */
    @Then("^I do not see chathead of contact (.*)")
    public void IDoNotSeeChatheadOfContactForSecondsWithAvatarAndMessage(
            String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        boolean chatheadIsVisible = getDialogPage().chatheadIsVisible(contact);
        Assert.assertFalse("Chathead visible.", chatheadIsVisible);
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
        getDialogPage().typeAndSendConversationMessage(message);
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
    public void ISeePluseButtonNextInput() throws Exception {
        Assert.assertTrue("Plus button is not visible", getDialogPage()
                .isPlusButtonVisible());
    }

    /**
     * Verify is plus button is not visible
     *
     * @throws Exception
     * @step. ^I see plus button is not shown$
     */
    @When("^I see plus button is not shown$")
    public void ISeePlusButtonNotShown() throws Exception {
        Assert.assertTrue("Plus button is still shown", getDialogPage()
                .waitPlusButtonNotVisible());
    }

    /**
     * Verify Details button is visible
     *
     * @throws Exception
     * @step. ^I see Details button is visible$
     */
    @When("^I see Details button is visible$")
    public void ISeeDetailsButtonShown() throws Exception {
        Assert.assertTrue("Details button is not visible", getDialogPage()
                .isOpenConversationDetailsButtonVisible());
    }

    /**
     * Verify Call button is visible
     *
     * @throws Exception
     * @step. ^I see Call button is visible$
     */
    @When("^I see Call button is visible$")
    public void ISeeCalButtonShown() throws Exception {
        Assert.assertTrue("Call button is not visible", getDialogPage()
                .isCallButtonVisible());
    }

    /**
     * Verify Camera button is visible
     *
     * @throws Exception
     * @step. ^I see Camera button is visible$
     */
    @When("^I see Camera button is visible$")
    public void ISeeCameraButtonShown() throws Exception {
        Assert.assertTrue("Camera button is not visible", getDialogPage()
                .isCameraButtonVisible());
    }

    /**
     * Verify Sketch button is visible
     *
     * @throws Exception
     * @step. ^I see Sketch button is visible$
     */
    @When("^I see Sketch button is visible$")
    public void ISeeSketchButtonShown() throws Exception {
        Assert.assertTrue("Sketch button is not visible", getDialogPage()
                .isOpenScetchButtonVisible());
    }

    /**
     * Verify Ping button is visible
     *
     * @throws Exception
     * @step. ^I see Ping button is visible$
     */
    @When("^I see Ping button is visible$")
    public void ISeePingButtonShown() throws Exception {
        Assert.assertTrue("Ping button is not visible", getDialogPage()
                .isPingButtonVisible());
    }

    /**
     * Verify Buttons: Details, Call, Camera, Sketch, Ping are visible
     *
     * @throws Exception
     * @step. ^I see Buttons: Details, Call, Camera, Sketch, Ping$
     */
    @When("^I see Buttons: Details, Call, Camera, Sketch, Ping$")
    public void ISeeButtonsDetailsCallCameraSketchPing() throws Exception {
        ISeeDetailsButtonShown();
        ISeeCalButtonShown();
        ISeeCameraButtonShown();
        ISeeSketchButtonShown();
        ISeePingButtonShown();
    }

    /**
     * Verify that only Details button is shown. Rest button should not be
     * visible
     *
     * @throws Exception
     * @step. I see only Details button. Call, Camera, Sketch, Ping are not
     * shown
     */
    @When("I see only Details button. Call, Camera, Sketch, Ping are not shown")
    public void ISeeOnlyDetailsButtonRestNotShown() throws Exception {
        ISeeDetailsButtonShown();
        Assert.assertFalse("Call button is visible", getDialogPage()
                .isCallButtonVisible());
        Assert.assertFalse("Camera button is visible", getDialogPage()
                .isCameraButtonVisible());
        Assert.assertFalse("Sketch button is visible", getDialogPage()
                .isOpenScetchButtonVisible());
        Assert.assertFalse("Ping button is visible", getDialogPage()
                .isPingButtonVisible());
    }

    /**
     * Verify Close button in options is NOT shown
     *
     * @throws Exception
     * @step. ^I see Close input options button is not visible$
     */
    @When("^I see Close input options button is not visible$")
    public void ISeeCloseButtonInputOptionsNotVisible() throws Exception {
        Assert.assertFalse("Close button is visible", getDialogPage()
                .isCloseButtonVisible());
    }

    /**
     * Verify that ping controller button's x coordinate is less then
     * conversation window's x coordinate
     *
     * @throws Exception
     * @step. ^I see controller buttons can not be visible$
     */
    @When("^I see controller buttons can not be visible$")
    public void ISeeControllerButtonsNotVisible() throws Exception {
        Assert.assertFalse(
                "Controller buttons can be visible. Please check screenshots",
                getDialogPage()
                        .isTherePossibilityControllerButtonsToBeDisplayed());
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
        getDialogPage().clickCloseButton();
    }

    /**
     * Verifies that vimeo link without ID is shown but NO player
     *
     * @param link of the vimeo video without ID
     * @throws Throwable
     * @step. ^I see vimeo link (.*) but NO media player$
     */
    @Then("^I see vimeo link (.*) but NO media player$")
    public void ISeeVimeoLinkButNOMediaPlayer(String link) throws Throwable {
        Assert.assertFalse("Media player is shown in dialog", getDialogPage()
                .isYoutubeContainerVisible());
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
    }

    /**
     * Verifies that vimeo link and the video container is visible
     *
     * @param link of vimeo video
     * @throws Throwable
     * @step. ^I see vimeo link (.*) and media in dialog$
     */
    @Then("^I see vimeo link (.*) and media in dialog$")
    public void ISeeVimeoLinkAndMediaInDialog(String link) throws Throwable {
        Assert.assertTrue("Media is missing in dialog", getDialogPage()
                .isYoutubeContainerVisible());
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
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
     * Verifies that link is seen in conversation view
     *
     * @param link that we sent to user
     * @throws Throwable
     * @step. ^I see Link (.*) in dialog$
     */
    @When("^I see Link (.*) in dialog$")
    public void ISeeLinkInDialog(String link) throws Throwable {
        Assert.assertEquals(link.toLowerCase(), getDialogPage()
                .getLastMessageFromDialog().orElseThrow(() ->
                        new AssertionError("No messages are present in the conversation view")
                ).toLowerCase());
    }

    /**
     * Tap on the sent link to open it
     *
     * @throws Throwable
     * @step. ^I tap on Link$
     */
    @When("^I tap on Link$")
    public void ITapOnLink() throws Throwable {
        getDialogPage().tapOnLink();
    }

    /**
     * Taps on a link that got sent together with a message
     *
     * @throws Throwable
     * @step. ^I tap on Link with a message$
     */
    @When("^I tap on Link with a message$")
    public void ITapOnLinkWithAMessage() throws Throwable {
        getDialogPage().tapOnLinkWithinAMessage();
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
    @When("^I see the only message in dialog is system message CONNECTED TO (.*)$")
    public void ISeeLastMessageIsSystem(String username) throws Exception {
        username = usrMgr.findUserByNameOrNameAlias(username).getName();
        ISeeXConvoEntries(1);
        Assert.assertTrue(getDialogPage()
                .isConnectedToUserStartedConversationLabelVisible(username));
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
        Assert.assertTrue(getDialogPage().isPlusButtonVisible());
        Assert.assertTrue(getDialogPage().isCursorInputVisible());
    }
}
