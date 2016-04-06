package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;

public class ConversationPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.70;

    private static final String TOOLTIP_PING = "Ping";
    private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + K)";
    private static final String SHORTCUT_PING_MAC = "(⌘⌥K)";
    private static final String TOOLTIP_CALL = "Call";
    private static final String SHORTCUT_CALL_WIN = "(Ctrl + Alt + R)";
    private static final String SHORTCUT_CALL_MAC = "(⌘⌥R)";

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());

    private String randomMessage;
    
    private final TestContext context;
    
    public ConversationPageSteps() {
        this.context = new TestContext();
    }

    public ConversationPageSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Sends random message (generated GUID) into opened conversation
     *
     * @throws Exception
     * @step. ^I write random message$
     */
    @When("^I write random message$")
    public void WhenIWriteRandomMessage() throws Exception {
        randomMessage = CommonUtils.generateGUID();
        IWriteMessage(randomMessage);
    }

    /**
     * Verify that the input text field contains random message
     */
    @Then("^I verify that random message was typed$")
    public void IVerifyThatRandomMessageWasTyped() throws Exception {
        assertThat("Random message in input field", context.getPagesCollection().getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(randomMessage));
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message \"(.*)\" was typed$")
    public void IVerifyThatMessageWasTyped(String message) throws Exception {
        assertThat("Message in input field", context.getPagesCollection().getPage(ConversationPage.class).getMessageFromInputField(),
                equalTo(message));
    }

    /**
     * Types text message to opened conversation, but does not send it
     *
     * @param message text message
     * @throws Exception
     * @step. ^I write message (.*)$
     */
    @When("^I write message (.*)$")
    public void IWriteMessage(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    @When("^I paste message from file (.*)$")
    public void IPasteMessageFromFile(String file) throws Exception {
        String s = WebCommonUtils.getTextFromFile(file);
        String message = "";
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c == '\n') {
                message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
            } else {
                message = message + c;
            }
        }
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    /**
     * Types x number of new lines to opened conversation, but does not send them
     *
     * @param amount number of lines to write
     * @throws Exception
     * @step. ^I write (.*) new lines$
     */
    @When("^I write (\\d+) new lines$")
    public void IWriteXNewLines(int amount) throws Exception {
        String message = "";
        for (int i = 0; i < amount; i++) {
            message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
        }
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    /**
     * Submits entered message for sending
     *
     * @step. ^I send message$
     */
    @When("^I send message$")
    public void WhenISendMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendNewMessage();
    }

    /**
     * Checks that last sent random message appear in conversation
     *
     * @throws Exception
     * @throws AssertionError if message did not appear in conversation
     * @step. ^I see random message in conversation$
     */
    @Then("^I see random message in conversation$")
    public void ThenISeeRandomMessageInConversation() throws Exception {
        ISeeTextMessage(randomMessage);
    }

    /**
     * Verifies whether YouTube video is visible
     *
     * @throws Exception
     * @step. ^I see embedded youtube video of (.*)
     */
    @Then("^I see embedded youtube video of (.*)")
    public void ThenISeeEmbeddedYoutubeVideoOf(String url) throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isYoutubeVideoEmbedded(url));
    }

    /**
     * Click People button in 1:1 conversation
     *
     * @throws Exception
     * @step. I click People button in one to one conversation$
     */
    @When("^I click People button in one to one conversation$")
    public void WhenIClickPeopleButtonIn1to1() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
    }

    /**
     * Click People button in a group conversation to close People Popover
     *
     * @throws Exception if the popover is not visible
     * @step. ^I close Group Participants popover$
     */
    @When("^I close Group Participants popover$")
    public void WhenICloseGroupParticipantsPopover() throws Exception {
        GroupPopoverContainer peoplePopoverPage = context.getPagesCollection().getPage(GroupPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
        }
    }

    /**
     * Click People button in 1:1 conversation to close People Popover
     *
     * @throws Exception if the popover is not visible
     * @step. ^I close Single User Profile popover$
     */
    @When("^I close Single User Profile popover$")
    public void WhenICloseSingleUserPopover() throws Exception {
        SingleUserPopoverContainer peoplePopoverPage = context.getPagesCollection().getPage(SingleUserPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
        }
    }

    /**
     * Click People button in a group conversation
     *
     * @throws Exception
     * @step. I click People button in group conversation$
     */
    @When("^I click People button in group conversation$")
    public void WhenIClickPeopleButtonInGroup() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
    }

    @When("^I see verified icon in conversation$")
    public void ISeeVerifiedIconInConversation() throws Throwable {
        assertThat("No verified icon", context.getPagesCollection().getPage(ConversationPage.class).isConversationVerified());
    }

    /**
     * Send a picture into current conversation
     *
     * @param pictureName the name of a picture file. This file should already exist in the ~/Documents folder
     * @throws Exception
     * @step. ^I send picture (.*) to the current conversation$
     */
    @When("^I send picture (.*) to the current conversation$")
    public void WhenISendPicture(String pictureName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendPicture(pictureName);
    }

    /**
     * Verifies whether previously sent picture exists in the conversation view
     *
     * @param pictureName the name of a picture file. This file should already exist in the ~/Documents folder
     * @throws Exception
     * @step. ^I see sent picture (.*) in the conversation view$
     */
    @Then("^I see sent picture (.*) in the conversation view$")
    public void ISeeSentPicture(String pictureName) throws Exception {
        assertThat("Overlap score of image comparsion", context.getPagesCollection().getPage(ConversationPage.class)
                .getOverlapScoreOfLastImage(pictureName), greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
    }

    /**
     * Verifies that only x images are in the conversation. Helps with checking for doubles.
     *
     * @param x the amount of images
     * @step. ^I see only (\\d+) picture[s]? in the conversation$
     */
    @Then("^I see only (\\d+) picture[s]? in the conversation$")
    public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
        assertThat("Number of images in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message  constant part of the system message
     * @throws Exception
     * @throws AssertionError if action message did not appear in conversation
     * @step. ^I see (.*) action in conversation$
     */
    @Then("^I( do not)? see (.*) action in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message) throws Exception {
        ThenISeeActionInConversation(doNot, message, 1);
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message  constant part of the system message
     * @param times  number of times the message appears
     * @throws Exception
     * @throws AssertionError if action message did not appear in conversation
     * @step. ^I see (.*) action in conversation$
     */
    @Then("^I( do not)? see (.*) action (\\d+) times in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message, int times) throws Exception {
        if (doNot == null) {
            assertThat(message + " action", context.getPagesCollection().getPage(ConversationPage.class)
                    .waitForNumberOfMessageHeadersContain(message), equalTo(times));
        } else {
            Assert.assertFalse("I see action containing " + message, context.getPagesCollection().getPage(ConversationPage.class)
                    .waitForMessageHeaderContains(message));
        }
    }

    /**
     * Verifies whether people button tool tip is correct or not.
     *
     * @step. ^I see correct people button tool tip$
     */
    @Then("^I see correct people button tool tip$")
    public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
        Assert.assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isPeopleButtonToolTipCorrect());
    }

    @Then("^I see connecting message for (.*) in conversation$")
    public void ISeeConnectingMessage(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        assertThat("User name", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageUser(),
                equalTo(contact));
        assertThat("Label", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageLabel(),
                equalTo("CONNECTING"));
    }

    @Then("^I see connected message for (.*) in conversation$")
    public void ISeeConnectedMessage(String contact) throws Exception {
        contact = context.getUserManager().replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
        assertThat("User name", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageUser(),
                equalTo(contact));
        assertThat("Label", context.getPagesCollection().getPage(ConversationPage.class).getConnectedMessageLabel(),
                equalTo("CONNECTED"));
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message  constant part of the system message
     * @param contacts list of comma separated contact names/aliases
     * @throws AssertionError if action message did not appear in conversation
     * @throws Exception
     * @step. ^I see (.*) action for (.*) in conversation$
     */
    @Then("^I( do not)? see (.*) action for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot, String message, String contacts) throws Exception {
        ThenISeeActionForContactInConversation(doNot, message, 1, contacts);
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message  constant part of the system message
     * @param times  number of times the message appears
     * @param contacts list of comma separated contact names/aliases
     * @throws AssertionError if action message did not appear in conversation
     * @throws Exception
     * @step. ^I see (.*) action for (.*) in conversation$
     */
    @Then("^I( do not)? see (.*) action (\\d+) times for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot, String message, int times, String contacts) throws Exception {
        contacts = context.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(CommonSteps.splitAliases(contacts));
        if (doNot == null) {
            if(times > 1) {
                assertThat(message + " action for " + contacts, context.getPagesCollection().getPage(ConversationPage.class)
                        .waitForNumberOfMessageHeadersContain(parts), equalTo(times));
            } else {
                context.getPagesCollection().getPage(ConversationPage.class).waitForMessageHeaderContains(parts);
            }
        } else {
            assertThat(message + " action for " + contacts, context.getPagesCollection().getPage(ConversationPage.class)
                    .waitForNumberOfMessageHeadersContain(parts), equalTo(0));
        }
    }

    /**
     * Add a user to group chat
     *
     * @param contact
     * @throws Exception
     * @step. ^I add (.*) to group chat$
     */
    @When("^I add (.*) to group chat$")
    public void IAddContactToGroupChat(String contact) throws Exception {
        WhenIClickPeopleButtonInGroup();
        GroupPopoverPageSteps cpSteps = new GroupPopoverPageSteps(context);
        cpSteps.IClickAddPeopleButton();
        cpSteps.ISearchForUser(contact);
        cpSteps.ISelectUserFromSearchResults(contact);
        cpSteps.IChooseToCreateGroupConversation();
    }

    /**
     * Click ping button to send ping and hot ping
     *
     * @throws Exception
     * @step. ^I click ping button$
     */
    @When("^I click ping button$")
    public void IClickPingButton() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPingButton();
    }

    /**
     * Verify a text message is visible in conversation.
     *
     * @param message
     * @throws Exception
     * @step. ^I see text message (.*)
     */
    @Then("^I see text message (.*)")
    public void ISeeTextMessage(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForMessageContains(message);
    }

    private static String expandPattern(final String originalStr) {
        final String lineBreak = "LF";
        final Pattern p = Pattern.compile("\\(\\s*'(\\w+)'\\s*\\*\\s*([0-9]+)\\s*\\)");
        final Matcher m = p.matcher(originalStr);
        final StringBuilder result = new StringBuilder();
        int lastPosInOriginalString = 0;
        while (m.find()) {
            if (m.start() > lastPosInOriginalString) {
                result.append(originalStr.substring(lastPosInOriginalString, m.start()));
            }
            final String toAdd = m.group(1).replace(lineBreak, "\n");
            final int times = Integer.parseInt(m.group(2));
            for (int i = 0; i < times; i++) {
                result.append(toAdd);
            }
            lastPosInOriginalString = m.end();
        }
        if (lastPosInOriginalString < originalStr.length()) {
            result.append(originalStr.substring(lastPosInOriginalString, originalStr.length()));
        }
        return result.toString();
    }

    /**
     * Verify the text of the last text message in conversation. Patterns are allowed, for example ('a' * 100) will print the a
     * character 100 times. Line break is equal to LF char sequence.
     *
     * @param expectedMessage the expected message
     * @throws Exception
     * @step. ^I verify the last text message equals to (.*)
     */
    @Then("^I verify the last text message equals to (.*)")
    public void IVerifyLastTextMessage(String expectedMessage) throws Exception {
        Assert.assertEquals(expandPattern(expectedMessage), context.getPagesCollection().getPage(ConversationPage.class)
                .getLastTextMessage());
    }

    @Then("^I verify the last text message equals file (.*)")
    public void IVerifyLastTextMessageEqualsFile(String file) throws Exception {
        String expectedMessage = WebCommonUtils.getTextFromFile(file);
        Assert.assertEquals(expandPattern(expectedMessage), context.getPagesCollection().getPage(ConversationPage.class)
                .getLastTextMessage());
    }

    /**
     * Verify the text of the second last text message in conversation. This step should only be used after verifying the last
     * message of the conversation, because otherwise you might run into a race condition.
     *
     * @param expectedMessage the expected message
     * @throws Exception
     * @step. ^I verify the second last text message equals to (.*)
     */
    @Then("^I verify the second last text message equals to (.*)")
    public void IVerifySecondLastTextMessage(String expectedMessage) throws Exception {
        assertThat(context.getPagesCollection().getPage(ConversationPage.class).getSecondLastTextMessage(), equalTo(expectedMessage));
    }

    /**
     * Verify a text message is not visible in conversation
     *
     * @param message
     * @throws Exception
     * @step. ^I do not see text message (.*)
     */
    @Then("^I do not see text message ?(.*)$")
    public void IDontSeeTextMessage(String message) throws Exception {
        Assert.assertFalse("Saw text message " + message, context.getPagesCollection().getPage(ConversationPage.class)
                .isTextMessageVisible(message == null ? "" : message));
    }

    /**
     * Start call in opened conversation
     *
     * @step. ^I call$
     */
    @When("^I call$")
    public void ICallUser() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).clickCallButton();
    }

    /**
     * Verifies whether calling button is visible or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws java.lang.Exception
     * @step. ^I can see calling button$
     */
    @Then("^I( do not)? see calling button$")
    public void ISeeCallButton(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        } else {
            Assert.assertFalse(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        }
    }

    /**
     * Accepts incoming call by clicking the check button on the calling bar
     *
     * @throws Exception
     * @step. ^I accept the incoming call$
     */
    @When("^I accept the incoming call$")
    public void IAcceptIncomingCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickAcceptCallButton();
    }

    /**
     * Accepts incoming video call by clicking the video call button on the calling bar
     *
     * @throws Exception
     * @step. ^I accept the incoming video call$
     */
    @When("^I accept the incoming video call$")
    public void IAcceptIncomingVideoCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickAcceptVideoCallButton();
    }

    /**
     * Silences the incoming call by clicking the corresponding button on the calling bar
     *
     * @throws Exception
     * @step. ^I silence the incoming call$
     */
    @When("^I silence the incoming call$")
    public void ISilenceIncomingCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickSilenceCallButton();
    }

    /**
     * Verify that conversation contains my missed call
     *
     * @throws Exception
     * @step. ^I see conversation with my missed call$
     */
    @Then("^I see conversation with my missed call$")
    public void ThenISeeConversationWithMyMissedCall() throws Exception {
        Assert.assertEquals("YOU CALLED", context.getPagesCollection().getPage(ConversationPage.class).getMissedCallMessage());
    }

    /**
     * Click on picture to open it in full screen mode
     *
     * @throws Exception
     * @step. ^I click on picture$
     */
    @When("^I click on picture$")
    public void WhenIClickOnPicture() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickOnPicture();
    }

    /**
     * Verifies whether picture is in fullscreen or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws java.lang.Exception
     * @step. ^I( do not)? see picture in fullscreen$
     */
    @Then("^I( do not)? see picture (.*) in fullscreen$")
    public void ISeePictureInFullscreen(String doNot, String pictureName) throws Exception {
        ConversationPage conversationPage = context.getPagesCollection().getPage(ConversationPage.class);
        if (doNot == null) {
            Assert.assertTrue(conversationPage.isPictureInModalDialog());
            Assert.assertTrue(conversationPage.isPictureInFullscreen());
            assertThat("Overlap score of image comparsion", conversationPage.getOverlapScoreOfFullscreenImage(pictureName),
                    greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            Assert.assertTrue(conversationPage.isPictureNotInModalDialog());
        }
    }

    /**
     * Click x button to close picture fullscreen mode
     *
     * @throws Exception
     * @step. ^I click x button to close fullscreen mode$
     */
    @When("^I click x button to close fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButton();
    }

    /**
     * I click on black border to close fullscreen mode
     *
     * @throws Exception
     * @step. ^I click on black border to close fullscreen mode$
     */
    @When("^I click on black border to close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickOnBlackBorder();
    }

    @When("^I click GIF button$")
    public void IClickGIFButton() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).clickGIFButton();
    }

    @Then("^I see sent gif in the conversation view$")
    public void ISeeSentGifInTheConversationView() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).isImageMessageFound();
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message (.*) was cached$")
    public void IVerifyThatMessageWasCached(String message) throws Exception {
        assertThat("Cached message in input field", context.getPagesCollection().getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(message));
    }

    /**
     * Types shortcut combination to open search
     *
     * @throws Exception
     * @step. ^I type shortcut combination to open search$
     */
    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressShortCutForSearch();
    }

    /**
     * Hovers ping button
     *
     * @throws Exception
     * @step. ^I hover ping button$
     */
    @Then("^I hover ping button$")
    public void IHoverPingButton() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).hoverPingButton();
    }

    /**
     * Types shortcut combination to ping
     *
     * @throws Exception
     * @step. ^I type shortcut combination to ping$
     */
    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPing();
    }

    /**
     * Verifies whether ping button tool tip is correct or not.
     *
     * @step. ^I see correct ping button tool tip$
     */
    @Then("^I see correct ping button tooltip$")
    public void ISeeCorrectPingButtonTooltip() throws Exception {

        String tooltip = TOOLTIP_PING + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_PING_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_PING_MAC;
        }
        assertThat("Ping button tooltip", context.getPagesCollection().getPage(ConversationPage.class).getPingButtonToolTip(),
                equalTo(tooltip));
    }

    /**
     * Hovers call button
     *
     * @step. ^I hover call button$
     */
    @When("^I hover call button$")
    public void IHoverCallButton() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).hoverCallButton();
    }

    /**
     * Verifies whether call button tool tip is correct or not.
     *
     * @step. ^I see correct call button tool tip$
     */
    @Then("^I see correct call button tooltip$")
    public void ISeeCorrectCallButtonTooltip() throws Exception {

        String tooltip = TOOLTIP_CALL + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_CALL_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_CALL_MAC;
        }
        assertThat("Call button tooltip", context.getPagesCollection().getPage(ConversationPage.class).getCallButtonToolTip(),
                equalTo(tooltip));
    }

    /**
     * Types shortcut combination to call
     *
     * @throws Exception
     * @step. ^I type shortcut combination to ping$
     */
    @Then("^I type shortcut combination to start a call$")
    public void ITypeShortcutCombinationToCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressShortCutForCall();
    }

    @And("^I click on pending user avatar$")
    public void IClickOnPendingUserAvatar() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar();
    }

    /**
     * Click on an avatar bubble inside the conversation view
     *
     * @param userAlias name of the user
     * @throws Exception
     * @step. ^I click on avatar of user (.*) in conversation view$
     */
    @And("^I click on avatar of user (.*) in conversation view$")
    public void IClickOnUserAvatar(String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar(user.getId());
    }

    /**
     * Start a video call in opened conversation
     *
     * @step. ^I start a video call$
     */
    @When("^I start a video call$")
    public void IMakeVideoCallToUser() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).clickVideoCallButton();
    }

}
