package com.wearezeta.auto.android_tablet.steps;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.AndroidTestContext;
import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.logging.LoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionFailedLoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionPassedLoggingProfile;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.AndroidPagesCollection;
import com.wearezeta.auto.android_tablet.common.AndroidTabletPagesCollection;
import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContext;
import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletBackendSelectPage;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.AppiumServer;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
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

    private static final Logger log = ZetaLogger.getLog(CommonAndroidTabletSteps.class.getSimpleName());

    public static final Platform CURRENT_PLATFORM = Platform.Android;

    public static final Timedelta FIRST_TIME_OVERLAY_TIMEOUT = Timedelta.fromSeconds(5);
    public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
    public static final Timedelta DEFAULT_SWIPE_TIME = Timedelta.fromMilliSeconds(1500);
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
        capabilities.setCapability("newCommandTimeout", AppiumServer.DEFAULT_COMMAND_TIMEOUT.asSeconds());
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
        final AndroidTabletTestContext androidTabletTestContext = new AndroidTabletTestContext(
                scenario,
                new AndroidTabletPagesCollection()
        );
        AndroidTabletTestContextHolder.getInstance().setTestContext(androidTabletTestContext);
        final AndroidTestContext androidTestContext = new AndroidTestContext(
                androidTabletTestContext.getUsersManager(),
                androidTabletTestContext.getDevicesManager(),
                androidTabletTestContext.getCallingManager(),
                androidTabletTestContext.getCommonSteps(),
                scenario, new AndroidPagesCollection()
        );
        AndroidTestContextHolder.getInstance().setTestContext(androidTestContext);

        AppiumServer.getInstance().resetLog();

        if (scenario.getSourceTagNames().contains("@useSpecialEmail")) {
            androidTabletTestContext.getUsersManager().useSpecialEmail();
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
        androidTabletTestContext.getScreenOrientationHelper().resetOrientation();
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
            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
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

        AndroidTestContextHolder.getInstance().getTestContext().reset();
        AndroidTabletTestContextHolder.getInstance().getTestContext().reset();
    }

    private void updateDriver(Future<ZetaAndroidDriver> lazyDriver, boolean hasBackendSelectPopup) throws Exception {
        ZetaFormatter.setLazyDriver(lazyDriver);
        if (AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection().hasPages()) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection().clearAllPages();
        }
        if (hasBackendSelectPopup) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .setFirstPage(new TabletBackendSelectPage(lazyDriver));
            AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getPage(TabletBackendSelectPage.class).waitForInitialScreen();
        } else {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .setFirstPage(new TabletWelcomePage(lazyDriver));
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().hideKeyboard();
    }

    @When("^I swipe right$")
    public void ISwipeRight() throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().swipeRightCoordinates(
                DEFAULT_SWIPE_TIME);
    }

    @When("^I navigate back$")
    public void INavigateBack() throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().navigateBack();
    }

    @When("^I swipe left$")
    public void ISwipeLeft() throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().swipeLeftCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe up$")
    public void ISwipeUp() throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().swipeUpCoordinates(DEFAULT_SWIPE_TIME);
    }

    @When("^I swipe down$")
    public void ISwipeDown() throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().swipeDownCoordinates(DEFAULT_SWIPE_TIME);
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
                AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().rotateLandscape();
                AndroidTabletTestContextHolder.getInstance().getTestContext()
                        .getScreenOrientationHelper().setOriginalOrientation(ScreenOrientation.LANDSCAPE);
                break;
            case "portrait":
                AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().rotatePortrait();
                AndroidTabletTestContextHolder.getInstance().getTestContext()
                        .getScreenOrientationHelper().setOriginalOrientation(ScreenOrientation.PORTRAIT);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().tapOnCenterOfScreen();
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().ConnectionRequestIsSentTo(userFromNameAlias,
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
            name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().IChangeUserAccentColor(name, colorName);
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
            name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().IChangeName(name, newName);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
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
        mutedUser = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(mutedUser).getName();
        otherUser = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(otherUser).getName();

        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().MuteConversationWithUser(otherUser, mutedUser);
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
        mutedGroup = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().replaceAliasesOccurences(mutedGroup, ClientUsersManager.FindBy.NAME_ALIAS);
        otherUser = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(otherUser).getName();

        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().MuteConversationWithGroup(otherUser, mutedGroup);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().IgnoreAllIncomingConnectRequest(userToNameAlias);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().WaitForTime(seconds);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().AcceptAllIncomingConnectionRequests(userToNameAlias);
    }

    /**
     * User A sends a ping to a conversation
     *
     * @param pingFromUserNameAlias The user to do the pinging
     * @param dstConversationName   the target conversation to send the ping to
     * @throws Exception
     * @step. ^User (\w+) (?:securely |\s*)pings? conversation (.*)
     */
    @When("^User (\\w+) (?:securely |\\s*)pings? conversation (.*)")
    public void UserPingedConversation(String pingFromUserNameAlias, String dstConversationName) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSharesLocationTo(userNameAlias, dstNameAlias, convoType.equals("group conversation"),
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
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
            }
        } else {
            if (isEncrypted == null) {
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
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
     *                                 Configuration.properties.
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
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    /**
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count                 the number of users to make
     * @param myNameAlias           the name of the user to set as the current user
     * @param withoutUniqueUsername equals null means the user will use user name as unique user name by default
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me( without unique user name)?$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me( without unique user name)?$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias, String withoutUniqueUsername) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
        if (withoutUniqueUsername == null) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(myNameAlias);
        }
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
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().ThereAreNUsersWhereXIsMeRegOnlyByMail(count, myNameAlias);
        } else {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
        }
        GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(myNameAlias);
    }

    /**
     * Verifies that user A has an avatar matching the picture in file X
     *
     * @param name    the user to check
     * @param picture the file name of the picture to check against. The file name
     *                is relative to the pictures directory as defined in the
     *                Configurations.properties file
     * @throws Exception
     * @step. ^(.*) has an avatar picture from file (.*)$
     */
    @Given("^(.*) has an avatar picture from file (.*)$")
    public void GivenUserHasAnAvatarPicture(String name, String picture) throws Exception {
        String picturePath = CommonUtils.getImagesPathFromConfig(this.getClass()) + File.separator + picture;
        try {
            name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(name).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().IChangeUserAvatarPicture(name, picturePath);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserXIsMe(nameAlias);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
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
     * @param alias          user alias
     * @param withCustomName whether we specify custom name for contact
     * @param customName     specified custom name
     * @param withInfo       whether we add extra infor for this contact in AB
     * @param infoType       which could be phone, email or phone + email
     * @throws Exception
     * @step. ^I add (\\w+)( having custom name "(.*)")? into Address Book( with (phone|email|phone and email))?$
     */
    @Given("^I add (\\w+)( having custom name \"(.*)\")? into Address Book( with (phone|email|phone and email))?$")
    public void IImportUserIntoAddressBook(String alias, String withCustomName, String customName, String withInfo,
                                           String infoType) throws Exception {
        //TODO: Robin handle when custom name contains space
        String name = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(alias).getName();
        if (withCustomName != null) {
            name = customName;
        }
        if (withInfo != null) {
            final String email = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(alias).getEmail();
            final PhoneNumber phoneNumber = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().findUserByNameOrNameAlias(alias).getPhoneNumber();
            AndroidCommonUtils.insertContact(name, email, phoneNumber, infoType);
        } else {
            AndroidCommonUtils.insertContact(name);
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
        expectedMsg = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().replaceAliasesOccurences(expectedMsg, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("An alert containing text '%s' is not visible", expectedMsg),
                AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().isAlertMessageVisible(expectedMsg));
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().WaitUntilTopPeopleContactsIsFoundInSearch(
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().tapAlertButton(caption);
    }

    /**
     * Add one or more remote devices to one or more remote users
     * @step. ^Users? adds? devices? (.*)
     *
     * @param mappingAsJson this should be valid JSON string. Keys are mandatory and
     *                      are interpreted as user names/aliases and values are device(s) info
     *                      mapped to these users. Device info objects can include following
     *                      optional fields:
     *                      name - device name (will be set to random unique value if not set)
     *                      label - the device label (will not be set if missing)
     *                      Examples:
     *                      {"user1Name" : [{}]}
     *                      {"user1Name" : [{}], "user2Name" : [{"name": "blabla", "label": "label"},
     *                                                          {"name": "blabla2", "label": "label2"}]}
     *
     * @param mappingAsJson
     * @throws Exception
     */
    @Given("^Users? adds? the following devices?: (.*)")
    public void UsersAddDevices(String mappingAsJson) throws Exception {
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersAddDevices(mappingAsJson);
    }

    /**
     * Presses the android back button X times
     *
     * @param times how many times to press
     * @throws Exception
     * @step. ^I press [Bb]ack button (\\d+) times?$
     */
    @When("^I press [Bb]ack button (\\d+) times?$")
    public void PressBackButtonXTimes(int times) throws Exception {
        for (int i = 0; i < times; i++) {
            AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getCommonPage().navigateBack();
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
                Timedelta.fromSeconds(timeoutSeconds),
                Timedelta.fromMilliSeconds(1000),
                () -> {
                    if (AndroidCommonUtils.isAppNotInForeground(packageId, FOREGROUND_TIMEOUT_MILLIS)) {
                        AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                                .getCommonPage().navigateBack();
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSentFileToConversation(contact, dstConvoName, sourceFilePath,
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
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserLikeLatestMessage(userNameAlias, dstNameAlias, deviceName);
                break;
            case "unlikes":
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserUnlikeLatestMessage(userNameAlias, dstNameAlias, deviceName);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserDeleteLatestMessage(userNameAlias, dstNameAlias, deviceName, isGroup, isDeleteEverywhere);
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserXRemembersLastMessage(userNameAlias, convoType.equals("group conversation"),
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
                CommonSteps.DEFAULT_WAIT_UNTIL_TIMEOUT.asSeconds()
                : Integer.parseInt(waitDuration.replaceAll("[\\D]", ""));
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserXFoundLastMessageChanged(userNameAlias, convoType.equals("group conversation"), dstNameAlias,
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
        AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UserUpdateLatestMessage(userNameAlias, dstNameAlias, newMessage, deviceName, isGroup);
    }

    /**
     * Assign a unique username
     *
     * @param userAs       user name/alias, who changes the username
     * @param action       either 'sets' or 'changes'. The uniqUsername parameter will be ignored if the action
     *                     is set to 'sets' and the autogenerated unique username, which is available
     *                     as 'user%dUniqueUsername' alias
     * @param uniqUsername unique username or an alias
     * @throws Exception
     * @step. ^User (.*) (sets|changes) the unique username( to ".*")?$
     * @step. ^Users? (.*) (sets?|changes) the unique username( to ".*")?$
     */
    @Given("^Users? (.*) (sets?|changes) the unique username( to \".*\")?$")
    public void UserSetsUniqueUsername(String userAs, String action, String uniqUsername) throws Exception {
        switch (action.toLowerCase()) {
            case "sets":
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(userAs);
                break;
            case "changes":
                if (uniqUsername == null) {
                    throw new IllegalArgumentException("Unique username is mandatory to set");
                }
                // Exclude quotes
                uniqUsername = uniqUsername.substring(5, uniqUsername.length() - 1);
                uniqUsername = AndroidTabletTestContextHolder.getInstance().getTestContext().getUsersManager().replaceAliasesOccurences(uniqUsername,
                        ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
                AndroidTabletTestContextHolder.getInstance().getTestContext().getCommonSteps().IChangeUniqueUsername(userAs, uniqUsername);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action '%s'", action));

        }
    }
}