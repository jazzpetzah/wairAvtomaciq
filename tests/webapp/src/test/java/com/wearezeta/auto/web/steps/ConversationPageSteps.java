package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

import java.awt.image.BufferedImage;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.Message;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.TakeOverPage;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConversationPageSteps {

    private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.69;

    private static final String TOOLTIP_PING = "Ping";
    private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + K)";
    private static final String SHORTCUT_PING_MAC = "(⌘⌥K)";
    private static final String TOOLTIP_CALL = "Call";
    private static final String TOOLTIP_VIDEO_CALL = "Video Call";

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());
    private static final String VIDEO_MESSAGE_IMAGE = "example.png";

    private String randomMessage;

    private String rememberedEditTimeStamp;

    private final TestContext context;

    public ConversationPageSteps() {
        this.context = new TestContext();
    }

    public ConversationPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I write random message$")
    public void WhenIWriteRandomMessage() throws Exception {
        randomMessage = CommonUtils.generateGUID();
        IWriteMessage(randomMessage);
    }

    @Then("^I verify that random message was typed$")
    public void IVerifyThatRandomMessageWasTyped() throws Exception {
        assertThat("Random message in input field", context.getPagesCollection().getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(randomMessage));
    }

    @Then("^I verify that message \"(.*)\" was typed$")
    public void IVerifyThatMessageWasTyped(String message) throws Exception {
        assertThat("Message in input field", context.getPagesCollection().getPage(ConversationPage.class).
                getMessageFromInputField(),
                equalTo(message));
    }

    @When("^I write message (.*)$")
    public void IWriteMessage(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    @When("^I paste message from file (.*)$")
    public void IPasteMessageFromFile(String file) throws Exception {
        String s = WebCommonUtils.getTextFromFile(file);
        String message = "";
        if (WebAppExecutionContext.getBrowser().isSupportingKeys()) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\n') {
                    message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
                } else {
                    message = message + c;
                }
            }
        } else {
            message = s;
        }
        int index = 0;
        while (index < message.length()) {
            String chunk = message.substring(index, Math.min(index + 100, message.length()));
            context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(chunk);
            index += 100;
        }
    }

    @When("^I write (\\d+) new lines$")
    public void IWriteXNewLines(int amount) throws Exception {
        String message = "";
        for (int i = 0; i < amount; i++) {
            if (WebAppExecutionContext.getBrowser().isSupportingKeys()) {
                message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
            } else {
                message = message + "\n";
            }
        }
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    @When("^I send message$")
    public void ISendMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendNewMessage();
    }

    @Then("^I see random message in conversation$")
    public void ThenISeeRandomMessageInConversation() throws Exception {
        ISeeTextMessage(randomMessage);
    }

    @Then("^I (do not )?see embedded( soundcloud| youtube| vimeo| spotify)? message (.*)")
    public void ISeeEmbeddedMessage(String doNot, String typeOfMessage, String url) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isMessageEmbedded(true, typeOfMessage, url));
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isMessageEmbedded(false, typeOfMessage, url));
        }
    }

    @When("^I click People button in one to one conversation$")
    public void WhenIClickPeopleButtonIn1to1() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
    }

    @When("^I close Group Participants popover$")
    public void WhenICloseGroupParticipantsPopover() throws Exception {
        GroupPopoverContainer peoplePopoverPage = context.getPagesCollection().getPage(GroupPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
        }
    }

    @When("^I close Single User Profile popover$")
    public void WhenICloseSingleUserPopover() throws Exception {
        SingleUserPopoverContainer peoplePopoverPage = context.getPagesCollection().getPage(SingleUserPopoverContainer.class);
        if (peoplePopoverPage.isVisible()) {

            peoplePopoverPage.waitUntilVisibleOrThrowException();
            context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
        }
    }

    @When("^I click People button in group conversation$")
    public void WhenIClickPeopleButtonInGroup() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPeopleButton();
    }

    @When("^I see verified icon in conversation$")
    public void ISeeVerifiedIconInConversation() throws Throwable {
        assertThat("No verified icon", context.getPagesCollection().getPage(ConversationPage.class).isConversationVerified());
    }

    @And("^I see titlebar with (.*)$")
    public void ISeeTitlebar(String conversationName) throws Throwable {
        assertThat("Wrong titlebar label", context.getPagesCollection().getPage(ConversationPage.class).getTitlebarLabel(),
                equalTo(conversationName.toUpperCase()));
    }

    @When("^I send picture (.*) to the current conversation$")
    public void WhenISendPicture(String pictureName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendPicture(pictureName);
    }

    @Then("^I see sent picture (.*) in the conversation view$")
    public void ISeeSentPicture(String pictureName) throws Exception {
        assertThat("Overlap score of image comparsion", context.getPagesCollection().getPage(ConversationPage.class)
                .getOverlapScoreOfLastImage(pictureName), greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
    }

    @Then("^I do not see any picture in the conversation view$")
    public void ISeeSentPicture() throws Exception {
        assertThat("I see a picture in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .isImageInvisible());
    }

    @Then("^I see only (\\d+) pictures? in the conversation$")
    public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
        assertThat("Number of images in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    @Then("^I see (\\d+) messages? in conversation$")
    public void ISeeXMessagesInConversation(int x) throws Exception {
        assertThat("Number of messages in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfMessagesInCurrentConversation(), equalTo(x));
    }

    @Then("^I verify the database is( not)? containing the message (.*) from (.*) in active conversation$")
    public void ISeeNoTraceInDatabase(String not, String message, String nameAlias) throws Exception {
        String userId = context.getUserManager().findUserByNameOrNameAlias(nameAlias).getId();
        String conversationId = context.getPagesCollection().getPage(ContactListPage.class).getActiveConversationId();
        assertThat("Couldn't get id of active conversation!", conversationId, not(isEmptyOrNullString()));
        if (not != null) {
            assertThat("Messages still in DB",
                    context.getPagesCollection().getPage(ConversationPage.class).getMessagesFromDb(conversationId, userId),
                    not(hasItem(message)));
        } else {
            assertThat("Messages not in DB",
                    context.getPagesCollection().getPage(ConversationPage.class).getMessagesFromDb(conversationId, userId),
                    hasItem(message));
        }
    }

    @Then("^I see (\\d+) messages? in database from (.*) in active conversation$")
    public void ISeeXMessagesInDatabase(int numberOfMessages, String nameAlias) throws Exception {
        String userId = context.getUserManager().findUserByNameOrNameAlias(nameAlias).getId();
        String conversationId = context.getPagesCollection().getPage(ContactListPage.class).getActiveConversationId();
        assertThat("Couldn't get id of active conversation!", conversationId, not(isEmptyOrNullString()));
        assertThat("Number of messages in DB",
                context.getPagesCollection().getPage(ConversationPage.class).getMessagesFromDb(conversationId, userId),
                hasSize(numberOfMessages));
    }

    @Then("^I see (\\d+) deleted messages? in conversation$")
    public void ISeeXDeletedMessagesInConversation(int x) throws Exception {
        assertThat("Number of deleted messages in the conversation", context.getPagesCollection().
                getPage(ConversationPage.class)
                .getNumberOfDeletedMessagesInCurrentConversation(), equalTo(x));
    }

    @Then("^I see file transfer button in conversation input$")
    public void ISeeFileButton() throws Exception {
        assertThat("No button found", context.getPagesCollection().getPage(ConversationPage.class).isFileButtonVisible());
    }

    @Then("^I verify that conversation input and buttons are not visible$")
    public void IDontSeeConversationInput() throws Exception {
        assertFalse("conversation input is still visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isConversationInputVisible());
        assertFalse("call button is still visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isCallButtonVisible());
        assertFalse("image button is still visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isImageButtonVisible());
        assertFalse("file button is still visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isFileButtonVisible());
        assertFalse("ping button is still visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isPingButtonVisible());
    }

    @When("^I send file (.*) to the current conversation$")
    public void WhenISendFile(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    @When("^I send (.*) sized file with name (.*) to the current conversation$")
    public void WhenIXSizedSendFile(String size, String fileName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");
        RandomAccessFile f = new RandomAccessFile(path + "/" + fileName, "rws");
        int fileSize = Integer.valueOf(size.replaceAll("\\D+", "").trim());
        if (size.contains("MB")) {
            f.setLength(fileSize * 1024 * 1024);
        } else if (size.contains("KB")) {
            f.setLength(fileSize * 1024);
        } else {
            f.setLength(fileSize);
        }
        f.close();
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    @When("^I send (.*) sized video with name (.*) to the current conversation$")
    public void WhenIXSizedSendVideo(String size, String fileName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");
        final String picturePath = WebCommonUtils.getFullPicturePath(VIDEO_MESSAGE_IMAGE);
        CommonUtils.generateVideoFile(path + "/" + fileName, size, picturePath);
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    @When("^I send audio file with length (.*) and name (.*) to the current conversation$")
    public void WhenIXSizedSendAudio(String length, String fileName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");

        CommonUtils.generateAudioFile(path + "/" + fileName, length);
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    @Then("^I (do not )?see file transfer for file (.*) in the conversation view$")
    public void ISeeFileTransferOfFile(String doNot, String fileName) throws Exception {
        if (doNot == null) {
            assertThat("Could not find file transfer for " + fileName, context.getPagesCollection().getPage(
                    ConversationPage.class)
                    .isFileTransferDisplayed(fileName));
            String fileNameWithoutExtension = null;
            if (fileName.substring(fileName.length() - 7).equalsIgnoreCase(".tar.gz")) {
                fileNameWithoutExtension = fileName.substring(0, fileName.length() - 7).toUpperCase();
            } else {
                fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.')).toUpperCase();
            }
            assertThat("Wrong file name for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                    .getFileNameOf(fileName), equalTo(fileNameWithoutExtension));
        } else {
            assertThat("File transfer displayed for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                    .isFileTransferInvisible(fileName));
        }
    }

    @When("^I cancel file upload of file (.*)$")
    public void WhenICancelFileUpload(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).cancelFileUpload(fileName);
    }

    @When("^I cancel video upload of video (.*)$")
    public void ICancelVideoUpload(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).cancelVideoUpload(fileName);
    }

    @Then("^I verify icon of file (.*) in the conversation view$")
    public void IVerifyIconOfFile(String fileName) throws Exception {
        assertThat("No file icon for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileIcon(fileName));
    }

    @Then("^I verify size of file (.*) is (.*) in the conversation view$")
    public void IVerifySizeOfFile(String fileName, String size) throws Exception {
        assertThat("Wrong file size for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileSizeOf(fileName), equalTo(size));
    }

    @Then("^I verify status of file (.*) is (.*) in the conversation view$")
    public void IVerifyStatusOfFile(String fileName, String status) throws Exception {
        assertThat("Wrong file status for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileStatusOf(fileName), equalTo(status));
    }

    @Then("^I verify status of file (.*) is (.*) in the conversation view if possible$")
    public void IVerifyStatusOfFileIfPossible(String fileName, String status) throws Exception {
        Optional<String> optionalStatus = context.getPagesCollection().getPage(ConversationPage.class)
                .getOptionalFileStatusOf(fileName);
        if (optionalStatus.isPresent()) {
            assertThat("Wrong file status for " + fileName, optionalStatus.get(), equalTo(status));
        }
    }

    @Then("^I verify type of file (.*) is (.*) in the conversation view$")
    public void IVerifyTypeOfFile(String fileName, String type) throws Exception {
        assertThat("Wrong file status for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileTypeOf(fileName), equalTo(type));
    }

    @Then("^I wait until file (.*) is (uploaded|downloaded) completely$")
    public void IWaitUntilFileUploaded(String fileName, String downloadType) throws Exception {
        assertThat("Upload still not finished for file " + fileName, context.getPagesCollection().
                getPage(ConversationPage.class)
                .waitUntilFileUploaded(fileName));
    }

    @Then("^I wait until placeholder for file (.*) disappears$")
    public void IWaitUntilFilePlaceholderDisappears(String fileName) throws Exception {
        assertThat("Upload still not finished for file " + fileName, context.getPagesCollection().
                getPage(ConversationPage.class)
                .waitUntilFilePlaceholderDisappears(fileName));
    }

    @Then("^I click icon to download file (.*) in the conversation view$")
    public void IDownloadFile(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickFileIcon(fileName);
    }

    @Then("^I wait until video (.*) is uploaded completely$")
    public void IWaitUntilVideoIsUploaded(String fileName) throws Exception {
        assertThat("Upload still not finished for video " + fileName, context.getPagesCollection().getPage(
                ConversationPage.class).waitUntilVideoUploaded(fileName));
    }

    @Then("^I click play button of video (.*) in the conversation view$")
    public void IClickPlayVideo(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).playVideo(fileName);
    }

    @Then("^I see cancel upload button for video (.*)$")
    public void ISeeCancelUpload(String fileName) throws Exception {
        assertThat("Cancel video upload button is not shown", context.getPagesCollection().getPage(ConversationPage.class)
                .isCancelButtonVisible(fileName));
    }

    @Then("^I see play button of video (.*) in the conversation view$")
    public void ISeePlayVideo(String fileName) throws Exception {
        assertThat("Play button is not shown", context.getPagesCollection().getPage(ConversationPage.class)
                .isPlayButtonVisible(fileName));
    }

    @Then("^I click pause button of video (.*) in the conversation view$")
    public void IClickPauseVideo(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pauseVideo(fileName);
    }

    @Then("^I wait until video (.*) is downloaded and starts to play$")
    public void IWaitUntilVideoStartsPlaying(String fileName) throws Exception {
        assertThat("Download still not finished for video " + fileName, context.getPagesCollection().getPage(
                ConversationPage.class)
                .waitUntilVideoPlays(fileName));
    }

    @Then("^I verify seek bar is shown for video (.*) in the conversation view$")
    public void IVerifyVideoSeekbar(String fileName) throws Exception {
        assertThat("No seekbar for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .isVideoSeekbarVisible(fileName));
    }

    @Then("^I verify time for video (.*) is changing in the conversation view$")
    public void IVerifyTimeOfVideo(String fileName) throws Exception {
        assertThat("Time is not changing for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .waitUntilVideoTimeChanges(fileName));
    }

    @Then("^I (do not )?see video message (.*) in the conversation view$")
    public void ISeeVideoMessage(String doNot, String fileName) throws Exception {
        ConversationPage page = context.getPagesCollection().getPage(ConversationPage.class);
        if (doNot == null) {
            assertThat("Could not find video message " + fileName, page.isVideoMessageVisible(fileName));
        } else {
            assertThat("video message displayed for " + fileName, page.isVideoMessageInvisible(fileName));
        }
    }

    @Then("^I wait until audio (.*) is uploaded completely$")
    public void IWaitUntilAudioIsUploaded(String fileName) throws Exception {
        assertThat("Upload still not finished for audio " + fileName, context.getPagesCollection().getPage(
                ConversationPage.class)
                .waitUntilAudioUploaded(fileName));
    }

    @Then("^I click play button of audio (.*) in the conversation view$")
    public void IClickPlayAudio(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).playAudio(fileName);
    }

    @Then("^I wait until audio (.*) is downloaded and starts to play$")
    public void IWaitUntilAudioStartsPlaying(String fileName) throws Exception {
        assertThat("Download still not finished for audio " + fileName, context.getPagesCollection().getPage(
                ConversationPage.class)
                .waitUntilAudioPlays(fileName));
    }

    @Then("^I verify seek bar is shown for audio (.*) in the conversation view$")
    public void IVerifyAudioSeekbar(String fileName) throws Exception {
        assertThat("No seekbar for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .isAudioSeekbarVisible(fileName));
    }

    @Then("^I verify time for audio (.*) is (.*) in the conversation view$")
    public void IVerifyTimeOfAudio(String fileName, String time) throws Exception {
        ConversationPage page = context.getPagesCollection().getPage(ConversationPage.class);
        if (time.equals("changing")) {
            assertThat("Time is not changing for " + fileName, page.waitUntilAudioTimeChanges(fileName));
        } else {
            assertThat("Time is wrong" + fileName, page.getAudioTime(fileName), equalTo(time));
        }
    }

    @Then("^I (do not )?see audio message (.*) in the conversation view$")
    public void ISeeAudioMessage(String doNot, String fileName) throws Exception {
        ConversationPage page = context.getPagesCollection().getPage(ConversationPage.class);
        if (doNot == null) {
            assertThat("Could not find audio message " + fileName, page.isAudioMessageVisible(fileName));
        } else {
            assertThat("audio message displayed for " + fileName, page.isAudioMessageInvisible(fileName));
        }
    }

    @When("^I click context menu of the (second |third )?last message$")
    public void IClickContextMenuOfThirdLastMessage(String indexNumber) throws Exception {
        int messageId = getXLastMessageIndex(indexNumber);
        context.getPagesCollection().getPage(ConversationPage.class).clickContextMenuOnMessage(messageId);
    }

    @When("^I click to delete message for everyone in context menu$")
    public void IClickDeleteEverywhereInContextMenuOfLatestMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickDeleteEverywhereInContextMenuOfLastMessage();
    }

    @When("^I click to delete message for me in context menu$")
    public void IClickDeleteForMeInContextMenuOfLatestMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickDeleteForMeInContextMenuOfLastMessage();
    }

    @When("^I click confirm to delete message for everyone$")
    public void IClickConfirmToDeleteForEveryone() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).confirmDeleteForEveryone();
    }

    @When("^I click confirm to delete message for me$")
    public void IClickConfirmToDelete() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).confirmDelete();
    }

    @When("^I hover over the latest message$")
    public void IHoverOverLatestMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).hoverOverLastMessage();
    }

    @When("^I do not see delete button for latest message$")
    public void IDoNotSeeDeleteButton() throws Exception {
        assertFalse("Delete button is visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isDeleteButtonVisibleForLastMessage());
    }

    @When("^I do not see edit button in context menu$")
    public void IDoNotSeeEditButtonInContext() throws Exception {
        assertTrue("Edit button is visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isEditButtonInvisibleForLastMessage());
    }

    @When("^I do not see delete for everyone button in context menu$")
    public void IDoNotSeeDeleteForEveryoneButtonInContext() throws Exception {
        assertTrue("Delete for everyone button is visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isDeleteForEveryoneButtonInContextMenuInvisible());
    }

    @When("^I do not see like button in context menu$")
    public void IDoNotSeeLikeButtonInContext() throws Exception {
        assertTrue("Like button is visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isLikeButtonInContextMenuInvisible());
    }

    @When("^I do not see download button in context menu$")
    public void IDoNotSeeDownloadButtonInContext() throws Exception {
        assertTrue("Download button is visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isDownloadButtonInContextMenuInvisible());
    }

    @When("^I see delete for me button in context menu$")
    public void ISeeDeleteForMeButtonInContext() throws Exception {
        assertTrue("Delete For Me button is not visible", context.getPagesCollection().getPage(ConversationPage.class)
                .isDeleteForMeButtonInContextMenuVisible());
    }

    @When("^I click to edit message in context menu$")
    public void IClickEditInContextMenuOfLatestMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickEditInMessageContextMenu();
    }

    @When("^I( do not)? see like symbol for (second )?last message$")
    public void ISeeLikeButton(String doNot, String second) throws Exception {
        boolean isSecond = " second".equals(second);
        if (!isSecond) {
            if (doNot == null) {
                assertTrue("Like symbol is not visible", context.getPagesCollection().getPage(ConversationPage.class).
                        isLikeSymbolVisibleForLastMessage());
            } else {
                assertTrue("Like symbol is visible", context.getPagesCollection().getPage(ConversationPage.class).
                        isLikeSymbolInvisibleForLastMessage());
            }
        } else {
            if (doNot == null) {
                assertTrue("Like symbol is not visible", context.getPagesCollection().getPage(ConversationPage.class).
                        isLikeSymbolVisibleForSecondLastMessage());
            } else {
                assertTrue("Like symbol is visible", context.getPagesCollection().getPage(ConversationPage.class).
                        isLikeSymbolInvisibleForSecondLastMessage());
            }
        }
    }

    @When("^I remember edit timestamp of( second)? last message$")
    public void IRememberEditTimestamp(String second) throws Exception {
        if (second == null) {
            rememberedEditTimeStamp = context.getPagesCollection().getPage(ConversationPage.class)
                    .getLastEditTimestamp();
        } else {
            rememberedEditTimeStamp = context.getPagesCollection().getPage(ConversationPage.class)
                    .getSecondLastEditTimestamp();
        }
    }

    @Then("^I verify the edit timestamp of( second)? last message equals the remembered timestamp$")
    public void ICompareTimestamps(String second) throws Exception {
        String editTimeStamp;
        if (second == null) {
            editTimeStamp = context.getPagesCollection().getPage(ConversationPage.class)
                    .getLastEditTimestamp();
        } else {
            editTimeStamp = context.getPagesCollection().getPage(ConversationPage.class)
                    .getSecondLastEditTimestamp();
        }
        assertEquals("The timestamps are not equal", rememberedEditTimeStamp, editTimeStamp);
    }

    @Then("^I( do not)? see delivery status of last message is (.*)$")
    public void ISeeDeliveryStatus(String doNot, String status) throws Exception {
        boolean isDoNot = " do not".equals(doNot);
        if (isDoNot) {
            if (context.getPagesCollection().getPage(ConversationPage.class).isDeliveryStatusOfLastMessageVisible()) {
                assertThat("Status wrong",
                        context.getPagesCollection().getPage(ConversationPage.class).getDeliveryStatusOfLastMessage(),
                        not(equalTo(status)));
            }
        } else {
            assertThat("Status wrong",
                    context.getPagesCollection().getPage(ConversationPage.class).getDeliveryStatusOfLastMessage(),
                    equalTo(status));
        }
    }

    @Then("^I( do not)? see message header for( second)? last message$")
    public void ISeeMessageHeader(String doNot, String second) throws Exception {
        boolean isDoNot = " do not".equals(doNot);
        boolean isSecond = " second".equals(second);
        if (isDoNot) {
            if (isSecond) {
                assertFalse("Second last message header is VISIBLE",
                        context.getPagesCollection().getPage(ConversationPage.class).isSecondLastMsgHeaderVisible());
            } else {
                assertFalse("Last message header is VISIBLE",
                        context.getPagesCollection().getPage(ConversationPage.class).isLastMsgHeaderVisible());
            }
        } else if (isSecond) {
            assertTrue("Second last message header is NOT VISIBLE",
                    context.getPagesCollection().getPage(ConversationPage.class).isSecondLastMsgHeaderVisible());
        } else {
            assertTrue("Last message header NOT VISIBLE",
                    context.getPagesCollection().getPage(ConversationPage.class).isLastMsgHeaderVisible());
        }
    }

    @When("^I click (like|unlike) button in context menu for last message$")
    public void IClickToLikeInContextMenuOfLatestMessage(String like) throws Exception {
        boolean isLike = "like".equals(like);
        if (isLike) {
            assertTrue("Like button is not visible", context.getPagesCollection().getPage(ConversationPage.class)
                    .isLikeButtonInContextMenuVisible());
            context.getPagesCollection().getPage(ConversationPage.class).clickReactInContextMenuOfLastMessage();
        } else {
            assertTrue("Unlike button is not visible", context.getPagesCollection().getPage(ConversationPage.class)
                    .isUnlikeButtonInContextMenuVisible());
            context.getPagesCollection().getPage(ConversationPage.class).clickReactInContextMenuOfLastMessage();
        }
    }

    @When("^I click to (like|unlike) the (third |second )?last message with(out)? other likes$")
    public void IClickToLikeLatestMessageWithoutOtherLikes(String like, String index, String out) throws Exception {
        boolean isWithout = "out".equals(out);
        boolean isLike = "like".equals(like);
        int indexNummer = getXLastMessageIndex(index);
        if (isWithout) {
            if (isLike) {
                context.getPagesCollection().getPage(ConversationPage.class).clickLikeMessageWithoutOtherLikes(indexNummer);
            } else {
                context.getPagesCollection().getPage(ConversationPage.class).clickUnlikeMessageWithoutOtherLikes(indexNummer);
            }
        } else if (isLike) {
            context.getPagesCollection().getPage(ConversationPage.class).clickLikeMessageWithOtherLikes(indexNummer);
        } else {
            context.getPagesCollection().getPage(ConversationPage.class).clickUnlikeMessageWithOtherLikes(indexNummer);
        }
    }

    @When("^I open like list of the latest message$")
    public void IOpenLikeListForLatestMsg() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickLatestLikeLine();
    }

    @When("^I( do not)? see like list of last message$")
    public void ISeeLikeListOfLatestMsg(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue("Like list is NOT visible", context.getPagesCollection()
                    .getPage(ConversationPage.class).isLikeListOfLatestMsgVisible());
        } else {
            assertFalse("Like list is VISIBLE", context.getPagesCollection()
                    .getPage(ConversationPage.class).isLikeListOfLatestMsgVisible());
        }
    }

    @When("^I see (\\d+) avatars in like list of last message$")
    public void ISeeXAvatarsInLikeList(int amount) throws Exception {
        assertThat("Wrong amount of avatars found", context.getPagesCollection()
                .getPage(ConversationPage.class).getAvatarsInLatestLikeList(),
                equalTo(amount));
    }

    @When("^I close like list of last message$")
    public void ICloseLikeListOfLatestMsg() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXLatestLikeList();
    }

    @Then("^I (do not )?see likes below the (third |second )?last message$")
    public void ISeeLikesForLatestMessage(String not, String indexValue) throws Exception {
        int messageIndex = getXLastMessageIndex(indexValue);
        if (not == null) {
            assertTrue("Likes of others are NOT visible for last message", context.getPagesCollection().getPage(
                    ConversationPage.class).isLikeLineVisibleForMessage(messageIndex));
        } else {
            assertTrue("Likes of others are visible for last message", context.getPagesCollection().getPage(
                    ConversationPage.class)
                    .isLikeLineInvisibleForMessage(messageIndex));
        }
    }

    @Then("^I see the (third |second )?last message is only liked by me$")
    public void ISeeLatestMessageIsOnlyLikedByMe(String indexValue) throws Exception {
        int messageIndex = getXLastMessageIndex(indexValue);
        assertTrue("The " + indexValue + "last message is NOT only liked by you", context.getPagesCollection().getPage(
                ConversationPage.class)
                .isUnlikeWithoutOtherLikesVisibleForMessage(messageIndex));
    }

    @Then("^I see the last message is only liked by others$")
    public void ISeeLastMessageIsOnlyLikedByOthers() throws Exception {
        assertTrue("The message is liked by you", context.getPagesCollection().getPage(ConversationPage.class)
                .isLikeWithOtherLikesVisibleForLastMessage());
    }

    @Then("^I see the last message is liked by others and me$")
    public void ISeeLastMessageIsLikedByOthersMe() throws Exception {
        assertTrue("Message is NOT liked by others and you", context.getPagesCollection().getPage(ConversationPage.class)
                .isUnlikeWithOtherLikesVisibleForLastMessage());
    }

    @Then("^I see the last message is liked by users? (.*)$")
    public void ISeeLastMessageIsLikedBy(String usersToNameAliases) throws Exception {
        List<String> likers = context.getPagesCollection().getPage(ConversationPage.class).getUsersThatLikeTheLastMessage();
        List<String> aliases = context.getUserManager().splitAliases(usersToNameAliases);
        String[] users = new String[aliases.size()];
        for (int i = 0; i < aliases.size(); i++) {
            ClientUser userTo = context.getUserManager().findUserByNameOrNameAlias(aliases.get(i));
            users[i] = userTo.getName();
        }
        assertThat("User not found in like message", likers, hasItems(users));
        assertThat("Wrong number of likes", likers, hasSize(users.length));
    }

    @Then("^I see (.*) is the most recent liker of last message$")
    public void ISeeMostRecentLiker(String liker) throws Exception {
        List<String> likers = context.getPagesCollection().getPage(ConversationPage.class).getUsersThatLikeTheLastMessage();
        ClientUser userTo = context.getUserManager().findUserByNameOrNameAlias(liker);
        String user = userTo.getName();
        assertThat("User is not the most recent liker", likers.get(likers.size() - 1), is(user));
    }

    @Then("^I see names in like string of last message$")
    public void ISeeNamesInLastLikeString() throws Exception {
        String likeString = context.getPagesCollection().getPage(ConversationPage.class).getLastLikeString();
        assertThat("There are no names in the like string", likeString, not(containsString("people")));
    }

    @Then("^I see count of (\\d+) people in like string of last message$")
    public void ISeePeopleCountInLastLikeString(int count) throws Exception {
        String likeString = context.getPagesCollection().getPage(ConversationPage.class).getLastLikeString();
        assertThat("Wrong people count", likeString, is(count + " people"));
    }

    @When("^I click reset session on the latest decryption error")
    public void IClickToResetSession() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickToResetSessionOnLatestError();
    }

    @When("^I close reset session dialog$")
    public void IClickConfirmToResetSession() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).setCloseResetSessionDialog();
    }

    @Then("^I see correct people button tool tip$")
    public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
        assertTrue("Tooltip NOT visible for people button", context.getPagesCollection().getPage(ConversationPage.class).
                isPeopleButtonToolTipCorrect());
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

    @Then("^I( do not)? see (.*) action in conversation$")
    public void ThenISeeActionInConversation(String doNot, String message) throws Exception {
        if (doNot == null) {
            ThenISeeActionInConversation(message, 1);
        } else {
            ThenISeeActionInConversation(message, 0);
        }
    }

    @Then("^I see (.*) action (\\d+) times in conversation$")
    public void ThenISeeActionInConversation(String message, int times) throws Exception {
        assertThat(message + " action", context.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(message), equalTo(times));
    }

    @Then("^I( do not)? see (.*) action for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String doNot, String message, String contacts) throws Exception {
        if (doNot == null) {
            ThenISeeActionForContactInConversation(message, 1, contacts);
        } else {
            ThenISeeActionForContactInConversation(message, 0, contacts);
        }
    }

    @And("^I see unique username starts with (.*) in conversation$")
    public void ISeeUniqueUsernameOnSelfProfilePage(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertThat("Username in conversation",
                context.getPagesCollection().getPage(ConversationPage.class).getUniqueUsername(), startsWith(name));
    }

    @Then("^I see (.*) action (\\d+) times for (.*) in conversation$")
    public void ThenISeeActionForContactInConversation(String message, int times, String contacts) throws Exception {
        contacts = context.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(context.getUserManager().splitAliases(contacts));
        assertThat(message + " action for " + contacts, context.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(parts), equalTo(times));
    }

    @When("^I add (.*) to group chat$")
    public void IAddContactToGroupChat(String contact) throws Exception {
        WhenIClickPeopleButtonInGroup();
        GroupPopoverPageSteps cpSteps = new GroupPopoverPageSteps(context);
        cpSteps.IClickAddPeopleButton();
        cpSteps.ISearchForUser(contact);
        cpSteps.ISelectUserFromSearchResults(contact);
        cpSteps.IChooseToCreateGroupConversation();
    }

    @When("^I click on ephemeral button$")
    public void IClickEphemeralButton() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickEphemeralButton();
    }

    @When("^I set the timer for ephemeral to (.*)$")
    public void ISetEphemeralTimer(String label) throws Exception {
        assertThat("Ephemeral option is available",
                context.getPagesCollection().getPage(ConversationPage.class).getEphemeralTimers(),
                hasItem(label.toUpperCase()));
        context.getPagesCollection().getPage(ConversationPage.class).setEphemeralTimer(label.toUpperCase());
    }

    @When("^I see (.*) with unit (.*) on ephemeral button$")
    public void ISeeTimeShortOnEphemeralTimer(String time, String unit) throws Exception {
        Assert.assertTrue("Time " + time + " on ephemeral button is not shown",
                context.getPagesCollection().getPage(ConversationPage.class).isTimeShortOnEphemeralButtonVisible(time));
        Assert.assertTrue("Time unit " + unit + " on ephemeral button is not shown",
                context.getPagesCollection().getPage(ConversationPage.class).isTimeUnitOnEphemeralButtonVisible(unit));
    }

    @When("^I see placeholder of conversation input is (.*)$")
    public void ISeePlaceholderOfInput(String label) throws Exception {
        Assert.assertThat(context.getPagesCollection().getPage(ConversationPage.class).getPlaceholderOfConversationInput(),
                equalTo(label));
    }

    @When("^I( do not)? see timer next to the last message$")
    public void ISeeTimer(String doNot) throws Exception {

    }

    @When("^I see the last message is( not)? obfuscated$")
    public void ISeeObfuscatedMessage(String not) throws Exception {
        if (not == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isLastMessageObfuscated());
        } else {
            assertTrue("Last message is obfuscated",
                    context.getPagesCollection().getPage(ConversationPage.class).isLastMessageNotObfuscated());
        }
    }

    @When("^I see the second last message is obfuscated$")
    public void ISeeSecondLastObfuscatedMessage() throws Exception {
        assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isSecondLastMessageObfuscated());
    }

    @When("^I (do not )?see block replaces the last message in the conversation view$")
    public void ISeeReplacingBlock(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue("Last message is not replaced with a replacing block",
                    context.getPagesCollection().getPage(ConversationPage.class).isLastMessageReplaced());
        } else {
            assertTrue("Replacing block is still shown on the last message",
                    context.getPagesCollection().getPage(ConversationPage.class).isOrangeBlockInLastMessageNotVisible());
        }
    }

    @When("^I click ping button$")
    public void IClickPingButton() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickPingButton();
    }

    @Then("^I see text message (.*)")
    public void ISeeTextMessage(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForPresentMessageContains(message);
    }

    @Then("^I see latest text message (.*)")
    public void ISeeLatestTextMessage(String message) throws Exception {
        assertTrue(String.format("Last message is NOT visible with text '%s'", message),
                context.getPagesCollection().getPage(ConversationPage.class).isLatestMessageWithTextVisible(message));
    }

    @Then("^I really see text message (.*)")
    public void ISeeTextMessageInViewPort(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForDisplayedMessageContains(message, 30);
    }

    private int getXLastMessageIndex(String indexValue) throws Exception {
        int indexNummer = 1;
        if (indexValue == null) {
            return indexNummer;
        }
        switch (indexValue) {
            case "third ":
                indexNummer = 3;
                break;
            case "second ":
                indexNummer = 2;
                break;
            default:
                indexNummer = 1;
                break;
        }
        return indexNummer;
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

    @Then("^I verify the second last text message equals to (.*)")
    public void IVerifySecondLastTextMessage(String expectedMessage) throws Exception {
        assertThat(context.getPagesCollection().getPage(ConversationPage.class).getSecondLastTextMessage(), equalTo(
                expectedMessage));
    }

    @Then("^I do not see text message ?(.*)$")
    public void IDontSeeTextMessage(String message) throws Exception {
        assertTrue("Saw text message " + message, context.getPagesCollection().getPage(ConversationPage.class)
                .isTextMessageInvisible(message == null ? "" : message));
    }

    @When("^I call$")
    public void ICallUser() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).clickCallButton();
    }

    @Then("^I( do not)? see call button$")
    public void ISeeCallButton(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonInvisible());
        }
    }

    @Then("^I( do not)? see video call button$")
    public void ISeeVideoCallButton(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isVideoCallButtonVisible());
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isVideoCallButtonInvisible());
        }
    }

    @When("^I accept the incoming call$")
    public void IAcceptIncomingCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickAcceptCallButton();
    }

    @When("^I accept the incoming video call$")
    public void IAcceptIncomingVideoCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickAcceptVideoCallButton();
    }

    @When("^I silence the incoming call$")
    public void ISilenceIncomingCall() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickSilenceCallButton();
    }

    @When("^I click on picture$")
    public void WhenIClickOnPicture() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickOnPicture();
    }

    @Then("^I( do not)? see picture (.*) in fullscreen$")
    public void ISeePictureInFullscreen(String doNot, String pictureName) throws Exception {
        ConversationPage conversationPage = context.getPagesCollection().getPage(ConversationPage.class);
        if (doNot == null) {
            assertTrue(conversationPage.isPictureInModalDialog());
            assertTrue(conversationPage.isPictureInFullscreen());
            assertThat("Overlap score of image comparsion", conversationPage.getOverlapScoreOfFullscreenImage(pictureName),
                    greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            assertTrue(conversationPage.isPictureNotInModalDialog());
        }
    }

    @When("^I click x button to close fullscreen mode$")
    public void IClickXButtonToCloseFullscreen() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButton();
    }

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
        assertThat("No image foud in conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .isImageMessageFound());
    }

    @Then("^I verify that message (.*) was cached$")
    public void IVerifyThatMessageWasCached(String message) throws Exception {
        assertThat("Cached message in input field", context.getPagesCollection().getPage(ConversationPage.class)
                .getMessageFromInputField(), equalTo(message));
    }

    @Then("^I type shortcut combination to open search$")
    public void ITypeShortcutCombinationToOpenSearch() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressShortCutForSearch();
    }

    @Then("^I type shortcut combination to ping$")
    public void ITypeShortcutCombinationToPing() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressShortCutForPing();
    }

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

    @Then("^I see correct call button tooltip$")
    public void ISeeCorrectCallButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_CALL;
        assertThat("Call button tooltip", context.getPagesCollection().getPage(ConversationPage.class).getCallButtonToolTip(),
                equalTo(tooltip));
    }

    @Then("^I see correct video call button tooltip$")
    public void ISeeCorrectVideoCallButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_VIDEO_CALL;
        assertThat("Video Call button tooltip", context.getPagesCollection().getPage(ConversationPage.class).
                getVideoCallButtonToolTip(),
                equalTo(tooltip));
    }

    @Then("^I see nobody to call message")
    public void ISeeNobodyToCallMessage() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).isNobodyToCallMsgVisible();
    }

    @And("^I click on pending user avatar$")
    public void IClickOnPendingUserAvatar() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar();
    }

    @Then("^I see cancel pending request button in the conversation view$")
    public void ISeeCancelRequestButton() throws Exception {
        assertTrue("Cancel request is NOT visible in conversation list", context.getPagesCollection().getPage(
                ConversationPage.class).isCancelRequestButtonVisible());
    }

    @Then("^I click cancel pending request button in the conversation view$")
    public void IClickOnCancelRequestButton() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickCancelPendingRequestButton();
    }

    @And("^I click on avatar of user (.*) in conversation view$")
    public void IClickOnUserAvatar(String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserBy(userAlias, FindBy.NAME_ALIAS);
        context.getPagesCollection().getPage(ConversationPage.class).clickUserAvatar(user.getId());
    }

    @When("^I start a video call$")
    public void IMakeVideoCallToUser() throws Throwable {
        context.getPagesCollection().getPage(ConversationPage.class).clickVideoCallButton();
    }

    @When("^I verify all remembered messages are present in conversation (.*)$")
    public void IVerifyAllRememberedMessages(String conversation) throws Exception {
        SortedSet<Message> processedMessages = new TreeSet<>();
        SortedSet<Message> chunkedActualMessages = context.getPagesCollection().getPage(ConversationPage.class).
                getAllLoadedMessages();
        SortedSet<Message> rememberedMessages = context.getConversationStates().getAllMessages(conversation);

        //DEBUG ON
        for (Message rememberedMessage : rememberedMessages) {
            log.info(rememberedMessage);
        }
        log.info("###");
        for (Message chunkedMessage : chunkedActualMessages) {
            log.info(chunkedMessage);
        }
        log.info("###");
        //DEBUG OFF

        Iterator<Message> iterator = chunkedActualMessages.iterator();
        Message currentMessage = null;
        for (Message rememberedMessage : rememberedMessages) {
            try {
                currentMessage = iterator.next();
            } catch (NoSuchElementException e) {
                try {
                    // reached end of loaded message list. We try to scroll up the conversation and continue 
                    // unless scrolling up does not deliver new messages
                    context.getPagesCollection().getPage(ConversationPage.class).scrollUp();
                    chunkedActualMessages = context.getPagesCollection().getPage(ConversationPage.class).
                            getAllLoadedMessages();
                    // removing already processed messages
                    chunkedActualMessages.removeAll(processedMessages);
                    log.info("###");
                    for (Message chunkedMessage : chunkedActualMessages) {
                        log.info(chunkedMessage);
                    }
                    iterator = chunkedActualMessages.iterator();
                    currentMessage = iterator.next();
                } catch (NoSuchElementException ex) {
                    throw new NoSuchElementException("Could not find " + rememberedMessage + " in actual message list");
                }
            }
            boolean isEquals = rememberedMessage.equals(currentMessage);
            log.info("Comparing " + rememberedMessage + " <> " + currentMessage + " = " + isEquals);
            //TODO comparision, correct timestamps. alternatively compare IDs
            processedMessages.add(currentMessage);
        }
    }

    @When("^I( do not)? see long message warning dialog$")
    public void ISeeLongMessageWarningDialog(String doNot) throws Exception {
        if (doNot == null) {
            assertThat(context.getPagesCollection().getPage(ConversationPage.class).isLongMessageWarnDialogShown(), is(true));
        } else {
            assertThat(context.getPagesCollection().getPage(ConversationPage.class).isLongMessageWarnDialogNotShown(), is(true));
        }
    }

    @When("^I click OK on long message warning dialog$")
    public void IClickOKOnLongMessageWarning() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickOKButtonOnLongMWarning();
    }

    @When("^I click X button on long message warning dialog$")
    public void IClickXOnLongMessageWarning() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButtonOnLongMWarning();
    }

    @When("^I delete (\\d+) characters from the conversation input$")
    public void IDeleteTypedMessage(int count) throws Exception {
        int i = count;
        while (i != 0) {
            context.getPagesCollection().getPage(ConversationPage.class).clearConversationInput();
            i--;
        }
    }

    @When("^I press Up Arrow to edit message$")
    public void IPressUpArrow() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressUpArrow();
    }

    @Then("^I (do not )?see location message (.*) with ([-+]?[0-9]*\\.?[0-9]+) and ([-+]?[0-9]*\\.?[0-9]+) in the conversation view$")
    public void ISeeLocationMessage(String doNot, String locationName, float longitude, float latitude) throws Exception {
        if (doNot == null) {
            //check location name:
            assertThat("Could not find location message " + locationName,
                    context.getPagesCollection().getPage(ConversationPage.class).getLocationName(), equalTo(locationName));
            String locationLinkValue = context.getPagesCollection().getPage(ConversationPage.class).getLocationNameFromLink();
            assertThat("The link doesn't contain a proper location", locationLinkValue, containsString(locationName));
            //getting coordinates from location link
            ArrayList<Float> listCoordinates = new ArrayList<>();
            Pattern p = Pattern.compile("[-]?[0-9]*\\.?[0-9]+");
            Matcher m = p.matcher(locationLinkValue);
            while (m.find()) {
                listCoordinates.add(Float.parseFloat(m.group()));
            }
            assertThat("The link doesn't contain proper coordinates", listCoordinates, hasItems(longitude, latitude));
        } else {
            assertThat("Location message " + locationName + "is shown",
                    context.getPagesCollection().getPage(ConversationPage.class).isLocationNotShownInConversationView());
        }
    }

    @When("^I cancel video download of video (.*)$")
    public void ICancelVideoDownload(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).cancelVideoDownload(fileName);
    }

    @When("^I see broadcast indicator is( not)? shown for video$")
    public void ISeeVideoButtonPulsating(String not) throws Exception {
        if (not == null) {
            // video button pulsating?
            assertThat("broadcast indicator not shown :(",
                    context.getPagesCollection().getPage(ConversationPage.class).isBroadcastIndicatorVideoShown());
        } else {
            // video button not pulsating?
            assertThat("broadcast indicator is shown :(",
                    context.getPagesCollection().getPage(ConversationPage.class).isBroadcastIndicatorVideoNotShown());
        }
    }

    @Then("^I (do not )?see a title (.*) in link preview in the conversation view$")
    public void ISeeLinkTitle(String doNot, String linkTitle) throws Exception {
        if (doNot == null) {
            assertThat("Could not find link title " + linkTitle,
                    context.getPagesCollection().getPage(ConversationPage.class).getLinkTitle(), containsString(linkTitle));
        } else {
            assertThat("link title " + linkTitle + "is shown",
                    context.getPagesCollection().getPage(ConversationPage.class).isLinkTitleNotShownInConversationView());
        }
    }

    @Then("^I (do not )?see a picture (.*) from link preview$")
    public void ISeePictureInLinkPreview(String doNot, String pictureName) throws Exception {
        if (doNot == null) {
            assertThat("I see a picture from link preview in the conversation",
                    context.getPagesCollection().getPage(ConversationPage.class).isImageFromLinkPreviewVisible());

            final String picturePath = WebCommonUtils.getFullPicturePath(pictureName);
            BufferedImage originalImage = ImageUtil.readImageFromFile(picturePath);
            BufferedImage linkPreviewScreenshot = context.getPagesCollection().getPage(ConversationPage.class).
                    getImageFromLastLinkPreview();

            assertThat("Not enough good matches", ImageUtil.getMatches(originalImage, linkPreviewScreenshot), greaterThan(100));
        } else {
            assertThat("I see a picture in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                    .isImageFromLinkPreviewNotVisible());
        }
    }

    @Then("^I see link (.*) in link preview message")
    public void ISeeLinkInLinkPreview(String link) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForLinkPreviewContains(link);
    }

    @Then("^I (do not )?see latest message is link preview message$")
    public void ISeeLatestMessageIsLinkPreview(String not) throws Exception {
        if (not == null) {
            assertTrue("latest message is no link preview", context.getPagesCollection().getPage(ConversationPage.class).
                    isLinkPreviewLinkVisibleForLatestMessage());
        } else {
            assertTrue("latest message is a link preview", context.getPagesCollection().getPage(ConversationPage.class).
                    isLinkPreviewLinkInvisibleForLatestMessage());
        }
    }

    @When("^I click x button to close edit mode$")
    public void IClickXButoonToCancelEdit() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButtonToCloseEdit();
    }

    @And("^I( do not)? see first time experience with watermark$")
    public void ISeeWelcomePage(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            assertThat("No watermark wire logo shown",
                    context.getPagesCollection().getPage(ConversationPage.class).isWatermarkVisible());
            assertThat("First time experience message",
                    context.getPagesCollection().getPage(ConversationPage.class).getFirstTimeExperienceMessage(),
                    containsString("Start a conversation or invite people to join."));
        } else {
            assertThat("Watermark wire logo shown",
                    context.getPagesCollection().getPage(ConversationPage.class).isWatermarkNotVisible());
            assertThat("First time experience message",
                    context.getPagesCollection().getPage(ConversationPage.class).getFirstTimeExperienceMessage(),
                    not(containsString("Start a conversation or invite people to join.")));
        }
    }

    @And("^I( do not)? see take over screen$")
    public void ISeeTakeOverScreen(String doNot) throws Exception {
        if (doNot == null) {
            assertThat("Take over screen not shown",
                    context.getPagesCollection().getPage(TakeOverPage.class).isTakeOverScreenVisible());
        } else {
            assertThat("Take over screen shown",
                    context.getPagesCollection().getPage(TakeOverPage.class).isTakeOverScreenNotVisible());
        }
    }
}
