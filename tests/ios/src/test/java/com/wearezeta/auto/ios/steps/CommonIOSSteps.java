package com.wearezeta.auto.ios.steps;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import static com.wearezeta.auto.common.CommonUtils.*;

public class CommonIOSSteps {
    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private static final String DEFAULT_USER_AVATAR = "android_dialog_sendpicture_result.png";
    private Date testStartedDate = new Date();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollecton = IOSPagesCollection
            .getInstance();

    public static final String DEFAULT_AUTOMATION_MESSAGE = "iPhone has stupid spell checker";

    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.http",
                "warn");
    }

    public static final Platform CURRENT_PLATFORM = Platform.iOS;

    private static String getPlatformVersion() throws Exception {
        return getPlatformVersionFromConfig(CommonIOSSteps.class);
    }

    private static String getUrl() throws Exception {
        return getIosAppiumUrlFromConfig(CommonIOSSteps.class);
    }

    private static String getPath() throws Exception {
        return getIosApplicationPathFromConfig(CommonIOSSteps.class);
    }

    private static boolean isUseNativeInstrumentsEnabled() throws Exception {
        return Boolean.parseBoolean(getIsUseNativeInstrumentsLibFromConfig(
                CommonIOSSteps.class).orElseGet(() -> "false"));
    }

    private static String getAppName() throws Exception {
        return getIOSAppName(CommonIOSSteps.class);
    }

    public Future<ZetaIOSDriver> resetIOSDriver(boolean enableAutoAcceptAlerts)
            throws Exception {
        return resetIOSDriver(enableAutoAcceptAlerts, false);
    }

    private static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    @SuppressWarnings("unchecked")
    public Future<ZetaIOSDriver> resetIOSDriver(boolean enableAutoAcceptAlerts,
                                                boolean overrideWaitForAppScript) throws Exception {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("nativeInstrumentsLib", isUseNativeInstrumentsEnabled());
        capabilities.setCapability("newCommandTimeout", IOSPage.IOS_DRIVER_INIT_TIMEOUT / 1000);
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        capabilities.setCapability("app", getPath());
        capabilities.setCapability("appName", getAppName());
        final String deviceName = getDeviceName(this.getClass());
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("launchTimeout", IOSPage.IOS_DRIVER_INIT_TIMEOUT);
        final String backendType = getBackendType(this.getClass());
        capabilities
                .setCapability(
                        "processArguments",
                        "--args -TutorialOverlaysEnabled 0 -SkipFirstTimeUseChecks 1 -DisableHockeyUpdates 1 -UseHockey 1 -ZMBackendEnvironmentType "
                                + backendType);
        if (enableAutoAcceptAlerts) {
            capabilities.setCapability("autoAcceptAlerts", true);
        }

        if (overrideWaitForAppScript) {
            capabilities.setCapability("waitForAppScript",
                    "$.delay(20000); true;");
        }

        setTestStartedDate(new Date());
        return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance()
                .resetDriver(getUrl(), capabilities, DRIVER_CREATION_RETRIES_COUNT);
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        try {
            SEBridge.getInstance().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(
                !scenario.getSourceTagNames().contains("@noAcceptAlert"));
        ZetaFormatter.setLazyDriver(lazyDriver);
        pagesCollecton.setFirstPage(new LoginPage(lazyDriver));
    }

    @When("^I see keyboard$")
    public void ISeeKeyboard() throws Exception {
        Assert.assertTrue(pagesCollecton.getCommonPage().isKeyboardVisible());
    }

    @When("^I dont see keyboard$")
    public void IDontSeeKeyboard() throws Exception {
        Assert.assertFalse(pagesCollecton.getCommonPage().isKeyboardVisible());
    }

    @When("^I press keyboard Delete button$")
    public void IPressKeyboardDeleteBtn() throws Exception {
        pagesCollecton.getCommonPage().clickKeyboardDeleteButton();
        pagesCollecton.getCommonPage().clickKeyboardDeleteButton();
    }

    /**
     * Presses the return on keyboard
     *
     * @throws Exception
     * @step. ^I press keyboard Return button$
     */
    @When("^I press keyboard Return button$")
    public void IPressKeyboardReturnBtn() throws Exception {
        pagesCollecton.getCommonPage().clickKeyboardReturnButton();
    }

    @When("^I scroll up page a bit$")
    public void IScrollUpPageABit() throws Exception {
        pagesCollecton.getCommonPage().smallScrollUp();
    }

    @When("^I accept alert$")
    public void IAcceptAlert() throws Exception {
        pagesCollecton.getCommonPage().acceptAlert();
    }

    @When("^I dismiss alert$")
    public void IDismissAlert() throws Exception {
        pagesCollecton.getCommonPage().dismissAlert();
    }

    /**
     * Dismiss all alerts that appears before timeout
     *
     * @throws Exception
     * @step. ^I dismiss all alerts$
     */
    @When("^I dismiss all alerts$")
    public void IDismissAllAlerts() throws Exception {
        pagesCollecton.getCommonPage().dismissAllAlerts();
    }

    /**
     * Hide keyboard using mobile command
     *
     * @throws Exception
     * @step. ^I hide keyboard$
     */
    @When("^I hide keyboard$")
    public void IHideKeyboard() throws Exception {
        pagesCollecton.getCommonPage().hideKeyboard();
    }

    /**
     * Hide keyboard by click on hide keyboard button
     *
     * @throws Exception
     * @step. ^I click hide keyboard button$
     */
    @When("^I click hide keyboard button$")
    public void IClickHideKeyboardBtn() throws Exception {
        pagesCollecton.getCommonPage().clickHideKeyboardButton();
    }

    /**
     * Click on Space button on keyboard
     *
     * @throws Exception
     * @step. I click space keyboard button
     */
    @When("I click space keyboard button")
    public void IClickSpaceKeyboardButton() throws Exception {
        pagesCollecton.getCommonPage().clickSpaceKeyboardButton();
    }

    /**
     * Click on Done button on keyboard
     *
     * @throws Exception
     * @step. I click DONE keyboard button
     */
    @When("I click DONE keyboard button")
    public void IClickDoneKeyboardButton() throws Exception {
        pagesCollecton.getCommonPage().clickDoneKeyboardButton();
    }

    /**
     * Closes the app for a certain amount of time in seconds
     *
     * @param seconds time in seconds to close the app
     * @throws Exception
     * @step. ^I close the app for (.*) seconds$
     */
    @When("^I close the app for (\\d+) seconds$")
    public void ICloseApp(int seconds) throws Exception {
        pagesCollecton.getCommonPage().minimizeApplication(seconds);
    }

    /**
     * Locks screen for a certain amount of time in seconds
     *
     * @param seconds time in seconds to lock screen
     * @throws Exception
     * @step.^I lock screen for (.*) seconds$
     */
    @When("^I lock screen for (\\d+) seconds$")
    public void ILockScreen(int seconds) throws Exception {
        pagesCollecton.getCommonPage().lockScreen(seconds);
    }

    @Given("^(.*) sent connection request to (.*)$")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
                                               String usersToNameAliases) throws Throwable {
        commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
    }

    @Given("^(.*) has group chat (.*) with (.*)$")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises)
            throws Exception {
        commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAlises);
    }

    /**
     * Removes user from group conversation
     *
     * @param chatOwnerNameAlias name of the user who deletes
     * @param userToRemove       name of the user to be removed
     * @param chatName           name of the group conversation
     * @throws Exception
     * @step. ^(.*) removed (.*) from group chat (.*)$
     */
    @Given("^(.*) removed (.*) from group chat (.*)$")
    public void UserARemovedUserBFromGroupChat(String chatOwnerNameAlias,
                                               String userToRemove, String chatName) throws Exception {
        commonSteps.UserXRemoveContactFromGroupChat(chatOwnerNameAlias,
                userToRemove, chatName);
    }

    /**
     * Adding user to group conversation
     *
     * @param chatOwnerNameAlias name of the user who is adding
     * @param userToAdd          name of the user to be added
     * @param chatName           name of the group conversation
     * @throws Exception
     * @step. ^(.*) added (.*) to group chat (.*)
     */
    @When("^(.*) added (.*) to group chat (.*)")
    public void UserXaddUserBToGroupChat(String chatOwnerNameAlias,
                                         String userToAdd, String chatName) throws Exception {
        commonSteps.UserXAddedContactsToGroupChat(chatOwnerNameAlias,
                userToAdd, chatName);
    }

    /**
     * User leaves group chat
     *
     * @param userName name of the user who leaves
     * @param chatName chat name that user leaves
     * @throws Exception
     * @step. ^(.*) leave(s) group chat (.*)$
     */
    @Given("^(.*) leave[s]* group chat (.*)$")
    public void UserLeavesGroupChat(String userName, String chatName)
            throws Exception {
        commonSteps.UserXLeavesGroupChat(userName, chatName);
    }

    @Given("^(.*) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias,
                                  String usersToNameAliases) throws Exception {
        commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^There \\w+ (\\d+) user[s]*$")
    public void ThereAreNUsers(int count) throws Exception {
        commonSteps.ThereAreNUsers(CURRENT_PLATFORM, count);
    }

    @Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
                myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
    }

    /**
     * Creates specified number of users and sets user with specified name as
     * main user. The user is registered with a phone number only and has no
     * email address attached
     *
     * @param count       number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with phone number
     * only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
                                                     String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(
                CURRENT_PLATFORM, count, myNameAlias);
    }

    /**
     * Creates specified number of users and sets user with specified name as
     * main user. The user is registered with a email only and has no phone
     * number attached
     *
     * @param count       number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with email only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with email only$")
    public void ThereAreNUsersWhereXIsMeWithoutPhone(int count,
                                                     String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMeRegOnlyByMail(CURRENT_PLATFORM,
                count, myNameAlias);
    }

    @When("^(.*) ignore all requests$")
    public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
            throws Exception {
        commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
    }

    /**
     * Cancel all outgoing connection requests in pending status
     *
     * @param userToNameAlias user name who will cancel requests
     * @throws Exception
     * @step. ^(.*) cancel all outgoing connection requests$
     */
    @When("^(.*) cancel all outgoing connection requests$")
    public void CancelAllOutgoingConnectRequest(String userToNameAlias)
            throws Exception {
        commonSteps.CancelAllOutgoingConnectRequests(userToNameAlias);
    }

    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        commonSteps.WaitForTime(seconds);
    }

    @When("^User (.*) blocks user (.*)$")
    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
    }

    @When("^(.*) archived conversation with (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias,
                                            String archivedUserNameAlias) throws Exception {
        commonSteps.ArchiveConversationWithUser(userToNameAlias,
                archivedUserNameAlias);
    }

    /**
     * Silences conversation in backend
     *
     * @param userToNameAlias    user that mutes the conversation
     * @param mutedUserNameAlias name of single conversation to mute
     * @throws Exception
     * @step. ^(.*) silenced conversation with (.*)$
     */
    @When("^(.*) silenced conversation with (.*)$")
    public void MuteConversationWithUser(String userToNameAlias,
                                         String mutedUserNameAlias) throws Exception {
        mutedUserNameAlias = usrMgr.replaceAliasesOccurences(
                mutedUserNameAlias, FindBy.NAME_ALIAS);
        commonSteps.MuteConversationWithUser(userToNameAlias,
                mutedUserNameAlias);
    }

    /**
     * Silences group conversation in backend
     *
     * @param userToNameAlias user that mutes the conversation
     * @param groupName       name of group conversation to mute
     * @throws Exception
     * @step. ^(.*) silenced group conversation with (.*)$
     */
    @When("^(.*) silenced group conversation with (.*)$")
    public void MuteGroupConversationWithUser(String userToNameAlias,
                                              String groupName) throws Exception {
        commonSteps.MuteConversationWithGroup(userToNameAlias, groupName);
    }

    /**
     * Verifies that an unread message dot is NOT seen in the conversation list
     *
     * @param userToNameAlias       user that archives the group conversation
     * @param archivedUserNameAlias name of group conversation to archive
     * @throws Exception
     * @step. ^(.*) archived conversation having groupname (.*)$
     */
    @When("^(.*) archived conversation having groupname (.*)$")
    public void ArchiveConversationHavingGroupname(String userToNameAlias,
                                                   String archivedUserNameAlias) throws Exception {
        commonSteps.ArchiveConversationWithGroup(userToNameAlias,
                archivedUserNameAlias);
    }

    /**
     * Call method in BackEnd that should generate new verification code
     *
     * @param user user name
     * @throws Exception
     */
    @When("New verification code is generated for user (.*)")
    public void newVerificationCodeGenrated(String user) throws Exception {
        commonSteps.GenerateNewLoginCode(user);
    }

    @When("^(.*) accept all requests$")
    public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
            throws Exception {
        commonSteps.AcceptAllIncomingConnectionRequests(userToNameAlias);
    }

    @Given("^User (\\w+) (securely )?pings conversation (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias, String isSecure,
                                       String dstConversationName) throws Exception {
        if (isSecure == null) {
            commonSteps.UserPingedConversation(pingFromUserNameAlias, dstConversationName);
        } else {
            commonSteps.UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
        }
    }

    @Given("^User (.*) sends (\\d+) (encrypted )?messages? to (user|group conversation) (.*)$")
    public void UserSendXMessagesToConversation(String msgFromUserNameAlias,
                                                int msgsCount, String areEncrypted,
                                                String conversationType,
                                                String conversationName) throws Exception {
        for (int i = 0; i < msgsCount; i++) {
            if (conversationType.equals("user")) {
                // 1:1 conversation
                if (areEncrypted == null) {
                    commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                } else {
                    commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                }
            } else {
                // group conversation
                if (areEncrypted == null) {
                    commonSteps.UserSentMessageToConversation(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                } else {
                    commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                }
            }
        }
    }

    @Given("^User (.*) sends (encrypted )?message \"(.*)\" to (user|group conversation) (.*)$")
    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String areEncrypted, String msg,
                                              String conversationType, String conversationName) throws Exception {
        if (conversationType.equals("user")) {
            // 1:1 conversation
            if (areEncrypted == null) {
                commonSteps.UserSentMessageToConversation(userFromNameAlias,
                        conversationName, msg);
            } else {
                commonSteps.UserSentOtrMessageToConversation(userFromNameAlias,
                        conversationName, msg);
            }
        } else {
            // group conversation
            if (areEncrypted == null) {
                commonSteps.UserSentMessageToConversation(userFromNameAlias,
                        conversationName, msg);
            } else {
                commonSteps.UserSentOtrMessageToConversation(userFromNameAlias,
                        conversationName, msg);
            }
        }
    }

    @When("^User (\\w+) changes? avatar picture to (.*)$")
    public void IChangeUserAvatarPicture(String userNameAlias, String name)
            throws Exception {
        final String rootPath = getSimulatorImagesPathFromConfig(getClass());
        commonSteps.IChangeUserAvatarPicture(userNameAlias, rootPath
                + "/"
                + (name.toLowerCase().equals("default") ? DEFAULT_USER_AVATAR
                : name));
    }

    /**
     * Change user name on the backend
     *
     * @param userNameAlias user's name alias to change
     * @param newName       new given name
     * @throws Exception
     * @step. ^User (\\w+) changes? name to (.*)$
     */
    @When("^User (\\w+) changes? name to (.*)$")
    public void IChangeUserName(String userNameAlias, String newName)
            throws Exception {
        commonSteps.IChangeUserName(userNameAlias, newName);
    }

    @When("^User (\\w+) changes? accent color to (.*)$")
    public void IChangeAccentColor(String userNameAlias, String newColor) throws Exception {
        commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
    }

    @Given("^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$")
    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
            throws Exception {
        commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
    }

    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
    }

    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    @Given("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
                                               String isEncrypted,
                                               String imageFileName, String conversationType,
                                               String dstConversationName) throws Exception {
        final String imagePath = IOSPage.getImagesPath() + imageFileName;
        final boolean isGroup = conversationType.equals("group");
        if (isEncrypted == null) {
            commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            commonSteps.UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    @After
    public void tearDown() {
        try {
            // async calls/waiting instances cleanup
            CommonCallingSteps2.getInstance().cleanup();
        } catch (Exception e) {
            // do not fail if smt fails here
            e.printStackTrace();
        }

        pagesCollecton.clearAllPages();

        try {
            if (getIsSimulatorFromConfig(getClass())) {
                IOSCommonUtils
                        .collectSimulatorLogs(
                                getDeviceName(getClass()),
                                getTestStartedDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            commonSteps.getUserManager().resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Date getTestStartedDate() {
        return testStartedDate;
    }

    public void setTestStartedDate(Date testStartedDate) {
        this.testStartedDate = testStartedDate;
    }

    /**
     * Rotate device to landscape
     *
     * @param orientation must be landscape or portrait
     * @throws Exception
     * @step. ^I rotate UI to (landscape|portrait)$
     */
    @When("^I rotate UI to (landscape|portrait)$")
    public void WhenIRotateUILandscape(ScreenOrientation orientation)
            throws Exception {
        pagesCollecton.getCommonPage().rotateScreen(orientation);
        Thread.sleep(1000); // fix for animation
    }

    /**
     * Tap in center of the screen
     *
     * @throws Exception
     * @step. ^I tap on center of the screen$
     */
    @When("^I tap on center of the screen$")
    public void ITapOnCenterOfTheScreen() throws Exception {
        pagesCollecton.getCommonPage().tapOnCenterOfScreen();
    }

    /**
     * Tap in top left corner of the screen
     *
     * @throws Exception
     * @step. ^I tap on top left corner of the screen$
     */
    @When("^I tap on top left corner of the screen$")
    public void ITapOnTopLeftCornerOfTheScreen() throws Exception {
        pagesCollecton.getCommonPage().tapOnTopLeftScreen();
    }

    /**
     * A user adds another user to a group chat
     *
     * @param user          that adds someone to a chat
     * @param userToBeAdded user that gets added by someone
     * @param group         group chat you get added to
     * @throws Throwable
     * @step. ^User (.*) adds [Uu]ser (.*) to group chat (.*)$
     */
    @When("^User (.*) adds [Uu]ser (.*) to group chat (.*)$")
    public void UserAddsUserToGroupChat(String user, String userToBeAdded,
                                        String group) throws Throwable {
        commonSteps.UserXAddedContactsToGroupChat(user, userToBeAdded, group);
    }

    /**
     * Click Simulator window at the corresponding position
     *
     * @param strX float number 0 <= x <= 1, relative width position of click point
     * @param strY float number 0 <= y <= 1, relative height position of click point
     * @throws Exception
     * @step. ^I click at ([\d\.]+),([\d\.]+) of Simulator window$
     */
    @When("^I click at ([\\d\\.]+),([\\d\\.]+) of Simulator window$")
    public void ReturnToWireApp(String strX, String strY) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.clickAt(strX, strY);
        } else {
            throw new PendingException("This step is not available for non-simulator devices");
        }
    }

    /**
     * Delete self profile picture
     *
     * @param nameAlias user name/alias
     * @throws Exception
     * @step. ^User (.*) removes his avatar picture$
     */
    @Given("^User (.*) removes his avatar picture$")
    public void UserRemovesAvatarPicture(String nameAlias) throws Exception {
        commonSteps.UserDeletesAvatarPicture(nameAlias);
    }

    @Then("^I verify the alert contains text (.*)")
    public void IVerifyAlertContains(String expectedText) throws Exception {
        Assert.assertTrue(String.format("there is not '%s' on the alert", expectedText),
                pagesCollecton.getCommonPage().isAlertContainsText(expectedText));
    }

    /**
     * User adds a remote device to his list of devices
     *
     * @param userNameAlias user name/alias
     * @param deviceName    unique name of the device
     * @throws Exception
     * @step. User (.*) adds a new device (.*)$
     */
    @When("^User (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias,
                                             String deviceName, String label) throws Exception {
        commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
    }

    /**
     * User adds multiple devices to his list of devices
     *
     * @param userNameAlias user name/alias
     * @param deviceNames   unique name of devices, comma-separated list
     * @throws Exception
     * @step. User (.*) adds new devices (.*)
     */
    @When("^User (.*) adds new devices (.*)")
    public void UserAddRemoteDeviceToAccount(String userNameAlias, String deviceNames) throws Exception {
        final List<String> names = CommonSteps.splitAliases(deviceNames);
        for (String name : names) {
            commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, name, CommonUtils.generateRandomString(10));
        }
    }

}
