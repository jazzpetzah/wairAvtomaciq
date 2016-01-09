package com.wearezeta.auto.android.steps;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.logging.LoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionFailedLoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionPassedLoggingProfile;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import gherkin.formatter.model.Result;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class CommonAndroidSteps {
    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
    }

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private static final Logger log = ZetaLogger.getLog(CommonAndroidSteps.class.getSimpleName());

    private static ArrayList<BufferedImage> images = new ArrayList<>();
    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    public static final Platform CURRENT_PLATFORM = Platform.Android;

    public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
    public static final int DEFAULT_SWIPE_TIME = 1500;
    private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";

    private static String getUrl() throws Exception {
        return CommonUtils.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class);
    }

    private static String getPath() throws Exception {
        return CommonUtils.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
    }

    @SuppressWarnings("unchecked")
    public Future<ZetaAndroidDriver> resetAndroidDriver(String url, String path, Class<?> cls) throws Exception {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        LoggingPreferences object = new LoggingPreferences();
        object.enable("logcat", Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, object);
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        // To init the first available device
        capabilities.setCapability("deviceName", "null");
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(cls));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidActivityFromConfig(cls));
        capabilities.setCapability("appWaitActivity", CommonUtils.getAndroidWaitActivitiesFromConfig(cls));
        capabilities.setCapability("applicationName", "selendroid");
        capabilities.setCapability("automationName", "selendroid");

        try {
            return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance().resetDriver(url, capabilities, 1,
                    this::onDriverInitFinished, this::onDriverInitStarted);
        } catch (SessionNotCreatedException e) {
            // Unlock the screen and retry
            AndroidCommonUtils.unlockScreen();
            Thread.sleep(5000);
            return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance().resetDriver(url, capabilities, 1,
                    this::onDriverInitFinished, this::onDriverInitStarted);
        }
    }

    private Boolean onDriverInitStarted() {
        try {
            AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
            AndroidCommonUtils.disableHints();
            AndroidCommonUtils.disableHockeyUpdates();
            AndroidCommonUtils.installTestingGalleryApp(this.getClass());
            String backendJSON = AndroidCommonUtils.createBackendJSON(CommonUtils.getBackendType(this.getClass()));
            AndroidCommonUtils.deployBackendFile(backendJSON);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return true;
    }

    private static final int UPDATE_ALERT_VISIBILITY_TIMEOUT = 5; // seconds

    @SuppressWarnings("unused")
    private void closeUpdateAlertIfAppears(RemoteWebDriver drv, By locator) {
        try {
            if (DriverUtils.waitUntilLocatorIsDisplayed(drv, locator, UPDATE_ALERT_VISIBILITY_TIMEOUT)) {
                drv.findElement(locator).click();
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private static final long INTERFACE_INIT_TIMEOUT_MILLISECONDS = 15000;

    private void onDriverInitFinished(RemoteWebDriver drv) {
        final By locator = By.xpath(AndroidPage.xpathDismissUpdateButton);
        final long millisecondsStarted = System.currentTimeMillis();
        WebDriverException savedException = null;
        do {
            try {
                DriverUtils.waitUntilLocatorIsDisplayed(drv, locator, 1);
                break;
            } catch (WebDriverException e) {
                savedException = e;
                log.debug("Waiting for the views to initialize properly...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    Throwables.propagate(e);
                }
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= INTERFACE_INIT_TIMEOUT_MILLISECONDS);
        if (System.currentTimeMillis() - millisecondsStarted > INTERFACE_INIT_TIMEOUT_MILLISECONDS) {
            log.error(String.format(
                    "UI views have not been initialized properly after %s seconds. Restarting Selendroid usually helps ;-)",
                    INTERFACE_INIT_TIMEOUT_MILLISECONDS));
            throw savedException;
        }
        // Break the glass in case of fire!
        // Just uncomment the following line if Android developers break update
        // alert appearance one more time
        // closeUpdateAlertIfAppears(drv, locator);
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        if (scenario.getSourceTagNames().contains("@performance")) {
            AndroidLogListener.getInstance(ListenerType.PERF).start();
        }
        AndroidLogListener.getInstance(ListenerType.DEFAULT).start();
        final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(getUrl(), getPath(), this.getClass());
        ZetaFormatter.setLazyDriver(lazyDriver);
        pagesCollection.setFirstPage(new WelcomePage(lazyDriver));
    }

    /**
     * Presses the android back button
     *
     * @throws IOException
     * @step. ^I press back button$
     */
    @When("^I press back button$")
    public void PressBackButton() throws Exception {
        commonSteps.WaitForTime(1);
        pagesCollection.getCommonPage().navigateBack();
    }

    /**
     * Hides the system keyboard
     *
     * @throws Exception
     * @step. ^I hide keyboard$
     */
    @When("^I hide keyboard$")
    public void IHideKeyboard() throws Exception {
        pagesCollection.getCommonPage().hideKeyboard();
    }

    @When("^I swipe right$")
    public void ISwipeRight() throws Exception {
        pagesCollection.getCommonPage().swipeRightCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe left$")
    public void ISwipeLeft() throws Exception {
        pagesCollection.getCommonPage().swipeLeftCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe up$")
    public void ISwipeUp() throws Exception {
        pagesCollection.getCommonPage().swipeUpCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe down$")
    public void ISwipeDown() throws Exception {
        pagesCollection.getCommonPage().swipeDownCoordinates(DEFAULT_SWIPE_TIME);
    }

    /**
     * Swipe down from given high %8 to 90% of hight
     *
     * @throws Exception
     * @step. ^I swipe down from\\s(\\d+)%$
     */
    @When("^I swipe down from\\s(\\d+)%$")
    public void ISwipeDownFrom(int startPercent) throws Exception {
        pagesCollection.getCommonPage().swipeByCoordinates(DEFAULT_SWIPE_TIME, 50, startPercent, 50, 90);
    }

    /**
     * Sends the application into back stack and displays the home screen.
     *
     * @throws Exception
     * @step. ^I minimize the application$
     */
    @When("^I minimize the application$")
    public void IMinimizeApplication() throws Exception {
        AndroidCommonUtils.tapHomeButton();
    }

    /**
     * Lock/unlock the device
     *
     * @param shouldUnlock equals to null is "un-" part does not exist
     * @throws Exception
     * @step. ^I (un)?lock the device$
     */
    @When("^I (un)?lock the device$")
    public void ILockUnlockTheDevice(String shouldUnlock) throws Exception {
        if (shouldUnlock == null) {
            AndroidCommonUtils.lockScreen();
        } else {
            AndroidCommonUtils.unlockDevice();
            // FIXME: Unlock selendroid app does not restore the previously active application
            AndroidCommonUtils.switchToApplication(
                    CommonUtils.getAndroidPackageFromConfig(this.getClass()),
                    CommonUtils.getAndroidActivityFromConfig(this.getClass()));
        }
    }

    /**
     * Takes 1st screenshot for comparison, previous taken screenshots will be cleaned
     *
     * @throws Exception
     * @step. ^I take( 1st)? screenshot$
     */
    @When("^I take( 1st)? screenshot$")
    public void WhenITake1stScreenshot(String first) throws Exception {
        final Optional<BufferedImage> screenshot = pagesCollection.getCommonPage().takeScreenshot();
        if (screenshot.isPresent()) {
            if (first != null || (images.size() >= 2 && first == null)) images.clear();
            images.add(screenshot.get());
//            File outputfile = new File("/Project/screen_"+System.nanoTime()+".png");
//            ImageIO.write(screenshot.get(), "png", outputfile);
        } else {
            throw new RuntimeException("Selenium has failed to take the screenshot from current page");
        }
    }

    /**
     * Takes 2nd screenshot for comparison
     *
     * @throws Exception
     * @step. ^I take 2nd screenshot$
     */
    @When("^I take 2nd screenshot$")
    public void WhenITake2ndScreenshot() throws Exception {
        final Optional<BufferedImage> screenshot = pagesCollection.getCommonPage().takeScreenshot();
        if (screenshot.isPresent()) {
            if (images.size() == 2) images.remove(1);
            images.add(screenshot.get());
        } else {
            throw new RuntimeException("Selenium has failed to take the screenshot from current page");
        }
    }

    /**
     * Taps on the center of the screen
     *
     * @throws Throwable
     * @step. ^I tap on center of screen$
     */
    @When("^I tap on center of screen")
    public void WhenITapOnCenterOfScreen() throws Throwable {
        pagesCollection.getCommonPage().tapByCoordinates(50, 40);
    }

    /**
     * Compare that 1st and 2nd screenshots are equal/not equal
     *
     * @param shouldBeEqual equals to null if screenshots should be different
     * @step. ^I compare 1st and 2nd screenshots and they are( not)?different$
     */
    @Then("^I compare 1st and 2nd screenshots and they are( not)? different$")
    public void ThenICompare1st2ndScreenshotsAndTheyAreDifferent(String shouldBeEqual) {
        double score = ImageUtil.getOverlapScore(images.get(0), images.get(1));
        double targetScore = 0.75d;
        if (shouldBeEqual == null)
            Assert.assertTrue("Screenshots overlap score=" + score + ", but expected less than " + targetScore, score < targetScore);
        else
            Assert.assertTrue("Screenshots overlap score=" + score + ", but expected more than " + targetScore, score >= targetScore);
    }

    /**
     * Restores the application from a minimized state.
     *
     * @throws Exception
     * @step. ^I restore the application$
     */
    @When("^I restore the application$")
    public void IRestoreApllication() throws Exception {
        AndroidCommonUtils.switchToApplication(CommonUtils.getAndroidPackageFromConfig(this.getClass()),
                CommonUtils.getAndroidActivityFromConfig(this.getClass()));
    }

    /**
     * Verifies that user A has sent a connection request to user B
     *
     * @param userFromNameAlias  the user from which the connection request originated
     * @param usersToNameAliases the target user
     * @step. ^(.*) sent connection request to (.*)$
     */
    @Given("^(.*) sent connection request to (.*)$")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias, String usersToNameAliases) throws Throwable {
        commonSteps.ConnectionRequestIsSentTo(userFromNameAlias, usersToNameAliases);
    }

    /**
     * Verifies that user A has an avatar matching the picture in file X
     *
     * @param name    the user to check
     * @param picture the file name of the picture to check against. The file name
     *                is relative to the pictures directory as defined in the
     *                Configurations.cnf file
     * @throws Exception
     * @step. ^(.*) has an avatar picture from file (.*)$
     */
    @Given("^(.*) has an avatar picture from file (.*)$")
    public void GivenUserHasAnAvatarPicture(String name, String picture) throws Exception {
        String picturePath = CommonUtils.getImagesPath(CommonAndroidSteps.class) + "/" + picture;
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        commonSteps.IChangeUserAvatarPicture(name, picturePath);
    }

    /**
     * Verifies that user A has an accent color C
     *
     * @param name      the user to check
     * @param colorName the assumed accent color
     * @throws Throwable
     * @step. ^(.*) has an accent color (.*)$
     */
    @Given("^(.*) has an accent color (.*)$")
    public void GivenUserHasAnAccentColor(String name, String colorName) throws Throwable {
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        commonSteps.IChangeUserAccentColor(name, colorName);
    }

    /**
     * Verifies that user A has the name X
     *
     * @param name    the user to check
     * @param newName the name to check they have
     * @throws Throwable
     * @step. ^(.*) has a name (.*)$
     */
    @Given("^(.*) has a name (.*)$")
    public void GivenUserHasAName(String name, String newName) throws Throwable {
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        commonSteps.IChangeUserName(name, newName);
    }

    /**
     * Verifies that user A is connected to a given list of users
     *
     * @param userFromNameAlias  the user to check
     * @param usersToNameAliases A separated list of user names to check to see if connected to
     *                           user A.
     * @throws Exception
     * @step. ^(.*) is connected to (.*)$
     */
    @Given("^(.*) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        System.out.println("userFromAlias: " + userFromNameAlias);
        System.out.println("usersToNameAliases: " + usersToNameAliases);
        commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    /**
     * Silences a given user from the perspective of the another user through
     * the backend
     *
     * @param mutedUser the user to silence
     * @param otherUser the user who does the silencing
     * @throws Exception
     * @step. ^(.*) is silenced to user (.*)$
     */
    @Given("^(.*) is silenced to user (.*)$")
    public void UserIsSilenced(String mutedUser, String otherUser) throws Exception {
        mutedUser = usrMgr.findUserByNameOrNameAlias(mutedUser).getName();
        otherUser = usrMgr.findUserByNameOrNameAlias(otherUser).getName();

        commonSteps.MuteConversationWithUser(otherUser, mutedUser);
    }

    /**
     * Silences a given group for a user via the backend
     *
     * @param mutedGroup the group to silence
     * @param otherUser  user its silenced from
     * @throws Throwable
     * @step. ^Group (.*) gets silenced for user (.*)$
     */
    @Given("^Group (.*) gets silenced for user (.*)$")
    public void GroupGetsSilenced(String mutedGroup, String otherUser) throws Throwable {
        mutedGroup = usrMgr.replaceAliasesOccurences(mutedGroup, ClientUsersManager.FindBy.NAME_ALIAS);
        otherUser = usrMgr.findUserByNameOrNameAlias(otherUser).getName();

        commonSteps.MuteConversationWithGroup(otherUser, mutedGroup);
    }

    /**
     * Unarchives a given group chat from the perspective of the another user
     * through the backend
     *
     * @param currentUser user which have archived chat
     * @param groupChat   archived group chat which should be unarchived
     * @throws Exception
     * @step. ^(.*) is unarchived group chat (.*)$
     */
    @Given("^(.*) is unarchived group chat (.*)$")
    public void UserIsUnarchivedGroupChat(String currentUser, String groupChat) throws Exception {
        currentUser = usrMgr.findUserByNameOrNameAlias(currentUser).getName();

        commonSteps.UnarchiveConversationWithGroup(currentUser, groupChat);
    }

    /**
     * Verifies that user A is in a group chat with a group of other users
     *
     * @param chatOwnerNameAlias           the user to check
     * @param chatName                     The name of the group chat
     * @param otherParticipantsNameAliases A separated list of user names to check to see if in a group
     *                                     conversation with user A.
     * @throws Exception
     * @step. ^(.*) has group chat (.*) with (.*)$
     */
    @Given("^(.*) has group chat (.*) with (.*)$")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias, String chatName,
                                             String otherParticipantsNameAliases) throws Exception {
        commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName, otherParticipantsNameAliases);
    }

    /**
     * Allows user A to ignore all incoming connection requests
     *
     * @param userToNameAlias the user who will do the ignoring
     * @throws Exception
     * @step. ^(.*) ignore all requests$
     */
    @When("^(.*) ignore all requests$")
    public void IgnoreAllIncomingConnectRequest(String userToNameAlias) throws Exception {
        commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
    }

    /**
     * Allows test to wait for T seconds
     *
     * @param seconds The number of seconds to wait
     * @throws Exception
     * @step. ^I wait for (.*) second[s]*$
     */
    @When("^I wait for\\s*(\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        commonSteps.WaitForTime(seconds);
    }

    /**
     * User A blocks user B
     *
     * @param blockAsUserNameAlias The user to do the blocking
     * @param userToBlockNameAlias The user to block
     * @throws Exception
     * @step. ^User (.*) blocks user (.*)$
     */
    @When("^User (.*) blocks user (.*)$")
    public void BlockContact(String blockAsUserNameAlias, String userToBlockNameAlias) throws Exception {
        commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
    }

    /**
     * User A accepts all requests
     *
     * @throws Exception
     * @step. ^(.*) accept all requests$
     */
    @When("^(.*) accept all requests$")
    public void AcceptAllIncomingConnectionRequests(String userToNameAlias) throws Exception {
        commonSteps.AcceptAllIncomingConnectionRequests(userToNameAlias);
    }

    /**
     * User A sends a ping to a conversation
     *
     * @param pingFromUserNameAlias The user to do the pinging
     * @param dstConversationName   the target conversation to send the ping to
     * @throws Exception
     * @step. ^Contact (.*) ping conversation (.*)$
     */
    @When("^Contact (.*) ping conversation (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias, String dstConversationName) throws Exception {
        commonSteps.UserPingedConversation(pingFromUserNameAlias, dstConversationName);
    }

    /**
     * User A sends a hotping to a conversation
     *
     * @param hotPingFromUserNameAlias The user to do the hotpinging
     * @param dstConversationName      the target converation to send the ping to
     * @throws Exception
     * @step. ^Contact (.*) hotping conversation (.*)$
     */
    @When("^Contact (.*) hotping conversation (.*)$")
    public void UserHotPingedConversation(String hotPingFromUserNameAlias, String dstConversationName)
            throws Exception {
        commonSteps.UserHotPingedConversation(hotPingFromUserNameAlias, dstConversationName);
    }

    /**
     * User A sends a simple text message to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg                  a message to send. Random string will be sent if it is empty
     * @param dstUserNameAlias     The user to receive the message
     * @param isEncrypted          whether the message has to be encrypted
     * @throws Exception
     * @step. ^Contact (.*) sends? (encrypted )?message (.*)to user (.*)$
     */
    @When("^Contact (.*) sends? (encrypted )?message (.*)to user (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias, String isEncrypted,
                                              String msg, String dstUserNameAlias)
            throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0) ?
                CommonUtils.generateRandomString(10) : msg.trim();
        if (isEncrypted == null) {
            commonSteps.UserSentMessageToUser(msgFromUserNameAlias, dstUserNameAlias, msgToSend);
        } else {
            commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias, dstUserNameAlias, msgToSend);
        }
    }

    /**
     * Send message to a conversation
     *
     * @param userFromNameAlias user who want to mute conversation
     * @param message           message to send
     * @param conversationName  the name of existing conversation to send the message to
     * @throws Exception
     * @step. ^User (.*) sent message (.*) to conversation (.*)$
     */
    @When("^User (.*) sent message (.*) to conversation (.*)$")
    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String message, String conversationName) throws Exception {
        commonSteps.UserSentMessageToConversation(userFromNameAlias,
                conversationName, message);
    }

    /**
     * Send messages from all registered user to myself (these users have to be
     * already connected to myself)
     *
     * @param message A message to send
     * @throws Exception
     * @step. ^All contacts send me a message (.*)$"
     */
    @When("^All contacts send me a message (.*)$")
    public void AllContactsSendMeAMessage(String message) throws Exception {
        for (ClientUser user : usrMgr.getCreatedUsers()) {
            if (!user.getName().equals(usrMgr.getSelfUser().getName())) {
                commonSteps.UserSentMessageToUser(user.getName(), usrMgr.getSelfUser().getName(), message);
            }
        }
    }

    /**
     * User A sends specified number of simple text messages to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param count                number of messages to send
     * @param dstUserNameAlias     The user to receive the message
     * @param areEncrypted         Whether the messages should be encrypted
     * @throws Exception
     * @step. ^Contact (.*) send[s]* (\d+) (encrypted )?messages? to user (.*)$
     */
    @When("^Contact (.*) send[s]* (\\d+) (encrypted )?messages? to user (.*)$")
    public void UserSendXMessagesToConversation(String msgFromUserNameAlias, int count, String areEncrypted,
                                                String dstUserNameAlias) throws Exception {
        for (int i = 0; i < count; i++) {
            UserSendMessageToConversation(msgFromUserNameAlias, areEncrypted,
                    null, dstUserNameAlias);
        }
    }

    /**
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count       the number of users to make
     * @param myNameAlias the name of the user to set as the current user
     * @throws Exception
     * @step. ^There \\w+ (\\d+) user[s]* where (.*) is me$
     */
    @Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
    }

    /**
     * Verifies that there are N new users for a test all sharing a common
     * prefix in their names and makes them if they don't exist.
     *
     * @param count      the number of users to make
     * @param namePrefix the prefix for all of the users to share
     * @throws Exception
     * @step. ^There \\w+ (\\d+) shared user[s]* with name prefix ([\\w\\.]+)$
     */
    @Given("^There \\w+ (\\d+) shared user[s]* with name prefix ([\\w\\.]+)$")
    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix) throws Exception {
        commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
    }

    /**
     * Sets the current user to one of the pre-defined users based on the name
     * of that user and assigns the default picture to it.
     *
     * @param nameAlias the user to set as current user.
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e$
     */
    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
        GivenUserHasAnAvatarPicture(nameAlias, DEFAULT_USER_AVATAR);
    }

    /**
     * Sets the current user to one of the pre-defined users based on the name
     * of that user.
     *
     * @param nameAlias the user to set as current user.
     * @throws Exception
     * @step. ^User (\w+) is [Mm]e without picture$
     */
    @Given("^User (\\w+) is [Mm]e without picture$")
    public void UserXIsMeWoPicture(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
    }

    /**
     * Waits for a given time to verify that another user exists in search
     * results
     *
     * @param searchByNameAlias the user to search for in the query results.
     * @param query             the search query to pass to the backend, which will return a
     *                          list of users.
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) exists in backend search results$
     */
    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Waits for a given time to verify that another user is blocked in search
     * results
     *
     * @param searchByNameAlias the user to search for in the query results.
     * @param query             the search query to pass to the backend, which will return a
     *                          list of users.
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) is blocked in backend search results$
     */
    @Given("^(\\w+) waits? until (.*) is blocked in backend search results$")
    public void UserWaitsUntilContactIsBlockedInSearchResults(String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactBlockStateInSearch(searchByNameAlias, query, true);
    }

    /**
     * Waits for a given time to verify that another user does not exist in
     * search results
     *
     * @param searchByNameAlias the user to search for in the query results.
     * @param query             the search query to pass to the backend, which will return a
     *                          list of users.
     * @param timeoutSeconds    maximum time to wait until the other user disappears from
     *                          search list
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) does not exist in backend search
     * results$
     */
    @Given("^(\\w+) waits? (\\d+) seconds? until (.*) does not exist in backend search results$")
    public void UserWaitsUntilContactDoesNotExistsInHisSearchResults(String searchByNameAlias, int timeoutSeconds,
                                                                     String query) throws Exception {
        commonSteps.WaitUntilContactIsNotFoundInSearch(searchByNameAlias, query, timeoutSeconds);
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName            the file path name of the image to send. The path name is
     *                                 defined relative to the image file defined in
     *                                 Configuration.cnf.
     * @param conversationType         "single user" or "group" conversation.
     * @param dstConversationName      the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^Contact (.*) sends image (.*) to (.*) conversation (.*)$
     */
    @When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias, String imageFileName,
                                               String conversationType, String dstConversationName) throws Exception {
        String imagePath = CommonUtils.getImagesPath(CommonAndroidSteps.class) + imageFileName;
        Boolean isGroup = null;
        if (conversationType.equals("single user")) {
            isGroup = false;
        } else if (conversationType.equals("group")) {
            isGroup = true;
        }
        if (isGroup == null) {
            throw new Exception("Incorrect type of conversation specified (single user | group) expected.");
        }
        commonSteps.UserSentImageToConversation(imageSenderUserNameAlias, imagePath, dstConversationName, isGroup);
    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        try {
            AndroidCommonUtils.setAirplaneMode(false);
        } catch (Exception e) {
            // do not fail if smt fails here
            e.printStackTrace();
        }

        try {
            // async calls/waiting instances cleanup
            CommonCallingSteps2.getInstance().cleanup();
        } catch (Exception e) {
            // do not fail if smt fails here
            e.printStackTrace();
        }

        pagesCollection.clearAllPages();

        if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
            try {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            } catch (WebDriverException e) {
                e.printStackTrace();
            }
        }

        AndroidLogListener.forceStopAll();
        LoggingProfile loggingProfile = new RegressionPassedLoggingProfile();
        if (!scenario.getStatus().equals(Result.PASSED)) {
            loggingProfile = new RegressionFailedLoggingProfile();
        }
        AndroidLogListener.writeDeviceLogsToConsole(AndroidLogListener.getInstance(ListenerType.DEFAULT),
                loggingProfile);

        commonSteps.getUserManager().resetUsers();
    }

    /**
     * Rotate device to landscape or portrait
     *
     * @param direction either landscape or portrait
     * @throws Exception
     * @step. ^I rotate UI to (landscape|portrait)$
     */
    @When("^I rotate UI to (landscape|portrait)$")
    public void WhenIRotateUI(String direction) throws Exception {
        if (direction.equals("landscape")) {
            pagesCollection.getCommonPage().rotateLandscape();
        } else {
            pagesCollection.getCommonPage().rotatePortrait();
        }
        // To let the UI to handle up changes
        Thread.sleep(1000);
    }

    /**
     * Checks to see that a device runs the target version, and if not, throws a
     * pending exception to skip this test without failing
     *
     * @throws Exception
     * @step. ^My device runs Android (.*) or higher$
     */
    @Given("^My device runs Android (.*) or higher$")
    public void MyDeviceRunsAndroid(String targetVersion) throws Exception {
        if (AndroidCommonUtils.compareAndroidVersion(targetVersion) < 0) {
            throw new PendingException(
                    "This test isn't suitable to run on " + "anything lower than Android " + targetVersion);
        }
    }

    /**
     * Enable/disable airplane mode
     *
     * @param action either 'enable' or 'disable'
     * @throws Exception
     * @step. ^I (enable|disable) Airplane mode on the device$
     */
    @Given("^I (enable|disable) Airplane mode on the device$")
    public void IChangeAirplaneMode(String action) throws Exception {
        AndroidCommonUtils.setAirplaneMode(action.equals("enable"));
    }

    /**
     * Delete all existing contacts from Address Book.
     * !Be careful to when executing this test on non-testing devices!
     *
     * @throws Exception
     * @step. ^I delete all contacts from Address Book$
     */
    @Given("^I delete all contacts from Address Book$")
    public void IDeleteAllContacts() throws Exception {
        AndroidCommonUtils.clearAllContacts();
    }

    /**
     * Add a new contact into address book
     *
     * @param alias user alias
     * @throws Exception
     * @step. ^I add (.*) into Address Book$
     */
    @Given("^I add (.*) into Address Book$")
    public void IImportUserIntoAddressBook(String alias) throws Exception {
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
        final PhoneNumber phoneNumber = usrMgr.findUserByNameOrNameAlias(alias).getPhoneNumber();
        AndroidCommonUtils.insertContact(name, email, phoneNumber);
    }

    /**
     * Send personal invitation over the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param toMail          the email to send the invitation to
     * @param message         the message for the invitee
     * @throws Exception
     * @step. ^(.*) send personal invitation to mail (.*) with message (.*)
     */
    @When("^(.*) sends personal invitation to mail (.*) with message (.*)")
    public void UserXSendsPersonalInvitation(String userToNameAlias,
                                             String toMail, String message) throws Exception {
        commonSteps.UserXSendsPersonalInvitationWithMessageToUserWithMail(
                userToNameAlias, toMail, message);
    }

    /**
     * Add a custom user to the internal test case users list. The user should be already created on the backend.
     * This might be useful if the user is not created by the test case
     *
     * @param nameAlias user name/alias
     * @throws Exception
     * @step. ^I add (.*) to the list of test case users$"
     */
    @Given("^I add (.*) to the list of test case users$")
    public void IAddUserToTheList(String nameAlias) throws Exception {
        commonSteps.IAddUserToTheListOfTestCaseUsers(nameAlias);
    }

    /**
     * User X removes User Y from a group conversation via backend
     *
     * @param user1
     * @param user2
     * @param group
     * @throws Exception
     * @step. ^(.*) removes (.*) from group (.*)$
     */
    @Given("^(.*) removes (.*) from group (.*)$")
    public void UserXRemovesUserYFromGroup(String user1, String user2, String group) throws Exception {
        user1 = usrMgr.findUserByNameOrNameAlias(user1).getName();
        user2 = usrMgr.findUserByNameOrNameAlias(user2).getName();
        group = usrMgr.replaceAliasesOccurences(group, ClientUsersManager.FindBy.NAME_ALIAS);
        commonSteps.UserXRemoveContactFromGroupChat(user1, user2, group);
    }
}
