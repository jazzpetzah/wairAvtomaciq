package com.wearezeta.auto.android.steps;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.logging.LoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionFailedLoggingProfile;
import com.wearezeta.auto.android.common.logging.RegressionPassedLoggingProfile;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.SecurityAlertPage;
import com.wearezeta.auto.android.pages.registration.BackendSelectPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.android.tools.WireDatabase;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.FileInfo;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.AppiumServer;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import cucumber.api.PendingException;
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
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CommonAndroidSteps {

    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
    }

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private static final Logger log = ZetaLogger.getLog(CommonAndroidSteps.class.getSimpleName());

    private final ElementState screenState = new ElementState(
            () -> pagesCollection.getCommonPage().takeScreenshot().orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of the whole screen")
            )
    );

    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    public static final Platform CURRENT_PLATFORM = Platform.Android;

    public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
    public static final int DEFAULT_SWIPE_TIME = 1500;
    public static final int FIRST_TIME_OVERLAY_TIMEOUT = 3; // seconds
    public static final int UI_DELAY_TIME = 2; // seconds
    private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";
    private static final String GCM_TOKEN_PATTERN = "token:\\s+(.*)$";
    //TODO: should I move this list to configuration file?
    private static final String[] wirePackageList = {"com.wire.candidate", "com.wire.x", "com.waz.zclient.dev", "com.wire" +
            ".qaavs"};

    private static String getUrl() throws Exception {
        return CommonUtils.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class);
    }

    private static String getPath() throws Exception {
        return CommonUtils.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
    }

    private static String getOldPath() throws Exception {
        return CommonUtils.getOldAppPathFromConfig(CommonAndroidSteps.class);
    }

    private static String getPackageName() throws Exception {
        return CommonUtils.getAndroidPackageFromConfig(CommonAndroidSteps.class);
    }

    @SuppressWarnings("unchecked")
    public Future<ZetaAndroidDriver> resetAndroidDriver(String url, String path, int retriesCount,
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
                retriesCount, this::onDriverInitFinished, null);
    }

    private static Void prepareDevice() throws Exception {
        AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
        AndroidCommonUtils.disableHockeyUpdates();
        AndroidCommonUtils.installTestingGalleryApp(CommonAndroidSteps.class);
        AndroidCommonUtils.installClipperApp(CommonAndroidSteps.class);
        return null;
    }

    private static final Future<Void> devicePreparationThread;

    static {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        devicePreparationThread = pool.submit(CommonAndroidSteps::prepareDevice);
        pool.shutdown();
    }

    private static final int DEVICE_PREPARATION_TIMEOUT_SECONDS = 30;

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

        try {
            if (isAutoAnswerCallEnabled) {
                AndroidCommonUtils.enableAutoAnswerCall(getClass());
            }
        } catch (Exception e) {
            Throwables.propagate(e);
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

    private static boolean isLogcatEnabled = true;

    static {
        try {
            isLogcatEnabled = CommonUtils.getAndroidShowLogcatFromConfig(CommonAndroidSteps.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAutoAcceptOfSecurityAlertsEnabled = false;
    private boolean isAutoAnswerCallEnabled = false;

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

        isAutoAnswerCallEnabled = scenario.getSourceTagNames().contains("@calling_autoAnswer");

        //Start Listenter
        int retriesCount = AndroidPage.DRIVER_CREATION_RETRIES_COUNT;
        if (scenario.getSourceTagNames().contains("@performance")) {
            AndroidLogListener.getInstance(ListenerType.PERF).start();
            retriesCount++;
        } else if (scenario.getSourceTagNames().contains("@analytics")) {
            AndroidLogListener.getInstance(ListenerType.ANALYTICS).start();
        } else if (scenario.getSourceTagNames().contains("@GCMToken")) {
            AndroidLogListener.getInstance(ListenerType.GCMToken).start();
        }
        if (isLogcatEnabled) {
            AndroidLogListener.getInstance(ListenerType.DEFAULT).start();
        }

        String appPath = getPath();
        if (scenario.getSourceTagNames().contains("@upgrade")) {
            appPath = getOldPath();
        }

        Map<String, Object> additionalCapsMap = new HashMap<>();
        if (!CommonUtils.getHasBackendSelection(getClass())) {
            additionalCapsMap.put("appActivity", CommonUtils.getAndroidMainActivityFromConfig(getClass()));
            additionalCapsMap.put("appWaitActivity", CommonUtils.getAndroidLoginActivityFromConfig(getClass()));
        }
        final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(getUrl(), appPath, retriesCount,
                additionalCapsMap.isEmpty() ? Optional.empty() : Optional.of(additionalCapsMap));

        updateDriver(lazyDriver, CommonUtils.getHasBackendSelection(getClass()));
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

        // Clear all contacts in address book
        try {
            AndroidCommonUtils.clearAllContacts();
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
                AndroidLogListener.writeDeviceLogsToConsole(AndroidLogListener.getInstance(ListenerType.DEFAULT),
                        loggingProfile);
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
            pagesCollection.setFirstPage(new BackendSelectPage(lazyDriver));
            pagesCollection.getPage(BackendSelectPage.class).waitForInitialScreen();
        } else {
            pagesCollection.setFirstPage(new WelcomePage(lazyDriver));
        }
    }

    /**
     * Install new Wire build taken from appPath Maven variable, but don't override the current state
     *
     * @throws Exception
     * @step. ^I upgrade Wire to the recent version$
     */
    @When("^I upgrade Wire to the recent version$")
    public void IUpgradeWire() throws Exception {
        final String appPath = getPath();
        AndroidCommonUtils.stopPackage(getPackageName());
        AndroidCommonUtils.installApp(new File(appPath));
        final Map<String, Object> customCaps = new HashMap<>();
        customCaps.put("noReset", true);
        customCaps.put("fullReset", false);
        customCaps.put("skipUninstall", true);
        customCaps.put("appActivity", CommonUtils.getAndroidMainActivityFromConfig(getClass()));
        final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(getUrl(), appPath, 1, Optional.of(customCaps));
        updateDriver(lazyDriver, false);
    }

    /**
     * Presses the android back button
     *
     * @throws Exception
     * @step. ^I press [Bb]ack button$
     */
    @When("^I press [Bb]ack button$")
    public void PressBackButton() throws Exception {
        pagesCollection.getCommonPage().navigateBack();
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
     * User leaves group chat
     *
     * @param userName name of the user who leaves
     * @param chatName chat name that user leaves
     * @throws Exception
     * @step. ^(.*) leave(s) group chat (.*)$
     */
    @Given("^(.*) leave[s]* group chat (.*)$")
    public void UserLeavesGroupChat(String userName, String chatName) throws Exception {
        commonSteps.UserXLeavesGroupChat(userName, chatName);
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

    /**
     * Do swipe gesture on the current screen
     *
     * @param direction one of possible direction values
     * @throws Exception
     * @step. ^I swipe (right|left|up|down)$
     */
    @When("^I swipe (right|left|up|down)$")
    public void ISwipeRight(String direction) throws Exception {
        switch (direction.toLowerCase()) {
            case "right":
                pagesCollection.getCommonPage().swipeRightCoordinates(DEFAULT_SWIPE_TIME);
                break;
            case "left":
                pagesCollection.getCommonPage().swipeLeftCoordinates(DEFAULT_SWIPE_TIME);
                break;
            case "up":
                pagesCollection.getCommonPage().swipeUpCoordinates(DEFAULT_SWIPE_TIME);
                break;
            case "down":
                pagesCollection.getCommonPage().swipeDownCoordinates(DEFAULT_SWIPE_TIME);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown swipe direction '%s'", direction));
        }
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
     * Sends the application into back stack and displays the home screen or vice versa
     *
     * @param action either minimize or restore
     * @throws Exception
     * @step. ^I (minimize|restore) the application$
     */
    @When("^I (minimize|restore) the application$")
    public void IMinimizeApplication(String action) throws Exception {
        if (action.equals("minimize")) {
            AndroidCommonUtils.tapHomeButton();
            WaitForTime(UI_DELAY_TIME);
        } else {
            AndroidCommonUtils.switchToApplication(getPackageName());
            WaitForTime(UI_DELAY_TIME);
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
            AndroidCommonUtils.switchToApplication(getPackageName());
        }
    }

    /**
     * Takes 1st screenshot for comparison, previous taken screenshots will be cleaned
     *
     * @throws Exception
     * @step. ^I take screenshot$
     */
    @When("^I take screenshot$")
    public void ITake1stScreenshot() throws Exception {
        screenState.remember();
    }

    /**
     * A user adds another user to a group chat
     *
     * @param user          that adds someone to a chat
     * @param userToBeAdded user that gets added by someone
     * @param group         group chat you get added to
     * @throws Exception
     * @step. ^User (.*) adds [Uu]ser (.*) to group chat (.*)$
     */
    @When("^User (.*) adds [Uu]ser (.*) to group chat (.*)$")
    public void UserAddsUserToGroupChat(String user, String userToBeAdded, String group) throws Exception {
        commonSteps.UserXAddedContactsToGroupChat(user, userToBeAdded, group);
    }

    /**
     * Taps on the center of the screen
     *
     * @throws Throwable
     * @step. ^I tap on center of screen$
     */
    @When("^I tap on center of screen")
    public void ITapOnCenterOfScreen() throws Throwable {
        pagesCollection.getCommonPage().tapByCoordinates(50, 40);
    }

    /**
     * Compare that 1st and 2nd screenshots are equal/not equal
     *
     * @param shouldBeEqual equals to null if screenshots should be different
     * @step. ^I verify the previous and the current screenshots are( not)? different$
     */
    @Then("^I verify the previous and the current screenshots are( not)? different$")
    public void ThenICompare1st2ndScreenshotsAndTheyAreDifferent(String shouldBeEqual) throws Exception {
        final int timeoutSeconds = 10;
        if (shouldBeEqual == null) {
            Assert.assertTrue(
                    String.format("The current screen state seems to be similar to the previous one after %s seconds",
                            timeoutSeconds), screenState.isChanged(timeoutSeconds, 0.98));
        } else {
            Assert.assertTrue(
                    String.format("The current screen state seems to be different to the previous one after %s seconds",
                            timeoutSeconds), screenState.isNotChanged(timeoutSeconds, 0.75));
        }
    }

    /**
     * Verifies that user A has sent a connection request to user B
     *
     * @param userFromNameAlias  the user from which the connection request originated
     * @param usersToNameAliases the target user
     * @step. ^(.*) sent connection request to (.*)$
     */
    @Given("^(.*) sent connection request to (.*)$")
    public void ConnectionRequestIsSentTo(String userFromNameAlias, String usersToNameAliases) throws Throwable {
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
    public void UserHasAnAvatarPicture(String name, String picture) throws Exception {
        String picturePath = CommonUtils.getImagesPathFromConfig(getClass()) + picture;
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
    public void UserHasAnAccentColor(String name, String colorName) throws Throwable {
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
    public void UserHasAName(String name, String newName) throws Throwable {
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
    @And("^I wait for\\s*(\\d+) seconds?$")
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
     * User X typing in specified conversation
     *
     * @param fromUserNameAlias   The user who is typing
     * @param dstConversationName The conversation where the user is typing
     * @throws Exception
     * @step. ^User (\w+) is typing in the conversation (.*)$
     */
    @When("^User (\\w+) is typing in the conversation (.*)$")
    public void UserTypingInConversation(String fromUserNameAlias, String dstConversationName) throws Exception {
        commonSteps.UserIsTypingInConversation(fromUserNameAlias, dstConversationName);
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
     * Verifies that there are N new users for a test, makes them if they don't
     * exist, and sets one of those users to be the current user.
     *
     * @param count       the number of users to make
     * @param myNameAlias the name of the user to set as the current user
     * @throws Exception
     * @step. ^There (?:are|is) (\\d+) user[s]* where (.*) is me$
     */
    @Given("^There (?:are|is) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        UserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
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
        UserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
    }

    /**
     * Verifies that there are N new users for a test all sharing a common
     * prefix in their names and makes them if they don't exist.
     *
     * @param count      the number of users to make
     * @param namePrefix the prefix for all of the users to share
     * @throws Exception
     * @step. ^There (?:are|is) (\d+) shared users? with name prefix ([\w\.]+)$$
     */
    @Given("^There (?:are|is) (\\d+) shared users? with name prefix ([\\w\\.]+)$")
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
        UserHasAnAvatarPicture(nameAlias, DEFAULT_USER_AVATAR);
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
     * Rotate device to landscape or portrait
     *
     * @param direction either landscape or portrait
     * @throws Exception
     * @step. ^I rotate UI to (landscape|portrait)$
     */
    @When("^I rotate UI to (landscape|portrait)$")
    public void IRotateUI(String direction) throws Exception {
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
     * Add existed and known contact into Address book
     *
     * @param contactName        expected contact name
     * @param contactPhoneNumber should be phone number without prefix
     * @param prefix             should be +49 or others with same formate
     * @throws Exception
     * @step. ^I add contact with name (.*) and phone (.*) to Address Book$
     */
    @Given("^I add name (.*) and phone (.*) with prefix (.*) to Address Book$")
    public void IAddContactIntoAddressBook(String contactName, String contactPhoneNumber, String prefix) throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(prefix, contactPhoneNumber);
        AndroidCommonUtils.insertContact(contactName, phoneNumber);
    }

    /**
     * Send personal invitation over the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param toMail          the email to send the invitation to
     * @param message         the message for the invitee
     * @throws Exception
     * @step. ^(.*) send personal invitation to mail (.*) with message (.*)$
     */
    @When("^(.*) sends personal invitation to mail (.*) with message (.*)$")
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
     * @param user1 removal action initiator
     * @param user2 user name to be removed from the group chat
     * @param group group chat name
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
     * Press Send button on OnScreen keyboard (the keyboard should be already populated)
     *
     * @throws Exception
     * @step. ^I press Send button at Android keyboard$
     */
    @And("^I press Send button at Android keyboard$")
    public void IPressSendButton() throws Exception {
        pagesCollection.getCommonPage().pressKeyboardSendButton();
    }

    /**
     * Remove all registered OTR clients for the particular user
     *
     * @param userAs user name/alias
     * @throws Exception
     * @step. ^User (.*) removes all his registered OTR clients$
     */
    @Given("^User (.*) removes all his registered OTR clients$")
    public void UserRemovesAllRegisteredOtrClients(String userAs) throws Exception {
        commonSteps.UserRemovesAllRegisteredOtrClients(userAs);
    }

    /**
     * Remove all registered OTR clients for the particular user except of the X most recent ones
     *
     * @param userAs       user name/alias
     * @param clientsCount the count of recents OTR clients to keep
     * @throws Exception
     * @step. ^User (.*) only keeps his (\d+) most recent OTR clients$
     */
    @Given("^User (.*) only keeps his (\\d+) most recent OTR clients$")
    public void UserKeepsXOtrClients(String userAs, int clientsCount) throws Exception {
        commonSteps.UserKeepsXOtrClients(userAs, clientsCount);
    }

    /**
     * Checks to see that an alert message contains the correct text
     *
     * @param expectedMsg the expected error message
     * @param pureText    to specifiy whether it replace the name alias
     * @throws Exception
     * @step. ^I see alert message containing (pure text )?\"(.*)\"$
     */
    @Then("^I see alert message containing (pure text )?\"(.*)\" in the (title|body)$")
    public void ISeeAlertMessage(String pureText, String expectedMsg, String location) throws Exception {
        if (pureText == null) {
            expectedMsg = usrMgr.replaceAliasesOccurences(expectedMsg, ClientUsersManager.FindBy.NAME_ALIAS);
        }
        switch (location.toLowerCase()) {
            case "body":
                Assert.assertTrue(String.format("An alert containing text '%s' in body is not visible", expectedMsg),
                        pagesCollection.getCommonPage().isAlertMessageVisible(expectedMsg));
                break;
            case "title":
                Assert.assertTrue(String.format("An alert containing text '%s' is title not visible", expectedMsg),
                        pagesCollection.getCommonPage().isAlertTitleVisible(expectedMsg));
                break;
            default:
                throw new IllegalArgumentException(String.format("'%s' location is unknown", location));
        }
    }

    /**
     * Execute Delete Conversation action on the particular device registered for this user
     *
     * @param userAs     user name/alias
     * @param convoType  either 'group' or 'single user'
     * @param convoName  conversation name
     * @param deviceName device name (this one should already exist)
     * @throws Exception
     * @step. ^User (.*) deletes? (single user|group) conversation (.*) using device (.*)
     */
    @Given("^User (.*) deletes? (single user|group) conversation (.*) using device (.*)")
    public void UserDeletedConversation(String userAs, String convoType, String convoName, String deviceName)
            throws Exception {
        commonSteps.UserClearsConversation(userAs, convoName, deviceName, convoType.equals("group"));
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
     * Remove file from /mnt/sdcard/Download/
     *
     * @param fileFullName the name of file that located in /mnt/sdcard/Download/
     * @throws Exception
     * @step. ^I remove the file "(.*)" from device's sdcard$
     */
    @Given("^I remove the file \"(.*)\" from device's sdcard$")
    public void IRemoveRemoteFile(String fileFullName) throws Exception {
        AndroidCommonUtils.removeFileFromSdcardDownload(fileFullName);
    }

    /**
     * Send file from SE to user/group
     *
     * @param contact      which could be a user alias
     * @param size         the expected file size such as be 20MB or 30KB
     * @param fileFullName the full file name with extension such as abc.txt,
     *                     which will be created and expected in root/target folder automatically
     * @param mimeType     the MIME type of file sent
     * @param deviceName   such as Device1, this device will be created automatically if it doesn't exist
     * @param convoType    user/group conversation
     * @param dstConvoName destination name could be a user alias or group name
     * @throws Exception
     * @step. ^(.*) sends (.*) file having name "(.*)" and MIME type "(.*)" via device (.*) to (user|group conversation) (.*)$
     */
    @When("^(.*) sends (.*) file having name \"(.*)\" and MIME type \"(.*)\" via device (.*) to (user|group conversation) " +
            " (.*)$")
    public void ContactSendsXFileFromSE(String contact, String size, String fileFullName, String mimeType,
                                        String deviceName, String convoType, String dstConvoName) throws Exception {
        String basePath = AndroidCommonUtils.getBuildPathFromConfig(getClass());
        String sourceFilePath = basePath + File.separator + fileFullName;

        CommonUtils.createRandomAccessFile(sourceFilePath, size);

        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserSentFileToConversation(contact, dstConvoName, sourceFilePath,
                mimeType, deviceName, isGroup);
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
        if (mimeType.toLowerCase().startsWith("image")) {
            basePath = CommonUtils.getImagesPathFromConfig(getClass());
        }

        String sourceFilePath = basePath + File.separator + fileFullName;

        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserSentFileToConversation(contact, dstConvoName, sourceFilePath,
                mimeType, deviceName, isGroup);
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
     * Check the remembered message is changed or not changed
     *
     * @param userNameAlias    user name/alias
     * @param convoType        either 'user' or 'group conversation'
     * @param dstNameAlias     destination user name/alias or group convo name
     * @param deviceName       source device name. Will be created if does not exist yet
     * @param shouldNotChanged equals null means the recent message should changed
     * @throws Exception
     * @step. ^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is( not)?
     * changed( in \\d+ seconds?)?$
     */
    @Then("^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is( not)? changed( in \\d+ seconds?)?$")
    public void UserXFoundLastMessageChanged(String userNameAlias, String convoType, String dstNameAlias,
                                             String deviceName, String shouldNotChanged, String waitDuration)
            throws Exception {
        final int durationSeconds = (waitDuration == null) ? CommonSteps.DEFAULT_WAIT_UNTIL_TIMEOUT_SECONDS
                : Integer.parseInt(waitDuration.replaceAll("[\\D]", ""));

        if (shouldNotChanged == null) {
            commonSteps.UserXFoundLastMessageChanged(userNameAlias, convoType.equals("group conversation"), dstNameAlias,
                    deviceName, durationSeconds);
        } else {
            commonSteps.UserXFoundLastMessageNotChanged(userNameAlias, convoType.equals("group conversation"), dstNameAlias,
                    deviceName, durationSeconds);
        }

    }

    /**
     * Verify the downloaded file are saved correctly
     *
     * @param size           the expected file size. 3 MB for example
     * @param fileFullName   file name with extension
     * @param timeoutSeconds max seconds to wait until the file appears on the device
     * @throws Exception
     * @step. ^I wait up (\d+) seconds? until (.*) file having name "(.*)" is downloaded to the device$
     */
    @Then("^I wait up (\\d+) seconds? until (.*) file having name \"(.*)\" is downloaded to the device$")
    public void TheXFileSavedInDownloadFolder(int timeoutSeconds, String size, String fileFullName)
            throws Exception {
        Optional<FileInfo> fileInfo = CommonUtils.waitUntil(timeoutSeconds,
                CommonSteps.DEFAULT_WAIT_UNTIL_INTERVAL_MILLISECONDS, () -> {
                    AndroidCommonUtils.pullFileFromSdcardDownload(fileFullName);
                    return CommonUtils.retrieveFileInfo(
                            AndroidCommonUtils.getBuildPathFromConfig(CommonAndroidSteps.class)
                                    + File.separator + fileFullName);
                });

        fileInfo.orElseThrow(() -> new IllegalStateException(String.format("File '%s' doesn't exist after %s seconds",
                fileFullName, timeoutSeconds)));

        long expectedSize = CommonUtils.getFileSizeFromString(size);
        long actualSize = fileInfo.get().getFileSize();

        Assert.assertEquals(String.format("File name should be %s", fileFullName),
                fileFullName, fileInfo.get().getFileName());
        Assert.assertTrue(String.format("File size should around %s bytes, but it is %s", expectedSize, actualSize),
                Math.abs(expectedSize - actualSize) < 100);
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
     * Verify whether Android clipboard content equals to the expected one
     *
     * @param expectedMsg the message to verify
     * @throws Exception
     * @step. ^I verify that Android clipboard content equals to "(.*)"$
     */
    @Then("^I verify that Android clipboard content equals to \"(.*)\"$")
    public void IVerifyClipboardContent(String expectedMsg) throws Exception {
        final Optional<String> currentContent = AndroidCommonUtils.getClipboardContent();
        Assert.assertEquals("The expected and the current clipboard contents are different",
                expectedMsg, currentContent.orElse(""));
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
     * Tap Accept/Deny button on Marshmallow's security alert
     *
     * @param action            either accept or dismiss
     * @param throwIfNotVisible equals to null if an exception is expected in case the security alert is not visible
     * @throws Exception
     * @step. ^I (accept|dismiss) security alert( if it is visible)?$
     */
    @When("^I (accept|dismiss) security alert( if it is visible)?$")
    public void IDoAlertAction(String action, String throwIfNotVisible) throws Exception {
        final SecurityAlertPage dstPage = pagesCollection.getPage(SecurityAlertPage.class);
        switch (action.toLowerCase()) {
            case "accept":
                if (throwIfNotVisible == null) {
                    dstPage.accept();
                } else {
                    dstPage.acceptIfVisible();
                }
                break;
            case "dismiss":
                if (throwIfNotVisible == null) {
                    dstPage.dismiss();
                } else {
                    dstPage.dismissIfVisible();
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action %s", action));
        }
    }

    /**
     * Verify whether current OS version is equal or better of the expected on
     *
     * @param minVersion the minimum version string
     * @step. ^I am on Android ([\d\.]+) or better$
     */
    @Given("^I am on Android ([\\d\\.]+) or better$")
    public void IAmOnAndroidXOrHigher(String minVersion) throws Exception {
        final DefaultArtifactVersion currentOsVersion = pagesCollection.getCommonPage().getOSVersion();
        if (currentOsVersion.compareTo(new DefaultArtifactVersion(minVersion)) < 0) {
            throw new PendingException(String.format("The current Android OS version %s is too low to run this test." +
                    "%s version is expected.", currentOsVersion, minVersion));
        }
    }

    /**
     * Verify the Wire build already enabled debug mode.
     * http://stackoverflow.com/questions/2409923/what-do-i-have-to-add-to-the-manifest-to-debug-an-android-application-on-an-actu
     * Check for support debugging by device because of bug: https://code.google.com/p/android/issues/detail?id=58373
     *
     * @throws Exception
     * @step. ^(?:Wire|Device) debug mode is (enabled|supported)$
     */
    @Given("^(?:Wire|Device) debug mode is (enabled|supported)$")
    public void WireEnableDebugMode(String checkMode) throws Exception {
        if (!AndroidCommonUtils.isWireDebugModeEnabled(checkMode.equals("supported"))) {
            throw new PendingException(String.format("Debug mode is not '%s'. Rerun on other device or check build.", checkMode));
        }
    }

    /**
     * Verify whether Android with Google Location Service, if not installed, it will throw a Pending Exception
     *
     * @throws Exception
     * @step. ^I am on Android with Google Location Service$
     */
    @Given("^I am on Android with Google Location Service$")
    public void IAmOnAndroidWithGoogleService() throws Exception {
        if (!AndroidCommonUtils.verifyGoogleLocationServiceInstalled()) {
            throw new PendingException("The current Android doesn't install Google Location Service");
        }
    }

    /**
     * Verify whether Android with GCM Service, if not supported, it will throw a Pending Exception
     *
     * @throws Exception
     * @step. ^I am on Android with GCM Service$
     */
    @Given("^I am on Android with GCM Service$")
    public void IAmOnAndroidWithGCM() throws Exception {
        if (!AndroidCommonUtils.verifyGoogleGCMServiceInstalled()) {
            throw new PendingException("The current Android doesn't support GCM service");
        }
    }

    /**
     * Verify whether no internet bar is visible
     *
     * @param shouldNotSee   equals null means the "no internet bar" should be visible
     * @param timeoutSeconds the custom timeout for verification
     * @throws Exception
     * @step. ^I (do not )?see No Internet bar in (\d+) seconds?$
     */
    @Then("^I (do not )?see No Internet bar in (\\d+) seconds?$")
    public void ISeeNoInternetBar(String shouldNotSee, int timeoutSeconds) throws Exception {
        if (shouldNotSee == null) {
            pagesCollection.getCommonPage().waitUntilNoInternetBarVisible(timeoutSeconds);
        } else {
            pagesCollection.getCommonPage().waitUntilNoInternetBarInvisible(timeoutSeconds);
        }
    }

    /**
     * Create and add new users to the test
     *
     * @param count count of new users to add. User indexing will be continued
     * @throws Exception
     * @step. ^There (?:is|are) (\d+) additional users?$
     */
    @Given("^There (?:is|are) (\\d+) additional users?$")
    public void ThereAreXAdditionalUsers(int count) throws Exception {
        commonSteps.ThereAreXAdditionalUsers(CURRENT_PLATFORM, count);
    }

    private static final int PUSH_NOTIFICATION_TIMEOUT_SEC = 15;

    /**
     * Verify whether the particular string is present in Wire push messages
     *
     * @param expectedMessage the expected push message
     * @param shouldNotSee    equals to null if the message should be visible
     * @throws Exception
     * @step. ^I see the message "(.*)" in push notifications list$
     */
    @Then("^I (do not )?see the message \"(.*)\" in push notifications list$")
    public void ISeePushMessage(String shouldNotSee, String expectedMessage) throws Exception {
        boolean isMsgFound = false;
        final Pattern pattern = Pattern.compile("\\b" + Pattern.quote(expectedMessage) + "\\b");
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            final String output = AndroidCommonUtils.getWirePushNotifications();
            log.debug(output);
            if (pattern.matcher(output).find()) {
                isMsgFound = true;
                break;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= PUSH_NOTIFICATION_TIMEOUT_SEC * 1000);
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("Push message '%s' has not been received within %s seconds timeout OR "
                            + "TestingGallery app has no access to read push notifications (please check phone settings)",
                    expectedMessage, PUSH_NOTIFICATION_TIMEOUT_SEC), isMsgFound);
        } else {
            Assert.assertFalse(String.format("Push message '%s' has been received, although it is not expected",
                    expectedMessage), isMsgFound);
        }
    }

    /**
     * Clear all push notifications.
     *
     * @throws Exception
     * @step. ^I clear Wire push notifications$
     */
    @When("^I clear Wire push notifications$")
    public void IClearNotification() throws Exception {
        AndroidCommonUtils.clearWirePushNotifications();
    }

    /**
     * Tap chathead notification as soon as it appears on the screen
     *
     * @throws Exception
     * @step. ^I tap the chathead$
     */
    @And("^I tap the chathead notification$")
    public void ITapChathead() throws Exception {
        pagesCollection.getCommonPage().tapChatheadNotification();
    }

    /**
     * Verify whether chathead notification is visible
     *
     * @param shouldNotSee equals to null if the notification should be visible
     * @throws Exception
     * @step. ^I (do not )?see chathead notification$
     */
    @Then("^I (do not )?see chathead notification$")
    public void ISeeChatheadNotification(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Chathead notification is not visible",
                    pagesCollection.getCommonPage().waitForChatheadNotification().isPresent());
        } else {
            Assert.assertTrue("Chathead notification is still visible",
                    pagesCollection.getCommonPage().waitUntilChatheadNotificationInvisible());
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

    private static final long LOG_SEARCH_TIMEOUT_MILLIS = 5000;

    /**
     * Verify whether the particular string is present in the logcat output
     *
     * @param logType          one of possible log types. See AndroidLogListener.ListenerType enumeration for more details
     * @param expectedTimesStr the times of appearance
     * @param expectedString   the string to verify
     * @throws Exception
     * @step. ^I verify that (PERF|ANALYTICS|DEFAULT) log contains string "(.*)"( \d+ times?)?$
     */
    @Then("^I verify that (PERF|ANALYTICS|DEFAULT) log contains string \"(.*)\"( \\d+ times?)?$")
    public void IVerifyLogContains(String logType, String expectedString, String expectedTimesStr) throws Exception {
        int result;
        int expectedTimes = (expectedTimesStr == null) ? 1 : Integer.parseInt(expectedTimesStr.replaceAll("[\\D]", ""));

        final long msStarted = System.currentTimeMillis();
        do {
            result = pagesCollection.getCommonPage().countOfLogContain(ListenerType.valueOf(logType), expectedString);
            if (result >= expectedTimes) {
                break;
            } else {
                Thread.sleep(100);
            }
        } while (System.currentTimeMillis() - msStarted <= LOG_SEARCH_TIMEOUT_MILLIS);

        Assert.assertTrue(
                String.format("The %s log does not contain '%s' substring %d times, (actural %d times)",
                        logType, expectedString, expectedTimes, result), result >= expectedTimes);
    }

    /**
     * Rest exist user password
     *
     * @param userNmaeAlias name alias of the contact
     * @throws Exception
     * @step. ^User (.*) resets password to default$
     */
    @When("^User (.*) resets password to \"(.*)\"$")
    public void UserXRestesPassword(String userNmaeAlias, String newPassword) throws Exception {
        newPassword = usrMgr.replaceAliasesOccurences(newPassword, ClientUsersManager.FindBy.PASSWORD_ALIAS);
        commonSteps.UserResetsPassword(userNmaeAlias, newPassword);
    }

    /**
     * Add email(s) into address book of a user and upload address book in backend
     *
     * @param asUser   name of the user where the address book is uploaded
     * @param contacts list of email addresses seperated by comma
     * @throws Exception
     * @step. ^User (.*) has (?: emails?|phone numbers?) (.*) in address book$
     */
    @Given("^User (.*) has (?:emails?|phone numbers?) (.*) in address book$")
    public void UserXHasEmailsInAddressBook(String asUser, String contacts)
            throws Exception {
        commonSteps.UserXHasContactsInAddressBook(asUser, contacts);
    }

    private String recentMsgId = null;

    /**
     * Store the id of the recent message into the local variable
     *
     * @param fromContact user name/alias
     * @throws Exception
     * @step. ^I remember the state of the recent message from user (.*) in the local database$
     */
    @When("^I remember the state of the recent message from user (.*) in the local database$")
    public void IRememberMessageStateInLocalDatabase(String fromContact) throws Exception {
        final ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(fromContact);
        final ClientUser myself = usrMgr.getSelfUserOrThrowError();
        final WireDatabase db = new WireDatabase();
        this.recentMsgId = db.getRecentMessageId(myself, dstUser).orElseThrow(
                () -> new IllegalStateException(
                        String.format("No messages from user %s have been found in the local database",
                                dstUser.getName()))
        );
    }

    /**
     * Verify whether the previously saved message has been properly removed from the local DB
     *
     * @throws Exception
     * @step. ^I verify the remembered message has been deleted from the local database$
     */
    @Then("^I verify the remembered message has been deleted from the local database$")
    public void IVerifyMessageStateInLocalDB() throws Exception {
        if (this.recentMsgId == null) {
            throw new IllegalStateException("Please remember the message state first");
        }
        final WireDatabase db = new WireDatabase();
        Assert.assertTrue(String.format("The previously remembered message [%s] appears to be improperly deleted " +
                "from the local database", this.recentMsgId), db.isMessageDeleted(this.recentMsgId));
    }

    /**
     * Unregister GCM Token, the step should be called after You already login device.
     *
     * @throws Exception
     * @step. I unregister GCM push token in (\d+) seconds$
     */
    @When("^I unregister GCM push token in (\\d+) seconds$")
    public void IUnresgisterGCMToekn(int timeoutSeconds) throws Exception {
        Optional<String> pushToken = CommonUtils.waitUntil(
                timeoutSeconds,
                CommonSteps.DEFAULT_WAIT_UNTIL_INTERVAL_MILLISECONDS,
                () -> {
                    String GCMTokenOutput = AndroidLogListener.getInstance(ListenerType.GCMToken).getStdOut();
                    final Pattern p = Pattern.compile(GCM_TOKEN_PATTERN, Pattern.MULTILINE);
                    final Matcher m = p.matcher(GCMTokenOutput);
                    if (m.find()) {
                        return m.group(1);
                    } else {
                        throw new IllegalStateException(String.format("Cannot find GCM Token from Logcat: %s", GCMTokenOutput));
                    }
                }
        );
        // Wait for 10 seconds until SE register TOKEN on BE
        // Because we still need to wait several seconds after it retrieve the GCM InstanceID from device.
        Thread.sleep(10000);
        commonSteps.UnregisterPushToken(pushToken.orElseThrow(() -> new IllegalStateException("Cannot find GCM Token from " +
                "logcat")));
    }

    /**
     * Try to remove all other Wire packages
     *
     * @throws Exception
     * @step. ^I uninstall all other version of Wire apps$
     */
    @When("^I uninstall all other version of Wire apps$")
    public void IUninstallAllOtherWires() throws Exception {
        String currentPackage = CommonUtils.getAndroidPackageFromConfig(getClass());
        for (String packageName : wirePackageList) {
            if (!packageName.equals(currentPackage)) {
                AndroidCommonUtils.uninstallPackage(packageName);
            }
        }
    }

    /**
     * Mute/Archive conversation from SE
     * Please note, there are also archieve/unarchive/mute/unmute functions by BE directly
     *
     * @param userToNameAlias  user who want to mute conversation
     * @param dstUserNameAlias conversation or user to be muted/archived
     * @throws Exception
     * @step. ^(.*) (mutes?|unmutes?|archives?|unarchives?)  conversation with (.*)$
     */
    @When("^(.*) (mutes?|unmutes?|archives?|unarchives?) conversation with (user|group) (.*) on device (.*)$")
    public void MuteConversationWithUser(String userToNameAlias, String action, String convType,
                                         String dstUserNameAlias, String deviceName) throws Exception {
        boolean isGroup = convType.equals("group");
        switch (action.toLowerCase()) {
            case "mutes":
            case "mute":
                commonSteps.UserMutesConversation(userToNameAlias, dstUserNameAlias, deviceName, isGroup);
                break;
            case "unmutes":
            case "unmute":
                commonSteps.UserUnmutesConversation(userToNameAlias, dstUserNameAlias, deviceName, isGroup);
                break;
            case "archives":
            case "archive":
                commonSteps.UserArchiveConversation(userToNameAlias, dstUserNameAlias, deviceName, isGroup);
                break;
            case "unarchives":
            case "unarchive":
                commonSteps.UserUnarchiveConversation(userToNameAlias, dstUserNameAlias, deviceName, isGroup);
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify action '%s'", action));
        }
    }

    /**
     * Verify the virtual keyboard is visible in the screen
     *
     * @throws Exception
     * @step. ^I see Android keyboard$
     */
    @Then("^I see Android keyboard$")
    public void ISeeKeyboard() throws Exception {
        Assert.assertTrue("The system keyboard is expected to be visible", AndroidCommonUtils.isKeyboardVisible());
    }

    /**
     * Switch the corresponding conversation to ephemeral mode
     *
     * @param userAs      user name/alias
     * @param isGroup     whether is 1:1 or group conversation
     * @param convoName   conversation name
     * @param timeout     ephemeral messages timeout
     * @param timeMetrics either seconds or minutes
     * @throws Exception
     * @step. ^User (.*) switches (user|group conversation) (.*) to ephemeral mode with (\d+) (seconds?|minutes?) timeout$"
     */
    @When("^User (.*) switches (user|group conversation) (.*) to ephemeral mode (?:via device (.*)\\s)?with " +
            "(\\d+) (seconds?|minutes?) timeout$")
    public void UserSwitchesToEphemeralMode(String userAs, String isGroup, String convoName, String deviceName, int timeout,
                                            String timeMetrics) throws Exception {
        final long timeoutMs = timeMetrics.startsWith("minute") ? timeout * 60 * 1000 : timeout * 1000;
        commonSteps.UserSwitchesToEphemeralMode(userAs, convoName, timeoutMs, isGroup.equals("group conversation"),
                deviceName);
    }
}
