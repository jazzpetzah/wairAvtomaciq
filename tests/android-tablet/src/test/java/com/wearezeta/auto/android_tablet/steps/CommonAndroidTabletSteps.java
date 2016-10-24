package com.wearezeta.auto.android_tablet.steps;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.logging.LoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionFailedLoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionPassedLoggingProfile;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.TabletBackendSelectPage;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.AppiumServer;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.Result;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CommonAndroidTabletSteps {
    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.http",
                "warn");
    }

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private final ScreenOrientationHelper screenOrientationHelper = ScreenOrientationHelper.getInstance();

    private static final Logger log = ZetaLogger.getLog(CommonAndroidTabletSteps.class.getSimpleName());

    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    public static final Platform CURRENT_PLATFORM = Platform.Android;

    public static final int FIRST_TIME_OVERLAY_TIMEOUT = 5; // seconds
    public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
    public static final int DEFAULT_SWIPE_TIME = 1500;
    private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";

    private static String getUrl() throws Exception {
        return CommonUtils.getAndroidAppiumUrlFromConfig(CommonAndroidTabletSteps.class);
    }

    private static String getPath() throws Exception {
        return CommonUtils.getAndroidApplicationPathFromConfig(CommonAndroidTabletSteps.class);
    }

    private boolean isAutoAcceptOfSecurityAlertsEnabled = false;

    @SuppressWarnings("unchecked")
    public Future<ZetaAndroidDriver> resetAndroidDriver(String url, String path,
                                                        Optional<Map<String, Object>> additionalCaps) throws Exception {
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        capabilities.setCapability("newCommandTimeout", AppiumServer.DEFAULT_COMMAND_TIMEOUT);
        // To init the first available device
        capabilities.setCapability("deviceName", "null");
        capabilities.setCapability("app", path);
        capabilities.setCapability("appPackage", CommonUtils.getAndroidPackageFromConfig(getClass()));
        capabilities.setCapability("appActivity", CommonUtils.getAndroidLaunchActivity(getClass()));
        capabilities.setCapability("automationName", "Selendroid");
        if (additionalCaps.isPresent()) {
            for (Map.Entry<String, Object> entry : additionalCaps.get().entrySet()) {
                capabilities.setCapability(entry.getKey(), entry.getValue());
            }
        }

        devicePreparationThread.get(DEVICE_PREPARATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance().resetDriver(url, capabilities,
                AndroidPage.DRIVER_CREATION_RETRIES_COUNT, this::onDriverInitFinished, null);
    }

    private static Void prepareDevice() throws Exception {
        AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
        AndroidCommonUtils.disableHockeyUpdates();
        AndroidCommonUtils.installTestingGalleryApp(CommonAndroidTabletSteps.class);
        AndroidCommonUtils.changeAccelerometerState(true);
        return null;
    }

    private static final Future<Void> devicePreparationThread;

    static {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        devicePreparationThread = pool.submit(CommonAndroidTabletSteps::prepareDevice);
        pool.shutdown();
    }

    private static final int DEVICE_PREPARATION_TIMEOUT_SECONDS = 20;

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
        // This is needed to make testing on Android 6+ with Selendroid possible
        try {
            if (isAutoAcceptOfSecurityAlertsEnabled &&
                    ((ZetaAndroidDriver) drv).getOSVersion().compareTo(new DefaultArtifactVersion("6.0")) >= 0) {
                AndroidCommonUtils.grantPermissionsTo(CommonUtils.getAndroidPackageFromConfig(getClass()),
                        AndroidCommonUtils.STANDARD_WIRE_PERMISSIONS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final long millisecondsStarted = System.currentTimeMillis();
        WebDriverException savedException = null;
        do {
            try {
                DriverUtils.waitUntilLocatorIsDisplayed(drv, AndroidPage.xpathDismissUpdateButton, 1);
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
                    .format("UI views were not initialized properly after %s seconds. Restarting Selendroid usually helps ;-)",
                            INTERFACE_INIT_TIMEOUT_MILLISECONDS));
            throw savedException;
        }
        // Break the glass in case of fire!
        // Just uncomment the following line if Android developers break update
        // alert appearance one more time
        // closeUpdateAlertIfAppears(drv, locator);
    }

    private static boolean isLogcatEnabled = true;

    static {
        try {
            isLogcatEnabled = CommonUtils.getAndroidShowLogcatFromConfig(CommonAndroidTabletSteps.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        try {
            SEBridge.getInstance().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppiumServer.getInstance().resetLog();

        if (scenario.getSourceTagNames().contains("@useSpecialEmail")) {
            usrMgr.useSpecialEmail();
        }

        isAutoAcceptOfSecurityAlertsEnabled = !scenario.getSourceTagNames().contains("@noAcceptAlert");

        if (isLogcatEnabled) {
            AndroidLogListener.getInstance(ListenerType.DEFAULT).start();
        }

        Map<String, Object> additionalCapsMap = new HashMap<>();
        if (!CommonUtils.getHasBackendSelection(getClass())) {
            additionalCapsMap.put("appActivity", CommonUtils.getAndroidMainActivityFromConfig(getClass()));
            additionalCapsMap.put("appWaitActivity", CommonUtils.getAndroidLoginActivityFromConfig(getClass()));
        }

        final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(getUrl(), getPath(),
                additionalCapsMap.isEmpty() ? Optional.empty() : Optional.of(additionalCapsMap));

        updateDriver(lazyDriver, CommonUtils.getHasBackendSelection(getClass()));
        screenOrientationHelper.resetOrientation();
    }

    @After
    public void tearDown(Scenario scenario) {
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

        try {
            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            usrMgr.resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoggingProfile loggingProfile = new RegressionPassedLoggingProfile();
        if (!scenario.getStatus().equals(Result.PASSED)) {
            loggingProfile = new RegressionFailedLoggingProfile();
        }
        if (isLogcatEnabled) {
            try {
                AndroidLogListener.writeDeviceLogsToConsole(AndroidLogListener
                        .getInstance(ListenerType.DEFAULT), loggingProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AndroidLogListener.forceStopAll();
    }

    private void updateDriver(Future<ZetaAndroidDriver> lazyDriver, boolean hasBackendSelectPopup) throws Exception {
        ZetaFormatter.setLazyDriver(lazyDriver);
        if (pagesCollection.hasPages()) {
            pagesCollection.clearAllPages();
        }
        if (hasBackendSelectPopup) {
            pagesCollection.setFirstPage(new TabletBackendSelectPage(lazyDriver));
            pagesCollection.getPage(TabletBackendSelectPage.class).waitForInitialScreen();
        } else {
            pagesCollection.setFirstPage(new TabletWelcomePage(lazyDriver));
        }
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
                AndroidCommonUtils.switchToApplication(CommonUtils.getAndroidPackageFromConfig(this.getClass()));
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
            AndroidCommonUtils.switchToApplication(CommonUtils.getAndroidPackageFromConfig(this.getClass()));
        }
    }

    /**
     * Rotate device
     *
     * @param orientation either landscape or portrait
     * @throws Exception
     * @step. ^I rotate UI to (landscape|portrait)$
     */
    @When("^I rotate UI to (landscape|portrait)$")
    public void WhenIRotateUILandscape(String orientation) throws Exception {
        switch (orientation.toLowerCase()) {
            case "landscape":
                pagesCollection.getCommonPage().rotateLandscape();
                screenOrientationHelper.setOriginalOrientation(ScreenOrientation.LANDSCAPE);
                break;
            case "portrait":
                pagesCollection.getCommonPage().rotatePortrait();
                screenOrientationHelper.setOriginalOrientation(ScreenOrientation.PORTRAIT);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown orientation value '%s'", orientation));
        }
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
     * @throws Exception
     * @step. ^(.*) has an accent color (.*)$
     */
    @Given("^(.*) has an accent color (.*)$")
    public void GivenUserHasAnAccentColor(String name, String colorName) throws Exception {
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
     * @throws Exception
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
     * @param isSecure              equals null if ping should not be secure
     * @throws Exception
     * @step. ^User (\\w+) (securely )?pings? conversation (.*)$
     */
    @When("^User (\\w+) (securely )?pings? conversation (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias,
                                       String isSecure, String dstConversationName) throws Exception {
        if (isSecure == null) {
            commonSteps.UserPingedConversation(pingFromUserNameAlias, dstConversationName);
        } else {
            commonSteps.UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
        }
    }

    /**
     * Send location sharing message
     *
     * @param userNameAlias sender name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  user name/alias or group conversation name
     * @param deviceName    destination device
     * @throws Exception
     * @step. ^User (.*) shares? his location to (user|group conversation) (.*) via device (.*)
     */
    @When("^User (.*) shares? his location to (user|group conversation) (.*) via device (.*)")
    public void UserXSharesLocationTo(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        commonSteps.UserSharesLocationTo(userNameAlias, dstNameAlias, convoType.equals("group conversation"),
                deviceName);
    }

    /**
     * User A sends a simple text message to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg                  a message to send. Random string will be sent if it is empty
     * @param deviceName           the device to use when using encryption
     * @param dstConvoName         The user to receive the message
     * @param isEncrypted          whether the message has to be encrypted
     * @param convoType            either 'user' or 'group conversation'
     * @throws Exception
     * @step. ^User (.*) sends? (encrypted )?message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$
     */
    @When("^User (.*) sends? (encrypted )?message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias, String isEncrypted,
                                              String msg, String deviceName, String convoType, String dstConvoName) throws
            Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0) ?
                CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            if (isEncrypted == null) {
                commonSteps.UserSentMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
            }
        } else {
            if (isEncrypted == null) {
                commonSteps.UserSentMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
            }
        }
    }

    /**
     * User A sends specified number of simple text messages to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param count                number of messages to send
     * @param dstUserNameAlias     The user to receive the message
     * @param deviceName           the device to use when using encryption
     * @param areEncrypted         Whether the messages should be encrypted
     * @throws Exception
     * @step. ^User (.*) sends? (\\d+) (encrypted )?messages? (?:via device (.*)\\s)?to user (.*)$
     */
    @When("^User (.*) sends? (\\d+) (encrypted )?messages? (?:via device (.*)\\s)?to user (.*)$")
    public void UserSendXMessagesToConversation(String msgFromUserNameAlias, int count, String areEncrypted,
                                                String deviceName, String dstUserNameAlias) throws Exception {
        for (int i = 0; i < count; i++) {
            UserSendMessageToConversation(msgFromUserNameAlias, areEncrypted, null, deviceName, "user", dstUserNameAlias);
        }
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param isEncrypted              whether the image has to be encrypted
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName            the file path name of the image to send. The path name is
     *                                 defined relative to the image file defined in
     *                                 Configuration.cnf.
     * @param conversationType         "single user" or "group" conversation.
     * @param dstConversationName      the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)$
     */
    @When("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)$")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias, String isEncrypted,
                                               String imageFileName, String conversationType,
                                               String dstConversationName) throws Exception {
        final String imagePath = CommonUtils.getImagesPathFromConfig(getClass()) + imageFileName;
        final boolean isGroup = conversationType.equals("group");
        if (isEncrypted == null) {
            commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            commonSteps.UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    /**
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count       the number of users to make
     * @param myNameAlias the name of the user to set as the current user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
    }

    /**
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count       the number of users to make
     * @param what        either 'email' or 'phone number'
     * @param myNameAlias the name of the user to set as the current user
     * @throws Exception
     * @step. ^There (?:are|is) (\d+) users? with (email address|phone number) only where (.*) is me$
     */
    @Given("^There (?:are|is) (\\d+) users? with (email address|phone number) only where (.*) is me$")
    public void ThereAreNUsersWithZOnlyWhereXIsMe(int count, String what, String myNameAlias) throws Exception {
        if (what.equals("email address")) {
            commonSteps.ThereAreNUsersWhereXIsMeRegOnlyByMail(count, myNameAlias);
        } else {
            commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
        }
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
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
        String picturePath = CommonUtils.getImagesPathFromConfig(this.getClass()) + File.separator + picture;
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

    private static final long FOREGROUND_TIMEOUT_MILLIS = 5000;

    /**
     * Verify whether the app is in foreground
     *
     * @param shouldNotBeInForeground equals to null if 'not' part does not exist in step signature
     * @step. ^I see the Wire app is (not )?in foreground$
     */
    @Then("^I see the Wire app is (not )?in foreground$")
    public void ISeeAppInForgeround(String shouldNotBeInForeground) throws Exception {
        final String packageId = AndroidCommonUtils.getAndroidPackageFromConfig(getClass());
        if (shouldNotBeInForeground == null) {
            Assert.assertTrue("Wire is currently not in foreground",
                    AndroidCommonUtils.isAppInForeground(packageId, FOREGROUND_TIMEOUT_MILLIS));
        } else {
            Assert.assertTrue("Wire is currently still in foreground",
                    AndroidCommonUtils.isAppNotInForeground(packageId, FOREGROUND_TIMEOUT_MILLIS));
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
     * @step. ^I add (.*) into Address Book(?:\s+excluding\s+|\s*)(.*)$
     */
    @Given("^I add (.*) into Address Book(?:\\s+excluding\\s+|\\s*)(.*)$")
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

    /**
     * Checks to see that an alert message contains the correct text
     *
     * @param expectedMsg the expected error message
     * @throws Exception
     * @step. ^I see alert message containing \"(.*)\"$
     */
    @Then("^I see alert message containing \"(.*)\"$")
    public void ISeeAlertMessage(String expectedMsg) throws Exception {
        expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("An alert containing text '%s' is not visible", expectedMsg),
                pagesCollection.getCommonPage().isAlertMessageVisible(expectedMsg));
    }

    /**
     * Pings BackEnd until user is indexed and available in top people
     *
     * @param searchByNameAlias user name to search string
     * @param size              number of top people
     * @throws Exception
     * @step. ^(\w+) (?:wait|waits) until (\d+) (?:person|people) (?:is|are) in the Top People list on the backend$
     */
    @Given("^(\\w+) (?:wait|waits) until (\\d+) (?:person|people) (?:is|are) in the Top People list on the backend$")
    public void UserWaitsUntilContactExistsInTopPeopleResults(String searchByNameAlias, int size) throws Exception {
        commonSteps.WaitUntilTopPeopleContactsIsFoundInSearch(
                searchByNameAlias, size);
    }

    /**
     * Tap the corresponding button on alert message
     *
     * @param caption button caption as it is shown in @value property
     * @throws Exception
     * @step. ^I tap (.*) button on the alert$
     */
    @And("^I tap (.*) button on the alert$")
    public void ITapAlertButton(String caption) throws Exception {
        pagesCollection.getCommonPage().tapAlertButton(caption);
    }

    /**
     * User adds multiple devices to his list of devices
     *
     * @param userNameAlias user name/alias
     * @param deviceNames   unique name of devices, comma-separated list
     * @throws Exception
     * @step. User (.*) adds new devices (.*)$
     */
    @When("^User (.*) adds new devices? (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias, String deviceNames) throws Exception {
        final List<String> names = CommonSteps.splitAliases(deviceNames);
        final int poolSize = 2;  // Runtime.getRuntime().availableProcessors()
        final ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        for (String name : names) {
            pool.submit(() -> {
                try {
                    commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, name, CommonUtils.generateRandomString(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        final int secondsTimeout = (names.size() / poolSize + 1) * 60;
        if (!pool.awaitTermination(secondsTimeout, TimeUnit.SECONDS)) {
            throw new IllegalStateException(String.format(
                    "Devices '%s' were not created within %s seconds timeout", names, secondsTimeout));
        }
    }

    /**
     * Presses the android back button X times
     *
     * @param times how many times to press
     * @throws Exception
     * @step. ^I press [Bb]ack button (\\d+) times$
     */
    @When("^I press [Bb]ack button (\\d+) times$")
    public void PressBackButtonXTimes(int times) throws Exception {
        for (int i = 0; i < times; i++) {
            pagesCollection.getCommonPage().navigateBack();
        }
    }

    /**
     * Prepare file in /mnt/sdcard/Download/
     *
     * @param size         such as 5MB, 30MB
     * @param fileFullName the name of the file with extension
     * @throws Exception
     * @step. ^I push (.*) file having name \"(.*)\" to the device$
     */
    @Given("^I push ([^\\s-]*) (video )?file having name \"(.*)\" to the device$")
    public void IPushXFileHavingNameYToDevice(String size, String isVideoFile, String fileFullName) throws Exception {
        AndroidCommonUtils.pushRandomFileToSdcardDownload(fileFullName, size, isVideoFile != null);
    }

    /**
     * Prepare file in /mnt/sdcard/Download/
     *
     * @param fileFullName the name of file that located in Android Tool Path
     * @throws Exception
     * @step. ^I push local file named \"(.*)\" to the device$
     */
    @Given("^I push local file named \"(.*)\" to the device$")
    public void IPushLocalFileNamedYToDevice(String fileFullName) throws Exception {
        AndroidCommonUtils.pushLocalFileToSdcardDownload(fileFullName);
    }

    /**
     * Press back button until Wire app is in foreground
     *
     * @param timeoutSeconds timeout in seconds for try process
     * @throws Exception
     * @step. ^I press [Bb]ack button until Wire app is in foreground in (\d+) seconds$
     */
    @When("^I press [Bb]ack button until Wire app is in foreground in (\\d+) seconds$")
    public void IPressBackButtonUntilWireAppInForeground(int timeoutSeconds) throws Exception {
        final String packageId = AndroidCommonUtils.getAndroidPackageFromConfig(getClass());
        CommonUtils.waitUntilTrue(
                timeoutSeconds,
                1000,
                () -> {
                    if (AndroidCommonUtils.isAppNotInForeground(packageId, FOREGROUND_TIMEOUT_MILLIS)) {
                        pagesCollection.getCommonPage().navigateBack();
                    }
                    return AndroidCommonUtils.isAppInForeground(packageId, FOREGROUND_TIMEOUT_MILLIS);
                }
        );
    }

    /**
     * Send a local file from SE
     *
     * @param contact      user name/alias
     * @param fileFullName the name of an existing file
     * @param mimeType     MIME type of the file, for example text/plain. Check
     *                     http://www.freeformatter.com/mime-types-list.html to get the full list of available MIME
     *                     types
     * @param convoType    either 'single user' or 'group'
     * @param dstConvoName conversation name
     * @param deviceName   the name of user device. The device will be created automatically if it does not exist yet
     * @throws Exception
     * @step. ^(.*) sends local file named "(.*)" and MIME type "(.*)" via device (.*) to (user|group conversation) (.*)$
     */
    @When("^(.*) sends local file named \"(.*)\" and MIME type \"(.*)\" via device (.*) to (user|group conversation) (.*)$")
    public void ContactSendsXLocalFileFromSE(String contact, String fileFullName, String mimeType,
                                             String deviceName, String convoType, String dstConvoName) throws Exception {
        String basePath = CommonUtils.getAudioPathFromConfig(getClass());
        String sourceFilePath = basePath + File.separator + fileFullName;

        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserSentFileToConversation(contact, dstConvoName, sourceFilePath,
                mimeType, deviceName, isGroup);
    }

    /**
     * User X react(like or unlike) the recent message in 1:1 conversation or group conversation
     *
     * @param userNameAlias User X's name or alias
     * @param reactionType  User X's reaction , could be like or unlike, be careful you should use like before unlike
     * @param dstNameAlias  the conversation which message is belong to
     * @param deviceName    User X's device
     * @throws Exception
     * @step. ^User (.*) (likes|unlikes) the recent message from (?:user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) (likes|unlikes) the recent message from (?:user|group conversation) (.*) via device (.*)$")
    public void UserReactLastMessage(String userNameAlias, String reactionType, String dstNameAlias, String deviceName)
            throws Exception {
        switch (reactionType.toLowerCase()) {
            case "likes":
                commonSteps.UserLikeLatestMessage(userNameAlias, dstNameAlias, deviceName);
                break;
            case "unlikes":
                commonSteps.UserUnlikeLatestMessage(userNameAlias, dstNameAlias, deviceName);
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the reaction type '%s'", reactionType));
        }
    }

    /**
     * User X delete message from User/Group via specified device
     * Note : The recent message means the recent message sent from specified device by SE, the device should online.
     *
     * @param userNameAlias    user name/alias
     * @param deleteEverywhere not null means delete everywhere, otherwise delete local
     * @param convoType        either 'user' or 'group conversation'
     * @param dstNameAlias     destination user name/alias or group convo name
     * @param deviceName       source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) deletes? the recent message (everywhere )?from (user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) deletes? the recent message (everywhere )?from (user|group conversation) (.*) via device (.*)$")
    public void UserXDeleteLastMessage(String userNameAlias, String deleteEverywhere, String convoType,
                                       String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        boolean isDeleteEverywhere = deleteEverywhere != null;
        commonSteps.UserDeleteLatestMessage(userNameAlias, dstNameAlias, deviceName, isGroup, isDeleteEverywhere);
    }

    /**
     * Remember the recent message Id
     *
     * @param userNameAlias user name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  destination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) remembers? the recent message from (user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) remembers? the recent message from (user|group conversation) (.*) via device (.*)$")
    public void UserXRemembersLastMessage(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        commonSteps.UserXRemembersLastMessage(userNameAlias, convoType.equals("group conversation"),
                dstNameAlias, deviceName);
    }

    /**
     * Check the remembered message is changed
     *
     * @param userNameAlias user name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  destination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is
     * changed( in \\d+ seconds?)?$
     */
    @Then("^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is " +
            "changed( in \\d+ seconds?)?$")
    public void UserXFoundLastMessageChanged(String userNameAlias, String convoType, String dstNameAlias,
                                             String deviceName, String waitDuration) throws Exception {
        final int durationSeconds = (waitDuration == null) ?
                CommonSteps.DEFAULT_WAIT_UNTIL_TIMEOUT_SECONDS
                : Integer.parseInt(waitDuration.replaceAll("[\\D]", ""));
        commonSteps.UserXFoundLastMessageChanged(userNameAlias, convoType.equals("group conversation"), dstNameAlias,
                deviceName, durationSeconds);
    }

    /**
     * User X edit his own messages, be careful this message will not control the type of the message you edit.
     *
     * @param userNameAlias user name/alias
     * @param newMessage    the message you want to update to
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  estination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) edits? the recent message to "(.*)" from (user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) edits? the recent message to \"(.*)\" from (user|group conversation) (.*) via device (.*)$")
    public void UserXEditLastMessage(String userNameAlias, String newMessage, String convoType,
                                     String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserUpdateLatestMessage(userNameAlias, dstNameAlias, newMessage, deviceName, isGroup);
    }
}