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
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.Message;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.commons.lang3.ObjectUtils;
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
        assertThat("Message in input field", context.getPagesCollection().getPage(ConversationPage.class).
                getMessageFromInputField(),
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
            if (WebAppExecutionContext.getBrowser().isSupportingKeys()) {
                message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
            } else {
                message = message + "\n";
            }
        }
        context.getPagesCollection().getPage(ConversationPage.class).writeNewMessage(message);
    }

    /**
     * Submits entered message for sending
     *
     * @step. ^I send message$
     */
    @When("^I send message$")
    public void ISendMessage() throws Exception {
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
     * Verifies whether soundcloud, youtube, vimeo or spotify message is embedded
     *
     * @throws Exception
     * @param typeOfMessage soundcloud| youtube| vimeo| spotify
     * @param url link of soundcloud| youtube| vimeo| spotify message
     *
     * @step. ^I (do not )?see embedded( soundcloud| youtube| vimeo| spotify)? message (.*)
     */
    @Then("^I (do not )?see embedded( soundcloud| youtube| vimeo| spotify)? message (.*)")
    public void ISeeEmbeddedMessage(String doNot, String typeOfMessage, String url) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isMessageEmbedded(true, typeOfMessage, url));
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isMessageEmbedded(false, typeOfMessage, url));
        }
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

    @And("^I see titlebar with (.*)$")
    public void ISeeTitlebar(String conversationName) throws Throwable {
        assertThat("Wrong titlebar label", context.getPagesCollection().getPage(ConversationPage.class).getTitlebarLabel(),
                equalTo(conversationName.toUpperCase()));
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

    @Then("^I do not see any picture in the conversation view$")
    public void ISeeSentPicture() throws Exception {
        assertThat("I see a picture in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .isImageInvisible());
    }

    /**
     * Verifies that only x images are in the conversation. Helps with checking for doubles.
     *
     * @param x the amount of images
     * @step. ^I see only (\\d+) picture[s]? in the conversation$
     */
    @Then("^I see only (\\d+) pictures? in the conversation$")
    public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
        assertThat("Number of images in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfImagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Verifies that x messages are in the conversation
     *
     * @param x the amount of sent messages
     * @step. ^I see (\\d+) messages in conversation$
     */
    @Then("^I see (\\d+) messages? in conversation$")
    public void ISeeXMessagesInConversation(int x) throws Exception {
        assertThat("Number of messages in the conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .getNumberOfMessagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Verifies that x deleted messages are in the conversation
     *
     * @param x the amount of deleted messages
     * @step. ^I see (\\d+) deleted messages in conversation$
     */
    @Then("^I see (\\d+) deleted messages? in conversation$")
    public void ISeeXDeletedMessagesInConversation(int x) throws Exception {
        assertThat("Number of deleted messages in the conversation", context.getPagesCollection().
                getPage(ConversationPage.class)
                .getNumberOfDeletedMessagesInCurrentConversation(), equalTo(x));
    }

    /**
     * Verifies if file transfer button is shown in the cursor
     *
     * @throws Exception
     * @step. ^I see file transfer button in conversation input$
     */
    @Then("^I see file transfer button in conversation input$")
    public void ISeeFileButton() throws Exception {
        assertThat("No button found", context.getPagesCollection().getPage(ConversationPage.class).isFileButtonVisible());
    }

    /**
     * Verifies if buttons are invisible and input is not usable
     *
     * @throws Exception
     * @step. ^I verify that conversation input and buttons are not visible$
     */
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

    /**
     * Send a file into current conversation
     *
     * @param fileName the name of a file.
     * @throws Exception
     * @step. ^I send picture (.*) to the current conversation$
     */
    @When("^I send file (.*) to the current conversation$")
    public void WhenISendFile(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    /**
     * Generate and send a file with a specific size into current conversation
     *
     * @param size the size of a file.
     * @param fileName the name of the file
     * @throws Exception
     */
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

    /**
     * Generate and send a video with a specific size into current conversation
     *
     * @param size the size of the video file.
     * @param fileName the name of the file
     * @throws Exception
     */
    @When("^I send (.*) sized video with name (.*) to the current conversation$")
    public void WhenIXSizedSendVideo(String size, String fileName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");
        final String picturePath = WebCommonUtils.getFullPicturePath(VIDEO_MESSAGE_IMAGE);
        CommonUtils.generateVideoFile(path + "/" + fileName, size, picturePath);
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    /**
     * Generate and send a audio file with a specific size into current conversation
     *
     * @param length the length in format 00:00 (minutes:seconds) of the audio file.
     * @param fileName the name of the file
     * @throws Exception
     */
    @When("^I send audio file with length (.*) and name (.*) to the current conversation$")
    public void WhenIXSizedSendAudio(String length, String fileName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");

        CommonUtils.generateAudioFile(path + "/" + fileName, length);
        context.getPagesCollection().getPage(ConversationPage.class).sendFile(fileName);
    }

    /**
     * Verifies if the file transfer placeholder contains correct file name
     *
     * @param fileName the name of a file
     * @throws Exception
     * @step. ^I see file transfer for file (.*) in the conversation view$
     */
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

    /**
     * Cancel file upload
     *
     * @param fileName the name of a file
     * @throws Exception
     * @step. ^I send picture (.*) to the current conversation$
     */
    @When("^I cancel file upload of file (.*)$")
    public void WhenICancelFileUpload(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).cancelFileUpload(fileName);
    }

    /**
     * Cancel video upload
     *
     * @param fileName the name of a file
     * @throws Exception
     */
    @When("^I cancel video upload of video (.*)$")
    public void ICancelVideoUpload(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).cancelVideoUpload(fileName);
    }

    /**
     * Verifies if the file transfer placeholder contains correct file name
     *
     * @param count the name of a file
     * @throws Exception
     * @step. ^I see file transfer for file (.*) in the conversation view$
     */
    @Then("^I see (//d+) file transfers in the conversation view$")
    public void ISeeFileTransfers(String count) throws Exception {
        // TODO
    }

    /**
     * Verifies if the file transfer placeholder contains correct file icon
     *
     * @param fileName the name of a file
     * @throws Exception
     * @step. ^I verify icon of file (.*) in the conversation view$
     */
    @Then("^I verify icon of file (.*) in the conversation view$")
    public void IVerifyIconOfFile(String fileName) throws Exception {
        assertThat("No file icon for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileIcon(fileName));
    }

    /**
     * Verifies if the file transfer placeholder contains correct file size
     *
     * @param fileName the name of a file
     * @throws Exception
     * @step. ^I verify size of file (.*) in the conversation view$
     */
    @Then("^I verify size of file (.*) is (.*) in the conversation view$")
    public void IVerifySizeOfFile(String fileName, String size) throws Exception {
        assertThat("Wrong file size for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileSizeOf(fileName), equalTo(size));
    }

    /**
     * Verifies if the file transfer placeholder contains correct file status
     *
     * @param fileName the name of a file
     * @param status the status of the transfer
     * @throws Exception
     * @step. ^I verify status of file (.*) is (.*) in the conversation view$
     */
    @Then("^I verify status of file (.*) is (.*) in the conversation view$")
    public void IVerifyStatusOfFile(String fileName, String status) throws Exception {
        assertThat("Wrong file status for " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
                .getFileStatusOf(fileName), equalTo(status));
    }

    /**
     * Verifies if the file transfer placeholder contains correct file status only if the file status is shown at all. This is
     * helpful in cases of UPLOADING... and DOWNLOADING... status.
     *
     * @param fileName the name of a file
     * @param status the status of the transfer
     * @throws Exception
     * @step. ^I verify status of file (.*) is (.*) in the conversation view$
     */
    @Then("^I verify status of file (.*) is (.*) in the conversation view if possible$")
    public void IVerifyStatusOfFileIfPossible(String fileName, String status) throws Exception {
        Optional<String> optionalStatus = context.getPagesCollection().getPage(ConversationPage.class)
                .getOptionalFileStatusOf(fileName);
        if (optionalStatus.isPresent()) {
            assertThat("Wrong file status for " + fileName, optionalStatus.get(), equalTo(status));
        }
    }

    /**
     * Verifies if the file transfer placeholder contains correct file type
     *
     * @param fileName the name of a file
     * @param type the type of the file
     * @throws Exception
     * @step. ^I verify status of file (.*) is (.*) in the conversation view$
     */
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

    /**
     * Clicks on download button to download certain file
     *
     * @param fileName the name of a file
     * @throws Exception
     * @step. ^II click icon to download file (.*) in the conversation view$
     */
    @Then("^I click icon to download file (.*) in the conversation view$")
    public void IDownloadFile(String fileName) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickFileIcon(fileName);
    }

    @Then("^I wait until video (.*) is uploaded completely$")
    public void IWaitUntilVideoIsUploaded(String fileName) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingInlineVideo()) {
            assertThat("Upload still not finished for video " + fileName, context.getPagesCollection().getPage(
                    ConversationPage.class).waitUntilVideoUploaded(fileName));
        } else {
            assertThat("Upload still not finished for file " + fileName, context.getPagesCollection().getPage(
                    ConversationPage.class).waitUntilFileUploaded(fileName));
        }
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
        assertThat("Upload still not finished for audio " + fileName, context.getPagesCollection().getPage(ConversationPage.class)
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
        int messageId = context.getPagesCollection().getPage(ConversationPage.class).getXLastMessageIndex(indexNumber);
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

    @When("^I click to delete the latest message$")
    public void IClickToDelete() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickToDeleteLastMessage();
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
                assertTrue("Like symbol is not visible", context.getPagesCollection().getPage(ConversationPage.class).isLikeSymbolVisibleForLastMessage());
            } else {
                assertTrue("Like symbol is visible", context.getPagesCollection().getPage(ConversationPage.class).isLikeSymbolInvisibleForLastMessage());
            }
        } else {
            if (doNot == null) {
                assertTrue("Like symbol is not visible", context.getPagesCollection().getPage(ConversationPage.class).isLikeSymbolVisibleForSecondLastMessage());
            } else {
                assertTrue("Like symbol is visible", context.getPagesCollection().getPage(ConversationPage.class).isLikeSymbolInvisibleForSecondLastMessage());
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

    @When("^I click (like|unlike) button in context menu for latest message$")
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
        int indexNummer = context.getPagesCollection().getPage(ConversationPage.class).getXLastMessageIndex(index);
        if (isWithout) {
            if (isLike) {
                context.getPagesCollection().getPage(ConversationPage.class).clickLikeMessageWithoutOtherLikes(indexNummer);
            } else {
                context.getPagesCollection().getPage(ConversationPage.class).clickUnlikeMessageWithoutOtherLikes(indexNummer);
                }
        } else if (isLike) {
                context.getPagesCollection().getPage(ConversationPage.class).clickLikeMessageWithOtherLikes(indexNummer);
            }
        else {
                context.getPagesCollection().getPage(ConversationPage.class).clickUnlikeMessageWithOtherLikes(indexNummer);
        }
    }

    @Then("^I (do not )?see likes below the last message$")
    public void ISeeLikesForLatestMessage(String not) throws Exception {
        if (not == null) {
            assertTrue("Likes of others are NOT visible for last message", context.getPagesCollection().getPage(
                    ConversationPage.class).isLikeLineVisibleForLastMessage());
        } else {
            assertTrue("Likes of others are visible for last message", context.getPagesCollection().getPage(ConversationPage.class)
                    .isLikeLineInvisibleForLastMessage());
        }
    }

    @Then("^I see the (third |second )?last message is only liked by me$")
    public void ISeeLatestMessageIsOnlyLikedByMe(String indexValue) throws Exception {
        int messageIndex = context.getPagesCollection().getPage(ConversationPage.class).getXLastMessageIndex(indexValue);
        assertTrue("The " + indexValue + "last message is NOT only liked by you", context.getPagesCollection().getPage(ConversationPage.class)
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

    @Then("^I see the last message is liked by (.*)$")
    public void ISeeLastMessageIsLikedBy(String usersToNameAliases) throws Exception {
        List<String> likers = context.getPagesCollection().getPage(ConversationPage.class).getUsersThatLikeTheLastMessage();
        List<String> aliases = CommonSteps.splitAliases(usersToNameAliases);
        String[] users = new String[aliases.size()];
        for(int i = 0; i < aliases.size(); i++) {
            ClientUser userTo = context.getUserManager().findUserByNameOrNameAlias(aliases.get(i));
            users[i] = userTo.getName();
        }
        assertThat("User not found in like message", likers, hasItems(users));
        assertThat("Wrong number of likes", likers, hasSize(users.length));
    }

    @When("^I click reset session on the latest decryption error")
    public void IClickToResetSession() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickToResetSessionOnLatestError();
    }

    @When("^I close reset session dialog$")
    public void IClickConfirmToResetSession() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).setCloseResetSessionDialog();
    }

    /**
     * Verifies whether people button tool tip is correct or not.
     *
     * @step. ^I see correct people button tool tip$
     */
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
        assertThat(message + " action", context.getPagesCollection().getPage(ConversationPage.class)
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
        contacts = context.getUserManager().replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
        Set<String> parts = new HashSet<String>();
        parts.add(message);
        parts.addAll(CommonSteps.splitAliases(contacts));
        assertThat(message + " action for " + contacts, context.getPagesCollection().getPage(ConversationPage.class)
                .waitForNumberOfMessageHeadersContain(parts), equalTo(times));
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
        context.getPagesCollection().getPage(ConversationPage.class).waitForPresentMessageContains(message);
    }
    
    /**
     * Verify latest text message is visible in conversation.
     *
     * @param message
     * @throws Exception
     * @step. ^I see text message (.*)
     */
    @Then("^I see latest text message (.*)")
    public void ISeeLatestTextMessage(String message) throws Exception {
        assertTrue(String.format("Last message is NOT visible with text '%s'", message), 
                context.getPagesCollection().getPage(ConversationPage.class).isLatestMessageWithTextVisible(message));
    }

    /**
     * Verify a text message is visible in conversation.
     *
     * @param message
     * @throws Exception
     * @step. ^I really see text message (.*)
     */
    @Then("^I really see text message (.*)")
    public void ISeeTextMessageInViewPort(String message) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForDisplayedMessageContains(message, 30);
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
        assertThat(context.getPagesCollection().getPage(ConversationPage.class).getSecondLastTextMessage(), equalTo(
                expectedMessage));
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
        assertTrue("Saw text message " + message, context.getPagesCollection().getPage(ConversationPage.class)
                .isTextMessageInvisible(message == null ? "" : message));
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
     * @step. ^I( do not)? see call button$
     */
    @Then("^I( do not)? see call button$")
    public void ISeeCallButton(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonVisible());
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isCallButtonInvisible());
        }
    }

    /**
     * Verifies whether video calling button is visible or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     * @throws java.lang.Exception
     * @step. ^I( do not)? see video call button$
     */
    @Then("^I( do not)? see video call button$")
    public void ISeeVideoCallButton(String doNot) throws Exception {
        if (doNot == null) {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isVideoCallButtonVisible());
        } else {
            assertTrue(context.getPagesCollection().getPage(ConversationPage.class).isVideoCallButtonInvisible());
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
            assertTrue(conversationPage.isPictureInModalDialog());
            assertTrue(conversationPage.isPictureInFullscreen());
            assertThat("Overlap score of image comparsion", conversationPage.getOverlapScoreOfFullscreenImage(pictureName),
                    greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
        } else {
            assertTrue(conversationPage.isPictureNotInModalDialog());
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
        assertThat("No image foud in conversation", context.getPagesCollection().getPage(ConversationPage.class)
                .isImageMessageFound());
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
     * Verifies whether call button tool tip is correct or not.
     *
     * @step. ^I see correct call button tool tip$
     */
    @Then("^I see correct call button tooltip$")
    public void ISeeCorrectCallButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_CALL;
        assertThat("Call button tooltip", context.getPagesCollection().getPage(ConversationPage.class).getCallButtonToolTip(),
                equalTo(tooltip));
    }

    /**
     * Verifies whether video call button tool tip is correct or not.
     *
     * @step. ^I see correct call button tool tip$
     */
    @Then("^I see correct video call button tooltip$")
    public void ISeeCorrectVideoCallButtonTooltip() throws Exception {
        String tooltip = TOOLTIP_VIDEO_CALL;
        assertThat("Video Call button tooltip", context.getPagesCollection().getPage(ConversationPage.class).
                getVideoCallButtonToolTip(),
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

    /**
     * Verifies all remembered messages are present in the given conversation
     *
     * @step. I verify all remembered messages are present in conversation (.*)
     */
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

    /**
     * Clicks OK on long message warning dialog
     *
     * @step. "^I click OK on long message warning dialog$"
     */
    @When("^I click OK on long message warning dialog$")
    public void IClickOKOnLongMessageWarning() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickOKButtonOnLongMWarning();
    }

    /**
     * Clicks X on long message warning dialog
     *
     * @step. "^I click X button on long message warning dialog$"
     */
    @When("^I click X button on long message warning dialog$")
    public void IClickXOnLongMessageWarning() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButtonOnLongMWarning();
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
            context.getPagesCollection().getPage(ConversationPage.class).clearConversationInput();
            i--;
        }
    }

    /**
     * Presses Up Arrow to edit message
     *
     * @step. "^I press Up Arrow to edit message$"
     */
    @When("^I press Up Arrow to edit message$")
    public void IPressUpArrow() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).pressUpArrow();
    }

    /**
     * Verifies whether location message is shown in the conversation view or not.
     *
     * @param doNot is set to null if "do not" part does not exist
     * @param locationName name of the shared location
     * @param longitude longitude of the shared location, float
     * @param latitude latitude of the shared location, float
     * @throws java.lang.Exception
     * @step. ^I (do not )?see location message (.*) with ([-+]?[0-9]*\.?[0-9]+) and ([-+]?[0-9]*\.?[0-9]+) in the conversation
     * view$
     */
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

    /**
     * Cancel video download
     *
     * @param fileName the name of a video file
     * @throws Exception
     */
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

    /**
     * Verifies whether link title is shown in the conversation view or not.
     *
     * @param linkTitle title of the link in link preview
     * @param doNot is set to null if "do not" part does not exist
     * @throws java.lang.Exception
     * @step. ^I see a title (.*) in link preview in the conversation view$
     */
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

    /**
     * Verifies whether previously link preview contains picture in the conversation view
     *
     * @param pictureName the name of a picture file. This file should already exist in the ~/Documents folder
     * @throws Exception
     * @step. ^I see a picture (.*) from link preview$
     */
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

    /**
     * Verify a link from link preview is visible in conversation.
     *
     * @param link link in the link preview
     * @throws Exception
     * @step. ^I see link (.*) in link preview message$
     */
    @Then("^I see link (.*) in link preview message")
    public void ISeeLinkInLinkPreview(String link) throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).waitForLinkPreviewContains(link);
    }
    
    /**
     * Verify latest message is a link preview.
     *
     * @param not is set to null if "do not" part does not exist
     * @throws Exception
     * @step. I (do not )?see latest message is link preview message$
     */
    @Then("^I (do not )?see latest message is link preview message$")
    public void ISeeLatestMessageIsLinkPreview(String not) throws Exception {
        if (not == null) {
            assertTrue("latest message is no link preview", context.getPagesCollection().getPage(ConversationPage.class).
                    isLinkPreviewLinkVisibleForLatestMessage());
        } else {
            assertTrue("latest message is a link preview", context.getPagesCollection().getPage(ConversationPage.class).isLinkPreviewLinkInvisibleForLatestMessage());
        }
    }

    /**
     * Clicks x button to close edit mode
     *
     * @step. "^I click x button to close edit mode$"
     */
    @When("^I click x button to close edit mode$")
    public void IClickXButoonToCancelEdit() throws Exception {
        context.getPagesCollection().getPage(ConversationPage.class).clickXButtonToCloseEdit();
    }
}
