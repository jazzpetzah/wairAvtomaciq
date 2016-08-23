package com.wearezeta.auto.win.steps.webapp;

import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.win.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;
import org.openqa.selenium.Keys;

public class ConversationPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.85;

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    private static final String TOOLTIP_PING = "Ping";
    private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + G)";
    private static final String SHORTCUT_PING_MAC = "(ââ¥G)";
    private static final String TOOLTIP_CALL = "Call";
    private static final String SHORTCUT_CALL_WIN = "(Ctrl + Alt + T)";
    private static final String SHORTCUT_CALL_MAC = "(ââ¥T)";

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger
            .getLog(ConversationPageSteps.class.getName());

    private String randomMessage;

    /**
     * Sends random message (generated GUID) into opened conversation
     *
     * @step. ^I write random message$
     * @throws Exception
     */
    @When("^I write random message$")
    public void WhenIWriteRandomMessage() throws Exception {
        randomMessage = UUID.randomUUID().toString();
        IWriteMessage(randomMessage);
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
        webappPagesCollection.getPage(ConversationPage.class)
                .writeNewMessage(message);
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
            message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
        }
        webappPagesCollection.getPage(ConversationPage.class)
                .writeNewMessage(message);
    }

    /**
     * Submits entered message for sending
     *
     * @step. ^I send message$
     */
    @When("^I send message$")
    public void WhenISendMessage() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .sendNewMessage();
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
        Assert.assertTrue(webappPagesCollection
                .getPage(ConversationPage.class).isMessageSent(randomMessage));
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
        Assert.assertTrue(webappPagesCollection
                .getPage(ConversationPage.class).isMessageEmbedded(true, "youtube", url));
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
        webappPagesCollection.getPage(ConversationPage.class)
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
        webappPagesCollection.getPage(ConversationPage.class)
                .sendPicture(pictureName);
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
        assertThat("Overlap score of image comparsion", webappPagesCollection.getPage(ConversationPage.class)
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
        assertThat(
                "Number of images in the conversation",
                webappPagesCollection
                .getPage(ConversationPage.class)
                .getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @param message constant part of the system message
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
     * @param message constant part of the system message
     * @param times number of times the message appears
     * @throws Exception
     * @throws AssertionError if action message did not appear in conversation
     * @step. ^I see (.*) action in conversation$
     */
    @Then("^I( do not)? see (.*) action (\\d+) times in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message, int times) throws Exception {
        if (doNot == null) {
            assertThat(message + " action", webappPagesCollection.getPage(ConversationPage.class)
                    .waitForNumberOfMessageHeadersContain(message), equalTo(times));
        } else {
            Assert.assertTrue(webappPagesCollection.getPage(ConversationPage.class).
                    isActionMessageNotSent(message));
        }
    }

    /**
     * Verifies whether people button tool tip is correct or not.
     *
     * @step. ^I see correct people button tool tip$
     *
     */
    @Then("^I see correct people button tool tip$")
    public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
        Assert.assertTrue(webappPagesCollection
                .getPage(ConversationPage.class).isPeopleButtonToolTipCorrect());
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @step. ^I see (.*) action for (.*) in conversation$
     *
     * @throws AssertionError if action message did not appear in conversation
     *
     * @param message constant part of the system message
     * @param contacts list of comma separated contact names/aliases
     * @throws Exception
     *
     */
    @Then("^I( do not)? see (.*) action for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot,
            String message, String contacts) throws Exception {
        contacts = usrMgr.replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(CommonSteps.splitAliases(contacts));
        if (doNot == null) {
            assertThat("Check action", webappPagesCollection
                    .getPage(ConversationPage.class).getLastActionMessage(),
                    containsString(message));
        } else {
            assertThat("Check action", webappPagesCollection
                    .getPage(ConversationPage.class).getLastActionMessage(),
                    not(containsString(message)));
        }
    }

    /**
     * Checks action message (e.g. you left, etc.) appear in conversation
     *
     * @step. ^I see (.*) user (.*) action for (.*) in conversation
     *
     * @throws AssertionError if action message did not appear in conversation
     *
     * @param message message string
     *
     * @param user1 user who did action string
     *
     * @param contacts user(s) who was actioned string
     *
     * @throws Exception
     *
     */
    @Then("^I see user (.*) action (.*) for (.*) in conversation$")
    public void ThenISeeUserActionForContactInConversation(String user1,
            String message, String contacts) throws Exception {
        user1 = usrMgr.replaceAliasesOccurences(user1, FindBy.NAME_ALIAS);
        contacts = usrMgr.replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        if (contacts.contains(usrMgr.getSelfUserOrThrowError().getName())) {
            contacts = contacts.replace(usrMgr.getSelfUserOrThrowError().getName(), "you");
        }
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.add(user1);
        parts.addAll(CommonSteps.splitAliases(contacts));
        Assert.assertTrue(webappPagesCollection
                .getPage(ConversationPage.class).isActionMessageSent(parts));
    }

    /**
     * Click ping button to send ping and hot ping
     *
     * @step. ^I click ping button$
     * @throws Exception
     */
    @When("^I click ping button$")
    public void IClickPingButton() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .clickPingButton();
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
        Assert.assertTrue(webappPagesCollection
                .getPage(ConversationPage.class).isTextMessageVisible(message));
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
        Assert.assertEquals(
                expandPattern(expectedMessage),
                webappPagesCollection
                .getPage(ConversationPage.class).getLastTextMessage());
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
        assertThat(
                webappPagesCollection
                .getPage(ConversationPage.class)
                .getSecondLastTextMessage(), equalTo(expectedMessage));
    }

    /**
     * Verify a text message is not visible in conversation
     *
     * @step. ^I do not see text message (.*)
     * @param message
     * @throws Exception
     */
    @Then("^I do not see text message ?(.*)$")
    public void IDontSeeTextMessage(String message) throws Exception {
        Assert.assertFalse("Saw text message " + message, webappPagesCollection.getPage(ConversationPage.class)
                .isTextMessageVisible(message == null ? "" : message));
    }

    /**
     * Verify that there is only one ping message visible in conversation
     *
     * @step. ^I see only one ping message$
     * @throws Exception
     */
    @When("^I see only one ping message$")
    public void ISeeOnlyOnePingMessage() throws Exception {
        assertThat("PING action", webappPagesCollection.getPage(com.wearezeta.auto.web.pages.ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(Collections.singleton("PING")), equalTo(1));
    }

    /**
     * Start call in opened conversation
     *
     * @step. ^I call$
     */
    @When("^I call$")
    public void ICallUser() throws Throwable {
        webappPagesCollection.getPage(ConversationPage.class)
                .clickCallButton();
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
            Assert.assertTrue(webappPagesCollection
                    .getPage(ConversationPage.class).isCallButtonVisible());
        } else {
            Assert.assertFalse(webappPagesCollection
                    .getPage(ConversationPage.class).isCallButtonVisible());
        }
    }

    /**
     * Verify that conversation contains missed call from contact
     *
     * @step. ^I see conversation with missed call from (.*)$
     *
     * @param contact contact name string
     *
     * @throws Exception
     */
    @Then("^I see conversation with missed call from (.*)$")
    public void ThenISeeConversationWithMissedCallFrom(String contact)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName()
                .toUpperCase();
        Assert.assertEquals(contact + " CALLED", webappPagesCollection.getPage(ConversationPage.class)
                .getMissedCallMessage());
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
        Assert.assertEquals("YOU CALLED", webappPagesCollection
                .getPage(ConversationPage.class).getMissedCallMessage());
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
        webappPagesCollection.getPage(ConversationPage.class)
                .clickOnPicture();
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
        if (doNot == null) {
            Assert.assertTrue(webappPagesCollection
                    .getPage(ConversationPage.class).isPictureInModalDialog());
            Assert.assertTrue(webappPagesCollection
                    .getPage(ConversationPage.class).isPictureInFullscreen());
            assertThat(
                    "Overlap score of image comparsion",
                    webappPagesCollection
                    .getPage(ConversationPage.class)
                    .getOverlapScoreOfFullscreenImage(pictureName),
                    org.hamcrest.Matchers
                    .greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            Assert.assertTrue(webappPagesCollection
                    .getPage(ConversationPage.class)
                    .isPictureNotInModalDialog());
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
        webappPagesCollection.getPage(ConversationPage.class)
                .clickXButton();
    }

    /**
     * I click on black border to close fullscreen mode
     *
     * @step. ^I click on black border to close fullscreen mode$
     * @throws Exception
     */
    @When("^I click on black border to close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .clickOnBlackBorder();
    }

    @When("^I click GIF button$")
    public void IClickGIFButton() throws Throwable {
        webappPagesCollection.getPage(ConversationPage.class)
                .clickGIFButton();
    }

    @Then("^I see sent gif in the conversation view$")
    public void ISeeSentGifInTheConversationView() throws Throwable {
        webappPagesCollection.getPage(ConversationPage.class).isImageMessageFound();
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message (.*) was cached$")
    public void IVerifyThatMessageWasCached(String message) throws Exception {
        assertThat("Cached message in input field", webappPagesCollection.getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(message));
    }

    /**
     * Verify that the input text field contains random message
     *
     */
    @Then("^I verify that random message was typed$")
    public void IVerifyThatRandomMessageWasTyped() throws Exception {
        assertThat("Random message in input field", webappPagesCollection.getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(randomMessage));
    }

    /**
     * Verify that the input text field contains message X
     *
     * @param message the message it should contain
     */
    @Then("^I verify that message \"(.*)\" was typed$")
    public void IVerifyThatMessageWasTyped(String message) throws Exception {
        assertThat("Message in input field", webappPagesCollection.getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(message));
    }

    /**
     * Types shortcut combination to ping
     *
     * @step. ^I type shortcut combination to ping$
     * @throws Exception
     */
    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForPing();
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
        assertThat("Ping button tooltip", webappPagesCollection
                .getPage(ConversationPage.class).getPingButtonToolTip(),
                equalTo(tooltip));
    }

    /**
     * Types shortcut combination to undo
     *
     * @step. ^I type shortcut combination to undo$
     * @throws Exception
     */
    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForUndo();
    }

    /**
     * Types shortcut combination to redo
     *
     * @step. ^I type shortcut combination to redo$
     * @throws Exception
     */
    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForRedo();
    }

    /**
     * Types shortcut combination to select all
     *
     * @step. ^I type shortcut combination to select all$
     * @throws Exception
     */
    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForSelectAll();
    }

    /**
     * Types shortcut combination to cut
     *
     * @step. ^I type shortcut combination to cut$
     * @throws Exception
     */
    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForCut();
    }

    /**
     * Types shortcut combination to paste
     *
     * @step. ^I type shortcut combination to paste$
     * @throws Exception
     */
    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForPaste();
    }

    /**
     * Types shortcut combination to copy
     *
     * @step. ^I type shortcut combination to copy$
     * @throws Exception
     */
    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForCopy();
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
        assertThat("Call button tooltip", webappPagesCollection
                .getPage(ConversationPage.class).getCallButtonToolTip(),
                equalTo(tooltip));
    }

    /**
     * Types shortcut combination to call
     *
     * @step. ^I type shortcut combination to ping$
     * @throws Exception
     */
    @Then("^I type shortcut combination to start a call$")
    public void ITypeShortcutCombinationToCall() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .pressShortCutForCall();
    }

    @And("^I click on pending user avatar$")
    public void IClickOnPendingUserAvatar() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class)
                .clickUserAvatar();
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
        ClientUser user = usrMgr.findUserBy(userAlias, FindBy.NAME_ALIAS);
        webappPagesCollection.getPage(ConversationPage.class)
                .clickUserAvatar(user.getId());
    }

    /**
     * Start a video call in opened conversation
     *
     * @step. ^I start a video call$
     */
    @When("^I start a video call$")
    public void IMakeVideoCallToUser() throws Throwable {
        webappPagesCollection.getPage(ConversationPage.class).clickVideoCallButton();
    }
    
    /**
     * Verifies that x messages are in the conversation
     *
     * @param x the amount of sent messages
     * @step. ^I see (\\d+) messages in conversation$
     */
    @Then("^I see (\\d+) messages? in conversation$")
    public void ISeeXMessagesInConversation(int x) throws Exception {
        assertThat("Number of messages in the conversation", webappPagesCollection.getPage(ConversationPage.class)
                .getNumberOfMessagesInCurrentConversation(), equalTo(x));
    }
    
    @When("^I click confirm to delete message for everyone$")
    public void IClickConfirmToDeleteForEveryone() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class).confirmDeleteForEveryone();
    }

    @When("^I click confirm to delete message for me$")
    public void IClickConfirmToDelete() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class).confirmDelete();
    }

}
