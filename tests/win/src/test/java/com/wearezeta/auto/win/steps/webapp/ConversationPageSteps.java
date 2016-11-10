package com.wearezeta.auto.win.steps.webapp;

import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.win.pages.webapp.ConversationPage;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConversationPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(ConversationPageSteps.class.getName());

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.85;
    private static final String TOOLTIP_PING = "Ping";
    private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + G)";
    private static final String SHORTCUT_PING_MAC = "(ââ¥G)";
    private static final String TOOLTIP_CALL = "Call";
    private static final String SHORTCUT_CALL_WIN = "(Ctrl + Alt + T)";
    private static final String SHORTCUT_CALL_MAC = "(ââ¥T)";

    private String randomMessage;
    private final WrapperTestContext context;

    public ConversationPageSteps() {
        this.context = new WrapperTestContext();
    }

    public ConversationPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I write random message$")
    public void WhenIWriteRandomMessage() throws Exception {
        randomMessage = UUID.randomUUID().toString();
        IWriteMessage(randomMessage);
    }

    @When("^I write message (.*)$")
    public void IWriteMessage(String message) throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    @When("^I write (\\d+) new lines$")
    public void IWriteXNewLines(int amount) throws Exception {
        String message = "";
        for (int i = 0; i < amount; i++) {
            message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
        }
        context.getWebappPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    @When("^I delete (\\d+) characters from the conversation input$")
    public void IDeleteTypedMessage(int count) throws Exception {
        int i = count;
        while (i != 0) {
            context.getWebappPagesCollection().getPage(ConversationPage.class).clearConversationInput();
            i--;
        }
    }

    @When("^I send message$")
    public void WhenISendMessage() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).sendNewMessage();
    }

    @Then("^I see random message in conversation$")
    public void ThenISeeRandomMessageInConversation() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isMessageSent(randomMessage));
    }

    @Then("^I see embedded youtube video of (.*)")
    public void ThenISeeEmbeddedYoutubeVideoOf(String url) throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isMessageEmbedded(true, "youtube",
                url));
    }

    @When("^I click People button in group conversation$")
    public void WhenIClickPeopleButtonInGroup() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
    }

    @When("^I send picture (.*) to the current conversation$")
    public void WhenISendPicture(String pictureName) throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).sendPicture(pictureName);
    }

    @Then("^I see sent picture (.*) in the conversation view$")
    public void ISeeSentPicture(String pictureName) throws Exception {
        assertThat("Overlap score of image comparsion", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getOverlapScoreOfLastImage(pictureName), greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
    }

    @Then("^I see only (\\d+) picture[s]? in the conversation$")
    public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
        assertThat("Number of images in the conversation", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    @Then("^I( do not)? see (.*) action in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message) throws Exception {
        ThenISeeActionInConversation(doNot, message, 1);
    }

    @Then("^I( do not)? see (.*) action (\\d+) times in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message, int times) throws Exception {
        if (doNot == null) {
            assertThat(message + " action", context.getWebappPagesCollection().getPage(ConversationPage.class).
                    waitForNumberOfMessageHeadersContain(message), equalTo(times));
        } else {
            Assert.
                    assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).
                            isActionMessageNotSent(message));
        }
    }

    @Then("^I see correct people button tool tip$")
    public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isPeopleButtonToolTipCorrect());
    }

    @Then("^I( do not)? see (.*) action for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot, String message, String contacts) throws Exception {
        contacts = context.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(CommonSteps.splitAliases(contacts));
        if (doNot == null) {
            assertThat("Check action", context.getWebappPagesCollection().getPage(ConversationPage.class).getLastActionMessage(),
                    containsString(message));
        } else {
            assertThat("Check action", context.getWebappPagesCollection().getPage(ConversationPage.class).getLastActionMessage(),
                    not(containsString(message)));
        }
    }

    @Then("^I see user (.*) action (.*) for (.*) in conversation$")
    public void ThenISeeUserActionForContactInConversation(String user1, String message, String contacts) throws Exception {
        user1 = context.getUserManager().replaceAliasesOccurences(user1, FindBy.NAME_ALIAS);
        contacts = context.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        if (contacts.contains(context.getUserManager().getSelfUserOrThrowError().getName())) {
            contacts = contacts.replace(context.getUserManager().getSelfUserOrThrowError().getName(), "you");
        }
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.add(user1);
        parts.addAll(CommonSteps.splitAliases(contacts));
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isActionMessageSent(parts));
    }

    @When("^I click ping button$")
    public void IClickPingButton() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickPingButton();
    }

    @Then("^I see text message (.*)")
    public void ISeeTextMessage(String message) throws Exception {
        Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isTextMessageVisible(message));
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

    @Then("^I verify the last text message equals to (.*)")
    public void IVerifyLastTextMessage(String expectedMessage) throws Exception {
        Assert.assertEquals(expandPattern(expectedMessage), context.getWebappPagesCollection().getPage(ConversationPage.class).
                getLastTextMessage());
    }

    @Then("^I verify the second last text message equals to (.*)")
    public void IVerifySecondLastTextMessage(String expectedMessage) throws Exception {
        assertThat(context.getWebappPagesCollection().getPage(ConversationPage.class).getSecondLastTextMessage(), equalTo(
                expectedMessage));
    }

    @Then("^I do not see text message ?(.*)$")
    public void IDontSeeTextMessage(String message) throws Exception {
        Assert.assertTrue("Saw text message " + message, context.getWebappPagesCollection().getPage(ConversationPage.class).
                isTextMessageInvisible(message == null ? "" : message));
    }

    @When("^I see only one ping message$")
    public void ISeeOnlyOnePingMessage() throws Exception {
        assertThat("PING action", context.getWebappPagesCollection().
                getPage(com.wearezeta.auto.web.pages.ConversationPage.class).waitForNumberOfMessageHeadersContain(Collections.
                singleton("PING")), equalTo(1));
    }

    @When("^I call$")
    public void ICallUser() throws Throwable {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickCallButton();
    }

    @Then("^I( do not)? see calling button$")
    public void ISeeCallButton(String doNot) throws Exception {
        if (doNot == null) {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        } else {
            Assert.assertFalse(context.getWebappPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        }
    }

    @Then("^I see conversation with missed call from (.*)$")
    public void ThenISeeConversationWithMissedCallFrom(String contact) throws Exception {
        contact = context.getUserManager().findUserByNameOrNameAlias(contact).getName().toUpperCase();
        Assert.assertEquals(contact + " CALLED", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getMissedCallMessage());
    }

    @Then("^I see conversation with my missed call$")
    public void ThenISeeConversationWithMyMissedCall() throws Exception {
        Assert.assertEquals("YOU CALLED", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getMissedCallMessage());
    }

    @When("^I click on picture$")
    public void WhenIClickOnPicture() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickOnPicture();
    }

    @Then("^I( do not)? see picture (.*) in fullscreen$")
    public void ISeePictureInFullscreen(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isPictureInModalDialog());
            Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isPictureInFullscreen());
            assertThat("Overlap score of image comparsion", context.getWebappPagesCollection().getPage(ConversationPage.class).
                    getOverlapScoreOfFullscreenImage(pictureName), org.hamcrest.Matchers.greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            Assert.assertTrue(context.getWebappPagesCollection().getPage(ConversationPage.class).isPictureNotInModalDialog());
        }
    }

    @When("^I click x button to close fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickXButton();
    }

    @When("^I click on black border to close fullscreen mode$")
    public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickOnBlackBorder();
    }

    @When("^I click GIF button$")
    public void IClickGIFButton() throws Throwable {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickGIFButton();
    }

    @Then("^I see sent gif in the conversation view$")
    public void ISeeSentGifInTheConversationView() throws Throwable {
        context.getWebappPagesCollection().getPage(ConversationPage.class).isImageMessageFound();
    }

    @Then("^I verify that message (.*) was cached$")
    public void IVerifyThatMessageWasCached(String message) throws Exception {
        assertThat("Cached message in input field", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getMessageFromInputField(), equalTo(message));
    }

    @Then("^I verify that random message was typed$")
    public void IVerifyThatRandomMessageWasTyped() throws Exception {
        assertThat("Random message in input field", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getMessageFromInputField(), equalTo(randomMessage));
    }

    @Then("^I verify that message \"(.*)\" was typed$")
    public void IVerifyThatMessageWasTyped(String message) throws Exception {
        assertThat("Message in input field", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getMessageFromInputField(), equalTo(message));
    }

    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForPing();
    }

    @Then("^I see correct ping button tooltip$")
    public void ISeeCorrectPingButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_PING + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_PING_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_PING_MAC;
        }
        assertThat("Ping button tooltip", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getPingButtonToolTip(), equalTo(tooltip));
    }

    @Then("^I type shortcut combination to undo$")
    public void ITypeShortcutCombinationToUndo() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForUndo();
    }

    @Then("^I type shortcut combination to redo$")
    public void ITypeShortcutCombinationToRedo() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForRedo();
    }

    @Then("^I type shortcut combination to select all$")
    public void ITypeShortcutCombinationToSelectAll() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForSelectAll();
    }

    @Then("^I type shortcut combination to cut$")
    public void ITypeShortcutCombinationToCut() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForCut();
    }

    @Then("^I type shortcut combination to paste$")
    public void ITypeShortcutCombinationToPaste() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForPaste();
    }

    @Then("^I type shortcut combination to copy$")
    public void ITypeShortcutCombinationToCopy() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForCopy();
    }

    @Then("^I see correct call button tooltip$")
    public void ISeeCorrectCallButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_CALL + " ";
        if (WebAppExecutionContext.isCurrentPlatformWindows()) {
            tooltip = tooltip + SHORTCUT_CALL_WIN;
        } else {
            tooltip = tooltip + SHORTCUT_CALL_MAC;
        }
        assertThat("Call button tooltip", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getCallButtonToolTip(), equalTo(tooltip));
    }

    @Then("^I type shortcut combination to start a call$")
    public void ITypeShortcutCombinationToCall() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).pressShortCutForCall();
    }

    @And("^I click on pending user avatar$")
    public void IClickOnPendingUserAvatar() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickUserAvatar();
    }

    @And("^I click on avatar of user (.*) in conversation view$")
    public void IClickOnUserAvatar(String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickUserAvatar(user.getId());
    }

    @When("^I start a video call$")
    public void IMakeVideoCallToUser() throws Throwable {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickVideoCallButton();
    }

    @Then("^I see (\\d+) messages? in conversation$")
    public void ISeeXMessagesInConversation(int x) throws Exception {
        assertThat("Number of messages in the conversation", context.getWebappPagesCollection().getPage(ConversationPage.class).
                getNumberOfMessagesInCurrentConversation(), equalTo(x));
    }

    @When("^I click confirm to delete message for everyone$")
    public void IClickConfirmToDeleteForEveryone() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).confirmDeleteForEveryone();
    }

    @When("^I click confirm to delete message for me$")
    public void IClickConfirmToDelete() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).confirmDelete();
    }

    @And("^I( do not)? see first time experience with watermark$")
    public void ISeeWelcomePage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            assertThat("No watermark wire logo shown", context.getWebappPagesCollection().getPage(ConversationPage.class).
                    isWatermarkVisible());
            // TODO: Check for first time experience info visible
        } else {
            assertThat("Watermark wire logo shown", context.getWebappPagesCollection().getPage(ConversationPage.class).
                    isWatermarkNotVisible());
            // TODO: Check for first time experience info not visible
        }
    }

    @When("^I click on ephemeral button$")
    public void IClickEphemeralButton() throws Exception {
        context.getWebappPagesCollection().getPage(ConversationPage.class).clickEphemeralButton();
    }

    @When("^I see (.*) with unit (.*) on ephemeral button$")
    public void ISeeTimeShortOnEphemeralTimer(String time, String unit) throws Exception {
        Assert.assertTrue("Time " + time + " on ephemeral button is not shown", context.getWebappPagesCollection().getPage(
                ConversationPage.class).
                isTimeShortOnEphemeralButtonVisible(time));
        Assert.assertTrue("Time unit " + unit + " on ephemeral button is not shown", context.getWebappPagesCollection().getPage(
                ConversationPage.class).
                isTimeUnitOnEphemeralButtonVisible(unit));
    }

    @When("^I see placeholder of conversation input is (.*)$")
    public void ISeePlaceholderOfInput(String label) throws Exception {
        Assert.assertThat(context.getWebappPagesCollection().getPage(com.wearezeta.auto.web.pages.ConversationPage.class).
                getPlaceholderOfConversationInput(), equalTo(label));
    }
}
