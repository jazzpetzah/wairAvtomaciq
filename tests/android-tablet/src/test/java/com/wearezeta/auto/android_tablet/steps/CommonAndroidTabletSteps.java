package com.wearezeta.auto.android_tablet.steps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;

import com.wearezeta.auto.android.common.logging.LoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionFailedLoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionPassedLoggingProfile;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import gherkin.formatter.model.Result;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonAndroidTabletSteps {
    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.http",
                "warn");
    }

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private final ScreenOrientationHelper screenOrientationHelper = ScreenOrientationHelper
            .getInstance();

    private static final Logger log = ZetaLogger
            .getLog(CommonAndroidTabletSteps.class.getSimpleName());

    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    public static final Platform CURRENT_PLATFORM = Platform.Android;

    public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
    public static final int DEFAULT_SWIPE_TIME = 1500;
    private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";

    private static String getUrl() throws Exception {
        return CommonUtils
                .getAndroidAppiumUrlFromConfig(CommonAndroidTabletSteps.class);
    }

    private static String getPath() throws Exception {
        return CommonUtils
                .getAndroidApplicationPathFromConfig(CommonAndroidTabletSteps.class);
    }

    @SuppressWarnings("unchecked")
    public Future<ZetaAndroidDriver> resetAndroidDriver(String url,
                                                        String path, boolean isUnicode) throws Exception {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        LoggingPreferences object = new LoggingPreferences();
        object.enable("logcat", Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, object);
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        // To init the first available device
        capabilities.setCapability("deviceName", "null");
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage",
                CommonUtils.getAndroidPackageFromConfig(this.getClass()));
        capabilities.setCapability("appActivity",
                CommonUtils.getAndroidActivityFromConfig(this.getClass()));
        capabilities
                .setCapability("appWaitActivity", CommonUtils
                        .getAndroidWaitActivitiesFromConfig(this.getClass()));
        capabilities.setCapability("applicationName", "selendroid");
        capabilities.setCapability("automationName", "selendroid");

        if (isUnicode) {
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
        }

        try {
            return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
                    .resetDriver(url, capabilities, 1,
                            this::onDriverInitFinished,
                            this::onDriverInitStarted);
        } catch (SessionNotCreatedException e) {
            // Unlock the screen and retry
            AndroidCommonUtils.unlockScreen();
            Thread.sleep(5000);
            return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
                    .resetDriver(url, capabilities, 1,
                            this::onDriverInitFinished,
                            this::onDriverInitStarted);
        }
    }

    private boolean onDriverInitStarted() {
        try {
            AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
            AndroidCommonUtils.disableHints();
            AndroidCommonUtils.disableHockeyUpdates();
            AndroidCommonUtils.installTestingGalleryApp(this.getClass());
            final String backendJSON = AndroidCommonUtils
                    .createBackendJSON(CommonUtils.getBackendType(this
                            .getClass()));
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
            if (DriverUtils.waitUntilLocatorIsDisplayed(drv, locator,
                    UPDATE_ALERT_VISIBILITY_TIMEOUT)) {
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
            log.error(String
                    .format("UI views have not been initialized properly after %s seconds. Restarting Selendroid usually helps ;-)",
                            INTERFACE_INIT_TIMEOUT_MILLISECONDS));
            throw savedException;
        }
        // Break the glass in case of fire!
        // Just uncomment the following line if Android developers break update
        // alert appearance one more time
        // closeUpdateAlertIfAppears(drv, locator);
    }

    private void initFirstPage(boolean isUnicode) throws Exception {
        AndroidLogListener.getInstance(ListenerType.DEFAULT).start();
        final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(
                getUrl(), getPath(), isUnicode);
        pagesCollection.setFirstPage(new TabletWelcomePage(lazyDriver));
        ZetaFormatter.setLazyDriver(lazyDriver);
        screenOrientationHelper.resetOrientation();
    }

    @Before
    public void setUp() throws Exception {
        initFirstPage(false);
    }

    @After
    public void tearDown() throws Exception {
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
        if (!ZetaFormatter.getRecentTestResult().equals(Result.PASSED.toString())) {
            loggingProfile = new RegressionFailedLoggingProfile();
        }
        AndroidLogListener.writeDeviceLogsToConsole(AndroidLogListener
                .getInstance(ListenerType.DEFAULT), loggingProfile);

        commonSteps.getUserManager().resetUsers();
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
        pagesCollection.getCommonPage().swipeRightCoordinates(
                DEFAULT_SWIPE_TIME);
    }

    @When("^I navigate back$")
    public void INavigateBack() throws Exception {
        pagesCollection.getCommonPage().navigateBack();
    }

    @When("^I swipe left$")
    public void ISwipeLeft() throws Exception {
        pagesCollection.getCommonPage()
                .swipeLeftCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe up$")
    public void ISwipeUp() throws Exception {
        pagesCollection.getCommonPage().swipeUpCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe down$")
    public void ISwipeDown() throws Exception {
        pagesCollection.getCommonPage()
                .swipeDownCoordinates(DEFAULT_SWIPE_TIME);
    }

    /**
     * Sends the application into back stack and displays the home screen or
     * restores it back to foreground
     *
     * @param action either 'minimize' or 'restore'
     * @throws Exception
     * @step. ^I (minimize|restore) the application$
     */
    @When("^I (minimize|restore) the application$")
    public void IMinimizeRestoreApllication(String action) throws Exception {
        switch (action) {
            case "minimize":
                AndroidCommonUtils.tapHomeButton();
                break;
            case "restore":
                AndroidCommonUtils.switchToApplication(
                        CommonUtils.getAndroidPackageFromConfig(this.getClass()),
                        CommonUtils.getAndroidActivityFromConfig(this.getClass()));
                break;
        }
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
     * Rotate device to landscape
     *
     * @throws Exception
     * @step. ^I rotate UI to landscape$
     */
    @When("^I rotate UI to landscape$")
    public void WhenIRotateUILandscape() throws Exception {
        pagesCollection.getCommonPage().rotateLandscape();
        screenOrientationHelper.setOrientation(ScreenOrientation.LANDSCAPE);
    }

    /**
     * Rotate device to portrait
     *
     * @throws Exception
     * @step. ^I rotate UI to portrait$
     */
    @When("^I rotate UI to portrait$")
    public void WhenIRotateUIPortrait() throws Exception {
        pagesCollection.getCommonPage().rotatePortrait();
        screenOrientationHelper.setOrientation(ScreenOrientation.PORTRAIT);
    }

    /**
     * Taps in the center of the screen
     *
     * @throws Exception
     * @step. ^I tap in the center of the screen$
     */
    @When("^I tap in the center of the screen$")
    public void WhenITapOnCenterOfScreen() throws Exception {
        pagesCollection.getCommonPage().tapOnCenterOfScreen();
    }

    /**
     * Verifies that user A has sent a connection request to user B
     *
     * @param userFromNameAlias  the user from which the connection request originated
     * @param usersToNameAliases the target user
     * @step. ^(.*) sent connection request to (.*)$
     */
    @Given("^(.*) sent connection request to (.*)$")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
                                               String usersToNameAliases) throws Exception {
        commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
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
    public void GivenUserHasAnAccentColor(String name, String colorName)
            throws Exception {
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
    public void GivenUserHasAName(String name, String newName) throws Exception {
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
    public void UserIsConnectedTo(String userFromNameAlias,
                                  String usersToNameAliases) throws Exception {
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
    public void UserIsSilenced(String mutedUser, String otherUser)
            throws Exception {
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
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAliases)
            throws Exception {
        commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAliases);
    }

    /**
     * Allows user A to ignore all incoming connection requests
     *
     * @param userToNameAlias the user who will do the ignoring
     * @throws Exception
     * @step. ^(.*) ignores? all requests$
     */
    @When("^(.*) ignores? all requests$")
    public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
            throws Exception {
        commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
    }

    /**
     * Allows test to wait for T seconds
     *
     * @param seconds The number of seconds to wait
     * @throws Exception
     * @step. ^I wait for (.*) second[s]*$
     */
    @When("^I wait for (\\d+) seconds?$")
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
    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
    }

    /**
     * User A accepts all requests
     *
     * @throws Exception
     * @step. ^(.*) accepts? all requests$
     */
    @When("^(.*) accepts? all requests$")
    public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
            throws Exception {
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
    public void UserPingedConversation(String pingFromUserNameAlias,
                                       String dstConversationName) throws Exception {
        commonSteps.UserPingedConversation(pingFromUserNameAlias,
                dstConversationName);
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
    public void UserHotPingedConversation(String hotPingFromUserNameAlias,
                                          String dstConversationName) throws Exception {
        commonSteps.UserHotPingedConversation(hotPingFromUserNameAlias,
                dstConversationName);
    }

    private static final int RANDOM_MSG_LENGTH = 10;

    /**
     * User A sends a simple text message to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param dstUserNameAlias     The user to receive the message
     * @throws Exception
     * @step. ^Contact (.*) sends? message to user (.*)$
     */
    @When("^Contact (.*) sends? message to user (.*)$")
    public void UserSendMessageToContact(String msgFromUserNameAlias,
                                         String dstUserNameAlias) throws Exception {
        commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
                dstUserNameAlias,
                CommonUtils.generateRandomString(RANDOM_MSG_LENGTH));
    }

    /**
     * Send multiple random messages to a user
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param count                count of messages to send
     * @param dstUserNameAlias     The user to receive the message
     * @throws Exception
     * @step. ^Contact (.*) sends? message to user (.*)$
     */
    @When("^Contact (.*) sends? (\\d+) messages? to user (.*)$")
    public void UserSendMultipleMessageToContact(String msgFromUserNameAlias,
                                                 int count, String dstUserNameAlias) throws Exception {
        for (int i = 0; i < count; i++) {
            commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
                    dstUserNameAlias,
                    CommonUtils.generateRandomString(RANDOM_MSG_LENGTH));
        }
    }

    /**
     * Send a particular text message via the backend
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg                  the message to send
     * @param dstUserNameAlias     The user to receive the message
     * @throws Exception
     * @step. ^Contact (.*) sends? message "(.*)" to user (.*)$
     */
    @When("^Contact (.*) sends? message \"(.*)\" to user (.*)$")
    public void UserSendMessageXToContact(String msgFromUserNameAlias,
                                          String msg, String dstUserNameAlias) throws Exception {
        commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
                dstUserNameAlias, msg);
    }

    /**
     * Send a particular text message via the backend
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg                  the message to send
     * @param convoName            destination conversation name
     * @throws Exception
     * @step. ^Contact (.*) sends? message "(.*)" to conversation (.*)$
     */
    @When("^Contact (.*) sends? message \"(.*)\" to conversation (.*)$")
    public void UserSendMessageXToConversation(String msgFromUserNameAlias,
                                               String msg, String convoName) throws Exception {
        commonSteps.UserSentMessageToConversation(msgFromUserNameAlias,
                convoName, msg);
    }

    /**
     * Verifies that there are N new users for a test, and makes them if they
     * don't exist. -unused
     *
     * @param count the number of users to make
     * @throws Exception
     * @step. ^There \\w+ (\\d+) user[s]*$
     */
    @Given("^There \\w+ (\\d+) user[s]*$")
    public void ThereAreNUsers(int count) throws Exception {
        commonSteps.ThereAreNUsers(CURRENT_PLATFORM, count);
    }

    /**
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count       the number of users to make
     * @param myNameAlias the name of the user to set as the current user
     * @throws Throwable
     * @step. ^There \\w+ (\\d+) user[s]* where (.*) is me$
     */
    @Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Throwable {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
                myNameAlias);
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
    }

    /**
     * Verifies that there are N new users for a test all sharing a common
     * prefix in their names and makes them if they don't exist.
     *
     * @param count      the number of users to make
     * @param namePrefix the prefix for all of the users to share
     * @throws Exception
     * @step. ^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$
     */
    @Given("^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$")
    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
            throws Exception {
        commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
    }

    /**
     * Verifies that user A has an avatar matching the picture in file X
     *
     * @param name    the user to check
     * @param picture the file name of the picture to check against. The file name
     *                is relative to the pictures directory as defined in the
     *                Configurations.cnf file
     * @throws Throwable
     * @step. ^(.*) has an avatar picture from file (.*)$
     */
    @Given("^(.*) has an avatar picture from file (.*)$")
    public void GivenUserHasAnAvatarPicture(String name, String picture)
            throws Throwable {
        String picturePath = CommonUtils.getImagesPath(this.getClass()) + "/"
                + picture;
        try {
            name = usrMgr.findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        commonSteps.IChangeUserAvatarPicture(name, picturePath);
    }

    /**
     * Sets the current user to one of the pre-defined users based on the name
     * of that user.
     *
     * @param nameAlias the user to set as current user.
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e$
     */
    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Throwable {
        commonSteps.UserXIsMe(nameAlias);
        GivenUserHasAnAvatarPicture(nameAlias, DEFAULT_USER_AVATAR);
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
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Enable/disable airplane mode
     *
     * @param action either 'enable' or 'disable'
     * @throws Exception
     * @step. ^I (enable|disable) Airplane mode on the device$
     */
    @Given("^I (enable|disable) Airplane mode on the device$")
    public void IChangeAirplaceMode(String action) throws Exception {
        AndroidCommonUtils.setAirplaneMode(action.equals("enable"));
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
    @When("^Contact (.*) sends image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
                                               String imageFileName, String conversationType,
                                               String dstConversationName) throws Exception {
        String imagePath = CommonUtils.getImagesPath(this.getClass())
                + imageFileName;
        commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                imagePath, dstConversationName,
                conversationType.equals("group"));
    }

    /**
     * Verify whether the app is in foreground
     *
     * @param shouldNotBeInForeground equals to null if 'not' part does not exist in step signature
     * @step. ^I see the Wire app is (not )?in foreground$
     */
    @Then("^I see the Wire app is (not )?in foreground$")
    public void ISeeAppInForgeround(String shouldNotBeInForeground)
            throws Exception {
        final String packageId = AndroidCommonUtils
                .getAndroidPackageFromConfig(getClass());
        if (shouldNotBeInForeground == null) {
            Assert.assertTrue("Wire is currently not in foreground",
                    AndroidCommonUtils.isAppInForeground(packageId));
        } else {
            Assert.assertFalse("Wire is currently still in foreground",
                    AndroidCommonUtils.isAppInForeground(packageId));
        }
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
     * @param alias         user alias
     * @param whatToExclude comma-separated list of user properties to exclude from being added to AB.
     *                      Can be email|phone
     * @throws Exception
     * @step. ^I add (.*) into Address Book(?:\s+excluding\s+|\s*)(.*)
     */
    @Given("^I add (.*) into Address Book(?:\\s+excluding\\s+|\\s*)(.*)")
    public void IImportUserIntoAddressBook(String alias, String whatToExclude) throws Exception {
        List<String> excludesList = new ArrayList<>();
        if (whatToExclude != null) {
            excludesList = CommonSteps.splitAliases(whatToExclude.trim());
        }
        final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
        final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
        final PhoneNumber phoneNumber = usrMgr.findUserByNameOrNameAlias(alias).getPhoneNumber();
        if (excludesList.isEmpty()) {
            AndroidCommonUtils.insertContact(name, email, phoneNumber);
        } else if (excludesList.contains("email") && excludesList.contains("phone")) {
            AndroidCommonUtils.insertContact(name);
        } else if (excludesList.contains("email")) {
            AndroidCommonUtils.insertContact(name, email);
        } else if (excludesList.contains("phone")) {
            AndroidCommonUtils.insertContact(name, phoneNumber);
        } else {
            throw new IllegalArgumentException(String.format("Cannot parse what to exclude from '%s'", whatToExclude));
        }
    }
}
