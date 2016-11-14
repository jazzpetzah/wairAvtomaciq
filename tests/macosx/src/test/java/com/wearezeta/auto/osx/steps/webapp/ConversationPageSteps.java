package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConversationPageSteps {
    
    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.75;

    private static final String TOOLTIP_PING = "Ping";
    private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + K)";
    private static final String SHORTCUT_PING_MAC = "(⌘⌥K)";
    private static final String TOOLTIP_CALL = "Call";
    private static final String SHORTCUT_CALL_WIN = "(Ctrl + Alt + R)";
    private static final String SHORTCUT_CALL_MAC = "(⌘⌥R)";

    private String randomMessage;
    
    private final TestContext webContext;
    private final TestContext wrapperContext;
    
    public ConversationPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public ConversationPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    /**
     * Sends random message (generated GUID) into opened conversation
     *
     * @step. ^I write random message$
     * @throws Exception
     */
    @When("^I write random message$")
    public void WhenIWriteRandomMessage() throws Exception {
        randomMessage = CommonUtils.generateGUID();
        IWriteMessage(randomMessage);
    }

    /**
     * Verify that the input text field contains random message
     *
     */
    @Then("^I verify that random message was typed$")
    public void IVerifyThatRandomMessageWasTyped() throws Exception {
        assertThat("Random message in input field", webContext.getPagesCollection()
                .getPage(ConversationPage.class).getMessageFromInputField(),
                equalTo(randomMessage));
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message \"(.*)\" was typed$")
    public void IVerifyThatMessageWasTyped(String message) throws Exception {
        assertThat("Message in input field",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(message));
    }

    /**
     * Types text message to opened conversation, but does not send it
     *
     * @step. ^I write message (.*)$
     *
     * @param message text message
     * @throws Exception
     */
    @When("^I write message (.*)$")
    public void IWriteMessage(String message) throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(
                message);
    }

    /**
     * Types x number of new lines to opened conversation, but does not send them
     *
     * @step. ^I write (.*) new lines$
     *
     * @param amount number of lines to write
     * @throws Exception
     */
    @When("^I write (\\d+) new lines$")
    public void IWriteXNewLines(int amount) throws Exception {
        String message = "";
        for (int i = 0; i < amount; i++) {
            message = message + Keys.chord(Keys.ALT, Keys.ENTER);
        }
        webContext.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(
                message);
    }
    
    /**
     * Deletes N characters from conversation input
     *
     * @param count count of characters
     * @step. "^I delete (\d+) characters from the conversation input$"
     */
    @When("^I delete (\\d+) characters from the conversation input$")
    public void IDeleteTypedMessage(int count) throws Exception {
        int i = count;
        while (i != 0) {
            webContext.getPagesCollection().getPage(ConversationPage.class).clearConversationInput();
            i--;
        }
    }

    /**
     * Submits entered message for sending
     *
     * @step. ^I send message$
     */
    @When("^I send message$")
    public void WhenISendMessage() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).sendNewMessage();
    }

    /**
     * Checks that last sent random message appear in conversation
     *
     * @step. ^I see random message in conversation$
     * @throws Exception
     *
     * @throws AssertionError if message did not appear in conversation
     */
    @Then("^I see random message in conversation$")
    public void ThenISeeRandomMessageInConversation() throws Exception {
        Assert.assertTrue(webContext.getPagesCollection().getPage(ConversationPage.class)
                .isMessageSent(randomMessage));
    }

    /**
     * Verifies whether YouTube video is visible
     *
     * @step. ^I see embedded youtube video of (.*)
     *
     * @throws Exception
     *
     */
    @Then("^I see embedded youtube video of (.*)")
    public void ThenISeeEmbeddedYoutubeVideoOf(String url) throws Exception {
        Assert.assertTrue(webContext.getPagesCollection().getPage(ConversationPage.class)
                .isMessageEmbedded(true, "youtube", url));
    }

    /**
     * Click People button in 1:1 conversation
     *
     * @step. I click People button in one to one conversation$
     *
     * @throws Exception
     */
    @When("^I click People button in one to one conversation$")
    public void WhenIClickPeopleButtonIn1to1() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class)
                .clickPeopleButton();
    }

    /**
     * Click People button in a group conversation to close People Popover
     *
     * @step. ^I close Group Participants popover$
     *
     * @throws Exception if the popover is not visible
     */
    @When("^I close Group Participants popover$")
    public void WhenICloseGroupParticipantsPopover() throws Exception {
        GroupPopoverContainer peoplePopoverPage = webContext.getPagesCollection()
                .getPage(GroupPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            webContext.getPagesCollection().getPage(ConversationPage.class)
                    .clickPeopleButton();
        }
    }

    /**
     * Click People button in 1:1 conversation to close People Popover
     *
     * @step. ^I close Single User Profile popover$
     *
     * @throws Exception if the popover is not visible
     */
    @When("^I close Single User Profile popover$")
    public void WhenICloseSingleUserPopover() throws Exception {
        SingleUserPopoverContainer peoplePopoverPage = webContext.getPagesCollection()
                .getPage(SingleUserPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            webContext.getPagesCollection().getPage(ConversationPage.class)
                    .clickPeopleButton();
        }
    }

    /**
     * Click People button in a group conversation
     *
     * @step. I click People button in group conversation$
     *
     * @throws Exception
     */
    @When("^I click People button in group conversation$")
    public void WhenIClickPeopleButtonInGroup() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class)
                .clickPeopleButton();
    }

    /**
     * Send a picture into current conversation
     *
     * @step. ^I send picture (.*) to the current conversation$
     *
     * @param pictureName the name of a picture file. This file should already exist in the ~/Documents folder
     *
     * @throws Exception
     */
    @When("^I send picture (.*) to the current conversation$")
    public void WhenISendPicture(String pictureName) throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).sendPicture(
                pictureName);
    }

    /**
     * Verifies whether previously sent picture exists in the conversation view
     *
     * @step. ^I see sent picture (.*) in the conversation view$
     *
     * @param pictureName the name of a picture file. This file should already exist in the ~/Documents folder
     * @throws Exception
     */
    @Then("^I see sent picture (.*) in the conversation view$")
    public void ISeeSentPicture(String pictureName) throws Exception {
        assertThat("Overlap score of image comparsion",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getOverlapScoreOfLastImage(pictureName),
                greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
    }

    /**
     * Verifies that only x images are in the conversation. Helps with checking for doubles.
     *
     * @step. ^I see only (\\d+) picture[s]? in the conversation$
     *
     * @param x the amount of images
     */
    @Then("^I see only (\\d+) picture[s]? in the conversation$")
    public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
        assertThat("Number of images in the conversation",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Verifies whether people button tool tip is correct or not.
     *
     * @step. ^I see correct people button tool tip$
     *
     */
    @Then("^I see correct people button tool tip$")
    public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
        Assert.assertTrue(webContext.getPagesCollection().getPage(ConversationPage.class)
                .isPeopleButtonToolTipCorrect());
    }

    @Then("^I see connecting message for (.*) in conversation$")
    public void ISeeConnectingMessage(String contact) throws Exception {
        contact = webContext.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        assertThat("User name",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getConnectedMessageUser(), equalTo(contact));
        assertThat("Label",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getConnectedMessageLabel(), equalTo("CONNECTING"));
    }

    @Then("^I see connected message for (.*) in conversation$")
    public void ISeeConnectedMessage(String contact) throws Exception {
        contact = webContext.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        assertThat("User name",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getConnectedMessageUser(), equalTo(contact));
        assertThat("Label",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getConnectedMessageLabel(), equalTo("CONNECTED"));
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param doNot if not null, checks if the action message does not display
     * @param message constant part of the system message
     * @throws Exception
     * @throws AssertionError if action message did not appear in conversation
     * @step. ^I see (.*) action in conversation$
     */
    @Then("^I( do not)? see (.*) action in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message) throws Exception {
        if (doNot == null) {
            ThenISeeActionInConversation(message, 1);
        } else {
            ThenISeeActionInConversation(message, 0);
        }
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message constant part of the system message
     * @param times number of times the message appears
     * @throws Exception
     * @throws AssertionError if action message did not appear in conversation
     * @step. ^I see (.*) action in conversation$
     */
    @Then("^I see (.*) action (\\d+) times in conversation$")
    public void ThenISeeActionInConversation(String message, int times) throws Exception {
        assertThat(message + " action", webContext.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(message), equalTo(times));
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param doNot if not null, checks if the action message does not display
     * @param message constant part of the system message
     * @param contacts list of comma separated contact names/aliases
     * @throws AssertionError if action message did not appear in conversation
     * @throws Exception
     * @step. ^I see (.*) action for (.*) in conversation$
     */
    @Then("^I( do not)? see (.*) action for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot, String message, String contacts) throws Exception {
        if (doNot == null) {
            ThenISeeActionForContactInConversation(message, 1, contacts);
        } else {
            ThenISeeActionForContactInConversation(message, 0, contacts);
        }
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message constant part of the system message
     * @param times number of times the message appears
     * @param contacts list of comma separated contact names/aliases
     * @throws AssertionError if action message did not appear in conversation
     * @throws Exception
     * @step. ^I see (.*) action for (.*) in conversation$
     */
    @Then("^I see (.*) action (\\d+) times for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String message, int times, String contacts) throws Exception {
        contacts = webContext.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(CommonSteps.splitAliases(contacts));
        assertThat(message + " action for " + contacts, webContext.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(parts), equalTo(times));
    }

    /**
     * Click ping button to send ping and hot ping
     *
     * @step. ^I click ping button$
     * @throws Exception
     */
    @When("^I click ping button$")
    public void IClickPingButton() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickPingButton();
    }

    /**
     * Verify a text message is visible in conversation.
     *
     * @step. ^I see text message (.*)
     * @param message
     * @throws Exception
     */
    @Then("^I see text message (.*)")
    public void ISeeTextMessage(String message) throws Exception {
            webContext.getPagesCollection().getPage(ConversationPage.class)
                    .waitForTextMessageContains(message);
    }
    
    /**
     * Verify a text message is not visible in conversation
     *
     * @step. ^I do not see text message ?(.*)$
     * @param message
     * @throws Exception
     */
    @Then("^I do not see text message ?(.*)$")
    public void IDontSeeTextMessage(String message) throws Exception {
        Assert.assertTrue("Saw text message " + message,
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .isTextMessageInvisible(message == null ? "" : message));
    }

    private static String expandPattern(final String originalStr) {
        final String lineBreak = "LF";
        final Pattern p = Pattern
                .compile("\\(\\s*'(\\w+)'\\s*\\*\\s*([0-9]+)\\s*\\)");
        final Matcher m = p.matcher(originalStr);
        final StringBuilder result = new StringBuilder();
        int lastPosInOriginalString = 0;
        while (m.find()) {
            if (m.start() > lastPosInOriginalString) {
                result.append(originalStr.substring(lastPosInOriginalString,
                        m.start()));
            }
            final String toAdd = m.group(1).replace(lineBreak, "\n");
            final int times = Integer.parseInt(m.group(2));
            for (int i = 0; i < times; i++) {
                result.append(toAdd);
            }
            lastPosInOriginalString = m.end();
        }
        if (lastPosInOriginalString < originalStr.length()) {
            result.append(originalStr.substring(lastPosInOriginalString,
                    originalStr.length()));
        }
        return result.toString();
    }

    /**
     * Verify the text of the last text message in conversation. Patterns are allowed, for example ('a' * 100) will print the a
     * character 100 times. Line break is equal to LF char sequence.
     *
     * @step. ^I verify the last text message equals to (.*)
     * @param expectedMessage the expected message
     * @throws Exception
     */
    @Then("^I verify the last text message equals to (.*)")
    public void IVerifyLastTextMessage(String expectedMessage) throws Exception {
        Assert.assertEquals(expandPattern(expectedMessage),
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getLastTextMessage());
    }

    /**
     * Verify the text of the second last text message in conversation. This step should only be used after verifying the last
     * message of the conversation, because otherwise you might run into a race condition.
     *
     * @step. ^I verify the second last text message equals to (.*)
     * @param expectedMessage the expected message
     * @throws Exception
     */
    @Then("^I verify the second last text message equals to (.*)")
    public void IVerifySecondLastTextMessage(String expectedMessage)
            throws Exception {
        assertThat(webContext.getPagesCollection().getPage(ConversationPage.class)
                .getSecondLastTextMessage(), equalTo(expectedMessage));
    }

    /**
     * Verify that there is only one ping message visible in conversation
     *
     * @step. ^I see only one ping message$
     * @throws Exception
     */
    @When("^I see only one ping message$")
    public void ISeeOnlyOnePingMessage() throws Exception {
        assertThat("PING action", webContext.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(Collections.singleton("PING")), equalTo(1));
    }

    /**
     * Start call in opened conversation
     *
     * @step. ^I call$
     */
    @When("^I call$")
    public void ICallUser() throws Throwable {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickCallButton();
    }

    /**
     * Verifies whether calling button is visible or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     *
     * @step. ^I can see calling button$
     * @throws java.lang.Exception
     */
    @Then("^I( do not)? see calling button$")
    public void ISeeCallButton(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue(webContext.getPagesCollection().getPage(
                    ConversationPage.class).isCallButtonVisible());
        } else {
            Assert.assertFalse(webContext.getPagesCollection().getPage(
                    ConversationPage.class).isCallButtonVisible());
        }
    }

    /**
     * Verify that conversation contains my missed call
     *
     * @step. ^I see conversation with my missed call$
     *
     * @throws Exception
     */
    @Then("^I see conversation with my missed call$")
    public void ThenISeeConversationWithMyMissedCall() throws Exception {
        Assert.assertEquals("YOU CALLED",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getMissedCallMessage());
    }

    /**
     * Click on picture to open it in full screen mode
     *
     * @step. ^I click on picture$
     *
     * @throws Exception
     */
    @When("^I click on picture$")
    public void WhenIClickOnPicture() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickOnPicture();
    }

    /**
     * Verifies whether picture is in fullscreen or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     *
     * @step. ^I( do not)? see picture in fullscreen$
     * @throws java.lang.Exception
     */
    @Then("^I( do not)? see picture (.*) in fullscreen$")
    public void ISeePictureInFullscreen(String doNot, String pictureName)
            throws Exception {
        ConversationPage conversationPage = webContext.getPagesCollection()
                .getPage(ConversationPage.class);
        if (doNot == null) {
            Assert.assertTrue(conversationPage.isPictureInModalDialog());
            Assert.assertTrue(conversationPage.isPictureInFullscreen());
            assertThat("Overlap score of image comparsion",
                    conversationPage
                    .getOverlapScoreOfFullscreenImage(pictureName),
                    greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            Assert.assertTrue(conversationPage.isPictureNotInModalDialog());
        }
    }

    /**
     * Click x button to close picture fullscreen mode
     *
     * @step. ^I click x button to close fullscreen mode$
     * @throws Exception
     */
    @When("^I click x button to close fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickXButton();
    }

    /**
     * I click on black border to close fullscreen mode
     *
     * @step. ^I click on black border to close fullscreen mode$
     * @throws Exception
     */
    @When("^I click on black border to close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class)
                .clickOnBlackBorder();
    }

    @When("^I click GIF button$")
    public void IClickGIFButton() throws Throwable {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickGIFButton();
    }

    @Then("^I see sent gif in the conversation view$")
    public void ISeeSentGifInTheConversationView() throws Throwable {
        webContext.getPagesCollection().getPage(ConversationPage.class).isImageMessageFound();
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message (.*) was cached$")
    public void IVerifyThatMessageWasCached(String message) throws Exception {
        assertThat("Cached message in input field", webContext.getPagesCollection()
                .getPage(ConversationPage.class).getMessageFromInputField(),
                equalTo(message));
    }

    /**
     * Verifies whether ping button tool tip is correct or not.
     *
     * @step. ^I see correct ping button tool tip$
     *
     */
    @Then("^I see correct ping button tooltip$")
    public void ISeeCorrectPingButtonTooltip() throws Exception {

        String tooltip = TOOLTIP_PING + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_PING_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_PING_MAC;
        }
        assertThat("Ping button tooltip",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getPingButtonToolTip(), equalTo(tooltip));
    }

    /**
     * Verifies whether call button tool tip is correct or not.
     *
     * @step. ^I see correct call button tool tip$
     *
     */
    @Then("^I see correct call button tooltip$")
    public void ISeeCorrectCallButtonTooltip() throws Exception {

        String tooltip = TOOLTIP_CALL + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_CALL_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_CALL_MAC;
        }
        assertThat("Call button tooltip",
                webContext.getPagesCollection().getPage(ConversationPage.class)
                .getCallButtonToolTip(), equalTo(tooltip));
    }

    @And("^I click on pending user avatar$")
    public void IClickOnPendingUserAvatar() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar();
    }

    /**
     * Click on an avatar bubble inside the conversation view
     *
     * @step. ^I click on avatar of user (.*) in conversation view$
     * @param userAlias name of the user
     * @throws Exception
     */
    @And("^I click on avatar of user (.*) in conversation view$")
    public void IClickOnUserAvatar(String userAlias) throws Exception {
        ClientUser user = webContext.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        webContext.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar(
                user.getId());
    }

    /**
     * Start a video call in opened conversation
     *
     * @step. ^I start a video call$
     */
    @When("^I start a video call$")
    public void IMakeVideoCallToUser() throws Throwable {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickVideoCallButton();
    }
    
    /**
     * Verifies that x messages are in the conversation
     *
     * @param x the amount of sent messages
     * @step. ^I see (\\d+) messages in conversation$
     */
    @Then("^I see (\\d+) messages? in conversation$")
    public void ISeeXMessagesInConversation(int x) throws Exception {
        assertThat("Number of messages in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfMessagesInCurrentConversation(), equalTo(x));
    }
    
    @When("^I click confirm to delete message for everyone$")
    public void IClickConfirmToDeleteForEveryone() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).confirmDeleteForEveryone();
    }

    @When("^I click confirm to delete message for me$")
    public void IClickConfirmToDelete() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).confirmDelete();
    }


    /**
     * Verify that the conversation list shows the info "Start a conversation or invite people to join" and the conversation
     * shows a watermark of the wire logo
     *
     * @param shouldNotBeVisible is set to null if "do not" part does not exist
     * @throws Exception
     * @step. ^I( do not)? see first time experience with watermark$
     */
    @And("^I( do not)? see first time experience with watermark$")
    public void ISeeWelcomePage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            assertThat("No watermark wire logo shown",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isWatermarkVisible());
            // TODO: Check for first time experience info visible
        } else {
            assertThat("Watermark wire logo shown",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isWatermarkNotVisible());
            // TODO: Check for first time experience info not visible
        }
    }
    
    @When("^I click on ephemeral button$")
    public void IClickEphemeralButton() throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).clickEphemeralButton();
    }

    @When("^I see (.*) with unit (.*) on ephemeral button$")
    public void ISeeTimeShortOnEphemeralTimer(String time, String unit) throws Exception {
        Assert.assertTrue("Time " + time + " on ephemeral button is not shown",
                webContext.getPagesCollection().getPage(ConversationPage.class).isTimeShortOnEphemeralButtonVisible(time));
        Assert.assertTrue("Time unit " + unit + " on ephemeral button is not shown",
                webContext.getPagesCollection().getPage(ConversationPage.class).isTimeUnitOnEphemeralButtonVisible(unit));
    }
    
    @When("^I see placeholder of conversation input is (.*)$")
    public void ISeePlaceholderOfInput(String label) throws Exception {
        Assert.assertThat(webContext.getPagesCollection().getPage(ConversationPage.class).getPlaceholderOfConversationInput(),
                equalTo(label));
    }
    
    @Then("^I see link (.*) in link preview message$")
    public void ISeeLinkInLinkPreview(String link) throws Exception {
        webContext.getPagesCollection().getPage(ConversationPage.class).waitForLinkPreviewContains(link);
    }
    
    @Then("^I (do not )?see a title (.*) in link preview in the conversation view$")
    public void ISeeLinkTitle(String doNot, String linkTitle) throws Exception {
        if (doNot == null) {
            assertThat("Could not find link title " + linkTitle,
                    webContext.getPagesCollection().getPage(ConversationPage.class).getLinkTitle(), containsString(linkTitle));
        } else {
            assertThat("link title " + linkTitle + "is shown",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isLinkTitleNotShownInConversationView());
        }
    }
    
    @Then("^I (do not )?see a picture (.*) from link preview$")
    public void ISeePictureInLinkPreview(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            assertThat("I do not see a picture from link preview in the conversation",
                    webContext.getPagesCollection().getPage(ConversationPage.class).isImageFromLinkPreviewVisible());

            BufferedImage expectedImage = ImageUtil.readImageFromFile(WebCommonUtils.getFullPicturePath(pictureName));
            BufferedImage actualImage = webContext.getPagesCollection().getPage(ConversationPage.class).
                    getImageFromLastLinkPreview();

            assertThat("Not enough good matches", ImageUtil.getMatches(expectedImage, actualImage), greaterThan(100));
        } else {
            assertThat("I see a picture in the conversation", webContext.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
    }
    
    @When("^I click context menu of the (second |third )?last message$")
    public void IClickContextMenuOfNthMessage(String indexNumber) throws Exception {
        int messageId = getXLastMessageIndex(indexNumber);
        webContext.getPagesCollection().getPage(ConversationPage.class).clickContextMenuOnMessage(messageId);
    }
    
    private int getXLastMessageIndex(String indexValue) throws Exception {
        int indexNummer = 1;
        if (indexValue == null)
            return indexNummer;
        switch (indexValue) {
            case "third ": indexNummer = 3;
                break;
            case "second ": indexNummer = 2;
                break;
            default: indexNummer = 1;
                break;
        }
        return indexNummer;
    }
    
}
