package com.wearezeta.auto.web.steps;

import java.io.RandomAccessFile;

import com.wearezeta.auto.common.email.AccountDeletionMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Message;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.AccountPage;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.external.DeleteAccountPage;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.logging.LogEntry;

public class CommonWebAppSteps {

    private static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class.getSimpleName());
    private static final int DELETION_RECEIVING_TIMEOUT = 120;
    private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";
    private static final String VIDEO_MESSAGE_IMAGE = "example.png";

    private final TestContext context;
    private String rememberedPage = null;
    private String rememberedMessageId = null;

    public CommonWebAppSteps(TestContext context) {
        this.context = context;
    }

    @Given("^My browser( does not)? support[s]? calling$")
    public void MyBrowserSupportsCalling(String doesNot) throws Exception {
        if (doesNot == null) {
            // should support calling
            if (!WebAppExecutionContext.getBrowser().isSupportingCalls()) {
                throw new PendingException("Browser "
                        + WebAppExecutionContext.getBrowser().toString()
                        + " does not support calling.");
            }
        } else // should not support calling
        {
            if (WebAppExecutionContext.getBrowser().isSupportingCalls()) {
                throw new PendingException(
                        "Browser "
                        + WebAppExecutionContext.getBrowser()
                                .toString()
                        + " does support calling but this test is just for browsers without support.");
            }
        }
    }

    @Given("^I switch language to (.*)$")
    public void ISwitchLanguageTo(String language) throws Exception {
        context.getPagesCollection().getPage(WebPage.class).switchLanguage(language);
    }

    @Then("^I see a string (.*) on the page$")
    public void ISeeAStringOnPage(String string) throws Throwable {
        assertThat(context.getPagesCollection().getPage(WebPage.class).getText(), containsString(string));
    }

    @Then("^I see a placeholder (.*) on the page$")
    public void ISeeAPlaceholderOnPage(String placeholder) throws Throwable {
        assertThat(context.getPagesCollection().getPage(WebPage.class).getPlaceholders(), hasItem(placeholder));
    }

    @Then("^I see a button with (.*) on the page$")
    public void ISeeAButtonOnPage(String value) throws Throwable {
        assertThat(context.getPagesCollection().getPage(WebPage.class).getButtonValues(), hasItem(value));
    }

    @Then("^I see a title (.*) on the page$")
    public void ISeeATitleOnPage(String title) throws Exception {
        assertThat("Title on the page is not correct", context.getPagesCollection().getPage(WebPage.class).getPageTitle(),
                equalTo(title));
    }

    @Given("^There is a known user (.*) with email (.*) and password (.*)$")
    public void ThereIsAKnownUser(String name, String email, String password) throws Exception {
        context.getCommonSteps().ThereIsAKnownUser(name, email, password);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(context.getCurrentPlatform(), count,
                myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
        context.getCommonSteps().UsersSetUniqueUsername(myNameAlias);
    }

    @Given("^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
    public void IChangeAccentColor(String userNameAlias, String newColor)
            throws Exception {
        context.getCommonSteps().IChangeUserAccentColor(userNameAlias, newColor);
    }

    @When("^User (\\w+) changes? name to (.*)")
    public void IChangeName(String userNameAlias, String name) throws Exception {
        context.getCommonSteps().IChangeName(userNameAlias, name);
    }

    @When("^User (\\w+) changes? unique username to (.*)")
    public void IChangeUniqueUsername(String userNameAlias, String name) throws Exception {
        context.getCommonSteps().IChangeUniqueUsername(userNameAlias, name);
    }

    @When("^User (\\w+) changes? his unique username to a random value$")
    public void IChangeUniqueUsernameRandom(String userNameAlias) throws Exception {
        String randomUniqueUsername = CommonUtils.generateGUID().replace("-", "").substring(0, 8);
        context.getCommonSteps().IChangeUniqueUsername(userNameAlias, randomUniqueUsername);
    }

    @Given("(.*) (?:has|have) unique usernames?$")
    public void UserHasUniqueUsername(String userNameAliases) throws Exception {
        context.getCommonSteps().UsersSetUniqueUsername(userNameAliases);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$")
    public void ThereAreNUsersWhereXIsMeWithoutAvatar(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(context.getCurrentPlatform(), count,
                myNameAlias);
        context.getCommonSteps().UsersSetUniqueUsername(myNameAlias);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me without unique username$")
    public void ThereAreNUsersWhereXIsMeWithoutUsername(int count, String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(context.getCurrentPlatform(), count,
                myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
    }

    @When("^User (\\w+) changes? avatar picture to (.*)")
    public void IChangeUserAvatarPicture(String userNameAlias, String path)
            throws Exception {
        String avatar = null;
        final String rootPath = "/images/";
        if (path.equals("default")) {
            avatar = DEFAULT_USER_PICTURE;
        } else {
            avatar = rootPath + path;
        }
        URI uri = new URI(CommonWebAppSteps.class.getResource(avatar)
                .toString());
        log.debug("Change avatar of user " + userNameAlias + " to "
                + uri.getPath());
        context.getCommonSteps().IChangeUserAvatarPicture(userNameAlias, uri.getPath());
    }

    @Given("^(\\w+) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias,
            String usersToNameAliases) throws Exception {
        context.getCommonSteps().UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^(\\w+) blocked (\\w+)$")
    public void UserBlocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        context.getCommonSteps().BlockContact(userAsNameAlias, userToBlockNameAlias);
    }

    @Given("^(.*) (?:has|have) group chat (.*) with (.*)")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
            String chatName, String otherParticipantsNameAlises)
            throws Exception {
        context.getCommonSteps().UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAlises);
    }

    @Given("^(.*) removes? (.*) from group conversation (.*)")
    public void UserRemovesContactFromGroup(String userWhoRemoves, String userToRemove, String chatName)
            throws Exception {
        context.getCommonSteps().UserRemovesAnotherUserFromGroupConversation(userWhoRemoves, userToRemove, chatName);
    }

    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
        IChangeUserAvatarPicture(nameAlias, "default");
    }

    @Given("^User (\\w+) is [Mm]e without avatar$")
    public void UserXIsMeWithoutAvatar(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
    }

    @Given("^(.*) sent connection request to (.*)")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
            String usersToNameAliases) throws Throwable {
        context.getCommonSteps().ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
    }

    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        context.startPinging();
        context.getCommonSteps().WaitUntilContactIsFoundInSearchByUniqueUsername(searchByNameAlias, query);
        context.stopPinging();
    }

    @Given("^(\\w+) waits? until (\\d+) people in backend top people results$")
    public void UserWaitsUntilContactExistsInTopPeopleResults(
            String searchByNameAlias, int size) throws Exception {
        context.startPinging();
        context.getCommonSteps().WaitUntilTopPeopleContactsIsFoundInSearch(searchByNameAlias, size);
        context.stopPinging();
    }

    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        context.getCommonSteps().WaitForTime(seconds);
    }

    @When("^(.*) muted conversation with (user|group) (.*) on device (.*)$")
    public void MuteConversationWithUser(String userToNameAlias, String convType, String muteUserNameAlias, String deviceName)
            throws Exception {
        context.getCommonSteps().UserMutesConversation(userToNameAlias, muteUserNameAlias, deviceName + context.getTestname().
                hashCode(), convType.equals("group"));
    }

    @When("^(.*) archived conversation with (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias,
            String archivedUserNameAlias) throws Exception {
        context.getCommonSteps().ArchiveConversationWithUser(userToNameAlias,
                archivedUserNameAlias);
    }

    @When("^User (.*) pinged in the conversation with (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        context.getCommonSteps().UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
    }

    @When("^Contact (.*) sends? message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias,
            String msg, String deviceName, String convoType, String dstConvoName) throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0)
                ? CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            context.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend,
                    deviceName + context.getTestname().hashCode());
        } else {
            context.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend,
                    deviceName + context.getTestname().hashCode());
        }
    }

    @When("^Contact (.*) sends? (\\d+) messages with prefix (.*) via device (.*) to (user|group conversation) (.*)$")
    public void UserSendAmountOfMessages(String msgFromUserNameAlias, int amount, String prefix, String deviceName,
            String convoType, String dstConvoName) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(msgFromUserNameAlias);
        if (convoType.equals("user")) {
            for (int i = 0; i < amount; i++) {
                context.getConversationStates().addMessage(dstConvoName, new Message(prefix + i, user.getId()));
                context.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, prefix + i,
                        deviceName + context.getTestname().hashCode());
            }
        } else {
            for (int i = 0; i < amount; i++) {
                context.getConversationStates().addMessage(dstConvoName, new Message(prefix + i, user.getId()));
                context.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, prefix + i,
                        deviceName + context.getTestname().hashCode());
            }
        }
    }

    @When("^Contact (.*) sends? long message from file \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendLongMessageToConversation(String msgFromUserNameAlias,
            String file, String deviceName, String convoType, String dstConvoName) throws Exception {
        String message = WebCommonUtils.getTextFromFile(file);
        if (convoType.equals("user")) {
            context.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, message, deviceName + context.
                    getTestname().hashCode());
        } else {
            context.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, message,
                    deviceName + context.getTestname().hashCode());
        }
    }

    @When("^I remember current page$")
    public void IRememberCurrentPage() throws Exception {
        WebPage page = context.getPagesCollection().getPage(WebPage.class);
        rememberedPage = page.getCurrentUrl();
    }

    @When("^I navigate to previously remembered page$")
    public void INavigateToPage() throws Exception {
        if (rememberedPage == null) {
            throw new RuntimeException(
                    "No page has been remembered before!");
        }

        WebPage page = context.getPagesCollection().getPage(WebPage.class);
        page.setUrl(rememberedPage);
        page.navigateTo();
    }

    @When("^User (.*) sends image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
            String imageFileName, String conversationType,
            String dstConversationName) throws Exception {
        final String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
        final boolean isGroup = conversationType.equals("group");
        context.getCommonSteps().UserSentImageToConversationOtr(imageSenderUserNameAlias, imagePath, dstConversationName,
                isGroup);
    }

    @When("^I break the session with device (.*) of user (.*)$")
    public void IBreakTheSession(String deviceName, String userAlias) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(userAlias);
        String deviceId = context.getDeviceManager().getDeviceId(user, deviceName + context.getTestname().hashCode());
        deviceId = WebCommonUtils.removeDeviceIdPadding(deviceId);
        context.getPagesCollection().getPage(WebPage.class).breakSession(deviceId);
    }

    @When("^(.*) sends? (.*) sized file with name (.*) via device (.*) to (user|group conversation) (.*)$")
    public void IXSizedSendFile(String contact, String size, String fileName, String deviceName, String convoType,
            String dstConvoName) throws Exception {
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
        boolean isGroup = convoType.equals("user") ? false : true;
        context.getCommonSteps().UserSentFileToConversation(contact, dstConvoName, path + "/" + fileName, "plain/text",
                deviceName + context.getTestname().hashCode(), isGroup);
    }

    @When("^(.*) sends? audio file (.*) via device (.*) to (user|group conversation) (.*)$")
    public void ISendAudioFile(String contact, String fileName, String deviceName, String convoType, String dstConvoName) throws
            Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();
        path = path.replace("%40", "@");
        boolean isGroup = !convoType.equals("user");
        context.getCommonSteps().UserSentFileToConversation(contact, dstConvoName, path + "/" + fileName, "audio/mp4",
                deviceName + context.getTestname().hashCode(), isGroup);
    }

    @When("^(.*) sends? (.*) sized video with name (.*) via device (.*) to (user|group conversation) (.*)$")
    public void ISendVideo(String contact, String size, String fileName, String deviceName, String convoType,
            String dstConvoName) throws Exception {
        String path = WebCommonUtils.class.getResource("/filetransfer/").getPath();

        final String picturePath = WebCommonUtils.getFullPicturePath(VIDEO_MESSAGE_IMAGE);
        CommonUtils.generateVideoFile(path + "/" + fileName, size, picturePath);
        boolean isGroup = !convoType.equals("user");
        context.getCommonSteps().UserSentFileToConversation(contact, dstConvoName, path + "/" + fileName, "video/mp4",
                deviceName + context.getTestname().hashCode(), isGroup);
    }

    @When("^Contact (.*) sends? unencrypted message (.*) to (user|group conversation) (.*)")
    public void UserSentMessageToConversation(String userFromNameAlias, String message, String convoType,
            String dstConvoName) throws Exception {
        if (convoType.equals("user")) {
            context.getCommonSteps().UserSentMessageToUser(userFromNameAlias, dstConvoName, message);
        } else {
            context.getCommonSteps().UserSentMessageToConversation(userFromNameAlias, dstConvoName, message);
        }
    }

    @When("^User (.*) sends? location (.*) with ([-+]?[0-9]*\\.?[0-9]+) and ([-+]?[0-9]*\\.?[0-9]+) to (user|group conversation) (.*) via device (.*)")
    public void UserSentLocationToConversation(String userFromNameAlias,
            String locationName, String longitude, String latitude, String convoType, String conversationName, String deviceName)
            throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        float longitudeFloat = Float.parseFloat(longitude);
        float latitudeFloat = Float.parseFloat(latitude);
        int zoom = 14;
        context.getCommonSteps().UserSentLocationToConversation(userFromNameAlias, deviceName + context.getTestname().
                hashCode(), conversationName, longitudeFloat, latitudeFloat, locationName, zoom, isGroup);
    }

    @When("^User (.*) deletes? the recent (\\d+) messages? (everywhere )?in (group|single) conversation (.*) via device (.*)$")
    public void UserXDeleteLastMessage(String userNameAlias, int amount, String deleteEverywhere, String convoType,
            String dstNameAlias, String deviceName)
            throws Exception {
        boolean isGroup = convoType.equals("group");
        boolean isDeleteEverywhere = deleteEverywhere != null;
        for (int deleteCounter = 0; deleteCounter < amount; deleteCounter++) {
            context.getCommonSteps().UserDeleteLatestMessage(userNameAlias, dstNameAlias, deviceName + context.getTestname().
                    hashCode(), isGroup, isDeleteEverywhere);
        }
    }

    @When("^User (.*) has seen recent (\\d+) ephemeral messages? from user (.*) via device (.*)$")
    public void UserXSeenRecentEphemeralMessage(String deviceUserNameAlias, int amount, String messageNameAlias,
            String deviceName)
            throws Exception {
        for (int deleteCounter = 0; deleteCounter < amount; deleteCounter++) {
            context.getCommonSteps().UserDeleteLatestMessage(deviceUserNameAlias, messageNameAlias, deviceName + context.
                    getTestname().
                    hashCode(), false, true);
        }
    }

    @When("^User (.*) edits? the recent message to \"(.*)\" from (user|group conversation) (.*) via device (.*)$")
    public void UserXEditLastMessage(String userNameAlias, String newMessage, String convoType,
            String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        context.getCommonSteps().UserUpdateLatestMessage(userNameAlias, dstNameAlias, newMessage, deviceName + context.
                getTestname().
                hashCode(), isGroup);
    }

    @When("^User (.*) edits? the second last message to \"(.*)\" from (user|group conversation) (.*) via device (.*)$")
    public void UserXEditSecondLastMessage(String userNameAlias, String newMessage, String convoType,
            String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        context.getCommonSteps().UserUpdateSecondLastMessage(userNameAlias, dstNameAlias, newMessage, deviceName + context.
                getTestname().
                hashCode(), isGroup);
    }

    @When("^I remember the message (.*)")
    public void IRememberTheMessage(String text) throws Exception {
        rememberedMessageId = context.getPagesCollection().getPage(ConversationPage.class).getMessageIdFromMessageText(text);
    }

    @When("^User (.*) edits? the remembered message to \"(.*)\" on device (.*)$")
    public void UserXEditRememberedMessage(String userNameAlias, String newMessage, String deviceName) throws Exception {
        if (rememberedMessageId == null) {
            throw new PendingException("No remembered message found. Please run the step first.");
        }
        context.getCommonSteps().UserUpdateMessageById(userNameAlias, rememberedMessageId, newMessage,
                deviceName + context.getTestname().hashCode());
    }

    @When("^User (.*) (likes|unlikes) the recent message from (?:user|group conversation) (.*) via device (.*)$")
    public void UserReactLastMessage(String userNameAlias, String reactionType, String dstNameAlias, String deviceName)
            throws Exception {
        switch (reactionType.toLowerCase()) {
            case "likes":
                context.getCommonSteps().UserLikeLatestMessage(userNameAlias, dstNameAlias, deviceName + context.getTestname().
                        hashCode());
                break;
            case "unlikes":
                context.getCommonSteps().UserUnlikeLatestMessage(userNameAlias, dstNameAlias,
                        deviceName + context.getTestname().
                                hashCode());
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the reaction type '%s'", reactionType));
        }
    }

    @When("^User (.*) reads the recent message from (user|group conversation) (.*) via device (.*)")
    public void UserReadsLastMessage(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        context.getCommonSteps().UserReadLastEphemeralMessage(userNameAlias, dstNameAlias,
                deviceName + context.getTestname().hashCode(), isGroup);
    }

    @When("^User (.*) reads the second last message from (user|group conversation) (.*) via device (.*)")
    public void UserReadsSecondLastMessage(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        context.getCommonSteps().UserReadSecondLastEphemeralMessage(userNameAlias, dstNameAlias,
                deviceName + context.getTestname().hashCode(), isGroup);
    }

    @When("^User (.*) switches (user|group conversation) (.*) to ephemeral mode (?:via device (.*)\\s)?with "
            + "(\\d+) (seconds?|minutes?) timeout$")
    public void UserSwitchesToEphemeralMode(String userAs, String isGroup, String convoName, String deviceName, int timeout,
            String timeMetrics) throws Exception {
        final long timeoutMs = timeMetrics.startsWith("minute") ? timeout * 60 * 1000 : timeout * 1000;
        context.getCommonSteps().UserSwitchesToEphemeralMode(userAs, convoName, timeoutMs, isGroup.equals("group conversation"),
                deviceName + context.getTestname().hashCode());
    }

    @When("^(.*) sends personal invitation to mail (.*) with message (.*)$")
    public void UserXSendsPersonalInvitation(String userToNameAlias,
            String toMail, String message) throws Exception {
        context.getCommonSteps().UserXSendsPersonalInvitationWithMessageToUserWithMail(
                userToNameAlias, toMail, message);
    }

    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Throwable {
        context.startPinging();
        final ClientUser user = context.getUserManager().findUserByNameOrNameAlias(alias);
        assertTrue(
                String.format("Invitation email for %s is not valid", user.getEmail()),
                BackendAPIWrappers
                        .getInvitationMessage(user)
                        .orElseThrow(
                                () -> {
                                    throw new IllegalStateException(
                                            "Invitation message has not been received");
                                }).isValid());
        context.stopPinging();
    }

    @Then("^I delete account of user (.*) via email$")
    public void IDeleteAccountViaEmail(String alias) throws Throwable {
        final ClientUser user = context.getUserManager().findUserByNameOrNameAlias(alias);
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, user.getEmail());
        expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
        AccountDeletionMessage message = new AccountDeletionMessage(mbox.getMessage(expectedHeaders,
                DELETION_RECEIVING_TIMEOUT, 0).get());
        final String url = message.extractAccountDeletionLink();
        log.info("URL: " + url);
        DeleteAccountPage deleteAccountPage = context.getPagesCollection().getPage(DeleteAccountPage.class);
        deleteAccountPage.setUrl(url);
        deleteAccountPage.navigateTo();
        deleteAccountPage.clickDeleteAccountButton();
        assertTrue("Delete account page does not show success message", deleteAccountPage.isSuccess());
    }

    @Then("^(.*) navigates to personal invitation registration page$")
    public void INavigateToPersonalInvitationRegistrationPage(String alias) throws Throwable {
        final ClientUser user = context.getUserManager().findUserByNameOrNameAlias(alias);
        String url = BackendAPIWrappers
                .getInvitationMessage(user)
                .orElseThrow(
                        () -> {
                            throw new IllegalStateException(
                                    "Invitation message has not been received");
                        }).extractInvitationLink();
        log.info("Personal invitation url: " + url);
        RegistrationPage registrationPage = context.getPagesCollection().getPage(RegistrationPage.class);
        registrationPage.setUrl(url);
        registrationPage.navigateTo();
        // workaround when using dev (backend is sending mail with account page that redirects to staging webapp)
        final String webapp = CommonUtils.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
        final String backend = CommonUtils.getBackendType(CommonWebAppSteps.class);
        if (registrationPage.getCurrentUrl().contains("staging") && webapp.contains("dev") && backend.equals("staging")) {
            url = registrationPage.getCurrentUrl().replace("staging", "dev");
            registrationPage.setUrl(url);
            registrationPage.navigateTo();
        }
    }

    @Given("^User (.*) added contacts? (.*) to group chat (.*)")
    public void UserXAddedContactsToGroupChat(String asUser, String contacts,
            String conversationName) throws Exception {
        context.getCommonSteps().UserXAddedContactsToGroupChat(asUser, contacts,
                conversationName);
    }

    @Given("^There are suggestions for user (.*) on backend$")
    public void suggestions(String userNameAlias) throws Exception {
        // TODO implement
        throw new UnsupportedOperationException("Not implemented");
//        context.getCommonSteps().WaitUntilSuggestionFound(userNameAlias);
    }

    @Given("^User (.*) has contacts? (.*) in address book")
    public void UserXHasContactsInAddressBook(String asUser, String emails)
            throws Exception {
        context.getCommonSteps().UserXHasContactsInAddressBook(asUser, emails);
    }

    @Given("(.*) takes? snapshot of current profile picture$")
    public void UserXTakesSnapshotOfProfilePicture(String asUser)
            throws Exception {
        context.getCommonSteps().UserXTakesSnapshotOfProfilePicture(asUser);
    }

    @Then("^I verify that current profile picture snapshot of (.*) differs? from the previous one$")
    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(String userNameAlias) throws Exception {
        context.getCommonSteps().UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias);
    }

    @When("user (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias,
            String deviceName, String label) throws Exception {
        context.startPinging();
        context.getCommonSteps().UserAddsRemoteDeviceToAccount(userNameAlias, deviceName + context.getTestname().hashCode(),
                label);
        context.stopPinging();
    }

    @Given("^My browser supports synthetic drag and drop$")
    public void MyBrowserSupportsSyntheticDragDrop() {
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingSyntheticDragAndDrop()) {
            throw new PendingException();
        }
    }

    @Then("^I verify browser log does not have errors$")
    public void VerifyBrowserLogIsEmpty() throws Exception {
        try {
            if (WebAppExecutionContext.getBrowser()
                    .isSupportingConsoleLogManagement()) {
                List<LogEntry> browserLog = context.getLogManager().getBrowserLog();

                StringBuilder bLog = new StringBuilder();
                browserLog = browserLog.stream()
                        .filter((entry)
                                -> entry.getLevel().intValue() >= Level.SEVERE.intValue())
                        // filter auto login attempts
                        .filter((entry)
                                -> !entry.getMessage().contains("/access"))
                        .filter((entry)
                                -> !entry.getMessage().contains("/self"))
                        .filter((entry)
                                -> !entry.getMessage().contains("attempt"))
                        // filter encryption precondition
                        .filter((entry)
                                -> !entry.getMessage().contains("412 (Precondition Failed)"))
                        .collect(Collectors.toList());

                browserLog.forEach((entry) -> {
                    bLog.append(entry.getLevel()).append(":")
                            .append(entry.getMessage())
                            .append("\n");
                });

                assertTrue("BrowserLog does have errors: \n" + bLog.toString(),
                        browserLog.isEmpty());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Then("^I refresh page$")
    public void IRefreshPage() throws Exception {
        context.getPagesCollection().getPage(RegistrationPage.class)
                .refreshPage();
    }

    @Given("^(\\w+) unblocks user (\\w+)$")
    public void UserUnblocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        context.getCommonSteps().UnblockContact(userAsNameAlias, userToBlockNameAlias);
    }

    @Given("^I open Sign In page$")
    public void IOpenSignInPage() throws Exception {
        context.getPagesCollection().getPage(RegistrationPage.class)
                .openSignInPage();
    }

    @Given("^User (.*) removes all his registered OTR clients$")
    public void UserRemovesAllRegisteredOtrClients(String userAs) throws Exception {
        context.getCommonSteps().UserRemovesAllRegisteredOtrClients(userAs);
    }

    @Given("^User (.*) only keeps his (\\d+) most recent OTR clients$")
    public void UserKeepsXOtrClients(String userAs, int clientsCount) throws Exception {
        context.getCommonSteps().UserKeepsXOtrClients(userAs, clientsCount);
    }

    @Given("^User (.*) updates? the unique user name to \"(.*)\"(?: via device (.*))?")
    public void UserXUpdateUniqueUserName(String userNameAlias, String uniqueUserName, String deviceName) throws Exception {
        context.getCommonSteps().UpdateUniqueUsername(userNameAlias, uniqueUserName, deviceName);
    }

    @Given("^User (.*) updates? the unique user name to the one started with \"(.*)\"(?: via device (.*))?")
    public void UserXUpdateUniqueUserNameAndRandom(String userNameAlias, String uniqueUserName, String deviceName) throws Exception {
        Random rand = new Random();
        String uniqUserName = uniqueUserName + rand.nextInt(5);
        context.getCommonSteps().UpdateUniqueUsername(userNameAlias, uniqUserName, deviceName);
    }
}
