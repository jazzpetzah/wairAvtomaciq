package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.email.AccountDeletionMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Lifecycle;
import static com.wearezeta.auto.web.common.Lifecycle.DRIVER_INIT_TIMEOUT;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.external.DeleteAccountPage;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.logging.LogEntry;

public class CommonWebAppSteps {

    public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
            .getSimpleName());
    
    private static final int DELETION_RECEIVING_TIMEOUT = 120;
    private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";
    
    private final TestContext context;
    
    public CommonWebAppSteps() {
        this.context = new TestContext();
    }
    
    public CommonWebAppSteps(TestContext context) {
        this.context = context;
    }
    
    /**
     * This step will throw special PendingException whether the current browser does support calling or not. This will cause
     * Cucumber interpreter to skip the current test instead of failing it.
     *
     * @param doesNot is set to null if "does not" part does not exist
     * @throws Exception
     * @step. ^My browser( does not)? support[s] calling$
     */
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
        if (WebAppExecutionContext.getBrowser().isSupportingCalls()) {
            throw new PendingException(
                    "Browser "
                    + WebAppExecutionContext.getBrowser()
                    .toString()
                    + " does support calling but this test is just for browsers without support.");
        }
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. Avatar picture for Self user is set
     * automatically
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(context.getCurrentPlatform(), count,
                myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
    }

    /**
     * Changes the accent color settings of the given user
     *
     * @param userNameAlias alias of the user where the accent color will be changed
     * @param newColor one of possible accent colors: StrongBlue|StrongLimeGreen|BrightYellow
     * |VividRed|BrightOrange|SoftPink|Violet
     * @throws Exception
     * @step. ^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow
     * |VividRed|BrightOrange|SoftPink|Violet)$
     */
    @Given("^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
    public void IChangeAccentColor(String userNameAlias, String newColor)
            throws Exception {
        context.getCommonSteps().IChangeUserAccentColor(userNameAlias, newColor);
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. Avatar picture for Self user is NOT set
     * automatically
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$")
    public void ThereAreNUsersWhereXIsMeWithoutAvatar(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMe(context.getCurrentPlatform(), count,
                myNameAlias);
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. The user is registered with a phone
     * number only and has no email address attached
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
            String myNameAlias) throws Exception {
        context.getCommonSteps().ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
    }

    /**
     * Set avatar picture for a particular user
     *
     * @param userNameAlias user name/alias
     * @param path path to a picture on a local file system or 'default' to set the default picture
     * @throws Exception
     * @step. ^User (\\w+) changes? avatar picture to (.*)
     */
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

    /**
     * Creates connection between to users
     *
     * @param userFromNameAlias user which sends connection request
     * @param usersToNameAliases user which accepts connection request
     * @throws Exception
     * @step. ^(\\w+) is connected to (.*)$
     */
    @Given("^(\\w+) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias,
            String usersToNameAliases) throws Exception {
        context.getCommonSteps().UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    /**
     * Blocks a user
     *
     * @param userAsNameAlias user which wants to block another
     * @param userToBlockNameAlias user to block
     * @throws Exception
     * @step. ^(\\w+) blocked (\\w+)$
     */
    @Given("^(\\w+) blocked (\\w+)$")
    public void UserBlocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        context.getCommonSteps().BlockContact(userAsNameAlias, userToBlockNameAlias);
    }

    /**
     * Creates group chat with specified users
     *
     * @param chatOwnerNameAlias user that creates group chat
     * @param chatName group chat name
     * @param otherParticipantsNameAlises list of users which will be added to chat separated by comma
     * @throws Exception
     * @step. ^(.*) (?:has|have) group chat (.*) with (.*)
     */
    @Given("^(.*) (?:has|have) group chat (.*) with (.*)")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
            String chatName, String otherParticipantsNameAlises)
            throws Exception {
        context.getCommonSteps().UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAlises);
    }

    /**
     * Sets self user to be the current user. Avatar picture for this user is set automatically
     *
     * @param nameAlias user to be set as self user
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e$
     */
    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
        IChangeUserAvatarPicture(nameAlias, "default");
    }

    /**
     * Sets self user to be the current user. Avatar picture for this user is NOT set automatically
     *
     * @param nameAlias user to be set as self user
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e without avatar$
     */
    @Given("^User (\\w+) is [Mm]e without avatar$")
    public void UserXIsMeWithoutAvatar(String nameAlias) throws Exception {
        context.getCommonSteps().UserXIsMe(nameAlias);
    }

    /**
     * Sends connection request by one user to another
     *
     * @param userFromNameAlias user that sends connection request
     * @param usersToNameAliases user which receive connection request
     * @throws Exception
     * @step. ^(.*) sent connection request to (.*)
     */
    @Given("^(.*) sent connection request to (.*)")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
            String usersToNameAliases) throws Throwable {
        context.getCommonSteps().ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
    }

    /**
     * Pings BackEnd until user is indexed and avialable in search
     *
     * @param searchByNameAlias user name to search string
     * @param query querry string
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) exists in backend search results$
     */
    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        context.getCommonSteps().WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Pings BackEnd until user is indexed and available in top people
     *
     * @param searchByNameAlias user name to search string
     * @param size number of top people
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) exists in backend search results$
     */
    @Given("^(\\w+) waits? until (\\d+) people in backend top people results$")
    public void UserWaitsUntilContactExistsInTopPeopleResults(
            String searchByNameAlias, int size) throws Exception {
        context.getCommonSteps().WaitUntilTopPeopleContactsIsFoundInSearch(
                searchByNameAlias, size);
    }

    /**
     * Wait for specified amount of seconds
     *
     * @param seconds
     * @throws NumberFormatException
     * @throws InterruptedException
     * @step. ^I wait for (\\d+) seconds?$
     */
    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        context.getCommonSteps().WaitForTime(seconds);
    }

    /**
     * Mute conversation
     *
     * @param userToNameAlias user who want to mute conversation
     * @param muteUserNameAlias conversation or user to be muted
     * @throws Exception
     * @step. ^(.*) muted conversation with (.*)$
     */
    @When("^(.*) muted conversation with (.*)$")
    public void MuteConversationWithUser(String userToNameAlias,
            String muteUserNameAlias) throws Exception {
        context.getCommonSteps()
                .MuteConversationWithUser(userToNameAlias, muteUserNameAlias);
    }

    /**
     * Archive conversation on the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param archivedUserNameAlias the name of conversation to archive
     * @throws Exception
     * @step. ^(.*) archived conversation with (.*)$
     */
    @When("^(.*) archived conversation with (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias,
            String archivedUserNameAlias) throws Exception {
        context.getCommonSteps().ArchiveConversationWithUser(userToNameAlias,
                archivedUserNameAlias);
    }

    /**
     * Send Ping into a conversation using the backend
     *
     * @param pingFromUserNameAlias conversations list owner name/alias
     * @param dstConversationName the name of conversation to send ping to
     * @throws Exception
     * @step. ^User (.*) pinged in the conversation with (.*)$
     */
    @When("^User (.*) pinged in the conversation with (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        context.getCommonSteps().UserPingedConversation(pingFromUserNameAlias,
                dstConversationName);
    }

    /**
     * Send Hotping into a conversation using the backend
     *
     * @param pingFromUserNameAlias conversations list owner name/alias
     * @param dstConversationName the name of conversation to send ping to
     * @throws Exception
     * @step. ^User (.*) pinged twice in the conversation with (.*)$
     */
    @When("^User (.*) pinged twice in the conversation with (.*)$")
    public void UserHotPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        context.getCommonSteps().UserHotPingedConversation(pingFromUserNameAlias,
                dstConversationName);
    }

    /**
     * User A sends a simple text message to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg a message to send. Random string will be sent if it is empty
     * @param dstConvoName The user to receive the message
     * @param isEncrypted whether the message has to be encrypted
     * @param convoType either 'user' or 'group conversation'
     * @throws Exception
     * @step. ^Contact (.*) sends? (encrypted )?message "?(.*?)"?\s?(?:via device (.*)\s)?to (user|group conversation) (.*)$
     */
    @When("^Contact (.*) sends? (encrypted )?message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias, String isEncrypted,
            String msg, String deviceName, String convoType, String dstConvoName) throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0)
                ? CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            if (isEncrypted == null) {
                context.getCommonSteps().UserSentMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                context.getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
            }
        } else if (isEncrypted == null) {
            context.getCommonSteps().UserSentMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend);
        } else {
            context.getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        }
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param isEncrypted whether the image has to be encrypted
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName the file path name of the image to send. The path name is defined relative to the image file defined
     * in Configuration.cnf.
     * @param conversationType "single user" or "group" conversation.
     * @param dstConversationName the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)
     */
    @When("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias, String isEncrypted,
            String imageFileName, String conversationType,
            String dstConversationName) throws Exception {
        final String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
        final boolean isGroup = conversationType.equals("group");
        if (isEncrypted == null) {
            context.getCommonSteps().UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            context.getCommonSteps().UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    /**
     * Send message to a conversation
     *
     * @param userFromNameAlias user who want to mute conversation
     * @param message message to send
     * @param conversationName the name of existing conversation to send the message to
     * @throws Exception
     * @step. ^User (.*) sent message (.*) to conversation (.*)
     */
    @When("^User (.*) sends? message (.*) to conversation (.*)")
    public void UserSentMessageToConversation(String userFromNameAlias,
            String message, String conversationName) throws Exception {
        context.getCommonSteps().UserSentMessageToConversation(userFromNameAlias,
                conversationName, message);
    }

    /**
     * Send personal invitation over the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param toMail the email to send the invitation to
     * @param message the message for the invitee
     * @throws Exception
     * @step. ^(.*) send personal invitation to mail (.*) with name (.*) and message (.*)$
     */
    @When("^(.*) sends personal invitation to mail (.*) with message (.*)$")
    public void UserXSendsPersonalInvitation(String userToNameAlias,
            String toMail, String message) throws Exception {
        context.getCommonSteps().UserXSendsPersonalInvitationWithMessageToUserWithMail(
                userToNameAlias, toMail, message);
    }

    /**
     * Verify that invitation email exists in user's mailbox
     *
     * @param alias user name/alias
     * @throws Throwable
     * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
     */
    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Throwable {
        final String email = context.getUserManager().findUserByNameOrNameAlias(alias).getEmail();
        assertTrue(
                String.format("Invitation email for %s is not valid", email),
                BackendAPIWrappers
                .getInvitationMessage(email)
                .orElseThrow(
                        () -> {
                            throw new IllegalStateException(
                                    "Invitation message has not been received");
                        }).isValid());
    }

    @Then("^I delete account of user (.*) via email$")
    public void IDeleteAccountViaEmail(String alias) throws Throwable {
        final String email = context.getUserManager().findUserByNameOrNameAlias(alias).getEmail();
        IMAPSMailbox mbox = IMAPSMailbox.getInstance();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
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

    /**
     * Navigates to the prefilled personal invitation registration page
     *
     * @param alias user name/alias
     * @throws Throwable
     * @step. ^(.*) navigates to personal invitation registration page$
     */
    @Then("^(.*) navigates to personal invitation registration page$")
    public void INavigateToPersonalInvitationRegistrationPage(String alias)
            throws Throwable {
        final String email = context.getUserManager().findUserByNameOrNameAlias(alias).getEmail();
        String url = BackendAPIWrappers
                .getInvitationMessage(email)
                .orElseThrow(
                        () -> {
                            throw new IllegalStateException(
                                    "Invitation message has not been received");
                        }).extractInvitationLink();
        RegistrationPage registrationPage = context.getPagesCollection().getPage(RegistrationPage.class);
        registrationPage.setUrl(url);
        registrationPage.navigateTo();
    }

    /**
     * Add one or more of your contacts to the existing group conversation on the backend
     *
     * @param asUser user name to add as
     * @param contacts the comma separated list of contacts to add
     * @param conversationName conversation name to add contacts to
     * @throws Exception
     * @step. ^User (.*) added contacts? (.*) to group chat (.*)
     */
    @Given("^User (.*) added contacts? (.*) to group chat (.*)")
    public void UserXAddedContactsToGroupChat(String asUser, String contacts,
            String conversationName) throws Exception {
        context.getCommonSteps().UserXAddedContactsToGroupChat(asUser, contacts,
                conversationName);
    }

    /**
     * Wait until suggestions are in the backend for a certain user
     *
     * @param userNameAlias the name of the user
     * @throws Exception
     */
    @Given("^There are suggestions for user (.*) on backend$")
    public void suggestions(String userNameAlias) throws Exception {
        context.getCommonSteps().WaitUntilSuggestionFound(userNameAlias);
    }

    /**
     * Add email(s) into address book of a user and upload address book in backend
     *
     * @param asUser name of the user where the address book is uploaded
     * @param emails list of email addresses seperated by comma
     * @throws Exception
     */
    @Given("^User (.*) has contacts? (.*) in address book")
    public void UserXHasContactsInAddressBook(String asUser, String emails)
            throws Exception {
        context.getCommonSteps().UserXHasContactsInAddressBook(asUser, emails);
    }

    /**
     * Record SHA256-hash of current user profile picture
     *
     * @param asUser user name/alias
     * @throws Exception
     * @step. (.*) takes? snapshot of current profile picture$
     */
    @Given("(.*) takes? snapshot of current profile picture$")
    public void UserXTakesSnapshotOfProfilePicture(String asUser)
            throws Exception {
        context.getCommonSteps().UserXTakesSnapshotOfProfilePicture(asUser);
    }

    /**
     * Verify whether current user picture is changed since the last snapshot was made
     *
     * @param userNameAlias user name/alias
     * @throws Exception
     * @step. ^I verify that current profile picture snapshot of (.*) differs? from the previous one$
     */
    @Then("^I verify that current profile picture snapshot of (.*) differs? from the previous one$")
    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias) throws Exception {
        context.getCommonSteps()
                .UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias);
    }

    /**
     * User adds a remote device to his list of devices
     *
     * @step. user (.*) adds a new device (.*)$
     *
     * @param userNameAlias user name/alias
     * @param deviceName unique name of the device
     * @throws Exception
     */
    @When("user (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias,
            String deviceName, String label) throws Exception {
        context.getCommonSteps().UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
    }

    /**
     * Will throw PendingException if the current browser does not support synthetic drag and drop
     *
     * @step. ^My browser supports synthetic drag and drop$
     */
    @Given("^My browser supports synthetic drag and drop$")
    public void MyBrowserSupportsSyntheticDragDrop() {
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingSyntheticDragAndDrop()) {
            throw new PendingException();
        }
    }

    /**
     * Verifies whether current browser log is empty or not
     *
     * @throws Exception
     * @step. ^I verify browser log is empty$
     */
    @Then("^I verify browser log is empty$")
    public void VerifyBrowserLogIsEmpty() throws Exception {
        if (PlatformDrivers.getInstance().hasDriver(context.getCurrentPlatform())) {
            try {
                if (WebAppExecutionContext.getBrowser()
                        .isSupportingConsoleLogManagement()) {
                    List<LogEntry> browserLog = Lifecycle.getBrowserLog(PlatformDrivers
                            .getInstance().getDriver(context.getCurrentPlatform())
                            .get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS));

                    StringBuilder bLog = new StringBuilder("\n");
                    browserLog.stream().forEach(
                            (entry) -> {
                                bLog.append(entry.getLevel()).append(":")
                                .append(entry.getMessage())
                                .append("\n");
                            });
                    assertTrue("BrowserLog is not empty: " + bLog.toString(),
                            browserLog.isEmpty());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Refreshes page by getting and setting the current URL. Note: Alternative 'WebDriver.navigate().refresh()' hangs with
     * Firefox.
     *
     * @throws Exception
     * @step. ^I refresh page$
     */
    @Then("^I refresh page$")
    public void IRefreshPage() throws Exception {
        context.getPagesCollection().getPage(RegistrationPage.class)
                .refreshPage();
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName the file path name of the image to send. The path name is defined relative to the image file defined
     * in Configuration.cnf.
     * @param conversationType "single user" or "group" conversation.
     * @param dstConversationName the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^Contact (.*) sends image (.*) to (.*) conversation (.*)$
     */
    @When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
            String imageFileName, String conversationType,
            String dstConversationName) throws Exception {
        String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
        Boolean isGroup = null;
        if (conversationType.equals("single user")) {
            isGroup = false;
        } else if (conversationType.equals("group")) {
            isGroup = true;
        }
        if (isGroup == null) {
            throw new Exception(
                    "Incorrect type of conversation specified (single user | group) expected.");
        }
        context.getCommonSteps().UserSentImageToConversation(imageSenderUserNameAlias,
                imagePath, dstConversationName, isGroup);
    }

    /**
     * Unblocks user
     *
     * @param userAsNameAlias user which wants to unblock another
     * @param userToBlockNameAlias user to unblock
     * @throws Exception
     * @step. ^(\\w+) unblocks (\\w+)$
     */
    @Given("^(\\w+) unblocks user (\\w+)$")
    public void UserUnblocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        context.getCommonSteps().UnblockContact(userAsNameAlias, userToBlockNameAlias);
    }

    /**
     * Open the sign in page directly (not through a link). This is useful when testing pages with dead ends (forget password,
     * email verification)
     *
     * @throws Exception
     * @step. ^I open Sign In page$
     */
    @Given("^I open Sign In page$")
    public void IOpenSignInPage() throws Exception {
        context.getPagesCollection().getPage(RegistrationPage.class)
                .openSignInPage();
    }

}
