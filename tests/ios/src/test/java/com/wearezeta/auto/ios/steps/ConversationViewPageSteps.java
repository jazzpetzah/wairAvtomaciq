package com.wearezeta.auto.ios.steps;

import java.util.Optional;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import cucumber.api.java.en.And;
import org.apache.commons.lang3.text.WordUtils;
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

    private ConversationViewPage getConversationViewPage() throws Exception {
        return pagesCollection.getPage(ConversationViewPage.class);
    }

    @When("^I see conversation view page$")
    public void WhenISeePage() throws Exception {
        ISeeTextInput(null);
    }

    @When("^I (long )?tap on text input$")
    public void ITapOnTextInput(String isLongTap) throws Exception {
        getConversationViewPage().tapTextInput(isLongTap != null);
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
     * @param expectedMsg  the expected system message. may contyain user name aliases
     * @param shouldNotSee equals to null if the message should be visible
     * @throws Exception
     * @step. ^I (do not )?see "(.*)" system message in the conversation view$
     */
    @Then("^I (do not )?see \"(.*)\" system message in the conversation view$")
    public void ISeeSystemMessage(String shouldNotSee, String expectedMsg) throws Exception {
        expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, FindBy.NAME_ALIAS);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The expected system message '%s' is not visible in the conversation",
                    expectedMsg), getConversationViewPage().isSystemMessageVisible(expectedMsg));
        } else {
            Assert.assertTrue(String.format(
                    "The expected system message '%s' should not be visible in the conversation",
                    expectedMsg), getConversationViewPage().isSystemMessageInvisible(expectedMsg));
        }
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
            getConversationViewPage().typeMessage(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE, true);
        } else {
            getConversationViewPage().typeMessage(msg.replaceAll("^\"|\"$", ""), true);
        }
    }

    /**
     * Tap the corresponding button in conversation
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. I tap (Send Message|Emoji Keyboard|Text Keyboard|Hourglass) button in conversation view
     */
    @And("^I tap (Send Message|Emoji Keyboard|Text Keyboard|Hourglass|Time Indicator) button in conversation view$")
    public void ITapConvoButton(String btnName) throws Exception {
        getConversationViewPage().tapViewButton(btnName);
    }

    /**
     * Verify button visibility in the conversation view
     *
     * @param shouldNotSee equals to null if the button should be visible
     * @param btnName      the name of the button to check
     * @throws Exception
     * @step. I (do not )?see (Send Message|Emoji Keyboard|Text Keyboard|Hourglass)button in conversation view$
     */
    @Then("^I (do not )?see (Send Message|Emoji Keyboard|Text Keyboard|Hourglass|Time Indicator) button in conversation view$")
    public void ISeeSendMessageButton(String shouldNotSee, String btnName) throws Exception {
        boolean result;
        if (shouldNotSee == null) {
            result = getConversationViewPage().isViewButtonVisible(btnName);
        } else {
            result = getConversationViewPage().isViewButtonInvisible(btnName);
        }
        Assert.assertTrue(
                String.format("'%s' button should be %s", btnName, (shouldNotSee == null) ? "visible" : "not visible"),
                result
        );
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

    /**
     * Wait until text messages are visible in the conversation
     *
     * @param expectedCount the expected count of messages. Should be equal or greater than zero
     * @param isDefault     equals to null if presence of any messages are supposed to be verified
     * @throws Exception
     * @step. ^I see (\d+) (default )?messages? in the conversation view$"
     */
    @Then("^I see (\\d+) (default )?messages? in the conversation view$")
    public void ThenISeeMessageInTheDialog(int expectedCount, String isDefault) throws Exception {
        final Optional<String> expectedMsg = (isDefault == null) ?
                Optional.empty() : Optional.of(CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
        if (expectedCount == 0) {
            if (expectedMsg.isPresent()) {
                Assert.assertTrue(
                        String.format("There are some '%s' messages in the conversation, but zero is expected",
                                expectedMsg.get()),
                        getConversationViewPage().waitUntilTextMessageIsNotVisible(expectedMsg.get()));
            } else {
                Assert.assertTrue("There are some messages in the conversation, but zero is expected",
                        getConversationViewPage().waitUntilAllTextMessageAreNotVisible());
            }
        } else if (expectedCount >= 1) {
            if (expectedMsg.isPresent()) {
                Assert.assertTrue(
                        String.format("There are less than %d '%s' messages in the conversation, but %d is expected",
                                expectedCount, expectedMsg.get(), expectedCount),
                        getConversationViewPage().waitUntilTextMessagesAreVisible(expectedMsg.get(), expectedCount));
            } else {
                Assert.assertTrue(
                        String.format("There are no messages in the conversation, but %d is expected",
                                expectedCount),
                        getConversationViewPage().waitUntilAnyTextMessagesAreVisible(expectedCount));
            }
        }
    }

    @Then("^I see last message in the conversation view (is|contains) expected message (.*)")
    public void ThenISeeLasMessageIsd(String operation, String msg) throws Exception {
        msg = usrMgr.replaceAliasesOccurences(msg, FindBy.EMAIL_ALIAS);
        if (operation.equals("is")) {
            Assert.assertTrue(
                    String.format("The last message in the conversation is different from the expected one '%s'",
                            msg), getConversationViewPage().isLastMessageEqual(msg));
        } else {
            Assert.assertTrue(
                    String.format("The last message in the conversation does not contain the expected one '%s'",
                            msg), getConversationViewPage().isRecentMessageContain(msg));
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
                    String.format("The expected message '%s' is visible in the conversation view", expectedMsg),
                    getConversationViewPage().waitUntilPartOfTextMessageIsNotVisible(expectedMsg));
        }
    }

    /**
     * Tap the corresponding button from input tools palette
     *
     * @param isLongTap       equals to null if simple tap should be performed
     * @param btnName         one of available button names
     * @param durationSeconds specific time duration you press the button
     * @throws Exception
     * @step. ^I (long )?tap (Add Picture|Ping|Sketch|Share Location|File Transfer|Video Message|Audio Message|GIF)
     * button( for \\d+ seconds?)? from input tools$
     */
    @When("^I (long )?tap (Add Picture|Ping|Sketch|Share Location|File Transfer|Video Message|Audio Message|GIF) " +
            "button( for \\d+ seconds?)? from input tools$")
    public void ITapButtonByNameFromInputTools(String isLongTap, String btnName, String durationSeconds)
            throws Exception {
        if (isLongTap == null) {
            getConversationViewPage().tapInputToolButtonByName(btnName);
        } else {
            if (durationSeconds == null) {
                getConversationViewPage().longTapInputToolButtonByName(btnName);
            } else {
                getConversationViewPage().longTapWithDurationInputToolButtonByName(btnName,
                        Integer.parseInt(durationSeconds.replaceAll("[\\D]", "")));
            }
        }
    }

    /**
     * Verify visibility of the corresponding button in input tools palette
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I (do not )?see (Add Picture|Ping|Sketch|File Transfer|Audio Message|Video Message|GIF) button in input
     * tools palette$
     */
    @When("^I (do not )?see (Add Picture|Ping|Sketch|File Transfer|Audio Message|Video Message|GIF) button in input " +
            "tools palette$")
    public void VerifyButtonVisibilityInInputTools(String shouldNot, String btnName) throws Exception {
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

    @Then("^I see Pending Connect to (.*) message in the conversation view$")
    public void ISeePendingConnectMessage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("Connecting to %s is not visible", contact),
                getConversationViewPage().isConnectingToUserConversationLabelVisible(contact));
    }

    /**
     * Verify whether images are visible in the conversarion
     *
     * @param expectedCount the expected count of images
     * @throws Exception
     * @step. ^I see (\d+) photos? in the conversation view$
     */
    @Then("^I see (\\d+) photos? in the conversation view$")
    public void ISeeNewPhotoInTheDialog(int expectedCount) throws Exception {
        if (expectedCount == 0) {
            Assert.assertTrue("No images are expected to be visible in the conversations",
                    getConversationViewPage().areNoImagesVisible());
        } else {
            Assert.assertTrue(
                    String.format("%d images are expected to be visible in the conversations", expectedCount),
                    getConversationViewPage().areXImagesVisible(expectedCount));
        }
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
        getConversationViewPage().tapTextInput(false);
        getConversationViewPage().tapTextInput(true);
        getConversationViewPage().tapBadgeItem("Paste");
        getConversationViewPage().tapSendButton();
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
            if (getConversationViewPage().isMediaBarNotDisplayed()) {
                return;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
        throw new AssertionError(String.format("The media bar is still visible after %s seconds timeout",
                timeoutSeconds));
    }

    @When("^I tap Play in media bar$")
    public void ITapPlayInMediaBar() throws Exception {
        getConversationViewPage().playMediaContent();
    }

    @When("^I stop media in media bar$")
    public void IStopMediaInMediaBar() throws Exception {
        getConversationViewPage().stopMediaContent();
    }

//    private ElementState previousMediaContainerState = new ElementState(
//            () -> getConversationViewPage().getMediaContainerStateGlyphScreenshot()
//    );
//
//    /**
//     * Store the current media container state into an internal varibale
//     *
//     * @throws Exception
//     * @step. ^I remember media container state$
//     */
//    @When("^I remember media container state$")
//    public void IRememberContainerState() throws Exception {
//        previousMediaContainerState.remember();
//    }

    private ElementState previousAssetsContainerState;
    /**
     * Store the current assets container state into an internal varibale
     *
     * @throws Exception
     * @step. ^I remember assets container state$
     */
    @When("^I remember (Media|Audio|Video|File Share|Location|Image|GIF|Link Preview) container state$")
    public void IRememberAssetsContainerState(String assetType) throws Exception {
        previousAssetsContainerState= new ElementState(
                () -> getConversationViewPage().getAssetsContainerStateGlyphScreenshot(assetType)
        );
        previousAssetsContainerState.remember();
    }

    private static final int ASSET_CONTAINER_STATE_CHANGE_TIMEOUT = 10;

    /**
     * Verify whether the state of a media container is changed
     *
     * @param shouldNotChange equals to null if the state should not be changed
     * @throws Exception
     * @step. ^I see media container state is (not )?changed$
     */
    @Then("^I see asset container state is (not )?changed$")
    public void IVerifyAssetContainerState(String shouldNotChange) throws Exception {
        if (this.previousAssetsContainerState == null) {
            throw new IllegalStateException("Please remember the previous container state first");
        }
        final double minScore = 0.8;
        if (shouldNotChange == null) {
            Assert.assertTrue(String.format("The current asset container state is not different from the expected one after " +
                            "%s seconds timeout", ASSET_CONTAINER_STATE_CHANGE_TIMEOUT),
                    previousAssetsContainerState.isChanged(ASSET_CONTAINER_STATE_CHANGE_TIMEOUT, minScore));
        } else {
            Assert.assertTrue(String.format("The current asset container state is different from the expected one after " +
                            "%s seconds timeout", ASSET_CONTAINER_STATE_CHANGE_TIMEOUT),
                    previousAssetsContainerState.isNotChanged(ASSET_CONTAINER_STATE_CHANGE_TIMEOUT, minScore));
        }
    }

     private static final int MEDIA_STATE_CHANGE_TIMEOUT = 10;
//
//    /**
//     * Verify whether the state of a media container is changed
//     *
//     * @param shouldNotChange equals to null if the state should not be changed
//     * @throws Exception
//     * @step. ^I see media container state is (not )?changed$
//     */
//    @Then("^I see media container state is (not )?changed$")
//    public void IVerifyContainerState(String shouldNotChange) throws Exception {
//        if (this.previousMediaContainerState == null) {
//            throw new IllegalStateException("Please remember the previous container state first");
//        }
//        final double minScore = 0.8;
//        if (shouldNotChange == null) {
//            Assert.assertTrue(String.format("The current media state is not different from the expected one after " +
//                            "%s seconds timeout", MEDIA_STATE_CHANGE_TIMEOUT),
//                    previousMediaContainerState.isChanged(MEDIA_STATE_CHANGE_TIMEOUT, minScore));
//        } else {
//            Assert.assertTrue(String.format("The current media state is different from the expected one after " +
//                            "%s seconds timeout", MEDIA_STATE_CHANGE_TIMEOUT),
//                    previousMediaContainerState.isNotChanged(MEDIA_STATE_CHANGE_TIMEOUT, minScore));
//        }
//    }

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

    @Then("^I (do not )?see media bar in the conversation view$")
    public void ISeeMediaBarInDialog(String shouldNotSee) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().isMediaBarDisplayed() :
                getConversationViewPage().isMediaBarNotDisplayed();
        Assert.assertTrue(String.format("Media bar is expected to be %s",
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    @When("^I tap on the media bar$")
    public void ITapOnMediaBar() throws Exception {
        getConversationViewPage().tapOnMediaBar();
    }

    @Then("^I see conversation view is scrolled back to the playing media link (.*)")
    public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link) throws Throwable {
        Assert.assertTrue(String.format("The last conversation message does not contain text '%s'", link),
                getConversationViewPage().isRecentMessageContain(link));
        Assert.assertTrue("View did not scroll back", getConversationViewPage()
                .isMediaContainerVisible());
    }

    @Then("^I navigate back to conversations list")
    public void INavigateToConversationsList() throws Exception {
        getConversationViewPage().returnToConversationsList();
    }

    @When("^I scroll to the beginning of the conversation$")
    public void IScrollToTheBeginningOfTheConversation() throws Throwable {
        getConversationViewPage().scrollToBeginningOfConversation();
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
     * @step. ^I tap missed call button to call contact (.*)
     */
    @When("^I tap missed call button to call contact (.*)")
    public void IClickMissedCallButton(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getConversationViewPage().clickOnCallButtonForContact(contact.toUpperCase());
    }

    /**
     * Check whether text or ephemeral input placeholder text is visible or not
     *
     * @param shouldNotBeVisible equals to null if the placeholder should be visible
     * @param placeholderText    text of placeholder, either standard or ephemeral
     * @throws Exception
     * @step. ^I (do not )?see (Standard|Ephemeral) input placeholder text$
     */
    @Then("^I (do not )?see (Standard|Ephemeral) input placeholder text$")
    public void ISeeInputPlaceholderText(String shouldNotBeVisible, String placeholderText) throws Exception {
        boolean result;
        if (shouldNotBeVisible == null) {
            result = getConversationViewPage().isPlaceholderTextVisible(placeholderText);
        } else {
            result = getConversationViewPage().isPlaceholderTextInvisible(placeholderText);
        }
        Assert.assertTrue(
                String.format("'%s' placeholder text should be %s", placeholderText, (shouldNotBeVisible == null) ? "visible" : "not visible"),
                result
        );
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
     * Verify if conversation view page with pointed user is shown.
     *
     * @param contact contact name
     * @throws Exception
     * @step. ^I see the conversation with (.*)$
     */
    @When("^I see the conversation with (.*)$")
    public void ISeeConversationWith(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Conversation with %s is not visible", contact),
                getConversationViewPage().isUserNameInUpperToolbarVisible(contact));
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
        Assert.assertFalse("Media player is visible, but should be hidden",
                getConversationViewPage().isMediaContainerVisible());
        Assert.assertTrue(String.format("The last conversation message does not contain %s link", link),
                getConversationViewPage().isRecentMessageContain(link));
    }

    /**
     * Verifies that vimeo link and the video container is visible
     *
     * @param link of vimeo video
     * @throws Exception
     * @step. ^I see vimeo link (.*) and media in the conversation view$
     */
    @Then("^I see vimeo link (.*) and media in the conversation view$")
    public void ISeeVimeoLinkAndMediaInDialog(String link) throws Exception {
        Assert.assertTrue("Media player is missing in the conversation",
                getConversationViewPage().isMediaContainerVisible());
        Assert.assertTrue(String.format("The recent conversation message does not contain %s link", link),
                getConversationViewPage().isRecentMessageContain(link));
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
     * Select the corresponding item from the modal menu, which appears after Delete badge is tapped
     *
     * @param name one of possible item names
     * @throws Exception
     * @step. ^I select (Delete for Me|Delete for Everyone|Cancel) item from Delete menu$
     */
    @And("^I select (Delete for Me|Delete for Everyone|Cancel) item from Delete menu$")
    public void ISelectDeleteMenuItem(String name) throws Exception {
        getConversationViewPage().selectDeleteMenuItem(name);
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
     * Verify whether particular message is present in the conversation input
     *
     * @param expectedMessage either 'default' or the expected message's text
     * @throws Exception
     * @step. ^I see the (default|".*") message in the conversation input$
     */
    @Then("^I see the (default|\".*\") message in the conversation input$")
    public void ISeeMessageInTheInput(String expectedMessage) throws Exception {
        if (expectedMessage.equals("default")) {
            expectedMessage = CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE;
        } else {
            expectedMessage = expectedMessage.replaceAll("^\"|\"$", "");
        }
        Assert.assertTrue(String.format("The text '%s' is not present in the conversation input", expectedMessage),
                getConversationViewPage().isCurrentInputTextEqualTo(expectedMessage));
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
     * Verify whether shield icon is visible next to convo input field
     *
     * @param shouldNotSee equals to null if the shield should be visible
     * @throws Exception
     * @step. ^I (do not )?see shield icon in the conversation view$
     */
    @Then("^I (do not )?see shield icon in the conversation view$")
    public void ISeeShieldIcon(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The shield icon is not visible",
                    getConversationViewPage().isShieldIconVisible());
        } else {
            Assert.assertTrue("The shield icon is visible, but should be hidden",
                    getConversationViewPage().isShieldIconInvisible());
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
        getConversationViewPage().tapThisDeviceLink();
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
     * @step. ^I see Upper Toolbar on the conversation view$
     */
    @Then("^I see Upper Toolbar in the conversation view$")
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

    private static final double MAX_SIMILARITY_THRESHOLD = 0.98;

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
        getConversationViewPage().tapFileTransferMenuItem(itemName);
    }

    /**
     * Verify visibility of the corresponding file transfer menu item
     *
     * @param itemName name of the item
     * @throws Exception
     * @step. ^I see file transfer menu item (.*)
     */
    @When("^I see file transfer menu item (.*)")
    public void ISeeFileTransferMenuItem(String itemName) throws Exception {
        Assert.assertTrue(String.format("File transfer menu item '%s' is not visible", itemName),
                getConversationViewPage().isFileTransferMenuItemVisible(itemName));
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
     * Tap on file transfer action button to download/preview file
     *
     * @throws Exception
     * @step. ^I tap file transfer action button
     */
    @When("^I tap file transfer action button$")
    public void ITapFileTransferActionButton() throws Exception {
        getConversationViewPage().tapFileTransferActionButton();
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
     * @param msg         message text
     * @param isLongTap   equals to null if normal tap should be performed
     * @param isDoubleTap equals to null if non-double tap should be performed
     * @throws Exception
     * @step. ^I (long )?tap last (default\".*\") message in conversation view$
     */
    @When("^I (long )?(double )?tap (default|\".*\") message in conversation view$")
    public void ITapAndHoldTextMessage(String isLongTap, String isDoubleTap, String msg) throws Exception {
        if (msg.equals("default")) {
            msg = CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE;
        } else {
            msg = msg.replaceAll("^\"|\"$", "");
        }
        getConversationViewPage().tapMessageByText(isLongTap != null, isDoubleTap != null, msg);
    }

    /**
     * Tap pointed control button
     *
     * @throws Exception
     * @step. ^I tap (Send|Cancel) record control button
     */
    @When("^I tap (Send|Cancel|Play) record control button$")
    public void ITapRecordControlButton(String buttonName) throws Exception {
        getConversationViewPage().tapRecordControlButton(buttonName);
    }

    /**
     * Verify visibility of the corresponding record control button
     *
     * @throws Exception
     * @step. ^I see (Send|Cancel|Play) record control button$
     */
    @Then("^I see (Send|Cancel|Play) record control button$")
    public void ISeeRecordControlButton(String buttonName) throws Exception {
        Assert.assertTrue(String.format("Record control button '%s' is not visible", buttonName),
                getConversationViewPage().isRecordControlButtonVisible(buttonName));
    }

    /**
     * Tap on record audio button waits and swipe up to send audio message
     *
     * @param sec time in seconds
     * @throws Exception
     * @step. ^I record (\d+) seconds? audio meassage and send by swipe up$
     */
    @When("^I record (\\d+) seconds? long audio message and send it using swipe up gesture$")
    public void IRecordXSecondsAudioMessageAndSendBySwipe(int sec) throws Exception {
        getConversationViewPage().tapAudioRecordWaitAndSwipe(sec);
    }

    /**
     * Long tap on a conversation view item
     *
     * @param conversationItem item name
     * @param isLongTap        equals to null if simple tap should be performed
     *                         Works with long tap only
     * @param isDoubleTap      os not equal to null if double tap should be performed
     * @throws Exception
     * @step. ^I (long )?(double )?tap on " +
     * "(image|media container|file transfer placeholder|audio message placeholder|video message|location map|link preview) " +
     * "in conversation view$
     */
    @When("^I (long )?(double )?tap on " +
            "(image|media container|file transfer placeholder|audio message placeholder|video message|location map|link preview) " +
            "in conversation view$")
    public void ITapMessagePlaceholder(String isLongTap, String isDoubleTap, String conversationItem) throws Exception {
        getConversationViewPage().tapContainer(conversationItem, isLongTap != null, isDoubleTap != null);
    }

    /**
     * Tap Play/Pause audio message button
     *
     * @param placeholderIndex optional parameter. If exists then button state for the particular placeholder will
     *                         be tap.
     *                         The most recent  audio message placeholder is the conversation view will have index 1
     * @throws Exception
     * @step. ^I tap (?:Play|Pause) audio message button (on audio message placeholder number \d+)?$
     */
    @When("^I tap (?:Play|Pause) audio message button( on audio message placeholder number \\d+)?$")
    public void ITapPlayAudioMessageButton(String placeholderIndex) throws Exception {
        if (placeholderIndex == null) {
            getConversationViewPage().tapPlayAudioMessageButton();
        } else {
            getConversationViewPage().tapPlayAudioMessageButton(
                    Integer.parseInt(placeholderIndex.replaceAll("[\\D]", "")));
        }
    }

    private ElementState playButtonState;

    /**
     * Remember the current state of Play/Pause button on Audio Message placeholder
     *
     * @param isSecond optional parameter. If exists then button state for the particular placeholder will be verified.
     *                 The most recent  audio message placeholder is the conversation view will have index 1
     * @throws Exception
     * @step. ^I remember the state of (?:Play|Pause) button on audio message placeholder$
     */
    @When("^I remember the state of (?:Play|Pause) button on (the second )?audio message placeholder?$")
    public void IRememberPlayButtonState(String isSecond) throws Exception {
        if (isSecond == null) {
            playButtonState = new ElementState(getConversationViewPage()::getFirstPlayAudioMessageButtonScreenshot);
        } else {
            playButtonState = new ElementState(getConversationViewPage()::getSecondPlayAudioMessageButtonScreenshot);
        }
        playButtonState.remember();
    }

    private static final int PLAY_BUTTON_STATE_CHANGE_TIMEOUT = 7;
    private static final double PLAY_BUTTON_MIN_SIMILARITY = 0.95;

    /**
     * Verify the sate of Play/Pause button has been changed
     *
     * @param didNotChange equals to null if button state should be changed
     * @throws Exception
     * @step. ^I verify the state of (?:Play|Pause) button on audio message placeholder is changed$
     */
    @Then("^I verify the state of (?:Play|Pause) button on audio message placeholder is (not )?changed$")
    public void IVerifyPlayButtonState(String didNotChange) throws Exception {
        if (playButtonState == null) {
            throw new IllegalStateException("Please remember button state first");
        }
        if (didNotChange == null) {
            Assert.assertTrue(String.format("The state of the button has not been changed after %s seconds",
                    PLAY_BUTTON_STATE_CHANGE_TIMEOUT), playButtonState.isChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                    PLAY_BUTTON_MIN_SIMILARITY));
        } else {
            Assert.assertTrue(String.format("The state of the button has changed after %s seconds",
                    PLAY_BUTTON_STATE_CHANGE_TIMEOUT), playButtonState.isNotChanged(PLAY_BUTTON_STATE_CHANGE_TIMEOUT,
                    PLAY_BUTTON_MIN_SIMILARITY));
        }
    }


    /**
     * Verify audio message in placeholder|record toolbar state after time label value
     *
     * @param playerType placeholder or record toolbar
     * @param state      played or paused
     * @throws Exception
     * @step. ^I see the audio message in (placeholder|record toolbar) gets (played|paused)$
     */
    @Then("^I see the audio message in (placeholder|record toolbar) gets (played|paused)$")
    public void ISeeTheAudioMessageGetsPlayed(String playerType, String state) throws Exception {
        FunctionalInterfaces.ISupplierWithException<Boolean> verificationFunc =
                (playerType.equals("placeholder")) ? getConversationViewPage()::isPlaceholderTimeLabelValueChanging :
                        getConversationViewPage()::isRecordTimeLabelValueChanging;
        switch (state) {
            case "played":
                Assert.assertTrue(String.format("The Audio message in %s did not get played. StartTime is the same as " +
                        "CurrentTime", playerType), verificationFunc.call());
                break;
            case "paused":
                Assert.assertFalse(String.format("The Audio message in %s did not get paused. StartTime is not the same as " +
                        "CurrentTime", playerType), verificationFunc.call());
                break;
            default:
                throw new IllegalArgumentException("Allowed states are 'played|paused'");
        }
    }

    /**
     * Verify play/pause button state in audio message placeholder
     *
     * @param placeholderIndex optional parameter. If exists then button state for the particular placeholder will
     *                         be verified.
     *                         The most recent  audio message placeholder is the conversation view will have index 1
     * @param buttonState      play or pause
     * @throws Exception
     * @step. ^I see state of button on audio message placeholder (number \d+ )?is (play|pause)$
     */
    @Then("^I see state of button on audio message placeholder (number \\d+ )?is (play|pause)$")
    public void ISeeAudioMessageControlButtonStateIs(String placeholderIndex, String buttonState) throws Exception {
        Assert.assertTrue(String.format("Wrong button state. Expected state is '%s'", buttonState),
                getConversationViewPage().isPlaceholderAudioMessageButtonState(buttonState,
                        (placeholderIndex == null) ? 1 : Integer.parseInt(placeholderIndex.replaceAll("[\\D]", ""))));
    }

    /**
     * Verify state of record control button playing or idle
     *
     * @param buttonState should be "playing" or "idle"
     * @throws Exception
     * @step. ^I see state of button on record toolbar is (playing|idle)$
     */
    @Then("^I see state of button on record toolbar is (playing|idle)$")
    public void IseeRecordToolbarButtonStateIs(String buttonState) throws Exception {
        Assert.assertTrue(String.format("Wrong button state. Expected state is '%s'", buttonState),
                getConversationViewPage().isRecordControlButtonState(buttonState));
    }

    /**
     * Verify visibility of default Map application
     *
     * @throws Exception
     * @step. ^I see map application is opened$
     */
    @Then("^I see map application is opened$")
    public void VerifyMapDefaultApplicationVisibility() throws Exception {
        Assert.assertTrue("The default map application is not visible",
                getConversationViewPage().isDefaultMapApplicationVisible());
    }

    /**
     * Verify whether container is visible in the conversation
     *
     * @param shouldNotSee  equals to null if the container should be visible
     * @param containerType media|video message|audio message record|location map|link previeww
     * @throws Exception
     * @step. ^I (do not )?see (media|video message|audio message record|location map|link preview) container in the
     * conversation view$
     */
    @Then("^I (do not )?see (media|video message|audio message recorder|audio message|location map|link preview) " +
            "container in the conversation view$")
    public void ISeeContainer(String shouldNotSee, String containerType) throws Exception {
        final boolean condition = (shouldNotSee == null) ?
                getConversationViewPage().isContainerVisible(containerType) :
                getConversationViewPage().isContainerInvisible(containerType);
        Assert.assertTrue(String.format("%s should be %s in the conversation view",
                WordUtils.capitalize(containerType), (shouldNotSee == null) ? "visible" : "invisible"),
                condition);
    }

    /**
     * Verify link preview image visibility
     *
     * @param shouldNotBeVisible equals to null if the placeholder should be visible
     * @throws Exception
     * @step. ^I (do not )?see link preview image in the conversation view
     */
    @When("^I (do not )?see link preview image in the conversation view$")
    public void ISeeLinkPreviewImage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Link preview image is not visible",
                    getConversationViewPage().isLinkPreviewImageVisible());
        } else {
            Assert.assertTrue("Link preview image is visible, but should be hidden",
                    getConversationViewPage().isLinkPreviewImageInvisible());
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
    public void ISeeMassagesHaveEqualHeight(String msg1, String msg2, String isNot, int expectedPercentage)
            throws Exception {
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
                    String.format("The height of '%s' message (%s) is less than %s%% different than the height of "
                            + "'%s' message (%s)", msg1, msg1Height, expectedPercentage, msg2, msg2Height),
                    currentPercentage >= expectedPercentage);
        } else {
            Assert.assertTrue(
                    String.format("The height of '%s' message (%s) is more than %s%% different than the height of "
                            + "'%s' message (%s)", msg1, msg1Height, expectedPercentage, msg2, msg2Height),
                    currentPercentage <= expectedPercentage);
        }
    }

    /**
     * Verify whether the Deleted on label is present for a message from a user
     *
     * @param nameAlias user name/alias
     * @throws Exception
     * @step. ^I see that Deleted label for a message from (.*) is present in the conversation view$
     */
    @Then("^I see that Deleted label for a message from (.*) is present in the conversation view$")
    public void ISeeDeletedLabel(String nameAlias) throws Exception {
        nameAlias = usrMgr.replaceAliasesOccurences(nameAlias, FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Deleted label is not present for a message from %s in the conversation view",
                nameAlias), getConversationViewPage().isDeletedOnLabelPresent(nameAlias));
    }

    /**
     * Verify that relevant delete menu item is not displayed
     *
     * @param name item name
     * @throws Exception
     * @step. ^I do not see (Delete for Everyone|Delete for Me|Cancel) item in Delete menu$
     */
    @Then("^I do not see (Delete for Everyone|Delete for Me|Cancel) item in Delete menu$")
    public void IDoNotSeeItemInDeleteMenu(String name) throws Exception {
        Assert.assertTrue(String.format("'%s' Delete menu item shouldn't be visible", name),
                getConversationViewPage().deleteMenuItemNotVisible(name));
    }

    /**
     * Tap the corresponding button on the control, which appears if I select
     * Edit basge item for a conversation item
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Undo|Confirm|Cancel) button on Edit control
     */
    @When("^I tap (Undo|Confirm|Cancel) button on Edit control")
    public void ITapEditControlButton(String btnName) throws Exception {
        getConversationViewPage().tapEditControlButton(btnName);
    }

    /**
     * Verify whether the expectced URL is visible on link preview container
     *
     * @param expectedSrc the expected URL to check. Only host name will be extracted and transformed to
     *                    lowercase for UI comparison
     * @throws Exception
     * @step. ^I see link preview source is equal to (.*)
     */
    @Then("^I see link preview source is equal to (.*)")
    public void ISeeLinkPreviewSource(String expectedSrc) throws Exception {
        Assert.assertTrue(String.format("The link preview source %s is not visible in the conversation view",
                expectedSrc), getConversationViewPage().isLinkPreviewSourceVisible(expectedSrc));
    }

    /**
     * Verify visibility of Edit message control buttton
     *
     * @param shouldNotSee equals to null if the toolbar should be visible
     * @param buttonName   name of the button
     * @throws Exception
     * @step. ^I (do not )?see (Undo|Confirm|Cancel) button on Edit Message control$
     */
    @Then("^I (do not )?see (Undo|Confirm|Cancel) button on Edit Message control$")
    public void VerifyVisibilityOfEditMessageControlButton(String shouldNotSee, String buttonName) throws Exception {
        boolean condition;
        if (shouldNotSee == null) {
            condition = getConversationViewPage().editControlButtonIsVisible(buttonName);
        } else {
            condition = getConversationViewPage().editControlButtonIsNotVisible(buttonName);
        }
        Assert.assertTrue(String.format("Edit message control button '%s' should be %s", buttonName,
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Verify if pointed message is presented on relevant position in conversation view
     *
     * @param message  message text to verify
     * @param position expected index of message in conversation view (the last one message is index [1])
     * @throws Exception
     * @step. I see message "(.*)" is on (\d+) position in conversation view$
     */
    @Then("^I see message \"(.*)\" is on (\\d+) position in conversation view$")
    public void ISeeMessageIsOnXPositionInConversation(String message, int position) throws Exception {
        Assert.assertTrue(String.format("Message '%s' is not presented on %s position in conversation view", message, position),
                getConversationViewPage().isMessageByPositionDisplayed(message, position));
    }

    private static final int LIKE_ICON_STATE_CHANGE_TIMEOUT = 7; //seconds
    private static final double LIKE_ICON_MIN_SIMILARITY = 0.9;
    private ElementState likeIconState = new ElementState(
            () -> getConversationViewPage().getLikeIconState()
    );

    /**
     * Store the current state of Like icon
     *
     * @throws Exception
     * @step. ^I remember the state of (?:Like|Unlike) icon in the conversation$
     */
    @When("^I remember the state of (?:Like|Unlike) icon in the conversation$")
    public void IRememberLikeIconState() throws Exception {
        this.likeIconState.remember();
    }

    /**
     * Verify whether the current state of Like icon differs from the previously remembered one
     *
     * @param shouldNotChange equals to null if the state should be changed
     * @throws Exception
     * @step. ^I see the state of (?:Like|Unlike) icon is (not )?changed in the conversation$
     */
    @Then("^I see the state of (?:Like|Unlike) icon is (not )?changed in the conversation$")
    public void IVerifyLikeIconState(String shouldNotChange) throws Exception {
        boolean condition;
        if (shouldNotChange == null) {
            condition = this.likeIconState.isChanged(LIKE_ICON_STATE_CHANGE_TIMEOUT, LIKE_ICON_MIN_SIMILARITY);
        } else {
            condition = this.likeIconState.isNotChanged(LIKE_ICON_STATE_CHANGE_TIMEOUT, LIKE_ICON_MIN_SIMILARITY);
        }
        Assert.assertTrue(String.format("Like icon state is expected %s in %s seconds",
                (shouldNotChange == null) ? "to be changed" : "to be not changed", LIKE_ICON_STATE_CHANGE_TIMEOUT),
                condition);
    }

    /**
     * Tap Like/Unlike icon in the conversation
     *
     * @throws Exception
     * @step. ^I tap (?:Like|Unlike) icon in the conversation$
     */
    @And("^I tap (?:Like|Unlike) icon in the conversation$")
    public void ITapLikeIcon() throws Exception {
        getConversationViewPage().tapLikeIcon();
    }

    /**
     * Verify visibility of the Like/Unlike icon
     *
     * @param shouldNotSee eqauls to null if the icon should be visible
     * @throws Exception
     * @step. ^I (do not )?see (?:Like|Unlike) icon in the conversation$
     */
    @Then("^I (do not )?see (?:Like|Unlike) icon in the conversation$")
    public void ISeeLikeIcon(String shouldNotSee) throws Exception {
        boolean condition;
        if (shouldNotSee == null) {
            condition = getConversationViewPage().isLikeIconVisible();
        } else {
            condition = getConversationViewPage().isLikeIconInvisible();
        }
        Assert.assertTrue(String.format("The Like/Unlike icon is expected to be %s",
                (shouldNotSee == null) ? "visible" : "invisible"), condition);
    }

    /**
     * Tap the toolbox of the recent message to open likers list
     *
     * @throws Exception
     * @step. ^I tap toolbox of the recent message$
     */
    @When("^I tap toolbox of the recent message$")
    public void ITapMessageToolbox() throws Exception {
        getConversationViewPage().tapRecentMessageToolbox();
    }

    /**
     * Tap the recent media container to show/hide like icon
     *
     * @param pWidth  destination cell X tap point (in percent 0-100)
     * @param pHeight destination cell Y tap point (in percent 0-100)
     * @throws Exception
     * @step. I tap at (\d+)% of width and (\d+)% of height of the recent message$
     */
    @When("^I tap at (\\d+)% of width and (\\d+)% of height of the recent message$")
    public void ITapAtContainerCorner(int pWidth, int pHeight) throws Exception {
        getConversationViewPage().tapAtRecentMessage(pWidth, pHeight);
    }

    /**
     * Tap relevant button on image
     *
     * @param buttonName Sketch ot Fullscreen button names allowed
     * @throws Exception
     * @step. ^I tap (Sketch|Fullscreen) button on image$
     */
    @When("^I tap (Sketch|Fullscreen) button on image$")
    public void ITapOnImageButtons(String buttonName) throws Exception {
        getConversationViewPage().tapImageButton(buttonName);
    }

    /**
     * Tap the corresponding key on Emoji keyboard. Tap by name does not work properly there.
     *
     * @param keyIndex Keys enumeration starts at the top left corner and finishes at
     *                 the bottom right corner of the keyboard. The first key has index 1
     * @throws Exception
     * @step. ^I tap number (\d+) key on Emoji Keyboard$
     */
    @When("^I tap key number (\\d+) on Emoji Keyboard$")
    public void TapKeyOnEmojiKeyboard(int keyIndex) throws Exception {
        getConversationViewPage().tapEmojiKeyboardKey(keyIndex);
    }

    /**
     * Verify whether the particular text is present or not on message toolbox
     *
     * @param shouldNotSee equals to null if the text should be visible
     * @param expectedText part of the text to verify for presence
     * @throws Exception
     * @step. ^I (do not )?see "(.*)" on the message toolbox in conversation view$
     */
    @Then("^I (do not )?see \"(.*)\" on the message toolbox in conversation view$")
    public void ISeeTextOnToolbox(String shouldNotSee, String expectedText) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(
                    String.format("The expected '%s' text is not visible on the message toolbox", expectedText),
                    getConversationViewPage().isMessageToolboxTextVisible(expectedText)
            );
        } else {
            Assert.assertTrue(
                    String.format("The expected '%s' text should not be visible on the message toolbox", expectedText),
                    getConversationViewPage().isMessageToolboxTextInvisible(expectedText)
            );
        }
    }

    /**
     * Set ephemeral messages timer to a corresponding value
     *
     * @param value one of available timer values
     * @throws Exception
     * @step. ^I set ephemeral messages expiration timer to (Off|5 seconds|15 seconds|1 minute|15 minutes)$
     */
    @And("^I set ephemeral messages expiration timer to (Off|5 seconds|15 seconds|1 minute|15 minutes)$")
    public void ISetExpirationTimer(String value) throws Exception {
        getConversationViewPage().setMessageExpirationTimer(value);
    }
}

